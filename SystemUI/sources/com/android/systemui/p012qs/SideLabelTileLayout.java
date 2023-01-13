package com.android.systemui.p012qs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.C1894R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\b\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0016J\b\u0010\u000f\u001a\u00020\u000bH\u0016J\b\u0010\u0010\u001a\u00020\u000bH\u0014¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/qs/SideLabelTileLayout;", "Lcom/android/systemui/qs/TileLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "getPhantomTopPosition", "", "index", "isFull", "", "updateMaxRows", "allowedHeight", "tilesCount", "updateResources", "useSidePadding", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.SideLabelTileLayout */
/* compiled from: SideLabelTileLayout.kt */
public class SideLabelTileLayout extends TileLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* access modifiers changed from: protected */
    public boolean useSidePadding() {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SideLabelTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public boolean updateResources() {
        boolean updateResources = super.updateResources();
        this.mMaxAllowedRows = getContext().getResources().getInteger(C1894R.integer.quick_settings_max_rows);
        return updateResources;
    }

    public boolean isFull() {
        return this.mRecords.size() >= maxTiles();
    }

    public final int getPhantomTopPosition(int i) {
        return getRowTop(i / this.mColumns);
    }

    public boolean updateMaxRows(int i, int i2) {
        int i3 = this.mRows;
        this.mRows = this.mMaxAllowedRows;
        if (this.mRows > ((this.mColumns + i2) - 1) / this.mColumns) {
            this.mRows = ((i2 + this.mColumns) - 1) / this.mColumns;
        }
        if (i3 != this.mRows) {
            return true;
        }
        return false;
    }
}
