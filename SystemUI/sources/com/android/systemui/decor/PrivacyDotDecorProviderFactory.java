package com.android.systemui.decor;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068BX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\bR\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b8VX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/decor/PrivacyDotDecorProviderFactory;", "Lcom/android/systemui/decor/DecorProviderFactory;", "res", "Landroid/content/res/Resources;", "(Landroid/content/res/Resources;)V", "hasProviders", "", "getHasProviders", "()Z", "isPrivacyDotEnabled", "providers", "", "Lcom/android/systemui/decor/DecorProvider;", "getProviders", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDotDecorProviderFactory.kt */
public final class PrivacyDotDecorProviderFactory extends DecorProviderFactory {
    private final Resources res;

    @Inject
    public PrivacyDotDecorProviderFactory(@Main Resources resources) {
        Intrinsics.checkNotNullParameter(resources, "res");
        this.res = resources;
    }

    private final boolean isPrivacyDotEnabled() {
        return this.res.getBoolean(C1893R.bool.config_enablePrivacyDot);
    }

    public boolean getHasProviders() {
        return isPrivacyDotEnabled();
    }

    public List<DecorProvider> getProviders() {
        if (!getHasProviders()) {
            return CollectionsKt.emptyList();
        }
        return CollectionsKt.listOf(new PrivacyDotCornerDecorProviderImpl(C1893R.C1897id.privacy_dot_top_left_container, 1, 0, C1893R.layout.privacy_dot_top_left), new PrivacyDotCornerDecorProviderImpl(C1893R.C1897id.privacy_dot_top_right_container, 1, 2, C1893R.layout.privacy_dot_top_right), new PrivacyDotCornerDecorProviderImpl(C1893R.C1897id.privacy_dot_bottom_left_container, 3, 0, C1893R.layout.privacy_dot_bottom_left), new PrivacyDotCornerDecorProviderImpl(C1893R.C1897id.privacy_dot_bottom_right_container, 3, 2, C1893R.layout.privacy_dot_bottom_right));
    }
}
