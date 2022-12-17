package com.nothing.settings.glyphs.notification;

import android.app.people.IPeopleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ServiceManager;
import com.android.settings.R$xml;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class GlyphConversationListFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_notification_conversations);
    NotificationBackend mBackend = new NotificationBackend();
    private List<AbstractPreferenceController> mControllers;
    private GlyphAllConversationsPreferenceController mGlyphAllConversationsPreferenceController;
    private GlyphNoConversationsPreferenceController mGlyphNoConversationsPreferenceController;
    IPeopleManager mPs = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
    private boolean mUpdatedInOnCreate = false;

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GlyphConversationListFragment";
    }

    public int getMetricsCategory() {
        return 2000;
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_conversations_list;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        this.mControllers = new ArrayList();
        this.mGlyphAllConversationsPreferenceController = new GlyphAllConversationsPreferenceController(context, this.mBackend);
        this.mGlyphNoConversationsPreferenceController = new GlyphNoConversationsPreferenceController(context);
        this.mControllers.add(this.mGlyphAllConversationsPreferenceController);
        this.mControllers.add(this.mGlyphNoConversationsPreferenceController);
        return this.mControllers;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
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

    private void update() {
        this.mGlyphNoConversationsPreferenceController.setAvailable(!this.mGlyphAllConversationsPreferenceController.updateList(this.mBackend.getConversations(false).getList()));
        this.mGlyphNoConversationsPreferenceController.displayPreference(getPreferenceScreen());
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

    public void onExpandButtonClick() {
        this.mMetricsFeatureProvider.action(0, 834, getMetricsCategory(), (String) null, 0);
    }
}
