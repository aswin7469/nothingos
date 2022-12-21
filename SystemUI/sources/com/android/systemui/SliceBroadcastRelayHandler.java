package com.android.systemui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class SliceBroadcastRelayHandler extends CoreStartable {
    private static final boolean DEBUG = false;
    private static final String TAG = "SliceBroadcastRelay";
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            SliceBroadcastRelayHandler.this.handleIntent(intent);
        }
    };
    private final ArrayMap<Uri, BroadcastRelay> mRelays = new ArrayMap<>();

    @Inject
    public SliceBroadcastRelayHandler(Context context, BroadcastDispatcher broadcastDispatcher) {
        super(context);
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter(SliceBroadcastRelay.ACTION_REGISTER);
        intentFilter.addAction(SliceBroadcastRelay.ACTION_UNREGISTER);
        this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
    }

    /* access modifiers changed from: package-private */
    public void handleIntent(Intent intent) {
        BroadcastRelay andRemoveRelay;
        if (SliceBroadcastRelay.ACTION_REGISTER.equals(intent.getAction())) {
            getOrCreateRelay((Uri) intent.getParcelableExtra(SliceBroadcastRelay.EXTRA_URI)).register(this.mContext, (ComponentName) intent.getParcelableExtra(SliceBroadcastRelay.EXTRA_RECEIVER), (IntentFilter) intent.getParcelableExtra(SliceBroadcastRelay.EXTRA_FILTER));
        } else if (SliceBroadcastRelay.ACTION_UNREGISTER.equals(intent.getAction()) && (andRemoveRelay = getAndRemoveRelay((Uri) intent.getParcelableExtra(SliceBroadcastRelay.EXTRA_URI))) != null) {
            andRemoveRelay.unregister(this.mContext);
        }
    }

    private BroadcastRelay getOrCreateRelay(Uri uri) {
        BroadcastRelay broadcastRelay = this.mRelays.get(uri);
        if (broadcastRelay != null) {
            return broadcastRelay;
        }
        BroadcastRelay broadcastRelay2 = new BroadcastRelay(uri);
        this.mRelays.put(uri, broadcastRelay2);
        return broadcastRelay2;
    }

    private BroadcastRelay getAndRemoveRelay(Uri uri) {
        return this.mRelays.remove(uri);
    }

    private static class BroadcastRelay extends BroadcastReceiver {
        private final ArraySet<ComponentName> mReceivers = new ArraySet<>();
        private final Uri mUri;
        private final UserHandle mUserId;

        public BroadcastRelay(Uri uri) {
            this.mUserId = new UserHandle(ContentProvider.getUserIdFromUri(uri));
            this.mUri = uri;
        }

        public void register(Context context, ComponentName componentName, IntentFilter intentFilter) {
            this.mReceivers.add(componentName);
            context.registerReceiver(this, intentFilter, 2);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }

        public void onReceive(Context context, Intent intent) {
            intent.addFlags(268435456);
            Iterator<ComponentName> it = this.mReceivers.iterator();
            while (it.hasNext()) {
                intent.setComponent(it.next());
                intent.putExtra(SliceBroadcastRelay.EXTRA_URI, this.mUri.toString());
                context.sendBroadcastAsUser(intent, this.mUserId);
            }
        }
    }
}
