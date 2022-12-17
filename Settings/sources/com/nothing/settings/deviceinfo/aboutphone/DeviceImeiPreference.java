package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$color;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.qti.extphone.QtiImeiInfo;

public class DeviceImeiPreference extends Preference {
    private Context mContext;
    private OnClickListener mOnClickListener;
    private QtiImeiInfo[] mQtiImeiInfo;
    private TelephonyManager mTelephonyManager;

    public interface OnClickListener {
        void onClick(int i);
    }

    public DeviceImeiPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public DeviceImeiPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DeviceImeiPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DeviceImeiPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.about_phone_two_columns_layout);
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_left_cl)).setOnClickListener(new DeviceImeiPreference$$ExternalSyntheticLambda0(this));
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_right_cl)).setOnClickListener(new DeviceImeiPreference$$ExternalSyntheticLambda1(this));
        TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_title);
        int i = R$string.nt_imei_multi_sim;
        textView.setText(getImeiTitle(i, 1));
        int i2 = R$color.about_device_title_color;
        textView.setTextColor(this.mContext.getColor(i2));
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_summary)).setText(String.valueOf(getSummary(0)));
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_title);
        textView2.setText(getImeiTitle(i, 2));
        textView2.setTextColor(this.mContext.getColor(i2));
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_summary)).setText(String.valueOf(getSummary(1)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(21);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(22);
        }
    }

    private String getImeiTitle(int i, int i2) {
        return this.mContext.getString(i, new Object[]{Integer.valueOf(i2)});
    }

    private CharSequence getSummary(int i) {
        int phoneType = getPhoneType(i);
        if (Utils.isSupportCTPA(this.mContext) && 2 == phoneType) {
            i = 0;
        }
        if (phoneType == 2) {
            return this.mTelephonyManager.getMeid(i);
        }
        return getImei(i);
    }

    private int getPhoneType(int i) {
        if (Utils.isSupportCTPA(this.mContext)) {
            return this.mTelephonyManager.getCurrentPhoneTypeForSlot(i);
        }
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(i);
        return this.mTelephonyManager.getCurrentPhoneType(activeSubscriptionInfoForSimSlotIndex != null ? activeSubscriptionInfoForSimSlotIndex.getSubscriptionId() : Integer.MAX_VALUE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getImei(int r6) {
        /*
            r5 = this;
            com.qti.extphone.QtiImeiInfo[] r0 = r5.mQtiImeiInfo
            if (r0 != 0) goto L_0x000a
            com.qti.extphone.QtiImeiInfo[] r0 = com.android.settings.network.telephony.TelephonyUtils.getImeiInfo()
            r5.mQtiImeiInfo = r0
        L_0x000a:
            com.qti.extphone.QtiImeiInfo[] r0 = r5.mQtiImeiInfo
            if (r0 == 0) goto L_0x0022
            int r1 = r0.length
            r2 = 0
        L_0x0010:
            if (r2 >= r1) goto L_0x0022
            r3 = r0[r2]
            int r4 = r3.getSlotId()
            if (r4 != r6) goto L_0x001f
            java.lang.String r0 = r3.getImei()
            goto L_0x0023
        L_0x001f:
            int r2 = r2 + 1
            goto L_0x0010
        L_0x0022:
            r0 = 0
        L_0x0023:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x002f
            android.telephony.TelephonyManager r5 = r5.mTelephonyManager
            java.lang.String r0 = r5.getImei(r6)
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.deviceinfo.aboutphone.DeviceImeiPreference.getImei(int):java.lang.String");
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
