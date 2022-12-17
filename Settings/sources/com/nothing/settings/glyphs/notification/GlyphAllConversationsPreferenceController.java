package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.UserHandle;
import android.service.notification.ConversationChannelWrapper;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import java.util.Map;

public class GlyphAllConversationsPreferenceController extends AbsConversationPreferenceController implements OnDestroy {
    private static final String KEY = "glyphs_all_conversation_list";
    private static final String TAG = "ConversationHeaderCtl";
    private ApplicationsState.AppEntry mAppEntry;
    private Context mContext;
    private ApplicationsState mState;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    /* access modifiers changed from: package-private */
    public Preference getSummaryPreference() {
        return null;
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

    /* access modifiers changed from: package-private */
    public boolean matchesFilter(ConversationChannelWrapper conversationChannelWrapper) {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    public GlyphAllConversationsPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, KEY, notificationBackend);
        this.mContext = context;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
    }

    public String getItemPreferenceSummary(ConversationChannelWrapper conversationChannelWrapper, NotificationBackend notificationBackend) {
        if (this.mState == null) {
            this.mState = ApplicationsState.getInstance(((Activity) this.mContext).getApplication());
        }
        this.mAppEntry = this.mState.getEntry(conversationChannelWrapper.getPkg(), UserHandle.getUserId(conversationChannelWrapper.getUid()));
        Context context = this.mContext;
        CharSequence notificationSummary = getNotificationSummary(notificationBackend.loadAppRow(context, context.getPackageManager(), this.mAppEntry.info), this.mContext, conversationChannelWrapper);
        if (notificationSummary != null) {
            return notificationSummary.toString();
        }
        return null;
    }

    private CharSequence getNotificationSummary(NotificationBackend.AppRow appRow, Context context, ConversationChannelWrapper conversationChannelWrapper) {
        Map<String, NotificationBackend.NotificationsSentState> map = appRow.sentByChannel;
        NotificationBackend.NotificationsSentState notificationsSentState = map != null ? map.get(conversationChannelWrapper.getNotificationChannel().getId()) : null;
        Log.d(TAG, "state:" + notificationsSentState);
        if (appRow.banned) {
            return context.getText(R$string.notifications_disabled);
        }
        int i = appRow.channelCount;
        if (i != 0) {
            int i2 = appRow.blockedChannelCount;
            if (i == i2) {
                return context.getText(R$string.notifications_disabled);
            }
            if (i2 == 0) {
                if (notificationsSentState == null) {
                    return this.mContext.getString(R$string.nt_glyphs_conversation_no_notificaton);
                }
                return NotificationBackend.getSentSummary(context, notificationsSentState, false);
            } else if (notificationsSentState == null) {
                return this.mContext.getString(R$string.nt_glyphs_conversation_no_notificaton);
            } else {
                int i3 = R$string.notifications_enabled_with_info;
                Resources resources = context.getResources();
                int i4 = R$plurals.notifications_categories_off;
                int i5 = appRow.blockedChannelCount;
                return context.getString(i3, new Object[]{NotificationBackend.getSentSummary(context, notificationsSentState, false), resources.getQuantityString(i4, i5, new Object[]{Integer.valueOf(i5)})});
            }
        } else if (notificationsSentState == null) {
            return this.mContext.getString(R$string.nt_glyphs_conversation_no_notificaton);
        } else {
            return NotificationBackend.getSentSummary(context, notificationsSentState, false);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
    }
}
