package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/media/MediaDeviceManager$Entry$configListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onLocaleListChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager$Entry$configListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ MediaDeviceManager.Entry this$0;

    MediaDeviceManager$Entry$configListener$1(MediaDeviceManager.Entry entry) {
        this.this$0 = entry;
    }

    public void onLocaleListChanged() {
        this.this$0.updateCurrent();
    }
}
