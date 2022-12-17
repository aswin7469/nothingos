package com.android.settings.gestures;

import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.R$raw;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.settings.gestures.IllustrationPreference;
import com.nothing.settings.gestures.NavigationSettingsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ButtonNavigationSettingsFragment extends DashboardFragment implements NavigationSettingsController.OnChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.button_navigation_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return SystemNavigationPreferenceController.isOverlayPackageAvailable(context, "com.android.internal.systemui.navbar.twobutton") || SystemNavigationPreferenceController.isOverlayPackageAvailable(context, "com.android.internal.systemui.navbar.threebutton");
        }
    };
    private static final List<AbstractPreferenceController> sControllers = new ArrayList();
    private IllustrationPreference mVideoPreference;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ButtonNavigationSettingsFragment";
    }

    public int getMetricsCategory() {
        return 1908;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getCurrentSystemNavigationMode();
    }

    private String getCurrentSystemNavigationMode() {
        boolean z = false;
        if (Settings.Secure.getInt(getPrefContext().getContentResolver(), "settings_navi_bar_combination", 0) == 0) {
            z = true;
        }
        return z ? "nt_button_nav_left" : "nt_button_nav_right";
    }

    public void onCheckedChanged(Preference preference) {
        for (AbstractPreferenceController updateState : sControllers) {
            updateState.updateState(preference);
        }
        setIllustrationVideo(this.mVideoPreference, preference.getKey(), getPrefContext(), false);
    }

    public void onResume() {
        super.onResume();
        IllustrationPreference illustrationPreference = (IllustrationPreference) getPreferenceScreen().findPreference("gesture_power_menu_video");
        this.mVideoPreference = illustrationPreference;
        setIllustrationVideo(illustrationPreference, getDefaultKey(), getPrefContext(), true);
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((NavigationSettingsController) it.next()).setOnChangeListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((NavigationSettingsController) it.next()).setOnChangeListener((NavigationSettingsController.OnChangeListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        if (sControllers.size() == 0) {
            String[] stringArray = context.getResources().getStringArray(R$array.nt_button_nav_keys);
            for (String navigationSettingsController : stringArray) {
                sControllers.add(new NavigationSettingsController(context, lifecycle, navigationSettingsController));
            }
        }
        return sControllers;
    }

    private void setIllustrationVideo(IllustrationPreference illustrationPreference, String str, Context context, boolean z) {
        Configuration configuration = context.getResources().getConfiguration();
        boolean z2 = true;
        if (configuration.getLayoutDirection() != 1) {
            z2 = false;
        }
        if (!z2) {
            str.hashCode();
            if (!str.equals("nt_button_nav_left")) {
                if (str.equals("nt_button_nav_right")) {
                    if (z) {
                        if ((configuration.uiMode & 48) == 32) {
                            illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_right_dark);
                        } else {
                            illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_right_light);
                        }
                    } else if ((configuration.uiMode & 48) == 32) {
                        illustrationPreference.setLottieAnimationResId(R$raw.nt_lefttoright_drak);
                    } else {
                        illustrationPreference.setLottieAnimationResId(R$raw.nt_lefttoright_light);
                    }
                }
            } else if (z) {
                if ((configuration.uiMode & 48) == 32) {
                    illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_left_dark);
                } else {
                    illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_left_light);
                }
            } else if ((configuration.uiMode & 48) == 32) {
                illustrationPreference.setLottieAnimationResId(R$raw.nt_righttoleft_dard);
            } else {
                illustrationPreference.setLottieAnimationResId(R$raw.nt_righttoleft_light);
            }
        } else {
            str.hashCode();
            if (!str.equals("nt_button_nav_left")) {
                if (str.equals("nt_button_nav_right")) {
                    if (z) {
                        if ((configuration.uiMode & 48) == 32) {
                            illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_right_dark_rtl);
                        } else {
                            illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_right_light_rtl);
                        }
                    } else if ((configuration.uiMode & 48) == 32) {
                        illustrationPreference.setLottieAnimationResId(R$raw.nt_lefttoright_dark_rtl);
                    } else {
                        illustrationPreference.setLottieAnimationResId(R$raw.nt_lefttoright_light_rtl);
                    }
                }
            } else if (z) {
                if ((configuration.uiMode & 48) == 32) {
                    illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_left_dark_rtl);
                } else {
                    illustrationPreference.setLottieAnimationResId(R$raw.nt_transition_left_light_rtl);
                }
            } else if ((configuration.uiMode & 48) == 32) {
                illustrationPreference.setLottieAnimationResId(R$raw.nt_righttoleft_dark_rtl);
            } else {
                illustrationPreference.setLottieAnimationResId(R$raw.nt_righttoleft_light_rtl);
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.button_navigation_settings;
    }
}
