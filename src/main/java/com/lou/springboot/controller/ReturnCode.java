package com.lou.springboot.controller;


public enum ReturnCode {
    SUCCESS(1, "ok"),
    FAIL(-1, "fail"),
    PERMISSION_DENIED(2,"permission denied"),
    REJECTED(-2,"rejected");

    ReturnCode() {}
    ReturnCode(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    String msg;
    int    code;


    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
