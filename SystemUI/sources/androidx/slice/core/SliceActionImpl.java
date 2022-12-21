package androidx.slice.core;

import android.app.PendingIntent;
import android.net.wifi.WifiConfiguration;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;

public class SliceActionImpl implements SliceAction {
    private PendingIntent mAction;
    private SliceItem mActionItem;
    private ActionType mActionType;
    private CharSequence mContentDescription;
    private long mDateTimeMillis;
    private IconCompat mIcon;
    private int mImageMode;
    private boolean mIsActivity;
    private boolean mIsChecked;
    private int mPriority;
    private SliceItem mSliceItem;
    private CharSequence mTitle;

    enum ActionType {
        DEFAULT,
        TOGGLE,
        DATE_PICKER,
        TIME_PICKER
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, CharSequence charSequence) {
        this(pendingIntent, iconCompat, 0, charSequence);
    }

    public SliceActionImpl(PendingIntent pendingIntent, CharSequence charSequence, long j, boolean z) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mTitle = charSequence;
        this.mActionType = z ? ActionType.DATE_PICKER : ActionType.TIME_PICKER;
        this.mDateTimeMillis = j;
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mIcon = iconCompat;
        this.mTitle = charSequence;
        this.mImageMode = i;
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, CharSequence charSequence, boolean z) {
        this(pendingIntent, iconCompat, 0, charSequence);
        this.mIsChecked = z;
        this.mActionType = ActionType.TOGGLE;
    }

    public SliceActionImpl(PendingIntent pendingIntent, CharSequence charSequence, boolean z) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mTitle = charSequence;
        this.mActionType = ActionType.TOGGLE;
        this.mIsChecked = z;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SliceActionImpl(androidx.slice.SliceItem r6) {
        /*
            r5 = this;
            r5.<init>()
            r0 = 5
            r5.mImageMode = r0
            androidx.slice.core.SliceActionImpl$ActionType r0 = androidx.slice.core.SliceActionImpl.ActionType.DEFAULT
            r5.mActionType = r0
            r0 = -1
            r5.mPriority = r0
            r1 = -1
            r5.mDateTimeMillis = r1
            r5.mSliceItem = r6
            java.lang.String r1 = "action"
            androidx.slice.SliceItem r6 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r6, (java.lang.String) r1)
            if (r6 != 0) goto L_0x001c
            return
        L_0x001c:
            r5.mActionItem = r6
            android.app.PendingIntent r1 = r6.getAction()
            r5.mAction = r1
            androidx.slice.Slice r1 = r6.getSlice()
            java.lang.String r2 = "image"
            androidx.slice.SliceItem r1 = androidx.slice.core.SliceQuery.find((androidx.slice.Slice) r1, (java.lang.String) r2)
            if (r1 == 0) goto L_0x003c
            androidx.core.graphics.drawable.IconCompat r2 = r1.getIcon()
            r5.mIcon = r2
            int r1 = parseImageMode(r1)
            r5.mImageMode = r1
        L_0x003c:
            androidx.slice.Slice r1 = r6.getSlice()
            java.lang.String r2 = "title"
            r3 = 0
            java.lang.String r4 = "text"
            androidx.slice.SliceItem r1 = androidx.slice.core.SliceQuery.find((androidx.slice.Slice) r1, (java.lang.String) r4, (java.lang.String) r2, (java.lang.String) r3)
            if (r1 == 0) goto L_0x0053
            java.lang.CharSequence r1 = r1.getSanitizedText()
            r5.mTitle = r1
        L_0x0053:
            androidx.slice.Slice r1 = r6.getSlice()
            java.lang.String r2 = "content_description"
            androidx.slice.SliceItem r1 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.Slice) r1, (java.lang.String) r4, (java.lang.String) r2)
            if (r1 == 0) goto L_0x0065
            java.lang.CharSequence r1 = r1.getText()
            r5.mContentDescription = r1
        L_0x0065:
            java.lang.String r1 = r6.getSubType()
            if (r1 != 0) goto L_0x0071
            androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.DEFAULT
            r5.mActionType = r1
            goto L_0x00dd
        L_0x0071:
            java.lang.String r1 = r6.getSubType()
            r1.hashCode()
            int r2 = r1.hashCode()
            switch(r2) {
                case -868304044: goto L_0x0098;
                case 759128640: goto L_0x008c;
                case 1250407999: goto L_0x0081;
                default: goto L_0x007f;
            }
        L_0x007f:
            r1 = r0
            goto L_0x00a3
        L_0x0081:
            java.lang.String r2 = "date_picker"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x008a
            goto L_0x007f
        L_0x008a:
            r1 = 2
            goto L_0x00a3
        L_0x008c:
            java.lang.String r2 = "time_picker"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x0096
            goto L_0x007f
        L_0x0096:
            r1 = 1
            goto L_0x00a3
        L_0x0098:
            java.lang.String r2 = "toggle"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x00a2
            goto L_0x007f
        L_0x00a2:
            r1 = 0
        L_0x00a3:
            java.lang.String r2 = "millis"
            java.lang.String r3 = "long"
            switch(r1) {
                case 0: goto L_0x00d1;
                case 1: goto L_0x00c0;
                case 2: goto L_0x00af;
                default: goto L_0x00aa;
            }
        L_0x00aa:
            androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.DEFAULT
            r5.mActionType = r1
            goto L_0x00dd
        L_0x00af:
            androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.DATE_PICKER
            r5.mActionType = r1
            androidx.slice.SliceItem r1 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.SliceItem) r6, (java.lang.String) r3, (java.lang.String) r2)
            if (r1 == 0) goto L_0x00dd
            long r1 = r1.getLong()
            r5.mDateTimeMillis = r1
            goto L_0x00dd
        L_0x00c0:
            androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TIME_PICKER
            r5.mActionType = r1
            androidx.slice.SliceItem r1 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.SliceItem) r6, (java.lang.String) r3, (java.lang.String) r2)
            if (r1 == 0) goto L_0x00dd
            long r1 = r1.getLong()
            r5.mDateTimeMillis = r1
            goto L_0x00dd
        L_0x00d1:
            androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TOGGLE
            r5.mActionType = r1
            java.lang.String r1 = "selected"
            boolean r1 = r6.hasHint(r1)
            r5.mIsChecked = r1
        L_0x00dd:
            androidx.slice.SliceItem r1 = r5.mSliceItem
            java.lang.String r2 = "activity"
            boolean r1 = r1.hasHint(r2)
            r5.mIsActivity = r1
            androidx.slice.Slice r6 = r6.getSlice()
            java.lang.String r1 = "int"
            java.lang.String r2 = "priority"
            androidx.slice.SliceItem r6 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.Slice) r6, (java.lang.String) r1, (java.lang.String) r2)
            if (r6 == 0) goto L_0x00f9
            int r0 = r6.getInt()
        L_0x00f9:
            r5.mPriority = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.core.SliceActionImpl.<init>(androidx.slice.SliceItem):void");
    }

    public SliceActionImpl setContentDescription(CharSequence charSequence) {
        this.mContentDescription = charSequence;
        return this;
    }

    public SliceActionImpl setChecked(boolean z) {
        this.mIsChecked = z;
        return this;
    }

    public SliceActionImpl setPriority(int i) {
        this.mPriority = i;
        return this;
    }

    public PendingIntent getAction() {
        PendingIntent pendingIntent = this.mAction;
        return pendingIntent != null ? pendingIntent : this.mActionItem.getAction();
    }

    public SliceItem getActionItem() {
        return this.mActionItem;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public boolean isToggle() {
        return this.mActionType == ActionType.TOGGLE;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public int getImageMode() {
        return this.mImageMode;
    }

    public boolean isDefaultToggle() {
        return this.mActionType == ActionType.TOGGLE && this.mIcon == null;
    }

    public SliceItem getSliceItem() {
        return this.mSliceItem;
    }

    public boolean isActivity() {
        return this.mIsActivity;
    }

    public Slice buildSlice(Slice.Builder builder) {
        return builder.addHints("shortcut").addAction(this.mAction, buildSliceContent(builder).build(), getSubtype()).build();
    }

    public Slice buildPrimaryActionSlice(Slice.Builder builder) {
        return buildSliceContent(builder).addHints("shortcut", "title").build();
    }

    private Slice.Builder buildSliceContent(Slice.Builder builder) {
        Slice.Builder builder2 = new Slice.Builder(builder);
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            int i = this.mImageMode;
            builder2.addIcon(iconCompat, (String) null, i == 6 ? new String[]{SliceHints.HINT_SHOW_LABEL} : i == 0 ? new String[0] : new String[]{"no_tint"});
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder2.addText(charSequence, (String) null, "title");
        }
        CharSequence charSequence2 = this.mContentDescription;
        if (charSequence2 != null) {
            builder2.addText(charSequence2, "content_description", new String[0]);
        }
        long j = this.mDateTimeMillis;
        if (j != -1) {
            builder2.addLong(j, SliceHints.SUBTYPE_MILLIS, new String[0]);
        }
        if (this.mActionType == ActionType.TOGGLE && this.mIsChecked) {
            builder2.addHints("selected");
        }
        int i2 = this.mPriority;
        if (i2 != -1) {
            builder2.addInt(i2, WifiConfiguration.priorityVarName, new String[0]);
        }
        if (this.mIsActivity) {
            builder.addHints(SliceHints.HINT_ACTIVITY);
        }
        return builder2;
    }

    /* renamed from: androidx.slice.core.SliceActionImpl$1 */
    static /* synthetic */ class C13401 {
        static final /* synthetic */ int[] $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                androidx.slice.core.SliceActionImpl$ActionType[] r0 = androidx.slice.core.SliceActionImpl.ActionType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType = r0
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TOGGLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType     // Catch:{ NoSuchFieldError -> 0x001d }
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.DATE_PICKER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType     // Catch:{ NoSuchFieldError -> 0x0028 }
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TIME_PICKER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.slice.core.SliceActionImpl.C13401.<clinit>():void");
        }
    }

    public String getSubtype() {
        int i = C13401.$SwitchMap$androidx$slice$core$SliceActionImpl$ActionType[this.mActionType.ordinal()];
        if (i == 1) {
            return "toggle";
        }
        if (i == 2) {
            return SliceHints.SUBTYPE_DATE_PICKER;
        }
        if (i != 3) {
            return null;
        }
        return SliceHints.SUBTYPE_TIME_PICKER;
    }

    public void setActivity(boolean z) {
        this.mIsActivity = z;
    }

    public static int parseImageMode(SliceItem sliceItem) {
        if (sliceItem.hasHint(SliceHints.HINT_SHOW_LABEL)) {
            return 6;
        }
        if (!sliceItem.hasHint("no_tint")) {
            return 0;
        }
        return sliceItem.hasHint(SliceHints.HINT_RAW) ? sliceItem.hasHint("large") ? 4 : 3 : sliceItem.hasHint("large") ? 2 : 1;
    }
}
