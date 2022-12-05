package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ICarrierMessagingClientService extends IInterface {
    public static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingClientService";

    /* loaded from: classes2.dex */
    public static class Default implements ICarrierMessagingClientService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ICarrierMessagingClientService {
        public Stub() {
            attachInterface(this, ICarrierMessagingClientService.DESCRIPTOR);
        }

        public static ICarrierMessagingClientService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICarrierMessagingClientService.DESCRIPTOR);
            if (iin != null && (iin instanceof ICarrierMessagingClientService)) {
                return (ICarrierMessagingClientService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(ICarrierMessagingClientService.DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ICarrierMessagingClientService {
            public static ICarrierMessagingClientService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICarrierMessagingClientService.DESCRIPTOR;
            }
        }

        public static boolean setDefaultImpl(ICarrierMessagingClientService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ICarrierMessagingClientService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
