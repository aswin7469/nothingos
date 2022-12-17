package com.android.settings.notification.app;

import android.content.Context;
import com.android.settingslib.core.AbstractPreferenceController;

class NoConversationsPreferenceController extends AbstractPreferenceController {
    private boolean mIsAvailable = false;

    public String getPreferenceKey() {
        return "no_conversations";
    }

    NoConversationsPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return this.mIsAvailable;
    }

    /* access modifiers changed from: package-private */
    public void setAvailable(boolean z) {
        this.mIsAvailable = z;
    }
}
