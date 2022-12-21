package com.android.systemui.privacy;

import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import com.android.internal.logging.UiEventLogger;
import com.nothing.p023os.device.DeviceConstant;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/privacy/PrivacyChipEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "ONGOING_INDICATORS_CHIP_VIEW", "ONGOING_INDICATORS_CHIP_CLICK", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyChipEvent.kt */
public enum PrivacyChipEvent implements UiEventLogger.UiEventEnum {
    ONGOING_INDICATORS_CHIP_VIEW(DeviceConstant.ORDER_SOUND),
    ONGOING_INDICATORS_CHIP_CLICK(IMDnsEventListener.SERVICE_DISCOVERY_FAILED);
    
    private final int _id;

    private PrivacyChipEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
