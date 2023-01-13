package com.android.systemui.accessibility;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.C1894R;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import java.util.Collections;

class MagnificationModeSwitch implements MagnificationGestureDetector.OnGestureListener, ComponentCallbacks {
    static final int DEFAULT_FADE_OUT_ANIMATION_DELAY_MS = 5000;
    static final long FADING_ANIMATION_DURATION_MS = 300;
    private final AccessibilityManager mAccessibilityManager;
    private final Configuration mConfiguration;
    /* access modifiers changed from: private */
    public final Context mContext;
    final Rect mDraggableWindowBounds;
    private final Runnable mFadeInAnimationTask;
    private final Runnable mFadeOutAnimationTask;
    private final MagnificationGestureDetector mGestureDetector;
    private final ImageView mImageView;
    boolean mIsFadeOutAnimating;
    private boolean mIsVisible;
    private int mMagnificationMode;
    private final WindowManager.LayoutParams mParams;
    private final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    private boolean mSingleTapDetected;
    private final SwitchListener mSwitchListener;
    private boolean mToLeftScreenEdge;
    private int mUiTimeout;
    private final Runnable mWindowInsetChangeRunnable;
    /* access modifiers changed from: private */
    public final WindowManager mWindowManager;

    public interface SwitchListener {
        void onSwitch(int i, int i2);
    }

    static int getIconResId(int i) {
        return i == 1 ? C1894R.C1896drawable.ic_open_in_new_window : C1894R.C1896drawable.ic_open_in_new_fullscreen;
    }

    public void onLowMemory() {
    }

    MagnificationModeSwitch(Context context, SwitchListener switchListener) {
        this(context, createView(context), new SfVsyncFrameCallbackProvider(), switchListener);
    }

