package com.nothing.settings.gestures;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R$layout;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class NtSelectorWithWidgetPreference extends SelectorWithWidgetPreference {
    public NtSelectorWithWidgetPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public NtSelectorWithWidgetPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public NtSelectorWithWidgetPreference(Context context, boolean z) {
        super(context, z);
        init();
    }

    public NtSelectorWithWidgetPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutResource(R$layout.nt_preference_selector_with_widget);
    }
}
