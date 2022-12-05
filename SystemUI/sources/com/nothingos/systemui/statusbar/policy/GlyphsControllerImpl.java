package com.nothingos.systemui.statusbar.policy;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.nothingos.systemui.statusbar.policy.GlyphsController;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class GlyphsControllerImpl implements GlyphsController {
    private final ArrayList<GlyphsController.Callback> mCallback = new ArrayList<>();
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private boolean mEnabled;

    public GlyphsControllerImpl(Context context) {
        ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.nothingos.systemui.statusbar.policy.GlyphsControllerImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                boolean z2 = false;
                int i = Settings.Global.getInt(GlyphsControllerImpl.this.mContext.getContentResolver(), "led_effect_enable", 0);
                GlyphsControllerImpl glyphsControllerImpl = GlyphsControllerImpl.this;
                if (i == 1) {
                    z2 = true;
                }
                glyphsControllerImpl.mEnabled = z2;
                GlyphsControllerImpl.this.fireGlyphsStateChange();
            }
        };
        this.mContentObserver = contentObserver;
        this.mContext = context;
        boolean z = false;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, contentObserver);
        this.mEnabled = Settings.Global.getInt(context.getContentResolver(), "led_effect_enable", 0) == 1 ? true : z;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(GlyphsController.Callback callback) {
        if (!this.mCallback.contains(callback)) {
            this.mCallback.add(callback);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(GlyphsController.Callback callback) {
        this.mCallback.remove(callback);
    }

    @Override // com.nothingos.systemui.statusbar.policy.GlyphsController
    public void setGlyphsEnable() {
        this.mEnabled = !this.mEnabled;
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_effect_enable", this.mEnabled ? 1 : 0);
    }

    @Override // com.nothingos.systemui.statusbar.policy.GlyphsController
    public boolean getGlyphsEnabled() {
        return this.mEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireGlyphsStateChange() {
        Iterator<GlyphsController.Callback> it = this.mCallback.iterator();
        while (it.hasNext()) {
            it.next().onGlyphsChange();
        }
    }
}
