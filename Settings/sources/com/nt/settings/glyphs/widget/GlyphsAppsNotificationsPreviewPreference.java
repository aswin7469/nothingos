package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphsAppsNotificationsPreviewPreference extends Preference {
    public GlyphsAppsNotificationsPreviewPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.prefrence_apps_notifications_preview);
    }

    public GlyphsAppsNotificationsPreviewPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.prefrence_apps_notifications_preview);
    }

    public GlyphsAppsNotificationsPreviewPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.prefrence_apps_notifications_preview);
    }

    public GlyphsAppsNotificationsPreviewPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.prefrence_apps_notifications_preview);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
    }
}
