package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.UserAwareController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001\u000bJ\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u000e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsListingController;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "Lcom/android/systemui/util/UserAwareController;", "getAppLabel", "", "name", "Landroid/content/ComponentName;", "getCurrentServices", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "ControlsListingCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsListingController.kt */
public interface ControlsListingController extends CallbackController<ControlsListingCallback>, UserAwareController {

    @FunctionalInterface
    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsListingController.kt */
    public interface ControlsListingCallback {
        void onServicesUpdated(List<ControlsServiceInfo> list);
    }

    List<ControlsServiceInfo> getCurrentServices();

    CharSequence getAppLabel(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, ZoneGetter.KEY_DISPLAYNAME);
        return "";
    }
}
