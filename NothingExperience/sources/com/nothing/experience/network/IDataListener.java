package com.nothing.experience.network;

import com.android.tools.r8.annotations.SynthesizedClass;
import com.nothing.experience.utils.LogUtil;

public interface IDataListener<M> {
    void onFailure(int i, String str);

    void onSuccess();

    void onSuccess(M m);

    @SynthesizedClass(kind = "$-CC")
    /* renamed from: com.nothing.experience.network.IDataListener$-CC  reason: invalid class name */
    public final /* synthetic */ class CC<M> {
        public static void $default$onSuccess(IDataListener _this) {
        }

        public static void $default$onSuccess(IDataListener _this, Object obj) {
        }

        public static void $default$onFailure(IDataListener _this, int i, String str) {
            LogUtil.m42d("fail code:" + i + " msg:" + str);
        }
    }
}
