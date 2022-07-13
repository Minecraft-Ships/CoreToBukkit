package org.core.implementation.bukkit.scheduler;

import org.core.TranslateCore;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;

import java.util.Optional;
import java.util.function.Consumer;

public class BThreadScheduler implements Scheduler.Threaded {

    private class BasicRunner implements Runnable {

        @Override
        public void run() {
            BThreadScheduler.this.run.accept(BThreadScheduler.this);
            if (BThreadScheduler.this.runAfter == null) {
                return;
            }
            ((BScheduleManager) TranslateCore.getScheduleManager()).unregister(BThreadScheduler.this);
            BThreadScheduler.this.runAfter.run();
        }
    }

    private final Consumer<Scheduler> run;
    private final Plugin plugin;
    private final String displayName;
    private final Scheduler runAfter;
    private Thread thread;

    public BThreadScheduler(SchedulerBuilder builder, Plugin plugin) {
        this.plugin = plugin;
        this.run = builder.getRunner();
        this.runAfter = builder.getToRunAfter().orElse(null);
        this.displayName = builder.getDisplayName().orElseThrow(() -> new IllegalStateException("Missing display name"));
    }

    @Override
    public Optional<Thread> getRunning() {
        return Optional.ofNullable(this.thread);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void run() {
        if (thread == null) {
            this.thread = new Thread(BasicRunner::new);
            this.thread.start();
            return;
        }
        throw new IllegalStateException("Already started");
    }

    @Override
    @Deprecated(forRemoval = true)
    public void cancel() {

    }

    @Override
    public Consumer<Scheduler> getRunner() {
        return this.run;
    }
}
