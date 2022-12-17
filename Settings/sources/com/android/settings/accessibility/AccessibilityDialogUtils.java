package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.icu.text.MessageFormat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$raw;
import com.android.settings.R$string;
import com.android.settings.accessibility.ItemInfoArrayAdapter;
import com.android.settings.utils.AnnotationSpan;
import java.util.List;

public class AccessibilityDialogUtils {
    public static AlertDialog showEditShortcutDialog(Context context, int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        AlertDialog createDialog = createDialog(context, i, charSequence, onClickListener);
        createDialog.show();
        setScrollIndicators(createDialog);
        return createDialog;
    }

    public static boolean updateSoftwareShortcutInDialog(Context context, Dialog dialog) {
        View findViewById = dialog.findViewById(R$id.container_layout);
        if (findViewById == null) {
            return false;
        }
        initSoftwareShortcut(context, findViewById);
        return true;
    }

    private static AlertDialog createDialog(Context context, int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setView(createEditDialogContentView(context, i)).setTitle(charSequence).setPositiveButton(R$string.save, onClickListener).setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) new AccessibilityDialogUtils$$ExternalSyntheticLambda1()).create();
    }

    private static void setScrollIndicators(AlertDialog alertDialog) {
        setScrollIndicators((View) (ScrollView) alertDialog.findViewById(R$id.container_layout));
    }

    private static void setScrollIndicators(View view) {
        view.setScrollIndicators(3, 3);
    }

    private static View createEditDialogContentView(Context context, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        if (i == 0) {
            View inflate = layoutInflater.inflate(R$layout.accessibility_edit_shortcut, (ViewGroup) null);
            initSoftwareShortcut(context, inflate);
            initHardwareShortcut(context, inflate);
            return inflate;
        } else if (i == 1) {
            View inflate2 = layoutInflater.inflate(R$layout.accessibility_edit_shortcut, (ViewGroup) null);
            initSoftwareShortcutForSUW(context, inflate2);
            initHardwareShortcut(context, inflate2);
            return inflate2;
        } else if (i == 2) {
            View inflate3 = layoutInflater.inflate(R$layout.accessibility_edit_shortcut_magnification, (ViewGroup) null);
            initSoftwareShortcut(context, inflate3);
            initHardwareShortcut(context, inflate3);
            initMagnifyShortcut(context, inflate3);
            initAdvancedWidget(inflate3);
            return inflate3;
        } else if (i == 3) {
            View inflate4 = layoutInflater.inflate(R$layout.accessibility_edit_shortcut_magnification, (ViewGroup) null);
            initSoftwareShortcutForSUW(context, inflate4);
            initHardwareShortcut(context, inflate4);
            initMagnifyShortcut(context, inflate4);
            initAdvancedWidget(inflate4);
            return inflate4;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void setupShortcutWidget(View view, CharSequence charSequence, CharSequence charSequence2, int i) {
        setupShortcutWidgetWithTitleAndSummary(view, charSequence, charSequence2);
        setupShortcutWidgetWithImageResource(view, i);
    }

    private static void setupShortcutWidgetWithImageRawResource(View view, CharSequence charSequence, CharSequence charSequence2, int i) {
        setupShortcutWidgetWithTitleAndSummary(view, charSequence, charSequence2);
        setupShortcutWidgetWithImageRawResource(view, i);
    }

    private static void setupShortcutWidgetWithTitleAndSummary(View view, CharSequence charSequence, CharSequence charSequence2) {
        ((CheckBox) view.findViewById(R$id.checkbox)).setText(charSequence);
        TextView textView = (TextView) view.findViewById(R$id.summary);
        if (TextUtils.isEmpty(charSequence2)) {
            textView.setVisibility(8);
            return;
        }
        textView.setText(charSequence2);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setFocusable(false);
    }

    private static void setupShortcutWidgetWithImageResource(View view, int i) {
        ((ImageView) view.findViewById(R$id.image)).setImageResource(i);
    }

    private static void setupShortcutWidgetWithImageRawResource(View view, int i) {
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R$id.image);
        lottieAnimationView.setFailureListener(new AccessibilityDialogUtils$$ExternalSyntheticLambda2(i));
        lottieAnimationView.setAnimation(i);
        lottieAnimationView.setRepeatCount(-1);
        lottieAnimationView.playAnimation();
    }

    private static void initSoftwareShortcutForSUW(Context context, View view) {
        View findViewById = view.findViewById(R$id.software_shortcut);
        setupShortcutWidget(findViewById, context.getText(R$string.accessibility_shortcut_edit_dialog_title_software), retrieveSoftwareShortcutSummaryForSUW(context, ((TextView) findViewById.findViewById(R$id.summary)).getLineHeight()), retrieveSoftwareShortcutImageResId(context));
    }

    private static void initSoftwareShortcut(Context context, View view) {
        View findViewById = view.findViewById(R$id.software_shortcut);
        setupShortcutWidget(findViewById, retrieveTitle(context), retrieveSoftwareShortcutSummary(context, ((TextView) findViewById.findViewById(R$id.summary)).getLineHeight()), retrieveSoftwareShortcutImageResId(context));
    }

    private static void initHardwareShortcut(Context context, View view) {
        setupShortcutWidget(view.findViewById(R$id.hardware_shortcut), context.getText(R$string.accessibility_shortcut_edit_dialog_title_hardware), context.getText(R$string.accessibility_shortcut_edit_dialog_summary_hardware), R$drawable.accessibility_shortcut_type_hardware);
    }

    private static void initMagnifyShortcut(Context context, View view) {
        setupShortcutWidgetWithImageRawResource(view.findViewById(R$id.triple_tap_shortcut), context.getText(R$string.accessibility_shortcut_edit_dialog_title_triple_tap), MessageFormat.format(context.getString(R$string.accessibility_shortcut_edit_dialog_summary_triple_tap), new Object[]{3}), R$raw.accessibility_shortcut_type_triple_tap);
    }

    private static void initAdvancedWidget(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R$id.advanced_shortcut);
        linearLayout.setOnClickListener(new AccessibilityDialogUtils$$ExternalSyntheticLambda3(linearLayout, view.findViewById(R$id.triple_tap_shortcut)));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$initAdvancedWidget$2(LinearLayout linearLayout, View view, View view2) {
        linearLayout.setVisibility(8);
        view.setVisibility(0);
    }

    private static CharSequence retrieveSoftwareShortcutSummaryForSUW(Context context, int i) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (!AccessibilityUtil.isFloatingMenuEnabled(context)) {
            spannableStringBuilder.append(getSummaryStringWithIcon(context, i));
        }
        return spannableStringBuilder;
    }

    private static CharSequence retrieveTitle(Context context) {
        int i;
        if (AccessibilityUtil.isFloatingMenuEnabled(context)) {
            i = R$string.accessibility_shortcut_edit_dialog_title_software;
        } else if (AccessibilityUtil.isGestureNavigateEnabled(context)) {
            i = R$string.accessibility_shortcut_edit_dialog_title_software_by_gesture;
        } else {
            i = R$string.accessibility_shortcut_edit_dialog_title_software;
        }
        return context.getText(i);
    }

    private static CharSequence retrieveSoftwareShortcutSummary(Context context, int i) {
        int i2;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (AccessibilityUtil.isFloatingMenuEnabled(context)) {
            spannableStringBuilder.append(getCustomizeAccessibilityButtonLink(context));
        } else if (AccessibilityUtil.isGestureNavigateEnabled(context)) {
            if (AccessibilityUtil.isTouchExploreEnabled(context)) {
                i2 = R$string.f140x9c77a93e;
            } else {
                i2 = R$string.accessibility_shortcut_edit_dialog_summary_software_gesture;
            }
            spannableStringBuilder.append(context.getText(i2));
            spannableStringBuilder.append("\n\n");
            spannableStringBuilder.append(getCustomizeAccessibilityButtonLink(context));
        } else {
            spannableStringBuilder.append(getSummaryStringWithIcon(context, i));
            spannableStringBuilder.append("\n\n");
            spannableStringBuilder.append(getCustomizeAccessibilityButtonLink(context));
        }
        return spannableStringBuilder;
    }

    private static int retrieveSoftwareShortcutImageResId(Context context) {
        if (AccessibilityUtil.isFloatingMenuEnabled(context)) {
            return R$drawable.accessibility_shortcut_type_software_floating;
        }
        if (!AccessibilityUtil.isGestureNavigateEnabled(context)) {
            return R$drawable.accessibility_shortcut_type_software;
        }
        if (AccessibilityUtil.isTouchExploreEnabled(context)) {
            return R$drawable.accessibility_shortcut_type_software_gesture_talkback;
        }
        return R$drawable.accessibility_shortcut_type_software_gesture;
    }

    private static CharSequence getCustomizeAccessibilityButtonLink(Context context) {
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo("link", new AccessibilityDialogUtils$$ExternalSyntheticLambda0(context));
        return AnnotationSpan.linkify(context.getText(R$string.accessibility_shortcut_edit_dialog_summary_software_floating), linkInfo);
    }

    private static SpannableString getSummaryStringWithIcon(Context context, int i) {
        String string = context.getString(R$string.accessibility_shortcut_edit_dialog_summary_software);
        SpannableString valueOf = SpannableString.valueOf(string);
        int indexOf = string.indexOf("%s");
        Drawable drawable = context.getDrawable(R$drawable.ic_accessibility_new);
        ImageSpan imageSpan = new ImageSpan(drawable);
        imageSpan.setContentDescription("");
        drawable.setBounds(0, 0, i, i);
        valueOf.setSpan(imageSpan, indexOf, indexOf + 2, 33);
        return valueOf;
    }

    public static Dialog createCustomDialog(Context context, CharSequence charSequence, View view, CharSequence charSequence2, DialogInterface.OnClickListener onClickListener, CharSequence charSequence3, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog create = new AlertDialog.Builder(context).setView(view).setTitle(charSequence).setCancelable(true).setPositiveButton(charSequence2, onClickListener).setNegativeButton(charSequence3, onClickListener2).create();
        if ((view instanceof ScrollView) || (view instanceof AbsListView)) {
            setScrollIndicators(view);
        }
        return create;
    }

    public static ListView createSingleChoiceListView(Context context, List<? extends ItemInfoArrayAdapter.ItemInfo> list, AdapterView.OnItemClickListener onItemClickListener) {
        ListView listView = new ListView(context);
        listView.setId(16908298);
        listView.setDivider((Drawable) null);
        listView.setChoiceMode(1);
        listView.setAdapter(new ItemInfoArrayAdapter(context, list));
        listView.setOnItemClickListener(onItemClickListener);
        return listView;
    }
}
