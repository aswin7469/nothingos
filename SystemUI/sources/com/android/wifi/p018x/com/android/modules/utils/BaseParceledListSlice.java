package com.android.wifi.p018x.com.android.modules.utils;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wifi.x.com.android.modules.utils.BaseParceledListSlice */
abstract class BaseParceledListSlice<T> implements Parcelable {
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    /* access modifiers changed from: private */
    public static final int MAX_IPC_SIZE = IBinder.getSuggestedMaxIpcSizeBytes();
    /* access modifiers changed from: private */
    public static String TAG = "ParceledListSlice";
    private int mInlineCountLimit = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public final List<T> mList;

    /* access modifiers changed from: protected */
    public abstract Parcelable.Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader);

    /* access modifiers changed from: protected */
    public abstract void writeElement(T t, Parcel parcel, int i);

    /* access modifiers changed from: protected */
    public abstract void writeParcelableCreator(T t, Parcel parcel);

    public BaseParceledListSlice(List<T> list) {
        this.mList = list;
    }

    BaseParceledListSlice(Parcel parcel, ClassLoader classLoader) {
        int readInt = parcel.readInt();
        this.mList = new ArrayList(readInt);
        if (DEBUG) {
            Log.d(TAG, "Retrieving " + readInt + " items");
        }
        if (readInt > 0) {
            Parcelable.Creator<?> readParcelableCreator = readParcelableCreator(parcel, classLoader);
            Class<?> cls = null;
            int i = 0;
            while (i < readInt && parcel.readInt() != 0) {
                Object readCreator = readCreator(readParcelableCreator, parcel, classLoader);
                if (cls == null) {
                    cls = readCreator.getClass();
                } else {
                    verifySameType(cls, readCreator.getClass());
                }
                this.mList.add(readCreator);
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder("Read inline #");
                    sb.append(i);
                    sb.append(": ");
                    List<T> list = this.mList;
                    sb.append((Object) list.get(list.size() - 1));
                    Log.d(str, sb.toString());
                }
                i++;
            }
            if (i < readInt) {
                IBinder readStrongBinder = parcel.readStrongBinder();
                while (i < readInt) {
                    if (DEBUG) {
                        Log.d(TAG, "Reading more @" + i + " of " + readInt + ": retriever=" + readStrongBinder);
                    }
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    obtain.writeInt(i);
                    try {
                        readStrongBinder.transact(1, obtain, obtain2, 0);
                        while (i < readInt && obtain2.readInt() != 0) {
                            Object readCreator2 = readCreator(readParcelableCreator, obtain2, classLoader);
                            verifySameType(cls, readCreator2.getClass());
                            this.mList.add(readCreator2);
                            if (DEBUG) {
                                String str2 = TAG;
                                StringBuilder sb2 = new StringBuilder("Read extra #");
                                sb2.append(i);
                                sb2.append(": ");
                                List<T> list2 = this.mList;
                                sb2.append((Object) list2.get(list2.size() - 1));
                                Log.d(str2, sb2.toString());
                            }
                            i++;
                        }
                        obtain2.recycle();
                        obtain.recycle();
                    } catch (RemoteException e) {
                        Log.w(TAG, "Failure retrieving array; only received " + i + " of " + readInt, e);
                        return;
                    }
                }
            }
        }
    }

    private T readCreator(Parcelable.Creator<?> creator, Parcel parcel, ClassLoader classLoader) {
        if (creator instanceof Parcelable.ClassLoaderCreator) {
            return ((Parcelable.ClassLoaderCreator) creator).createFromParcel(parcel, classLoader);
        }
        return creator.createFromParcel(parcel);
    }

    /* access modifiers changed from: private */
    public static void verifySameType(Class<?> cls, Class<?> cls2) {
        String str;
        if (!cls2.equals(cls)) {
            StringBuilder sb = new StringBuilder("Can't unparcel type ");
            String str2 = null;
            if (cls2 == null) {
                str = null;
            } else {
                str = cls2.getName();
            }
            sb.append(str);
            sb.append(" in list of type ");
            if (cls != null) {
                str2 = cls.getName();
            }
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public List<T> getList() {
        return this.mList;
    }

    public void setInlineCountLimit(int i) {
        this.mInlineCountLimit = i;
    }

    public void writeToParcel(Parcel parcel, final int i) {
        final int size = this.mList.size();
        parcel.writeInt(size);
        if (DEBUG) {
            Log.d(TAG, "Writing " + size + " items");
        }
        if (size > 0) {
            final Class<?> cls = this.mList.get(0).getClass();
            writeParcelableCreator(this.mList.get(0), parcel);
            int i2 = 0;
            while (i2 < size && i2 < this.mInlineCountLimit && parcel.dataSize() < MAX_IPC_SIZE) {
                parcel.writeInt(1);
                T t = this.mList.get(i2);
                verifySameType(cls, t.getClass());
                writeElement(t, parcel, i);
                if (DEBUG) {
                    Log.d(TAG, "Wrote inline #" + i2 + ": " + this.mList.get(i2));
                }
                i2++;
            }
            if (i2 < size) {
                parcel.writeInt(0);
                C33431 r2 = new Binder() {
                    /* access modifiers changed from: protected */
                    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
                        if (i != 1) {
                            return super.onTransact(i, parcel, parcel2, i2);
                        }
                        int readInt = parcel.readInt();
                        if (BaseParceledListSlice.DEBUG) {
                            String r5 = BaseParceledListSlice.TAG;
                            Log.d(r5, "Writing more @" + readInt + " of " + size);
                        }
                        while (readInt < size && parcel2.dataSize() < BaseParceledListSlice.MAX_IPC_SIZE) {
                            parcel2.writeInt(1);
                            Object obj = BaseParceledListSlice.this.mList.get(readInt);
                            BaseParceledListSlice.verifySameType(cls, obj.getClass());
                            BaseParceledListSlice.this.writeElement(obj, parcel2, i);
                            if (BaseParceledListSlice.DEBUG) {
                                String r52 = BaseParceledListSlice.TAG;
                                Log.d(r52, "Wrote extra #" + readInt + ": " + BaseParceledListSlice.this.mList.get(readInt));
                            }
                            readInt++;
                        }
                        if (readInt < size) {
                            if (BaseParceledListSlice.DEBUG) {
                                String r53 = BaseParceledListSlice.TAG;
                                Log.d(r53, "Breaking @" + readInt + " of " + size);
                            }
                            parcel2.writeInt(0);
                        }
                        return true;
                    }
                };
                if (DEBUG) {
                    Log.d(TAG, "Breaking @" + i2 + " of " + size + ": retriever=" + r2);
                }
                parcel.writeStrongBinder(r2);
            }
        }
    }
}
