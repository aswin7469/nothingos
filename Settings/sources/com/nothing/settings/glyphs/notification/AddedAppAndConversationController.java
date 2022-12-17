package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.service.notification.ConversationChannelWrapper;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.PrimaryDeletePreference;
import com.nothing.settings.glyphs.utils.ContactsManager;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AddedAppAndConversationController extends BasePreferenceController {
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1005;
    private static final String SUMMARY_KEY_SUFFIX = "_summary";
    private static final String TAG = "AddedAppAndConversationController";
    protected NotificationBackend mBackend;
    private ConversationChannelWrapper mClickConversationChannelWrapper;
    private Preference mClickPreference;
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
    private boolean mIsClickPreference;
    private PreferenceGroup mPreferenceGroup;

    private Preference getSummaryPreference() {
        return null;
    }

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

    public AddedAppAndConversationController(Context context, String str, NotificationBackend notificationBackend) {
        super(context, str);
        this.mBackend = notificationBackend;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public boolean updateList(List<ConversationChannelWrapper> list) {
        boolean z = false;
        if (this.mIsClickPreference) {
            this.mIsClickPreference = false;
            return false;
        }
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

    /* access modifiers changed from: private */
    /* renamed from: matchesFilter */
    public boolean lambda$populateConversations$0(ConversationChannelWrapper conversationChannelWrapper) {
        Uri sound = conversationChannelWrapper.getNotificationChannel().getSound();
        return sound != null && !TextUtils.isEmpty(sound.toString()) && !RingtoneManager.isDefault(sound) && !"android.resource".equals(sound.getScheme());
    }

    /* access modifiers changed from: package-private */
    public void populateConversations(List<ConversationChannelWrapper> list) {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        Log.d(TAG, "conversation size:" + list.size());
        list.stream().filter(new AddedAppAndConversationController$$ExternalSyntheticLambda0(this)).sorted(this.mConversationComparator).map(new AddedAppAndConversationController$$ExternalSyntheticLambda1(this)).forEachOrdered(new AddedAppAndConversationController$$ExternalSyntheticLambda2(this, atomicInteger));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$populateConversations$1(AtomicInteger atomicInteger, Preference preference) {
        preference.setOrder(atomicInteger.getAndIncrement());
        this.mPreferenceGroup.addPreference(preference);
    }

    /* access modifiers changed from: private */
    public Preference createConversationPref(final ConversationChannelWrapper conversationChannelWrapper) {
        PrimaryDeletePreference primaryDeletePreference = new PrimaryDeletePreference(this.mContext);
        primaryDeletePreference.setTitle(getTitle(conversationChannelWrapper));
        ThreadUtils.postOnBackgroundThread((Runnable) new AddedAppAndConversationController$$ExternalSyntheticLambda8(this, primaryDeletePreference, conversationChannelWrapper));
        primaryDeletePreference.setIcon(this.mBackend.getConversationDrawable(this.mContext, conversationChannelWrapper.getShortcutInfo(), conversationChannelWrapper.getPkg(), conversationChannelWrapper.getUid(), conversationChannelWrapper.getNotificationChannel().isImportantConversation()));
        primaryDeletePreference.setKey(conversationChannelWrapper.getNotificationChannel().getId());
        primaryDeletePreference.setOnSelectedListener(new PrimaryDeletePreference.OnSelectedListener() {
            public void onClick(Preference preference, ContactsManager.Contact contact) {
                AddedAppAndConversationController.this.startSelectFragment(conversationChannelWrapper, preference);
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onClickDelete$0(Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
                AddedAppAndConversationController.this.showDeleteDialog(preference, conversationChannelWrapper);
            }

            public void onClickDelete(Preference preference, ContactsManager.Contact contact) {
                ThreadUtils.postOnMainThread(new AddedAppAndConversationController$1$$ExternalSyntheticLambda0(this, preference, conversationChannelWrapper));
            }
        });
        return primaryDeletePreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createConversationPref$3(PrimaryDeletePreference primaryDeletePreference, ConversationChannelWrapper conversationChannelWrapper) {
        if (primaryDeletePreference != null) {
            ThreadUtils.postOnMainThread(new AddedAppAndConversationController$$ExternalSyntheticLambda6(primaryDeletePreference, getSummary(conversationChannelWrapper)));
        }
    }

    /* access modifiers changed from: private */
    public void showDeleteDialog(Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
        onCreateDialog(preference, conversationChannelWrapper).show();
    }

    private Dialog onCreateDialog(Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
        return new AlertDialog.Builder(this.mContext).setTitle(R$string.nt_glyphs_remove_customised_notification_sound_title).setMessage(this.mContext.getString(R$string.nt_glyphs_remove_customised_notification_sound_message)).setNegativeButton(R$string.nt_glyphs_cancel, new AddedAppAndConversationController$$ExternalSyntheticLambda3()).setPositiveButton(R$string.nt_glyphs_remove, new AddedAppAndConversationController$$ExternalSyntheticLambda4(this, preference, conversationChannelWrapper)).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$5(Preference preference, ConversationChannelWrapper conversationChannelWrapper, DialogInterface dialogInterface, int i) {
        resetNotificationSound(preference, conversationChannelWrapper);
    }

    private void resetNotificationSound(Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
        Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2);
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        Log.d(TAG, "resetNotificationSound uri:" + defaultUri + ", uri2:" + actualDefaultRingtoneUri);
        NotificationChannel notificationChannel = conversationChannelWrapper.getNotificationChannel();
        StringBuilder sb = new StringBuilder();
        sb.append("channel:");
        sb.append(notificationChannel);
        Log.d(TAG, sb.toString());
        if (notificationChannel != null) {
            notificationChannel.setSound(defaultUri, notificationChannel.getAudioAttributes());
            this.mBackend.updateChannel(conversationChannelWrapper.getPkg(), conversationChannelWrapper.getUid(), notificationChannel);
            this.mPreferenceGroup.removePreference(preference);
        }
    }

    /* access modifiers changed from: private */
    public void startSelectFragment(ConversationChannelWrapper conversationChannelWrapper, Preference preference) {
        int i;
        Uri sound = conversationChannelWrapper.getNotificationChannel().getSound();
        this.mClickConversationChannelWrapper = conversationChannelWrapper;
        this.mClickPreference = preference;
        this.mIsClickPreference = true;
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
                    ThreadUtils.postOnBackgroundThread((Runnable) new AddedAppAndConversationController$$ExternalSyntheticLambda5(this));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityControllerResult$7() {
        if (this.mClickPreference != null) {
            ThreadUtils.postOnMainThread(new AddedAppAndConversationController$$ExternalSyntheticLambda7(this, getSummary(this.mClickConversationChannelWrapper)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityControllerResult$6(String str) {
        this.mClickPreference.setSummary((CharSequence) str);
    }

    private String getSummary(ConversationChannelWrapper conversationChannelWrapper) {
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
}
