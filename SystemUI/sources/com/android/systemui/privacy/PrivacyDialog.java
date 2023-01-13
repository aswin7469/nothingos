package com.android.systemui.privacy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.WifiEnterpriseConfig;
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
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx;
import com.nothing.systemui.privacy.PrivacyDialogEx;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u000234BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012(\u0010\u0007\u001a$\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020\r0\b¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u0014J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0006H\u0002J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H\u0002J$\u0010(\u001a\u00020\u000b2\u0006\u0010)\u001a\u00020\u000b2\b\u0010*\u001a\u0004\u0018\u00010\u000b2\b\u0010+\u001a\u0004\u0018\u00010\u000bH\u0002J\u0010\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020.H\u0002J\u0012\u0010/\u001a\u00020\r2\b\u00100\u001a\u0004\u0018\u000101H\u0014J\b\u00102\u001a\u00020\rH\u0014R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00130\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\n \u001b*\u0004\u0018\u00010\u001a0\u001aX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n \u001b*\u0004\u0018\u00010\t0\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX.¢\u0006\u0002\n\u0000¨\u00065"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialog;", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "context", "Landroid/content/Context;", "list", "", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "activityStarter", "Lkotlin/Function4;", "", "", "", "Landroid/content/Intent;", "", "(Landroid/content/Context;Ljava/util/List;Lkotlin/jvm/functions/Function4;)V", "clickListener", "Landroid/view/View$OnClickListener;", "dismissListeners", "", "Ljava/lang/ref/WeakReference;", "Lcom/android/systemui/privacy/PrivacyDialog$OnDialogDismissed;", "dismissed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "enterpriseText", "iconColorSolid", "mEx", "Lcom/nothing/systemui/privacy/PrivacyDialogEx;", "kotlin.jvm.PlatformType", "phonecall", "rootView", "Landroid/view/ViewGroup;", "addOnDismissListener", "listener", "createView", "Landroid/view/View;", "element", "getDrawableForType", "Landroid/graphics/drawable/LayerDrawable;", "type", "Lcom/android/systemui/privacy/PrivacyType;", "getFinalText", "firstLine", "attributionLabel", "proxyLabel", "getStringIdForState", "active", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onStop", "OnDialogDismissed", "PrivacyElement", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDialog.kt */
public final class PrivacyDialog extends SystemUIDialog {
    private final View.OnClickListener clickListener;
    private final List<WeakReference<OnDialogDismissed>> dismissListeners = new ArrayList();
    private final AtomicBoolean dismissed = new AtomicBoolean(false);
    private final String enterpriseText;
    private final int iconColorSolid = Utils.getColorAttrDefaultColor(getContext(), 16843827);
    private final List<PrivacyElement> list;
    private PrivacyDialogEx mEx;
    private final String phonecall;
    private ViewGroup rootView;

    @Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialog$OnDialogDismissed;", "", "onDialogDismissed", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDialog.kt */
    public interface OnDialogDismissed {
        void onDialogDismissed();
    }

    @Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDialog.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PrivacyType.values().length];
            iArr[PrivacyType.TYPE_LOCATION.ordinal()] = 1;
            iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 2;
            iArr[PrivacyType.TYPE_MICROPHONE.ordinal()] = 3;
            iArr[PrivacyType.TYPE_MEDIA_PROJECTION.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private final int getStringIdForState(boolean z) {
        return z ? C1894R.string.ongoing_privacy_dialog_using_op : C1894R.string.ongoing_privacy_dialog_recent_op;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PrivacyDialog(Context context, List<PrivacyElement> list2, Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4) {
        super(context, (int) C1894R.style.PrivacyDialog);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list2, "list");
        Intrinsics.checkNotNullParameter(function4, "activityStarter");
        this.list = list2;
        this.enterpriseText = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + context.getString(C1894R.string.ongoing_privacy_dialog_enterprise);
        this.phonecall = context.getString(C1894R.string.ongoing_privacy_dialog_phonecall);
        this.mEx = (PrivacyDialogEx) NTDependencyEx.get(PrivacyDialogEx.class);
        this.clickListener = new PrivacyDialog$$ExternalSyntheticLambda0(function4);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() | WindowInsets.Type.statusBars());
            window.getAttributes().receiveInsetsIgnoringZOrder = true;
            window.setGravity(49);
        }
        setTitle(C1894R.string.ongoing_privacy_dialog_a11y_title);
        setContentView(C1894R.layout.privacy_dialog);
        View requireViewById = requireViewById(C1894R.C1898id.root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.root)");
        this.rootView = (ViewGroup) requireViewById;
        for (PrivacyElement privacyElement : this.list) {
            ViewGroup viewGroup = this.rootView;
            if (viewGroup == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rootView");
                viewGroup = null;
            }
            viewGroup.addView(createView(privacyElement));
        }
    }

    public final void addOnDismissListener(OnDialogDismissed onDialogDismissed) {
        Intrinsics.checkNotNullParameter(onDialogDismissed, "listener");
        if (this.dismissed.get()) {
            onDialogDismissed.onDialogDismissed();
        } else {
            this.dismissListeners.add(new WeakReference(onDialogDismissed));
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.dismissed.set(true);
        Iterator<WeakReference<OnDialogDismissed>> it = this.dismissListeners.iterator();
        while (it.hasNext()) {
            it.remove();
            OnDialogDismissed onDialogDismissed = (OnDialogDismissed) it.next().get();
            if (onDialogDismissed != null) {
                onDialogDismissed.onDialogDismissed();
            }
        }
    }

    private final View createView(PrivacyElement privacyElement) {
        LayoutInflater from = LayoutInflater.from(getContext());
        ViewGroup viewGroup = this.rootView;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
            viewGroup = null;
        }
        View inflate = from.inflate(C1894R.layout.privacy_dialog_item, viewGroup, false);
        if (inflate != null) {
            ViewGroup viewGroup2 = (ViewGroup) inflate;
            LayerDrawable drawableForType = getDrawableForType(privacyElement.getType());
            drawableForType.findDrawableByLayerId(C1894R.C1898id.icon).setTint(this.iconColorSolid);
            ImageView imageView = (ImageView) viewGroup2.requireViewById(C1894R.C1898id.icon);
            imageView.setImageDrawable(drawableForType);
            PrivacyType type = privacyElement.getType();
            Context context = imageView.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "context");
            imageView.setContentDescription(type.getName(context));
            int stringIdForState = getStringIdForState(privacyElement.getActive());
            CharSequence applicationName = privacyElement.getPhoneCall() ? this.phonecall : privacyElement.getApplicationName();
            if (privacyElement.getEnterprise()) {
                applicationName = TextUtils.concat(new CharSequence[]{applicationName, this.enterpriseText});
            }
            String string = getContext().getString(stringIdForState, new Object[]{applicationName});
            Intrinsics.checkNotNullExpressionValue(string, "firstLine");
            ((TextView) viewGroup2.requireViewById(C1894R.C1898id.text)).setText(getFinalText(string, privacyElement.getAttributionLabel(), privacyElement.getProxyLabel()));
            PrivacyDialogEx privacyDialogEx = this.mEx;
            Context context2 = getContext();
            Intrinsics.checkNotNullExpressionValue(context2, "context");
            View view = viewGroup2;
            privacyDialogEx.createView(context2, view, privacyElement, this.clickListener);
            return view;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    private final CharSequence getFinalText(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        CharSequence charSequence4;
        if (charSequence2 != null && charSequence3 != null) {
            charSequence4 = getContext().getString(C1894R.string.ongoing_privacy_dialog_attribution_proxy_label, new Object[]{charSequence2, charSequence3});
        } else if (charSequence2 != null) {
            charSequence4 = getContext().getString(C1894R.string.ongoing_privacy_dialog_attribution_label, new Object[]{charSequence2});
        } else if (charSequence3 != null) {
            charSequence4 = getContext().getString(C1894R.string.ongoing_privacy_dialog_attribution_text, new Object[]{charSequence3});
        } else {
            charSequence4 = null;
        }
        if (charSequence4 == null) {
            return charSequence;
        }
        CharSequence concat = TextUtils.concat(new CharSequence[]{charSequence, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, charSequence4});
        Intrinsics.checkNotNullExpressionValue(concat, "concat(firstLine, \" \", dialogText)");
        return concat;
    }

    private final LayerDrawable getDrawableForType(PrivacyType privacyType) {
        int i;
        Context context = getContext();
        int i2 = WhenMappings.$EnumSwitchMapping$0[privacyType.ordinal()];
        if (i2 == 1) {
            i = C1894R.C1896drawable.privacy_item_circle_location;
        } else if (i2 == 2) {
            i = C1894R.C1896drawable.privacy_item_circle_camera;
        } else if (i2 == 3) {
            i = C1894R.C1896drawable.privacy_item_circle_microphone;
        } else if (i2 == 4) {
            i = C1894R.C1896drawable.privacy_item_circle_media_projection;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Drawable drawable = context.getDrawable(i);
        if (drawable != null) {
            return (LayerDrawable) drawable;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    /* access modifiers changed from: private */
    /* renamed from: clickListener$lambda-4  reason: not valid java name */
    public static final void m2876clickListener$lambda4(Function4 function4, View view) {
        Intrinsics.checkNotNullParameter(function4, "$activityStarter");
        Object tag = view.getTag();
        if (tag != null) {
            PrivacyElement privacyElement = (PrivacyElement) tag;
            function4.invoke(privacyElement.getPackageName(), Integer.valueOf(privacyElement.getUserId()), privacyElement.getAttributionTag(), privacyElement.getNavigationIntent());
        }
    }

    @Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b$\b\b\u0018\u00002\u00020\u0001B\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\f\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0010\u0012\u0006\u0010\u0012\u001a\u00020\u0010\u0012\u0006\u0010\u0013\u001a\u00020\t\u0012\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015\u0012\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017¢\u0006\u0002\u0010\u0018J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0010HÆ\u0003J\t\u00104\u001a\u00020\u0010HÆ\u0003J\t\u00105\u001a\u00020\tHÆ\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0015HÆ\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\u0017HÆ\u0003J\t\u00108\u001a\u00020\u0005HÆ\u0003J\t\u00109\u001a\u00020\u0007HÆ\u0003J\t\u0010:\u001a\u00020\tHÆ\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\tHÆ\u0003J\t\u0010>\u001a\u00020\u000eHÆ\u0003J\t\u0010?\u001a\u00020\u0010HÆ\u0003J\u0001\u0010@\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\t2\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0017HÆ\u0001J\u0013\u0010A\u001a\u00020\u00102\b\u0010B\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010C\u001a\u00020\u0007HÖ\u0001J\b\u0010D\u001a\u00020\u0005H\u0016R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0013\u0010\n\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0012\u0010\u001f\u001a\u00060 j\u0002`!X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u0017¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u0015¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0013\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001aR\u0013\u0010\f\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b0\u00101¨\u0006E"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "", "type", "Lcom/android/systemui/privacy/PrivacyType;", "packageName", "", "userId", "", "applicationName", "", "attributionTag", "attributionLabel", "proxyLabel", "lastActiveTimestamp", "", "active", "", "enterprise", "phoneCall", "permGroupName", "navigationIntent", "Landroid/content/Intent;", "micModeInfo", "Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$MicModeInfo;", "(Lcom/android/systemui/privacy/PrivacyType;Ljava/lang/String;ILjava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;JZZZLjava/lang/CharSequence;Landroid/content/Intent;Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$MicModeInfo;)V", "getActive", "()Z", "getApplicationName", "()Ljava/lang/CharSequence;", "getAttributionLabel", "getAttributionTag", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "getEnterprise", "getLastActiveTimestamp", "()J", "getMicModeInfo", "()Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx$MicModeInfo;", "getNavigationIntent", "()Landroid/content/Intent;", "getPackageName", "()Ljava/lang/String;", "getPermGroupName", "getPhoneCall", "getProxyLabel", "getType", "()Lcom/android/systemui/privacy/PrivacyType;", "getUserId", "()I", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDialog.kt */
    public static final class PrivacyElement {
        private final boolean active;
        private final CharSequence applicationName;
        private final CharSequence attributionLabel;
        private final CharSequence attributionTag;
        private final StringBuilder builder;
        private final boolean enterprise;
        private final long lastActiveTimestamp;
        private final PrivacyDialogControllerEx.MicModeInfo micModeInfo;
        private final Intent navigationIntent;
        private final String packageName;
        private final CharSequence permGroupName;
        private final boolean phoneCall;
        private final CharSequence proxyLabel;
        private final PrivacyType type;
        private final int userId;

        public static /* synthetic */ PrivacyElement copy$default(PrivacyElement privacyElement, PrivacyType privacyType, String str, int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, long j, boolean z, boolean z2, boolean z3, CharSequence charSequence5, Intent intent, PrivacyDialogControllerEx.MicModeInfo micModeInfo2, int i2, Object obj) {
            PrivacyElement privacyElement2 = privacyElement;
            int i3 = i2;
            return privacyElement.copy((i3 & 1) != 0 ? privacyElement2.type : privacyType, (i3 & 2) != 0 ? privacyElement2.packageName : str, (i3 & 4) != 0 ? privacyElement2.userId : i, (i3 & 8) != 0 ? privacyElement2.applicationName : charSequence, (i3 & 16) != 0 ? privacyElement2.attributionTag : charSequence2, (i3 & 32) != 0 ? privacyElement2.attributionLabel : charSequence3, (i3 & 64) != 0 ? privacyElement2.proxyLabel : charSequence4, (i3 & 128) != 0 ? privacyElement2.lastActiveTimestamp : j, (i3 & 256) != 0 ? privacyElement2.active : z, (i3 & 512) != 0 ? privacyElement2.enterprise : z2, (i3 & 1024) != 0 ? privacyElement2.phoneCall : z3, (i3 & 2048) != 0 ? privacyElement2.permGroupName : charSequence5, (i3 & 4096) != 0 ? privacyElement2.navigationIntent : intent, (i3 & 8192) != 0 ? privacyElement2.micModeInfo : micModeInfo2);
        }

        public final PrivacyType component1() {
            return this.type;
        }

        public final boolean component10() {
            return this.enterprise;
        }

        public final boolean component11() {
            return this.phoneCall;
        }

        public final CharSequence component12() {
            return this.permGroupName;
        }

        public final Intent component13() {
            return this.navigationIntent;
        }

        public final PrivacyDialogControllerEx.MicModeInfo component14() {
            return this.micModeInfo;
        }

        public final String component2() {
            return this.packageName;
        }

        public final int component3() {
            return this.userId;
        }

        public final CharSequence component4() {
            return this.applicationName;
        }

        public final CharSequence component5() {
            return this.attributionTag;
        }

        public final CharSequence component6() {
            return this.attributionLabel;
        }

        public final CharSequence component7() {
            return this.proxyLabel;
        }

        public final long component8() {
            return this.lastActiveTimestamp;
        }

        public final boolean component9() {
            return this.active;
        }

        public final PrivacyElement copy(PrivacyType privacyType, String str, int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, long j, boolean z, boolean z2, boolean z3, CharSequence charSequence5, Intent intent, PrivacyDialogControllerEx.MicModeInfo micModeInfo2) {
            PrivacyType privacyType2 = privacyType;
            Intrinsics.checkNotNullParameter(privacyType2, "type");
            String str2 = str;
            Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            CharSequence charSequence6 = charSequence;
            Intrinsics.checkNotNullParameter(charSequence6, "applicationName");
            CharSequence charSequence7 = charSequence5;
            Intrinsics.checkNotNullParameter(charSequence7, "permGroupName");
            return new PrivacyElement(privacyType2, str2, i, charSequence6, charSequence2, charSequence3, charSequence4, j, z, z2, z3, charSequence7, intent, micModeInfo2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivacyElement)) {
                return false;
            }
            PrivacyElement privacyElement = (PrivacyElement) obj;
            return this.type == privacyElement.type && Intrinsics.areEqual((Object) this.packageName, (Object) privacyElement.packageName) && this.userId == privacyElement.userId && Intrinsics.areEqual((Object) this.applicationName, (Object) privacyElement.applicationName) && Intrinsics.areEqual((Object) this.attributionTag, (Object) privacyElement.attributionTag) && Intrinsics.areEqual((Object) this.attributionLabel, (Object) privacyElement.attributionLabel) && Intrinsics.areEqual((Object) this.proxyLabel, (Object) privacyElement.proxyLabel) && this.lastActiveTimestamp == privacyElement.lastActiveTimestamp && this.active == privacyElement.active && this.enterprise == privacyElement.enterprise && this.phoneCall == privacyElement.phoneCall && Intrinsics.areEqual((Object) this.permGroupName, (Object) privacyElement.permGroupName) && Intrinsics.areEqual((Object) this.navigationIntent, (Object) privacyElement.navigationIntent) && Intrinsics.areEqual((Object) this.micModeInfo, (Object) privacyElement.micModeInfo);
        }

        public int hashCode() {
            int hashCode = ((((((this.type.hashCode() * 31) + this.packageName.hashCode()) * 31) + Integer.hashCode(this.userId)) * 31) + this.applicationName.hashCode()) * 31;
            CharSequence charSequence = this.attributionTag;
            int i = 0;
            int hashCode2 = (hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
            CharSequence charSequence2 = this.attributionLabel;
            int hashCode3 = (hashCode2 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
            CharSequence charSequence3 = this.proxyLabel;
            int hashCode4 = (((hashCode3 + (charSequence3 == null ? 0 : charSequence3.hashCode())) * 31) + Long.hashCode(this.lastActiveTimestamp)) * 31;
            boolean z = this.active;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i2 = (hashCode4 + (z ? 1 : 0)) * 31;
            boolean z3 = this.enterprise;
            if (z3) {
                z3 = true;
            }
            int i3 = (i2 + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.phoneCall;
            if (!z4) {
                z2 = z4;
            }
            int hashCode5 = (((i3 + (z2 ? 1 : 0)) * 31) + this.permGroupName.hashCode()) * 31;
            Intent intent = this.navigationIntent;
            int hashCode6 = (hashCode5 + (intent == null ? 0 : intent.hashCode())) * 31;
            PrivacyDialogControllerEx.MicModeInfo micModeInfo2 = this.micModeInfo;
            if (micModeInfo2 != null) {
                i = micModeInfo2.hashCode();
            }
            return hashCode6 + i;
        }

        public PrivacyElement(PrivacyType privacyType, String str, int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, long j, boolean z, boolean z2, boolean z3, CharSequence charSequence5, Intent intent, PrivacyDialogControllerEx.MicModeInfo micModeInfo2) {
            PrivacyType privacyType2 = privacyType;
            String str2 = str;
            int i2 = i;
            CharSequence charSequence6 = charSequence;
            CharSequence charSequence7 = charSequence2;
            CharSequence charSequence8 = charSequence3;
            CharSequence charSequence9 = charSequence4;
            long j2 = j;
            boolean z4 = z;
            boolean z5 = z2;
            boolean z6 = z3;
            CharSequence charSequence10 = charSequence5;
            Intent intent2 = intent;
            Intrinsics.checkNotNullParameter(privacyType2, "type");
            Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            Intrinsics.checkNotNullParameter(charSequence6, "applicationName");
            Intrinsics.checkNotNullParameter(charSequence10, "permGroupName");
            this.type = privacyType2;
            this.packageName = str2;
            this.userId = i2;
            this.applicationName = charSequence6;
            this.attributionTag = charSequence7;
            this.attributionLabel = charSequence8;
            this.proxyLabel = charSequence9;
            this.lastActiveTimestamp = j2;
            this.active = z4;
            this.enterprise = z5;
            this.phoneCall = z6;
            this.permGroupName = charSequence10;
            this.navigationIntent = intent2;
            this.micModeInfo = micModeInfo2;
            StringBuilder sb = new StringBuilder("PrivacyElement(");
            this.builder = sb;
            sb.append("type=" + privacyType.getLogName());
            sb.append(", packageName=" + str2);
            sb.append(", userId=" + i2);
            sb.append(", appName=" + charSequence6);
            if (charSequence7 != null) {
                sb.append(", attributionTag=" + charSequence7);
            }
            if (charSequence8 != null) {
                sb.append(", attributionLabel=" + charSequence8);
            }
            if (charSequence9 != null) {
                sb.append(", proxyLabel=" + charSequence9);
            }
            sb.append(", lastActive=" + j2);
            if (z4) {
                sb.append(", active");
            }
            if (z5) {
                sb.append(", enterprise");
            }
            if (z6) {
                sb.append(", phoneCall");
            }
            sb.append(", permGroupName=" + charSequence10 + ')');
            if (intent2 != null) {
                sb.append(", navigationIntent=" + intent2);
            }
        }

        public final PrivacyType getType() {
            return this.type;
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUserId() {
            return this.userId;
        }

        public final CharSequence getApplicationName() {
            return this.applicationName;
        }

        public final CharSequence getAttributionTag() {
            return this.attributionTag;
        }

        public final CharSequence getAttributionLabel() {
            return this.attributionLabel;
        }

        public final CharSequence getProxyLabel() {
            return this.proxyLabel;
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

        public final CharSequence getPermGroupName() {
            return this.permGroupName;
        }

        public final Intent getNavigationIntent() {
            return this.navigationIntent;
        }

        public final PrivacyDialogControllerEx.MicModeInfo getMicModeInfo() {
            return this.micModeInfo;
        }

        public String toString() {
            String sb = this.builder.toString();
            Intrinsics.checkNotNullExpressionValue(sb, "builder.toString()");
            return sb;
        }
    }
}
