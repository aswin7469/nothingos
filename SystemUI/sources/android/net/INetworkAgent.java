package android.net;

import android.net.INetworkAgentRegistry;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkAgent extends IInterface {
    public static final String DESCRIPTOR = "android.net.INetworkAgent";

    public static class Default implements INetworkAgent {
        public IBinder asBinder() {
            return null;
        }

        public void onAddNattKeepalivePacketFilter(int i, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException {
        }

        public void onAddTcpKeepalivePacketFilter(int i, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException {
        }

        public void onBandwidthUpdateRequested() throws RemoteException {
        }

        public void onDisconnected() throws RemoteException {
        }

        public void onDscpPolicyStatusUpdated(int i, int i2) throws RemoteException {
        }

        public void onNetworkCreated() throws RemoteException {
        }

        public void onNetworkDestroyed() throws RemoteException {
        }

        public void onPreventAutomaticReconnect() throws RemoteException {
        }

        public void onQosCallbackUnregistered(int i) throws RemoteException {
        }

        public void onQosFilterCallbackRegistered(int i, QosFilterParcelable qosFilterParcelable) throws RemoteException {
        }

        public void onRegistered(INetworkAgentRegistry iNetworkAgentRegistry) throws RemoteException {
        }

        public void onRemoveKeepalivePacketFilter(int i) throws RemoteException {
        }

        public void onSaveAcceptUnvalidated(boolean z) throws RemoteException {
        }

        public void onSignalStrengthThresholdsUpdated(int[] iArr) throws RemoteException {
        }

        public void onStartNattSocketKeepalive(int i, int i2, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException {
        }

        public void onStartTcpSocketKeepalive(int i, int i2, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException {
        }

        public void onStopSocketKeepalive(int i) throws RemoteException {
        }

        public void onValidationStatusChanged(int i, String str) throws RemoteException {
        }
    }

    void onAddNattKeepalivePacketFilter(int i, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException;

    void onAddTcpKeepalivePacketFilter(int i, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException;

    void onBandwidthUpdateRequested() throws RemoteException;

    void onDisconnected() throws RemoteException;

    void onDscpPolicyStatusUpdated(int i, int i2) throws RemoteException;

    void onNetworkCreated() throws RemoteException;

    void onNetworkDestroyed() throws RemoteException;

    void onPreventAutomaticReconnect() throws RemoteException;

    void onQosCallbackUnregistered(int i) throws RemoteException;

    void onQosFilterCallbackRegistered(int i, QosFilterParcelable qosFilterParcelable) throws RemoteException;

    void onRegistered(INetworkAgentRegistry iNetworkAgentRegistry) throws RemoteException;

    void onRemoveKeepalivePacketFilter(int i) throws RemoteException;

    void onSaveAcceptUnvalidated(boolean z) throws RemoteException;

    void onSignalStrengthThresholdsUpdated(int[] iArr) throws RemoteException;

    void onStartNattSocketKeepalive(int i, int i2, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException;

    void onStartTcpSocketKeepalive(int i, int i2, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException;

    void onStopSocketKeepalive(int i) throws RemoteException;

    void onValidationStatusChanged(int i, String str) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkAgent {
        static final int TRANSACTION_onAddNattKeepalivePacketFilter = 11;
        static final int TRANSACTION_onAddTcpKeepalivePacketFilter = 12;
        static final int TRANSACTION_onBandwidthUpdateRequested = 3;
        static final int TRANSACTION_onDisconnected = 2;
        static final int TRANSACTION_onDscpPolicyStatusUpdated = 18;
        static final int TRANSACTION_onNetworkCreated = 16;
        static final int TRANSACTION_onNetworkDestroyed = 17;
        static final int TRANSACTION_onPreventAutomaticReconnect = 10;
        static final int TRANSACTION_onQosCallbackUnregistered = 15;
        static final int TRANSACTION_onQosFilterCallbackRegistered = 14;
        static final int TRANSACTION_onRegistered = 1;
        static final int TRANSACTION_onRemoveKeepalivePacketFilter = 13;
        static final int TRANSACTION_onSaveAcceptUnvalidated = 5;
        static final int TRANSACTION_onSignalStrengthThresholdsUpdated = 9;
        static final int TRANSACTION_onStartNattSocketKeepalive = 6;
        static final int TRANSACTION_onStartTcpSocketKeepalive = 7;
        static final int TRANSACTION_onStopSocketKeepalive = 8;
        static final int TRANSACTION_onValidationStatusChanged = 4;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "onRegistered";
                case 2:
                    return "onDisconnected";
                case 3:
                    return "onBandwidthUpdateRequested";
                case 4:
                    return "onValidationStatusChanged";
                case 5:
                    return "onSaveAcceptUnvalidated";
                case 6:
                    return "onStartNattSocketKeepalive";
                case 7:
                    return "onStartTcpSocketKeepalive";
                case 8:
                    return "onStopSocketKeepalive";
                case 9:
                    return "onSignalStrengthThresholdsUpdated";
                case 10:
                    return "onPreventAutomaticReconnect";
                case 11:
                    return "onAddNattKeepalivePacketFilter";
                case 12:
                    return "onAddTcpKeepalivePacketFilter";
                case 13:
                    return "onRemoveKeepalivePacketFilter";
                case 14:
                    return "onQosFilterCallbackRegistered";
                case 15:
                    return "onQosCallbackUnregistered";
                case 16:
                    return "onNetworkCreated";
                case 17:
                    return "onNetworkDestroyed";
                case 18:
                    return "onDscpPolicyStatusUpdated";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 17;
        }

        public Stub() {
            attachInterface(this, INetworkAgent.DESCRIPTOR);
        }

        public static INetworkAgent asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkAgent.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkAgent)) {
                return new Proxy(iBinder);
            }
            return (INetworkAgent) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkAgent.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        onRegistered(INetworkAgentRegistry.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 2:
                        onDisconnected();
                        break;
                    case 3:
                        onBandwidthUpdateRequested();
                        break;
                    case 4:
                        onValidationStatusChanged(parcel.readInt(), parcel.readString());
                        break;
                    case 5:
                        onSaveAcceptUnvalidated(parcel.readBoolean());
                        break;
                    case 6:
                        onStartNattSocketKeepalive(parcel.readInt(), parcel.readInt(), (NattKeepalivePacketData) parcel.readTypedObject(NattKeepalivePacketData.CREATOR));
                        break;
                    case 7:
                        onStartTcpSocketKeepalive(parcel.readInt(), parcel.readInt(), (TcpKeepalivePacketData) parcel.readTypedObject(TcpKeepalivePacketData.CREATOR));
                        break;
                    case 8:
                        onStopSocketKeepalive(parcel.readInt());
                        break;
                    case 9:
                        onSignalStrengthThresholdsUpdated(parcel.createIntArray());
                        break;
                    case 10:
                        onPreventAutomaticReconnect();
                        break;
                    case 11:
                        onAddNattKeepalivePacketFilter(parcel.readInt(), (NattKeepalivePacketData) parcel.readTypedObject(NattKeepalivePacketData.CREATOR));
                        break;
                    case 12:
                        onAddTcpKeepalivePacketFilter(parcel.readInt(), (TcpKeepalivePacketData) parcel.readTypedObject(TcpKeepalivePacketData.CREATOR));
                        break;
                    case 13:
                        onRemoveKeepalivePacketFilter(parcel.readInt());
                        break;
                    case 14:
                        onQosFilterCallbackRegistered(parcel.readInt(), (QosFilterParcelable) parcel.readTypedObject(QosFilterParcelable.CREATOR));
                        break;
                    case 15:
                        onQosCallbackUnregistered(parcel.readInt());
                        break;
                    case 16:
                        onNetworkCreated();
                        break;
                    case 17:
                        onNetworkDestroyed();
                        break;
                    case 18:
                        onDscpPolicyStatusUpdated(parcel.readInt(), parcel.readInt());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(INetworkAgent.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkAgent {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkAgent.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onRegistered(INetworkAgentRegistry iNetworkAgentRegistry) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkAgentRegistry);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDisconnected() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onBandwidthUpdateRequested() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onValidationStatusChanged(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSaveAcceptUnvalidated(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStartNattSocketKeepalive(int i, int i2, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(nattKeepalivePacketData, 0);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStartTcpSocketKeepalive(int i, int i2, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(tcpKeepalivePacketData, 0);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStopSocketKeepalive(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSignalStrengthThresholdsUpdated(int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPreventAutomaticReconnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAddNattKeepalivePacketFilter(int i, NattKeepalivePacketData nattKeepalivePacketData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nattKeepalivePacketData, 0);
                    this.mRemote.transact(11, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAddTcpKeepalivePacketFilter(int i, TcpKeepalivePacketData tcpKeepalivePacketData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(tcpKeepalivePacketData, 0);
                    this.mRemote.transact(12, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRemoveKeepalivePacketFilter(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onQosFilterCallbackRegistered(int i, QosFilterParcelable qosFilterParcelable) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(qosFilterParcelable, 0);
                    this.mRemote.transact(14, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onQosCallbackUnregistered(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onNetworkCreated() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    this.mRemote.transact(16, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onNetworkDestroyed() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    this.mRemote.transact(17, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDscpPolicyStatusUpdated(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkAgent.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(18, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
