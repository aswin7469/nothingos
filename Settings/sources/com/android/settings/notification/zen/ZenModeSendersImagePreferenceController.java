package com.android.settings.notification.zen;

import android.content.Context;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settingslib.widget.LayoutPreference;

public class ZenModeSendersImagePreferenceController extends AbstractZenModePreferenceController {
    private ImageView mImageView;
    private final boolean mIsMessages;

    public boolean isAvailable() {
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mImageView = (ImageView) ((LayoutPreference) preferenceScreen.findPreference(this.KEY)).findViewById(R$id.zen_mode_settings_senders_image);
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        int i;
        String str;
        int i2;
        int i3;
        int i4;
        int i5;
        int prioritySenders = getPrioritySenders();
        if (prioritySenders == 0) {
            if (this.mIsMessages) {
                i = R$drawable.zen_messages_any;
            } else {
                i = R$drawable.zen_calls_any;
            }
            str = this.mContext.getString(R$string.zen_mode_from_anyone);
        } else if (1 == prioritySenders) {
            if (this.mIsMessages) {
                i5 = R$drawable.zen_messages_contacts;
            } else {
                i5 = R$drawable.zen_calls_contacts;
            }
            str = this.mContext.getString(R$string.zen_mode_from_contacts);
        } else if (2 == prioritySenders) {
            if (this.mIsMessages) {
                i4 = R$drawable.zen_messages_starred;
            } else {
                i4 = R$drawable.zen_calls_starred;
            }
            str = this.mContext.getString(R$string.zen_mode_from_starred);
        } else {
            boolean z = this.mIsMessages;
            if (z) {
                i2 = R$drawable.zen_messages_none;
            } else {
                i2 = R$drawable.zen_calls_none;
            }
            Context context = this.mContext;
            if (z) {
                i3 = R$string.zen_mode_none_messages;
            } else {
                i3 = R$string.zen_mode_none_calls;
            }
            int i6 = i2;
            str = context.getString(i3);
            i = i6;
        }
        this.mImageView.setImageResource(i);
        this.mImageView.setContentDescription(str);
    }

    private int getPrioritySenders() {
        if (this.mIsMessages) {
            return this.mBackend.getPriorityMessageSenders();
        }
        return this.mBackend.getPriorityCallSenders();
    }
}