    MagnificationModeSwitch(Context context, ImageView imageView, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, SwitchListener switchListener) {
        this.mIsFadeOutAnimating = false;
        this.mMagnificationMode = 0;
        this.mDraggableWindowBounds = new Rect();
        this.mIsVisible = false;
        this.mSingleTapDetected = false;
        this.mToLeftScreenEdge = false;
        this.mContext = context;
        this.mConfiguration = new Configuration(context.getResources().getConfiguration());
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mSwitchListener = switchListener;
        this.mParams = createLayoutParams(context);
        this.mImageView = imageView;
        imageView.setOnTouchListener(new MagnificationModeSwitch$$ExternalSyntheticLambda0(this));
        imageView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setStateDescription(MagnificationModeSwitch.this.formatStateDescription());
                accessibilityNodeInfo.setContentDescription(MagnificationModeSwitch.this.mContext.getResources().getString(C1894R.string.magnification_mode_switch_description));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId(), MagnificationModeSwitch.this.mContext.getResources().getString(C1894R.string.magnification_mode_switch_click_label)));
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1894R.C1898id.accessibility_action_move_up, MagnificationModeSwitch.this.mContext.getString(C1894R.string.accessibility_control_move_up)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1894R.C1898id.accessibility_action_move_down, MagnificationModeSwitch.this.mContext.getString(C1894R.string.accessibility_control_move_down)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1894R.C1898id.accessibility_action_move_left, MagnificationModeSwitch.this.mContext.getString(C1894R.string.accessibility_control_move_left)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1894R.C1898id.accessibility_action_move_right, MagnificationModeSwitch.this.mContext.getString(C1894R.string.accessibility_control_move_right)));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (performA11yAction(i)) {
                    return true;
                }
                return super.performAccessibilityAction(view, i, bundle);
            }

            private boolean performA11yAction(int i) {
                Rect bounds = MagnificationModeSwitch.this.mWindowManager.getCurrentWindowMetrics().getBounds();
                if (i == AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId()) {
                    MagnificationModeSwitch.this.handleSingleTap();
                    return true;
                } else if (i == C1894R.C1898id.accessibility_action_move_up) {
                    MagnificationModeSwitch.this.moveButton(0.0f, (float) (-bounds.height()));
                    return true;
                } else if (i == C1894R.C1898id.accessibility_action_move_down) {
                    MagnificationModeSwitch.this.moveButton(0.0f, (float) bounds.height());
                    return true;
                } else if (i == C1894R.C1898id.accessibility_action_move_left) {
                    MagnificationModeSwitch.this.moveButton((float) (-bounds.width()), 0.0f);
                    return true;
                } else if (i != C1894R.C1898id.accessibility_action_move_right) {
                    return false;
                } else {
                    MagnificationModeSwitch.this.moveButton((float) bounds.width(), 0.0f);
                    return true;
                }
            }
        });
        this.mWindowInsetChangeRunnable = new MagnificationModeSwitch$$ExternalSyntheticLambda1(this);
        imageView.setOnApplyWindowInsetsListener(new MagnificationModeSwitch$$ExternalSyntheticLambda2(this));
        this.mFadeInAnimationTask = new MagnificationModeSwitch$$ExternalSyntheticLambda3(this);
        this.mFadeOutAnimationTask = new MagnificationModeSwitch$$ExternalSyntheticLambda4(this);
        this.mGestureDetector = new MagnificationGestureDetector(context, context.getMainThreadHandler(), this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-accessibility-MagnificationModeSwitch */
    public /* synthetic */ WindowInsets mo29917xb290f910(View view, WindowInsets windowInsets) {
        if (!this.mImageView.getHandler().hasCallbacks(this.mWindowInsetChangeRunnable)) {
            this.mImageView.getHandler().post(this.mWindowInsetChangeRunnable);
        }
        return view.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-accessibility-MagnificationModeSwitch */
    public /* synthetic */ void mo29918xb894c46f() {
        this.mImageView.animate().alpha(1.0f).setDuration(300).start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-accessibility-MagnificationModeSwitch */
    public /* synthetic */ void mo29920xc49c5b2d() {
        this.mImageView.animate().alpha(0.0f).setDuration(300).withEndAction(new MagnificationModeSwitch$$ExternalSyntheticLambda6(this)).start();
        this.mIsFadeOutAnimating = true;
    }

    /* access modifiers changed from: private */
    public CharSequence formatStateDescription() {
        return this.mContext.getResources().getString(this.mMagnificationMode == 2 ? C1894R.string.magnification_mode_switch_state_window : C1894R.string.magnification_mode_switch_state_full_screen);
    }

    private void applyResourcesValuesWithDensityChanged() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.magnification_switch_button_size);
        this.mParams.height = dimensionPixelSize;
        this.mParams.width = dimensionPixelSize;
        if (this.mIsVisible) {
            stickToScreenEdge(this.mToLeftScreenEdge);
            mo29919xbe988fce();
            showButton(this.mMagnificationMode, false);
        }
    }

    /* access modifiers changed from: private */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.mIsVisible) {
            return false;
        }
        return this.mGestureDetector.onTouch(motionEvent);
    }

    public boolean onSingleTap() {
        this.mSingleTapDetected = true;
        handleSingleTap();
        return true;
    }

    public boolean onDrag(float f, float f2) {
        moveButton(f, f2);
        return true;
    }

    public boolean onStart(float f, float f2) {
        stopFadeOutAnimation();
        return true;
    }

    public boolean onFinish(float f, float f2) {
        if (this.mIsVisible) {
            boolean z = this.mParams.x < this.mWindowManager.getCurrentWindowMetrics().getBounds().width() / 2;
            this.mToLeftScreenEdge = z;
            stickToScreenEdge(z);
        }
        if (!this.mSingleTapDetected) {
            showButton(this.mMagnificationMode);
        }
        this.mSingleTapDetected = false;
        return true;
    }

    private void stickToScreenEdge(boolean z) {
        this.mParams.x = z ? this.mDraggableWindowBounds.left : this.mDraggableWindowBounds.right;
        updateButtonViewLayoutIfNeeded();
    }

    /* access modifiers changed from: private */
    public void moveButton(float f, float f2) {
        this.mSfVsyncFrameProvider.postFrameCallback(new MagnificationModeSwitch$$ExternalSyntheticLambda5(this, f, f2));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$moveButton$4$com-android-systemui-accessibility-MagnificationModeSwitch */
    public /* synthetic */ void mo29916xa03e7f8f(float f, float f2, long j) {
        WindowManager.LayoutParams layoutParams = this.mParams;
        layoutParams.x = (int) (((float) layoutParams.x) + f);
        WindowManager.LayoutParams layoutParams2 = this.mParams;
        layoutParams2.y = (int) (((float) layoutParams2.y) + f2);
        updateButtonViewLayoutIfNeeded();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: removeButton */
    public void mo29919xbe988fce() {
        if (this.mIsVisible) {
            this.mImageView.removeCallbacks(this.mFadeInAnimationTask);
            this.mImageView.removeCallbacks(this.mFadeOutAnimationTask);
            this.mImageView.animate().cancel();
            this.mIsFadeOutAnimating = false;
            this.mImageView.setAlpha(0.0f);
            this.mWindowManager.removeView(this.mImageView);
            this.mContext.unregisterComponentCallbacks(this);
            this.mIsVisible = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void showButton(int i) {
        showButton(i, true);
    }

    private void showButton(int i, boolean z) {
        if (this.mMagnificationMode != i) {
            this.mMagnificationMode = i;
            this.mImageView.setImageResource(getIconResId(i));
        }
        if (!this.mIsVisible) {
            onConfigurationChanged(this.mContext.getResources().getConfiguration());
            this.mContext.registerComponentCallbacks(this);
            if (z) {
                this.mDraggableWindowBounds.set(getDraggableWindowBounds());
                this.mParams.x = this.mDraggableWindowBounds.right;
                this.mParams.y = this.mDraggableWindowBounds.bottom;
                this.mToLeftScreenEdge = false;
            }
            this.mWindowManager.addView(this.mImageView, this.mParams);
            setSystemGestureExclusion();
            this.mIsVisible = true;
            this.mImageView.postOnAnimation(this.mFadeInAnimationTask);
            this.mUiTimeout = this.mAccessibilityManager.getRecommendedTimeoutMillis(5000, 5);
        }
        stopFadeOutAnimation();
        this.mImageView.postOnAnimationDelayed(this.mFadeOutAnimationTask, (long) this.mUiTimeout);
    }

    private void stopFadeOutAnimation() {
        this.mImageView.removeCallbacks(this.mFadeOutAnimationTask);
        if (this.mIsFadeOutAnimating) {
            this.mImageView.animate().cancel();
            this.mImageView.setAlpha(1.0f);
            this.mIsFadeOutAnimating = false;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        int diff = configuration.diff(this.mConfiguration);
        this.mConfiguration.setTo(configuration);
        onConfigurationChanged(diff);
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        if (i != 0) {
            if ((i & 1152) != 0) {
                Rect rect = new Rect(this.mDraggableWindowBounds);
                this.mDraggableWindowBounds.set(getDraggableWindowBounds());
                float height = ((float) (this.mParams.y - rect.top)) / ((float) rect.height());
                this.mParams.y = ((int) (height * ((float) this.mDraggableWindowBounds.height()))) + this.mDraggableWindowBounds.top;
                stickToScreenEdge(this.mToLeftScreenEdge);
            } else if ((i & 4096) != 0) {
                applyResourcesValuesWithDensityChanged();
            } else if ((i & 4) != 0) {
                updateAccessibilityWindowTitle();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onWindowInsetChanged() {
        Rect draggableWindowBounds = getDraggableWindowBounds();
        if (!this.mDraggableWindowBounds.equals(draggableWindowBounds)) {
            this.mDraggableWindowBounds.set(draggableWindowBounds);
            stickToScreenEdge(this.mToLeftScreenEdge);
        }
    }

    private void updateButtonViewLayoutIfNeeded() {
        if (this.mIsVisible) {
            WindowManager.LayoutParams layoutParams = this.mParams;
            layoutParams.x = MathUtils.constrain(layoutParams.x, this.mDraggableWindowBounds.left, this.mDraggableWindowBounds.right);
            WindowManager.LayoutParams layoutParams2 = this.mParams;
            layoutParams2.y = MathUtils.constrain(layoutParams2.y, this.mDraggableWindowBounds.top, this.mDraggableWindowBounds.bottom);
            this.mWindowManager.updateViewLayout(this.mImageView, this.mParams);
        }
    }

    private void updateAccessibilityWindowTitle() {
        this.mParams.accessibilityTitle = getAccessibilityWindowTitle(this.mContext);
        if (this.mIsVisible) {
            this.mWindowManager.updateViewLayout(this.mImageView, this.mParams);
        }
    }

    private void toggleMagnificationMode() {
        int i = this.mMagnificationMode ^ 3;
        this.mMagnificationMode = i;
        this.mImageView.setImageResource(getIconResId(i));
        this.mSwitchListener.onSwitch(this.mContext.getDisplayId(), i);
    }

    /* access modifiers changed from: private */
    public void handleSingleTap() {
        mo29919xbe988fce();
        toggleMagnificationMode();
    }

    private static ImageView createView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setAlpha(0.0f);
        return imageView;
    }

    private static WindowManager.LayoutParams createLayoutParams(Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1894R.dimen.magnification_switch_button_size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(dimensionPixelSize, dimensionPixelSize, 2039, 8, -2);
        layoutParams.gravity = 51;
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle(context);
        layoutParams.layoutInDisplayCutoutMode = 3;
        return layoutParams;
    }

    private Rect getDraggableWindowBounds() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.magnification_switch_button_margin);
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        Rect rect = new Rect(currentWindowMetrics.getBounds());
        rect.offsetTo(0, 0);
        rect.inset(0, 0, this.mParams.width, this.mParams.height);
        rect.inset(insetsIgnoringVisibility);
        rect.inset(dimensionPixelSize, dimensionPixelSize);
        return rect;
    }

    private static String getAccessibilityWindowTitle(Context context) {
        return context.getString(17039665);
    }

    private void setSystemGestureExclusion() {
        this.mImageView.post(new MagnificationModeSwitch$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSystemGestureExclusion$5$com-android-systemui-accessibility-MagnificationModeSwitch */
    public /* synthetic */ void mo29921xc1717db5() {
        this.mImageView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, this.mImageView.getWidth(), this.mImageView.getHeight())));
    }
}
