package android.telephony;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.internal.telephony.util.TelephonyUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes3.dex */
public class SubscriptionInfo implements Parcelable {
    public static final Parcelable.Creator<SubscriptionInfo> CREATOR = new Parcelable.Creator<SubscriptionInfo>() { // from class: android.telephony.SubscriptionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SubscriptionInfo mo3559createFromParcel(Parcel source) {
            int id = source.readInt();
            String iccId = source.readString();
            int simSlotIndex = source.readInt();
            CharSequence displayName = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(source);
            CharSequence carrierName = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(source);
            int nameSource = source.readInt();
            int iconTint = source.readInt();
            String number = source.readString();
            int dataRoaming = source.readInt();
            String mcc = source.readString();
            String mnc = source.readString();
            String countryIso = source.readString();
            Bitmap iconBitmap = (Bitmap) source.readParcelable(Bitmap.class.getClassLoader());
            boolean isEmbedded = source.readBoolean();
            UiccAccessRule[] nativeAccessRules = (UiccAccessRule[]) source.createTypedArray(UiccAccessRule.CREATOR);
            String cardString = source.readString();
            int cardId = source.readInt();
            boolean isOpportunistic = source.readBoolean();
            String groupUUID = source.readString();
            boolean isGroupDisabled = source.readBoolean();
            int carrierid = source.readInt();
            int profileClass = source.readInt();
            int subType = source.readInt();
            String[] ehplmns = source.createStringArray();
            String[] hplmns = source.createStringArray();
            String groupOwner = source.readString();
            UiccAccessRule[] carrierConfigAccessRules = (UiccAccessRule[]) source.createTypedArray(UiccAccessRule.CREATOR);
            boolean areUiccApplicationsEnabled = source.readBoolean();
            SubscriptionInfo info = new SubscriptionInfo(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, dataRoaming, iconBitmap, mcc, mnc, countryIso, isEmbedded, nativeAccessRules, cardString, cardId, isOpportunistic, groupUUID, isGroupDisabled, carrierid, profileClass, subType, groupOwner, carrierConfigAccessRules, areUiccApplicationsEnabled);
            info.setAssociatedPlmns(ehplmns, hplmns);
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SubscriptionInfo[] mo3560newArray(int size) {
            return new SubscriptionInfo[size];
        }
    };
    private static final int TEXT_SIZE = 16;
    private boolean mAreUiccApplicationsEnabled;
    private int mCardId;
    private String mCardString;
    private UiccAccessRule[] mCarrierConfigAccessRules;
    private int mCarrierId;
    private CharSequence mCarrierName;
    private String mCountryIso;
    private int mDataRoaming;
    private CharSequence mDisplayName;
    private String[] mEhplmns;
    private String mGroupOwner;
    private ParcelUuid mGroupUUID;
    private String[] mHplmns;
    private String mIccId;
    private Bitmap mIconBitmap;
    private int mIconTint;
    private int mId;
    private boolean mIsEmbedded;
    private boolean mIsGroupDisabled;
    private boolean mIsOpportunistic;
    private String mMcc;
    private String mMnc;
    private int mNameSource;
    private UiccAccessRule[] mNativeAccessRules;
    private String mNumber;
    private int mProfileClass;
    private int mSimSlotIndex;
    private int mSubscriptionType;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SubscriptionInfo(SubscriptionInfo info) {
        this(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r1, r15, r15, r15, r1 == null ? null : r1.toString(), info.mIsGroupDisabled, info.mCarrierId, info.mProfileClass, info.mSubscriptionType, info.mGroupOwner, info.mCarrierConfigAccessRules, info.mAreUiccApplicationsEnabled);
        int i = info.mId;
        String str = info.mIccId;
        int i2 = info.mSimSlotIndex;
        CharSequence charSequence = info.mDisplayName;
        CharSequence charSequence2 = info.mCarrierName;
        int i3 = info.mNameSource;
        int i4 = info.mIconTint;
        String str2 = info.mNumber;
        int i5 = info.mDataRoaming;
        Bitmap bitmap = info.mIconBitmap;
        String str3 = info.mMcc;
        String str4 = info.mMnc;
        String str5 = info.mCountryIso;
        boolean z = info.mIsEmbedded;
        UiccAccessRule[] uiccAccessRuleArr = info.mNativeAccessRules;
        String str6 = info.mCardString;
        int i6 = info.mCardId;
        boolean z2 = info.mIsOpportunistic;
        ParcelUuid parcelUuid = info.mGroupUUID;
    }

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] nativeAccessRules, String cardString) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, isEmbedded, nativeAccessRules, cardString, -1, false, null, false, -1, -1, 0, null, null, true);
    }

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] nativeAccessRules, String cardString, boolean isOpportunistic, String groupUUID, int carrierId, int profileClass) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, isEmbedded, nativeAccessRules, cardString, -1, isOpportunistic, groupUUID, false, carrierId, profileClass, 0, null, null, true);
    }

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] nativeAccessRules, String cardString, int cardId, boolean isOpportunistic, String groupUUID, boolean isGroupDisabled, int carrierId, int profileClass, int subType, String groupOwner, UiccAccessRule[] carrierConfigAccessRules, boolean areUiccApplicationsEnabled) {
        this.mIsGroupDisabled = false;
        this.mAreUiccApplicationsEnabled = true;
        this.mId = id;
        this.mIccId = iccId;
        this.mSimSlotIndex = simSlotIndex;
        this.mDisplayName = displayName;
        this.mCarrierName = carrierName;
        this.mNameSource = nameSource;
        this.mIconTint = iconTint;
        this.mNumber = number;
        this.mDataRoaming = roaming;
        this.mIconBitmap = icon;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mCountryIso = countryIso;
        this.mIsEmbedded = isEmbedded;
        this.mNativeAccessRules = nativeAccessRules;
        this.mCardString = cardString;
        this.mCardId = cardId;
        this.mIsOpportunistic = isOpportunistic;
        this.mGroupUUID = groupUUID == null ? null : ParcelUuid.fromString(groupUUID);
        this.mIsGroupDisabled = isGroupDisabled;
        this.mCarrierId = carrierId;
        this.mProfileClass = profileClass;
        this.mSubscriptionType = subType;
        this.mGroupOwner = groupOwner;
        this.mCarrierConfigAccessRules = carrierConfigAccessRules;
        this.mAreUiccApplicationsEnabled = areUiccApplicationsEnabled;
    }

    public int getSubscriptionId() {
        return this.mId;
    }

    public String getIccId() {
        return this.mIccId;
    }

    public void clearIccId() {
        this.mIccId = "";
    }

    public int getSimSlotIndex() {
        return this.mSimSlotIndex;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public CharSequence getDisplayName() {
        return this.mDisplayName;
    }

    public void setDisplayName(CharSequence name) {
        this.mDisplayName = name;
    }

    public CharSequence getCarrierName() {
        return this.mCarrierName;
    }

    public void setCarrierName(CharSequence name) {
        this.mCarrierName = name;
    }

    public int getNameSource() {
        return this.mNameSource;
    }

    public void setAssociatedPlmns(String[] ehplmns, String[] hplmns) {
        this.mEhplmns = ehplmns;
        this.mHplmns = hplmns;
    }

    public Bitmap createIconBitmap(Context context) {
        int width = this.mIconBitmap.getWidth();
        int height = this.mIconBitmap.getHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Bitmap workingBitmap = Bitmap.createBitmap(metrics, width, height, this.mIconBitmap.getConfig());
        Canvas canvas = new Canvas(workingBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(this.mIconTint, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(this.mIconBitmap, 0.0f, 0.0f, paint);
        paint.setColorFilter(null);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_FAMILY, 0));
        paint.setColor(-1);
        paint.setTextSize(metrics.density * 16.0f);
        String index = TextUtils.formatSimple("%d", Integer.valueOf(this.mSimSlotIndex + 1));
        Rect textBound = new Rect();
        paint.getTextBounds(index, 0, 1, textBound);
        float xOffset = (width / 2.0f) - textBound.centerX();
        float yOffset = (height / 2.0f) - textBound.centerY();
        canvas.drawText(index, xOffset, yOffset, paint);
        return workingBitmap;
    }

    public int getIconTint() {
        return this.mIconTint;
    }

    public void setIconTint(int iconTint) {
        this.mIconTint = iconTint;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public void clearNumber() {
        this.mNumber = "";
    }

    public int getDataRoaming() {
        return this.mDataRoaming;
    }

    @Deprecated
    public int getMcc() {
        int i = 0;
        try {
            String str = this.mMcc;
            if (str != null) {
                i = Integer.valueOf(str).intValue();
            }
            return i;
        } catch (NumberFormatException e) {
            Log.w(SubscriptionInfo.class.getSimpleName(), "MCC string is not a number");
            return 0;
        }
    }

    @Deprecated
    public int getMnc() {
        int i = 0;
        try {
            String str = this.mMnc;
            if (str != null) {
                i = Integer.valueOf(str).intValue();
            }
            return i;
        } catch (NumberFormatException e) {
            Log.w(SubscriptionInfo.class.getSimpleName(), "MNC string is not a number");
            return 0;
        }
    }

    public String getMccString() {
        return this.mMcc;
    }

    public String getMncString() {
        return this.mMnc;
    }

    public String getCountryIso() {
        return this.mCountryIso;
    }

    public boolean isEmbedded() {
        return this.mIsEmbedded;
    }

    public boolean isOpportunistic() {
        return this.mIsOpportunistic;
    }

    public ParcelUuid getGroupUuid() {
        return this.mGroupUUID;
    }

    public void clearGroupUuid() {
        this.mGroupUUID = null;
    }

    public List<String> getEhplmns() {
        String[] strArr = this.mEhplmns;
        return strArr == null ? Collections.emptyList() : Arrays.asList(strArr);
    }

    public List<String> getHplmns() {
        String[] strArr = this.mHplmns;
        return strArr == null ? Collections.emptyList() : Arrays.asList(strArr);
    }

    public String getGroupOwner() {
        return this.mGroupOwner;
    }

    @SystemApi
    public int getProfileClass() {
        return this.mProfileClass;
    }

    public int getSubscriptionType() {
        return this.mSubscriptionType;
    }

    @Deprecated
    public boolean canManageSubscription(Context context) {
        return canManageSubscription(context, context.getPackageName());
    }

    @Deprecated
    public boolean canManageSubscription(Context context, String packageName) {
        List<UiccAccessRule> allAccessRules = getAllAccessRules();
        if (allAccessRules == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 134217728);
            for (UiccAccessRule rule : allAccessRules) {
                if (rule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("SubscriptionInfo", "canManageSubscription: Unknown package: " + packageName, e);
            return false;
        }
    }

    @SystemApi
    public List<UiccAccessRule> getAccessRules() {
        UiccAccessRule[] uiccAccessRuleArr = this.mNativeAccessRules;
        if (uiccAccessRuleArr == null) {
            return null;
        }
        return Arrays.asList(uiccAccessRuleArr);
    }

    public List<UiccAccessRule> getAllAccessRules() {
        List<UiccAccessRule> merged = new ArrayList<>();
        if (this.mNativeAccessRules != null) {
            merged.addAll(getAccessRules());
        }
        UiccAccessRule[] uiccAccessRuleArr = this.mCarrierConfigAccessRules;
        if (uiccAccessRuleArr != null) {
            merged.addAll(Arrays.asList(uiccAccessRuleArr));
        }
        if (merged.isEmpty()) {
            return null;
        }
        return merged;
    }

    public String getCardString() {
        return this.mCardString;
    }

    public void clearCardString() {
        this.mCardString = "";
    }

    public int getCardId() {
        return this.mCardId;
    }

    public void setGroupDisabled(boolean isGroupDisabled) {
        this.mIsGroupDisabled = isGroupDisabled;
    }

    @SystemApi
    public boolean isGroupDisabled() {
        return this.mIsGroupDisabled;
    }

    @SystemApi
    public boolean areUiccApplicationsEnabled() {
        return this.mAreUiccApplicationsEnabled;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mIccId);
        dest.writeInt(this.mSimSlotIndex);
        TextUtils.writeToParcel(this.mDisplayName, dest, 0);
        TextUtils.writeToParcel(this.mCarrierName, dest, 0);
        dest.writeInt(this.mNameSource);
        dest.writeInt(this.mIconTint);
        dest.writeString(this.mNumber);
        dest.writeInt(this.mDataRoaming);
        dest.writeString(this.mMcc);
        dest.writeString(this.mMnc);
        dest.writeString(this.mCountryIso);
        dest.writeParcelable(this.mIconBitmap, flags);
        dest.writeBoolean(this.mIsEmbedded);
        dest.writeTypedArray(this.mNativeAccessRules, flags);
        dest.writeString(this.mCardString);
        dest.writeInt(this.mCardId);
        dest.writeBoolean(this.mIsOpportunistic);
        ParcelUuid parcelUuid = this.mGroupUUID;
        dest.writeString(parcelUuid == null ? null : parcelUuid.toString());
        dest.writeBoolean(this.mIsGroupDisabled);
        dest.writeInt(this.mCarrierId);
        dest.writeInt(this.mProfileClass);
        dest.writeInt(this.mSubscriptionType);
        dest.writeStringArray(this.mEhplmns);
        dest.writeStringArray(this.mHplmns);
        dest.writeString(this.mGroupOwner);
        dest.writeTypedArray(this.mCarrierConfigAccessRules, flags);
        dest.writeBoolean(this.mAreUiccApplicationsEnabled);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static String givePrintableIccid(String iccId) {
        if (iccId == null) {
            return null;
        }
        if (iccId.length() > 9 && !TelephonyUtils.IS_DEBUGGABLE) {
            String iccIdToPrint = iccId.substring(0, 9) + com.android.telephony.Rlog.pii(false, (Object) iccId.substring(9));
            return iccIdToPrint;
        }
        return iccId;
    }

    public String toString() {
        String iccIdToPrint = givePrintableIccid(this.mIccId);
        String cardStringToPrint = givePrintableIccid(this.mCardString);
        return "{id=" + this.mId + " iccId=" + iccIdToPrint + " simSlotIndex=" + this.mSimSlotIndex + " carrierId=" + this.mCarrierId + " displayName=" + ((Object) this.mDisplayName) + " carrierName=" + ((Object) this.mCarrierName) + " nameSource=" + this.mNameSource + " iconTint=" + this.mIconTint + " number=" + com.android.telephony.Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mNumber) + " dataRoaming=" + this.mDataRoaming + " iconBitmap=" + this.mIconBitmap + " mcc=" + this.mMcc + " mnc=" + this.mMnc + " countryIso=" + this.mCountryIso + " isEmbedded=" + this.mIsEmbedded + " nativeAccessRules=" + Arrays.toString(this.mNativeAccessRules) + " cardString=" + cardStringToPrint + " cardId=" + this.mCardId + " isOpportunistic=" + this.mIsOpportunistic + " groupUUID=" + this.mGroupUUID + " isGroupDisabled=" + this.mIsGroupDisabled + " profileClass=" + this.mProfileClass + " ehplmns=" + Arrays.toString(this.mEhplmns) + " hplmns=" + Arrays.toString(this.mHplmns) + " subscriptionType=" + this.mSubscriptionType + " groupOwner=" + this.mGroupOwner + " carrierConfigAccessRules=" + Arrays.toString(this.mCarrierConfigAccessRules) + " areUiccApplicationsEnabled=" + this.mAreUiccApplicationsEnabled + "}";
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mId), Integer.valueOf(this.mSimSlotIndex), Integer.valueOf(this.mNameSource), Integer.valueOf(this.mIconTint), Integer.valueOf(this.mDataRoaming), Boolean.valueOf(this.mIsEmbedded), Boolean.valueOf(this.mIsOpportunistic), this.mGroupUUID, this.mIccId, this.mNumber, this.mMcc, this.mMnc, this.mCountryIso, this.mCardString, Integer.valueOf(this.mCardId), this.mDisplayName, this.mCarrierName, this.mNativeAccessRules, Boolean.valueOf(this.mIsGroupDisabled), Integer.valueOf(this.mCarrierId), Integer.valueOf(this.mProfileClass), this.mGroupOwner, Boolean.valueOf(this.mAreUiccApplicationsEnabled));
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        try {
            SubscriptionInfo toCompare = (SubscriptionInfo) obj;
            if (this.mId != toCompare.mId || this.mSimSlotIndex != toCompare.mSimSlotIndex || this.mNameSource != toCompare.mNameSource || this.mIconTint != toCompare.mIconTint || this.mDataRoaming != toCompare.mDataRoaming || this.mIsEmbedded != toCompare.mIsEmbedded || this.mIsOpportunistic != toCompare.mIsOpportunistic || this.mIsGroupDisabled != toCompare.mIsGroupDisabled || this.mAreUiccApplicationsEnabled != toCompare.mAreUiccApplicationsEnabled || this.mCarrierId != toCompare.mCarrierId || !Objects.equals(this.mGroupUUID, toCompare.mGroupUUID) || !Objects.equals(this.mIccId, toCompare.mIccId) || !Objects.equals(this.mNumber, toCompare.mNumber) || !Objects.equals(this.mMcc, toCompare.mMcc) || !Objects.equals(this.mMnc, toCompare.mMnc) || !Objects.equals(this.mCountryIso, toCompare.mCountryIso) || !Objects.equals(this.mCardString, toCompare.mCardString) || !Objects.equals(Integer.valueOf(this.mCardId), Integer.valueOf(toCompare.mCardId)) || !Objects.equals(this.mGroupOwner, toCompare.mGroupOwner) || !TextUtils.equals(this.mDisplayName, toCompare.mDisplayName) || !TextUtils.equals(this.mCarrierName, toCompare.mCarrierName) || !Arrays.equals(this.mNativeAccessRules, toCompare.mNativeAccessRules) || this.mProfileClass != toCompare.mProfileClass || !Arrays.equals(this.mEhplmns, toCompare.mEhplmns) || !Arrays.equals(this.mHplmns, toCompare.mHplmns)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
