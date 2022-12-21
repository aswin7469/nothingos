package android.net.wifi.p2p.nsd;

import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.wifi.p018x.com.android.net.module.util.DnsSdTxtRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WifiP2pDnsSdServiceInfo extends WifiP2pServiceInfo {
    public static final int DNS_TYPE_PTR = 12;
    public static final int DNS_TYPE_TXT = 16;
    public static final int VERSION_1 = 1;
    private static final Map<String, String> sVmPacket;

    static {
        HashMap hashMap = new HashMap();
        sVmPacket = hashMap;
        hashMap.put("_tcp.local.", "c00c");
        hashMap.put("local.", "c011");
        hashMap.put("_udp.local.", "c01c");
    }

    private WifiP2pDnsSdServiceInfo(List<String> list) {
        super(list);
    }

    public static WifiP2pDnsSdServiceInfo newInstance(String str, String str2, Map<String, String> map) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("instance name or service type cannot be empty");
        }
        DnsSdTxtRecord dnsSdTxtRecord = new DnsSdTxtRecord();
        if (map != null) {
            for (String next : map.keySet()) {
                dnsSdTxtRecord.set(next, map.get(next));
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(createPtrServiceQuery(str, str2));
        arrayList.add(createTxtServiceQuery(str, str2, dnsSdTxtRecord));
        return new WifiP2pDnsSdServiceInfo(arrayList);
    }

    private static String createPtrServiceQuery(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer("bonjour ");
        stringBuffer.append(createRequest(str2 + ".local.", 12, 1));
        stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        byte[] bytes = str.getBytes();
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(bytes.length)));
        stringBuffer.append(WifiP2pServiceInfo.bin2HexStr(bytes));
        stringBuffer.append("c027");
        return stringBuffer.toString();
    }

    private static String createTxtServiceQuery(String str, String str2, DnsSdTxtRecord dnsSdTxtRecord) {
        StringBuffer stringBuffer = new StringBuffer("bonjour ");
        stringBuffer.append(createRequest(str + BaseIconCache.EMPTY_CLASS_NAME + str2 + ".local.", 16, 1));
        stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        byte[] rawData = dnsSdTxtRecord.getRawData();
        if (rawData.length == 0) {
            stringBuffer.append("00");
        } else {
            stringBuffer.append(bin2HexStr(rawData));
        }
        return stringBuffer.toString();
    }

    static String createRequest(String str, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i == 16) {
            str = str.toLowerCase(Locale.ROOT);
        }
        stringBuffer.append(compressDnsName(str));
        stringBuffer.append(String.format(Locale.f700US, "%04x", Integer.valueOf(i)));
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(i2)));
        return stringBuffer.toString();
    }

    private static String compressDnsName(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String str2 = sVmPacket.get(str);
            if (str2 != null) {
                stringBuffer.append(str2);
                break;
            }
            int indexOf = str.indexOf(46);
            if (indexOf == -1) {
                if (str.length() > 0) {
                    stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(str.length())));
                    stringBuffer.append(WifiP2pServiceInfo.bin2HexStr(str.getBytes()));
                }
                stringBuffer.append("00");
            } else {
                String substring = str.substring(0, indexOf);
                str = str.substring(indexOf + 1);
                stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(substring.length())));
                stringBuffer.append(WifiP2pServiceInfo.bin2HexStr(substring.getBytes()));
            }
        }
        return stringBuffer.toString();
    }
}
