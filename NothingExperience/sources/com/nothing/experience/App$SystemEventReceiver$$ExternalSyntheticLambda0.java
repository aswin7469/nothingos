package com.nothing.experience;

import com.nothing.experience.App;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.service.DataQueryListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class App$SystemEventReceiver$$ExternalSyntheticLambda0 implements DataQueryListener {
    public final /* synthetic */ App.SystemEventReceiver f$0;

    public /* synthetic */ App$SystemEventReceiver$$ExternalSyntheticLambda0(App.SystemEventReceiver systemEventReceiver) {
        this.f$0 = systemEventReceiver;
    }

    public /* synthetic */ void onFail(String str) {
        DataQueryListener.CC.$default$onFail(this, str);
    }

    public final void onSuccess(Object obj) {
        this.f$0.mo13891x78457e71((LocalData) obj);
    }
}
