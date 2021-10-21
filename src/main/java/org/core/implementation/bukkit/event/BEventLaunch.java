package org.core.implementation.bukkit.event;

import org.array.utils.ArrayUtils;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class BEventLaunch {

    protected Plugin plugin;
    protected EventListener listener;
    protected Method method;
    protected long beforeTime;
    protected long afterTime;

    public BEventLaunch(Plugin plugin, EventListener listener, Method method){
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public long getBeforeTime(){
        return this.beforeTime;
    }

    public long getAfterTime(){
        return this.afterTime;
    }

    public long getTimeTaken(){
        return this.afterTime - this.beforeTime;
    }

    public void run(Event event){
        this.beforeTime = System.currentTimeMillis();
        try {
            this.method.invoke(this.listener, event);
            this.afterTime = System.currentTimeMillis();
        } catch (IllegalAccessException e) {
            System.err.println("Failed to know what to do: HEvent found on method, but method is not public on " + this.listener.getClass().getName() + "." + this.method.getName() + "("+ ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Failed to know what to do: EventListener caused exception from " + this.listener.getClass().getName() + "." + this.method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.getTargetException().printStackTrace();
        } catch (Throwable e){
            this.afterTime = System.currentTimeMillis();
            System.err.println("Failed to know what to do: HEvent found on method, but exception found when running " + this.listener.getClass().getName() + "." + this.method.getName() + "("+ ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ") found in plugin: " + this.plugin.getPluginName() + " - Time taken for event to process: " + TimeUnit.MILLISECONDS.toMicros(this.getTimeTaken()));
            e.printStackTrace();
        }
    }
}
