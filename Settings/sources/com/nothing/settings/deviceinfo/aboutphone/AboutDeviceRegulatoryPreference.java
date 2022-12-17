package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;

public class AboutDeviceRegulatoryPreference extends Preference {
    private Context mContext;

    public AboutDeviceRegulatoryPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public AboutDeviceRegulatoryPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public AboutDeviceRegulatoryPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AboutDeviceRegulatoryPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.about_device_regulatory);
        this.mContext = context;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.nt_about_regulatory_phone_label);
        Configuration configuration = this.mContext.getResources().getConfiguration();
        if (configuration.getLayoutDirection() == 1) {
            textView.setGravity(8388613);
        }
        ((TextView) preferenceViewHolder.findViewById(R$id.nt_about_regulatory_model)).setText(String.format(this.mContext.getResources().getString(R$string.nt_regulatory_phone_model), new Object[]{Build.MODEL}));
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(R$id.nt_about_regulatory_taiwan_label);
        if ("Taiwan".contentEquals(textView2.getText()) && configuration.getLayoutDirection() == 1) {
            textView2.setGravity(8388613);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.mContext.getString(R$string.nt_regulatory_taiwan_detail));
        Drawable drawable = this.mContext.getDrawable(R$drawable.nt_regulatory_va);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannableStringBuilder.setSpan(new ImageSpan(drawable), spannableStringBuilder.length() - 4, spannableStringBuilder.length() - 3, 33);
        ((TextView) preferenceViewHolder.findViewById(R$id.nt_about_regulatory_taiwan_detail)).setText(spannableStringBuilder);
        TextView textView3 = (TextView) preferenceViewHolder.findViewById(R$id.nt_about_regulatory_singapore_label);
        if ("Singapore".contentEquals(textView3.getText()) && configuration.getLayoutDirection() == 1) {
            textView3.setGravity(8388613);
        }
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_see_all_detail).setOnClickListener(new AboutDeviceRegulatoryPreference$$ExternalSyntheticLambda0(preferenceViewHolder));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onBindViewHolder$0(PreferenceViewHolder preferenceViewHolder, View view) {
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_all_detail).setVisibility(0);
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_all_detail2).setVisibility(0);
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_all_detail3).setVisibility(0);
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_all_detail_layout).setVisibility(8);
        preferenceViewHolder.findViewById(R$id.nt_regulatory_safety_instructions_text_see_all_detail).setEnabled(false);
    }
}
