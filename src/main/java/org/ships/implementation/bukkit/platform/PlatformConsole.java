package org.ships.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.source.command.ConsoleSource;
import org.core.source.viewer.CommandViewer;
import org.core.text.Text;
import org.core.text.TextColours;
import org.ships.implementation.bukkit.text.BText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlatformConsole implements ConsoleSource {
    @Override
    public CommandViewer sendMessage(Text message, UUID uuid) {
        try {
            Bukkit.getConsoleSender().getClass().getDeclaredMethod("sendMessage", UUID.class, String.class).invoke(Bukkit.getConsoleSender(), uuid, ((BText) message).toBukkitString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            sendMessage(message);
        }

        return this;
    }

    @Override
    public CommandViewer sendMessage(Text message) {
        Bukkit.getConsoleSender().sendMessage(((BText) message).toBukkitString());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        return sendMessage(new BText(TextColours.stripColours(message)));
    }

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        String legacy = message.toLegacy();
        Bukkit.getConsoleSender().sendMessage(uuid, legacy);
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        try {
            Class<?> clazz = Class.forName("net.kyori.adventure.text.Component");
            Method method = Bukkit.getConsoleSender().getClass().getMethod("sendMessage", clazz);
            method.invoke(Bukkit.getConsoleSender(), ((AdventureText) message).getComponent());
        } catch (ClassNotFoundException | NoSuchMethodException | ClassCastException | IllegalAccessException | InvocationTargetException e) {
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
