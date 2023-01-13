package com.android.systemui.media;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016J\b\u0010\b\u001a\u00020\u0003H\u0016¨\u0006\t"}, mo65043d2 = {"com/android/systemui/media/MediaCarouselController$configListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "onDensityOrFontScaleChanged", "onThemeChanged", "onUiModeChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselController$configListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ MediaCarouselController this$0;

    MediaCarouselController$configListener$1(MediaCarouselController mediaCarouselController) {
        this.this$0 = mediaCarouselController;
    }

    public void onDensityOrFontScaleChanged() {
        this.this$0.updatePlayers(true);
        this.this$0.inflateSettingsButton();
    }

    public void onThemeChanged() {
        this.this$0.updatePlayers(false);
        this.this$0.inflateSettingsButton();
    }

    public void onConfigChanged(Configuration configuration) {
        if (configuration != null) {
            MediaCarouselController mediaCarouselController = this.this$0;
            boolean z = true;
            if (configuration.getLayoutDirection() != 1) {
                z = false;
            }
            mediaCarouselController.setRtl(z);
        }
    }

    public void onUiModeChanged() {
        this.this$0.updatePlayers(false);
        this.this$0.inflateSettingsButton();
    }
}
