package com.android.systemui.p012qs;

import androidx.recyclerview.widget.DiffUtil;
import com.android.systemui.p012qs.FgsManagerController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0005H\u0016J\b\u0010\t\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/qs/FgsManagerController$AppListAdapter$setData$1", "Landroidx/recyclerview/widget/DiffUtil$Callback;", "areContentsTheSame", "", "oldItemPosition", "", "newItemPosition", "areItemsTheSame", "getNewListSize", "getOldListSize", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.FgsManagerController$AppListAdapter$setData$1 */
/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$AppListAdapter$setData$1 extends DiffUtil.Callback {
    final /* synthetic */ List<FgsManagerController.RunningApp> $newData;
    final /* synthetic */ Ref.ObjectRef<List<FgsManagerController.RunningApp>> $oldData;

    FgsManagerController$AppListAdapter$setData$1(Ref.ObjectRef<List<FgsManagerController.RunningApp>> objectRef, List<FgsManagerController.RunningApp> list) {
        this.$oldData = objectRef;
        this.$newData = list;
    }

    public int getOldListSize() {
        return ((List) this.$oldData.element).size();
    }

    public int getNewListSize() {
        return this.$newData.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        return Intrinsics.areEqual(((List) this.$oldData.element).get(i), (Object) this.$newData.get(i2));
    }

    public boolean areContentsTheSame(int i, int i2) {
        return ((FgsManagerController.RunningApp) ((List) this.$oldData.element).get(i)).getStopped() == this.$newData.get(i2).getStopped();
    }
}
