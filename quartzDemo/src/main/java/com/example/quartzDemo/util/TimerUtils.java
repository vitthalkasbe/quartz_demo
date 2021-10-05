package com.example.quartzDemo.util;

import com.example.quartzDemo.info.TimerInfo;
import org.quartz.*;

import java.util.Date;

public final class TimerUtils {

    private TimerUtils()
    {
    }

    public static JobDetail buildJobDetail(final Class jobClass, TimerInfo info)
    {
        final JobDataMap jobDataMap=new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(),info);

        return JobBuilder
                        .newJob(jobClass)
                        .withIdentity(jobClass.getSimpleName())
                        .setJobData(jobDataMap)
                        .build();
    }

    public static Trigger buildTrigger(final Class jobClass,TimerInfo info)
    {
        SimpleScheduleBuilder builder=SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.getRepeatIntervalInMillis());

        if(info.isRunForever())
        {
            builder=builder.repeatForever();
        }
        else
        {
            builder=builder.withRepeatCount(info.getTotalFireCount()-1);
        }

        return  TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobClass.getSimpleName())
                    .withSchedule(builder)
                    .startAt(new Date(System.currentTimeMillis()+info.getInitialOffsetInMillis()))
                    .build();
    }

}
