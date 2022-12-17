package com.android.settings.deviceinfo;

import android.content.Context;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;

public class ImsConnector implements FeatureConnector.Listener<ImsManager> {
    private static final String TAG = ImsConnector.class.getSimpleName();
    private FeatureConnector<ImsManager> mConnector;
    private Context mContext;
    private ImsManager mImsManager;
    private ImsMmTelManager.RegistrationCallback mRegistrationCallback;
    private int mSlotId;

    public ImsConnector(Context context, int i, ImsMmTelManager.RegistrationCallback registrationCallback) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mSlotId = i;
        this.mRegistrationCallback = registrationCallback;
        this.mConnector = ImsManager.getConnector(applicationContext, i, TAG, this, applicationContext.getMainExecutor());
    }

    public void connect() {
        FeatureConnector<ImsManager> featureConnector = this.mConnector;
        if (featureConnector != null) {
            featureConnector.connect();
        }
    }

    public void disconnect() {
        FeatureConnector<ImsManager> featureConnector = this.mConnector;
        if (featureConnector != null) {
            featureConnector.disconnect();
        }
    }

    public void connectionReady(ImsManager imsManager, int i) throws ImsException {
        this.mImsManager = imsManager;
        registerListener();
    }

    public void connectionUnavailable(int i) {
        unregisterListener();
    }

    private void registerListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            Log.e(TAG, "registerListener: mImsManager is null");
            return;
        }
        try {
            imsManager.addRegistrationCallback(this.mRegistrationCallback, this.mContext.getMainExecutor());
            String str = TAG;
            Log.d(str, "registerListener: add callback for mSlotId = " + this.mSlotId + " mImsManager = " + this.mImsManager);
        } catch (ImsException e) {
            Log.e(TAG, "registerListener: ", e);
        }
    }

    private void unregisterListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            Log.e(TAG, "unregisterListener: mImsManager is null");
            return;
        }
        imsManager.removeRegistrationListener(this.mRegistrationCallback);
        String str = TAG;
        Log.d(str, "unregisterListener: remove ims registration callback for mSlotId = " + this.mSlotId + " mImsManager = " + this.mImsManager);
    }
}
