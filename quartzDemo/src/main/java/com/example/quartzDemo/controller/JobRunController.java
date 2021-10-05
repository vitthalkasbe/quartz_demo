package com.example.quartzDemo.controller;

import com.example.quartzDemo.info.TimerInfo;
import com.example.quartzDemo.runner.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timer")
public class JobRunController {
    private RunnerService runnerService;

    @Autowired
    public JobRunController(RunnerService runnerService)
    {
        this.runnerService=runnerService;
    }

    @PostMapping("/runHelloWorld")
    public void runHelloWorld()
    {
        runnerService.runHelloWorldJob();
    }

    @GetMapping("/getAllRunningJobs")
    public List<TimerInfo> getAllRunningJobs()
    {
        return runnerService.getAllRunningJobs();
    }

    @GetMapping("/fingRunningJobById/{timerId}")
    public TimerInfo getRunningJobById(@PathVariable String timerId )
    {
        return runnerService.getRunningJobById(timerId);
    }

    @GetMapping("/deleteJobById/{timerId}")
    public Boolean deleteJobById(@PathVariable String timerId )
    {
        return   runnerService.deleteTimer(timerId);
    }
}
