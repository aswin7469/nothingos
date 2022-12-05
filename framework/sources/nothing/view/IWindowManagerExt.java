package nothing.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import nothing.view.IWindowModeCallback;
/* loaded from: classes4.dex */
public interface IWindowManagerExt extends IInterface {
    public static final String DESCRIPTOR = "nothing.view.IWindowManagerExt";

    void addNonHighRefreshRatePackage(String str) throws RemoteException;

    void clearNonHighRefreshRatePackage() throws RemoteException;

    void closeWindowMode(int i, boolean z) throws RemoteException;

    Point covertWindowModeCoordinate(int i, Point point) throws RemoteException;

    void enterWindowMode(int i, int i2, int i3, Rect rect, float f) throws RemoteException;

    void exitWindowModeToFullscreen(int i, boolean z) throws RemoteException;

    void flingWindowMode(float f, float f2, Point point) throws RemoteException;

    Bundle getNonHighRefreshRatePackages() throws RemoteException;

    Rect getWindowModeBound(int i) throws RemoteException;

    boolean isAppMuted(String str, int i) throws RemoteException;

    boolean isWindowMode(int i, IBinder iBinder) throws RemoteException;

    void moveWindowModeBound(int i, Rect rect, float[] fArr, boolean z) throws RemoteException;

    void registerWindowModeListener(int i, IWindowModeCallback iWindowModeCallback) throws RemoteException;

    void removeNonHighRefreshRatePackage(String str) throws RemoteException;

    void setAppMuted(String str, int i, boolean z) throws RemoteException;

    void setStartWindowMode(String str) throws RemoteException;

    void shutdownDueToTemperature(int i) throws RemoteException;

    void switchWindowModeToMaxMode(int i, boolean z) throws RemoteException;

    void unregisterWindowModeListener(int i, IWindowModeCallback iWindowModeCallback) throws RemoteException;

