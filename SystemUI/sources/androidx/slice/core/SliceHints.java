package androidx.slice.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SliceHints {
    public static final int ACTION_WITH_LABEL = 6;
    public static final int DETERMINATE_RANGE = 0;
    public static final String HINT_ACTIVITY = "activity";
    public static final String HINT_CACHED = "cached";
    public static final String HINT_END_OF_SECTION = "end_of_section";
    public static final String HINT_OVERLAY = "overlay";
    public static final String HINT_RAW = "raw";
    public static final String HINT_SELECTION_OPTION = "selection_option";
    public static final String HINT_SHOW_LABEL = "show_label";
    public static final int ICON_IMAGE = 0;
    public static final int INDETERMINATE_RANGE = 1;
    public static final long INFINITY = -1;
    public static final int LARGE_IMAGE = 2;
    public static final int RAW_IMAGE_LARGE = 4;
    public static final int RAW_IMAGE_SMALL = 3;
    public static final String SLICE_METADATA_KEY = "android.metadata.SLICE_URI";
    public static final int SMALL_IMAGE = 1;
    public static final int STAR_RATING = 2;
    public static final String SUBTYPE_DATE_PICKER = "date_picker";
    public static final String SUBTYPE_HOST_EXTRAS = "host_extras";
    public static final String SUBTYPE_MILLIS = "millis";
    public static final String SUBTYPE_MIN = "min";
    public static final String SUBTYPE_SELECTION = "selection";
    public static final String SUBTYPE_SELECTION_OPTION_KEY = "selection_option_key";
    public static final String SUBTYPE_SELECTION_OPTION_VALUE = "selection_option_value";
    public static final String SUBTYPE_TIME_PICKER = "time_picker";
    public static final int UNKNOWN_IMAGE = 5;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageMode {
    }

    private SliceHints() {
    }
}
