package com.android.systemui.decor;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.DisplayUtils;
import android.util.Size;
import android.view.RoundedCorners;
import androidx.core.view.ViewCompat;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\r\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J%\u00102\u001a\u0002032\u0006\u00104\u001a\u0002052\u000e\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000507H\u0016¢\u0006\u0002\u00108J\"\u00109\u001a\u0004\u0018\u00010\b2\u0006\u0010:\u001a\u00020'2\u0006\u0010;\u001a\u00020'2\u0006\u0010<\u001a\u00020'H\u0002J\u0010\u0010=\u001a\u0002032\u0006\u0010>\u001a\u00020'H\u0002J\b\u0010?\u001a\u000203H\u0002J\b\u0010@\u001a\u000203H\u0002J\u001f\u0010A\u001a\u0002032\b\u0010B\u001a\u0004\u0018\u00010\u00052\b\u0010>\u001a\u0004\u0018\u00010'¢\u0006\u0002\u0010CR\"\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\"\u0010\u0010\u001a\n \u0012*\u0004\u0018\u00010\u00110\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00188BX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u0007\u001a\u00020\u001b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u001e\u0010\u001f\u001a\u00020\u001b2\u0006\u0010\u0007\u001a\u00020\u001b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR$\u0010\"\u001a\u00020\u00182\u0006\u0010!\u001a\u00020\u0018@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001a\"\u0004\b$\u0010%R\u000e\u0010&\u001a\u00020'X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\"\u0010(\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u000bR\u001e\u0010*\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u000fR*\u0010,\u001a\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'@FX\u000e¢\u0006\u0010\n\u0002\u00101\u001a\u0004\b-\u0010.\"\u0004\b/\u00100¨\u0006D"}, mo64987d2 = {"Lcom/android/systemui/decor/RoundedCornerResDelegate;", "Lcom/android/systemui/Dumpable;", "res", "Landroid/content/res/Resources;", "displayUniqueId", "", "(Landroid/content/res/Resources;Ljava/lang/String;)V", "<set-?>", "Landroid/graphics/drawable/Drawable;", "bottomRoundedDrawable", "getBottomRoundedDrawable", "()Landroid/graphics/drawable/Drawable;", "Landroid/util/Size;", "bottomRoundedSize", "getBottomRoundedSize", "()Landroid/util/Size;", "colorTintList", "Landroid/content/res/ColorStateList;", "kotlin.jvm.PlatformType", "getColorTintList", "()Landroid/content/res/ColorStateList;", "setColorTintList", "(Landroid/content/res/ColorStateList;)V", "density", "", "getDensity", "()F", "", "hasBottom", "getHasBottom", "()Z", "hasTop", "getHasTop", "value", "physicalPixelDisplaySizeRatio", "getPhysicalPixelDisplaySizeRatio", "setPhysicalPixelDisplaySizeRatio", "(F)V", "reloadToken", "", "topRoundedDrawable", "getTopRoundedDrawable", "topRoundedSize", "getTopRoundedSize", "tuningSizeFactor", "getTuningSizeFactor", "()Ljava/lang/Integer;", "setTuningSizeFactor", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getDrawable", "displayConfigIndex", "arrayResId", "backupDrawableId", "reloadAll", "newReloadToken", "reloadMeasures", "reloadRes", "updateDisplayUniqueId", "newDisplayUniqueId", "(Ljava/lang/String;Ljava/lang/Integer;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RoundedCornerResDelegate.kt */
public final class RoundedCornerResDelegate implements Dumpable {
    private Drawable bottomRoundedDrawable;
    private Size bottomRoundedSize = new Size(0, 0);
    private ColorStateList colorTintList = ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK);
    private String displayUniqueId;
    private boolean hasBottom;
    private boolean hasTop;
    private float physicalPixelDisplaySizeRatio = 1.0f;
    private int reloadToken;
    private final Resources res;
    private Drawable topRoundedDrawable;
    private Size topRoundedSize = new Size(0, 0);
    private Integer tuningSizeFactor;

    public RoundedCornerResDelegate(Resources resources, String str) {
        Intrinsics.checkNotNullParameter(resources, "res");
        this.res = resources;
        this.displayUniqueId = str;
        reloadRes();
        reloadMeasures();
    }

    private final float getDensity() {
        return this.res.getDisplayMetrics().density;
    }

    public final boolean getHasTop() {
        return this.hasTop;
    }

    public final boolean getHasBottom() {
        return this.hasBottom;
    }

    public final Drawable getTopRoundedDrawable() {
        return this.topRoundedDrawable;
    }

    public final Drawable getBottomRoundedDrawable() {
        return this.bottomRoundedDrawable;
    }

    public final Size getTopRoundedSize() {
        return this.topRoundedSize;
    }

    public final Size getBottomRoundedSize() {
        return this.bottomRoundedSize;
    }

    public final ColorStateList getColorTintList() {
        return this.colorTintList;
    }

    public final void setColorTintList(ColorStateList colorStateList) {
        this.colorTintList = colorStateList;
    }

    public final Integer getTuningSizeFactor() {
        return this.tuningSizeFactor;
    }

    public final void setTuningSizeFactor(Integer num) {
        if (!Intrinsics.areEqual((Object) this.tuningSizeFactor, (Object) num)) {
            this.tuningSizeFactor = num;
            reloadMeasures();
        }
    }

    public final float getPhysicalPixelDisplaySizeRatio() {
        return this.physicalPixelDisplaySizeRatio;
    }

    public final void setPhysicalPixelDisplaySizeRatio(float f) {
        if (!(this.physicalPixelDisplaySizeRatio == f)) {
            this.physicalPixelDisplaySizeRatio = f;
            reloadMeasures();
        }
    }

    private final void reloadAll(int i) {
        if (this.reloadToken != i) {
            this.reloadToken = i;
            reloadRes();
            reloadMeasures();
        }
    }

    public final void updateDisplayUniqueId(String str, Integer num) {
        if (!Intrinsics.areEqual((Object) this.displayUniqueId, (Object) str)) {
            this.displayUniqueId = str;
            if (num != null) {
                this.reloadToken = num.intValue();
            }
            reloadRes();
            reloadMeasures();
        } else if (num != null) {
            reloadAll(num.intValue());
        }
    }

    private final void reloadRes() {
        int displayUniqueIdConfigIndex = DisplayUtils.getDisplayUniqueIdConfigIndex(this.res, this.displayUniqueId);
        boolean z = true;
        boolean z2 = RoundedCorners.getRoundedCornerRadius(this.res, this.displayUniqueId) > 0;
        this.hasTop = z2 || RoundedCorners.getRoundedCornerTopRadius(this.res, this.displayUniqueId) > 0;
        if (!z2 && RoundedCorners.getRoundedCornerBottomRadius(this.res, this.displayUniqueId) <= 0) {
            z = false;
        }
        this.hasBottom = z;
        this.topRoundedDrawable = getDrawable(displayUniqueIdConfigIndex, C1893R.array.config_roundedCornerTopDrawableArray, C1893R.C1895drawable.rounded_corner_top);
        this.bottomRoundedDrawable = getDrawable(displayUniqueIdConfigIndex, C1893R.array.config_roundedCornerBottomDrawableArray, C1893R.C1895drawable.rounded_corner_bottom);
    }

    private final void reloadMeasures() {
        Drawable drawable = this.topRoundedDrawable;
        if (drawable != null) {
            this.topRoundedSize = new Size(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        Drawable drawable2 = this.bottomRoundedDrawable;
        if (drawable2 != null) {
            this.bottomRoundedSize = new Size(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
        }
        Integer num = this.tuningSizeFactor;
        if (num != null) {
            int intValue = num.intValue();
            if (intValue > 0) {
                int density = (int) (((float) intValue) * getDensity());
                if (this.topRoundedSize.getWidth() > 0) {
                    this.topRoundedSize = new Size(density, density);
                }
                if (this.bottomRoundedSize.getWidth() > 0) {
                    this.bottomRoundedSize = new Size(density, density);
                }
            } else {
                return;
            }
        }
        if (!(this.physicalPixelDisplaySizeRatio == 1.0f)) {
            if (this.topRoundedSize.getWidth() != 0) {
                this.topRoundedSize = new Size((int) ((this.physicalPixelDisplaySizeRatio * ((float) this.topRoundedSize.getWidth())) + 0.5f), (int) ((this.physicalPixelDisplaySizeRatio * ((float) this.topRoundedSize.getHeight())) + 0.5f));
            }
            if (this.bottomRoundedSize.getWidth() != 0) {
                this.bottomRoundedSize = new Size((int) ((this.physicalPixelDisplaySizeRatio * ((float) this.bottomRoundedSize.getWidth())) + 0.5f), (int) ((this.physicalPixelDisplaySizeRatio * ((float) this.bottomRoundedSize.getHeight())) + 0.5f));
            }
        }
    }

    private final Drawable getDrawable(int i, int i2, int i3) {
        Drawable drawable;
        TypedArray obtainTypedArray = this.res.obtainTypedArray(i2);
        if (i < 0 || i >= obtainTypedArray.length()) {
            drawable = this.res.getDrawable(i3, (Resources.Theme) null);
        } else {
            drawable = obtainTypedArray.getDrawable(i);
        }
        obtainTypedArray.recycle();
        return drawable;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("RoundedCornerResDelegate state:");
        printWriter.println("  hasTop=" + this.hasTop);
        printWriter.println("  hasBottom=" + this.hasBottom);
        printWriter.println("  topRoundedSize(w,h)=(" + this.topRoundedSize.getWidth() + ',' + this.topRoundedSize.getHeight() + ')');
        printWriter.println("  bottomRoundedSize(w,h)=(" + this.bottomRoundedSize.getWidth() + ',' + this.bottomRoundedSize.getHeight() + ')');
        printWriter.println("  physicalPixelDisplaySizeRatio=" + this.physicalPixelDisplaySizeRatio);
    }
}
