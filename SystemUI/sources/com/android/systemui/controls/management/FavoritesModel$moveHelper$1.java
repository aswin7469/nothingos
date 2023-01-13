package com.android.systemui.controls.management;

import android.util.Log;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/management/FavoritesModel$moveHelper$1", "Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "canMoveAfter", "", "position", "", "canMoveBefore", "moveAfter", "", "moveBefore", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FavoritesModel.kt */
public final class FavoritesModel$moveHelper$1 implements ControlsModel.MoveHelper {
    final /* synthetic */ FavoritesModel this$0;

    FavoritesModel$moveHelper$1(FavoritesModel favoritesModel) {
        this.this$0 = favoritesModel;
    }

    public boolean canMoveBefore(int i) {
        return i > 0 && i < this.this$0.dividerPosition;
    }

    public boolean canMoveAfter(int i) {
        return i >= 0 && i < this.this$0.dividerPosition - 1;
    }

    public void moveBefore(int i) {
        if (!canMoveBefore(i)) {
            Log.w("FavoritesModel", "Cannot move position " + i + " before");
        } else {
            this.this$0.onMoveItem(i, i - 1);
        }
    }

    public void moveAfter(int i) {
        if (!canMoveAfter(i)) {
            Log.w("FavoritesModel", "Cannot move position " + i + " after");
        } else {
            this.this$0.onMoveItem(i, i + 1);
        }
    }
}
