package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISuggestionUserApprovalStatusListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ISuggestionUserApprovalStatusListener";

    public static class Default implements ISuggestionUserApprovalStatusListener {
        public IBinder asBinder() {
            return null;
        }

        public void onUserApprovalStatusChange(int i) throws RemoteException {
        }
    }

    void onUserApprovalStatusChange(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISuggestionUserApprovalStatusListener {
        static final int TRANSACTION_onUserApprovalStatusChange = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ISuggestionUserApprovalStatusListener.DESCRIPTOR);
        }

        public static ISuggestionUserApprovalStatusListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISuggestionUserApprovalStatusListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISuggestionUserApprovalStatusListener)) {
                return new Proxy(iBinder);
            }
            return (ISuggestionUserApprovalStatusListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISuggestionUserApprovalStatusListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ISuggestionUserApprovalStatusListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onUserApprovalStatusChange(parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements ISuggestionUserApprovalStatusListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ISuggestionUserApprovalStatusListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onUserApprovalStatusChange(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISuggestionUserApprovalStatusListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
