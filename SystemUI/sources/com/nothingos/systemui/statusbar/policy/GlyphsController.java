package com.nothingos.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes2.dex */
public interface GlyphsController extends CallbackController<Callback> {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onGlyphsChange();
    }

    boolean getGlyphsEnabled();

    void setGlyphsEnable();
}
