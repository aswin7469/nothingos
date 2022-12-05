package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.nothingos.systemui.qs.MicModeView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyDialog.kt */
/* loaded from: classes.dex */
public final class PrivacyDialog extends SystemUIDialog {
    @NotNull
    private final View.OnClickListener clickListener;
    @NotNull
    private final String enterpriseText;
    @NotNull
    private final List<PrivacyElement> list;
    private final String phonecall;
    private ViewGroup rootView;
    @NotNull
    private final List<WeakReference<OnDialogDismissed>> dismissListeners = new ArrayList();
    @NotNull
    private final AtomicBoolean dismissed = new AtomicBoolean(false);
    private final int iconColorSolid = Utils.getColorAttrDefaultColor(getContext(), 16843827);
    @NotNull
    private final ArrayList<String> modeChoiceWhiteList = new ArrayList<String>() { // from class: com.android.systemui.privacy.PrivacyDialog$modeChoiceWhiteList$1
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            add("com.nothing.camera");
            add("com.android.soundrecorder");
            add("com.google.android.googlequicksearchbox");
        }

        @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public final /* bridge */ boolean contains(Object obj) {
            if (!(obj == null ? true : obj instanceof String)) {
                return false;
            }
            return contains((String) obj);
        }

        public /* bridge */ boolean contains(String str) {
            return super.contains((Object) str);
        }

        public /* bridge */ int getSize() {
            return super.size();
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (!(obj == null ? true : obj instanceof String)) {
                return -1;
            }
            return indexOf((String) obj);
        }

        public /* bridge */ int indexOf(String str) {
            return super.indexOf((Object) str);
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (!(obj == null ? true : obj instanceof String)) {
                return -1;
            }
            return lastIndexOf((String) obj);
        }

        public /* bridge */ int lastIndexOf(String str) {
            return super.lastIndexOf((Object) str);
        }

        @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public final /* bridge */ boolean remove(Object obj) {
            if (!(obj == null ? true : obj instanceof String)) {
                return false;
            }
            return remove((String) obj);
        }

        public /* bridge */ boolean remove(String str) {
            return super.remove((Object) str);
        }

