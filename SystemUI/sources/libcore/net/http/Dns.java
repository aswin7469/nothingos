package libcore.net.http;

import android.annotation.SystemApi;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public interface Dns extends com.android.okhttp.internalandroidapi.Dns {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    List<InetAddress> lookup(String str) throws UnknownHostException;
}
