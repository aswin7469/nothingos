package com.android.systemui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.hardware.graphics.common.DisplayDecorationSupport;
import android.view.DisplayCutout;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.systemui.statusbar.phone.ScrimController;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(mo64986d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0018\u0018\u0000 @2\u00020\u0001:\u0001@B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u001f\u001a\u00020 H\u0007J\"\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u00182\u0006\u0010%\u001a\u00020\bH\u0002J\u0010\u0010&\u001a\u00020 2\u0006\u0010\"\u001a\u00020#H\u0002J\u0012\u0010'\u001a\u00020\u00142\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\u0010\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020\bH\u0002J\u0010\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bH\u0002J\u0010\u0010.\u001a\u00020\b2\u0006\u0010/\u001a\u00020\bH\u0002J\u0010\u00100\u001a\u00020\b2\u0006\u0010/\u001a\u00020\bH\u0002J\b\u00101\u001a\u00020 H\u0014J\u0010\u00102\u001a\u00020 2\u0006\u0010\"\u001a\u00020#H\u0016J\b\u00103\u001a\u00020 H\u0016J\b\u00104\u001a\u00020 H\u0002J\b\u00105\u001a\u00020 H\u0002J\b\u00106\u001a\u00020 H\u0002J\u0016\u00107\u001a\u00020 2\u0006\u00108\u001a\u00020\u00182\u0006\u00109\u001a\u00020\u0018J\b\u0010:\u001a\u00020 H\u0002J&\u0010;\u001a\u00020 2\u0006\u0010<\u001a\u00020\u00142\u0006\u0010=\u001a\u00020\u00142\u0006\u0010>\u001a\u00020\b2\u0006\u0010?\u001a\u00020\bR\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u0018X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u00020\u001c8\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000¨\u0006A"}, mo64987d2 = {"Lcom/android/systemui/ScreenDecorHwcLayer;", "Lcom/android/systemui/DisplayCutoutBaseView;", "context", "Landroid/content/Context;", "displayDecorationSupport", "Landroid/hardware/graphics/common/DisplayDecorationSupport;", "(Landroid/content/Context;Landroid/hardware/graphics/common/DisplayDecorationSupport;)V", "bgColor", "", "clearPaint", "Landroid/graphics/Paint;", "color", "colorMode", "getColorMode", "()I", "cornerBgFilter", "Landroid/graphics/ColorFilter;", "cornerFilter", "debugTransparentRegionPaint", "hasBottomRoundedCorner", "", "hasTopRoundedCorner", "roundedCornerBottomSize", "roundedCornerDrawableBottom", "Landroid/graphics/drawable/Drawable;", "roundedCornerDrawableTop", "roundedCornerTopSize", "tempRect", "Landroid/graphics/Rect;", "transparentRect", "useInvertedAlphaColor", "calculateTransparentRect", "", "drawRoundedCorner", "canvas", "Landroid/graphics/Canvas;", "drawable", "size", "drawRoundedCorners", "gatherTransparentRegion", "region", "Landroid/graphics/Region;", "getRoundedCornerRotationDegree", "defaultDegree", "getRoundedCornerSizeByPosition", "position", "getRoundedCornerTranslationX", "degree", "getRoundedCornerTranslationY", "onAttachedToWindow", "onDraw", "onUpdate", "removeCutoutFromTransparentRegion", "removeCutoutProtectionFromTransparentRegion", "removeRoundedCornersFromTransparentRegion", "updateRoundedCornerDrawable", "top", "bottom", "updateRoundedCornerDrawableBounds", "updateRoundedCornerExistenceAndSize", "hasTop", "hasBottom", "topSize", "bottomSize", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ScreenDecorHwcLayer.kt */
public final class ScreenDecorHwcLayer extends DisplayCutoutBaseView {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG_COLOR = ScreenDecorations.DEBUG_COLOR;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final int bgColor;
    private final Paint clearPaint;
    private final int color;
    private final int colorMode;
    private final ColorFilter cornerBgFilter;
    private final ColorFilter cornerFilter;
    private final Paint debugTransparentRegionPaint;
    private boolean hasBottomRoundedCorner;
    private boolean hasTopRoundedCorner;
    private int roundedCornerBottomSize;
    private Drawable roundedCornerDrawableBottom;
    private Drawable roundedCornerDrawableTop;
    private int roundedCornerTopSize;
    private final Rect tempRect;
    public final Rect transparentRect;
    private final boolean useInvertedAlphaColor;

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

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ScreenDecorHwcLayer(Context context, DisplayDecorationSupport displayDecorationSupport) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(displayDecorationSupport, "displayDecorationSupport");
        this.transparentRect = new Rect();
        this.tempRect = new Rect();
        if (displayDecorationSupport.format == 56) {
            if (DEBUG_COLOR) {
                this.color = ScrimController.DEBUG_FRONT_TINT;
                this.bgColor = 0;
                this.colorMode = 0;
                this.useInvertedAlphaColor = false;
                Paint paint = new Paint();
                paint.setColor(788594432);
                paint.setStyle(Paint.Style.FILL);
                this.debugTransparentRegionPaint = paint;
            } else {
                this.colorMode = 4;
                boolean z = displayDecorationSupport.alphaInterpretation == 0;
                this.useInvertedAlphaColor = z;
                if (z) {
                    this.color = 0;
                    this.bgColor = ViewCompat.MEASURED_STATE_MASK;
                } else {
                    this.color = ViewCompat.MEASURED_STATE_MASK;
                    this.bgColor = 0;
                }
                this.debugTransparentRegionPaint = null;
            }
            this.cornerFilter = new PorterDuffColorFilter(this.color, PorterDuff.Mode.SRC_IN);
            this.cornerBgFilter = new PorterDuffColorFilter(this.bgColor, PorterDuff.Mode.SRC_OUT);
            Paint paint2 = new Paint();
            this.clearPaint = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            return;
        }
        throw new IllegalArgumentException("Attempting to use unsupported mode " + PixelFormat.formatToString(displayDecorationSupport.format));
    }

    public final int getColorMode() {
        return this.colorMode;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getParent().requestTransparentRegion(this);
        if (!DEBUG_COLOR) {
            getViewRootImpl().setDisplayDecoration(true);
        }
        if (this.useInvertedAlphaColor) {
            this.paint.set(this.clearPaint);
            return;
        }
        this.paint.setColor(this.color);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public void onUpdate() {
        getParent().requestTransparentRegion(this);
    }

    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.useInvertedAlphaColor) {
            canvas.drawColor(this.bgColor);
        }
        drawRoundedCorners(canvas);
        super.onDraw(canvas);
        Paint paint = this.debugTransparentRegionPaint;
        if (paint != null) {
            canvas.drawRect(this.transparentRect, paint);
        }
    }

    public boolean gatherTransparentRegion(Region region) {
        if (region == null) {
            return false;
        }
        calculateTransparentRect();
        if (DEBUG_COLOR) {
            region.setEmpty();
            return false;
        }
        region.op(this.transparentRect, Region.Op.INTERSECT);
        return false;
    }

    public final void calculateTransparentRect() {
        this.transparentRect.set(0, 0, getWidth(), getHeight());
        removeCutoutFromTransparentRegion();
        removeCutoutProtectionFromTransparentRegion();
        removeRoundedCornersFromTransparentRegion();
    }

    private final void removeCutoutFromTransparentRegion() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout != null) {
            if (!displayCutout.getBoundingRectLeft().isEmpty()) {
                this.transparentRect.left = RangesKt.coerceAtLeast(displayCutout.getBoundingRectLeft().right, this.transparentRect.left);
            }
            if (!displayCutout.getBoundingRectTop().isEmpty()) {
                this.transparentRect.top = RangesKt.coerceAtLeast(displayCutout.getBoundingRectTop().bottom, this.transparentRect.top);
            }
            if (!displayCutout.getBoundingRectRight().isEmpty()) {
                this.transparentRect.right = RangesKt.coerceAtMost(displayCutout.getBoundingRectRight().left, this.transparentRect.right);
            }
            if (!displayCutout.getBoundingRectBottom().isEmpty()) {
                this.transparentRect.bottom = RangesKt.coerceAtMost(displayCutout.getBoundingRectBottom().top, this.transparentRect.bottom);
            }
        }
    }

    private final void removeCutoutProtectionFromTransparentRegion() {
        if (!this.protectionRect.isEmpty()) {
            float centerX = this.protectionRect.centerX();
            float centerY = this.protectionRect.centerY();
            float cameraProtectionProgress = (centerX - this.protectionRect.left) * getCameraProtectionProgress();
            float cameraProtectionProgress2 = (centerY - this.protectionRect.top) * getCameraProtectionProgress();
            this.tempRect.set((int) ((float) Math.floor((double) (centerX - cameraProtectionProgress))), (int) ((float) Math.floor((double) (centerY - cameraProtectionProgress2))), (int) ((float) Math.ceil((double) (centerX + cameraProtectionProgress))), (int) ((float) Math.ceil((double) (centerY + cameraProtectionProgress2))));
            int i = this.tempRect.left;
            int i2 = this.tempRect.top;
            int width = getWidth() - this.tempRect.right;
            int height = getHeight() - this.tempRect.bottom;
            int minOf = ComparisonsKt.minOf(i, i2, width, height);
            if (minOf == i) {
                this.transparentRect.left = RangesKt.coerceAtLeast(this.tempRect.right, this.transparentRect.left);
            } else if (minOf == i2) {
                this.transparentRect.top = RangesKt.coerceAtLeast(this.tempRect.bottom, this.transparentRect.top);
            } else if (minOf == width) {
                this.transparentRect.right = RangesKt.coerceAtMost(this.tempRect.left, this.transparentRect.right);
            } else if (minOf == height) {
                this.transparentRect.bottom = RangesKt.coerceAtMost(this.tempRect.top, this.transparentRect.bottom);
            }
        }
    }

    private final void removeRoundedCornersFromTransparentRegion() {
        boolean z;
        boolean z2;
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout != null) {
            z = !displayCutout.getBoundingRectTop().isEmpty() || !displayCutout.getBoundingRectBottom().isEmpty();
            z2 = !displayCutout.getBoundingRectLeft().isEmpty() || !displayCutout.getBoundingRectRight().isEmpty();
        } else {
            z2 = false;
            z = false;
        }
        if (getWidth() < getHeight()) {
            if (z || !z2) {
                this.transparentRect.top = RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(1), this.transparentRect.top);
                this.transparentRect.bottom = RangesKt.coerceAtMost(getHeight() - getRoundedCornerSizeByPosition(3), this.transparentRect.bottom);
                return;
            }
            this.transparentRect.left = RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(0), this.transparentRect.left);
            this.transparentRect.right = RangesKt.coerceAtMost(getWidth() - getRoundedCornerSizeByPosition(2), this.transparentRect.right);
        } else if (!z || z2) {
            this.transparentRect.left = RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(0), this.transparentRect.left);
            this.transparentRect.right = RangesKt.coerceAtMost(getWidth() - getRoundedCornerSizeByPosition(2), this.transparentRect.right);
        } else {
            this.transparentRect.top = RangesKt.coerceAtLeast(getRoundedCornerSizeByPosition(1), this.transparentRect.top);
            this.transparentRect.bottom = RangesKt.coerceAtMost(getHeight() - getRoundedCornerSizeByPosition(3), this.transparentRect.bottom);
        }
    }

    private final int getRoundedCornerSizeByPosition(int i) {
        int displayRotation = ((getDisplayRotation() + 0) + i) % 4;
        if (displayRotation == 0) {
            return RangesKt.coerceAtLeast(this.roundedCornerTopSize, this.roundedCornerBottomSize);
        }
        if (displayRotation == 1) {
            return this.roundedCornerTopSize;
        }
        if (displayRotation == 2) {
            return RangesKt.coerceAtLeast(this.roundedCornerTopSize, this.roundedCornerBottomSize);
        }
        if (displayRotation == 3) {
            return this.roundedCornerBottomSize;
        }
        throw new IllegalArgumentException("Incorrect position: " + i);
    }

    private final void drawRoundedCorners(Canvas canvas) {
        if (this.hasTopRoundedCorner || this.hasBottomRoundedCorner) {
            for (int i = 0; i < 4; i++) {
                canvas.save();
                int roundedCornerRotationDegree = getRoundedCornerRotationDegree(i * 90);
                canvas.rotate((float) roundedCornerRotationDegree);
                canvas.translate((float) getRoundedCornerTranslationX(roundedCornerRotationDegree), (float) getRoundedCornerTranslationY(roundedCornerRotationDegree));
                if (this.hasTopRoundedCorner && (i == 0 || i == 1)) {
                    drawRoundedCorner(canvas, this.roundedCornerDrawableTop, this.roundedCornerTopSize);
                } else if (this.hasBottomRoundedCorner && (i == 3 || i == 2)) {
                    drawRoundedCorner(canvas, this.roundedCornerDrawableBottom, this.roundedCornerBottomSize);
                }
                canvas.restore();
            }
        }
    }

    private final void drawRoundedCorner(Canvas canvas, Drawable drawable, int i) {
        if (this.useInvertedAlphaColor) {
            float f = (float) i;
            canvas.drawRect(0.0f, 0.0f, f, f, this.clearPaint);
            if (drawable != null) {
                drawable.setColorFilter(this.cornerBgFilter);
            }
        } else if (drawable != null) {
            drawable.setColorFilter(this.cornerFilter);
        }
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if (drawable != null) {
            drawable.clearColorFilter();
        }
    }

    private final int getRoundedCornerRotationDegree(int i) {
        return ((i - (getDisplayRotation() * 90)) + StackStateAnimator.ANIMATION_DURATION_STANDARD) % StackStateAnimator.ANIMATION_DURATION_STANDARD;
    }

    private final int getRoundedCornerTranslationX(int i) {
        int i2;
        if (i == 0 || i == 90) {
            return 0;
        }
        if (i == 180) {
            i2 = getWidth();
        } else if (i == 270) {
            i2 = getHeight();
        } else {
            throw new IllegalArgumentException("Incorrect degree: " + i);
        }
        return -i2;
    }

    private final int getRoundedCornerTranslationY(int i) {
        int i2;
        if (i != 0) {
            if (i == 90) {
                i2 = getWidth();
            } else if (i == 180) {
                i2 = getHeight();
            } else if (i != 270) {
                throw new IllegalArgumentException("Incorrect degree: " + i);
            }
            return -i2;
        }
        return 0;
    }

    public final void updateRoundedCornerDrawable(Drawable drawable, Drawable drawable2) {
        Intrinsics.checkNotNullParameter(drawable, "top");
        Intrinsics.checkNotNullParameter(drawable2, "bottom");
        this.roundedCornerDrawableTop = drawable;
        this.roundedCornerDrawableBottom = drawable2;
        updateRoundedCornerDrawableBounds();
        invalidate();
    }

    public final void updateRoundedCornerExistenceAndSize(boolean z, boolean z2, int i, int i2) {
        if (this.hasTopRoundedCorner != z || this.hasBottomRoundedCorner != z2 || this.roundedCornerTopSize != i || this.roundedCornerBottomSize != i2) {
            this.hasTopRoundedCorner = z;
            this.hasBottomRoundedCorner = z2;
            this.roundedCornerTopSize = i;
            this.roundedCornerBottomSize = i2;
            updateRoundedCornerDrawableBounds();
            requestLayout();
        }
    }

    private final void updateRoundedCornerDrawableBounds() {
        Drawable drawable = this.roundedCornerDrawableTop;
        if (!(drawable == null || drawable == null)) {
            int i = this.roundedCornerTopSize;
            drawable.setBounds(0, 0, i, i);
        }
        Drawable drawable2 = this.roundedCornerDrawableBottom;
        if (!(drawable2 == null || drawable2 == null)) {
            int i2 = this.roundedCornerBottomSize;
            drawable2.setBounds(0, 0, i2, i2);
        }
        invalidate();
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/ScreenDecorHwcLayer$Companion;", "", "()V", "DEBUG_COLOR", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ScreenDecorHwcLayer.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
