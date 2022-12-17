package androidx.window.layout;

import android.util.Log;
import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.layout.WindowLayoutComponent;
import androidx.window.layout.WindowInfoTracker;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;

/* compiled from: WindowInfoTracker.kt */
final class WindowInfoTracker$Companion$extensionBackend$2 extends Lambda implements Function0<ExtensionWindowLayoutInfoBackend> {
    public static final WindowInfoTracker$Companion$extensionBackend$2 INSTANCE = new WindowInfoTracker$Companion$extensionBackend$2();

    WindowInfoTracker$Companion$extensionBackend$2() {
        super(0);
    }

    @Nullable
    public final ExtensionWindowLayoutInfoBackend invoke() {
        WindowLayoutComponent windowLayoutComponent;
        try {
            ClassLoader classLoader = WindowInfoTracker.class.getClassLoader();
            SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = classLoader != null ? new SafeWindowLayoutComponentProvider(classLoader, new ConsumerAdapter(classLoader)) : null;
            if (safeWindowLayoutComponentProvider == null || (windowLayoutComponent = safeWindowLayoutComponentProvider.getWindowLayoutComponent()) == null) {
                return null;
            }
            Intrinsics.checkNotNullExpressionValue(classLoader, "loader");
            return new ExtensionWindowLayoutInfoBackend(windowLayoutComponent, new ConsumerAdapter(classLoader));
        } catch (Throwable unused) {
            if (!WindowInfoTracker.Companion.DEBUG) {
                return null;
            }
            Log.d(WindowInfoTracker.Companion.TAG, "Failed to load WindowExtensions");
            return null;
        }
    }
}
