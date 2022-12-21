package com.android.systemui.shortcut;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.shortcut.ShortcutKeyServiceProxy;
import javax.inject.Inject;

@SysUISingleton
public class ShortcutKeyDispatcher extends CoreStartable implements ShortcutKeyServiceProxy.Callbacks {
    private static final String TAG = "ShortcutKeyDispatcher";
    protected final long ALT_MASK = WifiManager.WIFI_FEATURE_CONNECTED_RAND_MAC;
    protected final long CTRL_MASK = WifiManager.WIFI_FEATURE_ADDITIONAL_STA_LOCAL_ONLY;
    protected final long META_MASK = WifiManager.WIFI_FEATURE_PASSPOINT_TERMS_AND_CONDITIONS;
    protected final long SC_DOCK_LEFT = 281474976710727L;
    protected final long SC_DOCK_RIGHT = 281474976710728L;
    protected final long SHIFT_MASK = WifiManager.WIFI_FEATURE_P2P_RAND_MAC;
    private ShortcutKeyServiceProxy mShortcutKeyServiceProxy = new ShortcutKeyServiceProxy(this);
    private IWindowManager mWindowManagerService = WindowManagerGlobal.getWindowManagerService();

    private void handleDockKey(long j) {
    }

    @Inject
    public ShortcutKeyDispatcher(Context context) {
        super(context);
    }

    public void registerShortcutKey(long j) {
        try {
            this.mWindowManagerService.registerShortcutKey(j, this.mShortcutKeyServiceProxy);
        } catch (RemoteException unused) {
        }
    }

    public void onShortcutKeyPressed(long j) {
        int i = this.mContext.getResources().getConfiguration().orientation;
        if ((j == 281474976710727L || j == 281474976710728L) && i == 2) {
            handleDockKey(j);
        }
    }

    public void start() {
        registerShortcutKey(281474976710727L);
        registerShortcutKey(281474976710728L);
    }
}
