package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.events.BackgroundAnimatableView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.privacy.OngoingPrivacyChipEx;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u00012\u00020\u0002B/\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nJ\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u001bH\u0014J(\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\bH\u0016J\b\u0010$\u001a\u00020\u001bH\u0002J\u0010\u0010%\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u000e\u0010\u000b\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R0\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019¨\u0006&"}, mo64987d2 = {"Lcom/android/systemui/privacy/OngoingPrivacyChip;", "Landroid/widget/FrameLayout;", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttrs", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "iconColor", "iconMargin", "iconSize", "iconsContainer", "Landroid/widget/LinearLayout;", "mEx", "Lcom/nothing/systemui/privacy/OngoingPrivacyChipEx;", "value", "", "Lcom/android/systemui/privacy/PrivacyItem;", "privacyList", "getPrivacyList", "()Ljava/util/List;", "setPrivacyList", "(Ljava/util/List;)V", "generateContentDescription", "", "builder", "Lcom/android/systemui/privacy/PrivacyChipBuilder;", "onFinishInflate", "setBoundsForAnimation", "l", "t", "r", "b", "updateResources", "updateView", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: OngoingPrivacyChip.kt */
public final class OngoingPrivacyChip extends FrameLayout implements BackgroundAnimatableView {
    public Map<Integer, View> _$_findViewCache;
    private int iconColor;
    private int iconMargin;
    private int iconSize;
    private LinearLayout iconsContainer;
    private OngoingPrivacyChipEx mEx;
    private List<PrivacyItem> privacyList;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
        Object obj = NTDependencyEx.get(OngoingPrivacyChipEx.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(OngoingPrivacyChipEx::class.java)");
        this.mEx = (OngoingPrivacyChipEx) obj;
        this.privacyList = CollectionsKt.emptyList();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public final List<PrivacyItem> getPrivacyList() {
        return this.privacyList;
    }

    public final void setPrivacyList(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "value");
        this.privacyList = list;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        updateView(new PrivacyChipBuilder(context, this.privacyList));
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View requireViewById = requireViewById(C1893R.C1897id.icons_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(R.id.icons_container)");
        this.iconsContainer = (LinearLayout) requireViewById;
        updateResources();
    }

    public void setBoundsForAnimation(int i, int i2, int i3, int i4) {
        LinearLayout linearLayout = this.iconsContainer;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
            linearLayout = null;
        }
        linearLayout.setLeftTopRightBottom(i - getLeft(), i2 - getTop(), i3 - getLeft(), i4 - getTop());
    }

    private static final void updateView$setIcons(OngoingPrivacyChip ongoingPrivacyChip, PrivacyChipBuilder privacyChipBuilder, ViewGroup viewGroup) {
        if (ongoingPrivacyChip.getTag() != null && ongoingPrivacyChip.getTag().equals(ongoingPrivacyChip.getContext().getString(C1893R.string.nt_qs_header_privacy_icons))) {
            ongoingPrivacyChip.iconColor = ongoingPrivacyChip.mEx.updateIconColor();
        }
        viewGroup.removeAllViews();
        int i = 0;
        for (Object next : privacyChipBuilder.generateIcons()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Drawable drawable = (Drawable) next;
            drawable.mutate();
            drawable.setTint(ongoingPrivacyChip.iconColor);
            ImageView imageView = new ImageView(ongoingPrivacyChip.getContext());
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int i3 = ongoingPrivacyChip.iconSize;
            viewGroup.addView(imageView, i3, i3);
            if (i != 0) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (layoutParams != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMarginStart(ongoingPrivacyChip.iconMargin);
                    imageView.setLayoutParams(marginLayoutParams);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                }
            }
            i = i2;
        }
    }

    private final void updateView(PrivacyChipBuilder privacyChipBuilder) {
        LinearLayout linearLayout = null;
        if (!this.privacyList.isEmpty()) {
            generateContentDescription(privacyChipBuilder);
            LinearLayout linearLayout2 = this.iconsContainer;
            if (linearLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
            } else {
                linearLayout = linearLayout2;
            }
            updateView$setIcons(this, privacyChipBuilder, linearLayout);
        } else {
            LinearLayout linearLayout3 = this.iconsContainer;
            if (linearLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
            } else {
                linearLayout = linearLayout3;
            }
            linearLayout.removeAllViews();
        }
        requestLayout();
    }

    private final void generateContentDescription(PrivacyChipBuilder privacyChipBuilder) {
        String joinTypes = privacyChipBuilder.joinTypes();
        setContentDescription(getContext().getString(C1893R.string.ongoing_privacy_chip_content_multiple_apps, new Object[]{joinTypes}));
    }

    private final void updateResources() {
        this.iconMargin = getContext().getResources().getDimensionPixelSize(C1893R.dimen.ongoing_appops_chip_icon_margin);
        this.iconSize = getContext().getResources().getDimensionPixelSize(C1893R.dimen.ongoing_appops_chip_icon_size);
        this.iconColor = Utils.getColorAttrDefaultColor(getContext(), 16843827);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(C1893R.dimen.ongoing_appops_chip_side_padding);
        LinearLayout linearLayout = this.iconsContainer;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
            linearLayout = null;
        }
        linearLayout.setPaddingRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
        LinearLayout linearLayout3 = this.iconsContainer;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
        } else {
            linearLayout2 = linearLayout3;
        }
        linearLayout2.setBackground(getContext().getDrawable(C1893R.C1895drawable.privacy_chip_bg));
    }
}
