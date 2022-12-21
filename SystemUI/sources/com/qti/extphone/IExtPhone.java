package com.qti.extphone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import com.qti.extphone.IDepersoResCallback;
import com.qti.extphone.IExtPhoneCallback;

public interface IExtPhone extends IInterface {
    public static final String DESCRIPTOR = "com.qti.extphone.IExtPhone";

    public static class Default implements IExtPhone {
        public boolean abortIncrementalScan(int i) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }

        public boolean checkSimPinLockStatus(int i) throws RemoteException {
            return false;
        }

        public Token disable5g(int i, Client client) throws RemoteException {
            return null;
        }

        public Token enable5g(int i, Client client) throws RemoteException {
            return null;
        }

        public Token enable5gOnly(int i, Client client) throws RemoteException {
            return null;
        }

        public Token enableEndc(int i, boolean z, Client client) throws RemoteException {
            return null;
        }

        public boolean getAirplaneMode() throws RemoteException {
            return false;
        }

        public int getCurrentPrimaryCardSlotId() throws RemoteException {
            return 0;
        }

        public Token getDdsSwitchCapability(int i, Client client) throws RemoteException {
            return null;
        }

        public void getFacilityLockForApp(int i, String str, String str2, int i2, String str3, boolean z, Client client) throws RemoteException {
        }

        public QtiImeiInfo[] getImeiInfo() throws RemoteException {
            return null;
        }

        public Token getNetworkSelectionMode(int i, Client client) throws RemoteException {
            return null;
        }

        public int getPrimaryCarrierSlotId() throws RemoteException {
            return 0;
        }

        public boolean getPropertyValueBool(String str, boolean z) throws RemoteException {
            return false;
        }

        public int getPropertyValueInt(String str, int i) throws RemoteException {
            return 0;
        }

        public String getPropertyValueString(String str, String str2) throws RemoteException {
            return null;
        }

        public Token getQtiRadioCapability(int i, Client client) throws RemoteException {
            return null;
        }

        public Token getSecureModeStatus(Client client) throws RemoteException {
            return null;
        }

        public boolean isEpdgOverCellularDataSupported(int i) throws RemoteException {
            return false;
        }

        public boolean isFeatureSupported(int i) throws RemoteException {
            return false;
        }

        public boolean isPrimaryCarrierSlotId(int i) throws RemoteException {
            return false;
        }

        public boolean isSMSPromptEnabled() throws RemoteException {
            return false;
        }

        public boolean isSmartDdsSwitchFeatureAvailable() throws RemoteException {
            return false;
        }

        public boolean performIncrementalScan(int i) throws RemoteException {
            return false;
        }

        public Token query5gConfigInfo(int i, Client client) throws RemoteException {
            return null;
        }

        public Token query5gStatus(int i, Client client) throws RemoteException {
            return null;
        }

        public void queryCallForwardStatus(int i, int i2, int i3, String str, boolean z, Client client) throws RemoteException {
        }

