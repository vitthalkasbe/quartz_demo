package com.example.quartzDemo.runner;

import com.example.quartzDemo.info.TimerInfo;
import com.example.quartzDemo.jobs.HelloWorldJob;
import com.example.quartzDemo.timerService.ScheduleService;
import com.example.quartzDemo.util.TimerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunnerService {

    private final ScheduleService scheduler;

    @Autowired
    public RunnerService(ScheduleService scheduler)
    {
        this.scheduler=scheduler;
    }

    public void runHelloWorldJob()
    {
        final TimerInfo info=new TimerInfo();
        info.setTotalFireCount(5);
        info.setRemainingFireCount(info.getTotalFireCount());
        info.setRepeatIntervalInMillis(5000);
        info.setInitialOffsetInMillis(1000);
        info.setCallBackData("Hey Mr. Java welcome to Java!");
        scheduler.schedule(HelloWorldJob.class,info);
    }

    public Boolean deleteTimer(final String timerInfo)
    {
      return scheduler.deleteTimer(timerInfo);
    }

    public List<TimerInfo> getAllRunningJobs()
    {
        return scheduler.getAllRunningTimers();
    }

    public TimerInfo getRunningJobById(String timerId) {
        return scheduler.getRunningJobById(timerId);
    }
}
