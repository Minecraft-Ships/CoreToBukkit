package org.core.implementation.bukkit.scheduler;

import org.core.schedule.ScheduleManager;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedTransferQueue;

public class BScheduleManager implements ScheduleManager {

    private final Collection<Scheduler> schedules = new LinkedTransferQueue<>();

    void register(Scheduler scheduler) {
        this.schedules.add(scheduler);
    }

    void unregister(Scheduler scheduler) {
        this.schedules.remove(scheduler);
    }

    @Override
    public SchedulerBuilder schedule() {
        return new BSchedulerBuilder();
    }

    @Override
    public Collection<Scheduler> getSchedules() {
        return Collections.unmodifiableCollection(this.schedules);
    }
}
