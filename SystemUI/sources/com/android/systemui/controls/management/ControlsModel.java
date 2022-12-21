package com.android.systemui.controls.management;

import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.controls.controller.ControlInfo;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0002\u001b\u001cJ\u0014\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0016R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u0006R\u0014\u0010\n\u001a\u0004\u0018\u00010\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u001dÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsModel;", "", "elements", "", "Lcom/android/systemui/controls/management/ElementWrapper;", "getElements", "()Ljava/util/List;", "favorites", "Lcom/android/systemui/controls/controller/ControlInfo;", "getFavorites", "moveHelper", "Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "getMoveHelper", "()Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "attachAdapter", "", "adapter", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "changeFavoriteStatus", "controlId", "", "favorite", "", "onMoveItem", "from", "", "to", "ControlsModelCallback", "MoveHelper", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsModel.kt */
public interface ControlsModel {

    @Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsModel$ControlsModelCallback;", "", "onFirstChange", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsModel.kt */
    public interface ControlsModelCallback {
        void onFirstChange();
    }

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "", "canMoveAfter", "", "position", "", "canMoveBefore", "moveAfter", "", "moveBefore", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsModel.kt */
    public interface MoveHelper {
        boolean canMoveAfter(int i);

        boolean canMoveBefore(int i);

        void moveAfter(int i);

        void moveBefore(int i);
    }

    void attachAdapter(RecyclerView.Adapter<?> adapter) {
        Intrinsics.checkNotNullParameter(adapter, "adapter");
    }

    void changeFavoriteStatus(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "controlId");
    }

    List<ElementWrapper> getElements();

    List<ControlInfo> getFavorites();

    MoveHelper getMoveHelper();

    void onMoveItem(int i, int i2) {
    }
}
