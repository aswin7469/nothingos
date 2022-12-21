package com.android.systemui.assist;

import androidx.slice.Clock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AssistModule_ProvideSystemClockFactory implements Factory<Clock> {
    public Clock get() {
        return provideSystemClock();
    }

    public static AssistModule_ProvideSystemClockFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Clock provideSystemClock() {
        return (Clock) Preconditions.checkNotNullFromProvides(AssistModule.provideSystemClock());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final AssistModule_ProvideSystemClockFactory INSTANCE = new AssistModule_ProvideSystemClockFactory();

        private InstanceHolder() {
        }
    }
}
