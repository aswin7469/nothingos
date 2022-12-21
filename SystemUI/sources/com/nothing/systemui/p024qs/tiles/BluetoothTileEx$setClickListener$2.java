package com.nothing.systemui.p024qs.tiles;

import android.icu.text.DateFormat;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.tiles.BluetoothTile;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/nothing/systemui/qs/tiles/BluetoothTileEx$setClickListener$2", "Landroid/view/View$OnLongClickListener;", "onLongClick", "", "v", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$setClickListener$2 */
/* compiled from: BluetoothTileEx.kt */
public final class BluetoothTileEx$setClickListener$2 implements View.OnLongClickListener {
    final /* synthetic */ BluetoothTileEx this$0;

    BluetoothTileEx$setClickListener$2(BluetoothTileEx bluetoothTileEx) {
        this.this$0 = bluetoothTileEx;
    }

    public boolean onLongClick(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        TeslaInfoController access$getTeslaInfoController$p = this.this$0.teslaInfoController;
        Intrinsics.checkNotNull(access$getTeslaInfoController$p);
        if (access$getTeslaInfoController$p.shouldShowTeslaInfo() && this.this$0.qSFragmentEx.getBtPageIndex() == 0) {
            view.setTag(C1893R.C1897id.qs_tesla_tag, "tesla");
        }
        BluetoothTile access$getTile$p = this.this$0.tile;
        Intrinsics.checkNotNull(access$getTile$p);
        access$getTile$p.handleIconClick(view, true);
        return true;
    }
}
