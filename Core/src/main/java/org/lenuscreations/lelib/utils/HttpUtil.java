package org.lenuscreations.lelib.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class HttpUtil {

    public static Object get(String url) {
        return null;
    }

    @SneakyThrows
    public static Object post(String url, Object body) {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");

        con.getOutputStream().write(body.toString().getBytes());

        con.disconnect();
        return null;
    }

}
