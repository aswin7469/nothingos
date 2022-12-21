package androidx.remotecallback;

import android.content.Context;
import android.os.Bundle;

public interface CallbackBase<T> {
    RemoteCallback toRemoteCallback(Class<T> cls, Context context, String str, Bundle bundle, String str2);
}
