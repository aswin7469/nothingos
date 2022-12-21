package com.google.android.setupcompat.logging.internal;

import android.os.PersistableBundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FooterBarMixinMetrics {
    public static final String EXTRA_PRIMARY_BUTTON_VISIBILITY = "PrimaryButtonVisibility";
    public static final String EXTRA_SECONDARY_BUTTON_VISIBILITY = "SecondaryButtonVisibility";
    String primaryButtonVisibility = FooterButtonVisibility.UNKNOWN;
    String secondaryButtonVisibility = FooterButtonVisibility.UNKNOWN;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FooterButtonVisibility {
        public static final String INVISIBLE = "Invisible";
        public static final String INVISIBLE_TO_VISIBLE = "Invisible_to_Visible";
        public static final String UNKNOWN = "Unknown";
        public static final String VISIBLE = "Visible";
        public static final String VISIBLE_TO_INVISIBLE = "Visible_to_Invisible";
        public static final String VISIBLE_USING_XML = "VisibleUsingXml";
        public static final String VISIBLE_USING_XML_TO_INVISIBLE = "VisibleUsingXml_to_Invisible";
    }

    public String getInitialStateVisibility(boolean z, boolean z2) {
        return z ? z2 ? FooterButtonVisibility.VISIBLE_USING_XML : FooterButtonVisibility.VISIBLE : FooterButtonVisibility.INVISIBLE;
    }

    public void logPrimaryButtonInitialStateVisibility(boolean z, boolean z2) {
        String str;
        if (this.primaryButtonVisibility.equals(FooterButtonVisibility.UNKNOWN)) {
            str = getInitialStateVisibility(z, z2);
        } else {
            str = this.primaryButtonVisibility;
        }
        this.primaryButtonVisibility = str;
    }

    public void logSecondaryButtonInitialStateVisibility(boolean z, boolean z2) {
        String str;
        if (this.secondaryButtonVisibility.equals(FooterButtonVisibility.UNKNOWN)) {
            str = getInitialStateVisibility(z, z2);
        } else {
            str = this.secondaryButtonVisibility;
        }
        this.secondaryButtonVisibility = str;
    }

    public void updateButtonVisibility(boolean z, boolean z2) {
        this.primaryButtonVisibility = updateButtonVisibilityState(this.primaryButtonVisibility, z);
        this.secondaryButtonVisibility = updateButtonVisibilityState(this.secondaryButtonVisibility, z2);
    }

    static String updateButtonVisibilityState(String str, boolean z) {
        if (!FooterButtonVisibility.VISIBLE_USING_XML.equals(str) && !FooterButtonVisibility.VISIBLE.equals(str) && !FooterButtonVisibility.INVISIBLE.equals(str)) {
            throw new IllegalStateException("Illegal visibility state: " + str);
        } else if (z && FooterButtonVisibility.INVISIBLE.equals(str)) {
            return FooterButtonVisibility.INVISIBLE_TO_VISIBLE;
        } else {
            if (z) {
                return str;
            }
            if (FooterButtonVisibility.VISIBLE_USING_XML.equals(str)) {
                return FooterButtonVisibility.VISIBLE_USING_XML_TO_INVISIBLE;
            }
            return FooterButtonVisibility.VISIBLE.equals(str) ? FooterButtonVisibility.VISIBLE_TO_INVISIBLE : str;
        }
    }

    public PersistableBundle getMetrics() {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(EXTRA_PRIMARY_BUTTON_VISIBILITY, this.primaryButtonVisibility);
        persistableBundle.putString(EXTRA_SECONDARY_BUTTON_VISIBILITY, this.secondaryButtonVisibility);
        return persistableBundle;
    }
}
