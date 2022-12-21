package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.C1893R;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.util.LargeScreenUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Set;

public class AuthBiometricView extends LinearLayout {
    protected static final int STATE_AUTHENTICATED = 6;
    protected static final int STATE_AUTHENTICATING = 2;
    protected static final int STATE_AUTHENTICATING_ANIMATING_IN = 1;
    protected static final int STATE_ERROR = 4;
    protected static final int STATE_HELP = 3;
    protected static final int STATE_IDLE = 0;
    protected static final int STATE_PENDING_CONFIRMATION = 5;
    private static final String TAG = "AuthBiometricView";
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityManager;
    int mAnimationDurationHideDialog;
    int mAnimationDurationLong;
    int mAnimationDurationShort;
    private final View.OnClickListener mBackgroundClickListener;
    protected Callback mCallback;
    Button mCancelButton;
    Button mConfirmButton;
    /* access modifiers changed from: private */
    public TextView mDescriptionView;
    protected boolean mDialogSizeAnimating;
    private int mEffectiveUserId;
    protected final Handler mHandler;
    AuthIconController mIconController;
    private View mIconHolderView;
    private float mIconOriginalY;
    protected ImageView mIconView;
    protected ImageView mIndicatorImg;
    protected TextView mIndicatorView;
    AuthDialog.LayoutParams mLayoutParams;
    private final LockPatternUtils mLockPatternUtils;
    Button mNegativeButton;
    private AuthPanelController mPanelController;
    private PromptInfo mPromptInfo;
    private boolean mRequireConfirmation;
    private final Runnable mResetErrorRunnable;
    private final Runnable mResetHelpRunnable;
    protected Bundle mSavedState;
    /* access modifiers changed from: private */
    public int mSize;
    protected int mState;
    /* access modifiers changed from: private */
    public TextView mSubtitleView;
    protected final int mTextColorError;
    protected final int mTextColorHint;
    /* access modifiers changed from: private */
    public TextView mTitleView;
    Button mTryAgainButton;
    Button mUseCredentialButton;
    private int mUserId;

    @Retention(RetentionPolicy.SOURCE)
    @interface BiometricState {
    }

    interface Callback {
        public static final int ACTION_AUTHENTICATED = 1;
        public static final int ACTION_BUTTON_NEGATIVE = 3;
        public static final int ACTION_BUTTON_TRY_AGAIN = 4;
        public static final int ACTION_ERROR = 5;
        public static final int ACTION_USER_CANCELED = 2;
        public static final int ACTION_USE_DEVICE_CREDENTIAL = 6;

        void onAction(int i);
    }

    /* access modifiers changed from: protected */
    public boolean forceRequireConfirmation(int i) {
        return false;
    }

    /* access modifiers changed from: protected */
    public int getConfirmationPrompt() {
        return C1893R.string.biometric_dialog_tap_confirm;
    }

