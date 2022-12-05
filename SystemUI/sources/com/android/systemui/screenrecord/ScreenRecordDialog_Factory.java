package com.android.systemui.screenrecord;

import com.android.systemui.settings.UserContextProvider;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenRecordDialog_Factory implements Factory<ScreenRecordDialog> {
    private final Provider<RecordingController> controllerProvider;
    private final Provider<UserContextProvider> userContextProvider;

    public ScreenRecordDialog_Factory(Provider<RecordingController> provider, Provider<UserContextProvider> provider2) {
        this.controllerProvider = provider;
        this.userContextProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ScreenRecordDialog mo1933get() {
        return newInstance(this.controllerProvider.mo1933get(), this.userContextProvider.mo1933get());
    }

    public static ScreenRecordDialog_Factory create(Provider<RecordingController> provider, Provider<UserContextProvider> provider2) {
        return new ScreenRecordDialog_Factory(provider, provider2);
    }

    public static ScreenRecordDialog newInstance(RecordingController recordingController, UserContextProvider userContextProvider) {
        return new ScreenRecordDialog(recordingController, userContextProvider);
    }
}
