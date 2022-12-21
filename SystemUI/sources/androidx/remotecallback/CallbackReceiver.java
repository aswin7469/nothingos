package androidx.remotecallback;

import android.content.Context;

public interface CallbackReceiver<T> {
    T createRemoteCallback(Context context);
}
