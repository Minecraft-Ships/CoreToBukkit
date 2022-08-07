package org.core.implementation.bukkit.event;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.format.NamedTextColours;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BEventLaunch {

    protected final Plugin plugin;
    protected final EventListener listener;
    protected final Method method;
    protected long beforeTime;
    protected long afterTime;

    public BEventLaunch(Plugin plugin, EventListener listener, Method method) {
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public long getBeforeTime() {
        return this.beforeTime;
    }

    public long getAfterTime() {
        return this.afterTime;
    }

    public long getTimeTaken() {
        return this.afterTime - this.beforeTime;
    }

    public void run(Event event) {
        this.beforeTime = System.currentTimeMillis();
        try {
            this.method.invoke(this.listener, event);
            this.afterTime = System.currentTimeMillis();
        } catch (IllegalAccessException e) {
            TranslateCore.getConsole().sendMessage(
                    AText.ofPlain("Failed to know what to do: HEvent found on method, " +
                                    "but method is not public on "
                                    + this.listener.getClass().getName()
                                    + "."
                                    + this.method.getName()
                                    + "("
                                    + Arrays.stream(this.method.getParameters()).map(p -> p
                                    .getType()
                                    .getSimpleName()
                                    + " "
                                    + p.getName()).collect(Collectors.joining(", "))
                                    + ")")
                            .withColour(NamedTextColours.RED));
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            TranslateCore
                    .getConsole()
                    .sendMessage(AText
                            .ofPlain(
                                    "Failed to know what to do: EventListener caused exception from "
                                            + this.listener.getClass().getName()
                                            + "."
                                            + this.method.getName()
                                            + "("
                                            + Arrays
                                            .stream(this.method.getParameters())
                                            .map(p -> p.getType().getSimpleName())
                                            .collect(Collectors.joining(", "))
                                            + ")")
                            .withColour(NamedTextColours.RED));
            e.getTargetException().printStackTrace();
        } catch (Throwable e) {
            this.afterTime = System.currentTimeMillis();
            TranslateCore
                    .getConsole()
                    .sendMessage(AText
                            .ofPlain(
                                    "Failed to know what to do: HEvent found on method, but exception found when " +
                                            "running "
                                            + this.listener.getClass().getName()
                                            + "."
                                            + this.method.getName()
                                            + "("
                                            + Arrays
                                            .stream(this.method.getParameters())
                                            .map(p -> p.getType().getSimpleName() + " " + p.getName())
                                            .collect(Collectors.joining(", "))
                                            + ") found in plugin: "
                                            + this.plugin.getPluginName()
                                            + " - Time taken for event to process: "
                                            + TimeUnit.MILLISECONDS.toMicros(this.getTimeTaken()))
                            .withColour(NamedTextColours.RED));
            e.printStackTrace();
        }
    }
}
