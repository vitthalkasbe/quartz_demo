package com.example.quartzDemo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TimerInfo implements Serializable {

    private int totalFireCount;

    private int remainingFireCount;

    private boolean runForever;

    private long repeatIntervalInMillis;

    private long initialOffsetInMillis;

    private String callBackData;

}
