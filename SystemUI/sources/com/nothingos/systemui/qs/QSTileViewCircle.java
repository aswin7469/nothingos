package com.nothingos.systemui.qs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: QSTileViewCircle.kt */
/* loaded from: classes2.dex */
public final class QSTileViewCircle extends QSTileViewImpl {
    private boolean animating;
    private float position;

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public int getTileGravity() {
        return 1;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public int getTileOrientation() {
        return 1;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public QSTileViewCircle(@NotNull Context context, @NotNull QSIconView _icon, boolean z) {
        super(context, _icon, z);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(_icon, "_icon");
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.circle_qs_tile_padding);
        setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    protected void dealAndAddIcon() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.circle_qs_icon_size);
        addView(get_icon(), new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    @NotNull
    public Drawable createTileBackground() {
        RippleDrawable rippleDrawable;
        if (this.animating) {
            Drawable drawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.circle_qs_tile_background_for_animating);
            Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            rippleDrawable = (RippleDrawable) drawable;
        } else {
            Drawable drawable2 = ((LinearLayout) this).mContext.getDrawable(R$drawable.circle_qs_tile_background);
            Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            rippleDrawable = (RippleDrawable) drawable2;
        }
        setRipple(rippleDrawable);
        Drawable findDrawableByLayerId = getRipple().findDrawableByLayerId(R$id.background);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ripple.findDrawableByLayerId(R.id.background)");
        setColorBackgroundDrawable(findDrawableByLayerId);
        return getColorBackgroundDrawable();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public int getLabelColorForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return getColorLabelInactive();
            }
            if (i == 2) {
                return getColorLabelInactive();
            }
            return 0;
        }
        return getColorLabelUnavailable();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public int getSecondaryLabelColorForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return getColorSecondaryLabelInactive();
            }
            if (i == 2) {
                return getColorSecondaryLabelInactive();
            }
            return 0;
        }
        return getColorSecondaryLabelUnavailable();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public void updateResources() {
        super.updateResources();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.circle_qs_tile_padding);
        setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
        if ((r4 == 1.0f) == false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void setExpansion(float f) {
        this.position = f;
        boolean z = true;
        if (!(f == 0.0f)) {
        }
        z = false;
        boolean z2 = this.animating;
        if (z != z2) {
            Log.d("QSTileViewCircle", Intrinsics.stringPlus("setExpansion: animating changed, animating = ", Boolean.valueOf(z2)));
            this.animating = z;
            setBackground(createTileBackground());
            setColor(getBackgroundColorForState(getLastState()));
        }
    }
}
