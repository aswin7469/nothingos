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
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public abstract class AuthCredentialView extends LinearLayout {
    protected Callback mCallback;
    protected AuthContainerView mContainerView;
    protected int mCredentialType;
    private TextView mDescriptionView;
    protected int mEffectiveUserId;
    protected ErrorTimer mErrorTimer;
    protected TextView mErrorView;
    private ImageView mIconView;
    protected long mOperationId;
    private AuthPanelController mPanelController;
    protected AsyncTask<?, ?, ?> mPendingLockCheck;
    private PromptInfo mPromptInfo;
    private boolean mShouldAnimateContents;
    private boolean mShouldAnimatePanel;
    private TextView mSubtitleView;
    private TextView mTitleView;
    protected int mUserId;
    protected final Runnable mClearErrorRunnable = new Runnable() { // from class: com.android.systemui.biometrics.AuthCredentialView.1
        @Override // java.lang.Runnable
        public void run() {
            TextView textView = AuthCredentialView.this.mErrorView;
            if (textView != null) {
                textView.setText("");
            }
        }
    };
    protected final LockPatternUtils mLockPatternUtils = new LockPatternUtils(((LinearLayout) this).mContext);
    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    private final AccessibilityManager mAccessibilityManager = (AccessibilityManager) ((LinearLayout) this).mContext.getSystemService(AccessibilityManager.class);
    private final UserManager mUserManager = (UserManager) ((LinearLayout) this).mContext.getSystemService(UserManager.class);
    private final DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) ((LinearLayout) this).mContext.getSystemService(DevicePolicyManager.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        void onCredentialMatched(byte[] bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onErrorTimeoutFinish() {
    }

    /* loaded from: classes.dex */
    protected static class ErrorTimer extends CountDownTimer {
        private final Context mContext;
        private final TextView mErrorView;

        public ErrorTimer(Context context, long j, long j2, TextView textView) {
            super(j, j2);
            this.mErrorView = textView;
            this.mContext = context;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            this.mErrorView.setText(this.mContext.getString(R$string.biometric_dialog_credential_too_many_attempts, Integer.valueOf((int) (j / 1000))));
        }
    }

    public AuthCredentialView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void showError(String str) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mClearErrorRunnable);
            this.mHandler.postDelayed(this.mClearErrorRunnable, 3000L);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserId(int i) {
        this.mUserId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOperationId(long j) {
        this.mOperationId = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCredentialType(int i) {
        this.mCredentialType = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPanelController(AuthPanelController authPanelController, boolean z) {
        this.mPanelController = authPanelController;
        this.mShouldAnimatePanel = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldAnimateContents(boolean z) {
        this.mShouldAnimateContents = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContainerView(AuthContainerView authContainerView) {
        this.mContainerView = authContainerView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        Drawable drawable;
        super.onAttachedToWindow();
        CharSequence title = getTitle(this.mPromptInfo);
        setText(this.mTitleView, title);
        setTextOrHide(this.mSubtitleView, getSubtitle(this.mPromptInfo));
        setTextOrHide(this.mDescriptionView, getDescription(this.mPromptInfo));
        announceForAccessibility(title);
        if (this.mIconView != null) {
            if (Utils.isManagedProfile(((LinearLayout) this).mContext, this.mEffectiveUserId)) {
                drawable = getResources().getDrawable(R$drawable.auth_dialog_enterprise, ((LinearLayout) this).mContext.getTheme());
            } else {
                drawable = getResources().getDrawable(R$drawable.auth_dialog_lock, ((LinearLayout) this).mContext.getTheme());
            }
            this.mIconView.setImageDrawable(drawable);
        }
        if (this.mShouldAnimateContents) {
            setTranslationY(getResources().getDimension(R$dimen.biometric_dialog_credential_translation_offset));
            setAlpha(0.0f);
            postOnAnimation(new Runnable() { // from class: com.android.systemui.biometrics.AuthCredentialView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AuthCredentialView.this.lambda$onAttachedToWindow$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAttachedToWindow$0() {
        animate().translationY(0.0f).setDuration(150L).alpha(1.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).withLayer().start();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ErrorTimer errorTimer = this.mErrorTimer;
        if (errorTimer != null) {
            errorTimer.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(R$id.title);
        this.mSubtitleView = (TextView) findViewById(R$id.subtitle);
        this.mDescriptionView = (TextView) findViewById(R$id.description);
        this.mIconView = (ImageView) findViewById(R$id.icon);
        this.mErrorView = (TextView) findViewById(R$id.error);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mShouldAnimatePanel) {
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController = this.mPanelController;
            authPanelController.updateForContentDimensions(authPanelController.getContainerWidth(), this.mPanelController.getContainerHeight(), 0);
            this.mShouldAnimatePanel = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        int i2;
        if (verifyCredentialResponse.isMatched()) {
            this.mClearErrorRunnable.run();
            this.mLockPatternUtils.userPresent(this.mEffectiveUserId);
            long gatekeeperPasswordHandle = verifyCredentialResponse.getGatekeeperPasswordHandle();
            this.mCallback.onCredentialMatched(this.mLockPatternUtils.verifyGatekeeperPasswordHandle(gatekeeperPasswordHandle, this.mOperationId, this.mEffectiveUserId).getGatekeeperHAT());
            this.mLockPatternUtils.removeGatekeeperPasswordHandle(gatekeeperPasswordHandle);
        } else if (i > 0) {
            this.mHandler.removeCallbacks(this.mClearErrorRunnable);
            ErrorTimer errorTimer = new ErrorTimer(((LinearLayout) this).mContext, this.mLockPatternUtils.setLockoutAttemptDeadline(this.mEffectiveUserId, i) - SystemClock.elapsedRealtime(), 1000L, this.mErrorView) { // from class: com.android.systemui.biometrics.AuthCredentialView.2
                @Override // android.os.CountDownTimer
                public void onFinish() {
                    AuthCredentialView.this.onErrorTimeoutFinish();
                    AuthCredentialView.this.mClearErrorRunnable.run();
                }
            };
            this.mErrorTimer = errorTimer;
            errorTimer.start();
        } else if (reportFailedAttempt()) {
        } else {
            int i3 = this.mCredentialType;
            if (i3 == 1) {
                i2 = R$string.biometric_dialog_wrong_pin;
            } else if (i3 == 2) {
                i2 = R$string.biometric_dialog_wrong_pattern;
            } else {
                i2 = R$string.biometric_dialog_wrong_password;
            }
            showError(getResources().getString(i2));
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
            showError(getResources().getString(R$string.biometric_dialog_credential_attempts_before_wipe, Integer.valueOf(i), Integer.valueOf(maximumFailedPasswordsForWipe)));
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
        AlertDialog create = new AlertDialog.Builder(((LinearLayout) this).mContext).setTitle(R$string.biometric_dialog_last_attempt_before_wipe_dialog_title).setMessage(getLastAttemptBeforeWipeMessageRes(getUserTypeForWipe(), this.mCredentialType)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        create.getWindow().setType(2017);
        create.show();
    }

    private void showNowWipingDialog() {
        AlertDialog create = new AlertDialog.Builder(((LinearLayout) this).mContext).setMessage(getNowWipingMessageRes(getUserTypeForWipe())).setPositiveButton(R$string.biometric_dialog_now_wiping_dialog_dismiss, (DialogInterface.OnClickListener) null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.biometrics.AuthCredentialView$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AuthCredentialView.this.lambda$showNowWipingDialog$1(dialogInterface);
            }
        }).create();
        create.getWindow().setType(2017);
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNowWipingDialog$1(DialogInterface dialogInterface) {
        this.mContainerView.animateAway(5);
    }

    private int getUserTypeForWipe() {
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mDevicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(this.mEffectiveUserId));
        if (userInfo == null || userInfo.isPrimary()) {
            return 1;
        }
        return userInfo.isManagedProfile() ? 2 : 3;
    }

    private static int getLastAttemptBeforeWipeMessageRes(int i, int i2) {
        if (i != 1) {
            if (i == 2) {
                return getLastAttemptBeforeWipeProfileMessageRes(i2);
            }
            if (i == 3) {
                return getLastAttemptBeforeWipeUserMessageRes(i2);
            }
            throw new IllegalArgumentException("Unrecognized user type:" + i);
        }
        return getLastAttemptBeforeWipeDeviceMessageRes(i2);
    }

    private static int getLastAttemptBeforeWipeDeviceMessageRes(int i) {
        if (i != 1) {
            if (i == 2) {
                return R$string.biometric_dialog_last_pattern_attempt_before_wipe_device;
            }
            return R$string.biometric_dialog_last_password_attempt_before_wipe_device;
        }
        return R$string.biometric_dialog_last_pin_attempt_before_wipe_device;
    }

    private static int getLastAttemptBeforeWipeProfileMessageRes(int i) {
        if (i != 1) {
            if (i == 2) {
                return R$string.biometric_dialog_last_pattern_attempt_before_wipe_profile;
            }
            return R$string.biometric_dialog_last_password_attempt_before_wipe_profile;
        }
        return R$string.biometric_dialog_last_pin_attempt_before_wipe_profile;
    }

    private static int getLastAttemptBeforeWipeUserMessageRes(int i) {
        if (i != 1) {
            if (i == 2) {
                return R$string.biometric_dialog_last_pattern_attempt_before_wipe_user;
            }
            return R$string.biometric_dialog_last_password_attempt_before_wipe_user;
        }
        return R$string.biometric_dialog_last_pin_attempt_before_wipe_user;
    }

    private static int getNowWipingMessageRes(int i) {
        if (i != 1) {
            if (i == 2) {
                return R$string.biometric_dialog_failed_attempts_now_wiping_profile;
            }
            if (i == 3) {
                return R$string.biometric_dialog_failed_attempts_now_wiping_user;
            }
            throw new IllegalArgumentException("Unrecognized user type:" + i);
        }
        return R$string.biometric_dialog_failed_attempts_now_wiping_device;
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