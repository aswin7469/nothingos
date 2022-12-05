package com.android.internal.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.internal.R;
import com.android.internal.app.AbstractMultiProfilePagerAdapter;
/* loaded from: classes4.dex */
public class ResolverMultiProfilePagerAdapter extends AbstractMultiProfilePagerAdapter {
    private final ResolverProfileDescriptor[] mItems;
    private final boolean mShouldShowNoCrossProfileIntentsEmptyState;
    private boolean mUseLayoutWithDefault;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResolverMultiProfilePagerAdapter(Context context, ResolverListAdapter adapter, UserHandle personalProfileUserHandle, UserHandle workProfileUserHandle) {
        super(context, 0, personalProfileUserHandle, workProfileUserHandle);
        this.mItems = new ResolverProfileDescriptor[]{createProfileDescriptor(adapter)};
        this.mShouldShowNoCrossProfileIntentsEmptyState = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResolverMultiProfilePagerAdapter(Context context, ResolverListAdapter personalAdapter, ResolverListAdapter workAdapter, int defaultProfile, UserHandle personalProfileUserHandle, UserHandle workProfileUserHandle, boolean shouldShowNoCrossProfileIntentsEmptyState) {
        super(context, defaultProfile, personalProfileUserHandle, workProfileUserHandle);
        this.mItems = new ResolverProfileDescriptor[]{createProfileDescriptor(personalAdapter), createProfileDescriptor(workAdapter)};
        this.mShouldShowNoCrossProfileIntentsEmptyState = shouldShowNoCrossProfileIntentsEmptyState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    public void updateAfterConfigChange() {
        ResolverProfileDescriptor[] resolverProfileDescriptorArr;
        super.updateAfterConfigChange();
        for (ResolverProfileDescriptor descriptor : this.mItems) {
            View emptyStateCont = descriptor.rootView.findViewById(R.id.resolver_empty_state_container);
            Resources resources = getContext().getResources();
            emptyStateCont.setPadding(emptyStateCont.getPaddingLeft(), resources.getDimensionPixelSize(R.dimen.resolver_empty_state_container_padding_top), emptyStateCont.getPaddingRight(), resources.getDimensionPixelSize(R.dimen.resolver_empty_state_container_padding_bottom));
        }
    }

    private ResolverProfileDescriptor createProfileDescriptor(ResolverListAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.resolver_list_per_profile, (ViewGroup) null, false);
        return new ResolverProfileDescriptor(rootView, adapter);
    }

    ListView getListViewForIndex(int index) {
        return mo3324getItem(index).listView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getItem  reason: collision with other method in class */
    public ResolverProfileDescriptor mo3324getItem(int pageIndex) {
        return this.mItems[pageIndex];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    public int getItemCount() {
        return this.mItems.length;
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    void setupListAdapter(int pageIndex) {
        ListView listView = mo3324getItem(pageIndex).listView;
        listView.setAdapter((ListAdapter) mo3324getItem(pageIndex).resolverListAdapter);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getAdapterForIndex */
    public ResolverListAdapter mo3322getAdapterForIndex(int pageIndex) {
        return this.mItems[pageIndex].resolverListAdapter;
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter, com.android.internal.widget.PagerAdapter
    /* renamed from: instantiateItem */
    public ViewGroup mo3325instantiateItem(ViewGroup container, int position) {
        setupListAdapter(position);
        return super.mo3325instantiateItem(container, position);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getListAdapterForUserHandle */
    public ResolverListAdapter mo3316getListAdapterForUserHandle(UserHandle userHandle) {
        if (mo3310getActiveListAdapter().getUserHandle().equals(userHandle)) {
            return mo3310getActiveListAdapter();
        }
        if (mo3314getInactiveListAdapter() != null && mo3314getInactiveListAdapter().getUserHandle().equals(userHandle)) {
            return mo3314getInactiveListAdapter();
        }
        return null;
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getActiveListAdapter */
    public ResolverListAdapter mo3310getActiveListAdapter() {
        return mo3322getAdapterForIndex(getCurrentPage());
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getInactiveListAdapter */
    public ResolverListAdapter mo3314getInactiveListAdapter() {
        if (getCount() == 1) {
            return null;
        }
        return mo3322getAdapterForIndex(1 - getCurrentPage());
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    public ResolverListAdapter getPersonalListAdapter() {
        return mo3322getAdapterForIndex(0);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    public ResolverListAdapter getWorkListAdapter() {
        return mo3322getAdapterForIndex(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getCurrentRootAdapter */
    public ResolverListAdapter mo3323getCurrentRootAdapter() {
        return mo3310getActiveListAdapter();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getActiveAdapterView  reason: collision with other method in class */
    public ListView mo3321getActiveAdapterView() {
        return getListViewForIndex(getCurrentPage());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    /* renamed from: getInactiveAdapterView */
    public ViewGroup mo3313getInactiveAdapterView() {
        if (getCount() == 1) {
            return null;
        }
        return getListViewForIndex(1 - getCurrentPage());
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    String getMetricsCategory() {
        return "intent_resolver";
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    boolean allowShowNoCrossProfileIntentsEmptyState() {
        return this.mShouldShowNoCrossProfileIntentsEmptyState;
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void showWorkProfileOffEmptyState(ResolverListAdapter activeListAdapter, View.OnClickListener listener) {
        showEmptyState(activeListAdapter, R.drawable.ic_work_apps_off, R.string.resolver_turn_on_work_apps, 0, listener);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void showNoPersonalToWorkIntentsEmptyState(ResolverListAdapter activeListAdapter) {
        showEmptyState(activeListAdapter, R.drawable.ic_sharing_disabled, R.string.resolver_cross_profile_blocked, R.string.resolver_cant_access_work_apps_explanation);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void showNoWorkToPersonalIntentsEmptyState(ResolverListAdapter activeListAdapter) {
        showEmptyState(activeListAdapter, R.drawable.ic_sharing_disabled, R.string.resolver_cross_profile_blocked, R.string.resolver_cant_access_personal_apps_explanation);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void showNoPersonalAppsAvailableEmptyState(ResolverListAdapter listAdapter) {
        showEmptyState(listAdapter, R.drawable.ic_no_apps, R.string.resolver_no_personal_apps_available, 0);
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void showNoWorkAppsAvailableEmptyState(ResolverListAdapter listAdapter) {
        showEmptyState(listAdapter, R.drawable.ic_no_apps, R.string.resolver_no_work_apps_available, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUseLayoutWithDefault(boolean useLayoutWithDefault) {
        this.mUseLayoutWithDefault = useLayoutWithDefault;
    }

    @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter
    protected void setupContainerPadding(View container) {
        int bottom = this.mUseLayoutWithDefault ? container.getPaddingBottom() : 0;
        container.setPadding(container.getPaddingLeft(), container.getPaddingTop(), container.getPaddingRight(), bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class ResolverProfileDescriptor extends AbstractMultiProfilePagerAdapter.ProfileDescriptor {
        final ListView listView;
        private ResolverListAdapter resolverListAdapter;

        ResolverProfileDescriptor(ViewGroup rootView, ResolverListAdapter adapter) {
            super(rootView);
            this.resolverListAdapter = adapter;
            this.listView = (ListView) rootView.findViewById(R.id.resolver_list);
        }
    }
}
