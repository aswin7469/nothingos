package com.android.systemui.statusbar.policy;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.NextAlarmController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class NextAlarmControllerImpl extends BroadcastReceiver implements NextAlarmController, Dumpable {
    private AlarmManager mAlarmManager;
    private final ArrayList<NextAlarmController.NextAlarmChangeCallback> mChangeCallbacks = new ArrayList<>();
    private AlarmManager.AlarmClockInfo mNextAlarm;

    @Inject
    public NextAlarmControllerImpl(AlarmManager alarmManager, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager) {
        dumpManager.registerDumpable("NextAlarmController", this);
        this.mAlarmManager = alarmManager;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        broadcastDispatcher.registerReceiver(this, intentFilter, (Executor) null, UserHandle.ALL);
        updateNextAlarm();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("mNextAlarm=");
        if (this.mNextAlarm != null) {
            printWriter.println((Object) new Date(this.mNextAlarm.getTriggerTime()));
            printWriter.print("  PendingIntentPkg=");
            if (this.mNextAlarm.getShowIntent() != null) {
                printWriter.println(this.mNextAlarm.getShowIntent().getCreatorPackage());
            } else {
                printWriter.println("showIntent=null");
            }
        } else {
            printWriter.println("null");
        }
        printWriter.println("Registered Callbacks:");
        Iterator<NextAlarmController.NextAlarmChangeCallback> it = this.mChangeCallbacks.iterator();
        while (it.hasNext()) {
            printWriter.print("    ");
            printWriter.println(it.next().toString());
        }
    }

    public void addCallback(NextAlarmController.NextAlarmChangeCallback nextAlarmChangeCallback) {
        this.mChangeCallbacks.add(nextAlarmChangeCallback);
        nextAlarmChangeCallback.onNextAlarmChanged(this.mNextAlarm);
    }

    public void removeCallback(NextAlarmController.NextAlarmChangeCallback nextAlarmChangeCallback) {
        this.mChangeCallbacks.remove((Object) nextAlarmChangeCallback);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.USER_SWITCHED") || action.equals("android.app.action.NEXT_ALARM_CLOCK_CHANGED")) {
            updateNextAlarm();
        }
    }

    private void updateNextAlarm() {
        this.mNextAlarm = this.mAlarmManager.getNextAlarmClock(-2);
        fireNextAlarmChanged();
    }

    private void fireNextAlarmChanged() {
        int size = this.mChangeCallbacks.size();
        for (int i = 0; i < size; i++) {
            this.mChangeCallbacks.get(i).onNextAlarmChanged(this.mNextAlarm);
        }
    }
}
