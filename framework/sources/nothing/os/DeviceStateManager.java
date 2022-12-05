package nothing.os;

import android.content.Context;
import android.content.ContextExt;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Singleton;
import nothing.os.IDeviceStateService;
/* loaded from: classes4.dex */
public class DeviceStateManager {
    private static final Singleton<IDeviceStateService> IDeviceStateServiceSingleton = new Singleton<IDeviceStateService>() { // from class: nothing.os.DeviceStateManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.Singleton
        /* renamed from: create  reason: collision with other method in class */
        public IDeviceStateService mo3561create() {
            IBinder b = ServiceManager.getService(ContextExt.DEVICE_STATE_SERVICE);
            IDeviceStateService ds = IDeviceStateService.Stub.asInterface(b);
            return ds;
        }
    };
    private static final String TAG = "DeviceStateManager";
    public Context mContext;

    public DeviceStateManager(Context context, IDeviceStateService service) {
        this.mContext = context;
    }

    public boolean isCtsRunning() {
        return isCtsRunningStatic();
    }

    public static boolean isCtsRunningStatic() {
        try {
            if (getService() == null) {
                long originCallingId = Binder.clearCallingIdentity();
                boolean result = SystemProperties.getBoolean("persist.sys.cts_state", false);
                Binder.restoreCallingIdentity(originCallingId);
                Log.e(TAG, "because service is null so get result directly ,result = " + result);
                return result;
            }
            return getService().isCtsRunning();
        } catch (RemoteException e) {
            return false;
        }
    }

    private static IDeviceStateService getService() {
        return IDeviceStateServiceSingleton.get();
    }
}
