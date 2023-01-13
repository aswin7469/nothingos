package com.android.systemui.statusbar.connectivity;

import android.icu.text.PluralRules;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import com.android.settingslib.SignalIcon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0000H\u0014J\u0013\u0010\u0013\u001a\u00020\u00042\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010\u0014\u001a\u00020\u000bH\u0016J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0014J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0014J\b\u0010\u0019\u001a\u00020\u0017H\u0016J\u0014\u0010\u0019\u001a\u00020\u00112\n\u0010\u001a\u001a\u00060\u001bj\u0002`\u001cH\u0014R\u0012\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00020\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo65043d2 = {"Lcom/android/systemui/statusbar/connectivity/ConnectivityState;", "", "()V", "activityIn", "", "activityOut", "connected", "enabled", "iconGroup", "Lcom/android/settingslib/SignalIcon$IconGroup;", "inetCondition", "", "level", "rssi", "time", "", "copyFrom", "", "other", "equals", "hashCode", "tableColumns", "", "", "tableData", "toString", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConnectivityState.kt */
public class ConnectivityState {
    public boolean activityIn;
    public boolean activityOut;
    public boolean connected;
    public boolean enabled;
    public SignalIcon.IconGroup iconGroup;
    public int inetCondition;
    public int level;
    public int rssi;
    public long time;

    public String toString() {
        if (this.time == 0) {
            return "Empty " + getClass().getSimpleName();
        }
        StringBuilder sb = new StringBuilder();
        toString(sb);
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "{\n            val builde…lder.toString()\n        }");
        return sb2;
    }

    /* access modifiers changed from: protected */
    public List<String> tableColumns() {
        return CollectionsKt.listOf(WifiManager.EXTRA_SUPPLICANT_CONNECTED, "enabled", "activityIn", "activityOut", "level", "iconGroup", ConnectivityManager.EXTRA_INET_CONDITION, "rssi", "time");
    }

    /* access modifiers changed from: protected */
    public List<String> tableData() {
        Iterable<Object> listOf = CollectionsKt.listOf(Boolean.valueOf(this.connected), Boolean.valueOf(this.enabled), Boolean.valueOf(this.activityIn), Boolean.valueOf(this.activityOut), Integer.valueOf(this.level), this.iconGroup, Integer.valueOf(this.inetCondition), Integer.valueOf(this.rssi), ConnectivityStateKt.sSDF.format(Long.valueOf(this.time)));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listOf, 10));
        for (Object valueOf : listOf) {
            arrayList.add(String.valueOf(valueOf));
        }
        return (List) arrayList;
    }

    /* access modifiers changed from: protected */
    public void copyFrom(ConnectivityState connectivityState) {
        Intrinsics.checkNotNullParameter(connectivityState, PluralRules.KEYWORD_OTHER);
        this.connected = connectivityState.connected;
        this.enabled = connectivityState.enabled;
        this.activityIn = connectivityState.activityIn;
        this.activityOut = connectivityState.activityOut;
        this.level = connectivityState.level;
        this.iconGroup = connectivityState.iconGroup;
        this.inetCondition = connectivityState.inetCondition;
        this.rssi = connectivityState.rssi;
        this.time = connectivityState.time;
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        Intrinsics.checkNotNullParameter(sb, "builder");
        sb.append("connected=" + this.connected + ',').append("enabled=" + this.enabled + ',').append("level=" + this.level + ',').append("inetCondition=" + this.inetCondition + ',').append("iconGroup=" + this.iconGroup + ',').append("activityIn=" + this.activityIn + ',').append("activityOut=" + this.activityOut + ',').append("rssi=" + this.rssi + ',').append("lastModified=" + ConnectivityStateKt.sSDF.format(Long.valueOf(this.time)));
    }

    public boolean equals(Object obj) {
        if (obj == null || !Intrinsics.areEqual((Object) obj.getClass(), (Object) getClass())) {
            return false;
        }
        ConnectivityState connectivityState = (ConnectivityState) obj;
        if (connectivityState.connected == this.connected && connectivityState.enabled == this.enabled && connectivityState.level == this.level && connectivityState.inetCondition == this.inetCondition && connectivityState.iconGroup == this.iconGroup && connectivityState.activityIn == this.activityIn && connectivityState.activityOut == this.activityOut && connectivityState.rssi == this.rssi) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = ((((((((Boolean.hashCode(this.connected) * 31) + Boolean.hashCode(this.enabled)) * 31) + Boolean.hashCode(this.activityIn)) * 31) + Boolean.hashCode(this.activityOut)) * 31) + this.level) * 31;
        SignalIcon.IconGroup iconGroup2 = this.iconGroup;
        return ((((((hashCode + (iconGroup2 != null ? iconGroup2.hashCode() : 0)) * 31) + this.inetCondition) * 31) + this.rssi) * 31) + Long.hashCode(this.time);
    }
}
