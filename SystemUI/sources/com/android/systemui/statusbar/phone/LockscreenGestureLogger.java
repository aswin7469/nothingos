package com.android.systemui.statusbar.phone;

import android.metrics.LogMaker;
import android.util.ArrayMap;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.EventLogConstants;
import com.android.systemui.EventLogTags;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;

@SysUISingleton
public class LockscreenGestureLogger {
    private ArrayMap<Integer, Integer> mLegacyMap = new ArrayMap<>(EventLogConstants.METRICS_GESTURE_TYPE_MAP.length);
    private final MetricsLogger mMetricsLogger;

    public enum LockscreenUiEvent implements UiEventLogger.UiEventEnum {
        LOCKSCREEN_PULL_SHADE_OPEN(539),
        LOCKSCREEN_LOCK_TAP(540),
        LOCKSCREEN_QUICK_SETTINGS_OPEN(541),
        LOCKSCREEN_UNLOCKED_QUICK_SETTINGS_OPEN(542),
        LOCKSCREEN_LOCK_SHOW_HINT(543),
        LOCKSCREEN_NOTIFICATION_SHADE_QUICK_SETTINGS_OPEN(544),
        LOCKSCREEN_DIALER(545),
        LOCKSCREEN_CAMERA(546),
        LOCKSCREEN_UNLOCK(547),
        LOCKSCREEN_NOTIFICATION_FALSE_TOUCH(548),
        LOCKSCREEN_UNLOCKED_NOTIFICATION_PANEL_EXPAND(549),
        LOCKSCREEN_SWITCH_USER_TAP(934);
        
        private final int mId;

        private LockscreenUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @Inject
    public LockscreenGestureLogger(MetricsLogger metricsLogger) {
        this.mMetricsLogger = metricsLogger;
        for (int i = 0; i < EventLogConstants.METRICS_GESTURE_TYPE_MAP.length; i++) {
            this.mLegacyMap.put(Integer.valueOf(EventLogConstants.METRICS_GESTURE_TYPE_MAP[i]), Integer.valueOf(i));
        }
    }

    public void write(int i, int i2, int i3) {
        this.mMetricsLogger.write(new LogMaker(i).setType(4).addTaggedData(826, Integer.valueOf(i2)).addTaggedData(827, Integer.valueOf(i3)));
        EventLogTags.writeSysuiLockscreenGesture(safeLookup(i), i2, i3);
    }

    public void log(LockscreenUiEvent lockscreenUiEvent) {
        new UiEventLoggerImpl().log(lockscreenUiEvent);
    }

    public void writeAtFractionalPosition(int i, int i2, int i3, int i4) {
        this.mMetricsLogger.write(new LogMaker(i).setType(4).addTaggedData(1326, Integer.valueOf(i2)).addTaggedData(1327, Integer.valueOf(i3)).addTaggedData(1329, Integer.valueOf(i4)));
    }

    private int safeLookup(int i) {
        Integer num = this.mLegacyMap.get(Integer.valueOf(i));
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }
}
