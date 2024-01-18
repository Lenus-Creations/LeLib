package org.lenuscreations.lelib.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.lenuscreations.lelib.utils.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RedisHandler {

    private final JedisPool jedisPool;
    private JedisPubSub jedisPubSub;

    private final String channel;
    private List<RedisListener> listeners;

    private static final float MAX_TIMEOUT = 10000.0F;

    public RedisHandler(String uri) {
        this(URI.create(uri), "default");
    }

    public RedisHandler(URI uri) {
        this(uri, "default");
    }

    public RedisHandler(URI uri, String channel) {
        this.jedisPool = new JedisPool(uri);
        this.channel = channel;

        this.start();
    }

    public RedisHandler(String host, int port) {
        this(host, port, null, null, "default");
    }

    public RedisHandler(String host, String channel) {
        this(host, 6379, null, null, channel);
    }

    public RedisHandler(String host, int port, String channel) {
        this(host, port, null, null, channel);
    }

    public RedisHandler(String host, int port, String username, String password, String channel) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            this.jedisPool = new JedisPool(host, port);
        else this.jedisPool = new JedisPool(host, port, username, password);
        this.channel = channel;

        this.start();
    }

    public void registerListener(RedisListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(RedisListener listener) {
        this.listeners.remove(listener);
    }

    private void start() {
        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                for (RedisListener listener : listeners) {
                    for (Method method : listener.getClass().getMethods()) {
                        if (!method.isAnnotationPresent(RedisPacket.class)) continue;

                        RedisPacket redisPacket = method.getAnnotation(RedisPacket.class);
                        JsonObject jsonObject = null;
                        try {
                            jsonObject = (JsonObject) new JsonParser().parse(message);
                        } catch (Exception e) {
                            System.out.println("Invalid JSON: " + message);
                        }

                        if (jsonObject == null) continue;
                        if (!jsonObject.has("action")) continue;

                        String action = jsonObject.get("action").getAsString();
                        if (Arrays.asList(redisPacket.value()).contains(action)) {
                            method.setAccessible(true);
                            if (jsonObject.has("replyTo")) {
                                String replyTo = jsonObject.get("replyTo").getAsString();
                                JsonObject response = null;
                                try {
                                    response = (JsonObject) method.invoke(listener, jsonObject);
                                } catch (IllegalAccessException | InvocationTargetException ignored) {

                                }

                                if (response == null) break;

                                response.addProperty("responseId", jsonObject.get("responseId").getAsString());
                                response.addProperty("replied", true);
                                try (Jedis jedis = jedisPool.getResource()) {
                                    jedis.publish(replyTo, response.toString());
                                }
                            } if (jsonObject.has("replied")) {
                                String responseId = jsonObject.get("responseId").getAsString();
                                responses.put(responseId, jsonObject);
                            } else {
                                try {
                                    method.invoke(listener, jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        };

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(jedisPubSub, channel);
            }
        }).start();
    }

    /**
     * Sends a packet to the default channel
     * @param packet The packet to send
     * @return The status of the packet
     */
    public Status sendPacket(String channel, Packet packet) {
        JsonObject jsonObject = packet.toJson();
        jsonObject.addProperty("action", packet.action());
        try (Jedis jedis = jedisPool.getResource()) {
            if (packet.async()) new Thread(() -> jedis.publish(channel, jsonObject.toString())).start();
            else jedis.publish(channel, jsonObject.toString());
        } catch (Exception e) {
            return Status.FAILED;
        }

        return Status.SUCCESS;
    }

    public Status sendPacket(String channel, String action, JsonObject jsonObject) {
        return sendPacket(channel, action, jsonObject, false);
    }

    /**
     * Sends a packet to a channel
     * @param channel The channel to send the packet to
     * @param action The action of the packet
     * @param jsonObject The packet
     * @param async Whether to send the packet asynchronously
     * @return The status of the packet
     */
    public Status sendPacket(String channel, String action, JsonObject jsonObject, boolean async) {
        jsonObject.addProperty("action", action);
        try (Jedis jedis = jedisPool.getResource()) {
            if (async) new Thread(() -> jedis.publish(channel, jsonObject.toString())).start();
            else jedis.publish(channel, jsonObject.toString());
        } catch (Exception e) {
            return Status.FAILED;
        }

        return Status.SUCCESS;
    }

    /**
     * Sends a packet and waits for a response
     * @param channel The channel to send the packet to
     * @param packet The packet to send
     * @return The response
     */
    @SneakyThrows
    public JsonObject get(String channel, Packet packet) {
        JsonObject jsonObject = packet.toJson();
        jsonObject.addProperty("action", packet.action());
        jsonObject.addProperty("replyTo", this.channel);

        String responseId = StringUtil.randomString(12, true);
        jsonObject.addProperty("responseId", responseId);

        try (Jedis jedis = jedisPool.getResource()) {
            if (packet.async()) new Thread(() -> jedis.publish(channel, jsonObject.toString())).start();
            else jedis.publish(channel, jsonObject.toString());
        }

        float time = 0.0F;
        while (!responses.containsKey(responseId)) {
            Thread.sleep(10);
            time += 10.0F;

            if (time >= MAX_TIMEOUT) {
                break;
            }
        }

        if (responses.containsKey(responseId)) {
            JsonObject response = responses.get(responseId);
            responses.remove(responseId);
            return response;
        }

        throw new TimeoutException("[REDIS] Response timed out for packet with response id of '" + responseId + "'.");
    }

    private final Map<String, JsonObject> responses = new HashMap<>();

    public void close() {
        this.jedisPubSub.unsubscribe();
        this.jedisPool.close();
    }

}
