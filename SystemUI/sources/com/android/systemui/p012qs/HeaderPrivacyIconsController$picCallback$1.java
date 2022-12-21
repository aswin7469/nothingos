package com.android.systemui.p012qs;

import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0016\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\b\u0010\u000b\u001a\u00020\u0003H\u0002¨\u0006\f"}, mo64987d2 = {"com/android/systemui/qs/HeaderPrivacyIconsController$picCallback$1", "Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "onFlagLocationChanged", "", "flag", "", "onFlagMicCameraChanged", "onPrivacyItemsChanged", "privacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "update", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.HeaderPrivacyIconsController$picCallback$1 */
/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$picCallback$1 implements PrivacyItemController.Callback {
    final /* synthetic */ HeaderPrivacyIconsController this$0;

    HeaderPrivacyIconsController$picCallback$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "privacyItems");
        this.this$0.privacyChip.setPrivacyList(list);
        this.this$0.setChipVisibility(!list.isEmpty());
    }

    public void onFlagMicCameraChanged(boolean z) {
        if (this.this$0.micCameraIndicatorsEnabled != z) {
            this.this$0.micCameraIndicatorsEnabled = z;
            update();
        }
    }

    public void onFlagLocationChanged(boolean z) {
        if (this.this$0.locationIndicatorsEnabled != z) {
            this.this$0.locationIndicatorsEnabled = z;
            update();
        }
    }

    private final void update() {
        this.this$0.updatePrivacyIconSlots();
        HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
        headerPrivacyIconsController.setChipVisibility(!headerPrivacyIconsController.privacyChip.getPrivacyList().isEmpty());
    }
}
