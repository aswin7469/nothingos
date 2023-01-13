package com.android.systemui.statusbar.connectivity;

import android.telephony.SubscriptionInfo;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0017J \u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nH\u0017J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0005H\u0017J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0005H\u0017J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\nH\u0017J\u0010\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u0018\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\nH\u0017J\u001b\u0010\u0018\u001a\u00020\u00032\u0011\u0010\u0019\u001a\r\u0012\t\u0012\u00070\u001b¢\u0006\u0002\b\u001c0\u001aH\u0017J\u0010\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001fH\u0017ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006 À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/connectivity/SignalCallback;", "", "setCallIndicator", "", "statusIcon", "Lcom/android/systemui/statusbar/connectivity/IconState;", "subId", "", "setConnectivityStatus", "noDefaultNetwork", "", "noValidatedNetwork", "noNetworksAvailable", "setEthernetIndicators", "icon", "setIsAirplaneMode", "setMobileDataEnabled", "enabled", "setMobileDataIndicators", "mobileDataIndicators", "Lcom/android/systemui/statusbar/connectivity/MobileDataIndicators;", "setNoSims", "show", "simDetected", "setSubs", "subs", "", "Landroid/telephony/SubscriptionInfo;", "Lkotlin/jvm/JvmSuppressWildcards;", "setWifiIndicators", "wifiIndicators", "Lcom/android/systemui/statusbar/connectivity/WifiIndicators;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SignalCallback.kt */
public interface SignalCallback {
    @JvmDefault
    void setCallIndicator(IconState iconState, int i) {
        Intrinsics.checkNotNullParameter(iconState, "statusIcon");
    }

    @JvmDefault
    void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
    }

    @JvmDefault
    void setEthernetIndicators(IconState iconState) {
        Intrinsics.checkNotNullParameter(iconState, BaseIconCache.IconDB.COLUMN_ICON);
    }

    @JvmDefault
    void setIsAirplaneMode(IconState iconState) {
        Intrinsics.checkNotNullParameter(iconState, BaseIconCache.IconDB.COLUMN_ICON);
    }

    @JvmDefault
    void setMobileDataEnabled(boolean z) {
    }

    @JvmDefault
    void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        Intrinsics.checkNotNullParameter(mobileDataIndicators, "mobileDataIndicators");
    }

    @JvmDefault
    void setNoSims(boolean z, boolean z2) {
    }

    @JvmDefault
    void setSubs(List<SubscriptionInfo> list) {
        Intrinsics.checkNotNullParameter(list, "subs");
    }

    @JvmDefault
    void setWifiIndicators(WifiIndicators wifiIndicators) {
        Intrinsics.checkNotNullParameter(wifiIndicators, "wifiIndicators");
    }
}
