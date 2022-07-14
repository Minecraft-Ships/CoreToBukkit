package org.core.implementation.bukkit.scheduler;

import org.core.TranslateCore;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

public class BThreadScheduler implements Scheduler.Threaded {

    private class BasicRunner implements Runnable {

        @Override
        public void run() {
            BThreadScheduler.this.startRunner = LocalTime.now();
            BThreadScheduler.this.run.accept(BThreadScheduler.this);
            if (BThreadScheduler.this.runAfter == null) {
                BThreadScheduler.this.endRunner = LocalTime.now();
                return;
            }
            ((BScheduleManager) TranslateCore.getScheduleManager()).unregister(BThreadScheduler.this);
            BThreadScheduler.this.runAfter.run();
            BThreadScheduler.this.endRunner = LocalTime.now();
        }
    }

    private final Consumer<Scheduler> run;
    private final Plugin plugin;
    private final String displayName;
    private final Scheduler runAfter;
    private LocalTime startSchedule;
    private LocalTime startRunner;
    private LocalTime endRunner;
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
    public Optional<LocalTime> getStartScheduleTime() {
        return Optional.ofNullable(this.startSchedule);
    }

    @Override
    public Optional<LocalTime> getStartRunnerTime() {
        return Optional.ofNullable(this.startRunner);
    }

    @Override
    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(this.endRunner);
    }

    @Override
    public boolean isAsync() {
        return true;
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
            this.startSchedule = LocalTime.now();
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
