package com.android.settings.notification.app;

import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.LayoutPreference;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RecentConversationsPreferenceController extends AbstractPreferenceController {
    private final NotificationBackend mBackend;
    Comparator<ConversationChannel> mConversationComparator = new Comparator<ConversationChannel>() {
        private final Collator sCollator = Collator.getInstance();

        public int compare(ConversationChannel conversationChannel, ConversationChannel conversationChannel2) {
            int compare = (conversationChannel.getShortcutInfo().getLabel() == null || conversationChannel2.getShortcutInfo().getLabel() == null) ? 0 : this.sCollator.compare(conversationChannel.getShortcutInfo().getLabel().toString(), conversationChannel2.getShortcutInfo().getLabel().toString());
            return compare == 0 ? conversationChannel.getNotificationChannel().getId().compareTo(conversationChannel2.getNotificationChannel().getId()) : compare;
        }
    };
    private PreferenceGroup mPreferenceGroup;
    private final IPeopleManager mPs;

    public String getPreferenceKey() {
        return "recent_conversations";
    }

    public boolean isAvailable() {
        return true;
    }

    public RecentConversationsPreferenceController(Context context, NotificationBackend notificationBackend, IPeopleManager iPeopleManager) {
        super(context);
        this.mBackend = notificationBackend;
        this.mPs = iPeopleManager;
    }

    /* access modifiers changed from: package-private */
    public LayoutPreference getClearAll(PreferenceGroup preferenceGroup) {
        LayoutPreference layoutPreference = new LayoutPreference(this.mContext, R$layout.conversations_clear_recents);
        layoutPreference.setKey(getPreferenceKey() + "_clear_all");
        layoutPreference.setOrder(1);
        Button button = (Button) layoutPreference.findViewById(R$id.conversation_settings_clear_recents);
        button.setOnClickListener(new C1172x815781de(this, preferenceGroup, button));
        return layoutPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getClearAll$0(PreferenceGroup preferenceGroup, Button button, View view) {
        try {
            this.mPs.removeAllRecentConversations();
            for (int preferenceCount = preferenceGroup.getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
                Preference preference = preferenceGroup.getPreference(preferenceCount);
                if ((preference instanceof RecentConversationPreference) && ((RecentConversationPreference) preference).hasClearListener()) {
                    preferenceGroup.removePreference(preference);
                }
            }
            button.announceForAccessibility(this.mContext.getString(R$string.recent_convos_removed));
        } catch (RemoteException e) {
            Slog.w("RecentConversationsPC", "Could not clear recents", e);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public boolean updateList() {
        List emptyList = Collections.emptyList();
        try {
            emptyList = this.mPs.getRecentConversations().getList();
        } catch (RemoteException e) {
            Slog.w("RecentConversationsPC", "Could not get recent conversations", e);
        }
        return populateList(emptyList);
    }

    /* access modifiers changed from: package-private */
    public boolean populateList(List<ConversationChannel> list) {
        LayoutPreference clearAll;
        this.mPreferenceGroup.removeAll();
        boolean z = false;
        boolean populateConversations = list != null ? populateConversations(list) : false;
        if (this.mPreferenceGroup.getPreferenceCount() != 0) {
            z = true;
        }
        this.mPreferenceGroup.setVisible(z);
        if (z && populateConversations && (clearAll = getClearAll(this.mPreferenceGroup)) != null) {
            this.mPreferenceGroup.addPreference(clearAll);
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean populateConversations(List<ConversationChannel> list) {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        list.stream().filter(new C1169x815781db()).sorted(this.mConversationComparator).map(new C1170x815781dc(this)).forEachOrdered(new C1171x815781dd(this, atomicInteger, atomicBoolean));
        return atomicBoolean.get();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$populateConversations$1(ConversationChannel conversationChannel) {
        return conversationChannel.getNotificationChannel().getImportance() != 0 && (conversationChannel.getNotificationChannelGroup() == null || !conversationChannel.getNotificationChannelGroup().isBlocked());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$populateConversations$2(AtomicInteger atomicInteger, AtomicBoolean atomicBoolean, RecentConversationPreference recentConversationPreference) {
        recentConversationPreference.setOrder(atomicInteger.getAndIncrement());
        this.mPreferenceGroup.addPreference(recentConversationPreference);
        if (recentConversationPreference.hasClearListener()) {
            atomicBoolean.set(true);
        }
    }

    /* access modifiers changed from: protected */
    public RecentConversationPreference createConversationPref(ConversationChannel conversationChannel) {
        String str = conversationChannel.getShortcutInfo().getPackage();
        int uid = conversationChannel.getUid();
        String id = conversationChannel.getShortcutInfo().getId();
        RecentConversationPreference recentConversationPreference = new RecentConversationPreference(this.mContext);
        if (!conversationChannel.hasActiveNotifications()) {
            recentConversationPreference.setOnClearClickListener(new C1173x815781df(this, str, uid, id, recentConversationPreference));
        }
        recentConversationPreference.setTitle(getTitle(conversationChannel));
        recentConversationPreference.setSummary(getSummary(conversationChannel));
        recentConversationPreference.setIcon(this.mBackend.getConversationDrawable(this.mContext, conversationChannel.getShortcutInfo(), str, uid, false));
        recentConversationPreference.setKey(conversationChannel.getNotificationChannel().getId() + ":" + id);
        recentConversationPreference.setOnPreferenceClickListener(new C1174x815781e0(this, str, uid, conversationChannel, id, recentConversationPreference));
        return recentConversationPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createConversationPref$3(String str, int i, String str2, RecentConversationPreference recentConversationPreference) {
        try {
            this.mPs.removeRecentConversation(str, UserHandle.getUserId(i), str2);
            recentConversationPreference.getClearView().announceForAccessibility(this.mContext.getString(R$string.recent_convo_removed));
            this.mPreferenceGroup.removePreference(recentConversationPreference);
        } catch (RemoteException e) {
            Slog.w("RecentConversationsPC", "Could not clear recent", e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createConversationPref$4(String str, int i, ConversationChannel conversationChannel, String str2, RecentConversationPreference recentConversationPreference, Preference preference) {
        this.mBackend.createConversationNotificationChannel(str, i, conversationChannel.getNotificationChannel(), str2);
        getSubSettingLauncher(conversationChannel, recentConversationPreference.getTitle()).launch();
        return true;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSummary(ConversationChannel conversationChannel) {
        if (conversationChannel.getNotificationChannelGroup() == null) {
            return conversationChannel.getNotificationChannel().getName();
        }
        return this.mContext.getString(R$string.notification_conversation_summary, new Object[]{conversationChannel.getNotificationChannel().getName(), conversationChannel.getNotificationChannelGroup().getName()});
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTitle(ConversationChannel conversationChannel) {
        return conversationChannel.getShortcutInfo().getLabel();
    }

    /* access modifiers changed from: package-private */
    public SubSettingLauncher getSubSettingLauncher(ConversationChannel conversationChannel, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putInt("uid", conversationChannel.getUid());
        bundle.putString("package", conversationChannel.getShortcutInfo().getPackage());
        bundle.putString("android.provider.extra.CHANNEL_ID", conversationChannel.getNotificationChannel().getId());
        bundle.putString("android.provider.extra.CONVERSATION_ID", conversationChannel.getShortcutInfo().getId());
        return new SubSettingLauncher(this.mContext).setDestination(ChannelNotificationSettings.class.getName()).setArguments(bundle).setExtras(bundle).setUserHandle(UserHandle.getUserHandleForUid(conversationChannel.getUid())).setTitleText(charSequence).setSourceMetricsCategory(1834);
    }
}
