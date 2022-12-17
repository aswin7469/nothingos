package com.nothing.settings.glyphs.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$xml;
import com.android.settings.applications.AppInfoBase;
import com.android.settings.bluetooth.BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.dashboard.DashboardFragment$$ExternalSyntheticLambda10;
import com.android.settings.dashboard.UiBlockerController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.glyphs.preference.NotificationAppIconPreference;
import com.nothing.settings.glyphs.ringtone.RingtoneDefaultController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppNotificationConversationFragment extends AppInfoBase implements OnActivityResultListener, PreferenceGroup.OnExpandButtonClickListener, BasePreferenceController.UiBlockListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_notification_conversations);
    UiBlockerController mBlockerController;
    private List<AbstractPreferenceController> mControllers;
    private boolean mCreated;
    private NotificationAppIconPreference mPreference;
    private final Map<Class, List<AbstractPreferenceController>> mPreferenceControllers = new ArrayMap();

    private String getLogTag() {
        return "AppNotificationConversation";
    }

    private void initPreference() {
    }

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 2000;
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        return false;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreference();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mCreated) {
            Log.w("AppConversationFragment", "onActivityCreated: ignoring duplicate call");
            return;
        }
        this.mCreated = true;
        if (this.mPackageInfo != null) {
            NotificationAppIconPreference notificationAppIconPreference = (NotificationAppIconPreference) getPreferenceScreen().findPreference("glyphs_notification_conversation_app");
            this.mPreference = notificationAppIconPreference;
            if (notificationAppIconPreference != null) {
                notificationAppIconPreference.setAppIcon(Utils.getBadgedIcon(getContext(), this.mPackageInfo.applicationInfo)).setAppName(this.mPackageInfo.applicationInfo.loadLabel(this.mPm).toString()).refresh();
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        createPreferenceControllers(context);
        this.mControllers.forEach(new AppNotificationConversationFragment$$ExternalSyntheticLambda0(getMetricsCategory()));
        for (AbstractPreferenceController addPreferenceController : this.mControllers) {
            addPreferenceController(addPreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onAttach$0(int i, AbstractPreferenceController abstractPreferenceController) {
        if (abstractPreferenceController instanceof BasePreferenceController) {
            ((BasePreferenceController) abstractPreferenceController).setMetricsCategory(i);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_notification_conversations;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new RingtoneDefaultController(context, "glyphs_default_ringtone"));
        return this.mControllers;
    }

    /* access modifiers changed from: protected */
    public void addPreferenceController(AbstractPreferenceController abstractPreferenceController) {
        if (this.mPreferenceControllers.get(abstractPreferenceController.getClass()) == null) {
            this.mPreferenceControllers.put(abstractPreferenceController.getClass(), new ArrayList());
        }
        this.mPreferenceControllers.get(abstractPreferenceController.getClass()).add(abstractPreferenceController);
    }

    /* access modifiers changed from: package-private */
    public void checkUiBlocker(List<AbstractPreferenceController> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        list.forEach(new AppNotificationConversationFragment$$ExternalSyntheticLambda3(this, arrayList, arrayList2));
        if (!arrayList.isEmpty()) {
            UiBlockerController uiBlockerController = new UiBlockerController(arrayList);
            this.mBlockerController = uiBlockerController;
            uiBlockerController.start(new AppNotificationConversationFragment$$ExternalSyntheticLambda4(this, arrayList2));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$1(List list, List list2, AbstractPreferenceController abstractPreferenceController) {
        if ((abstractPreferenceController instanceof BasePreferenceController.UiBlocker) && abstractPreferenceController.isAvailable()) {
            BasePreferenceController basePreferenceController = (BasePreferenceController) abstractPreferenceController;
            basePreferenceController.setUiBlockListener(this);
            list.add(abstractPreferenceController.getPreferenceKey());
            list2.add(basePreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$3(List list) {
        updatePreferenceVisibility(this.mPreferenceControllers);
        list.forEach(new AppNotificationConversationFragment$$ExternalSyntheticLambda5());
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        checkUiBlocker(this.mControllers);
        refreshAllPreferences(getLogTag());
        this.mControllers.stream().map(new AppNotificationConversationFragment$$ExternalSyntheticLambda1(this)).filter(new DashboardFragment$$ExternalSyntheticLambda10()).forEach(new AppNotificationConversationFragment$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Preference lambda$onCreatePreferences$4(AbstractPreferenceController abstractPreferenceController) {
        return findPreference(abstractPreferenceController.getPreferenceKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePreferences$5(Preference preference) {
        preference.getExtras().putInt(DashboardFragment.CATEGORY, getMetricsCategory());
    }

    private void refreshAllPreferences(String str) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        displayResourceTiles();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Log.d(str, "All preferences added, reporting fully drawn");
            activity.reportFullyDrawn();
        }
        updatePreferenceVisibility(this.mPreferenceControllers);
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceVisibility(Map<Class, List<AbstractPreferenceController>> map) {
        UiBlockerController uiBlockerController;
        if (getPreferenceScreen() != null && map != null && (uiBlockerController = this.mBlockerController) != null) {
            boolean isBlockerFinished = uiBlockerController.isBlockerFinished();
            for (List<AbstractPreferenceController> it : map.values()) {
                for (AbstractPreferenceController abstractPreferenceController : it) {
                    Preference findPreference = findPreference(abstractPreferenceController.getPreferenceKey());
                    if (findPreference != null) {
                        boolean z = true;
                        if (abstractPreferenceController instanceof BasePreferenceController.UiBlocker) {
                            boolean savedPrefVisibility = ((BasePreferenceController) abstractPreferenceController).getSavedPrefVisibility();
                            if (!isBlockerFinished || !abstractPreferenceController.isAvailable() || !savedPrefVisibility) {
                                z = false;
                            }
                            findPreference.setVisible(z);
                        } else {
                            if (!isBlockerFinished || !abstractPreferenceController.isAvailable()) {
                                z = false;
                            }
                            findPreference.setVisible(z);
                        }
                    }
                }
            }
        }
    }

    private void displayResourceTiles() {
        int preferenceScreenResId = getPreferenceScreenResId();
        if (preferenceScreenResId > 0) {
            addPreferencesFromResource(preferenceScreenResId);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.setOnExpandButtonClickListener(this);
            displayResourceTilesToScreen(preferenceScreen);
        }
    }

    /* access modifiers changed from: protected */
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        this.mPreferenceControllers.values().stream().flatMap(new BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda2()).forEach(new AppNotificationConversationFragment$$ExternalSyntheticLambda6(preferenceScreen));
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

    public void onBlockerWorkFinished(BasePreferenceController basePreferenceController) {
        this.mBlockerController.countDown(basePreferenceController.getPreferenceKey());
        basePreferenceController.setUiBlockerFinished(this.mBlockerController.isBlockerFinished());
    }
}
