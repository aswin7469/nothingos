package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.util.leak.RotationUtils;
import com.nothing.systemui.statusbar.phone.PhoneStatusBarViewEx;
import java.util.Objects;

public class PhoneStatusBarView extends FrameLayout {
    private static final String TAG = "PhoneStatusBarView";
    private DarkIconDispatcher.DarkReceiver mBattery;
    private DarkIconDispatcher.DarkReceiver mClock;
    private final StatusBarContentInsetsProvider mContentInsetsProvider = ((StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class));
    private int mCutoutSideNudge = 0;
    private View mCutoutSpace;
    private DisplayCutout mDisplayCutout;
    private Rect mDisplaySize;
    private int mLayoutDirection = -1;
    private int mRotationOrientation = -1;
    private int mStatusBarHeight;
    private TouchEventHandler mTouchEventHandler;

    public interface TouchEventHandler {
        boolean handleTouchEvent(MotionEvent motionEvent);

        void onInterceptTouchEvent(MotionEvent motionEvent);
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

    public PhoneStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public void setTouchEventHandler(TouchEventHandler touchEventHandler) {
        this.mTouchEventHandler = touchEventHandler;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBattery = (DarkIconDispatcher.DarkReceiver) findViewById(C1893R.C1897id.battery);
        this.mClock = (DarkIconDispatcher.DarkReceiver) findViewById(C1893R.C1897id.clock);
        this.mCutoutSpace = findViewById(C1893R.C1897id.cutout_space_view);
        updateResources();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mClock);
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mClock);
        this.mDisplayCutout = null;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
            requestLayout();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    private boolean updateDisplayParameters() {
        boolean z;
        int layoutDirection;
        int exactRotation = RotationUtils.getExactRotation(this.mContext);
        if (exactRotation != this.mRotationOrientation) {
            this.mRotationOrientation = exactRotation;
            z = true;
        } else {
            z = false;
        }
        if (!Objects.equals(getRootWindowInsets().getDisplayCutout(), this.mDisplayCutout)) {
            this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
            z = true;
        }
        Rect maxBounds = this.mContext.getResources().getConfiguration().windowConfiguration.getMaxBounds();
        if (!Objects.equals(maxBounds, this.mDisplaySize)) {
            this.mDisplaySize = maxBounds;
            z = true;
        }
        if (this.mContext.getResources() == null || this.mContext.getResources().getConfiguration() == null || this.mLayoutDirection == (layoutDirection = this.mContext.getResources().getConfiguration().getLayoutDirection())) {
            return z;
        }
        this.mLayoutDirection = layoutDirection;
        return true;
    }

    public boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        TouchEventHandler touchEventHandler = this.mTouchEventHandler;
        if (touchEventHandler != null) {
            return touchEventHandler.handleTouchEvent(motionEvent);
        }
        Log.w(TAG, String.format("onTouch: No touch handler provided; eating gesture at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.mTouchEventHandler.onInterceptTouchEvent(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void updateResources() {
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(C1893R.dimen.display_cutout_margin_consumption);
        updateStatusBarHeight();
    }

    private void updateStatusBarHeight() {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        this.mStatusBarHeight = statusBarHeight;
        layoutParams.height = statusBarHeight - i;
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.status_bar_padding_top);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1893R.dimen.status_bar_padding_start);
        PhoneStatusBarViewEx.updateSbContentPadding(this.mContext, findViewById(C1893R.C1897id.status_bar_contents), dimensionPixelSize2, dimensionPixelSize, getResources().getDimensionPixelSize(C1893R.dimen.status_bar_padding_end));
        findViewById(C1893R.C1897id.notification_lights_out).setPaddingRelative(0, dimensionPixelSize2, 0, 0);
        setLayoutParams(layoutParams);
    }

    private void updateLayoutForCutout() {
        updateStatusBarHeight();
        updateCutoutLocation();
        updateSafeInsets();
    }

    private void updateCutoutLocation() {
        if (this.mCutoutSpace != null) {
            boolean currentRotationHasCornerCutout = this.mContentInsetsProvider.currentRotationHasCornerCutout();
            DisplayCutout displayCutout = this.mDisplayCutout;
            if (displayCutout == null || displayCutout.isEmpty() || currentRotationHasCornerCutout) {
                this.mCutoutSpace.setVisibility(8);
                return;
            }
            this.mCutoutSpace.setVisibility(0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
            Rect boundingRectTop = this.mDisplayCutout.getBoundingRectTop();
            boundingRectTop.left += this.mCutoutSideNudge;
            boundingRectTop.right -= this.mCutoutSideNudge;
            layoutParams.width = boundingRectTop.width();
            layoutParams.height = boundingRectTop.height();
        }
    }

    private void updateSafeInsets() {
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), getPaddingTop(), ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), getPaddingBottom());
    }
}
