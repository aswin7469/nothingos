package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public interface ISoftApCallback extends IInterface {

    public static class Default implements ISoftApCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onBlockedClientConnecting(WifiClient wifiClient, int i) throws RemoteException {
        }

        public void onCapabilityChanged(SoftApCapability softApCapability) throws RemoteException {
        }

        public void onConnectedClientsOrInfoChanged(Map<String, SoftApInfo> map, Map<String, List<WifiClient>> map2, boolean z, boolean z2) throws RemoteException {
        }

        public void onStateChanged(int i, int i2) throws RemoteException {
        }
    }

    void onBlockedClientConnecting(WifiClient wifiClient, int i) throws RemoteException;

    void onCapabilityChanged(SoftApCapability softApCapability) throws RemoteException;

    void onConnectedClientsOrInfoChanged(Map<String, SoftApInfo> map, Map<String, List<WifiClient>> map2, boolean z, boolean z2) throws RemoteException;

    void onStateChanged(int i, int i2) throws RemoteException;

    public static abstract class Stub extends Binder implements ISoftApCallback {
        public static final String DESCRIPTOR = "android.net.wifi.ISoftApCallback";
        static final int TRANSACTION_onBlockedClientConnecting = 4;
        static final int TRANSACTION_onCapabilityChanged = 3;
        static final int TRANSACTION_onConnectedClientsOrInfoChanged = 2;
        static final int TRANSACTION_onStateChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISoftApCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISoftApCallback)) {
                return new Proxy(iBinder);
            }
            return (ISoftApCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            HashMap hashMap;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onStateChanged(parcel.readInt(), parcel.readInt());
                } else if (i == 2) {
                    int readInt = parcel.readInt();
                    HashMap hashMap2 = null;
                    if (readInt < 0) {
                        hashMap = null;
                    } else {
                        hashMap = new HashMap();
                    }
                    IntStream.range(0, readInt).forEach(new ISoftApCallback$Stub$$ExternalSyntheticLambda0(parcel, hashMap));
                    int readInt2 = parcel.readInt();
                    if (readInt2 >= 0) {
                        hashMap2 = new HashMap();
                    }
                    IntStream.range(0, readInt2).forEach(new ISoftApCallback$Stub$$ExternalSyntheticLambda1(parcel, hashMap2));
                    onConnectedClientsOrInfoChanged(hashMap, hashMap2, parcel.readBoolean(), parcel.readBoolean());
                } else if (i == 3) {
                    onCapabilityChanged((SoftApCapability) parcel.readTypedObject(SoftApCapability.CREATOR));
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onBlockedClientConnecting((WifiClient) parcel.readTypedObject(WifiClient.CREATOR), parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ISoftApCallback {
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

            public void onStateChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onConnectedClientsOrInfoChanged(Map<String, SoftApInfo> map, Map<String, List<WifiClient>> map2, boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (map == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(map.size());
                        map.forEach(new ISoftApCallback$Stub$Proxy$$ExternalSyntheticLambda0(obtain));
                    }
                    if (map2 == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(map2.size());
                        map2.forEach(new ISoftApCallback$Stub$Proxy$$ExternalSyntheticLambda1(obtain));
                    }
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            static /* synthetic */ void lambda$onConnectedClientsOrInfoChanged$0(Parcel parcel, String str, SoftApInfo softApInfo) {
                parcel.writeString(str);
                parcel.writeTypedObject(softApInfo, 0);
            }

            static /* synthetic */ void lambda$onConnectedClientsOrInfoChanged$1(Parcel parcel, String str, List list) {
                parcel.writeString(str);
                parcel.writeTypedList(list);
            }

            public void onCapabilityChanged(SoftApCapability softApCapability) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(softApCapability, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onBlockedClientConnecting(WifiClient wifiClient, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiClient, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
