package com.android.systemui.statusbar;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001:\u0002\u0014\u0015B\u0007\b\u0017¢\u0006\u0002\u0010\u0002B!\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J&\u0010\u0010\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u000eJ\u0010\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u000eH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/statusbar/DisableFlagsLogger;", "", "()V", "disable1FlagsList", "", "Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableFlag;", "disable2FlagsList", "(Ljava/util/List;Ljava/util/List;)V", "flagsListHasDuplicateSymbols", "", "list", "getDiffString", "", "old", "Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableState;", "new", "getDisableFlagsString", "newAfterLocalModification", "getFlagsString", "state", "DisableFlag", "DisableState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DisableFlagsLogger.kt */
public final class DisableFlagsLogger {
    private final List<DisableFlag> disable1FlagsList;
    private final List<DisableFlag> disable2FlagsList;

    public DisableFlagsLogger(List<DisableFlag> list, List<DisableFlag> list2) {
        Intrinsics.checkNotNullParameter(list, "disable1FlagsList");
        Intrinsics.checkNotNullParameter(list2, "disable2FlagsList");
        this.disable1FlagsList = list;
        this.disable2FlagsList = list2;
        if (flagsListHasDuplicateSymbols(list)) {
            throw new IllegalArgumentException("disable1 flags must have unique symbols");
        } else if (flagsListHasDuplicateSymbols(list2)) {
            throw new IllegalArgumentException("disable2 flags must have unique symbols");
        }
    }

    @Inject
    public DisableFlagsLogger() {
        this(DisableFlagsLoggerKt.defaultDisable1FlagsList, DisableFlagsLoggerKt.defaultDisable2FlagsList);
    }

    private final boolean flagsListHasDuplicateSymbols(List<DisableFlag> list) {
        Iterable<DisableFlag> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (DisableFlag flagStatus$SystemUI_nothingRelease : iterable) {
            arrayList.add(Character.valueOf(flagStatus$SystemUI_nothingRelease.getFlagStatus$SystemUI_nothingRelease(0)));
        }
        int size = CollectionsKt.distinct((List) arrayList).size();
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (DisableFlag flagStatus$SystemUI_nothingRelease2 : iterable) {
            arrayList2.add(Character.valueOf(flagStatus$SystemUI_nothingRelease2.getFlagStatus$SystemUI_nothingRelease(Integer.MAX_VALUE)));
        }
        int size2 = CollectionsKt.distinct((List) arrayList2).size();
        Collection collection = list;
        if (size < collection.size() || size2 < collection.size()) {
            return true;
        }
        return false;
    }

    public static /* synthetic */ String getDisableFlagsString$default(DisableFlagsLogger disableFlagsLogger, DisableState disableState, DisableState disableState2, DisableState disableState3, int i, Object obj) {
        if ((i & 1) != 0) {
            disableState = null;
        }
        if ((i & 4) != 0) {
            disableState3 = null;
        }
        return disableFlagsLogger.getDisableFlagsString(disableState, disableState2, disableState3);
    }

    public final String getDisableFlagsString(DisableState disableState, DisableState disableState2, DisableState disableState3) {
        Intrinsics.checkNotNullParameter(disableState2, "new");
        StringBuilder sb = new StringBuilder("Received new disable state: ");
        if (disableState != null && !Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            sb.append("Old: ");
            sb.append(getFlagsString(disableState));
            sb.append(" | New: ");
            sb.append(getFlagsString(disableState2));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(getDiffString(disableState, disableState2));
        } else if (disableState == null || !Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            sb.append(getFlagsString(disableState2));
        } else {
            sb.append(getFlagsString(disableState2));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(getDiffString(disableState, disableState2));
        }
        if (disableState3 != null && !Intrinsics.areEqual((Object) disableState2, (Object) disableState3)) {
            sb.append(" | New after local modification: ");
            sb.append(getFlagsString(disableState3));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(getDiffString(disableState2, disableState3));
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "builder.toString()");
        return sb2;
    }

