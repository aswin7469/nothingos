package android.view.inputmethod;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Printer;
import android.util.Slog;
import android.util.Xml;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes3.dex */
public final class InputMethodInfo implements Parcelable {
    public static final Parcelable.Creator<InputMethodInfo> CREATOR = new Parcelable.Creator<InputMethodInfo>() { // from class: android.view.inputmethod.InputMethodInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public InputMethodInfo mo3559createFromParcel(Parcel source) {
            return new InputMethodInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public InputMethodInfo[] mo3560newArray(int size) {
            return new InputMethodInfo[size];
        }
    };
    static final String TAG = "InputMethodInfo";
    private final boolean mForceDefault;
    private final int mHandledConfigChanges;
    final String mId;
    private final boolean mInlineSuggestionsEnabled;
    private final boolean mIsAuxIme;
    final int mIsDefaultResId;
    final boolean mIsVrOnly;
    final ResolveInfo mService;
    final String mSettingsActivityName;
    private final boolean mShowInInputMethodPicker;
    private final InputMethodSubtypeArray mSubtypes;
    private final boolean mSupportsSwitchingToNextInputMethod;
    private final boolean mSuppressesSpellChecker;

    public static String computeId(ResolveInfo service) {
        ServiceInfo si = service.serviceInfo;
        return new ComponentName(si.packageName, si.name).flattenToShortString();
    }

    public InputMethodInfo(Context context, ResolveInfo service) throws XmlPullParserException, IOException {
        this(context, service, null);
    }

