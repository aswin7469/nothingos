package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Trace;
import android.util.LruCache;
import android.util.Pair;
import android.view.DisplayCutout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import java.p026io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010#\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u00032\u00020\u0004:\u0001CB\u001f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0002H\u0016J\b\u0010 \u001a\u00020\u001eH\u0002J\u0006\u0010!\u001a\u00020\u0017J%\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020$2\u000e\u0010%\u001a\n\u0012\u0006\b\u0001\u0012\u00020'0&H\u0016¢\u0006\u0002\u0010(J*\u0010)\u001a\u00020\u00152\u0006\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u0014H\u0002J\u0018\u00101\u001a\u00020\u00152\u0006\u00102\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-J\u001a\u00103\u001a\u00020\u00142\u0006\u00102\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-H\u0002J\"\u00104\u001a\u00020\u00152\b\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010*\u001a\u00020+2\u0006\u0010.\u001a\u00020/H\u0002J\u0006\u00105\u001a\u00020\u0015J\u0010\u00106\u001a\u00020\u00152\u0006\u00102\u001a\u00020+H\u0007J\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020+08J\u001a\u00109\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020+082\u0006\u00102\u001a\u00020+J\u0017\u0010:\u001a\u00020+2\n\b\u0002\u00102\u001a\u0004\u0018\u00010+¢\u0006\u0002\u0010;J\b\u0010<\u001a\u00020\u001eH\u0002J\b\u0010=\u001a\u00020\u001eH\u0016J\u0010\u0010>\u001a\u00020\u001e2\u0006\u0010?\u001a\u00020\u0017H\u0016J\b\u0010@\u001a\u00020\u001eH\u0016J\b\u0010A\u001a\u00020\u001eH\u0016J\u0010\u0010B\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0002H\u0016R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013X\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0016\u001a\u00020\u00178BX\u0002¢\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u0016\u0010\u0018R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00020\u001cX\u0004¢\u0006\u0002\n\u0000¨\u0006D"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsChangedListener;", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/dump/DumpManager;)V", "getConfigurationController", "()Lcom/android/systemui/statusbar/policy/ConfigurationController;", "getContext", "()Landroid/content/Context;", "getDumpManager", "()Lcom/android/systemui/dump/DumpManager;", "insetsCache", "Landroid/util/LruCache;", "Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider$CacheKey;", "Landroid/graphics/Rect;", "isPrivacyDotEnabled", "", "()Z", "isPrivacyDotEnabled$delegate", "Lkotlin/Lazy;", "listeners", "", "addCallback", "", "listener", "clearCachedInsets", "currentRotationHasCornerCutout", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getAndSetCalculatedAreaForRotation", "targetRotation", "", "displayCutout", "Landroid/view/DisplayCutout;", "rotatedResources", "Landroid/content/res/Resources;", "key", "getBoundingRectForPrivacyChipForRotation", "rotation", "getCacheKey", "getCalculatedAreaForRotation", "getStatusBarContentAreaForCurrentRotation", "getStatusBarContentAreaForRotation", "getStatusBarContentInsetsForCurrentRotation", "Landroid/util/Pair;", "getStatusBarContentInsetsForRotation", "getStatusBarPaddingTop", "(Ljava/lang/Integer;)I", "notifyInsetsChanged", "onDensityOrFontScaleChanged", "onLayoutDirectionChanged", "isLayoutRtl", "onMaxBoundsChanged", "onThemeChanged", "removeCallback", "CacheKey", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarContentInsetsProvider.kt */
public final class StatusBarContentInsetsProvider implements CallbackController<StatusBarContentInsetsChangedListener>, ConfigurationController.ConfigurationListener, Dumpable {
    private final ConfigurationController configurationController;
    private final Context context;
    private final DumpManager dumpManager;
    private final LruCache<CacheKey, Rect> insetsCache = new LruCache<>(16);
    private final Lazy isPrivacyDotEnabled$delegate = LazyKt.lazy(LazyThreadSafetyMode.PUBLICATION, new StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(this));
    private final Set<StatusBarContentInsetsChangedListener> listeners = new LinkedHashSet();

    @Inject
    public StatusBarContentInsetsProvider(Context context2, ConfigurationController configurationController2, DumpManager dumpManager2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        this.context = context2;
        this.configurationController = configurationController2;
        this.dumpManager = dumpManager2;
        configurationController2.addCallback(this);
        dumpManager2.registerDumpable("StatusBarInsetsProvider", this);
    }

    public final Context getContext() {
        return this.context;
    }

    public final ConfigurationController getConfigurationController() {
        return this.configurationController;
    }

    public final DumpManager getDumpManager() {
        return this.dumpManager;
    }

    private final boolean isPrivacyDotEnabled() {
        return ((Boolean) this.isPrivacyDotEnabled$delegate.getValue()).booleanValue();
    }

