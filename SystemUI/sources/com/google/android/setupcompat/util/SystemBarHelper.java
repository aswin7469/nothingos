package com.google.android.setupcompat.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

public final class SystemBarHelper {
    public static final int DEFAULT_IMMERSIVE_FLAGS = 5634;
    public static final int DIALOG_IMMERSIVE_FLAGS = 4098;
    /* access modifiers changed from: private */
    public static final Logger LOG = new Logger("SystemBarHelper");
    private static final int PEEK_DECOR_VIEW_RETRIES = 3;
    private static final int STATUS_BAR_DISABLE_BACK = 4194304;

    private interface OnDecorViewInstalledListener {
        void onDecorViewInstalled(View view);
    }

    public static void addVisibilityFlag(View view, int i) {
        view.setSystemUiVisibility(i | view.getSystemUiVisibility());
    }

    public static void addVisibilityFlag(Window window, int i) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.systemUiVisibility = i | attributes.systemUiVisibility;
        window.setAttributes(attributes);
    }

    public static void removeVisibilityFlag(View view, int i) {
        view.setSystemUiVisibility((~i) & view.getSystemUiVisibility());
    }

    public static void removeVisibilityFlag(Window window, int i) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.systemUiVisibility = (~i) & attributes.systemUiVisibility;
        window.setAttributes(attributes);
    }

    public static void addImmersiveFlagsToDecorView(Window window, final int i) {
        getDecorView(window, new OnDecorViewInstalledListener() {
            public void onDecorViewInstalled(View view) {
                SystemBarHelper.addVisibilityFlag(view, i);
            }
        });
    }

    public static void removeImmersiveFlagsFromDecorView(Window window, final int i) {
        getDecorView(window, new OnDecorViewInstalledListener() {
            public void onDecorViewInstalled(View view) {
                SystemBarHelper.removeVisibilityFlag(view, i);
            }
        });
    }

    @Deprecated
    public static void hideSystemBars(Dialog dialog) {
        Window window = dialog.getWindow();
        temporarilyDisableDialogFocus(window);
        addVisibilityFlag(window, 4098);
        addImmersiveFlagsToDecorView(window, 4098);
        window.setNavigationBarColor(0);
        window.setStatusBarColor(0);
    }

    @Deprecated
    public static void hideSystemBars(Window window) {
        addVisibilityFlag(window, (int) DEFAULT_IMMERSIVE_FLAGS);
        addImmersiveFlagsToDecorView(window, DEFAULT_IMMERSIVE_FLAGS);
        window.setNavigationBarColor(0);
        window.setStatusBarColor(0);
    }

    @Deprecated
    public static void showSystemBars(Window window, Context context) {
        removeVisibilityFlag(window, (int) DEFAULT_IMMERSIVE_FLAGS);
        removeImmersiveFlagsFromDecorView(window, DEFAULT_IMMERSIVE_FLAGS);
        if (context != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843857, 16843858});
            int color = obtainStyledAttributes.getColor(0, 0);
            int color2 = obtainStyledAttributes.getColor(1, 0);
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color2);
            obtainStyledAttributes.recycle();
        }
    }

    public static void setBackButtonVisible(Window window, boolean z) {
        if (z) {
            removeVisibilityFlag(window, 4194304);
            removeImmersiveFlagsFromDecorView(window, 4194304);
            return;
        }
        addVisibilityFlag(window, 4194304);
        addImmersiveFlagsToDecorView(window, 4194304);
    }

    public static void setImeInsetView(View view) {
        view.setOnApplyWindowInsetsListener(new WindowInsetsListener());
    }

    private static void temporarilyDisableDialogFocus(final Window window) {
        window.setFlags(8, 8);
        window.setSoftInputMode(256);
        new Handler().post(new Runnable() {
            public void run() {
                window.clearFlags(8);
            }
        });
    }

    private static class WindowInsetsListener implements View.OnApplyWindowInsetsListener {
        private int bottomOffset;
        private boolean hasCalculatedBottomOffset;

        private WindowInsetsListener() {
            this.hasCalculatedBottomOffset = false;
        }

        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            if (!this.hasCalculatedBottomOffset) {
                this.bottomOffset = SystemBarHelper.getBottomDistance(view);
                this.hasCalculatedBottomOffset = true;
            }
            int systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
            int max = Math.max(windowInsets.getSystemWindowInsetBottom() - this.bottomOffset, 0);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (max < marginLayoutParams.bottomMargin + view.getHeight()) {
                marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, max);
                view.setLayoutParams(marginLayoutParams);
                systemWindowInsetBottom = 0;
            }
            return windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), systemWindowInsetBottom);
        }
    }

    /* access modifiers changed from: private */
    public static int getBottomDistance(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return (view.getRootView().getHeight() - iArr[1]) - view.getHeight();
    }

    private static void getDecorView(Window window, OnDecorViewInstalledListener onDecorViewInstalledListener) {
        new DecorViewFinder().getDecorView(window, onDecorViewInstalledListener, 3);
    }

    private static class DecorViewFinder {
        /* access modifiers changed from: private */
        public OnDecorViewInstalledListener callback;
        /* access modifiers changed from: private */
        public final Runnable checkDecorViewRunnable;
        /* access modifiers changed from: private */
        public final Handler handler;
        /* access modifiers changed from: private */
        public int retries;
        /* access modifiers changed from: private */
        public Window window;

        private DecorViewFinder() {
            this.handler = new Handler();
            this.checkDecorViewRunnable = new Runnable() {
                public void run() {
                    View peekDecorView = DecorViewFinder.this.window.peekDecorView();
                    if (peekDecorView != null) {
                        DecorViewFinder.this.callback.onDecorViewInstalled(peekDecorView);
                        return;
                    }
                    DecorViewFinder.access$510(DecorViewFinder.this);
                    if (DecorViewFinder.this.retries >= 0) {
                        DecorViewFinder.this.handler.post(DecorViewFinder.this.checkDecorViewRunnable);
                    } else {
                        SystemBarHelper.LOG.mo55170e("Cannot get decor view of window: " + DecorViewFinder.this.window);
                    }
                }
            };
        }

        static /* synthetic */ int access$510(DecorViewFinder decorViewFinder) {
            int i = decorViewFinder.retries;
            decorViewFinder.retries = i - 1;
            return i;
        }

        public void getDecorView(Window window2, OnDecorViewInstalledListener onDecorViewInstalledListener, int i) {
            this.window = window2;
            this.retries = i;
            this.callback = onDecorViewInstalledListener;
            this.checkDecorViewRunnable.run();
        }
    }

    private SystemBarHelper() {
    }
}
