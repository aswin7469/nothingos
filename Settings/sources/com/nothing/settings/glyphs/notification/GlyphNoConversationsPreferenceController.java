package com.nothing.settings.glyphs.notification;

import android.content.Context;
import com.android.settingslib.core.AbstractPreferenceController;

class GlyphNoConversationsPreferenceController extends AbstractPreferenceController {
    private boolean mIsAvailable = false;

    public String getPreferenceKey() {
        return "no_conversations";
    }

    GlyphNoConversationsPreferenceController(Context context) {
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
