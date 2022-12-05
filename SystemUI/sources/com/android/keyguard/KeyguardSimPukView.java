package com.android.keyguard;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class KeyguardSimPukView extends KeyguardPinBasedInputView {
    private Map<String, String> mWrongPukCodeMessageMap;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView
    public int getPromptReasonStringRes(int i) {
        return 0;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public void startAppearAnimation() {
    }

    @Override // com.android.keyguard.KeyguardInputView
    public boolean startDisappearAnimation(Runnable runnable) {
        return false;
    }

    public KeyguardSimPukView(Context context) {
        this(context, null);
    }

    public KeyguardSimPukView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWrongPukCodeMessageMap = new HashMap(4);
        updateWrongPukMessageMap(context);
    }

    void updateWrongPukMessageMap(Context context) {
        String[] stringArray = context.getResources().getStringArray(R$array.kg_wrong_puk_code_message_list);
        if (stringArray.length == 0) {
            Log.d("KeyguardSimPukView", "There is no customization PUK prompt");
            return;
        }
        for (String str : stringArray) {
            String[] split = str.trim().split(":");
            if (split.length != 2) {
                Log.e("KeyguardSimPukView", "invalid key value config " + str);
            } else {
                this.mWrongPukCodeMessageMap.put(split[0], split[1]);
            }
        }
    }

    private String getMessageTextForWrongPukCode(int i) {
        SubscriptionInfo subscriptionInfoForSubId = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).getSubscriptionInfoForSubId(i);
        if (subscriptionInfoForSubId != null) {
            return this.mWrongPukCodeMessageMap.get(subscriptionInfoForSubId.getMccString() + subscriptionInfoForSubId.getMncString());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPukPasswordErrorMessage(int i, boolean z, boolean z2, int i2) {
        int i3;
        String string;
        int i4;
        if (i == 0) {
            string = getMessageTextForWrongPukCode(i2);
            if (string == null) {
                string = getContext().getString(R$string.kg_password_wrong_puk_code_dead);
            }
        } else if (i > 0) {
            if (z) {
                i4 = R$plurals.kg_password_default_puk_message;
            } else {
                i4 = R$plurals.kg_password_wrong_puk_code;
            }
            string = getContext().getResources().getQuantityString(i4, i, Integer.valueOf(i));
        } else {
            if (z) {
                i3 = R$string.kg_puk_enter_puk_hint;
            } else {
                i3 = R$string.kg_password_puk_failed;
            }
            string = getContext().getString(i3);
        }
        if (z2) {
            string = getResources().getString(R$string.kg_sim_lock_esim_instructions, string);
        }
        Log.d("KeyguardSimPukView", "getPukPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + string);
        return string;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getPasswordTextViewId() {
        return R$id.pukEntry;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        View view = this.mEcaView;
        if (view instanceof EmergencyCarrierArea) {
            ((EmergencyCarrierArea) view).setCarrierTextVisible(true);
        }
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardInputView
    public CharSequence getTitle() {
        return getContext().getString(17040447);
    }
}
