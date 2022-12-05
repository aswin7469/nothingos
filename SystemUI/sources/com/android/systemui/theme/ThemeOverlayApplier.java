package com.android.systemui.theme;

import android.content.om.FabricatedOverlay;
import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.om.OverlayManagerTransaction;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.google.android.collect.Lists;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class ThemeOverlayApplier implements Dumpable {
    static final String ANDROID_PACKAGE = "android";
    static final String SETTINGS_PACKAGE = "com.android.settings";
    static final String SYSUI_PACKAGE = "com.android.systemui";
    private final Executor mBgExecutor;
    private final Map<String, String> mCategoryToTargetPackage;
    private final String mLauncherPackage;
    private final Executor mMainExecutor;
    private final OverlayManager mOverlayManager;
    private final Map<String, Set<String>> mTargetPackageToCategories;
    private final String mThemePickerPackage;
    private static final boolean DEBUG = Log.isLoggable("ThemeOverlayApplier", 3);
    static final String OVERLAY_CATEGORY_ICON_LAUNCHER = "android.theme.customization.icon_pack.launcher";
    static final String OVERLAY_CATEGORY_SHAPE = "android.theme.customization.adaptive_icon_shape";
    static final String OVERLAY_CATEGORY_FONT = "android.theme.customization.font";
    static final String OVERLAY_CATEGORY_ICON_ANDROID = "android.theme.customization.icon_pack.android";
    static final String OVERLAY_CATEGORY_ICON_SYSUI = "android.theme.customization.icon_pack.systemui";
    static final String OVERLAY_CATEGORY_ICON_SETTINGS = "android.theme.customization.icon_pack.settings";
    static final String OVERLAY_CATEGORY_ICON_THEME_PICKER = "android.theme.customization.icon_pack.themepicker";
    static final List<String> THEME_CATEGORIES = Lists.newArrayList(new String[]{"android.theme.customization.system_palette", OVERLAY_CATEGORY_ICON_LAUNCHER, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_FONT, "android.theme.customization.accent_color", OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI, OVERLAY_CATEGORY_ICON_SETTINGS, OVERLAY_CATEGORY_ICON_THEME_PICKER});
    static final Set<String> SYSTEM_USER_CATEGORIES = Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI});

    public ThemeOverlayApplier(OverlayManager overlayManager, Executor executor, Executor executor2, String str, String str2, DumpManager dumpManager) {
        ArrayMap arrayMap = new ArrayMap();
        this.mTargetPackageToCategories = arrayMap;
        ArrayMap arrayMap2 = new ArrayMap();
        this.mCategoryToTargetPackage = arrayMap2;
        this.mOverlayManager = overlayManager;
        this.mBgExecutor = executor;
        this.mMainExecutor = executor2;
        this.mLauncherPackage = str;
        this.mThemePickerPackage = str2;
        arrayMap.put(ANDROID_PACKAGE, Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID}));
        arrayMap.put(SYSUI_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SYSUI}));
        arrayMap.put(SETTINGS_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SETTINGS}));
        arrayMap.put(str, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_LAUNCHER}));
        arrayMap.put(str2, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_THEME_PICKER}));
        arrayMap2.put("android.theme.customization.accent_color", ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_FONT, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_SHAPE, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_ANDROID, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SYSUI, SYSUI_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SETTINGS, SETTINGS_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_LAUNCHER, str);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_THEME_PICKER, str2);
        dumpManager.registerDumpable("ThemeOverlayApplier", this);
    }

    public void applyCurrentUserOverlays(final Map<String, OverlayIdentifier> map, final FabricatedOverlay[] fabricatedOverlayArr, final int i, final Set<UserHandle> set) {
        this.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$7(map, fabricatedOverlayArr, i, set);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentUserOverlays$7(final Map map, FabricatedOverlay[] fabricatedOverlayArr, int i, Set set) {
        final HashSet hashSet = new HashSet(THEME_CATEGORIES);
        final ArrayList arrayList = new ArrayList();
        ((Set) hashSet.stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$applyCurrentUserOverlays$0;
                lambda$applyCurrentUserOverlays$0 = ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$0((String) obj);
                return lambda$applyCurrentUserOverlays$0;
            }
        }).collect(Collectors.toSet())).forEach(new Consumer() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$1(arrayList, (String) obj);
            }
        });
        List<Pair> list = (List) arrayList.stream().filter(new Predicate() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$applyCurrentUserOverlays$2;
                lambda$applyCurrentUserOverlays$2 = ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$2((OverlayInfo) obj);
                return lambda$applyCurrentUserOverlays$2;
            }
        }).filter(new Predicate() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$applyCurrentUserOverlays$3;
                lambda$applyCurrentUserOverlays$3 = ThemeOverlayApplier.lambda$applyCurrentUserOverlays$3(hashSet, (OverlayInfo) obj);
                return lambda$applyCurrentUserOverlays$3;
            }
        }).filter(new Predicate() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$applyCurrentUserOverlays$4;
                lambda$applyCurrentUserOverlays$4 = ThemeOverlayApplier.lambda$applyCurrentUserOverlays$4(map, (OverlayInfo) obj);
                return lambda$applyCurrentUserOverlays$4;
            }
        }).filter(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE).map(ThemeOverlayApplier$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
        OverlayManagerTransaction.Builder transactionBuilder = getTransactionBuilder();
        HashSet hashSet2 = new HashSet();
        if (fabricatedOverlayArr != null) {
            for (FabricatedOverlay fabricatedOverlay : fabricatedOverlayArr) {
                hashSet2.add(fabricatedOverlay.getIdentifier());
                transactionBuilder.registerFabricatedOverlay(fabricatedOverlay);
            }
        }
        for (Pair pair : list) {
            OverlayIdentifier overlayIdentifier = new OverlayIdentifier((String) pair.second);
            setEnabled(transactionBuilder, overlayIdentifier, (String) pair.first, i, set, false, hashSet2.contains(overlayIdentifier));
        }
        for (String str : THEME_CATEGORIES) {
            if (map.containsKey(str)) {
                OverlayIdentifier overlayIdentifier2 = (OverlayIdentifier) map.get(str);
                setEnabled(transactionBuilder, overlayIdentifier2, str, i, set, true, hashSet2.contains(overlayIdentifier2));
            }
        }
        try {
            this.mOverlayManager.commit(transactionBuilder.build());
        } catch (IllegalStateException | SecurityException e) {
            Log.e("ThemeOverlayApplier", "setEnabled failed", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$applyCurrentUserOverlays$0(String str) {
        return this.mCategoryToTargetPackage.get(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentUserOverlays$1(List list, String str) {
        list.addAll(this.mOverlayManager.getOverlayInfosForTarget(str, UserHandle.SYSTEM));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyCurrentUserOverlays$2(OverlayInfo overlayInfo) {
        return this.mTargetPackageToCategories.get(overlayInfo.targetPackageName).contains(overlayInfo.category);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$applyCurrentUserOverlays$3(Set set, OverlayInfo overlayInfo) {
        return set.contains(overlayInfo.category);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$applyCurrentUserOverlays$4(Map map, OverlayInfo overlayInfo) {
        return !map.containsValue(new OverlayIdentifier(overlayInfo.packageName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Pair lambda$applyCurrentUserOverlays$6(OverlayInfo overlayInfo) {
        return new Pair(overlayInfo.category, overlayInfo.packageName);
    }

    protected OverlayManagerTransaction.Builder getTransactionBuilder() {
        return new OverlayManagerTransaction.Builder();
    }

    private void setEnabled(OverlayManagerTransaction.Builder builder, OverlayIdentifier overlayIdentifier, String str, int i, Set<UserHandle> set, boolean z, boolean z2) {
        if (DEBUG) {
            Log.d("ThemeOverlayApplier", "setEnabled: " + overlayIdentifier.getPackageName() + " category: " + str + ": " + z);
        }
        if (this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.of(i)) == null && !z2) {
            Log.i("ThemeOverlayApplier", "Won't enable " + overlayIdentifier + ", it doesn't exist for user" + i);
            return;
        }
        builder.setEnabled(overlayIdentifier, z, i);
        if (i != UserHandle.SYSTEM.getIdentifier() && SYSTEM_USER_CATEGORIES.contains(str)) {
            builder.setEnabled(overlayIdentifier, z, UserHandle.SYSTEM.getIdentifier());
        }
        OverlayInfo overlayInfo = this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.SYSTEM);
        if (overlayInfo == null || overlayInfo.targetPackageName.equals(this.mLauncherPackage) || overlayInfo.targetPackageName.equals(this.mThemePickerPackage)) {
            return;
        }
        for (UserHandle userHandle : set) {
            builder.setEnabled(overlayIdentifier, z, userHandle.getIdentifier());
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("mTargetPackageToCategories=" + this.mTargetPackageToCategories);
        printWriter.println("mCategoryToTargetPackage=" + this.mCategoryToTargetPackage);
    }
}
