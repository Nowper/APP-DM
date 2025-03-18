package com.huanjing.iotapp.bean;

public class MyEnventBusMessage {
    public final String message;

    public static MyEnventBusMessage getInstance(String message) {
        return new MyEnventBusMessage(message);
    }

    private MyEnventBusMessage(String message) {
        this.message = message;
    }
}