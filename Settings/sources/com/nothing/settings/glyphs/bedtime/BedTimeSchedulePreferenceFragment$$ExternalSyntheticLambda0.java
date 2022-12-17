package com.nothing.settings.glyphs.bedtime;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BedTimeSchedulePreferenceFragment$$ExternalSyntheticLambda0 implements TimePickerDialog.OnTimeSetListener {
    public final /* synthetic */ BedTimeSchedulePreferenceFragment f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BedTimeSchedulePreferenceFragment$$ExternalSyntheticLambda0(BedTimeSchedulePreferenceFragment bedTimeSchedulePreferenceFragment, int i) {
        this.f$0 = bedTimeSchedulePreferenceFragment;
        this.f$1 = i;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, timePicker, i, i2);
    }
}
