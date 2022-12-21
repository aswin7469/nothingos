package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.CrossWindowBlurListeners;
import android.view.SurfaceControl;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ \u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\b\u0010\u001a\u001a\u00020\u001bH\u0017J%\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020!0 H\u0016¢\u0006\u0002\u0010\"J\u000e\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u0018J\b\u0010%\u001a\u00020\u0016H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, mo64987d2 = {"Lcom/android/systemui/statusbar/BlurUtils;", "Lcom/android/systemui/Dumpable;", "resources", "Landroid/content/res/Resources;", "crossWindowBlurListeners", "Landroid/view/CrossWindowBlurListeners;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/res/Resources;Landroid/view/CrossWindowBlurListeners;Lcom/android/systemui/dump/DumpManager;)V", "lastAppliedBlur", "", "maxBlurRadius", "getMaxBlurRadius", "()I", "minBlurRadius", "getMinBlurRadius", "applyBlur", "", "viewRootImpl", "Landroid/view/ViewRootImpl;", "radius", "opaque", "", "blurRadiusOfRatio", "", "ratio", "createTransaction", "Landroid/view/SurfaceControl$Transaction;", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "ratioOfBlurRadius", "blur", "supportsBlursOnWindows", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BlurUtils.kt */
public class BlurUtils implements Dumpable {
    private final CrossWindowBlurListeners crossWindowBlurListeners;
    private int lastAppliedBlur;
    private final int maxBlurRadius;
    private final int minBlurRadius;
    private final Resources resources;

    @Inject
    public BlurUtils(@Main Resources resources2, CrossWindowBlurListeners crossWindowBlurListeners2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(resources2, "resources");
        Intrinsics.checkNotNullParameter(crossWindowBlurListeners2, "crossWindowBlurListeners");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.resources = resources2;
        this.crossWindowBlurListeners = crossWindowBlurListeners2;
        this.minBlurRadius = resources2.getDimensionPixelSize(C1893R.dimen.min_window_blur_radius);
        this.maxBlurRadius = resources2.getDimensionPixelSize(C1893R.dimen.max_window_blur_radius);
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    public final int getMinBlurRadius() {
        return this.minBlurRadius;
    }

    public final int getMaxBlurRadius() {
        return this.maxBlurRadius;
    }

    public final float blurRadiusOfRatio(float f) {
        if (f == 0.0f) {
            return 0.0f;
        }
        return MathUtils.lerp((float) this.minBlurRadius, (float) this.maxBlurRadius, f);
    }

    public final float ratioOfBlurRadius(float f) {
        if (f == 0.0f) {
            return 0.0f;
        }
        return MathUtils.map((float) this.minBlurRadius, (float) this.maxBlurRadius, 0.0f, 1.0f, f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004a, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004b, code lost:
        kotlin.p028io.CloseableKt.closeFinally(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004e, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void applyBlur(android.view.ViewRootImpl r4, int r5, boolean r6) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x004f
            android.view.SurfaceControl r0 = r4.getSurfaceControl()
            boolean r0 = r0.isValid()
            if (r0 != 0) goto L_0x000d
            goto L_0x004f
        L_0x000d:
            android.view.SurfaceControl$Transaction r0 = r3.createTransaction()
            java.io.Closeable r0 = (java.p026io.Closeable) r0
            r1 = r0
            android.view.SurfaceControl$Transaction r1 = (android.view.SurfaceControl.Transaction) r1     // Catch:{ all -> 0x0048 }
            boolean r2 = r3.supportsBlursOnWindows()     // Catch:{ all -> 0x0048 }
            if (r2 == 0) goto L_0x0037
            android.view.SurfaceControl r2 = r4.getSurfaceControl()     // Catch:{ all -> 0x0048 }
            r1.setBackgroundBlurRadius(r2, r5)     // Catch:{ all -> 0x0048 }
            int r2 = r3.lastAppliedBlur     // Catch:{ all -> 0x0048 }
            if (r2 != 0) goto L_0x002c
            if (r5 == 0) goto L_0x002c
            r1.setEarlyWakeupStart()     // Catch:{ all -> 0x0048 }
        L_0x002c:
            int r2 = r3.lastAppliedBlur     // Catch:{ all -> 0x0048 }
            if (r2 == 0) goto L_0x0035
            if (r5 != 0) goto L_0x0035
            r1.setEarlyWakeupEnd()     // Catch:{ all -> 0x0048 }
        L_0x0035:
            r3.lastAppliedBlur = r5     // Catch:{ all -> 0x0048 }
        L_0x0037:
            android.view.SurfaceControl r3 = r4.getSurfaceControl()     // Catch:{ all -> 0x0048 }
            r1.setOpaque(r3, r6)     // Catch:{ all -> 0x0048 }
            r1.apply()     // Catch:{ all -> 0x0048 }
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0048 }
            r3 = 0
            kotlin.p028io.CloseableKt.closeFinally(r0, r3)
            return
        L_0x0048:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x004a }
        L_0x004a:
            r4 = move-exception
            kotlin.p028io.CloseableKt.closeFinally(r0, r3)
            throw r4
        L_0x004f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.BlurUtils.applyBlur(android.view.ViewRootImpl, int, boolean):void");
    }

    public SurfaceControl.Transaction createTransaction() {
        return new SurfaceControl.Transaction();
    }

    public boolean supportsBlursOnWindows() {
        if (!CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED || !ActivityManager.isHighEndGfx() || !this.crossWindowBlurListeners.isCrossWindowBlurEnabled() || SystemProperties.getBoolean("persist.sysui.disableBlur", false)) {
            return false;
        }
        return true;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("BlurUtils:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("minBlurRadius: " + this.minBlurRadius);
        indentingPrintWriter.println("maxBlurRadius: " + this.maxBlurRadius);
        indentingPrintWriter.println("supportsBlursOnWindows: " + supportsBlursOnWindows());
        indentingPrintWriter.println("CROSS_WINDOW_BLUR_SUPPORTED: " + CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED);
        indentingPrintWriter.println("isHighEndGfx: " + ActivityManager.isHighEndGfx());
    }
}
