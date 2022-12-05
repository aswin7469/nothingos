package com.nt.settings.glyphs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.OnActivityResultListener;
import com.android.settingslib.widget.settingsspinner.SettingsSpinner;
import com.android.settingslib.widget.settingsspinner.SettingsSpinnerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.utils.GlyphsSettings;
import com.nt.settings.glyphs.utils.ThreadUtils;
import com.nt.settings.glyphs.widget.ContactRecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
@TargetApi(5)
/* loaded from: classes2.dex */
public class GlyphsRingtonesPreferenceFragment extends InstrumentedFragment implements OnActivityResultListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, ContactRecyclerView.OnItemClickListener {
    private AppBarLayout mAppBarLayout;
    private ContactContentObserver mContactContentObserver;
    private ContactsManager.Contact mDefaultRingtone;
    private FilterSpinnerAdapter mFilterAdapter;
    private SettingsSpinner mFilterSpinner;
    private Future mFuture;
    private GlyphsSettings mGlyphsSettings;
    private ContactRecyclerView mRecyclerView;
    private View mRootView;
    private SearchView mSearchView;
    private View mSpinnerHeader;
    private List<ContactsManager.Contact> mTotalContacts;
    private Uri uri = ContactsContract.Contacts.CONTENT_URI;
    private boolean mIsSearchMode = false;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2002;
    }

    @Override // android.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (getActivity() == null) {
            return;
        }
        menuInflater.inflate(R.menu.app_seletor_apps, menu);
        MenuItem findItem = menu.findItem(R.id.search_app_list_menu);
        if (findItem == null) {
            return;
        }
        findItem.setOnActionExpandListener(this);
        SearchView searchView = (SearchView) findItem.getActionView();
        this.mSearchView = searchView;
        searchView.setQueryHint(getText(R.string.search_settings));
        this.mSearchView.setOnQueryTextListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        View inflate = layoutInflater.inflate(R.layout.nt_glyphs_contact_list_fragment, (ViewGroup) null);
        this.mRootView = inflate;
        ContactRecyclerView contactRecyclerView = (ContactRecyclerView) inflate.findViewById(R.id.crlv_contact_list);
        this.mRecyclerView = contactRecyclerView;
        contactRecyclerView.setOnItemClickListener(this);
        createHeader();
        updateData();
        disableToolBarScrollableBehavior();
        return this.mRootView;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        this.mGlyphsSettings = new GlyphsSettings(getActivity());
        ((SettingsActivity) getActivity()).setTitle(getActivity().getString(R.string.nt_glyphs_ringtones));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class FilterSpinnerAdapter extends SettingsSpinnerAdapter<CharSequence> {
        private List<ContactsManager.Contact> mContactList = new ArrayList();
        private final Context mContext;

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public FilterSpinnerAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public int getCount() {
            List<ContactsManager.Contact> list = this.mContactList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        /* renamed from: getItem */
        public CharSequence mo939getItem(int i) {
            return this.mContactList.get(i).getDisplayName();
        }
    }

    private void createHeader() {
        FrameLayout frameLayout = (FrameLayout) this.mRootView.findViewById(R.id.pinned_header);
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.nt_glyphs_contact_filter_spinner, (ViewGroup) frameLayout, false);
        this.mSpinnerHeader = inflate;
        this.mFilterSpinner = (SettingsSpinner) inflate.findViewById(R.id.filter_spinner);
        FilterSpinnerAdapter filterSpinnerAdapter = new FilterSpinnerAdapter(getActivity());
        this.mFilterAdapter = filterSpinnerAdapter;
        this.mFilterSpinner.setAdapter((SpinnerAdapter) filterSpinnerAdapter);
        this.mFilterSpinner.setVisibility(8);
        frameLayout.addView(this.mSpinnerHeader, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateData() {
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsRingtonesPreferenceFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtonesPreferenceFragment.this.lambda$updateData$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$2() {
        List<ContactsManager.Contact> searchContactsList = ContactsManager.getInstance().searchContactsList(getActivity());
        this.mTotalContacts = searchContactsList;
        if (searchContactsList != null && searchContactsList.size() > 0) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (ContactsManager.Contact contact : this.mTotalContacts) {
                if (TextUtils.isEmpty(contact.getRingtoneUri())) {
                    arrayList.add(contact);
                } else {
                    arrayList2.add(contact);
                    if ("Abra".equals(contact.getDisplayName())) {
                        this.mGlyphsSettings.setShowMusicItem(true);
                    }
                }
            }
            this.mTotalContacts.clear();
            this.mTotalContacts.add(getDefaultRingtone());
            if (arrayList2.size() > 0) {
                this.mTotalContacts.add(getCategory(getActivity().getString(R.string.nt_glyphs_custom_contact_ringtone)));
                this.mTotalContacts.addAll(arrayList2);
            }
            if (arrayList.size() > 0) {
                this.mTotalContacts.add(getCategory(getActivity().getString(R.string.nt_glyphs_all_contacts)));
                this.mTotalContacts.addAll(arrayList);
            }
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsRingtonesPreferenceFragment$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    GlyphsRingtonesPreferenceFragment.this.lambda$updateData$0();
                }
            });
            return;
        }
        this.mTotalContacts.add(getDefaultRingtone());
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsRingtonesPreferenceFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtonesPreferenceFragment.this.lambda$updateData$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$0() {
        this.mRecyclerView.setContactList(this.mTotalContacts);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$1() {
        this.mRecyclerView.setContactList(this.mTotalContacts);
    }

    private ContactsManager.Contact getDefaultRingtone() {
        if (this.mDefaultRingtone == null) {
            ContactsManager.Contact contact = new ContactsManager.Contact();
            this.mDefaultRingtone = contact;
            contact.setItemType(0);
            this.mDefaultRingtone.setDisplayName(getActivity().getString(R.string.nt_default_ringtones_title));
        }
        this.mDefaultRingtone.setRingtoneTitle(getDefaultRingtoneTitle());
        return this.mDefaultRingtone;
    }

    private String getDefaultRingtoneTitle() {
        try {
            return Ringtone.getTitle(getActivity(), RingtoneManager.getActualDefaultRingtoneUri(getActivity(), 1), false, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ContactsManager.Contact getCategory(String str) {
        ContactsManager.Contact contact = new ContactsManager.Contact();
        contact.setItemType(1);
        contact.setDisplayName(str);
        return contact;
    }

    private void showSearchLayout() {
        this.mFilterSpinner.setSelection(0);
    }

    private void showContentLayout() {
        this.mAppBarLayout.setExpanded(false, false);
        this.mSearchView.clearFocus();
        this.mFilterSpinner.setVisibility(8);
        this.mRecyclerView.setContactList(this.mTotalContacts);
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getDefaultRingtone();
        if (this.mRecyclerView.getAdapter() != null) {
            this.mRecyclerView.getAdapter().notifyItemChanged(0);
        }
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        registerContactDataChangeListener();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        unregisterContactDataChangeListener();
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        this.mIsSearchMode = true;
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, false);
        showSearchLayout();
        return true;
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        this.mIsSearchMode = false;
        this.mAppBarLayout.setExpanded(false, false);
        ViewCompat.setNestedScrollingEnabled(this.mRecyclerView, true);
        showContentLayout();
        return true;
    }

    private void disableToolBarScrollableBehavior() {
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() { // from class: com.nt.settings.glyphs.GlyphsRingtonesPreferenceFragment.1
            @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior.BaseDragCallback
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        ((CoordinatorLayout.LayoutParams) this.mAppBarLayout.getLayoutParams()).setBehavior(behavior);
    }

    @Override // android.widget.SearchView.OnQueryTextListener
    public boolean onQueryTextChange(String str) {
        this.mRecyclerView.search(str);
        return false;
    }

    private void registerContactDataChangeListener() {
        if (this.mContactContentObserver == null) {
            this.mContactContentObserver = new ContactContentObserver(new Handler());
        }
        getActivity().getContentResolver().registerContentObserver(this.uri, true, this.mContactContentObserver);
    }

    private void unregisterContactDataChangeListener() {
        getActivity().getContentResolver().unregisterContentObserver(this.mContactContentObserver);
    }

    @Override // com.nt.settings.glyphs.widget.ContactRecyclerView.OnItemClickListener
    public void onItemClick(View view, ContactsManager.Contact contact, int i) {
        if (this.mIsSearchMode) {
            this.mSearchView.clearFocus();
            getActivity().onBackPressed();
        }
        if (contact.getItemType() == 0) {
            Intent intent = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
            intent.putExtra("page_title", getActivity().getString(R.string.nt_default_ringtones_title));
            getActivity().startActivity(intent);
            return;
        }
        Intent intent2 = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
        intent2.putExtra("contact_id", contact.getContactId());
        intent2.putExtra("contact_name", contact.getDisplayName());
        intent2.putExtra("android.intent.extra.ringtone.EXISTING_URI", contact.getRingtoneUri());
        intent2.putExtra("page_title", getActivity().getString(R.string.nt_glyphs_set_contact_ringtone_title));
        getActivity().startActivity(intent2);
    }

    /* loaded from: classes2.dex */
    public class ContactContentObserver extends ContentObserver {
        public ContactContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            GlyphsRingtonesPreferenceFragment.this.updateData();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 1000) {
            Cursor managedQuery = getActivity().managedQuery(intent.getData(), null, null, null, null);
            if (managedQuery.moveToFirst()) {
                managedQuery.getString(managedQuery.getColumnIndex("display_name"));
                managedQuery.getString(managedQuery.getColumnIndex("has_phone_number"));
                managedQuery.getString(managedQuery.getColumnIndex("_id"));
            }
            managedQuery.close();
        }
    }
}
