package com.android.p019wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.window.RemoteTransition;

/* renamed from: com.android.wm.shell.splitscreen.ISplitScreen */
public interface ISplitScreen extends IInterface {

    /* renamed from: com.android.wm.shell.splitscreen.ISplitScreen$Default */
    public static class Default implements ISplitScreen {
        public IBinder asBinder() {
            return null;
        }

        public void exitSplitScreen(int i) throws RemoteException {
        }

        public void exitSplitScreenOnHide(boolean z) throws RemoteException {
        }

        public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
            return null;
        }

        public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
            return null;
        }

        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
        }

        public void removeFromSideStage(int i) throws RemoteException {
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) throws RemoteException {
        }

        public void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
        }

        public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) throws RemoteException {
        }

        public void startTask(int i, int i2, Bundle bundle) throws RemoteException {
        }

        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) throws RemoteException {
        }

        public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
        }

        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
        }
    }

    void exitSplitScreen(int i) throws RemoteException;

    void exitSplitScreenOnHide(boolean z) throws RemoteException;

    RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    void removeFromSideStage(int i) throws RemoteException;

    void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) throws RemoteException;

    void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startTask(int i, int i2, Bundle bundle) throws RemoteException;

    void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) throws RemoteException;

    void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    /* renamed from: com.android.wm.shell.splitscreen.ISplitScreen$Stub */
    public static abstract class Stub extends Binder implements ISplitScreen {
        private static final String DESCRIPTOR = "com.android.wm.shell.splitscreen.ISplitScreen";
        static final int TRANSACTION_exitSplitScreen = 6;
        static final int TRANSACTION_exitSplitScreenOnHide = 7;
        static final int TRANSACTION_onGoingToRecentsLegacy = 14;
        static final int TRANSACTION_onStartingSplitLegacy = 15;
        static final int TRANSACTION_registerSplitScreenListener = 2;
        static final int TRANSACTION_removeFromSideStage = 5;
        static final int TRANSACTION_startIntent = 10;
        static final int TRANSACTION_startIntentAndTaskWithLegacyTransition = 13;
        static final int TRANSACTION_startShortcut = 9;
        static final int TRANSACTION_startTask = 8;
        static final int TRANSACTION_startTasks = 11;
        static final int TRANSACTION_startTasksWithLegacyTransition = 12;
        static final int TRANSACTION_unregisterSplitScreenListener = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISplitScreen asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitScreen)) {
                return new Proxy(iBinder);
            }
            return (ISplitScreen) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: android.os.Bundle} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r13, android.os.Parcel r14, android.os.Parcel r15, int r16) throws android.os.RemoteException {
            /*
                r12 = this;
                r0 = r12
                r1 = r13
                r2 = r14
                r3 = r15
                r4 = 2
                r9 = 1
                java.lang.String r5 = "com.android.wm.shell.splitscreen.ISplitScreen"
                if (r1 == r4) goto L_0x021b
                r4 = 3
                if (r1 == r4) goto L_0x020c
                r4 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r1 == r4) goto L_0x0208
                r4 = 0
                switch(r1) {
                    case 5: goto L_0x01fd;
                    case 6: goto L_0x01f2;
                    case 7: goto L_0x01e2;
                    case 8: goto L_0x01c4;
                    case 9: goto L_0x018b;
                    case 10: goto L_0x0151;
                    case 11: goto L_0x0102;
                    case 12: goto L_0x00b3;
                    case 13: goto L_0x0047;
                    case 14: goto L_0x0031;
                    case 15: goto L_0x001b;
                    default: goto L_0x0016;
                }
            L_0x0016:
                boolean r0 = super.onTransact(r13, r14, r15, r16)
                return r0
            L_0x001b:
                r14.enforceInterface(r5)
                android.os.Parcelable$Creator r1 = android.view.RemoteAnimationTarget.CREATOR
                java.lang.Object[] r1 = r14.createTypedArray(r1)
                android.view.RemoteAnimationTarget[] r1 = (android.view.RemoteAnimationTarget[]) r1
                android.view.RemoteAnimationTarget[] r0 = r12.onStartingSplitLegacy(r1)
                r15.writeNoException()
                r15.writeTypedArray(r0, r9)
                return r9
            L_0x0031:
                r14.enforceInterface(r5)
                android.os.Parcelable$Creator r1 = android.view.RemoteAnimationTarget.CREATOR
                java.lang.Object[] r1 = r14.createTypedArray(r1)
                android.view.RemoteAnimationTarget[] r1 = (android.view.RemoteAnimationTarget[]) r1
                android.view.RemoteAnimationTarget[] r0 = r12.onGoingToRecentsLegacy(r1)
                r15.writeNoException()
                r15.writeTypedArray(r0, r9)
                return r9
            L_0x0047:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                if (r1 == 0) goto L_0x0059
                android.os.Parcelable$Creator r1 = android.app.PendingIntent.CREATOR
                java.lang.Object r1 = r1.createFromParcel(r14)
                android.app.PendingIntent r1 = (android.app.PendingIntent) r1
                goto L_0x005a
            L_0x0059:
                r1 = r4
            L_0x005a:
                int r3 = r14.readInt()
                if (r3 == 0) goto L_0x0069
                android.os.Parcelable$Creator r3 = android.content.Intent.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r14)
                android.content.Intent r3 = (android.content.Intent) r3
                goto L_0x006a
            L_0x0069:
                r3 = r4
            L_0x006a:
                int r5 = r14.readInt()
                int r6 = r14.readInt()
                if (r6 == 0) goto L_0x007d
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r14)
                android.os.Bundle r6 = (android.os.Bundle) r6
                goto L_0x007e
            L_0x007d:
                r6 = r4
            L_0x007e:
                int r7 = r14.readInt()
                if (r7 == 0) goto L_0x008d
                android.os.Parcelable$Creator r7 = android.os.Bundle.CREATOR
                java.lang.Object r7 = r7.createFromParcel(r14)
                android.os.Bundle r7 = (android.os.Bundle) r7
                goto L_0x008e
            L_0x008d:
                r7 = r4
            L_0x008e:
                int r8 = r14.readInt()
                float r10 = r14.readFloat()
                int r11 = r14.readInt()
                if (r11 == 0) goto L_0x00a6
                android.os.Parcelable$Creator r4 = android.view.RemoteAnimationAdapter.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                android.view.RemoteAnimationAdapter r2 = (android.view.RemoteAnimationAdapter) r2
                r11 = r2
                goto L_0x00a7
            L_0x00a6:
                r11 = r4
            L_0x00a7:
                r0 = r12
                r2 = r3
                r3 = r5
                r4 = r6
                r5 = r7
                r6 = r8
                r7 = r10
                r8 = r11
                r0.startIntentAndTaskWithLegacyTransition(r1, r2, r3, r4, r5, r6, r7, r8)
                return r9
            L_0x00b3:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                int r3 = r14.readInt()
                if (r3 == 0) goto L_0x00c9
                android.os.Parcelable$Creator r3 = android.os.Bundle.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r14)
                android.os.Bundle r3 = (android.os.Bundle) r3
                goto L_0x00ca
            L_0x00c9:
                r3 = r4
            L_0x00ca:
                int r5 = r14.readInt()
                int r6 = r14.readInt()
                if (r6 == 0) goto L_0x00dd
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r14)
                android.os.Bundle r6 = (android.os.Bundle) r6
                goto L_0x00de
            L_0x00dd:
                r6 = r4
            L_0x00de:
                int r7 = r14.readInt()
                float r8 = r14.readFloat()
                int r10 = r14.readInt()
                if (r10 == 0) goto L_0x00f6
                android.os.Parcelable$Creator r4 = android.view.RemoteAnimationAdapter.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                android.view.RemoteAnimationAdapter r2 = (android.view.RemoteAnimationAdapter) r2
                r10 = r2
                goto L_0x00f7
            L_0x00f6:
                r10 = r4
            L_0x00f7:
                r0 = r12
                r2 = r3
                r3 = r5
                r4 = r6
                r5 = r7
                r6 = r8
                r7 = r10
                r0.startTasksWithLegacyTransition(r1, r2, r3, r4, r5, r6, r7)
                return r9
            L_0x0102:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                int r3 = r14.readInt()
                if (r3 == 0) goto L_0x0118
                android.os.Parcelable$Creator r3 = android.os.Bundle.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r14)
                android.os.Bundle r3 = (android.os.Bundle) r3
                goto L_0x0119
            L_0x0118:
                r3 = r4
            L_0x0119:
                int r5 = r14.readInt()
                int r6 = r14.readInt()
                if (r6 == 0) goto L_0x012c
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r14)
                android.os.Bundle r6 = (android.os.Bundle) r6
                goto L_0x012d
            L_0x012c:
                r6 = r4
            L_0x012d:
                int r7 = r14.readInt()
                float r8 = r14.readFloat()
                int r10 = r14.readInt()
                if (r10 == 0) goto L_0x0145
                android.os.Parcelable$Creator r4 = android.window.RemoteTransition.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                android.window.RemoteTransition r2 = (android.window.RemoteTransition) r2
                r10 = r2
                goto L_0x0146
            L_0x0145:
                r10 = r4
            L_0x0146:
                r0 = r12
                r2 = r3
                r3 = r5
                r4 = r6
                r5 = r7
                r6 = r8
                r7 = r10
                r0.startTasks(r1, r2, r3, r4, r5, r6, r7)
                return r9
            L_0x0151:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                if (r1 == 0) goto L_0x0163
                android.os.Parcelable$Creator r1 = android.app.PendingIntent.CREATOR
                java.lang.Object r1 = r1.createFromParcel(r14)
                android.app.PendingIntent r1 = (android.app.PendingIntent) r1
                goto L_0x0164
            L_0x0163:
                r1 = r4
            L_0x0164:
                int r3 = r14.readInt()
                if (r3 == 0) goto L_0x0173
                android.os.Parcelable$Creator r3 = android.content.Intent.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r14)
                android.content.Intent r3 = (android.content.Intent) r3
                goto L_0x0174
            L_0x0173:
                r3 = r4
            L_0x0174:
                int r5 = r14.readInt()
                int r6 = r14.readInt()
                if (r6 == 0) goto L_0x0187
                android.os.Parcelable$Creator r4 = android.os.Bundle.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                r4 = r2
                android.os.Bundle r4 = (android.os.Bundle) r4
            L_0x0187:
                r12.startIntent(r1, r3, r5, r4)
                return r9
            L_0x018b:
                r14.enforceInterface(r5)
                java.lang.String r1 = r14.readString()
                java.lang.String r3 = r14.readString()
                int r5 = r14.readInt()
                int r6 = r14.readInt()
                if (r6 == 0) goto L_0x01a9
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r14)
                android.os.Bundle r6 = (android.os.Bundle) r6
                goto L_0x01aa
            L_0x01a9:
                r6 = r4
            L_0x01aa:
                int r7 = r14.readInt()
                if (r7 == 0) goto L_0x01ba
                android.os.Parcelable$Creator r4 = android.os.UserHandle.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                android.os.UserHandle r2 = (android.os.UserHandle) r2
                r7 = r2
                goto L_0x01bb
            L_0x01ba:
                r7 = r4
            L_0x01bb:
                r0 = r12
                r2 = r3
                r3 = r5
                r4 = r6
                r5 = r7
                r0.startShortcut(r1, r2, r3, r4, r5)
                return r9
            L_0x01c4:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                int r3 = r14.readInt()
                int r5 = r14.readInt()
                if (r5 == 0) goto L_0x01de
                android.os.Parcelable$Creator r4 = android.os.Bundle.CREATOR
                java.lang.Object r2 = r4.createFromParcel(r14)
                r4 = r2
                android.os.Bundle r4 = (android.os.Bundle) r4
            L_0x01de:
                r12.startTask(r1, r3, r4)
                return r9
            L_0x01e2:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                if (r1 == 0) goto L_0x01ed
                r1 = r9
                goto L_0x01ee
            L_0x01ed:
                r1 = 0
            L_0x01ee:
                r12.exitSplitScreenOnHide(r1)
                return r9
            L_0x01f2:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                r12.exitSplitScreen(r1)
                return r9
            L_0x01fd:
                r14.enforceInterface(r5)
                int r1 = r14.readInt()
                r12.removeFromSideStage(r1)
                return r9
            L_0x0208:
                r15.writeString(r5)
                return r9
            L_0x020c:
                r14.enforceInterface(r5)
                android.os.IBinder r1 = r14.readStrongBinder()
                com.android.wm.shell.splitscreen.ISplitScreenListener r1 = com.android.p019wm.shell.splitscreen.ISplitScreenListener.Stub.asInterface(r1)
                r12.unregisterSplitScreenListener(r1)
                return r9
            L_0x021b:
                r14.enforceInterface(r5)
                android.os.IBinder r1 = r14.readStrongBinder()
                com.android.wm.shell.splitscreen.ISplitScreenListener r1 = com.android.p019wm.shell.splitscreen.ISplitScreenListener.Stub.asInterface(r1)
                r12.registerSplitScreenListener(r1)
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.splitscreen.ISplitScreen.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* renamed from: com.android.wm.shell.splitscreen.ISplitScreen$Stub$Proxy */
        private static class Proxy implements ISplitScreen {
            public static ISplitScreen sDefaultImpl;
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

            public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSplitScreenListener != null ? iSplitScreenListener.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().registerSplitScreenListener(iSplitScreenListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSplitScreenListener != null ? iSplitScreenListener.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterSplitScreenListener(iSplitScreenListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void removeFromSideStage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(5, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().removeFromSideStage(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void exitSplitScreen(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(6, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().exitSplitScreen(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void exitSplitScreenOnHide(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(7, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().exitSplitScreenOnHide(z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startTask(int i, int i2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(8, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTask(i, i2, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (userHandle != null) {
                        obtain.writeInt(1);
                        userHandle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(9, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startShortcut(str, str2, i, bundle, userHandle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(10, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startIntent(pendingIntent, intent, i, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) throws RemoteException {
                Bundle bundle3 = bundle;
                Bundle bundle4 = bundle2;
                RemoteTransition remoteTransition2 = remoteTransition;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i4 = i;
                    obtain.writeInt(i);
                    if (bundle3 != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    int i5 = i2;
                    obtain.writeInt(i2);
                    if (bundle4 != null) {
                        obtain.writeInt(1);
                        bundle4.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i3);
                    obtain.writeFloat(f);
                    if (remoteTransition2 != null) {
                        obtain.writeInt(1);
                        remoteTransition2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(11, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTasks(i, bundle, i2, bundle2, i3, f, remoteTransition);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
                Bundle bundle3 = bundle;
                Bundle bundle4 = bundle2;
                RemoteAnimationAdapter remoteAnimationAdapter2 = remoteAnimationAdapter;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i4 = i;
                    obtain.writeInt(i);
                    if (bundle3 != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    int i5 = i2;
                    obtain.writeInt(i2);
                    if (bundle4 != null) {
                        obtain.writeInt(1);
                        bundle4.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i3);
                    obtain.writeFloat(f);
                    if (remoteAnimationAdapter2 != null) {
                        obtain.writeInt(1);
                        remoteAnimationAdapter2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(12, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTasksWithLegacyTransition(i, bundle, i2, bundle2, i3, f, remoteAnimationAdapter);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
                PendingIntent pendingIntent2 = pendingIntent;
                Intent intent2 = intent;
                Bundle bundle3 = bundle;
                Bundle bundle4 = bundle2;
                RemoteAnimationAdapter remoteAnimationAdapter2 = remoteAnimationAdapter;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pendingIntent2 != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (intent2 != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    if (bundle3 != null) {
                        obtain.writeInt(1);
                        bundle3.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle4 != null) {
                        obtain.writeInt(1);
                        bundle4.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i2);
                    obtain.writeFloat(f);
                    if (remoteAnimationAdapter2 != null) {
                        obtain.writeInt(1);
                        remoteAnimationAdapter2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(13, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startIntentAndTaskWithLegacyTransition(pendingIntent, intent, i, bundle, bundle2, i2, f, remoteAnimationAdapter);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedArray(remoteAnimationTargetArr, 0);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onGoingToRecentsLegacy(remoteAnimationTargetArr);
                    }
                    obtain2.readException();
                    RemoteAnimationTarget[] remoteAnimationTargetArr2 = (RemoteAnimationTarget[]) obtain2.createTypedArray(RemoteAnimationTarget.CREATOR);
                    obtain2.recycle();
                    obtain.recycle();
                    return remoteAnimationTargetArr2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedArray(remoteAnimationTargetArr, 0);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onStartingSplitLegacy(remoteAnimationTargetArr);
                    }
                    obtain2.readException();
                    RemoteAnimationTarget[] remoteAnimationTargetArr2 = (RemoteAnimationTarget[]) obtain2.createTypedArray(RemoteAnimationTarget.CREATOR);
                    obtain2.recycle();
                    obtain.recycle();
                    return remoteAnimationTargetArr2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISplitScreen iSplitScreen) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSplitScreen == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSplitScreen;
                return true;
            }
        }

        public static ISplitScreen getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
