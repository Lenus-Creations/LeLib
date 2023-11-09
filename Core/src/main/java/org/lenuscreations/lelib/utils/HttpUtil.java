package org.lenuscreations.lelib.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class HttpUtil {

    @SneakyThrows
    public static JsonObject fetch(String url) {
        return fetch(url, null);
    }

    @SneakyThrows
    public static JsonObject fetch(String url, Map<String, Object> body) {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();

        if (body != null) {
            con.setRequestMethod(body.getOrDefault("method", "GET").toString());
            if (body.containsKey("headers")) {
                Map<String, String> headers = (Map<String, String>) body.get("headers");
                for (String key : headers.keySet()) {
                    con.setRequestProperty(key, headers.get(key));
                    System.out.println(key + ": " + headers.get(key));
                }
            }
        } else con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.connect();

        // get json response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            content.append(inputLine);
        in.close();

        con.disconnect();

        System.out.println(content.toString());

        return (JsonObject) new JsonParser().parse(content.toString());
    }

    @SneakyThrows
    public static JsonObject getPterodactylApi(String url, String token) {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/vnd.pterodactyl.v1+json")
                .addHeader("User-Agent", "Ptero4J/v0.1")
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        JsonObject object = (JsonObject) new JsonParser().parse(response.body().string());

        response.close();
        return object.getAsJsonObject("attributes");
    }

    @SneakyThrows
    public static void postPterodactylApi(String url, String token, JsonObject object) {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + token);

        con.getOutputStream().write(object.toString().getBytes());
        con.disconnect();
    }

    @SneakyThrows
    public static Object post(String url, Object body) {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        con.getOutputStream().write(body.toString().getBytes());

        con.disconnect();
        return null;
    }

}
