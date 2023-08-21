package org.lenuscreations.lelib.rabbitmq;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.rabbitmq.impl.*;
import org.lenuscreations.lelib.rabbitmq.type.MQType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    @SneakyThrows
    private void start() {
        this.channel.basicConsume(queue, true, (tag, msg) -> {
            try {
                String message = new String(msg.getBody(), StandardCharsets.UTF_8);
                JsonObject object = (JsonObject) new JsonParser().parse(message);
                if (object == null) return;

                String action = object.get("action").getAsString();
                switch (packetReceiveMethod) {
                    case GSON:
                        for (MQListener listener : listeners) {
                            for (Method method : listener.getClass().getDeclaredMethods()) {
                                if (method.getParameterCount() != 1 && method.getParameterTypes()[0] != JsonObject.class) continue;

                                if (!method.isAnnotationPresent(MQPacket.class)) continue;

                                MQPacket packet = method.getDeclaredAnnotation(MQPacket.class);
                                if (Arrays.asList(packet.value()).contains(action)) {
                                    method.invoke(listener, object);
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
                                object.asMap().forEach((k, v) -> {
                                    if (k.equalsIgnoreCase("action")) return;

                                    for (MQParameter<?> parameter : this.parameters) {
                                        Object obj = parameter.parse(v);
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
