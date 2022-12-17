package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.widget.HomepagePreferenceLayoutHelper;

public class HomepagePreference extends Preference implements HomepagePreferenceLayoutHelper.HomepagePreferenceLayout {
    private final HomepagePreferenceLayoutHelper mHelper = new HomepagePreferenceLayoutHelper(this);

    public HomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HomepagePreference(Context context) {
        super(context);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }

    public HomepagePreferenceLayoutHelper getHelper() {
        return this.mHelper;
    }
}
