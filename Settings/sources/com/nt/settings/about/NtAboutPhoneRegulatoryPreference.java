package com.nt.settings.about;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NtAboutPhoneRegulatoryPreference extends Preference {
    private Context mContext;

    public NtAboutPhoneRegulatoryPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneRegulatoryPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneRegulatoryPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneRegulatoryPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_regulatory);
        this.mContext = context;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.mContext.getString(R.string.nt_regulatory_taiwan_detail));
        Drawable drawable = this.mContext.getDrawable(R.drawable.nt_va);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannableStringBuilder.setSpan(new ImageSpan(drawable), spannableStringBuilder.length() - 4, spannableStringBuilder.length() - 3, 33);
        ((TextView) preferenceViewHolder.findViewById(R.id.nt_about_regulatory_taiwan_detail)).setText(spannableStringBuilder);
    }
}
