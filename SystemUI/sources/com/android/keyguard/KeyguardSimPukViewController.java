package com.android.keyguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.telephony.PinResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSimPukViewController;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardSimPukViewController extends KeyguardPinBasedInputViewController<KeyguardSimPukView> {
    private CheckSimPuk mCheckSimPukThread;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private String mPinText;
    private String mPukText;
    private int mRemainingAttempts;
    private AlertDialog mRemainingAttemptsDialog;
    private boolean mShowDefaultMessage;
    private ProgressDialog mSimUnlockProgressDialog;
    private final TelephonyManager mTelephonyManager;
    private StateMachine mStateMachine = new StateMachine();
    private int mSubId = -1;
    KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardSimPukViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onSimStateChanged(int i, int i2, int i3) {
            Log.v("KeyguardSimPukView", "onSimStateChanged(subId=" + i + ",state=" + i3 + ")");
            if (i3 == 5) {
                KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                KeyguardSimPukViewController.this.resetState();
                return;
            }
            KeyguardSimPukViewController.this.resetState();
        }
    };
    private ImageView mSimImageView = (ImageView) ((KeyguardSimPukView) this.mView).findViewById(R$id.keyguard_sim);

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    protected boolean shouldLockout(long j) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyguardSimPukViewController(KeyguardSimPukView keyguardSimPukView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPukView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController
    public void resetState() {
        super.resetState();
        this.mStateMachine.reset();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public void verifyPasswordAndUnlock() {
        this.mStateMachine.next();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StateMachine {
        private int mState;

        private StateMachine() {
            this.mState = 0;
        }

        public void next() {
            int i;
            int i2 = this.mState;
            if (i2 == 0) {
                if (KeyguardSimPukViewController.this.checkPuk()) {
                    this.mState = 1;
                    i = R$string.kg_puk_enter_pin_hint;
                } else {
                    i = R$string.kg_invalid_sim_puk_hint;
                }
            } else if (i2 == 1) {
                if (KeyguardSimPukViewController.this.checkPin()) {
                    this.mState = 2;
                    i = R$string.kg_enter_confirm_pin_hint;
                } else {
                    i = R$string.kg_invalid_sim_pin_hint;
                }
            } else if (i2 != 2) {
                i = 0;
            } else if (KeyguardSimPukViewController.this.confirmPin()) {
                this.mState = 3;
                i = R$string.keyguard_sim_unlock_progress_dialog_message;
                KeyguardSimPukViewController.this.updateSim();
            } else {
                this.mState = 1;
                i = R$string.kg_invalid_confirm_pin_hint;
            }
            ((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).resetPasswordText(true, true);
            if (i != 0) {
                KeyguardSimPukViewController.this.mMessageAreaController.setMessage(i);
            }
        }

        void reset() {
            KeyguardSimPukViewController.this.mPinText = "";
            KeyguardSimPukViewController.this.mPukText = "";
            int i = 0;
            this.mState = 0;
            KeyguardSimPukViewController.this.handleSubInfoChangeIfNeeded();
            if (KeyguardSimPukViewController.this.mShowDefaultMessage) {
                KeyguardSimPukViewController.this.showDefaultMessage();
            }
            boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).getContext(), KeyguardSimPukViewController.this.mSubId);
            KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) ((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).findViewById(R$id.keyguard_esim_area);
            if (!isEsimLocked) {
                i = 8;
            }
            keyguardEsimArea.setVisibility(i);
            KeyguardSimPukViewController.this.mPasswordEntry.requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDefaultMessage() {
        String str;
        int i = this.mRemainingAttempts;
        if (i >= 0) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mMessageAreaController;
            T t = this.mView;
            keyguardMessageAreaController.setMessage(((KeyguardSimPukView) t).getPukPasswordErrorMessage(i, true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) t).getContext(), this.mSubId), this.mSubId));
            return;
        }
        boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) this.mView).getContext(), this.mSubId);
        TelephonyManager telephonyManager = this.mTelephonyManager;
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        Resources resources = ((KeyguardSimPukView) this.mView).getResources();
        TypedArray obtainStyledAttributes = ((KeyguardSimPukView) this.mView).getContext().obtainStyledAttributes(new int[]{16842904});
        int color = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        if (activeModemCount < 2) {
            str = resources.getString(R$string.kg_puk_enter_puk_hint);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            String string = resources.getString(R$string.kg_puk_enter_puk_hint_multi, subscriptionInfoForSubId != null ? subscriptionInfoForSubId.getDisplayName() : "");
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(R$string.kg_sim_lock_esim_instructions, str);
        }
        this.mMessageAreaController.setMessage(str);
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
        new CheckSimPuk("", "", this.mSubId) { // from class: com.android.keyguard.KeyguardSimPukViewController.2
            @Override // com.android.keyguard.KeyguardSimPukViewController.CheckSimPuk
            void onSimLockChangedResponse(PinResult pinResult) {
                if (pinResult == null) {
                    Log.e("KeyguardSimPukView", "onSimCheckResponse, pin result is NULL");
                    return;
                }
                Log.d("KeyguardSimPukView", "onSimCheckResponse  empty One result " + pinResult.toString());
                if (pinResult.getAttemptsRemaining() < 0) {
                    return;
                }
                KeyguardSimPukViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                keyguardSimPukViewController.mMessageAreaController.setMessage(((KeyguardSimPukView) ((ViewController) keyguardSimPukViewController).mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkPuk() {
        if (this.mPasswordEntry.getText().length() == 8) {
            this.mPukText = this.mPasswordEntry.getText();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkPin() {
        int length = this.mPasswordEntry.getText().length();
        if (length < 4 || length > 8) {
            return false;
        }
        this.mPinText = this.mPasswordEntry.getText();
        return true;
    }

    public boolean confirmPin() {
        return this.mPinText.equals(this.mPasswordEntry.getText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSim() {
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPukThread == null) {
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(this.mPukText, this.mPinText, this.mSubId);
            this.mCheckSimPukThread = anonymousClass3;
            anonymousClass3.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardSimPukViewController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends CheckSimPuk {
        AnonymousClass3(String str, String str2, int i) {
            super(str, str2, i);
        }

        @Override // com.android.keyguard.KeyguardSimPukViewController.CheckSimPuk
        void onSimLockChangedResponse(final PinResult pinResult) {
            ((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).post(new Runnable() { // from class: com.android.keyguard.KeyguardSimPukViewController$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardSimPukViewController.AnonymousClass3.this.lambda$onSimLockChangedResponse$0(pinResult);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSimLockChangedResponse$0(PinResult pinResult) {
            if (KeyguardSimPukViewController.this.mSimUnlockProgressDialog != null) {
                KeyguardSimPukViewController.this.mSimUnlockProgressDialog.hide();
            }
            ((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).resetPasswordText(true, pinResult.getResult() != 0);
            if (pinResult.getResult() == 0) {
                KeyguardSimPukViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPukViewController.this.mSubId);
                KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                KeyguardSimPukViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
            } else {
                KeyguardSimPukViewController.this.mShowDefaultMessage = false;
                if (pinResult.getResult() == 1) {
                    KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                    keyguardSimPukViewController.mMessageAreaController.setMessage(((KeyguardSimPukView) ((ViewController) keyguardSimPukViewController).mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                    if (pinResult.getAttemptsRemaining() <= 2) {
                        KeyguardSimPukViewController.this.getPukRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                    } else {
                        KeyguardSimPukViewController keyguardSimPukViewController2 = KeyguardSimPukViewController.this;
                        keyguardSimPukViewController2.mMessageAreaController.setMessage(((KeyguardSimPukView) ((ViewController) keyguardSimPukViewController2).mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                    }
                } else {
                    KeyguardSimPukViewController keyguardSimPukViewController3 = KeyguardSimPukViewController.this;
                    keyguardSimPukViewController3.mMessageAreaController.setMessage(((KeyguardSimPukView) ((ViewController) keyguardSimPukViewController3).mView).getResources().getString(R$string.kg_password_puk_failed));
                }
                Log.d("KeyguardSimPukView", "verifyPasswordAndUnlock  UpdateSim.onSimCheckResponse:  attemptsRemaining=" + pinResult.getAttemptsRemaining());
            }
            KeyguardSimPukViewController.this.mStateMachine.reset();
            KeyguardSimPukViewController.this.mCheckSimPukThread = null;
        }
    }

    private Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPukView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPukView) this.mView).getResources().getString(R$string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            if (!(((KeyguardSimPukView) this.mView).getContext() instanceof Activity)) {
                this.mSimUnlockProgressDialog.getWindow().setType(2009);
            }
        }
        return this.mSimUnlockProgressDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubInfoChangeIfNeeded() {
        int nextSubIdForState = this.mKeyguardUpdateMonitor.getNextSubIdForState(3);
        if (nextSubIdForState != this.mSubId && SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
            this.mSubId = nextSubIdForState;
            this.mShowDefaultMessage = true;
            this.mRemainingAttempts = -1;
            return;
        }
        this.mShowDefaultMessage = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Dialog getPukRemainingAttemptsDialog(int i) {
        T t = this.mView;
        String pukPasswordErrorMessage = ((KeyguardSimPukView) t).getPukPasswordErrorMessage(i, false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) t).getContext(), this.mSubId), this.mSubId);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPukView) this.mView).getContext());
            builder.setMessage(pukPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(2009);
        } else {
            alertDialog.setMessage(pukPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class CheckSimPuk extends Thread {
        private final String mPin;
        private final String mPuk;
        private final int mSubId;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: onSimLockChangedResponse */
        public abstract void lambda$run$0(PinResult pinResult);

        protected CheckSimPuk(String str, String str2, int i) {
            this.mPuk = str;
            this.mPin = str2;
            this.mSubId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.v("KeyguardSimPukView", "call supplyIccLockPuk(subid=" + this.mSubId + ")");
            final PinResult supplyIccLockPuk = KeyguardSimPukViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPuk(this.mPuk, this.mPin);
            Log.v("KeyguardSimPukView", "supplyIccLockPuk returned: " + supplyIccLockPuk.toString());
            ((KeyguardSimPukView) ((ViewController) KeyguardSimPukViewController.this).mView).post(new Runnable() { // from class: com.android.keyguard.KeyguardSimPukViewController$CheckSimPuk$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardSimPukViewController.CheckSimPuk.this.lambda$run$0(supplyIccLockPuk);
                }
            });
        }
    }
}
