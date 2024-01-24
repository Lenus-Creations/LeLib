package org.lenuscreations.lelib.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.lenuscreations.lelib.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class CooldownUtils {

    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public static void setCooldown(String name, UUID uuid, long time) {
        cooldowns.putIfAbsent(name, new HashMap<>());
        cooldowns.get(name).put(uuid, System.currentTimeMillis() + time);
    }

    public static boolean hasCooldown(String name, UUID uuid) {
        return cooldowns.get(name) != null && cooldowns.get(name).containsKey(uuid) && cooldowns.get(name).get(uuid) > System.currentTimeMillis();
    }

    public static long getCooldown(String name, UUID uuid) {
        if (cooldowns.get(name) == null || !cooldowns.get(name).containsKey(uuid)) return 0L;
        return cooldowns.get(name).getOrDefault(uuid, 0L);
    }

    public static long getRemaining(String name, UUID uuid) {
        if (cooldowns.get(name) == null || !cooldowns.get(name).containsKey(uuid)) return 0L;
        return cooldowns.get(name).getOrDefault(uuid, 0L) - System.currentTimeMillis();
    }

    public static void removeCooldown(String name, UUID uuid) {
        if (cooldowns.get(name) == null || !cooldowns.get(name).containsKey(uuid)) return;
        cooldowns.get(name).remove(uuid);
    }

    public static void clearCooldowns(String name) {
        if (cooldowns.get(name) == null) return;
        cooldowns.get(name).clear();
    }

    public static String getRemainingText(String name, UUID uuid) {
        long remaining = getRemaining(name, uuid);
        return TimeUtil.secondsToFormattedString((int) (remaining / 1000L));
    }

}