    private final String getDiffString(DisableState disableState, DisableState disableState2) {
        if (Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            return "(unchanged)";
        }
        StringBuilder sb = new StringBuilder("(changed: ");
        for (DisableFlag disableFlag : this.disable1FlagsList) {
            char flagStatus$SystemUI_nothingRelease = disableFlag.getFlagStatus$SystemUI_nothingRelease(disableState2.getDisable1());
            if (disableFlag.getFlagStatus$SystemUI_nothingRelease(disableState.getDisable1()) != flagStatus$SystemUI_nothingRelease) {
                sb.append(flagStatus$SystemUI_nothingRelease);
            }
        }
        sb.append(BaseIconCache.EMPTY_CLASS_NAME);
        for (DisableFlag disableFlag2 : this.disable2FlagsList) {
            char flagStatus$SystemUI_nothingRelease2 = disableFlag2.getFlagStatus$SystemUI_nothingRelease(disableState2.getDisable2());
            if (disableFlag2.getFlagStatus$SystemUI_nothingRelease(disableState.getDisable2()) != flagStatus$SystemUI_nothingRelease2) {
                sb.append(flagStatus$SystemUI_nothingRelease2);
            }
        }
        sb.append(NavigationBarInflaterView.KEY_CODE_END);
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "builder.toString()");
        return sb2;
    }

    private final String getFlagsString(DisableState disableState) {
        StringBuilder sb = new StringBuilder("");
        for (DisableFlag flagStatus$SystemUI_nothingRelease : this.disable1FlagsList) {
            sb.append(flagStatus$SystemUI_nothingRelease.getFlagStatus$SystemUI_nothingRelease(disableState.getDisable1()));
        }
        sb.append(BaseIconCache.EMPTY_CLASS_NAME);
        for (DisableFlag flagStatus$SystemUI_nothingRelease2 : this.disable2FlagsList) {
            sb.append(flagStatus$SystemUI_nothingRelease2.getFlagStatus$SystemUI_nothingRelease(disableState.getDisable2()));
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "builder.toString()");
        return sb2;
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\u0015\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0003H\u0000¢\u0006\u0002\b\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableFlag;", "", "bitMask", "", "flagIsSetSymbol", "", "flagNotSetSymbol", "(ICC)V", "getFlagStatus", "state", "getFlagStatus$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DisableFlagsLogger.kt */
    public static final class DisableFlag {
        private final int bitMask;
        private final char flagIsSetSymbol;
        private final char flagNotSetSymbol;

        public DisableFlag(int i, char c, char c2) {
            this.bitMask = i;
            this.flagIsSetSymbol = c;
            this.flagNotSetSymbol = c2;
        }

        public final char getFlagStatus$SystemUI_nothingRelease(int i) {
            if ((i & this.bitMask) != 0) {
                return this.flagIsSetSymbol;
            }
            return this.flagNotSetSymbol;
        }
    }

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableState;", "", "disable1", "", "disable2", "(II)V", "getDisable1", "()I", "getDisable2", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DisableFlagsLogger.kt */
    public static final class DisableState {
        private final int disable1;
        private final int disable2;

        public static /* synthetic */ DisableState copy$default(DisableState disableState, int i, int i2, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = disableState.disable1;
            }
            if ((i3 & 2) != 0) {
                i2 = disableState.disable2;
            }
            return disableState.copy(i, i2);
        }

        public final int component1() {
            return this.disable1;
        }

        public final int component2() {
            return this.disable2;
        }

        public final DisableState copy(int i, int i2) {
            return new DisableState(i, i2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DisableState)) {
                return false;
            }
            DisableState disableState = (DisableState) obj;
            return this.disable1 == disableState.disable1 && this.disable2 == disableState.disable2;
        }

        public int hashCode() {
            return (Integer.hashCode(this.disable1) * 31) + Integer.hashCode(this.disable2);
        }

        public String toString() {
            return "DisableState(disable1=" + this.disable1 + ", disable2=" + this.disable2 + ')';
        }

        public DisableState(int i, int i2) {
            this.disable1 = i;
            this.disable2 = i2;
        }

        public final int getDisable1() {
            return this.disable1;
        }

        public final int getDisable2() {
            return this.disable2;
        }
    }
}
