package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.DisplayCutout;
import android.view.WindowManager;
import android.view.WindowMetrics;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StatusBarContentInsetsProvider.kt */
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider implements CallbackController<StatusBarContentInsetsChangedListener>, ConfigurationController.ConfigurationListener, Dumpable {
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final Context context;
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final Rect[] insetsByCorner = new Rect[4];
    @NotNull
    private final Set<StatusBarContentInsetsChangedListener> listeners = new LinkedHashSet();
    @NotNull
    private final WindowManager windowManager;

    public StatusBarContentInsetsProvider(@NotNull Context context, @NotNull ConfigurationController configurationController, @NotNull WindowManager windowManager, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(windowManager, "windowManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context;
        this.configurationController = configurationController;
        this.windowManager = windowManager;
        this.dumpManager = dumpManager;
        configurationController.addCallback(this);
        dumpManager.registerDumpable("StatusBarInsetsProvider", this);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull StatusBarContentInsetsChangedListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull StatusBarContentInsetsChangedListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        clearCachedInsets();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onOverlayChanged() {
        clearCachedInsets();
    }

    private final void clearCachedInsets() {
        Rect[] rectArr = this.insetsByCorner;
        rectArr[0] = null;
        rectArr[1] = null;
        rectArr[2] = null;
        rectArr[3] = null;
        notifyInsetsChanged();
    }

    private final void notifyInsetsChanged() {
        for (StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener : this.listeners) {
            statusBarContentInsetsChangedListener.onStatusBarContentInsetsChanged();
        }
    }

    @NotNull
    public final Rect getBoundingRectForPrivacyChipForRotation(int i) {
        Rect rect = this.insetsByCorner[i];
        Resources rotatedResources = RotationUtils.getResourcesForRotation(i, this.context);
        if (rect == null) {
            Intrinsics.checkNotNullExpressionValue(rotatedResources, "rotatedResources");
            rect = getAndSetInsetsForRotation(i, rotatedResources);
        }
        int dimensionPixelSize = rotatedResources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_diameter);
        int dimensionPixelSize2 = rotatedResources.getDimensionPixelSize(R$dimen.ongoing_appops_chip_max_width);
        boolean z = true;
        if (this.context.getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        return StatusBarContentInsetsProviderKt.getPrivacyChipBoundingRectForInsets(rect, dimensionPixelSize, dimensionPixelSize2, z);
    }

    @NotNull
    public final Rect getStatusBarContentInsetsForRotation(int i) {
        Rect rect = this.insetsByCorner[i];
        if (rect == null) {
            Resources rotatedResources = RotationUtils.getResourcesForRotation(i, this.context);
            Intrinsics.checkNotNullExpressionValue(rotatedResources, "rotatedResources");
            return getAndSetInsetsForRotation(i, rotatedResources);
        }
        return rect;
    }

    private final Rect getAndSetInsetsForRotation(int i, Resources resources) {
        Rect calculatedInsetsForRotation = getCalculatedInsetsForRotation(i, resources);
        this.insetsByCorner[i] = calculatedInsetsForRotation;
        return calculatedInsetsForRotation;
    }

    private final Rect getCalculatedInsetsForRotation(int i, Resources resources) {
        int i2;
        int max;
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        int exactRotation = RotationUtils.getExactRotation(this.context);
        boolean z = true;
        if (resources.getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_min_padding);
        if (z) {
            max = dimensionPixelSize;
            i2 = Math.max(dimensionPixelSize2, dimensionPixelSize);
        } else {
            i2 = dimensionPixelSize;
            max = Math.max(dimensionPixelSize2, dimensionPixelSize);
        }
        WindowMetrics maximumWindowMetrics = this.windowManager.getMaximumWindowMetrics();
        Intrinsics.checkNotNullExpressionValue(maximumWindowMetrics, "windowManager.maximumWindowMetrics");
        return StatusBarContentInsetsProviderKt.calculateInsetsForRotationWithRotatedResources(exactRotation, i, cutout, maximumWindowMetrics, resources.getDimensionPixelSize(R$dimen.status_bar_height), i2, max);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        Rect[] rectArr = this.insetsByCorner;
        int length = rectArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            pw.println(((Object) RotationUtils.toString(i2)) + " -> " + rectArr[i]);
            i++;
            i2++;
        }
    }
}
