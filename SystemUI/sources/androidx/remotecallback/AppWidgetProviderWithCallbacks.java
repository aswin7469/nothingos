package androidx.remotecallback;

import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.remotecallback.CallbackReceiver;

public class AppWidgetProviderWithCallbacks<T extends CallbackReceiver> extends AppWidgetProvider implements CallbackReceiver<T>, CallbackBase<T> {
    public void onReceive(Context context, Intent intent) {
        if (BroadcastReceiverWithCallbacks.ACTION_BROADCAST_CALLBACK.equals(intent.getAction())) {
            CallbackHandlerRegistry.sInstance.invokeCallback(context, this, intent);
        } else {
            super.onReceive(context, intent);
        }
    }

    public T createRemoteCallback(Context context) {
        return CallbackHandlerRegistry.sInstance.getAndResetStub(getClass(), context, (String) null);
    }

    public RemoteCallback toRemoteCallback(Class<T> cls, Context context, String str, Bundle bundle, String str2) {
        Intent intent = new Intent(BroadcastReceiverWithCallbacks.ACTION_BROADCAST_CALLBACK);
        intent.setComponent(new ComponentName(context.getPackageName(), cls.getName()));
        bundle.putString(RemoteCallback.EXTRA_METHOD, str2);
        intent.putExtras(bundle);
        return new RemoteCallback(context, 0, intent, cls.getName(), bundle);
    }
}