        public Token queryEndcStatus(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryNrBearerAllocation(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryNrConfig(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryNrDcParam(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryNrIconType(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryNrSignalStrength(int i, Client client) throws RemoteException {
            return null;
        }

        public Token queryUpperLayerIndInfo(int i, Client client) throws RemoteException {
            return null;
        }

        public Client registerCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
            return null;
        }

        public Client registerQtiRadioConfigCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
            return null;
        }

        public Token sendCdmaSms(int i, byte[] bArr, boolean z, Client client) throws RemoteException {
            return null;
        }

        public Token sendUserPreferenceForDataDuringVoiceCall(int i, boolean z, Client client) throws RemoteException {
            return null;
        }

        public boolean setAirplaneMode(boolean z) throws RemoteException {
            return false;
        }

        public Token setCarrierInfoForImsiEncryption(int i, ImsiEncryptionInfo imsiEncryptionInfo, Client client) throws RemoteException {
            return null;
        }

        public Token setMsimPreference(Client client, MsimPreference msimPreference) throws RemoteException {
            return null;
        }

        public Token setNetworkSelectionModeAutomatic(int i, int i2, Client client) throws RemoteException {
            return null;
        }

        public Token setNetworkSelectionModeManual(int i, QtiSetNetworkSelectionMode qtiSetNetworkSelectionMode, Client client) throws RemoteException {
            return null;
        }

        public Token setNrConfig(int i, NrConfig nrConfig, Client client) throws RemoteException {
            return null;
        }

        public void setPrimaryCardOnSlot(int i) throws RemoteException {
        }

        public void setSMSPromptEnabled(boolean z) throws RemoteException {
        }

        public void setSmartDdsSwitchToggle(boolean z, Client client) throws RemoteException {
        }

        public Token startNetworkScan(int i, NetworkScanRequest networkScanRequest, Client client) throws RemoteException {
            return null;
        }

        public Token stopNetworkScan(int i, Client client) throws RemoteException {
            return null;
        }

        public void supplyIccDepersonalization(String str, String str2, IDepersoResCallback iDepersoResCallback, int i) throws RemoteException {
        }

        public boolean toggleSimPinLock(int i, boolean z, String str) throws RemoteException {
            return false;
        }

        public void unRegisterCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
        }

        public void unregisterQtiRadioConfigCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
        }

        public boolean verifySimPin(int i, String str) throws RemoteException {
            return false;
        }

        public boolean verifySimPukChangePin(int i, String str, String str2) throws RemoteException {
            return false;
        }
    }

    boolean abortIncrementalScan(int i) throws RemoteException;

    boolean checkSimPinLockStatus(int i) throws RemoteException;

    @Deprecated
    Token disable5g(int i, Client client) throws RemoteException;

    @Deprecated
    Token enable5g(int i, Client client) throws RemoteException;

    @Deprecated
    Token enable5gOnly(int i, Client client) throws RemoteException;

    Token enableEndc(int i, boolean z, Client client) throws RemoteException;

    boolean getAirplaneMode() throws RemoteException;

    int getCurrentPrimaryCardSlotId() throws RemoteException;

    Token getDdsSwitchCapability(int i, Client client) throws RemoteException;

    void getFacilityLockForApp(int i, String str, String str2, int i2, String str3, boolean z, Client client) throws RemoteException;

    QtiImeiInfo[] getImeiInfo() throws RemoteException;

    Token getNetworkSelectionMode(int i, Client client) throws RemoteException;

    int getPrimaryCarrierSlotId() throws RemoteException;

    boolean getPropertyValueBool(String str, boolean z) throws RemoteException;

    int getPropertyValueInt(String str, int i) throws RemoteException;

    String getPropertyValueString(String str, String str2) throws RemoteException;

    Token getQtiRadioCapability(int i, Client client) throws RemoteException;

    Token getSecureModeStatus(Client client) throws RemoteException;

    boolean isEpdgOverCellularDataSupported(int i) throws RemoteException;

    boolean isFeatureSupported(int i) throws RemoteException;

    boolean isPrimaryCarrierSlotId(int i) throws RemoteException;

    boolean isSMSPromptEnabled() throws RemoteException;

    boolean isSmartDdsSwitchFeatureAvailable() throws RemoteException;

    boolean performIncrementalScan(int i) throws RemoteException;

    @Deprecated
    Token query5gConfigInfo(int i, Client client) throws RemoteException;

    @Deprecated
    Token query5gStatus(int i, Client client) throws RemoteException;

    void queryCallForwardStatus(int i, int i2, int i3, String str, boolean z, Client client) throws RemoteException;

    Token queryEndcStatus(int i, Client client) throws RemoteException;

    @Deprecated
    Token queryNrBearerAllocation(int i, Client client) throws RemoteException;

    Token queryNrConfig(int i, Client client) throws RemoteException;

    @Deprecated
    Token queryNrDcParam(int i, Client client) throws RemoteException;

    Token queryNrIconType(int i, Client client) throws RemoteException;

    @Deprecated
    Token queryNrSignalStrength(int i, Client client) throws RemoteException;

