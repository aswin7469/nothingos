package com.android.systemui.media;

import android.app.Notification;
import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/media/LocalMediaManagerFactory;", "", "context", "Landroid/content/Context;", "localBluetoothManager", "Lcom/android/settingslib/bluetooth/LocalBluetoothManager;", "(Landroid/content/Context;Lcom/android/settingslib/bluetooth/LocalBluetoothManager;)V", "create", "Lcom/android/settingslib/media/LocalMediaManager;", "packageName", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LocalMediaManagerFactory.kt */
public final class LocalMediaManagerFactory {
    private final Context context;
    private final LocalBluetoothManager localBluetoothManager;

    @Inject
    public LocalMediaManagerFactory(Context context2, LocalBluetoothManager localBluetoothManager2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.localBluetoothManager = localBluetoothManager2;
    }

    public final LocalMediaManager create(String str) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        return new LocalMediaManager(this.context, this.localBluetoothManager, new InfoMediaManager(this.context, str, (Notification) null, this.localBluetoothManager), str);
    }
}
