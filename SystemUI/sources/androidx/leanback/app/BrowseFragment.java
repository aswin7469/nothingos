package androidx.leanback.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;
import androidx.leanback.C0742R;
import androidx.leanback.app.HeadersFragment;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.transition.TransitionListener;
import androidx.leanback.util.StateMachine;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.InvisibleRowPresenter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ScaleFrameLayout;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class BrowseFragment extends BaseFragment {
    private static final String ARG_HEADERS_STATE;
    private static final String ARG_TITLE;
    private static final String CURRENT_SELECTED_POSITION = "currentSelectedPosition";
    static final boolean DEBUG = false;
    public static final int HEADERS_DISABLED = 3;
    public static final int HEADERS_ENABLED = 1;
    public static final int HEADERS_HIDDEN = 2;
    static final String HEADER_SHOW = "headerShow";
    static final String HEADER_STACK_INDEX = "headerStackIndex";
    private static final String IS_PAGE_ROW = "isPageRow";
    private static final String LB_HEADERS_BACKSTACK = "lbHeadersBackStack_";
    static final String TAG = "BrowseFragment";
    final StateMachine.Event EVT_HEADER_VIEW_CREATED = new StateMachine.Event("headerFragmentViewCreated");
    final StateMachine.Event EVT_MAIN_FRAGMENT_VIEW_CREATED = new StateMachine.Event("mainFragmentViewCreated");
    final StateMachine.Event EVT_SCREEN_DATA_READY = new StateMachine.Event("screenDataReady");
    final StateMachine.State STATE_SET_ENTRANCE_START_STATE = new StateMachine.State("SET_ENTRANCE_START_STATE") {
        public void run() {
            BrowseFragment.this.setEntranceTransitionStartState();
        }
    };
    private ObjectAdapter mAdapter;
    private PresenterSelector mAdapterPresenter;
    BackStackListener mBackStackChangedListener;
    private int mBrandColor = 0;
    private boolean mBrandColorSet;
    BrowseFrameLayout mBrowseFrame;
    BrowseTransitionListener mBrowseTransitionListener;
    boolean mCanShowHeaders = true;
    private int mContainerListAlignTop;
    private int mContainerListMarginStart;
    OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private HeadersFragment.OnHeaderClickedListener mHeaderClickedListener = new HeadersFragment.OnHeaderClickedListener() {
        public void onHeaderClicked(RowHeaderPresenter.ViewHolder viewHolder, Row row) {
            if (BrowseFragment.this.mCanShowHeaders && BrowseFragment.this.mShowingHeaders && !BrowseFragment.this.isInHeadersTransition() && BrowseFragment.this.mMainFragment != null && BrowseFragment.this.mMainFragment.getView() != null) {
                BrowseFragment.this.startHeadersTransitionInternal(false);
                BrowseFragment.this.mMainFragment.getView().requestFocus();
            }
        }
    };
    private PresenterSelector mHeaderPresenterSelector;
    private HeadersFragment.OnHeaderViewSelectedListener mHeaderViewSelectedListener = new HeadersFragment.OnHeaderViewSelectedListener() {
        public void onHeaderSelected(RowHeaderPresenter.ViewHolder viewHolder, Row row) {
            int selectedPosition = BrowseFragment.this.mHeadersFragment.getSelectedPosition();
            if (BrowseFragment.this.mShowingHeaders) {
                BrowseFragment.this.onRowSelected(selectedPosition);
            }
        }
    };
    boolean mHeadersBackStackEnabled = true;
    HeadersFragment mHeadersFragment;
    private int mHeadersState = 1;
    Object mHeadersTransition;
    boolean mIsPageRow;
    Fragment mMainFragment;
    MainFragmentAdapter mMainFragmentAdapter;
    private MainFragmentAdapterRegistry mMainFragmentAdapterRegistry = new MainFragmentAdapterRegistry();
    ListRowDataAdapter mMainFragmentListRowDataAdapter;
    MainFragmentRowsAdapter mMainFragmentRowsAdapter;
    private boolean mMainFragmentScaleEnabled = true;
    private final BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener = new BrowseFrameLayout.OnChildFocusListener() {
        public boolean onRequestFocusInDescendants(int i, Rect rect) {
            if (BrowseFragment.this.getChildFragmentManager().isDestroyed()) {
                return true;
            }
            if (BrowseFragment.this.mCanShowHeaders && BrowseFragment.this.mShowingHeaders && BrowseFragment.this.mHeadersFragment != null && BrowseFragment.this.mHeadersFragment.getView() != null && BrowseFragment.this.mHeadersFragment.getView().requestFocus(i, rect)) {
                return true;
            }
            if (BrowseFragment.this.mMainFragment != null && BrowseFragment.this.mMainFragment.getView() != null && BrowseFragment.this.mMainFragment.getView().requestFocus(i, rect)) {
                return true;
            }
            if (BrowseFragment.this.getTitleView() == null || !BrowseFragment.this.getTitleView().requestFocus(i, rect)) {
                return false;
            }
            return true;
        }

        public void onRequestChildFocus(View view, View view2) {
            if (!BrowseFragment.this.getChildFragmentManager().isDestroyed() && BrowseFragment.this.mCanShowHeaders && !BrowseFragment.this.isInHeadersTransition()) {
                int id = view.getId();
                if (id == C0742R.C0745id.browse_container_dock && BrowseFragment.this.mShowingHeaders) {
                    BrowseFragment.this.startHeadersTransitionInternal(false);
                } else if (id == C0742R.C0745id.browse_headers_dock && !BrowseFragment.this.mShowingHeaders) {
                    BrowseFragment.this.startHeadersTransitionInternal(true);
                }
            }
        }
    };
    private final BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener = new BrowseFrameLayout.OnFocusSearchListener() {
        public View onFocusSearch(View view, int i) {
            if (BrowseFragment.this.mCanShowHeaders && BrowseFragment.this.isInHeadersTransition()) {
                return view;
            }
            if (BrowseFragment.this.getTitleView() != null && view != BrowseFragment.this.getTitleView() && i == 33) {
                return BrowseFragment.this.getTitleView();
            }
            if (BrowseFragment.this.getTitleView() != null && BrowseFragment.this.getTitleView().hasFocus() && i == 130) {
                return (!BrowseFragment.this.mCanShowHeaders || !BrowseFragment.this.mShowingHeaders) ? BrowseFragment.this.mMainFragment.getView() : BrowseFragment.this.mHeadersFragment.getVerticalGridView();
            }
            boolean z = true;
            if (ViewCompat.getLayoutDirection(view) != 1) {
                z = false;
            }
            int i2 = 66;
            int i3 = z ? 66 : 17;
            if (z) {
                i2 = 17;
            }
            if (BrowseFragment.this.mCanShowHeaders && i == i3) {
                return (BrowseFragment.this.isVerticalScrolling() || BrowseFragment.this.mShowingHeaders || !BrowseFragment.this.isHeadersDataReady()) ? view : BrowseFragment.this.mHeadersFragment.getVerticalGridView();
            }
            if (i == i2) {
                return (BrowseFragment.this.isVerticalScrolling() || BrowseFragment.this.mMainFragment == null || BrowseFragment.this.mMainFragment.getView() == null) ? view : BrowseFragment.this.mMainFragment.getView();
            }
            if (i != 130 || !BrowseFragment.this.mShowingHeaders) {
                return null;
            }
            return view;
        }
    };
    private OnItemViewClickedListener mOnItemViewClickedListener;
    Object mPageRow;
    private float mScaleFactor;
    private ScaleFrameLayout mScaleFrameLayout;
    private Object mSceneAfterEntranceTransition;
    Object mSceneWithHeaders;
    Object mSceneWithoutHeaders;
    private int mSelectedPosition = -1;
    private final SetSelectionRunnable mSetSelectionRunnable = new SetSelectionRunnable();
    boolean mShowingHeaders = true;
    boolean mStopped = true;
    private final RecyclerView.OnScrollListener mWaitScrollFinishAndCommitMainFragment = new RecyclerView.OnScrollListener() {
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0) {
                recyclerView.removeOnScrollListener(this);
                if (!BrowseFragment.this.mStopped) {
                    BrowseFragment.this.commitMainFragment();
                }
            }
        }
    };
    String mWithHeadersBackStackName;

    @Deprecated
    public static class BrowseTransitionListener {
        public void onHeadersTransitionStart(boolean z) {
        }

        public void onHeadersTransitionStop(boolean z) {
        }
    }

    @Deprecated
    public static abstract class FragmentFactory<T extends Fragment> {
        public abstract T createFragment(Object obj);
    }

    @Deprecated
    public interface FragmentHost {
        void notifyDataReady(MainFragmentAdapter mainFragmentAdapter);

        void notifyViewCreated(MainFragmentAdapter mainFragmentAdapter);

        void showTitleView(boolean z);
    }

    @Deprecated
    public interface MainFragmentAdapterProvider {
        MainFragmentAdapter getMainFragmentAdapter();
    }

    @Deprecated
    public interface MainFragmentRowsAdapterProvider {
        MainFragmentRowsAdapter getMainFragmentRowsAdapter();
    }

    /* access modifiers changed from: package-private */
    public void createStateMachineStates() {
        super.createStateMachineStates();
        this.mStateMachine.addState(this.STATE_SET_ENTRANCE_START_STATE);
    }

    /* access modifiers changed from: package-private */
    public void createStateMachineTransitions() {
        super.createStateMachineTransitions();
        this.mStateMachine.addTransition(this.STATE_ENTRANCE_ON_PREPARED, this.STATE_SET_ENTRANCE_START_STATE, this.EVT_HEADER_VIEW_CREATED);
        this.mStateMachine.addTransition(this.STATE_ENTRANCE_ON_PREPARED, this.STATE_ENTRANCE_ON_PREPARED_ON_CREATEVIEW, this.EVT_MAIN_FRAGMENT_VIEW_CREATED);
        this.mStateMachine.addTransition(this.STATE_ENTRANCE_ON_PREPARED, this.STATE_ENTRANCE_PERFORM, this.EVT_SCREEN_DATA_READY);
    }

    final class BackStackListener implements FragmentManager.OnBackStackChangedListener {
        int mIndexOfHeadersBackStack = -1;
        int mLastEntryCount;

        BackStackListener() {
            this.mLastEntryCount = BrowseFragment.this.getFragmentManager().getBackStackEntryCount();
        }

        /* access modifiers changed from: package-private */
        public void load(Bundle bundle) {
            if (bundle != null) {
                int i = bundle.getInt(BrowseFragment.HEADER_STACK_INDEX, -1);
                this.mIndexOfHeadersBackStack = i;
                BrowseFragment.this.mShowingHeaders = i == -1;
            } else if (!BrowseFragment.this.mShowingHeaders) {
                BrowseFragment.this.getFragmentManager().beginTransaction().addToBackStack(BrowseFragment.this.mWithHeadersBackStackName).commit();
            }
        }

        /* access modifiers changed from: package-private */
        public void save(Bundle bundle) {
            bundle.putInt(BrowseFragment.HEADER_STACK_INDEX, this.mIndexOfHeadersBackStack);
        }

        public void onBackStackChanged() {
            if (BrowseFragment.this.getFragmentManager() == null) {
                Log.w(BrowseFragment.TAG, "getFragmentManager() is null, stack:", new Exception());
                return;
            }
            int backStackEntryCount = BrowseFragment.this.getFragmentManager().getBackStackEntryCount();
            int i = this.mLastEntryCount;
            if (backStackEntryCount > i) {
                int i2 = backStackEntryCount - 1;
                if (BrowseFragment.this.mWithHeadersBackStackName.equals(BrowseFragment.this.getFragmentManager().getBackStackEntryAt(i2).getName())) {
                    this.mIndexOfHeadersBackStack = i2;
                }
            } else if (backStackEntryCount < i && this.mIndexOfHeadersBackStack >= backStackEntryCount) {
                if (!BrowseFragment.this.isHeadersDataReady()) {
                    BrowseFragment.this.getFragmentManager().beginTransaction().addToBackStack(BrowseFragment.this.mWithHeadersBackStackName).commit();
                    return;
                }
                this.mIndexOfHeadersBackStack = -1;
                if (!BrowseFragment.this.mShowingHeaders) {
                    BrowseFragment.this.startHeadersTransitionInternal(true);
                }
            }
            this.mLastEntryCount = backStackEntryCount;
        }
    }

    private final class SetSelectionRunnable implements Runnable {
        static final int TYPE_INTERNAL_SYNC = 0;
        static final int TYPE_INVALID = -1;
        static final int TYPE_USER_REQUEST = 1;
        private int mPosition;
        private boolean mSmooth;
        private int mType;

        SetSelectionRunnable() {
            reset();
        }

        /* access modifiers changed from: package-private */
        public void post(int i, int i2, boolean z) {
            if (i2 >= this.mType) {
                this.mPosition = i;
                this.mType = i2;
                this.mSmooth = z;
                BrowseFragment.this.mBrowseFrame.removeCallbacks(this);
                if (!BrowseFragment.this.mStopped) {
                    BrowseFragment.this.mBrowseFrame.post(this);
                }
            }
        }

        public void run() {
            BrowseFragment.this.setSelection(this.mPosition, this.mSmooth);
            reset();
        }

        public void stop() {
            BrowseFragment.this.mBrowseFrame.removeCallbacks(this);
        }

        public void start() {
            if (this.mType != -1) {
                BrowseFragment.this.mBrowseFrame.post(this);
            }
        }

        private void reset() {
            this.mPosition = -1;
            this.mType = -1;
            this.mSmooth = false;
        }
    }

    private final class FragmentHostImpl implements FragmentHost {
        boolean mShowTitleView = true;

        FragmentHostImpl() {
        }

        public void notifyViewCreated(MainFragmentAdapter mainFragmentAdapter) {
            BrowseFragment.this.mStateMachine.fireEvent(BrowseFragment.this.EVT_MAIN_FRAGMENT_VIEW_CREATED);
            if (!BrowseFragment.this.mIsPageRow) {
                BrowseFragment.this.mStateMachine.fireEvent(BrowseFragment.this.EVT_SCREEN_DATA_READY);
            }
        }

        public void notifyDataReady(MainFragmentAdapter mainFragmentAdapter) {
            if (BrowseFragment.this.mMainFragmentAdapter != null && BrowseFragment.this.mMainFragmentAdapter.getFragmentHost() == this && BrowseFragment.this.mIsPageRow) {
                BrowseFragment.this.mStateMachine.fireEvent(BrowseFragment.this.EVT_SCREEN_DATA_READY);
            }
        }

        public void showTitleView(boolean z) {
            this.mShowTitleView = z;
            if (BrowseFragment.this.mMainFragmentAdapter != null && BrowseFragment.this.mMainFragmentAdapter.getFragmentHost() == this && BrowseFragment.this.mIsPageRow) {
                BrowseFragment.this.updateTitleViewVisibility();
            }
        }
    }

    @Deprecated
    public static class MainFragmentAdapter<T extends Fragment> {
        private final T mFragment;
        FragmentHostImpl mFragmentHost;
        private boolean mScalingEnabled;

        public boolean isScrolling() {
            return false;
        }

        public void onTransitionEnd() {
        }

        public boolean onTransitionPrepare() {
            return false;
        }

        public void onTransitionStart() {
        }

        public void setAlignment(int i) {
        }

        public void setEntranceTransitionState(boolean z) {
        }

        public void setExpand(boolean z) {
        }

        public MainFragmentAdapter(T t) {
            this.mFragment = t;
        }

        public final T getFragment() {
            return this.mFragment;
        }

        public boolean isScalingEnabled() {
            return this.mScalingEnabled;
        }

        public void setScalingEnabled(boolean z) {
            this.mScalingEnabled = z;
        }

        public final FragmentHost getFragmentHost() {
            return this.mFragmentHost;
        }

        /* access modifiers changed from: package-private */
        public void setFragmentHost(FragmentHostImpl fragmentHostImpl) {
            this.mFragmentHost = fragmentHostImpl;
        }
    }

    @Deprecated
    public static class MainFragmentRowsAdapter<T extends Fragment> {
        private final T mFragment;

        public RowPresenter.ViewHolder findRowViewHolderByPosition(int i) {
            return null;
        }

        public int getSelectedPosition() {
            return 0;
        }

        public void setAdapter(ObjectAdapter objectAdapter) {
        }

        public void setOnItemViewClickedListener(OnItemViewClickedListener onItemViewClickedListener) {
        }

        public void setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener) {
        }

        public void setSelectedPosition(int i, boolean z) {
        }

        public void setSelectedPosition(int i, boolean z, Presenter.ViewHolderTask viewHolderTask) {
        }

        public MainFragmentRowsAdapter(T t) {
            if (t != null) {
                this.mFragment = t;
                return;
            }
            throw new IllegalArgumentException("Fragment can't be null");
        }

        public final T getFragment() {
            return this.mFragment;
        }
    }

    private boolean createMainFragment(ObjectAdapter objectAdapter, int i) {
        Object obj;
        boolean z = true;
        Object obj2 = null;
        if (!this.mCanShowHeaders) {
            obj = null;
        } else if (objectAdapter == null || objectAdapter.size() == 0) {
            return false;
        } else {
            if (i < 0) {
                i = 0;
            } else if (i >= objectAdapter.size()) {
                throw new IllegalArgumentException(String.format("Invalid position %d requested", Integer.valueOf(i)));
            }
            obj = objectAdapter.get(i);
        }
        boolean z2 = this.mIsPageRow;
        Object obj3 = this.mPageRow;
        boolean z3 = this.mCanShowHeaders && (obj instanceof PageRow);
        this.mIsPageRow = z3;
        if (z3) {
            obj2 = obj;
        }
        this.mPageRow = obj2;
        if (this.mMainFragment != null) {
            if (!z2) {
                z = z3;
            } else if (z3 && (obj3 == null || obj3 == obj2)) {
                z = false;
            }
        }
        if (z) {
            Fragment createFragment = this.mMainFragmentAdapterRegistry.createFragment(obj);
            this.mMainFragment = createFragment;
            if (createFragment instanceof MainFragmentAdapterProvider) {
                setMainFragmentAdapter();
            } else {
                throw new IllegalArgumentException("Fragment must implement MainFragmentAdapterProvider");
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void setMainFragmentAdapter() {
        MainFragmentAdapter mainFragmentAdapter = ((MainFragmentAdapterProvider) this.mMainFragment).getMainFragmentAdapter();
        this.mMainFragmentAdapter = mainFragmentAdapter;
        mainFragmentAdapter.setFragmentHost(new FragmentHostImpl());
        if (!this.mIsPageRow) {
            Fragment fragment = this.mMainFragment;
            if (fragment instanceof MainFragmentRowsAdapterProvider) {
                setMainFragmentRowsAdapter(((MainFragmentRowsAdapterProvider) fragment).getMainFragmentRowsAdapter());
            } else {
                setMainFragmentRowsAdapter((MainFragmentRowsAdapter) null);
            }
            this.mIsPageRow = this.mMainFragmentRowsAdapter == null;
            return;
        }
        setMainFragmentRowsAdapter((MainFragmentRowsAdapter) null);
    }

    @Deprecated
    public static class ListRowFragmentFactory extends FragmentFactory<RowsFragment> {
        public RowsFragment createFragment(Object obj) {
            return new RowsFragment();
        }
    }

    @Deprecated
    public static final class MainFragmentAdapterRegistry {
        private static final FragmentFactory sDefaultFragmentFactory = new ListRowFragmentFactory();
        private final Map<Class<?>, FragmentFactory> mItemToFragmentFactoryMapping = new HashMap();

        public MainFragmentAdapterRegistry() {
            registerFragment(ListRow.class, sDefaultFragmentFactory);
        }

        public void registerFragment(Class<?> cls, FragmentFactory fragmentFactory) {
            this.mItemToFragmentFactoryMapping.put(cls, fragmentFactory);
        }

        public Fragment createFragment(Object obj) {
            FragmentFactory fragmentFactory;
            if (obj == null) {
                fragmentFactory = sDefaultFragmentFactory;
            } else {
                fragmentFactory = this.mItemToFragmentFactoryMapping.get(obj.getClass());
            }
            if (fragmentFactory == null && !(obj instanceof PageRow)) {
                fragmentFactory = sDefaultFragmentFactory;
            }
            return fragmentFactory.createFragment(obj);
        }
    }

    static {
        Class<BrowseFragment> cls = BrowseFragment.class;
        ARG_TITLE = cls.getCanonicalName() + ".title";
        ARG_HEADERS_STATE = cls.getCanonicalName() + ".headersState";
    }

    public static Bundle createArgs(Bundle bundle, String str, int i) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ARG_TITLE, str);
        bundle.putInt(ARG_HEADERS_STATE, i);
        return bundle;
    }

    public void setBrandColor(int i) {
        this.mBrandColor = i;
        this.mBrandColorSet = true;
        HeadersFragment headersFragment = this.mHeadersFragment;
        if (headersFragment != null) {
            headersFragment.setBackgroundColor(i);
        }
    }

    public int getBrandColor() {
        return this.mBrandColor;
    }

    private void updateWrapperPresenter() {
        ObjectAdapter objectAdapter = this.mAdapter;
        if (objectAdapter == null) {
            this.mAdapterPresenter = null;
            return;
        }
        final PresenterSelector presenterSelector = objectAdapter.getPresenterSelector();
        if (presenterSelector == null) {
            throw new IllegalArgumentException("Adapter.getPresenterSelector() is null");
        } else if (presenterSelector != this.mAdapterPresenter) {
            this.mAdapterPresenter = presenterSelector;
            Presenter[] presenters = presenterSelector.getPresenters();
            final InvisibleRowPresenter invisibleRowPresenter = new InvisibleRowPresenter();
            int length = presenters.length + 1;
            final Presenter[] presenterArr = new Presenter[length];
            System.arraycopy((Object) presenterArr, 0, (Object) presenters, 0, presenters.length);
            presenterArr[length - 1] = invisibleRowPresenter;
            this.mAdapter.setPresenterSelector(new PresenterSelector() {
                public Presenter getPresenter(Object obj) {
                    if (((Row) obj).isRenderedAsRowView()) {
                        return presenterSelector.getPresenter(obj);
                    }
                    return invisibleRowPresenter;
                }

                public Presenter[] getPresenters() {
                    return presenterArr;
                }
            });
        }
    }

    public void setAdapter(ObjectAdapter objectAdapter) {
        this.mAdapter = objectAdapter;
        updateWrapperPresenter();
        if (getView() != null) {
            updateMainFragmentRowsAdapter();
            this.mHeadersFragment.setAdapter(this.mAdapter);
        }
    }

    /* access modifiers changed from: package-private */
    public void setMainFragmentRowsAdapter(MainFragmentRowsAdapter mainFragmentRowsAdapter) {
        MainFragmentRowsAdapter mainFragmentRowsAdapter2 = this.mMainFragmentRowsAdapter;
        if (mainFragmentRowsAdapter != mainFragmentRowsAdapter2) {
            if (mainFragmentRowsAdapter2 != null) {
                mainFragmentRowsAdapter2.setAdapter((ObjectAdapter) null);
            }
            this.mMainFragmentRowsAdapter = mainFragmentRowsAdapter;
            if (mainFragmentRowsAdapter != null) {
                mainFragmentRowsAdapter.setOnItemViewSelectedListener(new MainFragmentItemViewSelectedListener(this.mMainFragmentRowsAdapter));
                this.mMainFragmentRowsAdapter.setOnItemViewClickedListener(this.mOnItemViewClickedListener);
            }
            updateMainFragmentRowsAdapter();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateMainFragmentRowsAdapter() {
        ListRowDataAdapter listRowDataAdapter = this.mMainFragmentListRowDataAdapter;
        ListRowDataAdapter listRowDataAdapter2 = null;
        if (listRowDataAdapter != null) {
            listRowDataAdapter.detach();
            this.mMainFragmentListRowDataAdapter = null;
        }
        if (this.mMainFragmentRowsAdapter != null) {
            if (this.mAdapter != null) {
                listRowDataAdapter2 = new ListRowDataAdapter(this.mAdapter);
            }
            this.mMainFragmentListRowDataAdapter = listRowDataAdapter2;
            this.mMainFragmentRowsAdapter.setAdapter(listRowDataAdapter2);
        }
    }

    public final MainFragmentAdapterRegistry getMainFragmentRegistry() {
        return this.mMainFragmentAdapterRegistry;
    }

    public ObjectAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener) {
        this.mExternalOnItemViewSelectedListener = onItemViewSelectedListener;
    }

    public OnItemViewSelectedListener getOnItemViewSelectedListener() {
        return this.mExternalOnItemViewSelectedListener;
    }

    public RowsFragment getRowsFragment() {
        Fragment fragment = this.mMainFragment;
        if (fragment instanceof RowsFragment) {
            return (RowsFragment) fragment;
        }
        return null;
    }

    public Fragment getMainFragment() {
        return this.mMainFragment;
    }

    public HeadersFragment getHeadersFragment() {
        return this.mHeadersFragment;
    }

    public void setOnItemViewClickedListener(OnItemViewClickedListener onItemViewClickedListener) {
        this.mOnItemViewClickedListener = onItemViewClickedListener;
        MainFragmentRowsAdapter mainFragmentRowsAdapter = this.mMainFragmentRowsAdapter;
        if (mainFragmentRowsAdapter != null) {
            mainFragmentRowsAdapter.setOnItemViewClickedListener(onItemViewClickedListener);
        }
    }

    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return this.mOnItemViewClickedListener;
    }

    public void startHeadersTransition(boolean z) {
        if (!this.mCanShowHeaders) {
            throw new IllegalStateException("Cannot start headers transition");
        } else if (!isInHeadersTransition() && this.mShowingHeaders != z) {
            startHeadersTransitionInternal(z);
        }
    }

    public boolean isInHeadersTransition() {
        return this.mHeadersTransition != null;
    }

    public boolean isShowingHeaders() {
        return this.mShowingHeaders;
    }

    public void setBrowseTransitionListener(BrowseTransitionListener browseTransitionListener) {
        this.mBrowseTransitionListener = browseTransitionListener;
    }

    @Deprecated
    public void enableRowScaling(boolean z) {
        enableMainFragmentScaling(z);
    }

    public void enableMainFragmentScaling(boolean z) {
        this.mMainFragmentScaleEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void startHeadersTransitionInternal(final boolean z) {
        if (!getFragmentManager().isDestroyed() && isHeadersDataReady()) {
            this.mShowingHeaders = z;
            this.mMainFragmentAdapter.onTransitionPrepare();
            this.mMainFragmentAdapter.onTransitionStart();
            onExpandTransitionStart(!z, new Runnable() {
                public void run() {
                    BrowseFragment.this.mHeadersFragment.onTransitionPrepare();
                    BrowseFragment.this.mHeadersFragment.onTransitionStart();
                    BrowseFragment.this.createHeadersTransition();
                    if (BrowseFragment.this.mBrowseTransitionListener != null) {
                        BrowseFragment.this.mBrowseTransitionListener.onHeadersTransitionStart(z);
                    }
                    TransitionHelper.runTransition(z ? BrowseFragment.this.mSceneWithHeaders : BrowseFragment.this.mSceneWithoutHeaders, BrowseFragment.this.mHeadersTransition);
                    if (!BrowseFragment.this.mHeadersBackStackEnabled) {
                        return;
                    }
                    if (!z) {
                        BrowseFragment.this.getFragmentManager().beginTransaction().addToBackStack(BrowseFragment.this.mWithHeadersBackStackName).commit();
                        return;
                    }
                    int i = BrowseFragment.this.mBackStackChangedListener.mIndexOfHeadersBackStack;
                    if (i >= 0) {
                        BrowseFragment.this.getFragmentManager().popBackStackImmediate(BrowseFragment.this.getFragmentManager().getBackStackEntryAt(i).getId(), 1);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isVerticalScrolling() {
        return this.mHeadersFragment.isScrolling() || this.mMainFragmentAdapter.isScrolling();
    }

    /* access modifiers changed from: package-private */
    public final boolean isHeadersDataReady() {
        ObjectAdapter objectAdapter = this.mAdapter;
        return (objectAdapter == null || objectAdapter.size() == 0) ? false : true;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(CURRENT_SELECTED_POSITION, this.mSelectedPosition);
        bundle.putBoolean(IS_PAGE_ROW, this.mIsPageRow);
        BackStackListener backStackListener = this.mBackStackChangedListener;
        if (backStackListener != null) {
            backStackListener.save(bundle);
        } else {
            bundle.putBoolean(HEADER_SHOW, this.mShowingHeaders);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = FragmentUtil.getContext(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(C0742R.styleable.LeanbackTheme);
        this.mContainerListMarginStart = (int) obtainStyledAttributes.getDimension(C0742R.styleable.LeanbackTheme_browseRowsMarginStart, (float) context.getResources().getDimensionPixelSize(C0742R.dimen.lb_browse_rows_margin_start));
        this.mContainerListAlignTop = (int) obtainStyledAttributes.getDimension(C0742R.styleable.LeanbackTheme_browseRowsMarginTop, (float) context.getResources().getDimensionPixelSize(C0742R.dimen.lb_browse_rows_margin_top));
        obtainStyledAttributes.recycle();
        readArguments(getArguments());
        if (this.mCanShowHeaders) {
            if (this.mHeadersBackStackEnabled) {
                this.mWithHeadersBackStackName = LB_HEADERS_BACKSTACK + this;
                this.mBackStackChangedListener = new BackStackListener();
                getFragmentManager().addOnBackStackChangedListener(this.mBackStackChangedListener);
                this.mBackStackChangedListener.load(bundle);
            } else if (bundle != null) {
                this.mShowingHeaders = bundle.getBoolean(HEADER_SHOW);
            }
        }
        this.mScaleFactor = getResources().getFraction(C0742R.fraction.lb_browse_rows_scale, 1, 1);
    }

    public void onDestroyView() {
        setMainFragmentRowsAdapter((MainFragmentRowsAdapter) null);
        this.mPageRow = null;
        this.mMainFragmentAdapter = null;
        this.mMainFragment = null;
        this.mHeadersFragment = null;
        this.mBrowseFrame = null;
        this.mScaleFrameLayout = null;
        this.mSceneAfterEntranceTransition = null;
        this.mSceneWithHeaders = null;
        this.mSceneWithoutHeaders = null;
        super.onDestroyView();
    }

    public void onDestroy() {
        if (this.mBackStackChangedListener != null) {
            getFragmentManager().removeOnBackStackChangedListener(this.mBackStackChangedListener);
        }
        super.onDestroy();
    }

    public HeadersFragment onCreateHeadersFragment() {
        return new HeadersFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (getChildFragmentManager().findFragmentById(C0742R.C0745id.scale_frame) == null) {
            this.mHeadersFragment = onCreateHeadersFragment();
            createMainFragment(this.mAdapter, this.mSelectedPosition);
            FragmentTransaction replace = getChildFragmentManager().beginTransaction().replace(C0742R.C0745id.browse_headers_dock, this.mHeadersFragment);
            if (this.mMainFragment != null) {
                replace.replace(C0742R.C0745id.scale_frame, this.mMainFragment);
            } else {
                MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(null);
                this.mMainFragmentAdapter = mainFragmentAdapter;
                mainFragmentAdapter.setFragmentHost(new FragmentHostImpl());
            }
            replace.commit();
        } else {
            this.mHeadersFragment = (HeadersFragment) getChildFragmentManager().findFragmentById(C0742R.C0745id.browse_headers_dock);
            this.mMainFragment = getChildFragmentManager().findFragmentById(C0742R.C0745id.scale_frame);
            this.mIsPageRow = bundle != null && bundle.getBoolean(IS_PAGE_ROW, false);
            this.mSelectedPosition = bundle != null ? bundle.getInt(CURRENT_SELECTED_POSITION, 0) : 0;
            setMainFragmentAdapter();
        }
        this.mHeadersFragment.setHeadersGone(true ^ this.mCanShowHeaders);
        PresenterSelector presenterSelector = this.mHeaderPresenterSelector;
        if (presenterSelector != null) {
            this.mHeadersFragment.setPresenterSelector(presenterSelector);
        }
        this.mHeadersFragment.setAdapter(this.mAdapter);
        this.mHeadersFragment.setOnHeaderViewSelectedListener(this.mHeaderViewSelectedListener);
        this.mHeadersFragment.setOnHeaderClickedListener(this.mHeaderClickedListener);
        View inflate = layoutInflater.inflate(C0742R.layout.lb_browse_fragment, viewGroup, false);
        getProgressBarManager().setRootView((ViewGroup) inflate);
        BrowseFrameLayout browseFrameLayout = (BrowseFrameLayout) inflate.findViewById(C0742R.C0745id.browse_frame);
        this.mBrowseFrame = browseFrameLayout;
        browseFrameLayout.setOnChildFocusListener(this.mOnChildFocusListener);
        this.mBrowseFrame.setOnFocusSearchListener(this.mOnFocusSearchListener);
        installTitleView(layoutInflater, this.mBrowseFrame, bundle);
        ScaleFrameLayout scaleFrameLayout = (ScaleFrameLayout) inflate.findViewById(C0742R.C0745id.scale_frame);
        this.mScaleFrameLayout = scaleFrameLayout;
        scaleFrameLayout.setPivotX(0.0f);
        this.mScaleFrameLayout.setPivotY((float) this.mContainerListAlignTop);
        if (this.mBrandColorSet) {
            this.mHeadersFragment.setBackgroundColor(this.mBrandColor);
        }
        this.mSceneWithHeaders = TransitionHelper.createScene(this.mBrowseFrame, new Runnable() {
            public void run() {
                BrowseFragment.this.showHeaders(true);
            }
        });
        this.mSceneWithoutHeaders = TransitionHelper.createScene(this.mBrowseFrame, new Runnable() {
            public void run() {
                BrowseFragment.this.showHeaders(false);
            }
        });
        this.mSceneAfterEntranceTransition = TransitionHelper.createScene(this.mBrowseFrame, new Runnable() {
            public void run() {
                BrowseFragment.this.setEntranceTransitionEndState();
            }
        });
        return inflate;
    }

    /* access modifiers changed from: package-private */
    public void createHeadersTransition() {
        Object loadTransition = TransitionHelper.loadTransition(FragmentUtil.getContext(this), this.mShowingHeaders ? C0742R.C0746transition.lb_browse_headers_in : C0742R.C0746transition.lb_browse_headers_out);
        this.mHeadersTransition = loadTransition;
        TransitionHelper.addTransitionListener(loadTransition, new TransitionListener() {
            public void onTransitionStart(Object obj) {
            }

            public void onTransitionEnd(Object obj) {
                VerticalGridView verticalGridView;
                View view;
                BrowseFragment.this.mHeadersTransition = null;
                if (BrowseFragment.this.mMainFragmentAdapter != null) {
                    BrowseFragment.this.mMainFragmentAdapter.onTransitionEnd();
                    if (!BrowseFragment.this.mShowingHeaders && BrowseFragment.this.mMainFragment != null && (view = BrowseFragment.this.mMainFragment.getView()) != null && !view.hasFocus()) {
                        view.requestFocus();
                    }
                }
                if (BrowseFragment.this.mHeadersFragment != null) {
                    BrowseFragment.this.mHeadersFragment.onTransitionEnd();
                    if (BrowseFragment.this.mShowingHeaders && (verticalGridView = BrowseFragment.this.mHeadersFragment.getVerticalGridView()) != null && !verticalGridView.hasFocus()) {
                        verticalGridView.requestFocus();
                    }
                }
                BrowseFragment.this.updateTitleViewVisibility();
                if (BrowseFragment.this.mBrowseTransitionListener != null) {
                    BrowseFragment.this.mBrowseTransitionListener.onHeadersTransitionStop(BrowseFragment.this.mShowingHeaders);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void updateTitleViewVisibility() {
        boolean z;
        MainFragmentAdapter mainFragmentAdapter;
        boolean z2;
        MainFragmentAdapter mainFragmentAdapter2;
        if (!this.mShowingHeaders) {
            if (!this.mIsPageRow || (mainFragmentAdapter2 = this.mMainFragmentAdapter) == null) {
                z2 = isFirstRowWithContent(this.mSelectedPosition);
            } else {
                z2 = mainFragmentAdapter2.mFragmentHost.mShowTitleView;
            }
            if (z2) {
                showTitle(6);
            } else {
                showTitle(false);
            }
        } else {
            if (!this.mIsPageRow || (mainFragmentAdapter = this.mMainFragmentAdapter) == null) {
                z = isFirstRowWithContent(this.mSelectedPosition);
            } else {
                z = mainFragmentAdapter.mFragmentHost.mShowTitleView;
            }
            boolean isFirstRowWithContentOrPageRow = isFirstRowWithContentOrPageRow(this.mSelectedPosition);
            int i = z ? 2 : 0;
            if (isFirstRowWithContentOrPageRow) {
                i |= 4;
            }
            if (i != 0) {
                showTitle(i);
            } else {
                showTitle(false);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstRowWithContentOrPageRow(int i) {
        ObjectAdapter objectAdapter = this.mAdapter;
        if (objectAdapter == null || objectAdapter.size() == 0) {
            return true;
        }
        int i2 = 0;
        while (i2 < this.mAdapter.size()) {
            Row row = (Row) this.mAdapter.get(i2);
            if (!row.isRenderedAsRowView() && !(row instanceof PageRow)) {
                i2++;
            } else if (i == i2) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstRowWithContent(int i) {
        ObjectAdapter objectAdapter = this.mAdapter;
        if (!(objectAdapter == null || objectAdapter.size() == 0)) {
            int i2 = 0;
            while (i2 < this.mAdapter.size()) {
                if (!((Row) this.mAdapter.get(i2)).isRenderedAsRowView()) {
                    i2++;
                } else if (i == i2) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void setHeaderPresenterSelector(PresenterSelector presenterSelector) {
        this.mHeaderPresenterSelector = presenterSelector;
        HeadersFragment headersFragment = this.mHeadersFragment;
        if (headersFragment != null) {
            headersFragment.setPresenterSelector(presenterSelector);
        }
    }

    private void setHeadersOnScreen(boolean z) {
        int i;
        View view = this.mHeadersFragment.getView();
        if (view != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (z) {
                i = 0;
            } else {
                i = -this.mContainerListMarginStart;
            }
            marginLayoutParams.setMarginStart(i);
            view.setLayoutParams(marginLayoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    public void showHeaders(boolean z) {
        this.mHeadersFragment.setHeadersEnabled(z);
        setHeadersOnScreen(z);
        expandMainFragment(!z);
    }

    private void expandMainFragment(boolean z) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mScaleFrameLayout.getLayoutParams();
        marginLayoutParams.setMarginStart(!z ? this.mContainerListMarginStart : 0);
        this.mScaleFrameLayout.setLayoutParams(marginLayoutParams);
        this.mMainFragmentAdapter.setExpand(z);
        setMainFragmentAlignment();
        float f = (z || !this.mMainFragmentScaleEnabled || !this.mMainFragmentAdapter.isScalingEnabled()) ? 1.0f : this.mScaleFactor;
        this.mScaleFrameLayout.setLayoutScaleY(f);
        this.mScaleFrameLayout.setChildScale(f);
    }

    class MainFragmentItemViewSelectedListener implements OnItemViewSelectedListener {
        MainFragmentRowsAdapter mMainFragmentRowsAdapter;

        public MainFragmentItemViewSelectedListener(MainFragmentRowsAdapter mainFragmentRowsAdapter) {
            this.mMainFragmentRowsAdapter = mainFragmentRowsAdapter;
        }

        public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            BrowseFragment.this.onRowSelected(this.mMainFragmentRowsAdapter.getSelectedPosition());
            if (BrowseFragment.this.mExternalOnItemViewSelectedListener != null) {
                BrowseFragment.this.mExternalOnItemViewSelectedListener.onItemSelected(viewHolder, obj, viewHolder2, row);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onRowSelected(int i) {
        this.mSetSelectionRunnable.post(i, 0, true);
    }

    /* access modifiers changed from: package-private */
    public void setSelection(int i, boolean z) {
        if (i != -1) {
            this.mSelectedPosition = i;
            HeadersFragment headersFragment = this.mHeadersFragment;
            if (headersFragment != null && this.mMainFragmentAdapter != null) {
                headersFragment.setSelectedPosition(i, z);
                replaceMainFragment(i);
                MainFragmentRowsAdapter mainFragmentRowsAdapter = this.mMainFragmentRowsAdapter;
                if (mainFragmentRowsAdapter != null) {
                    mainFragmentRowsAdapter.setSelectedPosition(i, z);
                }
                updateTitleViewVisibility();
            }
        }
    }

    private void replaceMainFragment(int i) {
        if (createMainFragment(this.mAdapter, i)) {
            swapToMainFragment();
            expandMainFragment(!this.mCanShowHeaders || !this.mShowingHeaders);
        }
    }

    /* access modifiers changed from: package-private */
    public final void commitMainFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager.findFragmentById(C0742R.C0745id.scale_frame) != this.mMainFragment) {
            childFragmentManager.beginTransaction().replace(C0742R.C0745id.scale_frame, this.mMainFragment).commit();
        }
    }

    private void swapToMainFragment() {
        if (!this.mStopped) {
            VerticalGridView verticalGridView = this.mHeadersFragment.getVerticalGridView();
            if (!isShowingHeaders() || verticalGridView == null || verticalGridView.getScrollState() == 0) {
                commitMainFragment();
                return;
            }
            getChildFragmentManager().beginTransaction().replace(C0742R.C0745id.scale_frame, new Fragment()).commit();
            verticalGridView.removeOnScrollListener(this.mWaitScrollFinishAndCommitMainFragment);
            verticalGridView.addOnScrollListener(this.mWaitScrollFinishAndCommitMainFragment);
        }
    }

    public void setSelectedPosition(int i) {
        setSelectedPosition(i, true);
    }

    public int getSelectedPosition() {
        return this.mSelectedPosition;
    }

    public RowPresenter.ViewHolder getSelectedRowViewHolder() {
        MainFragmentRowsAdapter mainFragmentRowsAdapter = this.mMainFragmentRowsAdapter;
        if (mainFragmentRowsAdapter == null) {
            return null;
        }
        return this.mMainFragmentRowsAdapter.findRowViewHolderByPosition(mainFragmentRowsAdapter.getSelectedPosition());
    }

    public void setSelectedPosition(int i, boolean z) {
        this.mSetSelectionRunnable.post(i, 1, z);
    }

    public void setSelectedPosition(int i, boolean z, Presenter.ViewHolderTask viewHolderTask) {
        if (this.mMainFragmentAdapterRegistry != null) {
            if (viewHolderTask != null) {
                startHeadersTransition(false);
            }
            MainFragmentRowsAdapter mainFragmentRowsAdapter = this.mMainFragmentRowsAdapter;
            if (mainFragmentRowsAdapter != null) {
                mainFragmentRowsAdapter.setSelectedPosition(i, z, viewHolderTask);
            }
        }
    }

    public void onStart() {
        Fragment fragment;
        HeadersFragment headersFragment;
        super.onStart();
        this.mHeadersFragment.setAlignment(this.mContainerListAlignTop);
        setMainFragmentAlignment();
        if (this.mCanShowHeaders && this.mShowingHeaders && (headersFragment = this.mHeadersFragment) != null && headersFragment.getView() != null) {
            this.mHeadersFragment.getView().requestFocus();
        } else if (!((this.mCanShowHeaders && this.mShowingHeaders) || (fragment = this.mMainFragment) == null || fragment.getView() == null)) {
            this.mMainFragment.getView().requestFocus();
        }
        if (this.mCanShowHeaders) {
            showHeaders(this.mShowingHeaders);
        }
        this.mStateMachine.fireEvent(this.EVT_HEADER_VIEW_CREATED);
        this.mStopped = false;
        commitMainFragment();
        this.mSetSelectionRunnable.start();
    }

    public void onStop() {
        this.mStopped = true;
        this.mSetSelectionRunnable.stop();
        super.onStop();
    }

    private void onExpandTransitionStart(boolean z, Runnable runnable) {
        if (z) {
            runnable.run();
        } else {
            new ExpandPreLayout(runnable, this.mMainFragmentAdapter, getView()).execute();
        }
    }

    private void setMainFragmentAlignment() {
        int i = this.mContainerListAlignTop;
        if (this.mMainFragmentScaleEnabled && this.mMainFragmentAdapter.isScalingEnabled() && this.mShowingHeaders) {
            i = (int) ((((float) i) / this.mScaleFactor) + 0.5f);
        }
        this.mMainFragmentAdapter.setAlignment(i);
    }

    public final void setHeadersTransitionOnBackEnabled(boolean z) {
        this.mHeadersBackStackEnabled = z;
    }

    public final boolean isHeadersTransitionOnBackEnabled() {
        return this.mHeadersBackStackEnabled;
    }

    private void readArguments(Bundle bundle) {
        if (bundle != null) {
            String str = ARG_TITLE;
            if (bundle.containsKey(str)) {
                setTitle(bundle.getString(str));
            }
            String str2 = ARG_HEADERS_STATE;
            if (bundle.containsKey(str2)) {
                setHeadersState(bundle.getInt(str2));
            }
        }
    }

    public void setHeadersState(int i) {
        if (i < 1 || i > 3) {
            throw new IllegalArgumentException("Invalid headers state: " + i);
        } else if (i != this.mHeadersState) {
            this.mHeadersState = i;
            if (i == 1) {
                this.mCanShowHeaders = true;
                this.mShowingHeaders = true;
            } else if (i == 2) {
                this.mCanShowHeaders = true;
                this.mShowingHeaders = false;
            } else if (i != 3) {
                Log.w(TAG, "Unknown headers state: " + i);
            } else {
                this.mCanShowHeaders = false;
                this.mShowingHeaders = false;
            }
            HeadersFragment headersFragment = this.mHeadersFragment;
            if (headersFragment != null) {
                headersFragment.setHeadersGone(!this.mCanShowHeaders);
            }
        }
    }

    public int getHeadersState() {
        return this.mHeadersState;
    }

    /* access modifiers changed from: protected */
    public Object createEntranceTransition() {
        return TransitionHelper.loadTransition(FragmentUtil.getContext(this), C0742R.C0746transition.lb_browse_entrance_transition);
    }

    /* access modifiers changed from: protected */
    public void runEntranceTransition(Object obj) {
        TransitionHelper.runTransition(this.mSceneAfterEntranceTransition, obj);
    }

    /* access modifiers changed from: protected */
    public void onEntranceTransitionPrepare() {
        this.mHeadersFragment.onTransitionPrepare();
        this.mMainFragmentAdapter.setEntranceTransitionState(false);
        this.mMainFragmentAdapter.onTransitionPrepare();
    }

    /* access modifiers changed from: protected */
    public void onEntranceTransitionStart() {
        this.mHeadersFragment.onTransitionStart();
        this.mMainFragmentAdapter.onTransitionStart();
    }

    /* access modifiers changed from: protected */
    public void onEntranceTransitionEnd() {
        MainFragmentAdapter mainFragmentAdapter = this.mMainFragmentAdapter;
        if (mainFragmentAdapter != null) {
            mainFragmentAdapter.onTransitionEnd();
        }
        HeadersFragment headersFragment = this.mHeadersFragment;
        if (headersFragment != null) {
            headersFragment.onTransitionEnd();
        }
    }

    /* access modifiers changed from: package-private */
    public void setSearchOrbViewOnScreen(boolean z) {
        int i;
        View searchAffordanceView = getTitleViewAdapter().getSearchAffordanceView();
        if (searchAffordanceView != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) searchAffordanceView.getLayoutParams();
            if (z) {
                i = 0;
            } else {
                i = -this.mContainerListMarginStart;
            }
            marginLayoutParams.setMarginStart(i);
            searchAffordanceView.setLayoutParams(marginLayoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    public void setEntranceTransitionStartState() {
        setHeadersOnScreen(false);
        setSearchOrbViewOnScreen(false);
    }

    /* access modifiers changed from: package-private */
    public void setEntranceTransitionEndState() {
        setHeadersOnScreen(this.mShowingHeaders);
        setSearchOrbViewOnScreen(true);
        this.mMainFragmentAdapter.setEntranceTransitionState(true);
    }

    private class ExpandPreLayout implements ViewTreeObserver.OnPreDrawListener {
        static final int STATE_FIRST_DRAW = 1;
        static final int STATE_INIT = 0;
        static final int STATE_SECOND_DRAW = 2;
        private final Runnable mCallback;
        private int mState;
        private final View mView;
        private MainFragmentAdapter mainFragmentAdapter;

        ExpandPreLayout(Runnable runnable, MainFragmentAdapter mainFragmentAdapter2, View view) {
            this.mView = view;
            this.mCallback = runnable;
            this.mainFragmentAdapter = mainFragmentAdapter2;
        }

        /* access modifiers changed from: package-private */
        public void execute() {
            this.mView.getViewTreeObserver().addOnPreDrawListener(this);
            this.mainFragmentAdapter.setExpand(false);
            this.mView.invalidate();
            this.mState = 0;
        }

        public boolean onPreDraw() {
            if (BrowseFragment.this.getView() == null || FragmentUtil.getContext(BrowseFragment.this) == null) {
                this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
            int i = this.mState;
            if (i == 0) {
                this.mainFragmentAdapter.setExpand(true);
                this.mView.invalidate();
                this.mState = 1;
                return false;
            } else if (i != 1) {
                return false;
            } else {
                this.mCallback.run();
                this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
                this.mState = 2;
                return false;
            }
        }
    }
}
