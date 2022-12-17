package com.android.settings.notification.app;

import android.app.people.IPeopleManager;
import android.content.Context;
import android.os.Bundle;
import android.os.ServiceManager;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ConversationListSettings extends DashboardFragment {
    private AllConversationsPreferenceController mAllConversationsController;
    NotificationBackend mBackend = new NotificationBackend();
    protected List<AbstractPreferenceController> mControllers = new ArrayList();
    private NoConversationsPreferenceController mNoConversationsController;
    private PriorityConversationsPreferenceController mPriorityConversationsController;
    IPeopleManager mPs = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
    private RecentConversationsPreferenceController mRecentConversationsController;
    private boolean mUpdatedInOnCreate = false;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConvoListSettings";
    }

    public int getMetricsCategory() {
        return 1834;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.conversation_list_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        this.mControllers = new ArrayList();
        NoConversationsPreferenceController noConversationsPreferenceController = new NoConversationsPreferenceController(context);
        this.mNoConversationsController = noConversationsPreferenceController;
        this.mControllers.add(noConversationsPreferenceController);
        PriorityConversationsPreferenceController priorityConversationsPreferenceController = new PriorityConversationsPreferenceController(context, this.mBackend);
        this.mPriorityConversationsController = priorityConversationsPreferenceController;
        this.mControllers.add(priorityConversationsPreferenceController);
        AllConversationsPreferenceController allConversationsPreferenceController = new AllConversationsPreferenceController(context, this.mBackend);
        this.mAllConversationsController = allConversationsPreferenceController;
        this.mControllers.add(allConversationsPreferenceController);
        RecentConversationsPreferenceController recentConversationsPreferenceController = new RecentConversationsPreferenceController(context, this.mBackend, this.mPs);
        this.mRecentConversationsController = recentConversationsPreferenceController;
        this.mControllers.add(recentConversationsPreferenceController);
        return new ArrayList(this.mControllers);
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
        List list = this.mBackend.getConversations(false).getList();
        this.mNoConversationsController.setAvailable(!(this.mAllConversationsController.updateList(list) | this.mPriorityConversationsController.updateList(list) | this.mRecentConversationsController.updateList()));
        this.mNoConversationsController.displayPreference(getPreferenceScreen());
    }
}
