package com.android.systemui.media;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0004H\u0016¨\u0006\t"}, mo65043d2 = {"com/android/systemui/media/MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/systemui/media/MediaCarouselScrollHandler;", "getValue", "", "handler", "setValue", "", "value", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 extends FloatPropertyCompat<MediaCarouselScrollHandler> {
    MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1() {
        super("contentTranslation");
    }

    public float getValue(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "handler");
        return mediaCarouselScrollHandler.getContentTranslation();
    }

    public void setValue(MediaCarouselScrollHandler mediaCarouselScrollHandler, float f) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "handler");
        mediaCarouselScrollHandler.setContentTranslation(f);
    }
}
