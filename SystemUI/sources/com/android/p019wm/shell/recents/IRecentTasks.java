package com.android.p019wm.shell.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.p019wm.shell.recents.IRecentTasksListener;
import com.android.p019wm.shell.util.GroupedRecentTaskInfo;

/* renamed from: com.android.wm.shell.recents.IRecentTasks */
public interface IRecentTasks extends IInterface {

    /* renamed from: com.android.wm.shell.recents.IRecentTasks$Default */
    public static class Default implements IRecentTasks {
        public IBinder asBinder() {
            return null;
        }

        public GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException {
            return null;
        }

        public void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
        }

        public void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
        }
    }

    GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException;

    void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException;

    void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException;

    /* renamed from: com.android.wm.shell.recents.IRecentTasks$Stub */
    public static abstract class Stub extends Binder implements IRecentTasks {
        private static final String DESCRIPTOR = "com.android.wm.shell.recents.IRecentTasks";
        static final int TRANSACTION_getRecentTasks = 4;
        static final int TRANSACTION_registerRecentTasksListener = 2;
        static final int TRANSACTION_unregisterRecentTasksListener = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecentTasks asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRecentTasks)) {
                return new Proxy(iBinder);
            }
            return (IRecentTasks) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                registerRecentTasksListener(IRecentTasksListener.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                unregisterRecentTasksListener(IRecentTasksListener.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                GroupedRecentTaskInfo[] recentTasks = getRecentTasks(parcel.readInt(), parcel.readInt(), parcel.readInt());
                parcel2.writeNoException();
                parcel2.writeTypedArray(recentTasks, 1);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.recents.IRecentTasks$Stub$Proxy */
        private static class Proxy implements IRecentTasks {
            public static IRecentTasks sDefaultImpl;
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

            public void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRecentTasksListener != null ? iRecentTasksListener.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().registerRecentTasksListener(iRecentTasksListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRecentTasksListener != null ? iRecentTasksListener.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterRecentTasksListener(iRecentTasksListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecentTasks(i, i2, i3);
                    }
                    obtain2.readException();
                    GroupedRecentTaskInfo[] groupedRecentTaskInfoArr = (GroupedRecentTaskInfo[]) obtain2.createTypedArray(GroupedRecentTaskInfo.CREATOR);
                    obtain2.recycle();
                    obtain.recycle();
                    return groupedRecentTaskInfoArr;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IRecentTasks iRecentTasks) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iRecentTasks == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iRecentTasks;
                return true;
            }
        }

        public static IRecentTasks getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
