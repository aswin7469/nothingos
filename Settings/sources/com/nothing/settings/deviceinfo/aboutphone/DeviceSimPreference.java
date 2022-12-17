package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
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
import com.android.settingslib.DeviceInfoUtils;
import com.nothing.settings.utils.CommonUtils;
import java.util.List;

public class DeviceSimPreference extends Preference {
    private Context mContext;
    private OnClickListener mOnClickListener;
    private SubscriptionManager mSubscriptionManager;

    public interface OnClickListener {
        void onClick(int i);
    }

    public DeviceSimPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public DeviceSimPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DeviceSimPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DeviceSimPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.about_phone_two_columns_layout);
        this.mContext = context;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_left_cl)).setOnClickListener(new DeviceSimPreference$$ExternalSyntheticLambda0(this));
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_right_cl)).setOnClickListener(new DeviceSimPreference$$ExternalSyntheticLambda1(this));
        TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_title);
        int i = R$string.sim_card_number_title;
        textView.setText(getSimTitle(i, 1));
        int i2 = R$color.about_device_title_color;
        textView.setTextColor(this.mContext.getColor(i2));
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_summary)).setText(getCarrierName(0));
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_title);
        textView2.setText(getSimTitle(i, 2));
        textView2.setTextColor(this.mContext.getColor(i2));
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_summary)).setText(getCarrierName(1));
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

    private String getSimTitle(int i, int i2) {
        return this.mContext.getString(i, new Object[]{Integer.valueOf(i2)});
    }

    private CharSequence getCarrierName(int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null) {
            for (SubscriptionInfo next : activeSubscriptionInfoList) {
                if (next.getSimSlotIndex() == i) {
                    return CommonUtils.getCountryFromMcc(next.getMccString()) + 10 + DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, next);
                }
            }
        }
        return this.mContext.getText(R$string.device_info_not_available);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void updateState() {
        notifyChanged();
    }
}
