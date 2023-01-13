package com.android.systemui.media.nearby;

import android.os.IBinder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/media/nearby/NearbyMediaDevicesManager$deathRecipient$1", "Landroid/os/IBinder$DeathRecipient;", "binderDied", "", "who", "Landroid/os/IBinder;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager$deathRecipient$1 implements IBinder.DeathRecipient {
    final /* synthetic */ NearbyMediaDevicesManager this$0;

    public void binderDied() {
    }

    NearbyMediaDevicesManager$deathRecipient$1(NearbyMediaDevicesManager nearbyMediaDevicesManager) {
        this.this$0 = nearbyMediaDevicesManager;
    }

    public void binderDied(IBinder iBinder) {
        Intrinsics.checkNotNullParameter(iBinder, "who");
        this.this$0.binderDiedInternal(iBinder);
    }
}
