package com.qti.extphone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CellInfo;
import java.util.ArrayList;
import java.util.List;

public interface IExtPhoneCallback extends IInterface {
    public static final String DESCRIPTOR = "com.qti.extphone.IExtPhoneCallback";

    public static class Default implements IExtPhoneCallback {
        public IBinder asBinder() {
            return null;
        }

        public void getFacilityLockForAppResponse(Status status, int[] iArr) throws RemoteException {
        }

        public void getNetworkSelectionModeResponse(int i, Token token, Status status, NetworkSelectionMode networkSelectionMode) throws RemoteException {
        }

        public void getQtiRadioCapabilityResponse(int i, Token token, Status status, int i2) throws RemoteException {
        }

        public void getSecureModeStatusResponse(Token token, Status status, boolean z) throws RemoteException {
        }

        public void networkScanResult(int i, Token token, int i2, int i3, List<CellInfo> list) throws RemoteException {
        }

        public void on5gConfigInfo(int i, Token token, Status status, NrConfigType nrConfigType) throws RemoteException {
        }

        public void on5gStatus(int i, Token token, Status status, boolean z) throws RemoteException {
        }

        public void onAnyNrBearerAllocation(int i, Token token, Status status, BearerAllocationStatus bearerAllocationStatus) throws RemoteException {
        }

        public void onDataDeactivateDelayTime(int i, long j) throws RemoteException {
        }

        public void onDdsSwitchCapabilityChange(int i, Token token, Status status, boolean z) throws RemoteException {
        }

        public void onDdsSwitchCriteriaChange(int i, boolean z) throws RemoteException {
        }

        public void onDdsSwitchRecommendation(int i, int i2) throws RemoteException {
        }

        public void onEnableEndc(int i, Token token, Status status) throws RemoteException {
        }

        public void onEndcStatus(int i, Token token, Status status, boolean z) throws RemoteException {
        }

        public void onEpdgOverCellularDataSupported(int i, boolean z) throws RemoteException {
        }

        public void onImeiTypeChanged(QtiImeiInfo[] qtiImeiInfoArr) throws RemoteException {
        }

        public void onNrConfigStatus(int i, Token token, Status status, NrConfig nrConfig) throws RemoteException {
        }

        public void onNrDcParam(int i, Token token, Status status, DcParam dcParam) throws RemoteException {
        }

        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException {
        }

        public void onSecureModeStatusChange(boolean z) throws RemoteException {
        }

        public void onSendUserPreferenceForDataDuringVoiceCall(int i, Token token, Status status) throws RemoteException {
        }

        public void onSetNrConfig(int i, Token token, Status status) throws RemoteException {
        }

        public void onSignalStrength(int i, Token token, Status status, SignalStrength signalStrength) throws RemoteException {
        }

        public void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) throws RemoteException {
        }

        public void queryCallForwardStatusResponse(Status status, QtiCallForwardInfo[] qtiCallForwardInfoArr) throws RemoteException {
        }

        public void sendCdmaSmsResponse(int i, Token token, Status status, SmsResult smsResult) throws RemoteException {
        }

        public void setCarrierInfoForImsiEncryptionResponse(int i, Token token, QRadioResponseInfo qRadioResponseInfo) throws RemoteException {
        }

        public void setMsimPreferenceResponse(Token token, Status status) throws RemoteException {
        }

        public void setNetworkSelectionModeAutomaticResponse(int i, Token token, int i2) throws RemoteException {
        }

        public void setNetworkSelectionModeManualResponse(int i, Token token, int i2) throws RemoteException {
        }

        public void setSmartDdsSwitchToggleResponse(Token token, boolean z) throws RemoteException {
        }

