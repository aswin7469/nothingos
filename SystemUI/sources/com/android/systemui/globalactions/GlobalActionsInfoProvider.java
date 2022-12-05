package com.android.systemui.globalactions;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.plugins.ActivityStarter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: GlobalActionsInfoProvider.kt */
/* loaded from: classes.dex */
public final class GlobalActionsInfoProvider {
    @NotNull
    private final ActivityStarter activityStarter;
    @NotNull
    private final Context context;
    @NotNull
    private final ControlsController controlsController;
    @NotNull
    private PendingIntent pendingIntent;
    @NotNull
    private final QuickAccessWalletClient walletClient;

    public GlobalActionsInfoProvider(@NotNull Context context, @NotNull QuickAccessWalletClient walletClient, @NotNull ControlsController controlsController, @NotNull ActivityStarter activityStarter) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(walletClient, "walletClient");
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        this.context = context;
        this.walletClient = walletClient;
        this.controlsController = controlsController;
        this.activityStarter = activityStarter;
        String string = context.getResources().getString(R$string.global_actions_change_url);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(string));
        intent.addFlags(268435456);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 67108864);
        Intrinsics.checkNotNullExpressionValue(activity, "getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)");
        this.pendingIntent = activity;
    }

    public final void addPanel(@NotNull Context context, @NotNull ViewGroup parent, int i, @NotNull final Runnable dismissParent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(dismissParent, "dismissParent");
        if (!(context.getResources().getConfiguration().orientation == 2) || i <= 4) {
            View inflate = LayoutInflater.from(context).inflate(R$layout.global_actions_change_panel, parent, false);
            Object serviceLabel = this.walletClient.getServiceLabel();
            if (serviceLabel == null) {
                serviceLabel = context.getString(R$string.wallet_title);
            }
            TextView textView = (TextView) inflate.findViewById(R$id.global_actions_change_message);
            if (textView != null) {
                textView.setText(context.getString(R$string.global_actions_change_description, serviceLabel));
            }
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsInfoProvider$addPanel$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ActivityStarter activityStarter;
                    PendingIntent pendingIntent;
                    dismissParent.run();
                    activityStarter = this.activityStarter;
                    pendingIntent = this.pendingIntent;
                    activityStarter.postStartActivityDismissingKeyguard(pendingIntent);
                }
            });
            parent.addView(inflate, 0);
            incrementViewCount();
        }
    }

    public final boolean shouldShowMessage() {
        if (!this.context.getResources().getBoolean(R$bool.global_actions_show_change_info)) {
            return false;
        }
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("global_actions_info_prefs", 0);
        int i = (sharedPreferences.contains("view_count") || hadContent()) ? sharedPreferences.getInt("view_count", 0) : -1;
        return i > -1 && i < 3;
    }

    private final boolean hadContent() {
        boolean z = this.controlsController.getFavorites().size() > 0;
        boolean isWalletFeatureAvailable = this.walletClient.isWalletFeatureAvailable();
        Log.d("GlobalActionsInfo", "Previously had controls " + z + ", cards " + isWalletFeatureAvailable);
        return z || isWalletFeatureAvailable;
    }

    private final void incrementViewCount() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("global_actions_info_prefs", 0);
        sharedPreferences.edit().putInt("view_count", sharedPreferences.getInt("view_count", 0) + 1).apply();
    }
}
