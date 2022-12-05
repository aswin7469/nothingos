package com.android.systemui.controls.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlHolderAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean isFavorite;
    @Nullable
    private final ControlsModel.MoveHelper moveHelper;
    @NotNull
    private final Function0<Integer> positionRetriever;
    @NotNull
    private final Function1<Boolean, CharSequence> stateRetriever;
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final int MOVE_BEFORE_ID = R$id.accessibility_action_controls_move_before;
    private static final int MOVE_AFTER_ID = R$id.accessibility_action_controls_move_after;

    /* JADX WARN: Multi-variable type inference failed */
    public ControlHolderAccessibilityDelegate(@NotNull Function1<? super Boolean, ? extends CharSequence> stateRetriever, @NotNull Function0<Integer> positionRetriever, @Nullable ControlsModel.MoveHelper moveHelper) {
        Intrinsics.checkNotNullParameter(stateRetriever, "stateRetriever");
        Intrinsics.checkNotNullParameter(positionRetriever, "positionRetriever");
        this.stateRetriever = stateRetriever;
        this.positionRetriever = positionRetriever;
        this.moveHelper = moveHelper;
    }

    public final void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    /* compiled from: ControlAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(@NotNull View host, @NotNull AccessibilityNodeInfoCompat info) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(info, "info");
        super.onInitializeAccessibilityNodeInfo(host, info);
        info.setContextClickable(false);
        addClickAction(host, info);
        maybeAddMoveBeforeAction(host, info);
        maybeAddMoveAfterAction(host, info);
        info.setStateDescription(this.stateRetriever.mo1949invoke(Boolean.valueOf(this.isFavorite)));
        info.setCollectionItemInfo(null);
        info.setClassName(Switch.class.getName());
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public boolean performAccessibilityAction(@Nullable View view, int i, @Nullable Bundle bundle) {
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        if (i == MOVE_BEFORE_ID) {
            ControlsModel.MoveHelper moveHelper = this.moveHelper;
            if (moveHelper == null) {
                return true;
            }
            moveHelper.moveBefore(this.positionRetriever.mo1951invoke().intValue());
            return true;
        } else if (i != MOVE_AFTER_ID) {
            return false;
        } else {
            ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
            if (moveHelper2 == null) {
                return true;
            }
            moveHelper2.moveAfter(this.positionRetriever.mo1951invoke().intValue());
            return true;
        }
    }

    private final void addClickAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        String string;
        if (this.isFavorite) {
            string = view.getContext().getString(R$string.accessibility_control_change_unfavorite);
        } else {
            string = view.getContext().getString(R$string.accessibility_control_change_favorite);
        }
        accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, string));
    }

    private final void maybeAddMoveBeforeAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper = this.moveHelper;
        if (moveHelper == null ? false : moveHelper.canMoveBefore(this.positionRetriever.mo1951invoke().intValue())) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_BEFORE_ID, view.getContext().getString(R$string.accessibility_control_move, Integer.valueOf((this.positionRetriever.mo1951invoke().intValue() + 1) - 1))));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }

    private final void maybeAddMoveAfterAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper = this.moveHelper;
        if (moveHelper == null ? false : moveHelper.canMoveAfter(this.positionRetriever.mo1951invoke().intValue())) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_AFTER_ID, view.getContext().getString(R$string.accessibility_control_move, Integer.valueOf(this.positionRetriever.mo1951invoke().intValue() + 1 + 1))));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }
}