        public void startNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
        }

        public void stopNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
        }
    }

    void getFacilityLockForAppResponse(Status status, int[] iArr) throws RemoteException;

    void getNetworkSelectionModeResponse(int i, Token token, Status status, NetworkSelectionMode networkSelectionMode) throws RemoteException;

    void getQtiRadioCapabilityResponse(int i, Token token, Status status, int i2) throws RemoteException;

    void getSecureModeStatusResponse(Token token, Status status, boolean z) throws RemoteException;

    void networkScanResult(int i, Token token, int i2, int i3, List<CellInfo> list) throws RemoteException;

    @Deprecated
    void on5gConfigInfo(int i, Token token, Status status, NrConfigType nrConfigType) throws RemoteException;

    @Deprecated
    void on5gStatus(int i, Token token, Status status, boolean z) throws RemoteException;

    @Deprecated
    void onAnyNrBearerAllocation(int i, Token token, Status status, BearerAllocationStatus bearerAllocationStatus) throws RemoteException;

    void onDataDeactivateDelayTime(int i, long j) throws RemoteException;

    void onDdsSwitchCapabilityChange(int i, Token token, Status status, boolean z) throws RemoteException;

    void onDdsSwitchCriteriaChange(int i, boolean z) throws RemoteException;

    void onDdsSwitchRecommendation(int i, int i2) throws RemoteException;

    void onEnableEndc(int i, Token token, Status status) throws RemoteException;

    void onEndcStatus(int i, Token token, Status status, boolean z) throws RemoteException;

    void onEpdgOverCellularDataSupported(int i, boolean z) throws RemoteException;

    void onImeiTypeChanged(QtiImeiInfo[] qtiImeiInfoArr) throws RemoteException;

    void onNrConfigStatus(int i, Token token, Status status, NrConfig nrConfig) throws RemoteException;

    @Deprecated
    void onNrDcParam(int i, Token token, Status status, DcParam dcParam) throws RemoteException;

    void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException;

    void onSecureModeStatusChange(boolean z) throws RemoteException;

    void onSendUserPreferenceForDataDuringVoiceCall(int i, Token token, Status status) throws RemoteException;

    void onSetNrConfig(int i, Token token, Status status) throws RemoteException;

    @Deprecated
    void onSignalStrength(int i, Token token, Status status, SignalStrength signalStrength) throws RemoteException;

    @Deprecated
    void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) throws RemoteException;

    void queryCallForwardStatusResponse(Status status, QtiCallForwardInfo[] qtiCallForwardInfoArr) throws RemoteException;

    void sendCdmaSmsResponse(int i, Token token, Status status, SmsResult smsResult) throws RemoteException;

    void setCarrierInfoForImsiEncryptionResponse(int i, Token token, QRadioResponseInfo qRadioResponseInfo) throws RemoteException;

    void setMsimPreferenceResponse(Token token, Status status) throws RemoteException;

    void setNetworkSelectionModeAutomaticResponse(int i, Token token, int i2) throws RemoteException;

    void setNetworkSelectionModeManualResponse(int i, Token token, int i2) throws RemoteException;

    void setSmartDdsSwitchToggleResponse(Token token, boolean z) throws RemoteException;

    void startNetworkScanResponse(int i, Token token, int i2) throws RemoteException;

    void stopNetworkScanResponse(int i, Token token, int i2) throws RemoteException;

    public static abstract class Stub extends Binder implements IExtPhoneCallback {
        static final int TRANSACTION_getFacilityLockForAppResponse = 16;
        static final int TRANSACTION_getNetworkSelectionModeResponse = 31;
        static final int TRANSACTION_getQtiRadioCapabilityResponse = 7;
        static final int TRANSACTION_getSecureModeStatusResponse = 25;
        static final int TRANSACTION_networkScanResult = 32;
        static final int TRANSACTION_on5gConfigInfo = 12;
        static final int TRANSACTION_on5gStatus = 8;
        static final int TRANSACTION_onAnyNrBearerAllocation = 9;
        static final int TRANSACTION_onDataDeactivateDelayTime = 23;
        static final int TRANSACTION_onDdsSwitchCapabilityChange = 20;
        static final int TRANSACTION_onDdsSwitchCriteriaChange = 21;
        static final int TRANSACTION_onDdsSwitchRecommendation = 22;
        static final int TRANSACTION_onEnableEndc = 2;
        static final int TRANSACTION_onEndcStatus = 3;
        static final int TRANSACTION_onEpdgOverCellularDataSupported = 24;
        static final int TRANSACTION_onImeiTypeChanged = 18;
        static final int TRANSACTION_onNrConfigStatus = 5;
        static final int TRANSACTION_onNrDcParam = 10;
        static final int TRANSACTION_onNrIconType = 1;
        static final int TRANSACTION_onSecureModeStatusChange = 26;
        static final int TRANSACTION_onSendUserPreferenceForDataDuringVoiceCall = 19;
        static final int TRANSACTION_onSetNrConfig = 4;
        static final int TRANSACTION_onSignalStrength = 13;
        static final int TRANSACTION_onUpperLayerIndInfo = 11;
        static final int TRANSACTION_queryCallForwardStatusResponse = 15;
        static final int TRANSACTION_sendCdmaSmsResponse = 6;
        static final int TRANSACTION_setCarrierInfoForImsiEncryptionResponse = 14;
        static final int TRANSACTION_setMsimPreferenceResponse = 33;
        static final int TRANSACTION_setNetworkSelectionModeAutomaticResponse = 29;
        static final int TRANSACTION_setNetworkSelectionModeManualResponse = 30;
        static final int TRANSACTION_setSmartDdsSwitchToggleResponse = 17;
        static final int TRANSACTION_startNetworkScanResponse = 27;
        static final int TRANSACTION_stopNetworkScanResponse = 28;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IExtPhoneCallback.DESCRIPTOR);
        }

        public static IExtPhoneCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IExtPhoneCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IExtPhoneCallback)) {
                return new Proxy(iBinder);
            }
            return (IExtPhoneCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IExtPhoneCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceNoDataAvail();
                        onNrIconType(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (NrIconType) parcel.readTypedObject(NrIconType.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 2:
                        parcel.enforceNoDataAvail();
                        onEnableEndc(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 3:
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onEndcStatus(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), readBoolean);
                        parcel2.writeNoException();
                        break;
                    case 4:
                        parcel.enforceNoDataAvail();
                        onSetNrConfig(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 5:
                        parcel.enforceNoDataAvail();
                        onNrConfigStatus(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (NrConfig) parcel.readTypedObject(NrConfig.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 6:
                        parcel.enforceNoDataAvail();
                        sendCdmaSmsResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (SmsResult) parcel.readTypedObject(SmsResult.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 7:
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        getQtiRadioCapabilityResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), readInt);
                        parcel2.writeNoException();
                        break;
                    case 8:
                        boolean readBoolean2 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        on5gStatus(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), readBoolean2);
                        parcel2.writeNoException();
                        break;
                    case 9:
                        parcel.enforceNoDataAvail();
                        onAnyNrBearerAllocation(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (BearerAllocationStatus) parcel.readTypedObject(BearerAllocationStatus.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 10:
                        parcel.enforceNoDataAvail();
                        onNrDcParam(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (DcParam) parcel.readTypedObject(DcParam.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 11:
                        parcel.enforceNoDataAvail();
                        onUpperLayerIndInfo(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (UpperLayerIndInfo) parcel.readTypedObject(UpperLayerIndInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 12:
                        parcel.enforceNoDataAvail();
                        on5gConfigInfo(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (NrConfigType) parcel.readTypedObject(NrConfigType.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 13:
                        parcel.enforceNoDataAvail();
                        onSignalStrength(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (SignalStrength) parcel.readTypedObject(SignalStrength.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 14:
                        parcel.enforceNoDataAvail();
                        setCarrierInfoForImsiEncryptionResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (QRadioResponseInfo) parcel.readTypedObject(QRadioResponseInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 15:
                        parcel.enforceNoDataAvail();
                        queryCallForwardStatusResponse((Status) parcel.readTypedObject(Status.CREATOR), (QtiCallForwardInfo[]) parcel.createTypedArray(QtiCallForwardInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 16:
                        int[] createIntArray = parcel.createIntArray();
                        parcel.enforceNoDataAvail();
                        getFacilityLockForAppResponse((Status) parcel.readTypedObject(Status.CREATOR), createIntArray);
                        parcel2.writeNoException();
                        break;
                    case 17:
                        boolean readBoolean3 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        setSmartDdsSwitchToggleResponse((Token) parcel.readTypedObject(Token.CREATOR), readBoolean3);
                        parcel2.writeNoException();
                        break;
                    case 18:
                        parcel.enforceNoDataAvail();
                        onImeiTypeChanged((QtiImeiInfo[]) parcel.createTypedArray(QtiImeiInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 19:
                        parcel.enforceNoDataAvail();
                        onSendUserPreferenceForDataDuringVoiceCall(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 20:
                        boolean readBoolean4 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onDdsSwitchCapabilityChange(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), readBoolean4);
                        parcel2.writeNoException();
                        break;
                    case 21:
                        int readInt2 = parcel.readInt();
                        boolean readBoolean5 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onDdsSwitchCriteriaChange(readInt2, readBoolean5);
                        parcel2.writeNoException();
                        break;
                    case 22:
                        int readInt3 = parcel.readInt();
                        int readInt4 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onDdsSwitchRecommendation(readInt3, readInt4);
                        parcel2.writeNoException();
                        break;
                    case 23:
                        int readInt5 = parcel.readInt();
                        long readLong = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        onDataDeactivateDelayTime(readInt5, readLong);
                        parcel2.writeNoException();
                        break;
                    case 24:
                        int readInt6 = parcel.readInt();
                        boolean readBoolean6 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onEpdgOverCellularDataSupported(readInt6, readBoolean6);
                        parcel2.writeNoException();
                        break;
                    case 25:
                        boolean readBoolean7 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        getSecureModeStatusResponse((Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), readBoolean7);
                        parcel2.writeNoException();
                        break;
                    case 26:
                        boolean readBoolean8 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onSecureModeStatusChange(readBoolean8);
                        parcel2.writeNoException();
                        break;
                    case 27:
                        int readInt7 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        startNetworkScanResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), readInt7);
                        parcel2.writeNoException();
                        break;
                    case 28:
                        int readInt8 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        stopNetworkScanResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), readInt8);
                        parcel2.writeNoException();
                        break;
                    case 29:
                        int readInt9 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setNetworkSelectionModeAutomaticResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), readInt9);
                        parcel2.writeNoException();
                        break;
                    case 30:
                        int readInt10 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setNetworkSelectionModeManualResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), readInt10);
                        parcel2.writeNoException();
                        break;
                    case 31:
                        parcel.enforceNoDataAvail();
                        getNetworkSelectionModeResponse(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR), (NetworkSelectionMode) parcel.readTypedObject(NetworkSelectionMode.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 32:
                        int readInt11 = parcel.readInt();
                        int readInt12 = parcel.readInt();
                        ArrayList createTypedArrayList = parcel.createTypedArrayList(CellInfo.CREATOR);
                        parcel.enforceNoDataAvail();
                        networkScanResult(parcel.readInt(), (Token) parcel.readTypedObject(Token.CREATOR), readInt11, readInt12, createTypedArrayList);
                        parcel2.writeNoException();
                        break;
                    case 33:
                        parcel.enforceNoDataAvail();
                        setMsimPreferenceResponse((Token) parcel.readTypedObject(Token.CREATOR), (Status) parcel.readTypedObject(Status.CREATOR));
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IExtPhoneCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IExtPhoneCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IExtPhoneCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(nrIconType, 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onEnableEndc(int i, Token token, Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onEndcStatus(int i, Token token, Status status, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSetNrConfig(int i, Token token, Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onNrConfigStatus(int i, Token token, Status status, NrConfig nrConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(nrConfig, 0);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendCdmaSmsResponse(int i, Token token, Status status, SmsResult smsResult) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(smsResult, 0);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getQtiRadioCapabilityResponse(int i, Token token, Status status, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeInt(i2);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void on5gStatus(int i, Token token, Status status, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onAnyNrBearerAllocation(int i, Token token, Status status, BearerAllocationStatus bearerAllocationStatus) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(bearerAllocationStatus, 0);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onNrDcParam(int i, Token token, Status status, DcParam dcParam) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(dcParam, 0);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(upperLayerIndInfo, 0);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void on5gConfigInfo(int i, Token token, Status status, NrConfigType nrConfigType) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(nrConfigType, 0);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSignalStrength(int i, Token token, Status status, SignalStrength signalStrength) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(signalStrength, 0);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCarrierInfoForImsiEncryptionResponse(int i, Token token, QRadioResponseInfo qRadioResponseInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(qRadioResponseInfo, 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void queryCallForwardStatusResponse(Status status, QtiCallForwardInfo[] qtiCallForwardInfoArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedArray(qtiCallForwardInfoArr, 0);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getFacilityLockForAppResponse(Status status, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSmartDdsSwitchToggleResponse(Token token, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onImeiTypeChanged(QtiImeiInfo[] qtiImeiInfoArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedArray(qtiImeiInfoArr, 0);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSendUserPreferenceForDataDuringVoiceCall(int i, Token token, Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDdsSwitchCapabilityChange(int i, Token token, Status status, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDdsSwitchCriteriaChange(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDdsSwitchRecommendation(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDataDeactivateDelayTime(int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onEpdgOverCellularDataSupported(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getSecureModeStatusResponse(Token token, Status status, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSecureModeStatusChange(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeInt(i2);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopNetworkScanResponse(int i, Token token, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeInt(i2);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setNetworkSelectionModeAutomaticResponse(int i, Token token, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeInt(i2);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setNetworkSelectionModeManualResponse(int i, Token token, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeInt(i2);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getNetworkSelectionModeResponse(int i, Token token, Status status, NetworkSelectionMode networkSelectionMode) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    obtain.writeTypedObject(networkSelectionMode, 0);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void networkScanResult(int i, Token token, int i2, int i3, List<CellInfo> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMsimPreferenceResponse(Token token, Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IExtPhoneCallback.DESCRIPTOR);
                    obtain.writeTypedObject(token, 0);
                    obtain.writeTypedObject(status, 0);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
