package org.lenuscreations.lelib.bukkit.tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.disguise.DisguiseHandler;
import org.lenuscreations.lelib.bukkit.tag.event.TagChangeEvent;
import org.lenuscreations.lelib.bukkit.tag.event.TagClearEvent;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagHandler {

    private final Map<UUID, TagData> tagDataMap = new HashMap<>();

    public void setTag(@NotNull TagData tagData) {
        /*JsonObject object = new JsonObject();
        object.addProperty("prefix", tagData.getPrefix());
        object.addProperty("suffix", tagData.getSuffix());
        object.addProperty("colour", tagData.getColour());

        switch (Util.getNMSVersion()) {
            case "1_8_R3":
                V1_8_R3.getTagHandler().setTag(tagData.getPlayer(), object);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for this server version.");
        }*/


        Player player = tagData.getPlayer();

        String prefix = tagData.getPrefix();
        String suffix = tagData.getSuffix();
        String colour = tagData.getColour();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard == null) scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        if (scoreboard == null) throw new RuntimeException("Uncaught error.");

        DisguiseHandler disguiseHandler = AbstractPlugin.getInstance().getDisguiseHandler();
        String name = player.getName();
        TagData previous = tagDataMap.get(player.getUniqueId());
        if (disguiseHandler.isDisguised(player.getUniqueId())) {
            name = disguiseHandler.getDisguise(player).getActualName();
        }

        if (previous != null) {
            if (prefix == null) prefix = previous.getPrefix();
            if (suffix == null) suffix = previous.getSuffix();
            if (colour == null) colour = previous.getColour();
        }

        this.clear(player);

        if (prefix != null && prefix.equals("null")) prefix = null;
        if (suffix != null && suffix.equals("null")) suffix = null;
        if (colour != null && colour.equals("null")) colour = null;

        Team team = scoreboard.getTeam(name);
        if (team == null) team = scoreboard.registerNewTeam(name);

        if (prefix != null) team.setPrefix(Util.format(prefix));
        else team.setPrefix("");

        if (suffix != null) team.setSuffix(Util.format(suffix));
        else team.setSuffix("");

        if (colour != null) team.setPrefix(Util.format((team.getPrefix() == null ? "" : team.getPrefix()) + "&r" + colour));
        else team.setPrefix((team.getPrefix() == null ? "" : Util.format(team.getPrefix())));

        team.addEntry(name);

        tagDataMap.put(player.getUniqueId(), tagData);

        Bukkit.getServer().getPluginManager().callEvent(new TagChangeEvent(tagData, previous));
    }

    public void setTag(@NotNull Player player, @Nullable String prefix, @Nullable String suffix, @Nullable String colour) {
        this.setTag(new TagData(player, prefix, suffix, colour));
    }

    public void setPrefix(Player player, String prefix) {
        TagData tagData = tagDataMap.get(player.getUniqueId());
        if (tagData == null) tagData = new TagData(player, prefix, null, null);

        tagData.setPrefix(prefix);

        setTag(tagData);
    }

    public void setSuffix(Player player, String suffix) {
        TagData tagData = tagDataMap.get(player.getUniqueId());
        if (tagData == null) tagData = new TagData(player, null, suffix, null);

        tagData.setSuffix(suffix);

        setTag(tagData);
    }

    public void setColour(Player player, String colour) {
        TagData tagData = tagDataMap.get(player.getUniqueId());
        if (tagData == null) tagData = new TagData(player, null, null, colour);

        tagData.setColour(colour);

        setTag(tagData);
    }

    public void clear(Player player) {
        TagData tagData = tagDataMap.get(player.getUniqueId());
        tagDataMap.remove(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard == null) scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        if (scoreboard == null) throw new RuntimeException("Uncaught error.");

        DisguiseHandler disguiseHandler = AbstractPlugin.getInstance().getDisguiseHandler();
        String name = player.getName();
        if (disguiseHandler.isDisguised(player.getUniqueId())) name = disguiseHandler.getDisguise(player).getActualName();

        Team team = scoreboard.getTeam(name);
        if (team == null) return;

        team.removeEntry(name);
        team.unregister();

        Bukkit.getServer().getPluginManager().callEvent(new TagClearEvent(tagData));
    }
}
