package org.lenuscreations.lelib.rabbitmq;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.rabbitmq.impl.*;
import org.lenuscreations.lelib.rabbitmq.type.MQType;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MQHandler {

    private final ConnectionFactory factory;
    @Getter
    private final List<MQListener> listeners;
    @Getter
    private final List<MQParameter<?>> parameters;

    @Getter
    @Setter
    private MQType packetReceiveMethod;

    private final String queue;
    private final Connection connection;
    private final Channel channel;

    public static float MAX_TIMEOUT = 60000.0F;


    public MQHandler(String host, int port, String username, String password, String queue) {
        this(host, port, username, password, "/", queue, 60000, MQType.GSON);
    }

    public MQHandler(String host, int port, String username, String password, String queue, MQType packetReceiveMethod) {
        this(host, port, username, password, "/", queue, 60000, packetReceiveMethod);
    }

    public MQHandler(String host, int port, String username, String password, String vhost, String queue) {
        this(host, port, username, password, vhost, queue, 60000, MQType.GSON);
    }

    public MQHandler(String host, int port, String username, String password, String vhost, String queue, MQType packetReceiveMethod) {
        this(host, port, username, password, vhost, queue, 60000, packetReceiveMethod);
    }

    public MQHandler(String host, int port, String username, String password, String queue, int timeout) {
        this(host, port, username, password, "/", queue, timeout, MQType.GSON);
    }

    public MQHandler(String host, int port, String username, String password, String queue, int timeout, MQType packetReceiveMethod) {
        this(host, port, username, password, "/", queue, timeout, packetReceiveMethod);
    }

    public MQHandler(String host, int port, String username, String password, String vhost, String queue, int timeout) {
        this(host, port, username, password, vhost, queue, timeout, MQType.GSON);
    }

    @SneakyThrows
    public MQHandler(String host, int port, String username, String password, String vhost, String queue, int timeout, MQType packetReceiveMethod) {
        this.factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setConnectionTimeout(timeout);
        factory.setVirtualHost(vhost);

        this.packetReceiveMethod = packetReceiveMethod;
        this.queue = queue;
        this.listeners = new ArrayList<>();
        this.parameters = new ArrayList<>();

        this.register(
                new StringParameter(),
                new BooleanParameter(),
                new IntParameter(),
                new LongParameter(),
                new FloatParameter(),
                new DoubleParameter()
        );

        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        channel.queueDeclare(queue, false, false, false, null);

        this.start();
    }

    public Status send(String queue, Packet packet) {
        AtomicReference<Status> status = new AtomicReference<>(Status.FAILED);
        if (packet.async()) {
            new Thread(() -> status.set(this.send(queue, packet.getAction(), packet.getMessage()))).start();
        } else status.set(this.send(queue, packet.getAction(), packet.getMessage()));

        return status.get();
    }

    public Status send(String queue, String action, JsonObject object) {
        object.addProperty("action", action);

        try {
            channel.basicPublish("", queue, false, null, object.toString().getBytes());
            return Status.SENT;
        } catch (Exception e) {
            return Status.FAILED;
        }
    }

    public Status send(String queue, String action, Map<String, Object> map) {
        JsonObject object = (JsonObject) new JsonParser().parse(LeLib.GSON.toJson(map));
        return this.send(queue, action, object);
    }

    private final Map<String, JsonObject> responses = new HashMap<>();

    /*
     * Requires Packet#handle method implemented.
     */
    public void get(String queue, Packet packet) {
        JsonObject object = this.get(queue, packet.getAction(), packet.getMessage());
        if (object.get("error") != null && !object.get("error").isJsonNull()) {
            if (packet.async()) new Thread(() -> packet.handleError(object)).start();
            else packet.handleError(object);
            return;
        }

        if (packet.async()) new Thread(() -> packet.handle(object)).start();
        else packet.handle(object);
    }

    public JsonObject get(String queue, String action, JsonObject object) {
        object.addProperty("action", action);
        String correlationId = UUID.randomUUID().toString();

        try {
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .correlationId(correlationId)
                    .replyTo(this.queue)
                    .build();

            channel.basicPublish("", queue, false, properties, object.toString().getBytes());

            float time = 0.0F;
            while (!responses.containsKey(correlationId)) {
                Thread.sleep(10);
                time += 10.0F;

                if (time >= MAX_TIMEOUT) {
                    break;
                }
            }

            JsonObject response = responses.get(correlationId);
            if (response == null) {
                response = new JsonObject();
                response.addProperty("message", "Packet timed out.");
                response.addProperty("status", "failed");
                response.addProperty("error", "timeout");
            }

            responses.remove(correlationId);
            return response;
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("message", "Unable to sent packet. Connection issues?");
            error.addProperty("status", "failed");
            error.addProperty("error", e.getMessage());
            return error;
        }
    }

    @SneakyThrows
    private void start() {
        this.channel.basicConsume(queue, true, (tag, msg) -> {
            try {
                String message = new String(msg.getBody(), StandardCharsets.UTF_8);
                JsonObject object = (JsonObject) new JsonParser().parse(message);
                if (object == null) return;

                String action = object.get("action").getAsString();
                if (action.equals("response")) {
                    responses.put(object.get("correlationId").getAsString(), object);
                    return;
                }
                switch (packetReceiveMethod) {
                    case GSON:
                        for (MQListener listener : listeners) {
                            for (Method method : listener.getClass().getDeclaredMethods()) {
                                if (method.getParameterCount() != 1 && method.getParameterTypes()[0] != JsonObject.class) continue;

                                if (!method.isAnnotationPresent(MQPacket.class)) continue;

                                MQPacket packet = method.getDeclaredAnnotation(MQPacket.class);
                                if (Arrays.asList(packet.value()).contains(action)) {
                                    if (msg.getProperties() != null && msg.getProperties().getReplyTo() != null) {
                                        JsonObject response = (JsonObject) method.invoke(listener, object);
                                        if (response == null) break;

                                        response.addProperty("correlationId", msg.getProperties().getCorrelationId());
                                        response.addProperty("return_from", this.queue);
                                        send(msg.getProperties().getReplyTo(), "response", response);
                                    } else {
                                        method.invoke(listener, object);
                                    }
                                }
                            }
                        }
                        break;
                    case MAP:
                        TypeToken<Map<String, Object>> type = new TypeToken<Map<String, Object>>() {};
                        Map<String, Object> map = LeLib.GSON.fromJson(object, type.getType());
                        if (map == null) break;

                        for (MQListener listener : listeners) {
                            for (Method method : listener.getClass().getDeclaredMethods()) {
                                if (method.getParameterCount() != 1 && method.getParameterTypes()[0] != type.getType()) continue;

                                if (!method.isAnnotationPresent(MQPacket.class)) continue;

                                MQPacket packet = method.getDeclaredAnnotation(MQPacket.class);
                                if (Arrays.asList(packet.value()).contains(action)) {
                                    method.invoke(listener, map);
                                }
                            }
                        }

                        break;
                    case PARAMETERS:
                        for (MQListener listener : listeners) {
                            for (Method method : listener.getClass().getDeclaredMethods()) {
                                if (!method.isAnnotationPresent(MQPacket.class)) continue;

                                List<Object> parameters = new ArrayList<>();
                                object.entrySet().forEach((k) -> {
                                    if (k.getKey().equalsIgnoreCase("action")) return;

                                    for (MQParameter<?> parameter : this.parameters) {
                                        Object obj = parameter.parse(k.getValue());
                                        if (obj != null) {
                                            parameters.add(obj);
                                            break;
                                        }
                                    }
                                });

                                if (method.getParameterCount() != parameters.size()) continue;

                                MQPacket packet = method.getDeclaredAnnotation(MQPacket.class);
                                if (Arrays.asList(packet.value()).contains(action)) {
                                    method.invoke(listener, parameters.toArray());
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, tag -> {});
    }

    public void register(MQListener... listeners) {
        Arrays.asList(listeners).forEach(this::register);
    }

    public void register(MQParameter<?>... parameters) {
        Arrays.asList(parameters).forEach(this::register);
    }

    public void register(MQListener listener) {
        this.listeners.add(listener);
    }

    public void register(MQParameter<?> parameter) {
        this.parameters.add(parameter);
        this.parameters.sort(((o1, o2) -> o2.priority() - o1.priority()));
    }

    @SneakyThrows
    public void close() {
        channel.close();
        connection.close();
    }

}
