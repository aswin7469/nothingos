package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.ViewCompat;
import androidx.slice.CornerDrawable;
import androidx.slice.SliceItem;
import androidx.slice.SliceStructure;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$id;
import androidx.slice.view.R$layout;
import androidx.slice.view.R$plurals;
import androidx.slice.view.R$string;
import androidx.slice.view.R$style;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class RowView extends SliceChildView implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final boolean sCanSpecifyLargerRangeBarHeight;
    private final ProgressBar mActionSpinner;
    private boolean mAllowTwoLines;
    private final LinearLayout mContent;
    Handler mHandler;
    private List<SliceAction> mHeaderActions;
    private boolean mIsHeader;
    boolean mIsRangeSliding;
    private boolean mIsStarRating;
    long mLastSentRangeUpdate;
    private int mMeasuredRangeHeight;
    private View mRangeBar;
    boolean mRangeHasPendingUpdate;
    private SliceItem mRangeItem;
    int mRangeMaxValue;
    int mRangeMinValue;
    boolean mRangeUpdaterRunning;
    int mRangeValue;
    private final LinearLayout mRootView;
    private SliceActionImpl mRowAction;
    RowContent mRowContent;
    int mRowIndex;
    private View mSeeMoreView;
    private SliceItem mSelectionItem;
    private ArrayList<String> mSelectionOptionKeys;
    private ArrayList<CharSequence> mSelectionOptionValues;
    private Spinner mSelectionSpinner;
    boolean mShowActionSpinner;
    private SliceItem mStartItem;
    private final ArrayMap<SliceActionImpl, SliceActionView> mToggles = new ArrayMap<>();
    private final ArrayMap<SliceActionImpl, SliceActionView> mActions = new ArrayMap<>();
    protected Set<SliceItem> mLoadingActions = new HashSet();
    Runnable mRangeUpdater = new Runnable() { // from class: androidx.slice.widget.RowView.2
        @Override // java.lang.Runnable
        public void run() {
            RowView.this.sendSliderValue();
            RowView.this.mRangeUpdaterRunning = false;
        }
    };
    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: androidx.slice.widget.RowView.3
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = progress + rowView.mRangeMinValue;
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (rowView2.mRangeUpdaterRunning) {
            } else {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200L);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            RowView.this.mIsRangeSliding = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            RowView rowView = RowView.this;
            rowView.mIsRangeSliding = false;
            if (rowView.mRangeUpdaterRunning || rowView.mRangeHasPendingUpdate) {
                rowView.mRangeUpdaterRunning = false;
                rowView.mRangeHasPendingUpdate = false;
                rowView.mHandler.removeCallbacks(rowView.mRangeUpdater);
                RowView rowView2 = RowView.this;
                int progress = seekBar.getProgress();
                RowView rowView3 = RowView.this;
                rowView2.mRangeValue = progress + rowView3.mRangeMinValue;
                rowView3.sendSliderValue();
            }
        }
    };
    private final RatingBar.OnRatingBarChangeListener mRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() { // from class: androidx.slice.widget.RowView.4
        @Override // android.widget.RatingBar.OnRatingBarChangeListener
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = Math.round(rating + rowView.mRangeMinValue);
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (rowView2.mRangeUpdaterRunning) {
            } else {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200L);
            }
        }
    };
    private int mIconSize = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_icon_size);
    private int mImageSize = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
    private final LinearLayout mStartContainer = (LinearLayout) findViewById(R$id.icon_frame);
    private final LinearLayout mSubContent = (LinearLayout) findViewById(R$id.subcontent);
    private final TextView mPrimaryText = (TextView) findViewById(16908310);
    private final TextView mSecondaryText = (TextView) findViewById(16908304);
    private final TextView mLastUpdatedText = (TextView) findViewById(R$id.last_updated);
    private final View mBottomDivider = findViewById(R$id.bottom_divider);
    private final View mActionDivider = findViewById(R$id.action_divider);
    private final LinearLayout mEndContainer = (LinearLayout) findViewById(16908312);

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> parent) {
    }

    static {
        sCanSpecifyLargerRangeBarHeight = Build.VERSION.SDK_INT >= 23;
    }

    public RowView(Context context) {
        super(context);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R$layout.abc_slice_small_template, (ViewGroup) this, false);
        this.mRootView = linearLayout;
        addView(linearLayout);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(16908290);
        this.mContent = linearLayout2;
        ProgressBar progressBar = (ProgressBar) findViewById(R$id.action_sent_indicator);
        this.mActionSpinner = progressBar;
        SliceViewUtil.tintIndeterminateProgressBar(getContext(), progressBar);
        ViewCompat.setImportantForAccessibility(this, 2);
        ViewCompat.setImportantForAccessibility(linearLayout2, 2);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setStyle(SliceStyle styles, RowStyle rowStyle) {
        super.setStyle(styles, rowStyle);
        applyRowStyle();
    }

    private void applyRowStyle() {
        RowStyle rowStyle;
        if (this.mSliceStyle == null || (rowStyle = this.mRowStyle) == null) {
            return;
        }
        setViewSidePaddings(this.mStartContainer, rowStyle.getTitleItemStartPadding(), this.mRowStyle.getTitleItemEndPadding());
        setViewSidePaddings(this.mContent, this.mRowStyle.getContentStartPadding(), this.mRowStyle.getContentEndPadding());
        setViewSidePaddings(this.mPrimaryText, this.mRowStyle.getTitleStartPadding(), this.mRowStyle.getTitleEndPadding());
        setViewSidePaddings(this.mSubContent, this.mRowStyle.getSubContentStartPadding(), this.mRowStyle.getSubContentEndPadding());
        setViewSidePaddings(this.mEndContainer, this.mRowStyle.getEndItemStartPadding(), this.mRowStyle.getEndItemEndPadding());
        setViewSideMargins(this.mBottomDivider, this.mRowStyle.getBottomDividerStartPadding(), this.mRowStyle.getBottomDividerEndPadding());
        setViewHeight(this.mActionDivider, this.mRowStyle.getActionDividerHeight());
        if (this.mRowStyle.getTintColor() == -1) {
            return;
        }
        setTint(this.mRowStyle.getTintColor());
    }

    private void setViewSidePaddings(View v, int start, int end) {
        boolean z = start < 0 && end < 0;
        if (v == null || z) {
            return;
        }
        if (start < 0) {
            start = v.getPaddingStart();
        }
        int paddingTop = v.getPaddingTop();
        if (end < 0) {
            end = v.getPaddingEnd();
        }
        v.setPaddingRelative(start, paddingTop, end, v.getPaddingBottom());
    }

    private void setViewSideMargins(View v, int start, int end) {
        boolean z = start < 0 && end < 0;
        if (v == null || z) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (start >= 0) {
            marginLayoutParams.setMarginStart(start);
        }
        if (end >= 0) {
            marginLayoutParams.setMarginEnd(end);
        }
        v.setLayoutParams(marginLayoutParams);
    }

    private void setViewHeight(View v, int height) {
        if (v == null || height < 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = height;
        v.setLayoutParams(layoutParams);
    }

    private void setViewWidth(View v, int width) {
        if (v == null || width < 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = width;
        v.setLayoutParams(layoutParams);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setInsets(int l, int t, int r, int b) {
        super.setInsets(l, t, r, b);
        setPadding(l, t, r, b);
    }

    private int getRowContentHeight() {
        int height = this.mRowContent.getHeight(this.mSliceStyle, this.mViewPolicy);
        if (this.mRangeBar != null && this.mStartItem == null) {
            height -= this.mSliceStyle.getRowRangeHeight();
        }
        return this.mSelectionSpinner != null ? height - this.mSliceStyle.getRowSelectionHeight() : height;
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setTint(int tintColor) {
        super.setTint(tintColor);
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceActions(List<SliceAction> actions) {
        this.mHeaderActions = actions;
        if (this.mRowContent != null) {
            updateEndItems();
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setShowLastUpdated(boolean showLastUpdated) {
        super.setShowLastUpdated(showLastUpdated);
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setAllowTwoLines(boolean allowTwoLines) {
        this.mAllowTwoLines = allowTwoLines;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    private void measureChildWithExactHeight(View child, int widthMeasureSpec, int childHeight) {
        measureChild(child, widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(childHeight + this.mInsetTop + this.mInsetBottom, 1073741824));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int rowContentHeight = getRowContentHeight();
        if (rowContentHeight != 0) {
            this.mRootView.setVisibility(0);
            measureChildWithExactHeight(this.mRootView, widthMeasureSpec, rowContentHeight);
            i = this.mRootView.getMeasuredWidth();
        } else {
            this.mRootView.setVisibility(8);
            i = 0;
        }
        View view = this.mRangeBar;
        if (view != null && this.mStartItem == null) {
            if (sCanSpecifyLargerRangeBarHeight) {
                measureChildWithExactHeight(view, widthMeasureSpec, this.mSliceStyle.getRowRangeHeight());
            } else {
                measureChild(view, widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, 0));
            }
            this.mMeasuredRangeHeight = this.mRangeBar.getMeasuredHeight();
            i = Math.max(i, this.mRangeBar.getMeasuredWidth());
        } else {
            Spinner spinner = this.mSelectionSpinner;
            if (spinner != null) {
                measureChildWithExactHeight(spinner, widthMeasureSpec, this.mSliceStyle.getRowSelectionHeight());
                i = Math.max(i, this.mSelectionSpinner.getMeasuredWidth());
            }
        }
        int max = Math.max(i + this.mInsetStart + this.mInsetEnd, getSuggestedMinimumWidth());
        RowContent rowContent = this.mRowContent;
        setMeasuredDimension(FrameLayout.resolveSizeAndState(max, widthMeasureSpec, 0), (rowContent != null ? rowContent.getHeight(this.mSliceStyle, this.mViewPolicy) : 0) + this.mInsetTop + this.mInsetBottom);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        LinearLayout linearLayout = this.mRootView;
        linearLayout.layout(paddingLeft, this.mInsetTop, linearLayout.getMeasuredWidth() + paddingLeft, getRowContentHeight() + this.mInsetTop);
        if (this.mRangeBar != null && this.mStartItem == null) {
            int rowContentHeight = getRowContentHeight() + ((this.mSliceStyle.getRowRangeHeight() - this.mMeasuredRangeHeight) / 2) + this.mInsetTop;
            View view = this.mRangeBar;
            view.layout(paddingLeft, rowContentHeight, view.getMeasuredWidth() + paddingLeft, this.mMeasuredRangeHeight + rowContentHeight);
        } else if (this.mSelectionSpinner == null) {
        } else {
            int rowContentHeight2 = getRowContentHeight() + this.mInsetTop;
            Spinner spinner = this.mSelectionSpinner;
            spinner.layout(paddingLeft, rowContentHeight2, spinner.getMeasuredWidth() + paddingLeft, this.mSelectionSpinner.getMeasuredHeight() + rowContentHeight2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0053, code lost:
        if (r2 != false) goto L23;
     */
    @Override // androidx.slice.widget.SliceChildView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSliceItem(SliceContent content, boolean isHeader, int index, int rowCount, SliceView.OnSliceActionListener observer) {
        boolean z;
        RowContent rowContent;
        setSliceActionListener(observer);
        if (content != null && (rowContent = this.mRowContent) != null && rowContent.isValid()) {
            RowContent rowContent2 = this.mRowContent;
            SliceStructure sliceStructure = rowContent2 != null ? new SliceStructure(rowContent2.getSliceItem()) : null;
            SliceStructure sliceStructure2 = new SliceStructure(content.getSliceItem().getSlice());
            z = true;
            boolean z2 = sliceStructure != null && sliceStructure.equals(sliceStructure2);
            if ((sliceStructure == null || sliceStructure.getUri() == null || !sliceStructure.getUri().equals(sliceStructure2.getUri())) ? false : true) {
            }
        }
        z = false;
        this.mShowActionSpinner = false;
        this.mIsHeader = isHeader;
        this.mRowContent = (RowContent) content;
        this.mRowIndex = index;
        populateViews(z);
    }

    private void populateViews(boolean isUpdate) {
        int titleSize;
        boolean z = true;
        boolean z2 = isUpdate && this.mIsRangeSliding;
        if (!z2) {
            resetViewState();
        }
        char c = 65535;
        if (this.mRowContent.getLayoutDir() != -1) {
            setLayoutDirection(this.mRowContent.getLayoutDir());
        }
        if (this.mRowContent.isDefaultSeeMore()) {
            showSeeMore();
            return;
        }
        CharSequence contentDescription = this.mRowContent.getContentDescription();
        if (contentDescription != null) {
            this.mContent.setContentDescription(contentDescription);
        }
        SliceItem startItem = this.mRowContent.getStartItem();
        this.mStartItem = startItem;
        boolean z3 = startItem != null && (!this.mRowContent.getIsHeader() || this.mRowContent.hasTitleItems());
        if (z3) {
            z3 = addItem(this.mStartItem, this.mTintColor, true);
        }
        int i = 8;
        this.mStartContainer.setVisibility(z3 ? 0 : 8);
        SliceItem titleItem = this.mRowContent.getTitleItem();
        if (titleItem != null) {
            this.mPrimaryText.setText(titleItem.getSanitizedText());
        }
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            TextView textView = this.mPrimaryText;
            if (this.mIsHeader) {
                titleSize = sliceStyle.getHeaderTitleSize();
            } else {
                titleSize = sliceStyle.getTitleSize();
            }
            textView.setTextSize(0, titleSize);
            this.mPrimaryText.setTextColor(this.mRowStyle.getTitleColor());
        }
        this.mPrimaryText.setVisibility(titleItem != null ? 0 : 8);
        addSubtitle(titleItem != null);
        View view = this.mBottomDivider;
        if (this.mRowContent.hasBottomDivider()) {
            i = 0;
        }
        view.setVisibility(i);
        SliceItem primaryAction = this.mRowContent.getPrimaryAction();
        if (primaryAction != null && primaryAction != this.mStartItem) {
            SliceActionImpl sliceActionImpl = new SliceActionImpl(primaryAction);
            this.mRowAction = sliceActionImpl;
            if (sliceActionImpl.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                subtype.hashCode();
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        addAction(this.mRowAction, this.mTintColor, this.mEndContainer, false);
                        setViewClickable(this.mRootView, true);
                        return;
                    case 1:
                        setViewClickable(this.mRootView, true);
                        return;
                    case 2:
                        setViewClickable(this.mRootView, true);
                        return;
                }
            }
        }
        SliceItem range = this.mRowContent.getRange();
        if (range != null) {
            if (this.mRowAction != null) {
                setViewClickable(this.mRootView, true);
            }
            this.mRangeItem = range;
            SliceItem findSubtype = SliceQuery.findSubtype(range, "int", "range_mode");
            if (findSubtype != null) {
                if (findSubtype.getInt() != 2) {
                    z = false;
                }
                this.mIsStarRating = z;
            }
            if (!z2) {
                initRangeBar();
                addRangeView();
            }
            if (this.mStartItem == null) {
                return;
            }
        }
        SliceItem selection = this.mRowContent.getSelection();
        if (selection != null) {
            this.mSelectionItem = selection;
            addSelection(selection);
            return;
        }
        updateEndItems();
        updateActionSpinner();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:86:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateEndItems() {
        boolean z;
        SliceActionImpl sliceActionImpl;
        LinearLayout linearLayout;
        SliceItem sliceItem;
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            if (rowContent.getRange() != null && this.mStartItem == null) {
                return;
            }
            this.mEndContainer.removeAllViews();
            List endItems = this.mRowContent.getEndItems();
            List list = this.mHeaderActions;
            if (list != null) {
                endItems = list;
            }
            if (this.mRowContent.getIsHeader() && this.mStartItem != null && endItems.isEmpty() && !this.mRowContent.hasTitleItems()) {
                endItems.add(this.mStartItem);
            }
            SliceItem sliceItem2 = null;
            int i = 0;
            int i2 = 0;
            boolean z2 = false;
            boolean z3 = false;
            for (int i3 = 0; i3 < endItems.size(); i3++) {
                if (endItems.get(i3) instanceof SliceItem) {
                    sliceItem = endItems.get(i3);
                } else {
                    sliceItem = ((SliceActionImpl) endItems.get(i3)).getSliceItem();
                }
                if (i2 < 3 && addItem(sliceItem, this.mTintColor, false)) {
                    if (sliceItem2 == null && SliceQuery.find(sliceItem, "action") != null) {
                        sliceItem2 = sliceItem;
                    }
                    i2++;
                    if (i2 == 1) {
                        z2 = !this.mToggles.isEmpty() && SliceQuery.find(sliceItem.getSlice(), "image") == null;
                        z3 = endItems.size() == 1 && SliceQuery.find(sliceItem, "action") != null;
                    }
                }
            }
            int i4 = 8;
            this.mEndContainer.setVisibility(i2 > 0 ? 0 : 8);
            View view = this.mActionDivider;
            if (this.mRowAction != null && (z2 || (this.mRowContent.hasActionDivider() && z3))) {
                i4 = 0;
            }
            view.setVisibility(i4);
            SliceItem sliceItem3 = this.mStartItem;
            boolean z4 = (sliceItem3 == null || SliceQuery.find(sliceItem3, "action") == null) ? false : true;
            boolean z5 = sliceItem2 != null;
            if (this.mRowAction != null) {
                setViewClickable(this.mRootView, true);
            } else if (z5 != z4 && (i2 == 1 || z4)) {
                if (!this.mToggles.isEmpty()) {
                    this.mRowAction = this.mToggles.keySet().iterator().next();
                } else if (!this.mActions.isEmpty() && this.mActions.size() == 1) {
                    this.mRowAction = this.mActions.valueAt(0).getAction();
                }
                setViewClickable(this.mRootView, true);
                z = true;
                sliceActionImpl = this.mRowAction;
                if (sliceActionImpl != null && !z && this.mLoadingActions.contains(sliceActionImpl.getSliceItem())) {
                    this.mShowActionSpinner = true;
                }
                linearLayout = this.mRootView;
                if (!linearLayout.isClickable()) {
                    i = 2;
                }
                ViewCompat.setImportantForAccessibility(linearLayout, i);
            }
            z = false;
            sliceActionImpl = this.mRowAction;
            if (sliceActionImpl != null) {
                this.mShowActionSpinner = true;
            }
            linearLayout = this.mRootView;
            if (!linearLayout.isClickable()) {
            }
            ViewCompat.setImportantForAccessibility(linearLayout, i);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setLastUpdated(long lastUpdated) {
        super.setLastUpdated(lastUpdated);
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            addSubtitle(rowContent.getTitleItem() != null && TextUtils.isEmpty(this.mRowContent.getTitleItem().getSanitizedText()));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void addSubtitle(boolean hasTitle) {
        SliceItem subtitleItem;
        String str;
        boolean z;
        int i;
        SliceStyle sliceStyle;
        int subtitleSize;
        int verticalTextPadding;
        CharSequence relativeTimeString;
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            if (rowContent.getRange() != null && this.mStartItem != null) {
                return;
            }
            boolean z2 = true;
            if (getMode() == 1) {
                subtitleItem = this.mRowContent.getSummaryItem();
            } else {
                subtitleItem = this.mRowContent.getSubtitleItem();
            }
            CharSequence charSequence = null;
            if (this.mShowLastUpdated) {
                long j = this.mLastUpdated;
                if (j != -1 && (relativeTimeString = getRelativeTimeString(j)) != null) {
                    str = getResources().getString(R$string.abc_slice_updated, relativeTimeString);
                    if (subtitleItem != null) {
                        charSequence = subtitleItem.getSanitizedText();
                    }
                    z = TextUtils.isEmpty(charSequence) || (subtitleItem != null && subtitleItem.hasHint("partial"));
                    if (z) {
                        this.mSecondaryText.setText(charSequence);
                        SliceStyle sliceStyle2 = this.mSliceStyle;
                        if (sliceStyle2 != null) {
                            TextView textView = this.mSecondaryText;
                            if (this.mIsHeader) {
                                subtitleSize = sliceStyle2.getHeaderSubtitleSize();
                            } else {
                                subtitleSize = sliceStyle2.getSubtitleSize();
                            }
                            textView.setTextSize(0, subtitleSize);
                            this.mSecondaryText.setTextColor(this.mRowStyle.getSubtitleColor());
                            if (this.mIsHeader) {
                                verticalTextPadding = this.mSliceStyle.getVerticalHeaderTextPadding();
                            } else {
                                verticalTextPadding = this.mSliceStyle.getVerticalTextPadding();
                            }
                            this.mSecondaryText.setPadding(0, verticalTextPadding, 0, 0);
                        }
                    }
                    i = 2;
                    if (str != null) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            str = " · " + ((Object) str);
                        }
                        SpannableString spannableString = new SpannableString(str);
                        spannableString.setSpan(new StyleSpan(2), 0, str.length(), 0);
                        this.mLastUpdatedText.setText(spannableString);
                        if (this.mSliceStyle != null) {
                            this.mLastUpdatedText.setTextSize(0, this.mIsHeader ? sliceStyle.getHeaderSubtitleSize() : sliceStyle.getSubtitleSize());
                            this.mLastUpdatedText.setTextColor(this.mRowStyle.getSubtitleColor());
                        }
                    }
                    int i2 = 8;
                    this.mLastUpdatedText.setVisibility(!TextUtils.isEmpty(str) ? 8 : 0);
                    TextView textView2 = this.mSecondaryText;
                    if (z) {
                        i2 = 0;
                    }
                    textView2.setVisibility(i2);
                    if ((this.mRowContent.getIsHeader() || this.mAllowTwoLines) || hasTitle || !z || !TextUtils.isEmpty(str)) {
                        i = 1;
                    }
                    TextView textView3 = this.mSecondaryText;
                    if (i != 1) {
                        z2 = false;
                    }
                    textView3.setSingleLine(z2);
                    this.mSecondaryText.setMaxLines(i);
                    this.mSecondaryText.requestLayout();
                    this.mLastUpdatedText.requestLayout();
                }
            }
            str = null;
            if (subtitleItem != null) {
            }
            if (TextUtils.isEmpty(charSequence)) {
            }
            if (z) {
            }
            i = 2;
            if (str != null) {
            }
            int i22 = 8;
            this.mLastUpdatedText.setVisibility(!TextUtils.isEmpty(str) ? 8 : 0);
            TextView textView22 = this.mSecondaryText;
            if (z) {
            }
            textView22.setVisibility(i22);
            if (this.mRowContent.getIsHeader() || this.mAllowTwoLines) {
            }
            i = 1;
            TextView textView32 = this.mSecondaryText;
            if (i != 1) {
            }
            textView32.setSingleLine(z2);
            this.mSecondaryText.setMaxLines(i);
            this.mSecondaryText.requestLayout();
            this.mLastUpdatedText.requestLayout();
        }
    }

    private CharSequence getRelativeTimeString(long time) {
        long currentTimeMillis = System.currentTimeMillis() - time;
        if (currentTimeMillis > 31449600000L) {
            int i = (int) (currentTimeMillis / 31449600000L);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_years, i, Integer.valueOf(i));
        } else if (currentTimeMillis > 86400000) {
            int i2 = (int) (currentTimeMillis / 86400000);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_days, i2, Integer.valueOf(i2));
        } else if (currentTimeMillis <= 60000) {
            return null;
        } else {
            int i3 = (int) (currentTimeMillis / 60000);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_min, i3, Integer.valueOf(i3));
        }
    }

    private void initRangeBar() {
        SliceItem findSubtype = SliceQuery.findSubtype(this.mRangeItem, "int", "min");
        int i = 0;
        int i2 = findSubtype != null ? findSubtype.getInt() : 0;
        this.mRangeMinValue = i2;
        SliceItem findSubtype2 = SliceQuery.findSubtype(this.mRangeItem, "int", "max");
        int i3 = this.mIsStarRating ? 5 : 100;
        if (findSubtype2 != null) {
            i3 = findSubtype2.getInt();
        }
        this.mRangeMaxValue = i3;
        SliceItem findSubtype3 = SliceQuery.findSubtype(this.mRangeItem, "int", "value");
        if (findSubtype3 != null) {
            i = findSubtype3.getInt() - i2;
        }
        this.mRangeValue = i;
    }

    private void addRangeView() {
        ProgressBar progressBar;
        Drawable wrap;
        Drawable loadDrawable;
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        if (this.mIsStarRating) {
            addRatingBarView();
            return;
        }
        SliceItem findSubtype = SliceQuery.findSubtype(this.mRangeItem, "int", "range_mode");
        boolean z = findSubtype != null && findSubtype.getInt() == 1;
        boolean equals = "action".equals(this.mRangeItem.getFormat());
        boolean z2 = this.mStartItem == null;
        if (!equals) {
            if (z2) {
                progressBar = new ProgressBar(getContext(), null, 16842872);
            } else {
                progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_progress_inline_view, (ViewGroup) this, false);
                RowStyle rowStyle = this.mRowStyle;
                if (rowStyle != null) {
                    setViewWidth(progressBar, rowStyle.getProgressBarInlineWidth());
                    setViewSidePaddings(progressBar, this.mRowStyle.getProgressBarStartPadding(), this.mRowStyle.getProgressBarEndPadding());
                }
            }
            if (z) {
                progressBar.setIndeterminate(true);
            }
        } else if (z2) {
            progressBar = new SeekBar(getContext());
        } else {
            progressBar = (SeekBar) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_seekbar_view, (ViewGroup) this, false);
            RowStyle rowStyle2 = this.mRowStyle;
            if (rowStyle2 != null) {
                setViewWidth(progressBar, rowStyle2.getSeekBarInlineWidth());
            }
        }
        if (z) {
            wrap = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
        } else {
            wrap = DrawableCompat.wrap(progressBar.getProgressDrawable());
        }
        int i = this.mTintColor;
        if (i != -1 && wrap != null) {
            DrawableCompat.setTint(wrap, i);
            if (z) {
                progressBar.setIndeterminateDrawable(wrap);
            } else {
                progressBar.setProgressDrawable(wrap);
            }
        }
        progressBar.setMax(this.mRangeMaxValue - this.mRangeMinValue);
        progressBar.setProgress(this.mRangeValue);
        progressBar.setVisibility(0);
        if (this.mStartItem == null) {
            addView(progressBar, new FrameLayout.LayoutParams(-1, -2));
        } else {
            this.mSubContent.setVisibility(8);
            this.mContent.addView(progressBar, 1);
        }
        this.mRangeBar = progressBar;
        if (!equals) {
            return;
        }
        SliceItem inputRangeThumb = this.mRowContent.getInputRangeThumb();
        SeekBar seekBar = (SeekBar) this.mRangeBar;
        if (inputRangeThumb != null && inputRangeThumb.getIcon() != null && (loadDrawable = inputRangeThumb.getIcon().loadDrawable(getContext())) != null) {
            seekBar.setThumb(loadDrawable);
        }
        Drawable wrap2 = DrawableCompat.wrap(seekBar.getThumb());
        int i2 = this.mTintColor;
        if (i2 != -1 && wrap2 != null) {
            DrawableCompat.setTint(wrap2, i2);
            seekBar.setThumb(wrap2);
        }
        seekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
    }

    private void addRatingBarView() {
        RatingBar ratingBar = new RatingBar(getContext());
        ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(this.mTintColor, PorterDuff.Mode.SRC_IN);
        ratingBar.setStepSize(1.0f);
        ratingBar.setNumStars(this.mRangeMaxValue);
        ratingBar.setRating(this.mRangeValue);
        ratingBar.setVisibility(0);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(17);
        linearLayout.setVisibility(0);
        linearLayout.addView(ratingBar, new FrameLayout.LayoutParams(-2, -2));
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
        ratingBar.setOnRatingBarChangeListener(this.mRatingBarChangeListener);
        this.mRangeBar = linearLayout;
    }

    void sendSliderValue() {
        if (this.mRangeItem == null) {
            return;
        }
        try {
            this.mLastSentRangeUpdate = System.currentTimeMillis();
            this.mRangeItem.fireAction(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", this.mRangeValue));
            if (this.mObserver == null) {
                return;
            }
            EventInfo eventInfo = new EventInfo(getMode(), 2, 4, this.mRowIndex);
            eventInfo.state = this.mRangeValue;
            this.mObserver.onSliceAction(eventInfo, this.mRangeItem);
        } catch (PendingIntent.CanceledException e) {
            Log.e("RowView", "PendingIntent for slice cannot be sent", e);
        }
    }

    private void addSelection(final SliceItem selection) {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        this.mSelectionOptionKeys = new ArrayList<>();
        this.mSelectionOptionValues = new ArrayList<>();
        List<SliceItem> items = selection.getSlice().getItems();
        for (int i = 0; i < items.size(); i++) {
            SliceItem sliceItem = items.get(i);
            if (sliceItem.hasHint("selection_option")) {
                SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "text", "selection_option_key");
                SliceItem findSubtype2 = SliceQuery.findSubtype(sliceItem, "text", "selection_option_value");
                if (findSubtype != null && findSubtype2 != null) {
                    this.mSelectionOptionKeys.add(findSubtype.getText().toString());
                    this.mSelectionOptionValues.add(findSubtype2.getSanitizedText());
                }
            }
        }
        this.mSelectionSpinner = (Spinner) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_row_selection, (ViewGroup) this, false);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R$layout.abc_slice_row_selection_text, this.mSelectionOptionValues);
        arrayAdapter.setDropDownViewResource(R$layout.abc_slice_row_selection_dropdown_text);
        this.mSelectionSpinner.setAdapter((SpinnerAdapter) arrayAdapter);
        addView(this.mSelectionSpinner);
        this.mSelectionSpinner.setOnItemSelectedListener(this);
    }

    private void addAction(final SliceActionImpl actionContent, int color, ViewGroup container, boolean isStart) {
        SliceActionView sliceActionView = new SliceActionView(getContext(), this.mSliceStyle, this.mRowStyle);
        container.addView(sliceActionView);
        if (container.getVisibility() == 8) {
            container.setVisibility(0);
        }
        boolean isToggle = actionContent.isToggle();
        EventInfo eventInfo = new EventInfo(getMode(), !isToggle, isToggle != 0 ? 3 : 0, this.mRowIndex);
        if (isStart) {
            eventInfo.setPosition(0, 0, 1);
        }
        sliceActionView.setAction(actionContent, eventInfo, this.mObserver, color, this.mLoadingListener);
        if (this.mLoadingActions.contains(actionContent.getSliceItem())) {
            sliceActionView.setLoading(true);
        }
        if (isToggle != 0) {
            this.mToggles.put(actionContent, sliceActionView);
        } else {
            this.mActions.put(actionContent, sliceActionView);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean addItem(SliceItem sliceItem, int color, boolean isStart) {
        IconCompat iconCompat;
        SliceItem sliceItem2;
        SliceStyle sliceStyle;
        int i;
        int i2;
        ViewGroup viewGroup = isStart ? this.mStartContainer : this.mEndContainer;
        if ("slice".equals(sliceItem.getFormat()) || "action".equals(sliceItem.getFormat())) {
            if (sliceItem.hasHint("shortcut")) {
                addAction(new SliceActionImpl(sliceItem), color, viewGroup, isStart);
                return true;
            } else if (sliceItem.getSlice().getItems().size() == 0) {
                return false;
            } else {
                sliceItem = sliceItem.getSlice().getItems().get(0);
            }
        }
        TextView textView = null;
        if ("image".equals(sliceItem.getFormat())) {
            iconCompat = sliceItem.getIcon();
            sliceItem2 = null;
        } else if ("long".equals(sliceItem.getFormat())) {
            sliceItem2 = sliceItem;
            iconCompat = null;
        } else {
            iconCompat = null;
            sliceItem2 = null;
        }
        if (iconCompat != null) {
            boolean z = !sliceItem.hasHint("no_tint");
            boolean hasHint = sliceItem.hasHint("raw");
            float f = getResources().getDisplayMetrics().density;
            ImageView imageView = new ImageView(getContext());
            Drawable loadDrawable = iconCompat.loadDrawable(getContext());
            SliceStyle sliceStyle2 = this.mSliceStyle;
            if ((sliceStyle2 != null && sliceStyle2.getApplyCornerRadiusToLargeImages()) && sliceItem.hasHint("large")) {
                imageView.setImageDrawable(new CornerDrawable(loadDrawable, this.mSliceStyle.getImageCornerRadius()));
            } else {
                imageView.setImageDrawable(loadDrawable);
            }
            if (z && color != -1) {
                imageView.setColorFilter(color);
            }
            if (this.mIsRangeSliding) {
                viewGroup.removeAllViews();
                viewGroup.addView(imageView);
            } else {
                viewGroup.addView(imageView);
            }
            RowStyle rowStyle = this.mRowStyle;
            if (rowStyle != null) {
                int iconSize = rowStyle.getIconSize();
                if (iconSize <= 0) {
                    iconSize = this.mIconSize;
                }
                this.mIconSize = iconSize;
                int imageSize = this.mRowStyle.getImageSize();
                if (imageSize <= 0) {
                    imageSize = this.mImageSize;
                }
                this.mImageSize = imageSize;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = hasHint ? Math.round(loadDrawable.getIntrinsicWidth() / f) : this.mImageSize;
            if (hasHint) {
                i = Math.round(loadDrawable.getIntrinsicHeight() / f);
            } else {
                i = this.mImageSize;
            }
            layoutParams.height = i;
            imageView.setLayoutParams(layoutParams);
            if (z) {
                int i3 = this.mImageSize;
                i2 = i3 == -1 ? this.mIconSize / 2 : (i3 - this.mIconSize) / 2;
            } else {
                i2 = 0;
            }
            imageView.setPadding(i2, i2, i2, i2);
            textView = imageView;
        } else if (sliceItem2 != null) {
            textView = new TextView(getContext());
            textView.setText(SliceViewUtil.getTimestampString(getContext(), sliceItem.getLong()));
            if (this.mSliceStyle != null) {
                textView.setTextSize(0, sliceStyle.getSubtitleSize());
                textView.setTextColor(this.mRowStyle.getSubtitleColor());
            }
            viewGroup.addView(textView);
        }
        return textView != null;
    }

    private void showSeeMore() {
        final Button button = (Button) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_row_show_more, (ViewGroup) this, false);
        button.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.RowView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                try {
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        EventInfo eventInfo = new EventInfo(rowView.getMode(), 4, 0, RowView.this.mRowIndex);
                        RowView rowView2 = RowView.this;
                        rowView2.mObserver.onSliceAction(eventInfo, rowView2.mRowContent.getSliceItem());
                    }
                    RowView rowView3 = RowView.this;
                    rowView3.mShowActionSpinner = rowView3.mRowContent.getSliceItem().fireActionInternal(RowView.this.getContext(), null);
                    RowView rowView4 = RowView.this;
                    if (rowView4.mShowActionSpinner) {
                        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = rowView4.mLoadingListener;
                        if (sliceActionLoadingListener != null) {
                            sliceActionLoadingListener.onSliceActionLoading(rowView4.mRowContent.getSliceItem(), RowView.this.mRowIndex);
                        }
                        RowView rowView5 = RowView.this;
                        rowView5.mLoadingActions.add(rowView5.mRowContent.getSliceItem());
                        button.setVisibility(8);
                    }
                    RowView.this.updateActionSpinner();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        });
        int i = this.mTintColor;
        if (i != -1) {
            button.setTextColor(i);
        }
        this.mSeeMoreView = button;
        this.mRootView.addView(button);
        if (this.mLoadingActions.contains(this.mRowContent.getSliceItem())) {
            this.mShowActionSpinner = true;
            button.setVisibility(8);
            updateActionSpinner();
        }
    }

    void updateActionSpinner() {
        this.mActionSpinner.setVisibility(this.mShowActionSpinner ? 0 : 8);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setLoadingActions(Set<SliceItem> actions) {
        if (actions == null) {
            this.mLoadingActions.clear();
            this.mShowActionSpinner = false;
        } else {
            this.mLoadingActions = actions;
        }
        updateEndItems();
        updateActionSpinner();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        SliceActionView sliceActionView;
        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener;
        SliceActionImpl sliceActionImpl = this.mRowAction;
        if (sliceActionImpl == null || sliceActionImpl.getActionItem() == null) {
            return;
        }
        if (this.mRowAction.getSubtype() != null) {
            String subtype = this.mRowAction.getSubtype();
            subtype.hashCode();
            char c = 65535;
            switch (subtype.hashCode()) {
                case -868304044:
                    if (subtype.equals("toggle")) {
                        c = 0;
                        break;
                    }
                    break;
                case 759128640:
                    if (subtype.equals("time_picker")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1250407999:
                    if (subtype.equals("date_picker")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    sliceActionView = this.mToggles.get(this.mRowAction);
                    break;
                case 1:
                    onClickPicker(false);
                    return;
                case 2:
                    onClickPicker(true);
                    return;
                default:
                    sliceActionView = this.mActions.get(this.mRowAction);
                    break;
            }
        } else {
            sliceActionView = this.mActions.get(this.mRowAction);
        }
        if (sliceActionView != null && !(view instanceof SliceActionView)) {
            sliceActionView.sendAction();
        } else if (this.mRowContent.getIsHeader()) {
            performClick();
        } else {
            try {
                this.mShowActionSpinner = this.mRowAction.getActionItem().fireActionInternal(getContext(), null);
                if (this.mObserver != null) {
                    this.mObserver.onSliceAction(new EventInfo(getMode(), 3, 0, this.mRowIndex), this.mRowAction.getSliceItem());
                }
                if (this.mShowActionSpinner && (sliceActionLoadingListener = this.mLoadingListener) != null) {
                    sliceActionLoadingListener.onSliceActionLoading(this.mRowAction.getSliceItem(), this.mRowIndex);
                    this.mLoadingActions.add(this.mRowAction.getSliceItem());
                }
                updateActionSpinner();
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    private void onClickPicker(boolean isDatePicker) {
        if (this.mRowAction == null) {
            return;
        }
        Log.d("ASDF", "ASDF" + isDatePicker + ":" + this.mRowAction.getSliceItem());
        SliceItem findSubtype = SliceQuery.findSubtype(this.mRowAction.getSliceItem(), "long", "millis");
        if (findSubtype == null) {
            return;
        }
        int i = this.mRowIndex;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(findSubtype.getLong()));
        if (isDatePicker) {
            new DatePickerDialog(getContext(), R$style.DialogTheme, new DateSetListener(this.mRowAction.getSliceItem(), i), calendar.get(1), calendar.get(2), calendar.get(5)).show();
        } else {
            new TimePickerDialog(getContext(), R$style.DialogTheme, new TimeSetListener(this.mRowAction.getSliceItem(), i), calendar.get(11), calendar.get(12), false).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DateSetListener implements DatePickerDialog.OnDateSetListener {
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
                    sliceItem.fireAction(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver == null) {
                        return;
                    }
                    RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
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
                    sliceItem.fireAction(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver == null) {
                        return;
                    }
                    RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (this.mSelectionItem == null || parent != this.mSelectionSpinner || position < 0 || position >= this.mSelectionOptionKeys.size()) {
            return;
        }
        if (this.mObserver != null) {
            this.mObserver.onSliceAction(new EventInfo(getMode(), 5, 6, this.mRowIndex), this.mSelectionItem);
        }
        try {
            if (!this.mSelectionItem.fireActionInternal(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.SELECTION", this.mSelectionOptionKeys.get(position)))) {
                return;
            }
            this.mShowActionSpinner = true;
            SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
            if (sliceActionLoadingListener != null) {
                sliceActionLoadingListener.onSliceActionLoading(this.mRowAction.getSliceItem(), this.mRowIndex);
                this.mLoadingActions.add(this.mRowAction.getSliceItem());
            }
            updateActionSpinner();
        } catch (PendingIntent.CanceledException e) {
            Log.e("RowView", "PendingIntent for slice cannot be sent", e);
        }
    }

    private void setViewClickable(View layout, boolean isClickable) {
        Drawable drawable = null;
        layout.setOnClickListener(isClickable ? this : null);
        if (isClickable) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        layout.setBackground(drawable);
        layout.setClickable(isClickable);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void resetView() {
        this.mRowContent = null;
        this.mLoadingActions.clear();
        resetViewState();
    }

    private void resetViewState() {
        this.mRootView.setVisibility(0);
        setLayoutDirection(2);
        setViewClickable(this.mRootView, false);
        setViewClickable(this.mContent, false);
        this.mStartContainer.removeAllViews();
        this.mEndContainer.removeAllViews();
        this.mEndContainer.setVisibility(8);
        this.mPrimaryText.setText((CharSequence) null);
        this.mSecondaryText.setText((CharSequence) null);
        this.mLastUpdatedText.setText((CharSequence) null);
        this.mLastUpdatedText.setVisibility(8);
        this.mToggles.clear();
        this.mActions.clear();
        this.mRowAction = null;
        this.mBottomDivider.setVisibility(8);
        this.mActionDivider.setVisibility(8);
        View view = this.mSeeMoreView;
        if (view != null) {
            this.mRootView.removeView(view);
            this.mSeeMoreView = null;
        }
        this.mIsRangeSliding = false;
        this.mRangeHasPendingUpdate = false;
        this.mRangeItem = null;
        this.mRangeMinValue = 0;
        this.mRangeMaxValue = 0;
        this.mRangeValue = 0;
        this.mLastSentRangeUpdate = 0L;
        this.mHandler = null;
        View view2 = this.mRangeBar;
        if (view2 != null) {
            if (this.mStartItem == null) {
                removeView(view2);
            } else {
                this.mContent.removeView(view2);
            }
            this.mRangeBar = null;
        }
        this.mSubContent.setVisibility(0);
        this.mStartItem = null;
        this.mActionSpinner.setVisibility(8);
        Spinner spinner = this.mSelectionSpinner;
        if (spinner != null) {
            removeView(spinner);
            this.mSelectionSpinner = null;
        }
        this.mSelectionItem = null;
    }
}
