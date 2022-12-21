package com.android.settingslib.core.instrumentation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import androidx.preference.Preference;
import java.util.ArrayList;
import java.util.List;

public class MetricsFeatureProvider {
    public static final String EXTRA_SOURCE_METRICS_CATEGORY = ":settings:source_metrics";
    protected List<LogWriter> mLoggerWriters = new ArrayList();

    public MetricsFeatureProvider() {
        installLogWriters();
    }

    /* access modifiers changed from: protected */
    public void installLogWriters() {
        this.mLoggerWriters.add(new EventLogWriter());
    }

    public int getAttribution(Activity activity) {
        Intent intent;
        if (activity == null || (intent = activity.getIntent()) == null) {
            return 0;
        }
        return intent.getIntExtra(EXTRA_SOURCE_METRICS_CATEGORY, 0);
    }

    public void visible(Context context, int i, int i2, int i3) {
        for (LogWriter visible : this.mLoggerWriters) {
            visible.visible(context, i, i2, i3);
        }
    }

    public void hidden(Context context, int i, int i2) {
        for (LogWriter hidden : this.mLoggerWriters) {
            hidden.hidden(context, i, i2);
        }
    }

    public void action(Context context, int i, Pair<Integer, Object>... pairArr) {
        for (LogWriter action : this.mLoggerWriters) {
            action.action(context, i, pairArr);
        }
    }

    public void action(Context context, int i, String str) {
        for (LogWriter action : this.mLoggerWriters) {
            action.action(context, i, str);
        }
    }

    public void action(int i, int i2, int i3, String str, int i4) {
        for (LogWriter action : this.mLoggerWriters) {
            action.action(i, i2, i3, str, i4);
        }
    }

    public void action(Context context, int i, int i2) {
        for (LogWriter action : this.mLoggerWriters) {
            action.action(context, i, i2);
        }
    }

    public void action(Context context, int i, boolean z) {
        for (LogWriter action : this.mLoggerWriters) {
            action.action(context, i, z);
        }
    }

    public int getMetricsCategory(Object obj) {
        if (obj == null || !(obj instanceof Instrumentable)) {
            return 0;
        }
        return ((Instrumentable) obj).getMetricsCategory();
    }

    public boolean logClickedPreference(Preference preference, int i) {
        if (preference == null) {
            return false;
        }
        if (logSettingsTileClick(preference.getKey(), i) || logStartedIntent(preference.getIntent(), i) || logSettingsTileClick(preference.getFragment(), i)) {
            return true;
        }
        return false;
    }

    public boolean logStartedIntent(Intent intent, int i) {
        if (intent == null) {
            return false;
        }
        ComponentName component = intent.getComponent();
        return logSettingsTileClick(component != null ? component.flattenToString() : intent.getAction(), i);
    }

    public boolean logStartedIntentWithProfile(Intent intent, int i, boolean z) {
        if (intent == null) {
            return false;
        }
        ComponentName component = intent.getComponent();
        return logSettingsTileClick((component != null ? component.flattenToString() : intent.getAction()) + (z ? "/work" : "/personal"), i);
    }

    public boolean logSettingsTileClick(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        action(i, 830, 0, str, 0);
        return true;
    }
}