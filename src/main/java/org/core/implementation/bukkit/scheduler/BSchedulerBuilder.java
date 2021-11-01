package org.core.implementation.bukkit.scheduler;

import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;

import java.util.Optional;

public class BSchedulerBuilder implements SchedulerBuilder {

    protected Integer delay;
    protected TimeUnit delayUnit;
    protected Integer iteration;
    protected TimeUnit iterationUnit;
    protected Runnable executor;
    protected Scheduler runAfter;
    protected String displayName;
    protected boolean async;

    @Override
    public Optional<Integer> getDelay() {
        return Optional.ofNullable(this.delay);
    }

    @Override
    public SchedulerBuilder setDelay(Integer value) {
        this.delay = value;
        return this;
    }

    @Override
    public Optional<TimeUnit> getDelayUnit() {
        return Optional.ofNullable(this.delayUnit);
    }

    @Override
    public SchedulerBuilder setDelayUnit(TimeUnit unit) {
        this.delayUnit = unit;
        return this;
    }

    @Override
    public Optional<Integer> getIteration() {
        return Optional.ofNullable(this.iteration);
    }

    @Override
    public SchedulerBuilder setIteration(Integer value) {
        this.iteration = value;
        return this;
    }

    @Override
    public Optional<TimeUnit> getIterationUnit() {
        return Optional.ofNullable(this.iterationUnit);
    }

    @Override
    public SchedulerBuilder setIterationUnit(TimeUnit unit) {
        this.iterationUnit = unit;
        return this;
    }

    @Override
    public SchedulerBuilder setExecutor(Runnable runnable) {
        this.executor = runnable;
        return this;
    }

    @Override
    public Runnable getExecutor() {
        return this.executor;
    }

    @Override
    public SchedulerBuilder setToRunAfter(Scheduler scheduler) {
        this.runAfter = scheduler;
        return this;
    }

    @Override
    public Optional<Scheduler> getToRunAfter() {
        return Optional.ofNullable(this.runAfter);
    }

    @Override
    public Optional<String> getDisplayName() {
        return Optional.ofNullable(this.displayName);
    }

    @Override
    public SchedulerBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public SchedulerBuilder setAsync(boolean check) {
        this.async = check;
        return this;
    }

    @Override
    public Scheduler build(Plugin plugin) {
        if (this.executor==null) {
            throw new IllegalArgumentException("No Executor in build");
        }
        if (this.delay!=null && this.delayUnit==null) {
            throw new IllegalArgumentException("Invalid delayUnit in build");
        }
        return new BScheduler(this, plugin);
    }


}