    public void addCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        Intrinsics.checkNotNullParameter(statusBarContentInsetsChangedListener, "listener");
        this.listeners.add(statusBarContentInsetsChangedListener);
    }

    public void removeCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        Intrinsics.checkNotNullParameter(statusBarContentInsetsChangedListener, "listener");
        this.listeners.remove(statusBarContentInsetsChangedListener);
    }

    public void onDensityOrFontScaleChanged() {
        clearCachedInsets();
    }

    public void onThemeChanged() {
        clearCachedInsets();
    }

    public void onMaxBoundsChanged() {
        notifyInsetsChanged();
    }

    public void onLayoutDirectionChanged(boolean z) {
        clearCachedInsets();
    }

    private final void clearCachedInsets() {
        this.insetsCache.evictAll();
        notifyInsetsChanged();
    }

    private final void notifyInsetsChanged() {
        for (StatusBarContentInsetsChangedListener onStatusBarContentInsetsChanged : this.listeners) {
            onStatusBarContentInsetsChanged.onStatusBarContentInsetsChanged();
        }
    }

    public final boolean currentRotationHasCornerCutout() {
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        if (cutout == null) {
            return false;
        }
        Rect boundingRectTop = cutout.getBoundingRectTop();
        Point point = new Point();
        this.context.getDisplay().getRealSize(point);
        if (boundingRectTop.left <= 0 || boundingRectTop.right >= point.y) {
            return true;
        }
        return false;
    }

    public final Rect getBoundingRectForPrivacyChipForRotation(int i, DisplayCutout displayCutout) {
        Rect rect = this.insetsCache.get(getCacheKey(i, displayCutout));
        if (rect == null) {
            rect = getStatusBarContentAreaForRotation(i);
        }
        Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
        int dimensionPixelSize = resourcesForRotation.getDimensionPixelSize(C1894R.dimen.ongoing_appops_dot_diameter);
        int dimensionPixelSize2 = resourcesForRotation.getDimensionPixelSize(C1894R.dimen.ongoing_appops_chip_max_width);
        boolean isLayoutRtl = this.configurationController.isLayoutRtl();
        Intrinsics.checkNotNullExpressionValue(rect, "insets");
        return StatusBarContentInsetsProviderKt.getPrivacyChipBoundingRectForInsets(rect, dimensionPixelSize, dimensionPixelSize2, isLayoutRtl);
    }

    public final Pair<Integer, Integer> getStatusBarContentInsetsForCurrentRotation() {
        return getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(this.context));
    }

    public final Rect getStatusBarContentAreaForRotation(int i) {
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        CacheKey cacheKey = getCacheKey(i, cutout);
        Rect rect = this.insetsCache.get(cacheKey);
        if (rect != null) {
            return rect;
        }
        Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
        Intrinsics.checkNotNullExpressionValue(resourcesForRotation, "getResourcesForRotation(rotation, context)");
        return getAndSetCalculatedAreaForRotation(i, cutout, resourcesForRotation, cacheKey);
    }

    public final Rect getStatusBarContentAreaForCurrentRotation() {
        return getStatusBarContentAreaForRotation(RotationUtils.getExactRotation(this.context));
    }

    private final Rect getAndSetCalculatedAreaForRotation(int i, DisplayCutout displayCutout, Resources resources, CacheKey cacheKey) {
        Rect calculatedAreaForRotation = getCalculatedAreaForRotation(displayCutout, i, resources);
        this.insetsCache.put(cacheKey, calculatedAreaForRotation);
        return calculatedAreaForRotation;
    }

    private final Rect getCalculatedAreaForRotation(DisplayCutout displayCutout, int i, Resources resources) {
        int i2;
        int i3;
        int exactRotation = RotationUtils.getExactRotation(this.context);
        int dimensionPixelSize = resources.getDimensionPixelSize(C1894R.dimen.rounded_corner_content_padding);
        int dimensionPixelSize2 = isPrivacyDotEnabled() ? resources.getDimensionPixelSize(C1894R.dimen.ongoing_appops_dot_min_padding) : 0;
        int dimensionPixelSize3 = isPrivacyDotEnabled() ? resources.getDimensionPixelSize(C1894R.dimen.ongoing_appops_dot_diameter) : 0;
        if (this.configurationController.isLayoutRtl()) {
            i3 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i2 = dimensionPixelSize;
        } else {
            i2 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i3 = dimensionPixelSize;
        }
        Rect maxBounds = this.context.getResources().getConfiguration().windowConfiguration.getMaxBounds();
        Intrinsics.checkNotNullExpressionValue(maxBounds, "context.resources.config…owConfiguration.maxBounds");
        return StatusBarContentInsetsProviderKt.calculateInsetsForRotationWithRotatedResources(exactRotation, i, displayCutout, maxBounds, SystemBarUtils.getStatusBarHeightForRotation(this.context, i), i3, i2, this.configurationController.isLayoutRtl(), dimensionPixelSize3);
    }

    public static /* synthetic */ int getStatusBarPaddingTop$default(StatusBarContentInsetsProvider statusBarContentInsetsProvider, Integer num, int i, Object obj) {
        if ((i & 1) != 0) {
            num = null;
        }
        return statusBarContentInsetsProvider.getStatusBarPaddingTop(num);
    }

    public final int getStatusBarPaddingTop(Integer num) {
        Resources resources;
        if (num == null || (resources = RotationUtils.getResourcesForRotation(num.intValue(), this.context)) == null) {
            resources = this.context.getResources();
        }
        return resources.getDimensionPixelSize(C1894R.dimen.status_bar_padding_top);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        Map<CacheKey, Rect> snapshot = this.insetsCache.snapshot();
        Intrinsics.checkNotNullExpressionValue(snapshot, "insetsCache.snapshot()");
        for (Map.Entry next : snapshot.entrySet()) {
            printWriter.println(((CacheKey) next.getKey()) + " -> " + ((Rect) next.getValue()));
        }
        printWriter.println((Object) this.insetsCache);
    }

    private final CacheKey getCacheKey(int i, DisplayCutout displayCutout) {
        return new CacheKey(i, new Rect(this.context.getResources().getConfiguration().windowConfiguration.getMaxBounds()), displayCutout);
    }

    @Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007HÆ\u0003J)\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider$CacheKey;", "", "rotation", "", "displaySize", "Landroid/graphics/Rect;", "displayCutout", "Landroid/view/DisplayCutout;", "(ILandroid/graphics/Rect;Landroid/view/DisplayCutout;)V", "getDisplayCutout", "()Landroid/view/DisplayCutout;", "getDisplaySize", "()Landroid/graphics/Rect;", "getRotation", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StatusBarContentInsetsProvider.kt */
    private static final class CacheKey {
        private final DisplayCutout displayCutout;
        private final Rect displaySize;
        private final int rotation;

        public static /* synthetic */ CacheKey copy$default(CacheKey cacheKey, int i, Rect rect, DisplayCutout displayCutout2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = cacheKey.rotation;
            }
            if ((i2 & 2) != 0) {
                rect = cacheKey.displaySize;
            }
            if ((i2 & 4) != 0) {
                displayCutout2 = cacheKey.displayCutout;
            }
            return cacheKey.copy(i, rect, displayCutout2);
        }

        public final int component1() {
            return this.rotation;
        }

        public final Rect component2() {
            return this.displaySize;
        }

        public final DisplayCutout component3() {
            return this.displayCutout;
        }

        public final CacheKey copy(int i, Rect rect, DisplayCutout displayCutout2) {
            Intrinsics.checkNotNullParameter(rect, "displaySize");
            return new CacheKey(i, rect, displayCutout2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CacheKey)) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) obj;
            return this.rotation == cacheKey.rotation && Intrinsics.areEqual((Object) this.displaySize, (Object) cacheKey.displaySize) && Intrinsics.areEqual((Object) this.displayCutout, (Object) cacheKey.displayCutout);
        }

        public int hashCode() {
            int hashCode = ((Integer.hashCode(this.rotation) * 31) + this.displaySize.hashCode()) * 31;
            DisplayCutout displayCutout2 = this.displayCutout;
            return hashCode + (displayCutout2 == null ? 0 : displayCutout2.hashCode());
        }

        public String toString() {
            return "CacheKey(rotation=" + this.rotation + ", displaySize=" + this.displaySize + ", displayCutout=" + this.displayCutout + ')';
        }

        public CacheKey(int i, Rect rect, DisplayCutout displayCutout2) {
            Intrinsics.checkNotNullParameter(rect, "displaySize");
            this.rotation = i;
            this.displaySize = rect;
            this.displayCutout = displayCutout2;
        }

        public final int getRotation() {
            return this.rotation;
        }

        public final Rect getDisplaySize() {
            return this.displaySize;
        }

        public final DisplayCutout getDisplayCutout() {
            return this.displayCutout;
        }
    }

    public final Pair<Integer, Integer> getStatusBarContentInsetsForRotation(int i) {
        Trace.beginSection("StatusBarContentInsetsProvider.getStatusBarContentInsetsForRotation");
        try {
            DisplayCutout cutout = this.context.getDisplay().getCutout();
            CacheKey cacheKey = getCacheKey(i, cutout);
            Rect maxBounds = this.context.getResources().getConfiguration().windowConfiguration.getMaxBounds();
            Point point = new Point(maxBounds.width(), maxBounds.height());
            StatusBarContentInsetsProviderKt.orientToRotZero(point, RotationUtils.getExactRotation(this.context));
            int access$logicalWidth = StatusBarContentInsetsProviderKt.logicalWidth(point, i);
            Rect rect = this.insetsCache.get(cacheKey);
            if (rect == null) {
                Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
                Intrinsics.checkNotNullExpressionValue(resourcesForRotation, "getResourcesForRotation(rotation, context)");
                rect = getAndSetCalculatedAreaForRotation(i, cutout, resourcesForRotation, cacheKey);
            } else {
                Intrinsics.checkNotNullExpressionValue(rect, "insetsCache[key] ?: getA…(rotation, context), key)");
            }
            return new Pair<>(Integer.valueOf(rect.left), Integer.valueOf(access$logicalWidth - rect.right));
        } finally {
            Trace.endSection();
        }
    }
}
