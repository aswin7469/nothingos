package com.qti.extphone;

import android.os.RemoteException;
import android.telephony.CellInfo;
import android.util.Log;
import com.qti.extphone.IExtPhoneCallback;
import java.util.List;

public class ExtPhoneCallbackBase extends IExtPhoneCallback.Stub {
    private static final String TAG = "ExtPhoneCallbackBase";

    public void getNetworkSelectionModeResponse(int i, Token token, Status status, NetworkSelectionMode networkSelectionMode) throws RemoteException {
    }

    public void getSecureModeStatusResponse(Token token, Status status, boolean z) throws RemoteException {
    }

    public void networkScanResult(int i, Token token, int i2, int i3, List<CellInfo> list) throws RemoteException {
    }

    public void onDataDeactivateDelayTime(int i, long j) throws RemoteException {
    }

    public void onDdsSwitchCapabilityChange(int i, Token token, Status status, boolean z) throws RemoteException {
    }

    public void onDdsSwitchCriteriaChange(int i, boolean z) throws RemoteException {
    }

    public void onDdsSwitchRecommendation(int i, int i2) throws RemoteException {
    }

    public void onEpdgOverCellularDataSupported(int i, boolean z) throws RemoteException {
    }

    public void onImeiTypeChanged(QtiImeiInfo[] qtiImeiInfoArr) throws RemoteException {
    }

    public void onSecureModeStatusChange(boolean z) throws RemoteException {
    }

    public void onSendUserPreferenceForDataDuringVoiceCall(int i, Token token, Status status) throws RemoteException {
    }

    public void setMsimPreferenceResponse(Token token, Status status) throws RemoteException {
    }

    public void setNetworkSelectionModeAutomaticResponse(int i, Token token, int i2) throws RemoteException {
    }

    public void setNetworkSelectionModeManualResponse(int i, Token token, int i2) throws RemoteException {
    }

    public void startNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
    }

    public void stopNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
    }

    public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onNrIconType: slotId = " + i + " token = " + token + " status = " + status + " NrIconType = " + nrIconType);
    }

    public void onEnableEndc(int i, Token token, Status status) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onEnableEndc: slotId = " + i + " token = " + token + " status = " + status);
    }

    public void onEndcStatus(int i, Token token, Status status, boolean z) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onEndcStatus: slotId = " + i + " token = " + token + " status = " + status + " enableStatus = " + z);
    }

    public void onSetNrConfig(int i, Token token, Status status) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onSetNrConfig: slotId = " + i + " token = " + token + " status = " + status);
    }

    public void onNrConfigStatus(int i, Token token, Status status, NrConfig nrConfig) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onNrConfigStatus: slotId = " + i + " token = " + token + " status = " + status + " NrConfig = " + nrConfig);
    }

    public void sendCdmaSmsResponse(int i, Token token, Status status, SmsResult smsResult) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: sendCdmaSmsResponse: slotId = " + i + " token = " + token + " status = " + status + " SmsResult = " + smsResult);
    }

    public void on5gStatus(int i, Token token, Status status, boolean z) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: on5gStatus: slotId = " + i + " token = " + token + " status" + status + " enableStatus = " + z);
    }

    public void onAnyNrBearerAllocation(int i, Token token, Status status, BearerAllocationStatus bearerAllocationStatus) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onNrBearerAllocationChange: slotId = " + i + " token = " + token + " status = " + status + " bearerStatus = " + bearerAllocationStatus);
    }

    public void getQtiRadioCapabilityResponse(int i, Token token, Status status, int i2) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: getQtiRadioCapabilityResponse: slotId = " + i + " token = " + token + " status" + status + " raf = " + i2);
    }

    public void onNrDcParam(int i, Token token, Status status, DcParam dcParam) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onNrDcParam: slotId = " + i + " token = " + token + " status" + status + " dcParam = " + dcParam);
    }

    public void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onUpperLayerIndInfo: slotId = " + i + " token = " + token + " status" + status + " UpperLayerIndInfo = " + upperLayerIndInfo);
    }

    public void on5gConfigInfo(int i, Token token, Status status, NrConfigType nrConfigType) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: on5gConfigInfo: slotId = " + i + " token = " + token + " status" + status + " NrConfigType = " + nrConfigType);
    }

    public void onSignalStrength(int i, Token token, Status status, SignalStrength signalStrength) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: onSignalStrength: slotId = " + i + " token = " + token + " status" + status + " signalStrength = " + signalStrength);
    }

    public void setCarrierInfoForImsiEncryptionResponse(int i, Token token, QRadioResponseInfo qRadioResponseInfo) throws RemoteException {
        Log.d(TAG, "UNIMPLEMENTED: setCarrierInfoForImsiEncryptionResponse: slotId = " + i + " token = " + token + " info = " + qRadioResponseInfo);
    }

    public void queryCallForwardStatusResponse(Status status, QtiCallForwardInfo[] qtiCallForwardInfoArr) throws RemoteException {
        Log.d(TAG, "queryCallForwardStatusResponse: status = " + status + " CallForwardInfo = " + qtiCallForwardInfoArr);
    }

    public void getFacilityLockForAppResponse(Status status, int[] iArr) throws RemoteException {
        Log.d(TAG, "getFacilityLockForAppResponse: status = " + status + " response = " + iArr);
    }

    public void setSmartDdsSwitchToggleResponse(Token token, boolean z) throws RemoteException {
        Log.d(TAG, "setSmartDdsSwitchToggleResponse: token = " + token + " result = " + z);
    }
}
