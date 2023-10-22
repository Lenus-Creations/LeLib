package org.lenuscreations.lelib.jda.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import org.lenuscreations.lelib.utils.HttpUtil;

@UtilityClass
public class WebhookUtil {

    public static void sendWebhook(String url, String content) {
        sendWebhook(url, content, null);
    }

    public static void sendWebhook(String url, String content, String username) {
        sendWebhook(url, content, username, null);
    }

    public static void sendWebhook(String url, String content, String username, String avatarUrl) {
        sendWebhook(url, content, username, avatarUrl, false);
    }

    public static void sendWebhook(String url, String content, String username, String avatarUrl, boolean tts) {
        sendWebhook(url, null, content, username, avatarUrl, tts);
    }

    public static void sendWebhook(String url, JsonArray embeds) {
        sendWebhook(url, embeds, null);
    }

    public static void sendWebhook(String url, JsonArray embeds, String username, String avatarUrl) {
        sendWebhook(url, embeds, username, avatarUrl, false);
    }

    public static void sendWebhook(String url, JsonArray embeds, String username, String avatarUrl, boolean tts) {
        sendWebhook(url, embeds, null, username, avatarUrl, tts);
    }

    public static void sendWebhook(String url, JsonArray embeds, String content) {
        sendWebhook(url, embeds, content, null);
    }

    public static void sendWebhook(String url, JsonArray embeds, String content, String username, String avatarUrl) {
        sendWebhook(url, embeds, content, username, avatarUrl, false);
    }

    public static void sendWebhook(String url, JsonArray embeds, String content, String username, String avatarUrl, boolean tts) {
        JsonObject json = new JsonObject();
        if (embeds == null && content == null) throw new IllegalArgumentException("Embeds and content cannot be null at the same time");

        if (content != null) json.addProperty("content", content);
        if (username != null) json.addProperty("username", username);
        if (avatarUrl != null) json.addProperty("avatar_url", avatarUrl);
        json.addProperty("tts", tts);
        if (embeds != null) json.add("embeds", embeds);

        HttpUtil.post(url, json.toString());
    }

}
