package com.android.systemui.people.widget;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import androidx.core.content.p003pm.ShortcutManagerCompat;
import javax.inject.Inject;

public class PeopleSpaceWidgetPinnedReceiver extends BroadcastReceiver {
    private static final int BROADCAST_ID = 0;
    private static final boolean DEBUG = false;
    private static final int INVALID_WIDGET_ID = -1;
    private static final String TAG = "PeopleSpaceWgtPinReceiver";
    private final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    @Inject
    public PeopleSpaceWidgetPinnedReceiver(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    public static PendingIntent getPendingIntent(Context context, ShortcutInfo shortcutInfo) {
        Intent addFlags = new Intent(context, PeopleSpaceWidgetPinnedReceiver.class).addFlags(268435456);
        addFlags.putExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID, shortcutInfo.getId());
        addFlags.putExtra("android.intent.extra.USER_ID", shortcutInfo.getUserId());
        addFlags.putExtra("android.intent.extra.PACKAGE_NAME", shortcutInfo.getPackage());
        return PendingIntent.getBroadcast(context, 0, addFlags, 167772160);
    }

    public void onReceive(Context context, Intent intent) {
        int intExtra;
        if (context != null && intent != null && (intExtra = intent.getIntExtra("appWidgetId", -1)) != -1) {
            PeopleTileKey peopleTileKey = new PeopleTileKey(intent.getStringExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID), intent.getIntExtra("android.intent.extra.USER_ID", -1), intent.getStringExtra("android.intent.extra.PACKAGE_NAME"));
            if (PeopleTileKey.isValid(peopleTileKey)) {
                this.mPeopleSpaceWidgetManager.addNewWidget(intExtra, peopleTileKey);
            }
        }
    }
}
