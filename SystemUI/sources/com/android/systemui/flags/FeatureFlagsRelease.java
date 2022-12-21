package com.android.systemui.flags;

import android.content.res.Resources;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FlagListenable;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class FeatureFlagsRelease implements FeatureFlags, Dumpable {
    SparseBooleanArray mBooleanCache = new SparseBooleanArray();
    private final Resources mResources;
    SparseArray<String> mStringCache = new SparseArray<>();
    private final SystemPropertiesHelper mSystemProperties;

    public void addListener(Flag<?> flag, FlagListenable.Listener listener) {
    }

    public void removeListener(FlagListenable.Listener listener) {
    }

    @Inject
    public FeatureFlagsRelease(@Main Resources resources, SystemPropertiesHelper systemPropertiesHelper, DumpManager dumpManager) {
        this.mResources = resources;
        this.mSystemProperties = systemPropertiesHelper;
        dumpManager.registerDumpable("SysUIFlags", this);
    }

    public boolean isEnabled(BooleanFlag booleanFlag) {
        return booleanFlag.getDefault().booleanValue();
    }

    public boolean isEnabled(ResourceBooleanFlag resourceBooleanFlag) {
        int indexOfKey = this.mBooleanCache.indexOfKey(resourceBooleanFlag.getId());
        if (indexOfKey < 0) {
            return isEnabled(resourceBooleanFlag.getId(), this.mResources.getBoolean(resourceBooleanFlag.getResourceId()));
        }
        return this.mBooleanCache.valueAt(indexOfKey);
    }

    public boolean isEnabled(SysPropBooleanFlag sysPropBooleanFlag) {
        int indexOfKey = this.mBooleanCache.indexOfKey(sysPropBooleanFlag.getId());
        if (indexOfKey < 0) {
            return isEnabled(sysPropBooleanFlag.getId(), this.mSystemProperties.getBoolean(sysPropBooleanFlag.getName(), sysPropBooleanFlag.getDefault().booleanValue()));
        }
        return this.mBooleanCache.valueAt(indexOfKey);
    }

    private boolean isEnabled(int i, boolean z) {
        this.mBooleanCache.append(i, z);
        return z;
    }

    public String getString(StringFlag stringFlag) {
        return getString(stringFlag.getId(), stringFlag.getDefault());
    }

    public String getString(ResourceStringFlag resourceStringFlag) {
        int indexOfKey = this.mStringCache.indexOfKey(resourceStringFlag.getId());
        if (indexOfKey < 0) {
            return getString(resourceStringFlag.getId(), (String) Objects.requireNonNull(this.mResources.getString(resourceStringFlag.getResourceId())));
        }
        return this.mStringCache.valueAt(indexOfKey);
    }

    private String getString(int i, String str) {
        this.mStringCache.append(i, str);
        return str;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [int] */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(java.p026io.PrintWriter r7, java.lang.String[] r8) {
        /*
            r6 = this;
            java.lang.String r8 = "can override: false"
            r7.println((java.lang.String) r8)
            java.util.Map r8 = com.android.systemui.flags.Flags.collectFlags()
            java.util.Set r8 = r8.entrySet()
            java.util.Iterator r8 = r8.iterator()
        L_0x0011:
            boolean r0 = r8.hasNext()
            java.lang.String r1 = "  sysui_flag_"
            r2 = 0
            if (r0 == 0) goto L_0x0096
            java.lang.Object r0 = r8.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r3 = r0.getKey()
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            java.lang.Object r0 = r0.getValue()
            com.android.systemui.flags.Flag r0 = (com.android.systemui.flags.Flag) r0
            android.util.SparseBooleanArray r4 = r6.mBooleanCache
            int r5 = r0.getId()
            int r4 = r4.indexOfKey(r5)
            if (r4 >= 0) goto L_0x0074
            boolean r4 = r0 instanceof com.android.systemui.flags.SysPropBooleanFlag
            if (r4 == 0) goto L_0x0055
            com.android.systemui.flags.SysPropBooleanFlag r0 = (com.android.systemui.flags.SysPropBooleanFlag) r0
            com.android.systemui.flags.SystemPropertiesHelper r2 = r6.mSystemProperties
            java.lang.String r4 = r0.getName()
            java.lang.Boolean r0 = r0.getDefault()
            boolean r0 = r0.booleanValue()
            boolean r2 = r2.getBoolean(r4, r0)
            goto L_0x0074
        L_0x0055:
            boolean r4 = r0 instanceof com.android.systemui.flags.ResourceBooleanFlag
            if (r4 == 0) goto L_0x0066
            com.android.systemui.flags.ResourceBooleanFlag r0 = (com.android.systemui.flags.ResourceBooleanFlag) r0
            android.content.res.Resources r2 = r6.mResources
            int r0 = r0.getResourceId()
            boolean r2 = r2.getBoolean(r0)
            goto L_0x0074
        L_0x0066:
            boolean r4 = r0 instanceof com.android.systemui.flags.BooleanFlag
            if (r4 == 0) goto L_0x0074
            com.android.systemui.flags.BooleanFlag r0 = (com.android.systemui.flags.BooleanFlag) r0
            java.lang.Boolean r0 = r0.getDefault()
            boolean r2 = r0.booleanValue()
        L_0x0074:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((int) r3)
            java.lang.String r1 = ": "
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            android.util.SparseBooleanArray r1 = r6.mBooleanCache
            boolean r1 = r1.get(r3, r2)
            java.lang.StringBuilder r0 = r0.append((boolean) r1)
            java.lang.String r0 = r0.toString()
            r7.println((java.lang.String) r0)
            goto L_0x0011
        L_0x0096:
            android.util.SparseArray<java.lang.String> r8 = r6.mStringCache
            int r8 = r8.size()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "Strings: "
            r0.<init>((java.lang.String) r3)
            java.lang.StringBuilder r0 = r0.append((int) r8)
            java.lang.String r0 = r0.toString()
            r7.println((java.lang.String) r0)
        L_0x00ae:
            if (r2 >= r8) goto L_0x00ef
            android.util.SparseArray<java.lang.String> r0 = r6.mStringCache
            int r0 = r0.keyAt(r2)
            android.util.SparseArray<java.lang.String> r3 = r6.mStringCache
            java.lang.Object r3 = r3.valueAt(r2)
            java.lang.String r3 = (java.lang.String) r3
            int r4 = r3.length()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r5.append((int) r0)
            java.lang.String r5 = ": [length="
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r5)
            java.lang.StringBuilder r0 = r0.append((int) r4)
            java.lang.String r4 = "] \""
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r4)
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            java.lang.String r3 = "\""
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            java.lang.String r0 = r0.toString()
            r7.println((java.lang.String) r0)
            int r2 = r2 + 1
            goto L_0x00ae
        L_0x00ef:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.flags.FeatureFlagsRelease.dump(java.io.PrintWriter, java.lang.String[]):void");
    }
}
