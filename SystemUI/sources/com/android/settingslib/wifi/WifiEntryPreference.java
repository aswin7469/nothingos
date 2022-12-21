package com.android.settingslib.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifitrackerlib.WifiEntry;

public class WifiEntryPreference extends Preference implements WifiEntry.WifiEntryCallback, View.OnClickListener {
    private static final int[] FRICTION_ATTRS = {C1757R.attr.wifi_friction};
    private static final int[] STATE_SECURED = {C1757R.attr.state_encrypted};
    private static final int[] WIFI_CONNECTION_STRENGTH = {C1757R.string.accessibility_no_wifi, C1757R.string.accessibility_wifi_one_bar, C1757R.string.accessibility_wifi_two_bars, C1757R.string.accessibility_wifi_three_bars, C1757R.string.accessibility_wifi_signal_full};
    private CharSequence mContentDescription;
    private final StateListDrawable mFrictionSld;
    private final IconInjector mIconInjector;
    private int mLevel;
    private OnButtonClickListener mOnButtonClickListener;
    private boolean mShowX;
    private WifiEntry mWifiEntry;
    private int mWifiStandard;

    public interface OnButtonClickListener {
        void onButtonClick(WifiEntryPreference wifiEntryPreference);
    }

    public void onConnectResult(int i) {
    }

    public void onDisconnectResult(int i) {
    }

    public void onForgetResult(int i) {
    }

    public void onSignInResult(int i) {
    }

    public WifiEntryPreference(Context context, WifiEntry wifiEntry) {
        this(context, wifiEntry, new IconInjector(context));
    }

    WifiEntryPreference(Context context, WifiEntry wifiEntry, IconInjector iconInjector) {
        super(context);
        this.mLevel = -1;
        setLayoutResource(C1757R.layout.preference_access_point);
        setWidgetLayoutResource(C1757R.layout.access_point_friction_widget);
        this.mFrictionSld = getFrictionStateListDrawable();
        this.mWifiEntry = wifiEntry;
        wifiEntry.setListener(this);
        this.mIconInjector = iconInjector;
        refresh();
    }

    public WifiEntry getWifiEntry() {
        return this.mWifiEntry;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Drawable icon = getIcon();
        if (icon != null) {
            icon.setLevel(this.mLevel);
        }
        preferenceViewHolder.itemView.setContentDescription(this.mContentDescription);
        preferenceViewHolder.findViewById(C1757R.C1760id.two_target_divider).setVisibility(4);
        ImageButton imageButton = (ImageButton) preferenceViewHolder.findViewById(C1757R.C1760id.icon_button);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(C1757R.C1760id.friction_icon);
        if (this.mWifiEntry.getHelpUriString() == null || this.mWifiEntry.getConnectedState() != 0) {
            imageButton.setVisibility(8);
            if (imageView != null) {
                imageView.setVisibility(0);
                bindFrictionImage(imageView);
                return;
            }
            return;
        }
        Drawable drawable = getDrawable(C1757R.C1759drawable.ic_help);
        drawable.setTintList(Utils.getColorAttr(getContext(), 16843817));
        imageButton.setImageDrawable(drawable);
        imageButton.setVisibility(0);
        imageButton.setOnClickListener(this);
        imageButton.setContentDescription(getContext().getText(C1757R.string.help_label));
        if (imageView != null) {
            imageView.setVisibility(8);
        }
    }

    public void refresh() {
        setTitle((CharSequence) this.mWifiEntry.getTitle());
        int level = this.mWifiEntry.getLevel();
        int wifiStandard = this.mWifiEntry.getWifiStandard();
        boolean shouldShowXLevelIcon = this.mWifiEntry.shouldShowXLevelIcon();
        if (!(level == this.mLevel && shouldShowXLevelIcon == this.mShowX && wifiStandard == this.mWifiStandard)) {
            this.mLevel = level;
            this.mWifiStandard = wifiStandard;
            this.mShowX = shouldShowXLevelIcon;
            updateIcon(shouldShowXLevelIcon, level, wifiStandard);
            notifyChanged();
        }
        String summary = this.mWifiEntry.getSummary(false);
        if (this.mWifiEntry.isPskSaeTransitionMode()) {
            summary = "WPA3(SAE Transition Mode) " + summary;
        } else if (this.mWifiEntry.isOweTransitionMode()) {
            summary = "WPA3(OWE Transition Mode) " + summary;
        } else if (this.mWifiEntry.getSecurity() == 5) {
            summary = "WPA3(SAE) " + summary;
        } else if (this.mWifiEntry.getSecurity() == 4) {
            summary = "WPA3(OWE) " + summary;
        }
        setSummary((CharSequence) summary);
        this.mContentDescription = buildContentDescription();
    }

