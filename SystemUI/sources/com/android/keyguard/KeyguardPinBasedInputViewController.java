package com.android.keyguard;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardPinBasedInputView;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingCollector;

public abstract class KeyguardPinBasedInputViewController<T extends KeyguardPinBasedInputView> extends KeyguardAbsKeyInputViewController<T> {
    private final View.OnTouchListener mActionButtonTouchListener = new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda5(this);
    private final FalsingCollector mFalsingCollector;
    private final LiftToActivateListener mLiftToActivateListener;
    private final View.OnKeyListener mOnKeyListener = new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda4(this);
    protected PasswordTextView mPasswordEntry;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardPinBasedInputViewController */
    public /* synthetic */ boolean mo25988x2334539f(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            return ((KeyguardPinBasedInputView) this.mView).onKeyDown(i, keyEvent);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-keyguard-KeyguardPinBasedInputViewController */
    public /* synthetic */ boolean mo25989xb0216abe(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        ((KeyguardPinBasedInputView) this.mView).doHapticKeyClick();
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected KeyguardPinBasedInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector) {
        super(t, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mLiftToActivateListener = liftToActivateListener;
        this.mFalsingCollector = falsingCollector;
        this.mPasswordEntry = (PasswordTextView) ((KeyguardPinBasedInputView) this.mView).findViewById(((KeyguardPinBasedInputView) this.mView).getPasswordTextViewId());
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        for (NumPadKey onTouchListener : ((KeyguardPinBasedInputView) this.mView).getButtons()) {
            onTouchListener.setOnTouchListener(new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda0(this));
        }
        this.mPasswordEntry.setOnKeyListener(this.mOnKeyListener);
        this.mPasswordEntry.setUserActivityListener(new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda1(this));
        View findViewById = ((KeyguardPinBasedInputView) this.mView).findViewById(C1893R.C1897id.delete_button);
        findViewById.setOnTouchListener(this.mActionButtonTouchListener);
        findViewById.setOnClickListener(new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda2(this));
        findViewById.setOnLongClickListener(new KeyguardPinBasedInputViewController$$ExternalSyntheticLambda3(this));
        View findViewById2 = ((KeyguardPinBasedInputView) this.mView).findViewById(C1893R.C1897id.key_enter);
        if (findViewById2 != null) {
            findViewById2.setOnTouchListener(this.mActionButtonTouchListener);
            findViewById2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (KeyguardPinBasedInputViewController.this.mPasswordEntry.isEnabled()) {
                        KeyguardPinBasedInputViewController.this.verifyPasswordAndUnlock();
                    }
                }
            });
            findViewById2.setOnHoverListener(this.mLiftToActivateListener);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$2$com-android-keyguard-KeyguardPinBasedInputViewController */
    public /* synthetic */ boolean mo25990x2987a7b(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        this.mFalsingCollector.avoidGesture();
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$3$com-android-keyguard-KeyguardPinBasedInputViewController */
    public /* synthetic */ void mo25991x8f85919a(View view) {
        if (this.mPasswordEntry.isEnabled()) {
            this.mPasswordEntry.deleteLastChar();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$4$com-android-keyguard-KeyguardPinBasedInputViewController */
    public /* synthetic */ boolean mo25992x1c72a8b9(View view) {
        if (this.mPasswordEntry.isEnabled()) {
            ((KeyguardPinBasedInputView) this.mView).resetPasswordText(true, true);
        }
        ((KeyguardPinBasedInputView) this.mView).doHapticKeyClick();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        for (NumPadKey onTouchListener : ((KeyguardPinBasedInputView) this.mView).getButtons()) {
            onTouchListener.setOnTouchListener((View.OnTouchListener) null);
        }
    }

    public void onResume(int i) {
        super.onResume(i);
        this.mPasswordEntry.requestFocus();
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        ((KeyguardPinBasedInputView) this.mView).setPasswordEntryEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void startErrorAnimation() {
        super.startErrorAnimation();
        ((KeyguardPinBasedInputView) this.mView).startErrorAnimation();
    }
}
