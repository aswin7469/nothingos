package androidx.window.area;

import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: WindowAreaController.kt */
final class EmptyDecorator implements WindowAreaControllerDecorator {
    @NotNull
    public static final EmptyDecorator INSTANCE = new EmptyDecorator();

    @NotNull
    public WindowAreaController decorate(@NotNull WindowAreaController windowAreaController) {
        Intrinsics.checkNotNullParameter(windowAreaController, "controller");
        return windowAreaController;
    }

    private EmptyDecorator() {
    }
}
