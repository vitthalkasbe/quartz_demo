package com.example.quartzDemo.timerService;

import com.example.quartzDemo.info.TimerInfo;
import com.example.quartzDemo.util.TimerUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScheduleService {

    private final Scheduler scheduler;

    @Autowired
    public ScheduleService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(final Class jobClass, final TimerInfo info)
    {
        final JobDetail jobDetail= TimerUtils.buildJobDetail(jobClass,info);
        final Trigger trigger=TimerUtils.buildTrigger(jobClass,info);
        try {
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
           log.error("Exception occurred during scheduling the job : "+e.getMessage());
        }
    }

    public List<TimerInfo> getAllRunningTimers()
    {
        try
        {
           return scheduler.getJobKeys(GroupMatcher.anyGroup())
                     .stream()
                     .map(jobKey -> {

                             try {
                                 final JobDetail jobDetail=scheduler.getJobDetail(jobKey);
                                 return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                             } catch (SchedulerException e) {
                                 log.error(e.getMessage(),e);
                                 return  null;
                             }
                     })
                       .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        } catch (SchedulerException e) {
           log.error(e.getMessage(),e);
           return Collections.emptyList();
        }
    }

    public TimerInfo getRunningJobById(String timerId) {

        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null)
            {

                log.error("Failed to find timer with id {}", timerId);
                return null;
            }
            return  (TimerInfo) jobDetail.getJobDataMap().get(timerId);
        } catch (SchedulerException e) {
           log.error(e.getMessage(),e);
           return null;
        }

    }

    public void updateTimerCount(final String timerId, final TimerInfo info)
    {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null)
            {

                log.error("Failed to find timer with id {}", timerId);
                return;
            }
             jobDetail.getJobDataMap().put(timerId,info);

            scheduler.addJob(jobDetail,true,true);
        } catch (SchedulerException e) {
            log.error(e.getMessage(),e);
        }

    }

    public Boolean deleteTimer(final String timerInfo)
    {
        try {
           return scheduler.deleteJob(new JobKey(timerInfo));
        } catch (SchedulerException e) {
            log.error(e.getMessage(),e);
            return false;
        }

    }

    @PostConstruct
    public void init()
    {
        try {
            scheduler.start();
            scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
        }
        catch (Exception e)
        {
            log.error("Error occurred while starting the scheduler: "+e.getMessage());
        }
    }





    @PreDestroy
    public void preDestroy()
    {

        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("Error occurred during shutting down the scheduler.. "+e.getMessage());
        }
    }
}
