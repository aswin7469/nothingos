package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import androidx.core.view.ViewCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;

@Deprecated
public class NavBarTuner extends TunerPreferenceFragment {
    private static final String ICON = "icon";
    private static final int[][] ICONS = {new int[]{C1894R.C1896drawable.ic_qs_circle, C1894R.string.tuner_circle}, new int[]{C1894R.C1896drawable.ic_add, C1894R.string.tuner_plus}, new int[]{C1894R.C1896drawable.ic_remove, C1894R.string.tuner_minus}, new int[]{C1894R.C1896drawable.ic_left, C1894R.string.tuner_left}, new int[]{C1894R.C1896drawable.ic_right, C1894R.string.tuner_right}, new int[]{C1894R.C1896drawable.ic_menu, C1894R.string.tuner_menu}};
    private static final String KEYCODE = "keycode";
    private static final String LAYOUT = "layout";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TYPE = "type";
    private Handler mHandler;
    private final ArrayList<TunerService.Tunable> mTunables = new ArrayList<>();

    public void onCreate(Bundle bundle) {
        this.mHandler = new Handler();
        super.onCreate(bundle);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(C1894R.C1902xml.nav_bar_tuner);
        bindLayout((ListPreference) findPreference(LAYOUT));
        bindButton(NavigationBarInflaterView.NAV_BAR_LEFT, NavigationBarInflaterView.NAVSPACE, "left");
        bindButton(NavigationBarInflaterView.NAV_BAR_RIGHT, NavigationBarInflaterView.MENU_IME_ROTATE, "right");
    }

    public void onDestroy() {
        super.onDestroy();
        this.mTunables.forEach(new NavBarTuner$$ExternalSyntheticLambda3());
    }

    private void addTunable(TunerService.Tunable tunable, String... strArr) {
        this.mTunables.add(tunable);
        ((TunerService) Dependency.get(TunerService.class)).addTunable(tunable, strArr);
    }

