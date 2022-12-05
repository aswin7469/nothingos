package com.nt.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.widget.GlyphsContactsListPreference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsSearchRingtonesPreferenceController extends BasePreferenceController {
    private List<ContactsManager.Contact> mContactLists;
    private GlyphsContactsListPreference mPreference;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public final void updateState(Preference preference) {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsSearchRingtonesPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (GlyphsContactsListPreference) preferenceScreen.findPreference(getPreferenceKey());
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsSearchRingtonesPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsSearchRingtonesPreferenceController.this.lambda$displayPreference$0();
            }
        });
        this.mPreference.setOnItemClickListener(new GlyphsContactsListPreference.OnItemClickListener() { // from class: com.nt.settings.glyphs.GlyphsSearchRingtonesPreferenceController.1
            @Override // com.nt.settings.glyphs.widget.GlyphsContactsListPreference.OnItemClickListener
            public void onItemClick(RecyclerView.Adapter adapter, View view, int i) {
                Intent intent = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
                intent.putExtra("contact_id", ((GlyphsContactsListPreference.ContactListAdapter) adapter).getmContactLists().get(i).getContactId());
                intent.putExtra("page_title", ((AbstractPreferenceController) GlyphsSearchRingtonesPreferenceController.this).mContext.getString(R.string.nt_glyphs_set_contact_ringtone_title));
                ((AbstractPreferenceController) GlyphsSearchRingtonesPreferenceController.this).mContext.startActivity(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0() {
        List<ContactsManager.Contact> searchContactsList = ContactsManager.getInstance().searchContactsList(this.mContext);
        this.mContactLists = searchContactsList;
        this.mPreference.setContactsList(searchContactsList);
    }

    public void searchContact(String str) {
        String[] split;
        if (TextUtils.isEmpty(str)) {
            this.mPreference.setContactsList(this.mContactLists);
        } else if (this.mContactLists != null) {
            ArrayList arrayList = new ArrayList();
            for (ContactsManager.Contact contact : this.mContactLists) {
                String displayName = contact.getDisplayName();
                if (!TextUtils.isEmpty(displayName) && (split = displayName.split(" ")) != null && split.length != 0) {
                    for (String str2 : split) {
                        if (str2.toLowerCase().startsWith(str.toLowerCase())) {
                            arrayList.add(contact);
                        }
                    }
                }
            }
            this.mPreference.setContactsList(arrayList);
        }
    }
}
