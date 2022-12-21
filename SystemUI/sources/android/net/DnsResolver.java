package android.net;

import android.net.connectivity.com.android.net.module.util.DnsPacket;
import android.net.util.DnsUtils;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.MessageQueue;
import android.system.ErrnoException;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.p026io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public final class DnsResolver {
    public static final int CLASS_IN = 1;
    public static final int ERROR_PARSE = 0;
    public static final int ERROR_SYSTEM = 1;
    private static final int FD_EVENTS = 5;
    public static final int FLAG_EMPTY = 0;
    public static final int FLAG_NO_CACHE_LOOKUP = 4;
    public static final int FLAG_NO_CACHE_STORE = 2;
    public static final int FLAG_NO_RETRY = 1;
    private static final int MAXPACKET = 8192;
    private static final int NETID_UNSET = 0;
    private static final int SLEEP_TIME_MS = 2;
    private static final String TAG = "DnsResolver";
    public static final int TYPE_A = 1;
    public static final int TYPE_AAAA = 28;
    private static final DnsResolver sInstance = new DnsResolver();

    public interface Callback<T> {
        void onAnswer(T t, int i);

        void onError(DnsException dnsException);
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface DnsError {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryType {
    }

    public static DnsResolver getInstance() {
        return sInstance;
    }

    private DnsResolver() {
    }

    public static class DnsException extends Exception {
        public final int code;

        public DnsException(int i, Throwable th) {
            super(th);
            this.code = i;
        }
    }

    public void rawQuery(Network network, byte[] bArr, int i, Executor executor, CancellationSignal cancellationSignal, Callback<? super byte[]> callback) {
        int i2;
        if (cancellationSignal == null || !cancellationSignal.isCanceled()) {
            Object obj = new Object();
            if (network != null) {
                try {
                    i2 = network.getNetIdForResolv();
                } catch (ErrnoException e) {
                    executor.execute(new DnsResolver$$ExternalSyntheticLambda3(callback, e));
                    return;
                }
            } else {
                i2 = 0;
            }
            FileDescriptor resNetworkSend = NetworkUtils.resNetworkSend(i2, bArr, bArr.length, i);
            synchronized (obj) {
                registerFDListener(executor, resNetworkSend, callback, cancellationSignal, obj);
                if (cancellationSignal != null) {
                    addCancellationSignal(cancellationSignal, resNetworkSend, obj);
                }
            }
        }
    }

    public void rawQuery(Network network, String str, int i, int i2, int i3, Executor executor, CancellationSignal cancellationSignal, Callback<? super byte[]> callback) {
        int i4;
        if (cancellationSignal == null || !cancellationSignal.isCanceled()) {
            Object obj = new Object();
            if (network != null) {
                try {
                    i4 = network.getNetIdForResolv();
                } catch (ErrnoException e) {
                    executor.execute(new DnsResolver$$ExternalSyntheticLambda1(callback, e));
                    return;
                }
            } else {
                i4 = 0;
            }
            FileDescriptor resNetworkQuery = NetworkUtils.resNetworkQuery(i4, str, i, i2, i3);
            synchronized (obj) {
                registerFDListener(executor, resNetworkQuery, callback, cancellationSignal, obj);
                if (cancellationSignal != null) {
                    addCancellationSignal(cancellationSignal, resNetworkQuery, obj);
                }
            }
        }
    }

    private class InetAddressAnswerAccumulator implements Callback<byte[]> {
        private final List<InetAddress> mAllAnswers;
        private DnsException mDnsException;
        private final Network mNetwork;
        private int mRcode;
        private int mReceivedAnswerCount = 0;
        private final int mTargetAnswerCount;
        private final Callback<? super List<InetAddress>> mUserCallback;

        InetAddressAnswerAccumulator(Network network, int i, Callback<? super List<InetAddress>> callback) {
            this.mNetwork = network;
            this.mTargetAnswerCount = i;
            this.mAllAnswers = new ArrayList();
            this.mUserCallback = callback;
        }

        private boolean maybeReportError() {
            int i = this.mRcode;
            if (i != 0) {
                this.mUserCallback.onAnswer(this.mAllAnswers, i);
                return true;
            }
            DnsException dnsException = this.mDnsException;
            if (dnsException == null) {
                return false;
            }
            this.mUserCallback.onError(dnsException);
            return true;
        }

        private void maybeReportAnswer() {
            int i = this.mReceivedAnswerCount + 1;
            this.mReceivedAnswerCount = i;
            if (i == this.mTargetAnswerCount) {
                if (!this.mAllAnswers.isEmpty() || !maybeReportError()) {
                    this.mUserCallback.onAnswer(DnsUtils.rfc6724Sort(this.mNetwork, this.mAllAnswers), this.mRcode);
                }
            }
        }

        public void onAnswer(byte[] bArr, int i) {
            if (this.mReceivedAnswerCount == 0 || i == 0) {
                this.mRcode = i;
            }
            try {
                this.mAllAnswers.addAll(new DnsAddressAnswer(bArr).getAddresses());
            } catch (DnsPacket.ParseException e) {
                ParseException parseException = new ParseException(e.reason, e.getCause());
                parseException.setStackTrace(e.getStackTrace());
                this.mDnsException = new DnsException(0, parseException);
            }
            maybeReportAnswer();
        }

        public void onError(DnsException dnsException) {
            this.mDnsException = dnsException;
            maybeReportAnswer();
        }
    }

    public void query(Network network, String str, int i, Executor executor, CancellationSignal cancellationSignal, Callback<? super List<InetAddress>> callback) {
        Network network2;
        FileDescriptor fileDescriptor;
        int i2;
        FileDescriptor fileDescriptor2;
        String str2 = str;
        int i3 = i;
        Executor executor2 = executor;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        Callback<? super List<InetAddress>> callback2 = callback;
        if (cancellationSignal2 == null || !cancellationSignal.isCanceled()) {
            Object obj = new Object();
            if (network != null) {
                network2 = network;
            } else {
                try {
                    network2 = NetworkUtils.getDnsNetwork();
                } catch (ErrnoException e) {
                    executor2.execute(new DnsResolver$$ExternalSyntheticLambda5(callback2, e));
                    return;
                }
            }
            boolean haveIpv6 = DnsUtils.haveIpv6(network2);
            boolean haveIpv4 = DnsUtils.haveIpv4(network2);
            if (haveIpv6 || haveIpv4) {
                if (haveIpv6) {
                    try {
                        fileDescriptor = NetworkUtils.resNetworkQuery(network2.getNetIdForResolv(), str2, 1, 28, i3);
                        i2 = 1;
                    } catch (ErrnoException e2) {
                        executor2.execute(new DnsResolver$$ExternalSyntheticLambda7(callback2, e2));
                        return;
                    }
                } else {
                    i2 = 0;
                    fileDescriptor = null;
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
                if (haveIpv4) {
                    try {
                        i2++;
                        fileDescriptor2 = NetworkUtils.resNetworkQuery(network2.getNetIdForResolv(), str2, 1, 1, i3);
                    } catch (ErrnoException e3) {
                        if (haveIpv6) {
                            NetworkUtils.resNetworkCancel(fileDescriptor);
                        }
                        executor2.execute(new DnsResolver$$ExternalSyntheticLambda8(callback2, e3));
                        return;
                    }
                } else {
                    fileDescriptor2 = null;
                }
                InetAddressAnswerAccumulator inetAddressAnswerAccumulator = new InetAddressAnswerAccumulator(network2, i2, callback2);
                synchronized (obj) {
                    if (haveIpv6) {
                        try {
                            registerFDListener(executor, fileDescriptor, inetAddressAnswerAccumulator, cancellationSignal, obj);
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    if (haveIpv4) {
                        registerFDListener(executor, fileDescriptor2, inetAddressAnswerAccumulator, cancellationSignal, obj);
                    }
                    if (cancellationSignal2 != null) {
                        cancellationSignal2.setOnCancelListener(new DnsResolver$$ExternalSyntheticLambda9(this, obj, haveIpv4, fileDescriptor2, haveIpv6, fileDescriptor));
                        return;
                    }
                    return;
                }
            }
            executor2.execute(new DnsResolver$$ExternalSyntheticLambda6(callback2));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$query$6$android-net-DnsResolver  reason: not valid java name */
    public /* synthetic */ void m1877lambda$query$6$androidnetDnsResolver(Object obj, boolean z, FileDescriptor fileDescriptor, boolean z2, FileDescriptor fileDescriptor2) {
        synchronized (obj) {
            if (z) {
                try {
                    cancelQuery(fileDescriptor);
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z2) {
                cancelQuery(fileDescriptor2);
            }
        }
    }

    public void query(Network network, String str, int i, int i2, Executor executor, CancellationSignal cancellationSignal, Callback<? super List<InetAddress>> callback) {
        if (cancellationSignal == null || !cancellationSignal.isCanceled()) {
            Object obj = new Object();
            if (network == null) {
                try {
                    network = NetworkUtils.getDnsNetwork();
                } catch (ErrnoException e) {
                    executor.execute(new DnsResolver$$ExternalSyntheticLambda0(callback, e));
                    return;
                }
            }
            FileDescriptor resNetworkQuery = NetworkUtils.resNetworkQuery(network.getNetIdForResolv(), str, 1, i, i2);
            InetAddressAnswerAccumulator inetAddressAnswerAccumulator = new InetAddressAnswerAccumulator(network, 1, callback);
            synchronized (obj) {
                registerFDListener(executor, resNetworkQuery, inetAddressAnswerAccumulator, cancellationSignal, obj);
                if (cancellationSignal != null) {
                    addCancellationSignal(cancellationSignal, resNetworkQuery, obj);
                }
            }
        }
    }

    public static final class DnsResponse {
        public final byte[] answerbuf;
        public final int rcode;

        public DnsResponse(byte[] bArr, int i) {
            this.answerbuf = bArr;
            this.rcode = i;
        }
    }

    private void registerFDListener(Executor executor, FileDescriptor fileDescriptor, Callback<? super byte[]> callback, CancellationSignal cancellationSignal, Object obj) {
        MessageQueue queue = Looper.getMainLooper().getQueue();
        queue.addOnFileDescriptorEventListener(fileDescriptor, 5, new DnsResolver$$ExternalSyntheticLambda4(queue, executor, obj, cancellationSignal, callback));
    }

    static /* synthetic */ int lambda$registerFDListener$9(MessageQueue messageQueue, Executor executor, Object obj, CancellationSignal cancellationSignal, Callback callback, FileDescriptor fileDescriptor, int i) {
        messageQueue.removeOnFileDescriptorEventListener(fileDescriptor);
        executor.execute(new DnsResolver$$ExternalSyntheticLambda10(obj, cancellationSignal, fileDescriptor, callback));
        return 0;
    }

    static /* synthetic */ void lambda$registerFDListener$8(Object obj, CancellationSignal cancellationSignal, FileDescriptor fileDescriptor, Callback callback) {
        ErrnoException errnoException;
        DnsResponse dnsResponse;
        synchronized (obj) {
            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled()) {
                    return;
                }
            }
            errnoException = null;
            try {
                dnsResponse = NetworkUtils.resNetworkResult(fileDescriptor);
            } catch (ErrnoException e) {
                Log.w(TAG, "resNetworkResult:" + e.toString());
                ErrnoException errnoException2 = e;
                dnsResponse = null;
                errnoException = errnoException2;
            }
        }
        if (errnoException != null) {
            callback.onError(new DnsException(1, errnoException));
        } else {
            callback.onAnswer(dnsResponse.answerbuf, dnsResponse.rcode);
        }
    }

    private void cancelQuery(FileDescriptor fileDescriptor) {
        if (fileDescriptor.valid()) {
            Looper.getMainLooper().getQueue().removeOnFileDescriptorEventListener(fileDescriptor);
            NetworkUtils.resNetworkCancel(fileDescriptor);
        }
    }

    private void addCancellationSignal(CancellationSignal cancellationSignal, FileDescriptor fileDescriptor, Object obj) {
        cancellationSignal.setOnCancelListener(new DnsResolver$$ExternalSyntheticLambda2(this, obj, fileDescriptor));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCancellationSignal$10$android-net-DnsResolver  reason: not valid java name */
    public /* synthetic */ void m1876lambda$addCancellationSignal$10$androidnetDnsResolver(Object obj, FileDescriptor fileDescriptor) {
        synchronized (obj) {
            cancelQuery(fileDescriptor);
        }
    }

    private static class DnsAddressAnswer extends DnsPacket {
        private static final boolean DBG = false;
        private static final String TAG = "DnsResolver.DnsAddressAnswer";
        private final int mQueryType;

        DnsAddressAnswer(byte[] bArr) throws DnsPacket.ParseException {
            super(bArr);
            if ((this.mHeader.flags & 32768) == 0) {
                throw new DnsPacket.ParseException("Not an answer packet");
            } else if (this.mHeader.getRecordCount(0) != 0) {
                this.mQueryType = ((DnsPacket.DnsRecord) this.mRecords[0].get(0)).nsType;
            } else {
                throw new DnsPacket.ParseException("No question found");
            }
        }

        public List<InetAddress> getAddresses() {
            ArrayList arrayList = new ArrayList();
            if (this.mHeader.getRecordCount(1) == 0) {
                return arrayList;
            }
            for (DnsPacket.DnsRecord dnsRecord : this.mRecords[1]) {
                int i = dnsRecord.nsType;
                if (i == this.mQueryType && (i == 1 || i == 28)) {
                    try {
                        arrayList.add(InetAddress.getByAddress(dnsRecord.getRR()));
                    } catch (UnknownHostException unused) {
                    }
                }
            }
            return arrayList;
        }
    }
}
