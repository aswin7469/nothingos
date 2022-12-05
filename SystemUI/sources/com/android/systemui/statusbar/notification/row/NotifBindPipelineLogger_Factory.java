package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger_Factory implements Factory<NotifBindPipelineLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public NotifBindPipelineLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotifBindPipelineLogger mo1933get() {
        return newInstance(this.bufferProvider.mo1933get());
    }

    public static NotifBindPipelineLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotifBindPipelineLogger_Factory(provider);
    }

    public static NotifBindPipelineLogger newInstance(LogBuffer logBuffer) {
        return new NotifBindPipelineLogger(logBuffer);
    }
}
