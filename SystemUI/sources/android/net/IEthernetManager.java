package android.net;

import android.net.IEthernetServiceListener;
import android.net.INetworkInterfaceOutcomeReceiver;
import android.net.ITetheredInterfaceCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IEthernetManager extends IInterface {

    public static class Default implements IEthernetManager {
        public void addListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void connectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
        }

        public void disconnectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
        }

        public String[] getAvailableInterfaces() throws RemoteException {
            return null;
        }

        public IpConfiguration getConfiguration(String str) throws RemoteException {
            return null;
        }

        public List<String> getInterfaceList() throws RemoteException {
            return null;
        }

        public boolean isAvailable(String str) throws RemoteException {
            return false;
        }

        public void releaseTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException {
        }

        public void removeListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException {
        }

        public void requestTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException {
        }

        public void setConfiguration(String str, IpConfiguration ipConfiguration) throws RemoteException {
        }

        public void setEthernetEnabled(boolean z) throws RemoteException {
        }

        public void setIncludeTestInterfaces(boolean z) throws RemoteException {
        }

        public void updateConfiguration(String str, EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
        }
    }

    void addListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException;

    void connectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException;

    void disconnectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException;

    String[] getAvailableInterfaces() throws RemoteException;

    IpConfiguration getConfiguration(String str) throws RemoteException;

    List<String> getInterfaceList() throws RemoteException;

    boolean isAvailable(String str) throws RemoteException;

    void releaseTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException;

    void removeListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException;

    void requestTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException;

    void setConfiguration(String str, IpConfiguration ipConfiguration) throws RemoteException;

    void setEthernetEnabled(boolean z) throws RemoteException;

    void setIncludeTestInterfaces(boolean z) throws RemoteException;

    void updateConfiguration(String str, EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException;

    public static abstract class Stub extends Binder implements IEthernetManager {
        public static final String DESCRIPTOR = "android.net.IEthernetManager";
        static final int TRANSACTION_addListener = 5;
        static final int TRANSACTION_connectNetwork = 11;
        static final int TRANSACTION_disconnectNetwork = 12;
        static final int TRANSACTION_getAvailableInterfaces = 1;
        static final int TRANSACTION_getConfiguration = 2;
        static final int TRANSACTION_getInterfaceList = 14;
        static final int TRANSACTION_isAvailable = 4;
        static final int TRANSACTION_releaseTetheredInterface = 9;
        static final int TRANSACTION_removeListener = 6;
        static final int TRANSACTION_requestTetheredInterface = 8;
        static final int TRANSACTION_setConfiguration = 3;
        static final int TRANSACTION_setEthernetEnabled = 13;
        static final int TRANSACTION_setIncludeTestInterfaces = 7;
        static final int TRANSACTION_updateConfiguration = 10;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "getAvailableInterfaces";
                case 2:
                    return "getConfiguration";
                case 3:
                    return "setConfiguration";
                case 4:
                    return "isAvailable";
                case 5:
                    return "addListener";
                case 6:
                    return "removeListener";
                case 7:
                    return "setIncludeTestInterfaces";
                case 8:
                    return "requestTetheredInterface";
                case 9:
                    return "releaseTetheredInterface";
                case 10:
                    return "updateConfiguration";
                case 11:
                    return "connectNetwork";
                case 12:
                    return "disconnectNetwork";
                case 13:
                    return "setEthernetEnabled";
                case 14:
                    return "getInterfaceList";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 13;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEthernetManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IEthernetManager)) {
                return new Proxy(iBinder);
            }
            return (IEthernetManager) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        String[] availableInterfaces = getAvailableInterfaces();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(availableInterfaces);
                        break;
                    case 2:
                        String readString = parcel.readString();
                        parcel.enforceNoDataAvail();
                        IpConfiguration configuration = getConfiguration(readString);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(configuration, 1);
                        break;
                    case 3:
                        parcel.enforceNoDataAvail();
                        setConfiguration(parcel.readString(), (IpConfiguration) parcel.readTypedObject(IpConfiguration.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 4:
                        String readString2 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean isAvailable = isAvailable(readString2);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isAvailable);
                        break;
                    case 5:
                        IEthernetServiceListener asInterface = IEthernetServiceListener.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        addListener(asInterface);
                        parcel2.writeNoException();
                        break;
                    case 6:
                        IEthernetServiceListener asInterface2 = IEthernetServiceListener.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        removeListener(asInterface2);
                        parcel2.writeNoException();
                        break;
                    case 7:
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        setIncludeTestInterfaces(readBoolean);
                        parcel2.writeNoException();
                        break;
                    case 8:
                        ITetheredInterfaceCallback asInterface3 = ITetheredInterfaceCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        requestTetheredInterface(asInterface3);
                        parcel2.writeNoException();
                        break;
                    case 9:
                        ITetheredInterfaceCallback asInterface4 = ITetheredInterfaceCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        releaseTetheredInterface(asInterface4);
                        parcel2.writeNoException();
                        break;
                    case 10:
                        INetworkInterfaceOutcomeReceiver asInterface5 = INetworkInterfaceOutcomeReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        updateConfiguration(parcel.readString(), (EthernetNetworkUpdateRequest) parcel.readTypedObject(EthernetNetworkUpdateRequest.CREATOR), asInterface5);
                        parcel2.writeNoException();
                        break;
                    case 11:
                        String readString3 = parcel.readString();
                        INetworkInterfaceOutcomeReceiver asInterface6 = INetworkInterfaceOutcomeReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        connectNetwork(readString3, asInterface6);
                        parcel2.writeNoException();
                        break;
                    case 12:
                        String readString4 = parcel.readString();
                        INetworkInterfaceOutcomeReceiver asInterface7 = INetworkInterfaceOutcomeReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        disconnectNetwork(readString4, asInterface7);
                        parcel2.writeNoException();
                        break;
                    case 13:
                        boolean readBoolean2 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        setEthernetEnabled(readBoolean2);
                        parcel2.writeNoException();
                        break;
                    case 14:
                        List<String> interfaceList = getInterfaceList();
                        parcel2.writeNoException();
                        parcel2.writeStringList(interfaceList);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IEthernetManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String[] getAvailableInterfaces() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IpConfiguration getConfiguration(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IpConfiguration) obtain2.readTypedObject(IpConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setConfiguration(String str, IpConfiguration ipConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(ipConfiguration, 0);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isAvailable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iEthernetServiceListener);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iEthernetServiceListener);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIncludeTestInterfaces(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iTetheredInterfaceCallback);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void releaseTetheredInterface(ITetheredInterfaceCallback iTetheredInterfaceCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iTetheredInterfaceCallback);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateConfiguration(String str, EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(ethernetNetworkUpdateRequest, 0);
                    obtain.writeStrongInterface(iNetworkInterfaceOutcomeReceiver);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void connectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iNetworkInterfaceOutcomeReceiver);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disconnectNetwork(String str, INetworkInterfaceOutcomeReceiver iNetworkInterfaceOutcomeReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iNetworkInterfaceOutcomeReceiver);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setEthernetEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getInterfaceList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
