package androidx.window.area;

import androidx.window.core.ExperimentalWindowApi;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: WindowAreaSessionCallback.kt */
public interface WindowAreaSessionCallback {
    void onSessionEnded();

    void onSessionStarted(@NotNull WindowAreaSession windowAreaSession);
}
