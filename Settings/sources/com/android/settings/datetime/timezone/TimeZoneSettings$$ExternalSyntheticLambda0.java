package com.android.settings.datetime.timezone;

import com.android.settings.datetime.timezone.model.TimeZoneData;
import com.android.settings.datetime.timezone.model.TimeZoneDataLoader;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TimeZoneSettings$$ExternalSyntheticLambda0 implements TimeZoneDataLoader.OnDataReadyCallback {
    public final /* synthetic */ TimeZoneSettings f$0;

    public /* synthetic */ TimeZoneSettings$$ExternalSyntheticLambda0(TimeZoneSettings timeZoneSettings) {
        this.f$0 = timeZoneSettings;
    }

    public final void onTimeZoneDataReady(TimeZoneData timeZoneData) {
        this.f$0.onTimeZoneDataReady(timeZoneData);
    }
}
