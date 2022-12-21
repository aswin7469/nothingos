package com.android.permission.jarjar.com.android.internal.infra;

import android.net.nsd.NsdManager$$ExternalSyntheticLambda0;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.EventLog;
import android.util.Log;
import com.android.permission.jarjar.com.android.internal.infra.IAndroidFuture;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AndroidFuture<T> extends CompletableFuture<T> implements Parcelable {
    public static final Parcelable.Creator<AndroidFuture> CREATOR = new Parcelable.Creator<AndroidFuture>() {
        public AndroidFuture createFromParcel(Parcel parcel) {
            return new AndroidFuture(parcel);
        }

        public AndroidFuture[] newArray(int i) {
            return new AndroidFuture[i];
        }
    };
    private static final boolean DEBUG = false;
    private static final Executor DIRECT_EXECUTOR = new NsdManager$$ExternalSyntheticLambda0();
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    /* access modifiers changed from: private */
    public static final String LOG_TAG = "AndroidFuture";
    private static Handler sMainHandler;
    private BiConsumer<? super T, ? super Throwable> mListener;
    private Executor mListenerExecutor;
    private final Object mLock;
    private final IAndroidFuture mRemoteOrigin;
    private Handler mTimeoutHandler;

    static /* synthetic */ Object lambda$thenCombine$2(Object obj, Void voidR) {
        return obj;
    }

    public int describeContents() {
        return 0;
    }

    public AndroidFuture() {
        this.mLock = new Object();
        this.mListenerExecutor = DIRECT_EXECUTOR;
        this.mTimeoutHandler = getMainHandler();
        this.mRemoteOrigin = null;
    }

    AndroidFuture(Parcel parcel) {
        this.mLock = new Object();
        this.mListenerExecutor = DIRECT_EXECUTOR;
        this.mTimeoutHandler = getMainHandler();
        if (parcel.readBoolean()) {
            if (parcel.readBoolean()) {
                completeExceptionally(readThrowable(parcel));
            } else {
                complete(parcel.readValue((ClassLoader) null));
            }
            this.mRemoteOrigin = null;
            return;
        }
        this.mRemoteOrigin = IAndroidFuture.Stub.asInterface(parcel.readStrongBinder());
    }

    private static Handler getMainHandler() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
        return sMainHandler;
    }

    public static <U> AndroidFuture<U> completedFuture(U u) {
        AndroidFuture<U> androidFuture = new AndroidFuture<>();
        androidFuture.complete(u);
        return androidFuture;
    }

    public boolean complete(T t) {
        boolean complete = super.complete(t);
        if (complete) {
            onCompleted(t, (Throwable) null);
        }
        return complete;
    }

    public boolean completeExceptionally(Throwable th) {
        boolean completeExceptionally = super.completeExceptionally(th);
        if (completeExceptionally) {
            onCompleted((Object) null, th);
        }
        return completeExceptionally;
    }

    public boolean cancel(boolean z) {
        boolean cancel = super.cancel(z);
        if (cancel) {
            try {
                get();
                throw new IllegalStateException("Expected CancellationException");
            } catch (CancellationException e) {
                onCompleted((Object) null, e);
            } catch (Throwable th) {
                throw new IllegalStateException("Expected CancellationException", th);
            }
        }
        return cancel;
    }

    /* access modifiers changed from: protected */
    public void onCompleted(T t, Throwable th) {
        BiConsumer<? super T, ? super Throwable> biConsumer;
        cancelTimeout();
        synchronized (this.mLock) {
            biConsumer = this.mListener;
            this.mListener = null;
        }
        if (biConsumer != null) {
            callListenerAsync(biConsumer, t, th);
        }
        IAndroidFuture iAndroidFuture = this.mRemoteOrigin;
        if (iAndroidFuture != null) {
            try {
                iAndroidFuture.complete(this);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Failed to propagate completion", e);
            }
        }
    }

    public AndroidFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> biConsumer) {
        return whenCompleteAsync(biConsumer, DIRECT_EXECUTOR);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002c, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0031, code lost:
        r0 = null;
        r6 = get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0035, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0037, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0038, code lost:
        r0 = r0.getCause();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.permission.jarjar.com.android.internal.infra.AndroidFuture<T> whenCompleteAsync(java.util.function.BiConsumer<? super T, ? super java.lang.Throwable> r5, java.util.concurrent.Executor r6) {
        /*
            r4 = this;
            com.android.permission.jarjar.com.android.internal.util.Preconditions.checkNotNull(r5)
            com.android.permission.jarjar.com.android.internal.util.Preconditions.checkNotNull(r6)
            java.lang.Object r0 = r4.mLock
            monitor-enter(r0)
            boolean r1 = r4.isDone()     // Catch:{ all -> 0x0040 }
            if (r1 != 0) goto L_0x002b
            java.util.function.BiConsumer<? super T, ? super java.lang.Throwable> r1 = r4.mListener     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x001c
            java.util.concurrent.Executor r2 = r4.mListenerExecutor     // Catch:{ all -> 0x0040 }
            if (r6 == r2) goto L_0x001c
            super.whenCompleteAsync(r5, (java.util.concurrent.Executor) r6)     // Catch:{ all -> 0x0040 }
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return r4
        L_0x001c:
            r4.mListenerExecutor = r6     // Catch:{ all -> 0x0040 }
            if (r1 != 0) goto L_0x0021
            goto L_0x0027
        L_0x0021:
            com.android.permission.jarjar.com.android.internal.infra.AndroidFuture$$ExternalSyntheticLambda0 r6 = new com.android.permission.jarjar.com.android.internal.infra.AndroidFuture$$ExternalSyntheticLambda0     // Catch:{ all -> 0x0040 }
            r6.<init>(r1, r5)     // Catch:{ all -> 0x0040 }
            r5 = r6
        L_0x0027:
            r4.mListener = r5     // Catch:{ all -> 0x0040 }
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return r4
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            r6 = 0
            java.lang.Object r0 = r4.get()     // Catch:{ ExecutionException -> 0x0037, all -> 0x0035 }
            r3 = r0
            r0 = r6
            r6 = r3
            goto L_0x003c
        L_0x0035:
            r0 = move-exception
            goto L_0x003c
        L_0x0037:
            r0 = move-exception
            java.lang.Throwable r0 = r0.getCause()
        L_0x003c:
            r4.callListenerAsync(r5, r6, r0)
            return r4
        L_0x0040:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.permission.jarjar.com.android.internal.infra.AndroidFuture.whenCompleteAsync(java.util.function.BiConsumer, java.util.concurrent.Executor):com.android.permission.jarjar.com.android.internal.infra.AndroidFuture");
    }

    static /* synthetic */ void lambda$whenCompleteAsync$0(BiConsumer biConsumer, BiConsumer biConsumer2, Object obj, Throwable th) {
        callListener(biConsumer, obj, th);
        callListener(biConsumer2, obj, th);
    }

    private void callListenerAsync(BiConsumer<? super T, ? super Throwable> biConsumer, T t, Throwable th) {
        Executor executor = this.mListenerExecutor;
        if (executor == DIRECT_EXECUTOR) {
            callListener(biConsumer, t, th);
        } else {
            executor.execute(new AndroidFuture$$ExternalSyntheticLambda3(biConsumer, t, th));
        }
    }

    /* access modifiers changed from: package-private */
    public static <TT> void callListener(BiConsumer<? super TT, ? super Throwable> biConsumer, TT tt, Throwable th) {
        try {
            biConsumer.accept(tt, th);
        } catch (Throwable th2) {
            String str = LOG_TAG;
            Log.e(str, "Failed to call whenComplete listener. res = " + tt, th2);
        }
    }

    public AndroidFuture<T> orTimeout(long j, TimeUnit timeUnit) {
        this.mTimeoutHandler.postDelayed(new AndroidFuture$$ExternalSyntheticLambda1(this), this, timeUnit.toMillis(j));
        return this;
    }

    /* access modifiers changed from: package-private */
    public void triggerTimeout() {
        cancelTimeout();
        if (!isDone()) {
            completeExceptionally(new TimeoutException());
        }
    }

    public AndroidFuture<T> cancelTimeout() {
        this.mTimeoutHandler.removeCallbacksAndMessages(this);
        return this;
    }

    public AndroidFuture<T> setTimeoutHandler(Handler handler) {
        cancelTimeout();
        this.mTimeoutHandler = (Handler) Preconditions.checkNotNull(handler);
        return this;
    }

    public <U> AndroidFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> function) {
        return thenComposeAsync(function, DIRECT_EXECUTOR);
    }

    public <U> AndroidFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> function, Executor executor) {
        return new ThenComposeAsync(this, function, executor);
    }

    private static class ThenComposeAsync<T, U> extends AndroidFuture<U> implements BiConsumer<Object, Throwable>, Runnable {
        private final Executor mExecutor;
        private volatile Function<? super T, ? extends CompletionStage<U>> mFn;
        private volatile T mSourceResult = null;

        ThenComposeAsync(AndroidFuture<T> androidFuture, Function<? super T, ? extends CompletionStage<U>> function, Executor executor) {
            this.mFn = (Function) Preconditions.checkNotNull(function);
            this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
            androidFuture.whenComplete((BiConsumer<? super T, ? super Throwable>) this);
        }

        public void accept(Object obj, Throwable th) {
            if (th != null) {
                completeExceptionally(th);
            } else if (this.mFn != null) {
                this.mSourceResult = obj;
                this.mExecutor.execute(this);
            } else {
                complete(obj);
            }
        }

        public void run() {
            try {
                CompletionStage completionStage = (CompletionStage) Preconditions.checkNotNull((CompletionStage) this.mFn.apply(this.mSourceResult));
                this.mFn = null;
                completionStage.whenComplete(this);
            } catch (Throwable th) {
                this.mFn = null;
                throw th;
            }
        }
    }

    public <U> AndroidFuture<U> thenApply(Function<? super T, ? extends U> function) {
        return thenApplyAsync(function, DIRECT_EXECUTOR);
    }

    public <U> AndroidFuture<U> thenApplyAsync(Function<? super T, ? extends U> function, Executor executor) {
        return new ThenApplyAsync(this, function, executor);
    }

    private static class ThenApplyAsync<T, U> extends AndroidFuture<U> implements BiConsumer<T, Throwable>, Runnable {
        private final Executor mExecutor;
        private final Function<? super T, ? extends U> mFn;
        private volatile T mSourceResult = null;

        ThenApplyAsync(AndroidFuture<T> androidFuture, Function<? super T, ? extends U> function, Executor executor) {
            this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
            this.mFn = (Function) Preconditions.checkNotNull(function);
            androidFuture.whenComplete((BiConsumer<? super T, ? super Throwable>) this);
        }

        public void accept(T t, Throwable th) {
            if (th != null) {
                completeExceptionally(th);
                return;
            }
            this.mSourceResult = t;
            this.mExecutor.execute(this);
        }

        public void run() {
            try {
                complete(this.mFn.apply(this.mSourceResult));
            } catch (Throwable th) {
                completeExceptionally(th);
            }
        }
    }

    public <U, V> AndroidFuture<V> thenCombine(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return new ThenCombine(this, completionStage, biFunction);
    }

    public AndroidFuture<T> thenCombine(CompletionStage<Void> completionStage) {
        return thenCombine(completionStage, new AndroidFuture$$ExternalSyntheticLambda2());
    }

    private static class ThenCombine<T, U, V> extends AndroidFuture<V> implements BiConsumer<Object, Throwable> {
        private final BiFunction<? super T, ? super U, ? extends V> mCombineResults;
        private volatile T mResultT = null;
        private volatile CompletionStage<? extends U> mSourceU;

        ThenCombine(CompletableFuture<T> completableFuture, CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
            this.mSourceU = (CompletionStage) Preconditions.checkNotNull(completionStage);
            this.mCombineResults = (BiFunction) Preconditions.checkNotNull(biFunction);
            completableFuture.whenComplete((BiConsumer<? super T, ? super Throwable>) this);
        }

        public void accept(Object obj, Throwable th) {
            if (th != null) {
                completeExceptionally(th);
            } else if (this.mSourceU != null) {
                this.mResultT = obj;
                this.mSourceU.whenComplete(new AndroidFuture$ThenCombine$$ExternalSyntheticLambda0(this));
            } else {
                try {
                    complete(this.mCombineResults.apply(this.mResultT, obj));
                } catch (Throwable th2) {
                    completeExceptionally(th2);
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$accept$0$com-android-permission-jarjar-com-android-internal-infra-AndroidFuture$ThenCombine */
        public /* synthetic */ void mo27776x5f255859(Object obj, Throwable th) {
            this.mSourceU = null;
            accept(obj, th);
        }
    }

    public static <T> AndroidFuture<T> supply(Supplier<T> supplier) {
        return supplyAsync(supplier, DIRECT_EXECUTOR);
    }

    public static <T> AndroidFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        return new SupplyAsync(supplier, executor);
    }

    private static class SupplyAsync<T> extends AndroidFuture<T> implements Runnable {
        private final Supplier<T> mSupplier;

        SupplyAsync(Supplier<T> supplier, Executor executor) {
            this.mSupplier = supplier;
            executor.execute(this);
        }

        public void run() {
            try {
                complete(this.mSupplier.get());
            } catch (Throwable th) {
                completeExceptionally(th);
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        boolean isDone = isDone();
        parcel.writeBoolean(isDone);
        if (isDone) {
            try {
                Object obj = get();
                parcel.writeBoolean(false);
                parcel.writeValue(obj);
            } catch (Throwable th) {
                parcel.writeBoolean(true);
                writeThrowable(parcel, unwrapExecutionException(th));
            }
        } else {
            parcel.writeStrongBinder(new IAndroidFuture.Stub() {
                public void complete(AndroidFuture androidFuture) {
                    boolean z;
                    try {
                        z = AndroidFuture.this.complete(androidFuture.get());
                    } catch (Throwable th) {
                        AndroidFuture androidFuture2 = AndroidFuture.this;
                        z = androidFuture2.completeExceptionally(androidFuture2.unwrapExecutionException(th));
                    }
                    if (!z) {
                        String r0 = AndroidFuture.LOG_TAG;
                        Log.w(r0, "Remote result " + androidFuture + " ignored, as local future is already completed: " + AndroidFuture.this);
                    }
                }
            }.asBinder());
        }
    }

    /* access modifiers changed from: package-private */
    public Throwable unwrapExecutionException(Throwable th) {
        return th instanceof ExecutionException ? th.getCause() : th;
    }

    private static void writeThrowable(Parcel parcel, Throwable th) {
        boolean z = th != null;
        parcel.writeBoolean(z);
        if (z) {
            boolean z2 = (th instanceof Parcelable) && th.getClass().getClassLoader() == Parcelable.class.getClassLoader();
            parcel.writeBoolean(z2);
            if (z2) {
                parcel.writeParcelable((Parcelable) th, 1);
                return;
            }
            parcel.writeString(th.getClass().getName());
            parcel.writeString(th.getMessage());
            StackTraceElement[] stackTrace = th.getStackTrace();
            StringBuilder sb = new StringBuilder();
            int min = Math.min(stackTrace != null ? stackTrace.length : 0, 5);
            for (int i = 0; i < min; i++) {
                if (i > 0) {
                    sb.append(10);
                }
                sb.append("\tat ");
                sb.append((Object) stackTrace[i]);
            }
            parcel.writeString(sb.toString());
            writeThrowable(parcel, th.getCause());
        }
    }

    private static Throwable readThrowable(Parcel parcel) {
        Throwable th;
        if (!parcel.readBoolean()) {
            return null;
        }
        if (parcel.readBoolean()) {
            return (Throwable) parcel.readParcelable(Parcelable.class.getClassLoader());
        }
        String readString = parcel.readString();
        String str = parcel.readString() + 10 + parcel.readString();
        try {
            Class<?> cls = Class.forName(readString, true, Parcelable.class.getClassLoader());
            if (Throwable.class.isAssignableFrom(cls)) {
                th = (Throwable) cls.getConstructor(String.class).newInstance(str);
            } else {
                EventLog.writeEvent(1397638484, new Object[]{"186530450", -1, ""});
                th = new RuntimeException(readString + ": " + str);
            }
        } catch (Throwable th2) {
            RuntimeException runtimeException = new RuntimeException(readString + ": " + str);
            runtimeException.addSuppressed(th2);
            th = runtimeException;
        }
        th.setStackTrace(EMPTY_STACK_TRACE);
        Throwable readThrowable = readThrowable(parcel);
        if (readThrowable != null) {
            th.initCause(readThrowable);
        }
        return th;
    }
}
