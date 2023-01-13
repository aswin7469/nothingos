package libcore.net;

import android.annotation.SystemApi;
import android.system.GaiException;
import android.system.OsConstants;
import android.system.StructAddrinfo;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.InetAddress;
import libcore.p030io.Libcore;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class InetAddressUtils {
    private static final int NETID_UNSET = 0;

    private InetAddressUtils() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isNumericAddress(String str) {
        return parseNumericAddressNoThrow(str) != null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static InetAddress parseNumericAddress(String str) {
        InetAddress parseNumericAddressNoThrow = parseNumericAddressNoThrow(str);
        if (parseNumericAddressNoThrow != null) {
            return parseNumericAddressNoThrow;
        }
        throw new IllegalArgumentException("Not a numeric address: " + str);
    }

    public static InetAddress parseNumericAddressNoThrow(String str) {
        InetAddress[] inetAddressArr;
        StructAddrinfo structAddrinfo = new StructAddrinfo();
        structAddrinfo.ai_flags = OsConstants.AI_NUMERICHOST;
        try {
            inetAddressArr = Libcore.f855os.android_getaddrinfo(str, structAddrinfo, 0);
        } catch (GaiException unused) {
            inetAddressArr = null;
        }
        if (inetAddressArr == null) {
            return null;
        }
        return inetAddressArr[0];
    }

    public static InetAddress parseNumericAddressNoThrowStripOptionalBrackets(String str) {
        if (str.startsWith(NavigationBarInflaterView.SIZE_MOD_START) && str.endsWith(NavigationBarInflaterView.SIZE_MOD_END) && str.indexOf(58) != -1) {
            str = str.substring(1, str.length() - 1);
        }
        return parseNumericAddressNoThrow(str);
    }
}
