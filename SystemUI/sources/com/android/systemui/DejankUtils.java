package com.android.systemui;

import android.os.Binder;
import android.os.Handler;
import android.view.Choreographer;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.function.Supplier;

public class DejankUtils {
    public static final boolean STRICT_MODE_ENABLED;
    private static final Runnable sAnimationCallbackRunnable = new DejankUtils$$ExternalSyntheticLambda0();
    /* access modifiers changed from: private */
    public static Stack<String> sBlockingIpcs = new Stack<>();
    private static final Choreographer sChoreographer = Choreographer.getInstance();
    private static final Handler sHandler = new Handler();
    private static boolean sImmediate;
    /* access modifiers changed from: private */
    public static final Object sLock = new Object();
    private static final ArrayList<Runnable> sPendingRunnables = new ArrayList<>();
    private static final Binder.ProxyTransactListener sProxy;
    /* access modifiers changed from: private */
    public static boolean sTemporarilyIgnoreStrictMode;
    /* access modifiers changed from: private */
    public static final HashSet<String> sWhitelistedFrameworkClasses;

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000c, code lost:
        if (android.os.SystemProperties.getBoolean("persist.sysui.strictmode", false) != false) goto L_0x000e;
     */
    static {
        /*
            boolean r0 = android.os.Build.IS_ENG
            if (r0 != 0) goto L_0x000e
            java.lang.String r0 = "persist.sysui.strictmode"
            r1 = 0
            boolean r0 = android.os.SystemProperties.getBoolean(r0, r1)
            if (r0 == 0) goto L_0x000f
        L_0x000e:
            r1 = 1
        L_0x000f:
            STRICT_MODE_ENABLED = r1
            android.view.Choreographer r0 = android.view.Choreographer.getInstance()
            sChoreographer = r0
            android.os.Handler r0 = new android.os.Handler
            r0.<init>()
            sHandler = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            sPendingRunnables = r0
            java.util.Stack r0 = new java.util.Stack
            r0.<init>()
            sBlockingIpcs = r0
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            sWhitelistedFrameworkClasses = r0
            java.lang.Object r2 = new java.lang.Object
            r2.<init>()
            sLock = r2
            com.android.systemui.DejankUtils$1 r2 = new com.android.systemui.DejankUtils$1
            r2.<init>()
            sProxy = r2
            if (r1 == 0) goto L_0x0072
            java.lang.String r1 = "android.view.IWindowSession"
            r0.add(r1)
            java.lang.String r1 = "com.android.internal.policy.IKeyguardStateCallback"
            r0.add(r1)
            java.lang.String r1 = "android.os.IPowerManager"
            r0.add(r1)
            java.lang.String r1 = "com.android.internal.statusbar.IStatusBarService"
            r0.add(r1)
            android.os.Binder.setProxyTransactListener(r2)
            android.os.StrictMode$ThreadPolicy$Builder r0 = new android.os.StrictMode$ThreadPolicy$Builder
            r0.<init>()
            android.os.StrictMode$ThreadPolicy$Builder r0 = r0.detectCustomSlowCalls()
            android.os.StrictMode$ThreadPolicy$Builder r0 = r0.penaltyFlashScreen()
            android.os.StrictMode$ThreadPolicy$Builder r0 = r0.penaltyLog()
            android.os.StrictMode$ThreadPolicy r0 = r0.build()
            android.os.StrictMode.setThreadPolicy(r0)
        L_0x0072:
            com.android.systemui.DejankUtils$$ExternalSyntheticLambda0 r0 = new com.android.systemui.DejankUtils$$ExternalSyntheticLambda0
            r0.<init>()
            sAnimationCallbackRunnable = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.DejankUtils.<clinit>():void");
    }

    static /* synthetic */ void lambda$static$0() {
        int i = 0;
        while (true) {
            ArrayList<Runnable> arrayList = sPendingRunnables;
            if (i < arrayList.size()) {
                sHandler.post(arrayList.get(i));
                i++;
            } else {
                arrayList.clear();
                return;
            }
        }
    }

    public static void detectBlockingIpcs(Runnable runnable) {
        if (!STRICT_MODE_ENABLED || !sBlockingIpcs.empty()) {
            runnable.run();
            return;
        }
        Object obj = sLock;
        synchronized (obj) {
            sBlockingIpcs.push("detectBlockingIpcs");
        }
        try {
            runnable.run();
            synchronized (obj) {
                sBlockingIpcs.pop();
            }
        } catch (Throwable th) {
            synchronized (sLock) {
                sBlockingIpcs.pop();
                throw th;
            }
        }
    }

    public static void startDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.push(str);
            }
        }
    }

    public static void stopDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.remove((Object) str);
            }
        }
    }

    public static void whitelistIpcs(Runnable runnable) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            runnable.run();
            return;
        }
        Object obj = sLock;
        synchronized (obj) {
            sTemporarilyIgnoreStrictMode = true;
        }
        try {
            runnable.run();
            synchronized (obj) {
                sTemporarilyIgnoreStrictMode = false;
            }
        } catch (Throwable th) {
            synchronized (sLock) {
                sTemporarilyIgnoreStrictMode = false;
                throw th;
            }
        }
    }

    public static <T> T whitelistIpcs(Supplier<T> supplier) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            return supplier.get();
        }
        Object obj = sLock;
        synchronized (obj) {
            sTemporarilyIgnoreStrictMode = true;
        }
        try {
            T t = supplier.get();
            synchronized (obj) {
                sTemporarilyIgnoreStrictMode = false;
            }
            return t;
        } catch (Throwable th) {
            synchronized (sLock) {
                sTemporarilyIgnoreStrictMode = false;
                throw th;
            }
        }
    }

    public static void postAfterTraversal(Runnable runnable) {
        if (sImmediate) {
            runnable.run();
            return;
        }
        Assert.isMainThread();
        sPendingRunnables.add(runnable);
        postAnimationCallback();
    }

    public static void removeCallbacks(Runnable runnable) {
        Assert.isMainThread();
        sPendingRunnables.remove((Object) runnable);
        sHandler.removeCallbacks(runnable);
    }

    private static void postAnimationCallback() {
        sChoreographer.postCallback(1, sAnimationCallbackRunnable, (Object) null);
    }

    public static void setImmediate(boolean z) {
        sImmediate = z;
    }
}
