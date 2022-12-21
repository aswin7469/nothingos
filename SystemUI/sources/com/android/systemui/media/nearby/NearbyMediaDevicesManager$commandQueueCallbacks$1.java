package com.android.systemui.media.nearby;

import android.media.INearbyMediaDevicesProvider;
import android.media.INearbyMediaDevicesUpdateCallback;
import com.android.systemui.statusbar.CommandQueue;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/media/nearby/NearbyMediaDevicesManager$commandQueueCallbacks$1", "Lcom/android/systemui/statusbar/CommandQueue$Callbacks;", "registerNearbyMediaDevicesProvider", "", "newProvider", "Landroid/media/INearbyMediaDevicesProvider;", "unregisterNearbyMediaDevicesProvider", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    final /* synthetic */ NearbyMediaDevicesManager this$0;

    NearbyMediaDevicesManager$commandQueueCallbacks$1(NearbyMediaDevicesManager nearbyMediaDevicesManager) {
        this.this$0 = nearbyMediaDevicesManager;
    }

    public void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        Intrinsics.checkNotNullParameter(iNearbyMediaDevicesProvider, "newProvider");
        if (!this.this$0.providers.contains(iNearbyMediaDevicesProvider)) {
            for (INearbyMediaDevicesUpdateCallback registerNearbyDevicesCallback : this.this$0.activeCallbacks) {
                iNearbyMediaDevicesProvider.registerNearbyDevicesCallback(registerNearbyDevicesCallback);
            }
            this.this$0.providers.add(iNearbyMediaDevicesProvider);
            this.this$0.logger.logProviderRegistered(this.this$0.providers.size());
            iNearbyMediaDevicesProvider.asBinder().linkToDeath(this.this$0.deathRecipient, 0);
        }
    }

    public void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        Intrinsics.checkNotNullParameter(iNearbyMediaDevicesProvider, "newProvider");
        if (this.this$0.providers.remove((Object) iNearbyMediaDevicesProvider)) {
            this.this$0.logger.logProviderUnregistered(this.this$0.providers.size());
        }
    }
}
