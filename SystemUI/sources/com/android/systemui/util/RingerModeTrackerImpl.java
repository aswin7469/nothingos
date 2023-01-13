package com.android.systemui.util;

import android.media.AudioManager;
import androidx.lifecycle.LiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\r¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/util/RingerModeTrackerImpl;", "Lcom/android/systemui/util/RingerModeTracker;", "audioManager", "Landroid/media/AudioManager;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "executor", "Ljava/util/concurrent/Executor;", "(Landroid/media/AudioManager;Lcom/android/systemui/broadcast/BroadcastDispatcher;Ljava/util/concurrent/Executor;)V", "ringerMode", "Landroidx/lifecycle/LiveData;", "", "getRingerMode", "()Landroidx/lifecycle/LiveData;", "ringerModeInternal", "getRingerModeInternal", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RingerModeTrackerImpl.kt */
public final class RingerModeTrackerImpl implements RingerModeTracker {
    private final LiveData<Integer> ringerMode;
    private final LiveData<Integer> ringerModeInternal;

    @Inject
    public RingerModeTrackerImpl(AudioManager audioManager, BroadcastDispatcher broadcastDispatcher, @Background Executor executor) {
        Intrinsics.checkNotNullParameter(audioManager, "audioManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.ringerMode = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.RINGER_MODE_CHANGED", new RingerModeTrackerImpl$ringerMode$1(audioManager));
        this.ringerModeInternal = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION", new RingerModeTrackerImpl$ringerModeInternal$1(audioManager));
    }

    public LiveData<Integer> getRingerMode() {
        return this.ringerMode;
    }

    public LiveData<Integer> getRingerModeInternal() {
        return this.ringerModeInternal;
    }
}
