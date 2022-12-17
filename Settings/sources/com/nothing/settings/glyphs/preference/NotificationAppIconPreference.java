package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$layout;

public class NotificationAppIconPreference extends Preference {
    private Drawable mAppIconDrawable;
    private String mAppName;

    public NotificationAppIconPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public NotificationAppIconPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public NotificationAppIconPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public NotificationAppIconPreference(Context context) {
        super(context);
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
    }

    private void init() {
        setLayoutResource(R$layout.glyphs_conversation_app_icon);
    }

    public NotificationAppIconPreference setAppIcon(Drawable drawable) {
        this.mAppIconDrawable = drawable;
        return this;
    }

    public NotificationAppIconPreference setAppName(String str) {
        this.mAppName = str;
        return this;
    }

    public void refresh() {
        notifyChanged();
    }
}
