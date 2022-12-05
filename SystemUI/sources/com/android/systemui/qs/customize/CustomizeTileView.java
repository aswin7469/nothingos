package com.android.systemui.qs.customize;

import android.content.Context;
import android.text.TextUtils;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: CustomizeTileView.kt */
/* loaded from: classes.dex */
public final class CustomizeTileView extends QSTileViewImpl {
    private boolean showAppLabel;
    private boolean showSideView = true;

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    protected boolean animationsEnabled() {
        return false;
    }

    @Override // android.view.View
    public boolean isLongClickable() {
        return false;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CustomizeTileView(@NotNull Context context, @NotNull QSIconView icon) {
        super(context, icon, false);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(icon, "icon");
    }

    public final void setShowAppLabel(boolean z) {
        this.showAppLabel = z;
        mo813getSecondaryLabel().setVisibility(getVisibilityState(mo813getSecondaryLabel().getText()));
    }

    public final void setShowSideView(boolean z) {
        this.showSideView = z;
        if (!z) {
            getSideView().setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public void handleStateChanged(@NotNull QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        super.handleStateChanged(state);
        setShowRippleEffect(false);
        mo813getSecondaryLabel().setVisibility(getVisibilityState(state.secondaryLabel));
        if (!this.showSideView) {
            getSideView().setVisibility(8);
        }
    }

    private final int getVisibilityState(CharSequence charSequence) {
        return (!this.showAppLabel || TextUtils.isEmpty(charSequence)) ? 8 : 0;
    }

    public final void changeState(@NotNull QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        handleStateChanged(state);
    }
}
