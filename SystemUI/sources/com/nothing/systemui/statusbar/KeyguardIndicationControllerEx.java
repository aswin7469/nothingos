package com.nothing.systemui.statusbar;

import android.content.Context;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.C1894R;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;

public class KeyguardIndicationControllerEx {
    private static final String TAG = "KeyguardIndicationControllerEx";
    private Context mContext;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final KeyguardSecurityModel mSecurityModel;

    @Inject
    public KeyguardIndicationControllerEx(Context context, KeyguardSecurityModel keyguardSecurityModel, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewMediator keyguardViewMediator) {
        this.mContext = context;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mSecurityModel = keyguardSecurityModel;
    }

    public CharSequence getUserLockedIndication() {
        int i;
        ViewMediatorCallback viewMediatorCallback = this.mKeyguardViewMediator.getViewMediatorCallback();
        KeyguardSecurityModel.SecurityMode securityMode = this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
        if (viewMediatorCallback != null) {
            i = viewMediatorCallback.getBouncerPromptReason();
            NTLogUtil.m1686d(TAG, "securityMode " + securityMode + " getBouncerPromptReason " + i);
        } else {
            i = -1;
        }
        int i2 = 17040641;
        if (i == 1) {
            if (securityMode == KeyguardSecurityModel.SecurityMode.PIN) {
                i2 = C1894R.string.kg_prompt_reason_restart_pin;
            } else if (securityMode == KeyguardSecurityModel.SecurityMode.Password) {
                i2 = C1894R.string.kg_prompt_reason_restart_password;
            } else if (securityMode == KeyguardSecurityModel.SecurityMode.Pattern) {
                i2 = C1894R.string.kg_prompt_reason_restart_pattern;
            }
        } else if (i == 2) {
            if (securityMode == KeyguardSecurityModel.SecurityMode.PIN) {
                i2 = C1894R.string.kg_prompt_reason_timeout_pin;
            } else if (securityMode == KeyguardSecurityModel.SecurityMode.Password) {
                i2 = C1894R.string.kg_prompt_reason_timeout_password;
            } else if (securityMode == KeyguardSecurityModel.SecurityMode.Pattern) {
                i2 = C1894R.string.kg_prompt_reason_timeout_pattern;
            }
        }
        return this.mContext.getResources().getText(i2);
    }
}
