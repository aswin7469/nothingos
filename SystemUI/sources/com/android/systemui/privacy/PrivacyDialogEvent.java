package com.android.systemui.privacy;

import com.android.internal.logging.UiEventLogger;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialogEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS", "PRIVACY_DIALOG_DISMISSED", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDialogEvent.kt */
public enum PrivacyDialogEvent implements UiEventLogger.UiEventEnum {
    PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS(904),
    PRIVACY_DIALOG_DISMISSED(905);
    
    private final int _id;

    private PrivacyDialogEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
