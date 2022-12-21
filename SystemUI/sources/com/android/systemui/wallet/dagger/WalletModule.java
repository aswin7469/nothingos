package com.android.systemui.wallet.dagger;

import android.app.Activity;
import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.wallet.p017ui.WalletActivity;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import java.util.concurrent.Executor;

@Module
public abstract class WalletModule {
    @IntoMap
    @ClassKey(WalletActivity.class)
    @Binds
    public abstract Activity provideWalletActivity(WalletActivity walletActivity);

    @SysUISingleton
    @Provides
    public static QuickAccessWalletClient provideQuickAccessWalletClient(Context context, @Background Executor executor) {
        return QuickAccessWalletClient.create(context, executor);
    }
}
