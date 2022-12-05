package com.android.keyguard;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.android.internal.widget.LockPatternView;
import com.android.settingslib.animation.AppearAnimationCreator;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class KeyguardPatternView extends KeyguardInputView implements AppearAnimationCreator<LockPatternView.CellState> {
    private final AppearAnimationUtils mAppearAnimationUtils;
    private ViewGroup mContainer;
    private final DisappearAnimationUtils mDisappearAnimationUtils;
    private final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    private View mEcaView;
    private long mLastPokeTime;
    private final Rect mLockPatternScreenBounds;
    private LockPatternView mLockPatternView;
    KeyguardMessageArea mSecurityMessageDisplay;
    private final Rect mTempRect;
    private final int[] mTmpPosition;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public KeyguardPatternView(Context context) {
        this(context, null);
    }

    public KeyguardPatternView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTmpPosition = new int[2];
        this.mTempRect = new Rect();
        this.mLockPatternScreenBounds = new Rect();
        this.mLastPokeTime = -7000L;
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220L, 1.5f, 2.0f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563662));
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 125L, 1.2f, 0.6f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context, 187L, 1.2f, 0.6f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternView = findViewById(R$id.lockPatternView);
        this.mEcaView = findViewById(R$id.keyguard_selector_fade_container);
        this.mContainer = (ViewGroup) findViewById(R$id.pattern_container);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSecurityMessageDisplay = KeyguardMessageArea.findSecurityMessageDisplay(this);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastPokeTime;
        if (onTouchEvent && elapsedRealtime > 6900) {
            this.mLastPokeTime = SystemClock.elapsedRealtime();
        }
        boolean z = false;
        this.mTempRect.set(0, 0, 0, 0);
        offsetRectIntoDescendantCoords(this.mLockPatternView, this.mTempRect);
        Rect rect = this.mTempRect;
        motionEvent.offsetLocation(rect.left, rect.top);
        if (this.mLockPatternView.dispatchTouchEvent(motionEvent) || onTouchEvent) {
            z = true;
        }
        Rect rect2 = this.mTempRect;
        motionEvent.offsetLocation(-rect2.left, -rect2.top);
        return z;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mLockPatternView.getLocationOnScreen(this.mTmpPosition);
        Rect rect = this.mLockPatternScreenBounds;
        int[] iArr = this.mTmpPosition;
        rect.set(iArr[0] - 40, iArr[1] - 40, iArr[0] + this.mLockPatternView.getWidth() + 40, this.mTmpPosition[1] + this.mLockPatternView.getHeight() + 40);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.keyguard.KeyguardInputView
    public boolean disallowInterceptTouch(MotionEvent motionEvent) {
        return !this.mLockPatternView.isEmpty() || this.mLockPatternScreenBounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
    }

    @Override // com.android.keyguard.KeyguardInputView
    public void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        setTranslationY(this.mAppearAnimationUtils.getStartTranslation());
        AppearAnimationUtils.startTranslationYAnimation(this, 0L, 500L, 0.0f, this.mAppearAnimationUtils.getInterpolator(), getAnimationListener(18));
        this.mAppearAnimationUtils.startAnimation2d(this.mLockPatternView.getCellStates(), new Runnable() { // from class: com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPatternView.this.lambda$startAppearAnimation$0();
            }
        }, this);
        if (!TextUtils.isEmpty(this.mSecurityMessageDisplay.getText())) {
            AppearAnimationUtils appearAnimationUtils = this.mAppearAnimationUtils;
            appearAnimationUtils.createAnimation((View) this.mSecurityMessageDisplay, 0L, 220L, appearAnimationUtils.getStartTranslation(), true, this.mAppearAnimationUtils.getInterpolator(), (Runnable) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAppearAnimation$0() {
        enableClipping(true);
    }

    public boolean startDisappearAnimation(boolean z, final Runnable runnable) {
        float f = z ? 1.5f : 1.0f;
        this.mLockPatternView.clearPattern();
        enableClipping(false);
        setTranslationY(0.0f);
        AppearAnimationUtils.startTranslationYAnimation(this, 0L, 300.0f * f, -this.mDisappearAnimationUtils.getStartTranslation(), this.mDisappearAnimationUtils.getInterpolator(), getAnimationListener(21));
        (z ? this.mDisappearAnimationUtilsLocked : this.mDisappearAnimationUtils).startAnimation2d(this.mLockPatternView.getCellStates(), new Runnable() { // from class: com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPatternView.this.lambda$startDisappearAnimation$1(runnable);
            }
        }, this);
        if (!TextUtils.isEmpty(this.mSecurityMessageDisplay.getText())) {
            DisappearAnimationUtils disappearAnimationUtils = this.mDisappearAnimationUtils;
            disappearAnimationUtils.createAnimation((View) this.mSecurityMessageDisplay, 0L, f * 200.0f, (-disappearAnimationUtils.getStartTranslation()) * 3.0f, false, this.mDisappearAnimationUtils.getInterpolator(), (Runnable) null);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDisappearAnimation$1(Runnable runnable) {
        enableClipping(true);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void enableClipping(boolean z) {
        setClipChildren(z);
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
    }

    @Override // com.android.settingslib.animation.AppearAnimationCreator
    public void createAnimation(LockPatternView.CellState cellState, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable) {
        this.mLockPatternView.startCellStateAnimation(cellState, 1.0f, z ? 1.0f : 0.0f, z ? f : 0.0f, z ? 0.0f : f, z ? 0.0f : 1.0f, 1.0f, j, j2, interpolator, runnable);
        if (runnable != null) {
            this.mAppearAnimationUtils.createAnimation(this.mEcaView, j, j2, f, z, interpolator, (Runnable) null);
        }
    }

    @Override // com.android.keyguard.KeyguardInputView
    public CharSequence getTitle() {
        return getResources().getString(17040444);
    }
}
