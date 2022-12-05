package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class InitController_Factory implements Factory<InitController> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public InitController mo1933get() {
        return newInstance();
    }

    public static InitController_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static InitController newInstance() {
        return new InitController();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final InitController_Factory INSTANCE = new InitController_Factory();
    }
}
