package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.UserIcons;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.settings.GlobalSettings;
import java.net.HttpURLConnection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class KeyguardSecurityContainer extends FrameLayout {
    static final int BOUNCER_DISMISS_BIOMETRIC = 2;
    static final int BOUNCER_DISMISS_EXTENDED_ACCESS = 3;
    static final int BOUNCER_DISMISS_NONE_SECURITY = 0;
    static final int BOUNCER_DISMISS_PASSWORD = 1;
    static final int BOUNCER_DISMISS_SIM = 4;
    private static final long BOUNCER_HANDEDNESS_ANIMATION_DURATION_MS = 500;
    private static final float BOUNCER_HANDEDNESS_ANIMATION_FADE_OUT_PROPORTION = 0.2f;
    private static final long IME_DISAPPEAR_DURATION_MS = 125;
    private static final float MIN_DRAG_SIZE = 10.0f;
    static final int MODE_DEFAULT = 0;
    static final int MODE_ONE_HANDED = 1;
    static final int MODE_USER_SWITCHER = 2;
    private static final float SLOP_SCALE = 4.0f;
    private static final String TAG = "KeyguardSecurityView";
    private static final float TOUCH_Y_MULTIPLIER = 0.25f;
    static final int USER_TYPE_PRIMARY = 1;
    static final int USER_TYPE_SECONDARY_USER = 3;
    static final int USER_TYPE_WORK_PROFILE = 2;
    private int mActivePointerId;
    private AlertDialog mAlertDialog;
    private int mCurrentMode;
    /* access modifiers changed from: private */
    public boolean mDisappearAnimRunning;
    private FalsingManager mFalsingManager;
    private GlobalSettings mGlobalSettings;
    private boolean mIsDragging;
    private float mLastTouchY;
    private final List<Gefingerpoken> mMotionEventListeners;
    KeyguardSecurityViewFlipper mSecurityViewFlipper;
    private final SpringAnimation mSpringAnimation;
    private float mStartTouchY;
    private SwipeListener mSwipeListener;
    private boolean mSwipeUpToRetry;
    private UserSwitcherController mUserSwitcherController;
    private final VelocityTracker mVelocityTracker;
    private final ViewConfiguration mViewConfiguration;
    private ViewMode mViewMode;
    private int mWidth;
    private final WindowInsetsAnimation.Callback mWindowInsetsAnimationCallback;

    public @interface Mode {
    }

    public interface SecurityCallback {
        boolean dismiss(boolean z, int i, boolean z2, KeyguardSecurityModel.SecurityMode securityMode);

        void finish(boolean z, int i);

        void onCancelClicked();

        void onSecurityModeChanged(KeyguardSecurityModel.SecurityMode securityMode, boolean z);

        void reset();

        void userActivity();
    }

    public interface SwipeListener {
        void onSwipeUp();
    }

    interface ViewMode {
        int getChildWidthMeasureSpec(int i) {
            return i;
        }

        void handleTap(MotionEvent motionEvent) {
        }

        void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
        }

        void onDestroy() {
        }

        void reloadColors() {
        }

        void reset() {
        }

        void startAppearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        }

        void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        }

        void updatePositionByTouchX(float f) {
        }

        void updateSecurityViewLocation() {
        }
    }

    public boolean shouldDelayChildPressedState() {
        return true;
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

    public enum BouncerUiEvent implements UiEventLogger.UiEventEnum {
        UNKNOWN(0),
        BOUNCER_DISMISS_EXTENDED_ACCESS(HttpURLConnection.HTTP_ENTITY_TOO_LARGE),
        BOUNCER_DISMISS_BIOMETRIC(HttpURLConnection.HTTP_REQ_TOO_LONG),
        BOUNCER_DISMISS_NONE_SECURITY(HttpURLConnection.HTTP_UNSUPPORTED_TYPE),
        BOUNCER_DISMISS_PASSWORD(416),
        BOUNCER_DISMISS_SIM(417),
        BOUNCER_PASSWORD_SUCCESS(418),
        BOUNCER_PASSWORD_FAILURE(419);
        
        private final int mId;

        private BouncerUiEvent(int i) {
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
        this(context, (AttributeSet) null, 0);
    }

    public KeyguardSecurityContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mMotionEventListeners = new ArrayList();
        this.mLastTouchY = -1.0f;
        this.mActivePointerId = -1;
        this.mStartTouchY = -1.0f;
        this.mViewMode = new DefaultViewMode();
        this.mCurrentMode = 0;
        this.mWidth = -1;
        this.mWindowInsetsAnimationCallback = new WindowInsetsAnimation.Callback(0) {
            private final Rect mFinalBounds = new Rect();
            private final Rect mInitialBounds = new Rect();

            public void onPrepare(WindowInsetsAnimation windowInsetsAnimation) {
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mInitialBounds);
            }

            public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation windowInsetsAnimation, WindowInsetsAnimation.Bounds bounds) {
                if (!KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    KeyguardSecurityContainer.this.beginJankInstrument(17);
                } else {
                    KeyguardSecurityContainer.this.beginJankInstrument(20);
                }
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mFinalBounds);
                return bounds;
            }

            public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                int i;
                float f;
                if (KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    i = -(this.mFinalBounds.bottom - this.mInitialBounds.bottom);
                } else {
                    i = this.mInitialBounds.bottom - this.mFinalBounds.bottom;
                }
                float f2 = (float) i;
                float f3 = KeyguardSecurityContainer.this.mDisappearAnimRunning ? -(((float) (this.mFinalBounds.bottom - this.mInitialBounds.bottom)) * 0.75f) : 0.0f;
                int i2 = 0;
                float f4 = 1.0f;
                for (WindowInsetsAnimation next : list) {
                    if ((next.getTypeMask() & WindowInsets.Type.ime()) != 0) {
                        f4 = next.getInterpolatedFraction();
                        i2 += (int) MathUtils.lerp(f2, f3, f4);
                    }
                }
                if (KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    f = 1.0f - f4;
                } else {
                    f = Math.max(f4, KeyguardSecurityContainer.this.getAlpha());
                }
                updateChildren(i2, f);
                return windowInsets;
            }

            public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                if (!KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    KeyguardSecurityContainer.this.endJankInstrument(17);
                    updateChildren(0, 1.0f);
                    return;
                }
                KeyguardSecurityContainer.this.endJankInstrument(20);
            }

            private void updateChildren(int i, float f) {
                for (int i2 = 0; i2 < KeyguardSecurityContainer.this.getChildCount(); i2++) {
                    View childAt = KeyguardSecurityContainer.this.getChildAt(i2);
                    childAt.setTranslationY((float) i);
                    childAt.setAlpha(f);
                }
            }
        };
        this.mSpringAnimation = new SpringAnimation(this, DynamicAnimation.f133Y);
        this.mViewConfiguration = ViewConfiguration.get(context);
    }

    /* access modifiers changed from: package-private */
    public void onResume(KeyguardSecurityModel.SecurityMode securityMode, boolean z) {
        this.mSecurityViewFlipper.setWindowInsetsAnimationCallback(this.mWindowInsetsAnimationCallback);
        updateBiometricRetry(securityMode, z);
    }

    /* access modifiers changed from: package-private */
    public void initMode(int i, GlobalSettings globalSettings, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
        if (this.mCurrentMode != i) {
            Log.i(TAG, "Switching mode from " + modeToString(this.mCurrentMode) + " to " + modeToString(i));
            this.mCurrentMode = i;
            this.mViewMode.onDestroy();
            if (i == 1) {
                this.mViewMode = new OneHandedViewMode();
            } else if (i != 2) {
                this.mViewMode = new DefaultViewMode();
            } else {
                this.mViewMode = new UserSwitcherViewMode();
            }
            this.mGlobalSettings = globalSettings;
            this.mFalsingManager = falsingManager;
            this.mUserSwitcherController = userSwitcherController;
            setupViewMode();
        }
    }

    private String modeToString(int i) {
        if (i == 0) {
            return "Default";
        }
        if (i == 1) {
            return "OneHanded";
        }
        if (i == 2) {
            return "UserSwitcher";
        }
        throw new IllegalArgumentException("mode: " + i + " not supported");
    }

    private void setupViewMode() {
        GlobalSettings globalSettings;
        FalsingManager falsingManager;
        UserSwitcherController userSwitcherController;
        KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = this.mSecurityViewFlipper;
        if (keyguardSecurityViewFlipper != null && (globalSettings = this.mGlobalSettings) != null && (falsingManager = this.mFalsingManager) != null && (userSwitcherController = this.mUserSwitcherController) != null) {
            this.mViewMode.init(this, globalSettings, keyguardSecurityViewFlipper, falsingManager, userSwitcherController);
        }
    }

    /* access modifiers changed from: package-private */
    public int getMode() {
        return this.mCurrentMode;
    }

    /* access modifiers changed from: package-private */
    public void updatePositionByTouchX(float f) {
        this.mViewMode.updatePositionByTouchX(f);
    }

    public boolean isOneHandedModeLeftAligned() {
        if (this.mCurrentMode != 1 || !((OneHandedViewMode) this.mViewMode).isLeftAligned()) {
            return false;
        }
        return true;
    }

    public void onPause() {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mAlertDialog = null;
        }
        this.mSecurityViewFlipper.setWindowInsetsAnimationCallback((WindowInsetsAnimation.Callback) null);
        this.mViewMode.reset();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r3 != 3) goto L_0x007c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            java.util.List<com.android.systemui.Gefingerpoken> r0 = r5.mMotionEventListeners
            java.util.stream.Stream r0 = r0.stream()
            com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda3 r1 = new com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda3
            r1.<init>(r6)
            boolean r0 = r0.anyMatch(r1)
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x001c
            boolean r0 = super.onInterceptTouchEvent(r6)
            if (r0 == 0) goto L_0x001a
            goto L_0x001c
        L_0x001a:
            r0 = r1
            goto L_0x001d
        L_0x001c:
            r0 = r2
        L_0x001d:
            int r3 = r6.getActionMasked()
            if (r3 == 0) goto L_0x0067
            if (r3 == r2) goto L_0x0064
            r4 = 2
            if (r3 == r4) goto L_0x002c
            r6 = 3
            if (r3 == r6) goto L_0x0064
            goto L_0x007c
        L_0x002c:
            boolean r3 = r5.mIsDragging
            if (r3 == 0) goto L_0x0031
            return r2
        L_0x0031:
            boolean r3 = r5.mSwipeUpToRetry
            if (r3 != 0) goto L_0x0036
            return r1
        L_0x0036:
            com.android.keyguard.KeyguardSecurityViewFlipper r3 = r5.mSecurityViewFlipper
            com.android.keyguard.KeyguardInputView r3 = r3.getSecurityView()
            boolean r3 = r3.disallowInterceptTouch(r6)
            if (r3 == 0) goto L_0x0043
            return r1
        L_0x0043:
            int r1 = r5.mActivePointerId
            int r1 = r6.findPointerIndex(r1)
            android.view.ViewConfiguration r3 = r5.mViewConfiguration
            int r3 = r3.getScaledTouchSlop()
            float r3 = (float) r3
            r4 = 1082130432(0x40800000, float:4.0)
            float r3 = r3 * r4
            r4 = -1
            if (r1 == r4) goto L_0x007c
            float r4 = r5.mStartTouchY
            float r6 = r6.getY(r1)
            float r4 = r4 - r6
            int r6 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r6 <= 0) goto L_0x007c
            r5.mIsDragging = r2
            return r2
        L_0x0064:
            r5.mIsDragging = r1
            goto L_0x007c
        L_0x0067:
            int r1 = r6.getActionIndex()
            float r2 = r6.getY(r1)
            r5.mStartTouchY = r2
            int r6 = r6.getPointerId(r1)
            r5.mActivePointerId = r6
            android.view.VelocityTracker r5 = r5.mVelocityTracker
            r5.clear()
        L_0x007c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r7.getActionMasked()
            java.util.List<com.android.systemui.Gefingerpoken> r1 = r6.mMotionEventListeners
            java.util.stream.Stream r1 = r1.stream()
            com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda0 r2 = new com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda0
            r2.<init>(r7)
            boolean r1 = r1.anyMatch(r2)
            if (r1 != 0) goto L_0x0019
            boolean r1 = super.onTouchEvent(r7)
        L_0x0019:
            r1 = 0
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            r3 = 1
            if (r0 == r3) goto L_0x006a
            r4 = 2
            if (r0 == r4) goto L_0x0045
            r4 = 3
            if (r0 == r4) goto L_0x006a
            r2 = 6
            if (r0 == r2) goto L_0x0029
            goto L_0x007a
        L_0x0029:
            int r2 = r7.getActionIndex()
            int r4 = r7.getPointerId(r2)
            int r5 = r6.mActivePointerId
            if (r4 != r5) goto L_0x007a
            if (r2 != 0) goto L_0x0038
            r1 = r3
        L_0x0038:
            float r2 = r7.getY(r1)
            r6.mLastTouchY = r2
            int r1 = r7.getPointerId(r1)
            r6.mActivePointerId = r1
            goto L_0x007a
        L_0x0045:
            android.view.VelocityTracker r1 = r6.mVelocityTracker
            r1.addMovement(r7)
            int r1 = r6.mActivePointerId
            int r1 = r7.findPointerIndex(r1)
            float r1 = r7.getY(r1)
            float r4 = r6.mLastTouchY
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x0067
            float r2 = r1 - r4
            float r4 = r6.getTranslationY()
            r5 = 1048576000(0x3e800000, float:0.25)
            float r2 = r2 * r5
            float r4 = r4 + r2
            r6.setTranslationY(r4)
        L_0x0067:
            r6.mLastTouchY = r1
            goto L_0x007a
        L_0x006a:
            r4 = -1
            r6.mActivePointerId = r4
            r6.mLastTouchY = r2
            r6.mIsDragging = r1
            android.view.VelocityTracker r1 = r6.mVelocityTracker
            float r1 = r1.getYVelocity()
            r6.startSpringAnimation(r1)
        L_0x007a:
            if (r0 != r3) goto L_0x00a4
            float r0 = r6.getTranslationY()
            float r0 = -r0
            android.content.res.Resources r1 = r6.getResources()
            android.util.DisplayMetrics r1 = r1.getDisplayMetrics()
            r2 = 1092616192(0x41200000, float:10.0)
            float r1 = android.util.TypedValue.applyDimension(r3, r2, r1)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x009b
            com.android.keyguard.KeyguardSecurityContainer$SwipeListener r6 = r6.mSwipeListener
            if (r6 == 0) goto L_0x00a4
            r6.onSwipeUp()
            goto L_0x00a4
        L_0x009b:
            boolean r0 = r6.mIsDragging
            if (r0 != 0) goto L_0x00a4
            com.android.keyguard.KeyguardSecurityContainer$ViewMode r6 = r6.mViewMode
            r6.handleTap(r7)
        L_0x00a4:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.onTouchEvent(android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public void addMotionEventListener(Gefingerpoken gefingerpoken) {
        this.mMotionEventListeners.add(gefingerpoken);
    }

    /* access modifiers changed from: package-private */
    public void removeMotionEventListener(Gefingerpoken gefingerpoken) {
        this.mMotionEventListeners.remove((Object) gefingerpoken);
    }

    /* access modifiers changed from: package-private */
    public void setSwipeListener(SwipeListener swipeListener) {
        this.mSwipeListener = swipeListener;
    }

    private void startSpringAnimation(float f) {
        ((SpringAnimation) this.mSpringAnimation.setStartVelocity(f)).animateToFinalPosition(0.0f);
    }

    public void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mDisappearAnimRunning = true;
        this.mViewMode.startDisappearAnimation(securityMode);
    }

    public void startAppearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mViewMode.startAppearAnimation(securityMode);
    }

    /* access modifiers changed from: private */
    public void beginJankInstrument(int i) {
        KeyguardInputView securityView = this.mSecurityViewFlipper.getSecurityView();
        if (securityView != null) {
            InteractionJankMonitor.getInstance().begin(securityView, i);
        }
    }

    /* access modifiers changed from: private */
    public void endJankInstrument(int i) {
        InteractionJankMonitor.getInstance().end(i);
    }

    private void cancelJankInstrument(int i) {
        InteractionJankMonitor.getInstance().cancel(i);
    }

    private void updateBiometricRetry(KeyguardSecurityModel.SecurityMode securityMode, boolean z) {
        this.mSwipeUpToRetry = (!z || securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk || securityMode == KeyguardSecurityModel.SecurityMode.None) ? false : true;
    }

    public CharSequence getTitle() {
        return this.mSecurityViewFlipper.getTitle();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSecurityViewFlipper = (KeyguardSecurityViewFlipper) findViewById(C1893R.C1897id.view_flipper);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int max = Integer.max(windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom, windowInsets.getInsets(WindowInsets.Type.ime()).bottom);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), Integer.max(max, getContext().getResources().getDimensionPixelSize(C1893R.dimen.keyguard_security_view_bottom_margin)));
        return windowInsets.inset(0, 0, 0, max);
    }

    private void showDialog(String str, String str2) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        this.mAlertDialog = new AlertDialog.Builder(this.mContext).setTitle(str).setMessage(str2).setCancelable(false).setNeutralButton(C1893R.string.f262ok, (DialogInterface.OnClickListener) null).create();
        if (!(this.mContext instanceof Activity)) {
            this.mAlertDialog.getWindow().setType(Types.SQLXML);
        }
        this.mAlertDialog.show();
    }

    /* access modifiers changed from: package-private */
    public void showTimeoutDialog(int i, int i2, LockPatternUtils lockPatternUtils, KeyguardSecurityModel.SecurityMode securityMode) {
        int i3 = i2 / 1000;
        int i4 = C16192.f231xdc0e830a[securityMode.ordinal()];
        int i5 = i4 != 1 ? i4 != 2 ? i4 != 3 ? 0 : C1893R.string.kg_too_many_failed_password_attempts_dialog_message : C1893R.string.kg_too_many_failed_pin_attempts_dialog_message : C1893R.string.kg_too_many_failed_pattern_attempts_dialog_message;
        if (i5 != 0) {
            showDialog((String) null, this.mContext.getString(i5, new Object[]{Integer.valueOf(lockPatternUtils.getCurrentFailedPasswordAttempts(i)), Integer.valueOf(i3)}));
        }
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityContainer$2 */
    static /* synthetic */ class C16192 {

        /* renamed from: $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode */
        static final /* synthetic */ int[] f231xdc0e830a;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.keyguard.KeyguardSecurityModel$SecurityMode[] r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f231xdc0e830a = r0
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Pattern     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.PIN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Password     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Invalid     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.None     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPin     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = f231xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPuk     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.C16192.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                int childWidthMeasureSpec = this.mViewMode.getChildWidthMeasureSpec(i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                layoutParams.width = View.MeasureSpec.getSize(childWidthMeasureSpec);
                measureChildWithMargins(childAt, childWidthMeasureSpec, 0, i2, 0);
                i3 = Math.max(i3, childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                i4 = Math.max(i4, childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                i5 = combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(i3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i, i5), resolveSizeAndState(Math.max(i4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i2, i5 << 16));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int i5 = i3 - i;
        if (z && this.mWidth != i5) {
            this.mWidth = i5;
            this.mViewMode.updateSecurityViewLocation();
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mViewMode.updateSecurityViewLocation();
    }

    /* access modifiers changed from: package-private */
    public void showAlmostAtWipeDialog(int i, int i2, int i3) {
        String str;
        if (i3 == 1) {
            str = this.mContext.getString(C1893R.string.kg_failed_attempts_almost_at_wipe, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        } else if (i3 == 2) {
            str = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getResources().getString("SystemUi.KEYGUARD_DIALOG_FAILED_ATTEMPTS_ALMOST_ERASING_PROFILE", new KeyguardSecurityContainer$$ExternalSyntheticLambda2(this, i, i2), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        } else if (i3 != 3) {
            str = null;
        } else {
            str = this.mContext.getString(C1893R.string.kg_failed_attempts_almost_at_erase_user, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        }
        showDialog((String) null, str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showAlmostAtWipeDialog$2$com-android-keyguard-KeyguardSecurityContainer */
    public /* synthetic */ String mo26012x355c4cea(int i, int i2) {
        return this.mContext.getString(C1893R.string.kg_failed_attempts_almost_at_erase_profile, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }

    /* access modifiers changed from: package-private */
    public void showWipeDialog(int i, int i2) {
        String str;
        if (i2 == 1) {
            str = this.mContext.getString(C1893R.string.kg_failed_attempts_now_wiping, new Object[]{Integer.valueOf(i)});
        } else if (i2 == 2) {
            str = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getResources().getString("SystemUi.KEYGUARD_DIALOG_FAILED_ATTEMPTS_ERASING_PROFILE", new KeyguardSecurityContainer$$ExternalSyntheticLambda1(this, i), new Object[]{Integer.valueOf(i)});
        } else if (i2 != 3) {
            str = null;
        } else {
            str = this.mContext.getString(C1893R.string.kg_failed_attempts_now_erasing_user, new Object[]{Integer.valueOf(i)});
        }
        showDialog((String) null, str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showWipeDialog$3$com-android-keyguard-KeyguardSecurityContainer */
    public /* synthetic */ String mo26013x1518d08a(int i) {
        return this.mContext.getString(C1893R.string.kg_failed_attempts_now_erasing_profile, new Object[]{Integer.valueOf(i)});
    }

    public void reset() {
        this.mViewMode.reset();
        this.mDisappearAnimRunning = false;
    }

    /* access modifiers changed from: package-private */
    public void reloadColors() {
        this.mViewMode.reloadColors();
    }

    static class DefaultViewMode implements ViewMode {
        private ViewGroup mView;
        private KeyguardSecurityViewFlipper mViewFlipper;

        DefaultViewMode() {
        }

        public void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
            this.mView = viewGroup;
            this.mViewFlipper = keyguardSecurityViewFlipper;
            updateSecurityViewGroup();
        }

        private void updateSecurityViewGroup() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mViewFlipper.getLayoutParams();
            layoutParams.gravity = 1;
            this.mViewFlipper.setLayoutParams(layoutParams);
            this.mViewFlipper.setTranslationX(0.0f);
        }
    }

    static class UserSwitcherViewMode implements ViewMode {
        /* access modifiers changed from: private */
        public FalsingManager mFalsingManager;
        /* access modifiers changed from: private */
        public KeyguardUserSwitcherPopupMenu mPopup;
        private Resources mResources;
        private UserSwitcherController.UserSwitchCallback mUserSwitchCallback = new C1625xd955338d(this);
        private TextView mUserSwitcher;
        private UserSwitcherController mUserSwitcherController;
        private ViewGroup mUserSwitcherViewGroup;
        private ViewGroup mView;
        private KeyguardSecurityViewFlipper mViewFlipper;

        UserSwitcherViewMode() {
        }

        public void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
            this.mView = viewGroup;
            this.mViewFlipper = keyguardSecurityViewFlipper;
            this.mFalsingManager = falsingManager;
            this.mUserSwitcherController = userSwitcherController;
            this.mResources = viewGroup.getContext().getResources();
            if (this.mUserSwitcherViewGroup == null) {
                LayoutInflater.from(viewGroup.getContext()).inflate(C1893R.layout.keyguard_bouncer_user_switcher, this.mView, true);
                this.mUserSwitcherViewGroup = (ViewGroup) this.mView.findViewById(C1893R.C1897id.keyguard_bouncer_user_switcher);
            }
            updateSecurityViewLocation();
            this.mUserSwitcher = (TextView) this.mView.findViewById(C1893R.C1897id.user_switcher_header);
            setupUserSwitcher();
            this.mUserSwitcherController.addUserSwitchCallback(this.mUserSwitchCallback);
        }

        public void reset() {
            KeyguardUserSwitcherPopupMenu keyguardUserSwitcherPopupMenu = this.mPopup;
            if (keyguardUserSwitcherPopupMenu != null) {
                keyguardUserSwitcherPopupMenu.dismiss();
                this.mPopup = null;
            }
        }

        public void reloadColors() {
            TextView textView = (TextView) this.mView.findViewById(C1893R.C1897id.user_switcher_header);
            if (textView != null) {
                textView.setTextColor(Utils.getColorAttrDefaultColor(this.mView.getContext(), 16842806));
                textView.setBackground(this.mView.getContext().getDrawable(C1893R.C1895drawable.bouncer_user_switcher_header_bg));
            }
        }

        public void onDestroy() {
            this.mUserSwitcherController.removeUserSwitchCallback(this.mUserSwitchCallback);
        }

        private Drawable findUserIcon(int i) {
            Bitmap userIcon = UserManager.get(this.mView.getContext()).getUserIcon(i);
            if (userIcon != null) {
                return new BitmapDrawable(userIcon);
            }
            return UserIcons.getDefaultUserIcon(this.mResources, i, false);
        }

        public void startAppearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
            if (securityMode != KeyguardSecurityModel.SecurityMode.Password) {
                this.mUserSwitcherViewGroup.setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mUserSwitcherViewGroup, View.ALPHA, new float[]{1.0f});
                ofFloat.setInterpolator(Interpolators.ALPHA_IN);
                ofFloat.setDuration(500);
                ofFloat.start();
            }
        }

        public void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
            if (securityMode != KeyguardSecurityModel.SecurityMode.Password) {
                int dimensionPixelSize = this.mResources.getDimensionPixelSize(C1893R.dimen.disappear_y_translation);
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Y, new float[]{(float) dimensionPixelSize});
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mView, View.ALPHA, new float[]{0.0f});
                animatorSet.setInterpolator(Interpolators.STANDARD_ACCELERATE);
                animatorSet.playTogether(new Animator[]{ofFloat2, ofFloat});
                animatorSet.start();
            }
        }

        /* access modifiers changed from: private */
        public void setupUserSwitcher() {
            final UserSwitcherController.UserRecord currentUserRecord = this.mUserSwitcherController.getCurrentUserRecord();
            if (currentUserRecord == null) {
                Log.e(KeyguardSecurityContainer.TAG, "Current user in user switcher is null.");
                return;
            }
            ((ImageView) this.mView.findViewById(C1893R.C1897id.user_icon)).setImageDrawable(findUserIcon(currentUserRecord.info.id));
            this.mUserSwitcher.setText(this.mUserSwitcherController.getCurrentUserName());
            ViewGroup viewGroup = (ViewGroup) this.mView.findViewById(C1893R.C1897id.user_switcher_anchor);
            C16211 r2 = new UserSwitcherController.BaseUserAdapter(this.mUserSwitcherController) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    Drawable drawable;
                    UserSwitcherController.UserRecord item = getItem(i);
                    FrameLayout frameLayout = (FrameLayout) view;
                    boolean z = false;
                    if (frameLayout == null) {
                        frameLayout = (FrameLayout) LayoutInflater.from(viewGroup.getContext()).inflate(C1893R.layout.keyguard_bouncer_user_switcher_item, viewGroup, false);
                    }
                    TextView textView = (TextView) frameLayout.getChildAt(0);
                    textView.setText(getName(viewGroup.getContext(), item));
                    if (item.picture != null) {
                        drawable = new BitmapDrawable(item.picture);
                    } else {
                        drawable = getDrawable(item, frameLayout.getContext());
                    }
                    int dimensionPixelSize = frameLayout.getResources().getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_item_icon_size);
                    int dimensionPixelSize2 = frameLayout.getResources().getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_item_icon_padding);
                    drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                    textView.setCompoundDrawablePadding(dimensionPixelSize2);
                    textView.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                    if (item == currentUserRecord) {
                        textView.setBackground(frameLayout.getContext().getDrawable(C1893R.C1895drawable.bouncer_user_switcher_item_selected_bg));
                    } else {
                        textView.setBackground((Drawable) null);
                    }
                    if (item == currentUserRecord) {
                        z = true;
                    }
                    textView.setSelected(z);
                    frameLayout.setEnabled(item.isSwitchToEnabled);
                    frameLayout.setAlpha(frameLayout.isEnabled() ? 1.0f : 0.38f);
                    return frameLayout;
                }

                private Drawable getDrawable(UserSwitcherController.UserRecord userRecord, Context context) {
                    Drawable drawable;
                    int i;
                    if (!userRecord.isCurrent || !userRecord.isGuest) {
                        drawable = getIconDrawable(context, userRecord);
                    } else {
                        drawable = context.getDrawable(C1893R.C1895drawable.ic_avatar_guest_user);
                    }
                    if (userRecord.isSwitchToEnabled) {
                        i = Utils.getColorAttrDefaultColor(context, 17956901);
                    } else {
                        i = context.getResources().getColor(C1893R.C1894color.kg_user_switcher_restricted_avatar_icon_color, context.getTheme());
                    }
                    drawable.setTint(i);
                    Drawable drawable2 = context.getDrawable(C1893R.C1895drawable.user_avatar_bg);
                    drawable2.setTintBlendMode(BlendMode.DST);
                    drawable2.setTint(Utils.getColorAttrDefaultColor(context, 17956912));
                    return new LayerDrawable(new Drawable[]{drawable2, drawable});
                }
            };
            if (r2.getCount() < 2) {
                ((LayerDrawable) this.mUserSwitcher.getBackground()).getDrawable(1).setAlpha(0);
                viewGroup.setClickable(false);
                return;
            }
            ((LayerDrawable) this.mUserSwitcher.getBackground()).getDrawable(1).setAlpha(255);
            viewGroup.setOnClickListener(new C1624xd955338c(this, viewGroup, r2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setupUserSwitcher$0$com-android-keyguard-KeyguardSecurityContainer$UserSwitcherViewMode */
        public /* synthetic */ void mo26048x6c7f668e(ViewGroup viewGroup, final UserSwitcherController.BaseUserAdapter baseUserAdapter, View view) {
            if (!this.mFalsingManager.isFalseTap(1)) {
                KeyguardUserSwitcherPopupMenu keyguardUserSwitcherPopupMenu = new KeyguardUserSwitcherPopupMenu(view.getContext(), this.mFalsingManager);
                this.mPopup = keyguardUserSwitcherPopupMenu;
                keyguardUserSwitcherPopupMenu.setAnchorView(viewGroup);
                this.mPopup.setAdapter(baseUserAdapter);
                this.mPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                        if (!UserSwitcherViewMode.this.mFalsingManager.isFalseTap(1) && view.isEnabled()) {
                            UserSwitcherController.UserRecord item = baseUserAdapter.getItem(i - 1);
                            if (!item.isCurrent) {
                                baseUserAdapter.onUserListItemClicked(item);
                            }
                            UserSwitcherViewMode.this.mPopup.dismiss();
                            KeyguardUserSwitcherPopupMenu unused = UserSwitcherViewMode.this.mPopup = null;
                        }
                    }
                });
                this.mPopup.show();
            }
        }

        public int getChildWidthMeasureSpec(int i) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) / 2, 1073741824);
        }

        public void updateSecurityViewLocation() {
            int dimensionPixelSize = this.mResources.getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_y_trans);
            if (this.mResources.getConfiguration().orientation == 1) {
                updateViewGravity(this.mViewFlipper, 1);
                updateViewGravity(this.mUserSwitcherViewGroup, 1);
                this.mUserSwitcherViewGroup.setTranslationY((float) dimensionPixelSize);
                this.mViewFlipper.setTranslationY((float) (-dimensionPixelSize));
                return;
            }
            updateViewGravity(this.mViewFlipper, 85);
            updateViewGravity(this.mUserSwitcherViewGroup, 19);
            this.mUserSwitcherViewGroup.setTranslationY((float) (-dimensionPixelSize));
            this.mViewFlipper.setTranslationY(0.0f);
        }

        private void updateViewGravity(View view, int i) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.gravity = i;
            view.setLayoutParams(layoutParams);
        }
    }

    static class OneHandedViewMode implements ViewMode {
        private GlobalSettings mGlobalSettings;
        /* access modifiers changed from: private */
        public ValueAnimator mRunningOneHandedAnimator;
        private ViewGroup mView;
        private KeyguardSecurityViewFlipper mViewFlipper;

        OneHandedViewMode() {
        }

        public void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
            this.mView = viewGroup;
            this.mViewFlipper = keyguardSecurityViewFlipper;
            this.mGlobalSettings = globalSettings;
            updateSecurityViewGravity();
            updateSecurityViewLocation(isLeftAligned(), false);
        }

        public int getChildWidthMeasureSpec(int i) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) / 2, 1073741824);
        }

        private void updateSecurityViewGravity() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mViewFlipper.getLayoutParams();
            layoutParams.gravity = 83;
            this.mViewFlipper.setLayoutParams(layoutParams);
        }

        public void updatePositionByTouchX(float f) {
            boolean z = f <= ((float) this.mView.getWidth()) / 2.0f;
            updateSideSetting(z);
            updateSecurityViewLocation(z, false);
        }

        /* access modifiers changed from: package-private */
        public boolean isLeftAligned() {
            return this.mGlobalSettings.getInt("one_handed_keyguard_side", 0) == 0;
        }

        private void updateSideSetting(boolean z) {
            this.mGlobalSettings.putInt("one_handed_keyguard_side", z ^ true ? 1 : 0);
        }

        public void handleTap(MotionEvent motionEvent) {
            float x = motionEvent.getX();
            boolean isLeftAligned = isLeftAligned();
            if ((isLeftAligned && x > ((float) this.mView.getWidth()) / 2.0f) || (!isLeftAligned && x < ((float) this.mView.getWidth()) / 2.0f)) {
                boolean z = !isLeftAligned;
                updateSideSetting(z);
                SysUiStatsLog.write(63, z ? 5 : 6);
                updateSecurityViewLocation(z, true);
            }
        }

        public void updateSecurityViewLocation() {
            updateSecurityViewLocation(isLeftAligned(), false);
        }

        private void updateSecurityViewLocation(boolean z, boolean z2) {
            int i;
            ValueAnimator valueAnimator = this.mRunningOneHandedAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.mRunningOneHandedAnimator = null;
            }
            boolean z3 = false;
            if (z) {
                i = 0;
            } else {
                i = this.mView.getMeasuredWidth() - this.mViewFlipper.getWidth();
            }
            if (z2) {
                Interpolator loadInterpolator = AnimationUtils.loadInterpolator(this.mView.getContext(), 17563674);
                Interpolator interpolator = Interpolators.FAST_OUT_LINEAR_IN;
                Interpolator interpolator2 = Interpolators.LINEAR_OUT_SLOW_IN;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mRunningOneHandedAnimator = ofFloat;
                ofFloat.setDuration(500);
                this.mRunningOneHandedAnimator.setInterpolator(Interpolators.LINEAR);
                int translationX = (int) this.mViewFlipper.getTranslationX();
                int dimension = (int) this.mView.getResources().getDimension(C1893R.dimen.one_handed_bouncer_move_animation_translation);
                if (this.mViewFlipper.hasOverlappingRendering() && this.mViewFlipper.getLayerType() != 2) {
                    z3 = true;
                }
                boolean z4 = z3;
                if (z4) {
                    this.mViewFlipper.setLayerType(2, (Paint) null);
                }
                float alpha = this.mViewFlipper.getAlpha();
                this.mRunningOneHandedAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        ValueAnimator unused = OneHandedViewMode.this.mRunningOneHandedAnimator = null;
                    }
                });
                this.mRunningOneHandedAnimator.addUpdateListener(new C1623xc47775e2(this, loadInterpolator, dimension, z, interpolator, alpha, translationX, interpolator2, i, z4));
                this.mRunningOneHandedAnimator.start();
                return;
            }
            this.mViewFlipper.setTranslationX((float) i);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$updateSecurityViewLocation$0$com-android-keyguard-KeyguardSecurityContainer$OneHandedViewMode */
        public /* synthetic */ void mo26043x3b6e1f6e(Interpolator interpolator, int i, boolean z, Interpolator interpolator2, float f, int i2, Interpolator interpolator3, int i3, boolean z2, ValueAnimator valueAnimator) {
            boolean z3 = valueAnimator.getAnimatedFraction() < 0.2f;
            int interpolation = (int) (interpolator.getInterpolation(valueAnimator.getAnimatedFraction()) * ((float) i));
            int i4 = i - interpolation;
            if (z) {
                interpolation = -interpolation;
                i4 = -i4;
            }
            if (z3) {
                this.mViewFlipper.setAlpha(interpolator2.getInterpolation(MathUtils.constrainedMap(1.0f, 0.0f, 0.0f, 0.2f, valueAnimator.getAnimatedFraction())) * f);
                this.mViewFlipper.setTranslationX((float) (i2 + interpolation));
            } else {
                this.mViewFlipper.setAlpha(interpolator3.getInterpolation(MathUtils.constrainedMap(0.0f, 1.0f, 0.2f, 1.0f, valueAnimator.getAnimatedFraction())));
                this.mViewFlipper.setTranslationX((float) (i3 - i4));
            }
            if (valueAnimator.getAnimatedFraction() == 1.0f && z2) {
                this.mViewFlipper.setLayerType(0, (Paint) null);
            }
        }
    }
}
