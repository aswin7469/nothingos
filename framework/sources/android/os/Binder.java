package android.os;

import android.annotation.SystemApi;
import android.app.AppOpsManager;
import android.os.IBinder;
import android.provider.SettingsStringUtil;
import android.util.ExceptionUtils;
import android.util.Log;
import android.util.Slog;
import com.android.internal.os.BinderCallHeavyHitterWatcher;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FunctionalUtils;
import dalvik.annotation.optimization.CriticalNative;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import libcore.io.IoUtils;
import libcore.util.NativeAllocationRegistry;
/* loaded from: classes2.dex */
public class Binder implements IBinder {
    public static final boolean CHECK_PARCEL_SIZE = false;
    private static final boolean FIND_POTENTIAL_LEAKS = false;
    private static final int NATIVE_ALLOCATION_SIZE = 500;
    static final String TAG = "Binder";
    public static final int UNSET_WORKSOURCE = -1;
    private String mDescriptor;
    private final long mObject;
    private IInterface mOwner;
    public static boolean LOG_RUNTIME_EXCEPTION = false;
    private static volatile String sDumpDisabled = null;
    private static volatile TransactionTracker sTransactionTracker = null;
    private static BinderInternal.Observer sObserver = null;
    private static volatile BinderCallHeavyHitterWatcher sHeavyHitterWatcher = null;
    private static volatile boolean sTracingEnabled = false;
    static volatile boolean sWarnOnBlocking = false;
    static ThreadLocal<Boolean> sWarnOnBlockingOnCurrentThread = ThreadLocal.withInitial(Binder$$ExternalSyntheticLambda1.INSTANCE);
    private static volatile BinderInternal.WorkSourceProvider sWorkSourceProvider = Binder$$ExternalSyntheticLambda0.INSTANCE;

    public static final native void blockUntilThreadAvailable();

    @CriticalNative
    public static final native long clearCallingIdentity();

    @CriticalNative
    public static final native long clearCallingWorkSource();

    public static final native void flushPendingCommands();

    @CriticalNative
    public static final native int getCallingPid();

    @CriticalNative
    public static final native int getCallingUid();

    @CriticalNative
    public static final native int getCallingWorkSourceUid();

    private static native long getNativeBBinderHolder();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getNativeFinalizer();

    @CriticalNative
    public static final native int getThreadStrictModePolicy();

    @CriticalNative
    public static final native boolean isHandlingTransaction();

    @CriticalNative
    public static final native void restoreCallingIdentity(long j);

    @CriticalNative
    public static final native void restoreCallingWorkSource(long j);

    @CriticalNative
    public static final native long setCallingWorkSourceUid(int i);

    @CriticalNative
    public static final native void setThreadStrictModePolicy(int i);

    public final native void forceDowngradeToSystemStability();

    @Override // android.os.IBinder
    public final native IBinder getExtension();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final native void markVintfStability();

