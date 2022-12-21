package sun.nio.p033ch;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ShutdownChannelGroupException;
import java.security.AccessController;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import sun.security.action.GetIntegerAction;

/* renamed from: sun.nio.ch.Invoker */
class Invoker {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int maxHandlerInvokeCount = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.maxCompletionHandlersOnStack", 16))).intValue();
    /* access modifiers changed from: private */
    public static final ThreadLocal<GroupAndInvokeCount> myGroupAndInvokeCount = new ThreadLocal<GroupAndInvokeCount>() {
        /* access modifiers changed from: protected */
        public GroupAndInvokeCount initialValue() {
            return null;
        }
    };

    private Invoker() {
    }

    /* renamed from: sun.nio.ch.Invoker$GroupAndInvokeCount */
    static class GroupAndInvokeCount {
        /* access modifiers changed from: private */
        public final AsynchronousChannelGroupImpl group;
        private int handlerInvokeCount;

        GroupAndInvokeCount(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
            this.group = asynchronousChannelGroupImpl;
        }

        /* access modifiers changed from: package-private */
        public AsynchronousChannelGroupImpl group() {
            return this.group;
        }

        /* access modifiers changed from: package-private */
        public int invokeCount() {
            return this.handlerInvokeCount;
        }

        /* access modifiers changed from: package-private */
        public void setInvokeCount(int i) {
            this.handlerInvokeCount = i;
        }

        /* access modifiers changed from: package-private */
        public void resetInvokeCount() {
            this.handlerInvokeCount = 0;
        }

        /* access modifiers changed from: package-private */
        public void incrementInvokeCount() {
            this.handlerInvokeCount++;
        }
    }

    static void bindToGroup(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        myGroupAndInvokeCount.set(new GroupAndInvokeCount(asynchronousChannelGroupImpl));
    }

    static GroupAndInvokeCount getGroupAndInvokeCount() {
        return myGroupAndInvokeCount.get();
    }

    static boolean isBoundToAnyGroup() {
        return myGroupAndInvokeCount.get() != null;
    }

    static boolean mayInvokeDirect(GroupAndInvokeCount groupAndInvokeCount, AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        return groupAndInvokeCount != null && groupAndInvokeCount.group() == asynchronousChannelGroupImpl && groupAndInvokeCount.invokeCount() < maxHandlerInvokeCount;
    }

    static <V, A> void invokeUnchecked(CompletionHandler<V, ? super A> completionHandler, A a, V v, Throwable th) {
        if (th == null) {
            completionHandler.completed(v, a);
        } else {
            completionHandler.failed(th, a);
        }
        Thread.interrupted();
    }

    static <V, A> void invokeDirect(GroupAndInvokeCount groupAndInvokeCount, CompletionHandler<V, ? super A> completionHandler, A a, V v, Throwable th) {
        groupAndInvokeCount.incrementInvokeCount();
        invokeUnchecked(completionHandler, a, v, th);
    }

    static <V, A> void invoke(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a, V v, Throwable th) {
        boolean z;
        GroupAndInvokeCount groupAndInvokeCount = myGroupAndInvokeCount.get();
        boolean z2 = false;
        if (groupAndInvokeCount != null) {
            z = groupAndInvokeCount.group() == ((Groupable) asynchronousChannel).group();
            if (z && groupAndInvokeCount.invokeCount() < maxHandlerInvokeCount) {
                z2 = true;
            }
        } else {
            z = false;
        }
        if (z2) {
            invokeDirect(groupAndInvokeCount, completionHandler, a, v, th);
            return;
        }
        try {
            invokeIndirectly(asynchronousChannel, completionHandler, a, v, th);
        } catch (RejectedExecutionException unused) {
            if (z) {
                invokeDirect(groupAndInvokeCount, completionHandler, a, v, th);
                return;
            }
            throw new ShutdownChannelGroupException();
        }
    }

    static <V, A> void invokeIndirectly(AsynchronousChannel asynchronousChannel, final CompletionHandler<V, ? super A> completionHandler, final A a, final V v, final Throwable th) {
        try {
            ((Groupable) asynchronousChannel).group().executeOnPooledThread(new Runnable() {
                public void run() {
                    GroupAndInvokeCount groupAndInvokeCount = (GroupAndInvokeCount) Invoker.myGroupAndInvokeCount.get();
                    if (groupAndInvokeCount != null) {
                        groupAndInvokeCount.setInvokeCount(1);
                    }
                    Invoker.invokeUnchecked(CompletionHandler.this, a, v, th);
                }
            });
        } catch (RejectedExecutionException unused) {
            throw new ShutdownChannelGroupException();
        }
    }

    static <V, A> void invokeIndirectly(final CompletionHandler<V, ? super A> completionHandler, final A a, final V v, final Throwable th, Executor executor) {
        try {
            executor.execute(new Runnable() {
                public void run() {
                    Invoker.invokeUnchecked(CompletionHandler.this, a, v, th);
                }
            });
        } catch (RejectedExecutionException unused) {
            throw new ShutdownChannelGroupException();
        }
    }

    static void invokeOnThreadInThreadPool(Groupable groupable, Runnable runnable) {
        GroupAndInvokeCount groupAndInvokeCount = myGroupAndInvokeCount.get();
        AsynchronousChannelGroupImpl group = groupable.group();
        boolean z = false;
        if (groupAndInvokeCount != null && groupAndInvokeCount.group == group) {
            z = true;
        }
        if (z) {
            try {
                runnable.run();
            } catch (RejectedExecutionException unused) {
                throw new ShutdownChannelGroupException();
            }
        } else {
            group.executeOnPooledThread(runnable);
        }
    }

    static <V, A> void invokeUnchecked(PendingFuture<V, A> pendingFuture) {
        CompletionHandler<V, ? super A> handler = pendingFuture.handler();
        if (handler != null) {
            invokeUnchecked(handler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }

    static <V, A> void invoke(PendingFuture<V, A> pendingFuture) {
        CompletionHandler<V, ? super A> handler = pendingFuture.handler();
        if (handler != null) {
            invoke(pendingFuture.channel(), handler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }

    static <V, A> void invokeIndirectly(PendingFuture<V, A> pendingFuture) {
        CompletionHandler<V, ? super A> handler = pendingFuture.handler();
        if (handler != null) {
            invokeIndirectly(pendingFuture.channel(), handler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }
}
