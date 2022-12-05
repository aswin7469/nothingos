package com.nt.settings.about;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes2.dex */
public class NtAboutPhoneNothingOsPreference extends Preference {
    public NtAboutPhoneNothingOsPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public NtAboutPhoneNothingOsPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NtAboutPhoneNothingOsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NtAboutPhoneNothingOsPreference(Context context) {
        super(context);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Typeface create = Typeface.create("NDot55", 0);
        TextView textView = (TextView) preferenceViewHolder.findViewById(16908310);
        if (textView != null) {
            textView.setTypeface(create);
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView2 != null) {
            textView2.setTypeface(create);
        }
    }
}
