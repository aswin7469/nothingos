package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITestNetworkManager extends IInterface {
    public static final String DESCRIPTOR = "android.net.ITestNetworkManager";

    public static class Default implements ITestNetworkManager {
        public IBinder asBinder() {
            return null;
        }

        public TestNetworkInterface createInterface(boolean z, boolean z2, LinkAddress[] linkAddressArr, String str) throws RemoteException {
            return null;
        }

        public void setupTestNetwork(String str, LinkProperties linkProperties, boolean z, int[] iArr, IBinder iBinder) throws RemoteException {
        }

        public void teardownTestNetwork(int i) throws RemoteException {
        }
    }

    TestNetworkInterface createInterface(boolean z, boolean z2, LinkAddress[] linkAddressArr, String str) throws RemoteException;

    void setupTestNetwork(String str, LinkProperties linkProperties, boolean z, int[] iArr, IBinder iBinder) throws RemoteException;

    void teardownTestNetwork(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ITestNetworkManager {
        static final int TRANSACTION_createInterface = 1;
        static final int TRANSACTION_setupTestNetwork = 2;
        static final int TRANSACTION_teardownTestNetwork = 3;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "createInterface";
            }
            if (i == 2) {
                return "setupTestNetwork";
            }
            if (i != 3) {
                return null;
            }
            return "teardownTestNetwork";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 2;
        }

        public Stub() {
            attachInterface(this, ITestNetworkManager.DESCRIPTOR);
        }

        public static ITestNetworkManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITestNetworkManager.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITestNetworkManager)) {
                return new Proxy(iBinder);
            }
            return (ITestNetworkManager) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ITestNetworkManager.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    TestNetworkInterface createInterface = createInterface(parcel.readBoolean(), parcel.readBoolean(), (LinkAddress[]) parcel.createTypedArray(LinkAddress.CREATOR), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(createInterface, 1);
                } else if (i == 2) {
                    setupTestNetwork(parcel.readString(), (LinkProperties) parcel.readTypedObject(LinkProperties.CREATOR), parcel.readBoolean(), parcel.createIntArray(), parcel.readStrongBinder());
                    parcel2.writeNoException();
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    teardownTestNetwork(parcel.readInt());
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString(ITestNetworkManager.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ITestNetworkManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ITestNetworkManager.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public TestNetworkInterface createInterface(boolean z, boolean z2, LinkAddress[] linkAddressArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITestNetworkManager.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    obtain.writeTypedArray(linkAddressArr, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (TestNetworkInterface) obtain2.readTypedObject(TestNetworkInterface.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setupTestNetwork(String str, LinkProperties linkProperties, boolean z, int[] iArr, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITestNetworkManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(linkProperties, 0);
                    obtain.writeBoolean(z);
                    obtain.writeIntArray(iArr);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void teardownTestNetwork(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITestNetworkManager.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
