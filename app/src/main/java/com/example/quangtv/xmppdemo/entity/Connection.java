package com.example.quangtv.xmppdemo.entity;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by QuangTV on 11/6/15.
 */
public class Connection {

    private static XMPPTCPConnection connection;

    public  Connection(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    public static XMPPTCPConnection getConnection() {
        return connection;
    }

    public void setConnection(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    public Connection() {
    }
}
