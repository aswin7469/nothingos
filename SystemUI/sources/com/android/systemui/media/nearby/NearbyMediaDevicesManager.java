package com.android.systemui.media.nearby;

import android.media.INearbyMediaDevicesProvider;
import android.media.INearbyMediaDevicesUpdateCallback;
import android.os.IBinder;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0002\u000b\u000e\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u000e\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\tJ\u000e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\tR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0004\n\u0002\u0010\fR\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/media/nearby/NearbyMediaDevicesManager;", "", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "logger", "Lcom/android/systemui/media/nearby/NearbyMediaDevicesLogger;", "(Lcom/android/systemui/statusbar/CommandQueue;Lcom/android/systemui/media/nearby/NearbyMediaDevicesLogger;)V", "activeCallbacks", "", "Landroid/media/INearbyMediaDevicesUpdateCallback;", "commandQueueCallbacks", "com/android/systemui/media/nearby/NearbyMediaDevicesManager$commandQueueCallbacks$1", "Lcom/android/systemui/media/nearby/NearbyMediaDevicesManager$commandQueueCallbacks$1;", "deathRecipient", "com/android/systemui/media/nearby/NearbyMediaDevicesManager$deathRecipient$1", "Lcom/android/systemui/media/nearby/NearbyMediaDevicesManager$deathRecipient$1;", "providers", "Landroid/media/INearbyMediaDevicesProvider;", "binderDiedInternal", "", "who", "Landroid/os/IBinder;", "registerNearbyDevicesCallback", "callback", "unregisterNearbyDevicesCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager {
    /* access modifiers changed from: private */
    public List<INearbyMediaDevicesUpdateCallback> activeCallbacks = new ArrayList();
    private final NearbyMediaDevicesManager$commandQueueCallbacks$1 commandQueueCallbacks;
    /* access modifiers changed from: private */
    public final NearbyMediaDevicesManager$deathRecipient$1 deathRecipient;
    /* access modifiers changed from: private */
    public final NearbyMediaDevicesLogger logger;
    /* access modifiers changed from: private */
    public List<INearbyMediaDevicesProvider> providers = new ArrayList();

    @Inject
    public NearbyMediaDevicesManager(CommandQueue commandQueue, NearbyMediaDevicesLogger nearbyMediaDevicesLogger) {
        Intrinsics.checkNotNullParameter(commandQueue, "commandQueue");
        Intrinsics.checkNotNullParameter(nearbyMediaDevicesLogger, "logger");
        this.logger = nearbyMediaDevicesLogger;
        NearbyMediaDevicesManager$commandQueueCallbacks$1 nearbyMediaDevicesManager$commandQueueCallbacks$1 = new NearbyMediaDevicesManager$commandQueueCallbacks$1(this);
        this.commandQueueCallbacks = nearbyMediaDevicesManager$commandQueueCallbacks$1;
        this.deathRecipient = new NearbyMediaDevicesManager$deathRecipient$1(this);
        commandQueue.addCallback((CommandQueue.Callbacks) nearbyMediaDevicesManager$commandQueueCallbacks$1);
    }

    public final void registerNearbyDevicesCallback(INearbyMediaDevicesUpdateCallback iNearbyMediaDevicesUpdateCallback) {
        Intrinsics.checkNotNullParameter(iNearbyMediaDevicesUpdateCallback, "callback");
        for (INearbyMediaDevicesProvider registerNearbyDevicesCallback : this.providers) {
            registerNearbyDevicesCallback.registerNearbyDevicesCallback(iNearbyMediaDevicesUpdateCallback);
        }
        this.activeCallbacks.add(iNearbyMediaDevicesUpdateCallback);
    }

    public final void unregisterNearbyDevicesCallback(INearbyMediaDevicesUpdateCallback iNearbyMediaDevicesUpdateCallback) {
        Intrinsics.checkNotNullParameter(iNearbyMediaDevicesUpdateCallback, "callback");
        this.activeCallbacks.remove((Object) iNearbyMediaDevicesUpdateCallback);
        for (INearbyMediaDevicesProvider unregisterNearbyDevicesCallback : this.providers) {
            unregisterNearbyDevicesCallback.unregisterNearbyDevicesCallback(iNearbyMediaDevicesUpdateCallback);
        }
    }

    /* access modifiers changed from: private */
    public final void binderDiedInternal(IBinder iBinder) {
        synchronized (this.providers) {
            int size = this.providers.size() - 1;
            while (true) {
                if (-1 >= size) {
                    break;
                } else if (Intrinsics.areEqual((Object) this.providers.get(size).asBinder(), (Object) iBinder)) {
                    this.providers.remove(size);
                    this.logger.logProviderBinderDied(this.providers.size());
                    break;
                } else {
                    size--;
                }
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
