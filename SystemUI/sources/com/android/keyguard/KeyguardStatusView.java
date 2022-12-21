package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.GridLayout;
import androidx.core.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.p026io.PrintWriter;
import java.util.Set;

public class KeyguardStatusView extends GridLayout {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardStatusView";
    private KeyguardClockSwitch mClockView;
    private float mDarkAmount;
    private KeyguardSliceView mKeyguardSlice;
    private View mMediaHostContainer;
    private ViewGroup mStatusViewContainer;
    private int mTextColor;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardStatusView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDarkAmount = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusViewContainer = (ViewGroup) findViewById(C1893R.C1897id.status_view_container);
        this.mClockView = (KeyguardClockSwitch) findViewById(C1893R.C1897id.keyguard_clock_container);
        if (KeyguardClockAccessibilityDelegate.isNeeded(this.mContext)) {
            this.mClockView.setAccessibilityDelegate(new KeyguardClockAccessibilityDelegate(this.mContext));
        }
        this.mKeyguardSlice = (KeyguardSliceView) findViewById(C1893R.C1897id.keyguard_slice_view);
        this.mTextColor = this.mClockView.getCurrentTextColor();
        this.mMediaHostContainer = findViewById(C1893R.C1897id.status_view_media_container);
        updateDark();
    }

    /* access modifiers changed from: package-private */
    public void setDarkAmount(float f) {
        if (this.mDarkAmount != f) {
            this.mDarkAmount = f;
            this.mClockView.setDarkAmount(f);
            CrossFadeHelper.fadeOut(this.mMediaHostContainer, f);
            updateDark();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDark() {
        int blendARGB = ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
        this.mKeyguardSlice.setDarkAmount(this.mDarkAmount);
        this.mClockView.setTextColor(blendARGB);
    }

    public void setChildrenTranslationYExcludingMediaView(float f) {
        setChildrenTranslationYExcluding(f, Set.m1751of(this.mMediaHostContainer));
    }

    private void setChildrenTranslationYExcluding(float f, Set<View> set) {
        for (int i = 0; i < this.mStatusViewContainer.getChildCount(); i++) {
            View childAt = this.mStatusViewContainer.getChildAt(i);
            if (!set.contains(childAt)) {
                childAt.setTranslationY(f);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusView:");
        printWriter.println("  mDarkAmount: " + this.mDarkAmount);
        printWriter.println("  mTextColor: " + Integer.toHexString(this.mTextColor));
        KeyguardClockSwitch keyguardClockSwitch = this.mClockView;
        if (keyguardClockSwitch != null) {
            keyguardClockSwitch.dump(printWriter, strArr);
        }
        KeyguardSliceView keyguardSliceView = this.mKeyguardSlice;
        if (keyguardSliceView != null) {
            keyguardSliceView.dump(printWriter, strArr);
        }
    }
}
