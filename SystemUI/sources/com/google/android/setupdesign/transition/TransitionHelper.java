package com.google.android.setupdesign.transition;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Window;
import com.google.android.material.transition.platform.MaterialSharedAxis;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.ThemeHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TransitionHelper {
    public static final int CONFIG_TRANSITION_NONE = 0;
    public static final int CONFIG_TRANSITION_SHARED_X_AXIS = 1;
    public static final String EXTRA_ACTIVITY_OPTIONS = "sud:activity_options";
    private static final String TAG = "TransitionHelper";
    public static final int TRANSITION_CAPTIVE = 5;
    public static final int TRANSITION_FADE = 3;
    public static final int TRANSITION_FRAMEWORK_DEFAULT = 1;
    public static final int TRANSITION_FRAMEWORK_DEFAULT_PRE_P = 4;
    public static final int TRANSITION_NONE = -1;
    public static final int TRANSITION_NO_OVERRIDE = 0;
    public static final int TRANSITION_SLIDE = 2;
    static boolean isFinishCalled = false;
    static boolean isStartActivity = false;
    static boolean isStartActivityForResult = false;

    @Retention(RetentionPolicy.SOURCE)
    public @interface TransitionType {
    }

    private TransitionHelper() {
    }

    public static void applyForwardTransition(Activity activity) {
        applyForwardTransition(activity, 5);
    }

    public static void applyForwardTransition(Fragment fragment) {
        if (getConfigTransitionType(fragment.getContext()) == 1) {
            fragment.setExitTransition(new MaterialSharedAxis(0, true));
            fragment.setEnterTransition(new MaterialSharedAxis(0, true));
            return;
        }
        Log.w(TAG, "Not apply the forward transition for platform fragment.");
    }

    public static void applyForwardTransition(Activity activity, int i) {
        if (i == 2) {
            activity.overridePendingTransition(C3953R.anim.sud_slide_next_in, C3953R.anim.sud_slide_next_out);
        } else if (i == 3) {
            activity.overridePendingTransition(17432576, C3953R.anim.sud_stay);
        } else if (i == 1) {
            TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(16973825, new int[]{16842936, 16842937});
            activity.overridePendingTransition(obtainStyledAttributes.getResourceId(0, 0), obtainStyledAttributes.getResourceId(1, 0));
            obtainStyledAttributes.recycle();
        } else if (i == 4) {
            activity.overridePendingTransition(C3953R.anim.sud_pre_p_activity_open_enter, C3953R.anim.sud_pre_p_activity_open_exit);
        } else if (i == -1) {
            activity.overridePendingTransition(0, 0);
        } else if (i == 5 && getConfigTransitionType(activity) == 1) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setExitTransition(new MaterialSharedAxis(0, true));
                window.setAllowEnterTransitionOverlap(true);
                window.setEnterTransition(new MaterialSharedAxis(0, true));
                return;
            }
            Log.w(TAG, "applyForwardTransition: Invalid window=" + window);
        }
    }

    public static void applyBackwardTransition(Activity activity) {
        applyBackwardTransition(activity, 5);
    }

    public static void applyBackwardTransition(Fragment fragment) {
        if (getConfigTransitionType(fragment.getContext()) == 1) {
            fragment.setReturnTransition(new MaterialSharedAxis(0, false));
            fragment.setReenterTransition(new MaterialSharedAxis(0, false));
            return;
        }
        Log.w(TAG, "Not apply the backward transition for platform fragment.");
    }

    public static void applyBackwardTransition(Activity activity, int i) {
        if (i == 2) {
            activity.overridePendingTransition(C3953R.anim.sud_slide_back_in, C3953R.anim.sud_slide_back_out);
        } else if (i == 3) {
            activity.overridePendingTransition(C3953R.anim.sud_stay, 17432577);
        } else if (i == 1) {
            TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(16973825, new int[]{16842938, 16842939});
            activity.overridePendingTransition(obtainStyledAttributes.getResourceId(0, 0), obtainStyledAttributes.getResourceId(1, 0));
            obtainStyledAttributes.recycle();
        } else if (i == 4) {
            activity.overridePendingTransition(C3953R.anim.sud_pre_p_activity_close_enter, C3953R.anim.sud_pre_p_activity_close_exit);
        } else if (i == -1) {
            activity.overridePendingTransition(0, 0);
        } else if (i == 5 && getConfigTransitionType(activity) == 1) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setReenterTransition(new MaterialSharedAxis(0, false));
                window.setReturnTransition(new MaterialSharedAxis(0, false));
                return;
            }
            Log.w(TAG, "applyBackwardTransition: Invalid window=" + window);
        }
    }

    public static void startActivityWithTransition(Activity activity, Intent intent) {
        startActivityWithTransition(activity, intent, (Bundle) null);
    }

    public static void startActivityWithTransition(Activity activity, Intent intent, Bundle bundle) {
        if (activity == null) {
            throw new IllegalArgumentException("Invalid activity=" + activity);
        } else if (intent != null) {
            if ((intent.getFlags() & 268435456) == 268435456) {
                Log.e(TAG, "The transition won't take effect since the WindowManager does not allow override new task transitions");
            }
            if (!isStartActivity) {
                isStartActivity = true;
                if (getConfigTransitionType(activity) == 1) {
                    if (activity.getWindow() != null && !activity.getWindow().hasFeature(13)) {
                        Log.w(TAG, "The transition won't take effect due to NO FEATURE_ACTIVITY_TRANSITIONS feature");
                    }
                    if (bundle == null) {
                        bundle = makeActivityOptions(activity, intent);
                    }
                    intent.putExtra(EXTRA_ACTIVITY_OPTIONS, bundle);
                    activity.startActivity(intent, bundle);
                } else {
                    startActivityWithTransitionInternal(activity, intent, bundle);
                }
            }
            isStartActivity = false;
        } else {
            throw new IllegalArgumentException("Invalid intent=" + intent);
        }
    }

    private static void startActivityWithTransitionInternal(Activity activity, Intent intent, Bundle bundle) {
        try {
            if (getConfigTransitionType(activity) != 1 || bundle == null) {
                activity.startActivity(intent);
            } else {
                activity.startActivity(intent, bundle);
            }
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "Activity not found when startActivity with transition.");
            isStartActivity = false;
            throw e;
        }
    }

    public static void startActivityForResultWithTransition(Activity activity, Intent intent, int i) {
        startActivityForResultWithTransition(activity, intent, i, (Bundle) null);
    }

    public static void startActivityForResultWithTransition(Activity activity, Intent intent, int i, Bundle bundle) {
        if (activity == null) {
            throw new IllegalArgumentException("Invalid activity=" + activity);
        } else if (intent != null) {
            if ((intent.getFlags() & 268435456) == 268435456) {
                Log.e(TAG, "The transition won't take effect since the WindowManager does not allow override new task transitions");
            }
            if (!isStartActivityForResult) {
                isStartActivityForResult = true;
                if (getConfigTransitionType(activity) == 1) {
                    if (activity.getWindow() != null && !activity.getWindow().hasFeature(13)) {
                        Log.w(TAG, "The transition won't take effect due to NO FEATURE_ACTIVITY_TRANSITIONS feature");
                    }
                    if (bundle == null) {
                        bundle = makeActivityOptions(activity, intent);
                    }
                    intent.putExtra(EXTRA_ACTIVITY_OPTIONS, bundle);
                    activity.startActivityForResult(intent, i, bundle);
                } else {
                    startActivityForResultWithTransitionInternal(activity, intent, i, bundle);
                }
                isStartActivityForResult = false;
            }
        } else {
            throw new IllegalArgumentException("Invalid intent=" + intent);
        }
    }

    private static void startActivityForResultWithTransitionInternal(Activity activity, Intent intent, int i, Bundle bundle) {
        try {
            if (getConfigTransitionType(activity) != 1 || bundle == null) {
                activity.startActivityForResult(intent, i);
            } else {
                activity.startActivityForResult(intent, i, bundle);
            }
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "Activity not found when startActivityForResult with transition.");
            isStartActivityForResult = false;
            throw e;
        }
    }

    public static void finishActivity(Activity activity) {
        if (activity != null) {
            if (!isFinishCalled) {
                isFinishCalled = true;
                if (getConfigTransitionType(activity) == 1) {
                    activity.finishAfterTransition();
                } else {
                    Log.w(TAG, "Fallback to using Activity#finish() due to the Activity#finishAfterTransition() is supported from Android Sdk 21");
                    activity.finish();
                }
            }
            isFinishCalled = false;
            return;
        }
        throw new IllegalArgumentException("Invalid activity=" + activity);
    }

    public static int getConfigTransitionType(Context context) {
        if (!BuildCompatUtils.isAtLeastS() || !ThemeHelper.shouldApplyExtendedPartnerConfig(context)) {
            return 0;
        }
        return PartnerConfigHelper.get(context).getInteger(context, PartnerConfig.CONFIG_TRANSITION_TYPE, 0);
    }

    public static Bundle makeActivityOptions(Activity activity, Intent intent) {
        return makeActivityOptions(activity, intent, false);
    }

    public static Bundle makeActivityOptions(Activity activity, Intent intent, boolean z) {
        Bundle bundle;
        if (!(activity == null || intent == null)) {
            if ((intent.getFlags() & 268435456) == 268435456) {
                Log.e(TAG, "The transition won't take effect since the WindowManager does not allow override new task transitions");
            }
            if (getConfigTransitionType(activity) == 1) {
                if (activity.getWindow() != null && !activity.getWindow().hasFeature(13)) {
                    Log.w(TAG, "The transition won't take effect due to NO FEATURE_ACTIVITY_TRANSITIONS feature");
                }
                if (!z || activity.getIntent() == null) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(activity, new Pair[0]).toBundle();
                } else {
                    bundle = activity.getIntent().getBundleExtra(EXTRA_ACTIVITY_OPTIONS);
                }
                intent.putExtra(EXTRA_ACTIVITY_OPTIONS, bundle);
                return bundle;
            }
        }
        return null;
    }
}
