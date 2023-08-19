package org.lenuscreations.lelib;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import org.lenuscreations.lelib.adapters.ConfigurationAdapter;
import org.lenuscreations.lelib.adapters.ConfigurationListAdapter;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.FileHandler;
import org.lenuscreations.lelib.file.value.impl.StringValue;

import java.util.ArrayList;
import java.util.List;

public class LeLib {

    public static Gson GSON = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .create();
    public static Gson GSON_PPG = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Configuration.class, new ConfigurationAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<Configuration>>() {}.getType(), new ConfigurationListAdapter())
            .create();

    public static void main(String[] args) {
        //FileHandler fileHandler = new FileHandler("test.json");
        FileHandler fileHandler = new FileHandler("test.yml", LeLib.class);
        fileHandler.set(Configuration.builder().name("acbjdhaf").value(new StringValue("hi")).build());
        fileHandler.save(LeLib.class);

        System.out.println(fileHandler.getConfig("password").getValue());
    }

}
