package com.android.systemui.statusbar.p013tv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.CoreStartable;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.CommandQueue;
import dagger.Lazy;
import javax.inject.Inject;

@SysUISingleton
/* renamed from: com.android.systemui.statusbar.tv.TvStatusBar */
public class TvStatusBar extends CoreStartable implements CommandQueue.Callbacks {
    private static final String ACTION_SHOW_PIP_MENU = "com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU";
    private static final String SYSTEMUI_PERMISSION = "com.android.systemui.permission.SELF";
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final CommandQueue mCommandQueue;

    @Inject
    public TvStatusBar(Context context, CommandQueue commandQueue, Lazy<AssistManager> lazy) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mAssistManagerLazy = lazy;
    }

    public void start() {
        IStatusBarService asInterface = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        try {
            asInterface.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException unused) {
        }
    }

    public void startAssist(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }

    public void showPictureInPictureMenu() {
        this.mContext.sendBroadcast(new Intent(ACTION_SHOW_PIP_MENU).setPackage(this.mContext.getPackageName()), "com.android.systemui.permission.SELF");
    }
}
