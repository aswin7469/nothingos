package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PreparationCoordinatorLogger_Factory implements Factory<PreparationCoordinatorLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public PreparationCoordinatorLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PreparationCoordinatorLogger mo1933get() {
        return newInstance(this.bufferProvider.mo1933get());
    }

    public static PreparationCoordinatorLogger_Factory create(Provider<LogBuffer> provider) {
        return new PreparationCoordinatorLogger_Factory(provider);
    }

    public static PreparationCoordinatorLogger newInstance(LogBuffer logBuffer) {
        return new PreparationCoordinatorLogger(logBuffer);
    }
}