    void updateWindowModeShowState(int i, boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IWindowManagerExt {
        @Override // nothing.view.IWindowManagerExt
        public void registerWindowModeListener(int windowingMode, IWindowModeCallback callback) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void unregisterWindowModeListener(int windowingMode, IWindowModeCallback callback) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public Rect getWindowModeBound(int windowingMode) throws RemoteException {
            return null;
        }

        @Override // nothing.view.IWindowManagerExt
        public void moveWindowModeBound(int windowingMode, Rect showFrame, float[] float9, boolean anim) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public Point covertWindowModeCoordinate(int windowingMode, Point point) throws RemoteException {
            return null;
        }

        @Override // nothing.view.IWindowManagerExt
        public void exitWindowModeToFullscreen(int windowingMode, boolean anim) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public boolean isWindowMode(int windowingMode, IBinder token) throws RemoteException {
            return false;
        }

        @Override // nothing.view.IWindowManagerExt
        public void updateWindowModeShowState(int windowingMode, boolean show, boolean nextChange) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void closeWindowMode(int windowingMode, boolean anim) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void enterWindowMode(int taskId, int oldWindowingMode, int newWindowingMode, Rect showBound, float cornerRadius) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void switchWindowModeToMaxMode(int windowingMode, boolean maxMode) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void flingWindowMode(float velocityX, float velocityY, Point startPosition) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void setAppMuted(String packageName, int uid, boolean muted) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public boolean isAppMuted(String packageName, int uid) throws RemoteException {
            return false;
        }

        @Override // nothing.view.IWindowManagerExt
        public void setStartWindowMode(String packageName) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void addNonHighRefreshRatePackage(String packageName) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void removeNonHighRefreshRatePackage(String packageName) throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public void clearNonHighRefreshRatePackage() throws RemoteException {
        }

        @Override // nothing.view.IWindowManagerExt
        public Bundle getNonHighRefreshRatePackages() throws RemoteException {
            return null;
        }

        @Override // nothing.view.IWindowManagerExt
        public void shutdownDueToTemperature(int temperatureType) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IWindowManagerExt {
        static final int TRANSACTION_addNonHighRefreshRatePackage = 16;
        static final int TRANSACTION_clearNonHighRefreshRatePackage = 18;
        static final int TRANSACTION_closeWindowMode = 9;
        static final int TRANSACTION_covertWindowModeCoordinate = 5;
        static final int TRANSACTION_enterWindowMode = 10;
        static final int TRANSACTION_exitWindowModeToFullscreen = 6;
        static final int TRANSACTION_flingWindowMode = 12;
        static final int TRANSACTION_getNonHighRefreshRatePackages = 19;
        static final int TRANSACTION_getWindowModeBound = 3;
        static final int TRANSACTION_isAppMuted = 14;
        static final int TRANSACTION_isWindowMode = 7;
        static final int TRANSACTION_moveWindowModeBound = 4;
        static final int TRANSACTION_registerWindowModeListener = 1;
        static final int TRANSACTION_removeNonHighRefreshRatePackage = 17;
        static final int TRANSACTION_setAppMuted = 13;
        static final int TRANSACTION_setStartWindowMode = 15;
        static final int TRANSACTION_shutdownDueToTemperature = 20;
        static final int TRANSACTION_switchWindowModeToMaxMode = 11;
        static final int TRANSACTION_unregisterWindowModeListener = 2;
        static final int TRANSACTION_updateWindowModeShowState = 8;

        public Stub() {
            attachInterface(this, IWindowManagerExt.DESCRIPTOR);
        }

        public static IWindowManagerExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWindowManagerExt.DESCRIPTOR);
            if (iin != null && (iin instanceof IWindowManagerExt)) {
                return (IWindowManagerExt) iin;
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
                    return "registerWindowModeListener";
                case 2:
                    return "unregisterWindowModeListener";
                case 3:
                    return "getWindowModeBound";
                case 4:
                    return "moveWindowModeBound";
                case 5:
                    return "covertWindowModeCoordinate";
                case 6:
                    return "exitWindowModeToFullscreen";
                case 7:
                    return "isWindowMode";
                case 8:
                    return "updateWindowModeShowState";
                case 9:
                    return "closeWindowMode";
                case 10:
                    return "enterWindowMode";
                case 11:
                    return "switchWindowModeToMaxMode";
                case 12:
                    return "flingWindowMode";
                case 13:
                    return "setAppMuted";
                case 14:
                    return "isAppMuted";
                case 15:
                    return "setStartWindowMode";
                case 16:
                    return "addNonHighRefreshRatePackage";
                case 17:
                    return "removeNonHighRefreshRatePackage";
                case 18:
                    return "clearNonHighRefreshRatePackage";
                case 19:
                    return "getNonHighRefreshRatePackages";
                case 20:
                    return "shutdownDueToTemperature";
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
            Rect _arg1;
            Point _arg12;
            boolean _arg13;
            Rect _arg3;
            Point _arg2;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IWindowManagerExt.DESCRIPTOR);
                    return true;
                default:
                    boolean _arg22 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            IWindowModeCallback _arg14 = IWindowModeCallback.Stub.asInterface(data.readStrongBinder());
                            registerWindowModeListener(_arg0, _arg14);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg02 = data.readInt();
                            IWindowModeCallback _arg15 = IWindowModeCallback.Stub.asInterface(data.readStrongBinder());
                            unregisterWindowModeListener(_arg02, _arg15);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg03 = data.readInt();
                            Rect _result = getWindowModeBound(_arg03);
                            reply.writeNoException();
                            if (_result != null) {
                                reply.writeInt(1);
                                _result.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 4:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg04 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg1 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            float[] _arg23 = data.createFloatArray();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            moveWindowModeBound(_arg04, _arg1, _arg23, _arg22);
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg05 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg12 = Point.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            Point _result2 = covertWindowModeCoordinate(_arg05, _arg12);
                            reply.writeNoException();
                            if (_result2 != null) {
                                reply.writeInt(1);
                                _result2.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 6:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg06 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            exitWindowModeToFullscreen(_arg06, _arg22);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg07 = data.readInt();
                            IBinder _arg16 = data.readStrongBinder();
                            boolean isWindowMode = isWindowMode(_arg07, _arg16);
                            reply.writeNoException();
                            reply.writeInt(isWindowMode ? 1 : 0);
                            return true;
                        case 8:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg08 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg13 = true;
                            } else {
                                _arg13 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            updateWindowModeShowState(_arg08, _arg13, _arg22);
                            reply.writeNoException();
                            return true;
                        case 9:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg09 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            closeWindowMode(_arg09, _arg22);
                            reply.writeNoException();
                            return true;
                        case 10:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg010 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg24 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg3 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            float _arg4 = data.readFloat();
                            enterWindowMode(_arg010, _arg17, _arg24, _arg3, _arg4);
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg011 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            switchWindowModeToMaxMode(_arg011, _arg22);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            float _arg012 = data.readFloat();
                            float _arg18 = data.readFloat();
                            if (data.readInt() != 0) {
                                _arg2 = Point.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            flingWindowMode(_arg012, _arg18, _arg2);
                            reply.writeNoException();
                            return true;
                        case 13:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            String _arg013 = data.readString();
                            int _arg19 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            }
                            setAppMuted(_arg013, _arg19, _arg22);
                            reply.writeNoException();
                            return true;
                        case 14:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            String _arg014 = data.readString();
                            int _arg110 = data.readInt();
                            boolean isAppMuted = isAppMuted(_arg014, _arg110);
                            reply.writeNoException();
                            reply.writeInt(isAppMuted ? 1 : 0);
                            return true;
                        case 15:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            String _arg015 = data.readString();
                            setStartWindowMode(_arg015);
                            reply.writeNoException();
                            return true;
                        case 16:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            String _arg016 = data.readString();
                            addNonHighRefreshRatePackage(_arg016);
                            reply.writeNoException();
                            return true;
                        case 17:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            String _arg017 = data.readString();
                            removeNonHighRefreshRatePackage(_arg017);
                            reply.writeNoException();
                            return true;
                        case 18:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            clearNonHighRefreshRatePackage();
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            Bundle _result3 = getNonHighRefreshRatePackages();
                            reply.writeNoException();
                            if (_result3 != null) {
                                reply.writeInt(1);
                                _result3.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 20:
                            data.enforceInterface(IWindowManagerExt.DESCRIPTOR);
                            int _arg018 = data.readInt();
                            shutdownDueToTemperature(_arg018);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IWindowManagerExt {
            public static IWindowManagerExt sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IWindowManagerExt.DESCRIPTOR;
            }

            @Override // nothing.view.IWindowManagerExt
            public void registerWindowModeListener(int windowingMode, IWindowModeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerWindowModeListener(windowingMode, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void unregisterWindowModeListener(int windowingMode, IWindowModeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterWindowModeListener(windowingMode, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public Rect getWindowModeBound(int windowingMode) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWindowModeBound(windowingMode);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Rect.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void moveWindowModeBound(int windowingMode, Rect showFrame, float[] float9, boolean anim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    int i = 1;
                    if (showFrame != null) {
                        _data.writeInt(1);
                        showFrame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeFloatArray(float9);
                    if (!anim) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveWindowModeBound(windowingMode, showFrame, float9, anim);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public Point covertWindowModeCoordinate(int windowingMode, Point point) throws RemoteException {
                Point _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    if (point != null) {
                        _data.writeInt(1);
                        point.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().covertWindowModeCoordinate(windowingMode, point);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Point.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void exitWindowModeToFullscreen(int windowingMode, boolean anim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(anim ? 1 : 0);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().exitWindowModeToFullscreen(windowingMode, anim);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public boolean isWindowMode(int windowingMode, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeStrongBinder(token);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isWindowMode(windowingMode, token);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void updateWindowModeShowState(int windowingMode, boolean show, boolean nextChange) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    int i = 1;
                    _data.writeInt(show ? 1 : 0);
                    if (!nextChange) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateWindowModeShowState(windowingMode, show, nextChange);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void closeWindowMode(int windowingMode, boolean anim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(anim ? 1 : 0);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().closeWindowMode(windowingMode, anim);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void enterWindowMode(int taskId, int oldWindowingMode, int newWindowingMode, Rect showBound, float cornerRadius) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(oldWindowingMode);
                    _data.writeInt(newWindowingMode);
                    if (showBound != null) {
                        _data.writeInt(1);
                        showBound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeFloat(cornerRadius);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enterWindowMode(taskId, oldWindowingMode, newWindowingMode, showBound, cornerRadius);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void switchWindowModeToMaxMode(int windowingMode, boolean maxMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(maxMode ? 1 : 0);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().switchWindowModeToMaxMode(windowingMode, maxMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void flingWindowMode(float velocityX, float velocityY, Point startPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeFloat(velocityX);
                    _data.writeFloat(velocityY);
                    if (startPosition != null) {
                        _data.writeInt(1);
                        startPosition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().flingWindowMode(velocityX, velocityY, startPosition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void setAppMuted(String packageName, int uid, boolean muted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(muted ? 1 : 0);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAppMuted(packageName, uid, muted);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public boolean isAppMuted(String packageName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAppMuted(packageName, uid);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void setStartWindowMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStartWindowMode(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void addNonHighRefreshRatePackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addNonHighRefreshRatePackage(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void removeNonHighRefreshRatePackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeNonHighRefreshRatePackage(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void clearNonHighRefreshRatePackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearNonHighRefreshRatePackage();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public Bundle getNonHighRefreshRatePackages() throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNonHighRefreshRatePackages();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // nothing.view.IWindowManagerExt
            public void shutdownDueToTemperature(int temperatureType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWindowManagerExt.DESCRIPTOR);
                    _data.writeInt(temperatureType);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().shutdownDueToTemperature(temperatureType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWindowManagerExt impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWindowManagerExt getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
