package com.android.wifi.p018x.com.android.internal.util;

import android.os.Message;

/* renamed from: com.android.wifi.x.com.android.internal.util.State */
public class State implements IState {
    public void enter() {
    }

    public void exit() {
    }

    public boolean processMessage(Message message) {
        return false;
    }

    protected State() {
    }

    public String getName() {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf(36) + 1);
    }
}
