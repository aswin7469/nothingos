package com.qti.extphone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.util.Log;
import com.qti.extphone.IExtPhone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExtTelephonyManager {
    public static final int ACCESS_MODE_PLMN = 1;
    public static final int ACCESS_MODE_SNPN = 2;
    public static final String ACTION_SHOW_NOTICE_SCM_BLOCK_OTHERS = "org.codeaurora.intent.action.SHOW_NOTICE_SCM_BLOCK_OTHERS";
    public static final String ACTION_SMS_CALLBACK_MODE_CHANGED = "org.codeaurora.intent.action.SMS_CALLBACK_MODE_CHANGED";
    private static final boolean DBG = true;
    public static final String EXTRA_PHONE_IN_SCM_STATE = "org.codeaurora.extra.PHONE_IN_SCM_STATE";
    private static final int INVALID = -1;
    private static final String LOG_TAG = "ExtTelephonyManager";
    public static final String SIM_STATE_ESSENTIAL_RECORDS_LOADED = "ESSENTIAL_LOADED";
    private static ExtTelephonyManager mInstance;
    private ExtTelephonyServiceConnection mConnection = new ExtTelephonyServiceConnection();
    private Context mContext;
    /* access modifiers changed from: private */
    public IExtPhone mExtTelephonyService = null;
    private List<ServiceCallback> mServiceCbs = Collections.synchronizedList(new ArrayList());
    /* access modifiers changed from: private */
    public AtomicBoolean mServiceConnected;

    public ExtTelephonyManager(Context context) {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        this.mServiceConnected = atomicBoolean;
        if (context != null) {
            this.mContext = context;
            atomicBoolean.set(false);
            log("ExtTelephonyManager() ...");
            return;
        }
        throw new IllegalArgumentException("Context is null");
    }

    public static synchronized ExtTelephonyManager getInstance(Context context) {
        ExtTelephonyManager extTelephonyManager;
        synchronized (ExtTelephonyManager.class) {
            synchronized (ExtTelephonyManager.class) {
                if (mInstance == null) {
                    if (context != null) {
                        mInstance = new ExtTelephonyManager(context.getApplicationContext());
                    } else {
                        throw new IllegalArgumentException("Context is null");
                    }
                }
                extTelephonyManager = mInstance;
            }
        }
        return extTelephonyManager;
    }

    public boolean isServiceConnected() {
        return this.mServiceConnected.get();
    }

    public boolean isFeatureSupported(int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.isFeatureSupported(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "isFeatureSupported, remote exception", e);
            return false;
        }
    }

    public boolean connectService(ServiceCallback serviceCallback) {
        if (isServiceConnected() || !this.mServiceCbs.isEmpty()) {
            addServiceCallback(serviceCallback);
            if (!isServiceConnected() || serviceCallback == null) {
                return true;
            }
            serviceCallback.onConnected();
            return true;
        }
        log("Creating ExtTelephonyService. If not started yet, start ...");
        addServiceCallback(serviceCallback);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.qti.phone", "com.qti.phone.ExtTelephonyService"));
        boolean bindService = this.mContext.bindService(intent, this.mConnection, 1);
        log("bind Service result: " + bindService);
        return bindService;
    }

    private void addServiceCallback(ServiceCallback serviceCallback) {
        if (serviceCallback != null && !this.mServiceCbs.contains(serviceCallback)) {
            this.mServiceCbs.add(serviceCallback);
        }
    }

    public void disconnectService() {
        disconnectService((ServiceCallback) null);
    }

    public void disconnectService(ServiceCallback serviceCallback) {
        if (serviceCallback != null) {
            if (!isServiceConnected()) {
                serviceCallback.onDisconnected();
            }
            if (this.mServiceCbs.contains(serviceCallback)) {
                this.mServiceCbs.remove((Object) serviceCallback);
            }
        }
        if (isServiceConnected() && this.mServiceCbs.isEmpty()) {
            this.mContext.unbindService(this.mConnection);
        }
    }

    /* access modifiers changed from: private */
    public void notifyConnected() {
        for (ServiceCallback onConnected : this.mServiceCbs) {
            onConnected.onConnected();
        }
    }

    /* access modifiers changed from: private */
    public void notifyDisconnected() {
        for (ServiceCallback onDisconnected : this.mServiceCbs) {
            onDisconnected.onDisconnected();
        }
    }

    private class ExtTelephonyServiceConnection implements ServiceConnection {
        private ExtTelephonyServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ExtTelephonyManager.this.mExtTelephonyService = IExtPhone.Stub.asInterface(iBinder);
            if (ExtTelephonyManager.this.mExtTelephonyService == null) {
                ExtTelephonyManager.this.log("ExtTelephonyService Connect Failed (onServiceConnected)... ");
            } else {
                ExtTelephonyManager.this.log("ExtTelephonyService connected ... ");
            }
            ExtTelephonyManager.this.mServiceConnected.set(true);
            ExtTelephonyManager.this.notifyConnected();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ExtTelephonyManager.this.log("The connection to the service got disconnected!");
            ExtTelephonyManager.this.mExtTelephonyService = null;
            ExtTelephonyManager.this.mServiceConnected.set(false);
            ExtTelephonyManager.this.notifyDisconnected();
        }
    }

    public int getPropertyValueInt(String str, int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return -1;
        }
        log("getPropertyValueInt: property=" + str);
        try {
            return this.mExtTelephonyService.getPropertyValueInt(str, i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getPropertyValueInt, remote exception", e);
            return -1;
        }
    }

    public boolean getPropertyValueBool(String str, boolean z) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return z;
        }
        log("getPropertyValueBool: property=" + str);
        try {
            return this.mExtTelephonyService.getPropertyValueBool(str, z);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getPropertyValueBool, remote exception", e);
            return z;
        }
    }

    public String getPropertyValueString(String str, String str2) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return str2;
        }
        log("getPropertyValueString: property=" + str);
        try {
            return this.mExtTelephonyService.getPropertyValueString(str, str2);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getPropertyValueString, remote exception", e);
            return str2;
        }
    }

    public boolean isPrimaryCarrierSlotId(int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.isPrimaryCarrierSlotId(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "isPrimaryCarrierSlotId, remote exception", e);
            return false;
        }
    }

    public int getCurrentPrimaryCardSlotId() {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return -1;
        }
        try {
            return this.mExtTelephonyService.getCurrentPrimaryCardSlotId();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getCurrentPrimaryCardSlotId, remote exception", e);
            return -1;
        }
    }

    public int getPrimaryCarrierSlotId() {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return -1;
        }
        try {
            return this.mExtTelephonyService.getPrimaryCarrierSlotId();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getPrimaryCarrierSlotId, remote exception", e);
            return -1;
        }
    }

    public void setPrimaryCardOnSlot(int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return;
        }
        try {
            this.mExtTelephonyService.setPrimaryCardOnSlot(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setPrimaryCardOnSlot, remote exception", e);
        }
    }

    public boolean performIncrementalScan(int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.performIncrementalScan(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "performIncrementalScan, remote exception", e);
            return false;
        }
    }

    public boolean abortIncrementalScan(int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.abortIncrementalScan(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "abortIncrementalScan, remote exception", e);
            return false;
        }
    }

    public boolean isSMSPromptEnabled() {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.isSMSPromptEnabled();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "isSMSPromptEnabled, remote exception", e);
            return false;
        }
    }

    public void setSMSPromptEnabled(boolean z) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return;
        }
        try {
            this.mExtTelephonyService.setSMSPromptEnabled(z);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setSMSPromptEnabled, remote exception", e);
        }
    }

    public void supplyIccDepersonalization(String str, String str2, IDepersoResCallback iDepersoResCallback, int i) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return;
        }
        try {
            this.mExtTelephonyService.supplyIccDepersonalization(str, str2, iDepersoResCallback, i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "supplyIccDepersonalization, remote exception", e);
        }
    }

    public Token enableEndc(int i, boolean z, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.enableEndc(i, z, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "enableEndc, remote exception", e);
            return null;
        }
    }

    public Token queryNrIconType(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryNrIconType(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryNrIconType, remote exception", e);
            return null;
        }
    }

    public Token queryEndcStatus(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryEndcStatus(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryEndcStatus, remote exception", e);
            return null;
        }
    }

    public Token setNrConfig(int i, NrConfig nrConfig, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.setNrConfig(i, nrConfig, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setNrConfig, remote exception", e);
            return null;
        }
    }

    public Token setNetworkSelectionModeAutomatic(int i, int i2, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.setNetworkSelectionModeAutomatic(i, i2, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setNetworkSelectionModeAutomatic, remote exception", e);
            return null;
        }
    }

    public Token getNetworkSelectionMode(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.getNetworkSelectionMode(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getNetworkSelectionMode, remote exception", e);
            return null;
        }
    }

    public Token queryNrConfig(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryNrConfig(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryNrConfig, remote exception", e);
            return null;
        }
    }

    public Token sendCdmaSms(int i, byte[] bArr, boolean z, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.sendCdmaSms(i, bArr, z, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "sendCdmaSms, remote exception", e);
            return null;
        }
    }

    public Token startNetworkScan(int i, NetworkScanRequest networkScanRequest, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.startNetworkScan(i, networkScanRequest, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "startNetworkScan, remote exception", e);
            return null;
        }
    }

    public Token stopNetworkScan(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.stopNetworkScan(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "stopNetworkScan, remote exception", e);
            return null;
        }
    }

    public Token setNetworkSelectionModeManual(int i, QtiSetNetworkSelectionMode qtiSetNetworkSelectionMode, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.setNetworkSelectionModeManual(i, qtiSetNetworkSelectionMode, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "startNetworkScan, remote exception", e);
            return null;
        }
    }

    public Token getQtiRadioCapability(int i, Client client) throws RemoteException {
        if (isServiceConnected()) {
            return this.mExtTelephonyService.getQtiRadioCapability(i, client);
        }
        Log.e(LOG_TAG, "service not connected!");
        return null;
    }

    public Token enable5g(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.enable5g(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "enable5g, remote exception", e);
            return null;
        }
    }

    public Token disable5g(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.disable5g(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "disable5g, remote exception", e);
            return null;
        }
    }

    public Token queryNrBearerAllocation(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryNrBearerAllocation(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryNrBearerAllocation, remote exception", e);
            return null;
        }
    }

    public Token setCarrierInfoForImsiEncryption(int i, ImsiEncryptionInfo imsiEncryptionInfo, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.setCarrierInfoForImsiEncryption(i, imsiEncryptionInfo, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setCarrierInfoForImsiEncryption, remote exception", e);
            return null;
        }
    }

    public Token enable5gOnly(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.enable5gOnly(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "enable5gOnly, remote exception", e);
            return null;
        }
    }

    public Token query5gStatus(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.query5gStatus(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "query5gStatus, remote exception", e);
            return null;
        }
    }

    public Token queryNrDcParam(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryNrDcParam(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryNrDcParam, remote exception", e);
            return null;
        }
    }

    public Token queryNrSignalStrength(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryNrSignalStrength(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryNrSignalStrength, remote exception", e);
            return null;
        }
    }

    public Token queryUpperLayerIndInfo(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.queryUpperLayerIndInfo(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "queryUpperLayerIndInfo, remote exception", e);
            return null;
        }
    }

    public Token query5gConfigInfo(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.query5gConfigInfo(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "query5gConfigInfo, remote exception", e);
            return null;
        }
    }

    public void queryCallForwardStatus(int i, int i2, int i3, String str, boolean z, Client client) throws RemoteException {
        try {
            this.mExtTelephonyService.queryCallForwardStatus(i, i2, i3, str, z, client);
        } catch (RemoteException unused) {
            throw new RemoteException("queryCallForwardStatus ended in remote exception");
        }
    }

    public void getFacilityLockForApp(int i, String str, String str2, int i2, String str3, boolean z, Client client) throws RemoteException {
        try {
            this.mExtTelephonyService.getFacilityLockForApp(i, str, str2, i2, str3, z, client);
        } catch (RemoteException unused) {
            throw new RemoteException("getFacilityLockForApp ended in remote exception");
        }
    }

    public QtiImeiInfo[] getImeiInfo() {
        try {
            return this.mExtTelephonyService.getImeiInfo();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getImeiInfo ended in remote exception", e);
            return null;
        }
    }

    public boolean isSmartDdsSwitchFeatureAvailable() throws RemoteException {
        try {
            return this.mExtTelephonyService.isSmartDdsSwitchFeatureAvailable();
        } catch (RemoteException unused) {
            throw new RemoteException("isSmartDdsSwitchFeatureAvailable ended in remote exception");
        }
    }

    public void setSmartDdsSwitchToggle(boolean z, Client client) throws RemoteException {
        try {
            this.mExtTelephonyService.setSmartDdsSwitchToggle(z, client);
        } catch (RemoteException unused) {
            throw new RemoteException("setSmartDdsSwitchToggle ended in remote exception");
        }
    }

    public Token getDdsSwitchCapability(int i, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.getDdsSwitchCapability(i, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getDdsSwitchCapability, remote exception", e);
            return null;
        }
    }

    public Token sendUserPreferenceForDataDuringVoiceCall(int i, boolean z, Client client) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.sendUserPreferenceForDataDuringVoiceCall(i, z, client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "sendUserPreferenceForDataDuringVoiceCall, remote exception", e);
            return null;
        }
    }

    public boolean setAirplaneMode(boolean z) throws RemoteException {
        try {
            return this.mExtTelephonyService.setAirplaneMode(z);
        } catch (RemoteException unused) {
            throw new RemoteException("setAirplaneMode ended in remote exception");
        }
    }

    public boolean getAirplaneMode() throws RemoteException {
        try {
            return this.mExtTelephonyService.getAirplaneMode();
        } catch (RemoteException unused) {
            throw new RemoteException("getAirplaneMode ended in remote exception");
        }
    }

    public boolean checkSimPinLockStatus(int i) throws RemoteException {
        try {
            return this.mExtTelephonyService.checkSimPinLockStatus(i);
        } catch (RemoteException unused) {
            throw new RemoteException("checkSimPinLockStatus ended in remote exception");
        }
    }

    public boolean toggleSimPinLock(int i, boolean z, String str) throws RemoteException {
        try {
            return this.mExtTelephonyService.toggleSimPinLock(i, z, str);
        } catch (RemoteException unused) {
            throw new RemoteException("toggleSimPinLock ended in remote exception");
        }
    }

    public boolean verifySimPin(int i, String str) throws RemoteException {
        try {
            return this.mExtTelephonyService.verifySimPin(i, str);
        } catch (RemoteException unused) {
            throw new RemoteException("verifySimPin ended in remote exception");
        }
    }

    public boolean verifySimPukChangePin(int i, String str, String str2) throws RemoteException {
        try {
            return this.mExtTelephonyService.verifySimPukChangePin(i, str, str2);
        } catch (RemoteException unused) {
            throw new RemoteException("verifySimPukChangePin ended in remote exception");
        }
    }

    public boolean isEpdgOverCellularDataSupported(int i) throws RemoteException {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return false;
        }
        try {
            return this.mExtTelephonyService.isEpdgOverCellularDataSupported(i);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "isEpdgOverCellularDataSupported, remote exception", e);
            return false;
        }
    }

    public Token getSecureModeStatus(Client client) throws RemoteException {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.getSecureModeStatus(client);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "getSecureModeStatus ended in remote exception", e);
            return null;
        }
    }

    public Token setMsimPreference(Client client, MsimPreference msimPreference) throws RemoteException {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.setMsimPreference(client, msimPreference);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "setMsimPreference ended in remote exception", e);
            return null;
        }
    }

    public Client registerCallback(String str, IExtPhoneCallback iExtPhoneCallback) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.registerCallback(str, iExtPhoneCallback);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "registerCallback, remote exception", e);
            return null;
        }
    }

    public void unRegisterCallback(IExtPhoneCallback iExtPhoneCallback) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "service not connected!");
            return;
        }
        try {
            this.mExtTelephonyService.unRegisterCallback(iExtPhoneCallback);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "unRegisterCallback, remote exception ", e);
        }
    }

    public Client registerQtiRadioConfigCallback(String str, IExtPhoneCallback iExtPhoneCallback) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "Service not connected!");
            return null;
        }
        try {
            return this.mExtTelephonyService.registerQtiRadioConfigCallback(str, iExtPhoneCallback);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "registerQtiRadioConfigCallback, remote exception", e);
            return null;
        }
    }

    public void unregisterQtiRadioConfigCallback(IExtPhoneCallback iExtPhoneCallback) {
        if (!isServiceConnected()) {
            Log.e(LOG_TAG, "Service not connected!");
            return;
        }
        try {
            this.mExtTelephonyService.unregisterQtiRadioConfigCallback(iExtPhoneCallback);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "unregisterQtiRadioConfigCallback, remote exception", e);
        }
    }

    /* access modifiers changed from: private */
    public void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
