package com.android.settingslib;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Process;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.Log;
import java.util.Set;

public class SliceBroadcastRelay {
    public static final String ACTION_REGISTER = "com.android.settingslib.action.REGISTER_SLICE_RECEIVER";
    public static final String ACTION_UNREGISTER = "com.android.settingslib.action.UNREGISTER_SLICE_RECEIVER";
    public static final String EXTRA_FILTER = "filter";
    public static final String EXTRA_RECEIVER = "receiver";
    public static final String EXTRA_URI = "uri";
    public static final String SYSTEMUI_PACKAGE = "com.android.systemui";
    private static final String TAG = "SliceBroadcastRelay";
    private static final Set<Uri> sRegisteredUris = new ArraySet();

    public static void registerReceiver(Context context, Uri uri, Class<? extends BroadcastReceiver> cls, IntentFilter intentFilter) {
        Log.d(TAG, "Registering Uri for broadcast relay: " + uri);
        sRegisteredUris.add(uri);
        Intent intent = new Intent(ACTION_REGISTER);
        intent.setPackage("com.android.systemui");
        intent.putExtra(EXTRA_URI, ContentProvider.maybeAddUserId(uri, Process.myUserHandle().getIdentifier()));
        intent.putExtra(EXTRA_RECEIVER, new ComponentName(context.getPackageName(), cls.getName()));
        intent.putExtra(EXTRA_FILTER, intentFilter);
        context.sendBroadcastAsUser(intent, UserHandle.SYSTEM);
    }

    public static void unregisterReceivers(Context context, Uri uri) {
        Set<Uri> set = sRegisteredUris;
        if (set.contains(uri)) {
            Log.d(TAG, "Unregistering uri broadcast relay: " + uri);
            Intent intent = new Intent(ACTION_UNREGISTER);
            intent.setPackage("com.android.systemui");
            intent.putExtra(EXTRA_URI, ContentProvider.maybeAddUserId(uri, Process.myUserHandle().getIdentifier()));
            context.sendBroadcastAsUser(intent, UserHandle.SYSTEM);
            set.remove(uri);
        }
    }
}
