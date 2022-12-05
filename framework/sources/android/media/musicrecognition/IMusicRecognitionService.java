package android.media.musicrecognition;

import android.media.AudioFormat;
import android.media.musicrecognition.IMusicRecognitionAttributionTagCallback;
import android.media.musicrecognition.IMusicRecognitionServiceCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IMusicRecognitionService extends IInterface {
    public static final String DESCRIPTOR = "android.media.musicrecognition.IMusicRecognitionService";

    void getAttributionTag(IMusicRecognitionAttributionTagCallback iMusicRecognitionAttributionTagCallback) throws RemoteException;

    void onAudioStreamStarted(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, IMusicRecognitionServiceCallback iMusicRecognitionServiceCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IMusicRecognitionService {
        @Override // android.media.musicrecognition.IMusicRecognitionService
        public void onAudioStreamStarted(ParcelFileDescriptor fd, AudioFormat audioFormat, IMusicRecognitionServiceCallback callback) throws RemoteException {
        }

        @Override // android.media.musicrecognition.IMusicRecognitionService
        public void getAttributionTag(IMusicRecognitionAttributionTagCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IMusicRecognitionService {
        static final int TRANSACTION_getAttributionTag = 2;
        static final int TRANSACTION_onAudioStreamStarted = 1;

        public Stub() {
            attachInterface(this, IMusicRecognitionService.DESCRIPTOR);
        }

        public static IMusicRecognitionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMusicRecognitionService.DESCRIPTOR);
            if (iin != null && (iin instanceof IMusicRecognitionService)) {
                return (IMusicRecognitionService) iin;
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
                    return "onAudioStreamStarted";
                case 2:
                    return "getAttributionTag";
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
            ParcelFileDescriptor _arg0;
            AudioFormat _arg1;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IMusicRecognitionService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IMusicRecognitionService.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = AudioFormat.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            IMusicRecognitionServiceCallback _arg2 = IMusicRecognitionServiceCallback.Stub.asInterface(data.readStrongBinder());
                            onAudioStreamStarted(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            data.enforceInterface(IMusicRecognitionService.DESCRIPTOR);
                            IMusicRecognitionAttributionTagCallback _arg02 = IMusicRecognitionAttributionTagCallback.Stub.asInterface(data.readStrongBinder());
                            getAttributionTag(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IMusicRecognitionService {
            public static IMusicRecognitionService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMusicRecognitionService.DESCRIPTOR;
            }

            @Override // android.media.musicrecognition.IMusicRecognitionService
            public void onAudioStreamStarted(ParcelFileDescriptor fd, AudioFormat audioFormat, IMusicRecognitionServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMusicRecognitionService.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (audioFormat != null) {
                        _data.writeInt(1);
                        audioFormat.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAudioStreamStarted(fd, audioFormat, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.musicrecognition.IMusicRecognitionService
            public void getAttributionTag(IMusicRecognitionAttributionTagCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMusicRecognitionService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAttributionTag(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IMusicRecognitionService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IMusicRecognitionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
