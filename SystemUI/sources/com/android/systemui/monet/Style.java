package com.android.systemui.monet;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/monet/Style;", "", "coreSpec", "Lcom/android/systemui/monet/CoreSpec;", "(Ljava/lang/String;ILcom/android/systemui/monet/CoreSpec;)V", "getCoreSpec$monet_release", "()Lcom/android/systemui/monet/CoreSpec;", "SPRITZ", "TONAL_SPOT", "VIBRANT", "EXPRESSIVE", "RAINBOW", "FRUIT_SALAD", "CONTENT", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorScheme.kt */
public enum Style {
    SPRITZ(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(12.0d)), new TonalSpec(new HueSource(), new ChromaConstant(8.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueSource(), new ChromaConstant(2.0d)), new TonalSpec(new HueSource(), new ChromaConstant(2.0d)))),
    TONAL_SPOT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueAdd(60.0d), new ChromaConstant(24.0d)), new TonalSpec(new HueSource(), new ChromaConstant(4.0d)), new TonalSpec(new HueSource(), new ChromaConstant(8.0d)))),
    VIBRANT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaMaxOut()), new TonalSpec(new HueVibrantSecondary(), new ChromaConstant(24.0d)), new TonalSpec(new HueVibrantTertiary(), new ChromaConstant(32.0d)), new TonalSpec(new HueSource(), new ChromaConstant(10.0d)), new TonalSpec(new HueSource(), new ChromaConstant(12.0d)))),
    EXPRESSIVE(new CoreSpec(new TonalSpec(new HueAdd(240.0d), new ChromaConstant(40.0d)), new TonalSpec(new HueExpressiveSecondary(), new ChromaConstant(24.0d)), new TonalSpec(new HueExpressiveTertiary(), new ChromaConstant(32.0d)), new TonalSpec(new HueAdd(15.0d), new ChromaConstant(8.0d)), new TonalSpec(new HueAdd(15.0d), new ChromaConstant(12.0d)))),
    RAINBOW(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(48.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueAdd(60.0d), new ChromaConstant(24.0d)), new TonalSpec(new HueSource(), new ChromaConstant(0.0d)), new TonalSpec(new HueSource(), new ChromaConstant(0.0d)))),
    FRUIT_SALAD(new CoreSpec(new TonalSpec(new HueSubtract(50.0d), new ChromaConstant(48.0d)), new TonalSpec(new HueSubtract(50.0d), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(10.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)))),
    CONTENT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaSource()), new TonalSpec(new HueSource(), new ChromaMultiple(0.33d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.66d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.0833d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.1666d))));
    
    private final CoreSpec coreSpec;

    private Style(CoreSpec coreSpec2) {
        this.coreSpec = coreSpec2;
    }

    public final CoreSpec getCoreSpec$monet_release() {
        return this.coreSpec;
    }
}
