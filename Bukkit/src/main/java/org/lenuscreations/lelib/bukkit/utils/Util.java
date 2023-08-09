package org.lenuscreations.lelib.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Util {

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf('.') + 1);

        return v;
    }

    public static int getServerVersion() {
        Pattern pattern = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");
        String version = Bukkit.getBukkitVersion();

        Matcher matcher = pattern.matcher(version);

        if (matcher.find()) {
            String numericVersion = matcher.group().replaceAll("\\.", "");
            return Integer.parseInt(numericVersion);
        }

        return -1;
    }

}
