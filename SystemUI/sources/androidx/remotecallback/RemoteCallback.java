package androidx.remotecallback;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RemoteCallback {
    public static final String EXTRA_METHOD = "remotecallback.method";
    public static final RemoteCallback LOCAL = new RemoteCallback((Context) null, -1, (Intent) null, (String) null, (Bundle) null) {
        public int getType() {
            throw new UnsupportedOperationException("RemoteCallback.LOCAL cannot be used");
        }

        public Bundle getArgumentBundle() {
            throw new UnsupportedOperationException("RemoteCallback.LOCAL cannot be used");
        }

        public String getMethodName() {
            throw new UnsupportedOperationException("RemoteCallback.LOCAL cannot be used");
        }

        public String getReceiverClass() {
            throw new UnsupportedOperationException("RemoteCallback.LOCAL cannot be used");
        }

        public PendingIntent toPendingIntent() {
            throw new UnsupportedOperationException("RemoteCallback.LOCAL cannot be used");
        }
    };
    public static final int TYPE_PROVIDER = 1;
    public static final int TYPE_RECEIVER = 0;
    private final Bundle mArguments;
    private final Context mContext;
    private final Intent mIntent;
    private final String mReceiverClass;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface RemoteCallbackType {
    }

    public RemoteCallback(Context context, int i, Intent intent, String str, Bundle bundle) {
        this.mContext = context;
        this.mType = i;
        this.mIntent = intent;
        this.mReceiverClass = str;
        this.mArguments = bundle;
    }

    public int getType() {
        return this.mType;
    }

    public String getReceiverClass() {
        return this.mReceiverClass;
    }

    public String getMethodName() {
        return this.mArguments.getString(EXTRA_METHOD);
    }

    public Bundle getArgumentBundle() {
        return this.mArguments;
    }

    public PendingIntent toPendingIntent() {
        Intent intent = this.mIntent;
        intent.setData(generateUri(intent));
        this.mIntent.addFlags(268435456);
        return PendingIntent.getBroadcast(this.mContext, 0, this.mIntent, 134217728);
    }

    private static Uri generateUri(Intent intent) {
        if (intent.getData() != null) {
            return intent.getData();
        }
        Uri.Builder authority = new Uri.Builder().scheme("remotecallback").authority("");
        Bundle extras = intent.getExtras();
        for (String str : extras.keySet()) {
            authority.appendQueryParameter(str, String.valueOf(extras.get(str)));
        }
        return authority.build();
    }

    public static <T extends CallbackReceiver> T create(Class<T> cls, Context context) {
        return CallbackHandlerRegistry.sInstance.getAndResetStub(cls, context, (String) null);
    }
}
