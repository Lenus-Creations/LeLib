package org.lenuscreations.lelib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import org.lenuscreations.lelib.file.FileHandler;

public class LeLib {

    public static Gson GSON = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
    public static Gson GSON_PPG = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("test.yml");
    }

}
