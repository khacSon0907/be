package com.example.backendshop.controller;

public class ResponseMessage {
    private String message;
    private Object data;

    public ResponseMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
