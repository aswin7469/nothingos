package com.nothing.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;

public interface GlyphsController extends CallbackController<Callback> {

    public interface Callback {
        void onGlyphsChange();
    }

    boolean getGlyphsEnabled();

    void setGlyphsEnable();
}
