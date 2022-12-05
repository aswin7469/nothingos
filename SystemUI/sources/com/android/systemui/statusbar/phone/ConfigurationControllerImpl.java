package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ConfigurationControllerImpl.kt */
/* loaded from: classes.dex */
public final class ConfigurationControllerImpl implements ConfigurationController {
    @NotNull
    private final Context context;
    private int density;
    private float fontScale;
    private final boolean inCarMode;
    private int layoutDirection;
    @Nullable
    private LocaleList localeList;
    private int smallestScreenWidth;
    private int uiMode;
    @NotNull
    private final List<ConfigurationController.ConfigurationListener> listeners = new ArrayList();
    @NotNull
    private final Configuration lastConfig = new Configuration();

    public ConfigurationControllerImpl(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Configuration configuration = context.getResources().getConfiguration();
        this.context = context;
        this.fontScale = configuration.fontScale;
        this.density = configuration.densityDpi;
        this.smallestScreenWidth = configuration.smallestScreenWidthDp;
        int i = configuration.uiMode;
        this.inCarMode = (i & 15) == 3;
        this.uiMode = i & 48;
        this.localeList = configuration.getLocales();
        this.layoutDirection = configuration.getLayoutDirection();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public void notifyThemeChanged() {
        for (ConfigurationController.ConfigurationListener configurationListener : new ArrayList(this.listeners)) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004c, code lost:
        if (r4 == false) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0143 A[ORIG_RETURN, RETURN] */
    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        int i;
        LocaleList locales;
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        ArrayList<ConfigurationController.ConfigurationListener> arrayList = new ArrayList(this.listeners);
        for (ConfigurationController.ConfigurationListener configurationListener : arrayList) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onConfigChanged(newConfig);
            }
        }
        float f = newConfig.fontScale;
        int i2 = newConfig.densityDpi;
        int i3 = newConfig.uiMode & 48;
        boolean z = i3 != this.uiMode;
        if (i2 == this.density) {
            if (f == this.fontScale) {
                if (this.inCarMode) {
                }
                i = newConfig.smallestScreenWidthDp;
                if (i != this.smallestScreenWidth) {
                    this.smallestScreenWidth = i;
                    for (ConfigurationController.ConfigurationListener configurationListener2 : arrayList) {
                        if (this.listeners.contains(configurationListener2)) {
                            configurationListener2.onSmallestScreenWidthChanged();
                        }
                    }
                }
                locales = newConfig.getLocales();
                if (!Intrinsics.areEqual(locales, this.localeList)) {
                    this.localeList = locales;
                    for (ConfigurationController.ConfigurationListener configurationListener3 : arrayList) {
                        if (this.listeners.contains(configurationListener3)) {
                            configurationListener3.onLocaleListChanged();
                        }
                    }
                }
                if (z) {
                    this.context.getTheme().applyStyle(this.context.getThemeResId(), true);
                    this.uiMode = i3;
                    for (ConfigurationController.ConfigurationListener configurationListener4 : arrayList) {
                        if (this.listeners.contains(configurationListener4)) {
                            configurationListener4.onUiModeChanged();
                        }
                    }
                }
                if (this.layoutDirection != newConfig.getLayoutDirection()) {
                    this.layoutDirection = newConfig.getLayoutDirection();
                    for (ConfigurationController.ConfigurationListener configurationListener5 : arrayList) {
                        if (this.listeners.contains(configurationListener5)) {
                            configurationListener5.onLayoutDirectionChanged(this.layoutDirection == 1);
                        }
                    }
                }
                if ((this.lastConfig.updateFrom(newConfig) & Integer.MIN_VALUE) != 0) {
                    return;
                }
                for (ConfigurationController.ConfigurationListener configurationListener6 : arrayList) {
                    if (this.listeners.contains(configurationListener6)) {
                        configurationListener6.onOverlayChanged();
                    }
                }
                return;
            }
        }
        for (ConfigurationController.ConfigurationListener configurationListener7 : arrayList) {
            if (this.listeners.contains(configurationListener7)) {
                configurationListener7.onDensityOrFontScaleChanged();
            }
        }
        this.density = i2;
        this.fontScale = f;
        i = newConfig.smallestScreenWidthDp;
        if (i != this.smallestScreenWidth) {
        }
        locales = newConfig.getLocales();
        if (!Intrinsics.areEqual(locales, this.localeList)) {
        }
        if (z) {
        }
        if (this.layoutDirection != newConfig.getLayoutDirection()) {
        }
        if ((this.lastConfig.updateFrom(newConfig) & Integer.MIN_VALUE) != 0) {
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull ConfigurationController.ConfigurationListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
        listener.onDensityOrFontScaleChanged();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull ConfigurationController.ConfigurationListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public boolean isLayoutRtl() {
        return this.layoutDirection == 1;
    }
}
