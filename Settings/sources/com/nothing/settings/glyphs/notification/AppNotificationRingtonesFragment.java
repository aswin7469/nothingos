package com.nothing.settings.glyphs.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.android.settings.R$xml;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AppNotificationRingtonesFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_notification_ringtones);
    private AddedAppAndConversationController mAddedAppAndConversationController;
    NotificationBackend mBackend = new NotificationBackend();
    private List<AbstractPreferenceController> mControllers;
    private boolean mUpdatedInOnCreate = false;

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "AppNotificationHome";
    }

    public int getMetricsCategory() {
        return 2000;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        update();
        this.mUpdatedInOnCreate = true;
    }

    public void onStart() {
        super.onStart();
        if (this.mUpdatedInOnCreate) {
            this.mUpdatedInOnCreate = false;
        } else {
            update();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private void update() {
        this.mAddedAppAndConversationController.updateList(this.mBackend.getConversations(false).getList());
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_notification_ringtones;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new NotificationDefaultRingtonePreferenceController(context, "glyphs_default_ringtone"));
        AddedAppAndConversationController addedAppAndConversationController = new AddedAppAndConversationController(context, "glyphs_notification_ringtones_list", this.mBackend);
        this.mAddedAppAndConversationController = addedAppAndConversationController;
        this.mControllers.add(addedAppAndConversationController);
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
    }
}
