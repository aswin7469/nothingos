package com.nothing.systemui.p024qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.view.View;
import android.view.Window;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.tiles.CellularTile;
import com.android.systemui.p012qs.tiles.dialog.InternetDialog;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J&\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fJ&\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013¨\u0006\u0015"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/InternetTileEx;", "", "()V", "resetHeaderOnClickListener", "", "dataHeader", "Landroid/view/View;", "wifiHeader", "setHeaderOnClickListener", "dialog", "Lcom/android/systemui/qs/tiles/dialog/InternetDialog;", "controller", "Lcom/android/systemui/qs/tiles/dialog/InternetDialogController;", "updateWindowSize", "context", "Landroid/content/Context;", "window", "Landroid/view/Window;", "isOnCreate", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx */
/* compiled from: InternetTileEx.kt */
public final class InternetTileEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "InternetTileEx";

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/InternetTileEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx$Companion */
    /* compiled from: InternetTileEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void setHeaderOnClickListener(InternetDialog internetDialog, View view, View view2, InternetDialogController internetDialogController) {
        Intrinsics.checkNotNullParameter(internetDialog, "dialog");
        Intrinsics.checkNotNullParameter(view, "dataHeader");
        Intrinsics.checkNotNullParameter(view2, "wifiHeader");
        Intrinsics.checkNotNullParameter(internetDialogController, "controller");
        view.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda0(internetDialogController, internetDialog));
        view2.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda1(internetDialogController, internetDialog));
    }

    /* access modifiers changed from: private */
    /* renamed from: setHeaderOnClickListener$lambda-0  reason: not valid java name */
    public static final void m3520setHeaderOnClickListener$lambda0(InternetDialogController internetDialogController, InternetDialog internetDialog, View view) {
        Intrinsics.checkNotNullParameter(internetDialogController, "$controller");
        Intrinsics.checkNotNullParameter(internetDialog, "$dialog");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        internetDialogController.startActivityDismissingKeyguard(CellularTile.getCellularSettingIntent(), view);
        internetDialog.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: setHeaderOnClickListener$lambda-1  reason: not valid java name */
    public static final void m3521setHeaderOnClickListener$lambda1(InternetDialogController internetDialogController, InternetDialog internetDialog, View view) {
        Intrinsics.checkNotNullParameter(internetDialogController, "$controller");
        Intrinsics.checkNotNullParameter(internetDialog, "$dialog");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        internetDialogController.startActivityDismissingKeyguard(new Intent("android.settings.WIFI_SETTINGS"), view);
        internetDialog.dismiss();
    }

    public final void resetHeaderOnClickListener(View view, View view2) {
        Intrinsics.checkNotNullParameter(view, "dataHeader");
        Intrinsics.checkNotNullParameter(view2, "wifiHeader");
        view.setOnClickListener((View.OnClickListener) null);
        view2.setOnClickListener((View.OnClickListener) null);
    }

    public final void updateWindowSize(Context context, Window window, InternetDialogController internetDialogController, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(window, "window");
        Intrinsics.checkNotNullParameter(internetDialogController, "controller");
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1893R.dimen.settings_panel_width);
        int dimensionPixelSize2 = internetDialogController.isWifiEnabled() ? context.getResources().getDimensionPixelSize(C1893R.dimen.settings_panel_height) : -2;
        NTLogUtil.m1680d(TAG, "updateWindowSize: " + dimensionPixelSize + ", " + dimensionPixelSize2);
        if (z) {
            window.setLayout(dimensionPixelSize, dimensionPixelSize2);
        }
        ThreadUtils.postOnMainThread(new InternetTileEx$$ExternalSyntheticLambda2(window, dimensionPixelSize, dimensionPixelSize2));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWindowSize$lambda-2  reason: not valid java name */
    public static final void m3522updateWindowSize$lambda2(Window window, int i, int i2) {
        Intrinsics.checkNotNullParameter(window, "$window");
        window.setLayout(i, i2);
    }
}
