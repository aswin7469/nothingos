package com.google.android.setupcompat;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISetupCompatService extends IInterface {

    public static class Default implements ISetupCompatService {
        public IBinder asBinder() {
            return null;
        }

        public void logMetric(int i, Bundle bundle, Bundle bundle2) throws RemoteException {
        }

        public void onFocusStatusChanged(Bundle bundle) throws RemoteException {
        }

        public void validateActivity(String str, Bundle bundle) throws RemoteException {
        }
    }

    void logMetric(int i, Bundle bundle, Bundle bundle2) throws RemoteException;

    void onFocusStatusChanged(Bundle bundle) throws RemoteException;

    void validateActivity(String str, Bundle bundle) throws RemoteException;

    public static abstract class Stub extends Binder implements ISetupCompatService {
        private static final String DESCRIPTOR = "com.google.android.setupcompat.ISetupCompatService";
        static final int TRANSACTION_logMetric = 2;
        static final int TRANSACTION_onFocusStatusChanged = 3;
        static final int TRANSACTION_validateActivity = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISetupCompatService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISetupCompatService)) {
                return new Proxy(iBinder);
            }
            return (ISetupCompatService) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: android.os.Bundle} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 0
                r1 = 1
                java.lang.String r2 = "com.google.android.setupcompat.ISetupCompatService"
                if (r5 == r1) goto L_0x005a
                r3 = 2
                if (r5 == r3) goto L_0x0030
                r3 = 3
                if (r5 == r3) goto L_0x001a
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0016
                boolean r4 = super.onTransact(r5, r6, r7, r8)
                return r4
            L_0x0016:
                r7.writeString(r2)
                return r1
            L_0x001a:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x002c
                android.os.Parcelable$Creator r5 = android.os.Bundle.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                android.os.Bundle r0 = (android.os.Bundle) r0
            L_0x002c:
                r4.onFocusStatusChanged(r0)
                return r1
            L_0x0030:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0046
                android.os.Parcelable$Creator r7 = android.os.Bundle.CREATOR
                java.lang.Object r7 = r7.createFromParcel(r6)
                android.os.Bundle r7 = (android.os.Bundle) r7
                goto L_0x0047
            L_0x0046:
                r7 = r0
            L_0x0047:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0056
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r0 = r6
                android.os.Bundle r0 = (android.os.Bundle) r0
            L_0x0056:
                r4.logMetric(r5, r7, r0)
                return r1
            L_0x005a:
                r6.enforceInterface(r2)
                java.lang.String r5 = r6.readString()
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0070
                android.os.Parcelable$Creator r7 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r7.createFromParcel(r6)
                r0 = r6
                android.os.Bundle r0 = (android.os.Bundle) r0
            L_0x0070:
                r4.validateActivity(r5, r0)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.ISetupCompatService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        private static class Proxy implements ISetupCompatService {
            public static ISetupCompatService sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void validateActivity(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().validateActivity(str, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void logMetric(int i, Bundle bundle, Bundle bundle2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().logMetric(i, bundle, bundle2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onFocusStatusChanged(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onFocusStatusChanged(bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISetupCompatService iSetupCompatService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSetupCompatService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSetupCompatService;
                return true;
            }
        }

        public static ISetupCompatService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
