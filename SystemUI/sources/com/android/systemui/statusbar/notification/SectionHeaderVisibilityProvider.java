package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\t\"\u0004\b\f\u0010\r¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/SectionHeaderVisibilityProvider;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "<set-?>", "", "neverShowSectionHeaders", "getNeverShowSectionHeaders", "()Z", "sectionHeadersVisible", "getSectionHeadersVisible", "setSectionHeadersVisible", "(Z)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SectionHeaderVisibilityProvider.kt */
public final class SectionHeaderVisibilityProvider {
    private boolean neverShowSectionHeaders;
    private boolean sectionHeadersVisible = true;

    @Inject
    public SectionHeaderVisibilityProvider(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.neverShowSectionHeaders = context.getResources().getBoolean(C1894R.bool.config_notification_never_show_section_headers);
    }

    public final boolean getNeverShowSectionHeaders() {
        return this.neverShowSectionHeaders;
    }

    public final boolean getSectionHeadersVisible() {
        return this.sectionHeadersVisible;
    }

    public final void setSectionHeadersVisible(boolean z) {
        this.sectionHeadersVisible = z;
    }
}
