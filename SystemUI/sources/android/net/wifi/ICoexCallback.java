package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ICoexCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ICoexCallback";

    public static class Default implements ICoexCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onCoexUnsafeChannelsChanged(List<CoexUnsafeChannel> list, int i) throws RemoteException {
        }
    }

    void onCoexUnsafeChannelsChanged(List<CoexUnsafeChannel> list, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ICoexCallback {
        static final int TRANSACTION_onCoexUnsafeChannelsChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ICoexCallback.DESCRIPTOR);
        }

        public static ICoexCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ICoexCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ICoexCallback)) {
                return new Proxy(iBinder);
            }
            return (ICoexCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ICoexCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ICoexCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onCoexUnsafeChannelsChanged(parcel.createTypedArrayList(CoexUnsafeChannel.CREATOR), parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements ICoexCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ICoexCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onCoexUnsafeChannelsChanged(List<CoexUnsafeChannel> list, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ICoexCallback.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