    @Deprecated
    Token queryUpperLayerIndInfo(int i, Client client) throws RemoteException;

    Client registerCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException;

    Client registerQtiRadioConfigCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException;

    Token sendCdmaSms(int i, byte[] bArr, boolean z, Client client) throws RemoteException;

    Token sendUserPreferenceForDataDuringVoiceCall(int i, boolean z, Client client) throws RemoteException;

    boolean setAirplaneMode(boolean z) throws RemoteException;

    Token setCarrierInfoForImsiEncryption(int i, ImsiEncryptionInfo imsiEncryptionInfo, Client client) throws RemoteException;

    Token setMsimPreference(Client client, MsimPreference msimPreference) throws RemoteException;

    Token setNetworkSelectionModeAutomatic(int i, int i2, Client client) throws RemoteException;

    Token setNetworkSelectionModeManual(int i, QtiSetNetworkSelectionMode qtiSetNetworkSelectionMode, Client client) throws RemoteException;

    Token setNrConfig(int i, NrConfig nrConfig, Client client) throws RemoteException;

    void setPrimaryCardOnSlot(int i) throws RemoteException;

    void setSMSPromptEnabled(boolean z) throws RemoteException;

    void setSmartDdsSwitchToggle(boolean z, Client client) throws RemoteException;

    Token startNetworkScan(int i, NetworkScanRequest networkScanRequest, Client client) throws RemoteException;

    Token stopNetworkScan(int i, Client client) throws RemoteException;

    void supplyIccDepersonalization(String str, String str2, IDepersoResCallback iDepersoResCallback, int i) throws RemoteException;

    boolean toggleSimPinLock(int i, boolean z, String str) throws RemoteException;

    void unRegisterCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException;

    void unregisterQtiRadioConfigCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException;

    boolean verifySimPin(int i, String str) throws RemoteException;

    boolean verifySimPukChangePin(int i, String str, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements IExtPhone {
        static final int TRANSACTION_abortIncrementalScan = 9;
        static final int TRANSACTION_checkSimPinLockStatus = 45;
        static final int TRANSACTION_disable5g = 30;
        static final int TRANSACTION_enable5g = 29;
        static final int TRANSACTION_enable5gOnly = 32;
        static final int TRANSACTION_enableEndc = 19;
        static final int TRANSACTION_getAirplaneMode = 44;
        static final int TRANSACTION_getCurrentPrimaryCardSlotId = 4;
        static final int TRANSACTION_getDdsSwitchCapability = 51;
        static final int TRANSACTION_getFacilityLockForApp = 40;
        static final int TRANSACTION_getImeiInfo = 50;
        static final int TRANSACTION_getNetworkSelectionMode = 14;
        static final int TRANSACTION_getPrimaryCarrierSlotId = 5;
        static final int TRANSACTION_getPropertyValueBool = 2;
        static final int TRANSACTION_getPropertyValueInt = 1;
        static final int TRANSACTION_getPropertyValueString = 3;
        static final int TRANSACTION_getQtiRadioCapability = 28;
        static final int TRANSACTION_getSecureModeStatus = 54;
        static final int TRANSACTION_isEpdgOverCellularDataSupported = 53;
        static final int TRANSACTION_isFeatureSupported = 49;
        static final int TRANSACTION_isPrimaryCarrierSlotId = 6;
        static final int TRANSACTION_isSMSPromptEnabled = 15;
        static final int TRANSACTION_isSmartDdsSwitchFeatureAvailable = 41;
        static final int TRANSACTION_performIncrementalScan = 8;
        static final int TRANSACTION_query5gConfigInfo = 37;
        static final int TRANSACTION_query5gStatus = 33;
        static final int TRANSACTION_queryCallForwardStatus = 39;
        static final int TRANSACTION_queryEndcStatus = 20;
        static final int TRANSACTION_queryNrBearerAllocation = 31;
        static final int TRANSACTION_queryNrConfig = 26;
        static final int TRANSACTION_queryNrDcParam = 34;
        static final int TRANSACTION_queryNrIconType = 18;
        static final int TRANSACTION_queryNrSignalStrength = 35;
        static final int TRANSACTION_queryUpperLayerIndInfo = 36;
        static final int TRANSACTION_registerCallback = 21;
        static final int TRANSACTION_registerQtiRadioConfigCallback = 23;
        static final int TRANSACTION_sendCdmaSms = 27;
        static final int TRANSACTION_sendUserPreferenceForDataDuringVoiceCall = 52;
        static final int TRANSACTION_setAirplaneMode = 43;
        static final int TRANSACTION_setCarrierInfoForImsiEncryption = 38;
        static final int TRANSACTION_setMsimPreference = 55;
        static final int TRANSACTION_setNetworkSelectionModeAutomatic = 13;
        static final int TRANSACTION_setNetworkSelectionModeManual = 12;
        static final int TRANSACTION_setNrConfig = 25;
        static final int TRANSACTION_setPrimaryCardOnSlot = 7;
        static final int TRANSACTION_setSMSPromptEnabled = 16;
        static final int TRANSACTION_setSmartDdsSwitchToggle = 42;
        static final int TRANSACTION_startNetworkScan = 10;
        static final int TRANSACTION_stopNetworkScan = 11;
        static final int TRANSACTION_supplyIccDepersonalization = 17;
        static final int TRANSACTION_toggleSimPinLock = 46;
        static final int TRANSACTION_unRegisterCallback = 22;
        static final int TRANSACTION_unregisterQtiRadioConfigCallback = 24;
        static final int TRANSACTION_verifySimPin = 47;
        static final int TRANSACTION_verifySimPukChangePin = 48;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IExtPhone.DESCRIPTOR);
        }

