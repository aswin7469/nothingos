package com.android.systemui.biometrics;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.PromptInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public abstract class AuthCredentialView extends LinearLayout {
    private static final int ERROR_DURATION_MS = 3000;
    private static final String TAG = "BiometricPrompt/AuthCredentialView";
    static final int USER_TYPE_MANAGED_PROFILE = 2;
    static final int USER_TYPE_PRIMARY = 1;
    static final int USER_TYPE_SECONDARY = 3;
    private final AccessibilityManager mAccessibilityManager = ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class));
    @Background
    protected DelayableExecutor mBackgroundExecutor;
    protected Callback mCallback;
    protected final Runnable mClearErrorRunnable = new Runnable() {
        public void run() {
            if (AuthCredentialView.this.mErrorView != null) {
                AuthCredentialView.this.mErrorView.setText("");
            }
        }
    };
    protected AuthContainerView mContainerView;
    protected int mCredentialType;
    private TextView mDescriptionView;
    private final DevicePolicyManager mDevicePolicyManager = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class));
    protected int mEffectiveUserId;
    protected ErrorTimer mErrorTimer;
    protected TextView mErrorView;
    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    private ImageView mIconView;
    protected final LockPatternUtils mLockPatternUtils = new LockPatternUtils(this.mContext);
    protected long mOperationId;
    private AuthPanelController mPanelController;
    protected AsyncTask<?, ?, ?> mPendingLockCheck;
    private PromptInfo mPromptInfo;
    private boolean mShouldAnimateContents;
    private boolean mShouldAnimatePanel;
    private TextView mSubtitleView;
    private TextView mTitleView;
    protected int mUserId;
    private final UserManager mUserManager = ((UserManager) this.mContext.getSystemService(UserManager.class));

    interface Callback {
        void onCredentialMatched(byte[] bArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface UserType {
    }

    private static String getLastAttemptBeforeWipeProfileUpdatableStringId(int i) {
        return i != 1 ? i != 2 ? "SystemUi.BIOMETRIC_DIALOG_WORK_PASSWORD_LAST_ATTEMPT" : "SystemUi.BIOMETRIC_DIALOG_WORK_PATTERN_LAST_ATTEMPT" : "SystemUi.BIOMETRIC_DIALOG_WORK_PIN_LAST_ATTEMPT";
    }

    private String getNowWipingUpdatableStringId(int i) {
        return i != 2 ? "UNDEFINED" : "SystemUi.BIOMETRIC_DIALOG_WORK_LOCK_FAILED_ATTEMPTS";
    }

    /* access modifiers changed from: protected */
    public void onErrorTimeoutFinish() {
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

    protected static class ErrorTimer extends CountDownTimer {
        private final Context mContext;
        private final TextView mErrorView;

        public ErrorTimer(Context context, long j, long j2, TextView textView) {
            super(j, j2);
            this.mErrorView = textView;
            this.mContext = context;
        }

        public void onTick(long j) {
            this.mErrorView.setText(this.mContext.getString(C1894R.string.biometric_dialog_credential_too_many_attempts, new Object[]{Integer.valueOf((int) (j / 1000))}));
        }

        public void onFinish() {
            TextView textView = this.mErrorView;
            if (textView != null) {
                textView.setText("");
            }
        }
    }

    public AuthCredentialView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void showError(String str) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mClearErrorRunnable);
            this.mHandler.postDelayed(this.mClearErrorRunnable, 3000);
        }
        TextView textView = this.mErrorView;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void setTextOrHide(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setText(charSequence);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    private void setText(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
    }

    /* access modifiers changed from: package-private */
    public void setUserId(int i) {
        this.mUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void setOperationId(long j) {
        this.mOperationId = j;
    }

    /* access modifiers changed from: package-private */
    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void setCredentialType(int i) {
        this.mCredentialType = i;
    }

    /* access modifiers changed from: package-private */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /* access modifiers changed from: package-private */
    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    /* access modifiers changed from: package-private */
    public void setPanelController(AuthPanelController authPanelController, boolean z) {
        this.mPanelController = authPanelController;
        this.mShouldAnimatePanel = z;
    }

    /* access modifiers changed from: package-private */
    public void setShouldAnimateContents(boolean z) {
        this.mShouldAnimateContents = z;
    }

    /* access modifiers changed from: package-private */
    public void setContainerView(AuthContainerView authContainerView) {
        this.mContainerView = authContainerView;
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundExecutor(@Background DelayableExecutor delayableExecutor) {
        this.mBackgroundExecutor = delayableExecutor;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        Drawable drawable;
        super.onAttachedToWindow();
        CharSequence title = getTitle(this.mPromptInfo);
        setText(this.mTitleView, title);
        setTextOrHide(this.mSubtitleView, getSubtitle(this.mPromptInfo));
        setTextOrHide(this.mDescriptionView, getDescription(this.mPromptInfo));
        announceForAccessibility(title);
        if (this.mIconView != null) {
            if (Utils.isManagedProfile(this.mContext, this.mEffectiveUserId)) {
                drawable = getResources().getDrawable(C1894R.C1896drawable.auth_dialog_enterprise, this.mContext.getTheme());
            } else {
                drawable = getResources().getDrawable(C1894R.C1896drawable.auth_dialog_lock, this.mContext.getTheme());
            }
            this.mIconView.setImageDrawable(drawable);
        }
        if (this.mShouldAnimateContents) {
            setTranslationY(getResources().getDimension(C1894R.dimen.biometric_dialog_credential_translation_offset));
            setAlpha(0.0f);
            postOnAnimation(new AuthCredentialView$$ExternalSyntheticLambda4(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAttachedToWindow$0$com-android-systemui-biometrics-AuthCredentialView */
    public /* synthetic */ void mo30669xa3c15763() {
        animate().translationY(0.0f).setDuration(150).alpha(1.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).withLayer().start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ErrorTimer errorTimer = this.mErrorTimer;
        if (errorTimer != null) {
            errorTimer.cancel();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(C1894R.C1898id.title);
        this.mSubtitleView = (TextView) findViewById(C1894R.C1898id.subtitle);
        this.mDescriptionView = (TextView) findViewById(C1894R.C1898id.description);
        this.mIconView = (ImageView) findViewById(C1894R.C1898id.icon);
        this.mErrorView = (TextView) findViewById(C1894R.C1898id.error);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mShouldAnimatePanel) {
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController = this.mPanelController;
            authPanelController.updateForContentDimensions(authPanelController.getContainerWidth(), this.mPanelController.getContainerHeight(), 0);
            this.mShouldAnimatePanel = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        if (verifyCredentialResponse.isMatched()) {
            this.mClearErrorRunnable.run();
            this.mLockPatternUtils.userPresent(this.mEffectiveUserId);
            long gatekeeperPasswordHandle = verifyCredentialResponse.getGatekeeperPasswordHandle();
            this.mCallback.onCredentialMatched(this.mLockPatternUtils.verifyGatekeeperPasswordHandle(gatekeeperPasswordHandle, this.mOperationId, this.mEffectiveUserId).getGatekeeperHAT());
            this.mLockPatternUtils.removeGatekeeperPasswordHandle(gatekeeperPasswordHandle);
        } else if (i > 0) {
            this.mHandler.removeCallbacks(this.mClearErrorRunnable);
            C19732 r0 = new ErrorTimer(this.mContext, this.mLockPatternUtils.setLockoutAttemptDeadline(this.mEffectiveUserId, i) - SystemClock.elapsedRealtime(), 1000, this.mErrorView) {
                public void onFinish() {
                    AuthCredentialView.this.onErrorTimeoutFinish();
                    AuthCredentialView.this.mClearErrorRunnable.run();
                }
            };
            this.mErrorTimer = r0;
            r0.start();
        } else if (!reportFailedAttempt()) {
            int i2 = this.mCredentialType;
            showError(getResources().getString(i2 != 1 ? i2 != 2 ? C1894R.string.biometric_dialog_wrong_password : C1894R.string.biometric_dialog_wrong_pattern : C1894R.string.biometric_dialog_wrong_pin));
        }
    }

    private boolean reportFailedAttempt() {
        boolean updateErrorMessage = updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId) + 1);
        this.mLockPatternUtils.reportFailedPasswordAttempt(this.mEffectiveUserId);
        return updateErrorMessage;
    }

    private boolean updateErrorMessage(int i) {
        int maximumFailedPasswordsForWipe = this.mLockPatternUtils.getMaximumFailedPasswordsForWipe(this.mEffectiveUserId);
        if (maximumFailedPasswordsForWipe <= 0 || i <= 0) {
            return false;
        }
        if (this.mErrorView != null) {
            showError(getResources().getString(C1894R.string.biometric_dialog_credential_attempts_before_wipe, new Object[]{Integer.valueOf(i), Integer.valueOf(maximumFailedPasswordsForWipe)}));
        }
        int i2 = maximumFailedPasswordsForWipe - i;
        if (i2 == 1) {
            showLastAttemptBeforeWipeDialog();
        } else if (i2 <= 0) {
            showNowWipingDialog();
        }
        return true;
    }

    private void showLastAttemptBeforeWipeDialog() {
        this.mBackgroundExecutor.execute(new AuthCredentialView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showLastAttemptBeforeWipeDialog$1$com-android-systemui-biometrics-AuthCredentialView */
    public /* synthetic */ void mo30670x46d71122() {
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(C1894R.string.biometric_dialog_last_attempt_before_wipe_dialog_title).setMessage(getLastAttemptBeforeWipeMessage(getUserTypeForWipe(), this.mCredentialType)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        create.getWindow().setType(2017);
        Handler handler = this.mHandler;
        Objects.requireNonNull(create);
        handler.post(new AuthCredentialView$$ExternalSyntheticLambda5(create));
    }

    private void showNowWipingDialog() {
        this.mBackgroundExecutor.execute(new AuthCredentialView$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showNowWipingDialog$3$com-android-systemui-biometrics-AuthCredentialView */
    public /* synthetic */ void mo30672xefe00c23() {
        AlertDialog create = new AlertDialog.Builder(this.mContext).setMessage(getNowWipingMessage(getUserTypeForWipe())).setPositiveButton(C1894R.string.failed_attempts_now_wiping_dialog_dismiss, (DialogInterface.OnClickListener) null).setOnDismissListener(new AuthCredentialView$$ExternalSyntheticLambda6(this)).create();
        create.getWindow().setType(2017);
        Handler handler = this.mHandler;
        Objects.requireNonNull(create);
        handler.post(new AuthCredentialView$$ExternalSyntheticLambda5(create));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showNowWipingDialog$2$com-android-systemui-biometrics-AuthCredentialView */
    public /* synthetic */ void mo30671xc20771c4(DialogInterface dialogInterface) {
        this.mContainerView.animateAway(5);
    }

    private int getUserTypeForWipe() {
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mDevicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(this.mEffectiveUserId));
        if (userInfo == null || userInfo.isPrimary()) {
            return 1;
        }
        return userInfo.isManagedProfile() ? 2 : 3;
    }

    private String getLastAttemptBeforeWipeMessage(int i, int i2) {
        if (i == 1) {
            return getLastAttemptBeforeWipeDeviceMessage(i2);
        }
        if (i == 2) {
            return getLastAttemptBeforeWipeProfileMessage(i2);
        }
        if (i == 3) {
            return getLastAttemptBeforeWipeUserMessage(i2);
        }
        throw new IllegalArgumentException("Unrecognized user type:" + i);
    }

    private String getLastAttemptBeforeWipeDeviceMessage(int i) {
        if (i == 1) {
            return this.mContext.getString(C1894R.string.biometric_dialog_last_pin_attempt_before_wipe_device);
        }
        if (i != 2) {
            return this.mContext.getString(C1894R.string.biometric_dialog_last_password_attempt_before_wipe_device);
        }
        return this.mContext.getString(C1894R.string.biometric_dialog_last_pattern_attempt_before_wipe_device);
    }

    private String getLastAttemptBeforeWipeProfileMessage(int i) {
        return this.mDevicePolicyManager.getResources().getString(getLastAttemptBeforeWipeProfileUpdatableStringId(i), new AuthCredentialView$$ExternalSyntheticLambda3(this, i));
    }

    /* access modifiers changed from: private */
    /* renamed from: getLastAttemptBeforeWipeProfileDefaultMessage */
    public String mo30667x9489c268(int i) {
        return this.mContext.getString(i != 1 ? i != 2 ? C1894R.string.biometric_dialog_last_password_attempt_before_wipe_profile : C1894R.string.biometric_dialog_last_pattern_attempt_before_wipe_profile : C1894R.string.biometric_dialog_last_pin_attempt_before_wipe_profile);
    }

    private String getLastAttemptBeforeWipeUserMessage(int i) {
        return this.mContext.getString(i != 1 ? i != 2 ? C1894R.string.biometric_dialog_last_password_attempt_before_wipe_user : C1894R.string.biometric_dialog_last_pattern_attempt_before_wipe_user : C1894R.string.biometric_dialog_last_pin_attempt_before_wipe_user);
    }

    private String getNowWipingMessage(int i) {
        return this.mDevicePolicyManager.getResources().getString(getNowWipingUpdatableStringId(i), new AuthCredentialView$$ExternalSyntheticLambda1(this, i));
    }

    /* access modifiers changed from: private */
    /* renamed from: getNowWipingDefaultMessage */
    public String mo30668xf2e2be23(int i) {
        int i2;
        if (i == 1) {
            i2 = C1894R.string.failed_attempts_now_wiping_device;
        } else if (i == 2) {
            i2 = C1894R.string.failed_attempts_now_wiping_profile;
        } else if (i == 3) {
            i2 = C1894R.string.failed_attempts_now_wiping_user;
        } else {
            throw new IllegalArgumentException("Unrecognized user type:" + i);
        }
        return this.mContext.getString(i2);
    }

    private static CharSequence getTitle(PromptInfo promptInfo) {
        CharSequence deviceCredentialTitle = promptInfo.getDeviceCredentialTitle();
        return deviceCredentialTitle != null ? deviceCredentialTitle : promptInfo.getTitle();
    }

    private static CharSequence getSubtitle(PromptInfo promptInfo) {
        CharSequence deviceCredentialSubtitle = promptInfo.getDeviceCredentialSubtitle();
        return deviceCredentialSubtitle != null ? deviceCredentialSubtitle : promptInfo.getSubtitle();
    }

    private static CharSequence getDescription(PromptInfo promptInfo) {
        CharSequence deviceCredentialDescription = promptInfo.getDeviceCredentialDescription();
        return deviceCredentialDescription != null ? deviceCredentialDescription : promptInfo.getDescription();
    }
}
