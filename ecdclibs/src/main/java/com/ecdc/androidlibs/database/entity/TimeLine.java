package com.ecdc.androidlibs.database.entity;

public class TimeLine {
    private long startDate;
    private long endDate;

    public TimeLine(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isValidDate()
    {
        return startDate>0 || endDate>0;
    }
}
