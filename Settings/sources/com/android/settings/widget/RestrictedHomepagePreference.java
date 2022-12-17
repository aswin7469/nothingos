package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.widget.HomepagePreferenceLayoutHelper;
import com.android.settingslib.RestrictedTopLevelPreference;

public class RestrictedHomepagePreference extends RestrictedTopLevelPreference implements HomepagePreferenceLayoutHelper.HomepagePreferenceLayout {
    private final HomepagePreferenceLayoutHelper mHelper = new HomepagePreferenceLayoutHelper(this);

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }

    public HomepagePreferenceLayoutHelper getHelper() {
        return this.mHelper;
    }
}
