package android.hardware.biometrics;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class PromptInfo implements Parcelable {
    public static final Parcelable.Creator<PromptInfo> CREATOR = new Parcelable.Creator<PromptInfo>() { // from class: android.hardware.biometrics.PromptInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public PromptInfo mo3559createFromParcel(Parcel in) {
            return new PromptInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public PromptInfo[] mo3560newArray(int size) {
            return new PromptInfo[size];
        }
    };
    private boolean mAllowBackgroundAuthentication;
    private List<Integer> mAllowedSensorIds;
    private int mAuthenticators;
    private boolean mConfirmationRequested;
    private CharSequence mDescription;
    private boolean mDeviceCredentialAllowed;
    private CharSequence mDeviceCredentialDescription;
    private CharSequence mDeviceCredentialSubtitle;
    private CharSequence mDeviceCredentialTitle;
    private boolean mDisallowBiometricsIfPolicyExists;
    private CharSequence mNegativeButtonText;
    private boolean mReceiveSystemEvents;
    private CharSequence mSubtitle;
    private CharSequence mTitle;
    private boolean mUseDefaultTitle;

    public PromptInfo() {
        this.mConfirmationRequested = true;
        this.mAllowedSensorIds = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PromptInfo(Parcel in) {
        this.mConfirmationRequested = true;
        this.mAllowedSensorIds = new ArrayList();
        this.mTitle = in.readCharSequence();
        this.mUseDefaultTitle = in.readBoolean();
        this.mSubtitle = in.readCharSequence();
        this.mDescription = in.readCharSequence();
        this.mDeviceCredentialTitle = in.readCharSequence();
        this.mDeviceCredentialSubtitle = in.readCharSequence();
        this.mDeviceCredentialDescription = in.readCharSequence();
        this.mNegativeButtonText = in.readCharSequence();
        this.mConfirmationRequested = in.readBoolean();
        this.mDeviceCredentialAllowed = in.readBoolean();
        this.mAuthenticators = in.readInt();
        this.mDisallowBiometricsIfPolicyExists = in.readBoolean();
        this.mReceiveSystemEvents = in.readBoolean();
        this.mAllowedSensorIds = in.readArrayList(Integer.class.getClassLoader());
        this.mAllowBackgroundAuthentication = in.readBoolean();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeCharSequence(this.mTitle);
        dest.writeBoolean(this.mUseDefaultTitle);
        dest.writeCharSequence(this.mSubtitle);
        dest.writeCharSequence(this.mDescription);
        dest.writeCharSequence(this.mDeviceCredentialTitle);
        dest.writeCharSequence(this.mDeviceCredentialSubtitle);
        dest.writeCharSequence(this.mDeviceCredentialDescription);
        dest.writeCharSequence(this.mNegativeButtonText);
        dest.writeBoolean(this.mConfirmationRequested);
        dest.writeBoolean(this.mDeviceCredentialAllowed);
        dest.writeInt(this.mAuthenticators);
        dest.writeBoolean(this.mDisallowBiometricsIfPolicyExists);
        dest.writeBoolean(this.mReceiveSystemEvents);
        dest.writeList(this.mAllowedSensorIds);
        dest.writeBoolean(this.mAllowBackgroundAuthentication);
    }

    public boolean containsTestConfigurations() {
        return !this.mAllowedSensorIds.isEmpty() || this.mAllowBackgroundAuthentication;
    }

    public boolean containsPrivateApiConfigurations() {
        return this.mDisallowBiometricsIfPolicyExists || this.mUseDefaultTitle || this.mDeviceCredentialTitle != null || this.mDeviceCredentialSubtitle != null || this.mDeviceCredentialDescription != null || this.mReceiveSystemEvents;
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
    }

    public void setUseDefaultTitle(boolean useDefaultTitle) {
        this.mUseDefaultTitle = useDefaultTitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mSubtitle = subtitle;
    }

    public void setDescription(CharSequence description) {
        this.mDescription = description;
    }

    public void setDeviceCredentialTitle(CharSequence deviceCredentialTitle) {
        this.mDeviceCredentialTitle = deviceCredentialTitle;
    }

    public void setDeviceCredentialSubtitle(CharSequence deviceCredentialSubtitle) {
        this.mDeviceCredentialSubtitle = deviceCredentialSubtitle;
    }

    public void setDeviceCredentialDescription(CharSequence deviceCredentialDescription) {
        this.mDeviceCredentialDescription = deviceCredentialDescription;
    }

    public void setNegativeButtonText(CharSequence negativeButtonText) {
        this.mNegativeButtonText = negativeButtonText;
    }

    public void setConfirmationRequested(boolean confirmationRequested) {
        this.mConfirmationRequested = confirmationRequested;
    }

    public void setDeviceCredentialAllowed(boolean deviceCredentialAllowed) {
        this.mDeviceCredentialAllowed = deviceCredentialAllowed;
    }

    public void setAuthenticators(int authenticators) {
        this.mAuthenticators = authenticators;
    }

    public void setDisallowBiometricsIfPolicyExists(boolean disallowBiometricsIfPolicyExists) {
        this.mDisallowBiometricsIfPolicyExists = disallowBiometricsIfPolicyExists;
    }

    public void setReceiveSystemEvents(boolean receiveSystemEvents) {
        this.mReceiveSystemEvents = receiveSystemEvents;
    }

    public void setAllowedSensorIds(List<Integer> sensorIds) {
        this.mAllowedSensorIds = sensorIds;
    }

    public void setAllowBackgroundAuthentication(boolean allow) {
        this.mAllowBackgroundAuthentication = allow;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean isUseDefaultTitle() {
        return this.mUseDefaultTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public CharSequence getDeviceCredentialTitle() {
        return this.mDeviceCredentialTitle;
    }

    public CharSequence getDeviceCredentialSubtitle() {
        return this.mDeviceCredentialSubtitle;
    }

    public CharSequence getDeviceCredentialDescription() {
        return this.mDeviceCredentialDescription;
    }

    public CharSequence getNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public boolean isConfirmationRequested() {
        return this.mConfirmationRequested;
    }

    @Deprecated
    public boolean isDeviceCredentialAllowed() {
        return this.mDeviceCredentialAllowed;
    }

    public int getAuthenticators() {
        return this.mAuthenticators;
    }

    public boolean isDisallowBiometricsIfPolicyExists() {
        return this.mDisallowBiometricsIfPolicyExists;
    }

    public boolean isReceiveSystemEvents() {
        return this.mReceiveSystemEvents;
    }

    public List<Integer> getAllowedSensorIds() {
        return this.mAllowedSensorIds;
    }

    public boolean isAllowBackgroundAuthentication() {
        return this.mAllowBackgroundAuthentication;
    }
}
