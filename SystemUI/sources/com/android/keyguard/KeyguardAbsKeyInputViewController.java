package com.android.keyguard;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import android.util.PluralsMessageFormatter;
import android.view.KeyEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardAbsKeyInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.nothing.systemui.util.SystemUIEventUtils;
import java.util.HashMap;

public abstract class KeyguardAbsKeyInputViewController<T extends KeyguardAbsKeyInputView> extends KeyguardInputViewController<T> {
    public static final String NOTHING_PASSWORD_LENGTH = "nothing_password_length";
    public static final String TAG = "KeyguardAbsKeyInputViewController";
    private CountDownTimer mCountdownTimer;
    private boolean mDismissing;
    private final EmergencyButtonController.EmergencyButtonCallback mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() {
        public void onEmergencyButtonClickedWhenInCall() {
            KeyguardAbsKeyInputViewController.this.getKeyguardSecurityCallback().reset();
        }
    };
    private final EmergencyButtonController mEmergencyButtonController;
    private final FalsingCollector mFalsingCollector;
    private final KeyguardAbsKeyInputView.KeyDownListener mKeyDownListener = new KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0(this);
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final LatencyTracker mLatencyTracker;
    private final LockPatternUtils mLockPatternUtils;
    protected KeyguardMessageAreaController mMessageAreaController;
    protected AsyncTask<?, ?, ?> mPendingLockCheck;
    protected boolean mResumed;

    public boolean needsInput() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public abstract void resetState();

    /* access modifiers changed from: protected */
    public boolean shouldLockout(long j) {
        return j != 0;
    }

    /* access modifiers changed from: protected */
    public void startErrorAnimation() {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardAbsKeyInputViewController */
    public /* synthetic */ boolean mo25633x97d92a50(int i, KeyEvent keyEvent) {
        if (i == 0) {
            return false;
        }
        onUserInput();
        return false;
    }

    protected KeyguardAbsKeyInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(t, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        this.mMessageAreaController = factory.create(KeyguardMessageArea.findSecurityMessageDisplay(this.mView));
    }

    public void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        ((KeyguardAbsKeyInputView) this.mView).setKeyDownListener(this.mKeyDownListener);
        this.mEmergencyButtonController.setEmergencyButtonCallback(this.mEmergencyButtonCallback);
    }

