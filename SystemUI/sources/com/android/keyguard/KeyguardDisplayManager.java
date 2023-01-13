package com.android.keyguard;

import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.navigationbar.NavigationBarView;
import dagger.Lazy;
import java.sql.Types;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class KeyguardDisplayManager {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    protected static final String TAG = "KeyguardDisplayManager";
    private final Context mContext;
    private final DisplayManager.DisplayListener mDisplayListener;
    /* access modifiers changed from: private */
    public final DisplayManager mDisplayService;
    private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    private MediaRouter mMediaRouter = null;
    private final MediaRouter.SimpleCallback mMediaRouterCallback;
    private final Lazy<NavigationBarController> mNavigationBarControllerLazy;
    private final SparseArray<Presentation> mPresentations = new SparseArray<>();
    /* access modifiers changed from: private */
    public boolean mShowing;
    private final DisplayInfo mTmpDisplayInfo = new DisplayInfo();

    @Inject
    public KeyguardDisplayManager(Context context, Lazy<NavigationBarController> lazy, KeyguardStatusViewComponent.Factory factory, @UiBackground Executor executor) {
        C15981 r1 = new DisplayManager.DisplayListener() {
            public void onDisplayChanged(int i) {
            }

            public void onDisplayAdded(int i) {
                Trace.beginSection("KeyguardDisplayManager#onDisplayAdded(displayId=" + i + NavigationBarInflaterView.KEY_CODE_END);
                Display display = KeyguardDisplayManager.this.mDisplayService.getDisplay(i);
                if (KeyguardDisplayManager.this.mShowing) {
                    KeyguardDisplayManager.this.updateNavigationBarVisibility(i, false);
                    boolean unused = KeyguardDisplayManager.this.showPresentation(display);
                }
                Trace.endSection();
            }

            public void onDisplayRemoved(int i) {
                Trace.beginSection("KeyguardDisplayManager#onDisplayRemoved(displayId=" + i + NavigationBarInflaterView.KEY_CODE_END);
                KeyguardDisplayManager.this.hidePresentation(i);
                Trace.endSection();
            }
        };
        this.mDisplayListener = r1;
        this.mMediaRouterCallback = new MediaRouter.SimpleCallback() {
            public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d(KeyguardDisplayManager.TAG, "onRouteSelected: type=" + i + ", info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }

            public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d(KeyguardDisplayManager.TAG, "onRouteUnselected: type=" + i + ", info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }

            public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d(KeyguardDisplayManager.TAG, "onRoutePresentationDisplayChanged: info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }
        };
        this.mContext = context;
        this.mNavigationBarControllerLazy = lazy;
        this.mKeyguardStatusViewComponentFactory = factory;
        executor.execute(new KeyguardDisplayManager$$ExternalSyntheticLambda1(this));
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayService = displayManager;
        displayManager.registerDisplayListener(r1, (Handler) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardDisplayManager  reason: not valid java name */
    public /* synthetic */ void m2291lambda$new$0$comandroidkeyguardKeyguardDisplayManager() {
        this.mMediaRouter = (MediaRouter) this.mContext.getSystemService(MediaRouter.class);
    }

    private boolean isKeyguardShowable(Display display) {
        if (display == null) {
            if (DEBUG) {
                Log.i(TAG, "Cannot show Keyguard on null display");
            }
            return false;
        } else if (display.getDisplayId() == 0) {
            if (DEBUG) {
                Log.i(TAG, "Do not show KeyguardPresentation on the default display");
            }
            return false;
        } else {
            display.getDisplayInfo(this.mTmpDisplayInfo);
            if ((this.mTmpDisplayInfo.flags & 4) != 0) {
                if (DEBUG) {
                    Log.i(TAG, "Do not show KeyguardPresentation on a private display");
                }
                return false;
            } else if ((this.mTmpDisplayInfo.flags & 512) == 0) {
                return true;
            } else {
                if (DEBUG) {
                    Log.i(TAG, "Do not show KeyguardPresentation on an unlocked display");
                }
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean showPresentation(Display display) {
        if (!isKeyguardShowable(display)) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "Keyguard enabled on display: " + display);
        }
        int displayId = display.getDisplayId();
        if (this.mPresentations.get(displayId) == null) {
            KeyguardPresentation createPresentation = createPresentation(display);
            createPresentation.setOnDismissListener(new KeyguardDisplayManager$$ExternalSyntheticLambda0(this, createPresentation, displayId));
            try {
                createPresentation.show();
            } catch (WindowManager.InvalidDisplayException e) {
                Log.w(TAG, "Invalid display:", e);
                createPresentation = null;
            }
            if (createPresentation != null) {
                this.mPresentations.append(displayId, createPresentation);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showPresentation$1$com-android-keyguard-KeyguardDisplayManager */
    public /* synthetic */ void mo25732x6fc0adab(Presentation presentation, int i, DialogInterface dialogInterface) {
        if (presentation.equals(this.mPresentations.get(i))) {
            this.mPresentations.remove(i);
        }
    }

    /* access modifiers changed from: package-private */
    public KeyguardPresentation createPresentation(Display display) {
        return new KeyguardPresentation(this.mContext, display, this.mKeyguardStatusViewComponentFactory);
    }

    /* access modifiers changed from: private */
    public void hidePresentation(int i) {
        Presentation presentation = this.mPresentations.get(i);
        if (presentation != null) {
            presentation.dismiss();
            this.mPresentations.remove(i);
        }
    }

    public void show() {
        if (!this.mShowing) {
            if (DEBUG) {
                Log.v(TAG, "show");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.addCallback(4, this.mMediaRouterCallback, 8);
            } else {
                Log.w(TAG, "MediaRouter not yet initialized");
            }
            updateDisplays(true);
        }
        this.mShowing = true;
    }

    public void hide() {
        if (this.mShowing) {
            if (DEBUG) {
                Log.v(TAG, "hide");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.removeCallback(this.mMediaRouterCallback);
            }
            updateDisplays(false);
        }
        this.mShowing = false;
    }

    /* access modifiers changed from: protected */
    public boolean updateDisplays(boolean z) {
        boolean z2 = false;
        if (z) {
            boolean z3 = false;
            for (Display display : this.mDisplayService.getDisplays()) {
                updateNavigationBarVisibility(display.getDisplayId(), false);
                z3 |= showPresentation(display);
            }
            return z3;
        }
        if (this.mPresentations.size() > 0) {
            z2 = true;
        }
        for (int size = this.mPresentations.size() - 1; size >= 0; size--) {
            updateNavigationBarVisibility(this.mPresentations.keyAt(size), true);
            this.mPresentations.valueAt(size).dismiss();
        }
        this.mPresentations.clear();
        return z2;
    }

    /* access modifiers changed from: private */
    public void updateNavigationBarVisibility(int i, boolean z) {
        NavigationBarView navigationBarView;
        if (i != 0 && (navigationBarView = this.mNavigationBarControllerLazy.get().getNavigationBarView(i)) != null) {
            if (z) {
                navigationBarView.getRootView().setVisibility(0);
            } else {
                navigationBarView.getRootView().setVisibility(8);
            }
        }
    }

    static final class KeyguardPresentation extends Presentation {
        private static final int MOVE_CLOCK_TIMEOUT = 10000;
        private static final int VIDEO_SAFE_REGION = 80;
        /* access modifiers changed from: private */
        public View mClock;
        private KeyguardClockSwitchController mKeyguardClockSwitchController;
        private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
        /* access modifiers changed from: private */
        public int mMarginLeft;
        /* access modifiers changed from: private */
        public int mMarginTop;
        Runnable mMoveTextRunnable = new Runnable() {
            public void run() {
                int access$600 = KeyguardPresentation.this.mMarginLeft + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableWidth - KeyguardPresentation.this.mClock.getWidth()))));
                int access$900 = KeyguardPresentation.this.mMarginTop + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableHeight - KeyguardPresentation.this.mClock.getHeight()))));
                KeyguardPresentation.this.mClock.setTranslationX((float) access$600);
                KeyguardPresentation.this.mClock.setTranslationY((float) access$900);
                KeyguardPresentation.this.mClock.postDelayed(KeyguardPresentation.this.mMoveTextRunnable, 10000);
            }
        };
        /* access modifiers changed from: private */
        public int mUsableHeight;
        /* access modifiers changed from: private */
        public int mUsableWidth;

        public void cancel() {
        }

        KeyguardPresentation(Context context, Display display, KeyguardStatusViewComponent.Factory factory) {
            super(context, display, C1894R.style.Theme_SystemUI_KeyguardPresentation, Types.SQLXML);
            this.mKeyguardStatusViewComponentFactory = factory;
            setCancelable(false);
        }

        public void onDetachedFromWindow() {
            this.mClock.removeCallbacks(this.mMoveTextRunnable);
        }

        public void onDisplayChanged() {
            updateBounds();
            getWindow().getDecorView().requestLayout();
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            updateBounds();
            setContentView(LayoutInflater.from(getContext()).inflate(C1894R.layout.keyguard_presentation, (ViewGroup) null));
            getWindow().getDecorView().setSystemUiVisibility(1792);
            getWindow().getAttributes().setFitInsetsTypes(0);
            getWindow().setNavigationBarContrastEnforced(false);
            getWindow().setNavigationBarColor(0);
            View findViewById = findViewById(C1894R.C1898id.clock);
            this.mClock = findViewById;
            findViewById.post(this.mMoveTextRunnable);
            KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardStatusViewComponentFactory.build((KeyguardStatusView) findViewById(C1894R.C1898id.clock)).getKeyguardClockSwitchController();
            this.mKeyguardClockSwitchController = keyguardClockSwitchController;
            keyguardClockSwitchController.setOnlyClock(true);
            this.mKeyguardClockSwitchController.init();
        }

        private void updateBounds() {
            Rect bounds = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds();
            this.mUsableWidth = (bounds.width() * 80) / 100;
            this.mUsableHeight = (bounds.height() * 80) / 100;
            this.mMarginLeft = (bounds.width() * 20) / 200;
            this.mMarginTop = (bounds.height() * 20) / 200;
        }
    }
}
