package com.nt.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroupAdapter;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.nt.settings.utils.NtUtils;
/* loaded from: classes2.dex */
public class SettingsPreferenceFragment extends InstrumentedPreferenceFragment implements DialogCreatable {
    private String mHelpUrl;
    private Drawable mHighlightDrawable;
    private ViewGroup mPinnedHeaderFrameLayout;
    private String mPreferenceKey;
    private View mRootView;
    private View mScrollView;
    private boolean mPreferenceHighlighted = false;
    private boolean mNeedHightlightAnimation = true;
    private boolean mIsDataSetObserverRegistered = false;
    private DataSetObserver mDataSetObserver = new DataSetObserver() { // from class: com.nt.settings.SettingsPreferenceFragment.1
        @Override // android.database.DataSetObserver
        public void onChanged() {
            SettingsPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            SettingsPreferenceFragment.this.highlightPreferenceIfNeeded();
        }
    };
    private boolean mIsSavingInstanceState = false;
    private boolean mCanScrollOnTop = true;
    private View overScrollLayout = null;
    private View mView = null;

    @Override // com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        return 0;
    }

    protected int getHelpResource() {
        return 0;
    }

    protected void onBindPreferences() {
    }

    @Override // com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        return null;
    }

    @Override // com.nt.settings.core.lifecycle.ObservablePreferenceFragment
    public void onCreatePreferences(Bundle bundle, String str) {
    }

    protected void onUnbindPreferences() {
    }

    @Override // com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mPreferenceHighlighted = bundle.getBoolean("android:preference_highlighted");
        }
        int helpResource = getHelpResource();
        if (helpResource != 0) {
            this.mHelpUrl = getResources().getString(helpResource);
        }
    }

    @Override // com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mPinnedHeaderFrameLayout = (ViewGroup) onCreateView.findViewById(R.id.pinned_header);
        this.mRootView = onCreateView;
        return onCreateView;
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Preference preference;
        super.onViewCreated(view, bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null && preferenceScreen.getPreferenceCount() > 0 && (preference = preferenceScreen.getPreference(0)) != null && (preference instanceof PreferenceCategory)) {
            NtUtils.setViewAdjustToActionbarAndMenuBar(getContext(), view);
        }
        if (hasListView()) {
            this.mView = getListView();
        } else if (hasScrollView(view)) {
            this.mView = this.mScrollView;
        }
        View view2 = this.mView;
        if (view2 != null) {
            view2.setNestedScrollingEnabled(true);
            NtUtils.setViewPaddingBottom(getContext(), this.mView);
            if (this.mCanScrollOnTop) {
                this.mView.setOverScrollMode(0);
            } else {
                this.mView.setOverScrollMode(2);
            }
            this.mView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.nt.settings.SettingsPreferenceFragment.2
                @Override // android.view.View.OnScrollChangeListener
                public void onScrollChange(View view3, int i, int i2, int i3, int i4) {
                }
            });
        }
    }

    public boolean hasScrollView(View view) {
        if (this.mScrollView != null) {
            return true;
        }
        View findViewById = view.findViewById(R.id.scrollview);
        if (!(findViewById instanceof ScrollView)) {
            return false;
        }
        this.mScrollView = findViewById;
        return true;
    }

    @Override // com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mIsSavingInstanceState = true;
        bundle.putBoolean("android:preference_highlighted", this.mPreferenceHighlighted);
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (!TextUtils.isEmpty(this.mHelpUrl)) {
            setHasOptionsMenu(true);
        }
    }

    @Override // com.nt.settings.InstrumentedPreferenceFragment, com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.app.Fragment
    public void onResume() {
        Intent intent;
        super.onResume();
        this.mIsSavingInstanceState = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(":settings:fragment_args_key");
            this.mPreferenceKey = string;
            if (TextUtils.isEmpty(string) && (intent = getIntent()) != null) {
                this.mPreferenceKey = intent.getStringExtra(":settings:fragment_args_key");
            }
            highlightPreferenceIfNeeded();
        }
    }

    @Override // com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
    }

    public void highlightPreferenceIfNeeded() {
        if (this.mRootView == null || !isAdded() || this.mPreferenceHighlighted || TextUtils.isEmpty(this.mPreferenceKey)) {
            return;
        }
        this.mRootView.postDelayed(new Runnable() { // from class: com.nt.settings.SettingsPreferenceFragment.3
            @Override // java.lang.Runnable
            public void run() {
                SettingsPreferenceFragment settingsPreferenceFragment = SettingsPreferenceFragment.this;
                settingsPreferenceFragment.highlightPreference(settingsPreferenceFragment.mPreferenceKey);
            }
        }, 200L);
    }

    private Drawable getHighlightDrawable() {
        Activity activity;
        if (this.mHighlightDrawable == null && (activity = getActivity()) != null) {
            this.mHighlightDrawable = activity.getDrawable(R.drawable.dashboard_tile_background);
        }
        return this.mHighlightDrawable;
    }

    private int canUseListViewForHighLighting(String str) {
        ListAdapter adapter;
        if (hasListView() && (adapter = getListView().getAdapter()) != null && (adapter instanceof PreferenceGroupAdapter)) {
            return findListPositionFromKey(adapter, str);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void highlightPreference(String str) {
        int canUseListViewForHighLighting;
        Drawable highlightDrawable = getHighlightDrawable();
        if (highlightDrawable != null && (canUseListViewForHighLighting = canUseListViewForHighLighting(str)) >= 0) {
            this.mPreferenceHighlighted = true;
            ListView listView = getListView();
            PreferenceGroupAdapter adapter = listView.getAdapter();
            PreferenceGroupAdapter preferenceGroupAdapter = adapter;
            preferenceGroupAdapter.setHighlightedDrawable(highlightDrawable);
            preferenceGroupAdapter.setHighlighted(canUseListViewForHighLighting);
            listView.postDelayed(new AnonymousClass4(listView, canUseListViewForHighLighting, highlightDrawable, adapter), 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.nt.settings.SettingsPreferenceFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ ListAdapter val$adapter;
        final /* synthetic */ Drawable val$highlight;
        final /* synthetic */ ListView val$listView;
        final /* synthetic */ int val$position;

        AnonymousClass4(ListView listView, int i, Drawable drawable, ListAdapter listAdapter) {
            this.val$listView = listView;
            this.val$position = i;
            this.val$highlight = drawable;
            this.val$adapter = listAdapter;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SettingsPreferenceFragment.this.getView() != null && (SettingsPreferenceFragment.this.getView().getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                this.val$listView.setSelectionFromTop(this.val$position, -((ViewGroup.MarginLayoutParams) SettingsPreferenceFragment.this.getView().getLayoutParams()).topMargin);
            } else {
                this.val$listView.setSelection(this.val$position);
            }
            if (SettingsPreferenceFragment.this.mNeedHightlightAnimation) {
                this.val$listView.postDelayed(new Runnable() { // from class: com.nt.settings.SettingsPreferenceFragment.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass4 anonymousClass4 = AnonymousClass4.this;
                        int firstVisiblePosition = anonymousClass4.val$position - anonymousClass4.val$listView.getFirstVisiblePosition();
                        if (firstVisiblePosition < 0 || firstVisiblePosition >= AnonymousClass4.this.val$listView.getChildCount()) {
                            return;
                        }
                        final View childAt = AnonymousClass4.this.val$listView.getChildAt(firstVisiblePosition);
                        AnonymousClass4.this.val$highlight.setHotspot(childAt.getWidth() / 2, childAt.getHeight() / 2);
                        childAt.setBackground(AnonymousClass4.this.val$highlight);
                        childAt.setPressed(true);
                        childAt.postDelayed(new Runnable() { // from class: com.nt.settings.SettingsPreferenceFragment.4.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                childAt.setPressed(false);
                                childAt.setBackground(null);
                                AnonymousClass4.this.val$adapter.setHighlighted(-1);
                            }
                        }, 400L);
                    }
                }, 400L);
            }
        }
    }

    private int findListPositionFromKey(ListAdapter listAdapter, String str) {
        String key;
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            Object item = listAdapter.getItem(i);
            if ((item instanceof Preference) && (key = ((Preference) item).getKey()) != null && key.equals(str)) {
                if (item instanceof PreferenceCategory) {
                    this.mNeedHightlightAnimation = false;
                }
                return i;
            }
        }
        return -1;
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (this.mHelpUrl != null) {
            getActivity();
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        isRemoving();
        super.onDetach();
    }

    protected Context getPrefContext() {
        return getActivity();
    }

    @Override // android.app.Fragment
    public Context getContext() {
        return getPrefContext();
    }

    protected Intent getIntent() {
        if (getActivity() == null) {
            return null;
        }
        return getActivity().getIntent();
    }

    @Override // android.preference.PreferenceFragment
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getIntent() != null) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        if (preference.getFragment() != null) {
            return ((SettingsActivity) getActivity()).onPreferenceStartFragment(this, preference);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
