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
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.sql.Types;

public class KeyguardSimPinViewController extends KeyguardPinBasedInputViewController<KeyguardSimPinView> {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "KeyguardSimPinView";
    public static final String TAG = "KeyguardSimPinView";
    /* access modifiers changed from: private */
    public CheckSimPin mCheckSimPinThread;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public int mRemainingAttempts;
    private AlertDialog mRemainingAttemptsDialog;
    /* access modifiers changed from: private */
    public boolean mShowDefaultMessage;
    private ImageView mSimImageView;
    /* access modifiers changed from: private */
    public ProgressDialog mSimUnlockProgressDialog;
    private int mSlotId;
    /* access modifiers changed from: private */
    public int mSubId = -1;
    /* access modifiers changed from: private */
    public final TelephonyManager mTelephonyManager;
    KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onSimStateChanged(int i, int i2, int i3) {
            Log.v("KeyguardSimPinView", "onSimStateChanged(subId=" + i + ",slotId=" + i2 + ",simState=" + i3 + NavigationBarInflaterView.KEY_CODE_END);
            if (i3 == 5 || i3 == 10) {
                int unused = KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                KeyguardSimPinViewController.this.resetState();
                return;
            }
            KeyguardSimPinViewController.this.resetState();
        }
    };

    public boolean startDisappearAnimation(Runnable runnable) {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected KeyguardSimPinViewController(KeyguardSimPinView keyguardSimPinView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPinView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mSimImageView = (ImageView) ((KeyguardSimPinView) this.mView).findViewById(C1893R.C1897id.keyguard_sim);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        super.resetState();
        Log.v("KeyguardSimPinView", "Resetting state mShowDefaultMessage=" + this.mShowDefaultMessage);
        handleSubInfoChangeIfNeeded();
        this.mMessageAreaController.setMessage((CharSequence) "");
        if (this.mShowDefaultMessage) {
            showDefaultMessage();
        }
        ((KeyguardSimPinView) this.mView).setEsimLocked(KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId), this.mSubId);
    }

    public void onResume(int i) {
        super.onResume(i);
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    public void onPause() {
        super.onPause();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
        this.mMessageAreaController.setMessage((CharSequence) "");
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardSimPinView) this.mView).reloadColors();
    }

    /* access modifiers changed from: protected */
    public void verifyPasswordAndUnlock() {
        if (this.mPasswordEntry.getText().length() < 4) {
            this.mMessageAreaController.setMessage((int) C1893R.string.kg_invalid_sim_pin_hint);
            ((KeyguardSimPinView) this.mView).resetPasswordText(true, true);
            getKeyguardSecurityCallback().userActivity();
            return;
        }
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPinThread == null) {
            C16342 r0 = new CheckSimPin(this.mPasswordEntry.getText(), this.mSubId) {
                /* access modifiers changed from: package-private */
                public void onSimCheckResponse(PinResult pinResult) {
                    ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).post(new KeyguardSimPinViewController$2$$ExternalSyntheticLambda0(this, pinResult));
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onSimCheckResponse$0$com-android-keyguard-KeyguardSimPinViewController$2 */
                public /* synthetic */ void mo26093x7d7171c6(PinResult pinResult) {
                    int unused = KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    if (KeyguardSimPinViewController.this.mSimUnlockProgressDialog != null) {
                        KeyguardSimPinViewController.this.mSimUnlockProgressDialog.hide();
                    }
                    ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).resetPasswordText(true, pinResult.getResult() != 0);
                    if (pinResult.getResult() == 0) {
                        KeyguardSimPinViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPinViewController.this.mSubId);
                        int unused2 = KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                        boolean unused3 = KeyguardSimPinViewController.this.mShowDefaultMessage = true;
                        KeyguardSimPinViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser(), KeyguardSecurityModel.SecurityMode.SimPin);
                    } else {
                        boolean unused4 = KeyguardSimPinViewController.this.mShowDefaultMessage = false;
                        if (pinResult.getResult() != 1) {
                            KeyguardSimPinViewController.this.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).getResources().getString(C1893R.string.kg_password_pin_failed));
                        } else if (pinResult.getAttemptsRemaining() <= 2) {
                            KeyguardSimPinViewController.this.getSimRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                        } else {
                            KeyguardSimPinViewController.this.mMessageAreaController.setMessage((CharSequence) KeyguardSimPinViewController.this.getPinPasswordErrorMessage(pinResult.getAttemptsRemaining(), false));
                        }
                        Log.d("KeyguardSimPinView", "verifyPasswordAndUnlock  CheckSimPin.onSimCheckResponse: " + pinResult + " attemptsRemaining=" + pinResult.getAttemptsRemaining());
                    }
                    KeyguardSimPinViewController.this.getKeyguardSecurityCallback().userActivity();
                    CheckSimPin unused5 = KeyguardSimPinViewController.this.mCheckSimPinThread = null;
                }
            };
            this.mCheckSimPinThread = r0;
            r0.start();
        }
    }

    private Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPinView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPinView) this.mView).getResources().getString(C1893R.string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            this.mSimUnlockProgressDialog.getWindow().setType(Types.SQLXML);
        }
        return this.mSimUnlockProgressDialog;
    }

    /* access modifiers changed from: private */
    public Dialog getSimRemainingAttemptsDialog(int i) {
        String pinPasswordErrorMessage = getPinPasswordErrorMessage(i, false);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPinView) this.mView).getContext());
            builder.setMessage(pinPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(C1893R.string.f262ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(Types.SQLXML);
        } else {
            alertDialog.setMessage(pinPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    /* access modifiers changed from: private */
    public String getPinPasswordErrorMessage(int i, boolean z) {
        String str;
        if (i == 0) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(C1893R.string.kg_password_wrong_pin_code_pukked);
        } else if (i <= 0) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(z ? C1893R.string.kg_sim_pin_instructions : C1893R.string.kg_password_pin_failed);
        } else if (TelephonyManager.getDefault().getSimCount() > 1) {
            str = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(z ? C1893R.plurals.kg_password_default_pin_message_multi_sim : C1893R.plurals.kg_password_wrong_pin_code_multi_sim, i, new Object[]{Integer.valueOf(this.mSlotId), Integer.valueOf(i)});
        } else {
            str = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(z ? C1893R.plurals.kg_password_default_pin_message : C1893R.plurals.kg_password_wrong_pin_code, i, new Object[]{Integer.valueOf(i)});
        }
        if (KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId)) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(C1893R.string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        Log.d("KeyguardSimPinView", "getPinPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        return str;
    }

    private void showDefaultMessage() {
        setLockedSimMessage();
        if (this.mRemainingAttempts < 0) {
            this.mSlotId = SubscriptionManager.getSlotIndex(this.mSubId) + 1;
            new CheckSimPin("", this.mSubId) {
                /* access modifiers changed from: package-private */
                public void onSimCheckResponse(PinResult pinResult) {
                    Log.d("KeyguardSimPinView", "onSimCheckResponse  empty One result " + pinResult.toString());
                    if (pinResult.getAttemptsRemaining() >= 0) {
                        int unused = KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                        KeyguardSimPinViewController.this.setLockedSimMessage();
                    }
                }
            }.start();
        }
    }

    private abstract class CheckSimPin extends Thread {
        private final String mPin;
        private int mSubId;

        /* access modifiers changed from: package-private */
        /* renamed from: onSimCheckResponse */
        public abstract void mo26095xffba033a(PinResult pinResult);

        protected CheckSimPin(String str, int i) {
            this.mPin = str;
            this.mSubId = i;
        }

        public void run() {
            Log.v("KeyguardSimPinView", "call supplyIccLockPin(subid=" + this.mSubId + NavigationBarInflaterView.KEY_CODE_END);
            PinResult supplyIccLockPin = KeyguardSimPinViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPin(this.mPin);
            Log.v("KeyguardSimPinView", "supplyIccLockPin returned: " + supplyIccLockPin.toString());
            ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).post(new C1636xfea10a16(this, supplyIccLockPin));
        }
    }

    /* access modifiers changed from: private */
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
            str = resources.getString(C1893R.string.kg_sim_pin_instructions);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            String string = resources.getString(C1893R.string.kg_sim_pin_instructions_multi, new Object[]{subscriptionInfoForSubId != null ? subscriptionInfoForSubId.getDisplayName() : ""});
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(C1893R.string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        if (((KeyguardSimPinView) this.mView).getVisibility() == 0) {
            this.mMessageAreaController.setMessage((CharSequence) str);
        }
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    private void handleSubInfoChangeIfNeeded() {
        int unlockedSubIdForState = this.mKeyguardUpdateMonitor.getUnlockedSubIdForState(2);
        if (SubscriptionManager.isValidSubscriptionId(unlockedSubIdForState)) {
            Log.v("KeyguardSimPinView", "handleSubInfoChangeIfNeeded mSubId=" + this.mSubId + " subId=" + unlockedSubIdForState);
            this.mShowDefaultMessage = true;
            if (unlockedSubIdForState != this.mSubId) {
                this.mSubId = unlockedSubIdForState;
                this.mRemainingAttempts = -1;
                return;
            }
            return;
        }
        this.mShowDefaultMessage = false;
    }
}
