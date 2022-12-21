package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.LocaleList;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\nH\u0016J\b\u0010\u001b\u001a\u00020\u0018H\u0016J\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\fH\u0016J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/ConfigurationControllerImpl;", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "density", "", "fontScale", "", "inCarMode", "", "lastConfig", "Landroid/content/res/Configuration;", "layoutDirection", "listeners", "", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "localeList", "Landroid/os/LocaleList;", "maxBounds", "Landroid/graphics/Rect;", "smallestScreenWidth", "uiMode", "addCallback", "", "listener", "isLayoutRtl", "notifyThemeChanged", "onConfigurationChanged", "newConfig", "removeCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConfigurationControllerImpl.kt */
public final class ConfigurationControllerImpl implements ConfigurationController {
    private final Context context;
    private int density;
    private float fontScale;
    private final boolean inCarMode;
    private final Configuration lastConfig = new Configuration();
    private int layoutDirection;
    private final List<ConfigurationController.ConfigurationListener> listeners = new ArrayList();
    private LocaleList localeList;
    private Rect maxBounds;
    private int smallestScreenWidth;
    private int uiMode;

    @Inject
    public ConfigurationControllerImpl(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Configuration configuration = context2.getResources().getConfiguration();
        this.context = context2;
        this.fontScale = configuration.fontScale;
        this.density = configuration.densityDpi;
        this.smallestScreenWidth = configuration.smallestScreenWidthDp;
        this.inCarMode = (configuration.uiMode & 15) == 3;
        this.uiMode = configuration.uiMode & 48;
        this.localeList = configuration.getLocales();
        this.layoutDirection = configuration.getLayoutDirection();
    }

    public void notifyThemeChanged() {
        for (ConfigurationController.ConfigurationListener configurationListener : new ArrayList(this.listeners)) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        if (r4 == false) goto L_0x0074;
     */
    /* JADX WARNING: Removed duplicated region for block: B:110:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0159  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigurationChanged(android.content.res.Configuration r11) {
        /*
            r10 = this;
            java.lang.String r0 = "newConfig"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.util.ArrayList r0 = new java.util.ArrayList
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r10.listeners
            java.util.Collection r1 = (java.util.Collection) r1
            r0.<init>(r1)
            java.util.Collection r0 = (java.util.Collection) r0
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r1 = r0.iterator()
        L_0x0016:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x002e
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0016
            r2.onConfigChanged(r11)
            goto L_0x0016
        L_0x002e:
            float r1 = r11.fontScale
            int r2 = r11.densityDpi
            int r3 = r11.uiMode
            r3 = r3 & 48
            int r4 = r10.uiMode
            r5 = 0
            r6 = 1
            if (r3 == r4) goto L_0x003e
            r4 = r6
            goto L_0x003f
        L_0x003e:
            r4 = r5
        L_0x003f:
            int r7 = r10.density
            if (r2 != r7) goto L_0x0054
            float r7 = r10.fontScale
            int r7 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r7 != 0) goto L_0x004b
            r7 = r6
            goto L_0x004c
        L_0x004b:
            r7 = r5
        L_0x004c:
            if (r7 == 0) goto L_0x0054
            boolean r7 = r10.inCarMode
            if (r7 == 0) goto L_0x0074
            if (r4 == 0) goto L_0x0074
        L_0x0054:
            java.util.Iterator r7 = r0.iterator()
        L_0x0058:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x0070
            java.lang.Object r8 = r7.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r8 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r8
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r9 = r10.listeners
            boolean r9 = r9.contains(r8)
            if (r9 == 0) goto L_0x0058
            r8.onDensityOrFontScaleChanged()
            goto L_0x0058
        L_0x0070:
            r10.density = r2
            r10.fontScale = r1
        L_0x0074:
            int r1 = r11.smallestScreenWidthDp
            int r2 = r10.smallestScreenWidth
            if (r1 == r2) goto L_0x0098
            r10.smallestScreenWidth = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x0080:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0098
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x0080
            r2.onSmallestScreenWidthChanged()
            goto L_0x0080
        L_0x0098:
            android.app.WindowConfiguration r1 = r11.windowConfiguration
            android.graphics.Rect r1 = r1.getMaxBounds()
            android.graphics.Rect r2 = r10.maxBounds
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00c4
            r10.maxBounds = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00ac:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00c4
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x00ac
            r2.onMaxBoundsChanged()
            goto L_0x00ac
        L_0x00c4:
            android.os.LocaleList r1 = r11.getLocales()
            android.os.LocaleList r2 = r10.localeList
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00ee
            r10.localeList = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00d6:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00ee
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x00d6
            r2.onLocaleListChanged()
            goto L_0x00d6
        L_0x00ee:
            if (r4 == 0) goto L_0x011d
            android.content.Context r1 = r10.context
            android.content.res.Resources$Theme r1 = r1.getTheme()
            android.content.Context r2 = r10.context
            int r2 = r2.getThemeResId()
            r1.applyStyle(r2, r6)
            r10.uiMode = r3
            java.util.Iterator r1 = r0.iterator()
        L_0x0105:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x011d
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0105
            r2.onUiModeChanged()
            goto L_0x0105
        L_0x011d:
            int r1 = r10.layoutDirection
            int r2 = r11.getLayoutDirection()
            if (r1 == r2) goto L_0x014e
            int r1 = r11.getLayoutDirection()
            r10.layoutDirection = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x012f:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x014e
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x012f
            int r3 = r10.layoutDirection
            if (r3 != r6) goto L_0x0149
            r3 = r6
            goto L_0x014a
        L_0x0149:
            r3 = r5
        L_0x014a:
            r2.onLayoutDirectionChanged(r3)
            goto L_0x012f
        L_0x014e:
            android.content.res.Configuration r1 = r10.lastConfig
            int r11 = r1.updateFrom(r11)
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r11 = r11 & r1
            if (r11 == 0) goto L_0x0175
            java.util.Iterator r11 = r0.iterator()
        L_0x015d:
            boolean r0 = r11.hasNext()
            if (r0 == 0) goto L_0x0175
            java.lang.Object r0 = r11.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r0 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r0
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r10.listeners
            boolean r1 = r1.contains(r0)
            if (r1 == 0) goto L_0x015d
            r0.onThemeChanged()
            goto L_0x015d
        L_0x0175:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ConfigurationControllerImpl.onConfigurationChanged(android.content.res.Configuration):void");
    }

    public void addCallback(ConfigurationController.ConfigurationListener configurationListener) {
        Intrinsics.checkNotNullParameter(configurationListener, "listener");
        this.listeners.add(configurationListener);
        configurationListener.onDensityOrFontScaleChanged();
    }

    public void removeCallback(ConfigurationController.ConfigurationListener configurationListener) {
        Intrinsics.checkNotNullParameter(configurationListener, "listener");
        this.listeners.remove((Object) configurationListener);
    }

    public boolean isLayoutRtl() {
        return this.layoutDirection == 1;
    }
}
