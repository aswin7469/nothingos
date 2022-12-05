package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class KeyguardSecurityContainer extends FrameLayout {
    private int mActivePointerId;
    private AlertDialog mAlertDialog;
    private boolean mDisappearAnimRunning;
    private boolean mIsDragging;
    private boolean mIsSecurityViewLeftAligned;
    private float mLastTouchY;
    private final List<Gefingerpoken> mMotionEventListeners;
    private boolean mOneHandedMode;
    private ViewPropertyAnimator mRunningOneHandedAnimator;
    private KeyguardSecurityModel.SecurityMode mSecurityMode;
    KeyguardSecurityViewFlipper mSecurityViewFlipper;
    private final SpringAnimation mSpringAnimation;
    private float mStartTouchY;
    private SwipeListener mSwipeListener;
    private boolean mSwipeUpToRetry;
    private final VelocityTracker mVelocityTracker;
    private final ViewConfiguration mViewConfiguration;
    private final WindowInsetsAnimation.Callback mWindowInsetsAnimationCallback;

    /* loaded from: classes.dex */
    public interface SecurityCallback {
        boolean dismiss(boolean z, int i, boolean z2);

        void finish(boolean z, int i);

        void onCancelClicked();

        void onSecurityModeChanged(KeyguardSecurityModel.SecurityMode securityMode, boolean z);

        void reset();

        void userActivity();
    }

    /* loaded from: classes.dex */
    public interface SwipeListener {
        void onSwipeUp();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* loaded from: classes.dex */
    public enum BouncerUiEvent implements UiEventLogger.UiEventEnum {
        UNKNOWN(0),
        BOUNCER_DISMISS_EXTENDED_ACCESS(413),
        BOUNCER_DISMISS_BIOMETRIC(414),
        BOUNCER_DISMISS_NONE_SECURITY(415),
        BOUNCER_DISMISS_PASSWORD(416),
        BOUNCER_DISMISS_SIM(417),
        BOUNCER_PASSWORD_SUCCESS(418),
        BOUNCER_PASSWORD_FAILURE(419);
        
        private final int mId;

        BouncerUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public KeyguardSecurityContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardSecurityContainer(Context context) {
        this(context, null, 0);
    }

    public KeyguardSecurityContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mMotionEventListeners = new ArrayList();
        this.mLastTouchY = -1.0f;
        this.mActivePointerId = -1;
        this.mStartTouchY = -1.0f;
        this.mIsSecurityViewLeftAligned = true;
        this.mOneHandedMode = false;
        this.mSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
        this.mWindowInsetsAnimationCallback = new WindowInsetsAnimation.Callback(0) { // from class: com.android.keyguard.KeyguardSecurityContainer.1
            private final Rect mInitialBounds = new Rect();
            private final Rect mFinalBounds = new Rect();

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onPrepare(WindowInsetsAnimation windowInsetsAnimation) {
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mInitialBounds);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation windowInsetsAnimation, WindowInsetsAnimation.Bounds bounds) {
                if (!KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    KeyguardSecurityContainer.this.beginJankInstrument(17);
                } else {
                    KeyguardSecurityContainer.this.beginJankInstrument(20);
                }
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mFinalBounds);
                return bounds;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                int i2;
                KeyguardSecurityContainer keyguardSecurityContainer;
                if (KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    i2 = -(this.mFinalBounds.bottom - this.mInitialBounds.bottom);
                } else {
                    i2 = this.mInitialBounds.bottom - this.mFinalBounds.bottom;
                }
                float f = i2;
                float f2 = KeyguardSecurityContainer.this.mDisappearAnimRunning ? -((this.mFinalBounds.bottom - this.mInitialBounds.bottom) * 0.75f) : 0.0f;
                int i3 = 0;
                float f3 = 1.0f;
                for (WindowInsetsAnimation windowInsetsAnimation : list) {
                    if ((windowInsetsAnimation.getTypeMask() & WindowInsets.Type.ime()) != 0) {
                        f3 = windowInsetsAnimation.getInterpolatedFraction();
                        i3 += (int) MathUtils.lerp(f, f2, f3);
                    }
                }
                KeyguardSecurityContainer.this.mSecurityViewFlipper.animateForIme(i3, f3, !keyguardSecurityContainer.mDisappearAnimRunning);
                return windowInsets;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                if (!KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    KeyguardSecurityContainer.this.endJankInstrument(17);
                    KeyguardSecurityContainer.this.mSecurityViewFlipper.animateForIme(0, 1.0f, true);
                    return;
                }
                KeyguardSecurityContainer.this.endJankInstrument(20);
            }
        };
        this.mSpringAnimation = new SpringAnimation(this, DynamicAnimation.Y);
        this.mViewConfiguration = ViewConfiguration.get(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResume(KeyguardSecurityModel.SecurityMode securityMode, boolean z) {
        this.mSecurityMode = securityMode;
        this.mSecurityViewFlipper.setWindowInsetsAnimationCallback(this.mWindowInsetsAnimationCallback);
        updateBiometricRetry(securityMode, z);
        updateLayoutForSecurityMode(securityMode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLayoutForSecurityMode(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mSecurityMode = securityMode;
        boolean canUseOneHandedBouncer = canUseOneHandedBouncer();
        this.mOneHandedMode = canUseOneHandedBouncer;
        if (canUseOneHandedBouncer) {
            this.mIsSecurityViewLeftAligned = isOneHandedKeyguardLeftAligned(((FrameLayout) this).mContext);
        }
        updateSecurityViewGravity();
        updateSecurityViewLocation(false);
    }

    public void updateKeyguardPosition(float f) {
        if (this.mOneHandedMode) {
            moveBouncerForXCoordinate(f, false);
        }
    }

    private boolean canUseOneHandedBouncer() {
        if (getResources().getBoolean(17891532) && KeyguardSecurityModel.isSecurityViewOneHanded(this.mSecurityMode)) {
            return getResources().getBoolean(R$bool.can_use_one_handed_bouncer);
        }
        return false;
    }

    private boolean isOneHandedKeyguardLeftAligned(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), "one_handed_keyguard_side") == 0;
        } catch (Settings.SettingNotFoundException unused) {
            return true;
        }
    }

    private void updateSecurityViewGravity() {
        KeyguardSecurityViewFlipper findKeyguardSecurityView = findKeyguardSecurityView();
        if (findKeyguardSecurityView == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) findKeyguardSecurityView.getLayoutParams();
        if (this.mOneHandedMode) {
            layoutParams.gravity = 83;
        } else {
            layoutParams.gravity = 1;
        }
        findKeyguardSecurityView.setLayoutParams(layoutParams);
    }

    private void updateSecurityViewLocation(boolean z) {
        KeyguardSecurityViewFlipper findKeyguardSecurityView = findKeyguardSecurityView();
        if (findKeyguardSecurityView == null) {
            return;
        }
        if (!this.mOneHandedMode) {
            findKeyguardSecurityView.setTranslationX(0.0f);
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.mRunningOneHandedAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            this.mRunningOneHandedAnimator = null;
        }
        int measuredWidth = this.mIsSecurityViewLeftAligned ? 0 : (int) (getMeasuredWidth() / 2.0f);
        if (z) {
            ViewPropertyAnimator translationX = findKeyguardSecurityView.animate().translationX(measuredWidth);
            this.mRunningOneHandedAnimator = translationX;
            translationX.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mRunningOneHandedAnimator.setListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardSecurityContainer.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    KeyguardSecurityContainer.this.mRunningOneHandedAnimator = null;
                }
            });
            this.mRunningOneHandedAnimator.setDuration(360L);
            this.mRunningOneHandedAnimator.start();
            return;
        }
        findKeyguardSecurityView.setTranslationX(measuredWidth);
    }

    private KeyguardSecurityViewFlipper findKeyguardSecurityView() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (isKeyguardSecurityView(childAt)) {
                return (KeyguardSecurityViewFlipper) childAt;
            }
        }
        return null;
    }

    private boolean isKeyguardSecurityView(View view) {
        return view instanceof KeyguardSecurityViewFlipper;
    }

    public void onPause() {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mAlertDialog = null;
        }
        this.mSecurityViewFlipper.setWindowInsetsAnimationCallback(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r3 != 3) goto L13;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        boolean z = this.mMotionEventListeners.stream().anyMatch(new Predicate() { // from class: com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean onInterceptTouchEvent;
                onInterceptTouchEvent = ((Gefingerpoken) obj).onInterceptTouchEvent(motionEvent);
                return onInterceptTouchEvent;
            }
        }) || super.onInterceptTouchEvent(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            int actionIndex = motionEvent.getActionIndex();
            this.mStartTouchY = motionEvent.getY(actionIndex);
            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            this.mVelocityTracker.clear();
        } else {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.mIsDragging) {
                        return true;
                    }
                    if (!this.mSwipeUpToRetry || this.mSecurityViewFlipper.getSecurityView().disallowInterceptTouch(motionEvent)) {
                        return false;
                    }
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    float scaledTouchSlop = this.mViewConfiguration.getScaledTouchSlop() * 4.0f;
                    if (findPointerIndex != -1 && this.mStartTouchY - motionEvent.getY(findPointerIndex) > scaledTouchSlop) {
                        this.mIsDragging = true;
                        return true;
                    }
                }
            }
            this.mIsDragging = false;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x007c  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (!this.mMotionEventListeners.stream().anyMatch(new Predicate() { // from class: com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean onTouchEvent;
                onTouchEvent = ((Gefingerpoken) obj).onTouchEvent(motionEvent);
                return onTouchEvent;
            }
        })) {
            super.onTouchEvent(motionEvent);
        }
        int i = 0;
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                this.mVelocityTracker.addMovement(motionEvent);
                float y = motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                float f = this.mLastTouchY;
                if (f != -1.0f) {
                    setTranslationY(getTranslationY() + ((y - f) * 0.25f));
                }
                this.mLastTouchY = y;
            } else if (actionMasked != 3) {
                if (actionMasked == 6) {
                    int actionIndex = motionEvent.getActionIndex();
                    if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                        if (actionIndex == 0) {
                            i = 1;
                        }
                        this.mLastTouchY = motionEvent.getY(i);
                        this.mActivePointerId = motionEvent.getPointerId(i);
                    }
                }
            }
            if (actionMasked == 1) {
                if ((-getTranslationY()) > TypedValue.applyDimension(1, 10.0f, getResources().getDisplayMetrics())) {
                    SwipeListener swipeListener = this.mSwipeListener;
                    if (swipeListener != null) {
                        swipeListener.onSwipeUp();
                    }
                } else if (!this.mIsDragging) {
                    handleTap(motionEvent);
                }
            }
            return true;
        }
        this.mActivePointerId = -1;
        this.mLastTouchY = -1.0f;
        this.mIsDragging = false;
        startSpringAnimation(this.mVelocityTracker.getYVelocity());
        if (actionMasked == 1) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addMotionEventListener(Gefingerpoken gefingerpoken) {
        this.mMotionEventListeners.add(gefingerpoken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeMotionEventListener(Gefingerpoken gefingerpoken) {
        this.mMotionEventListeners.remove(gefingerpoken);
    }

    private void handleTap(MotionEvent motionEvent) {
        if (!this.mOneHandedMode) {
            return;
        }
        moveBouncerForXCoordinate(motionEvent.getX(), true);
    }

    private void moveBouncerForXCoordinate(float f, boolean z) {
        if ((!this.mIsSecurityViewLeftAligned || f <= getWidth() / 2.0f) && (this.mIsSecurityViewLeftAligned || f >= getWidth() / 2.0f)) {
            return;
        }
        this.mIsSecurityViewLeftAligned = !this.mIsSecurityViewLeftAligned;
        Settings.Global.putInt(((FrameLayout) this).mContext.getContentResolver(), "one_handed_keyguard_side", !this.mIsSecurityViewLeftAligned ? 1 : 0);
        updateSecurityViewLocation(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwipeListener(SwipeListener swipeListener) {
        this.mSwipeListener = swipeListener;
    }

    private void startSpringAnimation(float f) {
        this.mSpringAnimation.setStartVelocity(f).animateToFinalPosition(0.0f);
    }

    public void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mDisappearAnimRunning = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void beginJankInstrument(int i) {
        KeyguardInputView securityView = this.mSecurityViewFlipper.getSecurityView();
        if (securityView == null) {
            return;
        }
        InteractionJankMonitor.getInstance().begin(securityView, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endJankInstrument(int i) {
        InteractionJankMonitor.getInstance().end(i);
    }

    private void updateBiometricRetry(KeyguardSecurityModel.SecurityMode securityMode, boolean z) {
        this.mSwipeUpToRetry = (!z || securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk || securityMode == KeyguardSecurityModel.SecurityMode.None) ? false : true;
    }

    public CharSequence getTitle() {
        return this.mSecurityViewFlipper.getTitle();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSecurityViewFlipper = (KeyguardSecurityViewFlipper) findViewById(R$id.view_flipper);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int max = Integer.max(windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom, windowInsets.getInsets(WindowInsets.Type.ime()).bottom);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), max);
        return windowInsets.inset(0, 0, 0, max);
    }

    private void showDialog(String str, String str2) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        AlertDialog create = new AlertDialog.Builder(((FrameLayout) this).mContext).setTitle(str).setMessage(str2).setCancelable(false).setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null).create();
        this.mAlertDialog = create;
        if (!(((FrameLayout) this).mContext instanceof Activity)) {
            create.getWindow().setType(2009);
        }
        this.mAlertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showTimeoutDialog(int i, int i2, LockPatternUtils lockPatternUtils, KeyguardSecurityModel.SecurityMode securityMode) {
        int i3;
        int i4 = i2 / 1000;
        int i5 = AnonymousClass3.$SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[securityMode.ordinal()];
        if (i5 == 1) {
            i3 = R$string.kg_too_many_failed_pattern_attempts_dialog_message;
        } else if (i5 == 2) {
            i3 = R$string.kg_too_many_failed_pin_attempts_dialog_message;
        } else {
            i3 = i5 != 3 ? 0 : R$string.kg_too_many_failed_password_attempts_dialog_message;
        }
        if (i3 != 0) {
            showDialog(null, ((FrameLayout) this).mContext.getString(i3, Integer.valueOf(lockPatternUtils.getCurrentFailedPasswordAttempts(i)), Integer.valueOf(i4)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardSecurityContainer$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode;

        static {
            int[] iArr = new int[KeyguardSecurityModel.SecurityMode.values().length];
            $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode = iArr;
            try {
                iArr[KeyguardSecurityModel.SecurityMode.Pattern.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.PIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.Password.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.Invalid.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.None.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPin.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPuk.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) / 2, View.MeasureSpec.getMode(i));
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                if (this.mOneHandedMode && isKeyguardSecurityView(childAt)) {
                    measureChildWithMargins(childAt, makeMeasureSpec, 0, i2, 0);
                } else {
                    measureChildWithMargins(childAt, i, 0, i2, 0);
                }
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                int max = Math.max(i3, childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                i4 = Math.max(i4, childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                i3 = max;
                i5 = FrameLayout.combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        setMeasuredDimension(FrameLayout.resolveSizeAndState(Math.max(i3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i, i5), FrameLayout.resolveSizeAndState(Math.max(i4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i2, i5 << 16));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateSecurityViewLocation(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showAlmostAtWipeDialog(int i, int i2, int i3) {
        String string;
        if (i3 == 1) {
            string = ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_almost_at_wipe, Integer.valueOf(i), Integer.valueOf(i2));
        } else if (i3 != 2) {
            string = i3 != 3 ? null : ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_almost_at_erase_user, Integer.valueOf(i), Integer.valueOf(i2));
        } else {
            string = ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_almost_at_erase_profile, Integer.valueOf(i), Integer.valueOf(i2));
        }
        showDialog(null, string);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showWipeDialog(int i, int i2) {
        String string;
        if (i2 == 1) {
            string = ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_now_wiping, Integer.valueOf(i));
        } else if (i2 != 2) {
            string = i2 != 3 ? null : ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_now_erasing_user, Integer.valueOf(i));
        } else {
            string = ((FrameLayout) this).mContext.getString(R$string.kg_failed_attempts_now_erasing_profile, Integer.valueOf(i));
        }
        showDialog(null, string);
    }

    public void reset() {
        this.mDisappearAnimRunning = false;
    }
}
