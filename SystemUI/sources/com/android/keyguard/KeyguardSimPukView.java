package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.telephony.SubscriptionInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import java.util.HashMap;
import java.util.Map;

public class KeyguardSimPukView extends KeyguardPinBasedInputView {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    public static final String TAG = "KeyguardSimPukView";
    private ImageView mSimImageView;
    private Map<String, String> mWrongPukCodeMessageMap;

    /* access modifiers changed from: protected */
    public int getPasswordTextViewId() {
        return C1894R.C1898id.pukEntry;
    }

    /* access modifiers changed from: protected */
    public int getPromptReasonStringRes(int i) {
        return 0;
    }

    public void startAppearAnimation() {
    }

    public KeyguardSimPukView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardSimPukView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWrongPukCodeMessageMap = new HashMap(4);
        updateWrongPukMessageMap(context);
    }

    /* access modifiers changed from: package-private */
    public void updateWrongPukMessageMap(Context context) {
        String[] stringArray = context.getResources().getStringArray(C1894R.array.kg_wrong_puk_code_message_list);
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
        if (subscriptionInfoForSubId == null) {
            return null;
        }
        return this.mWrongPukCodeMessageMap.get(subscriptionInfoForSubId.getMccString() + subscriptionInfoForSubId.getMncString());
    }

    /* access modifiers changed from: package-private */
    public String getPukPasswordErrorMessage(int i, boolean z, boolean z2, int i2) {
        String str;
        if (i == 0) {
            str = getMessageTextForWrongPukCode(i2);
            if (str == null) {
                str = getContext().getString(C1894R.string.kg_password_wrong_puk_code_dead);
            }
        } else if (i > 0) {
            str = getContext().getResources().getQuantityString(z ? C1894R.plurals.kg_password_default_puk_message : C1894R.plurals.kg_password_wrong_puk_code, i, new Object[]{Integer.valueOf(i)});
        } else {
            str = getContext().getString(z ? C1894R.string.kg_puk_enter_puk_hint : C1894R.string.kg_password_puk_failed);
        }
        if (z2) {
            str = getResources().getString(C1894R.string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        if (DEBUG) {
            Log.d("KeyguardSimPukView", "getPukPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        }
        return str;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mSimImageView = (ImageView) findViewById(C1894R.C1898id.keyguard_sim);
        super.onFinishInflate();
        if (this.mEcaView instanceof EmergencyCarrierArea) {
            ((EmergencyCarrierArea) this.mEcaView).setCarrierTextVisible(true);
        }
    }

    public CharSequence getTitle() {
        return getContext().getString(17040523);
    }

    public void reloadColors() {
        super.reloadColors();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842808});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        DrawableCompat.setTint(DrawableCompat.wrap(this.mSimImageView.getDrawable()), color);
    }
}
