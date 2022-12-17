package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class ConversationListSummaryPreferenceController extends BasePreferenceController {
    private NotificationBackend mBackend = new NotificationBackend();

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

    public ConversationListSummaryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        int size = this.mBackend.getConversations(true).getList().size();
        if (size == 0) {
            return this.mContext.getText(R$string.priority_conversation_count_zero);
        }
        return this.mContext.getResources().getQuantityString(R$plurals.priority_conversation_count, size, new Object[]{Integer.valueOf(size)});
    }

    /* access modifiers changed from: package-private */
    public void setBackend(NotificationBackend notificationBackend) {
        this.mBackend = notificationBackend;
    }
}
