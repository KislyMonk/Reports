package ru.pegov.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ReportTT_Request {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;

    public ReportTT_Request() {
    }

    public ReportTT_Request(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "ReportTT_Request{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
