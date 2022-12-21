package com.nothing.experience.service;

import com.android.tools.r8.annotations.SynthesizedClass;

public interface DataQueryListener<T> {

    @SynthesizedClass(kind = "$-CC")
    /* renamed from: com.nothing.experience.service.DataQueryListener$-CC  reason: invalid class name */
    public final /* synthetic */ class CC<T> {
        public static void $default$onFail(DataQueryListener _this, String str) {
        }
    }

    void onFail(String str);

    void onSuccess(T t);
}
