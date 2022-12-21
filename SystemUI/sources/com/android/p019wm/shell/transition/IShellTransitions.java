package com.android.p019wm.shell.transition;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.window.RemoteTransition;
import android.window.TransitionFilter;

/* renamed from: com.android.wm.shell.transition.IShellTransitions */
public interface IShellTransitions extends IInterface {

    /* renamed from: com.android.wm.shell.transition.IShellTransitions$Default */
    public static class Default implements IShellTransitions {
        public IBinder asBinder() {
            return null;
        }

        public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) throws RemoteException {
        }

        public void unregisterRemote(RemoteTransition remoteTransition) throws RemoteException {
        }
    }

    void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) throws RemoteException;

    void unregisterRemote(RemoteTransition remoteTransition) throws RemoteException;

    /* renamed from: com.android.wm.shell.transition.IShellTransitions$Stub */
    public static abstract class Stub extends Binder implements IShellTransitions {
        private static final String DESCRIPTOR = "com.android.wm.shell.transition.IShellTransitions";
        static final int TRANSACTION_registerRemote = 2;
        static final int TRANSACTION_unregisterRemote = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IShellTransitions asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IShellTransitions)) {
                return new Proxy(iBinder);
            }
            return (IShellTransitions) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: android.window.RemoteTransition} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.window.RemoteTransition} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 2
                r1 = 0
                r2 = 1
                java.lang.String r3 = "com.android.wm.shell.transition.IShellTransitions"
                if (r5 == r0) goto L_0x002e
                r0 = 3
                if (r5 == r0) goto L_0x0018
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0014
                boolean r4 = super.onTransact(r5, r6, r7, r8)
                return r4
            L_0x0014:
                r7.writeString(r3)
                return r2
            L_0x0018:
                r6.enforceInterface(r3)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x002a
                android.os.Parcelable$Creator r5 = android.window.RemoteTransition.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r1 = r5
                android.window.RemoteTransition r1 = (android.window.RemoteTransition) r1
            L_0x002a:
                r4.unregisterRemote(r1)
                return r2
            L_0x002e:
                r6.enforceInterface(r3)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0040
                android.os.Parcelable$Creator r5 = android.window.TransitionFilter.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                android.window.TransitionFilter r5 = (android.window.TransitionFilter) r5
                goto L_0x0041
            L_0x0040:
                r5 = r1
            L_0x0041:
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0050
                android.os.Parcelable$Creator r7 = android.window.RemoteTransition.CREATOR
                java.lang.Object r6 = r7.createFromParcel(r6)
                r1 = r6
                android.window.RemoteTransition r1 = (android.window.RemoteTransition) r1
            L_0x0050:
                r4.registerRemote(r5, r1)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.transition.IShellTransitions.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* renamed from: com.android.wm.shell.transition.IShellTransitions$Stub$Proxy */
        private static class Proxy implements IShellTransitions {
            public static IShellTransitions sDefaultImpl;
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

            public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transitionFilter != null) {
                        obtain.writeInt(1);
                        transitionFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (remoteTransition != null) {
                        obtain.writeInt(1);
                        remoteTransition.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().registerRemote(transitionFilter, remoteTransition);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterRemote(RemoteTransition remoteTransition) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (remoteTransition != null) {
                        obtain.writeInt(1);
                        remoteTransition.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterRemote(remoteTransition);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IShellTransitions iShellTransitions) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iShellTransitions == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iShellTransitions;
                return true;
            }
        }

        public static IShellTransitions getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
