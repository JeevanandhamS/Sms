package com.jeeva.sms.utils.timeutils;

public class TimeAgo {

    public long timeDiff;

    public TimeUnit timeUnit;

    public TimeAgo(long timeDiff, TimeUnit timeUnit) {
        this.timeDiff = timeDiff;
        this.timeUnit = timeUnit;
    }
}