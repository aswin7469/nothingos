package com.android.systemui.p012qs.external;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.tileimpl.QSIconViewImpl;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.p012qs.tileimpl.QSTileViewImpl;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u000b2\u00020\u0001:\u0002\u000b\fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/qs/external/TileRequestDialog;", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "createTileView", "Lcom/android/systemui/plugins/qs/QSTileView;", "tileData", "Lcom/android/systemui/qs/external/TileRequestDialog$TileData;", "setTileData", "", "Companion", "TileData", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.external.TileRequestDialog */
/* compiled from: TileRequestDialog.kt */
public final class TileRequestDialog extends SystemUIDialog {
    /* access modifiers changed from: private */
    public static final int CONTENT_ID = C1893R.C1897id.content;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TileRequestDialog(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/qs/external/TileRequestDialog$Companion;", "", "()V", "CONTENT_ID", "", "getCONTENT_ID$SystemUI_nothingRelease", "()I", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileRequestDialog$Companion */
    /* compiled from: TileRequestDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getCONTENT_ID$SystemUI_nothingRelease() {
            return TileRequestDialog.CONTENT_ID;
        }
    }

    public final void setTileData(TileData tileData) {
        Intrinsics.checkNotNullParameter(tileData, "tileData");
        View inflate = LayoutInflater.from(getContext()).inflate(C1893R.layout.tile_service_request_dialog, (ViewGroup) null);
        if (inflate != null) {
            ViewGroup viewGroup = (ViewGroup) inflate;
            TextView textView = (TextView) viewGroup.requireViewById(C1893R.C1897id.text);
            textView.setText(textView.getContext().getString(C1893R.string.qs_tile_request_dialog_text, new Object[]{tileData.getAppName()}));
            viewGroup.addView(createTileView(tileData), viewGroup.getContext().getResources().getDimensionPixelSize(C1893R.dimen.qs_tile_service_request_tile_width), viewGroup.getContext().getResources().getDimensionPixelSize(C1893R.dimen.qs_quick_tile_size));
            viewGroup.setSelected(true);
            setView(viewGroup, 0, 0, 0, 0);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    private final QSTileView createTileView(TileData tileData) {
        QSTile.Icon icon;
        Drawable loadDrawable;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        QSTileViewImpl qSTileViewImpl = new QSTileViewImpl(context, new QSIconViewImpl(getContext()), true);
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = tileData.getLabel();
        booleanState.handlesLongClick = false;
        Icon icon2 = tileData.getIcon();
        if (icon2 == null || (loadDrawable = icon2.loadDrawable(getContext())) == null) {
            icon = QSTileImpl.ResourceIcon.get(C1893R.C1895drawable.f953android);
        } else {
            Intrinsics.checkNotNullExpressionValue(loadDrawable, "loadDrawable(context)");
            icon = new QSTileImpl.DrawableIcon(loadDrawable);
        }
        booleanState.icon = icon;
        qSTileViewImpl.onStateChanged(booleanState);
        qSTileViewImpl.post(new TileRequestDialog$$ExternalSyntheticLambda0(qSTileViewImpl));
        return qSTileViewImpl;
    }

    /* access modifiers changed from: private */
    /* renamed from: createTileView$lambda-4  reason: not valid java name */
    public static final void m2947createTileView$lambda4(QSTileViewImpl qSTileViewImpl) {
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "$tile");
        qSTileViewImpl.setStateDescription("");
        qSTileViewImpl.setClickable(false);
        qSTileViewImpl.setSelected(true);
    }

    @Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0006HÆ\u0003J)\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018"}, mo64987d2 = {"Lcom/android/systemui/qs/external/TileRequestDialog$TileData;", "", "appName", "", "label", "icon", "Landroid/graphics/drawable/Icon;", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Landroid/graphics/drawable/Icon;)V", "getAppName", "()Ljava/lang/CharSequence;", "getIcon", "()Landroid/graphics/drawable/Icon;", "getLabel", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileRequestDialog$TileData */
    /* compiled from: TileRequestDialog.kt */
    public static final class TileData {
        private final CharSequence appName;
        private final Icon icon;
        private final CharSequence label;

        public static /* synthetic */ TileData copy$default(TileData tileData, CharSequence charSequence, CharSequence charSequence2, Icon icon2, int i, Object obj) {
            if ((i & 1) != 0) {
                charSequence = tileData.appName;
            }
            if ((i & 2) != 0) {
                charSequence2 = tileData.label;
            }
            if ((i & 4) != 0) {
                icon2 = tileData.icon;
            }
            return tileData.copy(charSequence, charSequence2, icon2);
        }

        public final CharSequence component1() {
            return this.appName;
        }

        public final CharSequence component2() {
            return this.label;
        }

        public final Icon component3() {
            return this.icon;
        }

        public final TileData copy(CharSequence charSequence, CharSequence charSequence2, Icon icon2) {
            Intrinsics.checkNotNullParameter(charSequence, "appName");
            Intrinsics.checkNotNullParameter(charSequence2, BaseIconCache.IconDB.COLUMN_LABEL);
            return new TileData(charSequence, charSequence2, icon2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TileData)) {
                return false;
            }
            TileData tileData = (TileData) obj;
            return Intrinsics.areEqual((Object) this.appName, (Object) tileData.appName) && Intrinsics.areEqual((Object) this.label, (Object) tileData.label) && Intrinsics.areEqual((Object) this.icon, (Object) tileData.icon);
        }

        public int hashCode() {
            int hashCode = ((this.appName.hashCode() * 31) + this.label.hashCode()) * 31;
            Icon icon2 = this.icon;
            return hashCode + (icon2 == null ? 0 : icon2.hashCode());
        }

        public String toString() {
            return "TileData(appName=" + this.appName + ", label=" + this.label + ", icon=" + this.icon + ')';
        }

        public TileData(CharSequence charSequence, CharSequence charSequence2, Icon icon2) {
            Intrinsics.checkNotNullParameter(charSequence, "appName");
            Intrinsics.checkNotNullParameter(charSequence2, BaseIconCache.IconDB.COLUMN_LABEL);
            this.appName = charSequence;
            this.label = charSequence2;
            this.icon = icon2;
        }

        public final CharSequence getAppName() {
            return this.appName;
        }

        public final CharSequence getLabel() {
            return this.label;
        }

        public final Icon getIcon() {
            return this.icon;
        }
    }
}
