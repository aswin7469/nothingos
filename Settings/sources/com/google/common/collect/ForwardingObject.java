package com.google.common.collect;
/* loaded from: classes2.dex */
public abstract class ForwardingObject {
    /* renamed from: delegate */
    protected abstract Object mo848delegate();

    public String toString() {
        return mo848delegate().toString();
    }
}
