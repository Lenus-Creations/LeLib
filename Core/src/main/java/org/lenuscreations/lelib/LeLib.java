package org.lenuscreations.lelib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.FileHandler;
import org.lenuscreations.lelib.file.value.impl.StringValue;

public class LeLib {

    public static Gson GSON = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
    public static Gson GSON_PPG = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("test.yml");
        fileHandler.set(Configuration.builder().name("acbjdhaf").value(new StringValue("hi")).build());
        fileHandler.save();
    }

}
