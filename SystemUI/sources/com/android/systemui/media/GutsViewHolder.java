package com.android.systemui.media;

import android.content.res.ColorStateList;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.monet.ColorScheme;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\u0018\u0000 22\u00020\u0001:\u00012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&J\u000e\u0010'\u001a\u00020!2\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\rJ\u000e\u0010,\u001a\u00020!2\u0006\u0010-\u001a\u00020\u001bJ\u000e\u0010.\u001a\u00020!2\u0006\u0010/\u001a\u00020)J\u000e\u00100\u001a\u00020!2\u0006\u00101\u001a\u00020)R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u0013¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0016\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000bR\u0011\u0010\u0018\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000bR\u000e\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001c\u001a\u00020\u001d¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u00063"}, mo64987d2 = {"Lcom/android/systemui/media/GutsViewHolder;", "", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "cancel", "getCancel", "()Landroid/view/View;", "cancelText", "Landroid/widget/TextView;", "getCancelText", "()Landroid/widget/TextView;", "colorScheme", "Lcom/android/systemui/monet/ColorScheme;", "getColorScheme", "()Lcom/android/systemui/monet/ColorScheme;", "setColorScheme", "(Lcom/android/systemui/monet/ColorScheme;)V", "dismiss", "Landroid/view/ViewGroup;", "getDismiss", "()Landroid/view/ViewGroup;", "dismissText", "getDismissText", "gutsText", "getGutsText", "isDismissible", "", "settings", "Landroid/widget/ImageButton;", "getSettings", "()Landroid/widget/ImageButton;", "marquee", "", "start", "delay", "", "tag", "", "setAccentPrimaryColor", "accentPrimary", "", "setColors", "scheme", "setDismissible", "dismissible", "setSurfaceColor", "surfaceColor", "setTextPrimaryColor", "textPrimary", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: GutsViewHolder.kt */
public final class GutsViewHolder {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Set<Integer> ids = SetsKt.setOf(Integer.valueOf((int) C1893R.C1897id.remove_text), Integer.valueOf((int) C1893R.C1897id.cancel), Integer.valueOf((int) C1893R.C1897id.dismiss), Integer.valueOf((int) C1893R.C1897id.settings));
    private final View cancel;
    private final TextView cancelText;
    private ColorScheme colorScheme;
    private final ViewGroup dismiss;
    private final TextView dismissText;
    private final TextView gutsText;
    private boolean isDismissible = true;
    private final ImageButton settings;

    public GutsViewHolder(View view) {
        Intrinsics.checkNotNullParameter(view, "itemView");
        View requireViewById = view.requireViewById(C1893R.C1897id.remove_text);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.remove_text)");
        this.gutsText = (TextView) requireViewById;
        View requireViewById2 = view.requireViewById(C1893R.C1897id.cancel);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.cancel)");
        this.cancel = requireViewById2;
        View requireViewById3 = view.requireViewById(C1893R.C1897id.cancel_text);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.cancel_text)");
        this.cancelText = (TextView) requireViewById3;
        View requireViewById4 = view.requireViewById(C1893R.C1897id.dismiss);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById(R.id.dismiss)");
        this.dismiss = (ViewGroup) requireViewById4;
        View requireViewById5 = view.requireViewById(C1893R.C1897id.dismiss_text);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById(R.id.dismiss_text)");
        this.dismissText = (TextView) requireViewById5;
        View requireViewById6 = view.requireViewById(C1893R.C1897id.settings);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "itemView.requireViewById(R.id.settings)");
        this.settings = (ImageButton) requireViewById6;
    }

    public final TextView getGutsText() {
        return this.gutsText;
    }

    public final View getCancel() {
        return this.cancel;
    }

    public final TextView getCancelText() {
        return this.cancelText;
    }

    public final ViewGroup getDismiss() {
        return this.dismiss;
    }

    public final TextView getDismissText() {
        return this.dismissText;
    }

    public final ImageButton getSettings() {
        return this.settings;
    }

    public final ColorScheme getColorScheme() {
        return this.colorScheme;
    }

    public final void setColorScheme(ColorScheme colorScheme2) {
        this.colorScheme = colorScheme2;
    }

    public final void marquee(boolean z, long j, String str) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Handler handler = this.gutsText.getHandler();
        if (handler == null) {
            Log.d(str, "marquee while longPressText.getHandler() is null", new Exception());
        } else {
            handler.postDelayed(new GutsViewHolder$$ExternalSyntheticLambda0(this, z), j);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: marquee$lambda-0  reason: not valid java name */
    public static final void m2761marquee$lambda0(GutsViewHolder gutsViewHolder, boolean z) {
        Intrinsics.checkNotNullParameter(gutsViewHolder, "this$0");
        gutsViewHolder.gutsText.setSelected(z);
    }

    public final void setDismissible(boolean z) {
        if (this.isDismissible != z) {
            this.isDismissible = z;
            ColorScheme colorScheme2 = this.colorScheme;
            if (colorScheme2 != null) {
                setColors(colorScheme2);
            }
        }
    }

    public final void setColors(ColorScheme colorScheme2) {
        Intrinsics.checkNotNullParameter(colorScheme2, "scheme");
        this.colorScheme = colorScheme2;
        setSurfaceColor(MediaColorSchemesKt.surfaceFromScheme(colorScheme2));
        setTextPrimaryColor(MediaColorSchemesKt.textPrimaryFromScheme(colorScheme2));
        setAccentPrimaryColor(MediaColorSchemesKt.accentPrimaryFromScheme(colorScheme2));
    }

    public final void setSurfaceColor(int i) {
        this.dismissText.setTextColor(i);
        if (!this.isDismissible) {
            this.cancelText.setTextColor(i);
        }
    }

    public final void setAccentPrimaryColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.settings.setImageTintList(valueOf);
        this.cancelText.setBackgroundTintList(valueOf);
        this.dismissText.setBackgroundTintList(valueOf);
    }

    public final void setTextPrimaryColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.gutsText.setTextColor(valueOf);
        if (this.isDismissible) {
            this.cancelText.setTextColor(valueOf);
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/media/GutsViewHolder$Companion;", "", "()V", "ids", "", "", "getIds", "()Ljava/util/Set;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: GutsViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Set<Integer> getIds() {
            return GutsViewHolder.ids;
        }
    }
}
