package org.core.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.source.command.ConsoleSource;
import org.core.source.viewer.CommandViewer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlatformConsole implements ConsoleSource {

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        String legacy = message.toLegacy();
        Bukkit.getConsoleSender().sendMessage(uuid, legacy);
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        try {
            Class<?> clazz = Class.forName(AText.COMPONENT_CLASS_PATH);
            Method method = Bukkit.getConsoleSender().getClass().getMethod("sendMessage", clazz);
            method.invoke(Bukkit.getConsoleSender(), ((AdventureText) message).getComponent());
        } catch (ClassNotFoundException | NoSuchMethodException | ClassCastException | IllegalAccessException |
                 InvocationTargetException e) {
            String legacy = message.toLegacy();
            Bukkit.getConsoleSender().sendMessage(legacy);
        }

        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), wholeCommand);
    }
}
