package com.nothing.systemui.p024qs.tileimpl;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.tileimpl.IgnorableChildLinearLayout;
import com.android.systemui.p012qs.tileimpl.QSTileViewImpl;
import com.nothing.systemui.p024qs.CircleTileTextView;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 32\u00020\u0001:\u00013B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u001e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0013J>\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u0013J\u001e\u0010!\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0013J\u001e\u0010$\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u00042\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0013J\u001e\u0010(\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010)\u001a\u00020*J\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010+\u001a\u00020\u00042\u0006\u0010,\u001a\u00020-J\u0006\u0010.\u001a\u00020\u000eJ\u001e\u0010/\u001a\u00020\u000e2\u0006\u0010%\u001a\u00020\u00042\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u000201R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u00064"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileViewImplEx;", "", "()V", "isIntercepted", "", "touchDown", "Landroid/graphics/PointF;", "uiModeManager", "Landroid/app/UiModeManager;", "getUiModeManager", "()Landroid/app/UiModeManager;", "setUiModeManager", "(Landroid/app/UiModeManager;)V", "applyLabelContainerMarginStart", "", "isHorizontal", "labelContainer", "Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout;", "labelMargin", "", "getBottom", "bottom", "top", "heightOverride", "getCircleIconBackgroundColorForState", "context", "Landroid/content/Context;", "state", "colorActive", "colorInactiveCircleDark", "colorInactiveCircle", "colorUnavailableCircleDark", "colorUnavailableCircle", "getCircularTileBackgroundColor", "colorInactiveDark", "colorInactive", "getLabelColorForState", "isCircularTile", "colorLabelInactive", "colorLabelActive", "inflateLabelContainer", "tileViewImpl", "Lcom/android/systemui/qs/tileimpl/QSTileViewImpl;", "onTouchEvent", "ev", "Landroid/view/MotionEvent;", "resetIntercepted", "setCircleTileLabelIfNeeded", "label", "Landroid/widget/TextView;", "secondaryLabel", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tileimpl.QSTileViewImplEx */
/* compiled from: QSTileViewImplEx.kt */
public final class QSTileViewImplEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "QSTileViewImplEx";
    private static final int TILE_MISTOUCH_DIFF_X_THRESHOLD = 0;
    private boolean isIntercepted;
    private final PointF touchDown = new PointF();
    private UiModeManager uiModeManager;

    public final int getBottom(int i, int i2, int i3) {
        return i3 != -1 ? i2 + i3 : i;
    }

    public final int getLabelColorForState(boolean z, int i, int i2) {
        return z ? i : i2;
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileViewImplEx$Companion;", "", "()V", "TAG", "", "TILE_MISTOUCH_DIFF_X_THRESHOLD", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tileimpl.QSTileViewImplEx$Companion */
    /* compiled from: QSTileViewImplEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final UiModeManager getUiModeManager() {
        return this.uiModeManager;
    }

    public final void setUiModeManager(UiModeManager uiModeManager2) {
        this.uiModeManager = uiModeManager2;
    }

    public final void applyLabelContainerMarginStart(boolean z, IgnorableChildLinearLayout ignorableChildLinearLayout, int i) {
        Intrinsics.checkNotNullParameter(ignorableChildLinearLayout, "labelContainer");
        if (z) {
            ViewGroup.LayoutParams layoutParams = ignorableChildLinearLayout.getLayoutParams();
            if (layoutParams != null) {
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(i);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        }
    }

    public final int getCircularTileBackgroundColor(Context context, int i, int i2) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (this.uiModeManager == null) {
            this.uiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        }
        UiModeManager uiModeManager2 = this.uiModeManager;
        boolean z = false;
        if (uiModeManager2 != null && uiModeManager2.getNightMode() == 2) {
            z = true;
        }
        return z ? i : i2;
    }

    public final int getCircleIconBackgroundColorForState(Context context, int i, int i2, int i3, int i4, int i5, int i6) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (this.uiModeManager == null) {
            this.uiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        }
        UiModeManager uiModeManager2 = this.uiModeManager;
        boolean z = uiModeManager2 != null && uiModeManager2.getNightMode() == 2;
        if (i == 0) {
            return z ? i5 : i6;
        }
        if (i == 1) {
            return z ? i3 : i4;
        }
        if (i == 2) {
            return i2;
        }
        NTLogUtil.m1684w(TAG, "Invalid state " + i);
        return 0;
    }

    public final IgnorableChildLinearLayout inflateLabelContainer(boolean z, Context context, QSTileViewImpl qSTileViewImpl) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "tileViewImpl");
        View inflate = LayoutInflater.from(context).inflate(z ? C1893R.layout.qs_tile_label : C1893R.layout.qs_tile_label_circle, qSTileViewImpl, false);
        if (inflate != null) {
            return (IgnorableChildLinearLayout) inflate;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout");
    }

    public final void setCircleTileLabelIfNeeded(boolean z, TextView textView, TextView textView2) {
        Intrinsics.checkNotNullParameter(textView, BaseIconCache.IconDB.COLUMN_LABEL);
        Intrinsics.checkNotNullParameter(textView2, "secondaryLabel");
        if (z) {
            ((CircleTileTextView) textView).setIsLabel(true);
            ((CircleTileTextView) textView2).setIsSecondLabel(true);
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        float abs = Math.abs(motionEvent.getRawX() - this.touchDown.x);
        Math.abs(motionEvent.getRawY() - this.touchDown.y);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.touchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.isIntercepted = false;
        } else if (action == 1 && abs > 0.0f) {
            this.isIntercepted = true;
            return true;
        }
        return false;
    }

    public final boolean isIntercepted() {
        return this.isIntercepted;
    }

    public final void resetIntercepted() {
        this.isIntercepted = false;
    }
}
