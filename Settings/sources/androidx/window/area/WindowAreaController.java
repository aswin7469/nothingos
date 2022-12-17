package androidx.window.area;

import android.app.Activity;
import android.util.Log;
import androidx.window.core.BuildConfig;
import androidx.window.core.ExperimentalWindowApi;
import androidx.window.core.SpecificationComputer;
import androidx.window.extensions.WindowExtensionsProvider;
import androidx.window.extensions.area.WindowAreaComponent;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: WindowAreaController.kt */
public interface WindowAreaController {
    @NotNull
    public static final Companion Companion = Companion.$$INSTANCE;

    @NotNull
    static WindowAreaController getOrCreate() {
        return Companion.getOrCreate();
    }

    static void overrideDecorator(@NotNull WindowAreaControllerDecorator windowAreaControllerDecorator) {
        Companion.overrideDecorator(windowAreaControllerDecorator);
    }

    static void reset() {
        Companion.reset();
    }

    void rearDisplayMode(@NotNull Activity activity, @NotNull Executor executor, @NotNull WindowAreaSessionCallback windowAreaSessionCallback) throws UnsupportedOperationException;

    @NotNull
    Flow<WindowAreaStatus> rearDisplayStatus();

    /* compiled from: WindowAreaController.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        @NotNull
        private static final UnsupportedOperationException REAR_DISPLAY_ERROR = new UnsupportedOperationException("Rear Display mode cannot be enabled currently");
        @Nullable
        private static final String TAG = Reflection.getOrCreateKotlinClass(WindowAreaController.class).getSimpleName();
        @NotNull
        private static WindowAreaControllerDecorator decorator = EmptyDecorator.INSTANCE;

        private Companion() {
        }

        @NotNull
        public final UnsupportedOperationException getREAR_DISPLAY_ERROR$window_release() {
            return REAR_DISPLAY_ERROR;
        }

        @NotNull
        public final WindowAreaController getOrCreate() {
            WindowAreaComponent windowAreaComponent;
            WindowAreaController windowAreaController;
            try {
                windowAreaComponent = WindowExtensionsProvider.getWindowExtensions().getWindowAreaComponent();
            } catch (Throwable unused) {
                if (BuildConfig.INSTANCE.getVerificationMode() == SpecificationComputer.VerificationMode.STRICT) {
                    Log.d(TAG, "Failed to load WindowExtensions");
                }
                windowAreaComponent = null;
            }
            if (windowAreaComponent == null) {
                windowAreaController = new EmptyWindowAreaControllerImpl();
            } else {
                windowAreaController = new WindowAreaControllerImpl(windowAreaComponent);
            }
            return decorator.decorate(windowAreaController);
        }

        public final void overrideDecorator(@NotNull WindowAreaControllerDecorator windowAreaControllerDecorator) {
            Intrinsics.checkNotNullParameter(windowAreaControllerDecorator, "overridingDecorator");
            decorator = windowAreaControllerDecorator;
        }

        public final void reset() {
            decorator = EmptyDecorator.INSTANCE;
        }
    }
}
