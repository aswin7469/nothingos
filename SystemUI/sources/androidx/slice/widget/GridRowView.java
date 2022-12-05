package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.content.ContextCompat;
import androidx.slice.CornerDrawable;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$drawable;
import androidx.slice.view.R$id;
import androidx.slice.view.R$layout;
import androidx.slice.view.R$string;
import androidx.slice.view.R$style;
import androidx.slice.widget.GridContent;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
/* loaded from: classes.dex */
public class GridRowView extends SliceChildView implements View.OnClickListener, View.OnTouchListener {
    private static final int TEXT_LAYOUT = R$layout.abc_slice_secondary_text;
    protected final View mForeground;
    protected GridContent mGridContent;
    private final int mGutter;
    private int mHiddenItemCount;
    protected final int mIconSize;
    protected final int mLargeImageHeight;
    private final int[] mLoc;
    boolean mMaxCellUpdateScheduled;
    protected int mMaxCells;
    private final ViewTreeObserver.OnPreDrawListener mMaxCellsUpdater;
    protected int mRowCount;
    protected int mRowIndex;
    protected final int mSmallImageMinWidth;
    protected final int mSmallImageSize;
    private final int mTextPadding;
    protected final LinearLayout mViewContainer;

    public GridRowView(Context context) {
        this(context, null);
    }

