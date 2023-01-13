package com.android.keyguard;

import android.app.StatsManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.PluralsMessageFormatter;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.nothing.systemui.util.SystemUIEventUtils;
import java.util.HashMap;
import java.util.List;

public class KeyguardPatternViewController extends KeyguardInputViewController<KeyguardPatternView> {
    private static final int MIN_PATTERN_BEFORE_POKE_WAKELOCK = 2;
    private static final int PATTERN_CLEAR_TIMEOUT_MS = 2000;
    /* access modifiers changed from: private */
    public Runnable mCancelPatternRunnable = new Runnable() {
        public void run() {
            KeyguardPatternViewController.this.mLockPatternView.clearPattern();
        }
    };
    private CountDownTimer mCountdownTimer;
    private EmergencyButtonController.EmergencyButtonCallback mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() {
        public void onEmergencyButtonClickedWhenInCall() {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().reset();
        }
    };
    private final EmergencyButtonController mEmergencyButtonController;
    /* access modifiers changed from: private */
    public final FalsingCollector mFalsingCollector;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final LatencyTracker mLatencyTracker;
    /* access modifiers changed from: private */
    public final LockPatternUtils mLockPatternUtils;
    /* access modifiers changed from: private */
    public LockPatternView mLockPatternView;
    /* access modifiers changed from: private */
    public KeyguardMessageAreaController mMessageAreaController;
    private final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
    /* access modifiers changed from: private */
    public AsyncTask<?, ?, ?> mPendingLockCheck;
    private final DevicePostureController.Callback mPostureCallback = new KeyguardPatternViewController$$ExternalSyntheticLambda2(this);
    private final DevicePostureController mPostureController;

