package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.Dumpable;
import com.android.systemui.R$color;
import com.android.systemui.R$id;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class LockIconView extends FrameLayout implements Dumpable {
    private ImageView mBgView;
    private ImageView mLockIcon;
    private int mLockIconColor;
    private int mRadius;
    private PointF mLockIconCenter = new PointF(0.0f, 0.0f);
    private boolean mUseBackground = false;
    private final RectF mSensorRect = new RectF();

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public LockIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockIcon = (ImageView) findViewById(R$id.lock_icon);
        this.mBgView = (ImageView) findViewById(R$id.lock_icon_bg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateColorAndBackgroundVisibility() {
        this.mLockIconColor = getContext().getColor(R$color.nothingDefaultTextColor);
        this.mBgView.setVisibility(8);
        this.mLockIcon.setImageTintList(ColorStateList.valueOf(this.mLockIconColor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImageDrawable(Drawable drawable) {
        this.mLockIcon.setImageDrawable(drawable);
        if (!this.mUseBackground) {
            return;
        }
        if (drawable == null) {
            this.mBgView.setVisibility(4);
        } else {
            this.mBgView.setVisibility(0);
        }
    }

    public void setUseBackground(boolean z) {
        this.mUseBackground = z;
        updateColorAndBackgroundVisibility();
    }

    public void setCenterLocation(PointF pointF, int i) {
        this.mLockIconCenter = pointF;
        this.mRadius = i;
        RectF rectF = this.mSensorRect;
        float f = pointF.x;
        float f2 = pointF.y;
        rectF.set(f - i, f2 - i, f + i, f2 + i);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        RectF rectF2 = this.mSensorRect;
        float f3 = rectF2.right;
        float f4 = rectF2.left;
        layoutParams.width = (int) (f3 - f4);
        float f5 = rectF2.bottom;
        float f6 = rectF2.top;
        layoutParams.height = (int) (f5 - f6);
        layoutParams.topMargin = (int) f6;
        layoutParams.setMarginStart((int) f4);
        setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLocationTop() {
        return this.mLockIconCenter.y - this.mRadius;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Center in px (x, y)= (" + this.mLockIconCenter.x + ", " + this.mLockIconCenter.y + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("Radius in pixels: ");
        sb.append(this.mRadius);
        printWriter.println(sb.toString());
        printWriter.println("topLeft= (" + getX() + ", " + getY() + ")");
    }
}
