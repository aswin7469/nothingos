package com.android.systemui.broadcast;

import com.android.systemui.CoreStartable;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class BroadcastDispatcherModule {
    /* access modifiers changed from: package-private */
    @IntoMap
    @ClassKey(BroadcastDispatcherStartable.class)
    @Binds
    public abstract CoreStartable bindsBroadastDispatcherStartable(BroadcastDispatcherStartable broadcastDispatcherStartable);
}
