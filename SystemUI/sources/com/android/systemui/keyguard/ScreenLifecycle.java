package com.android.systemui.keyguard;

import android.os.Trace;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScreenLifecycle extends Lifecycle<Observer> implements Dumpable {
    public static final int SCREEN_OFF = 0;
    public static final int SCREEN_ON = 2;
    public static final int SCREEN_TURNING_OFF = 3;
    public static final int SCREEN_TURNING_ON = 1;
    private int mScreenState = 0;

    public interface Observer {
        void onScreenTurnedOff() {
        }

        void onScreenTurnedOn() {
        }

        void onScreenTurningOff() {
        }

        void onScreenTurningOn(Runnable runnable) {
        }
    }

    @Inject
    public ScreenLifecycle(DumpManager dumpManager) {
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public int getScreenState() {
        return this.mScreenState;
    }

    public void dispatchScreenTurningOn(Runnable runnable) {
        setScreenState(1);
        dispatch(new ScreenLifecycle$$ExternalSyntheticLambda1(), runnable);
    }

    public void dispatchScreenTurnedOn() {
        setScreenState(2);
        dispatch(new ScreenLifecycle$$ExternalSyntheticLambda0());
    }

    public void dispatchScreenTurningOff() {
        setScreenState(3);
        dispatch(new ScreenLifecycle$$ExternalSyntheticLambda2());
    }

    public void dispatchScreenTurnedOff() {
        setScreenState(0);
        dispatch(new ScreenLifecycle$$ExternalSyntheticLambda3());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ScreenLifecycle:");
        printWriter.println("  mScreenState=" + this.mScreenState);
    }

    private void setScreenState(int i) {
        this.mScreenState = i;
        Trace.traceCounter(4096, "screenState", i);
    }
}
