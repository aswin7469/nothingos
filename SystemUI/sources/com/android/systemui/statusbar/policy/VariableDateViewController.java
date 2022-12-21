package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.SystemClock;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u001d\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001.B%\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0002¢\u0006\u0002\u0010\nJ\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0010H\u0002J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020\u0018H\u0002J\b\u0010&\u001a\u00020\"H\u0014J\b\u0010'\u001a\u00020\"H\u0014J\u001d\u0010(\u001a\u0004\u0018\u00010)2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\"0+H\u0002¢\u0006\u0002\u0010,J\b\u0010-\u001a\u00020\"H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\u00020\u00108BX\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0010\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0004\n\u0002\u0010\u001eR\u0014\u0010\u001f\u001a\u00020\u00108BX\u0004¢\u0006\u0006\u001a\u0004\b \u0010\u001bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/VariableDateViewController;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/statusbar/policy/VariableDateView;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "timeTickHandler", "Landroid/os/Handler;", "view", "(Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/broadcast/BroadcastDispatcher;Landroid/os/Handler;Lcom/android/systemui/statusbar/policy/VariableDateView;)V", "currentTime", "Ljava/util/Date;", "dateFormat", "Landroid/icu/text/DateFormat;", "value", "", "datePattern", "setDatePattern", "(Ljava/lang/String;)V", "intentReceiver", "Landroid/content/BroadcastReceiver;", "lastText", "lastWidth", "", "longerPattern", "getLongerPattern", "()Ljava/lang/String;", "onMeasureListener", "com/android/systemui/statusbar/policy/VariableDateViewController$onMeasureListener$1", "Lcom/android/systemui/statusbar/policy/VariableDateViewController$onMeasureListener$1;", "shorterPattern", "getShorterPattern", "changePattern", "", "newPattern", "maybeChangeFormat", "availableWidth", "onViewAttached", "onViewDetached", "post", "", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Boolean;", "updateClock", "Factory", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController extends ViewController<VariableDateView> {
    private final BroadcastDispatcher broadcastDispatcher;
    private Date currentTime = new Date();
    /* access modifiers changed from: private */
    public DateFormat dateFormat;
    private String datePattern;
    private final BroadcastReceiver intentReceiver = new VariableDateViewController$intentReceiver$1(this);
    private String lastText = "";
    /* access modifiers changed from: private */
    public int lastWidth = Integer.MAX_VALUE;
    private final VariableDateViewController$onMeasureListener$1 onMeasureListener = new VariableDateViewController$onMeasureListener$1(this);
    private final SystemClock systemClock;
    private final Handler timeTickHandler;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public VariableDateViewController(SystemClock systemClock2, BroadcastDispatcher broadcastDispatcher2, Handler handler, VariableDateView variableDateView) {
        super(variableDateView);
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(handler, "timeTickHandler");
        Intrinsics.checkNotNullParameter(variableDateView, "view");
        this.systemClock = systemClock2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.timeTickHandler = handler;
        this.datePattern = variableDateView.getLongerPattern();
    }

    private final void setDatePattern(String str) {
        if (!Intrinsics.areEqual((Object) this.datePattern, (Object) str)) {
            this.datePattern = str;
            this.dateFormat = null;
            if (isAttachedToWindow()) {
                post(new VariableDateViewController$datePattern$1(this));
            }
        }
    }

    private final String getLongerPattern() {
        return ((VariableDateView) this.mView).getLongerPattern();
    }

    private final String getShorterPattern() {
        return ((VariableDateView) this.mView).getShorterPattern();
    }

    private final Boolean post(Function0<Unit> function0) {
        Handler handler = ((VariableDateView) this.mView).getHandler();
        if (handler != null) {
            return Boolean.valueOf(handler.post(new VariableDateViewController$$ExternalSyntheticLambda0(function0)));
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* renamed from: post$lambda-0  reason: not valid java name */
    public static final void m3246post$lambda0(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$tmp0");
        function0.invoke();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.intentReceiver, intentFilter, new HandlerExecutor(this.timeTickHandler), UserHandle.SYSTEM, 0, (String) null, 48, (Object) null);
        post(new VariableDateViewController$onViewAttached$1(this));
        ((VariableDateView) this.mView).onAttach(this.onMeasureListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.dateFormat = null;
        ((VariableDateView) this.mView).onAttach((VariableDateView.OnMeasureListener) null);
        this.broadcastDispatcher.unregisterReceiver(this.intentReceiver);
    }

    /* access modifiers changed from: private */
    public final void updateClock() {
        if (this.dateFormat == null) {
            this.dateFormat = VariableDateViewControllerKt.getFormatFromPattern(this.datePattern);
        }
        this.currentTime.setTime(this.systemClock.currentTimeMillis());
        Date date = this.currentTime;
        DateFormat dateFormat2 = this.dateFormat;
        Intrinsics.checkNotNull(dateFormat2);
        String textForFormat = VariableDateViewControllerKt.getTextForFormat(date, dateFormat2);
        if (!Intrinsics.areEqual((Object) textForFormat, (Object) this.lastText)) {
            ((VariableDateView) this.mView).setText(textForFormat);
            this.lastText = textForFormat;
        }
    }

    /* access modifiers changed from: private */
    public final void maybeChangeFormat(int i) {
        if (((VariableDateView) this.mView).getFreezeSwitching()) {
            return;
        }
        if (i > this.lastWidth && Intrinsics.areEqual((Object) this.datePattern, (Object) getLongerPattern())) {
            return;
        }
        if (i >= this.lastWidth || !Intrinsics.areEqual((Object) this.datePattern, (Object) "")) {
            float f = (float) i;
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
        if (!str.equals(this.datePattern)) {
            setDatePattern(str);
        }
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/VariableDateViewController$Factory;", "", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "handler", "Landroid/os/Handler;", "(Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/broadcast/BroadcastDispatcher;Landroid/os/Handler;)V", "create", "Lcom/android/systemui/statusbar/policy/VariableDateViewController;", "view", "Lcom/android/systemui/statusbar/policy/VariableDateView;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: VariableDateViewController.kt */
    public static final class Factory {
        private final BroadcastDispatcher broadcastDispatcher;
        private final Handler handler;
        private final SystemClock systemClock;

        @Inject
        public Factory(SystemClock systemClock2, BroadcastDispatcher broadcastDispatcher2, @Named("time_tick_handler") Handler handler2) {
            Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
            Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
            Intrinsics.checkNotNullParameter(handler2, "handler");
            this.systemClock = systemClock2;
            this.broadcastDispatcher = broadcastDispatcher2;
            this.handler = handler2;
        }

        public final VariableDateViewController create(VariableDateView variableDateView) {
            Intrinsics.checkNotNullParameter(variableDateView, "view");
            return new VariableDateViewController(this.systemClock, this.broadcastDispatcher, this.handler, variableDateView);
        }
    }
}
