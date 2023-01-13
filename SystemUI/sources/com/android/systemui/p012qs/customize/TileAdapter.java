package com.android.systemui.p012qs.customize;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.R;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.QSEditEvent;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.customize.TileQueryHelper;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.dagger.QSThemedContext;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.tileimpl.QSIconViewImpl;
import com.android.systemui.p012qs.tileimpl.QSTileViewImpl;
import com.nothing.systemui.p024qs.QSTileHostEx;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@QSScope
/* renamed from: com.android.systemui.qs.customize.TileAdapter */
public class TileAdapter extends RecyclerView.Adapter<Holder> implements TileQueryHelper.TileStateListener {
    private static final int ACTION_ADD = 1;
    private static final int ACTION_MOVE = 2;
    private static final int ACTION_NONE = 0;
    private static final long DIVIDER_ID = 20000;
    private static final long DRAG_LENGTH = 100;
    private static final float DRAG_SCALE = 1.2f;
    private static final long EDIT_ID = 10000;
    public static final long MOVE_DURATION = 150;
    private static final int NUM_COLUMNS_ID = 2131493040;
    private static final int TYPE_ACCESSIBLE_DROP = 2;
    private static final int TYPE_DIVIDER = 4;
    private static final int TYPE_EDIT = 1;
    private static final int TYPE_HEADER = 3;
    private static final int TYPE_TILE = 0;
    /* access modifiers changed from: private */
    public int mAccessibilityAction = 0;
    /* access modifiers changed from: private */
    public final AccessibilityDelegateCompat mAccessibilityDelegate;
    private int mAccessibilityFromIndex;
    private List<TileQueryHelper.TileInfo> mAllTiles;
    private final ItemTouchHelper.Callback mCallbacks;
    private final Context mContext;
    /* access modifiers changed from: private */
    public Holder mCurrentDrag;
    private List<String> mCurrentSpecs;
    private RecyclerView.ItemDecoration mDecoration;
    /* access modifiers changed from: private */
    public int mEditIndex;
    private int mFocusIndex;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private final QSTileHost mHost;
    private final ItemTouchHelper mItemTouchHelper;
    private final MarginTileDecoration mMarginDecoration;
    private final int mMinNumTiles;
    private boolean mNeedsFocus;
    /* access modifiers changed from: private */
    public int mNumColumns;
    private List<TileQueryHelper.TileInfo> mOtherTiles;
    private RecyclerView mRecyclerView;
    private final GridLayoutManager.SpanSizeLookup mSizeLookup;
    private int mTileDividerIndex;
    /* access modifiers changed from: private */
    public final List<TileQueryHelper.TileInfo> mTiles = new ArrayList();
    private final UiEventLogger mUiEventLogger;

