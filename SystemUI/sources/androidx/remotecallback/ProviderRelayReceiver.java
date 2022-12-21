package androidx.remotecallback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ProviderRelayReceiver extends BroadcastReceiver {
    public static final String ACTION_PROVIDER_RELAY = "androidx.remotecallback.action.PROVIDER_RELAY";
    public static final String EXTRA_AUTHORITY = "androidx.remotecallback.extra.AUTHORITY";
    public static final String METHOD_PROVIDER_CALLBACK = "androidx.remotecallback.method.PROVIDER_CALLBACK";

    public void onReceive(Context context, Intent intent) {
        if (ACTION_PROVIDER_RELAY.equals(intent.getAction())) {
            context.getContentResolver().call(new Uri.Builder().scheme("content").authority(intent.getStringExtra(EXTRA_AUTHORITY)).build(), METHOD_PROVIDER_CALLBACK, (String) null, intent.getExtras());
        }
    }
}
