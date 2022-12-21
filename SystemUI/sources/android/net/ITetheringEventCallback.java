package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ITetheringEventCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.ITetheringEventCallback";

    public static class Default implements ITetheringEventCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onCallbackStarted(TetheringCallbackStartedParcel tetheringCallbackStartedParcel) throws RemoteException {
        }

        public void onCallbackStopped(int i) throws RemoteException {
        }

        public void onConfigurationChanged(TetheringConfigurationParcel tetheringConfigurationParcel) throws RemoteException {
        }

        public void onOffloadStatusChanged(int i) throws RemoteException {
        }

        public void onSupportedTetheringTypes(long j) throws RemoteException {
        }

        public void onTetherClientsChanged(List<TetheredClient> list) throws RemoteException {
        }

        public void onTetherStatesChanged(TetherStatesParcel tetherStatesParcel) throws RemoteException {
        }

        public void onUpstreamChanged(Network network) throws RemoteException {
        }
    }

    void onCallbackStarted(TetheringCallbackStartedParcel tetheringCallbackStartedParcel) throws RemoteException;

    void onCallbackStopped(int i) throws RemoteException;

    void onConfigurationChanged(TetheringConfigurationParcel tetheringConfigurationParcel) throws RemoteException;

    void onOffloadStatusChanged(int i) throws RemoteException;

    void onSupportedTetheringTypes(long j) throws RemoteException;

    void onTetherClientsChanged(List<TetheredClient> list) throws RemoteException;

    void onTetherStatesChanged(TetherStatesParcel tetherStatesParcel) throws RemoteException;

    void onUpstreamChanged(Network network) throws RemoteException;

    public static abstract class Stub extends Binder implements ITetheringEventCallback {
        static final int TRANSACTION_onCallbackStarted = 1;
        static final int TRANSACTION_onCallbackStopped = 2;
        static final int TRANSACTION_onConfigurationChanged = 4;
        static final int TRANSACTION_onOffloadStatusChanged = 7;
        static final int TRANSACTION_onSupportedTetheringTypes = 8;
        static final int TRANSACTION_onTetherClientsChanged = 6;
        static final int TRANSACTION_onTetherStatesChanged = 5;
        static final int TRANSACTION_onUpstreamChanged = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ITetheringEventCallback.DESCRIPTOR);
        }

        public static ITetheringEventCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITetheringEventCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITetheringEventCallback)) {
                return new Proxy(iBinder);
            }
            return (ITetheringEventCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ITetheringEventCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        onCallbackStarted((TetheringCallbackStartedParcel) parcel.readTypedObject(TetheringCallbackStartedParcel.CREATOR));
                        break;
                    case 2:
                        onCallbackStopped(parcel.readInt());
                        break;
                    case 3:
                        onUpstreamChanged((Network) parcel.readTypedObject(Network.CREATOR));
                        break;
                    case 4:
                        onConfigurationChanged((TetheringConfigurationParcel) parcel.readTypedObject(TetheringConfigurationParcel.CREATOR));
                        break;
                    case 5:
                        onTetherStatesChanged((TetherStatesParcel) parcel.readTypedObject(TetherStatesParcel.CREATOR));
                        break;
                    case 6:
                        onTetherClientsChanged(parcel.createTypedArrayList(TetheredClient.CREATOR));
                        break;
                    case 7:
                        onOffloadStatusChanged(parcel.readInt());
                        break;
                    case 8:
                        onSupportedTetheringTypes(parcel.readLong());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(ITetheringEventCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ITetheringEventCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ITetheringEventCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onCallbackStarted(TetheringCallbackStartedParcel tetheringCallbackStartedParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeTypedObject(tetheringCallbackStartedParcel, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onCallbackStopped(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUpstreamChanged(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onConfigurationChanged(TetheringConfigurationParcel tetheringConfigurationParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeTypedObject(tetheringConfigurationParcel, 0);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onTetherStatesChanged(TetherStatesParcel tetherStatesParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeTypedObject(tetherStatesParcel, 0);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onTetherClientsChanged(List<TetheredClient> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onOffloadStatusChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSupportedTetheringTypes(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringEventCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
