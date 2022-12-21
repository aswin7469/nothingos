package com.android.systemui.dreams.complication;

import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Complication {
    public static final int CATEGORY_STANDARD = 1;
    public static final int CATEGORY_SYSTEM = 2;
    public static final int COMPLICATION_TYPE_AIR_QUALITY = 8;
    public static final int COMPLICATION_TYPE_CAST_INFO = 16;
    public static final int COMPLICATION_TYPE_DATE = 2;
    public static final int COMPLICATION_TYPE_NONE = 0;
    public static final int COMPLICATION_TYPE_TIME = 1;
    public static final int COMPLICATION_TYPE_WEATHER = 4;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ComplicationType {
    }

    public interface Host {
        void requestExitDream();
    }

    public interface ViewHolder {
        int getCategory() {
            return 1;
        }

        ComplicationLayoutParams getLayoutParams();

        View getView();
    }

    public interface VisibilityController {
        void setVisibility(int i, boolean z);
    }

    ViewHolder createView(ComplicationViewModel complicationViewModel);

    int getRequiredTypeAvailability() {
        return 0;
    }
}
