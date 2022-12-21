package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface IIpSecService extends IInterface {

    public static class Default implements IIpSecService {
        public void addAddressToTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException {
        }

        public IpSecSpiResponse allocateSecurityParameterIndex(String str, int i, IBinder iBinder) throws RemoteException {
            return null;
        }

        public void applyTransportModeTransform(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException {
        }

        public void applyTunnelModeTransform(int i, int i2, int i3, String str) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void closeUdpEncapsulationSocket(int i) throws RemoteException {
        }

        public IpSecTransformResponse createTransform(IpSecConfig ipSecConfig, IBinder iBinder, String str) throws RemoteException {
            return null;
        }

        public IpSecTunnelInterfaceResponse createTunnelInterface(String str, String str2, Network network, IBinder iBinder, String str3) throws RemoteException {
            return null;
        }

        public void deleteTransform(int i) throws RemoteException {
        }

        public void deleteTunnelInterface(int i, String str) throws RemoteException {
        }

        public IpSecUdpEncapResponse openUdpEncapsulationSocket(int i, IBinder iBinder) throws RemoteException {
            return null;
        }

        public void releaseSecurityParameterIndex(int i) throws RemoteException {
        }

        public void removeAddressFromTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException {
        }

        public void removeTransportModeTransforms(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
        }

        public void setNetworkForTunnelInterface(int i, Network network, String str) throws RemoteException {
        }
    }

    void addAddressToTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException;

    IpSecSpiResponse allocateSecurityParameterIndex(String str, int i, IBinder iBinder) throws RemoteException;

    void applyTransportModeTransform(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException;

    void applyTunnelModeTransform(int i, int i2, int i3, String str) throws RemoteException;

    void closeUdpEncapsulationSocket(int i) throws RemoteException;

    IpSecTransformResponse createTransform(IpSecConfig ipSecConfig, IBinder iBinder, String str) throws RemoteException;

    IpSecTunnelInterfaceResponse createTunnelInterface(String str, String str2, Network network, IBinder iBinder, String str3) throws RemoteException;

    void deleteTransform(int i) throws RemoteException;

    void deleteTunnelInterface(int i, String str) throws RemoteException;

    IpSecUdpEncapResponse openUdpEncapsulationSocket(int i, IBinder iBinder) throws RemoteException;

    void releaseSecurityParameterIndex(int i) throws RemoteException;

    void removeAddressFromTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException;

    void removeTransportModeTransforms(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void setNetworkForTunnelInterface(int i, Network network, String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IIpSecService {
        public static final String DESCRIPTOR = "android.net.IIpSecService";
        static final int TRANSACTION_addAddressToTunnelInterface = 6;
        static final int TRANSACTION_allocateSecurityParameterIndex = 1;
        static final int TRANSACTION_applyTransportModeTransform = 12;
        static final int TRANSACTION_applyTunnelModeTransform = 13;
        static final int TRANSACTION_closeUdpEncapsulationSocket = 4;
        static final int TRANSACTION_createTransform = 10;
        static final int TRANSACTION_createTunnelInterface = 5;
        static final int TRANSACTION_deleteTransform = 11;
        static final int TRANSACTION_deleteTunnelInterface = 9;
        static final int TRANSACTION_openUdpEncapsulationSocket = 3;
        static final int TRANSACTION_releaseSecurityParameterIndex = 2;
        static final int TRANSACTION_removeAddressFromTunnelInterface = 7;
        static final int TRANSACTION_removeTransportModeTransforms = 14;
        static final int TRANSACTION_setNetworkForTunnelInterface = 8;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "allocateSecurityParameterIndex";
                case 2:
                    return "releaseSecurityParameterIndex";
                case 3:
                    return "openUdpEncapsulationSocket";
                case 4:
                    return "closeUdpEncapsulationSocket";
                case 5:
                    return "createTunnelInterface";
                case 6:
                    return "addAddressToTunnelInterface";
                case 7:
                    return "removeAddressFromTunnelInterface";
                case 8:
                    return "setNetworkForTunnelInterface";
                case 9:
                    return "deleteTunnelInterface";
                case 10:
                    return "createTransform";
                case 11:
                    return "deleteTransform";
                case 12:
                    return "applyTransportModeTransform";
                case 13:
                    return "applyTunnelModeTransform";
                case 14:
                    return "removeTransportModeTransforms";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 13;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIpSecService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IIpSecService)) {
                return new Proxy(iBinder);
            }
            return (IIpSecService) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        String readString = parcel.readString();
                        int readInt = parcel.readInt();
                        IBinder readStrongBinder = parcel.readStrongBinder();
                        parcel.enforceNoDataAvail();
                        IpSecSpiResponse allocateSecurityParameterIndex = allocateSecurityParameterIndex(readString, readInt, readStrongBinder);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(allocateSecurityParameterIndex, 1);
                        break;
                    case 2:
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        releaseSecurityParameterIndex(readInt2);
                        parcel2.writeNoException();
                        break;
                    case 3:
                        int readInt3 = parcel.readInt();
                        IBinder readStrongBinder2 = parcel.readStrongBinder();
                        parcel.enforceNoDataAvail();
                        IpSecUdpEncapResponse openUdpEncapsulationSocket = openUdpEncapsulationSocket(readInt3, readStrongBinder2);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(openUdpEncapsulationSocket, 1);
                        break;
                    case 4:
                        int readInt4 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        closeUdpEncapsulationSocket(readInt4);
                        parcel2.writeNoException();
                        break;
                    case 5:
                        IBinder readStrongBinder3 = parcel.readStrongBinder();
                        String readString2 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        IpSecTunnelInterfaceResponse createTunnelInterface = createTunnelInterface(parcel.readString(), parcel.readString(), (Network) parcel.readTypedObject(Network.CREATOR), readStrongBinder3, readString2);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(createTunnelInterface, 1);
                        break;
                    case 6:
                        String readString3 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        addAddressToTunnelInterface(parcel.readInt(), (LinkAddress) parcel.readTypedObject(LinkAddress.CREATOR), readString3);
                        parcel2.writeNoException();
                        break;
                    case 7:
                        String readString4 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        removeAddressFromTunnelInterface(parcel.readInt(), (LinkAddress) parcel.readTypedObject(LinkAddress.CREATOR), readString4);
                        parcel2.writeNoException();
                        break;
                    case 8:
                        String readString5 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        setNetworkForTunnelInterface(parcel.readInt(), (Network) parcel.readTypedObject(Network.CREATOR), readString5);
                        parcel2.writeNoException();
                        break;
                    case 9:
                        int readInt5 = parcel.readInt();
                        String readString6 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        deleteTunnelInterface(readInt5, readString6);
                        parcel2.writeNoException();
                        break;
                    case 10:
                        IBinder readStrongBinder4 = parcel.readStrongBinder();
                        String readString7 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        IpSecTransformResponse createTransform = createTransform((IpSecConfig) parcel.readTypedObject(IpSecConfig.CREATOR), readStrongBinder4, readString7);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(createTransform, 1);
                        break;
                    case 11:
                        int readInt6 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        deleteTransform(readInt6);
                        parcel2.writeNoException();
                        break;
                    case 12:
                        int readInt7 = parcel.readInt();
                        int readInt8 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        applyTransportModeTransform((ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR), readInt7, readInt8);
                        parcel2.writeNoException();
                        break;
                    case 13:
                        int readInt9 = parcel.readInt();
                        int readInt10 = parcel.readInt();
                        int readInt11 = parcel.readInt();
                        String readString8 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        applyTunnelModeTransform(readInt9, readInt10, readInt11, readString8);
                        parcel2.writeNoException();
                        break;
                    case 14:
                        parcel.enforceNoDataAvail();
                        removeTransportModeTransforms((ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR));
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

        private static class Proxy implements IIpSecService {
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

            public IpSecSpiResponse allocateSecurityParameterIndex(String str, int i, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IpSecSpiResponse) obtain2.readTypedObject(IpSecSpiResponse.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void releaseSecurityParameterIndex(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IpSecUdpEncapResponse openUdpEncapsulationSocket(int i, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IpSecUdpEncapResponse) obtain2.readTypedObject(IpSecUdpEncapResponse.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void closeUdpEncapsulationSocket(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IpSecTunnelInterfaceResponse createTunnelInterface(String str, String str2, Network network, IBinder iBinder, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str3);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IpSecTunnelInterfaceResponse) obtain2.readTypedObject(IpSecTunnelInterfaceResponse.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addAddressToTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(linkAddress, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeAddressFromTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(linkAddress, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setNetworkForTunnelInterface(int i, Network network, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void deleteTunnelInterface(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IpSecTransformResponse createTransform(IpSecConfig ipSecConfig, IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(ipSecConfig, 0);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IpSecTransformResponse) obtain2.readTypedObject(IpSecTransformResponse.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void deleteTransform(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void applyTransportModeTransform(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void applyTunnelModeTransform(int i, int i2, int i3, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeString(str);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeTransportModeTransforms(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
