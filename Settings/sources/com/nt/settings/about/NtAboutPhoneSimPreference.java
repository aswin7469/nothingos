package com.nt.settings.about;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.DeviceInfoUtils;
import com.nt.settings.utils.NtUtils;
import java.util.List;
/* loaded from: classes2.dex */
public class NtAboutPhoneSimPreference extends Preference {
    private Context mContext;
    private TextView mLeftSummary;
    private TextView mLeftTitle;
    private OnClickListener mOnClickListener;
    private RelativeLayout mRelativeLayoutLeft;
    private RelativeLayout mRelativeLayoutRight;
    private TextView mRightSummary;
    private TextView mRightTitle;
    private SubscriptionManager mSubscriptionManager;

    /* loaded from: classes2.dex */
    public interface OnClickListener {
        void onClick(int i);
    }

    public NtAboutPhoneSimPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneSimPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneSimPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneSimPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_two_columns_item);
        this.mContext = context;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Log.d("NtAboutPhoneSim", "@_@ ------ onBindViewHolder");
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_left_cl);
        this.mRelativeLayoutLeft = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneSimPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneSimPreference.this.mOnClickListener != null) {
                    NtAboutPhoneSimPreference.this.mOnClickListener.onClick(21);
                }
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_right_cl);
        this.mRelativeLayoutRight = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneSimPreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneSimPreference.this.mOnClickListener != null) {
                    NtAboutPhoneSimPreference.this.mOnClickListener.onClick(22);
                }
            }
        });
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_title);
        this.mLeftTitle = textView;
        int i = R.string.sim_card_number_title;
        textView.setText(getSimTitle(i, 1));
        TextView textView2 = this.mLeftTitle;
        Context context = this.mContext;
        int i2 = R.color.nothing_setting_text_color_1;
        textView2.setTextColor(context.getColor(i2));
        TextView textView3 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_summary);
        this.mLeftSummary = textView3;
        textView3.setText(getCarrierName(0));
        TextView textView4 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_title);
        this.mRightTitle = textView4;
        textView4.setText(getSimTitle(i, 2));
        this.mRightTitle.setTextColor(this.mContext.getColor(i2));
        TextView textView5 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_summary);
        this.mRightSummary = textView5;
        textView5.setText(getCarrierName(1));
    }

    private String getSimTitle(int i, int i2) {
        return this.mContext.getString(i, Integer.valueOf(i2));
    }

    private CharSequence getCarrierName(int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (subscriptionInfo.getSimSlotIndex() == i) {
                    return NtUtils.getCountryFromMcc(subscriptionInfo.getMccString()) + '\n' + DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, subscriptionInfo);
                }
            }
        }
        return this.mContext.getText(R.string.device_info_not_available);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
