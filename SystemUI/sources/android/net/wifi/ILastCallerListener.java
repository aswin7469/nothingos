package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILastCallerListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ILastCallerListener";

    public static class Default implements ILastCallerListener {
        public IBinder asBinder() {
            return null;
        }

        public void onResult(String str, boolean z) throws RemoteException {
        }
    }

    void onResult(String str, boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements ILastCallerListener {
        static final int TRANSACTION_onResult = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ILastCallerListener.DESCRIPTOR);
        }

        public static ILastCallerListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ILastCallerListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ILastCallerListener)) {
                return new Proxy(iBinder);
            }
            return (ILastCallerListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ILastCallerListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ILastCallerListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onResult(parcel.readString(), parcel.readBoolean());
                return true;
            }
        }

        private static class Proxy implements ILastCallerListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ILastCallerListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onResult(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ILastCallerListener.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
