package android.media;

import java.util.List;
import java.util.function.BiConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class MediaRouter2$MediaRouter2Stub$$ExternalSyntheticLambda7 implements BiConsumer {
    public static final /* synthetic */ MediaRouter2$MediaRouter2Stub$$ExternalSyntheticLambda7 INSTANCE = new MediaRouter2$MediaRouter2Stub$$ExternalSyntheticLambda7();

    private /* synthetic */ MediaRouter2$MediaRouter2Stub$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((MediaRouter2) obj).removeRoutesOnHandler((List) obj2);
    }
}
