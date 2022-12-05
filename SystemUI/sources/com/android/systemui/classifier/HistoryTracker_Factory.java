package com.android.systemui.classifier;

import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HistoryTracker_Factory implements Factory<HistoryTracker> {
    private final Provider<SystemClock> systemClockProvider;

    public HistoryTracker_Factory(Provider<SystemClock> provider) {
        this.systemClockProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public HistoryTracker mo1933get() {
        return newInstance(this.systemClockProvider.mo1933get());
    }

    public static HistoryTracker_Factory create(Provider<SystemClock> provider) {
        return new HistoryTracker_Factory(provider);
    }

    public static HistoryTracker newInstance(SystemClock systemClock) {
        return new HistoryTracker(systemClock);
    }
}
