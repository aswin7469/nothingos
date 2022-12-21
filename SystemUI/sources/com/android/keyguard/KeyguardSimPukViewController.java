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
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.sql.Types;

public class KeyguardSimPukViewController extends KeyguardPinBasedInputViewController<KeyguardSimPukView> {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public static final String TAG = "KeyguardSimPukView";
    /* access modifiers changed from: private */
    public CheckSimPuk mCheckSimPukThread;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public String mPinText;
    /* access modifiers changed from: private */
    public String mPukText;
    /* access modifiers changed from: private */
    public int mRemainingAttempts;
    private AlertDialog mRemainingAttemptsDialog;
    /* access modifiers changed from: private */
    public boolean mShowDefaultMessage;
    private ImageView mSimImageView;
    /* access modifiers changed from: private */
    public ProgressDialog mSimUnlockProgressDialog;
    /* access modifiers changed from: private */
    public StateMachine mStateMachine = new StateMachine();
    /* access modifiers changed from: private */
    public int mSubId = -1;
    /* access modifiers changed from: private */
    public final TelephonyManager mTelephonyManager;
    KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onSimStateChanged(int i, int i2, int i3) {
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "onSimStateChanged(subId=" + i + ",state=" + i3 + NavigationBarInflaterView.KEY_CODE_END);
            }
            if (i3 == 5) {
                int unused = KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                boolean unused2 = KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                KeyguardSimPukViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser(), KeyguardSecurityModel.SecurityMode.SimPuk);
                return;
            }
            KeyguardSimPukViewController.this.resetState();
        }
    };

    /* access modifiers changed from: protected */
    public boolean shouldLockout(long j) {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected KeyguardSimPukViewController(KeyguardSimPukView keyguardSimPukView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPukView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mSimImageView = (ImageView) ((KeyguardSimPukView) this.mView).findViewById(C1893R.C1897id.keyguard_sim);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        super.resetState();
        this.mStateMachine.reset();
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardSimPukView) this.mView).reloadColors();
    }

    /* access modifiers changed from: protected */
    public void verifyPasswordAndUnlock() {
        this.mStateMachine.next();
    }

    private class StateMachine {
        static final int CONFIRM_PIN = 2;
        static final int DONE = 3;
        static final int ENTER_PIN = 1;
        static final int ENTER_PUK = 0;
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
                    i = C1893R.string.kg_puk_enter_pin_hint;
                } else {
                    i = C1893R.string.kg_invalid_sim_puk_hint;
                }
            } else if (i2 == 1) {
                if (KeyguardSimPukViewController.this.checkPin()) {
                    this.mState = 2;
                    i = C1893R.string.kg_enter_confirm_pin_hint;
                } else {
                    i = C1893R.string.kg_invalid_sim_pin_hint;
                }
            } else if (i2 != 2) {
                i = 0;
            } else if (KeyguardSimPukViewController.this.confirmPin()) {
                this.mState = 3;
                KeyguardSimPukViewController.this.updateSim();
                i = C1893R.string.keyguard_sim_unlock_progress_dialog_message;
            } else {
                this.mState = 1;
                i = C1893R.string.kg_invalid_confirm_pin_hint;
            }
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).resetPasswordText(true, true);
            if (i != 0) {
                KeyguardSimPukViewController.this.mMessageAreaController.setMessage(i);
            }
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            String unused = KeyguardSimPukViewController.this.mPinText = "";
            String unused2 = KeyguardSimPukViewController.this.mPukText = "";
            int i = 0;
            this.mState = 0;
            KeyguardSimPukViewController.this.handleSubInfoChangeIfNeeded();
            if (KeyguardSimPukViewController.this.mShowDefaultMessage) {
                KeyguardSimPukViewController.this.showDefaultMessage();
            }
            boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId);
            KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).findViewById(C1893R.C1897id.keyguard_esim_area);
            keyguardEsimArea.setSubscriptionId(KeyguardSimPukViewController.this.mSubId);
            if (!isEsimLocked) {
                i = 8;
            }
            keyguardEsimArea.setVisibility(i);
            KeyguardSimPukViewController.this.mPasswordEntry.requestFocus();
        }
    }

    /* access modifiers changed from: private */
    public void showDefaultMessage() {
        String str;
        CharSequence charSequence;
        if (this.mRemainingAttempts >= 0) {
            this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) this.mView).getPukPasswordErrorMessage(this.mRemainingAttempts, true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) this.mView).getContext(), this.mSubId), this.mSubId));
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
            str = resources.getString(C1893R.string.kg_puk_enter_puk_hint);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            if (subscriptionInfoForSubId != null) {
                charSequence = subscriptionInfoForSubId.getDisplayName();
            } else {
                charSequence = "";
            }
            String string = resources.getString(C1893R.string.kg_puk_enter_puk_hint_multi, new Object[]{charSequence});
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(C1893R.string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        this.mMessageAreaController.setMessage((CharSequence) str);
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
        new CheckSimPuk("", "", this.mSubId) {
            /* access modifiers changed from: package-private */
            public void onSimLockChangedResponse(PinResult pinResult) {
                if (pinResult == null) {
                    Log.e("KeyguardSimPukView", "onSimCheckResponse, pin result is NULL");
                    return;
                }
                Log.d("KeyguardSimPukView", "onSimCheckResponse  empty One result " + pinResult.toString());
                if (pinResult.getAttemptsRemaining() >= 0) {
                    int unused = KeyguardSimPukViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    KeyguardSimPukViewController.this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public boolean checkPuk() {
        if (this.mPasswordEntry.getText().length() != 8) {
            return false;
        }
        this.mPukText = this.mPasswordEntry.getText();
        return true;
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public void updateSim() {
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPukThread == null) {
            C16393 r0 = new CheckSimPuk(this.mPukText, this.mPinText, this.mSubId) {
                /* access modifiers changed from: package-private */
                public void onSimLockChangedResponse(PinResult pinResult) {
                    ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new KeyguardSimPukViewController$3$$ExternalSyntheticLambda0(this, pinResult));
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onSimLockChangedResponse$0$com-android-keyguard-KeyguardSimPukViewController$3 */
                public /* synthetic */ void mo26100x4eaa5fb9(PinResult pinResult) {
                    if (KeyguardSimPukViewController.this.mSimUnlockProgressDialog != null) {
                        KeyguardSimPukViewController.this.mSimUnlockProgressDialog.hide();
                    }
                    ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).resetPasswordText(true, pinResult.getResult() != 0);
                    if (pinResult.getResult() == 0) {
                        KeyguardSimPukViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPukViewController.this.mSubId);
                        int unused = KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                        boolean unused2 = KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                        KeyguardSimPukViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser(), KeyguardSecurityModel.SecurityMode.SimPuk);
                    } else {
                        boolean unused3 = KeyguardSimPukViewController.this.mShowDefaultMessage = false;
                        if (pinResult.getResult() == 1) {
                            KeyguardSimPukViewController.this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                            if (pinResult.getAttemptsRemaining() <= 2) {
                                KeyguardSimPukViewController.this.getPukRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                            } else {
                                KeyguardSimPukViewController.this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                            }
                        } else {
                            KeyguardSimPukViewController.this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getResources().getString(C1893R.string.kg_password_puk_failed));
                        }
                        if (KeyguardSimPukViewController.DEBUG) {
                            Log.d("KeyguardSimPukView", "verifyPasswordAndUnlock  UpdateSim.onSimCheckResponse:  attemptsRemaining=" + pinResult.getAttemptsRemaining());
                        }
                    }
                    KeyguardSimPukViewController.this.mStateMachine.reset();
                    CheckSimPuk unused4 = KeyguardSimPukViewController.this.mCheckSimPukThread = null;
                }
            };
            this.mCheckSimPukThread = r0;
            r0.start();
        }
    }

    private Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPukView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPukView) this.mView).getResources().getString(C1893R.string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            if (!(((KeyguardSimPukView) this.mView).getContext() instanceof Activity)) {
                this.mSimUnlockProgressDialog.getWindow().setType(Types.SQLXML);
            }
        }
        return this.mSimUnlockProgressDialog;
    }

    /* access modifiers changed from: private */
    public void handleSubInfoChangeIfNeeded() {
        int nextSubIdForState = this.mKeyguardUpdateMonitor.getNextSubIdForState(3);
        if (nextSubIdForState == this.mSubId || !SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
            this.mShowDefaultMessage = false;
            return;
        }
        this.mSubId = nextSubIdForState;
        this.mShowDefaultMessage = true;
        this.mRemainingAttempts = -1;
    }

    /* access modifiers changed from: private */
    public Dialog getPukRemainingAttemptsDialog(int i) {
        String pukPasswordErrorMessage = ((KeyguardSimPukView) this.mView).getPukPasswordErrorMessage(i, false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) this.mView).getContext(), this.mSubId), this.mSubId);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPukView) this.mView).getContext());
            builder.setMessage(pukPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(C1893R.string.f262ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(Types.SQLXML);
        } else {
            alertDialog.setMessage(pukPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    public void onPause() {
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
    }

    private abstract class CheckSimPuk extends Thread {
        private final String mPin;
        private final String mPuk;
        private final int mSubId;

        /* access modifiers changed from: package-private */
        /* renamed from: onSimLockChangedResponse */
        public abstract void mo26101x9d932adc(PinResult pinResult);

        protected CheckSimPuk(String str, String str2, int i) {
            this.mPuk = str;
            this.mPin = str2;
            this.mSubId = i;
        }

        public void run() {
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "call supplyIccLockPuk(subid=" + this.mSubId + NavigationBarInflaterView.KEY_CODE_END);
            }
            PinResult supplyIccLockPuk = KeyguardSimPukViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPuk(this.mPuk, this.mPin);
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "supplyIccLockPuk returned: " + supplyIccLockPuk.toString());
            }
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new C1640x2e140b38(this, supplyIccLockPuk));
        }
    }
}