    @Inject
    public TileAdapter(@QSThemedContext Context context, QSTileHost qSTileHost, UiEventLogger uiEventLogger) {
        C23694 r0 = new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                int itemViewType = TileAdapter.this.getItemViewType(i);
                if (itemViewType == 1 || itemViewType == 4 || itemViewType == 3) {
                    return TileAdapter.this.mNumColumns;
                }
                return 1;
            }
        };
        this.mSizeLookup = r0;
        C23705 r1 = new ItemTouchHelper.Callback() {
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            public boolean isLongPressDragEnabled() {
                return true;
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            }

            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
                super.onSelectedChanged(viewHolder, i);
                if (i != 2) {
                    viewHolder = null;
                }
                if (viewHolder != TileAdapter.this.mCurrentDrag) {
                    if (TileAdapter.this.mCurrentDrag != null) {
                        int adapterPosition = TileAdapter.this.mCurrentDrag.getAdapterPosition();
                        if (adapterPosition != -1) {
                            ((CustomizeTileView) TileAdapter.this.mCurrentDrag.mTileView).setShowAppLabel(adapterPosition > TileAdapter.this.mEditIndex && !((TileQueryHelper.TileInfo) TileAdapter.this.mTiles.get(adapterPosition)).isSystem);
                            TileAdapter.this.mCurrentDrag.stopDrag();
                            Holder unused = TileAdapter.this.mCurrentDrag = null;
                        } else {
                            return;
                        }
                    }
                    if (viewHolder != null) {
                        Holder unused2 = TileAdapter.this.mCurrentDrag = (Holder) viewHolder;
                        TileAdapter.this.mCurrentDrag.startDrag();
                    }
                    TileAdapter.this.mHandler.post(new Runnable() {
                        public void run() {
                            TileAdapter.this.notifyItemChanged(TileAdapter.this.mEditIndex);
                        }
                    });
                }
            }

            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                int adapterPosition = viewHolder2.getAdapterPosition();
                if (adapterPosition == 0 || adapterPosition == -1) {
                    return false;
                }
                if (TileAdapter.this.canRemoveTiles() || viewHolder.getAdapterPosition() >= TileAdapter.this.mEditIndex) {
                    if (adapterPosition <= TileAdapter.this.mEditIndex + 1) {
                        return true;
                    }
                    return false;
                } else if (adapterPosition < TileAdapter.this.mEditIndex) {
                    return true;
                } else {
                    return false;
                }
            }

            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType == 1 || itemViewType == 3 || itemViewType == 4) {
                    return makeMovementFlags(0, 0);
                }
                return makeMovementFlags(15, 0);
            }

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                if (adapterPosition == 0 || adapterPosition == -1 || adapterPosition2 == 0 || adapterPosition2 == -1) {
                    return false;
                }
                return TileAdapter.this.move(adapterPosition, adapterPosition2);
            }

            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                ((Holder) viewHolder).stopDrag();
                super.clearView(recyclerView, viewHolder);
            }
        };
        this.mCallbacks = r1;
        this.mContext = context;
        this.mHost = qSTileHost;
        this.mUiEventLogger = uiEventLogger;
        this.mItemTouchHelper = new ItemTouchHelper(r1);
        this.mDecoration = new TileItemDecoration(context);
        this.mMarginDecoration = new MarginTileDecoration();
        this.mMinNumTiles = context.getResources().getInteger(C1894R.integer.quick_settings_min_num_tiles);
        this.mNumColumns = context.getResources().getInteger(C1894R.integer.quick_settings_num_columns);
        this.mAccessibilityDelegate = new TileAdapterDelegate();
        r0.setSpanIndexCacheEnabled(true);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = null;
    }

    public boolean updateNumColumns() {
        int integer = this.mContext.getResources().getInteger(C1894R.integer.quick_settings_num_columns);
        if (integer == this.mNumColumns) {
            return false;
        }
        this.mNumColumns = integer;
        return true;
    }

    public int getNumColumns() {
        return this.mNumColumns;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return this.mItemTouchHelper;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return this.mDecoration;
    }

    public RecyclerView.ItemDecoration getMarginItemDecoration() {
        return this.mMarginDecoration;
    }

    public void changeHalfMargin(int i) {
        this.mMarginDecoration.setHalfMargin(i);
    }

    public void saveSpecs(QSTileHost qSTileHost) {
        ArrayList arrayList = new ArrayList();
        clearAccessibilityState();
        int i = 1;
        while (i < this.mTiles.size() && this.mTiles.get(i) != null) {
            arrayList.add(this.mTiles.get(i).spec);
            i++;
        }
        qSTileHost.changeTiles(this.mCurrentSpecs, arrayList);
        this.mCurrentSpecs = arrayList;
    }

    private void clearAccessibilityState() {
        this.mNeedsFocus = false;
        if (this.mAccessibilityAction == 1) {
            List<TileQueryHelper.TileInfo> list = this.mTiles;
            int i = this.mEditIndex - 1;
            this.mEditIndex = i;
            list.remove(i);
            notifyDataSetChanged();
        }
        this.mAccessibilityAction = 0;
    }

    public void resetTileSpecs(List<String> list) {
        this.mHost.changeTiles(this.mCurrentSpecs, list);
        setTileSpecs(list);
    }

    public void setTileSpecs(List<String> list) {
        List<String> removeCircleForSpecs = QSTileHostEx.removeCircleForSpecs(list);
        if (!removeCircleForSpecs.equals(this.mCurrentSpecs)) {
            this.mCurrentSpecs = removeCircleForSpecs;
            recalcSpecs();
        }
    }

    public void onTilesChanged(List<TileQueryHelper.TileInfo> list) {
        this.mAllTiles = QSTileHostEx.removeCircleForTileInfo(list);
        recalcSpecs();
    }

    private void recalcSpecs() {
        if (this.mCurrentSpecs != null && this.mAllTiles != null) {
            this.mOtherTiles = new ArrayList(this.mAllTiles);
            this.mTiles.clear();
            this.mTiles.add(null);
            int i = 0;
            for (int i2 = 0; i2 < this.mCurrentSpecs.size(); i2++) {
                TileQueryHelper.TileInfo andRemoveOther = getAndRemoveOther(this.mCurrentSpecs.get(i2));
                if (andRemoveOther != null) {
                    this.mTiles.add(andRemoveOther);
                }
            }
            this.mTiles.add(null);
            while (i < this.mOtherTiles.size()) {
                TileQueryHelper.TileInfo tileInfo = this.mOtherTiles.get(i);
                if (tileInfo.isSystem) {
                    this.mOtherTiles.remove(i);
                    this.mTiles.add(tileInfo);
                    i--;
                }
                i++;
            }
            this.mTileDividerIndex = this.mTiles.size();
            this.mTiles.add(null);
            this.mTiles.addAll(this.mOtherTiles);
            updateDividerLocations();
            notifyDataSetChanged();
        }
    }

    private TileQueryHelper.TileInfo getAndRemoveOther(String str) {
        for (int i = 0; i < this.mOtherTiles.size(); i++) {
            if (this.mOtherTiles.get(i).spec.equals(str)) {
                return this.mOtherTiles.remove(i);
            }
        }
        return null;
    }

    public int getItemViewType(int i) {
        if (i == 0) {
            return 3;
        }
        if (this.mAccessibilityAction == 1 && i == this.mEditIndex - 1) {
            return 2;
        }
        if (i == this.mTileDividerIndex) {
            return 4;
        }
        if (this.mTiles.get(i) == null) {
            return 1;
        }
        return 0;
    }

    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater from = LayoutInflater.from(context);
        if (i == 3) {
            View inflate = from.inflate(C1894R.layout.qs_customize_header, viewGroup, false);
            inflate.setMinimumHeight(calculateHeaderMinHeight(context));
            return new Holder(inflate);
        } else if (i == 4) {
            return new Holder(from.inflate(C1894R.layout.qs_customize_tile_divider, viewGroup, false));
        } else {
            if (i == 1) {
                return new Holder(from.inflate(C1894R.layout.qs_customize_divider, viewGroup, false));
            }
            FrameLayout frameLayout = (FrameLayout) from.inflate(C1894R.layout.qs_customize_tile_frame, viewGroup, false);
            frameLayout.addView(new CustomizeTileView(context, new QSIconViewImpl(context)));
            return new Holder(frameLayout);
        }
    }

    public int getItemCount() {
        return this.mTiles.size();
    }

    public boolean onFailedToRecycleView(Holder holder) {
        holder.stopDrag();
        holder.clearDrag();
        return true;
    }

    private void setSelectableForHeaders(View view) {
        int i = 1;
        boolean z = this.mAccessibilityAction == 0;
        view.setFocusable(z);
        if (!z) {
            i = 4;
        }
        view.setImportantForAccessibility(i);
        view.setFocusableInTouchMode(z);
    }

    public void onBindViewHolder(final Holder holder, int i) {
        String str;
        if (holder.getItemViewType() == 3) {
            setSelectableForHeaders(holder.itemView);
            return;
        }
        int i2 = 4;
        boolean z = false;
        if (holder.getItemViewType() == 4) {
            View view = holder.itemView;
            if (this.mTileDividerIndex < this.mTiles.size() - 1) {
                i2 = 0;
            }
            view.setVisibility(i2);
        } else if (holder.getItemViewType() == 1) {
            Resources resources = this.mContext.getResources();
            if (this.mCurrentDrag == null) {
                str = resources.getString(C1894R.string.drag_to_add_tiles);
            } else if (canRemoveTiles() || this.mCurrentDrag.getAdapterPosition() >= this.mEditIndex) {
                str = resources.getString(C1894R.string.drag_to_remove_tiles);
            } else {
                str = resources.getString(C1894R.string.drag_to_remove_disabled, new Object[]{Integer.valueOf(this.mMinNumTiles)});
            }
            ((TextView) holder.itemView.findViewById(16908310)).setText(str);
            setSelectableForHeaders(holder.itemView);
        } else if (holder.getItemViewType() == 2) {
            holder.mTileView.setClickable(true);
            holder.mTileView.setFocusable(true);
            holder.mTileView.setFocusableInTouchMode(true);
            holder.mTileView.setVisibility(0);
            holder.mTileView.setImportantForAccessibility(1);
            holder.mTileView.setContentDescription(this.mContext.getString(C1894R.string.accessibility_qs_edit_tile_add_to_position, new Object[]{Integer.valueOf(i)}));
            holder.mTileView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TileAdapter.this.selectPosition(holder.getLayoutPosition());
                }
            });
            focusOnHolder(holder);
        } else {
            TileQueryHelper.TileInfo tileInfo = this.mTiles.get(i);
            boolean z2 = i > 0 && i < this.mEditIndex;
            if (z2 && this.mAccessibilityAction == 1) {
                tileInfo.state.contentDescription = this.mContext.getString(C1894R.string.accessibility_qs_edit_tile_add_to_position, new Object[]{Integer.valueOf(i)});
            } else if (!z2 || this.mAccessibilityAction != 2) {
                tileInfo.state.contentDescription = tileInfo.state.label;
            } else {
                tileInfo.state.contentDescription = this.mContext.getString(C1894R.string.accessibility_qs_edit_tile_move_to_position, new Object[]{Integer.valueOf(i)});
            }
            tileInfo.state.expandedAccessibilityClassName = "";
            CustomizeTileView customizeTileView = (CustomizeTileView) Objects.requireNonNull(holder.getTileAsCustomizeView(), "The holder must have a tileView");
            customizeTileView.changeState(tileInfo.state);
            customizeTileView.setShowAppLabel(i > this.mEditIndex && !tileInfo.isSystem);
            if (i < this.mEditIndex || tileInfo.isSystem) {
                z = true;
            }
            customizeTileView.setShowSideView(z);
            holder.mTileView.setSelected(true);
            holder.mTileView.setImportantForAccessibility(1);
            holder.mTileView.setClickable(true);
            holder.mTileView.setOnClickListener((View.OnClickListener) null);
            holder.mTileView.setFocusable(true);
            holder.mTileView.setFocusableInTouchMode(true);
            if (this.mAccessibilityAction != 0) {
                holder.mTileView.setClickable(z2);
                holder.mTileView.setFocusable(z2);
                holder.mTileView.setFocusableInTouchMode(z2);
                QSTileViewImpl access$200 = holder.mTileView;
                if (z2) {
                    i2 = 1;
                }
                access$200.setImportantForAccessibility(i2);
                if (z2) {
                    holder.mTileView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            int layoutPosition = holder.getLayoutPosition();
                            if (layoutPosition != -1 && TileAdapter.this.mAccessibilityAction != 0) {
                                TileAdapter.this.selectPosition(layoutPosition);
                            }
                        }
                    });
                }
            }
            if (i == this.mFocusIndex) {
                focusOnHolder(holder);
            }
        }
    }

    private void focusOnHolder(final Holder holder) {
        if (this.mNeedsFocus) {
            holder.mTileView.requestLayout();
            holder.mTileView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    holder.mTileView.removeOnLayoutChangeListener(this);
                    holder.mTileView.requestAccessibilityFocus();
                }
            });
            this.mNeedsFocus = false;
            this.mFocusIndex = -1;
        }
    }

    /* access modifiers changed from: private */
    public boolean canRemoveTiles() {
        return this.mCurrentSpecs.size() > this.mMinNumTiles;
    }

    /* access modifiers changed from: private */
    public void selectPosition(int i) {
        if (this.mAccessibilityAction == 1) {
            List<TileQueryHelper.TileInfo> list = this.mTiles;
            int i2 = this.mEditIndex;
            this.mEditIndex = i2 - 1;
            list.remove(i2);
        }
        this.mAccessibilityAction = 0;
        move(this.mAccessibilityFromIndex, i, false);
        this.mFocusIndex = i;
        this.mNeedsFocus = true;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void startAccessibleAdd(int i) {
        this.mAccessibilityFromIndex = i;
        this.mAccessibilityAction = 1;
        List<TileQueryHelper.TileInfo> list = this.mTiles;
        int i2 = this.mEditIndex;
        this.mEditIndex = i2 + 1;
        list.add(i2, null);
        this.mTileDividerIndex++;
        int i3 = this.mEditIndex - 1;
        this.mFocusIndex = i3;
        this.mNeedsFocus = true;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.post(new TileAdapter$$ExternalSyntheticLambda0(this, i3));
        }
        notifyDataSetChanged();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startAccessibleAdd$0$com-android-systemui-qs-customize-TileAdapter */
    public /* synthetic */ void mo36532xba7506ba(int i) {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(i);
        }
    }

    /* access modifiers changed from: private */
    public void startAccessibleMove(int i) {
        this.mAccessibilityFromIndex = i;
        this.mAccessibilityAction = 2;
        this.mFocusIndex = i;
        this.mNeedsFocus = true;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public boolean canRemoveFromPosition(int i) {
        return canRemoveTiles() && isCurrentTile(i);
    }

    /* access modifiers changed from: private */
    public boolean isCurrentTile(int i) {
        return i < this.mEditIndex;
    }

    /* access modifiers changed from: private */
    public boolean canAddFromPosition(int i) {
        return i > this.mEditIndex;
    }

    /* access modifiers changed from: private */
    public boolean addFromPosition(int i) {
        if (!canAddFromPosition(i)) {
            return false;
        }
        move(i, this.mEditIndex);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean removeFromPosition(int i) {
        if (!canRemoveFromPosition(i)) {
            return false;
        }
        move(i, this.mTiles.get(i).isSystem ? this.mEditIndex : this.mTileDividerIndex);
        return true;
    }

    public GridLayoutManager.SpanSizeLookup getSizeLookup() {
        return this.mSizeLookup;
    }

    /* access modifiers changed from: private */
    public boolean move(int i, int i2) {
        return move(i, i2, true);
    }

    private boolean move(int i, int i2, boolean z) {
        if (i2 == i) {
            return true;
        }
        move(i, i2, this.mTiles, z);
        updateDividerLocations();
        int i3 = this.mEditIndex;
        if (i2 >= i3) {
            this.mUiEventLogger.log(QSEditEvent.QS_EDIT_REMOVE, 0, strip(this.mTiles.get(i2)));
        } else if (i >= i3) {
            this.mUiEventLogger.log(QSEditEvent.QS_EDIT_ADD, 0, strip(this.mTiles.get(i2)));
        } else {
            this.mUiEventLogger.log(QSEditEvent.QS_EDIT_MOVE, 0, strip(this.mTiles.get(i2)));
        }
        saveSpecs(this.mHost);
        return true;
    }

    private void updateDividerLocations() {
        this.mEditIndex = -1;
        this.mTileDividerIndex = this.mTiles.size();
        for (int i = 1; i < this.mTiles.size(); i++) {
            if (this.mTiles.get(i) == null) {
                if (this.mEditIndex == -1) {
                    this.mEditIndex = i;
                } else {
                    this.mTileDividerIndex = i;
                }
            }
        }
        int size = this.mTiles.size() - 1;
        int i2 = this.mTileDividerIndex;
        if (size == i2) {
            notifyItemChanged(i2);
        }
    }

    private static String strip(TileQueryHelper.TileInfo tileInfo) {
        String str = tileInfo.spec;
        return str.startsWith(CustomTile.PREFIX) ? CustomTile.getComponentFromSpec(str).getPackageName() : str;
    }

    private <T> void move(int i, int i2, List<T> list, boolean z) {
        list.add(i2, list.remove(i));
        if (z) {
            notifyItemMoved(i, i2);
        }
    }

    /* renamed from: com.android.systemui.qs.customize.TileAdapter$Holder */
    public class Holder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public QSTileViewImpl mTileView;

        public Holder(View view) {
            super(view);
            if (view instanceof FrameLayout) {
                QSTileViewImpl qSTileViewImpl = (QSTileViewImpl) ((FrameLayout) view).getChildAt(0);
                this.mTileView = qSTileViewImpl;
                qSTileViewImpl.getIcon().disableAnimation();
                this.mTileView.setTag(this);
                ViewCompat.setAccessibilityDelegate(this.mTileView, TileAdapter.this.mAccessibilityDelegate);
            }
        }

        public CustomizeTileView getTileAsCustomizeView() {
            return (CustomizeTileView) this.mTileView;
        }

        public void clearDrag() {
            this.itemView.clearAnimation();
            this.itemView.setScaleX(1.0f);
            this.itemView.setScaleY(1.0f);
        }

        public void startDrag() {
            this.itemView.animate().setDuration(100).scaleX(TileAdapter.DRAG_SCALE).scaleY(TileAdapter.DRAG_SCALE);
        }

        public void stopDrag() {
            this.itemView.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f);
        }

        /* access modifiers changed from: package-private */
        public boolean canRemove() {
            return TileAdapter.this.canRemoveFromPosition(getLayoutPosition());
        }

        /* access modifiers changed from: package-private */
        public boolean canAdd() {
            return TileAdapter.this.canAddFromPosition(getLayoutPosition());
        }

        /* access modifiers changed from: package-private */
        public void toggleState() {
            if (canAdd()) {
                add();
            } else {
                remove();
            }
        }

        private void add() {
            if (TileAdapter.this.addFromPosition(getLayoutPosition())) {
                this.itemView.announceForAccessibility(this.itemView.getContext().getText(C1894R.string.accessibility_qs_edit_tile_added));
            }
        }

        private void remove() {
            if (TileAdapter.this.removeFromPosition(getLayoutPosition())) {
                this.itemView.announceForAccessibility(this.itemView.getContext().getText(C1894R.string.accessibility_qs_edit_tile_removed));
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isCurrentTile() {
            return TileAdapter.this.isCurrentTile(getLayoutPosition());
        }

        /* access modifiers changed from: package-private */
        public void startAccessibleAdd() {
            TileAdapter.this.startAccessibleAdd(getLayoutPosition());
        }

        /* access modifiers changed from: package-private */
        public void startAccessibleMove() {
            TileAdapter.this.startAccessibleMove(getLayoutPosition());
        }

        /* access modifiers changed from: package-private */
        public boolean canTakeAccessibleAction() {
            return TileAdapter.this.mAccessibilityAction == 0;
        }
    }

    /* renamed from: com.android.systemui.qs.customize.TileAdapter$TileItemDecoration */
    private class TileItemDecoration extends RecyclerView.ItemDecoration {
        private final Drawable mDrawable;

        private TileItemDecoration(Context context) {
            this.mDrawable = context.getDrawable(C1894R.C1896drawable.qs_customize_tile_decoration);
        }

        public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            super.onDraw(canvas, recyclerView, state);
            int childCount = recyclerView.getChildCount();
            int width = recyclerView.getWidth();
            int bottom = recyclerView.getBottom();
            int i = 0;
            while (i < childCount) {
                View childAt = recyclerView.getChildAt(i);
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childAt);
                if (childViewHolder == TileAdapter.this.mCurrentDrag || childViewHolder.getAdapterPosition() == 0 || (childViewHolder.getAdapterPosition() < TileAdapter.this.mEditIndex && !(childAt instanceof TextView))) {
                    i++;
                } else {
                    this.mDrawable.setBounds(0, childAt.getTop() + Math.round(ViewCompat.getTranslationY(childAt)), width, bottom);
                    this.mDrawable.draw(canvas);
                    return;
                }
            }
        }
    }

    /* renamed from: com.android.systemui.qs.customize.TileAdapter$MarginTileDecoration */
    private static class MarginTileDecoration extends RecyclerView.ItemDecoration {
        private int mHalfMargin;

        private MarginTileDecoration() {
        }

        public void setHalfMargin(int i) {
            this.mHalfMargin = i;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getLayoutManager() != null) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
                if (view instanceof TextView) {
                    super.getItemOffsets(rect, view, recyclerView, state);
                } else if (spanIndex != 0 && spanIndex != gridLayoutManager.getSpanCount() - 1) {
                    rect.left = this.mHalfMargin;
                    rect.right = this.mHalfMargin;
                } else if (recyclerView.isLayoutRtl()) {
                    if (spanIndex == 0) {
                        rect.left = this.mHalfMargin;
                        rect.right = 0;
                        return;
                    }
                    rect.left = 0;
                    rect.right = this.mHalfMargin;
                } else if (spanIndex == 0) {
                    rect.left = 0;
                    rect.right = this.mHalfMargin;
                } else {
                    rect.left = this.mHalfMargin;
                    rect.right = 0;
                }
            }
        }
    }

    private static int calculateHeaderMinHeight(Context context) {
        Resources resources = context.getResources();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(C1894R.style.QSCustomizeToolbar, R.styleable.Toolbar);
        int i = 0;
        int resourceId = obtainStyledAttributes.getResourceId(27, 0);
        obtainStyledAttributes.recycle();
        if (resourceId != 0) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, android.R.styleable.View);
            i = obtainStyledAttributes2.getDimensionPixelSize(36, 0);
            obtainStyledAttributes2.recycle();
        }
        return ((((resources.getDimensionPixelSize(C1894R.dimen.qs_panel_padding_top) + resources.getDimensionPixelSize(C1894R.dimen.brightness_mirror_height)) + resources.getDimensionPixelSize(C1894R.dimen.qs_brightness_margin_top)) + resources.getDimensionPixelSize(C1894R.dimen.qs_brightness_margin_bottom)) - i) - resources.getDimensionPixelSize(C1894R.dimen.qs_tile_margin_top_bottom);
    }
}
