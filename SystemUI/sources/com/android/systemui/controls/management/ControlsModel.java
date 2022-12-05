package com.android.systemui.controls.management;

import com.android.systemui.controls.controller.ControlInfo;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public interface ControlsModel {

    /* compiled from: ControlsModel.kt */
    /* loaded from: classes.dex */
    public interface ControlsModelCallback {
        void onFirstChange();
    }

    /* compiled from: ControlsModel.kt */
    /* loaded from: classes.dex */
    public interface MoveHelper {
        boolean canMoveAfter(int i);

        boolean canMoveBefore(int i);

        void moveAfter(int i);

        void moveBefore(int i);
    }

    void changeFavoriteStatus(@NotNull String str, boolean z);

    @NotNull
    List<ElementWrapper> getElements();

    @NotNull
    List<ControlInfo> getFavorites();

    @Nullable
    /* renamed from: getMoveHelper */
    MoveHelper mo411getMoveHelper();
}
