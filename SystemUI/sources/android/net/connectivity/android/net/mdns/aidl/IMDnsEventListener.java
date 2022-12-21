package android.net.connectivity.android.net.mdns.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMDnsEventListener extends IInterface {
    public static final String DESCRIPTOR = "android$net$mdns$aidl$IMDnsEventListener".replace('$', '.');
    public static final String HASH = "ae4cfe565d66acc7d816aabd0dfab991e64031ab";
    public static final int SERVICE_DISCOVERY_FAILED = 602;
    public static final int SERVICE_FOUND = 603;
    public static final int SERVICE_GET_ADDR_FAILED = 611;
    public static final int SERVICE_GET_ADDR_SUCCESS = 612;
    public static final int SERVICE_LOST = 604;
    public static final int SERVICE_REGISTERED = 606;
    public static final int SERVICE_REGISTRATION_FAILED = 605;
    public static final int SERVICE_RESOLUTION_FAILED = 607;
    public static final int SERVICE_RESOLVED = 608;
    public static final int VERSION = 1;

    public static class Default implements IMDnsEventListener {
        public IBinder asBinder() {
            return null;
        }

        public String getInterfaceHash() {
            return "";
        }

        public int getInterfaceVersion() {
            return 0;
        }

        public void onGettingServiceAddressStatus(GetAddressInfo getAddressInfo) throws RemoteException {
        }

        public void onServiceDiscoveryStatus(DiscoveryInfo discoveryInfo) throws RemoteException {
        }

        public void onServiceRegistrationStatus(RegistrationInfo registrationInfo) throws RemoteException {
        }

        public void onServiceResolutionStatus(ResolutionInfo resolutionInfo) throws RemoteException {
        }
    }

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void onGettingServiceAddressStatus(GetAddressInfo getAddressInfo) throws RemoteException;

    void onServiceDiscoveryStatus(DiscoveryInfo discoveryInfo) throws RemoteException;

    void onServiceRegistrationStatus(RegistrationInfo registrationInfo) throws RemoteException;

    void onServiceResolutionStatus(ResolutionInfo resolutionInfo) throws RemoteException;

    public static abstract class Stub extends Binder implements IMDnsEventListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onGettingServiceAddressStatus = 4;
        static final int TRANSACTION_onServiceDiscoveryStatus = 2;
        static final int TRANSACTION_onServiceRegistrationStatus = 1;
        static final int TRANSACTION_onServiceResolutionStatus = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMDnsEventListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IMDnsEventListener)) {
                return new Proxy(iBinder);
            }
            return (IMDnsEventListener) queryLocalInterface;
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
                    if (i == 1) {
                        onServiceRegistrationStatus((RegistrationInfo) parcel.readTypedObject(RegistrationInfo.CREATOR));
                    } else if (i == 2) {
                        onServiceDiscoveryStatus((DiscoveryInfo) parcel.readTypedObject(DiscoveryInfo.CREATOR));
                    } else if (i == 3) {
                        onServiceResolutionStatus((ResolutionInfo) parcel.readTypedObject(ResolutionInfo.CREATOR));
                    } else if (i != 4) {
                        return super.onTransact(i, parcel, parcel2, i2);
                    } else {
                        onGettingServiceAddressStatus((GetAddressInfo) parcel.readTypedObject(GetAddressInfo.CREATOR));
                    }
                    return true;
            }
        }

        private static class Proxy implements IMDnsEventListener {
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

            public void onServiceRegistrationStatus(RegistrationInfo registrationInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(registrationInfo, 0);
                    if (!this.mRemote.transact(1, obtain, (Parcel) null, 1)) {
                        throw new RemoteException("Method onServiceRegistrationStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceDiscoveryStatus(DiscoveryInfo discoveryInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(discoveryInfo, 0);
                    if (!this.mRemote.transact(2, obtain, (Parcel) null, 1)) {
                        throw new RemoteException("Method onServiceDiscoveryStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceResolutionStatus(ResolutionInfo resolutionInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(resolutionInfo, 0);
                    if (!this.mRemote.transact(3, obtain, (Parcel) null, 1)) {
                        throw new RemoteException("Method onServiceResolutionStatus is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onGettingServiceAddressStatus(GetAddressInfo getAddressInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeTypedObject(getAddressInfo, 0);
                    if (!this.mRemote.transact(4, obtain, (Parcel) null, 1)) {
                        throw new RemoteException("Method onGettingServiceAddressStatus is unimplemented.");
                    }
                } finally {
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
