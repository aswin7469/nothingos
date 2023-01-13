package com.android.systemui.controls.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.systemui.C1894R;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000 #2\u00020\u0001:\u0001#B3\u0012\u0014\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0018\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0018\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0018\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\"\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016R\u001a\u0010\f\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001f\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlHolderAccessibilityDelegate;", "Landroidx/core/view/AccessibilityDelegateCompat;", "stateRetriever", "Lkotlin/Function1;", "", "", "positionRetriever", "Lkotlin/Function0;", "", "moveHelper", "Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;)V", "isFavorite", "()Z", "setFavorite", "(Z)V", "getMoveHelper", "()Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "getPositionRetriever", "()Lkotlin/jvm/functions/Function0;", "getStateRetriever", "()Lkotlin/jvm/functions/Function1;", "addClickAction", "", "host", "Landroid/view/View;", "info", "Landroidx/core/view/accessibility/AccessibilityNodeInfoCompat;", "maybeAddMoveAfterAction", "maybeAddMoveBeforeAction", "onInitializeAccessibilityNodeInfo", "performAccessibilityAction", "action", "args", "Landroid/os/Bundle;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlAdapter.kt */
final class ControlHolderAccessibilityDelegate extends AccessibilityDelegateCompat {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int MOVE_AFTER_ID = C1894R.C1898id.accessibility_action_controls_move_after;
    private static final int MOVE_BEFORE_ID = C1894R.C1898id.accessibility_action_controls_move_before;
    private boolean isFavorite;
    private final ControlsModel.MoveHelper moveHelper;
    private final Function0<Integer> positionRetriever;
    private final Function1<Boolean, CharSequence> stateRetriever;

    public final Function1<Boolean, CharSequence> getStateRetriever() {
        return this.stateRetriever;
    }

    public final Function0<Integer> getPositionRetriever() {
        return this.positionRetriever;
    }

    public final ControlsModel.MoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public ControlHolderAccessibilityDelegate(Function1<? super Boolean, ? extends CharSequence> function1, Function0<Integer> function0, ControlsModel.MoveHelper moveHelper2) {
        Intrinsics.checkNotNullParameter(function1, "stateRetriever");
        Intrinsics.checkNotNullParameter(function0, "positionRetriever");
        this.stateRetriever = function1;
        this.positionRetriever = function0;
        this.moveHelper = moveHelper2;
    }

    public final boolean isFavorite() {
        return this.isFavorite;
    }

    public final void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlHolderAccessibilityDelegate$Companion;", "", "()V", "MOVE_AFTER_ID", "", "MOVE_BEFORE_ID", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlAdapter.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        Intrinsics.checkNotNullParameter(view, "host");
        Intrinsics.checkNotNullParameter(accessibilityNodeInfoCompat, "info");
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.setContextClickable(false);
        addClickAction(view, accessibilityNodeInfoCompat);
        maybeAddMoveBeforeAction(view, accessibilityNodeInfoCompat);
        maybeAddMoveAfterAction(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.setStateDescription(this.stateRetriever.invoke(Boolean.valueOf(this.isFavorite)));
        accessibilityNodeInfoCompat.setCollectionItemInfo((Object) null);
        accessibilityNodeInfoCompat.setClassName(Switch.class.getName());
    }

    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        Intrinsics.checkNotNullParameter(view, "host");
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        if (i == MOVE_BEFORE_ID) {
            ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
            if (moveHelper2 == null) {
                return true;
            }
            moveHelper2.moveBefore(this.positionRetriever.invoke().intValue());
            return true;
        } else if (i != MOVE_AFTER_ID) {
            return false;
        } else {
            ControlsModel.MoveHelper moveHelper3 = this.moveHelper;
            if (moveHelper3 == null) {
                return true;
            }
            moveHelper3.moveAfter(this.positionRetriever.invoke().intValue());
            return true;
        }
    }

    private final void addClickAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        String str;
        if (this.isFavorite) {
            str = view.getContext().getString(C1894R.string.accessibility_control_change_unfavorite);
        } else {
            str = view.getContext().getString(C1894R.string.accessibility_control_change_favorite);
        }
        accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, str));
    }

    private final void maybeAddMoveBeforeAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
        if (moveHelper2 != null ? moveHelper2.canMoveBefore(this.positionRetriever.invoke().intValue()) : false) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_BEFORE_ID, view.getContext().getString(C1894R.string.accessibility_control_move, new Object[]{Integer.valueOf((this.positionRetriever.invoke().intValue() + 1) - 1)})));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }

    private final void maybeAddMoveAfterAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
        if (moveHelper2 != null ? moveHelper2.canMoveAfter(this.positionRetriever.invoke().intValue()) : false) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_AFTER_ID, view.getContext().getString(C1894R.string.accessibility_control_move, new Object[]{Integer.valueOf(this.positionRetriever.invoke().intValue() + 1 + 1)})));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }
}
