package com.android.permission.jarjar.com.android.internal.infra;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import com.android.permission.jarjar.android.p008os.HandlerExecutor;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ServiceConnector<I extends IInterface> {

    @FunctionalInterface
    public interface Job<II, R> {
        R run(II ii) throws Exception;
    }

    public interface ServiceLifecycleCallbacks<II extends IInterface> {
        void onBinderDied() {
        }

        void onConnected(II ii) {
        }

        void onDisconnected(II ii) {
        }
    }

    AndroidFuture<I> connect();

    AndroidFuture<Void> post(VoidJob<I> voidJob);

    <R> AndroidFuture<R> postAsync(Job<I, CompletableFuture<R>> job);

    <R> AndroidFuture<R> postForResult(Job<I, R> job);

    boolean run(VoidJob<I> voidJob);

    void setServiceLifecycleCallbacks(ServiceLifecycleCallbacks<I> serviceLifecycleCallbacks);

    void unbind();

    @FunctionalInterface
    public interface VoidJob<II> extends Job<II, Void> {
        void runNoResult(II ii) throws Exception;

        Void run(II ii) throws Exception {
            runNoResult(ii);
            return null;
        }
    }

    public static class Impl<I extends IInterface> extends ArrayDeque<Job<I, ?>> implements ServiceConnector<I>, ServiceConnection, IBinder.DeathRecipient, Runnable {
        static final boolean DEBUG = false;
        private static final long DEFAULT_DISCONNECT_TIMEOUT_MS = 15000;
        private static final long DEFAULT_REQUEST_TIMEOUT_MS = 30000;
        static final String LOG_TAG = "ServiceConnector.Impl";
        private final Function<IBinder, I> mBinderAsInterface;
        private boolean mBinding = false;
        private final int mBindingFlags;
        protected final Context mContext;
        protected final Executor mExecutor;
        private final Handler mHandler;
        private final Intent mIntent;
        private final Handler mMainHandler = new Handler(Looper.getMainLooper());
        /* access modifiers changed from: private */
        public final Queue<Job<I, ?>> mQueue = this;
        private volatile I mService = null;
        private final ServiceConnection mServiceConnection = this;
        private Impl<I>.CompletionAwareJob<I, I> mServiceConnectionFutureCache = null;
        private volatile ServiceLifecycleCallbacks<I> mServiceLifecycleCallbacks = null;
        private final Runnable mTimeoutDisconnect = this;
        private boolean mUnbinding = false;
        /* access modifiers changed from: private */
        public final List<Impl<I>.CompletionAwareJob<I, ?>> mUnfinishedJobs = new ArrayList();

        static /* synthetic */ IInterface lambda$connect$0(IInterface iInterface) throws Exception {
            return iInterface;
        }

        /* access modifiers changed from: protected */
        public long getAutoDisconnectTimeoutMs() {
            return DEFAULT_DISCONNECT_TIMEOUT_MS;
        }

        /* access modifiers changed from: protected */
        public long getRequestTimeoutMs() {
            return 30000;
        }

        /* access modifiers changed from: protected */
        public void onServiceConnectionStatusChanged(I i, boolean z) {
        }

        /* access modifiers changed from: protected */
        public void onServiceUnbound() {
        }

        public Impl(Context context, Intent intent, int i, int i2, Function<IBinder, I> function) {
            this.mContext = context.createContextAsUser(UserHandle.of(i2), 0);
            this.mIntent = intent;
            this.mBindingFlags = i;
            this.mBinderAsInterface = function;
            Handler jobHandler = getJobHandler();
            this.mHandler = jobHandler;
            this.mExecutor = new HandlerExecutor(jobHandler);
        }

        /* access modifiers changed from: protected */
        public Handler getJobHandler() {
            return this.mMainHandler;
        }

        /* access modifiers changed from: protected */
        public boolean bindService(ServiceConnection serviceConnection) {
            return this.mContext.bindService(this.mIntent, this.mBindingFlags | 1, this.mExecutor, serviceConnection);
        }

        /* access modifiers changed from: protected */
        public I binderAsInterface(IBinder iBinder) {
            return (IInterface) this.mBinderAsInterface.apply(iBinder);
        }

        private void dispatchOnServiceConnectionStatusChanged(I i, boolean z) {
            ServiceLifecycleCallbacks<I> serviceLifecycleCallbacks = this.mServiceLifecycleCallbacks;
            if (serviceLifecycleCallbacks != null) {
                if (z) {
                    serviceLifecycleCallbacks.onConnected(i);
                } else {
                    serviceLifecycleCallbacks.onDisconnected(i);
                }
            }
            onServiceConnectionStatusChanged(i, z);
        }

        public boolean run(VoidJob<I> voidJob) {
            return enqueue(voidJob);
        }

        public AndroidFuture<Void> post(VoidJob<I> voidJob) {
            return postForResult((Job) voidJob);
        }

        public <R> Impl<I>.CompletionAwareJob<I, R> postForResult(Job<I, R> job) {
            Impl<I>.CompletionAwareJob<I, R> completionAwareJob = new CompletionAwareJob<>();
            completionAwareJob.mDelegate = (Job) Objects.requireNonNull(job);
            enqueue(completionAwareJob);
            return completionAwareJob;
        }

        public <R> AndroidFuture<R> postAsync(Job<I, CompletableFuture<R>> job) {
            CompletionAwareJob completionAwareJob = new CompletionAwareJob();
            completionAwareJob.mDelegate = (Job) Objects.requireNonNull(job);
            completionAwareJob.mAsync = true;
            enqueue(completionAwareJob);
            return completionAwareJob;
        }

        public synchronized AndroidFuture<I> connect() {
            if (this.mServiceConnectionFutureCache == null) {
                Impl<I>.CompletionAwareJob<I, I> completionAwareJob = new CompletionAwareJob<>();
                this.mServiceConnectionFutureCache = completionAwareJob;
                completionAwareJob.mDelegate = new ServiceConnector$Impl$$ExternalSyntheticLambda2();
                I i = this.mService;
                if (i != null) {
                    this.mServiceConnectionFutureCache.complete(i);
                } else {
                    enqueue(this.mServiceConnectionFutureCache);
                }
            }
            return this.mServiceConnectionFutureCache;
        }

        private void enqueue(Impl<I>.CompletionAwareJob<I, ?> completionAwareJob) {
            if (!enqueue(completionAwareJob)) {
                completionAwareJob.completeExceptionally(new IllegalStateException("Failed to post a job to handler. Likely " + this.mHandler.getLooper() + " is exiting"));
            }
        }

        private boolean enqueue(Job<I, ?> job) {
            cancelTimeout();
            return this.mHandler.post(new ServiceConnector$Impl$$ExternalSyntheticLambda1(this, job));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: enqueueJobThread */
        public void mo27799x8b7bd86e(Job<I, ?> job) {
            cancelTimeout();
            if (this.mUnbinding) {
                completeExceptionally(job, new IllegalStateException("Service is unbinding. Ignoring " + job));
            } else if (!this.mQueue.offer(job)) {
                completeExceptionally(job, new IllegalStateException("Failed to add to queue: " + job));
            } else if (isBound()) {
                processQueue();
            } else if (this.mBinding) {
            } else {
                if (bindService(this.mServiceConnection)) {
                    this.mBinding = true;
                    return;
                }
                completeExceptionally(job, new IllegalStateException("Failed to bind to service " + this.mIntent));
            }
        }

        private void cancelTimeout() {
            this.mMainHandler.removeCallbacks(this.mTimeoutDisconnect);
        }

        /* access modifiers changed from: package-private */
        public void completeExceptionally(Job<?, ?> job, Throwable th) {
            CompletionAwareJob completionAwareJob = (CompletionAwareJob) castOrNull(job, CompletionAwareJob.class);
            if (completionAwareJob != null) {
                completionAwareJob.completeExceptionally(th);
            }
            if (completionAwareJob == null) {
                Log.e(LOG_TAG, "Job failed: " + job, th);
            }
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [T, java.lang.Object, BASE] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static <BASE, T extends BASE> T castOrNull(BASE r0, java.lang.Class<T> r1) {
            /*
                boolean r1 = r1.isInstance(r0)
                if (r1 == 0) goto L_0x0007
                goto L_0x0008
            L_0x0007:
                r0 = 0
            L_0x0008:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.permission.jarjar.com.android.internal.infra.ServiceConnector.Impl.castOrNull(java.lang.Object, java.lang.Class):java.lang.Object");
        }

        private void processQueue() {
            while (true) {
                Job poll = this.mQueue.poll();
                if (poll != null) {
                    CompletionAwareJob completionAwareJob = (CompletionAwareJob) castOrNull(poll, CompletionAwareJob.class);
                    try {
                        I i = this.mService;
                        if (i != null) {
                            Object run = poll.run(i);
                            if (completionAwareJob != null) {
                                if (completionAwareJob.mAsync) {
                                    this.mUnfinishedJobs.add(completionAwareJob);
                                    ((CompletionStage) run).whenComplete(completionAwareJob);
                                } else {
                                    completionAwareJob.complete(run);
                                }
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        completeExceptionally(poll, th);
                    }
                } else {
                    maybeScheduleUnbindTimeout();
                    return;
                }
            }
        }

        /* access modifiers changed from: private */
        public void maybeScheduleUnbindTimeout() {
            if (this.mUnfinishedJobs.isEmpty() && this.mQueue.isEmpty()) {
                scheduleUnbindTimeout();
            }
        }

        private void scheduleUnbindTimeout() {
            long autoDisconnectTimeoutMs = getAutoDisconnectTimeoutMs();
            if (autoDisconnectTimeoutMs > 0) {
                this.mMainHandler.postDelayed(this.mTimeoutDisconnect, autoDisconnectTimeoutMs);
            }
        }

        private boolean isBound() {
            return this.mService != null;
        }

        public void unbind() {
            this.mUnbinding = true;
            this.mHandler.post(new ServiceConnector$Impl$$ExternalSyntheticLambda0(this));
        }

        public void setServiceLifecycleCallbacks(ServiceLifecycleCallbacks<I> serviceLifecycleCallbacks) {
            this.mServiceLifecycleCallbacks = serviceLifecycleCallbacks;
        }

        /* access modifiers changed from: package-private */
        public void unbindJobThread() {
            cancelTimeout();
            I i = this.mService;
            boolean z = i != null;
            if (z || this.mBinding) {
                try {
                    this.mContext.unbindService(this.mServiceConnection);
                } catch (IllegalArgumentException e) {
                    Slog.e(LOG_TAG, "Failed to unbind: " + e);
                }
            }
            if (z) {
                dispatchOnServiceConnectionStatusChanged(i, false);
                i.asBinder().unlinkToDeath(this, 0);
                this.mService = null;
            }
            this.mBinding = false;
            this.mUnbinding = false;
            synchronized (this) {
                Impl<I>.CompletionAwareJob<I, I> completionAwareJob = this.mServiceConnectionFutureCache;
                if (completionAwareJob != null) {
                    completionAwareJob.cancel(true);
                    this.mServiceConnectionFutureCache = null;
                }
            }
            cancelPendingJobs();
            if (z) {
                onServiceUnbound();
            }
        }

        /* access modifiers changed from: protected */
        public void cancelPendingJobs() {
            while (true) {
                Job poll = this.mQueue.poll();
                if (poll != null) {
                    CompletionAwareJob completionAwareJob = (CompletionAwareJob) castOrNull(poll, CompletionAwareJob.class);
                    if (completionAwareJob != null) {
                        completionAwareJob.cancel(false);
                    }
                } else {
                    return;
                }
            }
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (this.mUnbinding) {
                Log.i(LOG_TAG, "Ignoring onServiceConnected due to ongoing unbinding: " + this);
                return;
            }
            I binderAsInterface = binderAsInterface(iBinder);
            this.mService = binderAsInterface;
            this.mBinding = false;
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "onServiceConnected " + componentName + ": ", e);
            }
            dispatchOnServiceConnectionStatusChanged(binderAsInterface, true);
            processQueue();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.mBinding = true;
            I i = this.mService;
            if (i != null) {
                dispatchOnServiceConnectionStatusChanged(i, false);
                this.mService = null;
            }
        }

        public void onBindingDied(ComponentName componentName) {
            binderDied();
        }

        public void binderDied() {
            this.mService = null;
            unbind();
            dispatchOnBinderDied();
        }

        private void dispatchOnBinderDied() {
            ServiceLifecycleCallbacks<I> serviceLifecycleCallbacks = this.mServiceLifecycleCallbacks;
            if (serviceLifecycleCallbacks != null) {
                serviceLifecycleCallbacks.onBinderDied();
            }
        }

        public void run() {
            onTimeout();
        }

        private void onTimeout() {
            unbind();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("ServiceConnector@");
            sb.append(System.identityHashCode(this) % 1000);
            sb.append(NavigationBarInflaterView.KEY_CODE_START);
            sb.append((Object) this.mIntent);
            sb.append(", user: ");
            sb.append(this.mContext.getUser().getIdentifier());
            sb.append(")[");
            sb.append(stateToString());
            if (!this.mQueue.isEmpty()) {
                sb.append(", ");
                sb.append(this.mQueue.size());
                sb.append(" pending job(s)");
            }
            if (!this.mUnfinishedJobs.isEmpty()) {
                sb.append(", ");
                sb.append(this.mUnfinishedJobs.size());
                sb.append(" unfinished async job(s)");
            }
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
            return sb.toString();
        }

        public void dump(String str, PrintWriter printWriter) {
            printWriter.append((CharSequence) str).append((CharSequence) "ServiceConnector:").println();
            printWriter.append((CharSequence) str).append((CharSequence) "  ").append((CharSequence) String.valueOf((Object) this.mIntent)).println();
            printWriter.append((CharSequence) str).append((CharSequence) "  ").append((CharSequence) "userId: ").append((CharSequence) String.valueOf(this.mContext.getUser().getIdentifier())).println();
            printWriter.append((CharSequence) str).append((CharSequence) "  ").append((CharSequence) "State: ").append((CharSequence) stateToString()).println();
            printWriter.append((CharSequence) str).append((CharSequence) "  ").append((CharSequence) "Pending jobs: ").append((CharSequence) String.valueOf(this.mQueue.size())).println();
            printWriter.append((CharSequence) str).append((CharSequence) "  ").append((CharSequence) "Unfinished async jobs: ").append((CharSequence) String.valueOf(this.mUnfinishedJobs.size())).println();
        }

        private String stateToString() {
            if (this.mBinding) {
                return "Binding...";
            }
            if (this.mUnbinding) {
                return "Unbinding...";
            }
            return isBound() ? "Bound" : "Unbound";
        }

        private void logTrace() {
            Log.i(LOG_TAG, "See stacktrace", new Throwable());
        }

        class CompletionAwareJob<II, R> extends AndroidFuture<R> implements Job<II, R>, BiConsumer<R, Throwable> {
            boolean mAsync = false;
            private String mDebugName;
            Job<II, R> mDelegate;

            CompletionAwareJob() {
                long requestTimeoutMs = Impl.this.getRequestTimeoutMs();
                if (requestTimeoutMs > 0) {
                    orTimeout(requestTimeoutMs, TimeUnit.MILLISECONDS);
                }
            }

            private static /* synthetic */ boolean lambda$new$0(StackTraceElement stackTraceElement) {
                return !stackTraceElement.getClassName().contains(ServiceConnector.class.getName());
            }

            public R run(II ii) throws Exception {
                return this.mDelegate.run(ii);
            }

            public boolean cancel(boolean z) {
                if (z) {
                    Log.w(Impl.LOG_TAG, "mayInterruptIfRunning not supported - ignoring");
                }
                return super.cancel(z) || Impl.this.mQueue.remove(this);
            }

            public String toString() {
                return this.mDelegate + " wrapped into " + super.toString();
            }

            public void accept(R r, Throwable th) {
                if (th != null) {
                    completeExceptionally(th);
                } else {
                    complete(r);
                }
            }

            /* access modifiers changed from: protected */
            public void onCompleted(R r, Throwable th) {
                super.onCompleted(r, th);
                if (Impl.this.mUnfinishedJobs.remove((Object) this)) {
                    Impl.this.maybeScheduleUnbindTimeout();
                }
            }
        }
    }

    public static class NoOp<T extends IInterface> extends AndroidFuture<Object> implements ServiceConnector<T> {
        public AndroidFuture<T> connect() {
            return this;
        }

        public AndroidFuture<Void> post(VoidJob<T> voidJob) {
            return this;
        }

        public <R> AndroidFuture<R> postAsync(Job<T, CompletableFuture<R>> job) {
            return this;
        }

        public <R> AndroidFuture<R> postForResult(Job<T, R> job) {
            return this;
        }

        public boolean run(VoidJob<T> voidJob) {
            return false;
        }

        public void setServiceLifecycleCallbacks(ServiceLifecycleCallbacks<T> serviceLifecycleCallbacks) {
        }

        public void unbind() {
        }

        public NoOp() {
            completeExceptionally(new IllegalStateException("ServiceConnector is a no-op"));
        }
    }
}
