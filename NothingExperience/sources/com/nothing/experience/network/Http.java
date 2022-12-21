package com.nothing.experience.network;

import android.util.SparseArray;
import com.nothing.experience.utils.LogUtil;

public class Http {
    public static String activationUrl = "https://dc-api.nothingtech.link/mobile/activate";
    public static String apiKey = "wc3W67RnBBbEoq7ddObO4x40Dr4v3qgX";
    public static String productUrl = "https://dc-api.nothingtech.link/mobile/behavior";
    public static String qualityUrl = "https://dc-api.nothingtech.link/mobile/quality";
    private static final ThreadPoolManager sManager = ThreadPoolManager.getHttpInstance();
    public static SparseArray<String> sUrlMapping = new SparseArray<>();

    public static <T, M> void sendJsonRequest(T t, String str, Class<M> cls, IDataListener<M> iDataListener) {
        sManager.execute(new HttpTask(t, str, new JsonHttpRequest(), new JsonHttpListener(cls, iDataListener)));
    }

    public static void resetUrl() {
        if (LogUtil.isTestEnv()) {
            productUrl = "https://testapi.nothingtech.link/mobile/behavior";
            qualityUrl = "https://testapi.nothingtech.link/mobile/quality";
            activationUrl = "https://testapi.nothingtech.link/mobile/activate";
        } else {
            productUrl = "https://dc-api.nothingtech.link/mobile/behavior";
            qualityUrl = "https://dc-api.nothingtech.link/mobile/quality";
            activationUrl = "https://dc-api.nothingtech.link/mobile/activate";
        }
        sUrlMapping.put(1, activationUrl);
        sUrlMapping.put(0, productUrl);
        sUrlMapping.put(2, qualityUrl);
    }
}
