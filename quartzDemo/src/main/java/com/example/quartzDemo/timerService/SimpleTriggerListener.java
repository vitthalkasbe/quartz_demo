package com.example.quartzDemo.timerService;

import com.example.quartzDemo.info.TimerInfo;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class SimpleTriggerListener implements TriggerListener {

   private final ScheduleService service;

   public SimpleTriggerListener(ScheduleService service){
    this.service=service;
   }

    @Override
    public String getName() {
        return "simpleListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {

       final String timerId=trigger.getKey().getName();
       JobDataMap jobDataMap=jobExecutionContext.getJobDetail().getJobDataMap();
       TimerInfo info=(TimerInfo) jobDataMap.get(timerId);

       if(!info.isRunForever())
       {
           int remainingFiredCount=info.getRemainingFireCount();
           if(remainingFiredCount==0)
           {
               return;
           }
           info.setRemainingFireCount(remainingFiredCount-1);
       }

       service.updateTimerCount(timerId,info);

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {

    }
}
