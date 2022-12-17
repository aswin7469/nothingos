package com.nothing.settings.glyphs.ringtone;

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
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.PrimaryDeletePreference;
import com.nothing.settings.glyphs.utils.ContactsManager;
import com.nothing.settings.glyphs.utils.GlyphsSettings;
import com.nothing.settings.glyphs.utils.LedSettingUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import java.util.List;
import java.util.concurrent.Future;

public class CustomContactRingtoneController extends BasePreferenceController implements OnDestroy {
    private static final String KEY_ITEM_PREFIX = "key_glyphs_contact_";
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1003;
    private static final String TAG = "CustomRingtoneCtr";
    private ContactContentObserver mContactContentObserver;
    private Future mFuture;
    private GlyphsSettings mGlyphsSettings;
    private PreferenceCategory mPreferenceCategory;
    /* access modifiers changed from: private */
    public PreferenceScreen mScreen;
    /* access modifiers changed from: private */
    public ContactsManager.Contact mSelectedContact = null;
    private List<ContactsManager.Contact> mTotalContacts;
    private Uri uri = ContactsContract.Contacts.CONTENT_URI;

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

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterContactDataChangeListener();
    }

    public class ContactContentObserver extends ContentObserver {
        public ContactContentObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            Log.d(CustomContactRingtoneController.TAG, "selfChange " + z);
            CustomContactRingtoneController.this.updateData();
        }
    }

    public CustomContactRingtoneController(Context context, String str) {
        super(context, str);
        registerContactDataChangeListener();
        this.mGlyphsSettings = new GlyphsSettings(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        queryContact();
    }

    /* access modifiers changed from: private */
    public void selectRingtone(String str, Uri uri2) {
        int i;
        if (uri2 != null) {
            String param = UrlUtil.getParam(uri2.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                try {
                    i = Integer.parseInt(param);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Context context = this.mContext;
                ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_select_ringtone_title), str, uri2, 1, i, SELECT_RINGTONE_REQUEST_CODE);
            }
        }
        i = 0;
        Context context2 = this.mContext;
        ResultPickHelper.startRingtoneSoundSelector((Activity) context2, context2.getString(R$string.nt_glyphs_select_ringtone_title), str, uri2, 1, i, SELECT_RINGTONE_REQUEST_CODE);
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
        ThreadUtils.postOnBackgroundThread((Runnable) new CustomContactRingtoneController$$ExternalSyntheticLambda2(this));
    }

    public void updateData() {
        Log.d(TAG, "updateData");
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new CustomContactRingtoneController$$ExternalSyntheticLambda0(this));
    }

    public void updateDataInner() {
        this.mTotalContacts = ContactsManager.getInstance().searchCustomRingtoneContactsList(this.mContext);
        if (this.mPreferenceCategory != null) {
            ThreadUtils.postOnMainThread(new CustomContactRingtoneController$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataInner$0() {
        Preference preference = this.mPreferenceCategory.getPreference(0);
        this.mPreferenceCategory.removeAll();
        this.mScreen.removePreference(this.mPreferenceCategory);
        this.mScreen.addPreference(this.mPreferenceCategory);
        this.mPreferenceCategory.addPreference(preference);
        List<ContactsManager.Contact> list = this.mTotalContacts;
        if (list != null && list.size() > 0) {
            for (ContactsManager.Contact next : this.mTotalContacts) {
                if ("Abra".equals(next.getDisplayName())) {
                    this.mGlyphsSettings.setShowMusicItem(true);
                }
                PrimaryDeletePreference primaryDeletePreference = new PrimaryDeletePreference(this.mContext);
                primaryDeletePreference.setPersistent(false);
                primaryDeletePreference.setData(next);
                primaryDeletePreference.setKey(KEY_ITEM_PREFIX + next.getContactId());
                primaryDeletePreference.setOnSelectedListener(new PrimaryDeletePreference.OnSelectedListener() {
                    public void onClickDelete(Preference preference, ContactsManager.Contact contact) {
                        ContactsManager.Contact contact2 = CustomContactRingtoneController.this.getContact(preference.getKey());
                        if (contact2 != null) {
                            CustomContactRingtoneController.this.showDeleteDialog(preference, contact2.getDisplayName(), contact2.getContactId());
                        }
                    }

                    public void onClick(Preference preference, ContactsManager.Contact contact) {
                        ContactsManager.Contact contact2 = CustomContactRingtoneController.this.getContact(preference.getKey());
                        if (contact2 != null) {
                            Uri uri = null;
                            if (!TextUtils.isEmpty(contact2.getRingtoneUri())) {
                                uri = Uri.parse(contact2.getRingtoneUri());
                            }
                            CustomContactRingtoneController.this.mSelectedContact = contact2;
                            CustomContactRingtoneController.this.selectRingtone(contact2.getDisplayName(), uri);
                        }
                    }
                });
                this.mPreferenceCategory.addPreference(primaryDeletePreference);
            }
        }
    }

    public ContactsManager.Contact getContact(String str) {
        List<ContactsManager.Contact> list = this.mTotalContacts;
        Log.d(TAG, "list:" + list + ", contactName:" + str);
        if (list == null || list.size() == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        Log.d(TAG, "list size:" + list.size());
        for (ContactsManager.Contact next : this.mTotalContacts) {
            if (TextUtils.equals(KEY_ITEM_PREFIX + next.getContactId(), str)) {
                return next;
            }
        }
        return null;
    }

    private void updateContact(String str, String str2) {
        List<ContactsManager.Contact> list = this.mTotalContacts;
        if (list != null && list.size() != 0) {
            for (ContactsManager.Contact next : this.mTotalContacts) {
                if (TextUtils.equals(str, next.getContactId())) {
                    next.setRingtoneUri(str2);
                    PreferenceCategory preferenceCategory = this.mPreferenceCategory;
                    Preference findPreference = preferenceCategory.findPreference(KEY_ITEM_PREFIX + next.getContactId());
                    if (findPreference != null && (findPreference instanceof PrimaryDeletePreference)) {
                        ((PrimaryDeletePreference) findPreference).setData(next);
                        return;
                    }
                    return;
                }
            }
        }
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        String str;
        Intent intent2 = intent;
        super.onActivityControllerResult(i, i2, intent);
        if (i2 == -1 && i == SELECT_RINGTONE_REQUEST_CODE && this.mSelectedContact != null) {
            Uri uri2 = (Uri) intent2.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            String str2 = "";
            boolean z = uri2 == null;
            ContactsManager.getInstance().updateContactRingtone(this.mContext, this.mSelectedContact.getContactId(), z ? str2 : uri2.toString());
            Context context = this.mContext;
            if (z) {
                str = str2;
            } else {
                str = uri2.toString();
            }
            LedSettingUtils.setContactLed(context, "", "", "", str, this.mSelectedContact.getContactId(), intent2.getIntExtra("key_sound_only", -1));
            String contactId = this.mSelectedContact.getContactId();
            if (uri2 != null) {
                str2 = uri2.toString();
            }
            updateContact(contactId, str2);
        }
    }

    public void showDeleteDialog(Preference preference, String str, String str2) {
        onCreateDialog(preference, str, str2).show();
    }

    public Dialog onCreateDialog(final Preference preference, String str, final String str2) {
        return new AlertDialog.Builder(this.mContext).setTitle(R$string.nt_glyphs_remove_custom_ringtone_title).setMessage(this.mContext.getString(R$string.nt_glyphs_remove_custom_ringtone_message, new Object[]{str})).setNegativeButton(R$string.nt_glyphs_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(R$string.nt_glyphs_remove, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                final boolean z;
                Log.d(CustomContactRingtoneController.TAG, " " + Thread.currentThread().getName() + " contactId " + str2 + "  key " + preference.getKey());
                if (CustomContactRingtoneController.this.mScreen != null) {
                    PreferenceScreen r3 = CustomContactRingtoneController.this.mScreen;
                    z = r3.removePreferenceRecursively(CustomContactRingtoneController.KEY_ITEM_PREFIX + str2);
                } else {
                    z = false;
                }
                Log.d(CustomContactRingtoneController.TAG, " result " + z);
                ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
                    public void run() {
                        ContactsManager.getInstance().updateContactRingtone(CustomContactRingtoneController.this.mContext, str2, (String) null);
                        if (!z) {
                            CustomContactRingtoneController.this.updateData();
                        }
                    }
                });
            }
        }).create();
    }
}
