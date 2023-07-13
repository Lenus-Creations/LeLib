package org.lenuscreations.lelib.bukkit.command.test;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.gui.GUIHandler;
import org.lenuscreations.lelib.bukkit.gui.example.ExampleGUI;
import org.lenuscreations.lelib.command.Arg;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.command.Flag;
import org.lenuscreations.lelib.command.FlagValue;

public class TestCommands {

    @Command(name = "testing")
    public void test(Player sender) {
        AbstractPlugin.getInstance().getGuiHandler().openGUI(sender, ExampleGUI.class);
    }

    @Command(name = "test")
    public void testt(CommandSender sender, @Arg(name = "value") String value) {
        sender.sendMessage("did this wokr? value: " + value);
    }

    @Command(name = "test wildcards")
    public void wildcardTest(CommandSender sender, @Arg(name = "value", wildcard = true) String value) {
        sender.sendMessage("wildcards: " + value);
    }

    @Command(name = "test multi")
    public void testMulti(CommandSender sender, @Arg(name = "value") String value1, @Arg(name = "value") String value) {
        sender.sendMessage("value 1: " + value1);
        sender.sendMessage("value 2: " + value);
    }

    @Command(name = "test multi2")
    public void testMulti2(CommandSender sender, @Flag("e") boolean e, @Arg(name = "value") String value) {
        sender.sendMessage("flag e: " + e);
        sender.sendMessage("value: " + value);
    }

    @Command(name = "test multi3")
    public void testMulti3(CommandSender sender, @FlagValue(flagName = "e", valueName = "value") String value, @Arg(name = "value") String value2) {
        sender.sendMessage("flag e: " + value);
        sender.sendMessage("value: " + value2);
    }

    @Command(name = "test multi4")
    public void testMulti4(CommandSender sender, @Arg(name = "value") String value1, @Arg(name = "value", defaultValue = "defaultvalue") String value2) {
        sender.sendMessage("value 1: " + value1);
        sender.sendMessage("value 2: " + value2);
    }

    @Command(name = "testt")
    public void testtt(CommandSender sender, @Flag("s") boolean value) {
        sender.sendMessage("flag added? " + value);
    }

    @Command(name = "testtt")
    public void testttt(CommandSender sender, @FlagValue(flagName = "s", valueName = "value") String value) {
        sender.sendMessage("flag value added? " + value);
    }

}
