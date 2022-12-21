package com.google.android.setupdesign.transition.support;

import android.app.Activity;
import android.util.Log;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.google.android.material.transition.platform.MaterialSharedAxis;

public class TransitionHelper {
    private static final String TAG = "TransitionHelper";

    private TransitionHelper() {
    }

    public static void applyForwardTransition(Fragment fragment) {
        if (1 == com.google.android.setupdesign.transition.TransitionHelper.getConfigTransitionType(fragment.getContext())) {
            fragment.setExitTransition(new MaterialSharedAxis(0, true));
            fragment.setEnterTransition(new MaterialSharedAxis(0, true));
            return;
        }
        Log.w(TAG, "Not apply the forward transition for support lib's fragment.");
    }

    public static void applyBackwardTransition(Fragment fragment) {
        if (1 == com.google.android.setupdesign.transition.TransitionHelper.getConfigTransitionType(fragment.getContext())) {
            fragment.setReturnTransition(new MaterialSharedAxis(0, false));
            fragment.setReenterTransition(new MaterialSharedAxis(0, false));
            return;
        }
        Log.w(TAG, "Not apply the backward transition for support lib's fragment.");
    }

    public static ActivityOptionsCompat makeActivityOptionsCompat(Activity activity) {
        if (activity == null || com.google.android.setupdesign.transition.TransitionHelper.getConfigTransitionType(activity) != 1) {
            return null;
        }
        if (activity.getWindow() != null && !activity.getWindow().hasFeature(13)) {
            Log.w(TAG, "The transition won't take effect due to NO FEATURE_ACTIVITY_TRANSITIONS feature");
        }
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, new Pair[0]);
    }
}
