package com.android.p019wm.shell.legacysplitscreen;

import android.graphics.Rect;
import android.window.WindowContainerToken;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.p026io.PrintWriter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ExternalThread
/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreen */
public interface LegacySplitScreen {
    void dismissSplitToPrimaryTask();

    void dump(PrintWriter printWriter);

    DividerView getDividerView();

    WindowContainerToken getSecondaryRoot();

    boolean isDividerVisible();

    boolean isHomeStackResizable();

    boolean isMinimized();

    void onAppTransitionFinished();

    void onKeyguardVisibilityChanged(boolean z);

    void onUndockingTask();

    void registerBoundsChangeListener(BiConsumer<Rect, Rect> biConsumer);

    void registerInSplitScreenListener(Consumer<Boolean> consumer);

    void setMinimized(boolean z);

    boolean splitPrimaryTask();

    void unregisterInSplitScreenListener(Consumer<Boolean> consumer);
}
