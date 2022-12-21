package com.android.systemui.p012qs.external;

import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.p012qs.external.TileLifecycleManager;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.TileLifecycleManager_Factory_Impl */
public final class TileLifecycleManager_Factory_Impl implements TileLifecycleManager.Factory {
    private final C4822TileLifecycleManager_Factory delegateFactory;

    TileLifecycleManager_Factory_Impl(C4822TileLifecycleManager_Factory tileLifecycleManager_Factory) {
        this.delegateFactory = tileLifecycleManager_Factory;
    }

    public TileLifecycleManager create(Intent intent, UserHandle userHandle) {
        return this.delegateFactory.get(intent, userHandle);
    }

    public static Provider<TileLifecycleManager.Factory> create(C4822TileLifecycleManager_Factory tileLifecycleManager_Factory) {
        return InstanceFactory.create(new TileLifecycleManager_Factory_Impl(tileLifecycleManager_Factory));
    }
}
