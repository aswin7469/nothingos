package com.nt.settings.about;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.network.telephony.TelephonyUtils;
import com.qti.extphone.QtiImeiInfo;
/* loaded from: classes2.dex */
public class NtAboutPhoneImeiPreference extends Preference {
    private Context mContext;
    private TextView mLeftSummary;
    private TextView mLeftTitle;
    private OnClickListener mOnClickListener;
    private QtiImeiInfo[] mQtiImeiInfo;
    private RelativeLayout mRelativeLayoutLeft;
    private RelativeLayout mRelativeLayoutRight;
    private TextView mRightSummary;
    private TextView mRightTitle;
    private TelephonyManager mTelephonyManager;

    /* loaded from: classes2.dex */
    public interface OnClickListener {
        void onClick(int i);
    }

    public NtAboutPhoneImeiPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneImeiPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneImeiPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneImeiPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_two_columns_item);
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Log.d("NtAboutPhoneImei", "@_@ ------ onBindViewHolder");
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_left_cl);
        this.mRelativeLayoutLeft = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneImeiPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneImeiPreference.this.mOnClickListener != null) {
                    NtAboutPhoneImeiPreference.this.mOnClickListener.onClick(21);
                }
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_right_cl);
        this.mRelativeLayoutRight = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneImeiPreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneImeiPreference.this.mOnClickListener != null) {
                    NtAboutPhoneImeiPreference.this.mOnClickListener.onClick(22);
                }
            }
        });
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_title);
        this.mLeftTitle = textView;
        int i = R.string.nt_imei_multi_sim;
        textView.setText(getImeiTitle(i, 1));
        TextView textView2 = this.mLeftTitle;
        Context context = this.mContext;
        int i2 = R.color.nothing_setting_text_color_1;
        textView2.setTextColor(context.getColor(i2));
        TextView textView3 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_summary);
        this.mLeftSummary = textView3;
        textView3.setText(String.valueOf(getSummary(0)));
        TextView textView4 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_title);
        this.mRightTitle = textView4;
        textView4.setText(getImeiTitle(i, 2));
        this.mRightTitle.setTextColor(this.mContext.getColor(i2));
        TextView textView5 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_summary);
        this.mRightSummary = textView5;
        textView5.setText(String.valueOf(getSummary(1)));
    }

    private String getImeiTitle(int i, int i2) {
        return this.mContext.getString(i, Integer.valueOf(i2));
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

    /* JADX WARN: Removed duplicated region for block: B:16:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getImei(int i) {
        String str;
        if (this.mQtiImeiInfo == null) {
            this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
        }
        if (this.mQtiImeiInfo != null) {
            int i2 = 0;
            while (true) {
                QtiImeiInfo[] qtiImeiInfoArr = this.mQtiImeiInfo;
                if (i2 >= qtiImeiInfoArr.length) {
                    break;
                } else if (qtiImeiInfoArr[i2].getSlotId() == i) {
                    str = this.mQtiImeiInfo[i2].getImei();
                    break;
                } else {
                    i2++;
                }
            }
            return !TextUtils.isEmpty(str) ? this.mTelephonyManager.getImei(i) : str;
        }
        str = null;
        if (!TextUtils.isEmpty(str)) {
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
