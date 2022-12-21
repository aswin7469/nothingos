package androidx.leanback.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.RecyclerView;

@Deprecated
public class DetailsOverviewRowPresenter extends RowPresenter {
    static final boolean DEBUG = false;
    private static final long DEFAULT_TIMEOUT = 5000;
    static final String TAG = "DetailsOverviewRowP";
    OnActionClickedListener mActionClickedListener;
    private int mBackgroundColor = 0;
    private boolean mBackgroundColorSet;
    final Presenter mDetailsPresenter;
    private boolean mIsStyleLarge = true;
    private DetailsOverviewSharedElementHelper mSharedElementHelper;

    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    class ActionsItemBridgeAdapter extends ItemBridgeAdapter {
        ViewHolder mViewHolder;

        ActionsItemBridgeAdapter(ViewHolder viewHolder) {
            this.mViewHolder = viewHolder;
        }

        public void onBind(final ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mViewHolder.getOnItemViewClickedListener() != null || DetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                viewHolder.getPresenter().setOnClickListener(viewHolder.getViewHolder(), new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ActionsItemBridgeAdapter.this.mViewHolder.getOnItemViewClickedListener() != null) {
                            ActionsItemBridgeAdapter.this.mViewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder.getViewHolder(), viewHolder.getItem(), ActionsItemBridgeAdapter.this.mViewHolder, ActionsItemBridgeAdapter.this.mViewHolder.getRow());
                        }
                        if (DetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                            DetailsOverviewRowPresenter.this.mActionClickedListener.onActionClicked((Action) viewHolder.getItem());
                        }
                    }
                });
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mViewHolder.getOnItemViewClickedListener() != null || DetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                viewHolder.getPresenter().setOnClickListener(viewHolder.getViewHolder(), (View.OnClickListener) null);
            }
        }

        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
            viewHolder.itemView.addOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
        }

        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
            this.mViewHolder.checkFirstAndLastPosition(false);
        }
    }

    public final class ViewHolder extends RowPresenter.ViewHolder {
        ItemBridgeAdapter mActionBridgeAdapter;
        final HorizontalGridView mActionsRow;
        final OnChildSelectedListener mChildSelectedListener;
        final FrameLayout mDetailsDescriptionFrame;
        public final Presenter.ViewHolder mDetailsDescriptionViewHolder;
        final Handler mHandler = new Handler();
        final ImageView mImageView;
        final View.OnLayoutChangeListener mLayoutChangeListener = new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                ViewHolder.this.checkFirstAndLastPosition(false);
            }
        };
        final DetailsOverviewRow.Listener mListener = new DetailsOverviewRow.Listener() {
            public void onImageDrawableChanged(DetailsOverviewRow detailsOverviewRow) {
                ViewHolder.this.mHandler.removeCallbacks(ViewHolder.this.mUpdateDrawableCallback);
                ViewHolder.this.mHandler.post(ViewHolder.this.mUpdateDrawableCallback);
            }

            public void onItemChanged(DetailsOverviewRow detailsOverviewRow) {
                if (ViewHolder.this.mDetailsDescriptionViewHolder != null) {
                    DetailsOverviewRowPresenter.this.mDetailsPresenter.onUnbindViewHolder(ViewHolder.this.mDetailsDescriptionViewHolder);
                }
                DetailsOverviewRowPresenter.this.mDetailsPresenter.onBindViewHolder(ViewHolder.this.mDetailsDescriptionViewHolder, detailsOverviewRow.getItem());
            }

            public void onActionsAdapterChanged(DetailsOverviewRow detailsOverviewRow) {
                ViewHolder.this.bindActions(detailsOverviewRow.getActionsAdapter());
            }
        };
        int mNumItems;
        final FrameLayout mOverviewFrame;
        final ViewGroup mOverviewView;
        final ViewGroup mRightPanel;
        final RecyclerView.OnScrollListener mScrollListener;
        boolean mShowMoreLeft;
        boolean mShowMoreRight;
        final Runnable mUpdateDrawableCallback = new Runnable() {
            public void run() {
                DetailsOverviewRowPresenter.this.bindImageDrawable(ViewHolder.this);
            }
        };

        /* access modifiers changed from: package-private */
        public void bindActions(ObjectAdapter objectAdapter) {
            this.mActionBridgeAdapter.setAdapter(objectAdapter);
            this.mActionsRow.setAdapter(this.mActionBridgeAdapter);
            this.mNumItems = this.mActionBridgeAdapter.getItemCount();
            this.mShowMoreRight = false;
            this.mShowMoreLeft = true;
            showMoreLeft(false);
        }

        /* access modifiers changed from: package-private */
        public void unbindActions() {
            this.mActionsRow.setAdapter((RecyclerView.Adapter) null);
            this.mActionBridgeAdapter.setAdapter((ObjectAdapter) null);
            this.mNumItems = 0;
        }

        /* access modifiers changed from: package-private */
        public void dispatchItemSelection(View view) {
            RecyclerView.ViewHolder viewHolder;
            if (isSelected()) {
                if (view != null) {
                    viewHolder = this.mActionsRow.getChildViewHolder(view);
                } else {
                    HorizontalGridView horizontalGridView = this.mActionsRow;
                    viewHolder = horizontalGridView.findViewHolderForPosition(horizontalGridView.getSelectedPosition());
                }
                ItemBridgeAdapter.ViewHolder viewHolder2 = (ItemBridgeAdapter.ViewHolder) viewHolder;
                if (viewHolder2 == null) {
                    if (getOnItemViewSelectedListener() != null) {
                        getOnItemViewSelectedListener().onItemSelected((Presenter.ViewHolder) null, (Object) null, this, getRow());
                    }
                } else if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(viewHolder2.getViewHolder(), viewHolder2.getItem(), this, getRow());
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void checkFirstAndLastPosition(boolean z) {
            boolean z2 = true;
            RecyclerView.ViewHolder findViewHolderForPosition = this.mActionsRow.findViewHolderForPosition(this.mNumItems - 1);
            boolean z3 = findViewHolderForPosition == null || findViewHolderForPosition.itemView.getRight() > this.mActionsRow.getWidth();
            RecyclerView.ViewHolder findViewHolderForPosition2 = this.mActionsRow.findViewHolderForPosition(0);
            if (findViewHolderForPosition2 != null && findViewHolderForPosition2.itemView.getLeft() >= 0) {
                z2 = false;
            }
            showMoreRight(z3);
            showMoreLeft(z2);
        }

        private void showMoreLeft(boolean z) {
            if (z != this.mShowMoreLeft) {
                this.mActionsRow.setFadingLeftEdge(z);
                this.mShowMoreLeft = z;
            }
        }

        private void showMoreRight(boolean z) {
            if (z != this.mShowMoreRight) {
                this.mActionsRow.setFadingRightEdge(z);
                this.mShowMoreRight = z;
            }
        }

        public ViewHolder(View view, Presenter presenter) {
            super(view);
            C09864 r5 = new OnChildSelectedListener() {
                public void onChildSelected(ViewGroup viewGroup, View view, int i, long j) {
                    ViewHolder.this.dispatchItemSelection(view);
                }
            };
            this.mChildSelectedListener = r5;
            C09875 r0 = new RecyclerView.OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                }

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    ViewHolder.this.checkFirstAndLastPosition(true);
                }
            };
            this.mScrollListener = r0;
            this.mOverviewFrame = (FrameLayout) view.findViewById(C0742R.C0745id.details_frame);
            this.mOverviewView = (ViewGroup) view.findViewById(C0742R.C0745id.details_overview);
            this.mImageView = (ImageView) view.findViewById(C0742R.C0745id.details_overview_image);
            ViewGroup viewGroup = (ViewGroup) view.findViewById(C0742R.C0745id.details_overview_right_panel);
            this.mRightPanel = viewGroup;
            FrameLayout frameLayout = (FrameLayout) viewGroup.findViewById(C0742R.C0745id.details_overview_description);
            this.mDetailsDescriptionFrame = frameLayout;
            HorizontalGridView horizontalGridView = (HorizontalGridView) viewGroup.findViewById(C0742R.C0745id.details_overview_actions);
            this.mActionsRow = horizontalGridView;
            horizontalGridView.setHasOverlappingRendering(false);
            horizontalGridView.setOnScrollListener(r0);
            horizontalGridView.setAdapter(this.mActionBridgeAdapter);
            horizontalGridView.setOnChildSelectedListener(r5);
            int dimensionPixelSize = view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_overview_actions_fade_size);
            horizontalGridView.setFadingRightEdgeLength(dimensionPixelSize);
            horizontalGridView.setFadingLeftEdgeLength(dimensionPixelSize);
            Presenter.ViewHolder onCreateViewHolder = presenter.onCreateViewHolder(frameLayout);
            this.mDetailsDescriptionViewHolder = onCreateViewHolder;
            frameLayout.addView(onCreateViewHolder.view);
        }
    }

    public DetailsOverviewRowPresenter(Presenter presenter) {
        setHeaderPresenter((RowHeaderPresenter) null);
        setSelectEffectEnabled(false);
        this.mDetailsPresenter = presenter;
    }

    public void setOnActionClickedListener(OnActionClickedListener onActionClickedListener) {
        this.mActionClickedListener = onActionClickedListener;
    }

    public OnActionClickedListener getOnActionClickedListener() {
        return this.mActionClickedListener;
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        this.mBackgroundColorSet = true;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setStyleLarge(boolean z) {
        this.mIsStyleLarge = z;
    }

    public boolean isStyleLarge() {
        return this.mIsStyleLarge;
    }

    public final void setSharedElementEnterTransition(Activity activity, String str, long j) {
        if (this.mSharedElementHelper == null) {
            this.mSharedElementHelper = new DetailsOverviewSharedElementHelper();
        }
        this.mSharedElementHelper.setSharedElementEnterTransition(activity, str, j);
    }

    public final void setSharedElementEnterTransition(Activity activity, String str) {
        setSharedElementEnterTransition(activity, str, 5000);
    }

    private int getDefaultBackgroundColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.defaultBrandColor, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return context.getResources().getColor(C0742R.C0743color.lb_default_brand_color);
    }

    /* access modifiers changed from: protected */
    public void onRowViewSelected(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.onRowViewSelected(viewHolder, z);
        if (z) {
            ((ViewHolder) viewHolder).dispatchItemSelection((View) null);
        }
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_details_overview, viewGroup, false), this.mDetailsPresenter);
        initDetailsOverview(viewHolder);
        return viewHolder;
    }

    private int getCardHeight(Context context) {
        int i;
        if (this.mIsStyleLarge) {
            i = C0742R.dimen.lb_details_overview_height_large;
        } else {
            i = C0742R.dimen.lb_details_overview_height_small;
        }
        return context.getResources().getDimensionPixelSize(i);
    }

    private void initDetailsOverview(final ViewHolder viewHolder) {
        viewHolder.mActionBridgeAdapter = new ActionsItemBridgeAdapter(viewHolder);
        FrameLayout frameLayout = viewHolder.mOverviewFrame;
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.height = getCardHeight(frameLayout.getContext());
        frameLayout.setLayoutParams(layoutParams);
        if (!getSelectEffectEnabled()) {
            viewHolder.mOverviewFrame.setForeground((Drawable) null);
        }
        viewHolder.mActionsRow.setOnUnhandledKeyListener(new BaseGridView.OnUnhandledKeyListener() {
            public boolean onUnhandledKey(KeyEvent keyEvent) {
                return viewHolder.getOnKeyListener() != null && viewHolder.getOnKeyListener().onKey(viewHolder.view, keyEvent.getKeyCode(), keyEvent);
            }
        });
    }

    private static int getNonNegativeWidth(Drawable drawable) {
        int intrinsicWidth = drawable == null ? 0 : drawable.getIntrinsicWidth();
        if (intrinsicWidth > 0) {
            return intrinsicWidth;
        }
        return 0;
    }

    private static int getNonNegativeHeight(Drawable drawable) {
        int intrinsicHeight = drawable == null ? 0 : drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            return intrinsicHeight;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindImageDrawable(androidx.leanback.widget.DetailsOverviewRowPresenter.ViewHolder r14) {
        /*
            r13 = this;
            androidx.leanback.widget.Row r0 = r14.getRow()
            androidx.leanback.widget.DetailsOverviewRow r0 = (androidx.leanback.widget.DetailsOverviewRow) r0
            android.widget.ImageView r1 = r14.mImageView
            android.view.ViewGroup$LayoutParams r1 = r1.getLayoutParams()
            android.view.ViewGroup$MarginLayoutParams r1 = (android.view.ViewGroup.MarginLayoutParams) r1
            android.widget.ImageView r2 = r14.mImageView
            android.content.Context r2 = r2.getContext()
            int r2 = r13.getCardHeight(r2)
            android.widget.ImageView r3 = r14.mImageView
            android.content.res.Resources r3 = r3.getResources()
            int r4 = androidx.leanback.C0742R.dimen.lb_details_overview_image_margin_vertical
            int r3 = r3.getDimensionPixelSize(r4)
            android.widget.ImageView r4 = r14.mImageView
            android.content.res.Resources r4 = r4.getResources()
            int r5 = androidx.leanback.C0742R.dimen.lb_details_overview_image_margin_horizontal
            int r4 = r4.getDimensionPixelSize(r5)
            android.graphics.drawable.Drawable r5 = r0.getImageDrawable()
            int r5 = getNonNegativeWidth(r5)
            android.graphics.drawable.Drawable r6 = r0.getImageDrawable()
            int r6 = getNonNegativeHeight(r6)
            boolean r7 = r0.isImageScaleUpAllowed()
            android.graphics.drawable.Drawable r8 = r0.getImageDrawable()
            r9 = 1
            r10 = 0
            if (r8 == 0) goto L_0x007a
            if (r5 <= r6) goto L_0x0057
            boolean r8 = r13.mIsStyleLarge
            if (r8 == 0) goto L_0x0054
            r8 = r9
            goto L_0x0058
        L_0x0054:
            r8 = r9
            r11 = r10
            goto L_0x0059
        L_0x0057:
            r8 = r10
        L_0x0058:
            r11 = r8
        L_0x0059:
            if (r8 == 0) goto L_0x005d
            if (r5 > r2) goto L_0x0061
        L_0x005d:
            if (r8 != 0) goto L_0x0062
            if (r6 <= r2) goto L_0x0062
        L_0x0061:
            r7 = r9
        L_0x0062:
            if (r7 != 0) goto L_0x0065
            r11 = r9
        L_0x0065:
            if (r11 == 0) goto L_0x007b
            if (r7 != 0) goto L_0x007b
            if (r8 == 0) goto L_0x0071
            int r12 = r2 - r4
            if (r5 <= r12) goto L_0x0071
        L_0x006f:
            r7 = r9
            goto L_0x007b
        L_0x0071:
            if (r8 != 0) goto L_0x007b
            int r8 = r3 * 2
            int r8 = r2 - r8
            if (r6 <= r8) goto L_0x007b
            goto L_0x006f
        L_0x007a:
            r11 = r10
        L_0x007b:
            boolean r6 = r13.mBackgroundColorSet
            if (r6 == 0) goto L_0x0082
            int r6 = r13.mBackgroundColor
            goto L_0x008c
        L_0x0082:
            android.view.ViewGroup r6 = r14.mOverviewView
            android.content.Context r6 = r6.getContext()
            int r6 = r13.getDefaultBackgroundColor(r6)
        L_0x008c:
            r8 = 0
            if (r11 == 0) goto L_0x00a6
            r1.setMarginStart(r4)
            r1.bottomMargin = r3
            r1.topMargin = r3
            android.widget.FrameLayout r3 = r14.mOverviewFrame
            r3.setBackgroundColor(r6)
            android.view.ViewGroup r3 = r14.mRightPanel
            r3.setBackground(r8)
            android.widget.ImageView r3 = r14.mImageView
            r3.setBackground(r8)
            goto L_0x00bb
        L_0x00a6:
            r1.bottomMargin = r10
            r1.topMargin = r10
            r1.leftMargin = r10
            android.view.ViewGroup r3 = r14.mRightPanel
            r3.setBackgroundColor(r6)
            android.widget.ImageView r3 = r14.mImageView
            r3.setBackgroundColor(r6)
            android.widget.FrameLayout r3 = r14.mOverviewFrame
            r3.setBackground(r8)
        L_0x00bb:
            android.widget.FrameLayout r3 = r14.mOverviewFrame
            androidx.leanback.widget.RoundedRectHelper.setClipToRoundedOutline(r3, r9)
            r3 = -2
            if (r7 == 0) goto L_0x00da
            android.widget.ImageView r4 = r14.mImageView
            android.widget.ImageView$ScaleType r5 = android.widget.ImageView.ScaleType.FIT_START
            r4.setScaleType(r5)
            android.widget.ImageView r4 = r14.mImageView
            r4.setAdjustViewBounds(r9)
            android.widget.ImageView r4 = r14.mImageView
            r4.setMaxWidth(r2)
            r2 = -1
            r1.height = r2
            r1.width = r3
            goto L_0x00ee
        L_0x00da:
            android.widget.ImageView r4 = r14.mImageView
            android.widget.ImageView$ScaleType r6 = android.widget.ImageView.ScaleType.CENTER
            r4.setScaleType(r6)
            android.widget.ImageView r4 = r14.mImageView
            r4.setAdjustViewBounds(r10)
            r1.height = r3
            int r2 = java.lang.Math.min((int) r2, (int) r5)
            r1.width = r2
        L_0x00ee:
            android.widget.ImageView r2 = r14.mImageView
            r2.setLayoutParams(r1)
            android.widget.ImageView r1 = r14.mImageView
            android.graphics.drawable.Drawable r2 = r0.getImageDrawable()
            r1.setImageDrawable(r2)
            android.graphics.drawable.Drawable r0 = r0.getImageDrawable()
            if (r0 == 0) goto L_0x0109
            androidx.leanback.widget.DetailsOverviewSharedElementHelper r13 = r13.mSharedElementHelper
            if (r13 == 0) goto L_0x0109
            r13.onBindToDrawable(r14)
        L_0x0109:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.DetailsOverviewRowPresenter.bindImageDrawable(androidx.leanback.widget.DetailsOverviewRowPresenter$ViewHolder):void");
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        DetailsOverviewRow detailsOverviewRow = (DetailsOverviewRow) obj;
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        bindImageDrawable(viewHolder2);
        this.mDetailsPresenter.onBindViewHolder(viewHolder2.mDetailsDescriptionViewHolder, detailsOverviewRow.getItem());
        viewHolder2.bindActions(detailsOverviewRow.getActionsAdapter());
        detailsOverviewRow.addListener(viewHolder2.mListener);
    }

    /* access modifiers changed from: protected */
    public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        ((DetailsOverviewRow) viewHolder2.getRow()).removeListener(viewHolder2.mListener);
        if (viewHolder2.mDetailsDescriptionViewHolder != null) {
            this.mDetailsPresenter.onUnbindViewHolder(viewHolder2.mDetailsDescriptionViewHolder);
        }
        viewHolder2.unbindActions();
        super.onUnbindRowViewHolder(viewHolder);
    }

    /* access modifiers changed from: protected */
    public void onSelectLevelChanged(RowPresenter.ViewHolder viewHolder) {
        super.onSelectLevelChanged(viewHolder);
        if (getSelectEffectEnabled()) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            ((ColorDrawable) viewHolder2.mOverviewFrame.getForeground().mutate()).setColor(viewHolder2.mColorDimmer.getPaint().getColor());
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewAttachedToWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewAttachedToWindow(viewHolder);
        Presenter presenter = this.mDetailsPresenter;
        if (presenter != null) {
            presenter.onViewAttachedToWindow(((ViewHolder) viewHolder).mDetailsDescriptionViewHolder);
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewDetachedFromWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewDetachedFromWindow(viewHolder);
        Presenter presenter = this.mDetailsPresenter;
        if (presenter != null) {
            presenter.onViewDetachedFromWindow(((ViewHolder) viewHolder).mDetailsDescriptionViewHolder);
        }
    }
}
