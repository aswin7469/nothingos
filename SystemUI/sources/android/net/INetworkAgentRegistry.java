package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.data.EpsBearerQosSessionAttributes;
import android.telephony.data.NrQosSessionAttributes;
import java.util.List;

public interface INetworkAgentRegistry extends IInterface {
    public static final String DESCRIPTOR = "android.net.INetworkAgentRegistry";

    public static class Default implements INetworkAgentRegistry {
        public IBinder asBinder() {
            return null;
        }

        public void sendAddDscpPolicy(DscpPolicy dscpPolicy) throws RemoteException {
        }

        public void sendEpsQosSessionAvailable(int i, QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException {
        }

        public void sendExplicitlySelected(boolean z, boolean z2) throws RemoteException {
        }

        public void sendLingerDuration(int i) throws RemoteException {
        }

        public void sendLinkProperties(LinkProperties linkProperties) throws RemoteException {
        }

        public void sendNetworkCapabilities(NetworkCapabilities networkCapabilities) throws RemoteException {
        }

        public void sendNetworkInfo(NetworkInfo networkInfo) throws RemoteException {
        }

        public void sendNrQosSessionAvailable(int i, QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException {
        }

        public void sendQosCallbackError(int i, int i2) throws RemoteException {
        }

        public void sendQosSessionLost(int i, QosSession qosSession) throws RemoteException {
        }

        public void sendRemoveAllDscpPolicies() throws RemoteException {
        }

        public void sendRemoveDscpPolicy(int i) throws RemoteException {
        }

        public void sendScore(NetworkScore networkScore) throws RemoteException {
        }

        public void sendSocketKeepaliveEvent(int i, int i2) throws RemoteException {
        }

        public void sendTeardownDelayMs(int i) throws RemoteException {
        }

        public void sendUnderlyingNetworks(List<Network> list) throws RemoteException {
        }

        public void sendUnregisterAfterReplacement(int i) throws RemoteException {
        }
    }

    void sendAddDscpPolicy(DscpPolicy dscpPolicy) throws RemoteException;

    void sendEpsQosSessionAvailable(int i, QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException;

    void sendExplicitlySelected(boolean z, boolean z2) throws RemoteException;

    void sendLingerDuration(int i) throws RemoteException;

    void sendLinkProperties(LinkProperties linkProperties) throws RemoteException;

    void sendNetworkCapabilities(NetworkCapabilities networkCapabilities) throws RemoteException;

    void sendNetworkInfo(NetworkInfo networkInfo) throws RemoteException;

    void sendNrQosSessionAvailable(int i, QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException;

    void sendQosCallbackError(int i, int i2) throws RemoteException;

    void sendQosSessionLost(int i, QosSession qosSession) throws RemoteException;

    void sendRemoveAllDscpPolicies() throws RemoteException;

    void sendRemoveDscpPolicy(int i) throws RemoteException;

    void sendScore(NetworkScore networkScore) throws RemoteException;

    void sendSocketKeepaliveEvent(int i, int i2) throws RemoteException;

    void sendTeardownDelayMs(int i) throws RemoteException;

    void sendUnderlyingNetworks(List<Network> list) throws RemoteException;

    void sendUnregisterAfterReplacement(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkAgentRegistry {
        static final int TRANSACTION_sendAddDscpPolicy = 14;
        static final int TRANSACTION_sendEpsQosSessionAvailable = 8;
        static final int TRANSACTION_sendExplicitlySelected = 5;
        static final int TRANSACTION_sendLingerDuration = 13;
        static final int TRANSACTION_sendLinkProperties = 2;
        static final int TRANSACTION_sendNetworkCapabilities = 1;
        static final int TRANSACTION_sendNetworkInfo = 3;
        static final int TRANSACTION_sendNrQosSessionAvailable = 9;
        static final int TRANSACTION_sendQosCallbackError = 11;
        static final int TRANSACTION_sendQosSessionLost = 10;
        static final int TRANSACTION_sendRemoveAllDscpPolicies = 16;
        static final int TRANSACTION_sendRemoveDscpPolicy = 15;
        static final int TRANSACTION_sendScore = 4;
        static final int TRANSACTION_sendSocketKeepaliveEvent = 6;
        static final int TRANSACTION_sendTeardownDelayMs = 12;
        static final int TRANSACTION_sendUnderlyingNetworks = 7;
        static final int TRANSACTION_sendUnregisterAfterReplacement = 17;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "sendNetworkCapabilities";
                case 2:
                    return "sendLinkProperties";
                case 3:
                    return "sendNetworkInfo";
                case 4:
                    return "sendScore";
                case 5:
                    return "sendExplicitlySelected";
                case 6:
                    return "sendSocketKeepaliveEvent";
                case 7:
                    return "sendUnderlyingNetworks";
                case 8:
                    return "sendEpsQosSessionAvailable";
                case 9:
                    return "sendNrQosSessionAvailable";
                case 10:
                    return "sendQosSessionLost";
                case 11:
                    return "sendQosCallbackError";
                case 12:
                    return "sendTeardownDelayMs";
                case 13:
                    return "sendLingerDuration";
                case 14:
                    return "sendAddDscpPolicy";
                case 15:
                    return "sendRemoveDscpPolicy";
                case 16:
                    return "sendRemoveAllDscpPolicies";
                case 17:
                    return "sendUnregisterAfterReplacement";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 16;
        }

        public Stub() {
            attachInterface(this, INetworkAgentRegistry.DESCRIPTOR);
        }

        public static INetworkAgentRegistry asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkAgentRegistry.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkAgentRegistry)) {
                return new Proxy(iBinder);
            }
            return (INetworkAgentRegistry) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkAgentRegistry.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        sendNetworkCapabilities((NetworkCapabilities) parcel.readTypedObject(NetworkCapabilities.CREATOR));
                        break;
                    case 2:
                        sendLinkProperties((LinkProperties) parcel.readTypedObject(LinkProperties.CREATOR));
                        break;
                    case 3:
                        sendNetworkInfo((NetworkInfo) parcel.readTypedObject(NetworkInfo.CREATOR));
                        break;
                    case 4:
                        sendScore((NetworkScore) parcel.readTypedObject(NetworkScore.CREATOR));
                        break;
                    case 5:
                        sendExplicitlySelected(parcel.readBoolean(), parcel.readBoolean());
                        break;
                    case 6:
                        sendSocketKeepaliveEvent(parcel.readInt(), parcel.readInt());
                        break;
                    case 7:
                        sendUnderlyingNetworks(parcel.createTypedArrayList(Network.CREATOR));
                        break;
                    case 8:
                        sendEpsQosSessionAvailable(parcel.readInt(), (QosSession) parcel.readTypedObject(QosSession.CREATOR), (EpsBearerQosSessionAttributes) parcel.readTypedObject(EpsBearerQosSessionAttributes.CREATOR));
                        break;
                    case 9:
                        sendNrQosSessionAvailable(parcel.readInt(), (QosSession) parcel.readTypedObject(QosSession.CREATOR), (NrQosSessionAttributes) parcel.readTypedObject(NrQosSessionAttributes.CREATOR));
                        break;
                    case 10:
                        sendQosSessionLost(parcel.readInt(), (QosSession) parcel.readTypedObject(QosSession.CREATOR));
                        break;
                    case 11:
                        sendQosCallbackError(parcel.readInt(), parcel.readInt());
                        break;
                    case 12:
                        sendTeardownDelayMs(parcel.readInt());
                        break;
                    case 13:
                        sendLingerDuration(parcel.readInt());
                        break;
                    case 14:
                        sendAddDscpPolicy((DscpPolicy) parcel.readTypedObject(DscpPolicy.CREATOR));
                        break;
                    case 15:
                        sendRemoveDscpPolicy(parcel.readInt());
                        break;
                    case 16:
                        sendRemoveAllDscpPolicies();
                        break;
                    case 17:
                        sendUnregisterAfterReplacement(parcel.readInt());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(INetworkAgentRegistry.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkAgentRegistry {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkAgentRegistry.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void sendNetworkCapabilities(NetworkCapabilities networkCapabilities) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendLinkProperties(LinkProperties linkProperties) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedObject(linkProperties, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendNetworkInfo(NetworkInfo networkInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedObject(networkInfo, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendScore(NetworkScore networkScore) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedObject(networkScore, 0);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendExplicitlySelected(boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendSocketKeepaliveEvent(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendUnderlyingNetworks(List<Network> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendEpsQosSessionAvailable(int i, QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(qosSession, 0);
                    obtain.writeTypedObject(epsBearerQosSessionAttributes, 0);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendNrQosSessionAvailable(int i, QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(qosSession, 0);
                    obtain.writeTypedObject(nrQosSessionAttributes, 0);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendQosSessionLost(int i, QosSession qosSession) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(qosSession, 0);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendQosCallbackError(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(11, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendTeardownDelayMs(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(12, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendLingerDuration(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendAddDscpPolicy(DscpPolicy dscpPolicy) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeTypedObject(dscpPolicy, 0);
                    this.mRemote.transact(14, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendRemoveDscpPolicy(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendRemoveAllDscpPolicies() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    this.mRemote.transact(16, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendUnregisterAfterReplacement(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgentRegistry.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
