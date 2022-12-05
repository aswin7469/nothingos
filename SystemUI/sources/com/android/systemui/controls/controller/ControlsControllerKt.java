package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsController.kt */
/* loaded from: classes.dex */
public final class ControlsControllerKt {
    public static /* synthetic */ ControlsController.LoadData createLoadDataObject$default(List list, List list2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return createLoadDataObject(list, list2, z);
    }

    @NotNull
    public static final ControlsController.LoadData createLoadDataObject(@NotNull final List<ControlStatus> allControls, @NotNull final List<String> favorites, final boolean z) {
        Intrinsics.checkNotNullParameter(allControls, "allControls");
        Intrinsics.checkNotNullParameter(favorites, "favorites");
        return new ControlsController.LoadData(allControls, favorites, z) { // from class: com.android.systemui.controls.controller.ControlsControllerKt$createLoadDataObject$1
            final /* synthetic */ List<ControlStatus> $allControls;
            final /* synthetic */ boolean $error;
            final /* synthetic */ List<String> $favorites;
            @NotNull
            private final List<ControlStatus> allControls;
            private final boolean errorOnLoad;
            @NotNull
            private final List<String> favoritesIds;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$allControls = allControls;
                this.$favorites = favorites;
                this.$error = z;
                this.allControls = allControls;
                this.favoritesIds = favorites;
                this.errorOnLoad = z;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            @NotNull
            public List<ControlStatus> getAllControls() {
                return this.allControls;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            @NotNull
            public List<String> getFavoritesIds() {
                return this.favoritesIds;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            public boolean getErrorOnLoad() {
                return this.errorOnLoad;
            }
        };
    }
}
