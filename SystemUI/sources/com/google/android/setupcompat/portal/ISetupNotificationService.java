package com.google.android.setupcompat.portal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;

public interface ISetupNotificationService extends IInterface {

    public static class Default implements ISetupNotificationService {
        public IBinder asBinder() {
            return null;
        }

        public boolean isPortalAvailable() throws RemoteException {
            return false;
        }

        public boolean isProgressServiceAlive(ProgressServiceComponent progressServiceComponent, UserHandle userHandle) throws RemoteException {
            return false;
        }

        public boolean registerNotification(NotificationComponent notificationComponent) throws RemoteException {
            return false;
        }

        public void registerProgressService(ProgressServiceComponent progressServiceComponent, UserHandle userHandle, IPortalRegisterResultListener iPortalRegisterResultListener) throws RemoteException {
        }

        public void unregisterNotification(NotificationComponent notificationComponent) throws RemoteException {
        }
    }

    boolean isPortalAvailable() throws RemoteException;

    boolean isProgressServiceAlive(ProgressServiceComponent progressServiceComponent, UserHandle userHandle) throws RemoteException;

    boolean registerNotification(NotificationComponent notificationComponent) throws RemoteException;

    void registerProgressService(ProgressServiceComponent progressServiceComponent, UserHandle userHandle, IPortalRegisterResultListener iPortalRegisterResultListener) throws RemoteException;

    void unregisterNotification(NotificationComponent notificationComponent) throws RemoteException;

    public static abstract class Stub extends Binder implements ISetupNotificationService {
        private static final String DESCRIPTOR = "com.google.android.setupcompat.portal.ISetupNotificationService";
        static final int TRANSACTION_isPortalAvailable = 5;
        static final int TRANSACTION_isProgressServiceAlive = 4;
        static final int TRANSACTION_registerNotification = 1;
        static final int TRANSACTION_registerProgressService = 3;
        static final int TRANSACTION_unregisterNotification = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISetupNotificationService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISetupNotificationService)) {
                return new Proxy(iBinder);
            }
            return (ISetupNotificationService) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: com.google.android.setupcompat.portal.NotificationComponent} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: com.google.android.setupcompat.portal.NotificationComponent} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.os.UserHandle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: android.os.UserHandle} */
        /* JADX WARNING: type inference failed for: r0v0 */
        /* JADX WARNING: type inference failed for: r0v13 */
        /* JADX WARNING: type inference failed for: r0v14 */
        /* JADX WARNING: type inference failed for: r0v15 */
        /* JADX WARNING: type inference failed for: r0v16 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 0
                r1 = 1
                java.lang.String r2 = "com.google.android.setupcompat.portal.ISetupNotificationService"
                if (r5 == r1) goto L_0x00a5
                r3 = 2
                if (r5 == r3) goto L_0x008c
                r3 = 3
                if (r5 == r3) goto L_0x005b
                r3 = 4
                if (r5 == r3) goto L_0x002e
                r0 = 5
                if (r5 == r0) goto L_0x0020
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x001c
                boolean r4 = super.onTransact(r5, r6, r7, r8)
                return r4
            L_0x001c:
                r7.writeString(r2)
                return r1
            L_0x0020:
                r6.enforceInterface(r2)
                boolean r4 = r4.isPortalAvailable()
                r7.writeNoException()
                r7.writeInt(r4)
                return r1
            L_0x002e:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0040
                android.os.Parcelable$Creator<com.google.android.setupcompat.portal.ProgressServiceComponent> r5 = com.google.android.setupcompat.portal.ProgressServiceComponent.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.setupcompat.portal.ProgressServiceComponent r5 = (com.google.android.setupcompat.portal.ProgressServiceComponent) r5
                goto L_0x0041
            L_0x0040:
                r5 = r0
            L_0x0041:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0050
                android.os.Parcelable$Creator r8 = android.os.UserHandle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r0 = r6
                android.os.UserHandle r0 = (android.os.UserHandle) r0
            L_0x0050:
                boolean r4 = r4.isProgressServiceAlive(r5, r0)
                r7.writeNoException()
                r7.writeInt(r4)
                return r1
            L_0x005b:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x006d
                android.os.Parcelable$Creator<com.google.android.setupcompat.portal.ProgressServiceComponent> r5 = com.google.android.setupcompat.portal.ProgressServiceComponent.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.setupcompat.portal.ProgressServiceComponent r5 = (com.google.android.setupcompat.portal.ProgressServiceComponent) r5
                goto L_0x006e
            L_0x006d:
                r5 = r0
            L_0x006e:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x007d
                android.os.Parcelable$Creator r8 = android.os.UserHandle.CREATOR
                java.lang.Object r8 = r8.createFromParcel(r6)
                r0 = r8
                android.os.UserHandle r0 = (android.os.UserHandle) r0
            L_0x007d:
                android.os.IBinder r6 = r6.readStrongBinder()
                com.google.android.setupcompat.portal.IPortalRegisterResultListener r6 = com.google.android.setupcompat.portal.IPortalRegisterResultListener.Stub.asInterface(r6)
                r4.registerProgressService(r5, r0, r6)
                r7.writeNoException()
                return r1
            L_0x008c:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x009e
                android.os.Parcelable$Creator<com.google.android.setupcompat.portal.NotificationComponent> r5 = com.google.android.setupcompat.portal.NotificationComponent.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                com.google.android.setupcompat.portal.NotificationComponent r0 = (com.google.android.setupcompat.portal.NotificationComponent) r0
            L_0x009e:
                r4.unregisterNotification(r0)
                r7.writeNoException()
                return r1
            L_0x00a5:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x00b7
                android.os.Parcelable$Creator<com.google.android.setupcompat.portal.NotificationComponent> r5 = com.google.android.setupcompat.portal.NotificationComponent.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                com.google.android.setupcompat.portal.NotificationComponent r0 = (com.google.android.setupcompat.portal.NotificationComponent) r0
            L_0x00b7:
                boolean r4 = r4.registerNotification(r0)
                r7.writeNoException()
                r7.writeInt(r4)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.portal.ISetupNotificationService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        private static class Proxy implements ISetupNotificationService {
            public static ISetupNotificationService sDefaultImpl;
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

            public boolean registerNotification(NotificationComponent notificationComponent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    if (notificationComponent != null) {
                        obtain.writeInt(1);
                        notificationComponent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerNotification(notificationComponent);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterNotification(NotificationComponent notificationComponent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (notificationComponent != null) {
                        obtain.writeInt(1);
                        notificationComponent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterNotification(notificationComponent);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerProgressService(ProgressServiceComponent progressServiceComponent, UserHandle userHandle, IPortalRegisterResultListener iPortalRegisterResultListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (progressServiceComponent != null) {
                        obtain.writeInt(1);
                        progressServiceComponent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (userHandle != null) {
                        obtain.writeInt(1);
                        userHandle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPortalRegisterResultListener != null ? iPortalRegisterResultListener.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerProgressService(progressServiceComponent, userHandle, iPortalRegisterResultListener);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isProgressServiceAlive(ProgressServiceComponent progressServiceComponent, UserHandle userHandle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    if (progressServiceComponent != null) {
                        obtain.writeInt(1);
                        progressServiceComponent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (userHandle != null) {
                        obtain.writeInt(1);
                        userHandle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isProgressServiceAlive(progressServiceComponent, userHandle);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isPortalAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPortalAvailable();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISetupNotificationService iSetupNotificationService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSetupNotificationService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSetupNotificationService;
                return true;
            }
        }

        public static ISetupNotificationService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
