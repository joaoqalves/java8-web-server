package net.joaoqalves.model;

import com.google.gson.annotations.Expose;

public class HttpExceptionWrapper {

    @Expose
    private String msg;

    public HttpExceptionWrapper(String msg) {
        this.msg = msg;
    }

    public HttpExceptionWrapper() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
