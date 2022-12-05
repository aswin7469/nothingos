package com.nt.settings.glyphs.ringtone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.pick.GlyphsPickResultHelper;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.utils.LedSettingUtils;
/* loaded from: classes2.dex */
public class GlyphsAddContactRingtoneController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_CONTACT_REQUEST_CODE = 1000;
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1001;
    private static final String TAG = "AddContactRingtoneCtr";
    private String mContactId;
    private String mContactName;

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

    public GlyphsAddContactRingtoneController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
    }

    private void selectContactFormIntent() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/contact");
        ((Activity) this.mContext).startActivityForResult(intent, SELECT_CONTACT_REQUEST_CODE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectRingtone(final String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            Log.d(TAG, "contactId is null");
        } else {
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsAddContactRingtoneController.1
                @Override // java.lang.Runnable
                public void run() {
                    GlyphsPickResultHelper.startRingtoneSoundSelector((Activity) ((AbstractPreferenceController) GlyphsAddContactRingtoneController.this).mContext, ((AbstractPreferenceController) GlyphsAddContactRingtoneController.this).mContext.getString(R.string.nt_glyphs_select_ringtone_title), str, null, 1, 0, GlyphsAddContactRingtoneController.SELECT_RINGTONE_REQUEST_CODE);
                }
            });
        }
    }

    private void queryContact(final Uri uri) {
        this.mContactName = null;
        this.mContactId = null;
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsAddContactRingtoneController.2
            @Override // java.lang.Runnable
            public void run() {
                Cursor query = ((AbstractPreferenceController) GlyphsAddContactRingtoneController.this).mContext.getContentResolver().query(uri, null, null, null, null);
                if (query != null) {
                    if (query.moveToFirst()) {
                        GlyphsAddContactRingtoneController.this.mContactName = query.getString(query.getColumnIndex("display_name"));
                        String string = query.getString(query.getColumnIndex("has_phone_number"));
                        GlyphsAddContactRingtoneController.this.mContactId = query.getString(query.getColumnIndex("_id"));
                        Log.d(GlyphsAddContactRingtoneController.TAG, "name " + GlyphsAddContactRingtoneController.this.mContactName + " hasPhone " + string + "  contactId " + GlyphsAddContactRingtoneController.this.mContactId);
                        GlyphsAddContactRingtoneController glyphsAddContactRingtoneController = GlyphsAddContactRingtoneController.this;
                        glyphsAddContactRingtoneController.selectRingtone(glyphsAddContactRingtoneController.mContactName, GlyphsAddContactRingtoneController.this.mContactId);
                        query.close();
                        return;
                    }
                    query.close();
                }
            }
        });
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, final Intent intent) {
        if (i2 != -1) {
            return;
        }
        if (i == SELECT_CONTACT_REQUEST_CODE) {
            queryContact(intent.getData());
        } else if (i != SELECT_RINGTONE_REQUEST_CODE) {
        } else {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsAddContactRingtoneController.3
                @Override // java.lang.Runnable
                public void run() {
                    Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
                    String str = "";
                    ContactsManager.getInstance().updateContactRingtone(((AbstractPreferenceController) GlyphsAddContactRingtoneController.this).mContext, GlyphsAddContactRingtoneController.this.mContactId, uri == null ? str : uri.toString());
                    Context context = ((AbstractPreferenceController) GlyphsAddContactRingtoneController.this).mContext;
                    if (uri != null) {
                        str = uri.toString();
                    }
                    LedSettingUtils.setContactLed(context, "", "", "", str, GlyphsAddContactRingtoneController.this.mContactId, intent.getIntExtra("key_sound_only", -1));
                }
            });
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        selectContactFormIntent();
        return true;
    }
}
