package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.android.settings.R$xml;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ContactRingtonesFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_contact_ringtones);
    private AddContactRingtoneController mAddContactRingtoneController = null;
    private List<AbstractPreferenceController> mControllers;
    private CustomContactRingtoneController mCustomContactRingtoneController;

    private void initPreference() {
    }

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "ContactRingtoneHome";
    }

    public int getMetricsCategory() {
        return 2000;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreference();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_contact_ringtones;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new RingtoneDefaultController(context, "glyphs_default_ringtone"));
        AddContactRingtoneController addContactRingtoneController = new AddContactRingtoneController(context, "glyphs_add_contact_ringtone");
        this.mAddContactRingtoneController = addContactRingtoneController;
        this.mControllers.add(addContactRingtoneController);
        CustomContactRingtoneController customContactRingtoneController = new CustomContactRingtoneController(context, "glyphs_contact_ringtones_list");
        this.mCustomContactRingtoneController = customContactRingtoneController;
        this.mControllers.add(customContactRingtoneController);
        return this.mControllers;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        List<AbstractPreferenceController> list = this.mControllers;
        if (list != null) {
            for (AbstractPreferenceController next : list) {
                if (next instanceof BasePreferenceController) {
                    ((BasePreferenceController) next).onActivityControllerResult(i, i2, intent);
                }
            }
        }
    }

    public void onDetach() {
        super.onDetach();
        CustomContactRingtoneController customContactRingtoneController = this.mCustomContactRingtoneController;
        if (customContactRingtoneController != null) {
            customContactRingtoneController.onDestroy();
        }
    }
}
