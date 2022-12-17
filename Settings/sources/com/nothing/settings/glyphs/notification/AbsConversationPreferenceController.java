package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.ConversationChannelWrapper;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.ChannelNotificationSettings;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AppPreference;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbsConversationPreferenceController extends BasePreferenceController {
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1005;
    private static final String SUMMARY_KEY_SUFFIX = "_summary";
    private static final String TAG = "ConversationPref";
    protected final NotificationBackend mBackend;
    private ConversationChannelWrapper mClickConversationChannelWrapper;
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
    String mPackageName;
    private PreferenceGroup mPreferenceGroup;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: package-private */
    public abstract String getItemPreferenceSummary(ConversationChannelWrapper conversationChannelWrapper, NotificationBackend notificationBackend);

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    /* access modifiers changed from: package-private */
    public abstract Preference getSummaryPreference();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: matchesFilter */
    public abstract boolean lambda$populateConversations$0(ConversationChannelWrapper conversationChannelWrapper);

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AbsConversationPreferenceController(Context context, String str, NotificationBackend notificationBackend) {
        super(context, str);
        this.mBackend = notificationBackend;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    /* access modifiers changed from: package-private */
    public boolean updateList(List<ConversationChannelWrapper> list) {
        boolean z = false;
        this.mPreferenceGroup.setVisible(false);
        this.mPreferenceGroup.removeAll();
        StringBuilder sb = new StringBuilder();
        sb.append("have conversation:");
        sb.append(list != null);
        Log.d(TAG, sb.toString());
        if (list != null) {
            populateConversations(list);
        }
        if (this.mPreferenceGroup.getPreferenceCount() != 0) {
            z = true;
        }
        if (z) {
            Preference summaryPreference = getSummaryPreference();
            if (summaryPreference != null) {
                summaryPreference.setKey(getPreferenceKey() + SUMMARY_KEY_SUFFIX);
                this.mPreferenceGroup.addPreference(summaryPreference);
            }
            this.mPreferenceGroup.setVisible(true);
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void populateConversations(List<ConversationChannelWrapper> list) {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        Log.d(TAG, "conversation size:" + list.size());
        list.stream().filter(new AbsConversationPreferenceController$$ExternalSyntheticLambda0(this)).sorted(this.mConversationComparator).map(new AbsConversationPreferenceController$$ExternalSyntheticLambda1(this)).forEachOrdered(new AbsConversationPreferenceController$$ExternalSyntheticLambda2(this, atomicInteger));
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
        ThreadUtils.postOnBackgroundThread((Runnable) new AbsConversationPreferenceController$$ExternalSyntheticLambda4(this, appPreference, conversationChannelWrapper));
        appPreference.setIcon(this.mBackend.getConversationDrawable(this.mContext, conversationChannelWrapper.getShortcutInfo(), conversationChannelWrapper.getPkg(), conversationChannelWrapper.getUid(), conversationChannelWrapper.getNotificationChannel().isImportantConversation()));
        appPreference.setKey(conversationChannelWrapper.getNotificationChannel().getId());
        appPreference.setOnPreferenceClickListener(new AbsConversationPreferenceController$$ExternalSyntheticLambda5(this, conversationChannelWrapper));
        return appPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createConversationPref$3(AppPreference appPreference, ConversationChannelWrapper conversationChannelWrapper) {
        if (appPreference != null) {
            ThreadUtils.postOnMainThread(new AbsConversationPreferenceController$$ExternalSyntheticLambda3(appPreference, getSummary(conversationChannelWrapper)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createConversationPref$4(ConversationChannelWrapper conversationChannelWrapper, Preference preference) {
        startSelectFragment(conversationChannelWrapper);
        return true;
    }

    private void startSelectFragment(ConversationChannelWrapper conversationChannelWrapper) {
        int i;
        Uri sound = conversationChannelWrapper.getNotificationChannel().getSound();
        this.mClickConversationChannelWrapper = conversationChannelWrapper;
        Context context = this.mContext;
        Log.d(TAG, "startSelectFragment uri:" + sound);
        if (sound == null) {
            i = Settings.Global.getInt(this.mContext.getContentResolver(), "led_notification_mode", 0);
        } else {
            String param = UrlUtil.getParam(sound.toString(), "soundOnly");
            Log.d(TAG, "startSelectFragment param:" + param);
            if (!TextUtils.isEmpty(param)) {
                i = Integer.parseInt(param);
            } else {
                i = Settings.Global.getInt(this.mContext.getContentResolver(), "led_notification_mode", 0);
            }
        }
        ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_single_notifications_title), "", sound, 2, i, SELECT_RINGTONE_REQUEST_CODE);
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        if (SELECT_RINGTONE_REQUEST_CODE == i && i2 == -1) {
            Log.d(TAG, "mClickConversationChannelWrapper:" + this.mClickConversationChannelWrapper);
            ConversationChannelWrapper conversationChannelWrapper = this.mClickConversationChannelWrapper;
            if (conversationChannelWrapper != null) {
                NotificationChannel notificationChannel = conversationChannelWrapper.getNotificationChannel();
                Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
                Log.d(TAG, "channel:" + notificationChannel + ", uri:" + uri);
                if (notificationChannel != null) {
                    notificationChannel.setSound(uri, notificationChannel.getAudioAttributes());
                    this.mBackend.updateChannel(this.mClickConversationChannelWrapper.getPkg(), this.mClickConversationChannelWrapper.getUid(), notificationChannel);
                }
            }
            ((Activity) this.mContext).finish();
        }
    }

    private String getSummary(ConversationChannelWrapper conversationChannelWrapper) {
        String itemPreferenceSummary = getItemPreferenceSummary(conversationChannelWrapper, this.mBackend);
        Log.d(TAG, "getSummary summary:" + itemPreferenceSummary);
        if (!TextUtils.isEmpty(itemPreferenceSummary)) {
            return itemPreferenceSummary;
        }
        Uri sound = conversationChannelWrapper.getNotificationChannel().getSound();
        Log.d(TAG, "getSummary uri:" + sound);
        try {
            return Ringtone.getTitle(this.mContext, sound, false, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CharSequence getTitle(ConversationChannelWrapper conversationChannelWrapper) {
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
