package com.android.systemui.privacy;

import android.content.pm.UserInfo;
import android.os.UserHandle;
import com.android.systemui.appops.AppOpsController;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, mo65043d2 = {"com/android/systemui/privacy/AppOpsPrivacyItemMonitor$appOpsCallback$1", "Lcom/android/systemui/appops/AppOpsController$Callback;", "onActiveStateChanged", "", "code", "", "uid", "packageName", "", "active", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$appOpsCallback$1 implements AppOpsController.Callback {
    final /* synthetic */ AppOpsPrivacyItemMonitor this$0;

    AppOpsPrivacyItemMonitor$appOpsCallback$1(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor) {
        this.this$0 = appOpsPrivacyItemMonitor;
    }

    public void onActiveStateChanged(int i, int i2, String str, boolean z) {
        boolean z2;
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Object access$getLock$p = this.this$0.lock;
        AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            if (ArraysKt.contains(AppOpsPrivacyItemMonitor.Companion.getOPS_MIC_CAMERA(), i) && !appOpsPrivacyItemMonitor.micCameraAvailable) {
                return;
            }
            if (!ArraysKt.contains(AppOpsPrivacyItemMonitor.Companion.getOPS_LOCATION(), i) || appOpsPrivacyItemMonitor.locationAvailable) {
                Iterable userProfiles = appOpsPrivacyItemMonitor.userTracker.getUserProfiles();
                boolean z3 = false;
                if (!(userProfiles instanceof Collection) || !((Collection) userProfiles).isEmpty()) {
                    Iterator it = userProfiles.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (((UserInfo) it.next()).id == UserHandle.getUserId(i2)) {
                                z2 = true;
                                continue;
                            } else {
                                z2 = false;
                                continue;
                            }
                            if (z2) {
                                z3 = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (z3 || ArraysKt.contains(AppOpsPrivacyItemMonitor.Companion.getUSER_INDEPENDENT_OPS(), i)) {
                    appOpsPrivacyItemMonitor.logger.logUpdatedItemFromAppOps(i, i2, str, z);
                    appOpsPrivacyItemMonitor.dispatchOnPrivacyItemsChanged();
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
