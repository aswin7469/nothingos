package androidx.window.layout;

import android.app.Activity;
import androidx.core.util.Consumer;
import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.layout.WindowLayoutComponent;
import androidx.window.extensions.layout.WindowLayoutInfo;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ExtensionWindowLayoutInfoBackend.kt */
public final class ExtensionWindowLayoutInfoBackend implements WindowBackend {
    @NotNull
    private final Map<Activity, MulticastConsumer> activityToListeners = new LinkedHashMap();
    @NotNull
    private final WindowLayoutComponent component;
    @NotNull
    private final ConsumerAdapter consumerAdapter;
    @NotNull
    private final Map<MulticastConsumer, ConsumerAdapter.Subscription> consumerToToken = new LinkedHashMap();
    @NotNull
    private final ReentrantLock extensionWindowBackendLock = new ReentrantLock();
    @NotNull
    private final Map<Consumer<WindowLayoutInfo>, Activity> listenerToActivity = new LinkedHashMap();

    public ExtensionWindowLayoutInfoBackend(@NotNull WindowLayoutComponent windowLayoutComponent, @NotNull ConsumerAdapter consumerAdapter2) {
        Intrinsics.checkNotNullParameter(windowLayoutComponent, "component");
        Intrinsics.checkNotNullParameter(consumerAdapter2, "consumerAdapter");
        this.component = windowLayoutComponent;
        this.consumerAdapter = consumerAdapter2;
    }

    public void registerLayoutChangeCallback(@NotNull Activity activity, @NotNull Executor executor, @NotNull Consumer<WindowLayoutInfo> consumer) {
        Unit unit;
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(consumer, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
            if (multicastConsumer != null) {
                multicastConsumer.addListener(consumer);
                this.listenerToActivity.put(consumer, activity);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            if (unit == null) {
                MulticastConsumer multicastConsumer2 = new MulticastConsumer(activity);
                this.activityToListeners.put(activity, multicastConsumer2);
                this.listenerToActivity.put(consumer, activity);
                multicastConsumer2.addListener(consumer);
                this.consumerToToken.put(multicastConsumer2, this.consumerAdapter.createSubscription(this.component, Reflection.getOrCreateKotlinClass(WindowLayoutInfo.class), "addWindowLayoutInfoListener", "removeWindowLayoutInfoListener", activity, new C0457xa47eee96(multicastConsumer2)));
            }
            Unit unit2 = Unit.INSTANCE;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void unregisterLayoutChangeCallback(@NotNull Consumer<WindowLayoutInfo> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            Activity activity = this.listenerToActivity.get(consumer);
            if (activity != null) {
                MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
                if (multicastConsumer == null) {
                    reentrantLock.unlock();
                    return;
                }
                multicastConsumer.removeListener(consumer);
                if (multicastConsumer.isEmpty()) {
                    ConsumerAdapter.Subscription remove = this.consumerToToken.remove(multicastConsumer);
                    if (remove != null) {
                        remove.dispose();
                    }
                    this.listenerToActivity.remove(consumer);
                    this.activityToListeners.remove(activity);
                }
                Unit unit = Unit.INSTANCE;
                reentrantLock.unlock();
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    /* compiled from: ExtensionWindowLayoutInfoBackend.kt */
    private static final class MulticastConsumer implements Consumer<WindowLayoutInfo> {
        @NotNull
        private final Activity activity;
        @Nullable
        private WindowLayoutInfo lastKnownValue;
        @NotNull
        private final ReentrantLock multicastConsumerLock = new ReentrantLock();
        @NotNull
        private final Set<Consumer<WindowLayoutInfo>> registeredListeners = new LinkedHashSet();

        public MulticastConsumer(@NotNull Activity activity2) {
            Intrinsics.checkNotNullParameter(activity2, "activity");
            this.activity = activity2;
        }

        public void accept(@NotNull WindowLayoutInfo windowLayoutInfo) {
            Intrinsics.checkNotNullParameter(windowLayoutInfo, "value");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.lastKnownValue = ExtensionsWindowLayoutInfoAdapter.INSTANCE.translate$window_release(this.activity, windowLayoutInfo);
                for (Consumer accept : this.registeredListeners) {
                    accept.accept(this.lastKnownValue);
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                reentrantLock.unlock();
            }
        }

        public final void addListener(@NotNull Consumer<WindowLayoutInfo> consumer) {
            Intrinsics.checkNotNullParameter(consumer, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                WindowLayoutInfo windowLayoutInfo = this.lastKnownValue;
                if (windowLayoutInfo != null) {
                    consumer.accept(windowLayoutInfo);
                }
                this.registeredListeners.add(consumer);
            } finally {
                reentrantLock.unlock();
            }
        }

        public final void removeListener(@NotNull Consumer<WindowLayoutInfo> consumer) {
            Intrinsics.checkNotNullParameter(consumer, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.registeredListeners.remove(consumer);
            } finally {
                reentrantLock.unlock();
            }
        }

        public final boolean isEmpty() {
            return this.registeredListeners.isEmpty();
        }
    }
}
