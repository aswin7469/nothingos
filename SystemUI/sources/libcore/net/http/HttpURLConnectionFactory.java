package libcore.net.http;

import android.annotation.SystemApi;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class HttpURLConnectionFactory {
    private final com.android.okhttp.internalandroidapi.HttpURLConnectionFactory mFactory = new com.android.okhttp.internalandroidapi.HttpURLConnectionFactory();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static HttpURLConnectionFactory createInstance() {
        return new HttpURLConnectionFactory();
    }

    HttpURLConnectionFactory() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setNewConnectionPool(int i, long j, TimeUnit timeUnit) {
        this.mFactory.setNewConnectionPool(i, j, timeUnit);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setDns(Dns dns) {
        this.mFactory.setDns(dns);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public URLConnection openConnection(URL url, SocketFactory socketFactory, Proxy proxy) throws IOException {
        return this.mFactory.openConnection(url, socketFactory, proxy);
    }
}
