package com.android.systemui.recents;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.CommandQueue;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.p026io.PrintWriter;

public class Recents extends CoreStartable implements CommandQueue.Callbacks {
    private final CommandQueue mCommandQueue;
    private final RecentsImplementation mImpl;

    public Recents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        super(context);
        this.mImpl = recentsImplementation;
        this.mCommandQueue = commandQueue;
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mImpl.onStart(this.mContext);
    }

    public void onBootCompleted() {
        this.mImpl.onBootCompleted();
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mImpl.onConfigurationChanged(configuration);
    }

    public void appTransitionFinished(int i) {
        if (this.mContext.getDisplayId() == i) {
            this.mImpl.onAppTransitionFinished();
        }
    }

    public void showRecentApps(boolean z) {
        if (isUserSetup()) {
            this.mImpl.showRecentApps(z);
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        if (isUserSetup()) {
            this.mImpl.hideRecentApps(z, z2);
        }
    }

    public void toggleRecentApps() {
        if (isUserSetup()) {
            this.mImpl.toggleRecentApps();
        }
    }

    public void preloadRecentApps() {
        if (isUserSetup()) {
            this.mImpl.preloadRecentApps();
        }
    }

    public void cancelPreloadRecentApps() {
        if (isUserSetup()) {
            this.mImpl.cancelPreloadRecentApps();
        }
    }

    private boolean isUserSetup() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED, 0) == 0 || Settings.Secure.getInt(contentResolver, WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0) == 0) {
            return false;
        }
        return true;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mImpl.dump(printWriter);
    }
}
