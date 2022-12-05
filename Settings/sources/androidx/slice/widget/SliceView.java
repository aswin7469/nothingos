package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceMetadata;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$attr;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$style;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class SliceView extends ViewGroup implements Observer<Slice>, View.OnClickListener {
    public static final Comparator<SliceAction> SLICE_ACTION_PRIORITY_COMPARATOR = new Comparator<SliceAction>() { // from class: androidx.slice.widget.SliceView.3
        @Override // java.util.Comparator
        public int compare(SliceAction action1, SliceAction action2) {
            int priority = action1.getPriority();
            int priority2 = action2.getPriority();
            if (priority >= 0 || priority2 >= 0) {
                if (priority < 0) {
                    return 1;
                }
                if (priority2 < 0) {
                    return -1;
                }
                if (priority2 < priority) {
                    return 1;
                }
                return priority2 > priority ? -1 : 0;
            }
            return 0;
        }
    };
    private ActionRow mActionRow;
    private int mActionRowHeight;
    private List<SliceAction> mActions;
    int[] mClickInfo;
    private Slice mCurrentSlice;
    private boolean mCurrentSliceLoggedVisible;
    private SliceMetrics mCurrentSliceMetrics;
    SliceChildView mCurrentView;
    private int mDownX;
    private int mDownY;
    Handler mHandler;
    boolean mInLongpress;
    private int mLargeHeight;
    ListContent mListContent;
    View.OnLongClickListener mLongClickListener;
    Runnable mLongpressCheck;
    private int mMinTemplateHeight;
    private View.OnClickListener mOnClickListener;
    boolean mPressing;
    Runnable mRefreshLastUpdated;
    private int mShortcutSize;
    private boolean mShowActionDividers;
    private boolean mShowActions;
    private boolean mShowHeaderDivider;
    private boolean mShowLastUpdated;
    private boolean mShowTitleItems;
    SliceMetadata mSliceMetadata;
    private OnSliceActionListener mSliceObserver;
    private SliceStyle mSliceStyle;
    private int mThemeTintColor;
    private int mTouchSlopSquared;
    private SliceViewPolicy mViewPolicy;

    /* loaded from: classes.dex */
    public interface OnSliceActionListener {
        void onSliceAction(EventInfo info, SliceItem item);
    }

    public SliceView(Context context) {
        this(context, null);
    }

    public SliceView(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.sliceViewStyle);
    }

    public SliceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mShowActions = false;
        this.mShowLastUpdated = true;
        this.mCurrentSliceLoggedVisible = false;
        this.mShowTitleItems = false;
        this.mShowHeaderDivider = false;
        this.mShowActionDividers = false;
        this.mThemeTintColor = -1;
        this.mLongpressCheck = new Runnable() { // from class: androidx.slice.widget.SliceView.1
            @Override // java.lang.Runnable
            public void run() {
                View.OnLongClickListener onLongClickListener;
                SliceView sliceView = SliceView.this;
                if (!sliceView.mPressing || (onLongClickListener = sliceView.mLongClickListener) == null) {
                    return;
                }
                sliceView.mInLongpress = true;
                onLongClickListener.onLongClick(sliceView);
                SliceView.this.performHapticFeedback(0);
            }
        };
        this.mRefreshLastUpdated = new Runnable() { // from class: androidx.slice.widget.SliceView.2
            @Override // java.lang.Runnable
            public void run() {
                SliceMetadata sliceMetadata = SliceView.this.mSliceMetadata;
                if (sliceMetadata != null && sliceMetadata.isExpired()) {
                    SliceView.this.mCurrentView.setShowLastUpdated(true);
                    SliceView sliceView = SliceView.this;
                    sliceView.mCurrentView.setSliceContent(sliceView.mListContent);
                }
                SliceView.this.mHandler.postDelayed(this, 60000L);
            }
        };
        init(context, attrs, defStyleAttr, R$style.Widget_SliceView);
    }

    public SliceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mShowActions = false;
        this.mShowLastUpdated = true;
        this.mCurrentSliceLoggedVisible = false;
        this.mShowTitleItems = false;
        this.mShowHeaderDivider = false;
        this.mShowActionDividers = false;
        this.mThemeTintColor = -1;
        this.mLongpressCheck = new Runnable() { // from class: androidx.slice.widget.SliceView.1
            @Override // java.lang.Runnable
            public void run() {
                View.OnLongClickListener onLongClickListener;
                SliceView sliceView = SliceView.this;
                if (!sliceView.mPressing || (onLongClickListener = sliceView.mLongClickListener) == null) {
                    return;
                }
                sliceView.mInLongpress = true;
                onLongClickListener.onLongClick(sliceView);
                SliceView.this.performHapticFeedback(0);
            }
        };
        this.mRefreshLastUpdated = new Runnable() { // from class: androidx.slice.widget.SliceView.2
            @Override // java.lang.Runnable
            public void run() {
                SliceMetadata sliceMetadata = SliceView.this.mSliceMetadata;
                if (sliceMetadata != null && sliceMetadata.isExpired()) {
                    SliceView.this.mCurrentView.setShowLastUpdated(true);
                    SliceView sliceView = SliceView.this;
                    sliceView.mCurrentView.setSliceContent(sliceView.mListContent);
                }
                SliceView.this.mHandler.postDelayed(this, 60000L);
            }
        };
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        SliceStyle sliceStyle = new SliceStyle(context, attrs, defStyleAttr, defStyleRes);
        this.mSliceStyle = sliceStyle;
        this.mThemeTintColor = sliceStyle.getTintColor();
        this.mShortcutSize = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_shortcut_size);
        this.mMinTemplateHeight = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_row_min_height);
        this.mLargeHeight = getResources().getDimensionPixelSize(R$dimen.abc_slice_large_height);
        this.mActionRowHeight = getResources().getDimensionPixelSize(R$dimen.abc_slice_action_row_height);
        this.mViewPolicy = new SliceViewPolicy();
        TemplateView templateView = new TemplateView(getContext());
        this.mCurrentView = templateView;
        templateView.setPolicy(this.mViewPolicy);
        SliceChildView sliceChildView = this.mCurrentView;
        addView(sliceChildView, getChildLp(sliceChildView));
        applyConfigurations();
        ActionRow actionRow = new ActionRow(getContext(), true);
        this.mActionRow = actionRow;
        actionRow.setBackground(new ColorDrawable(-1118482));
        ActionRow actionRow2 = this.mActionRow;
        addView(actionRow2, getChildLp(actionRow2));
        updateActions();
        int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mTouchSlopSquared = scaledTouchSlop * scaledTouchSlop;
        this.mHandler = new Handler();
        setClipToPadding(false);
        super.setOnClickListener(this);
    }

    public void setCurrentView(SliceChildView currentView) {
        removeView(this.mCurrentView);
        this.mCurrentView = currentView;
        currentView.setPolicy(this.mViewPolicy);
        SliceChildView sliceChildView = this.mCurrentView;
        addView(sliceChildView, getChildLp(sliceChildView));
        applyConfigurations();
    }

    void setSliceViewPolicy(SliceViewPolicy policy) {
        this.mViewPolicy = policy;
    }

    public boolean isSliceViewClickable() {
        ListContent listContent;
        return (this.mOnClickListener == null && ((listContent = this.mListContent) == null || listContent.getShortcut(getContext()) == null)) ? false : true;
    }

    public void setClickInfo(int[] info) {
        this.mClickInfo = info;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int[] iArr;
        ListContent listContent = this.mListContent;
        if (listContent != null && listContent.getShortcut(getContext()) != null) {
            try {
                SliceActionImpl sliceActionImpl = (SliceActionImpl) this.mListContent.getShortcut(getContext());
                SliceItem actionItem = sliceActionImpl.getActionItem();
                if (actionItem != null && actionItem.fireActionInternal(getContext(), null)) {
                    this.mCurrentView.setActionLoading(sliceActionImpl.getSliceItem());
                }
                if (actionItem == null || this.mSliceObserver == null || (iArr = this.mClickInfo) == null || iArr.length <= 1) {
                    return;
                }
                int mode = getMode();
                int[] iArr2 = this.mClickInfo;
                EventInfo eventInfo = new EventInfo(mode, 3, iArr2[0], iArr2[1]);
                this.mSliceObserver.onSliceAction(eventInfo, sliceActionImpl.getSliceItem());
                logSliceMetricsOnTouch(sliceActionImpl.getSliceItem(), eventInfo);
                return;
            } catch (PendingIntent.CanceledException e) {
                Log.e("SliceView", "PendingIntent for slice cannot be sent", e);
                return;
            }
        }
        View.OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener == null) {
            return;
        }
        onClickListener.onClick(this);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener listener) {
        super.setOnLongClickListener(listener);
        this.mLongClickListener = listener;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (this.mLongClickListener != null && handleTouchForLongpress(ev)) || super.onInterceptTouchEvent(ev);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        return (this.mLongClickListener != null && handleTouchForLongpress(ev)) || super.onTouchEvent(ev);
    }

    private boolean handleTouchForLongpress(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 0) {
            this.mHandler.removeCallbacks(this.mLongpressCheck);
            this.mDownX = (int) ev.getRawX();
            this.mDownY = (int) ev.getRawY();
            this.mPressing = true;
            this.mInLongpress = false;
            this.mHandler.postDelayed(this.mLongpressCheck, ViewConfiguration.getLongPressTimeout());
            return false;
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                int rawX = ((int) ev.getRawX()) - this.mDownX;
                int rawY = ((int) ev.getRawY()) - this.mDownY;
                if ((rawX * rawX) + (rawY * rawY) > this.mTouchSlopSquared) {
                    this.mPressing = false;
                    this.mHandler.removeCallbacks(this.mLongpressCheck);
                }
                return this.mInLongpress;
            } else if (actionMasked != 3) {
                return false;
            }
        }
        boolean z = this.mInLongpress;
        this.mPressing = false;
        this.mInLongpress = false;
        this.mHandler.removeCallbacks(this.mLongpressCheck);
        return z;
    }

    protected void configureViewPolicy(int maxHeight) {
        ListContent listContent = this.mListContent;
        if (listContent == null || !listContent.isValid() || getMode() == 3) {
            return;
        }
        if (maxHeight > 0 && maxHeight < this.mSliceStyle.getRowMaxHeight()) {
            int i = this.mMinTemplateHeight;
            if (maxHeight <= i) {
                maxHeight = i;
            }
            this.mViewPolicy.setMaxSmallHeight(maxHeight);
        } else {
            this.mViewPolicy.setMaxSmallHeight(0);
        }
        this.mViewPolicy.setMaxHeight(maxHeight);
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0089, code lost:
        if (r2 >= (r9 + r0)) goto L21;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        if (3 == getMode()) {
            size = this.mShortcutSize + getPaddingLeft() + getPaddingRight();
        }
        int i2 = 0;
        int i3 = this.mActionRow.getVisibility() != 8 ? this.mActionRowHeight : 0;
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        configureViewPolicy(((layoutParams == null || layoutParams.height != -2) && mode != 0) ? size2 : -1);
        int paddingTop = (size2 - getPaddingTop()) - getPaddingBottom();
        if (mode != 1073741824) {
            ListContent listContent = this.mListContent;
            if (listContent == null || !listContent.isValid()) {
                paddingTop = i3;
            } else {
                if (getMode() == 3) {
                    i = this.mShortcutSize;
                } else {
                    int height = this.mListContent.getHeight(this.mSliceStyle, this.mViewPolicy) + i3;
                    if (paddingTop > height || mode == 0) {
                        paddingTop = height;
                    } else if (!this.mSliceStyle.getExpandToAvailableHeight()) {
                        if (getMode() == 2) {
                            i = this.mLargeHeight;
                        }
                        int i4 = this.mMinTemplateHeight;
                        if (paddingTop <= i4) {
                            paddingTop = i4;
                        }
                    }
                }
                paddingTop = i + i3;
            }
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
        this.mActionRow.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i3 > 0 ? getPaddingBottom() + i3 : 0, 1073741824));
        int paddingTop2 = paddingTop + getPaddingTop();
        if (i3 <= 0) {
            i2 = getPaddingBottom();
        }
        this.mCurrentView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop2 + i2, 1073741824));
        setMeasuredDimension(size, this.mCurrentView.getMeasuredHeight() + this.mActionRow.getMeasuredHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        SliceChildView sliceChildView = this.mCurrentView;
        sliceChildView.layout(0, 0, sliceChildView.getMeasuredWidth(), sliceChildView.getMeasuredHeight());
        if (this.mActionRow.getVisibility() != 8) {
            int measuredHeight = sliceChildView.getMeasuredHeight();
            ActionRow actionRow = this.mActionRow;
            actionRow.layout(0, measuredHeight, actionRow.getMeasuredWidth(), this.mActionRow.getMeasuredHeight() + measuredHeight);
        }
    }

    @Override // androidx.lifecycle.Observer
    public void onChanged(Slice slice) {
        setSlice(slice);
    }

    public void setSlice(Slice slice) {
        LocationBasedViewTracker.trackInputFocused(this);
        LocationBasedViewTracker.trackA11yFocus(this);
        initSliceMetrics(slice);
        boolean z = false;
        boolean z2 = (slice == null || this.mCurrentSlice == null || !slice.getUri().equals(this.mCurrentSlice.getUri())) ? false : true;
        SliceMetadata sliceMetadata = this.mSliceMetadata;
        this.mCurrentSlice = slice;
        SliceMetadata from = slice != null ? SliceMetadata.from(getContext(), this.mCurrentSlice) : null;
        this.mSliceMetadata = from;
        if (z2) {
            if (sliceMetadata.getLoadingState() == 2 && from.getLoadingState() == 0) {
                return;
            }
        } else {
            this.mCurrentView.resetView();
        }
        SliceMetadata sliceMetadata2 = this.mSliceMetadata;
        this.mListContent = sliceMetadata2 != null ? sliceMetadata2.getListContent() : null;
        if (this.mShowTitleItems) {
            showTitleItems(true);
        }
        if (this.mShowHeaderDivider) {
            showHeaderDivider(true);
        }
        if (this.mShowActionDividers) {
            showActionDividers(true);
        }
        ListContent listContent = this.mListContent;
        if (listContent == null || !listContent.isValid()) {
            this.mActions = null;
            this.mCurrentView.resetView();
            updateActions();
            return;
        }
        this.mCurrentView.setLoadingActions(null);
        this.mActions = this.mSliceMetadata.getSliceActions();
        this.mCurrentView.setLastUpdated(this.mSliceMetadata.getLastUpdatedTime());
        SliceChildView sliceChildView = this.mCurrentView;
        if (this.mShowLastUpdated && this.mSliceMetadata.isExpired()) {
            z = true;
        }
        sliceChildView.setShowLastUpdated(z);
        this.mCurrentView.setAllowTwoLines(this.mSliceMetadata.isPermissionSlice());
        this.mCurrentView.setTint(getTintColor());
        if (this.mListContent.getLayoutDir() != -1) {
            this.mCurrentView.setLayoutDirection(this.mListContent.getLayoutDir());
        } else {
            this.mCurrentView.setLayoutDirection(2);
        }
        this.mCurrentView.setSliceContent(this.mListContent);
        updateActions();
        logSliceMetricsVisibilityChange(true);
        refreshLastUpdatedLabel(true);
    }

    public Slice getSlice() {
        return this.mCurrentSlice;
    }

    public List<SliceAction> getSliceActions() {
        List<SliceAction> list = this.mActions;
        if (list == null || !list.isEmpty()) {
            return this.mActions;
        }
        return null;
    }

    public void setSliceActions(List<SliceAction> newActions) {
        SliceMetadata sliceMetadata;
        if (this.mCurrentSlice == null || (sliceMetadata = this.mSliceMetadata) == null) {
            throw new IllegalStateException("Trying to set actions on a view without a slice");
        }
        List<SliceAction> sliceActions = sliceMetadata.getSliceActions();
        if (sliceActions != null && newActions != null) {
            for (int i = 0; i < newActions.size(); i++) {
                if (!sliceActions.contains(newActions.get(i))) {
                    throw new IllegalArgumentException("Trying to set an action that isn't available: " + newActions.get(i));
                }
            }
        }
        if (newActions == null) {
            newActions = new ArrayList<>();
        }
        this.mActions = newActions;
        updateActions();
    }

    public void setMode(int mode) {
        setMode(mode, false);
    }

    public void setScrollable(boolean isScrollable) {
        if (isScrollable != this.mViewPolicy.isScrollable()) {
            this.mViewPolicy.setScrollable(isScrollable);
        }
    }

    public void setOnSliceActionListener(OnSliceActionListener observer) {
        this.mSliceObserver = observer;
        this.mCurrentView.setSliceActionListener(observer);
    }

    public void setAccentColor(int accentColor) {
        this.mThemeTintColor = accentColor;
        this.mSliceStyle.setTintColor(accentColor);
        this.mCurrentView.setTint(getTintColor());
    }

    public void setRowStyleFactory(RowStyleFactory rowStyleFactory) {
        this.mSliceStyle.setRowStyleFactory(rowStyleFactory);
    }

    public void setMode(int mode, boolean animate) {
        if (animate) {
            Log.e("SliceView", "Animation not supported yet");
        }
        if (this.mViewPolicy.getMode() == mode) {
            return;
        }
        if (mode != 1 && mode != 2 && mode != 3) {
            Log.w("SliceView", "Unknown mode: " + mode + " please use one of MODE_SHORTCUT, MODE_SMALL, MODE_LARGE");
            mode = 2;
        }
        this.mViewPolicy.setMode(mode);
        updateViewConfig();
    }

    public int getMode() {
        return this.mViewPolicy.getMode();
    }

    public void setShowTitleItems(boolean enabled) {
        this.mShowTitleItems = enabled;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showTitleItems(enabled);
        }
    }

    @Deprecated
    public void showTitleItems(boolean enabled) {
        setShowTitleItems(enabled);
    }

    public void setShowHeaderDivider(boolean enabled) {
        this.mShowHeaderDivider = enabled;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showHeaderDivider(enabled);
        }
    }

    @Deprecated
    public void showHeaderDivider(boolean enabled) {
        setShowHeaderDivider(enabled);
    }

    public void setShowActionDividers(boolean enabled) {
        this.mShowActionDividers = enabled;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showActionDividers(enabled);
        }
    }

    @Deprecated
    public void showActionDividers(boolean enabled) {
        setShowActionDividers(enabled);
    }

    public void setShowActionRow(boolean show) {
        this.mShowActions = show;
        updateActions();
    }

    public int getHiddenItemCount() {
        return this.mCurrentView.getHiddenItemCount();
    }

    private void updateViewConfig() {
        int mode = getMode();
        SliceChildView sliceChildView = this.mCurrentView;
        boolean z = sliceChildView instanceof ShortcutView;
        Set<SliceItem> loadingActions = sliceChildView.getLoadingActions();
        boolean z2 = true;
        if (mode == 3 && !z) {
            removeView(this.mCurrentView);
            ShortcutView shortcutView = new ShortcutView(getContext());
            this.mCurrentView = shortcutView;
            addView(shortcutView, getChildLp(shortcutView));
        } else if (mode == 3 || !z) {
            z2 = false;
        } else {
            removeView(this.mCurrentView);
            TemplateView templateView = new TemplateView(getContext());
            this.mCurrentView = templateView;
            addView(templateView, getChildLp(templateView));
        }
        if (z2) {
            this.mCurrentView.setPolicy(this.mViewPolicy);
            applyConfigurations();
            ListContent listContent = this.mListContent;
            if (listContent != null && listContent.isValid()) {
                this.mCurrentView.setSliceContent(this.mListContent);
            }
            this.mCurrentView.setLoadingActions(loadingActions);
        }
        updateActions();
    }

    private void applyConfigurations() {
        this.mCurrentView.setSliceActionListener(this.mSliceObserver);
        SliceChildView sliceChildView = this.mCurrentView;
        SliceStyle sliceStyle = this.mSliceStyle;
        sliceChildView.setStyle(sliceStyle, sliceStyle.getRowStyle(null));
        this.mCurrentView.setTint(getTintColor());
        ListContent listContent = this.mListContent;
        if (listContent != null && listContent.getLayoutDir() != -1) {
            this.mCurrentView.setLayoutDirection(this.mListContent.getLayoutDir());
        } else {
            this.mCurrentView.setLayoutDirection(2);
        }
    }

    private void updateActions() {
        if (this.mActions == null) {
            this.mActionRow.setVisibility(8);
            this.mCurrentView.setSliceActions(null);
            this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            return;
        }
        ArrayList arrayList = new ArrayList(this.mActions);
        Collections.sort(arrayList, SLICE_ACTION_PRIORITY_COMPARATOR);
        if (this.mShowActions && getMode() != 3 && this.mActions.size() >= 2) {
            this.mActionRow.setActions(arrayList, getTintColor());
            this.mActionRow.setVisibility(0);
            this.mCurrentView.setSliceActions(null);
            this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), 0);
            this.mActionRow.setPaddingRelative(getPaddingStart(), 0, getPaddingEnd(), getPaddingBottom());
            return;
        }
        this.mCurrentView.setSliceActions(arrayList);
        this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
        this.mActionRow.setVisibility(8);
    }

    private int getTintColor() {
        int i = this.mThemeTintColor;
        if (i != -1) {
            return i;
        }
        SliceItem findSubtype = SliceQuery.findSubtype(this.mCurrentSlice, "int", "color");
        if (findSubtype != null) {
            return findSubtype.getInt();
        }
        return SliceViewUtil.getColorAccent(getContext());
    }

    private ViewGroup.LayoutParams getChildLp(View child) {
        if (child instanceof ShortcutView) {
            int i = this.mShortcutSize;
            return new ViewGroup.LayoutParams(i, i);
        }
        return new ViewGroup.LayoutParams(-1, -1);
    }

    public static String modeToString(int mode) {
        if (mode != 1) {
            if (mode == 2) {
                return "MODE LARGE";
            }
            if (mode == 3) {
                return "MODE SHORTCUT";
            }
            return "unknown mode: " + mode;
        }
        return "MODE SMALL";
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isShown()) {
            logSliceMetricsVisibilityChange(true);
            refreshLastUpdatedLabel(true);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        logSliceMetricsVisibilityChange(false);
        refreshLastUpdatedLabel(false);
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (isAttachedToWindow()) {
            boolean z = true;
            logSliceMetricsVisibilityChange(visibility == 0);
            if (visibility != 0) {
                z = false;
            }
            refreshLastUpdatedLabel(z);
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        boolean z = true;
        logSliceMetricsVisibilityChange(visibility == 0);
        if (visibility != 0) {
            z = false;
        }
        refreshLastUpdatedLabel(z);
    }

    private void initSliceMetrics(Slice slice) {
        if (slice == null || slice.getUri() == null) {
            logSliceMetricsVisibilityChange(false);
            this.mCurrentSliceMetrics = null;
            return;
        }
        Slice slice2 = this.mCurrentSlice;
        if (slice2 != null && slice2.getUri().equals(slice.getUri())) {
            return;
        }
        logSliceMetricsVisibilityChange(false);
        this.mCurrentSliceMetrics = SliceMetrics.getInstance(getContext(), slice.getUri());
    }

    private void logSliceMetricsVisibilityChange(boolean visibility) {
        SliceMetrics sliceMetrics = this.mCurrentSliceMetrics;
        if (sliceMetrics != null) {
            if (visibility && !this.mCurrentSliceLoggedVisible) {
                sliceMetrics.logVisible();
                this.mCurrentSliceLoggedVisible = true;
            }
            if (visibility || !this.mCurrentSliceLoggedVisible) {
                return;
            }
            this.mCurrentSliceMetrics.logHidden();
            this.mCurrentSliceLoggedVisible = false;
        }
    }

    private void logSliceMetricsOnTouch(SliceItem item, EventInfo info) {
        if (this.mCurrentSliceMetrics == null || item.getSlice() == null || item.getSlice().getUri() == null) {
            return;
        }
        this.mCurrentSliceMetrics.logTouch(info.actionType, item.getSlice().getUri());
    }

    private void refreshLastUpdatedLabel(boolean visibility) {
        SliceMetadata sliceMetadata;
        if (!this.mShowLastUpdated || (sliceMetadata = this.mSliceMetadata) == null || sliceMetadata.neverExpires()) {
            return;
        }
        if (visibility) {
            Handler handler = this.mHandler;
            Runnable runnable = this.mRefreshLastUpdated;
            long j = 60000;
            if (!this.mSliceMetadata.isExpired()) {
                j = 60000 + this.mSliceMetadata.getTimeToExpiry();
            }
            handler.postDelayed(runnable, j);
            return;
        }
        this.mHandler.removeCallbacks(this.mRefreshLastUpdated);
    }
}
