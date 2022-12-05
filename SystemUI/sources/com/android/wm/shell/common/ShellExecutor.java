package com.android.wm.shell.common;

import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public interface ShellExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);

    void executeDelayed(Runnable runnable, long j);

    boolean hasCallback(Runnable runnable);

    void removeCallbacks(Runnable runnable);

    default void executeBlocking(final Runnable runnable, int i, TimeUnit timeUnit) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new Runnable() { // from class: com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ShellExecutor.lambda$executeBlocking$0(runnable, countDownLatch);
            }
        });
        countDownLatch.await(i, timeUnit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static /* synthetic */ void lambda$executeBlocking$0(Runnable runnable, CountDownLatch countDownLatch) {
        runnable.run();
        countDownLatch.countDown();
    }

    default void executeBlocking(Runnable runnable) throws InterruptedException {
        executeBlocking(runnable, 2, TimeUnit.SECONDS);
    }

    default <T> T executeBlockingForResult(final Supplier<T> supplier, Class cls) {
        final Object[] objArr = (Object[]) Array.newInstance(cls, 1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new Runnable() { // from class: com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ShellExecutor.lambda$executeBlockingForResult$1(objArr, supplier, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
            return (T) objArr[0];
        } catch (InterruptedException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static /* synthetic */ void lambda$executeBlockingForResult$1(Object[] objArr, Supplier supplier, CountDownLatch countDownLatch) {
        objArr[0] = supplier.get();
        countDownLatch.countDown();
    }
}
