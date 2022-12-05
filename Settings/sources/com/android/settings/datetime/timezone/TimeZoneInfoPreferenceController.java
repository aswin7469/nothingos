package com.android.settings.datetime.timezone;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.icu.util.BasicTimeZone;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.icu.util.TimeZoneTransition;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.Date;
/* loaded from: classes.dex */
public class TimeZoneInfoPreferenceController extends BasePreferenceController {
    Date mDate = new Date();
    private final DateFormat mDateFormat;
    private TimeZoneInfo mTimeZoneInfo;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TimeZoneInfoPreferenceController(Context context, String str) {
        super(context, str);
        DateFormat dateInstance = DateFormat.getDateInstance(1);
        this.mDateFormat = dateInstance;
        dateInstance.setContext(DisplayContext.CAPITALIZATION_NONE);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mTimeZoneInfo != null ? 1 : 3;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        TimeZoneInfo timeZoneInfo = this.mTimeZoneInfo;
        return timeZoneInfo == null ? "" : formatInfo(timeZoneInfo);
    }

    public void setTimeZoneInfo(TimeZoneInfo timeZoneInfo) {
        this.mTimeZoneInfo = timeZoneInfo;
    }

    private CharSequence formatOffsetAndName(TimeZoneInfo timeZoneInfo) {
        String genericName = timeZoneInfo.getGenericName();
        if (genericName == null) {
            if (timeZoneInfo.getTimeZone().inDaylightTime(this.mDate)) {
                genericName = timeZoneInfo.getDaylightName();
            } else {
                genericName = timeZoneInfo.getStandardName();
            }
        }
        if (genericName == null) {
            return timeZoneInfo.getGmtOffset().toString();
        }
        return SpannableUtil.getResourcesText(this.mContext.getResources(), R.string.zone_info_offset_and_name, timeZoneInfo.getGmtOffset(), genericName);
    }

    private CharSequence formatInfo(TimeZoneInfo timeZoneInfo) {
        CharSequence formatOffsetAndName = formatOffsetAndName(timeZoneInfo);
        TimeZone timeZone = timeZoneInfo.getTimeZone();
        if (!timeZone.observesDaylightTime()) {
            return this.mContext.getString(R.string.zone_info_footer_no_dst, formatOffsetAndName);
        }
        TimeZoneTransition findNextDstTransition = findNextDstTransition(timeZone);
        if (findNextDstTransition == null) {
            return null;
        }
        boolean z = findNextDstTransition.getTo().getDSTSavings() != 0;
        String daylightName = z ? timeZoneInfo.getDaylightName() : timeZoneInfo.getStandardName();
        if (daylightName == null) {
            if (z) {
                daylightName = this.mContext.getString(R.string.zone_time_type_dst);
            } else {
                daylightName = this.mContext.getString(R.string.zone_time_type_standard);
            }
        }
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(findNextDstTransition.getTime());
        return SpannableUtil.getResourcesText(this.mContext.getResources(), R.string.zone_info_footer, formatOffsetAndName, daylightName, this.mDateFormat.format(calendar));
    }

    private TimeZoneTransition findNextDstTransition(TimeZone timeZone) {
        if (!(timeZone instanceof BasicTimeZone)) {
            return null;
        }
        BasicTimeZone basicTimeZone = (BasicTimeZone) timeZone;
        TimeZoneTransition nextTransition = basicTimeZone.getNextTransition(this.mDate.getTime(), false);
        while (nextTransition.getTo().getDSTSavings() == nextTransition.getFrom().getDSTSavings() && (nextTransition = basicTimeZone.getNextTransition(nextTransition.getTime(), false)) != null) {
        }
        return nextTransition;
    }
}
