package com.nothing.systemui.power;

import android.os.Handler;
import android.util.Slog;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import sun.util.locale.LanguageTag;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@"}, mo64987d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@DebugMetadata(mo65240c = "com.nothing.systemui.power.PowerUIEx$determineTemperatureAndHandleRxTX$1", mo65241f = "PowerUIEx.kt", mo65242i = {}, mo65243l = {}, mo65244m = "invokeSuspend", mo65245n = {}, mo65246s = {})
/* compiled from: PowerUIEx.kt */
final class PowerUIEx$determineTemperatureAndHandleRxTX$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    int label;
    final /* synthetic */ PowerUIEx this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PowerUIEx$determineTemperatureAndHandleRxTX$1(PowerUIEx powerUIEx, Continuation<? super PowerUIEx$determineTemperatureAndHandleRxTX$1> continuation) {
        super(2, continuation);
        this.this$0 = powerUIEx;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new PowerUIEx$determineTemperatureAndHandleRxTX$1(this.this$0, continuation);
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((PowerUIEx$determineTemperatureAndHandleRxTX$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                float flashTemperature = this.this$0.flashTemperature();
                String access$readFile = this.this$0.readFile("/sys/class/qcom-battery/wls_reverse_status");
                Intrinsics.checkNotNull(access$readFile);
                String substring = access$readFile.substring(StringsKt.indexOf$default((CharSequence) access$readFile, LanguageTag.PRIVATEUSE, 0, false, 6, (Object) null) + 1);
                Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
                int parseInt = Integer.parseInt(substring, CharsKt.checkRadix(16));
                NTLogUtil.m1680d("PowerUIEx", "status = " + parseInt + " ; temperature = " + flashTemperature);
                if (parseInt != 0) {
                    if (parseInt == 1) {
                        if (flashTemperature >= 50.0f && !this.this$0.isCloseTx()) {
                            Handler handler = this.this$0.getHandler();
                            final PowerUIEx powerUIEx = this.this$0;
                            handler.post(new Runnable() {
                                public void run() {
                                    powerUIEx.getWarnings().showHighTemperatureWarning();
                                }
                            });
                            this.this$0.setCloseTx(true);
                            PowerUIEx powerUIEx2 = this.this$0;
                            powerUIEx2.setMIsTheLastValue(powerUIEx2.getSettingsValue() == 1);
                            if (this.this$0.getMIsTheLastValue()) {
                                Slog.d("PowerUIEx", ">= 50 setWirelessReverseCharge false");
                                PowerUIEx powerUIEx3 = this.this$0;
                                powerUIEx3.setWirelessReverseCharge(powerUIEx3.getContentResolver(), false);
                            }
                        }
                    }
                } else if (flashTemperature >= 53.0f && this.this$0.getMPluggedInWireless() && !this.this$0.isCloseRx()) {
                    Slog.d("PowerUIEx", ">= 53 setWirelessForwardCharge false");
                    Handler handler2 = this.this$0.getHandler();
                    final PowerUIEx powerUIEx4 = this.this$0;
                    handler2.post(new Runnable() {
                        public void run() {
                            powerUIEx4.getWarnings().showHighTemperatureWarning();
                        }
                    });
                    this.this$0.setCloseRx(true);
                    PowerUIEx powerUIEx5 = this.this$0;
                    powerUIEx5.setWirelessForwardCharge(powerUIEx5.getContentResolver(), false);
                }
                if (flashTemperature <= 48.0f && this.this$0.isCloseRx()) {
                    Handler handler3 = this.this$0.getHandler();
                    final PowerUIEx powerUIEx6 = this.this$0;
                    handler3.post(new Runnable() {
                        public void run() {
                            powerUIEx6.getWarnings().dismissHighTemperatureWarning();
                        }
                    });
                    this.this$0.setCloseRx(false);
                    Slog.d("PowerUIEx", "<= 48 setWirelessForwardCharge true");
                    PowerUIEx powerUIEx7 = this.this$0;
                    powerUIEx7.setWirelessForwardCharge(powerUIEx7.getContentResolver(), true);
                }
                if (flashTemperature <= 45.0f && this.this$0.isCloseTx() && this.this$0.getMIsTheLastValue()) {
                    Handler handler4 = this.this$0.getHandler();
                    final PowerUIEx powerUIEx8 = this.this$0;
                    handler4.post(new Runnable() {
                        public void run() {
                            powerUIEx8.getWarnings().dismissHighTemperatureWarning();
                        }
                    });
                    this.this$0.setCloseTx(false);
                    Slog.d("PowerUIEx", "<= 45 setWirelessReverseCharge true");
                    PowerUIEx powerUIEx9 = this.this$0;
                    powerUIEx9.setWirelessReverseCharge(powerUIEx9.getContentResolver(), true);
                }
            } catch (Exception e) {
                NTLogUtil.m1680d("PowerUIEx", e.toString());
            }
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
