package android.accounts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public interface AccountManagerFuture<V> {
    boolean cancel(boolean z);

    /* renamed from: getResult */
    V mo19getResult() throws OperationCanceledException, IOException, AuthenticatorException;

    /* renamed from: getResult */
    V mo20getResult(long j, TimeUnit timeUnit) throws OperationCanceledException, IOException, AuthenticatorException;

    boolean isCancelled();

    boolean isDone();
}
