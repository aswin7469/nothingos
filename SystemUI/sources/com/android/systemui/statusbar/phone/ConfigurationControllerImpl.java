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
@Metadata(mo65042d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0010H\u0016J\b\u0010\u001d\u001a\u00020\u0006H\u0016J\b\u0010\u001e\u001a\u00020\nH\u0016J\b\u0010\u001f\u001a\u00020\u001bH\u0016J\u0010\u0010 \u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\fH\u0016J\u0010\u0010\"\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/ConfigurationControllerImpl;", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "density", "", "fontScale", "", "inCarMode", "", "lastConfig", "Landroid/content/res/Configuration;", "layoutDirection", "listeners", "", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "localeList", "Landroid/os/LocaleList;", "maxBounds", "Landroid/graphics/Rect;", "orientation", "smallestScreenWidth", "uiMode", "uiModeDelayRunnable", "Ljava/lang/Runnable;", "addCallback", "", "listener", "getOrientation", "isLayoutRtl", "notifyThemeChanged", "onConfigurationChanged", "newConfig", "removeCallback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
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
    private int orientation;
    private int smallestScreenWidth;
    private int uiMode;
    private final Runnable uiModeDelayRunnable = new ConfigurationControllerImpl$$ExternalSyntheticLambda0(this);

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
        this.orientation = configuration.orientation;
    }

    /* access modifiers changed from: private */
    /* renamed from: uiModeDelayRunnable$lambda-2  reason: not valid java name */
    public static final void m3181uiModeDelayRunnable$lambda2(ConfigurationControllerImpl configurationControllerImpl) {
        Intrinsics.checkNotNullParameter(configurationControllerImpl, "this$0");
        for (ConfigurationController.ConfigurationListener configurationListener : configurationControllerImpl.listeners) {
            if (configurationControllerImpl.listeners.contains(configurationListener)) {
                configurationListener.onUiModeChangedDelayCheck();
            }
        }
    }

    public void notifyThemeChanged() {
        for (ConfigurationController.ConfigurationListener configurationListener : new ArrayList(this.listeners)) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0056, code lost:
        if (r4 == false) goto L_0x0078;
     */
    /* JADX WARNING: Removed duplicated region for block: B:125:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f4  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x016e  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0199  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigurationChanged(android.content.res.Configuration r12) {
        /*
            r11 = this;
            java.lang.String r0 = "newConfig"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.util.ArrayList r0 = new java.util.ArrayList
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r11.listeners
            java.util.Collection r1 = (java.util.Collection) r1
            r0.<init>(r1)
            java.util.Collection r0 = (java.util.Collection) r0
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r1 = r0.iterator()
        L_0x0016:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0030
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r11.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0016
            if (r2 == 0) goto L_0x0016
            r2.onConfigChanged(r12)
            goto L_0x0016
        L_0x0030:
            float r1 = r12.fontScale
            int r2 = r12.densityDpi
            int r3 = r12.uiMode
            r3 = r3 & 48
            int r4 = r11.uiMode
            r5 = 0
            r6 = 1
            if (r3 == r4) goto L_0x0040
            r4 = r6
            goto L_0x0041
        L_0x0040:
            r4 = r5
        L_0x0041:
            int r7 = r12.orientation
            int r8 = r11.density
            if (r2 != r8) goto L_0x0058
            float r8 = r11.fontScale
            int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r8 != 0) goto L_0x004f
            r8 = r6
            goto L_0x0050
        L_0x004f:
            r8 = r5
        L_0x0050:
            if (r8 == 0) goto L_0x0058
            boolean r8 = r11.inCarMode
            if (r8 == 0) goto L_0x0078
            if (r4 == 0) goto L_0x0078
        L_0x0058:
            java.util.Iterator r8 = r0.iterator()
        L_0x005c:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L_0x0074
            java.lang.Object r9 = r8.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r9 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r9
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r10 = r11.listeners
            boolean r10 = r10.contains(r9)
            if (r10 == 0) goto L_0x005c
            r9.onDensityOrFontScaleChanged()
            goto L_0x005c
        L_0x0074:
            r11.density = r2
            r11.fontScale = r1
        L_0x0078:
            int r1 = r12.smallestScreenWidthDp
            int r2 = r11.smallestScreenWidth
            if (r1 == r2) goto L_0x009c
            r11.smallestScreenWidth = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x0084:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x009c
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r8 = r11.listeners
            boolean r8 = r8.contains(r2)
            if (r8 == 0) goto L_0x0084
            r2.onSmallestScreenWidthChanged()
            goto L_0x0084
        L_0x009c:
            android.app.WindowConfiguration r1 = r12.windowConfiguration
            android.graphics.Rect r1 = r1.getMaxBounds()
            android.graphics.Rect r2 = r11.maxBounds
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00c8
            r11.maxBounds = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00b0:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00c8
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r8 = r11.listeners
            boolean r8 = r8.contains(r2)
            if (r8 == 0) goto L_0x00b0
            r2.onMaxBoundsChanged()
            goto L_0x00b0
        L_0x00c8:
            android.os.LocaleList r1 = r12.getLocales()
            android.os.LocaleList r2 = r11.localeList
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00f2
            r11.localeList = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00da:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00f2
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r8 = r11.listeners
            boolean r8 = r8.contains(r2)
            if (r8 == 0) goto L_0x00da
            r2.onLocaleListChanged()
            goto L_0x00da
        L_0x00f2:
            if (r4 == 0) goto L_0x0139
            android.content.Context r1 = r11.context
            android.content.res.Resources$Theme r1 = r1.getTheme()
            android.content.Context r2 = r11.context
            int r2 = r2.getThemeResId()
            r1.applyStyle(r2, r6)
            r11.uiMode = r3
            java.util.Iterator r1 = r0.iterator()
        L_0x0109:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0121
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r11.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0109
            r2.onUiModeChanged()
            goto L_0x0109
        L_0x0121:
            android.content.Context r1 = r11.context
            android.os.Handler r1 = r1.getMainThreadHandler()
            java.lang.Runnable r2 = r11.uiModeDelayRunnable
            r1.removeCallbacks(r2)
            android.content.Context r1 = r11.context
            android.os.Handler r1 = r1.getMainThreadHandler()
            java.lang.Runnable r2 = r11.uiModeDelayRunnable
            r3 = 5000(0x1388, double:2.4703E-320)
            r1.postDelayed(r2, r3)
        L_0x0139:
            int r1 = r11.layoutDirection
            int r2 = r12.getLayoutDirection()
            if (r1 == r2) goto L_0x016a
            int r1 = r12.getLayoutDirection()
            r11.layoutDirection = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x014b:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x016a
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r11.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x014b
            int r3 = r11.layoutDirection
            if (r3 != r6) goto L_0x0165
            r3 = r6
            goto L_0x0166
        L_0x0165:
            r3 = r5
        L_0x0166:
            r2.onLayoutDirectionChanged(r3)
            goto L_0x014b
        L_0x016a:
            int r1 = r11.orientation
            if (r7 == r1) goto L_0x018e
            r11.orientation = r7
            java.util.Iterator r1 = r0.iterator()
        L_0x0174:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x018e
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r11.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0174
            int r3 = r11.orientation
            r2.onOrientationChanged(r3)
            goto L_0x0174
        L_0x018e:
            android.content.res.Configuration r1 = r11.lastConfig
            int r12 = r1.updateFrom(r12)
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r12 = r12 & r1
            if (r12 == 0) goto L_0x01b5
            java.util.Iterator r12 = r0.iterator()
        L_0x019d:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x01b5
            java.lang.Object r0 = r12.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r0 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r0
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r11.listeners
            boolean r1 = r1.contains(r0)
            if (r1 == 0) goto L_0x019d
            r0.onThemeChanged()
            goto L_0x019d
        L_0x01b5:
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

    public int getOrientation() {
        return this.orientation;
    }
}