    /* JADX WARN: Not initialized variable reg: 17, insn: 0x0264: MOVE  (r5 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('isAuxIme' boolean)]), block:B:115:0x0264 */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x026a: MOVE  (r5 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('isAuxIme' boolean)]), block:B:113:0x026a */
    /* JADX WARN: Not initialized variable reg: 21, insn: 0x0266: MOVE  (r6 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r21 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('supportsSwitchingToNextInputMethod' boolean)]), block:B:115:0x0264 */
    /* JADX WARN: Not initialized variable reg: 21, insn: 0x026c: MOVE  (r6 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r21 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('supportsSwitchingToNextInputMethod' boolean)]), block:B:113:0x026a */
    /* JADX WARN: Removed duplicated region for block: B:86:0x029a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public InputMethodInfo(Context context, ResolveInfo service, List<InputMethodSubtype> additionalSubtypes) throws XmlPullParserException, IOException {
        int type;
        this.mService = service;
        ServiceInfo si = service.serviceInfo;
        this.mId = computeId(service);
        this.mForceDefault = false;
        PackageManager pm = context.getPackageManager();
        XmlResourceParser parser = null;
        ArrayList<InputMethodSubtype> subtypes = new ArrayList<>();
        try {
            parser = si.loadXmlMetaData(pm, InputMethod.SERVICE_META_DATA);
            try {
                if (parser == null) {
                    throw new XmlPullParserException("No android.view.im meta-data");
                }
                Resources res = pm.getResourcesForApplication(si.applicationInfo);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                while (true) {
                    int type2 = parser.next();
                    if (type2 == 1) {
                        type = type2;
                        break;
                    }
                    type = type2;
                    if (type == 2) {
                        break;
                    }
                }
                String nodeName = parser.getName();
                if (!"input-method".equals(nodeName)) {
                    throw new XmlPullParserException("Meta-data does not start with input-method tag");
                }
                AttributeSet attrs2 = attrs;
                TypedArray sa = res.obtainAttributes(attrs2, R.styleable.InputMethod);
                boolean isAuxIme = true;
                try {
                    String settingsActivityComponent = sa.getString(2);
                    boolean supportsSwitchingToNextInputMethod = false;
                    try {
                        boolean isVrOnly = sa.getBoolean(4, false);
                        int isDefaultResId = sa.getResourceId(1, 0);
                        supportsSwitchingToNextInputMethod = sa.getBoolean(3, false);
                        try {
                            boolean inlineSuggestionsEnabled = sa.getBoolean(5, false);
                            boolean suppressesSpellChecker = sa.getBoolean(6, false);
                            boolean showInInputMethodPicker = sa.getBoolean(7, true);
                            this.mHandledConfigChanges = sa.getInt(0, 0);
                            sa.recycle();
                            int depth = parser.getDepth();
                            while (true) {
                                int type3 = parser.next();
                                TypedArray sa2 = sa;
                                if (type3 == 3) {
                                    try {
                                        if (parser.getDepth() <= depth) {
                                            break;
                                        }
                                    } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e) {
                                        try {
                                            throw new XmlPullParserException("Unable to create context for: " + si.packageName);
                                        } catch (Throwable th) {
                                            e = th;
                                            if (parser != null) {
                                                parser.close();
                                            }
                                            throw e;
                                        }
                                    } catch (Throwable th2) {
                                        e = th2;
                                        if (parser != null) {
                                        }
                                        throw e;
                                    }
                                }
                                if (type3 == 1) {
                                    break;
                                } else if (type3 == 2) {
                                    String nodeName2 = parser.getName();
                                    if (!"subtype".equals(nodeName2)) {
                                        throw new XmlPullParserException("Meta-data in input-method does not start with subtype tag");
                                    }
                                    TypedArray a = res.obtainAttributes(attrs2, R.styleable.InputMethod_Subtype);
                                    AttributeSet attrs3 = attrs2;
                                    int depth2 = depth;
                                    PackageManager pm2 = pm;
                                    InputMethodSubtype subtype = new InputMethodSubtype.InputMethodSubtypeBuilder().setSubtypeNameResId(a.getResourceId(0, 0)).setSubtypeIconResId(a.getResourceId(1, 0)).setLanguageTag(a.getString(9)).setSubtypeLocale(a.getString(2)).setSubtypeMode(a.getString(3)).setSubtypeExtraValue(a.getString(4)).setIsAuxiliary(a.getBoolean(5, false)).setOverridesImplicitlyEnabledSubtype(a.getBoolean(6, false)).setSubtypeId(a.getInt(7, 0)).setIsAsciiCapable(a.getBoolean(8, false)).build();
                                    isAuxIme = !subtype.isAuxiliary() ? false : isAuxIme;
                                    subtypes.add(subtype);
                                    pm = pm2;
                                    attrs2 = attrs3;
                                    sa = sa2;
                                    depth = depth2;
                                } else {
                                    sa = sa2;
                                    depth = depth;
                                }
                            }
                            if (parser != null) {
                                parser.close();
                            }
                            boolean isAuxIme2 = subtypes.size() == 0 ? false : isAuxIme;
                            if (additionalSubtypes != null) {
                                int N = additionalSubtypes.size();
                                for (int i = 0; i < N; i++) {
                                    InputMethodSubtype subtype2 = additionalSubtypes.get(i);
                                    if (!subtypes.contains(subtype2)) {
                                        subtypes.add(subtype2);
                                    } else {
                                        Slog.w(TAG, "Duplicated subtype definition found: " + subtype2.getLocale() + ", " + subtype2.getMode());
                                    }
                                }
                            }
                            this.mSubtypes = new InputMethodSubtypeArray(subtypes);
                            this.mSettingsActivityName = settingsActivityComponent;
                            this.mIsDefaultResId = isDefaultResId;
                            this.mIsAuxIme = isAuxIme2;
                            this.mSupportsSwitchingToNextInputMethod = supportsSwitchingToNextInputMethod;
                            this.mInlineSuggestionsEnabled = inlineSuggestionsEnabled;
                            this.mSuppressesSpellChecker = suppressesSpellChecker;
                            this.mShowInInputMethodPicker = showInInputMethodPicker;
                            this.mIsVrOnly = isVrOnly;
                        } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e2) {
                        } catch (Throwable th3) {
                            e = th3;
                        }
                    } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e3) {
                    } catch (Throwable th4) {
                        e = th4;
                    }
                } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e4) {
                } catch (Throwable th5) {
                    e = th5;
                }
            } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e5) {
            } catch (Throwable th6) {
                e = th6;
            }
        } catch (PackageManager.NameNotFoundException | IndexOutOfBoundsException | NumberFormatException e6) {
        } catch (Throwable th7) {
            e = th7;
        }
    }

    InputMethodInfo(Parcel source) {
        this.mId = source.readString();
        this.mSettingsActivityName = source.readString();
        this.mIsDefaultResId = source.readInt();
        boolean z = true;
        this.mIsAuxIme = source.readInt() == 1;
        this.mSupportsSwitchingToNextInputMethod = source.readInt() == 1;
        this.mInlineSuggestionsEnabled = source.readInt() != 1 ? false : z;
        this.mSuppressesSpellChecker = source.readBoolean();
        this.mShowInInputMethodPicker = source.readBoolean();
        this.mIsVrOnly = source.readBoolean();
        this.mService = ResolveInfo.CREATOR.mo3559createFromParcel(source);
        this.mSubtypes = new InputMethodSubtypeArray(source);
        this.mHandledConfigChanges = source.readInt();
        this.mForceDefault = false;
    }

    public InputMethodInfo(String packageName, String className, CharSequence label, String settingsActivity) {
        this(buildFakeResolveInfo(packageName, className, label), false, settingsActivity, null, 0, false, true, false, false, 0);
    }

    public InputMethodInfo(String packageName, String className, CharSequence label, String settingsActivity, int handledConfigChanges) {
        this(buildFakeResolveInfo(packageName, className, label), false, settingsActivity, null, 0, false, true, false, false, handledConfigChanges);
    }

    public InputMethodInfo(ResolveInfo ri, boolean isAuxIme, String settingsActivity, List<InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault) {
        this(ri, isAuxIme, settingsActivity, subtypes, isDefaultResId, forceDefault, true, false, false, 0);
    }

    public InputMethodInfo(ResolveInfo ri, boolean isAuxIme, String settingsActivity, List<InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault, boolean supportsSwitchingToNextInputMethod, boolean isVrOnly) {
        this(ri, isAuxIme, settingsActivity, subtypes, isDefaultResId, forceDefault, supportsSwitchingToNextInputMethod, false, isVrOnly, 0);
    }

    public InputMethodInfo(ResolveInfo ri, boolean isAuxIme, String settingsActivity, List<InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault, boolean supportsSwitchingToNextInputMethod, boolean inlineSuggestionsEnabled, boolean isVrOnly, int handledConfigChanges) {
        ServiceInfo si = ri.serviceInfo;
        this.mService = ri;
        this.mId = new ComponentName(si.packageName, si.name).flattenToShortString();
        this.mSettingsActivityName = settingsActivity;
        this.mIsDefaultResId = isDefaultResId;
        this.mIsAuxIme = isAuxIme;
        this.mSubtypes = new InputMethodSubtypeArray(subtypes);
        this.mForceDefault = forceDefault;
        this.mSupportsSwitchingToNextInputMethod = supportsSwitchingToNextInputMethod;
        this.mInlineSuggestionsEnabled = inlineSuggestionsEnabled;
        this.mSuppressesSpellChecker = false;
        this.mShowInInputMethodPicker = true;
        this.mIsVrOnly = isVrOnly;
        this.mHandledConfigChanges = handledConfigChanges;
    }

    private static ResolveInfo buildFakeResolveInfo(String packageName, String className, CharSequence label) {
        ResolveInfo ri = new ResolveInfo();
        ServiceInfo si = new ServiceInfo();
        ApplicationInfo ai = new ApplicationInfo();
        ai.packageName = packageName;
        ai.enabled = true;
        si.applicationInfo = ai;
        si.enabled = true;
        si.packageName = packageName;
        si.name = className;
        si.exported = true;
        si.nonLocalizedLabel = label;
        ri.serviceInfo = si;
        return ri;
    }

    public String getId() {
        return this.mId;
    }

    public String getPackageName() {
        return this.mService.serviceInfo.packageName;
    }

    public String getServiceName() {
        return this.mService.serviceInfo.name;
    }

    public ServiceInfo getServiceInfo() {
        return this.mService.serviceInfo;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    public String getSettingsActivity() {
        return this.mSettingsActivityName;
    }

    public boolean isVrOnly() {
        return this.mIsVrOnly;
    }

    public int getSubtypeCount() {
        return this.mSubtypes.getCount();
    }

    public InputMethodSubtype getSubtypeAt(int index) {
        return this.mSubtypes.get(index);
    }

    public int getIsDefaultResourceId() {
        return this.mIsDefaultResId;
    }

    public boolean isDefault(Context context) {
        if (this.mForceDefault) {
            return true;
        }
        try {
            if (getIsDefaultResourceId() == 0) {
                return false;
            }
            Resources res = context.createPackageContext(getPackageName(), 0).getResources();
            return res.getBoolean(getIsDefaultResourceId());
        } catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
            return false;
        }
    }

    public int getConfigChanges() {
        return this.mHandledConfigChanges;
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + "mId=" + this.mId + " mSettingsActivityName=" + this.mSettingsActivityName + " mIsVrOnly=" + this.mIsVrOnly + " mSupportsSwitchingToNextInputMethod=" + this.mSupportsSwitchingToNextInputMethod + " mInlineSuggestionsEnabled=" + this.mInlineSuggestionsEnabled + " mSuppressesSpellChecker=" + this.mSuppressesSpellChecker + " mShowInInputMethodPicker=" + this.mShowInInputMethodPicker);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("mIsDefaultResId=0x");
        sb.append(Integer.toHexString(this.mIsDefaultResId));
        pw.println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(prefix);
        sb2.append("Service:");
        pw.println(sb2.toString());
        ResolveInfo resolveInfo = this.mService;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(prefix);
        sb3.append("  ");
        resolveInfo.dump(pw, sb3.toString());
    }

    public String toString() {
        return "InputMethodInfo{" + this.mId + ", settings: " + this.mSettingsActivityName + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof InputMethodInfo)) {
            return false;
        }
        InputMethodInfo obj = (InputMethodInfo) o;
        return this.mId.equals(obj.mId);
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    public boolean isSystem() {
        return (this.mService.serviceInfo.applicationInfo.flags & 1) != 0;
    }

    public boolean isAuxiliaryIme() {
        return this.mIsAuxIme;
    }

    public boolean supportsSwitchingToNextInputMethod() {
        return this.mSupportsSwitchingToNextInputMethod;
    }

    public boolean isInlineSuggestionsEnabled() {
        return this.mInlineSuggestionsEnabled;
    }

    public boolean suppressesSpellChecker() {
        return this.mSuppressesSpellChecker;
    }

    public boolean shouldShowInInputMethodPicker() {
        return this.mShowInInputMethodPicker;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mSettingsActivityName);
        dest.writeInt(this.mIsDefaultResId);
        dest.writeInt(this.mIsAuxIme ? 1 : 0);
        dest.writeInt(this.mSupportsSwitchingToNextInputMethod ? 1 : 0);
        dest.writeInt(this.mInlineSuggestionsEnabled ? 1 : 0);
        dest.writeBoolean(this.mSuppressesSpellChecker);
        dest.writeBoolean(this.mShowInInputMethodPicker);
        dest.writeBoolean(this.mIsVrOnly);
        this.mService.writeToParcel(dest, flags);
        this.mSubtypes.writeToParcel(dest);
        dest.writeInt(this.mHandledConfigChanges);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
