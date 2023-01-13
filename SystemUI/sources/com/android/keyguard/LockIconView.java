package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintWriter;

public class LockIconView extends FrameLayout implements Dumpable {
    public static final int ICON_FINGERPRINT = 1;
    public static final int ICON_LOCK = 0;
    public static final int ICON_NONE = -1;
    public static final int ICON_UNLOCK = 2;
    private boolean mAod;
    private ImageView mBgView;
    private float mDozeAmount = 0.0f;
    private int mIconType;
    private ImageView mLockIcon;
    private PointF mLockIconCenter = new PointF(0.0f, 0.0f);
    private int mLockIconColor;
    private int mLockIconPadding;
    private float mRadius;
    private final RectF mSensorRect = new RectF();
    private boolean mUseBackground = false;

    public @interface IconType {
    }

    private static int[] getLockIconState(int i, boolean z) {
        if (i == -1) {
            return new int[0];
        }
        int[] iArr = new int[2];
        if (i == 0) {
            iArr[0] = 16842916;
        } else if (i == 1) {
            iArr[0] = 16842917;
        } else if (i == 2) {
            iArr[0] = 16842918;
        }
        if (z) {
            iArr[1] = 16842915;
        } else {
            iArr[1] = -16842915;
        }
        return iArr;
    }

    private String typeToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? "invalid" : "unlock" : "fingerprint" : "lock" : "none";
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public LockIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockIcon = (ImageView) findViewById(C1894R.C1898id.lock_icon);
        this.mBgView = (ImageView) findViewById(C1894R.C1898id.lock_icon_bg);
    }

    /* access modifiers changed from: package-private */
    public void setDozeAmount(float f) {
        this.mDozeAmount = f;
        updateColorAndBackgroundVisibility();
    }

    /* access modifiers changed from: package-private */
    public void updateColorAndBackgroundVisibility() {
        this.mLockIconColor = getContext().getColor(C1894R.C1895color.nt_default_text_color);
        this.mBgView.setVisibility(8);
        this.mLockIcon.setImageTintList(ColorStateList.valueOf(this.mLockIconColor));
    }

    public void setImageDrawable(Drawable drawable) {
        this.mLockIcon.setImageDrawable(drawable);
        if (this.mUseBackground) {
            if (drawable == null) {
                this.mBgView.setVisibility(4);
            } else {
                this.mBgView.setVisibility(0);
            }
        }
    }

    public void setUseBackground(boolean z) {
        this.mUseBackground = z;
        updateColorAndBackgroundVisibility();
    }

    public void setCenterLocation(PointF pointF, float f, int i) {
        this.mLockIconCenter = pointF;
        this.mRadius = f;
        this.mLockIconPadding = i;
        this.mLockIcon.setPadding(i, i, i, i);
        this.mSensorRect.set(this.mLockIconCenter.x - this.mRadius, this.mLockIconCenter.y - this.mRadius, this.mLockIconCenter.x + this.mRadius, this.mLockIconCenter.y + this.mRadius);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.width = (int) (this.mSensorRect.right - this.mSensorRect.left);
        layoutParams.height = (int) (this.mSensorRect.bottom - this.mSensorRect.top);
        layoutParams.topMargin = (int) this.mSensorRect.top;
        layoutParams.setMarginStart((int) this.mSensorRect.left);
        setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: package-private */
    public float getLocationTop() {
        return this.mLockIconCenter.y - this.mRadius;
    }

    public void clearIcon() {
        updateIcon(-1, false);
    }

    public void updateIcon(int i, boolean z) {
        this.mIconType = i;
        this.mAod = z;
        this.mLockIcon.setImageState(getLockIconState(i, z), true);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Lock Icon View Parameters:");
        printWriter.println("    Center in px (x, y)= (" + this.mLockIconCenter.x + ", " + this.mLockIconCenter.y + NavigationBarInflaterView.KEY_CODE_END);
        printWriter.println("    Radius in pixels: " + this.mRadius);
        printWriter.println("    Drawable padding: " + this.mLockIconPadding);
        printWriter.println("    mIconType=" + typeToString(this.mIconType));
        printWriter.println("    mAod=" + this.mAod);
        printWriter.println("Lock Icon View actual measurements:");
        printWriter.println("    topLeft= (" + getX() + ", " + getY() + NavigationBarInflaterView.KEY_CODE_END);
        printWriter.println("    width=" + getWidth() + " height=" + getHeight());
    }
}
