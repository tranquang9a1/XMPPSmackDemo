package com.example.quangtv.xmppdemo.entity;

/**
 * Created by QuangTV on 11/3/15.
 */
public class MessageInfo {

    private String body;
    private boolean isUser;
    private String type;

    public MessageInfo(String body, boolean isUser, String type) {
        this.body = body;
        this.isUser = isUser;
        this.type = type;
    }

    public MessageInfo() {

    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
