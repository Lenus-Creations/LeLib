package org.lenuscreations.lelib.rabbitmq.test;

import com.google.gson.JsonObject;
import org.lenuscreations.lelib.rabbitmq.MQListener;
import org.lenuscreations.lelib.rabbitmq.MQPacket;

public class TestListener implements MQListener {

    @MQPacket("idk")
    public JsonObject test(JsonObject packet) {
        System.out.println("received packet");
        JsonObject object = new JsonObject();
        object.addProperty("test", "hello");

        return object;
    }

}
