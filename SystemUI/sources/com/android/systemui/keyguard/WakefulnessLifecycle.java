package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Trace;
import android.util.DisplayMetrics;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class WakefulnessLifecycle extends Lifecycle<Observer> implements Dumpable {
    public static final int WAKEFULNESS_ASLEEP = 0;
    public static final int WAKEFULNESS_AWAKE = 2;
    public static final int WAKEFULNESS_GOING_TO_SLEEP = 3;
    public static final int WAKEFULNESS_WAKING = 1;
    private final Context mContext;
    private final DisplayMetrics mDisplayMetrics;
    private Point mLastSleepOriginLocation = null;
    private int mLastSleepReason = 0;
    private Point mLastWakeOriginLocation = null;
    private int mLastWakeReason = 0;
    private int mWakefulness = 2;
    private final IWallpaperManager mWallpaperManagerService;

    public interface Observer {
        void onFinishedGoingToSleep() {
        }

        void onFinishedWakingUp() {
        }

        void onPostFinishedWakingUp() {
        }

        void onStartedGoingToSleep() {
        }

        void onStartedWakingUp() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Wakefulness {
    }

    @Inject
    public WakefulnessLifecycle(Context context, IWallpaperManager iWallpaperManager, DumpManager dumpManager) {
        this.mContext = context;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mWallpaperManagerService = iWallpaperManager;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public int getWakefulness() {
        return this.mWakefulness;
    }

    public int getLastWakeReason() {
        return this.mLastWakeReason;
    }

    public int getLastSleepReason() {
        return this.mLastSleepReason;
    }

    public void dispatchStartedWakingUp(int i) {
        if (getWakefulness() != 1) {
            setWakefulness(1);
            this.mLastWakeReason = i;
            updateLastWakeOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    iWallpaperManager.notifyWakingUp(this.mLastWakeOriginLocation.x, this.mLastWakeOriginLocation.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda4());
        }
    }

    public void dispatchFinishedWakingUp() {
        if (getWakefulness() != 2) {
            setWakefulness(2);
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda2());
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda3());
        }
    }

    public void dispatchStartedGoingToSleep(int i) {
        if (getWakefulness() != 3) {
            setWakefulness(3);
            this.mLastSleepReason = i;
            updateLastSleepOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    iWallpaperManager.notifyGoingToSleep(this.mLastSleepOriginLocation.x, this.mLastSleepOriginLocation.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda0());
        }
    }

    public void dispatchFinishedGoingToSleep() {
        if (getWakefulness() != 0) {
            setWakefulness(0);
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda1());
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("WakefulnessLifecycle:");
        printWriter.println("  mWakefulness=" + this.mWakefulness);
    }

    private void setWakefulness(int i) {
        this.mWakefulness = i;
        Trace.traceCounter(4096, "wakefulness", i);
    }

    private void updateLastWakeOriginLocation() {
        this.mLastWakeOriginLocation = null;
        if (this.mLastWakeReason != 1) {
            this.mLastWakeOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastWakeOriginLocation = getPowerButtonOrigin();
        }
    }

    private void updateLastSleepOriginLocation() {
        this.mLastSleepOriginLocation = null;
        if (this.mLastSleepReason != 4) {
            this.mLastSleepOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastSleepOriginLocation = getPowerButtonOrigin();
        }
    }

    private Point getPowerButtonOrigin() {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            return new Point(this.mDisplayMetrics.widthPixels, this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.physical_power_button_center_screen_location_y));
        }
        return new Point(this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.physical_power_button_center_screen_location_y), this.mDisplayMetrics.heightPixels);
    }

    private Point getDefaultWakeSleepOrigin() {
        return new Point(this.mDisplayMetrics.widthPixels / 2, this.mDisplayMetrics.heightPixels);
    }
}
