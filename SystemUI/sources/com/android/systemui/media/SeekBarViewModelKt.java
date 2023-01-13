package com.android.systemui.media;

import android.media.session.PlaybackState;
import android.os.SystemClock;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u001a\u0014\u0010\u0004\u001a\u00020\u0003*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H\u0002\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0005H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"MIN_FLING_VELOCITY_SCALE_FACTOR", "", "POSITION_UPDATE_INTERVAL_MILLIS", "", "computePosition", "Landroid/media/session/PlaybackState;", "duration", "isInMotion", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModelKt {
    private static final int MIN_FLING_VELOCITY_SCALE_FACTOR = 10;
    private static final long POSITION_UPDATE_INTERVAL_MILLIS = 100;

    /* access modifiers changed from: private */
    public static final boolean isInMotion(PlaybackState playbackState) {
        return playbackState.getState() == 3 || playbackState.getState() == 4 || playbackState.getState() == 5;
    }

    /* access modifiers changed from: private */
    public static final long computePosition(PlaybackState playbackState, long j) {
        long position = playbackState.getPosition();
        if (!isInMotion(playbackState)) {
            return position;
        }
        long lastPositionUpdateTime = playbackState.getLastPositionUpdateTime();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (lastPositionUpdateTime <= 0) {
            return position;
        }
        long playbackSpeed = ((long) (playbackState.getPlaybackSpeed() * ((float) (elapsedRealtime - lastPositionUpdateTime)))) + playbackState.getPosition();
        if (j < 0 || playbackSpeed <= j) {
            j = playbackSpeed < 0 ? 0 : playbackSpeed;
        }
        return j;
    }
}
