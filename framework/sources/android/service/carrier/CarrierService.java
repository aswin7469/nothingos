package android.service.carrier;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.service.carrier.ICarrierService;
import android.telephony.TelephonyRegistryManager;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public abstract class CarrierService extends Service {
    public static final String CARRIER_SERVICE_INTERFACE = "android.service.carrier.CarrierService";
    private static final String LOG_TAG = "CarrierService";
    private final ICarrierService.Stub mStubWrapper = new ICarrierServiceWrapper();

    public abstract PersistableBundle onLoadConfig(CarrierIdentifier carrierIdentifier);

    public final void notifyCarrierNetworkChange(boolean active) {
        TelephonyRegistryManager telephonyRegistryMgr = (TelephonyRegistryManager) getSystemService(Context.TELEPHONY_REGISTRY_SERVICE);
        if (telephonyRegistryMgr != null) {
            telephonyRegistryMgr.notifyCarrierNetworkChange(active);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mStubWrapper;
    }

    /* loaded from: classes2.dex */
    public class ICarrierServiceWrapper extends ICarrierService.Stub {
        public static final String KEY_CONFIG_BUNDLE = "config_bundle";
        public static final int RESULT_ERROR = 1;
        public static final int RESULT_OK = 0;

        public ICarrierServiceWrapper() {
        }

        @Override // android.service.carrier.ICarrierService
        public void getCarrierConfig(CarrierIdentifier id, ResultReceiver result) {
            try {
                Bundle data = new Bundle();
                data.putParcelable(KEY_CONFIG_BUNDLE, CarrierService.this.onLoadConfig(id));
                result.send(0, data);
            } catch (Exception e) {
                Log.e(CarrierService.LOG_TAG, "Error in onLoadConfig: " + e.getMessage(), e);
                result.send(1, null);
            }
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
            CarrierService.this.dump(fd, pw, args);
        }
    }
}
