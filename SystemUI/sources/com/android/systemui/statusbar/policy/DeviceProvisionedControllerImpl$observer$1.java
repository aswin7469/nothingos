package com.android.systemui.statusbar.policy;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J.\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016¨\u0006\f"}, mo64987d2 = {"com/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$observer$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uris", "", "Landroid/net/Uri;", "flags", "", "userId", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DeviceProvisionedControllerImpl.kt */
public final class DeviceProvisionedControllerImpl$observer$1 extends ContentObserver {
    final /* synthetic */ DeviceProvisionedControllerImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DeviceProvisionedControllerImpl$observer$1(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl, Handler handler) {
        super(handler);
        this.this$0 = deviceProvisionedControllerImpl;
    }

    public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
        Intrinsics.checkNotNullParameter(collection, "uris");
        boolean contains = collection.contains(this.this$0.deviceProvisionedUri);
        if (!collection.contains(this.this$0.userSetupUri)) {
            i2 = -2;
        }
        this.this$0.updateValues(contains, i2);
        if (contains) {
            this.this$0.onDeviceProvisionedChanged();
        }
        if (i2 != -2) {
            this.this$0.onUserSetupChanged();
        }
    }
}
