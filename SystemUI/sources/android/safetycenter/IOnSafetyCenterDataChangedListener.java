package android.safetycenter;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnSafetyCenterDataChangedListener extends IInterface {
    public static final String DESCRIPTOR = "android.safetycenter.IOnSafetyCenterDataChangedListener";

    public static class Default implements IOnSafetyCenterDataChangedListener {
        public IBinder asBinder() {
            return null;
        }

        public void onError(SafetyCenterErrorDetails safetyCenterErrorDetails) throws RemoteException {
        }

        public void onSafetyCenterDataChanged(SafetyCenterData safetyCenterData) throws RemoteException {
        }
    }

    void onError(SafetyCenterErrorDetails safetyCenterErrorDetails) throws RemoteException;

    void onSafetyCenterDataChanged(SafetyCenterData safetyCenterData) throws RemoteException;

    public static abstract class Stub extends Binder implements IOnSafetyCenterDataChangedListener {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onSafetyCenterDataChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IOnSafetyCenterDataChangedListener.DESCRIPTOR);
        }

        public static IOnSafetyCenterDataChangedListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnSafetyCenterDataChangedListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnSafetyCenterDataChangedListener)) {
                return new Proxy(iBinder);
            }
            return (IOnSafetyCenterDataChangedListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnSafetyCenterDataChangedListener.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onSafetyCenterDataChanged((SafetyCenterData) parcel.readTypedObject(SafetyCenterData.CREATOR));
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onError((SafetyCenterErrorDetails) parcel.readTypedObject(SafetyCenterErrorDetails.CREATOR));
                }
                return true;
            }
            parcel2.writeString(IOnSafetyCenterDataChangedListener.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IOnSafetyCenterDataChangedListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnSafetyCenterDataChangedListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onSafetyCenterDataChanged(SafetyCenterData safetyCenterData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnSafetyCenterDataChangedListener.DESCRIPTOR);
                    obtain.writeTypedObject(safetyCenterData, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(SafetyCenterErrorDetails safetyCenterErrorDetails) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnSafetyCenterDataChangedListener.DESCRIPTOR);
                    obtain.writeTypedObject(safetyCenterErrorDetails, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
