package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.android.systemui.dagger.SysUISingleton;
import com.nothing.systemui.statusbar.policy.GlyphsController;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class GlyphsControllerImpl implements GlyphsController {
    private static final String SETTINGS_KEY = "led_effect_enable";
    private final ArrayList<GlyphsController.Callback> mCallback = new ArrayList<>();
    private final ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public boolean mEnabled;

    @Inject
    public GlyphsControllerImpl(Context context) {
        C42381 r0 = new ContentObserver(new Handler()) {
            public void onChange(boolean z) {
                super.onChange(z);
                boolean z2 = false;
                int i = Settings.Global.getInt(GlyphsControllerImpl.this.mContext.getContentResolver(), GlyphsControllerImpl.SETTINGS_KEY, 0);
                GlyphsControllerImpl glyphsControllerImpl = GlyphsControllerImpl.this;
                if (i == 1) {
                    z2 = true;
                }
                boolean unused = glyphsControllerImpl.mEnabled = z2;
                GlyphsControllerImpl.this.fireGlyphsStateChange();
            }
        };
        this.mContentObserver = r0;
        this.mContext = context;
        boolean z = false;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor(SETTINGS_KEY), false, r0);
        this.mEnabled = Settings.Global.getInt(context.getContentResolver(), SETTINGS_KEY, 0) == 1 ? true : z;
    }

    public void addCallback(GlyphsController.Callback callback) {
        if (!this.mCallback.contains(callback)) {
            this.mCallback.add(callback);
        }
    }

    public void removeCallback(GlyphsController.Callback callback) {
        this.mCallback.remove((Object) callback);
    }

    public void setGlyphsEnable() {
        this.mEnabled = !this.mEnabled;
        Settings.Global.putInt(this.mContext.getContentResolver(), SETTINGS_KEY, this.mEnabled ? 1 : 0);
    }

    public boolean getGlyphsEnabled() {
        return this.mEnabled;
    }

    /* access modifiers changed from: private */
    public void fireGlyphsStateChange() {
        Iterator<GlyphsController.Callback> it = this.mCallback.iterator();
        while (it.hasNext()) {
            it.next().onGlyphsChange();
        }
    }
}
