package com.android.systemui.statusbar.connectivity;

import android.content.Intent;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001\u0012J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\u0007H&J\u0012\u0010\t\u001a\u00020\u00072\b\u0010\n\u001a\u0004\u0018\u00010\u000bH&J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH&J\n\u0010\u000e\u001a\u0004\u0018\u00010\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0011\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0013À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/connectivity/AccessPointController;", "", "addAccessPointCallback", "", "callback", "Lcom/android/systemui/statusbar/connectivity/AccessPointController$AccessPointCallback;", "canConfigMobileData", "", "canConfigWifi", "connect", "ap", "Lcom/android/wifitrackerlib/WifiEntry;", "getIcon", "", "getMergedCarrierEntry", "Lcom/android/wifitrackerlib/MergedCarrierEntry;", "removeAccessPointCallback", "scanForAccessPoints", "AccessPointCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AccessPointController.kt */
public interface AccessPointController {

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001b\u0010\u0002\u001a\u00020\u00032\u0011\u0010\u0004\u001a\r\u0012\t\u0012\u00070\u0006¢\u0006\u0002\b\u00070\u0005H&J\u0012\u0010\b\u001a\u00020\u00032\b\u0010\t\u001a\u0004\u0018\u00010\nH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/connectivity/AccessPointController$AccessPointCallback;", "", "onAccessPointsChanged", "", "accessPoints", "", "Lcom/android/wifitrackerlib/WifiEntry;", "Lkotlin/jvm/JvmSuppressWildcards;", "onSettingsActivityTriggered", "settingsIntent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AccessPointController.kt */
    public interface AccessPointCallback {
        void onAccessPointsChanged(List<WifiEntry> list);

        void onSettingsActivityTriggered(Intent intent);
    }

    void addAccessPointCallback(AccessPointCallback accessPointCallback);

    boolean canConfigMobileData();

    boolean canConfigWifi();

    boolean connect(WifiEntry wifiEntry);

    int getIcon(WifiEntry wifiEntry);

    MergedCarrierEntry getMergedCarrierEntry();

    void removeAccessPointCallback(AccessPointCallback accessPointCallback);

    void scanForAccessPoints();
}