    private void bindLayout(ListPreference listPreference) {
        addTunable(new NavBarTuner$$ExternalSyntheticLambda1(this, listPreference), NavigationBarInflaterView.NAV_BAR_VIEWS);
        listPreference.setOnPreferenceChangeListener(new NavBarTuner$$ExternalSyntheticLambda2());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindLayout$2$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ void m3264lambda$bindLayout$2$comandroidsystemuitunerNavBarTuner(ListPreference listPreference, String str, String str2) {
        this.mHandler.post(new NavBarTuner$$ExternalSyntheticLambda0(str2, listPreference));
    }

    static /* synthetic */ void lambda$bindLayout$1(String str, ListPreference listPreference) {
        if (str == null) {
            str = "default";
        }
        listPreference.setValue(str);
    }

    static /* synthetic */ boolean lambda$bindLayout$3(Preference preference, Object obj) {
        String str = (String) obj;
        if ("default".equals(str)) {
            str = null;
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(NavigationBarInflaterView.NAV_BAR_VIEWS, str);
        return true;
    }

    private void bindButton(String str, String str2, String str3) {
        ListPreference listPreference = (ListPreference) findPreference("type_" + str3);
        Preference findPreference = findPreference("keycode_" + str3);
        ListPreference listPreference2 = (ListPreference) findPreference("icon_" + str3);
        setupIcons(listPreference2);
        ListPreference listPreference3 = listPreference;
        addTunable(new NavBarTuner$$ExternalSyntheticLambda7(this, str2, listPreference3, listPreference2, findPreference), str);
        ListPreference listPreference4 = listPreference2;
        NavBarTuner$$ExternalSyntheticLambda8 navBarTuner$$ExternalSyntheticLambda8 = new NavBarTuner$$ExternalSyntheticLambda8(this, str, listPreference3, findPreference, listPreference4);
        listPreference.setOnPreferenceChangeListener(navBarTuner$$ExternalSyntheticLambda8);
        listPreference2.setOnPreferenceChangeListener(navBarTuner$$ExternalSyntheticLambda8);
        findPreference.setOnPreferenceClickListener(new NavBarTuner$$ExternalSyntheticLambda9(this, findPreference, str, listPreference, listPreference4));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$5$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ void m3259lambda$bindButton$5$comandroidsystemuitunerNavBarTuner(String str, ListPreference listPreference, ListPreference listPreference2, Preference preference, String str2, String str3) {
        this.mHandler.post(new NavBarTuner$$ExternalSyntheticLambda6(this, str3, str, listPreference, listPreference2, preference));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$4$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ void m3258lambda$bindButton$4$comandroidsystemuitunerNavBarTuner(String str, String str2, ListPreference listPreference, ListPreference listPreference2, Preference preference) {
        if (str == null) {
            str = str2;
        }
        String extractButton = NavigationBarInflaterView.extractButton(str);
        if (extractButton.startsWith("key")) {
            listPreference.setValue("key");
            String extractImage = NavigationBarInflaterView.extractImage(extractButton);
            int extractKeycode = NavigationBarInflaterView.extractKeycode(extractButton);
            listPreference2.setValue(extractImage);
            updateSummary(listPreference2);
            preference.setSummary((CharSequence) extractKeycode + "");
            preference.setVisible(true);
            listPreference2.setVisible(true);
            return;
        }
        listPreference.setValue(extractButton);
        preference.setVisible(false);
        listPreference2.setVisible(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$7$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ boolean m3261lambda$bindButton$7$comandroidsystemuitunerNavBarTuner(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2, Preference preference2, Object obj) {
        this.mHandler.post(new NavBarTuner$$ExternalSyntheticLambda4(this, str, listPreference, preference, listPreference2));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$6$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ void m3260lambda$bindButton$6$comandroidsystemuitunerNavBarTuner(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2) {
        setValue(str, listPreference, preference, listPreference2);
        updateSummary(listPreference2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$9$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ boolean m3263lambda$bindButton$9$comandroidsystemuitunerNavBarTuner(Preference preference, String str, ListPreference listPreference, ListPreference listPreference2, Preference preference2) {
        EditText editText = new EditText(getContext());
        new AlertDialog.Builder(getContext()).setTitle(preference2.getTitle()).setView(editText).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, new NavBarTuner$$ExternalSyntheticLambda5(this, editText, preference, str, listPreference, listPreference2)).show();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButton$8$com-android-systemui-tuner-NavBarTuner  reason: not valid java name */
    public /* synthetic */ void m3262lambda$bindButton$8$comandroidsystemuitunerNavBarTuner(EditText editText, Preference preference, String str, ListPreference listPreference, ListPreference listPreference2, DialogInterface dialogInterface, int i) {
        int i2;
        try {
            i2 = Integer.parseInt(editText.getText().toString());
        } catch (Exception unused) {
            i2 = 66;
        }
        preference.setSummary((CharSequence) i2 + "");
        setValue(str, listPreference, preference, listPreference2);
    }

    private void updateSummary(ListPreference listPreference) {
        try {
            int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
            String str = listPreference.getValue().split("/")[0];
            int parseInt = Integer.parseInt(listPreference.getValue().split("/")[1]);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            Drawable loadDrawable = Icon.createWithResource(str, parseInt).loadDrawable(getContext());
            loadDrawable.setTint(ViewCompat.MEASURED_STATE_MASK);
            loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
            spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
            spannableStringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            int i = 0;
            while (true) {
                int[][] iArr = ICONS;
                if (i < iArr.length) {
                    int[] iArr2 = iArr[i];
                    if (iArr2[0] == parseInt) {
                        spannableStringBuilder.append(getString(iArr2[1]));
                    }
                    i++;
                } else {
                    listPreference.setSummary(spannableStringBuilder);
                    return;
                }
            }
        } catch (Exception e) {
            Log.d("NavButton", "Problem with summary", e);
            listPreference.setSummary((CharSequence) null);
        }
    }

    private void setValue(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2) {
        int i;
        String value = listPreference.getValue();
        if ("key".equals(value)) {
            String value2 = listPreference2.getValue();
            try {
                i = Integer.parseInt(preference.getSummary().toString());
            } catch (Exception unused) {
                i = 66;
            }
            value = value + NavigationBarInflaterView.KEY_CODE_START + i + ":" + value2 + NavigationBarInflaterView.KEY_CODE_END;
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(str, value);
    }

    private void setupIcons(ListPreference listPreference) {
        int[][] iArr = ICONS;
        CharSequence[] charSequenceArr = new CharSequence[iArr.length];
        CharSequence[] charSequenceArr2 = new CharSequence[iArr.length];
        int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
        int i = 0;
        while (true) {
            int[][] iArr2 = ICONS;
            if (i < iArr2.length) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                Drawable loadDrawable = Icon.createWithResource(getContext().getPackageName(), iArr2[i][0]).loadDrawable(getContext());
                loadDrawable.setTint(ViewCompat.MEASURED_STATE_MASK);
                loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
                spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
                spannableStringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                spannableStringBuilder.append(getString(iArr2[i][1]));
                charSequenceArr[i] = spannableStringBuilder;
                charSequenceArr2[i] = getContext().getPackageName() + "/" + iArr2[i][0];
                i++;
            } else {
                listPreference.setEntries(charSequenceArr);
                listPreference.setEntryValues(charSequenceArr2);
                return;
            }
        }
    }
}
