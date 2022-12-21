package com.android.systemui.p012qs.external;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.external.PackageManagerAdapter */
public class PackageManagerAdapter {
    private static final String TAG = "PackageManagerAdapter";
    private IPackageManager mIPackageManager = AppGlobals.getPackageManager();
    private PackageManager mPackageManager;

    @Inject
    public PackageManagerAdapter(Context context) {
        this.mPackageManager = context.getPackageManager();
    }

    public ServiceInfo getServiceInfo(ComponentName componentName, int i, int i2) throws RemoteException {
        return this.mIPackageManager.getServiceInfo(componentName, (long) i, i2);
    }

    public ServiceInfo getServiceInfo(ComponentName componentName, int i) throws PackageManager.NameNotFoundException {
        return this.mPackageManager.getServiceInfo(componentName, i);
    }

    public PackageInfo getPackageInfoAsUser(String str, int i, int i2) throws PackageManager.NameNotFoundException {
        return this.mPackageManager.getPackageInfoAsUser(str, i, i2);
    }
}
