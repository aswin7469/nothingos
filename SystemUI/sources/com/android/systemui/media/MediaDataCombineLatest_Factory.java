package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaDataCombineLatest_Factory implements Factory<MediaDataCombineLatest> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public MediaDataCombineLatest mo1933get() {
        return newInstance();
    }

    public static MediaDataCombineLatest_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaDataCombineLatest newInstance() {
        return new MediaDataCombineLatest();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final MediaDataCombineLatest_Factory INSTANCE = new MediaDataCombineLatest_Factory();
    }
}
