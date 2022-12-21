package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.AuthCredentialView;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthContainerView extends LinearLayout implements AuthDialog, WakefulnessLifecycle.Observer {
    private static final int ANIMATION_DURATION_AWAY_MS = 350;
    private static final int ANIMATION_DURATION_SHOW_MS = 250;
    private static final int STATE_ANIMATING_IN = 1;
    private static final int STATE_ANIMATING_OUT = 4;
    private static final int STATE_GONE = 5;
    private static final int STATE_PENDING_DISMISS = 2;
    private static final int STATE_SHOWING = 3;
    private static final int STATE_UNKNOWN = 0;
    private static final String TAG = "AuthContainerView";
    @Background
    private final DelayableExecutor mBackgroundExecutor;
    private final ImageView mBackgroundView;
    final BiometricCallback mBiometricCallback;
    private final ScrollView mBiometricScrollView;
    private AuthBiometricView mBiometricView;
    /* access modifiers changed from: private */
    public final Config mConfig;
    private int mContainerState = 0;
    /* access modifiers changed from: private */
    public byte[] mCredentialAttestation;
    private final CredentialCallback mCredentialCallback;
    private AuthCredentialView mCredentialView;
    private final int mEffectiveUserId;
    /* access modifiers changed from: private */
    public final Set<Integer> mFailedModalities = new HashSet();
    private final FrameLayout mFrameLayout;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final Interpolator mLinearOutSlowIn;
    private final LockPatternUtils mLockPatternUtils;
    private final AuthPanelController mPanelController;
    private final View mPanelView;
    private Integer mPendingCallbackReason;
    private Bundle mSavedState;
    private final float mTranslationY;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WindowManager mWindowManager;
    private final IBinder mWindowToken = new Binder();

    @Retention(RetentionPolicy.SOURCE)
    private @interface ContainerState {
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

    static class Config {
        AuthDialogCallback mCallback;
        Context mContext;
        int mMultiSensorConfig = 0;
        String mOpPackageName;
        long mOperationId;
        PromptInfo mPromptInfo;
        long mRequestId = -1;
        boolean mRequireConfirmation;
        int[] mSensorIds;
        boolean mSkipAnimation = false;
        boolean mSkipIntro;
        int mUserId;

        Config() {
        }
    }

    public static class Builder {
        Config mConfig;

        public Builder(Context context) {
            Config config = new Config();
            this.mConfig = config;
            config.mContext = context;
        }

        public Builder setCallback(AuthDialogCallback authDialogCallback) {
            this.mConfig.mCallback = authDialogCallback;
            return this;
        }

        public Builder setPromptInfo(PromptInfo promptInfo) {
            this.mConfig.mPromptInfo = promptInfo;
            return this;
        }

        public Builder setRequireConfirmation(boolean z) {
            this.mConfig.mRequireConfirmation = z;
            return this;
        }

        public Builder setUserId(int i) {
            this.mConfig.mUserId = i;
            return this;
        }

        public Builder setOpPackageName(String str) {
            this.mConfig.mOpPackageName = str;
            return this;
        }

        public Builder setSkipIntro(boolean z) {
            this.mConfig.mSkipIntro = z;
            return this;
        }

        public Builder setOperationId(long j) {
            this.mConfig.mOperationId = j;
            return this;
        }

        public Builder setRequestId(long j) {
            this.mConfig.mRequestId = j;
            return this;
        }

        public Builder setSkipAnimationDuration(boolean z) {
            this.mConfig.mSkipAnimation = z;
            return this;
        }

        public Builder setMultiSensorConfig(int i) {
            this.mConfig.mMultiSensorConfig = i;
            return this;
        }

        public AuthContainerView build(@Background DelayableExecutor delayableExecutor, int[] iArr, List<FingerprintSensorPropertiesInternal> list, List<FaceSensorPropertiesInternal> list2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils) {
            this.mConfig.mSensorIds = iArr;
            return new AuthContainerView(this.mConfig, list, list2, wakefulnessLifecycle, userManager, lockPatternUtils, new Handler(Looper.getMainLooper()), delayableExecutor);
        }
    }

    final class BiometricCallback implements AuthBiometricView.Callback {
        BiometricCallback() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAction$0$com-android-systemui-biometrics-AuthContainerView$BiometricCallback */
        public /* synthetic */ void mo30579x4d57019f() {
            AuthContainerView.this.addCredentialView(false, true);
        }

        public void onAction(int i) {
            switch (i) {
                case 1:
                    AuthContainerView.this.animateAway(4);
                    return;
                case 2:
                    AuthContainerView.this.sendEarlyUserCanceled();
                    AuthContainerView.this.animateAway(1);
                    return;
                case 3:
                    AuthContainerView.this.animateAway(2);
                    return;
                case 4:
                    AuthContainerView.this.mFailedModalities.clear();
                    AuthContainerView.this.mConfig.mCallback.onTryAgainPressed();
                    return;
                case 5:
                    AuthContainerView.this.animateAway(5);
                    return;
                case 6:
                    AuthContainerView.this.mConfig.mCallback.onDeviceCredentialPressed();
                    AuthContainerView.this.mHandler.postDelayed(new AuthContainerView$BiometricCallback$$ExternalSyntheticLambda0(this), AuthContainerView.this.mConfig.mSkipAnimation ? 0 : 300);
                    return;
                default:
                    Log.e(AuthContainerView.TAG, "Unhandled action: " + i);
                    return;
            }
        }
    }

    final class CredentialCallback implements AuthCredentialView.Callback {
        CredentialCallback() {
        }

        public void onCredentialMatched(byte[] bArr) {
            byte[] unused = AuthContainerView.this.mCredentialAttestation = bArr;
            AuthContainerView.this.animateAway(7);
        }
    }

    AuthContainerView(Config config, List<FingerprintSensorPropertiesInternal> list, List<FaceSensorPropertiesInternal> list2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils, Handler handler, @Background DelayableExecutor delayableExecutor) {
        super(config.mContext);
        this.mConfig = config;
        this.mLockPatternUtils = lockPatternUtils;
        int credentialOwnerProfile = userManager.getCredentialOwnerProfile(config.mUserId);
        this.mEffectiveUserId = credentialOwnerProfile;
        this.mHandler = handler;
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mTranslationY = getResources().getDimension(C1893R.dimen.biometric_dialog_animation_translation_offset);
        this.mLinearOutSlowIn = Interpolators.LINEAR_OUT_SLOW_IN;
        BiometricCallback biometricCallback = new BiometricCallback();
        this.mBiometricCallback = biometricCallback;
        this.mCredentialCallback = new CredentialCallback();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        FrameLayout frameLayout = (FrameLayout) from.inflate(C1893R.layout.auth_container_view, this, false);
        this.mFrameLayout = frameLayout;
        addView(frameLayout);
        this.mBiometricScrollView = (ScrollView) frameLayout.findViewById(C1893R.C1897id.biometric_scrollview);
        ImageView imageView = (ImageView) frameLayout.findViewById(C1893R.C1897id.background);
        this.mBackgroundView = imageView;
        View findViewById = frameLayout.findViewById(C1893R.C1897id.panel);
        this.mPanelView = findViewById;
        AuthPanelController authPanelController = new AuthPanelController(this.mContext, findViewById);
        this.mPanelController = authPanelController;
        this.mBackgroundExecutor = delayableExecutor;
        if (Utils.isBiometricAllowed(config.mPromptInfo)) {
            FingerprintSensorPropertiesInternal findFirstSensorProperties = Utils.findFirstSensorProperties(list, config.mSensorIds);
            FaceSensorPropertiesInternal findFirstSensorProperties2 = Utils.findFirstSensorProperties(list2, config.mSensorIds);
            if (findFirstSensorProperties != null && findFirstSensorProperties2 != null) {
                AuthBiometricFingerprintAndFaceView authBiometricFingerprintAndFaceView = (AuthBiometricFingerprintAndFaceView) from.inflate(C1893R.layout.auth_biometric_fingerprint_and_face_view, (ViewGroup) null, false);
                authBiometricFingerprintAndFaceView.setSensorProperties(findFirstSensorProperties);
                this.mBiometricView = authBiometricFingerprintAndFaceView;
            } else if (findFirstSensorProperties != null) {
                AuthBiometricFingerprintView authBiometricFingerprintView = (AuthBiometricFingerprintView) from.inflate(C1893R.layout.auth_biometric_fingerprint_view, (ViewGroup) null, false);
                authBiometricFingerprintView.setSensorProperties(findFirstSensorProperties);
                this.mBiometricView = authBiometricFingerprintView;
            } else if (findFirstSensorProperties2 != null) {
                this.mBiometricView = (AuthBiometricFaceView) from.inflate(C1893R.layout.auth_biometric_face_view, (ViewGroup) null, false);
            } else {
                Log.e(TAG, "No sensors found!");
            }
        }
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.setRequireConfirmation(config.mRequireConfirmation);
            this.mBiometricView.setPanelController(authPanelController);
            this.mBiometricView.setPromptInfo(config.mPromptInfo);
            this.mBiometricView.setCallback(biometricCallback);
            this.mBiometricView.setBackgroundView(imageView);
            this.mBiometricView.setUserId(config.mUserId);
            this.mBiometricView.setEffectiveUserId(credentialOwnerProfile);
        }
        setOnKeyListener(new AuthContainerView$$ExternalSyntheticLambda0(this));
        setImportantForAccessibility(2);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-biometrics-AuthContainerView  reason: not valid java name */
    public /* synthetic */ boolean m2556lambda$new$0$comandroidsystemuibiometricsAuthContainerView(View view, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        if (keyEvent.getAction() == 1) {
            sendEarlyUserCanceled();
            animateAway(1);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void sendEarlyUserCanceled() {
        this.mConfig.mCallback.onSystemEvent(1);
    }

    public boolean isAllowDeviceCredentials() {
        return Utils.isDeviceCredentialAllowed(this.mConfig.mPromptInfo);
    }

    /* access modifiers changed from: private */
    public void addCredentialView(boolean z, boolean z2) {
        LayoutInflater from = LayoutInflater.from(this.mContext);
        int credentialType = Utils.getCredentialType(this.mLockPatternUtils, this.mEffectiveUserId);
        if (credentialType != 1) {
            if (credentialType == 2) {
                this.mCredentialView = (AuthCredentialView) from.inflate(C1893R.layout.auth_credential_pattern_view, (ViewGroup) null, false);
                this.mBackgroundView.setOnClickListener((View.OnClickListener) null);
                this.mBackgroundView.setImportantForAccessibility(2);
                this.mCredentialView.setContainerView(this);
                this.mCredentialView.setUserId(this.mConfig.mUserId);
                this.mCredentialView.setOperationId(this.mConfig.mOperationId);
                this.mCredentialView.setEffectiveUserId(this.mEffectiveUserId);
                this.mCredentialView.setCredentialType(credentialType);
                this.mCredentialView.setCallback(this.mCredentialCallback);
                this.mCredentialView.setPromptInfo(this.mConfig.mPromptInfo);
                this.mCredentialView.setPanelController(this.mPanelController, z);
                this.mCredentialView.setShouldAnimateContents(z2);
                this.mCredentialView.setBackgroundExecutor(this.mBackgroundExecutor);
                this.mFrameLayout.addView(this.mCredentialView);
            } else if (credentialType != 3) {
                throw new IllegalStateException("Unknown credential type: " + credentialType);
            }
        }
        this.mCredentialView = (AuthCredentialView) from.inflate(C1893R.layout.auth_credential_password_view, (ViewGroup) null, false);
        this.mBackgroundView.setOnClickListener((View.OnClickListener) null);
        this.mBackgroundView.setImportantForAccessibility(2);
        this.mCredentialView.setContainerView(this);
        this.mCredentialView.setUserId(this.mConfig.mUserId);
        this.mCredentialView.setOperationId(this.mConfig.mOperationId);
        this.mCredentialView.setEffectiveUserId(this.mEffectiveUserId);
        this.mCredentialView.setCredentialType(credentialType);
        this.mCredentialView.setCallback(this.mCredentialCallback);
        this.mCredentialView.setPromptInfo(this.mConfig.mPromptInfo);
        this.mCredentialView.setPanelController(this.mPanelController, z);
        this.mCredentialView.setShouldAnimateContents(z2);
        this.mCredentialView.setBackgroundExecutor(this.mBackgroundExecutor);
        this.mFrameLayout.addView(this.mCredentialView);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mPanelController.setContainerDimensions(getMeasuredWidth(), getMeasuredHeight());
    }

    public void onOrientationChanged() {
        maybeUpdatePositionForUdfps(true);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Log.v(TAG, "onWindowFocusChanged " + z);
        if (!z) {
            this.mHandler.postDelayed(new AuthContainerView$$ExternalSyntheticLambda1(this), 100);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onWindowFocusChanged$1$com-android-systemui-biometrics-AuthContainerView */
    public /* synthetic */ void mo30565xc5ea931d() {
        if (!hasWindowFocus()) {
            Log.v(TAG, "Lost window focus, dismissing the dialog");
            animateAway(1);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWakefulnessLifecycle.addObserver(this);
        if (Utils.isBiometricAllowed(this.mConfig.mPromptInfo)) {
            this.mBiometricScrollView.addView(this.mBiometricView);
        } else if (Utils.isDeviceCredentialAllowed(this.mConfig.mPromptInfo)) {
            addCredentialView(true, false);
        } else {
            throw new IllegalStateException("Unknown configuration: " + this.mConfig.mPromptInfo.getAuthenticators());
        }
        maybeUpdatePositionForUdfps(false);
        if (this.mConfig.mSkipIntro) {
            if (!(this.mSavedState == null || this.mConfig.mCallback == null || !this.mSavedState.getBoolean(AuthDialog.KEY_CONTAINER_ANIMATING_IN, false))) {
                Log.d(TAG, "Trigger onDialogAnimatedIn manually because original animation is canceled");
                this.mConfig.mCallback.onDialogAnimatedIn();
            }
            this.mContainerState = 3;
            return;
        }
        this.mContainerState = 1;
        this.mPanelView.setY(this.mTranslationY);
        this.mBiometricScrollView.setY(this.mTranslationY);
        setAlpha(0.0f);
        postOnAnimation(new AuthContainerView$$ExternalSyntheticLambda5(this, this.mConfig.mSkipAnimation ? 0 : 250));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAttachedToWindow$2$com-android-systemui-biometrics-AuthContainerView */
    public /* synthetic */ void mo30564x415a35a1(long j) {
        this.mPanelView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().withEndAction(new AuthContainerView$$ExternalSyntheticLambda4(this)).start();
        this.mBiometricScrollView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        AuthCredentialView authCredentialView = this.mCredentialView;
        if (authCredentialView != null && authCredentialView.isAttachedToWindow()) {
            this.mCredentialView.setY(this.mTranslationY);
            this.mCredentialView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        }
        animate().alpha(1.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
    }

    private static boolean shouldUpdatePositionForUdfps(View view) {
        if (view instanceof AuthBiometricFingerprintView) {
            return ((AuthBiometricFingerprintView) view).isUdfps();
        }
        return false;
    }

    private boolean maybeUpdatePositionForUdfps(boolean z) {
        Display display = getDisplay();
        if (display == null || !shouldUpdatePositionForUdfps(this.mBiometricView)) {
            return false;
        }
        int rotation = display.getRotation();
        if (rotation == 0) {
            this.mPanelController.setPosition(1);
            setScrollViewGravity(81);
        } else if (rotation == 1) {
            this.mPanelController.setPosition(3);
            setScrollViewGravity(21);
        } else if (rotation != 3) {
            Log.e(TAG, "Unsupported display rotation: " + rotation);
            this.mPanelController.setPosition(1);
            setScrollViewGravity(81);
        } else {
            this.mPanelController.setPosition(2);
            setScrollViewGravity(19);
        }
        if (z) {
            this.mPanelView.invalidateOutline();
            this.mBiometricView.requestLayout();
        }
        return true;
    }

    private void setScrollViewGravity(int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mBiometricScrollView.getLayoutParams();
        layoutParams.gravity = i;
        this.mBiometricScrollView.setLayoutParams(layoutParams);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mWakefulnessLifecycle.removeObserver(this);
    }

    public void onStartedGoingToSleep() {
        animateAway(1);
    }

    public void show(WindowManager windowManager, Bundle bundle) {
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.restoreState(bundle);
        }
        this.mSavedState = bundle;
        windowManager.addView(this, getLayoutParams(this.mWindowToken, this.mConfig.mPromptInfo.getTitle()));
    }

    public void dismissWithoutCallback(boolean z) {
        if (z) {
            animateAway(false, 0);
        } else {
            removeWindowIfAttached();
        }
    }

    public void dismissFromSystemServer() {
        animateAway(false, 0);
    }

    public void onAuthenticationSucceeded(int i) {
        this.mBiometricView.onAuthenticationSucceeded(i);
    }

    public void onAuthenticationFailed(int i, String str) {
        this.mFailedModalities.add(Integer.valueOf(i));
        this.mBiometricView.onAuthenticationFailed(i, str);
    }

    public void onHelp(int i, String str) {
        this.mBiometricView.onHelp(i, str);
    }

    public void onError(int i, String str) {
        this.mBiometricView.onError(i, str);
    }

    public void onPointerDown() {
        if (this.mBiometricView.onPointerDown(this.mFailedModalities)) {
            Log.d(TAG, "retrying failed modalities (pointer down)");
            this.mBiometricCallback.onAction(4);
        }
    }

    public void onSaveState(Bundle bundle) {
        boolean z = false;
        bundle.putBoolean(AuthDialog.KEY_CONTAINER_GOING_AWAY, this.mContainerState == 4);
        bundle.putBoolean(AuthDialog.KEY_BIOMETRIC_SHOWING, this.mBiometricView != null && this.mCredentialView == null);
        bundle.putBoolean(AuthDialog.KEY_CREDENTIAL_SHOWING, this.mCredentialView != null);
        if (this.mContainerState == 1) {
            z = true;
        }
        bundle.putBoolean(AuthDialog.KEY_CONTAINER_ANIMATING_IN, z);
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.onSaveState(bundle);
        }
    }

    public String getOpPackageName() {
        return this.mConfig.mOpPackageName;
    }

    public long getRequestId() {
        return this.mConfig.mRequestId;
    }

    public void animateToCredentialUI() {
        this.mBiometricView.startTransitionToCredentialUI();
    }

    /* access modifiers changed from: package-private */
    public void animateAway(int i) {
        animateAway(true, i);
    }

    private void animateAway(boolean z, int i) {
        int i2 = this.mContainerState;
        if (i2 == 1) {
            Log.w(TAG, "startDismiss(): waiting for onDialogAnimatedIn");
            this.mContainerState = 2;
        } else if (i2 == 4) {
            Log.w(TAG, "Already dismissing, sendReason: " + z + " reason: " + i);
        } else {
            this.mContainerState = 4;
            if (z) {
                this.mPendingCallbackReason = Integer.valueOf(i);
            } else {
                this.mPendingCallbackReason = null;
            }
            postOnAnimation(new AuthContainerView$$ExternalSyntheticLambda3(this, this.mConfig.mSkipAnimation ? 0 : 350, new AuthContainerView$$ExternalSyntheticLambda2(this)));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$animateAway$3$com-android-systemui-biometrics-AuthContainerView */
    public /* synthetic */ void mo30561x7e61d1e9() {
        setVisibility(4);
        removeWindowIfAttached();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$animateAway$4$com-android-systemui-biometrics-AuthContainerView */
    public /* synthetic */ void mo30562xc1ecefaa(long j, Runnable runnable) {
        this.mPanelView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().withEndAction(runnable).start();
        this.mBiometricScrollView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        AuthCredentialView authCredentialView = this.mCredentialView;
        if (authCredentialView != null && authCredentialView.isAttachedToWindow()) {
            this.mCredentialView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        }
        animate().alpha(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
    }

    private void sendPendingCallbackIfNotNull() {
        Log.d(TAG, "pendingCallback: " + this.mPendingCallbackReason);
        if (this.mPendingCallbackReason != null) {
            this.mConfig.mCallback.onDismissed(this.mPendingCallbackReason.intValue(), this.mCredentialAttestation);
            this.mPendingCallbackReason = null;
        }
    }

    private void removeWindowIfAttached() {
        sendPendingCallbackIfNotNull();
        if (this.mContainerState != 5) {
            this.mContainerState = 5;
            if (isAttachedToWindow()) {
                this.mWindowManager.removeView(this);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDialogAnimatedIn() {
        int i = this.mContainerState;
        if (i == 2) {
            Log.d(TAG, "onDialogAnimatedIn(): mPendingDismissDialog=true, dismissing now");
            animateAway(1);
        } else if (i == 4 || i == 5) {
            Log.d(TAG, "onDialogAnimatedIn(): ignore, already animating out or gone - state: " + this.mContainerState);
        } else {
            this.mContainerState = 3;
            if (this.mBiometricView != null) {
                this.mConfig.mCallback.onDialogAnimatedIn();
                this.mBiometricView.onDialogAnimatedIn();
            }
        }
    }

    static WindowManager.LayoutParams getLayoutParams(IBinder iBinder, CharSequence charSequence) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2041, 16785408, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(layoutParams.getFitInsetsTypes() & (~WindowInsets.Type.ime()));
        layoutParams.setTitle("BiometricPrompt");
        layoutParams.accessibilityTitle = charSequence;
        layoutParams.token = iBinder;
        layoutParams.screenOrientation = 1;
        return layoutParams;
    }
}
