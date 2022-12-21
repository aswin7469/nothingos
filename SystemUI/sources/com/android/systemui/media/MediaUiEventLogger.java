package com.android.systemui.media;

import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001c\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ&\u0010\u0010\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\fJ\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\fJ\u0006\u0010\u0014\u001a\u00020\nJ\u001e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\u0017\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\fJ\u001e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\u001c\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ&\u0010\u001e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\fJ\u001e\u0010\u001f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u0016\u0010 \u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u0016\u0010!\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010\"\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\fJ\u0016\u0010#\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010$\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010%\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u0006\u0010&\u001a\u00020\nJ&\u0010'\u001a\u00020\n2\u0006\u0010(\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u001e\u0010)\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, mo64987d2 = {"Lcom/android/systemui/media/MediaUiEventLogger;", "", "logger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/internal/logging/UiEventLogger;)V", "instanceIdSequence", "Lcom/android/internal/logging/InstanceIdSequence;", "getNewInstanceId", "Lcom/android/internal/logging/InstanceId;", "logActiveConvertedToResume", "", "uid", "", "packageName", "", "instanceId", "logActiveMediaAdded", "playbackLocation", "logCarouselPosition", "location", "logCarouselSettings", "logLongPressDismiss", "logLongPressOpen", "logLongPressSettings", "logMediaCarouselPage", "position", "logMediaRemoved", "logMediaTimeout", "logOpenBroadcastDialog", "logOpenOutputSwitcher", "logPlaybackLocationChange", "logRecommendationActivated", "logRecommendationAdded", "logRecommendationCardTap", "logRecommendationItemTap", "logRecommendationRemoved", "logResumeMediaAdded", "logSeek", "logSwipeDismiss", "logTapAction", "buttonId", "logTapContentView", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaUiEventLogger.kt */
public final class MediaUiEventLogger {
    private final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    private final UiEventLogger logger;

    @Inject
    public MediaUiEventLogger(UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(uiEventLogger, "logger");
        this.logger = uiEventLogger;
    }

    public final InstanceId getNewInstanceId() {
        InstanceId newInstanceId = this.instanceIdSequence.newInstanceId();
        Intrinsics.checkNotNullExpressionValue(newInstanceId, "instanceIdSequence.newInstanceId()");
        return newInstanceId;
    }

    public final void logActiveMediaAdded(int i, String str, InstanceId instanceId, int i2) {
        MediaUiEvent mediaUiEvent;
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        if (i2 == 0) {
            mediaUiEvent = MediaUiEvent.LOCAL_MEDIA_ADDED;
        } else if (i2 == 1) {
            mediaUiEvent = MediaUiEvent.CAST_MEDIA_ADDED;
        } else if (i2 == 2) {
            mediaUiEvent = MediaUiEvent.REMOTE_MEDIA_ADDED;
        } else {
            throw new IllegalArgumentException("Unknown playback location");
        }
        this.logger.logWithInstanceId((UiEventLogger.UiEventEnum) mediaUiEvent, i, str, instanceId);
    }

    public final void logPlaybackLocationChange(int i, String str, InstanceId instanceId, int i2) {
        MediaUiEvent mediaUiEvent;
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        if (i2 == 0) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_LOCAL;
        } else if (i2 == 1) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_CAST;
        } else if (i2 == 2) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_REMOTE;
        } else {
            throw new IllegalArgumentException("Unknown playback location");
        }
        this.logger.logWithInstanceId((UiEventLogger.UiEventEnum) mediaUiEvent, i, str, instanceId);
    }

    public final void logResumeMediaAdded(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.RESUME_MEDIA_ADDED, i, str, instanceId);
    }

    public final void logActiveConvertedToResume(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.ACTIVE_TO_RESUME, i, str, instanceId);
    }

    public final void logMediaTimeout(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_TIMEOUT, i, str, instanceId);
    }

    public final void logMediaRemoved(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_REMOVED, i, str, instanceId);
    }

    public final void logMediaCarouselPage(int i) {
        this.logger.logWithPosition(MediaUiEvent.CAROUSEL_PAGE, 0, (String) null, i);
    }

    public final void logSwipeDismiss() {
        this.logger.log(MediaUiEvent.DISMISS_SWIPE);
    }

    public final void logLongPressOpen(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_LONG_PRESS, i, str, instanceId);
    }

    public final void logLongPressDismiss(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.DISMISS_LONG_PRESS, i, str, instanceId);
    }

    public final void logLongPressSettings(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_SETTINGS_LONG_PRESS, i, str, instanceId);
    }

    public final void logCarouselSettings() {
        this.logger.log(MediaUiEvent.OPEN_SETTINGS_CAROUSEL);
    }

    public final void logTapAction(int i, int i2, String str, InstanceId instanceId) {
        MediaUiEvent mediaUiEvent;
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        switch (i) {
            case C1893R.C1897id.actionNext:
                mediaUiEvent = MediaUiEvent.TAP_ACTION_NEXT;
                break;
            case C1893R.C1897id.actionPlayPause:
                mediaUiEvent = MediaUiEvent.TAP_ACTION_PLAY_PAUSE;
                break;
            case C1893R.C1897id.actionPrev:
                mediaUiEvent = MediaUiEvent.TAP_ACTION_PREV;
                break;
            default:
                mediaUiEvent = MediaUiEvent.TAP_ACTION_OTHER;
                break;
        }
        this.logger.logWithInstanceId((UiEventLogger.UiEventEnum) mediaUiEvent, i2, str, instanceId);
    }

    public final void logSeek(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.ACTION_SEEK, i, str, instanceId);
    }

    public final void logOpenOutputSwitcher(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_OUTPUT_SWITCHER, i, str, instanceId);
    }

    public final void logTapContentView(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_TAP_CONTENT_VIEW, i, str, instanceId);
    }

    public final void logCarouselPosition(int i) {
        MediaUiEvent mediaUiEvent;
        if (i == 0) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_QS;
        } else if (i == 1) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_QQS;
        } else if (i == 2) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_LOCKSCREEN;
        } else if (i == 3) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_DREAM;
        } else {
            throw new IllegalArgumentException("Unknown media carousel location " + i);
        }
        this.logger.log((UiEventLogger.UiEventEnum) mediaUiEvent);
    }

    public final void logRecommendationAdded(String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_ADDED, 0, str, instanceId);
    }

    public final void logRecommendationRemoved(String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_REMOVED, 0, str, instanceId);
    }

    public final void logRecommendationActivated(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_ACTIVATED, i, str, instanceId);
    }

    public final void logRecommendationItemTap(String str, InstanceId instanceId, int i) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceIdAndPosition(MediaUiEvent.MEDIA_RECOMMENDATION_ITEM_TAP, 0, str, instanceId, i);
    }

    public final void logRecommendationCardTap(String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_CARD_TAP, 0, str, instanceId);
    }

    public final void logOpenBroadcastDialog(int i, String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_OPEN_BROADCAST_DIALOG, i, str, instanceId);
    }
}
