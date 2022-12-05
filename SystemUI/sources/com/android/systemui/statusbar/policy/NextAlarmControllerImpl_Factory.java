package com.android.systemui.statusbar.policy;

import android.app.AlarmManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NextAlarmControllerImpl_Factory implements Factory<NextAlarmControllerImpl> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<DumpManager> dumpManagerProvider;

    public NextAlarmControllerImpl_Factory(Provider<AlarmManager> provider, Provider<BroadcastDispatcher> provider2, Provider<DumpManager> provider3) {
        this.alarmManagerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NextAlarmControllerImpl mo1933get() {
        return newInstance(this.alarmManagerProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static NextAlarmControllerImpl_Factory create(Provider<AlarmManager> provider, Provider<BroadcastDispatcher> provider2, Provider<DumpManager> provider3) {
        return new NextAlarmControllerImpl_Factory(provider, provider2, provider3);
    }

    public static NextAlarmControllerImpl newInstance(AlarmManager alarmManager, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager) {
        return new NextAlarmControllerImpl(alarmManager, broadcastDispatcher, dumpManager);
    }
}