        public static IExtPhone asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IExtPhone.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IExtPhone)) {
                return new Proxy(iBinder);
            }
            return (IExtPhone) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IExtPhone.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        String readString = parcel.readString();
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        int propertyValueInt = getPropertyValueInt(readString, readInt);
                        parcel2.writeNoException();
                        parcel2.writeInt(propertyValueInt);
                        break;
                    case 2:
                        String readString2 = parcel.readString();
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        boolean propertyValueBool = getPropertyValueBool(readString2, readBoolean);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(propertyValueBool);
                        break;
                    case 3:
                        String readString3 = parcel.readString();
                        String readString4 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        String propertyValueString = getPropertyValueString(readString3, readString4);
                        parcel2.writeNoException();
                        parcel2.writeString(propertyValueString);
                        break;
                    case 4:
                        int currentPrimaryCardSlotId = getCurrentPrimaryCardSlotId();
                        parcel2.writeNoException();
                        parcel2.writeInt(currentPrimaryCardSlotId);
                        break;
                    case 5:
                        int primaryCarrierSlotId = getPrimaryCarrierSlotId();
                        parcel2.writeNoException();
                        parcel2.writeInt(primaryCarrierSlotId);
                        break;
                    case 6:
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean isPrimaryCarrierSlotId = isPrimaryCarrierSlotId(readInt2);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isPrimaryCarrierSlotId);
                        break;
                    case 7:
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setPrimaryCardOnSlot(readInt3);
                        parcel2.writeNoException();
                        break;
                    case 8:
                        int readInt4 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean performIncrementalScan = performIncrementalScan(readInt4);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(performIncrementalScan);
                        break;
                    case 9:
                        int readInt5 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean abortIncrementalScan = abortIncrementalScan(readInt5);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(abortIncrementalScan);
                        break;
                    case 10:
                        parcel.enforceNoDataAvail();
                        Token startNetworkScan = startNetworkScan(parcel.readInt(), (NetworkScanRequest) parcel.readTypedObject(NetworkScanRequest.CREATOR), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(startNetworkScan, 1);
                        break;
                    case 11:
                        parcel.enforceNoDataAvail();
                        Token stopNetworkScan = stopNetworkScan(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(stopNetworkScan, 1);
                        break;
                    case 12:
                        parcel.enforceNoDataAvail();
                        Token networkSelectionModeManual = setNetworkSelectionModeManual(parcel.readInt(), (QtiSetNetworkSelectionMode) parcel.readTypedObject(QtiSetNetworkSelectionMode.CREATOR), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(networkSelectionModeManual, 1);
                        break;
                    case 13:
                        parcel.enforceNoDataAvail();
                        Token networkSelectionModeAutomatic = setNetworkSelectionModeAutomatic(parcel.readInt(), parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(networkSelectionModeAutomatic, 1);
                        break;
                    case 14:
                        parcel.enforceNoDataAvail();
                        Token networkSelectionMode = getNetworkSelectionMode(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(networkSelectionMode, 1);
                        break;
                    case 15:
                        boolean isSMSPromptEnabled = isSMSPromptEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isSMSPromptEnabled);
                        break;
                    case 16:
                        boolean readBoolean2 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        setSMSPromptEnabled(readBoolean2);
                        parcel2.writeNoException();
                        break;
                    case 17:
                        String readString5 = parcel.readString();
                        String readString6 = parcel.readString();
                        IDepersoResCallback asInterface = IDepersoResCallback.Stub.asInterface(parcel.readStrongBinder());
                        int readInt6 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        supplyIccDepersonalization(readString5, readString6, asInterface, readInt6);
                        parcel2.writeNoException();
                        break;
                    case 18:
                        parcel.enforceNoDataAvail();
                        Token queryNrIconType = queryNrIconType(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryNrIconType, 1);
                        break;
                    case 19:
                        parcel.enforceNoDataAvail();
                        Token enableEndc = enableEndc(parcel.readInt(), parcel.readBoolean(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(enableEndc, 1);
                        break;
                    case 20:
                        parcel.enforceNoDataAvail();
                        Token queryEndcStatus = queryEndcStatus(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryEndcStatus, 1);
                        break;
                    case 21:
                        String readString7 = parcel.readString();
                        IExtPhoneCallback asInterface2 = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        Client registerCallback = registerCallback(readString7, asInterface2);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(registerCallback, 1);
                        break;
                    case 22:
                        IExtPhoneCallback asInterface3 = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        unRegisterCallback(asInterface3);
                        parcel2.writeNoException();
                        break;
                    case 23:
                        String readString8 = parcel.readString();
                        IExtPhoneCallback asInterface4 = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        Client registerQtiRadioConfigCallback = registerQtiRadioConfigCallback(readString8, asInterface4);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(registerQtiRadioConfigCallback, 1);
                        break;
                    case 24:
                        IExtPhoneCallback asInterface5 = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        unregisterQtiRadioConfigCallback(asInterface5);
                        parcel2.writeNoException();
                        break;
                    case 25:
                        parcel.enforceNoDataAvail();
                        Token nrConfig = setNrConfig(parcel.readInt(), (NrConfig) parcel.readTypedObject(NrConfig.CREATOR), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(nrConfig, 1);
                        break;
                    case 26:
                        parcel.enforceNoDataAvail();
                        Token queryNrConfig = queryNrConfig(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryNrConfig, 1);
                        break;
                    case 27:
                        parcel.enforceNoDataAvail();
                        Token sendCdmaSms = sendCdmaSms(parcel.readInt(), parcel.createByteArray(), parcel.readBoolean(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(sendCdmaSms, 1);
                        break;
                    case 28:
                        parcel.enforceNoDataAvail();
                        Token qtiRadioCapability = getQtiRadioCapability(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(qtiRadioCapability, 1);
                        break;
                    case 29:
                        parcel.enforceNoDataAvail();
                        Token enable5g = enable5g(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(enable5g, 1);
                        break;
                    case 30:
                        parcel.enforceNoDataAvail();
                        Token disable5g = disable5g(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(disable5g, 1);
                        break;
                    case 31:
                        parcel.enforceNoDataAvail();
                        Token queryNrBearerAllocation = queryNrBearerAllocation(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryNrBearerAllocation, 1);
                        break;
                    case 32:
                        parcel.enforceNoDataAvail();
                        Token enable5gOnly = enable5gOnly(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(enable5gOnly, 1);
                        break;
                    case 33:
                        parcel.enforceNoDataAvail();
                        Token query5gStatus = query5gStatus(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(query5gStatus, 1);
                        break;
                    case 34:
                        parcel.enforceNoDataAvail();
                        Token queryNrDcParam = queryNrDcParam(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryNrDcParam, 1);
                        break;
                    case 35:
                        parcel.enforceNoDataAvail();
                        Token queryNrSignalStrength = queryNrSignalStrength(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryNrSignalStrength, 1);
                        break;
                    case 36:
                        parcel.enforceNoDataAvail();
                        Token queryUpperLayerIndInfo = queryUpperLayerIndInfo(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(queryUpperLayerIndInfo, 1);
                        break;
                    case 37:
                        parcel.enforceNoDataAvail();
                        Token query5gConfigInfo = query5gConfigInfo(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(query5gConfigInfo, 1);
                        break;
                    case 38:
                        parcel.enforceNoDataAvail();
                        Token carrierInfoForImsiEncryption = setCarrierInfoForImsiEncryption(parcel.readInt(), (ImsiEncryptionInfo) parcel.readTypedObject(ImsiEncryptionInfo.CREATOR), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(carrierInfoForImsiEncryption, 1);
                        break;
                    case 39:
                        int readInt7 = parcel.readInt();
                        int readInt8 = parcel.readInt();
                        int readInt9 = parcel.readInt();
                        String readString9 = parcel.readString();
                        boolean readBoolean3 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        queryCallForwardStatus(readInt7, readInt8, readInt9, readString9, readBoolean3, (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 40:
                        int readInt10 = parcel.readInt();
                        String readString10 = parcel.readString();
                        String readString11 = parcel.readString();
                        int readInt11 = parcel.readInt();
                        String readString12 = parcel.readString();
                        boolean readBoolean4 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        getFacilityLockForApp(readInt10, readString10, readString11, readInt11, readString12, readBoolean4, (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 41:
                        boolean isSmartDdsSwitchFeatureAvailable = isSmartDdsSwitchFeatureAvailable();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isSmartDdsSwitchFeatureAvailable);
                        break;
                    case 42:
                        parcel.enforceNoDataAvail();
                        setSmartDdsSwitchToggle(parcel.readBoolean(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 43:
                        boolean readBoolean5 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        boolean airplaneMode = setAirplaneMode(readBoolean5);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(airplaneMode);
                        break;
                    case 44:
                        boolean airplaneMode2 = getAirplaneMode();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(airplaneMode2);
                        break;
                    case 45:
                        int readInt12 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean checkSimPinLockStatus = checkSimPinLockStatus(readInt12);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(checkSimPinLockStatus);
                        break;
                    case 46:
                        int readInt13 = parcel.readInt();
                        boolean readBoolean6 = parcel.readBoolean();
                        String readString13 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean z = toggleSimPinLock(readInt13, readBoolean6, readString13);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(z);
                        break;
                    case 47:
                        int readInt14 = parcel.readInt();
                        String readString14 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean verifySimPin = verifySimPin(readInt14, readString14);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(verifySimPin);
                        break;
                    case 48:
                        int readInt15 = parcel.readInt();
                        String readString15 = parcel.readString();
                        String readString16 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean verifySimPukChangePin = verifySimPukChangePin(readInt15, readString15, readString16);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(verifySimPukChangePin);
                        break;
                    case 49:
                        int readInt16 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean isFeatureSupported = isFeatureSupported(readInt16);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isFeatureSupported);
                        break;
                    case 50:
                        QtiImeiInfo[] imeiInfo = getImeiInfo();
                        parcel2.writeNoException();
                        parcel2.writeTypedArray(imeiInfo, 1);
                        break;
                    case 51:
                        parcel.enforceNoDataAvail();
                        Token ddsSwitchCapability = getDdsSwitchCapability(parcel.readInt(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(ddsSwitchCapability, 1);
                        break;
                    case 52:
                        parcel.enforceNoDataAvail();
                        Token sendUserPreferenceForDataDuringVoiceCall = sendUserPreferenceForDataDuringVoiceCall(parcel.readInt(), parcel.readBoolean(), (Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(sendUserPreferenceForDataDuringVoiceCall, 1);
                        break;
                    case 53:
                        int readInt17 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean isEpdgOverCellularDataSupported = isEpdgOverCellularDataSupported(readInt17);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isEpdgOverCellularDataSupported);
                        break;
                    case 54:
                        parcel.enforceNoDataAvail();
                        Token secureModeStatus = getSecureModeStatus((Client) parcel.readTypedObject(Client.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(secureModeStatus, 1);
                        break;
                    case 55:
                        parcel.enforceNoDataAvail();
                        Token msimPreference = setMsimPreference((Client) parcel.readTypedObject(Client.CREATOR), (MsimPreference) parcel.readTypedObject(MsimPreference.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(msimPreference, 1);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IExtPhone.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IExtPhone {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IExtPhone.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int getPropertyValueInt(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getPropertyValueBool(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPropertyValueString(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCurrentPrimaryCardSlotId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getPrimaryCarrierSlotId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isPrimaryCarrierSlotId(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPrimaryCardOnSlot(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean performIncrementalScan(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean abortIncrementalScan(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token startNetworkScan(int i, NetworkScanRequest networkScanRequest, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(networkScanRequest, 0);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token stopNetworkScan(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token setNetworkSelectionModeManual(int i, QtiSetNetworkSelectionMode qtiSetNetworkSelectionMode, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(qtiSetNetworkSelectionMode, 0);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token setNetworkSelectionModeAutomatic(int i, int i2, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token getNetworkSelectionMode(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSMSPromptEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSMSPromptEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void supplyIccDepersonalization(String str, String str2, IDepersoResCallback iDepersoResCallback, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iDepersoResCallback);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryNrIconType(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token enableEndc(int i, boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryEndcStatus(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Client registerCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iExtPhoneCallback);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Client) obtain2.readTypedObject(Client.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unRegisterCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeStrongInterface(iExtPhoneCallback);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Client registerQtiRadioConfigCallback(String str, IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iExtPhoneCallback);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Client) obtain2.readTypedObject(Client.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterQtiRadioConfigCallback(IExtPhoneCallback iExtPhoneCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeStrongInterface(iExtPhoneCallback);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token setNrConfig(int i, NrConfig nrConfig, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nrConfig, 0);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryNrConfig(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token sendCdmaSms(int i, byte[] bArr, boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token getQtiRadioCapability(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token enable5g(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token disable5g(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryNrBearerAllocation(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token enable5gOnly(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token query5gStatus(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryNrDcParam(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryNrSignalStrength(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token queryUpperLayerIndInfo(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token query5gConfigInfo(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token setCarrierInfoForImsiEncryption(int i, ImsiEncryptionInfo imsiEncryptionInfo, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(imsiEncryptionInfo, 0);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void queryCallForwardStatus(int i, int i2, int i3, String str, boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getFacilityLockForApp(int i, String str, String str2, int i2, String str3, boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i2);
                    obtain.writeString(str3);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSmartDdsSwitchFeatureAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSmartDdsSwitchToggle(boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setAirplaneMode(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getAirplaneMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean checkSimPinLockStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean toggleSimPinLock(int i, boolean z, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean verifySimPin(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean verifySimPukChangePin(int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFeatureSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public QtiImeiInfo[] getImeiInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                    return (QtiImeiInfo[]) obtain2.createTypedArray(QtiImeiInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token getDdsSwitchCapability(int i, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token sendUserPreferenceForDataDuringVoiceCall(int i, boolean z, Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isEpdgOverCellularDataSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token getSecureModeStatus(Client client) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeTypedObject(client, 0);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Token setMsimPreference(Client client, MsimPreference msimPreference) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhone.DESCRIPTOR);
                    obtain.writeTypedObject(client, 0);
                    obtain.writeTypedObject(msimPreference, 0);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Token) obtain2.readTypedObject(Token.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
