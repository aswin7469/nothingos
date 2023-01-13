package com.android.systemui.privacy;

import com.android.systemui.Dumpable;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0001\nJ\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\u0006H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemMonitor;", "Lcom/android/systemui/Dumpable;", "getActivePrivacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "startListening", "", "callback", "Lcom/android/systemui/privacy/PrivacyItemMonitor$Callback;", "stopListening", "Callback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyItemMonitor.kt */
public interface PrivacyItemMonitor extends Dumpable {

    @Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemMonitor$Callback;", "", "onPrivacyItemsChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyItemMonitor.kt */
    public interface Callback {
        void onPrivacyItemsChanged();
    }

    List<PrivacyItem> getActivePrivacyItems();

    void startListening(Callback callback);

    void stopListening();
}
