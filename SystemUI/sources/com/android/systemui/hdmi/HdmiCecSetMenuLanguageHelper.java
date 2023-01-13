package com.android.systemui.hdmi;

import com.android.internal.app.LocalePicker;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.util.settings.SecureSettings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class HdmiCecSetMenuLanguageHelper {
    private static final String SEPARATOR = ",";
    private static final String TAG = "HdmiCecSetMenuLanguageHelper";
    private final Executor mBackgroundExecutor;
    private HashSet<String> mDenylist;
    private Locale mLocale;
    private final SecureSettings mSecureSettings;

    @Inject
    public HdmiCecSetMenuLanguageHelper(@Background Executor executor, SecureSettings secureSettings) {
        Collection collection;
        this.mBackgroundExecutor = executor;
        this.mSecureSettings = secureSettings;
        String stringForUser = secureSettings.getStringForUser("hdmi_cec_set_menu_language_denylist", -2);
        if (stringForUser == null) {
            collection = Collections.EMPTY_SET;
        } else {
            collection = Arrays.asList(stringForUser.split(","));
        }
        this.mDenylist = new HashSet<>(collection);
    }

    public void setLocale(String str) {
        this.mLocale = Locale.forLanguageTag(str);
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public boolean isLocaleDenylisted() {
        return this.mDenylist.contains(this.mLocale.toLanguageTag());
    }

    public void acceptLocale() {
        this.mBackgroundExecutor.execute(new HdmiCecSetMenuLanguageHelper$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$acceptLocale$0$com-android-systemui-hdmi-HdmiCecSetMenuLanguageHelper */
    public /* synthetic */ void mo33092x62d654ec() {
        LocalePicker.updateLocale(this.mLocale);
    }

    public void declineLocale() {
        this.mDenylist.add(this.mLocale.toLanguageTag());
        this.mSecureSettings.putStringForUser("hdmi_cec_set_menu_language_denylist", String.join((CharSequence) ",", (Iterable<? extends CharSequence>) this.mDenylist), -2);
    }
}
