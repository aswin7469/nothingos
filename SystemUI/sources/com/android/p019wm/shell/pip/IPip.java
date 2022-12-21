package com.android.p019wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.pip.IPip */
public interface IPip extends IInterface {

    /* renamed from: com.android.wm.shell.pip.IPip$Default */
    public static class Default implements IPip {
        public IBinder asBinder() {
            return null;
        }

        public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) throws RemoteException {
        }

        public void setShelfHeight(boolean z, int i) throws RemoteException {
        }

        public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) throws RemoteException {
            return null;
        }

        public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) throws RemoteException {
        }
    }

    void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) throws RemoteException;

    void setShelfHeight(boolean z, int i) throws RemoteException;

    Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) throws RemoteException;

    void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) throws RemoteException;

    /* renamed from: com.android.wm.shell.pip.IPip$Stub */
    public static abstract class Stub extends Binder implements IPip {
        private static final String DESCRIPTOR = "com.android.wm.shell.pip.IPip";
        static final int TRANSACTION_setPinnedStackAnimationListener = 4;
        static final int TRANSACTION_setShelfHeight = 5;
        static final int TRANSACTION_startSwipePipToHome = 2;
        static final int TRANSACTION_stopSwipePipToHome = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPip asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPip)) {
                return new Proxy(iBinder);
            }
            return (IPip) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: android.view.SurfaceControl} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.view.SurfaceControl} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: android.app.PictureInPictureParams} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: android.view.SurfaceControl} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v1, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: android.view.SurfaceControl} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: android.view.SurfaceControl} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: android.view.SurfaceControl} */
        /* JADX WARNING: type inference failed for: r2v3, types: [android.app.PictureInPictureParams] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r11, android.os.Parcel r12, android.os.Parcel r13, int r14) throws android.os.RemoteException {
            /*
                r10 = this;
                r0 = 2
                r1 = 0
                r2 = 0
                r3 = 1
                java.lang.String r4 = "com.android.wm.shell.pip.IPip"
                if (r11 == r0) goto L_0x007a
                r0 = 3
                if (r11 == r0) goto L_0x0040
                r0 = 4
                if (r11 == r0) goto L_0x0031
                r0 = 5
                if (r11 == r0) goto L_0x001f
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r11 == r0) goto L_0x001b
                boolean r10 = super.onTransact(r11, r12, r13, r14)
                return r10
            L_0x001b:
                r13.writeString(r4)
                return r3
            L_0x001f:
                r12.enforceInterface(r4)
                int r11 = r12.readInt()
                if (r11 == 0) goto L_0x0029
                r1 = r3
            L_0x0029:
                int r11 = r12.readInt()
                r10.setShelfHeight(r1, r11)
                return r3
            L_0x0031:
                r12.enforceInterface(r4)
                android.os.IBinder r11 = r12.readStrongBinder()
                com.android.wm.shell.pip.IPipAnimationListener r11 = com.android.p019wm.shell.pip.IPipAnimationListener.Stub.asInterface(r11)
                r10.setPinnedStackAnimationListener(r11)
                return r3
            L_0x0040:
                r12.enforceInterface(r4)
                int r11 = r12.readInt()
                int r13 = r12.readInt()
                if (r13 == 0) goto L_0x0056
                android.os.Parcelable$Creator r13 = android.content.ComponentName.CREATOR
                java.lang.Object r13 = r13.createFromParcel(r12)
                android.content.ComponentName r13 = (android.content.ComponentName) r13
                goto L_0x0057
            L_0x0056:
                r13 = r2
            L_0x0057:
                int r14 = r12.readInt()
                if (r14 == 0) goto L_0x0066
                android.os.Parcelable$Creator r14 = android.graphics.Rect.CREATOR
                java.lang.Object r14 = r14.createFromParcel(r12)
                android.graphics.Rect r14 = (android.graphics.Rect) r14
                goto L_0x0067
            L_0x0066:
                r14 = r2
            L_0x0067:
                int r0 = r12.readInt()
                if (r0 == 0) goto L_0x0076
                android.os.Parcelable$Creator r0 = android.view.SurfaceControl.CREATOR
                java.lang.Object r12 = r0.createFromParcel(r12)
                r2 = r12
                android.view.SurfaceControl r2 = (android.view.SurfaceControl) r2
            L_0x0076:
                r10.stopSwipePipToHome(r11, r13, r14, r2)
                return r3
            L_0x007a:
                r12.enforceInterface(r4)
                int r11 = r12.readInt()
                if (r11 == 0) goto L_0x008d
                android.os.Parcelable$Creator r11 = android.content.ComponentName.CREATOR
                java.lang.Object r11 = r11.createFromParcel(r12)
                android.content.ComponentName r11 = (android.content.ComponentName) r11
                r5 = r11
                goto L_0x008e
            L_0x008d:
                r5 = r2
            L_0x008e:
                int r11 = r12.readInt()
                if (r11 == 0) goto L_0x009e
                android.os.Parcelable$Creator r11 = android.content.pm.ActivityInfo.CREATOR
                java.lang.Object r11 = r11.createFromParcel(r12)
                android.content.pm.ActivityInfo r11 = (android.content.pm.ActivityInfo) r11
                r6 = r11
                goto L_0x009f
            L_0x009e:
                r6 = r2
            L_0x009f:
                int r11 = r12.readInt()
                if (r11 == 0) goto L_0x00ae
                android.os.Parcelable$Creator r11 = android.app.PictureInPictureParams.CREATOR
                java.lang.Object r11 = r11.createFromParcel(r12)
                r2 = r11
                android.app.PictureInPictureParams r2 = (android.app.PictureInPictureParams) r2
            L_0x00ae:
                r7 = r2
                int r8 = r12.readInt()
                int r9 = r12.readInt()
                r4 = r10
                android.graphics.Rect r10 = r4.startSwipePipToHome(r5, r6, r7, r8, r9)
                r13.writeNoException()
                if (r10 == 0) goto L_0x00c8
                r13.writeInt(r3)
                r10.writeToParcel(r13, r3)
                goto L_0x00cb
            L_0x00c8:
                r13.writeInt(r1)
            L_0x00cb:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.pip.IPip.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* renamed from: com.android.wm.shell.pip.IPip$Stub$Proxy */
        private static class Proxy implements IPip {
            public static IPip sDefaultImpl;
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

            public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (activityInfo != null) {
                        obtain.writeInt(1);
                        activityInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pictureInPictureParams != null) {
                        obtain.writeInt(1);
                        pictureInPictureParams.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startSwipePipToHome(componentName, activityInfo, pictureInPictureParams, i, i2);
                    }
                    obtain2.readException();
                    Rect rect = obtain2.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return rect;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (surfaceControl != null) {
                        obtain.writeInt(1);
                        surfaceControl.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().stopSwipePipToHome(i, componentName, rect, surfaceControl);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iPipAnimationListener != null ? iPipAnimationListener.asBinder() : null);
                    if (this.mRemote.transact(4, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().setPinnedStackAnimationListener(iPipAnimationListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void setShelfHeight(boolean z, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(5, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().setShelfHeight(z, i);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPip iPip) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iPip == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iPip;
                return true;
            }
        }

        public static IPip getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
