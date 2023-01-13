package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.core.app.NotificationChannelCompat;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u00107\u001a\u000208H\u0007J\u0010\u00109\u001a\u0002082\u0006\u0010:\u001a\u00020\u000eH\u0002J\u0006\u0010;\u001a\u00020\u000eJ\b\u0010<\u001a\u000208H\u0002J\b\u0010=\u001a\u00020\u000eH\u0002J\u0006\u0010>\u001a\u000208J\b\u0010?\u001a\u000208H\u0002J\u000e\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00160AH\u0002J\u001c\u0010B\u001a\b\u0012\u0004\u0012\u00020\u001e0C2\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00160CH\u0002J\u0010\u0010E\u001a\u00020!2\b\u0010F\u001a\u0004\u0018\u00010\fJ\b\u0010G\u001a\u00020\u000eH\u0002J\b\u0010H\u001a\u000208H\u0002J\u0010\u0010I\u001a\u0002082\u0006\u0010J\u001a\u00020KH\u0007J\u0016\u0010L\u001a\u0002082\f\u0010M\u001a\b\u0012\u0004\u0012\u00020\u001e0NH\u0002J>\u0010O\u001a\u0002082\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u0010P\u001a\u00020\u00122\f\u0010M\u001a\b\u0012\u0004\u0012\u00020\u001e0N2\u0006\u0010\t\u001a\u00020\n2\b\u0010-\u001a\u0004\u0018\u00010.J\u0016\u0010Q\u001a\u0002082\u0006\u0010R\u001a\u00020\u001e2\u0006\u0010S\u001a\u00020\u0012J\u000e\u0010T\u001a\u0002082\u0006\u0010U\u001a\u00020\u000eJ\b\u0010V\u001a\u000208H\u0002J\u0018\u0010W\u001a\u0002082\u0006\u0010R\u001a\u00020\u001e2\u0006\u0010X\u001a\u00020\u0012H\u0002J\u0006\u0010Y\u001a\u000208R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0017\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00120\u001dX\u0004¢\u0006\u0002\n\u0000R8\u0010\u001f\u001a\u001e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020!0 j\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020!`\"8\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b#\u0010$\u001a\u0004\b%\u0010&R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010'\u001a\u0004\u0018\u00010(X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\"\u00100\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00158\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b1\u0010$\u001a\u0004\b2\u00103R\u000e\u00104\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0014\u00105\u001a\b\u0012\u0004\u0012\u00020\u001e0\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0012XD¢\u0006\u0002\n\u0000¨\u0006Z"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;", "", "c", "Landroid/content/Context;", "noMan", "Landroid/app/INotificationManager;", "dialogBuilder", "Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog$Builder;", "(Landroid/content/Context;Landroid/app/INotificationManager;Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog$Builder;)V", "appIcon", "Landroid/graphics/drawable/Drawable;", "appName", "", "appNotificationsCurrentlyEnabled", "", "Ljava/lang/Boolean;", "appNotificationsEnabled", "appUid", "", "Ljava/lang/Integer;", "channelGroupList", "", "Landroid/app/NotificationChannelGroup;", "context", "getContext", "()Landroid/content/Context;", "dialog", "Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog;", "edits", "", "Landroid/app/NotificationChannel;", "groupNameLookup", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "getGroupNameLookup$SystemUI_nothingRelease$annotations", "()V", "getGroupNameLookup$SystemUI_nothingRelease", "()Ljava/util/HashMap;", "onFinishListener", "Lcom/android/systemui/statusbar/notification/row/OnChannelEditorDialogFinishedListener;", "getOnFinishListener", "()Lcom/android/systemui/statusbar/notification/row/OnChannelEditorDialogFinishedListener;", "setOnFinishListener", "(Lcom/android/systemui/statusbar/notification/row/OnChannelEditorDialogFinishedListener;)V", "onSettingsClickListener", "Lcom/android/systemui/statusbar/notification/row/NotificationInfo$OnSettingsClickListener;", "packageName", "paddedChannels", "getPaddedChannels$SystemUI_nothingRelease$annotations", "getPaddedChannels$SystemUI_nothingRelease", "()Ljava/util/List;", "prepared", "providedChannels", "wmFlags", "apply", "", "applyAppNotificationsOn", "b", "areAppNotificationsEnabled", "buildGroupNameLookup", "checkAreAppNotificationsOn", "close", "done", "fetchNotificationChannelGroups", "", "getDisplayableChannels", "Lkotlin/sequences/Sequence;", "groupList", "groupNameForId", "groupId", "hasChanges", "initDialog", "launchSettings", "sender", "Landroid/view/View;", "padToFourChannels", "channels", "", "prepareDialogForApp", "uid", "proposeEditForChannel", "channel", "edit", "proposeSetAppNotificationsEnabled", "enabled", "resetState", "setChannelImportance", "importance", "show", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialogController {
    private Drawable appIcon;
    private String appName;
    private Boolean appNotificationsCurrentlyEnabled;
    private boolean appNotificationsEnabled = true;
    private Integer appUid;
    private final List<NotificationChannelGroup> channelGroupList = new ArrayList();
    private final Context context;
    private ChannelEditorDialog dialog;
    private final ChannelEditorDialog.Builder dialogBuilder;
    private final Map<NotificationChannel, Integer> edits = new LinkedHashMap();
    private final HashMap<String, CharSequence> groupNameLookup = new HashMap<>();
    private final INotificationManager noMan;
    private OnChannelEditorDialogFinishedListener onFinishListener;
    private NotificationInfo.OnSettingsClickListener onSettingsClickListener;
    private String packageName;
    private final List<NotificationChannel> paddedChannels = new ArrayList();
    private boolean prepared;
    private final List<NotificationChannel> providedChannels = new ArrayList();
    private final int wmFlags = -2130444288;

    public static /* synthetic */ void getGroupNameLookup$SystemUI_nothingRelease$annotations() {
    }

    public static /* synthetic */ void getPaddedChannels$SystemUI_nothingRelease$annotations() {
    }

    @Inject
    public ChannelEditorDialogController(Context context2, INotificationManager iNotificationManager, ChannelEditorDialog.Builder builder) {
        Intrinsics.checkNotNullParameter(context2, "c");
        Intrinsics.checkNotNullParameter(iNotificationManager, "noMan");
        Intrinsics.checkNotNullParameter(builder, "dialogBuilder");
        this.noMan = iNotificationManager;
        this.dialogBuilder = builder;
        Context applicationContext = context2.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "c.applicationContext");
        this.context = applicationContext;
    }

    public final Context getContext() {
        return this.context;
    }

    public final OnChannelEditorDialogFinishedListener getOnFinishListener() {
        return this.onFinishListener;
    }

    public final void setOnFinishListener(OnChannelEditorDialogFinishedListener onChannelEditorDialogFinishedListener) {
        this.onFinishListener = onChannelEditorDialogFinishedListener;
    }

    public final List<NotificationChannel> getPaddedChannels$SystemUI_nothingRelease() {
        return this.paddedChannels;
    }

    public final HashMap<String, CharSequence> getGroupNameLookup$SystemUI_nothingRelease() {
        return this.groupNameLookup;
    }

    public final void prepareDialogForApp(String str, String str2, int i, Set<NotificationChannel> set, Drawable drawable, NotificationInfo.OnSettingsClickListener onSettingsClickListener2) {
        Intrinsics.checkNotNullParameter(str, "appName");
        Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(set, "channels");
        Intrinsics.checkNotNullParameter(drawable, "appIcon");
        this.appName = str;
        this.packageName = str2;
        this.appUid = Integer.valueOf(i);
        this.appIcon = drawable;
        boolean checkAreAppNotificationsOn = checkAreAppNotificationsOn();
        this.appNotificationsEnabled = checkAreAppNotificationsOn;
        this.onSettingsClickListener = onSettingsClickListener2;
        this.appNotificationsCurrentlyEnabled = Boolean.valueOf(checkAreAppNotificationsOn);
        this.channelGroupList.clear();
        this.channelGroupList.addAll(fetchNotificationChannelGroups());
        buildGroupNameLookup();
        this.providedChannels.clear();
        this.providedChannels.addAll(set);
        padToFourChannels(set);
        initDialog();
        this.prepared = true;
    }

    private final void buildGroupNameLookup() {
        for (NotificationChannelGroup notificationChannelGroup : this.channelGroupList) {
            if (notificationChannelGroup.getId() != null) {
                String id = notificationChannelGroup.getId();
                Intrinsics.checkNotNullExpressionValue(id, "group.id");
                CharSequence name = notificationChannelGroup.getName();
                Intrinsics.checkNotNullExpressionValue(name, "group.name");
                this.groupNameLookup.put(id, name);
            }
        }
    }

    private final void padToFourChannels(Set<NotificationChannel> set) {
        this.paddedChannels.clear();
        CollectionsKt.addAll(this.paddedChannels, SequencesKt.take(CollectionsKt.asSequence(set), 4));
        CollectionsKt.addAll(this.paddedChannels, SequencesKt.take(SequencesKt.distinct(SequencesKt.filterNot(getDisplayableChannels(CollectionsKt.asSequence(this.channelGroupList)), new ChannelEditorDialogController$padToFourChannels$1(this))), 4 - this.paddedChannels.size()));
        if (this.paddedChannels.size() == 1 && Intrinsics.areEqual((Object) NotificationChannelCompat.DEFAULT_CHANNEL_ID, (Object) this.paddedChannels.get(0).getId())) {
            this.paddedChannels.clear();
        }
    }

    private final Sequence<NotificationChannel> getDisplayableChannels(Sequence<NotificationChannelGroup> sequence) {
        return SequencesKt.sortedWith(SequencesKt.flatMap(sequence, ChannelEditorDialogController$getDisplayableChannels$channels$1.INSTANCE), new C2745x78bbb58a());
    }

    public final void show() {
        if (this.prepared) {
            ChannelEditorDialog channelEditorDialog = this.dialog;
            if (channelEditorDialog == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialog");
                channelEditorDialog = null;
            }
            channelEditorDialog.show();
            return;
        }
        throw new IllegalStateException("Must call prepareDialogForApp() before calling show()");
    }

    public final void close() {
        done();
    }

    private final void done() {
        resetState();
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            channelEditorDialog = null;
        }
        channelEditorDialog.dismiss();
    }

    private final void resetState() {
        this.appIcon = null;
        this.appUid = null;
        this.packageName = null;
        this.appName = null;
        this.appNotificationsCurrentlyEnabled = null;
        this.edits.clear();
        this.paddedChannels.clear();
        this.providedChannels.clear();
        this.groupNameLookup.clear();
    }

    public final CharSequence groupNameForId(String str) {
        CharSequence charSequence = (CharSequence) this.groupNameLookup.get(str);
        return charSequence == null ? "" : charSequence;
    }

    public final void proposeEditForChannel(NotificationChannel notificationChannel, int i) {
        Intrinsics.checkNotNullParameter(notificationChannel, "channel");
        if (notificationChannel.getImportance() == i) {
            this.edits.remove(notificationChannel);
        } else {
            this.edits.put(notificationChannel, Integer.valueOf(i));
        }
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            channelEditorDialog = null;
        }
        channelEditorDialog.updateDoneButtonText(hasChanges());
    }

    public final void proposeSetAppNotificationsEnabled(boolean z) {
        this.appNotificationsEnabled = z;
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            channelEditorDialog = null;
        }
        channelEditorDialog.updateDoneButtonText(hasChanges());
    }

    public final boolean areAppNotificationsEnabled() {
        return this.appNotificationsEnabled;
    }

    private final boolean hasChanges() {
        return (this.edits.isEmpty() ^ true) || !Intrinsics.areEqual((Object) Boolean.valueOf(this.appNotificationsEnabled), (Object) this.appNotificationsCurrentlyEnabled);
    }

    private final List<NotificationChannelGroup> fetchNotificationChannelGroups() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            List<NotificationChannelGroup> list = iNotificationManager.getNotificationChannelGroupsForPackage(str, num.intValue(), false).getList();
            if (!(list instanceof List)) {
                list = null;
            }
            if (list == null) {
                return CollectionsKt.emptyList();
            }
            return list;
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error fetching channel groups", e);
            return CollectionsKt.emptyList();
        }
    }

    private final boolean checkAreAppNotificationsOn() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            return iNotificationManager.areNotificationsEnabledForPackage(str, num.intValue());
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
            return false;
        }
    }

    private final void applyAppNotificationsOn(boolean z) {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.setNotificationsEnabledForPackage(str, num.intValue(), z);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
        }
    }

    private final void setChannelImportance(NotificationChannel notificationChannel, int i) {
        try {
            notificationChannel.setImportance(i);
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.updateNotificationChannelForPackage(str, num.intValue(), notificationChannel);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Unable to update notification importance", e);
        }
    }

    public final void apply() {
        for (Map.Entry next : this.edits.entrySet()) {
            NotificationChannel notificationChannel = (NotificationChannel) next.getKey();
            int intValue = ((Number) next.getValue()).intValue();
            if (notificationChannel.getImportance() != intValue) {
                setChannelImportance(notificationChannel, intValue);
            }
        }
        if (!Intrinsics.areEqual((Object) Boolean.valueOf(this.appNotificationsEnabled), (Object) this.appNotificationsCurrentlyEnabled)) {
            applyAppNotificationsOn(this.appNotificationsEnabled);
        }
    }

    public final void launchSettings(View view) {
        Intrinsics.checkNotNullParameter(view, "sender");
        NotificationChannel notificationChannel = this.providedChannels.size() == 1 ? this.providedChannels.get(0) : null;
        NotificationInfo.OnSettingsClickListener onSettingsClickListener2 = this.onSettingsClickListener;
        if (onSettingsClickListener2 != null) {
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            onSettingsClickListener2.onClick(view, notificationChannel, num.intValue());
        }
    }

    private final void initDialog() {
        this.dialogBuilder.setContext(this.context);
        ChannelEditorDialog build = this.dialogBuilder.build();
        this.dialog = build;
        ChannelEditorDialog channelEditorDialog = null;
        if (build == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            build = null;
        }
        Window window = build.getWindow();
        if (window != null) {
            window.requestFeature(1);
        }
        ChannelEditorDialog channelEditorDialog2 = this.dialog;
        if (channelEditorDialog2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            channelEditorDialog2 = null;
        }
        channelEditorDialog2.setTitle(" ");
        ChannelEditorDialog channelEditorDialog3 = this.dialog;
        if (channelEditorDialog3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
        } else {
            channelEditorDialog = channelEditorDialog3;
        }
        channelEditorDialog.setContentView(C1894R.layout.notif_half_shelf);
        channelEditorDialog.setCanceledOnTouchOutside(true);
        channelEditorDialog.setOnDismissListener(new ChannelEditorDialogController$$ExternalSyntheticLambda0(this));
        ChannelEditorListView channelEditorListView = (ChannelEditorListView) channelEditorDialog.findViewById(C1894R.C1898id.half_shelf_container);
        if (channelEditorListView != null) {
            Intrinsics.checkNotNullExpressionValue(channelEditorListView, "listView");
            channelEditorListView.setController(this);
            channelEditorListView.setAppIcon(this.appIcon);
            channelEditorListView.setAppName(this.appName);
            channelEditorListView.setChannels(this.paddedChannels);
        }
        channelEditorDialog.setOnShowListener(new ChannelEditorDialogController$$ExternalSyntheticLambda1(this, channelEditorListView));
        TextView textView = (TextView) channelEditorDialog.findViewById(C1894R.C1898id.done_button);
        if (textView != null) {
            textView.setOnClickListener(new ChannelEditorDialogController$$ExternalSyntheticLambda2(this));
        }
        TextView textView2 = (TextView) channelEditorDialog.findViewById(C1894R.C1898id.see_more_button);
        if (textView2 != null) {
            textView2.setOnClickListener(new ChannelEditorDialogController$$ExternalSyntheticLambda3(this));
        }
        Window window2 = channelEditorDialog.getWindow();
        if (window2 != null) {
            Intrinsics.checkNotNullExpressionValue(window2, "window");
            window2.setBackgroundDrawable(new ColorDrawable(0));
            window2.addFlags(this.wmFlags);
            window2.setType(2017);
            window2.setWindowAnimations(16973910);
            WindowManager.LayoutParams attributes = window2.getAttributes();
            attributes.format = -3;
            attributes.setTitle("ChannelEditorDialogController");
            attributes.gravity = 81;
            attributes.setFitInsetsTypes(window2.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
            attributes.width = -1;
            attributes.height = -2;
            window2.setAttributes(attributes);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: initDialog$lambda-9$lambda-2  reason: not valid java name */
    public static final void m3138initDialog$lambda9$lambda2(ChannelEditorDialogController channelEditorDialogController, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "this$0");
        OnChannelEditorDialogFinishedListener onChannelEditorDialogFinishedListener = channelEditorDialogController.onFinishListener;
        if (onChannelEditorDialogFinishedListener != null) {
            onChannelEditorDialogFinishedListener.onChannelEditorDialogFinished();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: initDialog$lambda-9$lambda-4  reason: not valid java name */
    public static final void m3139initDialog$lambda9$lambda4(ChannelEditorDialogController channelEditorDialogController, ChannelEditorListView channelEditorListView, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "this$0");
        for (NotificationChannel next : channelEditorDialogController.providedChannels) {
            if (channelEditorListView != null) {
                channelEditorListView.highlightChannel(next);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: initDialog$lambda-9$lambda-5  reason: not valid java name */
    public static final void m3140initDialog$lambda9$lambda5(ChannelEditorDialogController channelEditorDialogController, View view) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "this$0");
        channelEditorDialogController.apply();
        channelEditorDialogController.done();
    }

    /* access modifiers changed from: private */
    /* renamed from: initDialog$lambda-9$lambda-6  reason: not valid java name */
    public static final void m3141initDialog$lambda9$lambda6(ChannelEditorDialogController channelEditorDialogController, View view) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "this$0");
        Intrinsics.checkNotNullExpressionValue(view, "it");
        channelEditorDialogController.launchSettings(view);
        channelEditorDialogController.done();
    }
}
