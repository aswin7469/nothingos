package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class RowInflaterTask_Factory implements Factory<RowInflaterTask> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public RowInflaterTask mo1933get() {
        return newInstance();
    }

    public static RowInflaterTask_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static RowInflaterTask newInstance() {
        return new RowInflaterTask();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final RowInflaterTask_Factory INSTANCE = new RowInflaterTask_Factory();
    }
}
