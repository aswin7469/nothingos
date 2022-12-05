package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaHostStatesManager_Factory implements Factory<MediaHostStatesManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public MediaHostStatesManager mo1933get() {
        return newInstance();
    }

    public static MediaHostStatesManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaHostStatesManager newInstance() {
        return new MediaHostStatesManager();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final MediaHostStatesManager_Factory INSTANCE = new MediaHostStatesManager_Factory();
    }
}
