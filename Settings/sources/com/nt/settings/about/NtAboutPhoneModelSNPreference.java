package com.nt.settings.about;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NtAboutPhoneModelSNPreference extends Preference {
    private TextView mLeftSummary;
    private TextView mLeftTitle;
    private RelativeLayout mRelativeLayoutLeft;
    private RelativeLayout mRelativeLayoutRight;
    private TextView mRightSummary;
    private TextView mRightTitle;

    public NtAboutPhoneModelSNPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneModelSNPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneModelSNPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneModelSNPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_two_columns_item);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Log.d("NtAboutPhoneModelSN", "@_@ ------ onBindViewHolder");
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_left_cl);
        this.mRelativeLayoutLeft = relativeLayout;
        relativeLayout.setBackground(null);
        RelativeLayout relativeLayout2 = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_right_cl);
        this.mRelativeLayoutRight = relativeLayout2;
        relativeLayout2.setBackground(null);
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_title);
        this.mLeftTitle = textView;
        textView.setText(R.string.nt_model_number);
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_summary);
        this.mLeftSummary = textView2;
        textView2.setText(Build.MODEL);
        TextView textView3 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_title);
        this.mRightTitle = textView3;
        textView3.setText(R.string.status_serial_number);
        TextView textView4 = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_summary);
        this.mRightSummary = textView4;
        textView4.setText(Build.getSerial());
    }
}
