package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.controls.IControlsProvider;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.settingslib.datetime.ZoneGetter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0012\u0010\t\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/controls/controller/ControlsProviderLifecycleManager$serviceConnection$1", "Landroid/content/ServiceConnection;", "onNullBinding", "", "name", "Landroid/content/ComponentName;", "onServiceConnected", "service", "Landroid/os/IBinder;", "onServiceDisconnected", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsProviderLifecycleManager.kt */
public final class ControlsProviderLifecycleManager$serviceConnection$1 implements ServiceConnection {
    final /* synthetic */ ControlsProviderLifecycleManager this$0;

    ControlsProviderLifecycleManager$serviceConnection$1(ControlsProviderLifecycleManager controlsProviderLifecycleManager) {
        this.this$0 = controlsProviderLifecycleManager;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Intrinsics.checkNotNullParameter(componentName, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(iBinder, NotificationCompat.CATEGORY_SERVICE);
        Log.d(this.this$0.TAG, "onServiceConnected " + componentName);
        this.this$0.bindTryCount = 0;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0;
        IControlsProvider asInterface = IControlsProvider.Stub.asInterface(iBinder);
        Intrinsics.checkNotNullExpressionValue(asInterface, "asInterface(service)");
        controlsProviderLifecycleManager.wrapper = new ServiceWrapper(asInterface);
        try {
            iBinder.linkToDeath(this.this$0, 0);
        } catch (RemoteException unused) {
        }
        this.this$0.handlePendingServiceMethods();
    }

    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(this.this$0.TAG, "onServiceDisconnected " + componentName);
        this.this$0.wrapper = null;
        this.this$0.bindService(false);
    }

    public void onNullBinding(ComponentName componentName) {
        Log.d(this.this$0.TAG, "onNullBinding " + componentName);
        this.this$0.wrapper = null;
        this.this$0.context.unbindService(this);
    }
}
