package androidx.remotecallback;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import androidx.remotecallback.ContentProviderWithCallbacks;

public abstract class ContentProviderWithCallbacks<T extends ContentProviderWithCallbacks> extends ContentProvider implements CallbackReceiver<T>, CallbackBase<T> {
    String mAuthority;

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        this.mAuthority = providerInfo.authority;
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        if (!ProviderRelayReceiver.METHOD_PROVIDER_CALLBACK.equals(str)) {
            return super.call(str, str2, bundle);
        }
        CallbackHandlerRegistry.sInstance.invokeCallback(getContext(), this, bundle);
        return null;
    }

    public T createRemoteCallback(Context context) {
        return (ContentProviderWithCallbacks) CallbackHandlerRegistry.sInstance.getAndResetStub(getClass(), context, this.mAuthority);
    }

    public RemoteCallback toRemoteCallback(Class<T> cls, Context context, String str, Bundle bundle, String str2) {
        if (str != null) {
            Intent intent = new Intent(ProviderRelayReceiver.ACTION_PROVIDER_RELAY);
            intent.setComponent(new ComponentName(context.getPackageName(), ProviderRelayReceiver.class.getName()));
            bundle.putString(RemoteCallback.EXTRA_METHOD, str2);
            bundle.putString(ProviderRelayReceiver.EXTRA_AUTHORITY, str);
            intent.putExtras(bundle);
            return new RemoteCallback(context, 1, intent, cls.getName(), bundle);
        }
        throw new IllegalStateException("ContentProvider must be attached before creating callbacks");
    }
}
