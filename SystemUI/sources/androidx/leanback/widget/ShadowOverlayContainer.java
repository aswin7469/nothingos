package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.leanback.R$dimen;
/* loaded from: classes.dex */
public class ShadowOverlayContainer extends FrameLayout {
    private static final Rect sTempRect = new Rect();
    private float mFocusedZ;
    private boolean mInitialized;
    int mOverlayColor;
    private Paint mOverlayPaint;
    private int mShadowType;
    private float mUnfocusedZ;
    private View mWrappedView;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public ShadowOverlayContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowOverlayContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mShadowType = 1;
        useStaticShadow();
        useDynamicShadow();
    }

    public static boolean supportsShadow() {
        return StaticShadowHelper.supportsShadow();
    }

    public static boolean supportsDynamicShadow() {
        return ShadowHelper.supportsDynamicShadow();
    }

    public void useDynamicShadow() {
        useDynamicShadow(getResources().getDimension(R$dimen.lb_material_shadow_normal_z), getResources().getDimension(R$dimen.lb_material_shadow_focused_z));
    }

    public void useDynamicShadow(float unfocusedZ, float focusedZ) {
        if (this.mInitialized) {
            throw new IllegalStateException("Already initialized");
        }
        if (!supportsDynamicShadow()) {
            return;
        }
        this.mShadowType = 3;
        this.mUnfocusedZ = unfocusedZ;
        this.mFocusedZ = focusedZ;
    }

    public void useStaticShadow() {
        if (this.mInitialized) {
            throw new IllegalStateException("Already initialized");
        }
        if (!supportsShadow()) {
            return;
        }
        this.mShadowType = 2;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mOverlayPaint == null || this.mOverlayColor == 0) {
            return;
        }
        canvas.drawRect(this.mWrappedView.getLeft(), this.mWrappedView.getTop(), this.mWrappedView.getRight(), this.mWrappedView.getBottom(), this.mOverlayPaint);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view;
        super.onLayout(changed, l, t, r, b);
        if (!changed || (view = this.mWrappedView) == null) {
            return;
        }
        Rect rect = sTempRect;
        rect.left = (int) view.getPivotX();
        rect.top = (int) this.mWrappedView.getPivotY();
        offsetDescendantRectToMyCoords(this.mWrappedView, rect);
        setPivotX(rect.left);
        setPivotY(rect.top);
    }
}
