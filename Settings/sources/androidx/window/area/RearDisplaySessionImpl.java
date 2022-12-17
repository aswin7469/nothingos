package androidx.window.area;

import androidx.window.core.ExperimentalWindowApi;
import androidx.window.extensions.area.WindowAreaComponent;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: RearDisplaySessionImpl.kt */
public final class RearDisplaySessionImpl implements WindowAreaSession {
    @NotNull
    private final WindowAreaComponent windowAreaComponent;

    public RearDisplaySessionImpl(@NotNull WindowAreaComponent windowAreaComponent2) {
        Intrinsics.checkNotNullParameter(windowAreaComponent2, "windowAreaComponent");
        this.windowAreaComponent = windowAreaComponent2;
    }

    public void close() {
        this.windowAreaComponent.endRearDisplaySession();
    }
}
