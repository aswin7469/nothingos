package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.INetworkOfferCallback;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;

@SystemApi
public class NetworkProvider {
    public static final int CMD_CANCEL_REQUEST = 2;
    public static final int CMD_REQUEST_NETWORK = 1;
    public static final int FIRST_PROVIDER_ID = 1;
    public static final int ID_NONE = -1;
    private final Context mContext;
    private final Messenger mMessenger;
    /* access modifiers changed from: private */
    public final String mName;
    private int mProviderId = -1;
    private final ArrayList<NetworkOfferCallbackProxy> mProxies = new ArrayList<>();

    @SystemApi
    public interface NetworkOfferCallback {
        void onNetworkNeeded(NetworkRequest networkRequest);

        void onNetworkUnneeded(NetworkRequest networkRequest);
    }

    @SystemApi
    public void onNetworkRequestWithdrawn(NetworkRequest networkRequest) {
    }

    @SystemApi
    public void onNetworkRequested(NetworkRequest networkRequest, int i, int i2) {
    }

    @SystemApi
    public NetworkProvider(Context context, Looper looper, String str) {
        C00801 r0 = new Handler(looper) {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    NetworkProvider.this.onNetworkRequested((NetworkRequest) message.obj, message.arg1, message.arg2);
                } else if (i != 2) {
                    String r2 = NetworkProvider.this.mName;
                    Log.e(r2, "Unhandled message: " + message.what);
                } else {
                    NetworkProvider.this.onNetworkRequestWithdrawn((NetworkRequest) message.obj);
                }
            }
        };
        this.mContext = context;
        this.mMessenger = new Messenger(r0);
        this.mName = str;
    }

    public Messenger getMessenger() {
        return this.mMessenger;
    }

    public String getName() {
        return this.mName;
    }

    public int getProviderId() {
        return this.mProviderId;
    }

    public void setProviderId(int i) {
        this.mProviderId = i;
    }

    @SystemApi
    public void declareNetworkRequestUnfulfillable(NetworkRequest networkRequest) {
        ConnectivityManager.from(this.mContext).declareNetworkRequestUnfulfillable(networkRequest);
    }

    private class NetworkOfferCallbackProxy extends INetworkOfferCallback.Stub {
        public final NetworkOfferCallback callback;
        private final Executor mExecutor;

        NetworkOfferCallbackProxy(NetworkOfferCallback networkOfferCallback, Executor executor) {
            this.callback = networkOfferCallback;
            this.mExecutor = executor;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onNetworkNeeded$0$android-net-NetworkProvider$NetworkOfferCallbackProxy */
        public /* synthetic */ void mo3056x7e11d377(NetworkRequest networkRequest) {
            this.callback.onNetworkNeeded(networkRequest);
        }

        public void onNetworkNeeded(NetworkRequest networkRequest) {
            this.mExecutor.execute(new C0082xaead4de6(this, networkRequest));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onNetworkUnneeded$1$android-net-NetworkProvider$NetworkOfferCallbackProxy */
        public /* synthetic */ void mo3057x8a023511(NetworkRequest networkRequest) {
            this.callback.onNetworkUnneeded(networkRequest);
        }

        public void onNetworkUnneeded(NetworkRequest networkRequest) {
            this.mExecutor.execute(new C0081xaead4de5(this, networkRequest));
        }
    }

    private NetworkOfferCallbackProxy findProxyForCallback(NetworkOfferCallback networkOfferCallback) {
        synchronized (this.mProxies) {
            Iterator<NetworkOfferCallbackProxy> it = this.mProxies.iterator();
            while (it.hasNext()) {
                NetworkOfferCallbackProxy next = it.next();
                if (next.callback == networkOfferCallback) {
                    return next;
                }
            }
            return null;
        }
    }

    @SystemApi
    public void registerNetworkOffer(NetworkScore networkScore, NetworkCapabilities networkCapabilities, Executor executor, NetworkOfferCallback networkOfferCallback) {
        NetworkOfferCallbackProxy networkOfferCallbackProxy;
        int i = this.mProviderId;
        if (i != -1) {
            synchronized (this.mProxies) {
                Iterator<NetworkOfferCallbackProxy> it = this.mProxies.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        networkOfferCallbackProxy = null;
                        break;
                    }
                    networkOfferCallbackProxy = it.next();
                    if (networkOfferCallbackProxy.callback == networkOfferCallback) {
                        break;
                    }
                }
                if (networkOfferCallbackProxy == null) {
                    networkOfferCallbackProxy = new NetworkOfferCallbackProxy(networkOfferCallback, executor);
                    this.mProxies.add(networkOfferCallbackProxy);
                }
            }
            ((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class)).offerNetwork(i, networkScore, networkCapabilities, networkOfferCallbackProxy);
        }
    }

    @SystemApi
    public void unregisterNetworkOffer(NetworkOfferCallback networkOfferCallback) {
        NetworkOfferCallbackProxy findProxyForCallback = findProxyForCallback(networkOfferCallback);
        if (findProxyForCallback != null) {
            this.mProxies.remove((Object) findProxyForCallback);
            ((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class)).unofferNetwork(findProxyForCallback);
        }
    }
}
