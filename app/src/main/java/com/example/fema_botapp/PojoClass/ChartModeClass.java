package com.example.fema_botapp.PojoClass;

public class ChartModeClass {
    String message;
    String sender;
    String time;

    public ChartModeClass(String message, String sender, String time) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}