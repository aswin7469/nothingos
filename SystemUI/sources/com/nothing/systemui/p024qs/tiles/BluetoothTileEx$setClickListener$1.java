package com.nothing.systemui.p024qs.tiles;

import android.icu.text.DateFormat;
import android.view.View;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.p012qs.tiles.BluetoothTile;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/nothing/systemui/qs/tiles/BluetoothTileEx$setClickListener$1", "Landroid/view/View$OnClickListener;", "onClick", "", "v", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$setClickListener$1 */
/* compiled from: BluetoothTileEx.kt */
public final class BluetoothTileEx$setClickListener$1 implements View.OnClickListener {
    final /* synthetic */ BluetoothTileEx this$0;

    BluetoothTileEx$setClickListener$1(BluetoothTileEx bluetoothTileEx) {
        this.this$0 = bluetoothTileEx;
    }

    public void onClick(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        BluetoothController access$getBluetoothController$p = this.this$0.bluetoothController;
        Intrinsics.checkNotNull(access$getBluetoothController$p);
        if (!access$getBluetoothController$p.isBluetoothConnected()) {
            TeslaInfoController access$getTeslaInfoController$p = this.this$0.teslaInfoController;
            Intrinsics.checkNotNull(access$getTeslaInfoController$p);
            if (!access$getTeslaInfoController$p.shouldShowTeslaInfo() || this.this$0.qSFragmentEx.getBtPageIndex() != 0) {
                BluetoothController access$getBluetoothController$p2 = this.this$0.bluetoothController;
                Intrinsics.checkNotNull(access$getBluetoothController$p2);
                boolean isBluetoothEnabled = access$getBluetoothController$p2.isBluetoothEnabled();
                NTLogUtil.m1686d("BluetoothTileEx", "setBluetoothEnabled: " + (!isBluetoothEnabled));
                this.this$0.refreshState(isBluetoothEnabled ? null : QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING);
                view.post(new BluetoothTileEx$setClickListener$1$$ExternalSyntheticLambda0(this.this$0, isBluetoothEnabled));
                return;
            }
        }
        BluetoothTile access$getTile$p = this.this$0.tile;
        Intrinsics.checkNotNull(access$getTile$p);
        access$getTile$p.handleIconClick(view, false);
    }

    /* access modifiers changed from: private */
    /* renamed from: onClick$lambda-0  reason: not valid java name */
    public static final void m3526onClick$lambda0(BluetoothTileEx bluetoothTileEx, boolean z) {
        Intrinsics.checkNotNullParameter(bluetoothTileEx, "this$0");
        BluetoothController access$getBluetoothController$p = bluetoothTileEx.bluetoothController;
        Intrinsics.checkNotNull(access$getBluetoothController$p);
        access$getBluetoothController$p.setBluetoothEnabled(!z);
    }
}
