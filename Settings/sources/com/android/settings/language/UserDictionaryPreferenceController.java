package com.android.settings.language;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.inputmethod.UserDictionaryList;
import com.android.settings.inputmethod.UserDictionaryListPreferenceController;
import com.android.settings.inputmethod.UserDictionarySettings;
import java.util.TreeSet;

public class UserDictionaryPreferenceController extends BasePreferenceController {
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

    public UserDictionaryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        Class cls;
        if (isAvailable() && preference != null) {
            TreeSet<String> dictionaryLocales = getDictionaryLocales();
            Bundle extras = preference.getExtras();
            if (dictionaryLocales.size() <= 1) {
                if (!dictionaryLocales.isEmpty()) {
                    extras.putString("locale", dictionaryLocales.first());
                }
                cls = UserDictionarySettings.class;
            } else {
                cls = UserDictionaryList.class;
            }
            preference.setFragment(cls.getCanonicalName());
        }
    }

    /* access modifiers changed from: protected */
    public TreeSet<String> getDictionaryLocales() {
        return UserDictionaryListPreferenceController.getUserDictionaryLocalesSet(this.mContext);
    }
}
