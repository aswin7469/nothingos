package com.android.systemui.dreams;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.dreams.IDreamManager;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.qualifiers.Main;
import javax.inject.Inject;

public class DreamOverlayRegistrant extends CoreStartable {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "DreamOverlayRegistrant";
    private boolean mCurrentRegisteredState = false;
    private final IDreamManager mDreamManager;
    private final ComponentName mOverlayServiceComponent;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (DreamOverlayRegistrant.DEBUG) {
                Log.d(DreamOverlayRegistrant.TAG, "package changed receiver - onReceive");
            }
            DreamOverlayRegistrant.this.registerOverlayService();
        }
    };
    private final Resources mResources;

    /* access modifiers changed from: private */
    public void registerOverlayService() {
        PackageManager packageManager = this.mContext.getPackageManager();
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent);
        boolean z = false;
        if (componentEnabledSetting != 3) {
            int i = this.mResources.getBoolean(C1893R.bool.config_dreamOverlayServiceEnabled) ? 1 : 2;
            if (i != componentEnabledSetting) {
                packageManager.setComponentEnabledSetting(this.mOverlayServiceComponent, i, 0);
            }
        }
        if (packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent) == 1) {
            z = true;
        }
        if (this.mCurrentRegisteredState != z) {
            this.mCurrentRegisteredState = z;
            try {
                if (DEBUG) {
                    Log.d(TAG, z ? "registering dream overlay service:" + this.mOverlayServiceComponent : "clearing dream overlay service");
                }
                this.mDreamManager.registerDreamOverlayService(this.mCurrentRegisteredState ? this.mOverlayServiceComponent : null);
            } catch (RemoteException e) {
                Log.e(TAG, "could not register dream overlay service:" + e);
            }
        }
    }

    @Inject
    public DreamOverlayRegistrant(Context context, @Main Resources resources) {
        super(context);
        this.mResources = resources;
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        this.mOverlayServiceComponent = new ComponentName(this.mContext, DreamOverlayService.class);
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(this.mOverlayServiceComponent.getPackageName(), 0);
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        registerOverlayService();
    }
}
