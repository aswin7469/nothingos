package com.android.systemui.recents;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.globalactions.C2137x58d1e4b7;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public class OverviewProxyRecentsImpl implements RecentsImplementation {
    private static final String TAG = "OverviewProxyRecentsImpl";
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private Handler mHandler;
    private final OverviewProxyService mOverviewProxyService;

    @Inject
    public OverviewProxyRecentsImpl(Lazy<Optional<CentralSurfaces>> lazy, OverviewProxyService overviewProxyService) {
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mOverviewProxyService = overviewProxyService;
    }

    public void onStart(Context context) {
        this.mHandler = new Handler();
    }

    public void showRecentApps(boolean z) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewShown(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to send overview show event to launcher.", e);
            }
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewHidden(z, z2);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to send overview hide event to launcher.", e);
            }
        }
    }

    public void toggleRecentApps() {
        if (this.mOverviewProxyService.getProxy() != null) {
            OverviewProxyRecentsImpl$$ExternalSyntheticLambda0 overviewProxyRecentsImpl$$ExternalSyntheticLambda0 = new OverviewProxyRecentsImpl$$ExternalSyntheticLambda0(this);
            Optional optional = this.mCentralSurfacesOptionalLazy.get();
            if (((Boolean) optional.map(new C2137x58d1e4b7()).orElse(false)).booleanValue()) {
                ((CentralSurfaces) optional.get()).executeRunnableDismissingKeyguard(new OverviewProxyRecentsImpl$$ExternalSyntheticLambda1(this, overviewProxyRecentsImpl$$ExternalSyntheticLambda0), (Runnable) null, true, false, true);
            } else {
                overviewProxyRecentsImpl$$ExternalSyntheticLambda0.run();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$toggleRecentApps$0$com-android-systemui-recents-OverviewProxyRecentsImpl */
    public /* synthetic */ void mo37119xd8f0a03e() {
        try {
            if (this.mOverviewProxyService.getProxy() != null) {
                this.mOverviewProxyService.getProxy().onOverviewToggle();
                this.mOverviewProxyService.notifyToggleRecentApps();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot send toggle recents through proxy service.", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$toggleRecentApps$1$com-android-systemui-recents-OverviewProxyRecentsImpl */
    public /* synthetic */ void mo37120x244f57f(Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
