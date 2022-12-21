package com.nothing.experience.utils;

import com.nothing.experience.datas.LocalData;
import com.nothing.experience.service.DataQueryListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DataSyncTask$$ExternalSyntheticLambda1 implements DataQueryListener {
    public final /* synthetic */ DataSyncTask f$0;

    public /* synthetic */ DataSyncTask$$ExternalSyntheticLambda1(DataSyncTask dataSyncTask) {
        this.f$0 = dataSyncTask;
    }

    public /* synthetic */ void onFail(String str) {
        DataQueryListener.CC.$default$onFail(this, str);
    }

    public final void onSuccess(Object obj) {
        this.f$0.mo13966xd906d711((LocalData) obj);
    }
}
