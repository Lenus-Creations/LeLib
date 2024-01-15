package org.lenuscreations.lelib.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class RedisHandler {

    private final JedisPool jedisPool;
    private JedisPubSub jedisPubSub;

    private final String channel;
    private List<RedisListener> listeners;

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
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }

                                if (response == null) break;
                                try (Jedis jedis = jedisPool.getResource()) {
                                    jedis.publish(replyTo, response.toString());
                                }
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

    public Status sendPacket(Packet packet) {
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

    public JsonObject get(String channel, Packet packet) {
        JsonObject jsonObject = packet.toJson();
        jsonObject.addProperty("action", packet.action());
        jsonObject.addProperty("replyTo", this.channel);

        try (Jedis jedis = jedisPool.getResource()) {
            if (packet.async()) new Thread(() -> jedis.publish(channel, jsonObject.toString())).start();
            else jedis.publish(channel, jsonObject.toString());
        }

        return null;
    }

    private final Map<String, JsonObject> responses = new HashMap<>();

    public void close() {
        this.jedisPubSub.unsubscribe();
        this.jedisPool.close();
    }

}
