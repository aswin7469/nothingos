package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.utils.ContactsManager;
import com.nothing.settings.glyphs.utils.LedSettingUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;

public class AddAppRingtoneController extends BasePreferenceController {
    private static final int SELECT_CONTACT_REQUEST_CODE = 1000;
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1001;
    private static final String TAG = "AddContactRingtoneCtr";
    /* access modifiers changed from: private */
    public String mContactId;
    /* access modifiers changed from: private */
    public String mContactName;

    private void selectContactFormIntent() {
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AddAppRingtoneController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey());
    }

    public void selectRingtone(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            Log.d(TAG, "contactId is null");
        } else {
            ThreadUtils.postOnMainThread(new AddAppRingtoneController$$ExternalSyntheticLambda0(this, str));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$selectRingtone$0(String str) {
        Context context = this.mContext;
        ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_select_ringtone_title), str, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1), 1, 0, SELECT_RINGTONE_REQUEST_CODE);
    }

    private void queryContact(final Uri uri) {
        this.mContactName = null;
        this.mContactId = null;
        ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
            public void run() {
                Cursor query = AddAppRingtoneController.this.mContext.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
                if (query != null) {
                    if (query.moveToFirst()) {
                        AddAppRingtoneController.this.mContactName = query.getString(query.getColumnIndex("display_name"));
                        String string = query.getString(query.getColumnIndex("has_phone_number"));
                        AddAppRingtoneController.this.mContactId = query.getString(query.getColumnIndex("_id"));
                        Log.d(AddAppRingtoneController.TAG, "name " + AddAppRingtoneController.this.mContactName + " hasPhone " + string + "  contactId " + AddAppRingtoneController.this.mContactId);
                        AddAppRingtoneController addAppRingtoneController = AddAppRingtoneController.this;
                        addAppRingtoneController.selectRingtone(addAppRingtoneController.mContactName, AddAppRingtoneController.this.mContactId);
                        query.close();
                        return;
                    }
                    query.close();
                }
            }
        });
    }

    public void onActivityControllerResult(int i, int i2, final Intent intent) {
        if (i2 != -1) {
            return;
        }
        if (i == SELECT_CONTACT_REQUEST_CODE) {
            queryContact(intent.getData());
        } else if (i == SELECT_RINGTONE_REQUEST_CODE) {
            ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
                public void run() {
                    Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
                    String str = "";
                    ContactsManager.getInstance().updateContactRingtone(AddAppRingtoneController.this.mContext, AddAppRingtoneController.this.mContactId, uri == null ? str : uri.toString());
                    if (uri != null) {
                        str = uri.toString();
                    }
                    LedSettingUtils.setContactLed(AddAppRingtoneController.this.mContext, "", "", "", str, AddAppRingtoneController.this.mContactId, intent.getIntExtra("key_sound_only", -1));
                }
            });
        }
    }
}
