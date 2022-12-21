package com.android.systemui.statusbar.connectivity;

import android.icu.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001BY\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005¢\u0006\u0002\u0010\rJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0001H\u0016J\u0013\u0010\u0011\u001a\u00020\u00052\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0002J\b\u0010\u0014\u001a\u00020\nH\u0016J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0016H\u0014J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0016H\u0014J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0014R\u0012\u0010\b\u001a\u00020\u00058\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\u00058\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/connectivity/WifiState;", "Lcom/android/systemui/statusbar/connectivity/ConnectivityState;", "ssid", "", "isTransient", "", "isDefault", "statusLabel", "isCarrierMerged", "subId", "", "wifiStandard", "isReady", "(Ljava/lang/String;ZZLjava/lang/String;ZIIZ)V", "copyFrom", "", "s", "equals", "other", "", "hashCode", "tableColumns", "", "tableData", "toString", "builder", "Ljava/lang/StringBuilder;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: WifiState.kt */
public final class WifiState extends ConnectivityState {
    public boolean isCarrierMerged;
    public boolean isDefault;
    public boolean isReady;
    public boolean isTransient;
    public String ssid;
    public String statusLabel;
    public int subId;
    public int wifiStandard;

    public WifiState() {
        this((String) null, false, false, (String) null, false, 0, 0, false, 255, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ WifiState(String str, boolean z, boolean z2, String str2, boolean z3, int i, int i2, boolean z4, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : str, (i3 & 2) != 0 ? false : z, (i3 & 4) != 0 ? false : z2, (i3 & 8) != 0 ? null : str2, (i3 & 16) != 0 ? false : z3, (i3 & 32) != 0 ? 0 : i, (i3 & 64) != 0 ? 0 : i2, (i3 & 128) != 0 ? false : z4);
    }

    public WifiState(String str, boolean z, boolean z2, String str2, boolean z3, int i, int i2, boolean z4) {
        this.ssid = str;
        this.isTransient = z;
        this.isDefault = z2;
        this.statusLabel = str2;
        this.isCarrierMerged = z3;
        this.subId = i;
        this.wifiStandard = i2;
        this.isReady = z4;
    }

    public void copyFrom(ConnectivityState connectivityState) {
        Intrinsics.checkNotNullParameter(connectivityState, DateFormat.SECOND);
        super.copyFrom(connectivityState);
        WifiState wifiState = (WifiState) connectivityState;
        this.ssid = wifiState.ssid;
        this.isTransient = wifiState.isTransient;
        this.isDefault = wifiState.isDefault;
        this.statusLabel = wifiState.statusLabel;
        this.isCarrierMerged = wifiState.isCarrierMerged;
        this.subId = wifiState.subId;
        this.wifiStandard = wifiState.wifiStandard;
        this.isReady = wifiState.isReady;
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        Intrinsics.checkNotNullParameter(sb, "builder");
        super.toString(sb);
        sb.append(",ssid=").append(this.ssid).append(",wifiStandard=").append(this.wifiStandard).append(",isReady=").append(this.isReady).append(",isTransient=").append(this.isTransient).append(",isDefault=").append(this.isDefault).append(",statusLabel=").append(this.statusLabel).append(",isCarrierMerged=").append(this.isCarrierMerged).append(",subId=").append(this.subId);
    }

    /* access modifiers changed from: protected */
    public List<String> tableColumns() {
        return CollectionsKt.plus(super.tableColumns(), CollectionsKt.listOf("ssid", "isTransient", "isDefault", "statusLabel", "isCarrierMerged", "subId"));
    }

    /* access modifiers changed from: protected */
    public List<String> tableData() {
        Iterable<Object> listOf = CollectionsKt.listOf(this.ssid, Boolean.valueOf(this.isTransient), Boolean.valueOf(this.isDefault), this.statusLabel, Boolean.valueOf(this.isCarrierMerged), Integer.valueOf(this.subId));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listOf, 10));
        for (Object valueOf : listOf) {
            arrayList.add(String.valueOf(valueOf));
        }
        return CollectionsKt.plus(super.tableData(), (List) arrayList);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!Intrinsics.areEqual((Object) getClass(), (Object) obj != null ? obj.getClass() : null) || !super.equals(obj)) {
            return false;
        }
        if (obj != null) {
            WifiState wifiState = (WifiState) obj;
            return Intrinsics.areEqual((Object) this.ssid, (Object) wifiState.ssid) && this.wifiStandard == wifiState.wifiStandard && this.isReady == wifiState.isReady && this.isTransient == wifiState.isTransient && this.isDefault == wifiState.isDefault && Intrinsics.areEqual((Object) this.statusLabel, (Object) wifiState.statusLabel) && this.isCarrierMerged == wifiState.isCarrierMerged && this.subId == wifiState.subId;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.WifiState");
    }

    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.ssid;
        int i = 0;
        int hashCode2 = (((((hashCode + (str != null ? str.hashCode() : 0)) * 31) + Boolean.hashCode(this.isTransient)) * 31) + Boolean.hashCode(this.isDefault)) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return ((((hashCode2 + i) * 31) + Boolean.hashCode(this.isCarrierMerged)) * 31) + this.subId;
    }
}
