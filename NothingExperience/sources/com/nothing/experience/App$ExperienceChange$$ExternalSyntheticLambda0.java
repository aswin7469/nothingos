package com.nothing.experience;

import com.nothing.experience.App;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.service.DataQueryListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class App$ExperienceChange$$ExternalSyntheticLambda0 implements DataQueryListener {
    public final /* synthetic */ App.ExperienceChange f$0;

    public /* synthetic */ App$ExperienceChange$$ExternalSyntheticLambda0(App.ExperienceChange experienceChange) {
        this.f$0 = experienceChange;
    }

    public /* synthetic */ void onFail(String str) {
        DataQueryListener.CC.$default$onFail(this, str);
    }

    public final void onSuccess(Object obj) {
        this.f$0.m88lambda$onChange$0$comnothingexperienceApp$ExperienceChange((LocalData) obj);
    }
}