    public final native void setExtension(IBinder iBinder);

    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Binder.class.getClassLoader(), Binder.getNativeFinalizer(), 500);

        private NoImagePreloadHolder() {
        }
    }

    public static void enableTracing() {
        sTracingEnabled = true;
    }

    public static void disableTracing() {
        sTracingEnabled = false;
    }

    public static boolean isTracingEnabled() {
        return sTracingEnabled;
    }

    public static synchronized TransactionTracker getTransactionTracker() {
        TransactionTracker transactionTracker;
        synchronized (Binder.class) {
            if (sTransactionTracker == null) {
                sTransactionTracker = new TransactionTracker();
            }
            transactionTracker = sTransactionTracker;
        }
        return transactionTracker;
    }

    public static void setObserver(BinderInternal.Observer observer) {
        sObserver = observer;
    }

    public static void setWarnOnBlocking(boolean warnOnBlocking) {
        sWarnOnBlocking = warnOnBlocking;
    }

    public static IBinder allowBlocking(IBinder binder) {
        try {
            if (binder instanceof BinderProxy) {
                ((BinderProxy) binder).mWarnOnBlocking = false;
            } else if (binder != null && binder.getInterfaceDescriptor() != null && binder.queryLocalInterface(binder.getInterfaceDescriptor()) == null) {
                Log.w(TAG, "Unable to allow blocking on interface " + binder);
            }
        } catch (RemoteException e) {
        }
        return binder;
    }

    public static IBinder defaultBlocking(IBinder binder) {
        if (binder instanceof BinderProxy) {
            ((BinderProxy) binder).mWarnOnBlocking = sWarnOnBlocking;
        }
        return binder;
    }

    public static void copyAllowBlocking(IBinder fromBinder, IBinder toBinder) {
        if ((fromBinder instanceof BinderProxy) && (toBinder instanceof BinderProxy)) {
            ((BinderProxy) toBinder).mWarnOnBlocking = ((BinderProxy) fromBinder).mWarnOnBlocking;
        }
    }

    public static void allowBlockingForCurrentThread() {
        sWarnOnBlockingOnCurrentThread.set(false);
    }

    public static void defaultBlockingForCurrentThread() {
        sWarnOnBlockingOnCurrentThread.set(Boolean.valueOf(sWarnOnBlocking));
    }

    public static final int getCallingUidOrThrow() {
        if (!isHandlingTransaction()) {
            throw new IllegalStateException("Thread is not in a binder transcation");
        }
        return getCallingUid();
    }

    public static final UserHandle getCallingUserHandle() {
        return UserHandle.of(UserHandle.getUserId(getCallingUid()));
    }

    public static final void withCleanCallingIdentity(FunctionalUtils.ThrowingRunnable action) {
        long callingIdentity = clearCallingIdentity();
        try {
            action.runOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (0 != 0) {
                throw ExceptionUtils.propagate(null);
            }
        } catch (Throwable throwable) {
            restoreCallingIdentity(callingIdentity);
            throw ExceptionUtils.propagate(throwable);
        }
    }

    public static final <T> T withCleanCallingIdentity(FunctionalUtils.ThrowingSupplier<T> action) {
        long callingIdentity = clearCallingIdentity();
        try {
            T orThrow = action.getOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (0 == 0) {
                return orThrow;
            }
            throw ExceptionUtils.propagate(null);
        } catch (Throwable throwable) {
            restoreCallingIdentity(callingIdentity);
            throw ExceptionUtils.propagate(throwable);
        }
    }

    public static final void joinThreadPool() {
        BinderInternal.joinThreadPool();
    }

    public static final boolean isProxy(IInterface iface) {
        return iface.asBinder() != iface;
    }

    public Binder() {
        this(null);
    }

    public Binder(String descriptor) {
        long nativeBBinderHolder = getNativeBBinderHolder();
        this.mObject = nativeBBinderHolder;
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, nativeBBinderHolder);
        this.mDescriptor = descriptor;
    }

    public void attachInterface(IInterface owner, String descriptor) {
        this.mOwner = owner;
        this.mDescriptor = descriptor;
    }

    @Override // android.os.IBinder
    public String getInterfaceDescriptor() {
        return this.mDescriptor;
    }

    @Override // android.os.IBinder
    public boolean pingBinder() {
        return true;
    }

    @Override // android.os.IBinder
    public boolean isBinderAlive() {
        return true;
    }

    @Override // android.os.IBinder
    public IInterface queryLocalInterface(String descriptor) {
        String str = this.mDescriptor;
        if (str != null && str.equals(descriptor)) {
            return this.mOwner;
        }
        return null;
    }

    public static void setDumpDisabled(String msg) {
        sDumpDisabled = msg;
    }

    @SystemApi
    /* loaded from: classes2.dex */
    public interface ProxyTransactListener {
        void onTransactEnded(Object obj);

        Object onTransactStarted(IBinder iBinder, int i);

        default Object onTransactStarted(IBinder binder, int transactionCode, int flags) {
            return onTransactStarted(binder, transactionCode);
        }
    }

    /* loaded from: classes2.dex */
    public static class PropagateWorkSourceTransactListener implements ProxyTransactListener {
        @Override // android.os.Binder.ProxyTransactListener
        public Object onTransactStarted(IBinder binder, int transactionCode) {
            int uid = ThreadLocalWorkSource.getUid();
            if (uid != -1) {
                return Long.valueOf(Binder.setCallingWorkSourceUid(uid));
            }
            return null;
        }

        @Override // android.os.Binder.ProxyTransactListener
        public void onTransactEnded(Object session) {
            if (session != null) {
                long token = ((Long) session).longValue();
                Binder.restoreCallingWorkSource(token);
            }
        }
    }

    @SystemApi
    public static void setProxyTransactListener(ProxyTransactListener listener) {
        BinderProxy.setTransactListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        FileDescriptor fileDescriptor;
        if (code != 1598968902) {
            if (code != 1598311760) {
                if (code == 1598246212) {
                    ParcelFileDescriptor in = data.readFileDescriptor();
                    ParcelFileDescriptor out = data.readFileDescriptor();
                    ParcelFileDescriptor err = data.readFileDescriptor();
                    String[] args = data.readStringArray();
                    ShellCallback shellCallback = ShellCallback.CREATOR.mo3559createFromParcel(data);
                    ResultReceiver resultReceiver = ResultReceiver.CREATOR.mo3559createFromParcel(data);
                    if (out != null) {
                        if (in == null) {
                            fileDescriptor = null;
                        } else {
                            try {
                                fileDescriptor = in.getFileDescriptor();
                            } catch (Throwable th) {
                                IoUtils.closeQuietly(in);
                                IoUtils.closeQuietly(out);
                                IoUtils.closeQuietly(err);
                                if (reply != null) {
                                    reply.writeNoException();
                                } else {
                                    StrictMode.clearGatheredViolations();
                                }
                                throw th;
                            }
                        }
                        shellCommand(fileDescriptor, out.getFileDescriptor(), err != null ? err.getFileDescriptor() : out.getFileDescriptor(), args, shellCallback, resultReceiver);
                    }
                    IoUtils.closeQuietly(in);
                    IoUtils.closeQuietly(out);
                    IoUtils.closeQuietly(err);
                    if (reply != null) {
                        reply.writeNoException();
                    } else {
                        StrictMode.clearGatheredViolations();
                    }
                    return true;
                }
                return false;
            }
            ParcelFileDescriptor fd = data.readFileDescriptor();
            String[] args2 = data.readStringArray();
            if (fd != null) {
                try {
                    try {
                        dump(fd.getFileDescriptor(), args2);
                        IoUtils.closeQuietly(fd);
                    } catch (Throwable th2) {
                        th = th2;
                        IoUtils.closeQuietly(fd);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            if (reply != null) {
                reply.writeNoException();
            } else {
                StrictMode.clearGatheredViolations();
            }
            return true;
        }
        reply.writeString(getInterfaceDescriptor());
        return true;
    }

    public String getTransactionName(int transactionCode) {
        return null;
    }

    @Override // android.os.IBinder
    public void dump(FileDescriptor fd, String[] args) {
        FileOutputStream fout = new FileOutputStream(fd);
        PrintWriter pw = new FastPrintWriter(fout);
        try {
            doDump(fd, pw, args);
        } finally {
            pw.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doDump(FileDescriptor fd, PrintWriter pw, String[] args) {
        String disabled = sDumpDisabled;
        if (disabled == null) {
            try {
                dump(fd, pw, args);
                return;
            } catch (SecurityException e) {
                pw.println("Security exception: " + e.getMessage());
                throw e;
            } catch (Throwable e2) {
                pw.println();
                pw.println("Exception occurred while dumping:");
                e2.printStackTrace(pw);
                return;
            }
        }
        pw.println(sDumpDisabled);
    }

    @Override // android.os.IBinder
    public void dumpAsync(final FileDescriptor fd, final String[] args) {
        FileOutputStream fout = new FileOutputStream(fd);
        final PrintWriter pw = new FastPrintWriter(fout);
        Thread thr = new Thread("Binder.dumpAsync") { // from class: android.os.Binder.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    Binder.this.dump(fd, pw, args);
                } finally {
                    pw.flush();
                }
            }
        };
        thr.start();
    }

    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
    }

    @Override // android.os.IBinder
    public void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        onShellCommand(in, out, err, args, callback, resultReceiver);
    }

    public void onShellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        ParcelFileDescriptor errPfd;
        int callingUid = getCallingUid();
        int result = -1;
        if (callingUid != 0 && callingUid != 2000) {
            resultReceiver.send(result, null);
            throw new SecurityException("Shell commands are only callable by ADB");
        }
        if (in == null) {
            try {
                in = new FileInputStream("/dev/null").getFD();
            } catch (IOException e) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(err != null ? err : out));
                pw.println("Failed to open /dev/null: " + e.getMessage());
                pw.flush();
                return;
            }
        }
        if (out == null) {
            out = new FileOutputStream("/dev/null").getFD();
        }
        if (err == null) {
            err = out;
        }
        if (args == null) {
            args = new String[0];
        }
        result = -1;
        try {
            try {
                errPfd = ParcelFileDescriptor.dup(in);
            } catch (IOException e2) {
                PrintWriter pw2 = new FastPrintWriter(new FileOutputStream(err));
                pw2.println("dup() failed: " + e2.getMessage());
                pw2.flush();
            }
            try {
                ParcelFileDescriptor outPfd = ParcelFileDescriptor.dup(out);
                errPfd = ParcelFileDescriptor.dup(err);
                try {
                    result = handleShellCommand(errPfd, outPfd, errPfd, args);
                    if (errPfd != null) {
                        errPfd.close();
                    }
                    if (outPfd != null) {
                        outPfd.close();
                    }
                    if (errPfd != null) {
                        errPfd.close();
                    }
                } finally {
                }
            } finally {
            }
        } finally {
            resultReceiver.send(-1, null);
        }
    }

    @SystemApi
    public int handleShellCommand(ParcelFileDescriptor in, ParcelFileDescriptor out, ParcelFileDescriptor err, String[] args) {
        FileOutputStream ferr = new FileOutputStream(err.getFileDescriptor());
        PrintWriter pw = new FastPrintWriter(ferr);
        pw.println("No shell command implementation.");
        pw.flush();
        return 0;
    }

    @Override // android.os.IBinder
    public final boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (data != null) {
            data.setDataPosition(0);
        }
        boolean r = onTransact(code, data, reply, flags);
        if (reply != null) {
            reply.setDataPosition(0);
        }
        return r;
    }

    @Override // android.os.IBinder
    public void linkToDeath(IBinder.DeathRecipient recipient, int flags) {
    }

    @Override // android.os.IBinder
    public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkParcel(IBinder obj, int code, Parcel parcel, String msg) {
    }

    public static void setWorkSourceProvider(BinderInternal.WorkSourceProvider workSourceProvider) {
        if (workSourceProvider == null) {
            throw new IllegalArgumentException("workSourceProvider cannot be null");
        }
        sWorkSourceProvider = workSourceProvider;
    }

    private boolean execTransact(int code, long dataObj, long replyObj, int flags) {
        int callingUid = getCallingUid();
        long origWorkSource = ThreadLocalWorkSource.setUid(callingUid);
        try {
            return execTransactInternal(code, dataObj, replyObj, flags, callingUid);
        } finally {
            ThreadLocalWorkSource.restore(origWorkSource);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x007f, code lost:
        if (r4 != null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0081, code lost:
        r9 = android.os.Binder.sWorkSourceProvider.resolveWorkSourceUid(r6.readCallingWorkSourceUid());
        r4.callEnded(r5, r6.dataSize(), r7.dataSize(), r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d3, code lost:
        checkParcel(r15, r16, r7, "Unreasonably large binder reply buffer");
        r7.recycle();
        r6.recycle();
        android.os.StrictMode.clearGatheredViolations();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00e1, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d0, code lost:
        if (r4 != null) goto L27;
     */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00ea  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean execTransactInternal(int code, long dataObj, long replyObj, int flags, int callingUid) {
        boolean res;
        BinderInternal.Observer observer = sObserver;
        BinderInternal.CallSession callSession = observer != null ? observer.callStarted(this, code, -1) : null;
        Parcel data = Parcel.obtain(dataObj);
        Parcel reply = Parcel.obtain(replyObj);
        boolean tracingEnabled = isTracingEnabled();
        try {
            try {
                BinderCallHeavyHitterWatcher heavyHitterWatcher = sHeavyHitterWatcher;
                if (heavyHitterWatcher != null) {
                    try {
                        heavyHitterWatcher.onTransaction(callingUid, getClass(), code);
                    } catch (RemoteException | RuntimeException e) {
                        e = e;
                        if (observer != null) {
                            observer.callThrewException(callSession, e);
                        }
                        if (LOG_RUNTIME_EXCEPTION) {
                            Log.w(TAG, "Caught a RuntimeException from the binder stub implementation.", e);
                        }
                        if ((flags & 1) == 0) {
                            reply.setDataSize(0);
                            reply.setDataPosition(0);
                            reply.writeException(e);
                        } else if (e instanceof RemoteException) {
                            Log.w(TAG, "Binder call failed.", e);
                        } else {
                            Log.w(TAG, "Caught a RuntimeException from the binder stub implementation.", e);
                        }
                        res = true;
                        if (tracingEnabled) {
                            Trace.traceEnd(1L);
                        }
                    }
                }
                if (tracingEnabled) {
                    String transactionName = getTransactionName(code);
                    StringBuilder sb = new StringBuilder();
                    sb.append(getClass().getName());
                    sb.append(SettingsStringUtil.DELIMITER);
                    sb.append(transactionName != null ? transactionName : Integer.valueOf(code));
                    Trace.traceBegin(1L, sb.toString());
                }
                if ((flags & 2) != 0) {
                    AppOpsManager.startNotedAppOpsCollection(callingUid);
                    try {
                        res = onTransact(code, data, reply, flags);
                        AppOpsManager.finishNotedAppOpsCollection();
                    } catch (Throwable th) {
                        AppOpsManager.finishNotedAppOpsCollection();
                        throw th;
                    }
                } else {
                    res = onTransact(code, data, reply, flags);
                }
                if (tracingEnabled) {
                    Trace.traceEnd(1L);
                }
            } catch (Throwable th2) {
                th = th2;
                if (tracingEnabled) {
                    Trace.traceEnd(1L);
                }
                if (observer != null) {
                    int workSourceUid = sWorkSourceProvider.resolveWorkSourceUid(data.readCallingWorkSourceUid());
                    observer.callEnded(callSession, data.dataSize(), reply.dataSize(), workSourceUid);
                }
                throw th;
            }
        } catch (RemoteException | RuntimeException e2) {
            e = e2;
        } catch (Throwable th3) {
            th = th3;
            if (tracingEnabled) {
            }
            if (observer != null) {
            }
            throw th;
        }
    }

    public static synchronized void setHeavyHitterWatcherConfig(boolean enabled, int batchSize, float threshold, BinderCallHeavyHitterWatcher.BinderCallHeavyHitterListener listener) {
        synchronized (Binder.class) {
            Slog.i(TAG, "Setting heavy hitter watcher config: " + enabled + ", " + batchSize + ", " + threshold);
            BinderCallHeavyHitterWatcher watcher = sHeavyHitterWatcher;
            if (enabled) {
                if (listener == null) {
                    throw new IllegalArgumentException();
                }
                boolean newWatcher = false;
                if (watcher == null) {
                    watcher = BinderCallHeavyHitterWatcher.getInstance();
                    newWatcher = true;
                }
                watcher.setConfig(true, batchSize, threshold, listener);
                if (newWatcher) {
                    sHeavyHitterWatcher = watcher;
                }
            } else if (watcher != null) {
                watcher.setConfig(false, 0, 0.0f, null);
            }
        }
    }
}
