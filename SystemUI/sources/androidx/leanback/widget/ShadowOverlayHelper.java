package androidx.leanback.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.C0742R;

public final class ShadowOverlayHelper {
    public static final int SHADOW_DYNAMIC = 3;
    public static final int SHADOW_NONE = 1;
    public static final int SHADOW_STATIC = 2;
    float mFocusedZ;
    boolean mNeedsOverlay;
    boolean mNeedsRoundedCorner;
    boolean mNeedsShadow;
    boolean mNeedsWrapper;
    int mRoundedCornerRadius;
    int mShadowType = 1;
    float mUnfocusedZ;

    public static final class Builder {
        private boolean keepForegroundDrawable;
        private boolean needsOverlay;
        private boolean needsRoundedCorner;
        private boolean needsShadow;
        private Options options = Options.DEFAULT;
        private boolean preferZOrder = true;

        public Builder needsOverlay(boolean z) {
            this.needsOverlay = z;
            return this;
        }

        public Builder needsShadow(boolean z) {
            this.needsShadow = z;
            return this;
        }

        public Builder needsRoundedCorner(boolean z) {
            this.needsRoundedCorner = z;
            return this;
        }

        public Builder preferZOrder(boolean z) {
            this.preferZOrder = z;
            return this;
        }

        public Builder keepForegroundDrawable(boolean z) {
            this.keepForegroundDrawable = z;
            return this;
        }

        public Builder options(Options options2) {
            this.options = options2;
            return this;
        }

        public ShadowOverlayHelper build(Context context) {
            ShadowOverlayHelper shadowOverlayHelper = new ShadowOverlayHelper();
            shadowOverlayHelper.mNeedsOverlay = this.needsOverlay;
            boolean z = false;
            shadowOverlayHelper.mNeedsRoundedCorner = this.needsRoundedCorner && ShadowOverlayHelper.supportsRoundedCorner();
            shadowOverlayHelper.mNeedsShadow = this.needsShadow && ShadowOverlayHelper.supportsShadow();
            if (shadowOverlayHelper.mNeedsRoundedCorner) {
                shadowOverlayHelper.setupRoundedCornerRadius(this.options, context);
            }
            if (!shadowOverlayHelper.mNeedsShadow) {
                shadowOverlayHelper.mShadowType = 1;
                if ((!ShadowOverlayHelper.supportsForeground() || this.keepForegroundDrawable) && shadowOverlayHelper.mNeedsOverlay) {
                    z = true;
                }
                shadowOverlayHelper.mNeedsWrapper = z;
            } else if (!this.preferZOrder || !ShadowOverlayHelper.supportsDynamicShadow()) {
                shadowOverlayHelper.mShadowType = 2;
                shadowOverlayHelper.mNeedsWrapper = true;
            } else {
                shadowOverlayHelper.mShadowType = 3;
                shadowOverlayHelper.setupDynamicShadowZ(this.options, context);
                if ((!ShadowOverlayHelper.supportsForeground() || this.keepForegroundDrawable) && shadowOverlayHelper.mNeedsOverlay) {
                    z = true;
                }
                shadowOverlayHelper.mNeedsWrapper = z;
            }
            return shadowOverlayHelper;
        }
    }

    public static final class Options {
        public static final Options DEFAULT = new Options();
        private float dynamicShadowFocusedZ = -1.0f;
        private float dynamicShadowUnfocusedZ = -1.0f;
        private int roundedCornerRadius = 0;

        public Options roundedCornerRadius(int i) {
            this.roundedCornerRadius = i;
            return this;
        }

        public Options dynamicShadowZ(float f, float f2) {
            this.dynamicShadowUnfocusedZ = f;
            this.dynamicShadowFocusedZ = f2;
            return this;
        }

        public final int getRoundedCornerRadius() {
            return this.roundedCornerRadius;
        }

        public final float getDynamicShadowUnfocusedZ() {
            return this.dynamicShadowUnfocusedZ;
        }

        public final float getDynamicShadowFocusedZ() {
            return this.dynamicShadowFocusedZ;
        }
    }

    public static boolean supportsShadow() {
        return StaticShadowHelper.supportsShadow();
    }

    public static boolean supportsDynamicShadow() {
        return ShadowHelper.supportsDynamicShadow();
    }

    public static boolean supportsRoundedCorner() {
        return RoundedRectHelper.supportsRoundedCorner();
    }

    public static boolean supportsForeground() {
        return ForegroundHelper.supportsForeground();
    }

    ShadowOverlayHelper() {
    }

    public void prepareParentForShadow(ViewGroup viewGroup) {
        if (this.mShadowType == 2) {
            StaticShadowHelper.prepareParent(viewGroup);
        }
    }

