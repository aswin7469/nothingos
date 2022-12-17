package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.utils.ContactsManager;
import com.nothing.settings.glyphs.utils.GlyphsSettings;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import java.util.concurrent.Future;

public class CustomAppNotificationRingtoneController extends BasePreferenceController implements OnDestroy {
    private static final String KEY_ITEM_PREFIX = "key_glyphs_contact_";
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1003;
    private static final String TAG = "CustomRingtoneCtr";
    private ContactContentObserver mContactContentObserver;
    private Future mFuture;
    private GlyphsSettings mGlyphsSettings;
    private PreferenceCategory mPreferenceCategory;
    private PreferenceScreen mScreen;

    public class ContactContentObserver extends ContentObserver {
    }

    private void registerContactDataChangeListener() {
    }

    private void unregisterContactDataChangeListener() {
    }

    private void updateContact(String str, String str2) {
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public ContactsManager.Contact getContact(String str) {
        return null;
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

    public void updateDataInner() {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterContactDataChangeListener();
    }

    public CustomAppNotificationRingtoneController(Context context, String str) {
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

    public void selectRingtone(String str, Uri uri) {
        int i;
        int i2 = 0;
        if (uri != null) {
            String param = UrlUtil.getParam(uri.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                try {
                    i2 = Integer.parseInt(param);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Context context = this.mContext;
                i = i2;
                ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_select_ringtone_title), str, uri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
                Context context2 = this.mContext;
                ResultPickHelper.startRingtoneSoundSelector((Activity) context2, context2.getString(R$string.nt_glyphs_select_ringtone_title), str, uri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
            }
        }
        i = 0;
        Context context22 = this.mContext;
        ResultPickHelper.startRingtoneSoundSelector((Activity) context22, context22.getString(R$string.nt_glyphs_select_ringtone_title), str, uri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
    }

    private void queryContact() {
        ThreadUtils.postOnBackgroundThread((Runnable) new C2021xd175b9a9(this));
    }

    public void updateData() {
        Log.d(TAG, "updateData");
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new C2020xd175b9a8(this));
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
    }
}
