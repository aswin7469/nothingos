package com.nothing.systemui.navigationbar;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.SparseArray;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarView;
import com.nothing.systemui.util.NTLogUtil;

public class NavigationBarControllerEx {
    private static final String TAG = "NavigationBarControllerEx";
    private Context mContext;
    private Handler mHandler;
    private SparseArray<NavigationBar> mNavigationBars = new SparseArray<>();

    public void registerNavBarCombinationObserver(Context context, Handler handler, SparseArray<NavigationBar> sparseArray) {
        NTLogUtil.m1680d(TAG, "registerNavBarCombinationObserver");
        this.mContext = context;
        this.mHandler = handler;
        this.mNavigationBars = sparseArray;
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(NavigationBarInflaterViewEx.NAV_BAR_COMBINATION), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                NTLogUtil.m1680d(NavigationBarControllerEx.TAG, "receive NavBarCombinationChange");
                NavigationBarControllerEx.this.onNavBarCombinationChange();
            }
        }, -1);
    }

    /* access modifiers changed from: private */
    public void onNavBarCombinationChange() {
        this.mHandler.post(new NavigationBarControllerEx$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNavBarCombinationChange$0$com-nothing-systemui-navigationbar-NavigationBarControllerEx */
    public /* synthetic */ void mo57489x378af467() {
        for (int i = 0; i < this.mNavigationBars.size(); i++) {
            NavigationBar valueAt = this.mNavigationBars.valueAt(i);
            if (valueAt == null) {
                NTLogUtil.m1684w(TAG, "onNavBarCombinationChange: navbar is null");
            } else {
                NavigationBarView view = valueAt.getView();
                if (view != null) {
                    NTLogUtil.m1684w(TAG, "onNavBarCombinationChange: update");
                    view.updateNavBarCombination();
                }
            }
        }
    }
}
