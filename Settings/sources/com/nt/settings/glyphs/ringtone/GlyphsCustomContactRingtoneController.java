package com.nt.settings.glyphs.ringtone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.pick.GlyphsPickResultHelper;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.utils.GlyphsSettings;
import com.nt.settings.glyphs.utils.LedSettingUtils;
import com.nt.settings.glyphs.utils.UrlUtil;
import com.nt.settings.glyphs.widget.PrimaryDeletePreference;
import java.util.List;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class GlyphsCustomContactRingtoneController extends BasePreferenceController implements OnDestroy {
    private static final String KEY_ITEM_PREFIX = "key_glyphs_contact_";
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1003;
    private static final String TAG = "CustomRingtoneCtr";
    private ContactContentObserver mContactContentObserver;
    private Future mFuture;
    private GlyphsSettings mGlyphsSettings;
    private PreferenceCategory mPreferenceCategory;
    private PreferenceScreen mScreen;
    private List<ContactsManager.Contact> mTotalContacts;
    private Uri uri = ContactsContract.Contacts.CONTENT_URI;
    private ContactsManager.Contact mSelectedContact = null;

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

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterContactDataChangeListener();
    }

    /* loaded from: classes2.dex */
    public class ContactContentObserver extends ContentObserver {
        public ContactContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            Log.d(GlyphsCustomContactRingtoneController.TAG, "selfChange " + z);
            GlyphsCustomContactRingtoneController.this.updateData();
        }
    }

    public GlyphsCustomContactRingtoneController(Context context, String str) {
        super(context, str);
        registerContactDataChangeListener();
        this.mGlyphsSettings = new GlyphsSettings(context);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        queryContact();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectRingtone(String str, Uri uri) {
        int i;
        if (uri != null) {
            String param = UrlUtil.getParam(uri.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                try {
                    i = Integer.parseInt(param);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Context context = this.mContext;
                GlyphsPickResultHelper.startRingtoneSoundSelector((Activity) context, context.getString(R.string.nt_glyphs_select_ringtone_title), str, uri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
            }
        }
        i = 0;
        Context context2 = this.mContext;
        GlyphsPickResultHelper.startRingtoneSoundSelector((Activity) context2, context2.getString(R.string.nt_glyphs_select_ringtone_title), str, uri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
    }

    private void registerContactDataChangeListener() {
        if (this.mContactContentObserver == null) {
            this.mContactContentObserver = new ContactContentObserver(new Handler());
        }
        this.mContext.getContentResolver().registerContentObserver(this.uri, true, this.mContactContentObserver);
    }

    private void unregisterContactDataChangeListener() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContactContentObserver);
    }

    private void queryContact() {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.1
            @Override // java.lang.Runnable
            public void run() {
                GlyphsCustomContactRingtoneController.this.updateData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateData() {
        Log.d(TAG, "updateData");
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsCustomContactRingtoneController.this.lambda$updateData$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$0() {
        this.mTotalContacts = ContactsManager.getInstance().searchCustomRingtoneContactsList(this.mContext);
        if (this.mPreferenceCategory == null) {
            return;
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.2
            @Override // java.lang.Runnable
            public void run() {
                Preference preference = GlyphsCustomContactRingtoneController.this.mPreferenceCategory.getPreference(0);
                GlyphsCustomContactRingtoneController.this.mPreferenceCategory.removeAll();
                GlyphsCustomContactRingtoneController.this.mScreen.removePreference(GlyphsCustomContactRingtoneController.this.mPreferenceCategory);
                GlyphsCustomContactRingtoneController.this.mScreen.addPreference(GlyphsCustomContactRingtoneController.this.mPreferenceCategory);
                GlyphsCustomContactRingtoneController.this.mPreferenceCategory.addPreference(preference);
                if (GlyphsCustomContactRingtoneController.this.mTotalContacts == null || GlyphsCustomContactRingtoneController.this.mTotalContacts.size() <= 0) {
                    return;
                }
                for (ContactsManager.Contact contact : GlyphsCustomContactRingtoneController.this.mTotalContacts) {
                    if ("Abra".equals(contact.getDisplayName())) {
                        GlyphsCustomContactRingtoneController.this.mGlyphsSettings.setShowMusicItem(true);
                    }
                    PrimaryDeletePreference primaryDeletePreference = new PrimaryDeletePreference(((AbstractPreferenceController) GlyphsCustomContactRingtoneController.this).mContext);
                    primaryDeletePreference.setPersistent(false);
                    primaryDeletePreference.setData(contact);
                    primaryDeletePreference.setKey(GlyphsCustomContactRingtoneController.KEY_ITEM_PREFIX + contact.getContactId());
                    primaryDeletePreference.setOnSelectedListener(new PrimaryDeletePreference.OnSelectedListener() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.2.1
                        @Override // com.nt.settings.glyphs.widget.PrimaryDeletePreference.OnSelectedListener
                        public void onClickDelete(Preference preference2, ContactsManager.Contact contact2) {
                            ContactsManager.Contact contact3 = GlyphsCustomContactRingtoneController.this.getContact(preference2.getKey());
                            if (contact3 != null) {
                                GlyphsCustomContactRingtoneController.this.showDeleteDialog(preference2, contact3.getDisplayName(), contact3.getContactId());
                            }
                        }

                        @Override // com.nt.settings.glyphs.widget.PrimaryDeletePreference.OnSelectedListener
                        public void onClick(Preference preference2, ContactsManager.Contact contact2) {
                            ContactsManager.Contact contact3 = GlyphsCustomContactRingtoneController.this.getContact(preference2.getKey());
                            if (contact3 == null) {
                                return;
                            }
                            Uri uri = null;
                            if (!TextUtils.isEmpty(contact3.getRingtoneUri())) {
                                uri = Uri.parse(contact3.getRingtoneUri());
                            }
                            GlyphsCustomContactRingtoneController.this.mSelectedContact = contact3;
                            GlyphsCustomContactRingtoneController.this.selectRingtone(contact3.getDisplayName(), uri);
                        }
                    });
                    GlyphsCustomContactRingtoneController.this.mPreferenceCategory.addPreference(primaryDeletePreference);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ContactsManager.Contact getContact(String str) {
        List<ContactsManager.Contact> list = this.mTotalContacts;
        if (list != null && list.size() != 0 && !TextUtils.isEmpty(str)) {
            for (ContactsManager.Contact contact : this.mTotalContacts) {
                if (TextUtils.equals(KEY_ITEM_PREFIX + contact.getContactId(), str)) {
                    return contact;
                }
            }
        }
        return null;
    }

    private void updateContact(String str, String str2) {
        List<ContactsManager.Contact> list = this.mTotalContacts;
        if (list == null || list.size() == 0) {
            return;
        }
        for (ContactsManager.Contact contact : this.mTotalContacts) {
            if (TextUtils.equals(str, contact.getContactId())) {
                contact.setRingtoneUri(str2);
                PreferenceCategory preferenceCategory = this.mPreferenceCategory;
                Preference findPreference = preferenceCategory.findPreference(KEY_ITEM_PREFIX + contact.getContactId());
                if (findPreference == null || !(findPreference instanceof PrimaryDeletePreference)) {
                    return;
                }
                ((PrimaryDeletePreference) findPreference).setData(contact);
                return;
            }
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i2 == -1 && i == SELECT_RINGTONE_REQUEST_CODE && this.mSelectedContact != null) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            String str = "";
            ContactsManager.getInstance().updateContactRingtone(this.mContext, this.mSelectedContact.getContactId(), uri == null ? str : uri.toString());
            LedSettingUtils.setContactLed(this.mContext, "", "", "", uri == null ? str : uri.toString(), this.mSelectedContact.getContactId(), intent.getIntExtra("key_sound_only", -1));
            String contactId = this.mSelectedContact.getContactId();
            if (uri != null) {
                str = uri.toString();
            }
            updateContact(contactId, str);
        }
    }

    public void showDeleteDialog(Preference preference, String str, String str2) {
        onCreateDialog(preference, str, str2).show();
    }

    public Dialog onCreateDialog(final Preference preference, String str, final String str2) {
        return new AlertDialog.Builder(this.mContext).setTitle(R.string.nt_glyphs_remove_custom_ringtone_title).setMessage(this.mContext.getString(R.string.nt_glyphs_remove_custom_ringtone_message, str)).setNegativeButton(R.string.nt_glyphs_cancel, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(R.string.nt_glyphs_remove, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                final boolean z;
                Log.d(GlyphsCustomContactRingtoneController.TAG, " " + Thread.currentThread().getName() + " contactId " + str2 + "  key " + preference.getKey());
                if (GlyphsCustomContactRingtoneController.this.mScreen != null) {
                    PreferenceScreen preferenceScreen = GlyphsCustomContactRingtoneController.this.mScreen;
                    z = preferenceScreen.removePreferenceRecursively(GlyphsCustomContactRingtoneController.KEY_ITEM_PREFIX + str2);
                } else {
                    z = false;
                }
                Log.d(GlyphsCustomContactRingtoneController.TAG, " result " + z);
                ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsCustomContactRingtoneController.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ContactsManager.getInstance().updateContactRingtone(((AbstractPreferenceController) GlyphsCustomContactRingtoneController.this).mContext, str2, null);
                        if (!z) {
                            GlyphsCustomContactRingtoneController.this.updateData();
                        }
                    }
                });
            }
        }).create();
    }
}
