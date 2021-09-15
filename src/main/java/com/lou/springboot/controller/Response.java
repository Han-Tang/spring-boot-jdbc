package com.lou.springboot.controller;

import java.io.Serializable;

public class Response implements Serializable {

    private Head responseHead;
    private ResponseBody responseBody;
    public Response() {}

    public Head getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(Head responseHead) {
        this.responseHead = responseHead;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}
