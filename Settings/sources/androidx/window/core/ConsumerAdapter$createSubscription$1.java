package androidx.window.core;

import androidx.window.core.ConsumerAdapter;
import java.lang.reflect.Method;

/* compiled from: ConsumerAdapter.kt */
public final class ConsumerAdapter$createSubscription$1 implements ConsumerAdapter.Subscription {
    final /* synthetic */ Object $javaConsumer;
    final /* synthetic */ Object $obj;
    final /* synthetic */ Method $removeMethod;

    ConsumerAdapter$createSubscription$1(Method method, Object obj, Object obj2) {
        this.$removeMethod = method;
        this.$obj = obj;
        this.$javaConsumer = obj2;
    }

    public void dispose() {
        this.$removeMethod.invoke(this.$obj, new Object[]{this.$javaConsumer});
    }
}
