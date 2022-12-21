package com.android.systemui.p012qs.customize;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.p012qs.tileimpl.QSTileViewImpl;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0011\u001a\u00020\bH\u0014J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0012\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u001b\u001a\u00020\bH\u0016R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR$\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\r¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/qs/customize/CustomizeTileView;", "Lcom/android/systemui/qs/tileimpl/QSTileViewImpl;", "context", "Landroid/content/Context;", "icon", "Lcom/android/systemui/plugins/qs/QSIconView;", "(Landroid/content/Context;Lcom/android/systemui/plugins/qs/QSIconView;)V", "value", "", "showAppLabel", "getShowAppLabel", "()Z", "setShowAppLabel", "(Z)V", "showSideView", "getShowSideView", "setShowSideView", "animationsEnabled", "changeState", "", "state", "Lcom/android/systemui/plugins/qs/QSTile$State;", "getVisibilityState", "", "text", "", "handleStateChanged", "isLongClickable", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.customize.CustomizeTileView */
/* compiled from: CustomizeTileView.kt */
public final class CustomizeTileView extends QSTileViewImpl {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private boolean showAppLabel;
    private boolean showSideView;

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
    public boolean animationsEnabled() {
        return false;
    }

    public boolean isLongClickable() {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CustomizeTileView(Context context, QSIconView qSIconView) {
        super(context, qSIconView, false);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, BaseIconCache.IconDB.COLUMN_ICON);
        this.showSideView = true;
    }

    public final boolean getShowAppLabel() {
        return this.showAppLabel;
    }

    public final void setShowAppLabel(boolean z) {
        this.showAppLabel = z;
        getSecondaryLabel().setVisibility(getVisibilityState(getSecondaryLabel().getText()));
    }

    public final boolean getShowSideView() {
        return this.showSideView;
    }

    public final void setShowSideView(boolean z) {
        this.showSideView = z;
        if (!z) {
            getSideView().setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void handleStateChanged(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        super.handleStateChanged(state);
        setShowRippleEffect(false);
        getSecondaryLabel().setVisibility(getVisibilityState(state.secondaryLabel));
        if (!this.showSideView) {
            getSideView().setVisibility(8);
        }
    }

    private final int getVisibilityState(CharSequence charSequence) {
        return (!this.showAppLabel || TextUtils.isEmpty(charSequence)) ? 8 : 0;
    }

    public final void changeState(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        handleStateChanged(state);
    }
}
