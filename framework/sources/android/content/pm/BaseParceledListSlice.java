package android.content.pm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class BaseParceledListSlice<T> implements Parcelable {
    private int mInlineCountLimit = Integer.MAX_VALUE;
    private final List<T> mList;
    private static String TAG = "ParceledListSlice";
    private static boolean DEBUG = false;
    private static final int MAX_IPC_SIZE = IBinder.getSuggestedMaxIpcSizeBytes();

    protected abstract Parcelable.Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader);

    protected abstract void writeElement(T t, Parcel parcel, int i);

    protected abstract void writeParcelableCreator(T t, Parcel parcel);

    public BaseParceledListSlice(List<T> list) {
        this.mList = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseParceledListSlice(Parcel p, ClassLoader loader) {
        int N = p.readInt();
        this.mList = new ArrayList(N);
        if (DEBUG) {
            String str = TAG;
            Log.d(str, "Retrieving " + N + " items");
        }
        if (N <= 0) {
            return;
        }
        Parcelable.Creator<?> creator = readParcelableCreator(p, loader);
        Class<?> listElementClass = null;
        int i = 0;
        while (i < N && p.readInt() != 0) {
            listElementClass = readVerifyAndAddElement(creator, p, loader, listElementClass);
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Read inline #");
                sb.append(i);
                sb.append(": ");
                List<T> list = this.mList;
                sb.append(list.get(list.size() - 1));
                Log.d(str2, sb.toString());
            }
            i++;
        }
        if (i >= N) {
            return;
        }
        IBinder retriever = p.readStrongBinder();
        while (i < N) {
            if (DEBUG) {
                String str3 = TAG;
                Log.d(str3, "Reading more @" + i + " of " + N + ": retriever=" + retriever);
            }
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInt(i);
            try {
                retriever.transact(1, data, reply, 0);
                while (i < N && reply.readInt() != 0) {
                    listElementClass = readVerifyAndAddElement(creator, reply, loader, listElementClass);
                    if (DEBUG) {
                        String str4 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Read extra #");
                        sb2.append(i);
                        sb2.append(": ");
                        List<T> list2 = this.mList;
                        sb2.append(list2.get(list2.size() - 1));
                        Log.d(str4, sb2.toString());
                    }
                    i++;
                }
                reply.recycle();
                data.recycle();
            } catch (RemoteException e) {
                String str5 = TAG;
                Log.w(str5, "Failure retrieving array; only received " + i + " of " + N, e);
                return;
            }
        }
    }

    private Class<?> readVerifyAndAddElement(Parcelable.Creator<?> creator, Parcel p, ClassLoader loader, Class<?> listElementClass) {
        T parcelable = readCreator(creator, p, loader);
        if (listElementClass == null) {
            listElementClass = parcelable.getClass();
        } else {
            verifySameType(listElementClass, parcelable.getClass());
        }
        this.mList.add(parcelable);
        return listElementClass;
    }

    private T readCreator(Parcelable.Creator<?> creator, Parcel p, ClassLoader loader) {
        if (creator instanceof Parcelable.ClassLoaderCreator) {
            Parcelable.ClassLoaderCreator<?> classLoaderCreator = (Parcelable.ClassLoaderCreator) creator;
            return (T) classLoaderCreator.mo3532createFromParcel(p, loader);
        }
        return (T) creator.mo3559createFromParcel(p);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't unparcel type ");
            String str = null;
            sb.append(actual == null ? null : actual.getName());
            sb.append(" in list of type ");
            if (expected != null) {
                str = expected.getName();
            }
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public List<T> getList() {
        return this.mList;
    }

    public void setInlineCountLimit(int maxCount) {
        this.mInlineCountLimit = maxCount;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0090, code lost:
        r10.writeInt(0);
        r3 = new android.content.pm.BaseParceledListSlice.AnonymousClass1(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x009a, code lost:
        if (android.content.pm.BaseParceledListSlice.DEBUG == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x009c, code lost:
        r5 = android.content.pm.BaseParceledListSlice.TAG;
        android.util.Log.d(r5, "Breaking @" + r4 + " of " + r0 + ": retriever=" + r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00c2, code lost:
        r10.writeStrongBinder(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00c5, code lost:
        return;
     */
    @Override // android.os.Parcelable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeToParcel(Parcel dest, final int flags) {
        final int N = this.mList.size();
        dest.writeInt(N);
        if (DEBUG) {
            String str = TAG;
            Log.d(str, "Writing " + N + " items");
        }
        if (N > 0) {
            final Class<?> listElementClass = this.mList.get(0).getClass();
            writeParcelableCreator(this.mList.get(0), dest);
            int i = 0;
            while (i < N && i < this.mInlineCountLimit && dest.dataSize() < MAX_IPC_SIZE) {
                dest.writeInt(1);
                T parcelable = this.mList.get(i);
                verifySameType(listElementClass, parcelable.getClass());
                writeElement(parcelable, dest, flags);
                if (DEBUG) {
                    String str2 = TAG;
                    Log.d(str2, "Wrote inline #" + i + ": " + this.mList.get(i));
                }
                i++;
            }
        }
    }
}
