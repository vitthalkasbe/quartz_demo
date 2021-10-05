package com.example.quartzDemo.jobs;

import com.example.quartzDemo.info.TimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap=jobExecutionContext.getJobDetail().getJobDataMap();
        TimerInfo info=(TimerInfo) jobDataMap.get(HelloWorldJob.class.getSimpleName());

        log.info(info.getCallBackData());
        log.info("Remaining fire count is {}",info.getRemainingFireCount());


    }
}