    public boolean needsInput() {
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardPatternViewController  reason: not valid java name */
    public /* synthetic */ void m2294lambda$new$0$comandroidkeyguardKeyguardPatternViewController(int i) {
        ((KeyguardPatternView) this.mView).onDevicePostureChanged(i);
    }

    private class UnlockPatternListener implements LockPatternView.OnPatternListener {
        public void onPatternCleared() {
        }

        private UnlockPatternListener() {
        }

        public void onPatternStart() {
            KeyguardPatternViewController.this.mLockPatternView.removeCallbacks(KeyguardPatternViewController.this.mCancelPatternRunnable);
            KeyguardPatternViewController.this.mMessageAreaController.setMessage((CharSequence) "");
        }

        public void onPatternCellAdded(List<LockPatternView.Cell> list) {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
        }

        public void onPatternDetected(List<LockPatternView.Cell> list) {
            KeyguardPatternViewController.this.mKeyguardUpdateMonitor.setCredentialAttempted();
            KeyguardPatternViewController.this.mLockPatternView.disableInput();
            if (KeyguardPatternViewController.this.mPendingLockCheck != null) {
                KeyguardPatternViewController.this.mPendingLockCheck.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (list.size() < 4) {
                if (list.size() == 1) {
                    KeyguardPatternViewController.this.mFalsingCollector.updateFalseConfidence(FalsingClassifier.Result.falsed(0.7d, getClass().getSimpleName(), "empty pattern input"));
                }
                KeyguardPatternViewController.this.mLockPatternView.enableInput();
                onPatternChecked(currentUser, false, 0, false);
                return;
            }
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(3);
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(4);
            KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
            AsyncTask unused = keyguardPatternViewController.mPendingLockCheck = LockPatternChecker.checkCredential(keyguardPatternViewController.mLockPatternUtils, LockscreenCredential.createPattern(list), currentUser, new LockPatternChecker.OnCheckCallback() {
                public void onEarlyMatched() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(3);
                    UnlockPatternListener.this.onPatternChecked(currentUser, true, 0, true);
                }

                public void onChecked(boolean z, int i) {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                    KeyguardPatternViewController.this.mLockPatternView.enableInput();
                    AsyncTask unused = KeyguardPatternViewController.this.mPendingLockCheck = null;
                    if (!z) {
                        UnlockPatternListener.this.onPatternChecked(currentUser, false, i, true);
                    }
                }

                public void onCancelled() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                }
            });
            if (list.size() > 2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
            }
        }

        /* access modifiers changed from: private */
        public void onPatternChecked(int i, boolean z, int i2, boolean z2) {
            boolean z3 = KeyguardUpdateMonitor.getCurrentUser() == i;
            if (z) {
                KeyguardPatternViewController.this.mLockPatternUtils.sanitizePassword();
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
                if (z3) {
                    KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    KeyguardPatternViewController.this.mLatencyTracker.onActionStart(11);
                    KeyguardPatternViewController.this.getKeyguardSecurityCallback().dismiss(true, i, KeyguardSecurityModel.SecurityMode.Pattern);
                }
                SystemUIEventUtils.collectUnLockResults(KeyguardPatternViewController.this.getContext(), SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 3);
                return;
            }
            KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            if (z2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
                if (i2 > 0) {
                    KeyguardPatternViewController.this.handleAttemptLockout(KeyguardPatternViewController.this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
                }
            }
            if (i2 == 0) {
                KeyguardPatternViewController.this.mMessageAreaController.setMessage((int) C1894R.string.kg_wrong_pattern);
                KeyguardPatternViewController.this.mLockPatternView.postDelayed(KeyguardPatternViewController.this.mCancelPatternRunnable, StatsManager.DEFAULT_TIMEOUT_MILLIS);
            }
            SystemUIEventUtils.collectUnLockResults(KeyguardPatternViewController.this.getContext(), SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_FAIL, 3);
        }
    }

    protected KeyguardPatternViewController(KeyguardPatternView keyguardPatternView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController, KeyguardMessageAreaController.Factory factory, DevicePostureController devicePostureController) {
        super(keyguardPatternView, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        this.mMessageAreaControllerFactory = factory;
        this.mMessageAreaController = factory.create(KeyguardMessageArea.findSecurityMessageDisplay(this.mView));
        this.mLockPatternView = ((KeyguardPatternView) this.mView).findViewById(C1894R.C1898id.lockPatternView);
        this.mPostureController = devicePostureController;
    }

    public void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        this.mLockPatternView.setOnPatternListener(new UnlockPatternListener());
        this.mLockPatternView.setSaveEnabled(false);
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.setOnTouchListener(new KeyguardPatternViewController$$ExternalSyntheticLambda0(this));
        this.mEmergencyButtonController.setEmergencyButtonCallback(this.mEmergencyButtonCallback);
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(C1894R.C1898id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new KeyguardPatternViewController$$ExternalSyntheticLambda1(this));
        }
        this.mPostureController.addCallback(this.mPostureCallback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$1$com-android-keyguard-KeyguardPatternViewController */
    public /* synthetic */ boolean mo25968x1a25cbe0(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        this.mFalsingCollector.avoidGesture();
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$2$com-android-keyguard-KeyguardPatternViewController */
    public /* synthetic */ void mo25969x47fe663f(View view) {
        getKeyguardSecurityCallback().reset();
        getKeyguardSecurityCallback().onCancelClicked();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        this.mLockPatternView.setOnPatternListener((LockPatternView.OnPatternListener) null);
        this.mLockPatternView.setOnTouchListener((View.OnTouchListener) null);
        this.mEmergencyButtonController.setEmergencyButtonCallback((EmergencyButtonController.EmergencyButtonCallback) null);
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(C1894R.C1898id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener((View.OnClickListener) null);
        }
        this.mPostureController.removeCallback(this.mPostureCallback);
    }

    public void reset() {
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.enableInput();
        this.mLockPatternView.setEnabled(true);
        this.mLockPatternView.clearPattern();
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (lockoutAttemptDeadline != 0) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            displayDefaultSecurityMessage();
        }
    }

    public void reloadColors() {
        super.reloadColors();
        this.mMessageAreaController.reloadColors();
        int defaultColor = Utils.getColorAttr(this.mLockPatternView.getContext(), 16842808).getDefaultColor();
        this.mLockPatternView.setColors(defaultColor, defaultColor, Utils.getColorError(this.mLockPatternView.getContext()).getDefaultColor());
    }

    public void onPause() {
        super.onPause();
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
        displayDefaultSecurityMessage();
    }

    public void showPromptReason(int i) {
        if (i == 0) {
            return;
        }
        if (i == 1) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_restart_pattern);
        } else if (i == 2) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_timeout_pattern);
        } else if (i == 3) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_device_admin);
        } else if (i == 4) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_user_request);
        } else if (i == 6) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_timeout_pattern);
        } else if (i != 7) {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_timeout_pattern);
        } else {
            this.mMessageAreaController.setMessage((int) C1894R.string.kg_prompt_reason_timeout_pattern);
        }
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mMessageAreaController.setNextMessageColor(colorStateList);
        }
        this.mMessageAreaController.setMessage(charSequence);
    }

    public void startAppearAnimation() {
        super.startAppearAnimation();
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardPatternView) this.mView).startDisappearAnimation(this.mKeyguardUpdateMonitor.needsSlowUnlockTransition(), runnable);
    }

    /* access modifiers changed from: private */
    public void displayDefaultSecurityMessage() {
        this.mMessageAreaController.setMessage((CharSequence) "");
    }

    /* access modifiers changed from: private */
    public void handleAttemptLockout(long j) {
        this.mLockPatternView.clearPattern();
        this.mLockPatternView.setEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil(((double) (j - SystemClock.elapsedRealtime())) / 1000.0d)) * 1000, 1000) {
            public void onTick(long j) {
                HashMap hashMap = new HashMap();
                hashMap.put("count", Integer.valueOf((int) Math.round(((double) j) / 1000.0d)));
                KeyguardPatternViewController.this.mMessageAreaController.setMessage((CharSequence) PluralsMessageFormatter.format(((KeyguardPatternView) KeyguardPatternViewController.this.mView).getResources(), hashMap, C1894R.string.kg_too_many_failed_attempts_countdown));
            }

            public void onFinish() {
                KeyguardPatternViewController.this.mLockPatternView.setEnabled(true);
                KeyguardPatternViewController.this.displayDefaultSecurityMessage();
            }
        }.start();
    }
}
