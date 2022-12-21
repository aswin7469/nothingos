package com.android.p019wm.shell.pip;

import android.content.res.Configuration;
import android.graphics.Rect;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.p026io.PrintWriter;
import java.util.function.Consumer;

@ExternalThread
/* renamed from: com.android.wm.shell.pip.Pip */
public interface Pip {
    void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    IPip createExternalInterface() {
        return null;
    }

    void dump(PrintWriter printWriter) {
    }

    void expandPip() {
    }

    void onConfigurationChanged(Configuration configuration) {
    }

    void onDensityOrFontScaleChanged() {
    }

    void onKeyguardDismissAnimationFinished() {
    }

    void onKeyguardVisibilityChanged(boolean z, boolean z2) {
    }

    void onOverlayChanged() {
    }

    void onSystemUiStateChanged(boolean z, int i) {
    }

    void registerSessionListenerForCurrentUser() {
    }

    void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    void setPinnedStackAnimationListener(Consumer<Boolean> consumer) {
    }

    void setPinnedStackAnimationType(int i) {
    }

    void setShelfHeight(boolean z, int i) {
    }

    void showPictureInPictureMenu() {
    }
}
