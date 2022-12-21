package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/media/MediaDeviceManager$Entry$configListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onLocaleListChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
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
