package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.rtt.IRttCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.WorkSource;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.util.List;
import java.util.concurrent.Executor;

public class WifiRttManager {
    public static final String ACTION_WIFI_RTT_STATE_CHANGED = "android.net.wifi.rtt.action.WIFI_RTT_STATE_CHANGED";
    private static final String TAG = "WifiRttManager";
    private static final boolean VDBG = false;
    private final Context mContext;
    private final IWifiRttManager mService;

    public WifiRttManager(Context context, IWifiRttManager iWifiRttManager) {
        this.mContext = context;
        this.mService = iWifiRttManager;
    }

    public boolean isAvailable() {
        try {
            return this.mService.isAvailable();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startRanging(RangingRequest rangingRequest, Executor executor, RangingResultCallback rangingResultCallback) {
        startRanging((WorkSource) null, rangingRequest, executor, rangingResultCallback);
    }

    @SystemApi
    public void startRanging(WorkSource workSource, RangingRequest rangingRequest, final Executor executor, final RangingResultCallback rangingResultCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("Null executor provided");
        } else if (rangingResultCallback != null) {
            Binder binder = new Binder();
            try {
                Bundle bundle = new Bundle();
                if (SdkLevel.isAtLeastS()) {
                    bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                }
                this.mService.startRanging(binder, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), workSource, rangingRequest, new IRttCallback.Stub() {
                    public void onRangingFailure(int i) throws RemoteException {
                        clearCallingIdentity();
                        executor.execute(new WifiRttManager$1$$ExternalSyntheticLambda1(rangingResultCallback, i));
                    }

                    public void onRangingResults(List<RangingResult> list) throws RemoteException {
                        clearCallingIdentity();
                        executor.execute(new WifiRttManager$1$$ExternalSyntheticLambda0(rangingResultCallback, list));
                    }
                }, bundle);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Null callback provided");
        }
    }

    @SystemApi
    public void cancelRanging(WorkSource workSource) {
        try {
            this.mService.cancelRanging(workSource);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
