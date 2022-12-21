package androidx.leanback.widget;

import android.content.Context;
import android.os.Bundle;
import androidx.leanback.widget.GuidedAction;
import java.util.Calendar;

public class GuidedDatePickerAction extends GuidedAction {
    long mDate;
    String mDatePickerFormat;
    long mMaxDate = Long.MAX_VALUE;
    long mMinDate = Long.MIN_VALUE;

    public static abstract class BuilderBase<B extends BuilderBase> extends GuidedAction.BuilderBase<B> {
        private long mDate = Calendar.getInstance().getTimeInMillis();
        private String mDatePickerFormat;
        private long mMaxDate = Long.MAX_VALUE;
        private long mMinDate = Long.MIN_VALUE;

        public BuilderBase(Context context) {
            super(context);
            hasEditableActivatorView(true);
        }

        public B datePickerFormat(String str) {
            this.mDatePickerFormat = str;
            return this;
        }

        public B date(long j) {
            this.mDate = j;
            return this;
        }

        public B minDate(long j) {
            this.mMinDate = j;
            return this;
        }

        public B maxDate(long j) {
            this.mMaxDate = j;
            return this;
        }

        /* access modifiers changed from: protected */
        public final void applyDatePickerValues(GuidedDatePickerAction guidedDatePickerAction) {
            super.applyValues(guidedDatePickerAction);
            guidedDatePickerAction.mDatePickerFormat = this.mDatePickerFormat;
            guidedDatePickerAction.mDate = this.mDate;
            long j = this.mMinDate;
            if (j <= this.mMaxDate) {
                guidedDatePickerAction.mMinDate = j;
                guidedDatePickerAction.mMaxDate = this.mMaxDate;
                return;
            }
            throw new IllegalArgumentException("MinDate cannot be larger than MaxDate");
        }
    }

    public static final class Builder extends BuilderBase<Builder> {
        public Builder(Context context) {
            super(context);
        }

        public GuidedDatePickerAction build() {
            GuidedDatePickerAction guidedDatePickerAction = new GuidedDatePickerAction();
            applyDatePickerValues(guidedDatePickerAction);
            return guidedDatePickerAction;
        }
    }

    public String getDatePickerFormat() {
        return this.mDatePickerFormat;
    }

    public long getDate() {
        return this.mDate;
    }

    public void setDate(long j) {
        this.mDate = j;
    }

    public long getMinDate() {
        return this.mMinDate;
    }

    public long getMaxDate() {
        return this.mMaxDate;
    }

    public void onSaveInstanceState(Bundle bundle, String str) {
        bundle.putLong(str, getDate());
    }

    public void onRestoreInstanceState(Bundle bundle, String str) {
        setDate(bundle.getLong(str, getDate()));
    }
}