    public void reset() {
        this.mDismissing = false;
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(false, false);
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (shouldLockout(lockoutAttemptDeadline)) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            resetState();
        }
    }

    public void reloadColors() {
        super.reloadColors();
        this.mMessageAreaController.reloadColors();
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mMessageAreaController.setNextMessageColor(colorStateList);
        }
        this.mMessageAreaController.setMessage(charSequence);
    }

    /* access modifiers changed from: protected */
    public void handleAttemptLockout(long j) {
        ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil(((double) (j - SystemClock.elapsedRealtime())) / 1000.0d)) * 1000, 1000) {
            public void onTick(long j) {
                HashMap hashMap = new HashMap();
                hashMap.put("count", Integer.valueOf((int) Math.round(((double) j) / 1000.0d)));
                KeyguardAbsKeyInputViewController.this.mMessageAreaController.setMessage((CharSequence) PluralsMessageFormatter.format(((KeyguardAbsKeyInputView) KeyguardAbsKeyInputViewController.this.mView).getResources(), hashMap, C1893R.string.kg_too_many_failed_attempts_countdown));
            }

            public void onFinish() {
                KeyguardAbsKeyInputViewController.this.mMessageAreaController.setMessage((CharSequence) "");
                KeyguardAbsKeyInputViewController.this.resetState();
            }
        }.start();
    }

    /* access modifiers changed from: package-private */
    public void onPasswordChecked(int i, boolean z, int i2, boolean z2) {
        boolean z3 = KeyguardUpdateMonitor.getCurrentUser() == i;
        if (z) {
            this.mLockPatternUtils.sanitizePassword();
            getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
            if (z3) {
                this.mDismissing = true;
                this.mLatencyTracker.onActionStart(11);
                getKeyguardSecurityCallback().dismiss(true, i, getSecurityMode());
            }
            LockscreenCredential enteredCredential = ((KeyguardAbsKeyInputView) this.mView).getEnteredCredential();
            Log.d(TAG, "onPasswordChecked: password size = " + enteredCredential.size());
            if (getPasswordLength() <= 0) {
                setPasswordLength((long) enteredCredential.size());
            }
            SystemUIEventUtils.collectUnLockResults(getContext(), SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 2);
            return;
        }
        if (z2) {
            getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
            if (i2 > 0) {
                handleAttemptLockout(this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
            }
        }
        if (i2 == 0) {
            this.mMessageAreaController.setMessage(((KeyguardAbsKeyInputView) this.mView).getWrongPasswordStringId());
        }
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(true, false);
        SystemUIEventUtils.collectUnLockResults(getContext(), SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_FAIL, 2);
        startErrorAnimation();
    }

    /* access modifiers changed from: protected */
    public void verifyPasswordAndUnlock() {
        if (!this.mDismissing) {
            final LockscreenCredential enteredCredential = ((KeyguardAbsKeyInputView) this.mView).getEnteredCredential();
            ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(false);
            AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
            if (asyncTask != null) {
                asyncTask.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (enteredCredential.size() <= 3) {
                ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(true);
                onPasswordChecked(currentUser, false, 0, false);
                enteredCredential.zeroize();
                return;
            }
            this.mLatencyTracker.onActionStart(3);
            this.mLatencyTracker.onActionStart(4);
            this.mKeyguardUpdateMonitor.setCredentialAttempted();
            this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, enteredCredential, currentUser, new LockPatternChecker.OnCheckCallback() {
                public void onEarlyMatched() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(3);
                    KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, true, 0, true);
                    enteredCredential.zeroize();
                }

                public void onChecked(boolean z, int i) {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    ((KeyguardAbsKeyInputView) KeyguardAbsKeyInputViewController.this.mView).setPasswordEntryInputEnabled(true);
                    KeyguardAbsKeyInputViewController.this.mPendingLockCheck = null;
                    if (!z) {
                        KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, false, i, true);
                    }
                    enteredCredential.zeroize();
                }

                public void onCancelled() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    enteredCredential.zeroize();
                }
            });
        }
    }

    public void showPromptReason(int i) {
        int promptReasonStringRes;
        if (i != 0 && (promptReasonStringRes = ((KeyguardAbsKeyInputView) this.mView).getPromptReasonStringRes(i)) != 0) {
            this.mMessageAreaController.setMessage(promptReasonStringRes);
        }
    }

    /* access modifiers changed from: protected */
    public void onUserInput() {
        this.mFalsingCollector.updateFalseConfidence(FalsingClassifier.Result.passed(0.6d));
        getKeyguardSecurityCallback().userActivity();
        getKeyguardSecurityCallback().onUserInput();
        this.mMessageAreaController.setMessage((CharSequence) "");
        if ((this.mView instanceof KeyguardPINView) && getPasswordLength() > 0) {
            synchronized (this) {
                verifyPasswordAndUnlockAuto();
            }
        }
    }

    public void onResume(int i) {
        View findViewById;
        this.mResumed = true;
        if ((this.mView instanceof KeyguardPINView) && (findViewById = ((KeyguardAbsKeyInputView) this.mView).findViewById(C1893R.C1897id.key_enter)) != null) {
            if (getPasswordLength() > 0) {
                findViewById.setVisibility(4);
            } else {
                findViewById.setVisibility(0);
            }
        }
    }

    public void onPause() {
        this.mResumed = false;
        CountDownTimer countDownTimer = this.mCountdownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mCountdownTimer = null;
        }
        AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mPendingLockCheck = null;
        }
        reset();
    }

    /* access modifiers changed from: protected */
    public void verifyPasswordAndUnlockAuto() {
        if (!this.mDismissing) {
            final LockscreenCredential enteredCredential = ((KeyguardAbsKeyInputView) this.mView).getEnteredCredential();
            if (enteredCredential.size() != 0) {
                final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                int passwordLength = getPasswordLength();
                Log.d(TAG, "pwdSize =: " + passwordLength + " password.size = " + enteredCredential.size());
                if (enteredCredential.size() == passwordLength) {
                    ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(false);
                    AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
                    if (asyncTask != null) {
                        asyncTask.cancel(false);
                    }
                    this.mLatencyTracker.onActionStart(3);
                    this.mLatencyTracker.onActionStart(4);
                    this.mKeyguardUpdateMonitor.setCredentialAttempted();
                    this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, enteredCredential, currentUser, new LockPatternChecker.OnCheckCallback() {
                        public void onEarlyMatched() {
                            KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(3);
                            KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, true, 0, true);
                            enteredCredential.zeroize();
                        }

                        public void onChecked(boolean z, int i) {
                            KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                            ((KeyguardAbsKeyInputView) KeyguardAbsKeyInputViewController.this.mView).setPasswordEntryInputEnabled(true);
                            KeyguardAbsKeyInputViewController.this.mPendingLockCheck = null;
                            if (!z) {
                                KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, false, i, true);
                            }
                            enteredCredential.zeroize();
                        }

                        public void onCancelled() {
                            KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                            enteredCredential.zeroize();
                        }
                    });
                }
            }
        }
    }

    private void setPasswordLength(long j) {
        try {
            ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings")).setLong(NOTHING_PASSWORD_LENGTH, j, KeyguardUpdateMonitor.getCurrentUser());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getPasswordLength() {
        try {
            return (int) ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings")).getLong(NOTHING_PASSWORD_LENGTH, -1, KeyguardUpdateMonitor.getCurrentUser());
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
