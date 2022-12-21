package com.nothing.experience.utils;

import com.nothing.experience.datas.LocalData;
import com.nothing.experience.service.DataQueryListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DataSyncTask$$ExternalSyntheticLambda2 implements DataQueryListener {
    public final /* synthetic */ DataSyncTask f$0;

    public /* synthetic */ DataSyncTask$$ExternalSyntheticLambda2(DataSyncTask dataSyncTask) {
        this.f$0 = dataSyncTask;
    }

    public /* synthetic */ void onFail(String str) {
        DataQueryListener.CC.$default$onFail(this, str);
    }

    public final void onSuccess(Object obj) {
        this.f$0.mo13967xbe4845d2((LocalData) obj);
    }
}
