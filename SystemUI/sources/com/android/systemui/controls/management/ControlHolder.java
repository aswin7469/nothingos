package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.systemui.C1893R;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.p010ui.RenderInfo;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u001c\u0010\u0006\u001a\u0018\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0007j\u0002`\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0002J\u0010\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020%H\u0016J\u0018\u0010&\u001a\u00020 2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*H\u0002J\u0012\u0010+\u001a\u0004\u0018\u00010,2\u0006\u0010\u000f\u001a\u00020\tH\u0002J\u0010\u0010-\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\tH\u0016R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R'\u0010\u0006\u001a\u0018\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0007j\u0002`\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0013\u001a\n \u0014*\u0004\u0018\u00010\b0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0016\u0010\u0019\u001a\n \u0014*\u0004\u0018\u00010\b0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000¨\u0006."}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlHolder;", "Lcom/android/systemui/controls/management/Holder;", "view", "Landroid/view/View;", "moveHelper", "Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "favoriteCallback", "Lkotlin/Function2;", "", "", "", "Lcom/android/systemui/controls/management/ModelFavoriteChanger;", "(Landroid/view/View;Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;Lkotlin/jvm/functions/Function2;)V", "accessibilityDelegate", "Lcom/android/systemui/controls/management/ControlHolderAccessibilityDelegate;", "favorite", "Landroid/widget/CheckBox;", "getFavoriteCallback", "()Lkotlin/jvm/functions/Function2;", "favoriteStateDescription", "kotlin.jvm.PlatformType", "icon", "Landroid/widget/ImageView;", "getMoveHelper", "()Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "notFavoriteStateDescription", "removed", "Landroid/widget/TextView;", "subtitle", "title", "applyRenderInfo", "ri", "Lcom/android/systemui/controls/ui/RenderInfo;", "ci", "Lcom/android/systemui/controls/ControlInterface;", "bindData", "wrapper", "Lcom/android/systemui/controls/management/ElementWrapper;", "getRenderInfo", "component", "Landroid/content/ComponentName;", "deviceType", "", "stateDescription", "", "updateFavorite", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlAdapter.kt */
public final class ControlHolder extends Holder {
    private final ControlHolderAccessibilityDelegate accessibilityDelegate;
    private final CheckBox favorite;
    private final Function2<String, Boolean, Unit> favoriteCallback;
    private final String favoriteStateDescription = this.itemView.getContext().getString(C1893R.string.accessibility_control_favorite);
    private final ImageView icon;
    private final ControlsModel.MoveHelper moveHelper;
    private final String notFavoriteStateDescription = this.itemView.getContext().getString(C1893R.string.accessibility_control_not_favorite);
    private final TextView removed;
    private final TextView subtitle;
    private final TextView title;

    public final ControlsModel.MoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public final Function2<String, Boolean, Unit> getFavoriteCallback() {
        return this.favoriteCallback;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlHolder(View view, ControlsModel.MoveHelper moveHelper2, Function2<? super String, ? super Boolean, Unit> function2) {
        super(view, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(function2, "favoriteCallback");
        this.moveHelper = moveHelper2;
        this.favoriteCallback = function2;
        View requireViewById = this.itemView.requireViewById(C1893R.C1897id.icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.icon)");
        this.icon = (ImageView) requireViewById;
        View requireViewById2 = this.itemView.requireViewById(C1893R.C1897id.title);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.title)");
        this.title = (TextView) requireViewById2;
        View requireViewById3 = this.itemView.requireViewById(C1893R.C1897id.subtitle);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.subtitle)");
        this.subtitle = (TextView) requireViewById3;
        View requireViewById4 = this.itemView.requireViewById(C1893R.C1897id.status);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById(R.id.status)");
        this.removed = (TextView) requireViewById4;
        View requireViewById5 = this.itemView.requireViewById(C1893R.C1897id.favorite);
        CheckBox checkBox = (CheckBox) requireViewById5;
        checkBox.setVisibility(0);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById…lity = View.VISIBLE\n    }");
        this.favorite = checkBox;
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = new ControlHolderAccessibilityDelegate(new ControlHolder$accessibilityDelegate$1(this), new ControlHolder$accessibilityDelegate$2(this), moveHelper2);
        this.accessibilityDelegate = controlHolderAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this.itemView, controlHolderAccessibilityDelegate);
    }

    /* access modifiers changed from: private */
    public final CharSequence stateDescription(boolean z) {
        if (!z) {
            return this.notFavoriteStateDescription;
        }
        if (this.moveHelper == null) {
            return this.favoriteStateDescription;
        }
        return this.itemView.getContext().getString(C1893R.string.accessibility_control_favorite_position, new Object[]{Integer.valueOf(getLayoutPosition() + 1)});
    }

    public void bindData(ElementWrapper elementWrapper) {
        Intrinsics.checkNotNullParameter(elementWrapper, "wrapper");
        ControlInterface controlInterface = (ControlInterface) elementWrapper;
        RenderInfo renderInfo = getRenderInfo(controlInterface.getComponent(), controlInterface.getDeviceType());
        this.title.setText(controlInterface.getTitle());
        this.subtitle.setText(controlInterface.getSubtitle());
        updateFavorite(controlInterface.getFavorite());
        this.removed.setText(controlInterface.getRemoved() ? this.itemView.getContext().getText(C1893R.string.controls_removed) : "");
        this.itemView.setOnClickListener(new ControlHolder$$ExternalSyntheticLambda0(this, elementWrapper));
        applyRenderInfo(renderInfo, controlInterface);
    }

    /* access modifiers changed from: private */
    /* renamed from: bindData$lambda-1  reason: not valid java name */
    public static final void m2636bindData$lambda1(ControlHolder controlHolder, ElementWrapper elementWrapper, View view) {
        Intrinsics.checkNotNullParameter(controlHolder, "this$0");
        Intrinsics.checkNotNullParameter(elementWrapper, "$wrapper");
        controlHolder.updateFavorite(!controlHolder.favorite.isChecked());
        controlHolder.favoriteCallback.invoke(((ControlInterface) elementWrapper).getControlId(), Boolean.valueOf(controlHolder.favorite.isChecked()));
    }

    public void updateFavorite(boolean z) {
        this.favorite.setChecked(z);
        this.accessibilityDelegate.setFavorite(z);
        this.itemView.setStateDescription(stateDescription(z));
    }

    private final RenderInfo getRenderInfo(ComponentName componentName, int i) {
        RenderInfo.Companion companion = RenderInfo.Companion;
        Context context = this.itemView.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "itemView.context");
        return RenderInfo.Companion.lookup$default(companion, context, componentName, i, 0, 8, (Object) null);
    }

    private final void applyRenderInfo(RenderInfo renderInfo, ControlInterface controlInterface) {
        Context context = this.itemView.getContext();
        ColorStateList colorStateList = context.getResources().getColorStateList(renderInfo.getForeground(), context.getTheme());
        Unit unit = null;
        this.icon.setImageTintList((ColorStateList) null);
        Icon customIcon = controlInterface.getCustomIcon();
        if (customIcon != null) {
            this.icon.setImageIcon(customIcon);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            ControlHolder controlHolder = this;
            this.icon.setImageDrawable(renderInfo.getIcon());
            if (controlInterface.getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
    }
}
