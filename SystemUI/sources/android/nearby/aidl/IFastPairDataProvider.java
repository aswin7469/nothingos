package android.nearby.aidl;

import android.nearby.aidl.IFastPairAccountDevicesMetadataCallback;
import android.nearby.aidl.IFastPairAntispoofKeyDeviceMetadataCallback;
import android.nearby.aidl.IFastPairEligibleAccountsCallback;
import android.nearby.aidl.IFastPairManageAccountCallback;
import android.nearby.aidl.IFastPairManageAccountDeviceCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairDataProvider extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairDataProvider";

    public static class Default implements IFastPairDataProvider {
        public IBinder asBinder() {
            return null;
        }

        public void loadFastPairAccountDevicesMetadata(FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel, IFastPairAccountDevicesMetadataCallback iFastPairAccountDevicesMetadataCallback) throws RemoteException {
        }

        public void loadFastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataRequestParcel fastPairAntispoofKeyDeviceMetadataRequestParcel, IFastPairAntispoofKeyDeviceMetadataCallback iFastPairAntispoofKeyDeviceMetadataCallback) throws RemoteException {
        }

        public void loadFastPairEligibleAccounts(FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel, IFastPairEligibleAccountsCallback iFastPairEligibleAccountsCallback) throws RemoteException {
        }

        public void manageFastPairAccount(FastPairManageAccountRequestParcel fastPairManageAccountRequestParcel, IFastPairManageAccountCallback iFastPairManageAccountCallback) throws RemoteException {
        }

        public void manageFastPairAccountDevice(FastPairManageAccountDeviceRequestParcel fastPairManageAccountDeviceRequestParcel, IFastPairManageAccountDeviceCallback iFastPairManageAccountDeviceCallback) throws RemoteException {
        }
    }

    void loadFastPairAccountDevicesMetadata(FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel, IFastPairAccountDevicesMetadataCallback iFastPairAccountDevicesMetadataCallback) throws RemoteException;

    void loadFastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataRequestParcel fastPairAntispoofKeyDeviceMetadataRequestParcel, IFastPairAntispoofKeyDeviceMetadataCallback iFastPairAntispoofKeyDeviceMetadataCallback) throws RemoteException;

    void loadFastPairEligibleAccounts(FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel, IFastPairEligibleAccountsCallback iFastPairEligibleAccountsCallback) throws RemoteException;

    void manageFastPairAccount(FastPairManageAccountRequestParcel fastPairManageAccountRequestParcel, IFastPairManageAccountCallback iFastPairManageAccountCallback) throws RemoteException;

    void manageFastPairAccountDevice(FastPairManageAccountDeviceRequestParcel fastPairManageAccountDeviceRequestParcel, IFastPairManageAccountDeviceCallback iFastPairManageAccountDeviceCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairDataProvider {
        static final int TRANSACTION_loadFastPairAccountDevicesMetadata = 2;
        static final int TRANSACTION_loadFastPairAntispoofKeyDeviceMetadata = 1;
        static final int TRANSACTION_loadFastPairEligibleAccounts = 3;
        static final int TRANSACTION_manageFastPairAccount = 4;
        static final int TRANSACTION_manageFastPairAccountDevice = 5;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "loadFastPairAntispoofKeyDeviceMetadata";
            }
            if (i == 2) {
                return "loadFastPairAccountDevicesMetadata";
            }
            if (i == 3) {
                return "loadFastPairEligibleAccounts";
            }
            if (i == 4) {
                return "manageFastPairAccount";
            }
            if (i != 5) {
                return null;
            }
            return "manageFastPairAccountDevice";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 4;
        }

        public Stub() {
            attachInterface(this, IFastPairDataProvider.DESCRIPTOR);
        }

        public static IFastPairDataProvider asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairDataProvider.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairDataProvider)) {
                return new Proxy(iBinder);
            }
            return (IFastPairDataProvider) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairDataProvider.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IFastPairAntispoofKeyDeviceMetadataCallback asInterface = IFastPairAntispoofKeyDeviceMetadataCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    loadFastPairAntispoofKeyDeviceMetadata((FastPairAntispoofKeyDeviceMetadataRequestParcel) parcel.readTypedObject(FastPairAntispoofKeyDeviceMetadataRequestParcel.CREATOR), asInterface);
                } else if (i == 2) {
                    IFastPairAccountDevicesMetadataCallback asInterface2 = IFastPairAccountDevicesMetadataCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    loadFastPairAccountDevicesMetadata((FastPairAccountDevicesMetadataRequestParcel) parcel.readTypedObject(FastPairAccountDevicesMetadataRequestParcel.CREATOR), asInterface2);
                } else if (i == 3) {
                    IFastPairEligibleAccountsCallback asInterface3 = IFastPairEligibleAccountsCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    loadFastPairEligibleAccounts((FastPairEligibleAccountsRequestParcel) parcel.readTypedObject(FastPairEligibleAccountsRequestParcel.CREATOR), asInterface3);
                } else if (i == 4) {
                    IFastPairManageAccountCallback asInterface4 = IFastPairManageAccountCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    manageFastPairAccount((FastPairManageAccountRequestParcel) parcel.readTypedObject(FastPairManageAccountRequestParcel.CREATOR), asInterface4);
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    IFastPairManageAccountDeviceCallback asInterface5 = IFastPairManageAccountDeviceCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    manageFastPairAccountDevice((FastPairManageAccountDeviceRequestParcel) parcel.readTypedObject(FastPairManageAccountDeviceRequestParcel.CREATOR), asInterface5);
                }
                return true;
            }
            parcel2.writeString(IFastPairDataProvider.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IFastPairDataProvider {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairDataProvider.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void loadFastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataRequestParcel fastPairAntispoofKeyDeviceMetadataRequestParcel, IFastPairAntispoofKeyDeviceMetadataCallback iFastPairAntispoofKeyDeviceMetadataCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairDataProvider.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairAntispoofKeyDeviceMetadataRequestParcel, 0);
                    obtain.writeStrongInterface(iFastPairAntispoofKeyDeviceMetadataCallback);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void loadFastPairAccountDevicesMetadata(FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel, IFastPairAccountDevicesMetadataCallback iFastPairAccountDevicesMetadataCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairDataProvider.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairAccountDevicesMetadataRequestParcel, 0);
                    obtain.writeStrongInterface(iFastPairAccountDevicesMetadataCallback);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void loadFastPairEligibleAccounts(FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel, IFastPairEligibleAccountsCallback iFastPairEligibleAccountsCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairDataProvider.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairEligibleAccountsRequestParcel, 0);
                    obtain.writeStrongInterface(iFastPairEligibleAccountsCallback);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void manageFastPairAccount(FastPairManageAccountRequestParcel fastPairManageAccountRequestParcel, IFastPairManageAccountCallback iFastPairManageAccountCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairDataProvider.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairManageAccountRequestParcel, 0);
                    obtain.writeStrongInterface(iFastPairManageAccountCallback);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void manageFastPairAccountDevice(FastPairManageAccountDeviceRequestParcel fastPairManageAccountDeviceRequestParcel, IFastPairManageAccountDeviceCallback iFastPairManageAccountDeviceCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairDataProvider.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairManageAccountDeviceRequestParcel, 0);
                    obtain.writeStrongInterface(iFastPairManageAccountDeviceCallback);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
