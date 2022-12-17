package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$font;

public class SoftwareInfoPreference extends Preference {
    public SoftwareInfoPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Typeface font = getContext().getResources().getFont(R$font.ndot_55);
        TextView textView = (TextView) preferenceViewHolder.findViewById(16908310);
        if (textView != null) {
            textView.setAllCaps(true);
            textView.setTypeface(font);
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView2 != null) {
            textView2.setTypeface(font);
        }
    }
}
