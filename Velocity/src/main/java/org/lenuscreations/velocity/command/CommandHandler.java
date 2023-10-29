package org.lenuscreations.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.command.ParameterType;
import org.lenuscreations.velocity.VAbstractPlugin;
import org.lenuscreations.velocity.command.parameters.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    public static Component NO_PERMISSION_MESSAGE = Component.text("You don't have permission to execute this command.", NamedTextColor.RED);
    public static Component PLAYER_ONLY_MESSAGE = Component.text("This command can only be executed by a player.", NamedTextColor.RED);


    @Getter
    private static final Map<Class<?>, ParameterType<?, CommandSource>> parameterTypes = new HashMap<>();

    public static void registerParameterType(Class<?> clazz, ParameterType<?, CommandSource> parameterType) {
        parameterTypes.put(clazz, parameterType);
    }

    public static void registerCommands(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            registerCommand(clazz);
        }
    }

    @SneakyThrows
    public static void registerCommand(Class<?> clazz) {
        Object inst = clazz.getDeclaredConstructor().newInstance();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command command = method.getDeclaredAnnotation(Command.class);
                registerCommand(method, command, inst);
            }
        }
    }

    private static void registerCommand(Method method, Command command, Object instance) {
        CommandNode node = new CommandNode(method, command, instance);
        VelocityCommand velocityCommand = new VelocityCommand(node);
        VAbstractPlugin.getInstance().getServer().getCommandManager().register(command.name(), velocityCommand, command.aliases());
    }

    static {
        registerParameterType(String.class, new StringParameter());
        registerParameterType(Integer.class, new IntegerParameter());
        registerParameterType(int.class, new IntegerParameter());
        registerParameterType(Double.class, new DoubleParameter());
        registerParameterType(double.class, new DoubleParameter());
        registerParameterType(Float.class, new FloatParameter());
        registerParameterType(float.class, new FloatParameter());
        registerParameterType(Boolean.class, new BooleanParameter());
        registerParameterType(boolean.class, new BooleanParameter());
        registerParameterType(Player.class, new PlayerParameter());
    }

}
