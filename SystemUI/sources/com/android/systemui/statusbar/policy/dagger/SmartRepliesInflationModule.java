package com.android.systemui.statusbar.policy.dagger;

import com.android.systemui.statusbar.policy.SmartActionInflater;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyInflater;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflater;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl;
import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\bH'J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u000bH'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/dagger/SmartRepliesInflationModule;", "", "bindSmartActionsInflater", "Lcom/android/systemui/statusbar/policy/SmartActionInflater;", "impl", "Lcom/android/systemui/statusbar/policy/SmartActionInflaterImpl;", "bindSmartReplyInflater", "Lcom/android/systemui/statusbar/policy/SmartReplyInflater;", "Lcom/android/systemui/statusbar/policy/SmartReplyInflaterImpl;", "bindsInflatedSmartRepliesProvider", "Lcom/android/systemui/statusbar/policy/SmartReplyStateInflater;", "Lcom/android/systemui/statusbar/policy/SmartReplyStateInflaterImpl;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: SmartRepliesInflationModule.kt */
public interface SmartRepliesInflationModule {
    @Binds
    SmartActionInflater bindSmartActionsInflater(SmartActionInflaterImpl smartActionInflaterImpl);

    @Binds
    SmartReplyInflater bindSmartReplyInflater(SmartReplyInflaterImpl smartReplyInflaterImpl);

    @Binds
    SmartReplyStateInflater bindsInflatedSmartRepliesProvider(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl);
}
