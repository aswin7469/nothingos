package androidx.window.area;

import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: WindowAreaStatus.kt */
public final class WindowAreaStatus {
    @NotNull
    public static final WindowAreaStatus AVAILABLE = new WindowAreaStatus("AVAILABLE");
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final WindowAreaStatus UNAVAILABLE = new WindowAreaStatus("UNAVAILABLE");
    @NotNull
    public static final WindowAreaStatus UNSUPPORTED = new WindowAreaStatus("UNSUPPORTED");
    @NotNull
    private final String mDescription;

    @NotNull
    public static final WindowAreaStatus translate$window_release(int i) {
        return Companion.translate$window_release(i);
    }

    private WindowAreaStatus(String str) {
        this.mDescription = str;
    }

    @NotNull
    public String toString() {
        return this.mDescription;
    }

    /* compiled from: WindowAreaStatus.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final WindowAreaStatus translate$window_release(int i) {
            if (i == 1) {
                return WindowAreaStatus.UNAVAILABLE;
            }
            if (i != 2) {
                return WindowAreaStatus.UNSUPPORTED;
            }
            return WindowAreaStatus.AVAILABLE;
        }
    }
}
