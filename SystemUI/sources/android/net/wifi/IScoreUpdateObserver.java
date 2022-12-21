package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IScoreUpdateObserver extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IScoreUpdateObserver";

    public static class Default implements IScoreUpdateObserver {
        public IBinder asBinder() {
            return null;
        }

        public void blocklistCurrentBssid(int i) throws RemoteException {
        }

        public void notifyScoreUpdate(int i, int i2) throws RemoteException {
        }

        public void notifyStatusUpdate(int i, boolean z) throws RemoteException {
        }

        public void requestNudOperation(int i) throws RemoteException {
        }

        public void triggerUpdateOfWifiUsabilityStats(int i) throws RemoteException {
        }
    }

    void blocklistCurrentBssid(int i) throws RemoteException;

    void notifyScoreUpdate(int i, int i2) throws RemoteException;

    void notifyStatusUpdate(int i, boolean z) throws RemoteException;

    void requestNudOperation(int i) throws RemoteException;

    void triggerUpdateOfWifiUsabilityStats(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IScoreUpdateObserver {
        static final int TRANSACTION_blocklistCurrentBssid = 5;
        static final int TRANSACTION_notifyScoreUpdate = 1;
        static final int TRANSACTION_notifyStatusUpdate = 3;
        static final int TRANSACTION_requestNudOperation = 4;
        static final int TRANSACTION_triggerUpdateOfWifiUsabilityStats = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IScoreUpdateObserver.DESCRIPTOR);
        }

        public static IScoreUpdateObserver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IScoreUpdateObserver.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IScoreUpdateObserver)) {
                return new Proxy(iBinder);
            }
            return (IScoreUpdateObserver) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IScoreUpdateObserver.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    notifyScoreUpdate(parcel.readInt(), parcel.readInt());
                } else if (i == 2) {
                    triggerUpdateOfWifiUsabilityStats(parcel.readInt());
                } else if (i == 3) {
                    notifyStatusUpdate(parcel.readInt(), parcel.readBoolean());
                } else if (i == 4) {
                    requestNudOperation(parcel.readInt());
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    blocklistCurrentBssid(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(IScoreUpdateObserver.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IScoreUpdateObserver {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IScoreUpdateObserver.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void notifyScoreUpdate(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScoreUpdateObserver.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void triggerUpdateOfWifiUsabilityStats(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScoreUpdateObserver.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void notifyStatusUpdate(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScoreUpdateObserver.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void requestNudOperation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScoreUpdateObserver.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void blocklistCurrentBssid(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScoreUpdateObserver.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
