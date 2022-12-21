package android.net.wifi.aware;

import android.net.wifi.aware.IWifiAwareDiscoverySessionCallback;
import android.net.wifi.aware.IWifiAwareEventCallback;
import android.net.wifi.aware.IWifiAwareMacAddressProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiAwareManager extends IInterface {

    public static class Default implements IWifiAwareManager {
        public IBinder asBinder() {
            return null;
        }

        public void connect(IBinder iBinder, String str, String str2, IWifiAwareEventCallback iWifiAwareEventCallback, ConfigRequest configRequest, boolean z, Bundle bundle) throws RemoteException {
        }

        public void disconnect(int i, IBinder iBinder) throws RemoteException {
        }

        public void enableInstantCommunicationMode(String str, boolean z) throws RemoteException {
        }

        public AwareResources getAvailableAwareResources() throws RemoteException {
            return null;
        }

        public Characteristics getCharacteristics() throws RemoteException {
            return null;
        }

        public boolean isDeviceAttached() throws RemoteException {
            return false;
        }

        public boolean isInstantCommunicationModeEnabled() throws RemoteException {
            return false;
        }

        public boolean isSetChannelOnDataPathSupported() throws RemoteException {
            return false;
        }

        public boolean isUsageEnabled() throws RemoteException {
            return false;
        }

        public void publish(String str, String str2, int i, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException {
        }

        public void requestMacAddresses(int i, int[] iArr, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) throws RemoteException {
        }

        public void sendMessage(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws RemoteException {
        }

        public void setAwareParams(AwareParams awareParams) throws RemoteException {
        }

        public void subscribe(String str, String str2, int i, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException {
        }

        public void terminateSession(int i, int i2) throws RemoteException {
        }

        public void updatePublish(int i, int i2, PublishConfig publishConfig) throws RemoteException {
        }

        public void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) throws RemoteException {
        }
    }

    void connect(IBinder iBinder, String str, String str2, IWifiAwareEventCallback iWifiAwareEventCallback, ConfigRequest configRequest, boolean z, Bundle bundle) throws RemoteException;

    void disconnect(int i, IBinder iBinder) throws RemoteException;

    void enableInstantCommunicationMode(String str, boolean z) throws RemoteException;

    AwareResources getAvailableAwareResources() throws RemoteException;

    Characteristics getCharacteristics() throws RemoteException;

    boolean isDeviceAttached() throws RemoteException;

    boolean isInstantCommunicationModeEnabled() throws RemoteException;

    boolean isSetChannelOnDataPathSupported() throws RemoteException;

    boolean isUsageEnabled() throws RemoteException;

    void publish(String str, String str2, int i, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException;

    void requestMacAddresses(int i, int[] iArr, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) throws RemoteException;

    void sendMessage(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws RemoteException;

    void setAwareParams(AwareParams awareParams) throws RemoteException;

    void subscribe(String str, String str2, int i, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException;

    void terminateSession(int i, int i2) throws RemoteException;

    void updatePublish(int i, int i2, PublishConfig publishConfig) throws RemoteException;

    void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiAwareManager {
        public static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareManager";
        static final int TRANSACTION_connect = 9;
        static final int TRANSACTION_disconnect = 10;
        static final int TRANSACTION_enableInstantCommunicationMode = 5;
        static final int TRANSACTION_getAvailableAwareResources = 3;
        static final int TRANSACTION_getCharacteristics = 2;
        static final int TRANSACTION_isDeviceAttached = 4;
        static final int TRANSACTION_isInstantCommunicationModeEnabled = 6;
        static final int TRANSACTION_isSetChannelOnDataPathSupported = 7;
        static final int TRANSACTION_isUsageEnabled = 1;
        static final int TRANSACTION_publish = 11;
        static final int TRANSACTION_requestMacAddresses = 17;
        static final int TRANSACTION_sendMessage = 15;
        static final int TRANSACTION_setAwareParams = 8;
        static final int TRANSACTION_subscribe = 12;
        static final int TRANSACTION_terminateSession = 16;
        static final int TRANSACTION_updatePublish = 13;
        static final int TRANSACTION_updateSubscribe = 14;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiAwareManager)) {
                return new Proxy(iBinder);
            }
            return (IWifiAwareManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        boolean isUsageEnabled = isUsageEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isUsageEnabled);
                        break;
                    case 2:
                        Characteristics characteristics = getCharacteristics();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(characteristics, 1);
                        break;
                    case 3:
                        AwareResources availableAwareResources = getAvailableAwareResources();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(availableAwareResources, 1);
                        break;
                    case 4:
                        boolean isDeviceAttached = isDeviceAttached();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isDeviceAttached);
                        break;
                    case 5:
                        enableInstantCommunicationMode(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 6:
                        boolean isInstantCommunicationModeEnabled = isInstantCommunicationModeEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isInstantCommunicationModeEnabled);
                        break;
                    case 7:
                        boolean isSetChannelOnDataPathSupported = isSetChannelOnDataPathSupported();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isSetChannelOnDataPathSupported);
                        break;
                    case 8:
                        setAwareParams((AwareParams) parcel.readTypedObject(AwareParams.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 9:
                        connect(parcel.readStrongBinder(), parcel.readString(), parcel.readString(), IWifiAwareEventCallback.Stub.asInterface(parcel.readStrongBinder()), (ConfigRequest) parcel.readTypedObject(ConfigRequest.CREATOR), parcel.readBoolean(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 10:
                        disconnect(parcel.readInt(), parcel.readStrongBinder());
                        parcel2.writeNoException();
                        break;
                    case 11:
                        publish(parcel.readString(), parcel.readString(), parcel.readInt(), (PublishConfig) parcel.readTypedObject(PublishConfig.CREATOR), IWifiAwareDiscoverySessionCallback.Stub.asInterface(parcel.readStrongBinder()), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 12:
                        subscribe(parcel.readString(), parcel.readString(), parcel.readInt(), (SubscribeConfig) parcel.readTypedObject(SubscribeConfig.CREATOR), IWifiAwareDiscoverySessionCallback.Stub.asInterface(parcel.readStrongBinder()), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 13:
                        updatePublish(parcel.readInt(), parcel.readInt(), (PublishConfig) parcel.readTypedObject(PublishConfig.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 14:
                        updateSubscribe(parcel.readInt(), parcel.readInt(), (SubscribeConfig) parcel.readTypedObject(SubscribeConfig.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 15:
                        sendMessage(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 16:
                        terminateSession(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 17:
                        requestMacAddresses(parcel.readInt(), parcel.createIntArray(), IWifiAwareMacAddressProvider.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiAwareManager {
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

            public boolean isUsageEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Characteristics getCharacteristics() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Characteristics) obtain2.readTypedObject(Characteristics.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public AwareResources getAvailableAwareResources() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (AwareResources) obtain2.readTypedObject(AwareResources.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDeviceAttached() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enableInstantCommunicationMode(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isInstantCommunicationModeEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSetChannelOnDataPathSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAwareParams(AwareParams awareParams) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(awareParams, 0);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void connect(IBinder iBinder, String str, String str2, IWifiAwareEventCallback iWifiAwareEventCallback, ConfigRequest configRequest, boolean z, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iWifiAwareEventCallback);
                    obtain.writeTypedObject(configRequest, 0);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disconnect(int i, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void publish(String str, String str2, int i, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(publishConfig, 0);
                    obtain.writeStrongInterface(iWifiAwareDiscoverySessionCallback);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void subscribe(String str, String str2, int i, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(subscribeConfig, 0);
                    obtain.writeStrongInterface(iWifiAwareDiscoverySessionCallback);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updatePublish(int i, int i2, PublishConfig publishConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(publishConfig, 0);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(subscribeConfig, 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendMessage(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void terminateSession(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestMacAddresses(int i, int[] iArr, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    obtain.writeStrongInterface(iWifiAwareMacAddressProvider);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
