package com.android.systemui.statusbar;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaArtworkProcessor_Factory implements Factory<MediaArtworkProcessor> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public MediaArtworkProcessor mo1933get() {
        return newInstance();
    }

    public static MediaArtworkProcessor_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaArtworkProcessor newInstance() {
        return new MediaArtworkProcessor();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final MediaArtworkProcessor_Factory INSTANCE = new MediaArtworkProcessor_Factory();
    }
}
