package android.net;

import android.net.IIntResultListener;
import android.net.ITetheringEventCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;

public interface ITetheringConnector extends IInterface {
    public static final String DESCRIPTOR = "android.net.ITetheringConnector";

    public static class Default implements ITetheringConnector {
        public IBinder asBinder() {
            return null;
        }

        public void isTetheringSupported(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void registerTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException {
        }

        public void requestLatestTetheringEntitlementResult(int i, ResultReceiver resultReceiver, boolean z, String str, String str2) throws RemoteException {
        }

        public void setPreferTestNetworks(boolean z, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void setUsbTethering(boolean z, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void startTethering(TetheringRequestParcel tetheringRequestParcel, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void stopAllTethering(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void stopTethering(int i, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void tether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException {
        }

        public void unregisterTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException {
        }

        public void untether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException {
        }
    }

    void isTetheringSupported(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException;

    void registerTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException;

    void requestLatestTetheringEntitlementResult(int i, ResultReceiver resultReceiver, boolean z, String str, String str2) throws RemoteException;

    void setPreferTestNetworks(boolean z, IIntResultListener iIntResultListener) throws RemoteException;

    void setUsbTethering(boolean z, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException;

    void startTethering(TetheringRequestParcel tetheringRequestParcel, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException;

    void stopAllTethering(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException;

    void stopTethering(int i, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException;

    void tether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException;

    void unregisterTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException;

    void untether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException;

    public static abstract class Stub extends Binder implements ITetheringConnector {
        static final int TRANSACTION_isTetheringSupported = 9;
        static final int TRANSACTION_registerTetheringEventCallback = 7;
        static final int TRANSACTION_requestLatestTetheringEntitlementResult = 6;
        static final int TRANSACTION_setPreferTestNetworks = 11;
        static final int TRANSACTION_setUsbTethering = 3;
        static final int TRANSACTION_startTethering = 4;
        static final int TRANSACTION_stopAllTethering = 10;
        static final int TRANSACTION_stopTethering = 5;
        static final int TRANSACTION_tether = 1;
        static final int TRANSACTION_unregisterTetheringEventCallback = 8;
        static final int TRANSACTION_untether = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ITetheringConnector.DESCRIPTOR);
        }

        public static ITetheringConnector asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITetheringConnector.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITetheringConnector)) {
                return new Proxy(iBinder);
            }
            return (ITetheringConnector) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ITetheringConnector.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        tether(parcel.readString(), parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 2:
                        untether(parcel.readString(), parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 3:
                        setUsbTethering(parcel.readBoolean(), parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 4:
                        startTethering((TetheringRequestParcel) parcel.readTypedObject(TetheringRequestParcel.CREATOR), parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 5:
                        stopTethering(parcel.readInt(), parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 6:
                        requestLatestTetheringEntitlementResult(parcel.readInt(), (ResultReceiver) parcel.readTypedObject(ResultReceiver.CREATOR), parcel.readBoolean(), parcel.readString(), parcel.readString());
                        break;
                    case 7:
                        registerTetheringEventCallback(ITetheringEventCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        break;
                    case 8:
                        unregisterTetheringEventCallback(ITetheringEventCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        break;
                    case 9:
                        isTetheringSupported(parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 10:
                        stopAllTethering(parcel.readString(), parcel.readString(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 11:
                        setPreferTestNetworks(parcel.readBoolean(), IIntResultListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(ITetheringConnector.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ITetheringConnector {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ITetheringConnector.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void tether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void untether(String str, String str2, String str3, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setUsbTethering(boolean z, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void startTethering(TetheringRequestParcel tetheringRequestParcel, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeTypedObject(tetheringRequestParcel, 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void stopTethering(int i, String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void requestLatestTetheringEntitlementResult(int i, ResultReceiver resultReceiver, boolean z, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(resultReceiver, 0);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void registerTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeStrongInterface(iTetheringEventCallback);
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeStrongInterface(iTetheringEventCallback);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void isTetheringSupported(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void stopAllTethering(String str, String str2, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setPreferTestNetworks(boolean z, IIntResultListener iIntResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheringConnector.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeStrongInterface(iIntResultListener);
                    this.mRemote.transact(11, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
