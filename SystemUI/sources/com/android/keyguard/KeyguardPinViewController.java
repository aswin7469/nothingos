package com.android.keyguard;

import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.C1894R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;

public class KeyguardPinViewController extends KeyguardPinBasedInputViewController<KeyguardPINView> {
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final DevicePostureController.Callback mPostureCallback = new KeyguardPinViewController$$ExternalSyntheticLambda1(this);
    private final DevicePostureController mPostureController;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardPinViewController  reason: not valid java name */
    public /* synthetic */ void m2295lambda$new$0$comandroidkeyguardKeyguardPinViewController(int i) {
        ((KeyguardPINView) this.mView).onDevicePostureChanged(i);
    }

    protected KeyguardPinViewController(KeyguardPINView keyguardPINView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector, DevicePostureController devicePostureController) {
        super(keyguardPINView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPostureController = devicePostureController;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        View findViewById = ((KeyguardPINView) this.mView).findViewById(C1894R.C1898id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new KeyguardPinViewController$$ExternalSyntheticLambda0(this));
        }
        this.mPostureController.addCallback(this.mPostureCallback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$1$com-android-keyguard-KeyguardPinViewController */
    public /* synthetic */ void mo26001x82dd84a5(View view) {
        getKeyguardSecurityCallback().reset();
        getKeyguardSecurityCallback().onCancelClicked();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        this.mPostureController.removeCallback(this.mPostureCallback);
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardPINView) this.mView).reloadColors();
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        super.resetState();
        this.mMessageAreaController.setMessage((CharSequence) "");
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardPINView) this.mView).startDisappearAnimation(this.mKeyguardUpdateMonitor.needsSlowUnlockTransition(), runnable);
    }
}
