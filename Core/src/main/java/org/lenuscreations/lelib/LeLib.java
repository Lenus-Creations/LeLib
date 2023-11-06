package org.lenuscreations.lelib;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.LongSerializationPolicy;
import org.lenuscreations.lelib.adapters.*;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.pterodactyl.ServerStatus;
import org.lenuscreations.lelib.pterodactyl.admin.PteroAdmin;
import org.lenuscreations.lelib.pterodactyl.admin.location.PteroLocation;
import org.lenuscreations.lelib.pterodactyl.admin.node.PteroNode;
import org.lenuscreations.lelib.pterodactyl.admin.server.PteroServer;
import org.lenuscreations.lelib.pterodactyl.client.PteroClient;
import org.lenuscreations.lelib.rabbitmq.MQHandler;
import org.lenuscreations.lelib.rabbitmq.test.TestListener;
import org.lenuscreations.lelib.rabbitmq.type.MQType;

import java.util.ArrayList;

public class LeLib {

    public static Gson GSON = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
            .registerTypeAdapter(PteroLocation.class, new PteroLocationAdapter())
            .registerTypeAdapter(PteroNode.class, new PteroNodeAdapter())
            .registerTypeAdapter(PteroServer.class, new PteroServerAdapter())
            .create();
    public static Gson GSON_PPG = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
            .registerTypeAdapter(PteroLocation.class, new PteroLocationAdapter())
            .registerTypeAdapter(PteroNode.class, new PteroNodeAdapter())
            .registerTypeAdapter(PteroServer.class, new PteroServerAdapter())
            .create();

    public static Gson GSON_EMPTY = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

    public static void main(String[] args) {
        //PteroAdmin admin = new PteroAdmin("https://panel.perlexmc.com", "ptla_opwpM7BklvncNfWTPQ0iyPbNFq344aB02h20LNsRIAZ");
        //System.out.println(admin.getServer("32"));

        MQHandler handler = new MQHandler("cow-01.rmq2.cloudamqp.com", 5672, "rbbtwhpv", "L2Dt4Bhz9a7dG70ukN0eQJU7CxococR9", "rbbtwhpv", "test");
        handler.register(new TestListener());
        System.out.println(handler.get("test", "idk", new JsonObject()).toString());
    }

}
