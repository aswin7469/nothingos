package com.android.settings.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.android.settings.SubSettings;
/* loaded from: classes.dex */
public class SubSettingLauncher {
    private final Context mContext;
    private final LaunchRequest mLaunchRequest;
    private boolean mLaunched;

    public SubSettingLauncher(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must be non-null.");
        }
        this.mContext = context;
        LaunchRequest launchRequest = new LaunchRequest();
        this.mLaunchRequest = launchRequest;
        launchRequest.transitionType = 0;
    }

    public SubSettingLauncher setDestination(String str) {
        this.mLaunchRequest.destinationName = str;
        return this;
    }

    public SubSettingLauncher setTitleRes(int i) {
        return setTitleRes(null, i);
    }

    public SubSettingLauncher setTitleRes(String str, int i) {
        LaunchRequest launchRequest = this.mLaunchRequest;
        launchRequest.titleResPackageName = str;
        launchRequest.titleResId = i;
        launchRequest.title = null;
        return this;
    }

    public SubSettingLauncher setTitleText(CharSequence charSequence) {
        this.mLaunchRequest.title = charSequence;
        return this;
    }

    public SubSettingLauncher setArguments(Bundle bundle) {
        this.mLaunchRequest.arguments = bundle;
        return this;
    }

    public SubSettingLauncher setExtras(Bundle bundle) {
        this.mLaunchRequest.extras = bundle;
        return this;
    }

    public SubSettingLauncher setSourceMetricsCategory(int i) {
        this.mLaunchRequest.sourceMetricsCategory = i;
        return this;
    }

    public SubSettingLauncher setResultListener(Fragment fragment, int i) {
        LaunchRequest launchRequest = this.mLaunchRequest;
        launchRequest.mRequestCode = i;
        launchRequest.mResultListener = fragment;
        return this;
    }

    public SubSettingLauncher addFlags(int i) {
        LaunchRequest launchRequest = this.mLaunchRequest;
        launchRequest.flags = i | launchRequest.flags;
        return this;
    }

    public SubSettingLauncher setUserHandle(UserHandle userHandle) {
        this.mLaunchRequest.userHandle = userHandle;
        return this;
    }

    public SubSettingLauncher setTransitionType(int i) {
        this.mLaunchRequest.transitionType = i;
        return this;
    }

    public void launch() {
        if (this.mLaunched) {
            throw new IllegalStateException("This launcher has already been executed. Do not reuse");
        }
        boolean z = true;
        this.mLaunched = true;
        Intent intent = toIntent();
        UserHandle userHandle = this.mLaunchRequest.userHandle;
        boolean z2 = (userHandle == null || userHandle.getIdentifier() == UserHandle.myUserId()) ? false : true;
        LaunchRequest launchRequest = this.mLaunchRequest;
        Fragment fragment = launchRequest.mResultListener;
        if (fragment == null) {
            z = false;
        }
        if (z2 && z) {
            launchForResultAsUser(intent, launchRequest.userHandle, fragment, launchRequest.mRequestCode);
        } else if (z2 && !z) {
            launchAsUser(intent, launchRequest.userHandle);
        } else if (!z2 && z) {
            launchForResult(fragment, intent, launchRequest.mRequestCode);
        } else {
            launch(intent);
        }
    }

    public Intent toIntent() {
        Intent intent = new Intent("android.intent.action.MAIN");
        copyExtras(intent);
        intent.setClass(this.mContext, SubSettings.class);
        if (TextUtils.isEmpty(this.mLaunchRequest.destinationName)) {
            throw new IllegalArgumentException("Destination fragment must be set");
        }
        intent.putExtra(":settings:show_fragment", this.mLaunchRequest.destinationName);
        int i = this.mLaunchRequest.sourceMetricsCategory;
        if (i < 0) {
            throw new IllegalArgumentException("Source metrics category must be set");
        }
        intent.putExtra(":settings:source_metrics", i);
        intent.putExtra(":settings:show_fragment_args", this.mLaunchRequest.arguments);
        intent.putExtra(":settings:show_fragment_title_res_package_name", this.mLaunchRequest.titleResPackageName);
        intent.putExtra(":settings:show_fragment_title_resid", this.mLaunchRequest.titleResId);
        intent.putExtra(":settings:show_fragment_title", this.mLaunchRequest.title);
        intent.addFlags(this.mLaunchRequest.flags);
        intent.putExtra("page_transition_type", this.mLaunchRequest.transitionType);
        return intent;
    }

    void launch(Intent intent) {
        this.mContext.startActivity(intent);
    }

    void launchAsUser(Intent intent, UserHandle userHandle) {
        this.mContext.startActivityAsUser(intent, userHandle);
    }

    void launchForResultAsUser(Intent intent, UserHandle userHandle, Fragment fragment, int i) {
        fragment.getActivity().startActivityForResultAsUser(intent, i, userHandle);
    }

    void launchForResult(Fragment fragment, Intent intent, int i) {
        fragment.startActivityForResult(intent, i);
    }

    private void copyExtras(Intent intent) {
        Bundle bundle = this.mLaunchRequest.extras;
        if (bundle != null) {
            intent.replaceExtras(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LaunchRequest {
        Bundle arguments;
        String destinationName;
        Bundle extras;
        int flags;
        int mRequestCode;
        Fragment mResultListener;
        int sourceMetricsCategory = -100;
        CharSequence title;
        int titleResId;
        String titleResPackageName;
        int transitionType;
        UserHandle userHandle;

        LaunchRequest() {
        }
    }
}
