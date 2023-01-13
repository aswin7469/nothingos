package com.android.systemui.decor;

import java.util.List;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/decor/DecorProviderFactory;", "", "()V", "hasProviders", "", "getHasProviders", "()Z", "providers", "", "Lcom/android/systemui/decor/DecorProvider;", "getProviders", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DecorProviderFactory.kt */
public abstract class DecorProviderFactory {
    public abstract boolean getHasProviders();

    public abstract List<DecorProvider> getProviders();
}
