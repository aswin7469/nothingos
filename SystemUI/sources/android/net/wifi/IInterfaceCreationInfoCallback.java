package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IInterfaceCreationInfoCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IInterfaceCreationInfoCallback";

    public static class Default implements IInterfaceCreationInfoCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onResults(boolean z, int[] iArr, String[] strArr) throws RemoteException {
        }
    }

    void onResults(boolean z, int[] iArr, String[] strArr) throws RemoteException;

    public static abstract class Stub extends Binder implements IInterfaceCreationInfoCallback {
        static final int TRANSACTION_onResults = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IInterfaceCreationInfoCallback.DESCRIPTOR);
        }

        public static IInterfaceCreationInfoCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IInterfaceCreationInfoCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IInterfaceCreationInfoCallback)) {
                return new Proxy(iBinder);
            }
            return (IInterfaceCreationInfoCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IInterfaceCreationInfoCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IInterfaceCreationInfoCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onResults(parcel.readBoolean(), parcel.createIntArray(), parcel.createStringArray());
                return true;
            }
        }

        private static class Proxy implements IInterfaceCreationInfoCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IInterfaceCreationInfoCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onResults(boolean z, int[] iArr, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IInterfaceCreationInfoCallback.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeIntArray(iArr);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
