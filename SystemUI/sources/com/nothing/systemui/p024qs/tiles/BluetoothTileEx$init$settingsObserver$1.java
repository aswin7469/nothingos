package com.nothing.systemui.p024qs.tiles;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/nothing/systemui/qs/tiles/BluetoothTileEx$init$settingsObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$init$settingsObserver$1 */
/* compiled from: BluetoothTileEx.kt */
public final class BluetoothTileEx$init$settingsObserver$1 extends ContentObserver {
    final /* synthetic */ Context $context;
    final /* synthetic */ BluetoothTileEx this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BluetoothTileEx$init$settingsObserver$1(Handler handler, BluetoothTileEx bluetoothTileEx, Context context) {
        super(handler);
        this.this$0 = bluetoothTileEx;
        this.$context = context;
    }

    public void onChange(boolean z, Uri uri) {
        if (uri != null) {
            this.this$0.airpodsSwitch = Settings.System.getInt(this.$context.getContentResolver(), "nt_airpods_switch", 0);
        }
    }
}