    public int getShadowType() {
        return this.mShadowType;
    }

    public boolean needsOverlay() {
        return this.mNeedsOverlay;
    }

    public boolean needsRoundedCorner() {
        return this.mNeedsRoundedCorner;
    }

    public boolean needsWrapper() {
        return this.mNeedsWrapper;
    }

    public ShadowOverlayContainer createShadowOverlayContainer(Context context) {
        if (needsWrapper()) {
            return new ShadowOverlayContainer(context, this.mShadowType, this.mNeedsOverlay, this.mUnfocusedZ, this.mFocusedZ, this.mRoundedCornerRadius);
        }
        throw new IllegalArgumentException();
    }

    public static void setNoneWrapperOverlayColor(View view, int i) {
        Drawable foreground = ForegroundHelper.getForeground(view);
        if (foreground instanceof ColorDrawable) {
            ((ColorDrawable) foreground).setColor(i);
        } else {
            ForegroundHelper.setForeground(view, new ColorDrawable(i));
        }
    }

    public void setOverlayColor(View view, int i) {
        if (needsWrapper()) {
            ((ShadowOverlayContainer) view).setOverlayColor(i);
        } else {
            setNoneWrapperOverlayColor(view, i);
        }
    }

    public void onViewCreated(View view) {
        if (needsWrapper()) {
            return;
        }
        if (!this.mNeedsShadow) {
            if (this.mNeedsRoundedCorner) {
                RoundedRectHelper.setClipToRoundedOutline(view, true, this.mRoundedCornerRadius);
            }
        } else if (this.mShadowType == 3) {
            view.setTag(C0742R.C0745id.lb_shadow_impl, ShadowHelper.addDynamicShadow(view, this.mUnfocusedZ, this.mFocusedZ, this.mRoundedCornerRadius));
        } else if (this.mNeedsRoundedCorner) {
            RoundedRectHelper.setClipToRoundedOutline(view, true, this.mRoundedCornerRadius);
        }
    }

    public static void setNoneWrapperShadowFocusLevel(View view, float f) {
        setShadowFocusLevel(getNoneWrapperDynamicShadowImpl(view), 3, f);
    }

    public void setShadowFocusLevel(View view, float f) {
        if (needsWrapper()) {
            ((ShadowOverlayContainer) view).setShadowFocusLevel(f);
        } else {
            setShadowFocusLevel(getNoneWrapperDynamicShadowImpl(view), 3, f);
        }
    }

    /* access modifiers changed from: package-private */
    public void setupDynamicShadowZ(Options options, Context context) {
        if (options.getDynamicShadowUnfocusedZ() < 0.0f) {
            Resources resources = context.getResources();
            this.mFocusedZ = resources.getDimension(C0742R.dimen.lb_material_shadow_focused_z);
            this.mUnfocusedZ = resources.getDimension(C0742R.dimen.lb_material_shadow_normal_z);
            return;
        }
        this.mFocusedZ = options.getDynamicShadowFocusedZ();
        this.mUnfocusedZ = options.getDynamicShadowUnfocusedZ();
    }

    /* access modifiers changed from: package-private */
    public void setupRoundedCornerRadius(Options options, Context context) {
        if (options.getRoundedCornerRadius() == 0) {
            this.mRoundedCornerRadius = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_rounded_rect_corner_radius);
        } else {
            this.mRoundedCornerRadius = options.getRoundedCornerRadius();
        }
    }

    static Object getNoneWrapperDynamicShadowImpl(View view) {
        return view.getTag(C0742R.C0745id.lb_shadow_impl);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
        if (r4 > 1.0f) goto L_0x0007;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void setShadowFocusLevel(java.lang.Object r2, int r3, float r4) {
        /*
            if (r2 == 0) goto L_0x001e
            r0 = 0
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 >= 0) goto L_0x0009
        L_0x0007:
            r4 = r0
            goto L_0x0010
        L_0x0009:
            r0 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 <= 0) goto L_0x0010
            goto L_0x0007
        L_0x0010:
            r0 = 2
            if (r3 == r0) goto L_0x001b
            r0 = 3
            if (r3 == r0) goto L_0x0017
            goto L_0x001e
        L_0x0017:
            androidx.leanback.widget.ShadowHelper.setShadowFocusLevel(r2, r4)
            goto L_0x001e
        L_0x001b:
            androidx.leanback.widget.StaticShadowHelper.setShadowFocusLevel(r2, r4)
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.ShadowOverlayHelper.setShadowFocusLevel(java.lang.Object, int, float):void");
    }
}
