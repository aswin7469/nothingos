package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: InternetDialogFactory.kt */
/* loaded from: classes.dex */
public final class InternetDialogFactory {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private static InternetDialog internetDialog;
    @NotNull
    private final Context context;
    @NotNull
    private final Executor executor;
    @NotNull
    private final Handler handler;
    @NotNull
    private final InternetDialogController internetDialogController;
    @NotNull
    private final UiEventLogger uiEventLogger;

    public InternetDialogFactory(@NotNull Handler handler, @NotNull Executor executor, @NotNull InternetDialogController internetDialogController, @NotNull Context context, @NotNull UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(internetDialogController, "internetDialogController");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.handler = handler;
        this.executor = executor;
        this.internetDialogController = internetDialogController;
        this.context = context;
        this.uiEventLogger = uiEventLogger;
    }

    /* compiled from: InternetDialogFactory.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @Nullable
        public final InternetDialog getInternetDialog() {
            return InternetDialogFactory.internetDialog;
        }

        public final void setInternetDialog(@Nullable InternetDialog internetDialog) {
            InternetDialogFactory.internetDialog = internetDialog;
        }
    }

    public final void create(boolean z, boolean z2, boolean z3) {
        boolean z4;
        Companion companion = Companion;
        if (companion.getInternetDialog() != null) {
            z4 = InternetDialogFactoryKt.DEBUG;
            if (!z4) {
                return;
            }
            Log.d("InternetDialogFactory", "InternetDialog is showing, do not create it twice.");
            return;
        }
        companion.setInternetDialog(new InternetDialog(this.context, this, this.internetDialogController, z2, z3, z, this.uiEventLogger, this.handler, this.executor));
        InternetDialog internetDialog2 = companion.getInternetDialog();
        if (internetDialog2 == null) {
            return;
        }
        internetDialog2.show();
    }

    public final void destroyDialog() {
        boolean z;
        z = InternetDialogFactoryKt.DEBUG;
        if (z) {
            Log.d("InternetDialogFactory", "destroyDialog");
        }
        Companion.setInternetDialog(null);
    }
}
