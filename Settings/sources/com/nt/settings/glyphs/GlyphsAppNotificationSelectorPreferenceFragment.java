package com.nt.settings.glyphs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFrameLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.applications.manageapplications.AppFilterRegistry;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.utils.ThreadUtils;
import com.google.android.material.appbar.AppBarLayout;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsAppNotificationSelectorPreferenceFragment extends InstrumentedFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_app_notification_selector);
    private AppBarLayout mAppBarLayout;
    private AppListAdapter mApplications;
    private ApplicationsState mApplicationsState;
    boolean mExpandSearch;
    private View mLoadingContainer;
    private Menu mOptionsMenu;
    RecyclerView mRecyclerView;
    private View mRootView;
    private SearchView mSearchView;
    private ApplicationsState.Session mSession;
    private List<String> selectedPackages = new ArrayList();

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2001;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override // android.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    void updateOptionsMenu() {
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.nt_glyphs_apps_selector, (ViewGroup) null);
        this.mRootView = inflate;
        this.mLoadingContainer = inflate.findViewById(R.id.loading_container);
        this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R.id.apps_list);
        this.mApplications = new AppListAdapter(getActivity(), null);
        this.mRecyclerView.setItemAnimator(null);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.mRecyclerView.setAdapter(this.mApplications);
        if (viewGroup instanceof PreferenceFrameLayout) {
            this.mRootView.getLayoutParams().removeBorders = true;
        }
        this.mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        disableToolBarScrollableBehavior();
        getActivity().setTitle(R.string.nt_glyphs_select_app_title);
        return this.mRootView;
    }

    private void disableToolBarScrollableBehavior() {
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() { // from class: com.nt.settings.glyphs.GlyphsAppNotificationSelectorPreferenceFragment.1
            @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior.BaseDragCallback
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        ((CoordinatorLayout.LayoutParams) this.mAppBarLayout.getLayoutParams()).setBehavior(behavior);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ApplicationsState applicationsState = ApplicationsState.getInstance(getActivity().getApplication());
        this.mApplicationsState = applicationsState;
        this.mSession = applicationsState.newSession(new ApplicationsState.Callbacks() { // from class: com.nt.settings.glyphs.GlyphsAppNotificationSelectorPreferenceFragment.2
            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onLauncherInfoChanged() {
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onPackageIconChanged() {
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onPackageListChanged() {
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onPackageSizeChanged(String str) {
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onRunningStateChanged(boolean z) {
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
                GlyphsAppNotificationSelectorPreferenceFragment.this.mLoadingContainer.setVisibility(8);
                GlyphsAppNotificationSelectorPreferenceFragment.this.mRecyclerView.setVisibility(0);
                GlyphsAppNotificationSelectorPreferenceFragment.this.mApplications.setNewData(arrayList);
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onAllSizesComputed() {
                GlyphsAppNotificationSelectorPreferenceFragment.this.getAppList();
            }

            @Override // com.android.settingslib.applications.ApplicationsState.Callbacks
            public void onLoadEntriesCompleted() {
                if (GlyphsAppNotificationSelectorPreferenceFragment.this.mLoadingContainer == null) {
                    return;
                }
                GlyphsAppNotificationSelectorPreferenceFragment.this.mLoadingContainer.setVisibility(8);
                GlyphsAppNotificationSelectorPreferenceFragment.this.mRecyclerView.setVisibility(0);
            }
        });
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (getActivity() == null) {
            return;
        }
        this.mOptionsMenu = menu;
        menuInflater.inflate(R.menu.app_seletor_apps, menu);
        MenuItem findItem = menu.findItem(R.id.search_app_list_menu);
        if (findItem != null) {
            SearchView searchView = (SearchView) findItem.getActionView();
            this.mSearchView = searchView;
            searchView.setQueryHint(getText(R.string.search_settings));
            this.mSearchView.setOnQueryTextListener(this);
            if (this.mExpandSearch) {
                findItem.expandActionView();
            }
        }
        updateOptionsMenu();
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        ApplicationsState.Session session = this.mSession;
        if (session == null) {
            return;
        }
        session.onResume();
        getAppList();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class AppListAdapter extends RecyclerView.Adapter<AppsViewHolder> {
        private LayoutInflater inflater;
        private List<ApplicationsState.AppEntry> mEntries;
        private List<ApplicationsState.AppEntry> mOriginalEntries;
        private SearchFilter mSearchFilter;

        public AppListAdapter(Context context, List<ApplicationsState.AppEntry> list) {
            this.mEntries = list;
            this.mOriginalEntries = list;
            this.inflater = LayoutInflater.from(context);
        }

        public void setNewData(List<ApplicationsState.AppEntry> list) {
            this.mEntries = list;
            this.mOriginalEntries = list;
            notifyDataSetChanged();
        }

        void filterSearch(String str) {
            if (this.mSearchFilter == null) {
                this.mSearchFilter = new SearchFilter();
            }
            if (this.mOriginalEntries == null) {
                Log.w("AppNotificationSelector", "Apps haven't loaded completely yet, so nothing can be filtered");
            } else {
                this.mSearchFilter.filter(str);
            }
        }

        /* loaded from: classes2.dex */
        public class SearchFilter extends Filter {
            public SearchFilter() {
            }

            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                List list;
                if (TextUtils.isEmpty(charSequence)) {
                    list = AppListAdapter.this.mOriginalEntries;
                } else {
                    ArrayList arrayList = new ArrayList();
                    for (ApplicationsState.AppEntry appEntry : AppListAdapter.this.mOriginalEntries) {
                        if (appEntry.label.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList.add(appEntry);
                        }
                    }
                    list = arrayList;
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = list;
                filterResults.count = list.size();
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                AppListAdapter.this.mEntries = (ArrayList) filterResults.values;
                AppListAdapter.this.notifyDataSetChanged();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public AppsViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new AppsViewHolder(this.inflater.inflate(R.layout.nt_glyphs_item_apps_select_list, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(AppsViewHolder appsViewHolder, int i) {
            ApplicationsState.AppEntry appEntry = this.mEntries.get(i);
            setTitle(appsViewHolder, appEntry.label, appEntry.labelDescription);
            setIcon(appsViewHolder, appEntry.icon);
            setCheckedStaus(appsViewHolder, appEntry.info.packageName);
        }

        private void setTitle(AppsViewHolder appsViewHolder, CharSequence charSequence, CharSequence charSequence2) {
            if (charSequence == null) {
                return;
            }
            appsViewHolder.tvAppName.setText(charSequence);
            if (TextUtils.isEmpty(charSequence2)) {
                return;
            }
            appsViewHolder.tvAppName.setContentDescription(charSequence2);
        }

        private void setIcon(AppsViewHolder appsViewHolder, Drawable drawable) {
            if (drawable == null) {
                return;
            }
            appsViewHolder.imgIcon.setImageDrawable(drawable);
        }

        private void setCheckedStaus(AppsViewHolder appsViewHolder, final String str) {
            appsViewHolder.cbStatus.setChecked(GlyphsAppNotificationSelectorPreferenceFragment.this.selectedPackages.contains(str));
            appsViewHolder.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.glyphs.GlyphsAppNotificationSelectorPreferenceFragment.AppListAdapter.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z) {
                        if (GlyphsAppNotificationSelectorPreferenceFragment.this.selectedPackages.contains(str)) {
                            return;
                        }
                        GlyphsAppNotificationSelectorPreferenceFragment.this.selectedPackages.add(str);
                        return;
                    }
                    GlyphsAppNotificationSelectorPreferenceFragment.this.selectedPackages.remove(str);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<ApplicationsState.AppEntry> list = this.mEntries;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        /* loaded from: classes2.dex */
        public class AppsViewHolder extends RecyclerView.ViewHolder {
            public CheckBox cbStatus;
            public ImageView imgIcon;
            public TextView tvAppName;

            public AppsViewHolder(View view) {
                super(view);
                this.imgIcon = (ImageView) view.findViewById(R.id.img_app_icon);
                this.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
                this.cbStatus = (CheckBox) view.findViewById(R.id.cb_status);
            }
        }
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, false);
        return true;
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, true);
        return true;
    }

    @Override // android.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextChange(String str) {
        this.mApplications.filterSearch(str);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAppList() {
        if (this.mApplications.getItemCount() == 0) {
            this.mLoadingContainer.setVisibility(0);
            this.mRecyclerView.setVisibility(8);
        } else {
            this.mLoadingContainer.setVisibility(8);
            this.mRecyclerView.setVisibility(0);
        }
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsAppNotificationSelectorPreferenceFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsAppNotificationSelectorPreferenceFragment.this.lambda$getAppList$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAppList$0() {
        this.mSession.rebuild(new ApplicationsState.CompoundFilter(AppFilterRegistry.getInstance().get(4).getFilter(), ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER_AND_INSTANT), ApplicationsState.ALPHA_COMPARATOR, false);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ApplicationsState.Session session = this.mSession;
        if (session == null) {
            return;
        }
        session.onDestroy();
    }
}
