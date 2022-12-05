package android.content.pm;

import android.content.Context;
import android.content.ContextExt;
import android.content.pm.IPackageManagerExt;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
/* loaded from: classes.dex */
public final class NtPackageManager {
    private static final String REAL_STORAGE_PREFIX = "/data/media/";
    private static final String STORAGE_PREFIX = "/storage/emulated/";
    private static final String TAG = "PackageManagerExt";
    private static NtPackageManager mFpm = null;
    private static IPackageManagerExt mService;
    private Context mContext;

    private NtPackageManager(Context context) {
        IBinder b = ServiceManager.getService(ContextExt.NT_PACKAGE_MANAGER_SERVICE);
        mService = IPackageManagerExt.Stub.asInterface(b);
        this.mContext = context;
    }

    public static NtPackageManager getInstance(Context context) {
        if (mFpm == null) {
            synchronized (NtPackageManager.class) {
                if (mFpm == null || mService == null) {
                    mFpm = new NtPackageManager(context);
                }
                if (mService == null) {
                    mFpm = null;
                }
            }
        }
        return mFpm;
    }

    public boolean setForceFull(String packageName, boolean forceFull) {
        try {
            return mService.setForceFull(packageName, forceFull);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isForceFullForUid(int uid) {
        try {
            return mService.isForceFullForUid(uid);
        } catch (RemoteException e) {
            return true;
        }
    }
}
