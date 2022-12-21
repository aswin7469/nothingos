package com.nothing.experience.network;

import java.io.Serializable;

public class ResponseBean<T> implements Serializable {
    private int code;
    private T data;
    private String msg;

    public String toString() {
        return "BaseBean{code=" + this.code + ", msg='" + this.msg + '\'' + ", data='" + this.data + '\'' + '}';
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T t) {
        this.data = t;
    }
}
