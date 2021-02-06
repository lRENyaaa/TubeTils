package de.tubeof.tubetils.api.cronscheduler;

import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.UUID;

import static org.quartz.CronScheduleBuilder.cronSchedule;

public class CronScheduler {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private String cronSchedulerName;

    private final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private final Scheduler scheduler = schedulerFactory.getScheduler();

    public CronScheduler(String cronSchedulerName) throws SchedulerException {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new CronScheduler with name: " + cronSchedulerName);

        this.cronSchedulerName = cronSchedulerName;
    }

    /**
     *
     * @param name
     * @param group
     * @param cronTrigger
     * @param cronjob
     * @throws SchedulerException
     */
    public void addCronjob(String name, String group, String cronTrigger, Class<? extends Job> cronjob) throws SchedulerException {
        String finalName = name + UUID.randomUUID().toString();

        JobDetail job = JobBuilder.newJob(cronjob).withIdentity(finalName, group).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(finalName, group).withSchedule(cronSchedule(cronTrigger)).forJob(job).build();
        scheduler.scheduleJob(job, trigger);

        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new CronSchedulerJob with name: " + finalName);
    }

    /**
     *
     * @throws SchedulerException
     */
    public void startCronjobs() throws SchedulerException {
        scheduler.start();
    }
}
