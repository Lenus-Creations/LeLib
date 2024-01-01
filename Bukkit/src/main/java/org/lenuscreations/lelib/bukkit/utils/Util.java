package org.lenuscreations.lelib.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Util {

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, format(messages.get(i)));
        }

        return messages;
    }

    public static String[] format(String[] messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = format(messages[i]);
        }

        return messages;
    }

    public static String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf('.') + 1);

        return v;
    }

    public static int getServerVersion() {
        String version = Bukkit.getBukkitVersion();

        try {
            return Integer.parseInt(version.split("-")[0].replace(".", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
