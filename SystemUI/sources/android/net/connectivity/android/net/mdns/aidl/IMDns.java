package android.net.connectivity.android.net.mdns.aidl;

import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMDns extends IInterface {
    public static final String DESCRIPTOR = "android$net$mdns$aidl$IMDns".replace('$', '.');
    public static final String HASH = "ae4cfe565d66acc7d816aabd0dfab991e64031ab";
    public static final int VERSION = 1;

    public static class Default implements IMDns {
        public IBinder asBinder() {
            return null;
        }

        public void discover(DiscoveryInfo discoveryInfo) throws RemoteException {
        }

        public String getInterfaceHash() {
            return "";
        }

        public int getInterfaceVersion() {
            return 0;
        }

        public void getServiceAddress(GetAddressInfo getAddressInfo) throws RemoteException {
        }

        public void registerEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException {
        }

        public void registerService(RegistrationInfo registrationInfo) throws RemoteException {
        }

        public void resolve(ResolutionInfo resolutionInfo) throws RemoteException {
        }

        public void startDaemon() throws RemoteException {
        }

        public void stopDaemon() throws RemoteException {
        }

        public void stopOperation(int i) throws RemoteException {
        }

        public void unregisterEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException {
        }
    }

    void discover(DiscoveryInfo discoveryInfo) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getServiceAddress(GetAddressInfo getAddressInfo) throws RemoteException;

    void registerEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException;

    void registerService(RegistrationInfo registrationInfo) throws RemoteException;

    void resolve(ResolutionInfo resolutionInfo) throws RemoteException;

    void startDaemon() throws RemoteException;

    void stopDaemon() throws RemoteException;

    void stopOperation(int i) throws RemoteException;

    void unregisterEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException;

    public static abstract class Stub extends Binder implements IMDns {
        static final int TRANSACTION_discover = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getServiceAddress = 6;
        static final int TRANSACTION_registerEventListener = 8;
        static final int TRANSACTION_registerService = 3;
        static final int TRANSACTION_resolve = 5;
        static final int TRANSACTION_startDaemon = 1;
        static final int TRANSACTION_stopDaemon = 2;
        static final int TRANSACTION_stopOperation = 7;
        static final int TRANSACTION_unregisterEventListener = 9;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMDns asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IMDns)) {
                return new Proxy(iBinder);
            }
            return (IMDns) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = DESCRIPTOR;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case TRANSACTION_getInterfaceHash /*16777214*/:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case 16777215:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    switch (i) {
                        case 1:
                            startDaemon();
                            parcel2.writeNoException();
                            break;
                        case 2:
                            stopDaemon();
                            parcel2.writeNoException();
                            break;
                        case 3:
                            registerService((RegistrationInfo) parcel.readTypedObject(RegistrationInfo.CREATOR));
                            parcel2.writeNoException();
                            break;
                        case 4:
                            discover((DiscoveryInfo) parcel.readTypedObject(DiscoveryInfo.CREATOR));
                            parcel2.writeNoException();
                            break;
                        case 5:
                            resolve((ResolutionInfo) parcel.readTypedObject(ResolutionInfo.CREATOR));
                            parcel2.writeNoException();
                            break;
                        case 6:
                            getServiceAddress((GetAddressInfo) parcel.readTypedObject(GetAddressInfo.CREATOR));
                            parcel2.writeNoException();
                            break;
                        case 7:
                            stopOperation(parcel.readInt());
                            parcel2.writeNoException();
                            break;
                        case 8:
                            registerEventListener(IMDnsEventListener.Stub.asInterface(parcel.readStrongBinder()));
                            parcel2.writeNoException();
                            break;
                        case 9:
                            unregisterEventListener(IMDnsEventListener.Stub.asInterface(parcel.readStrongBinder()));
                            parcel2.writeNoException();
                            break;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
                    return true;
            }
        }

        private static class Proxy implements IMDns {
            private String mCachedHash = "-1";
            private int mCachedVersion = -1;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            public void startDaemon() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method startDaemon is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopDaemon() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (this.mRemote.transact(2, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method stopDaemon is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerService(RegistrationInfo registrationInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(registrationInfo, 0);
                    if (this.mRemote.transact(3, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method registerService is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void discover(DiscoveryInfo discoveryInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(discoveryInfo, 0);
                    if (this.mRemote.transact(4, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method discover is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void resolve(ResolutionInfo resolutionInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(resolutionInfo, 0);
                    if (this.mRemote.transact(5, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method resolve is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getServiceAddress(GetAddressInfo getAddressInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(getAddressInfo, 0);
                    if (this.mRemote.transact(6, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method getServiceAddress is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopOperation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(7, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method stopOperation is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeStrongInterface(iMDnsEventListener);
                    if (this.mRemote.transact(8, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method registerEventListener is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterEventListener(IMDnsEventListener iMDnsEventListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeStrongInterface(iMDnsEventListener);
                    if (this.mRemote.transact(9, obtain, obtain2, 0)) {
                        obtain2.readException();
                        return;
                    }
                    throw new RemoteException("Method unregisterEventListener is unimplemented.");
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(16777215, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedVersion = obtain2.readInt();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            /* JADX INFO: finally extract failed */
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, obtain, obtain2, 0);
                        obtain2.readException();
                        this.mCachedHash = obtain2.readString();
                        obtain2.recycle();
                        obtain.recycle();
                    } catch (Throwable th) {
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
