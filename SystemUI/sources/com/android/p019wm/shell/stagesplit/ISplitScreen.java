package com.android.p019wm.shell.stagesplit;

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

/* renamed from: com.android.wm.shell.stagesplit.ISplitScreen */
public interface ISplitScreen extends IInterface {

    /* renamed from: com.android.wm.shell.stagesplit.ISplitScreen$Default */
    public static class Default implements ISplitScreen {
        public IBinder asBinder() {
            return null;
        }

        public void exitSplitScreen(int i) throws RemoteException {
        }

        public void exitSplitScreenOnHide(boolean z) throws RemoteException {
        }

        public RemoteAnimationTarget[] onGoingToRecentsLegacy(boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
            return null;
        }

        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
        }

        public void removeFromSideStage(int i) throws RemoteException {
        }

        public void setSideStageVisibility(boolean z) throws RemoteException {
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) throws RemoteException {
        }

        public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) throws RemoteException {
        }

        public void startTask(int i, int i2, int i3, Bundle bundle) throws RemoteException {
        }

        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) throws RemoteException {
        }

        public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
        }

        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException {
        }
    }

    void exitSplitScreen(int i) throws RemoteException;

    void exitSplitScreenOnHide(boolean z) throws RemoteException;

    RemoteAnimationTarget[] onGoingToRecentsLegacy(boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    void removeFromSideStage(int i) throws RemoteException;

    void setSideStageVisibility(boolean z) throws RemoteException;

    void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) throws RemoteException;

    void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startTask(int i, int i2, int i3, Bundle bundle) throws RemoteException;

    void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) throws RemoteException;

    void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    /* renamed from: com.android.wm.shell.stagesplit.ISplitScreen$Stub */
    public static abstract class Stub extends Binder implements ISplitScreen {
        private static final String DESCRIPTOR = "com.android.wm.shell.stagesplit.ISplitScreen";
        static final int TRANSACTION_exitSplitScreen = 6;
        static final int TRANSACTION_exitSplitScreenOnHide = 7;
        static final int TRANSACTION_onGoingToRecentsLegacy = 13;
        static final int TRANSACTION_registerSplitScreenListener = 2;
        static final int TRANSACTION_removeFromSideStage = 5;
        static final int TRANSACTION_setSideStageVisibility = 4;
        static final int TRANSACTION_startIntent = 10;
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

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: android.window.RemoteTransition} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: android.window.RemoteTransition} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r11, android.os.Parcel r12, android.os.Parcel r13, int r14) throws android.os.RemoteException {
            /*
                r10 = this;
                r4 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r7 = 1
                java.lang.String r5 = "com.android.wm.shell.stagesplit.ISplitScreen"
                if (r11 == r4) goto L_0x01ad
                r4 = 0
                r6 = 0
                switch(r11) {
                    case 2: goto L_0x019e;
                    case 3: goto L_0x018f;
                    case 4: goto L_0x0181;
                    case 5: goto L_0x0176;
                    case 6: goto L_0x016b;
                    case 7: goto L_0x015d;
                    case 8: goto L_0x013b;
                    case 9: goto L_0x0100;
                    case 10: goto L_0x00bd;
                    case 11: goto L_0x0076;
                    case 12: goto L_0x002f;
                    case 13: goto L_0x0012;
                    default: goto L_0x000d;
                }
            L_0x000d:
                boolean r0 = super.onTransact(r11, r12, r13, r14)
                return r0
            L_0x0012:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                if (r1 == 0) goto L_0x001c
                r4 = r7
            L_0x001c:
                android.os.Parcelable$Creator r1 = android.view.RemoteAnimationTarget.CREATOR
                java.lang.Object[] r1 = r12.createTypedArray(r1)
                android.view.RemoteAnimationTarget[] r1 = (android.view.RemoteAnimationTarget[]) r1
                android.view.RemoteAnimationTarget[] r0 = r10.onGoingToRecentsLegacy(r4, r1)
                r13.writeNoException()
                r13.writeTypedArray(r0, r7)
                return r7
            L_0x002f:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                int r3 = r12.readInt()
                if (r3 == 0) goto L_0x0045
                android.os.Parcelable$Creator r3 = android.os.Bundle.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r12)
                android.os.Bundle r3 = (android.os.Bundle) r3
                goto L_0x0046
            L_0x0045:
                r3 = r6
            L_0x0046:
                int r4 = r12.readInt()
                int r5 = r12.readInt()
                if (r5 == 0) goto L_0x0059
                android.os.Parcelable$Creator r5 = android.os.Bundle.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r12)
                android.os.Bundle r5 = (android.os.Bundle) r5
                goto L_0x005a
            L_0x0059:
                r5 = r6
            L_0x005a:
                int r8 = r12.readInt()
                int r9 = r12.readInt()
                if (r9 == 0) goto L_0x006d
                android.os.Parcelable$Creator r6 = android.view.RemoteAnimationAdapter.CREATOR
                java.lang.Object r2 = r6.createFromParcel(r12)
                android.view.RemoteAnimationAdapter r2 = (android.view.RemoteAnimationAdapter) r2
                r6 = r2
            L_0x006d:
                r0 = r10
                r2 = r3
                r3 = r4
                r4 = r5
                r5 = r8
                r0.startTasksWithLegacyTransition(r1, r2, r3, r4, r5, r6)
                return r7
            L_0x0076:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                int r3 = r12.readInt()
                if (r3 == 0) goto L_0x008c
                android.os.Parcelable$Creator r3 = android.os.Bundle.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r12)
                android.os.Bundle r3 = (android.os.Bundle) r3
                goto L_0x008d
            L_0x008c:
                r3 = r6
            L_0x008d:
                int r4 = r12.readInt()
                int r5 = r12.readInt()
                if (r5 == 0) goto L_0x00a0
                android.os.Parcelable$Creator r5 = android.os.Bundle.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r12)
                android.os.Bundle r5 = (android.os.Bundle) r5
                goto L_0x00a1
            L_0x00a0:
                r5 = r6
            L_0x00a1:
                int r8 = r12.readInt()
                int r9 = r12.readInt()
                if (r9 == 0) goto L_0x00b4
                android.os.Parcelable$Creator r6 = android.window.RemoteTransition.CREATOR
                java.lang.Object r2 = r6.createFromParcel(r12)
                android.window.RemoteTransition r2 = (android.window.RemoteTransition) r2
                r6 = r2
            L_0x00b4:
                r0 = r10
                r2 = r3
                r3 = r4
                r4 = r5
                r5 = r8
                r0.startTasks(r1, r2, r3, r4, r5, r6)
                return r7
            L_0x00bd:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                if (r1 == 0) goto L_0x00cf
                android.os.Parcelable$Creator r1 = android.app.PendingIntent.CREATOR
                java.lang.Object r1 = r1.createFromParcel(r12)
                android.app.PendingIntent r1 = (android.app.PendingIntent) r1
                goto L_0x00d0
            L_0x00cf:
                r1 = r6
            L_0x00d0:
                int r3 = r12.readInt()
                if (r3 == 0) goto L_0x00df
                android.os.Parcelable$Creator r3 = android.content.Intent.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r12)
                android.content.Intent r3 = (android.content.Intent) r3
                goto L_0x00e0
            L_0x00df:
                r3 = r6
            L_0x00e0:
                int r4 = r12.readInt()
                int r5 = r12.readInt()
                int r8 = r12.readInt()
                if (r8 == 0) goto L_0x00f7
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r2 = r6.createFromParcel(r12)
                android.os.Bundle r2 = (android.os.Bundle) r2
                r6 = r2
            L_0x00f7:
                r0 = r10
                r2 = r3
                r3 = r4
                r4 = r5
                r5 = r6
                r0.startIntent(r1, r2, r3, r4, r5)
                return r7
            L_0x0100:
                r12.enforceInterface(r5)
                java.lang.String r1 = r12.readString()
                java.lang.String r3 = r12.readString()
                int r4 = r12.readInt()
                int r5 = r12.readInt()
                int r8 = r12.readInt()
                if (r8 == 0) goto L_0x0122
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r8 = r8.createFromParcel(r12)
                android.os.Bundle r8 = (android.os.Bundle) r8
                goto L_0x0123
            L_0x0122:
                r8 = r6
            L_0x0123:
                int r9 = r12.readInt()
                if (r9 == 0) goto L_0x0132
                android.os.Parcelable$Creator r6 = android.os.UserHandle.CREATOR
                java.lang.Object r2 = r6.createFromParcel(r12)
                android.os.UserHandle r2 = (android.os.UserHandle) r2
                r6 = r2
            L_0x0132:
                r0 = r10
                r2 = r3
                r3 = r4
                r4 = r5
                r5 = r8
                r0.startShortcut(r1, r2, r3, r4, r5, r6)
                return r7
            L_0x013b:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                int r3 = r12.readInt()
                int r4 = r12.readInt()
                int r5 = r12.readInt()
                if (r5 == 0) goto L_0x0159
                android.os.Parcelable$Creator r5 = android.os.Bundle.CREATOR
                java.lang.Object r2 = r5.createFromParcel(r12)
                r6 = r2
                android.os.Bundle r6 = (android.os.Bundle) r6
            L_0x0159:
                r10.startTask(r1, r3, r4, r6)
                return r7
            L_0x015d:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                if (r1 == 0) goto L_0x0167
                r4 = r7
            L_0x0167:
                r10.exitSplitScreenOnHide(r4)
                return r7
            L_0x016b:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                r10.exitSplitScreen(r1)
                return r7
            L_0x0176:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                r10.removeFromSideStage(r1)
                return r7
            L_0x0181:
                r12.enforceInterface(r5)
                int r1 = r12.readInt()
                if (r1 == 0) goto L_0x018b
                r4 = r7
            L_0x018b:
                r10.setSideStageVisibility(r4)
                return r7
            L_0x018f:
                r12.enforceInterface(r5)
                android.os.IBinder r1 = r12.readStrongBinder()
                com.android.wm.shell.stagesplit.ISplitScreenListener r1 = com.android.p019wm.shell.stagesplit.ISplitScreenListener.Stub.asInterface(r1)
                r10.unregisterSplitScreenListener(r1)
                return r7
            L_0x019e:
                r12.enforceInterface(r5)
                android.os.IBinder r1 = r12.readStrongBinder()
                com.android.wm.shell.stagesplit.ISplitScreenListener r1 = com.android.p019wm.shell.stagesplit.ISplitScreenListener.Stub.asInterface(r1)
                r10.registerSplitScreenListener(r1)
                return r7
            L_0x01ad:
                r13.writeString(r5)
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.stagesplit.ISplitScreen.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* renamed from: com.android.wm.shell.stagesplit.ISplitScreen$Stub$Proxy */
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

            public void setSideStageVisibility(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(4, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().setSideStageVisibility(z);
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

            public void startTask(int i, int i2, int i3, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(8, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTask(i, i2, i3, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
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
                        Stub.getDefaultImpl().startShortcut(str, str2, i, i2, bundle, userHandle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) throws RemoteException {
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
                    obtain.writeInt(i2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(10, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startIntent(pendingIntent, intent, i, i2, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) throws RemoteException {
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
                    obtain.writeInt(i2);
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i3);
                    if (remoteTransition != null) {
                        obtain.writeInt(1);
                        remoteTransition.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(11, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTasks(i, bundle, i2, bundle2, i3, remoteTransition);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException {
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
                    obtain.writeInt(i2);
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i3);
                    if (remoteAnimationAdapter != null) {
                        obtain.writeInt(1);
                        remoteAnimationAdapter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(12, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startTasksWithLegacyTransition(i, bundle, i2, bundle2, i3, remoteAnimationAdapter);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public RemoteAnimationTarget[] onGoingToRecentsLegacy(boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeTypedArray(remoteAnimationTargetArr, 0);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onGoingToRecentsLegacy(z, remoteAnimationTargetArr);
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
