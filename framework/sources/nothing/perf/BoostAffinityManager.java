package nothing.perf;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
/* loaded from: classes4.dex */
public class BoostAffinityManager {
    private static final String SERVICE_NAME = "boost_affinity_service";
    private static final String TAG = "BoostAffinityManager";
    private final int PERF_HINT_TRANSACTION_CODE;
    private static final boolean DEBUG = SystemProperties.getBoolean("debug.boostaffinity.log", false);
    protected static IBinder mService = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class BoostAffinityManagerBuilder {
        static BoostAffinityManager sBoostAffinityManager = new BoostAffinityManager();

        private BoostAffinityManagerBuilder() {
        }
    }

    public static BoostAffinityManager getInstance() {
        return BoostAffinityManagerBuilder.sBoostAffinityManager;
    }

    private BoostAffinityManager() {
        this.PERF_HINT_TRANSACTION_CODE = 1001;
        Log.i(TAG, "BoostAffinityManager() called");
        bindServerIfNeeded();
    }

    private void bindServerIfNeeded() {
        if (mService != null) {
            return;
        }
        synchronized (this) {
            if (mService == null) {
                boolean z = DEBUG;
                if (z) {
                    Log.d(TAG, "Need bind server!");
                }
                try {
                    IBinder svc = ServiceManager.checkService(SERVICE_NAME);
                    if (svc != null) {
                        mService = svc;
                    } else {
                        mService = null;
                        if (z) {
                            Log.w(TAG, "Check boost_affinity_service failed!");
                        }
                    }
                    IBinder iBinder = mService;
                    if (iBinder != null) {
                        iBinder.linkToDeath(new BAServerDeathRecipient(), 0);
                    }
                } catch (Exception e) {
                    mService = null;
                    Log.w(TAG, "Bind server exception " + e);
                }
            }
        }
    }

    public int perfHint(int hint, String userDataStr, int userData1, int userData2) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("perfHint() called with: hint = [");
            sb.append(hint);
            sb.append("], userDataStr = [");
            sb.append(userDataStr == null ? "null" : userDataStr);
            sb.append("]userData1=");
            sb.append(userData1);
            sb.append(",userData2=");
            sb.append(userData2);
            Log.d(TAG, sb.toString());
        }
        int ret = -1;
        bindServerIfNeeded();
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            if (mService != null) {
                try {
                    _data.writeInterfaceToken("com.nothing.perf.sdk.IBoostAffinity");
                    _data.writeInt(hint);
                    _data.writeString(userDataStr);
                    _data.writeInt(userData1);
                    _data.writeInt(userData2);
                    mService.transact(1001, _data, _reply, 0);
                    _reply.readException();
                    ret = _reply.readInt();
                } catch (RemoteException e) {
                    Log.w(TAG, "RemoteException " + e);
                }
            }
            return ret;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class BAServerDeathRecipient implements IBinder.DeathRecipient {
        private BAServerDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.w(BoostAffinityManager.TAG, "boost_affinity_service binder died!!!");
            synchronized (BoostAffinityManager.this) {
                BoostAffinityManager.mService.unlinkToDeath(this, 0);
                BoostAffinityManager.mService = null;
            }
        }
    }
}
