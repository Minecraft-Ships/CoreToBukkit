package org.core.implementation.bukkit.scheduler;

import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

public class BInstanceThreadScheduler implements Scheduler.Threaded {

    private @Nullable LocalTime startTime;
    private @Nullable LocalTime endTime;
    private final @NotNull String displayName;
    private final @NotNull Plugin plugin;
    private final @NotNull Consumer<Scheduler> consumer;
    private @Nullable Thread currentThread;

    public BInstanceThreadScheduler(@NotNull SchedulerBuilder builder, @NotNull Plugin plugin) {
        this.consumer = builder.getRunner();
        this.plugin = plugin;
        this.displayName = builder.getDisplayName().orElseThrow(() -> new RuntimeException("No Displayname"));
    }


    @Override
    public Optional<LocalTime> getStartScheduleTime() {
        return Optional.ofNullable(this.startTime);
    }

    @Override
    public Optional<LocalTime> getStartRunnerTime() {
        return Optional.ofNullable(this.startTime);
    }

    @Override
    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(this.endTime);
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
        this.currentThread = new Thread(() -> {
            this.consumer.accept(this);
            this.endTime = LocalTime.now();
        });
        this.startTime = LocalTime.now();
        this.currentThread.start();
    }

    @Override
    public Consumer<Scheduler> getRunner() {
        return this.consumer;
    }

    @Override
    public Optional<Thread> getRunning() {
        return Optional.ofNullable(this.currentThread);
    }
}
