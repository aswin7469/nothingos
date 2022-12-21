package com.nothing.systemui.p024qs;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0004H\u0016¨\u0006\n"}, mo64987d2 = {"Lcom/nothing/systemui/qs/TileLayoutEx;", "", "()V", "getRowTop", "", "row", "collapsedSignalOrBtCellHeight", "cellMarginVertical", "cellHeight", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.TileLayoutEx */
/* compiled from: TileLayoutEx.kt */
public final class TileLayoutEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "TileLayoutEx";

    public int getRowTop(int i, int i2, int i3, int i4) {
        if (i2 == 0) {
            return i * (i4 + i3);
        }
        if (i != 0) {
            return i != 1 ? i2 + i3 + ((i - 1) * (i4 + i3)) : i2 + i3;
        }
        return 0;
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/qs/TileLayoutEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.TileLayoutEx$Companion */
    /* compiled from: TileLayoutEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
