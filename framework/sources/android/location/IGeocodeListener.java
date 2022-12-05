package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface IGeocodeListener extends IInterface {
    public static final String DESCRIPTOR = "android.location.IGeocodeListener";

    void onResults(String str, List<Address> list) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IGeocodeListener {
        @Override // android.location.IGeocodeListener
        public void onResults(String error, List<Address> results) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IGeocodeListener {
        static final int TRANSACTION_onResults = 1;

        public Stub() {
            attachInterface(this, IGeocodeListener.DESCRIPTOR);
        }

        public static IGeocodeListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IGeocodeListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IGeocodeListener)) {
                return (IGeocodeListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onResults";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IGeocodeListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IGeocodeListener.DESCRIPTOR);
                            String _arg0 = data.readString();
                            List<Address> _arg1 = data.createTypedArrayList(Address.CREATOR);
                            onResults(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IGeocodeListener {
            public static IGeocodeListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IGeocodeListener.DESCRIPTOR;
            }

            @Override // android.location.IGeocodeListener
            public void onResults(String error, List<Address> results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGeocodeListener.DESCRIPTOR);
                    _data.writeString(error);
                    _data.writeTypedList(results);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onResults(error, results);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGeocodeListener impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IGeocodeListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
