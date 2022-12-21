package androidx.slice.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventInfo {
    public static final int ACTION_TYPE_BUTTON = 1;
    public static final int ACTION_TYPE_CONTENT = 3;
    public static final int ACTION_TYPE_DATE_PICK = 6;
    public static final int ACTION_TYPE_SEE_MORE = 4;
    public static final int ACTION_TYPE_SELECTION = 5;
    public static final int ACTION_TYPE_SLIDER = 2;
    public static final int ACTION_TYPE_TIME_PICK = 7;
    public static final int ACTION_TYPE_TOGGLE = 0;
    public static final int POSITION_CELL = 2;
    public static final int POSITION_END = 1;
    public static final int POSITION_START = 0;
    public static final int ROW_TYPE_DATE_PICK = 7;
    public static final int ROW_TYPE_GRID = 1;
    public static final int ROW_TYPE_LIST = 0;
    public static final int ROW_TYPE_MESSAGING = 2;
    public static final int ROW_TYPE_PROGRESS = 5;
    public static final int ROW_TYPE_SELECTION = 6;
    public static final int ROW_TYPE_SHORTCUT = -1;
    public static final int ROW_TYPE_SLIDER = 4;
    public static final int ROW_TYPE_TIME_PICK = 8;
    public static final int ROW_TYPE_TOGGLE = 3;
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 1;
    public int actionCount = -1;
    public int actionIndex = -1;
    public int actionPosition = -1;
    public int actionType;
    public int rowIndex;
    public int rowTemplateType;
    public int sliceMode;
    public int state = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceActionType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceButtonPosition {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceRowType {
    }

    public EventInfo(int i, int i2, int i3, int i4) {
        this.sliceMode = i;
        this.actionType = i2;
        this.rowTemplateType = i3;
        this.rowIndex = i4;
    }

    public void setPosition(int i, int i2, int i3) {
        this.actionPosition = i;
        this.actionIndex = i2;
        this.actionCount = i3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("mode=");
        sb.append(SliceView.modeToString(this.sliceMode));
        sb.append(", actionType=").append(actionToString(this.actionType));
        sb.append(", rowTemplateType=").append(rowTypeToString(this.rowTemplateType));
        sb.append(", rowIndex=").append(this.rowIndex);
        sb.append(", actionPosition=").append(positionToString(this.actionPosition));
        sb.append(", actionIndex=").append(this.actionIndex);
        sb.append(", actionCount=").append(this.actionCount);
        sb.append(", state=").append(this.state);
        return sb.toString();
    }

    private static String positionToString(int i) {
        if (i == 0) {
            return "START";
        }
        if (i != 1) {
            return i != 2 ? "unknown position: " + i : "CELL";
        }
        return "END";
    }

    private static String actionToString(int i) {
        switch (i) {
            case 0:
                return "TOGGLE";
            case 1:
                return "BUTTON";
            case 2:
                return "SLIDER";
            case 3:
                return "CONTENT";
            case 4:
                return "SEE MORE";
            case 5:
                return "SELECTION";
            case 6:
                return "DATE_PICK";
            case 7:
                return "TIME_PICK";
            default:
                return "unknown action: " + i;
        }
    }

    private static String rowTypeToString(int i) {
        switch (i) {
            case -1:
                return "SHORTCUT";
            case 0:
                return "LIST";
            case 1:
                return "GRID";
            case 2:
                return "MESSAGING";
            case 3:
                return "TOGGLE";
            case 4:
                return "SLIDER";
            case 5:
                return "PROGRESS";
            case 6:
                return "SELECTION";
            case 7:
                return "DATE_PICK";
            case 8:
                return "TIME_PICK";
            default:
                return "unknown row type: " + i;
        }
    }
}