        @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public final /* bridge */ int size() {
            return getSize();
        }
    };

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public interface OnDialogDismissed {
        void onDialogDismissed();
    }

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PrivacyType.valuesCustom().length];
            iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 1;
            iArr[PrivacyType.TYPE_MICROPHONE.ordinal()] = 2;
            iArr[PrivacyType.TYPE_LOCATION.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PrivacyDialog(@NotNull Context context, @NotNull List<PrivacyElement> list, @NotNull final Function2<? super String, ? super Integer, Unit> activityStarter) {
        super(context, R$style.PrivacyDialog);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list, "list");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        this.list = list;
        this.enterpriseText = Intrinsics.stringPlus(" ", context.getString(R$string.ongoing_privacy_dialog_enterprise));
        this.phonecall = context.getString(R$string.ongoing_privacy_dialog_phonecall);
        this.clickListener = new View.OnClickListener() { // from class: com.android.systemui.privacy.PrivacyDialog$clickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Object tag = view.getTag();
                if (tag == null) {
                    return;
                }
                PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) tag;
                activityStarter.mo1950invoke(privacyElement.getPackageName(), Integer.valueOf(privacyElement.getUserId()));
            }
        };
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() | WindowInsets.Type.statusBars());
            window.getAttributes().receiveInsetsIgnoringZOrder = true;
            window.setLayout(Math.min(window.getContext().getResources().getDisplayMetrics().widthPixels, window.getContext().getResources().getDisplayMetrics().heightPixels), -2);
            window.setGravity(17);
        }
        setContentView(R$layout.privacy_dialog);
        View requireViewById = requireViewById(R$id.root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.root)");
        this.rootView = (ViewGroup) requireViewById;
        for (PrivacyElement privacyElement : this.list) {
            if (privacyElement.getActive()) {
                ViewGroup viewGroup = this.rootView;
                if (viewGroup == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                    throw null;
                }
                viewGroup.addView(createView(privacyElement));
            }
        }
    }

    public final void addOnDismissListener(@NotNull OnDialogDismissed listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        if (this.dismissed.get()) {
            listener.onDialogDismissed();
        } else {
            this.dismissListeners.add(new WeakReference<>(listener));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStop() {
        super.onStop();
        this.dismissed.set(true);
        Iterator<WeakReference<OnDialogDismissed>> it = this.dismissListeners.iterator();
        while (it.hasNext()) {
            it.remove();
            OnDialogDismissed onDialogDismissed = it.next().get();
            if (onDialogDismissed != null) {
                onDialogDismissed.onDialogDismissed();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v14, types: [java.lang.CharSequence] */
    private final View createView(PrivacyElement privacyElement) {
        ?? concat;
        LayoutInflater from = LayoutInflater.from(getContext());
        int i = R$layout.nt_privacy_dialog_item;
        ViewGroup viewGroup = this.rootView;
        if (viewGroup != null) {
            View inflate = from.inflate(i, viewGroup, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
            ViewGroup viewGroup2 = (ViewGroup) inflate;
            LayerDrawable drawableForType = getDrawableForType(privacyElement.getType());
            int i2 = R$id.icon;
            drawableForType.findDrawableByLayerId(i2).setTint(this.iconColorSolid);
            ImageView imageView = (ImageView) viewGroup2.requireViewById(i2);
            imageView.setImageDrawable(drawableForType);
            PrivacyType type = privacyElement.getType();
            Context context = imageView.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "context");
            imageView.setContentDescription(type.getName(context));
            int stringIdForState = getStringIdForState(privacyElement.getActive());
            CharSequence applicationName = privacyElement.getPhoneCall() ? this.phonecall : privacyElement.getApplicationName();
            if (privacyElement.getEnterprise()) {
                applicationName = TextUtils.concat(applicationName, this.enterpriseText);
            }
            String string = getContext().getString(stringIdForState, applicationName);
            CharSequence attribution = privacyElement.getAttribution();
            if (attribution != null && (concat = TextUtils.concat(string, " ", getContext().getString(R$string.ongoing_privacy_dialog_attribution_text, attribution))) != 0) {
                string = concat;
            }
            int i3 = R$id.text;
            ((TextView) viewGroup2.requireViewById(i3)).setText(string);
            TextView textView = (TextView) viewGroup2.requireViewById(i3);
            textView.setTag(privacyElement);
            textView.setOnClickListener(this.clickListener);
            if (!this.modeChoiceWhiteList.contains(privacyElement.getPackageName())) {
                int i4 = WhenMappings.$EnumSwitchMapping$0[privacyElement.getType().ordinal()];
                if (i4 == 1) {
                    View requireViewById = viewGroup2.requireViewById(R$id.mode_container);
                    Objects.requireNonNull(requireViewById, "null cannot be cast to non-null type android.view.ViewGroup");
                    ViewGroup viewGroup3 = (ViewGroup) requireViewById;
                    LayoutInflater.from(getContext()).inflate(R$layout.layout_privacy_item_camera, viewGroup3, true);
                    viewGroup3.setVisibility(0);
                } else if (i4 == 2) {
                    PrivacyDialogController.MicModeInfo micModeInfo = privacyElement.getMicModeInfo();
                    if (Intrinsics.areEqual(micModeInfo == null ? null : Boolean.valueOf(micModeInfo.getShowUI()), Boolean.TRUE)) {
                        View inflate2 = LayoutInflater.from(getContext()).inflate(R$layout.layout_privacy_item_audio, (ViewGroup) null, false);
                        Objects.requireNonNull(inflate2, "null cannot be cast to non-null type com.nothingos.systemui.qs.MicModeView");
                        MicModeView micModeView = (MicModeView) inflate2;
                        micModeView.setPrivacyElement(privacyElement);
                        View requireViewById2 = viewGroup2.requireViewById(R$id.mode_container);
                        Objects.requireNonNull(requireViewById2, "null cannot be cast to non-null type android.view.ViewGroup");
                        ViewGroup viewGroup4 = (ViewGroup) requireViewById2;
                        viewGroup4.setVisibility(0);
                        viewGroup4.addView(micModeView);
                    }
                }
            }
            return viewGroup2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("rootView");
        throw null;
    }

    private final int getStringIdForState(boolean z) {
        if (z) {
            return R$string.ongoing_privacy_dialog_using_op;
        }
        return R$string.ongoing_privacy_dialog_recent_op;
    }

    private final LayerDrawable getDrawableForType(PrivacyType privacyType) {
        int i;
        Context context = getContext();
        int i2 = WhenMappings.$EnumSwitchMapping$0[privacyType.ordinal()];
        if (i2 == 1) {
            i = R$drawable.privacy_item_circle_camera;
        } else if (i2 == 2) {
            i = R$drawable.privacy_item_circle_microphone;
        } else if (i2 == 3) {
            i = R$drawable.privacy_item_circle_location;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Drawable drawable = context.getDrawable(i);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        return (LayerDrawable) drawable;
    }

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public static final class PrivacyElement {
        private final boolean active;
        @NotNull
        private final CharSequence applicationName;
        @Nullable
        private final CharSequence attribution;
        @NotNull
        private final StringBuilder builder;
        private final boolean enterprise;
        private final long lastActiveTimestamp;
        @Nullable
        private final PrivacyDialogController.MicModeInfo micModeInfo;
        @NotNull
        private final String packageName;
        private final boolean phoneCall;
        @NotNull
        private final PrivacyType type;
        private final int userId;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivacyElement)) {
                return false;
            }
            PrivacyElement privacyElement = (PrivacyElement) obj;
            return this.type == privacyElement.type && Intrinsics.areEqual(this.packageName, privacyElement.packageName) && this.userId == privacyElement.userId && Intrinsics.areEqual(this.applicationName, privacyElement.applicationName) && Intrinsics.areEqual(this.attribution, privacyElement.attribution) && this.lastActiveTimestamp == privacyElement.lastActiveTimestamp && this.active == privacyElement.active && this.enterprise == privacyElement.enterprise && this.phoneCall == privacyElement.phoneCall && Intrinsics.areEqual(this.micModeInfo, privacyElement.micModeInfo);
        }

        public int hashCode() {
            int hashCode = ((((((this.type.hashCode() * 31) + this.packageName.hashCode()) * 31) + Integer.hashCode(this.userId)) * 31) + this.applicationName.hashCode()) * 31;
            CharSequence charSequence = this.attribution;
            int i = 0;
            int hashCode2 = (((hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31) + Long.hashCode(this.lastActiveTimestamp)) * 31;
            boolean z = this.active;
            int i2 = 1;
            if (z) {
                z = true;
            }
            int i3 = z ? 1 : 0;
            int i4 = z ? 1 : 0;
            int i5 = (hashCode2 + i3) * 31;
            boolean z2 = this.enterprise;
            if (z2) {
                z2 = true;
            }
            int i6 = z2 ? 1 : 0;
            int i7 = z2 ? 1 : 0;
            int i8 = (i5 + i6) * 31;
            boolean z3 = this.phoneCall;
            if (!z3) {
                i2 = z3 ? 1 : 0;
            }
            int i9 = (i8 + i2) * 31;
            PrivacyDialogController.MicModeInfo micModeInfo = this.micModeInfo;
            if (micModeInfo != null) {
                i = micModeInfo.hashCode();
            }
            return i9 + i;
        }

        public PrivacyElement(@NotNull PrivacyType type, @NotNull String packageName, int i, @NotNull CharSequence applicationName, @Nullable CharSequence charSequence, long j, boolean z, boolean z2, boolean z3, @Nullable PrivacyDialogController.MicModeInfo micModeInfo) {
            Intrinsics.checkNotNullParameter(type, "type");
            Intrinsics.checkNotNullParameter(packageName, "packageName");
            Intrinsics.checkNotNullParameter(applicationName, "applicationName");
            this.type = type;
            this.packageName = packageName;
            this.userId = i;
            this.applicationName = applicationName;
            this.attribution = charSequence;
            this.lastActiveTimestamp = j;
            this.active = z;
            this.enterprise = z2;
            this.phoneCall = z3;
            this.micModeInfo = micModeInfo;
            StringBuilder sb = new StringBuilder("PrivacyElement(");
            this.builder = sb;
            sb.append(Intrinsics.stringPlus("type=", type.getLogName()));
            sb.append(Intrinsics.stringPlus(", packageName=", packageName));
            sb.append(Intrinsics.stringPlus(", userId=", Integer.valueOf(i)));
            sb.append(Intrinsics.stringPlus(", appName=", applicationName));
            if (charSequence != null) {
                sb.append(Intrinsics.stringPlus(", attribution=", charSequence));
            }
            sb.append(Intrinsics.stringPlus(", lastActive=", Long.valueOf(j)));
            if (z) {
                sb.append(", active");
            }
            if (z2) {
                sb.append(", enterprise");
            }
            if (z3) {
                sb.append(", phoneCall");
            }
            sb.append(")");
        }

        @NotNull
        public final PrivacyType getType() {
            return this.type;
        }

        @NotNull
        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUserId() {
            return this.userId;
        }

        @NotNull
        public final CharSequence getApplicationName() {
            return this.applicationName;
        }

        @Nullable
        public final CharSequence getAttribution() {
            return this.attribution;
        }

        public final long getLastActiveTimestamp() {
            return this.lastActiveTimestamp;
        }

        public final boolean getActive() {
            return this.active;
        }

        public final boolean getEnterprise() {
            return this.enterprise;
        }

        public final boolean getPhoneCall() {
            return this.phoneCall;
        }

        @Nullable
        public final PrivacyDialogController.MicModeInfo getMicModeInfo() {
            return this.micModeInfo;
        }

        @NotNull
        public String toString() {
            String sb = this.builder.toString();
            Intrinsics.checkNotNullExpressionValue(sb, "builder.toString()");
            return sb;
        }
    }
}
