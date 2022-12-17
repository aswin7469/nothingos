package com.android.settings.datausage;

import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkTemplate;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import com.android.settings.R$string;
import com.android.settings.datausage.TemplatePreference;
import com.android.settings.network.MobileDataEnabledListener;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.CustomDialogPreferenceCompat;

public class CellDataPreference extends CustomDialogPreferenceCompat implements TemplatePreference, MobileDataEnabledListener.Client {
    public boolean mChecked;
    private MobileDataEnabledListener mDataStateListener;
    final ProxySubscriptionManager.OnActiveSubscriptionChangedListener mOnSubscriptionsChangeListener = new ProxySubscriptionManager.OnActiveSubscriptionChangedListener() {
        public void onChanged() {
            CellDataPreference.this.updateEnabled();
        }
    };
    public int mSubId = -1;

    public CellDataPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R$attr.switchPreferenceStyle, 16843629));
        this.mDataStateListener = new MobileDataEnabledListener(context, this);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        CellDataState cellDataState = (CellDataState) parcelable;
        super.onRestoreInstanceState(cellDataState.getSuperState());
        this.mChecked = cellDataState.mChecked;
        if (this.mSubId == -1) {
            this.mSubId = cellDataState.mSubId;
            setKey(getKey() + this.mSubId);
        }
        notifyChanged();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        CellDataState cellDataState = new CellDataState(super.onSaveInstanceState());
        cellDataState.mChecked = this.mChecked;
        cellDataState.mSubId = this.mSubId;
        return cellDataState;
    }

    public void onAttached() {
        super.onAttached();
        this.mDataStateListener.start(this.mSubId);
        getProxySubscriptionManager().addActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
    }

    public void onDetached() {
        this.mDataStateListener.stop();
        getProxySubscriptionManager().removeActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
        super.onDetached();
    }

    public void setTemplate(NetworkTemplate networkTemplate, int i, TemplatePreference.NetworkServices networkServices) {
        if (i != -1) {
            getProxySubscriptionManager().addActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
            if (this.mSubId == -1) {
                this.mSubId = i;
                setKey(getKey() + i);
            }
            updateEnabled();
            updateChecked();
            return;
        }
        throw new IllegalArgumentException("CellDataPreference needs a SubscriptionInfo");
    }

    /* access modifiers changed from: package-private */
    public ProxySubscriptionManager getProxySubscriptionManager() {
        return ProxySubscriptionManager.getInstance(getContext());
    }

    /* access modifiers changed from: package-private */
    public SubscriptionInfo getActiveSubscriptionInfo(int i) {
        return getProxySubscriptionManager().getActiveSubscriptionInfo(i);
    }

    private void updateChecked() {
        setChecked(((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).getDataEnabled(this.mSubId));
    }

    /* access modifiers changed from: private */
    public void updateEnabled() {
        setEnabled(getActiveSubscriptionInfo(this.mSubId) != null);
    }

    /* access modifiers changed from: protected */
    public void performClick(View view) {
        Context context = getContext();
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 178, !this.mChecked);
        SubscriptionInfo activeSubscriptionInfo = getActiveSubscriptionInfo(this.mSubId);
        SubscriptionInfo activeSubscriptionInfo2 = getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (this.mChecked) {
            setMobileDataEnabled(false);
            if (activeSubscriptionInfo2 != null && activeSubscriptionInfo != null && activeSubscriptionInfo.getSubscriptionId() == activeSubscriptionInfo2.getSubscriptionId()) {
                disableDataForOtherSubscriptions(this.mSubId);
                return;
            }
            return;
        }
        setMobileDataEnabled(true);
    }

    private void setMobileDataEnabled(boolean z) {
        ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).setDataEnabled(this.mSubId, z);
        setChecked(z);
    }

    private void setChecked(boolean z) {
        if (this.mChecked != z) {
            this.mChecked = z;
            notifyChanged();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908352);
        findViewById.setClickable(false);
        ((Checkable) findViewById).setChecked(this.mChecked);
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        showDisableDialog(builder, onClickListener);
    }

    private void showDisableDialog(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        builder.setTitle((CharSequence) null).setMessage(R$string.data_usage_disable_mobile).setPositiveButton(17039370, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
    }

    private void disableDataForOtherSubscriptions(int i) {
        if (getActiveSubscriptionInfo(i) != null) {
            ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).setDataEnabled(i, false);
        }
    }

    /* access modifiers changed from: protected */
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            setMobileDataEnabled(false);
        }
    }

    public void onMobileDataEnabledChange() {
        updateChecked();
    }

    public static class CellDataState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<CellDataState> CREATOR = new Parcelable.Creator<CellDataState>() {
            public CellDataState createFromParcel(Parcel parcel) {
                return new CellDataState(parcel);
            }

            public CellDataState[] newArray(int i) {
                return new CellDataState[i];
            }
        };
        public boolean mChecked;
        public int mSubId;

        public CellDataState(Parcelable parcelable) {
            super(parcelable);
        }

        public CellDataState(Parcel parcel) {
            super(parcel);
            this.mChecked = parcel.readByte() != 0;
            this.mSubId = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.mChecked ? (byte) 1 : 0);
            parcel.writeInt(this.mSubId);
        }
    }
}
