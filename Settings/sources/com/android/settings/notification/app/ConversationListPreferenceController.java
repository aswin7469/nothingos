package com.android.settings.notification.app;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.ConversationChannelWrapper;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.AppPreference;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConversationListPreferenceController extends AbstractPreferenceController {
    protected final NotificationBackend mBackend;
    Comparator<ConversationChannelWrapper> mConversationComparator = new Comparator<ConversationChannelWrapper>() {
        private final Collator sCollator = Collator.getInstance();

        public int compare(ConversationChannelWrapper conversationChannelWrapper, ConversationChannelWrapper conversationChannelWrapper2) {
            if (conversationChannelWrapper.getShortcutInfo() != null && conversationChannelWrapper2.getShortcutInfo() == null) {
                return -1;
            }
            if (conversationChannelWrapper.getShortcutInfo() == null && conversationChannelWrapper2.getShortcutInfo() != null) {
                return 1;
            }
            if (conversationChannelWrapper.getShortcutInfo() == null && conversationChannelWrapper2.getShortcutInfo() == null) {
                return conversationChannelWrapper.getNotificationChannel().getId().compareTo(conversationChannelWrapper2.getNotificationChannel().getId());
            }
            if (conversationChannelWrapper.getShortcutInfo().getLabel() == null && conversationChannelWrapper2.getShortcutInfo().getLabel() != null) {
                return 1;
            }
            if (conversationChannelWrapper.getShortcutInfo().getLabel() == null || conversationChannelWrapper2.getShortcutInfo().getLabel() != null) {
                return this.sCollator.compare(conversationChannelWrapper.getShortcutInfo().getLabel().toString(), conversationChannelWrapper2.getShortcutInfo().getLabel().toString());
            }
            return -1;
        }
    };
    private PreferenceGroup mPreferenceGroup;

    /* access modifiers changed from: package-private */
    public abstract Preference getSummaryPreference();

    public boolean isAvailable() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public abstract boolean matchesFilter(ConversationChannelWrapper conversationChannelWrapper);

    public ConversationListPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context);
        this.mBackend = notificationBackend;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public boolean updateList(List<ConversationChannelWrapper> list) {
        boolean z = false;
        this.mPreferenceGroup.setVisible(false);
        this.mPreferenceGroup.removeAll();
        if (list != null) {
            populateConversations(list);
        }
        if (this.mPreferenceGroup.getPreferenceCount() != 0) {
            z = true;
        }
        if (z) {
            Preference summaryPreference = getSummaryPreference();
            if (summaryPreference != null) {
                summaryPreference.setKey(getPreferenceKey() + "_summary");
                this.mPreferenceGroup.addPreference(summaryPreference);
            }
            this.mPreferenceGroup.setVisible(true);
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void populateConversations(List<ConversationChannelWrapper> list) {
        list.stream().filter(new ConversationListPreferenceController$$ExternalSyntheticLambda0(this)).sorted(this.mConversationComparator).map(new ConversationListPreferenceController$$ExternalSyntheticLambda1(this)).forEachOrdered(new ConversationListPreferenceController$$ExternalSyntheticLambda2(this, new AtomicInteger(100)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$populateConversations$0(ConversationChannelWrapper conversationChannelWrapper) {
        return !conversationChannelWrapper.getNotificationChannel().isDemoted() && matchesFilter(conversationChannelWrapper);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$populateConversations$1(AtomicInteger atomicInteger, Preference preference) {
        preference.setOrder(atomicInteger.getAndIncrement());
        this.mPreferenceGroup.addPreference(preference);
    }

    /* access modifiers changed from: private */
    public Preference createConversationPref(ConversationChannelWrapper conversationChannelWrapper) {
        AppPreference appPreference = new AppPreference(this.mContext);
        appPreference.setTitle(getTitle(conversationChannelWrapper));
        appPreference.setSummary(getSummary(conversationChannelWrapper));
        appPreference.setIcon(this.mBackend.getConversationDrawable(this.mContext, conversationChannelWrapper.getShortcutInfo(), conversationChannelWrapper.getPkg(), conversationChannelWrapper.getUid(), conversationChannelWrapper.getNotificationChannel().isImportantConversation()));
        appPreference.setKey(conversationChannelWrapper.getNotificationChannel().getId());
        appPreference.setOnPreferenceClickListener(new ConversationListPreferenceController$$ExternalSyntheticLambda3(this, conversationChannelWrapper, appPreference));
        return appPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createConversationPref$2(ConversationChannelWrapper conversationChannelWrapper, AppPreference appPreference, Preference preference) {
        getSubSettingLauncher(conversationChannelWrapper, appPreference.getTitle()).launch();
        return true;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSummary(ConversationChannelWrapper conversationChannelWrapper) {
        if (TextUtils.isEmpty(conversationChannelWrapper.getGroupLabel())) {
            return conversationChannelWrapper.getParentChannelLabel();
        }
        return this.mContext.getString(R$string.notification_conversation_summary, new Object[]{conversationChannelWrapper.getParentChannelLabel(), conversationChannelWrapper.getGroupLabel()});
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTitle(ConversationChannelWrapper conversationChannelWrapper) {
        ShortcutInfo shortcutInfo = conversationChannelWrapper.getShortcutInfo();
        if (shortcutInfo != null) {
            return shortcutInfo.getLabel();
        }
        return conversationChannelWrapper.getNotificationChannel().getName();
    }

    /* access modifiers changed from: package-private */
    public SubSettingLauncher getSubSettingLauncher(ConversationChannelWrapper conversationChannelWrapper, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putInt("uid", conversationChannelWrapper.getUid());
        bundle.putString("package", conversationChannelWrapper.getPkg());
        bundle.putString("android.provider.extra.CHANNEL_ID", conversationChannelWrapper.getNotificationChannel().getId());
        bundle.putString("android.provider.extra.CONVERSATION_ID", conversationChannelWrapper.getNotificationChannel().getConversationId());
        return new SubSettingLauncher(this.mContext).setDestination(ChannelNotificationSettings.class.getName()).setArguments(bundle).setExtras(bundle).setUserHandle(UserHandle.getUserHandleForUid(conversationChannelWrapper.getUid())).setTitleText(charSequence).setSourceMetricsCategory(1834);
    }
}
