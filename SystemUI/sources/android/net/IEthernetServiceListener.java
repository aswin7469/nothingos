package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEthernetServiceListener extends IInterface {

    public static class Default implements IEthernetServiceListener {
        public IBinder asBinder() {
            return null;
        }

        public void onEthernetStateChanged(int i) throws RemoteException {
        }

        public void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) throws RemoteException {
        }
    }

    void onEthernetStateChanged(int i) throws RemoteException;

    void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) throws RemoteException;

    public static abstract class Stub extends Binder implements IEthernetServiceListener {
        public static final String DESCRIPTOR = "android.net.IEthernetServiceListener";
        static final int TRANSACTION_onEthernetStateChanged = 1;
        static final int TRANSACTION_onInterfaceStateChanged = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onEthernetStateChanged";
            }
            if (i != 2) {
                return null;
            }
            return "onInterfaceStateChanged";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEthernetServiceListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IEthernetServiceListener)) {
                return new Proxy(iBinder);
            }
            return (IEthernetServiceListener) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onEthernetStateChanged(readInt);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    onInterfaceStateChanged(parcel.readString(), parcel.readInt(), parcel.readInt(), (IpConfiguration) parcel.readTypedObject(IpConfiguration.CREATOR));
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IEthernetServiceListener {
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

            public void onEthernetStateChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(ipConfiguration, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
