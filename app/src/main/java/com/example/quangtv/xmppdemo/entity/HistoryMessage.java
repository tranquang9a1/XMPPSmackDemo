package com.example.quangtv.xmppdemo.entity;

/**
 * Created by QuangTV on 11/5/15.
 */
public class HistoryMessage implements Comparable<HistoryMessage>{
    private String name;
    private String message;
    private boolean isUser;
    private String type;
    private long time;

    public HistoryMessage(String name, String message, boolean isUser, String type) {
        this.name = name;
        this.message = message;
        this.type = type;
        this.isUser = isUser;
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

    public boolean isUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
