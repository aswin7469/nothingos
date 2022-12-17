package androidx.window.layout;

import android.app.Activity;
import android.content.Context;
import kotlin.Lazy;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WindowInfoTracker.kt */
public interface WindowInfoTracker {
    @NotNull
    public static final Companion Companion = Companion.$$INSTANCE;

    @NotNull
    static WindowInfoTracker getOrCreate(@NotNull Context context) {
        return Companion.getOrCreate(context);
    }

    static void overrideDecorator(@NotNull WindowInfoTrackerDecorator windowInfoTrackerDecorator) {
        Companion.overrideDecorator(windowInfoTrackerDecorator);
    }

    static void reset() {
        Companion.reset();
    }

    @NotNull
    Flow<WindowLayoutInfo> windowLayoutInfo(@NotNull Activity activity);

    /* compiled from: WindowInfoTracker.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        /* access modifiers changed from: private */
        public static final boolean DEBUG = false;
        /* access modifiers changed from: private */
        @Nullable
        public static final String TAG = Reflection.getOrCreateKotlinClass(WindowInfoTracker.class).getSimpleName();
        @NotNull
        private static WindowInfoTrackerDecorator decorator = EmptyDecorator.INSTANCE;
        @NotNull
        private static final Lazy<ExtensionWindowLayoutInfoBackend> extensionBackend$delegate = LazyKt__LazyJVMKt.lazy(WindowInfoTracker$Companion$extensionBackend$2.INSTANCE);

        public static /* synthetic */ void getExtensionBackend$window_release$annotations() {
        }

        private Companion() {
        }

        @Nullable
        public final WindowBackend getExtensionBackend$window_release() {
            return extensionBackend$delegate.getValue();
        }

        @NotNull
        public final WindowInfoTracker getOrCreate(@NotNull Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            WindowBackend extensionBackend$window_release = getExtensionBackend$window_release();
            if (extensionBackend$window_release == null) {
                extensionBackend$window_release = SidecarWindowBackend.Companion.getInstance(context);
            }
            return decorator.decorate(new WindowInfoTrackerImpl(WindowMetricsCalculatorCompat.INSTANCE, extensionBackend$window_release));
        }

        public final void overrideDecorator(@NotNull WindowInfoTrackerDecorator windowInfoTrackerDecorator) {
            Intrinsics.checkNotNullParameter(windowInfoTrackerDecorator, "overridingDecorator");
            decorator = windowInfoTrackerDecorator;
        }

        public final void reset() {
            decorator = EmptyDecorator.INSTANCE;
        }
    }
}
