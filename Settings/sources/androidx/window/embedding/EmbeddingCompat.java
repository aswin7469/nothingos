package androidx.window.embedding;

import android.app.Activity;
import android.util.Log;
import androidx.window.core.ConsumerAdapter;
import androidx.window.core.ExperimentalWindowApi;
import androidx.window.embedding.EmbeddingInterfaceCompat;
import androidx.window.extensions.WindowExtensionsProvider;
import androidx.window.extensions.embedding.ActivityEmbeddingComponent;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: EmbeddingCompat.kt */
public final class EmbeddingCompat implements EmbeddingInterfaceCompat {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final boolean DEBUG = true;
    @NotNull
    private static final String TAG = "EmbeddingCompat";
    /* access modifiers changed from: private */
    @NotNull
    public final EmbeddingAdapter adapter;
    @NotNull
    private final ConsumerAdapter consumerAdapter;
    @NotNull
    private final ActivityEmbeddingComponent embeddingExtension;

    public EmbeddingCompat(@NotNull ActivityEmbeddingComponent activityEmbeddingComponent, @NotNull EmbeddingAdapter embeddingAdapter, @NotNull ConsumerAdapter consumerAdapter2) {
        Intrinsics.checkNotNullParameter(activityEmbeddingComponent, "embeddingExtension");
        Intrinsics.checkNotNullParameter(embeddingAdapter, "adapter");
        Intrinsics.checkNotNullParameter(consumerAdapter2, "consumerAdapter");
        this.embeddingExtension = activityEmbeddingComponent;
        this.adapter = embeddingAdapter;
        this.consumerAdapter = consumerAdapter2;
    }

    public void setSplitRules(@NotNull Set<? extends EmbeddingRule> set) {
        Intrinsics.checkNotNullParameter(set, "rules");
        this.embeddingExtension.setEmbeddingRules(this.adapter.translate(set));
    }

    public void setEmbeddingCallback(@NotNull EmbeddingInterfaceCompat.EmbeddingCallbackInterface embeddingCallbackInterface) {
        Intrinsics.checkNotNullParameter(embeddingCallbackInterface, "embeddingCallback");
        this.consumerAdapter.addConsumer(this.embeddingExtension, Reflection.getOrCreateKotlinClass(List.class), "setSplitInfoCallback", new EmbeddingCompat$setEmbeddingCallback$1(embeddingCallbackInterface, this));
    }

    public boolean isActivityEmbedded(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        return this.embeddingExtension.isActivityEmbedded(activity);
    }

    /* compiled from: EmbeddingCompat.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @Nullable
        public final Integer getExtensionApiLevel() {
            try {
                return Integer.valueOf(WindowExtensionsProvider.getWindowExtensions().getVendorApiLevel());
            } catch (NoClassDefFoundError unused) {
                Log.d(EmbeddingCompat.TAG, "Embedding extension version not found");
                return null;
            } catch (UnsupportedOperationException unused2) {
                Log.d(EmbeddingCompat.TAG, "Stub Extension");
                return null;
            }
        }

        public final boolean isEmbeddingAvailable() {
            try {
                return WindowExtensionsProvider.getWindowExtensions().getActivityEmbeddingComponent() != null;
            } catch (NoClassDefFoundError unused) {
                Log.d(EmbeddingCompat.TAG, "Embedding extension version not found");
                return false;
            } catch (UnsupportedOperationException unused2) {
                Log.d(EmbeddingCompat.TAG, "Stub Extension");
                return false;
            }
        }

        @NotNull
        public final ActivityEmbeddingComponent embeddingComponent() {
            Class<EmbeddingCompat> cls = EmbeddingCompat.class;
            if (isEmbeddingAvailable()) {
                ActivityEmbeddingComponent activityEmbeddingComponent = WindowExtensionsProvider.getWindowExtensions().getActivityEmbeddingComponent();
                if (activityEmbeddingComponent != null) {
                    return activityEmbeddingComponent;
                }
                Object newProxyInstance = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{ActivityEmbeddingComponent.class}, new EmbeddingCompat$Companion$$ExternalSyntheticLambda0());
                if (newProxyInstance != null) {
                    return (ActivityEmbeddingComponent) newProxyInstance;
                }
                throw new NullPointerException("null cannot be cast to non-null type androidx.window.extensions.embedding.ActivityEmbeddingComponent");
            }
            Object newProxyInstance2 = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{ActivityEmbeddingComponent.class}, new EmbeddingCompat$Companion$$ExternalSyntheticLambda1());
            if (newProxyInstance2 != null) {
                return (ActivityEmbeddingComponent) newProxyInstance2;
            }
            throw new NullPointerException("null cannot be cast to non-null type androidx.window.extensions.embedding.ActivityEmbeddingComponent");
        }

        /* access modifiers changed from: private */
        /* renamed from: embeddingComponent$lambda-0  reason: not valid java name */
        public static final Unit m49embeddingComponent$lambda0(Object obj, Method method, Object[] objArr) {
            return Unit.INSTANCE;
        }

        /* access modifiers changed from: private */
        /* renamed from: embeddingComponent$lambda-1  reason: not valid java name */
        public static final Unit m50embeddingComponent$lambda1(Object obj, Method method, Object[] objArr) {
            return Unit.INSTANCE;
        }
    }
}
