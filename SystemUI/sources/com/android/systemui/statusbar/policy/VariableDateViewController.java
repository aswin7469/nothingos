package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.SystemClock;
import java.util.Date;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: VariableDateViewController.kt */
/* loaded from: classes2.dex */
public final class VariableDateViewController extends ViewController<VariableDateView> {
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @Nullable
    private DateFormat dateFormat;
    @NotNull
    private String datePattern;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final Handler timeTickHandler;
    private int lastWidth = Integer.MAX_VALUE;
    @NotNull
    private String lastText = "";
    @NotNull
    private Date currentTime = new Date();
    @NotNull
    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(@NotNull Context context, @NotNull Intent intent) {
            View view;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(intent, "intent");
            view = ((ViewController) VariableDateViewController.this).mView;
            Handler handler = ((VariableDateView) view).getHandler();
            if (handler == null) {
                return;
            }
            String action = intent.getAction();
            if (!Intrinsics.areEqual("android.intent.action.TIME_TICK", action) && !Intrinsics.areEqual("android.intent.action.TIME_SET", action) && !Intrinsics.areEqual("android.intent.action.TIMEZONE_CHANGED", action) && !Intrinsics.areEqual("android.intent.action.LOCALE_CHANGED", action)) {
                return;
            }
            if (Intrinsics.areEqual("android.intent.action.LOCALE_CHANGED", action) || Intrinsics.areEqual("android.intent.action.TIMEZONE_CHANGED", action)) {
                final VariableDateViewController variableDateViewController = VariableDateViewController.this;
                handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1$onReceive$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        VariableDateViewController.this.dateFormat = null;
                    }
                });
            }
            final VariableDateViewController variableDateViewController2 = VariableDateViewController.this;
            handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1$onReceive$2
                @Override // java.lang.Runnable
                public final void run() {
                    VariableDateViewController.this.updateClock();
                }
            });
        }
    };
    @NotNull
    private final VariableDateViewController$onMeasureListener$1 onMeasureListener = new VariableDateView.OnMeasureListener() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$onMeasureListener$1
        @Override // com.android.systemui.statusbar.policy.VariableDateView.OnMeasureListener
        public void onMeasureAction(int i) {
            int i2;
            i2 = VariableDateViewController.this.lastWidth;
            if (i != i2) {
                VariableDateViewController.this.maybeChangeFormat(i);
                VariableDateViewController.this.lastWidth = i;
            }
        }
    };

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.statusbar.policy.VariableDateViewController$onMeasureListener$1] */
    public VariableDateViewController(@NotNull SystemClock systemClock, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Handler timeTickHandler, @NotNull VariableDateView view) {
        super(view);
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(timeTickHandler, "timeTickHandler");
        Intrinsics.checkNotNullParameter(view, "view");
        this.systemClock = systemClock;
        this.broadcastDispatcher = broadcastDispatcher;
        this.timeTickHandler = timeTickHandler;
        this.datePattern = view.getLongerPattern();
    }

    private final void setDatePattern(String str) {
        if (Intrinsics.areEqual(this.datePattern, str)) {
            return;
        }
        this.datePattern = str;
        this.dateFormat = null;
        if (!isAttachedToWindow()) {
            return;
        }
        post(new VariableDateViewController$datePattern$1(this));
    }

    private final String getLongerPattern() {
        return ((VariableDateView) this.mView).getLongerPattern();
    }

    private final String getShorterPattern() {
        return ((VariableDateView) this.mView).getShorterPattern();
    }

    private final Boolean post(final Function0<Unit> function0) {
        Handler handler = ((VariableDateView) this.mView).getHandler();
        if (handler == null) {
            return null;
        }
        return Boolean.valueOf(handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.VariableDateViewControllerKt$sam$java_lang_Runnable$0
            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                Function0.this.mo1951invoke();
            }
        }));
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        this.broadcastDispatcher.registerReceiver(this.intentReceiver, intentFilter, new HandlerExecutor(this.timeTickHandler), UserHandle.SYSTEM);
        post(new VariableDateViewController$onViewAttached$1(this));
        ((VariableDateView) this.mView).onAttach(this.onMeasureListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.dateFormat = null;
        ((VariableDateView) this.mView).onAttach(null);
        this.broadcastDispatcher.unregisterReceiver(this.intentReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateClock() {
        if (this.dateFormat == null) {
            this.dateFormat = VariableDateViewControllerKt.getFormatFromPattern(this.datePattern);
        }
        this.currentTime.setTime(this.systemClock.currentTimeMillis());
        Date date = this.currentTime;
        DateFormat dateFormat = this.dateFormat;
        Intrinsics.checkNotNull(dateFormat);
        String textForFormat = VariableDateViewControllerKt.getTextForFormat(date, dateFormat);
        if (!Intrinsics.areEqual(textForFormat, this.lastText)) {
            ((VariableDateView) this.mView).setText(textForFormat);
            this.lastText = textForFormat;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void maybeChangeFormat(int i) {
        if (!((VariableDateView) this.mView).getFreezeSwitching()) {
            if (i > this.lastWidth && Intrinsics.areEqual(this.datePattern, getLongerPattern())) {
                return;
            }
            if (i < this.lastWidth && Intrinsics.areEqual(this.datePattern, "")) {
                return;
            }
            float f = i;
            if (((VariableDateView) this.mView).getDesiredWidthForText(VariableDateViewControllerKt.getTextForFormat(this.currentTime, VariableDateViewControllerKt.getFormatFromPattern(getLongerPattern()))) <= f) {
                changePattern(getLongerPattern());
                return;
            }
            if (((VariableDateView) this.mView).getDesiredWidthForText(VariableDateViewControllerKt.getTextForFormat(this.currentTime, VariableDateViewControllerKt.getFormatFromPattern(getShorterPattern()))) <= f) {
                changePattern(getShorterPattern());
            } else {
                changePattern("");
            }
        }
    }

    private final void changePattern(String str) {
        if (str.equals(this.datePattern)) {
            return;
        }
        setDatePattern(str);
    }

    /* compiled from: VariableDateViewController.kt */
    /* loaded from: classes2.dex */
    public static final class Factory {
        @NotNull
        private final BroadcastDispatcher broadcastDispatcher;
        @NotNull
        private final Handler handler;
        @NotNull
        private final SystemClock systemClock;

        public Factory(@NotNull SystemClock systemClock, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Handler handler) {
            Intrinsics.checkNotNullParameter(systemClock, "systemClock");
            Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
            Intrinsics.checkNotNullParameter(handler, "handler");
            this.systemClock = systemClock;
            this.broadcastDispatcher = broadcastDispatcher;
            this.handler = handler;
        }

        @NotNull
        public final VariableDateViewController create(@NotNull VariableDateView view) {
            Intrinsics.checkNotNullParameter(view, "view");
            return new VariableDateViewController(this.systemClock, this.broadcastDispatcher, this.handler, view);
        }
    }
}
