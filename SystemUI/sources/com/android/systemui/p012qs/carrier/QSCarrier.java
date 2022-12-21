package com.android.systemui.p012qs.carrier;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.C1893R;
import com.android.systemui.FontSizeUtils;
import java.util.Objects;

/* renamed from: com.android.systemui.qs.carrier.QSCarrier */
public class QSCarrier extends LinearLayout {
    private TextView mCarrierText;
    private boolean mIsSingleCarrier;
    private CellSignalState mLastSignalState;
    private View mMobileGroup;
    private ImageView mMobileRoaming;
    private ImageView mMobileSignal;
    private boolean mProviderModelInitialized = false;
    private View mSpacer;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSCarrier(Context context) {
        super(context);
    }

    public QSCarrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mMobileGroup = findViewById(C1893R.C1897id.mobile_combo);
        this.mMobileRoaming = (ImageView) findViewById(C1893R.C1897id.mobile_roaming);
        this.mMobileSignal = (ImageView) findViewById(C1893R.C1897id.mobile_signal);
        this.mCarrierText = (TextView) findViewById(C1893R.C1897id.qs_carrier_text);
        this.mSpacer = findViewById(C1893R.C1897id.spacer);
    }

    public boolean updateState(CellSignalState cellSignalState, boolean z) {
        int i = 0;
        if (Objects.equals(cellSignalState, this.mLastSignalState) && z == this.mIsSingleCarrier) {
            return false;
        }
        this.mLastSignalState = cellSignalState;
        this.mIsSingleCarrier = z;
        boolean z2 = cellSignalState.visible && !z;
        this.mMobileGroup.setVisibility(z2 ? 0 : 8);
        this.mSpacer.setVisibility(z ? 0 : 8);
        if (z2) {
            ImageView imageView = this.mMobileRoaming;
            if (!cellSignalState.roaming) {
                i = 8;
            }
            imageView.setVisibility(i);
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16842806);
            this.mMobileRoaming.setImageTintList(colorAttr);
            this.mMobileSignal.setImageTintList(colorAttr);
            if (cellSignalState.providerModelBehavior) {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(this.mContext.getDrawable(C1893R.C1895drawable.ic_qs_no_calling_sms));
                }
                this.mMobileSignal.setImageDrawable(this.mContext.getDrawable(cellSignalState.mobileSignalIconId));
                this.mMobileSignal.setContentDescription(cellSignalState.contentDescription);
            } else {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(new SignalDrawable(this.mContext));
                }
                this.mMobileSignal.setImageLevel(cellSignalState.mobileSignalIconId);
                StringBuilder sb = new StringBuilder();
                if (cellSignalState.contentDescription != null) {
                    sb.append(cellSignalState.contentDescription).append(", ");
                }
                if (cellSignalState.roaming) {
                    sb.append(this.mContext.getString(C1893R.string.data_connection_roaming)).append(", ");
                }
                if (hasValidTypeContentDescription(cellSignalState.typeContentDescription)) {
                    sb.append(cellSignalState.typeContentDescription);
                }
                this.mMobileSignal.setContentDescription(sb);
            }
        }
        return true;
    }

    private boolean hasValidTypeContentDescription(String str) {
        return TextUtils.equals(str, this.mContext.getString(C1893R.string.data_connection_no_internet)) || TextUtils.equals(str, this.mContext.getString(C1893R.string.cell_data_off_content_description)) || TextUtils.equals(str, this.mContext.getString(C1893R.string.not_default_data_content_description));
    }

    /* access modifiers changed from: package-private */
    public View getRSSIView() {
        return this.mMobileGroup;
    }

    public void setCarrierText(CharSequence charSequence) {
        this.mCarrierText.setText(charSequence);
    }

    public void updateTextAppearance(int i) {
        FontSizeUtils.updateFontSizeFromStyle(this.mCarrierText, i);
    }
}
