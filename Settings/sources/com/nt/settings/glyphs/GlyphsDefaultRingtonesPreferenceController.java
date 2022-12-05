package com.nt.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.widget.GlyphsContactsListPreference;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsDefaultRingtonesPreferenceController extends BasePreferenceController {
    private GlyphsContactsListPreference preference;

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

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsDefaultRingtonesPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setData(List<ContactsManager.Contact> list) {
        GlyphsContactsListPreference glyphsContactsListPreference = this.preference;
        if (glyphsContactsListPreference != null) {
            glyphsContactsListPreference.setContactsList(list);
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsContactsListPreference glyphsContactsListPreference = (GlyphsContactsListPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.preference = glyphsContactsListPreference;
        glyphsContactsListPreference.setOnItemClickListener(new GlyphsContactsListPreference.OnItemClickListener() { // from class: com.nt.settings.glyphs.GlyphsDefaultRingtonesPreferenceController.1
            @Override // com.nt.settings.glyphs.widget.GlyphsContactsListPreference.OnItemClickListener
            public void onItemClick(RecyclerView.Adapter adapter, View view, int i) {
                ContactsManager.Contact contact = ((GlyphsContactsListPreference.ContactListAdapter) adapter).getmContactLists().get(i);
                Intent intent = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
                intent.putExtra("contact_id", contact.getContactId());
                intent.putExtra("contact_name", contact.getDisplayName());
                intent.putExtra("page_title", ((AbstractPreferenceController) GlyphsDefaultRingtonesPreferenceController.this).mContext.getString(R.string.nt_glyphs_set_contact_ringtone_title));
                ((AbstractPreferenceController) GlyphsDefaultRingtonesPreferenceController.this).mContext.startActivity(intent);
            }
        });
    }
}