    public GridRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLoc = new int[2];
        this.mMaxCells = -1;
        this.mMaxCellsUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: androidx.slice.widget.GridRowView.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                GridRowView gridRowView = GridRowView.this;
                gridRowView.mMaxCells = gridRowView.getMaxCells();
                GridRowView.this.populateViews();
                GridRowView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                GridRowView.this.mMaxCellUpdateScheduled = false;
                return true;
            }
        };
        Resources resources = getContext().getResources();
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mViewContainer = linearLayout;
        linearLayout.setOrientation(0);
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
        linearLayout.setGravity(16);
        this.mIconSize = resources.getDimensionPixelSize(R$dimen.abc_slice_icon_size);
        this.mSmallImageSize = resources.getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
        this.mLargeImageHeight = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_only_height);
        this.mSmallImageMinWidth = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_min_width);
        this.mGutter = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_gutter);
        this.mTextPadding = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_text_padding);
        View view = new View(getContext());
        this.mForeground = view;
        addView(view, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setInsets(int l, int t, int r, int b) {
        super.setInsets(l, t, r, b);
        this.mViewContainer.setPadding(l, t + getExtraTopPadding(), r, b + getExtraBottomPadding());
    }

    protected int getTitleTextLayout() {
        return R$layout.abc_slice_title;
    }

    protected int getExtraTopPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages() || this.mRowIndex != 0 || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.getGridTopPadding();
    }

    protected int getExtraBottomPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages()) {
            return 0;
        }
        if ((this.mRowIndex != this.mRowCount - 1 && getMode() != 1) || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.getGridBottomPadding();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = this.mGridContent.getHeight(this.mSliceStyle, this.mViewPolicy) + this.mInsetTop + this.mInsetBottom;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
        this.mViewContainer.getLayoutParams().height = height;
        super.onMeasure(widthMeasureSpec, makeMeasureSpec);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setTint(int tintColor) {
        super.setTint(tintColor);
        if (this.mGridContent != null) {
            resetView();
            populateViews();
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceItem(SliceContent slice, boolean isHeader, int rowIndex, int rowCount, SliceView.OnSliceActionListener observer) {
        resetView();
        setSliceActionListener(observer);
        this.mRowIndex = rowIndex;
        this.mRowCount = rowCount;
        this.mGridContent = (GridContent) slice;
        if (!scheduleMaxCellsUpdate()) {
            populateViews();
        }
        this.mViewContainer.setPadding(this.mInsetStart, this.mInsetTop + getExtraTopPadding(), this.mInsetEnd, this.mInsetBottom + getExtraBottomPadding());
    }

    protected boolean scheduleMaxCellsUpdate() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            return true;
        }
        if (getWidth() == 0) {
            this.mMaxCellUpdateScheduled = true;
            getViewTreeObserver().addOnPreDrawListener(this.mMaxCellsUpdater);
            return true;
        }
        this.mMaxCells = getMaxCells();
        return false;
    }

    protected int getMaxCells() {
        int i;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid() || getWidth() == 0) {
            return -1;
        }
        if (this.mGridContent.getGridContent().size() <= 1) {
            return 1;
        }
        int largestImageMode = this.mGridContent.getLargestImageMode();
        if (largestImageMode == 2) {
            i = this.mLargeImageHeight;
        } else if (largestImageMode == 4) {
            i = this.mGridContent.getFirstImageSize(getContext()).x;
        } else {
            i = this.mSmallImageMinWidth;
        }
        return getWidth() / (i + this.mGutter);
    }

    protected void populateViews() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            resetView();
        } else if (!scheduleMaxCellsUpdate()) {
            if (this.mGridContent.getLayoutDir() != -1) {
                setLayoutDirection(this.mGridContent.getLayoutDir());
            }
            boolean z = true;
            if (this.mGridContent.getContentIntent() != null) {
                this.mViewContainer.setTag(new Pair(this.mGridContent.getContentIntent(), new EventInfo(getMode(), 3, 1, this.mRowIndex)));
                makeEntireGridClickable(true);
            }
            CharSequence contentDescription = this.mGridContent.getContentDescription();
            if (contentDescription != null) {
                this.mViewContainer.setContentDescription(contentDescription);
            }
            ArrayList<GridContent.CellContent> gridContent2 = this.mGridContent.getGridContent();
            if (this.mGridContent.getLargestImageMode() == 2 || this.mGridContent.getLargestImageMode() == 4) {
                this.mViewContainer.setGravity(48);
            } else {
                this.mViewContainer.setGravity(16);
            }
            int i = this.mMaxCells;
            if (this.mGridContent.getSeeMoreItem() == null) {
                z = false;
            }
            this.mHiddenItemCount = 0;
            for (int i2 = 0; i2 < gridContent2.size(); i2++) {
                if (this.mViewContainer.getChildCount() >= i) {
                    int size = gridContent2.size() - i;
                    this.mHiddenItemCount = size;
                    if (!z) {
                        return;
                    }
                    addSeeMoreCount(size);
                    return;
                }
                addCell(gridContent2.get(i2), i2, Math.min(gridContent2.size(), i));
            }
        }
    }

    private void addSeeMoreCount(int numExtra) {
        ViewGroup viewGroup;
        TextView textView;
        LinearLayout linearLayout = this.mViewContainer;
        View childAt = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
        this.mViewContainer.removeView(childAt);
        SliceItem seeMoreItem = this.mGridContent.getSeeMoreItem();
        int childCount = this.mViewContainer.getChildCount();
        int i = this.mMaxCells;
        if (("slice".equals(seeMoreItem.getFormat()) || "action".equals(seeMoreItem.getFormat())) && seeMoreItem.getSlice().getItems().size() > 0) {
            addCell(new GridContent.CellContent(seeMoreItem), childCount, i);
            return;
        }
        LayoutInflater from = LayoutInflater.from(getContext());
        if (this.mGridContent.isAllImages()) {
            viewGroup = (FrameLayout) from.inflate(R$layout.abc_slice_grid_see_more_overlay, (ViewGroup) this.mViewContainer, false);
            viewGroup.addView(childAt, 0, new FrameLayout.LayoutParams(-1, -1));
            textView = (TextView) viewGroup.findViewById(R$id.text_see_more_count);
            viewGroup.findViewById(R$id.overlay_see_more).setBackground(new CornerDrawable(SliceViewUtil.getDrawable(getContext(), 16842800), this.mSliceStyle.getImageCornerRadius()));
        } else {
            viewGroup = (LinearLayout) from.inflate(R$layout.abc_slice_grid_see_more, (ViewGroup) this.mViewContainer, false);
            textView = (TextView) viewGroup.findViewById(R$id.text_see_more_count);
            TextView textView2 = (TextView) viewGroup.findViewById(R$id.text_see_more);
            SliceStyle sliceStyle = this.mSliceStyle;
            if (sliceStyle != null && this.mRowStyle != null) {
                textView2.setTextSize(0, sliceStyle.getGridTitleSize());
                textView2.setTextColor(this.mRowStyle.getTitleColor());
            }
        }
        this.mViewContainer.addView(viewGroup, new LinearLayout.LayoutParams(0, -1, 1.0f));
        textView.setText(getResources().getString(R$string.abc_slice_more_content, Integer.valueOf(numExtra)));
        EventInfo eventInfo = new EventInfo(getMode(), 4, 1, this.mRowIndex);
        eventInfo.setPosition(2, childCount, i);
        viewGroup.setTag(new Pair(seeMoreItem, eventInfo));
        makeClickable(viewGroup, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c8, code lost:
        if ("long".equals(r4) != false) goto L57;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void addCell(GridContent.CellContent cell, int index, int total) {
        ArrayList arrayList;
        SliceActionView sliceActionView;
        boolean z;
        SliceItem sliceItem;
        int i;
        int i2;
        int i3;
        SliceItem sliceItem2;
        ArrayList arrayList2;
        SliceItem sliceItem3;
        String str;
        int i4 = (getMode() != 1 || !this.mGridContent.hasImage()) ? 2 : 1;
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        ArrayList<SliceItem> cellItems = cell.getCellItems();
        SliceItem contentIntent = cell.getContentIntent();
        SliceItem picker = cell.getPicker();
        SliceItem toggleItem = cell.getToggleItem();
        boolean z2 = cellItems.size() == 1;
        String str2 = "text";
        if (z2 || getMode() != 1) {
            arrayList = null;
        } else {
            ArrayList arrayList3 = new ArrayList();
            Iterator<SliceItem> it = cellItems.iterator();
            while (it.hasNext()) {
                SliceItem next = it.next();
                if (str2.equals(next.getFormat())) {
                    arrayList3.add(next);
                }
            }
            Iterator it2 = arrayList3.iterator();
            while (arrayList3.size() > i4) {
                if (!((SliceItem) it2.next()).hasAnyHints("title", "large")) {
                    it2.remove();
                }
            }
            arrayList = arrayList3;
        }
        SliceItem sliceItem4 = null;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        boolean z3 = false;
        while (i7 < cellItems.size()) {
            SliceItem sliceItem5 = cellItems.get(i7);
            int i8 = i7;
            String format = sliceItem5.getFormat();
            SliceItem sliceItem6 = toggleItem;
            int determinePadding = determinePadding(sliceItem4);
            if (i6 < i4) {
                if (!str2.equals(format)) {
                    sliceItem = sliceItem4;
                } else {
                    sliceItem = sliceItem4;
                }
                if ((arrayList == null || arrayList.contains(sliceItem5)) && addTextItem(sliceItem5, linearLayout, determinePadding)) {
                    i6++;
                    str = str2;
                    sliceItem4 = sliceItem5;
                    i3 = i8;
                    sliceItem2 = sliceItem6;
                    z3 = true;
                    arrayList2 = arrayList;
                    i7 = i3 + 1;
                    arrayList = arrayList2;
                    str2 = str;
                    toggleItem = sliceItem2;
                }
                i = i5;
                i2 = i6;
                i3 = i8;
                sliceItem2 = sliceItem6;
                arrayList2 = arrayList;
                sliceItem3 = sliceItem;
                str = str2;
                sliceItem4 = sliceItem3;
                i5 = i;
                i6 = i2;
                i7 = i3 + 1;
                arrayList = arrayList2;
                str2 = str;
                toggleItem = sliceItem2;
            } else {
                sliceItem = sliceItem4;
            }
            if (i5 < 1 && "image".equals(sliceItem5.getFormat())) {
                i = i5;
                i2 = i6;
                SliceItem sliceItem7 = sliceItem;
                str = str2;
                i3 = i8;
                arrayList2 = arrayList;
                sliceItem3 = sliceItem7;
                sliceItem2 = sliceItem6;
                if (addImageItem(sliceItem5, cell.getOverlayItem(), this.mTintColor, linearLayout, z2)) {
                    i5 = i + 1;
                    sliceItem4 = sliceItem5;
                    i6 = i2;
                    z3 = true;
                    i7 = i3 + 1;
                    arrayList = arrayList2;
                    str2 = str;
                    toggleItem = sliceItem2;
                }
                sliceItem4 = sliceItem3;
                i5 = i;
                i6 = i2;
                i7 = i3 + 1;
                arrayList = arrayList2;
                str2 = str;
                toggleItem = sliceItem2;
            }
            i = i5;
            i2 = i6;
            i3 = i8;
            sliceItem2 = sliceItem6;
            arrayList2 = arrayList;
            sliceItem3 = sliceItem;
            str = str2;
            sliceItem4 = sliceItem3;
            i5 = i;
            i6 = i2;
            i7 = i3 + 1;
            arrayList = arrayList2;
            str2 = str;
            toggleItem = sliceItem2;
        }
        SliceItem sliceItem8 = sliceItem4;
        SliceItem sliceItem9 = toggleItem;
        if (picker != null) {
            if ("date_picker".equals(picker.getSubType())) {
                z3 = addPickerItem(picker, linearLayout, determinePadding(sliceItem8), true);
            } else if ("time_picker".equals(picker.getSubType())) {
                z3 = addPickerItem(picker, linearLayout, determinePadding(sliceItem8), false);
            }
        }
        if (sliceItem9 != null) {
            SliceActionView sliceActionView2 = new SliceActionView(getContext(), this.mSliceStyle, this.mRowStyle);
            linearLayout.addView(sliceActionView2);
            sliceActionView = sliceActionView2;
            z = true;
        } else {
            sliceActionView = null;
            z = z3;
        }
        if (z) {
            CharSequence contentDescription = cell.getContentDescription();
            if (contentDescription != null) {
                linearLayout.setContentDescription(contentDescription);
            }
            this.mViewContainer.addView(linearLayout, new LinearLayout.LayoutParams(0, -2, 1.0f));
            if (index != total - 1) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
                marginLayoutParams.setMarginEnd(this.mGutter);
                linearLayout.setLayoutParams(marginLayoutParams);
            }
            if (contentIntent != null) {
                EventInfo eventInfo = new EventInfo(getMode(), 1, 1, this.mRowIndex);
                eventInfo.setPosition(2, index, total);
                linearLayout.setTag(new Pair(contentIntent, eventInfo));
                makeClickable(linearLayout, true);
            }
            if (sliceItem9 == null) {
                return;
            }
            EventInfo eventInfo2 = new EventInfo(getMode(), 0, 3, this.mRowIndex);
            sliceActionView.setAction(new SliceActionImpl(sliceItem9), eventInfo2, this.mObserver, this.mTintColor, this.mLoadingListener);
            eventInfo2.setPosition(2, index, total);
        }
    }

    private boolean addTextItem(SliceItem item, ViewGroup container, int padding) {
        CharSequence sanitizedText;
        String format = item.getFormat();
        if ("text".equals(format) || "long".equals(format)) {
            boolean hasAnyHints = SliceQuery.hasAnyHints(item, "large", "title");
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(hasAnyHints ? getTitleTextLayout() : TEXT_LAYOUT, (ViewGroup) null);
            SliceStyle sliceStyle = this.mSliceStyle;
            if (sliceStyle != null && this.mRowStyle != null) {
                textView.setTextSize(0, hasAnyHints ? sliceStyle.getGridTitleSize() : sliceStyle.getGridSubtitleSize());
                textView.setTextColor(hasAnyHints ? this.mRowStyle.getTitleColor() : this.mRowStyle.getSubtitleColor());
            }
            if ("long".equals(format)) {
                sanitizedText = SliceViewUtil.getTimestampString(getContext(), item.getLong());
            } else {
                sanitizedText = item.getSanitizedText();
            }
            textView.setText(sanitizedText);
            container.addView(textView);
            textView.setPadding(0, padding, 0, 0);
            return true;
        }
        return false;
    }

    protected boolean addImageItem(SliceItem item, SliceItem overlayItem, int color, ViewGroup container, boolean isSingle) {
        Drawable loadDrawable;
        ViewGroup.LayoutParams layoutParams;
        String format = item.getFormat();
        SliceStyle sliceStyle = this.mSliceStyle;
        boolean z = sliceStyle != null && sliceStyle.getApplyCornerRadiusToLargeImages();
        if (!"image".equals(format) || item.getIcon() == null || (loadDrawable = item.getIcon().loadDrawable(getContext())) == null) {
            return false;
        }
        ImageView imageView = new ImageView(getContext());
        if (z) {
            imageView.setImageDrawable(new CornerDrawable(loadDrawable, this.mSliceStyle.getImageCornerRadius()));
        } else {
            imageView.setImageDrawable(loadDrawable);
        }
        if (item.hasHint("raw")) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layoutParams = new LinearLayout.LayoutParams(this.mGridContent.getFirstImageSize(getContext()).x, this.mGridContent.getFirstImageSize(getContext()).y);
        } else if (item.hasHint("large")) {
            imageView.setScaleType(z ? ImageView.ScaleType.FIT_XY : ImageView.ScaleType.CENTER_CROP);
            layoutParams = new LinearLayout.LayoutParams(-1, isSingle ? -1 : this.mLargeImageHeight);
        } else {
            boolean z2 = !item.hasHint("no_tint");
            int i = !z2 ? this.mSmallImageSize : this.mIconSize;
            imageView.setScaleType(z2 ? ImageView.ScaleType.CENTER_INSIDE : ImageView.ScaleType.CENTER_CROP);
            layoutParams = new LinearLayout.LayoutParams(i, i);
        }
        if (color != -1 && !item.hasHint("no_tint")) {
            imageView.setColorFilter(color);
        }
        if (overlayItem == null || this.mViewContainer.getChildCount() == this.mMaxCells - 1) {
            container.addView(imageView, layoutParams);
            return true;
        }
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_grid_text_overlay_image, container, false);
        frameLayout.addView(imageView, 0, new FrameLayout.LayoutParams(-2, -2));
        ((TextView) frameLayout.findViewById(R$id.text_overlay)).setText(overlayItem.getText());
        frameLayout.findViewById(R$id.tint_overlay).setBackground(new CornerDrawable(ContextCompat.getDrawable(getContext(), R$drawable.abc_slice_gradient), this.mSliceStyle.getImageCornerRadius()));
        container.addView(frameLayout, layoutParams);
        return true;
    }

    private boolean addPickerItem(final SliceItem pickerItem, ViewGroup container, int padding, final boolean isDatePicker) {
        SliceItem findSubtype = SliceQuery.findSubtype(pickerItem, "long", "millis");
        if (findSubtype == null) {
            return false;
        }
        long j = findSubtype.getLong();
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(getTitleTextLayout(), (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            textView.setTextSize(0, sliceStyle.getGridTitleSize());
            textView.setTextColor(this.mSliceStyle.getTitleColor());
        }
        final Date date = new Date(j);
        SliceItem find = SliceQuery.find(pickerItem, "text", "title", (String) null);
        if (find != null) {
            textView.setText(find.getText());
        }
        final int i = this.mRowIndex;
        container.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.GridRowView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (isDatePicker) {
                    new DatePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new DateSetListener(pickerItem, i), calendar.get(1), calendar.get(2), calendar.get(5)).show();
                } else {
                    new TimePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new TimeSetListener(pickerItem, i), calendar.get(11), calendar.get(12), false).show();
                }
            }
        });
        container.setClickable(true);
        int i2 = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i2 = 16843868;
        }
        container.setBackground(SliceViewUtil.getDrawable(getContext(), i2));
        container.addView(textView);
        textView.setPadding(0, padding, 0, 0);
        return true;
    }

    /* loaded from: classes.dex */
    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        DateSetListener(SliceItem datePickerItem, int mRowIndex) {
            this.mActionItem = datePickerItem;
            this.mRowIndex = mRowIndex;
        }

        @Override // android.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date time = calendar.getTime();
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver == null) {
                        return;
                    }
                    GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        TimeSetListener(SliceItem timePickerItem, int mRowIndex) {
            this.mActionItem = timePickerItem;
            this.mRowIndex = mRowIndex;
        }

        @Override // android.app.TimePickerDialog.OnTimeSetListener
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Date time = Calendar.getInstance().getTime();
            time.setHours(hour);
            time.setMinutes(minute);
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver == null) {
                        return;
                    }
                    GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    private int determinePadding(SliceItem prevItem) {
        SliceStyle sliceStyle;
        if (prevItem == null) {
            return 0;
        }
        if ("image".equals(prevItem.getFormat())) {
            return this.mTextPadding;
        }
        if ((!"text".equals(prevItem.getFormat()) && !"long".equals(prevItem.getFormat())) || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.getVerticalGridTextPadding();
    }

    private void makeEntireGridClickable(boolean isClickable) {
        Drawable drawable = null;
        this.mViewContainer.setOnTouchListener(isClickable ? this : null);
        this.mViewContainer.setOnClickListener(isClickable ? this : null);
        View view = this.mForeground;
        if (isClickable) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        this.mViewContainer.setClickable(isClickable);
    }

    private void makeClickable(View layout, boolean isClickable) {
        Drawable drawable = null;
        layout.setOnClickListener(isClickable ? this : null);
        int i = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i = 16843868;
        }
        if (isClickable) {
            drawable = SliceViewUtil.getDrawable(getContext(), i);
        }
        layout.setBackground(drawable);
        layout.setClickable(isClickable);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        SliceItem find;
        Pair pair = (Pair) view.getTag();
        SliceItem sliceItem = (SliceItem) pair.first;
        EventInfo eventInfo = (EventInfo) pair.second;
        if (sliceItem == null || (find = SliceQuery.find(sliceItem, "action", (String) null, (String) null)) == null) {
            return;
        }
        try {
            find.fireAction(null, null);
            SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
            if (onSliceActionListener == null) {
                return;
            }
            onSliceActionListener.onSliceAction(eventInfo, find);
        } catch (PendingIntent.CanceledException e) {
            Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent event) {
        onForegroundActivated(event);
        return false;
    }

    private void onForegroundActivated(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mForeground.getLocationOnScreen(this.mLoc);
            this.mForeground.getBackground().setHotspot((int) (event.getRawX() - this.mLoc[0]), (int) (event.getRawY() - this.mLoc[1]));
        }
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            this.mForeground.setPressed(true);
        } else if (actionMasked != 3 && actionMasked != 1 && actionMasked != 2) {
        } else {
            this.mForeground.setPressed(false);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void resetView() {
        if (this.mMaxCellUpdateScheduled) {
            this.mMaxCellUpdateScheduled = false;
            getViewTreeObserver().removeOnPreDrawListener(this.mMaxCellsUpdater);
        }
        this.mViewContainer.removeAllViews();
        setLayoutDirection(2);
        makeEntireGridClickable(false);
    }
}
