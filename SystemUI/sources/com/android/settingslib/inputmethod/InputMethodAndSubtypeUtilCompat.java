package com.android.settingslib.inputmethod;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.text.ListFormatter;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import com.android.internal.app.LocaleHelper;
import com.android.settingslib.PrimarySwitchPreference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InputMethodAndSubtypeUtilCompat {
    private static final boolean DEBUG = false;
    private static final char INPUT_METHOD_SEPARATER = ':';
    private static final char INPUT_METHOD_SUBTYPE_SEPARATER = ';';
    private static final int NOT_A_SUBTYPE_ID = -1;
    private static final String SUBTYPE_MODE_KEYBOARD = "keyboard";
    private static final String TAG = "InputMethdAndSubtypeUtlCompat";
    private static final TextUtils.SimpleStringSplitter sStringInputMethodSplitter = new TextUtils.SimpleStringSplitter(':');
    private static final TextUtils.SimpleStringSplitter sStringInputMethodSubtypeSplitter = new TextUtils.SimpleStringSplitter(INPUT_METHOD_SUBTYPE_SEPARATER);

    public static String buildInputMethodsAndSubtypesString(HashMap<String, HashSet<String>> hashMap) {
        StringBuilder sb = new StringBuilder();
        for (String next : hashMap.keySet()) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(next);
            Iterator it = hashMap.get(next).iterator();
            while (it.hasNext()) {
                sb.append((char) INPUT_METHOD_SUBTYPE_SEPARATER).append((String) it.next());
            }
        }
        return sb.toString();
    }

    private static String buildInputMethodsString(HashSet<String> hashSet) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = hashSet.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(next);
        }
        return sb.toString();
    }

    private static int getInputMethodSubtypeSelected(ContentResolver contentResolver) {
        try {
            return Settings.Secure.getInt(contentResolver, "selected_input_method_subtype");
        } catch (Settings.SettingNotFoundException unused) {
            return -1;
        }
    }

    private static boolean isInputMethodSubtypeSelected(ContentResolver contentResolver) {
        return getInputMethodSubtypeSelected(contentResolver) != -1;
    }

    private static void putSelectedInputMethodSubtype(ContentResolver contentResolver, int i) {
        Settings.Secure.putInt(contentResolver, "selected_input_method_subtype", i);
    }

    static HashMap<String, HashSet<String>> getEnabledInputMethodsAndSubtypeList(ContentResolver contentResolver) {
        return parseInputMethodsAndSubtypesString(Settings.Secure.getString(contentResolver, "enabled_input_methods"));
    }

    public static HashMap<String, HashSet<String>> parseInputMethodsAndSubtypesString(String str) {
        HashMap<String, HashSet<String>> hashMap = new HashMap<>();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        sStringInputMethodSplitter.setString(str);
        while (true) {
            TextUtils.SimpleStringSplitter simpleStringSplitter = sStringInputMethodSplitter;
            if (!simpleStringSplitter.hasNext()) {
                return hashMap;
            }
            String next = simpleStringSplitter.next();
            TextUtils.SimpleStringSplitter simpleStringSplitter2 = sStringInputMethodSubtypeSplitter;
            simpleStringSplitter2.setString(next);
            if (simpleStringSplitter2.hasNext()) {
                HashSet hashSet = new HashSet();
                String next2 = simpleStringSplitter2.next();
                while (true) {
                    TextUtils.SimpleStringSplitter simpleStringSplitter3 = sStringInputMethodSubtypeSplitter;
                    if (!simpleStringSplitter3.hasNext()) {
                        break;
                    }
                    hashSet.add(simpleStringSplitter3.next());
                }
                hashMap.put(next2, hashSet);
            }
        }
    }

    private static HashSet<String> getDisabledSystemIMEs(ContentResolver contentResolver) {
        HashSet<String> hashSet = new HashSet<>();
        String string = Settings.Secure.getString(contentResolver, "disabled_system_input_methods");
        if (TextUtils.isEmpty(string)) {
            return hashSet;
        }
        sStringInputMethodSplitter.setString(string);
        while (true) {
            TextUtils.SimpleStringSplitter simpleStringSplitter = sStringInputMethodSplitter;
            if (!simpleStringSplitter.hasNext()) {
                return hashSet;
            }
            hashSet.add(simpleStringSplitter.next());
        }
    }

    public static void saveInputMethodSubtypeList(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, boolean z) {
        saveInputMethodSubtypeListForUserInternal(preferenceFragmentCompat, contentResolver, list, z, UserHandle.myUserId());
    }

    public static void saveInputMethodSubtypeListForUser(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, boolean z, int i) {
        saveInputMethodSubtypeListForUserInternal(preferenceFragmentCompat, contentResolver, list, z, i);
    }

    private static void saveInputMethodSubtypeListForUserInternal(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, boolean z, int i) {
        boolean z2;
        Iterator<InputMethodInfo> it;
        Context context;
        int i2;
        int i3;
        boolean z3;
        PreferenceFragmentCompat preferenceFragmentCompat2 = preferenceFragmentCompat;
        ContentResolver contentResolver2 = contentResolver;
        String string = Settings.Secure.getString(contentResolver2, "default_input_method");
        int inputMethodSubtypeSelected = getInputMethodSubtypeSelected(contentResolver);
        HashMap<String, HashSet<String>> enabledInputMethodsAndSubtypeList = getEnabledInputMethodsAndSubtypeList(contentResolver);
        HashSet<String> disabledSystemIMEs = getDisabledSystemIMEs(contentResolver);
        Iterator<InputMethodInfo> it2 = list.iterator();
        boolean z4 = false;
        while (it2.hasNext()) {
            InputMethodInfo next = it2.next();
            String id = next.getId();
            Preference findPreference = preferenceFragmentCompat2.findPreference(id);
            if (findPreference != null) {
                if (findPreference instanceof TwoStatePreference) {
                    z2 = ((TwoStatePreference) findPreference).isChecked();
                } else if (findPreference instanceof PrimarySwitchPreference) {
                    z2 = ((PrimarySwitchPreference) findPreference).isChecked();
                } else {
                    z2 = enabledInputMethodsAndSubtypeList.containsKey(id);
                }
                boolean equals = id.equals(string);
                boolean isSystem = next.isSystem();
                if (i == UserHandle.myUserId()) {
                    context = preferenceFragmentCompat.getActivity();
                    it = it2;
                    i2 = 0;
                } else {
                    it = it2;
                    i2 = 0;
                    context = preferenceFragmentCompat.getActivity().createContextAsUser(UserHandle.of(i), 0);
                }
                if ((z || !InputMethodSettingValuesWrapper.getInstance(context).isAlwaysCheckedIme(next)) && !z2) {
                    enabledInputMethodsAndSubtypeList.remove(id);
                    if (equals) {
                        string = null;
                    }
                } else {
                    if (!enabledInputMethodsAndSubtypeList.containsKey(id)) {
                        enabledInputMethodsAndSubtypeList.put(id, new HashSet());
                    }
                    HashSet hashSet = enabledInputMethodsAndSubtypeList.get(id);
                    int subtypeCount = next.getSubtypeCount();
                    int i4 = i2;
                    while (i2 < subtypeCount) {
                        InputMethodSubtype subtypeAt = next.getSubtypeAt(i2);
                        boolean z5 = z4;
                        String valueOf = String.valueOf(subtypeAt.hashCode());
                        InputMethodInfo inputMethodInfo = next;
                        TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceFragmentCompat2.findPreference(id + valueOf);
                        if (twoStatePreference == null) {
                            z4 = z5;
                        } else {
                            if (i4 == 0) {
                                hashSet.clear();
                                z3 = true;
                                i3 = 1;
                            } else {
                                boolean z6 = z5;
                                i3 = i4;
                                z3 = z6;
                            }
                            if (!twoStatePreference.isEnabled() || !twoStatePreference.isChecked()) {
                                hashSet.remove(valueOf);
                            } else {
                                hashSet.add(valueOf);
                                if (equals && inputMethodSubtypeSelected == subtypeAt.hashCode()) {
                                    i4 = i3;
                                    z4 = false;
                                }
                            }
                            z4 = z3;
                            i4 = i3;
                        }
                        i2++;
                        next = inputMethodInfo;
                    }
                    boolean z7 = z4;
                }
                if (isSystem && z) {
                    if (disabledSystemIMEs.contains(id)) {
                        if (z2) {
                            disabledSystemIMEs.remove(id);
                        }
                    } else if (!z2) {
                        disabledSystemIMEs.add(id);
                    }
                }
                it2 = it;
            }
        }
        String buildInputMethodsAndSubtypesString = buildInputMethodsAndSubtypesString(enabledInputMethodsAndSubtypeList);
        String buildInputMethodsString = buildInputMethodsString(disabledSystemIMEs);
        if (z4 || !isInputMethodSubtypeSelected(contentResolver)) {
            putSelectedInputMethodSubtype(contentResolver2, -1);
        }
        Settings.Secure.putString(contentResolver2, "enabled_input_methods", buildInputMethodsAndSubtypesString);
        if (buildInputMethodsString.length() > 0) {
            Settings.Secure.putString(contentResolver2, "disabled_system_input_methods", buildInputMethodsString);
        }
        if (string == null) {
            string = "";
        }
        Settings.Secure.putString(contentResolver2, "default_input_method", string);
    }

    public static void loadInputMethodSubtypeList(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, Map<String, List<Preference>> map) {
        HashMap<String, HashSet<String>> enabledInputMethodsAndSubtypeList = getEnabledInputMethodsAndSubtypeList(contentResolver);
        for (InputMethodInfo id : list) {
            String id2 = id.getId();
            Preference findPreference = preferenceFragmentCompat.findPreference(id2);
            if (findPreference instanceof TwoStatePreference) {
                boolean containsKey = enabledInputMethodsAndSubtypeList.containsKey(id2);
                ((TwoStatePreference) findPreference).setChecked(containsKey);
                if (map != null) {
                    for (Preference enabled : map.get(id2)) {
                        enabled.setEnabled(containsKey);
                    }
                }
                setSubtypesPreferenceEnabled(preferenceFragmentCompat, list, id2, containsKey);
            }
        }
        updateSubtypesPreferenceChecked(preferenceFragmentCompat, list, enabledInputMethodsAndSubtypeList);
    }

    private static void setSubtypesPreferenceEnabled(PreferenceFragmentCompat preferenceFragmentCompat, List<InputMethodInfo> list, String str, boolean z) {
        PreferenceScreen preferenceScreen = preferenceFragmentCompat.getPreferenceScreen();
        for (InputMethodInfo next : list) {
            if (str.equals(next.getId())) {
                int subtypeCount = next.getSubtypeCount();
                for (int i = 0; i < subtypeCount; i++) {
                    TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceScreen.findPreference(str + next.getSubtypeAt(i).hashCode());
                    if (twoStatePreference != null) {
                        twoStatePreference.setEnabled(z);
                    }
                }
            }
        }
    }

    private static void updateSubtypesPreferenceChecked(PreferenceFragmentCompat preferenceFragmentCompat, List<InputMethodInfo> list, HashMap<String, HashSet<String>> hashMap) {
        PreferenceScreen preferenceScreen = preferenceFragmentCompat.getPreferenceScreen();
        for (InputMethodInfo next : list) {
            String id = next.getId();
            if (hashMap.containsKey(id)) {
                HashSet hashSet = hashMap.get(id);
                int subtypeCount = next.getSubtypeCount();
                for (int i = 0; i < subtypeCount; i++) {
                    String valueOf = String.valueOf(next.getSubtypeAt(i).hashCode());
                    TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceScreen.findPreference(id + valueOf);
                    if (twoStatePreference != null) {
                        twoStatePreference.setChecked(hashSet.contains(valueOf));
                    }
                }
            }
        }
    }

    public static void removeUnnecessaryNonPersistentPreference(Preference preference) {
        SharedPreferences sharedPreferences;
        String key = preference.getKey();
        if (!preference.isPersistent() && key != null && (sharedPreferences = preference.getSharedPreferences()) != null && sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply();
        }
    }

    public static String getSubtypeLocaleNameAsSentence(InputMethodSubtype inputMethodSubtype, Context context, InputMethodInfo inputMethodInfo) {
        if (inputMethodSubtype == null) {
            return "";
        }
        return LocaleHelper.toSentenceCase(inputMethodSubtype.getDisplayName(context, inputMethodInfo.getPackageName(), inputMethodInfo.getServiceInfo().applicationInfo).toString(), getDisplayLocale(context));
    }

    public static String getSubtypeLocaleNameListAsSentence(List<InputMethodSubtype> list, Context context, InputMethodInfo inputMethodInfo) {
        if (list.isEmpty()) {
            return "";
        }
        Locale displayLocale = getDisplayLocale(context);
        int size = list.size();
        CharSequence[] charSequenceArr = new CharSequence[size];
        for (int i = 0; i < size; i++) {
            charSequenceArr[i] = list.get(i).getDisplayName(context, inputMethodInfo.getPackageName(), inputMethodInfo.getServiceInfo().applicationInfo);
        }
        return LocaleHelper.toSentenceCase(ListFormatter.getInstance(displayLocale).format((Object[]) charSequenceArr), displayLocale);
    }

    private static Locale getDisplayLocale(Context context) {
        if (context == null) {
            return Locale.getDefault();
        }
        if (context.getResources() == null) {
            return Locale.getDefault();
        }
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null) {
            return Locale.getDefault();
        }
        Locale locale = configuration.getLocales().get(0);
        return locale == null ? Locale.getDefault() : locale;
    }

    public static boolean isValidSystemNonAuxAsciiCapableIme(InputMethodInfo inputMethodInfo) {
        if (!inputMethodInfo.isAuxiliaryIme() && inputMethodInfo.isSystem()) {
            int subtypeCount = inputMethodInfo.getSubtypeCount();
            for (int i = 0; i < subtypeCount; i++) {
                InputMethodSubtype subtypeAt = inputMethodInfo.getSubtypeAt(i);
                if (SUBTYPE_MODE_KEYBOARD.equalsIgnoreCase(subtypeAt.getMode()) && subtypeAt.isAsciiCapable()) {
                    return true;
                }
            }
        }
        return false;
    }
}
