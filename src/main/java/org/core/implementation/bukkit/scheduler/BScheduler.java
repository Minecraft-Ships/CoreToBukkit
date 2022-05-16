package org.core.implementation.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BScheduler implements Scheduler {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            try {
                BScheduler.this.taskToRun.accept(BScheduler.this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Scheduler scheduler = BScheduler.this.runAfter;
            if (scheduler!=null) {
                if (scheduler instanceof BScheduler) {
                    ((BScheduler) scheduler).parent = BScheduler.this.parent;
                }
                scheduler.run();
            }
            Bukkit.getScheduler().cancelTask(BScheduler.this.task);
        }

        @Override
        public String toString() {
            String str =
                    BScheduler.this.displayName + ": Delay(Time: " + BScheduler.this.delayCount + " Unit: " + BScheduler.this.delayTimeUnit +
                            ") Iteration: (Time: " + BScheduler.this.iteration + " Unit: " + BScheduler.this.iterationTimeUnit + ") Plugin: " + BScheduler.this.plugin.getPluginId() + " ID:" + BScheduler.this.task;
            if (BScheduler.this.runAfter==null) {
                return str + " ToRunAfter: None";
            } else if (BScheduler.this.runAfter instanceof BScheduler) {
                return str + " ToRunAfter: " + ((BScheduler) BScheduler.this.runAfter).task;
            }
            return str + " ToRunAfter: Unknown";

        }

    }

    protected final @NotNull Consumer<Scheduler> taskToRun;
    protected @Nullable Scheduler runAfter;
    protected final int delayCount;
    protected final @NotNull TimeUnit delayTimeUnit;
    protected final @Nullable Integer iteration;
    protected final @Nullable TimeUnit iterationTimeUnit;
    protected final @NotNull String displayName;
    protected final boolean async;
    protected final @NotNull Plugin plugin;
    private @Nullable String parent;

    protected int task;

    public BScheduler(@NotNull SchedulerBuilder builder, @NotNull Plugin plugin) {
        this.taskToRun = builder.getRunner();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.plugin = plugin;
        this.displayName = builder.getDisplayName().orElseThrow(() -> new RuntimeException("No Displayname"));
        this.async = builder.isAsync();
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    @Override
    public void run() {
        long ticks = (long) this.delayTimeUnit.toTicks(this.delayCount);
        Integer iter = null;
        if (this.iteration!=null) {
            if (this.iterationTimeUnit==null) {
                throw new RuntimeException("Iteration time was set however the timeunit was not");
            }
            iter = (int) this.iterationTimeUnit.toTicks(this.iteration);
        }
        Runnable runAfterScheduler = new RunAfterScheduler();
        if (iter==null) {
            if (this.async) {
                this.task =
                        Bukkit.getScheduler().scheduleAsyncDelayedTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), runAfterScheduler, ticks);
                return;
            }
            this.task =
                    Bukkit.getScheduler().scheduleSyncDelayedTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), runAfterScheduler, ticks);

            return;
        }
        if (this.async) {
            this.task =
                    Bukkit
                            .getScheduler()
                            .scheduleAsyncRepeatingTask(
                                    (org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(),
                                    runAfterScheduler,
                                    ticks,
                                    iter);
            return;
        }
        this.task =
                Bukkit
                        .getScheduler()
                        .scheduleSyncRepeatingTask(
                                (org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(),
                                runAfterScheduler,
                                ticks,
                                iter);


    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.task);
    }

    @Override
    public Consumer<Scheduler> getRunner() {
        return this.taskToRun;
    }

    @Override
    public String toString() {
        String str = this.displayName + ": Delay(Time: " + this.delayCount + " Unit: " + this.delayTimeUnit + ") Iteration: (Time: " + this.iteration + " Unit: " + this.iterationTimeUnit + ") Plugin: " + this.plugin.getPluginId() + " ID:" + this.task;
        if (this.runAfter==null) {
            return str + " ToRunAfter: None";
        } else if (this.runAfter instanceof BScheduler) {
            return str + " ToRunAfter: " + ((BScheduler) this.runAfter).task;
        }
        return str + " ToRunAfter: Unknown";

    }
}
