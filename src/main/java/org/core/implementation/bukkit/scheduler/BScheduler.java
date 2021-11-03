package org.core.implementation.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;

public class BScheduler implements Scheduler {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            BScheduler.this.taskToRun.run();
            Scheduler scheduler = BScheduler.this.runAfter;
            if (scheduler!=null) {
                scheduler.run();
            }
        }
    }

    protected final Runnable taskToRun;
    protected Scheduler runAfter;
    protected final int delayCount;
    protected final TimeUnit delayTimeUnit;
    protected final Integer iteration;
    protected final TimeUnit iterationTimeUnit;
    protected final String displayName;
    protected final boolean async;
    protected final Plugin plugin;

    protected int task;

    public BScheduler(SchedulerBuilder builder, Plugin plugin) {
        this.taskToRun = builder.getExecutor();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.plugin = plugin;
        this.displayName = builder.getDisplayName().orElse(null);
        this.async = builder.isAsync();
        if (this.displayName==null) {
            throw new IllegalStateException("No DisplayName");
        }
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    @Override
    public void run() {
        long ticks = (long) this.delayTimeUnit.toTicks(this.delayCount);
        Integer iter = null;
        if (this.iteration!=null) {
            iter = (int) this.iterationTimeUnit.toTicks(this.iteration);
        }
        if (iter==null) {
            if (this.async) {
                this.task = Bukkit.getScheduler().scheduleAsyncDelayedTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), new BScheduler.RunAfterScheduler(), ticks);
            } else {
                this.task = Bukkit.getScheduler().scheduleSyncDelayedTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), new BScheduler.RunAfterScheduler(), ticks);
            }
        } else {
            if (this.async) {
                this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), new BScheduler.RunAfterScheduler(), ticks, iter);
            } else {
                this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(), new BScheduler.RunAfterScheduler(), ticks, iter);
            }
        }
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.task);
    }

    @Override
    public Runnable getExecutor() {
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
