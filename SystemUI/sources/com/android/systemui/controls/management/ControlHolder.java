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
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.RenderInfo;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlHolder extends Holder {
    @NotNull
    private final ControlHolderAccessibilityDelegate accessibilityDelegate;
    @NotNull
    private final CheckBox favorite;
    @NotNull
    private final Function2<String, Boolean, Unit> favoriteCallback;
    @NotNull
    private final ImageView icon;
    @Nullable
    private final ControlsModel.MoveHelper moveHelper;
    @NotNull
    private final TextView removed;
    @NotNull
    private final TextView subtitle;
    @NotNull
    private final TextView title;
    private final String favoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_favorite);
    private final String notFavoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_not_favorite);

    @NotNull
    public final Function2<String, Boolean, Unit> getFavoriteCallback() {
        return this.favoriteCallback;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ControlHolder(@NotNull View view, @Nullable ControlsModel.MoveHelper moveHelper, @NotNull Function2<? super String, ? super Boolean, Unit> favoriteCallback) {
        super(view, null);
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(favoriteCallback, "favoriteCallback");
        this.moveHelper = moveHelper;
        this.favoriteCallback = favoriteCallback;
        View requireViewById = this.itemView.requireViewById(R$id.icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.icon)");
        this.icon = (ImageView) requireViewById;
        View requireViewById2 = this.itemView.requireViewById(R$id.title);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.title)");
        this.title = (TextView) requireViewById2;
        View requireViewById3 = this.itemView.requireViewById(R$id.subtitle);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.subtitle)");
        this.subtitle = (TextView) requireViewById3;
        View requireViewById4 = this.itemView.requireViewById(R$id.status);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById(R.id.status)");
        this.removed = (TextView) requireViewById4;
        View requireViewById5 = this.itemView.requireViewById(R$id.favorite);
        CheckBox checkBox = (CheckBox) requireViewById5;
        checkBox.setVisibility(0);
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById<CheckBox>(R.id.favorite).apply {\n        visibility = View.VISIBLE\n    }");
        this.favorite = checkBox;
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = new ControlHolderAccessibilityDelegate(new ControlHolder$accessibilityDelegate$1(this), new ControlHolder$accessibilityDelegate$2(this), moveHelper);
        this.accessibilityDelegate = controlHolderAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this.itemView, controlHolderAccessibilityDelegate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CharSequence stateDescription(boolean z) {
        if (!z) {
            return this.notFavoriteStateDescription;
        }
        if (this.moveHelper == null) {
            return this.favoriteStateDescription;
        }
        return this.itemView.getContext().getString(R$string.accessibility_control_favorite_position, Integer.valueOf(getLayoutPosition() + 1));
    }

    @Override // com.android.systemui.controls.management.Holder
    public void bindData(@NotNull final ElementWrapper wrapper) {
        Intrinsics.checkNotNullParameter(wrapper, "wrapper");
        ControlInterface controlInterface = (ControlInterface) wrapper;
        RenderInfo renderInfo = getRenderInfo(controlInterface.getComponent(), controlInterface.getDeviceType());
        this.title.setText(controlInterface.getTitle());
        this.subtitle.setText(controlInterface.getSubtitle());
        updateFavorite(controlInterface.getFavorite());
        this.removed.setText(controlInterface.getRemoved() ? this.itemView.getContext().getText(R$string.controls_removed) : "");
        this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlHolder$bindData$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CheckBox checkBox;
                CheckBox checkBox2;
                ControlHolder controlHolder = ControlHolder.this;
                checkBox = controlHolder.favorite;
                controlHolder.updateFavorite(!checkBox.isChecked());
                Function2<String, Boolean, Unit> favoriteCallback = ControlHolder.this.getFavoriteCallback();
                String controlId = ((ControlInterface) wrapper).getControlId();
                checkBox2 = ControlHolder.this.favorite;
                favoriteCallback.mo1950invoke(controlId, Boolean.valueOf(checkBox2.isChecked()));
            }
        });
        applyRenderInfo(renderInfo, controlInterface);
    }

    @Override // com.android.systemui.controls.management.Holder
    public void updateFavorite(boolean z) {
        this.favorite.setChecked(z);
        this.accessibilityDelegate.setFavorite(z);
        this.itemView.setStateDescription(stateDescription(z));
    }

    private final RenderInfo getRenderInfo(ComponentName componentName, int i) {
        RenderInfo.Companion companion = RenderInfo.Companion;
        Context context = this.itemView.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "itemView.context");
        return RenderInfo.Companion.lookup$default(companion, context, componentName, i, 0, 8, null);
    }

    private final void applyRenderInfo(RenderInfo renderInfo, ControlInterface controlInterface) {
        Context context = this.itemView.getContext();
        ColorStateList colorStateList = context.getResources().getColorStateList(renderInfo.getForeground(), context.getTheme());
        Unit unit = null;
        this.icon.setImageTintList(null);
        Icon customIcon = controlInterface.getCustomIcon();
        if (customIcon != null) {
            this.icon.setImageIcon(customIcon);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            this.icon.setImageDrawable(renderInfo.getIcon());
            if (controlInterface.getDeviceType() == 52) {
                return;
            }
            this.icon.setImageTintList(colorStateList);
        }
    }
}
