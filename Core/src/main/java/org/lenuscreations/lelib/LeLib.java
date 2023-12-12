package org.lenuscreations.lelib;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.LongSerializationPolicy;
import org.lenuscreations.lelib.adapters.*;
import org.lenuscreations.lelib.arguments.ArgumentHandler;
import org.lenuscreations.lelib.arguments.test.TestArguments;
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
import org.lenuscreations.lelib.utils.reflection.LClass;
import org.lenuscreations.lelib.utils.reflection.LField;
import org.lenuscreations.lelib.utils.reflection.LMethod;
import org.lenuscreations.lelib.utils.reflection.ReflectionUtil;

import java.util.ArrayList;

public class LeLib {

    public static Gson GSON = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
            .registerTypeAdapter(PteroLocation.class, new PteroLocationAdapter())
            .registerTypeAdapter(PteroNode.class, new PteroNodeAdapter())
            .registerTypeAdapter(PteroServer.class, new PteroServerAdapter())
            .create();
    public static Gson GSON_PPG = new GsonBuilder()
            .setPrettyPrinting().
            setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
            .registerTypeAdapter(PteroLocation.class, new PteroLocationAdapter())
            .registerTypeAdapter(PteroNode.class, new PteroNodeAdapter())
            .registerTypeAdapter(PteroServer.class, new PteroServerAdapter())
            .create();

    public static Gson GSON_EMPTY = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .create();

    public static void main(String[] args) {
        LClass clazz = ReflectionUtil.ofClass(Configuration.class);
        System.out.println(clazz);

        LField field = clazz.getField("name");
        System.out.println(field);

        LMethod method = clazz.getMethod("getName");
        System.out.println(method);
    }

}
