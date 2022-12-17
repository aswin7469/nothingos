package androidx.window.area;

import android.app.Activity;
import android.util.Log;
import androidx.window.core.BuildConfig;
import androidx.window.core.ExperimentalWindowApi;
import androidx.window.core.SpecificationComputer;
import androidx.window.extensions.area.WindowAreaComponent;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: WindowAreaControllerImpl.kt */
public final class WindowAreaControllerImpl implements WindowAreaController {
    private static final int BUFFER_CAPACITY = 10;
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    @Nullable
    public static final String TAG = Reflection.getOrCreateKotlinClass(WindowAreaControllerImpl.class).getSimpleName();
    /* access modifiers changed from: private */
    @Nullable
    public WindowAreaStatus currentStatus;
    private Consumer<Integer> rearDisplaySessionConsumer;
    /* access modifiers changed from: private */
    @NotNull
    public final WindowAreaComponent windowAreaComponent;

    public WindowAreaControllerImpl(@NotNull WindowAreaComponent windowAreaComponent2) {
        Intrinsics.checkNotNullParameter(windowAreaComponent2, "windowAreaComponent");
        this.windowAreaComponent = windowAreaComponent2;
    }

    @NotNull
    public Flow<WindowAreaStatus> rearDisplayStatus() {
        return FlowKt.distinctUntilChanged(FlowKt.flow(new WindowAreaControllerImpl$rearDisplayStatus$1(this, (Continuation<? super WindowAreaControllerImpl$rearDisplayStatus$1>) null)));
    }

    public void rearDisplayMode(@NotNull Activity activity, @NotNull Executor executor, @NotNull WindowAreaSessionCallback windowAreaSessionCallback) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(windowAreaSessionCallback, "windowAreaSessionCallback");
        WindowAreaStatus windowAreaStatus = this.currentStatus;
        if (windowAreaStatus == null || Intrinsics.areEqual(windowAreaStatus, WindowAreaStatus.AVAILABLE)) {
            RearDisplaySessionConsumer rearDisplaySessionConsumer2 = new RearDisplaySessionConsumer(windowAreaSessionCallback, this.windowAreaComponent);
            this.rearDisplaySessionConsumer = rearDisplaySessionConsumer2;
            this.windowAreaComponent.startRearDisplaySession(activity, rearDisplaySessionConsumer2);
            return;
        }
        throw WindowAreaController.Companion.getREAR_DISPLAY_ERROR$window_release();
    }

    /* compiled from: WindowAreaControllerImpl.kt */
    public static final class RearDisplaySessionConsumer implements Consumer<Integer> {
        @NotNull
        private final WindowAreaSessionCallback appCallback;
        @NotNull
        private final WindowAreaComponent extensionsComponent;
        @Nullable
        private WindowAreaSession session;

        public RearDisplaySessionConsumer(@NotNull WindowAreaSessionCallback windowAreaSessionCallback, @NotNull WindowAreaComponent windowAreaComponent) {
            Intrinsics.checkNotNullParameter(windowAreaSessionCallback, "appCallback");
            Intrinsics.checkNotNullParameter(windowAreaComponent, "extensionsComponent");
            this.appCallback = windowAreaSessionCallback;
            this.extensionsComponent = windowAreaComponent;
        }

        public /* bridge */ /* synthetic */ void accept(Object obj) {
            accept(((Number) obj).intValue());
        }

        public void accept(int i) {
            if (i == 0) {
                onSessionFinished();
            } else if (i != 1) {
                if (BuildConfig.INSTANCE.getVerificationMode() == SpecificationComputer.VerificationMode.STRICT) {
                    String access$getTAG$cp = WindowAreaControllerImpl.TAG;
                    Log.d(access$getTAG$cp, "Received an unknown session status value: " + i);
                }
                onSessionFinished();
            } else {
                onSessionStarted();
            }
        }

        private final void onSessionStarted() {
            RearDisplaySessionImpl rearDisplaySessionImpl = new RearDisplaySessionImpl(this.extensionsComponent);
            this.session = rearDisplaySessionImpl;
            this.appCallback.onSessionStarted(rearDisplaySessionImpl);
        }

        private final void onSessionFinished() {
            this.session = null;
            this.appCallback.onSessionEnded();
        }
    }

    /* compiled from: WindowAreaControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