    /* access modifiers changed from: protected */
    public int getDelayAfterAuthenticatedDurationMs() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getStateForAfterError() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterError() {
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterHelp() {
    }

    /* access modifiers changed from: protected */
    public boolean ignoreUnsuccessfulEventsFrom(int i) {
        return false;
    }

    public boolean onPointerDown(Set<Integer> set) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean supportsManualRetry() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean supportsRequireConfirmation() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean supportsSmallDialog() {
        return false;
    }

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

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-biometrics-AuthBiometricView  reason: not valid java name */
    public /* synthetic */ void m2552lambda$new$0$comandroidsystemuibiometricsAuthBiometricView(View view) {
        if (this.mState == 6) {
            Log.w(TAG, "Ignoring background click after authenticated");
            return;
        }
        int i = this.mSize;
        if (i == 1) {
            Log.w(TAG, "Ignoring background click during small dialog");
        } else if (i == 3) {
            Log.w(TAG, "Ignoring background click during large dialog");
        } else {
            this.mCallback.onAction(2);
        }
    }

    public AuthBiometricView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AuthBiometricView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSize = 0;
        this.mAnimationDurationShort = 150;
        this.mAnimationDurationLong = 450;
        this.mAnimationDurationHideDialog = 2000;
        this.mBackgroundClickListener = new AuthBiometricView$$ExternalSyntheticLambda3(this);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mTextColorError = getResources().getColor(C1893R.C1894color.biometric_dialog_error, context.getTheme());
        this.mTextColorHint = getResources().getColor(C1893R.C1894color.biometric_dialog_gray, context.getTheme());
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mResetErrorRunnable = new AuthBiometricView$$ExternalSyntheticLambda4(this);
        this.mResetHelpRunnable = new AuthBiometricView$$ExternalSyntheticLambda5(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-biometrics-AuthBiometricView  reason: not valid java name */
    public /* synthetic */ void m2553lambda$new$1$comandroidsystemuibiometricsAuthBiometricView() {
        updateState(getStateForAfterError());
        handleResetAfterError();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-biometrics-AuthBiometricView  reason: not valid java name */
    public /* synthetic */ void m2554lambda$new$2$comandroidsystemuibiometricsAuthBiometricView() {
        updateState(2);
        handleResetAfterHelp();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: protected */
    public AuthIconController createIconController() {
        return new AuthIconController(this.mContext, this.mIconView) {
            public void updateIcon(int i, int i2) {
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void setPanelController(AuthPanelController authPanelController) {
        this.mPanelController = authPanelController;
    }

    /* access modifiers changed from: package-private */
    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    /* access modifiers changed from: package-private */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundView(View view) {
        view.setOnClickListener(this.mBackgroundClickListener);
    }

    /* access modifiers changed from: package-private */
    public void setUserId(int i) {
        this.mUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void setRequireConfirmation(boolean z) {
        this.mRequireConfirmation = z && supportsRequireConfirmation();
    }

    /* access modifiers changed from: package-private */
    public final void updateSize(final int i) {
        Log.v(TAG, "Current size: " + this.mSize + " New size: " + i);
        if (i == 1) {
            this.mTitleView.setVisibility(8);
            this.mSubtitleView.setVisibility(8);
            this.mDescriptionView.setVisibility(8);
            this.mIndicatorView.setVisibility(8);
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setVisibility(8);
            float dimension = getResources().getDimension(C1893R.dimen.biometric_dialog_icon_padding);
            this.mIconHolderView.setY(((float) (getHeight() - this.mIconHolderView.getHeight())) - dimension);
            this.mPanelController.updateForContentDimensions(this.mLayoutParams.mMediumWidth, ((this.mIconHolderView.getHeight() + (((int) dimension) * 2)) - this.mIconHolderView.getPaddingTop()) - this.mIconHolderView.getPaddingBottom(), 0);
            this.mSize = i;
        } else if (this.mSize == 1 && i == 2) {
            if (!this.mDialogSizeAnimating) {
                this.mDialogSizeAnimating = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mIconHolderView.getY(), this.mIconOriginalY});
                ofFloat.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda0(this));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat2.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda6(this));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration((long) this.mAnimationDurationShort);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        AuthBiometricView.this.mTitleView.setVisibility(0);
                        AuthBiometricView.this.mIndicatorView.setVisibility(0);
                        if (AuthBiometricView.this.isDeviceCredentialAllowed()) {
                            AuthBiometricView.this.mUseCredentialButton.setVisibility(0);
                        } else {
                            AuthBiometricView.this.mNegativeButton.setVisibility(0);
                        }
                        if (AuthBiometricView.this.supportsManualRetry()) {
                            AuthBiometricView.this.mTryAgainButton.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mSubtitleView.getText())) {
                            AuthBiometricView.this.mSubtitleView.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mDescriptionView.getText())) {
                            AuthBiometricView.this.mDescriptionView.setVisibility(0);
                        }
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        int unused = AuthBiometricView.this.mSize = i;
                        AuthBiometricView.this.mDialogSizeAnimating = false;
                        Utils.notifyAccessibilityContentChanged(AuthBiometricView.this.mAccessibilityManager, AuthBiometricView.this);
                    }
                });
                animatorSet.play(ofFloat).with(ofFloat2);
                animatorSet.start();
                this.mPanelController.updateForContentDimensions(this.mLayoutParams.mMediumWidth, this.mLayoutParams.mMediumHeight, 150);
            } else {
                return;
            }
        } else if (i == 2) {
            this.mPanelController.updateForContentDimensions(this.mLayoutParams.mMediumWidth, this.mLayoutParams.mMediumHeight, 0);
            this.mSize = i;
        } else if (i == 3) {
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{getY(), getY() - getResources().getDimension(C1893R.dimen.biometric_dialog_medium_to_large_translation_offset)});
            ofFloat3.setDuration((long) this.mAnimationDurationLong);
            ofFloat3.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda7(this));
            ofFloat3.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (this.getParent() instanceof ViewGroup) {
                        ((ViewGroup) this.getParent()).removeView(this);
                    }
                    int unused = AuthBiometricView.this.mSize = i;
                }
            });
            ValueAnimator ofFloat4 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat4.setDuration((long) (this.mAnimationDurationLong / 2));
            ofFloat4.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda8(this));
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController = this.mPanelController;
            authPanelController.updateForContentDimensions(authPanelController.getContainerWidth(), this.mPanelController.getContainerHeight(), this.mAnimationDurationLong);
            AnimatorSet animatorSet2 = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            arrayList.add(ofFloat3);
            arrayList.add(ofFloat4);
            animatorSet2.playTogether(arrayList);
            animatorSet2.setDuration((long) ((this.mAnimationDurationLong * 2) / 3));
            animatorSet2.start();
        } else {
            Log.e(TAG, "Unknown transition from: " + this.mSize + " to: " + i);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateSize$3$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30514xd6d3b775(ValueAnimator valueAnimator) {
        this.mIconHolderView.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateSize$4$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30515x1a5ed536(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mTitleView.setAlpha(floatValue);
        this.mIndicatorView.setAlpha(floatValue);
        this.mNegativeButton.setAlpha(floatValue);
        this.mCancelButton.setAlpha(floatValue);
        this.mTryAgainButton.setAlpha(floatValue);
        if (!TextUtils.isEmpty(this.mSubtitleView.getText())) {
            this.mSubtitleView.setAlpha(floatValue);
        }
        if (!TextUtils.isEmpty(this.mDescriptionView.getText())) {
            this.mDescriptionView.setAlpha(floatValue);
        }
    }

    public void updateState(int i) {
        Log.v(TAG, "newState: " + i);
        this.mIconController.updateState(this.mState, i);
        if (i == 1 || i == 2) {
            removePendingAnimations();
            if (this.mRequireConfirmation) {
                this.mConfirmButton.setEnabled(false);
                this.mConfirmButton.setVisibility(0);
            }
        } else if (i != 4) {
            int i2 = 8;
            if (i == 5) {
                removePendingAnimations();
                this.mNegativeButton.setVisibility(8);
                this.mCancelButton.setVisibility(0);
                this.mUseCredentialButton.setVisibility(8);
                this.mConfirmButton.setEnabled(this.mRequireConfirmation);
                Button button = this.mConfirmButton;
                if (this.mRequireConfirmation) {
                    i2 = 0;
                }
                button.setVisibility(i2);
                this.mIndicatorView.setTextColor(this.mTextColorHint);
                this.mIndicatorView.setText(getConfirmationPrompt());
                this.mIndicatorView.setVisibility(0);
                ImageView imageView = this.mIndicatorImg;
                if (imageView != null) {
                    imageView.setVisibility(4);
                }
            } else if (i != 6) {
                Log.w(TAG, "Unhandled state: " + i);
            } else {
                if (this.mSize != 1) {
                    this.mConfirmButton.setVisibility(8);
                    this.mNegativeButton.setVisibility(8);
                    this.mUseCredentialButton.setVisibility(8);
                    this.mCancelButton.setVisibility(8);
                    this.mIndicatorView.setVisibility(4);
                    ImageView imageView2 = this.mIndicatorImg;
                    if (imageView2 != null) {
                        imageView2.setVisibility(4);
                    }
                }
                announceForAccessibility(getResources().getString(C1893R.string.biometric_dialog_authenticated));
                this.mHandler.postDelayed(new AuthBiometricView$$ExternalSyntheticLambda2(this), (long) getDelayAfterAuthenticatedDurationMs());
            }
        } else if (this.mSize == 1) {
            updateSize(2);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
        this.mState = i;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateState$7$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30516x2a8e0afd() {
        this.mCallback.onAction(1);
    }

    public void onDialogAnimatedIn() {
        updateState(2);
    }

    public void onAuthenticationSucceeded(int i) {
        removePendingAnimations();
        if (this.mRequireConfirmation || forceRequireConfirmation(i)) {
            updateState(5);
        } else {
            updateState(6);
        }
    }

    public void onAuthenticationFailed(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            showTemporaryMessage(str, this.mResetErrorRunnable);
            updateState(4);
        }
    }

    public void onError(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            showTemporaryMessage(str, this.mResetErrorRunnable);
            updateState(4);
            this.mHandler.postDelayed(new AuthBiometricView$$ExternalSyntheticLambda1(this), (long) this.mAnimationDurationHideDialog);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onError$8$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30507xf33c09df() {
        this.mCallback.onAction(5);
    }

    public void onHelp(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            if (this.mSize != 2) {
                Log.w(TAG, "Help received in size: " + this.mSize);
            } else if (TextUtils.isEmpty(str)) {
                Log.w(TAG, "Ignoring blank help message");
            } else {
                showTemporaryMessage(str, this.mResetHelpRunnable);
                updateState(3);
            }
        }
    }

    public void onSaveState(Bundle bundle) {
        bundle.putInt(AuthDialog.KEY_BIOMETRIC_CONFIRM_VISIBILITY, this.mConfirmButton.getVisibility());
        bundle.putInt(AuthDialog.KEY_BIOMETRIC_TRY_AGAIN_VISIBILITY, this.mTryAgainButton.getVisibility());
        bundle.putInt(AuthDialog.KEY_BIOMETRIC_STATE, this.mState);
        bundle.putString(AuthDialog.KEY_BIOMETRIC_INDICATOR_STRING, this.mIndicatorView.getText() != null ? this.mIndicatorView.getText().toString() : "");
        bundle.putBoolean(AuthDialog.KEY_BIOMETRIC_INDICATOR_ERROR_SHOWING, this.mHandler.hasCallbacks(this.mResetErrorRunnable));
        bundle.putBoolean(AuthDialog.KEY_BIOMETRIC_INDICATOR_HELP_SHOWING, this.mHandler.hasCallbacks(this.mResetHelpRunnable));
        bundle.putInt(AuthDialog.KEY_BIOMETRIC_DIALOG_SIZE, this.mSize);
    }

    public void restoreState(Bundle bundle) {
        this.mSavedState = bundle;
    }

    private void setTextOrHide(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setText(charSequence);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    private void removePendingAnimations() {
        this.mHandler.removeCallbacks(this.mResetHelpRunnable);
        this.mHandler.removeCallbacks(this.mResetErrorRunnable);
    }

    private void showTemporaryMessage(String str, Runnable runnable) {
        removePendingAnimations();
        this.mIndicatorView.setText(str);
        this.mIndicatorView.setTextColor(this.mTextColorError);
        ImageView imageView = this.mIndicatorImg;
        if (imageView != null) {
            imageView.setVisibility(4);
        }
        boolean z = false;
        this.mIndicatorView.setVisibility(0);
        TextView textView = this.mIndicatorView;
        if (!this.mAccessibilityManager.isEnabled() || !this.mAccessibilityManager.isTouchExplorationEnabled()) {
            z = true;
        }
        textView.setSelected(z);
        this.mHandler.postDelayed(runnable, (long) this.mAnimationDurationHideDialog);
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(C1893R.C1897id.title);
        this.mSubtitleView = (TextView) findViewById(C1893R.C1897id.subtitle);
        this.mDescriptionView = (TextView) findViewById(C1893R.C1897id.description);
        this.mIconView = (ImageView) findViewById(C1893R.C1897id.biometric_icon);
        this.mIconHolderView = findViewById(C1893R.C1897id.biometric_icon_frame);
        this.mIndicatorView = (TextView) findViewById(C1893R.C1897id.indicator);
        this.mIndicatorImg = (ImageView) findViewById(C1893R.C1897id.indicator_icon);
        this.mNegativeButton = (Button) findViewById(C1893R.C1897id.button_negative);
        this.mCancelButton = (Button) findViewById(C1893R.C1897id.button_cancel);
        this.mUseCredentialButton = (Button) findViewById(C1893R.C1897id.button_use_credential);
        this.mConfirmButton = (Button) findViewById(C1893R.C1897id.button_confirm);
        this.mTryAgainButton = (Button) findViewById(C1893R.C1897id.button_try_again);
        this.mNegativeButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda9(this));
        this.mCancelButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda10(this));
        this.mUseCredentialButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda11(this));
        this.mConfirmButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda12(this));
        this.mTryAgainButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda13(this));
        AuthIconController createIconController = createIconController();
        this.mIconController = createIconController;
        if (createIconController.getActsAsConfirmButton()) {
            this.mIconView.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda14(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$9$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30513xc510558c(View view) {
        this.mCallback.onAction(3);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$10$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30508x466deca(View view) {
        this.mCallback.onAction(2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$11$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30509x47f1fc8b(View view) {
        startTransitionToCredentialUI();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$12$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30510x8b7d1a4c(View view) {
        updateState(6);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$13$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30511xcf08380d(View view) {
        updateState(2);
        this.mCallback.onAction(4);
        this.mTryAgainButton.setVisibility(8);
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$14$com-android-systemui-biometrics-AuthBiometricView */
    public /* synthetic */ void mo30512x129355ce(View view) {
        if (this.mState == 5) {
            updateState(6);
        }
    }

    /* access modifiers changed from: package-private */
    public void startTransitionToCredentialUI() {
        updateSize(3);
        this.mCallback.onAction(6);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        String str;
        super.onAttachedToWindow();
        this.mTitleView.setText(this.mPromptInfo.getTitle());
        this.mTitleView.setSelected(true);
        this.mSubtitleView.setSelected(true);
        this.mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
        if (isDeviceCredentialAllowed()) {
            int credentialType = Utils.getCredentialType(this.mLockPatternUtils, this.mEffectiveUserId);
            if (credentialType != 1) {
                str = credentialType != 2 ? getResources().getString(C1893R.string.biometric_dialog_use_password) : getResources().getString(C1893R.string.biometric_dialog_use_pattern);
            } else {
                str = getResources().getString(C1893R.string.biometric_dialog_use_pin);
            }
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setText(str);
            this.mUseCredentialButton.setVisibility(0);
        } else {
            this.mNegativeButton.setText(this.mPromptInfo.getNegativeButtonText());
        }
        setTextOrHide(this.mSubtitleView, this.mPromptInfo.getSubtitle());
        setTextOrHide(this.mDescriptionView, this.mPromptInfo.getDescription());
        Bundle bundle = this.mSavedState;
        if (bundle == null) {
            updateState(1);
            return;
        }
        updateState(bundle.getInt(AuthDialog.KEY_BIOMETRIC_STATE));
        this.mConfirmButton.setVisibility(this.mSavedState.getInt(AuthDialog.KEY_BIOMETRIC_CONFIRM_VISIBILITY));
        if (this.mConfirmButton.getVisibility() == 8) {
            setRequireConfirmation(false);
        }
        this.mTryAgainButton.setVisibility(this.mSavedState.getInt(AuthDialog.KEY_BIOMETRIC_TRY_AGAIN_VISIBILITY));
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIconController.setDeactivated(true);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    /* access modifiers changed from: package-private */
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getId() == C1893R.C1897id.space_above_icon || childAt.getId() == C1893R.C1897id.space_below_icon || childAt.getId() == C1893R.C1897id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == C1893R.C1897id.biometric_icon_frame) {
                View findViewById = findViewById(C1893R.C1897id.biometric_icon);
                childAt.measure(View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == C1893R.C1897id.biometric_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            }
            if (childAt.getVisibility() != 8) {
                i3 += childAt.getMeasuredHeight();
            }
        }
        return new AuthDialog.LayoutParams(i, i3);
    }

    private boolean isLargeDisplay() {
        return LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (isLargeDisplay()) {
            i3 = (Math.min(size, size2) * 2) / 3;
        } else {
            i3 = Math.min(size, size2);
        }
        AuthDialog.LayoutParams onMeasureInternal = onMeasureInternal(i3, size2);
        this.mLayoutParams = onMeasureInternal;
        setMeasuredDimension(onMeasureInternal.mMediumWidth, this.mLayoutParams.mMediumHeight);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mIconOriginalY == 0.0f) {
            this.mIconOriginalY = this.mIconHolderView.getY();
            Bundle bundle = this.mSavedState;
            if (bundle == null) {
                updateSize((this.mRequireConfirmation || !supportsSmallDialog()) ? 2 : 1);
                return;
            }
            updateSize(bundle.getInt(AuthDialog.KEY_BIOMETRIC_DIALOG_SIZE));
            String string = this.mSavedState.getString(AuthDialog.KEY_BIOMETRIC_INDICATOR_STRING);
            if (this.mSavedState.getBoolean(AuthDialog.KEY_BIOMETRIC_INDICATOR_HELP_SHOWING)) {
                onHelp(0, string);
            } else if (this.mSavedState.getBoolean(AuthDialog.KEY_BIOMETRIC_INDICATOR_ERROR_SHOWING)) {
                onAuthenticationFailed(0, string);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceCredentialAllowed() {
        return Utils.isDeviceCredentialAllowed(this.mPromptInfo);
    }

    /* access modifiers changed from: package-private */
    public int getSize() {
        return this.mSize;
    }
}
