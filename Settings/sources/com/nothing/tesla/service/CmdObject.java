package com.nothing.tesla.service;
/* loaded from: classes2.dex */
public class CmdObject {
    public int cmd;
    public boolean status;
    public String subTitle;
    public String title;
    public int type;

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean z) {
        this.status = z;
    }

    public int getCmd() {
        return this.cmd;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public int getType() {
        return this.type;
    }
}