    public void onUpdated() {
        refresh();
    }

    /* access modifiers changed from: protected */
    public int getIconColorAttr() {
        return this.mWifiEntry.hasInternetAccess() && this.mWifiEntry.getConnectedState() == 2 ? 16843829 : 16843817;
    }

    private void updateIcon(boolean z, int i, int i2) {
        if (i == -1) {
            setIcon((Drawable) null);
            return;
        }
        Drawable icon = this.mIconInjector.getIcon(z, i, i2);
        if (icon != null) {
            icon.setTint(Utils.getColorAttrDefaultColor(getContext(), getIconColorAttr()));
            setIcon(icon);
            return;
        }
        setIcon((Drawable) null);
    }

    private StateListDrawable getFrictionStateListDrawable() {
        TypedArray typedArray;
        try {
            typedArray = getContext().getTheme().obtainStyledAttributes(FRICTION_ATTRS);
        } catch (Resources.NotFoundException unused) {
            typedArray = null;
        }
        if (typedArray != null) {
            return (StateListDrawable) typedArray.getDrawable(0);
        }
        return null;
    }

    private void bindFrictionImage(ImageView imageView) {
        if (imageView != null && this.mFrictionSld != null) {
            if (!(this.mWifiEntry.getSecurity() == 0 || this.mWifiEntry.getSecurity() == 4)) {
                this.mFrictionSld.setState(STATE_SECURED);
            }
            imageView.setImageDrawable(this.mFrictionSld.getCurrent());
        }
    }

    /* access modifiers changed from: package-private */
    public CharSequence buildContentDescription() {
        String str;
        Context context = getContext();
        CharSequence title = getTitle();
        CharSequence summary = getSummary();
        if (!TextUtils.isEmpty(summary)) {
            title = TextUtils.concat(new CharSequence[]{title, NavigationBarInflaterView.BUTTON_SEPARATOR, summary});
        }
        int level = this.mWifiEntry.getLevel();
        if (level >= 0) {
            int[] iArr = WIFI_CONNECTION_STRENGTH;
            if (level < iArr.length) {
                title = TextUtils.concat(new CharSequence[]{title, NavigationBarInflaterView.BUTTON_SEPARATOR, context.getString(iArr[level])});
            }
        }
        CharSequence[] charSequenceArr = new CharSequence[3];
        charSequenceArr[0] = title;
        charSequenceArr[1] = NavigationBarInflaterView.BUTTON_SEPARATOR;
        if (this.mWifiEntry.getSecurity() == 0) {
            str = context.getString(C1757R.string.accessibility_wifi_security_type_none);
        } else {
            str = context.getString(C1757R.string.accessibility_wifi_security_type_secured);
        }
        charSequenceArr[2] = str;
        return TextUtils.concat(charSequenceArr);
    }

    static class IconInjector {
        private final Context mContext;

        IconInjector(Context context) {
            this.mContext = context;
        }

        public Drawable getIcon(boolean z, int i, int i2) {
            return this.mContext.getDrawable(WifiUtils.getInternetIconResource(i, z, i2));
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
        notifyChanged();
    }

    public void onClick(View view) {
        OnButtonClickListener onButtonClickListener;
        if (view.getId() == C1757R.C1760id.icon_button && (onButtonClickListener = this.mOnButtonClickListener) != null) {
            onButtonClickListener.onButtonClick(this);
        }
    }

    private Drawable getDrawable(int i) {
        try {
            return getContext().getDrawable(i);
        } catch (Resources.NotFoundException unused) {
            return null;
        }
    }
}
