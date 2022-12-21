package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import androidx.slice.view.C1349R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SliceView extends ViewGroup implements Observer<Slice>, View.OnClickListener {
    public static final int MODE_LARGE = 2;
    public static final int MODE_SHORTCUT = 3;
    public static final int MODE_SMALL = 1;
    private static final int REFRESH_LAST_UPDATED_IN_MILLIS = 60000;
    public static final Comparator<SliceAction> SLICE_ACTION_PRIORITY_COMPARATOR = new Comparator<SliceAction>() {
        public int compare(SliceAction sliceAction, SliceAction sliceAction2) {
            int priority = sliceAction.getPriority();
            int priority2 = sliceAction2.getPriority();
            if (priority < 0 && priority2 < 0) {
                return 0;
            }
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
    };
    private static final String TAG = "SliceView";
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

    public interface OnSliceActionListener {
        void onSliceAction(EventInfo eventInfo, SliceItem sliceItem);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceMode {
    }

    public SliceView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SliceView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C1349R.attr.sliceViewStyle);
    }

    public SliceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowActions = false;
        this.mShowLastUpdated = true;
        this.mCurrentSliceLoggedVisible = false;
        this.mShowTitleItems = false;
        this.mShowHeaderDivider = false;
        this.mShowActionDividers = false;
        this.mThemeTintColor = -1;
        this.mLongpressCheck = new Runnable() {
            public void run() {
                if (SliceView.this.mPressing && SliceView.this.mLongClickListener != null) {
                    SliceView.this.mInLongpress = true;
                    SliceView.this.mLongClickListener.onLongClick(SliceView.this);
                    SliceView.this.performHapticFeedback(0);
                }
            }
        };
        this.mRefreshLastUpdated = new Runnable() {
            public void run() {
                if (SliceView.this.mSliceMetadata != null && SliceView.this.mSliceMetadata.isExpired()) {
                    SliceView.this.mCurrentView.setShowLastUpdated(true);
                    SliceView.this.mCurrentView.setSliceContent(SliceView.this.mListContent);
                }
                SliceView.this.mHandler.postDelayed(this, 60000);
            }
        };
        init(context, attributeSet, i, C1349R.style.Widget_SliceView);
    }

    public SliceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mShowActions = false;
        this.mShowLastUpdated = true;
        this.mCurrentSliceLoggedVisible = false;
        this.mShowTitleItems = false;
        this.mShowHeaderDivider = false;
        this.mShowActionDividers = false;
        this.mThemeTintColor = -1;
        this.mLongpressCheck = new Runnable() {
            public void run() {
                if (SliceView.this.mPressing && SliceView.this.mLongClickListener != null) {
                    SliceView.this.mInLongpress = true;
                    SliceView.this.mLongClickListener.onLongClick(SliceView.this);
                    SliceView.this.performHapticFeedback(0);
                }
            }
        };
        this.mRefreshLastUpdated = new Runnable() {
            public void run() {
                if (SliceView.this.mSliceMetadata != null && SliceView.this.mSliceMetadata.isExpired()) {
                    SliceView.this.mCurrentView.setShowLastUpdated(true);
                    SliceView.this.mCurrentView.setSliceContent(SliceView.this.mListContent);
                }
                SliceView.this.mHandler.postDelayed(this, 60000);
            }
        };
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        SliceStyle sliceStyle = new SliceStyle(context, attributeSet, i, i2);
        this.mSliceStyle = sliceStyle;
        this.mThemeTintColor = sliceStyle.getTintColor();
        this.mShortcutSize = getContext().getResources().getDimensionPixelSize(C1349R.dimen.abc_slice_shortcut_size);
        this.mMinTemplateHeight = getContext().getResources().getDimensionPixelSize(C1349R.dimen.abc_slice_row_min_height);
        this.mLargeHeight = getResources().getDimensionPixelSize(C1349R.dimen.abc_slice_large_height);
        this.mActionRowHeight = getResources().getDimensionPixelSize(C1349R.dimen.abc_slice_action_row_height);
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

    /* access modifiers changed from: package-private */
    public void setSliceViewPolicy(SliceViewPolicy sliceViewPolicy) {
        this.mViewPolicy = sliceViewPolicy;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r1.mListContent;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSliceViewClickable() {
        /*
            r1 = this;
            android.view.View$OnClickListener r0 = r1.mOnClickListener
            if (r0 != 0) goto L_0x0015
            androidx.slice.widget.ListContent r0 = r1.mListContent
            if (r0 == 0) goto L_0x0013
            android.content.Context r1 = r1.getContext()
            androidx.slice.core.SliceAction r1 = r0.getShortcut(r1)
            if (r1 == 0) goto L_0x0013
            goto L_0x0015
        L_0x0013:
            r1 = 0
            goto L_0x0016
        L_0x0015:
            r1 = 1
        L_0x0016:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.SliceView.isSliceViewClickable():boolean");
    }

    public void setClickInfo(int[] iArr) {
        this.mClickInfo = iArr;
    }

    public void onClick(View view) {
        int[] iArr;
        ListContent listContent = this.mListContent;
        if (listContent == null || listContent.getShortcut(getContext()) == null) {
            View.OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(this);
                return;
            }
            return;
        }
        try {
            SliceActionImpl sliceActionImpl = (SliceActionImpl) this.mListContent.getShortcut(getContext());
            SliceItem actionItem = sliceActionImpl.getActionItem();
            if (actionItem != null && actionItem.fireActionInternal(getContext(), (Intent) null)) {
                this.mCurrentView.setActionLoading(sliceActionImpl.getSliceItem());
            }
            if (actionItem != null && this.mSliceObserver != null && (iArr = this.mClickInfo) != null && iArr.length > 1) {
                int mode = getMode();
                int[] iArr2 = this.mClickInfo;
                EventInfo eventInfo = new EventInfo(mode, 3, iArr2[0], iArr2[1]);
                this.mSliceObserver.onSliceAction(eventInfo, sliceActionImpl.getSliceItem());
                logSliceMetricsOnTouch(sliceActionImpl.getSliceItem(), eventInfo);
            }
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "PendingIntent for slice cannot be sent", e);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        super.setOnLongClickListener(onLongClickListener);
        this.mLongClickListener = onLongClickListener;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return (this.mLongClickListener != null && handleTouchForLongpress(motionEvent)) || super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return (this.mLongClickListener != null && handleTouchForLongpress(motionEvent)) || super.onTouchEvent(motionEvent);
    }

    private boolean handleTouchForLongpress(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int rawX = ((int) motionEvent.getRawX()) - this.mDownX;
                    int rawY = ((int) motionEvent.getRawY()) - this.mDownY;
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
        this.mHandler.removeCallbacks(this.mLongpressCheck);
        this.mDownX = (int) motionEvent.getRawX();
        this.mDownY = (int) motionEvent.getRawY();
        this.mPressing = true;
        this.mInLongpress = false;
        this.mHandler.postDelayed(this.mLongpressCheck, (long) ViewConfiguration.getLongPressTimeout());
        return false;
    }

    /* access modifiers changed from: protected */
    public void configureViewPolicy(int i) {
        ListContent listContent = this.mListContent;
        if (listContent != null && listContent.isValid() && getMode() != 3) {
            if (i <= 0 || i >= this.mSliceStyle.getRowMaxHeight()) {
                this.mViewPolicy.setMaxSmallHeight(0);
            } else {
                int i2 = this.mMinTemplateHeight;
                if (i <= i2) {
                    i = i2;
                }
                this.mViewPolicy.setMaxSmallHeight(i);
            }
            this.mViewPolicy.setMaxHeight(i);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0089, code lost:
        if (r2 >= (r9 + r0)) goto L_0x0062;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r8, int r9) {
        /*
            r7 = this;
            int r8 = android.view.View.MeasureSpec.getSize(r8)
            int r0 = r7.getMode()
            r1 = 3
            if (r1 != r0) goto L_0x0017
            int r8 = r7.mShortcutSize
            int r0 = r7.getPaddingLeft()
            int r8 = r8 + r0
            int r0 = r7.getPaddingRight()
            int r8 = r8 + r0
        L_0x0017:
            androidx.slice.widget.ActionRow r0 = r7.mActionRow
            int r0 = r0.getVisibility()
            r2 = 8
            r3 = 0
            if (r0 == r2) goto L_0x0025
            int r0 = r7.mActionRowHeight
            goto L_0x0026
        L_0x0025:
            r0 = r3
        L_0x0026:
            int r2 = android.view.View.MeasureSpec.getSize(r9)
            int r9 = android.view.View.MeasureSpec.getMode(r9)
            android.view.ViewGroup$LayoutParams r4 = r7.getLayoutParams()
            if (r4 == 0) goto L_0x0039
            int r4 = r4.height
            r5 = -2
            if (r4 == r5) goto L_0x003b
        L_0x0039:
            if (r9 != 0) goto L_0x003d
        L_0x003b:
            r4 = -1
            goto L_0x003e
        L_0x003d:
            r4 = r2
        L_0x003e:
            r7.configureViewPolicy(r4)
            int r4 = r7.getPaddingTop()
            int r2 = r2 - r4
            int r4 = r7.getPaddingBottom()
            int r2 = r2 - r4
            r4 = 1073741824(0x40000000, float:2.0)
            if (r9 == r4) goto L_0x0095
            androidx.slice.widget.ListContent r5 = r7.mListContent
            if (r5 == 0) goto L_0x0094
            boolean r5 = r5.isValid()
            if (r5 != 0) goto L_0x005a
            goto L_0x0094
        L_0x005a:
            int r5 = r7.getMode()
            if (r5 != r1) goto L_0x0065
            int r9 = r7.mShortcutSize
        L_0x0062:
            int r2 = r9 + r0
            goto L_0x0095
        L_0x0065:
            androidx.slice.widget.ListContent r1 = r7.mListContent
            androidx.slice.widget.SliceStyle r5 = r7.mSliceStyle
            androidx.slice.widget.SliceViewPolicy r6 = r7.mViewPolicy
            int r1 = r1.getHeight(r5, r6)
            int r1 = r1 + r0
            if (r2 > r1) goto L_0x0092
            if (r9 != 0) goto L_0x0075
            goto L_0x0092
        L_0x0075:
            androidx.slice.widget.SliceStyle r9 = r7.mSliceStyle
            boolean r9 = r9.getExpandToAvailableHeight()
            if (r9 == 0) goto L_0x007e
            goto L_0x0095
        L_0x007e:
            int r9 = r7.getMode()
            r1 = 2
            if (r9 != r1) goto L_0x008c
            int r9 = r7.mLargeHeight
            int r1 = r9 + r0
            if (r2 < r1) goto L_0x008c
            goto L_0x0062
        L_0x008c:
            int r9 = r7.mMinTemplateHeight
            if (r2 > r9) goto L_0x0095
            r2 = r9
            goto L_0x0095
        L_0x0092:
            r2 = r1
            goto L_0x0095
        L_0x0094:
            r2 = r0
        L_0x0095:
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r4)
            if (r0 <= 0) goto L_0x00a1
            int r1 = r7.getPaddingBottom()
            int r1 = r1 + r0
            goto L_0x00a2
        L_0x00a1:
            r1 = r3
        L_0x00a2:
            androidx.slice.widget.ActionRow r5 = r7.mActionRow
            int r1 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r4)
            r5.measure(r9, r1)
            int r1 = r7.getPaddingTop()
            int r2 = r2 + r1
            if (r0 <= 0) goto L_0x00b3
            goto L_0x00b7
        L_0x00b3:
            int r3 = r7.getPaddingBottom()
        L_0x00b7:
            int r2 = r2 + r3
            androidx.slice.widget.SliceChildView r0 = r7.mCurrentView
            int r1 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
            r0.measure(r9, r1)
            androidx.slice.widget.SliceChildView r9 = r7.mCurrentView
            int r9 = r9.getMeasuredHeight()
            androidx.slice.widget.ActionRow r0 = r7.mActionRow
            int r0 = r0.getMeasuredHeight()
            int r9 = r9 + r0
            r7.setMeasuredDimension(r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.SliceView.onMeasure(int, int):void");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        SliceChildView sliceChildView = this.mCurrentView;
        sliceChildView.layout(0, 0, sliceChildView.getMeasuredWidth(), sliceChildView.getMeasuredHeight());
        if (this.mActionRow.getVisibility() != 8) {
            int measuredHeight = sliceChildView.getMeasuredHeight();
            ActionRow actionRow = this.mActionRow;
            actionRow.layout(0, measuredHeight, actionRow.getMeasuredWidth(), this.mActionRow.getMeasuredHeight() + measuredHeight);
        }
    }

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
        if (!z2) {
            this.mCurrentView.resetView();
        } else if (sliceMetadata.getLoadingState() == 2 && from.getLoadingState() == 0) {
            return;
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
        this.mCurrentView.setLoadingActions((Set<SliceItem>) null);
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

    public void setSliceActions(List<SliceAction> list) {
        SliceMetadata sliceMetadata;
        if (this.mCurrentSlice == null || (sliceMetadata = this.mSliceMetadata) == null) {
            throw new IllegalStateException("Trying to set actions on a view without a slice");
        }
        List<SliceAction> sliceActions = sliceMetadata.getSliceActions();
        if (!(sliceActions == null || list == null)) {
            int i = 0;
            while (i < list.size()) {
                if (sliceActions.contains(list.get(i))) {
                    i++;
                } else {
                    throw new IllegalArgumentException("Trying to set an action that isn't available: " + list.get(i));
                }
            }
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mActions = list;
        updateActions();
    }

    public void setMode(int i) {
        setMode(i, false);
    }

    public void setScrollable(boolean z) {
        if (z != this.mViewPolicy.isScrollable()) {
            this.mViewPolicy.setScrollable(z);
        }
    }

    public boolean isScrollable() {
        return this.mViewPolicy.isScrollable();
    }

    public void setOnSliceActionListener(OnSliceActionListener onSliceActionListener) {
        this.mSliceObserver = onSliceActionListener;
        this.mCurrentView.setSliceActionListener(onSliceActionListener);
    }

    public void setAccentColor(int i) {
        this.mThemeTintColor = i;
        this.mSliceStyle.setTintColor(i);
        this.mCurrentView.setTint(getTintColor());
    }

    public void setRowStyleFactory(RowStyleFactory rowStyleFactory) {
        this.mSliceStyle.setRowStyleFactory(rowStyleFactory);
    }

    public void setMode(int i, boolean z) {
        if (z) {
            Log.e(TAG, "Animation not supported yet");
        }
        if (this.mViewPolicy.getMode() != i) {
            if (!(i == 1 || i == 2 || i == 3)) {
                Log.w(TAG, "Unknown mode: " + i + " please use one of MODE_SHORTCUT, MODE_SMALL, MODE_LARGE");
                i = 2;
            }
            this.mViewPolicy.setMode(i);
            updateViewConfig();
        }
    }

    public int getMode() {
        return this.mViewPolicy.getMode();
    }

    public void setShowTitleItems(boolean z) {
        this.mShowTitleItems = z;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showTitleItems(z);
        }
    }

    @Deprecated
    public void showTitleItems(boolean z) {
        setShowTitleItems(z);
    }

    public void setShowHeaderDivider(boolean z) {
        this.mShowHeaderDivider = z;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showHeaderDivider(z);
        }
    }

    @Deprecated
    public void showHeaderDivider(boolean z) {
        setShowHeaderDivider(z);
    }

    public void setShowActionDividers(boolean z) {
        this.mShowActionDividers = z;
        ListContent listContent = this.mListContent;
        if (listContent != null) {
            listContent.showActionDividers(z);
        }
    }

    @Deprecated
    public void showActionDividers(boolean z) {
        setShowActionDividers(z);
    }

    public void setShowActionRow(boolean z) {
        this.mShowActions = z;
        updateActions();
    }

    public boolean isShowingActionRow() {
        return this.mShowActions;
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
        sliceChildView.setStyle(sliceStyle, sliceStyle.getRowStyle((SliceItem) null));
        this.mCurrentView.setTint(getTintColor());
        ListContent listContent = this.mListContent;
        if (listContent == null || listContent.getLayoutDir() == -1) {
            this.mCurrentView.setLayoutDirection(2);
        } else {
            this.mCurrentView.setLayoutDirection(this.mListContent.getLayoutDir());
        }
    }

    private void updateActions() {
        if (this.mActions == null) {
            this.mActionRow.setVisibility(8);
            this.mCurrentView.setSliceActions((List<SliceAction>) null);
            this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            return;
        }
        ArrayList arrayList = new ArrayList(this.mActions);
        Collections.sort(arrayList, SLICE_ACTION_PRIORITY_COMPARATOR);
        if (!this.mShowActions || getMode() == 3 || this.mActions.size() < 2) {
            this.mCurrentView.setSliceActions(arrayList);
            this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            this.mActionRow.setVisibility(8);
            return;
        }
        this.mActionRow.setActions(arrayList, getTintColor());
        this.mActionRow.setVisibility(0);
        this.mCurrentView.setSliceActions((List<SliceAction>) null);
        this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), 0);
        this.mActionRow.setPaddingRelative(getPaddingStart(), 0, getPaddingEnd(), getPaddingBottom());
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

    private ViewGroup.LayoutParams getChildLp(View view) {
        if (!(view instanceof ShortcutView)) {
            return new ViewGroup.LayoutParams(-1, -1);
        }
        int i = this.mShortcutSize;
        return new ViewGroup.LayoutParams(i, i);
    }

    public static String modeToString(int i) {
        if (i == 1) {
            return "MODE SMALL";
        }
        if (i != 2) {
            return i != 3 ? "unknown mode: " + i : "MODE SHORTCUT";
        }
        return "MODE LARGE";
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isShown()) {
            logSliceMetricsVisibilityChange(true);
            refreshLastUpdatedLabel(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        logSliceMetricsVisibilityChange(false);
        refreshLastUpdatedLabel(false);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (isAttachedToWindow()) {
            boolean z = true;
            logSliceMetricsVisibilityChange(i == 0);
            if (i != 0) {
                z = false;
            }
            refreshLastUpdatedLabel(z);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        boolean z = true;
        logSliceMetricsVisibilityChange(i == 0);
        if (i != 0) {
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
        if (slice2 == null || !slice2.getUri().equals(slice.getUri())) {
            logSliceMetricsVisibilityChange(false);
            this.mCurrentSliceMetrics = SliceMetrics.getInstance(getContext(), slice.getUri());
        }
    }

    private void logSliceMetricsVisibilityChange(boolean z) {
        SliceMetrics sliceMetrics = this.mCurrentSliceMetrics;
        if (sliceMetrics != null) {
            if (z && !this.mCurrentSliceLoggedVisible) {
                sliceMetrics.logVisible();
                this.mCurrentSliceLoggedVisible = true;
            }
            if (!z && this.mCurrentSliceLoggedVisible) {
                this.mCurrentSliceMetrics.logHidden();
                this.mCurrentSliceLoggedVisible = false;
            }
        }
    }

    private void logSliceMetricsOnTouch(SliceItem sliceItem, EventInfo eventInfo) {
        if (this.mCurrentSliceMetrics != null && sliceItem.getSlice() != null && sliceItem.getSlice().getUri() != null) {
            this.mCurrentSliceMetrics.logTouch(eventInfo.actionType, sliceItem.getSlice().getUri());
        }
    }

    private void refreshLastUpdatedLabel(boolean z) {
        SliceMetadata sliceMetadata;
        if (this.mShowLastUpdated && (sliceMetadata = this.mSliceMetadata) != null && !sliceMetadata.neverExpires()) {
            if (z) {
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
}
