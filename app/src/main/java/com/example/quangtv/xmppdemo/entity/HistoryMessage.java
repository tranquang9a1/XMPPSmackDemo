package com.example.quangtv.xmppdemo.entity;

/**
 * Created by QuangTV on 11/5/15.
 */
public class HistoryMessage implements Comparable<HistoryMessage>{
    private String name;
    private String message;
    private long time;

    public HistoryMessage(String name, String message, long time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public HistoryMessage() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(HistoryMessage historyMessage) {
        return (int) (historyMessage.getTime() - getTime());
    }
}
