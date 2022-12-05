package com.android.keyguard;

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
import com.android.keyguard.KeyguardSimPinViewController;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardSimPinViewController extends KeyguardPinBasedInputViewController<KeyguardSimPinView> {
    private CheckSimPin mCheckSimPinThread;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private int mRemainingAttempts;
    private AlertDialog mRemainingAttemptsDialog;
    private boolean mShowDefaultMessage;
    private ProgressDialog mSimUnlockProgressDialog;
    private int mSlotId;
    private final TelephonyManager mTelephonyManager;
    private int mSubId = -1;
    KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardSimPinViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onSimStateChanged(int i, int i2, int i3) {
            Log.v("KeyguardSimPinView", "onSimStateChanged(subId=" + i + ",slotId=" + i2 + ",simState=" + i3 + ")");
            if (i3 == 5 || i3 == 10) {
                KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                KeyguardSimPinViewController.this.resetState();
                return;
            }
            KeyguardSimPinViewController.this.resetState();
        }
    };
    private ImageView mSimImageView = (ImageView) ((KeyguardSimPinView) this.mView).findViewById(R$id.keyguard_sim);

    @Override // com.android.keyguard.KeyguardInputViewController
    public boolean startDisappearAnimation(Runnable runnable) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyguardSimPinViewController(KeyguardSimPinView keyguardSimPinView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPinView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController
    public void resetState() {
        super.resetState();
        Log.v("KeyguardSimPinView", "Resetting state mShowDefaultMessage=" + this.mShowDefaultMessage);
        handleSubInfoChangeIfNeeded();
        this.mMessageAreaController.setMessage("");
        if (this.mShowDefaultMessage) {
            showDefaultMessage();
        }
        T t = this.mView;
        ((KeyguardSimPinView) t).setEsimLocked(KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) t).getContext(), this.mSubId));
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        super.onResume(i);
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        super.onPause();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
        this.mMessageAreaController.setMessage("");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public void verifyPasswordAndUnlock() {
        if (this.mPasswordEntry.getText().length() < 4) {
            this.mMessageAreaController.setMessage(R$string.kg_invalid_sim_pin_hint);
            ((KeyguardSimPinView) this.mView).resetPasswordText(true, true);
            getKeyguardSecurityCallback().userActivity();
            return;
        }
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPinThread != null) {
            return;
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.mPasswordEntry.getText(), this.mSubId);
        this.mCheckSimPinThread = anonymousClass2;
        anonymousClass2.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardSimPinViewController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends CheckSimPin {
        AnonymousClass2(String str, int i) {
            super(str, i);
        }

        @Override // com.android.keyguard.KeyguardSimPinViewController.CheckSimPin
        void onSimCheckResponse(final PinResult pinResult) {
            ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).post(new Runnable() { // from class: com.android.keyguard.KeyguardSimPinViewController$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardSimPinViewController.AnonymousClass2.this.lambda$onSimCheckResponse$0(pinResult);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSimCheckResponse$0(PinResult pinResult) {
            KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
            if (KeyguardSimPinViewController.this.mSimUnlockProgressDialog != null) {
                KeyguardSimPinViewController.this.mSimUnlockProgressDialog.hide();
            }
            ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).resetPasswordText(true, pinResult.getResult() != 0);
            if (pinResult.getResult() == 0) {
                KeyguardSimPinViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPinViewController.this.mSubId);
                KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                KeyguardSimPinViewController.this.mShowDefaultMessage = true;
                KeyguardSimPinViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
            } else {
                KeyguardSimPinViewController.this.mShowDefaultMessage = false;
                if (pinResult.getResult() == 1) {
                    if (pinResult.getAttemptsRemaining() <= 2) {
                        KeyguardSimPinViewController.this.getSimRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                    } else {
                        KeyguardSimPinViewController keyguardSimPinViewController = KeyguardSimPinViewController.this;
                        keyguardSimPinViewController.mMessageAreaController.setMessage(keyguardSimPinViewController.getPinPasswordErrorMessage(pinResult.getAttemptsRemaining(), false));
                    }
                } else {
                    KeyguardSimPinViewController keyguardSimPinViewController2 = KeyguardSimPinViewController.this;
                    keyguardSimPinViewController2.mMessageAreaController.setMessage(((KeyguardSimPinView) ((ViewController) keyguardSimPinViewController2).mView).getResources().getString(R$string.kg_password_pin_failed));
                }
                Log.d("KeyguardSimPinView", "verifyPasswordAndUnlock  CheckSimPin.onSimCheckResponse: " + pinResult + " attemptsRemaining=" + pinResult.getAttemptsRemaining());
            }
            KeyguardSimPinViewController.this.getKeyguardSecurityCallback().userActivity();
            KeyguardSimPinViewController.this.mCheckSimPinThread = null;
        }
    }

    private Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPinView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            this.mSimUnlockProgressDialog.getWindow().setType(2009);
        }
        return this.mSimUnlockProgressDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Dialog getSimRemainingAttemptsDialog(int i) {
        String pinPasswordErrorMessage = getPinPasswordErrorMessage(i, false);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPinView) this.mView).getContext());
            builder.setMessage(pinPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(2009);
        } else {
            alertDialog.setMessage(pinPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPinPasswordErrorMessage(int i, boolean z) {
        String string;
        int i2;
        int i3;
        if (i == 0) {
            string = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_password_wrong_pin_code_pukked);
        } else if (i > 0) {
            if (TelephonyManager.getDefault().getSimCount() > 1) {
                if (z) {
                    i3 = R$plurals.kg_password_default_pin_message_multi_sim;
                } else {
                    i3 = R$plurals.kg_password_wrong_pin_code_multi_sim;
                }
                string = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(i3, i, Integer.valueOf(this.mSlotId), Integer.valueOf(i));
            } else {
                if (z) {
                    i2 = R$plurals.kg_password_default_pin_message;
                } else {
                    i2 = R$plurals.kg_password_wrong_pin_code;
                }
                string = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(i2, i, Integer.valueOf(i));
            }
        } else {
            string = ((KeyguardSimPinView) this.mView).getResources().getString(z ? R$string.kg_sim_pin_instructions : R$string.kg_password_pin_failed);
        }
        if (KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId)) {
            string = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_lock_esim_instructions, string);
        }
        Log.d("KeyguardSimPinView", "getPinPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + string);
        return string;
    }

    private void showDefaultMessage() {
        setLockedSimMessage();
        if (this.mRemainingAttempts >= 0) {
            return;
        }
        this.mSlotId = SubscriptionManager.getSlotIndex(this.mSubId) + 1;
        new CheckSimPin("", this.mSubId) { // from class: com.android.keyguard.KeyguardSimPinViewController.3
            @Override // com.android.keyguard.KeyguardSimPinViewController.CheckSimPin
            void onSimCheckResponse(PinResult pinResult) {
                Log.d("KeyguardSimPinView", "onSimCheckResponse  empty One result " + pinResult.toString());
                if (pinResult.getAttemptsRemaining() >= 0) {
                    KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    KeyguardSimPinViewController.this.setLockedSimMessage();
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class CheckSimPin extends Thread {
        private final String mPin;
        private int mSubId;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: onSimCheckResponse */
        public abstract void lambda$run$0(PinResult pinResult);

        protected CheckSimPin(String str, int i) {
            this.mPin = str;
            this.mSubId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.v("KeyguardSimPinView", "call supplyIccLockPin(subid=" + this.mSubId + ")");
            final PinResult supplyIccLockPin = KeyguardSimPinViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPin(this.mPin);
            Log.v("KeyguardSimPinView", "supplyIccLockPin returned: " + supplyIccLockPin.toString());
            ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).post(new Runnable() { // from class: com.android.keyguard.KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardSimPinViewController.CheckSimPin.this.lambda$run$0(supplyIccLockPin);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLockedSimMessage() {
        String str;
        boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId);
        TelephonyManager telephonyManager = this.mTelephonyManager;
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        Resources resources = ((KeyguardSimPinView) this.mView).getResources();
        TypedArray obtainStyledAttributes = ((KeyguardSimPinView) this.mView).getContext().obtainStyledAttributes(new int[]{16842904});
        int color = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        if (activeModemCount < 2) {
            str = resources.getString(R$string.kg_sim_pin_instructions);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            String string = resources.getString(R$string.kg_sim_pin_instructions_multi, subscriptionInfoForSubId != null ? subscriptionInfoForSubId.getDisplayName() : "");
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(R$string.kg_sim_lock_esim_instructions, str);
        }
        if (((KeyguardSimPinView) this.mView).getVisibility() == 0) {
            this.mMessageAreaController.setMessage(str);
        }
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    private void handleSubInfoChangeIfNeeded() {
        int unlockedSubIdForState = this.mKeyguardUpdateMonitor.getUnlockedSubIdForState(2);
        if (SubscriptionManager.isValidSubscriptionId(unlockedSubIdForState)) {
            Log.v("KeyguardSimPinView", "handleSubInfoChangeIfNeeded mSubId=" + this.mSubId + " subId=" + unlockedSubIdForState);
            this.mShowDefaultMessage = true;
            if (unlockedSubIdForState == this.mSubId) {
                return;
            }
            this.mSubId = unlockedSubIdForState;
            this.mRemainingAttempts = -1;
            return;
        }
        this.mShowDefaultMessage = false;
    }
}
