package androidx.window.area;

import androidx.window.core.ExperimentalWindowApi;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: WindowAreaController.kt */
public interface WindowAreaControllerDecorator {
    @NotNull
    WindowAreaController decorate(@NotNull WindowAreaController windowAreaController);
}
