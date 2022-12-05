package nothing.view;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IWindowModeCallback extends IInterface {
    public static final String DESCRIPTOR = "nothing.view.IWindowModeCallback";

    void onPackageNameChanged(String str, int i, int i2, boolean z) throws RemoteException;

    void onStackWindowModeChanged(boolean z, Bundle bundle) throws RemoteException;

    void onWindowModeBoundChanged(Rect rect, int i) throws RemoteException;

    void onWindowModeFlingToTarget(Rect rect, Rect rect2) throws RemoteException;

    void onWindowModeStateChanged(int i) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IWindowModeCallback {
        @Override // nothing.view.IWindowModeCallback
        public void onStackWindowModeChanged(boolean isNtWindowMode, Bundle data) throws RemoteException {
        }

        @Override // nothing.view.IWindowModeCallback
        public void onPackageNameChanged(String packageName, int uid, int pid, boolean topStack) throws RemoteException {
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeBoundChanged(Rect displayBound, int displayRotation) throws RemoteException {
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeStateChanged(int windowModeState) throws RemoteException {
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeFlingToTarget(Rect fromBound, Rect toBound) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IWindowModeCallback {
        static final int TRANSACTION_onPackageNameChanged = 2;
        static final int TRANSACTION_onStackWindowModeChanged = 1;
        static final int TRANSACTION_onWindowModeBoundChanged = 3;
        static final int TRANSACTION_onWindowModeFlingToTarget = 5;
        static final int TRANSACTION_onWindowModeStateChanged = 4;

        public Stub() {
            attachInterface(this, IWindowModeCallback.DESCRIPTOR);
        }

        public static IWindowModeCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWindowModeCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IWindowModeCallback)) {
                return (IWindowModeCallback) iin;
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
                    return "onStackWindowModeChanged";
                case 2:
                    return "onPackageNameChanged";
                case 3:
                    return "onWindowModeBoundChanged";
                case 4:
                    return "onWindowModeStateChanged";
                case 5:
                    return "onWindowModeFlingToTarget";
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
            Bundle _arg1;
            Rect _arg0;
            Rect _arg02;
            Rect _arg12;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IWindowModeCallback.DESCRIPTOR);
                    return true;
                default:
                    boolean _arg3 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IWindowModeCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg3 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            onStackWindowModeChanged(_arg3, _arg1);
                            return true;
                        case 2:
                            data.enforceInterface(IWindowModeCallback.DESCRIPTOR);
                            String _arg03 = data.readString();
                            int _arg13 = data.readInt();
                            int _arg2 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg3 = true;
                            }
                            onPackageNameChanged(_arg03, _arg13, _arg2, _arg3);
                            return true;
                        case 3:
                            data.enforceInterface(IWindowModeCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            int _arg14 = data.readInt();
                            onWindowModeBoundChanged(_arg0, _arg14);
                            return true;
                        case 4:
                            data.enforceInterface(IWindowModeCallback.DESCRIPTOR);
                            int _arg04 = data.readInt();
                            onWindowModeStateChanged(_arg04);
                            return true;
                        case 5:
                            data.enforceInterface(IWindowModeCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg12 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            onWindowModeFlingToTarget(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IWindowModeCallback {
            public static IWindowModeCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IWindowModeCallback.DESCRIPTOR;
            }

            @Override // nothing.view.IWindowModeCallback
            public void onStackWindowModeChanged(boolean isNtWindowMode, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowModeCallback.DESCRIPTOR);
                    _data.writeInt(isNtWindowMode ? 1 : 0);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStackWindowModeChanged(isNtWindowMode, data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowModeCallback
            public void onPackageNameChanged(String packageName, int uid, int pid, boolean topStack) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowModeCallback.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeInt(topStack ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPackageNameChanged(packageName, uid, pid, topStack);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowModeCallback
            public void onWindowModeBoundChanged(Rect displayBound, int displayRotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowModeCallback.DESCRIPTOR);
                    if (displayBound != null) {
                        _data.writeInt(1);
                        displayBound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(displayRotation);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onWindowModeBoundChanged(displayBound, displayRotation);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowModeCallback
            public void onWindowModeStateChanged(int windowModeState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowModeCallback.DESCRIPTOR);
                    _data.writeInt(windowModeState);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onWindowModeStateChanged(windowModeState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowModeCallback
            public void onWindowModeFlingToTarget(Rect fromBound, Rect toBound) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowModeCallback.DESCRIPTOR);
                    if (fromBound != null) {
                        _data.writeInt(1);
                        fromBound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (toBound != null) {
                        _data.writeInt(1);
                        toBound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onWindowModeFlingToTarget(fromBound, toBound);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWindowModeCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWindowModeCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
