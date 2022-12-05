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
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialogController {
    @Nullable
    private Drawable appIcon;
    @Nullable
    private String appName;
    @Nullable
    private Boolean appNotificationsCurrentlyEnabled;
    @Nullable
    private Integer appUid;
    @NotNull
    private final Context context;
    private ChannelEditorDialog dialog;
    @NotNull
    private final ChannelEditorDialog.Builder dialogBuilder;
    @NotNull
    private final INotificationManager noMan;
    @Nullable
    private OnChannelEditorDialogFinishedListener onFinishListener;
    @Nullable
    private NotificationInfo.OnSettingsClickListener onSettingsClickListener;
    @Nullable
    private String packageName;
    private boolean prepared;
    @NotNull
    private final List<NotificationChannel> paddedChannels = new ArrayList();
    @NotNull
    private final List<NotificationChannel> providedChannels = new ArrayList();
    @NotNull
    private final Map<NotificationChannel, Integer> edits = new LinkedHashMap();
    private boolean appNotificationsEnabled = true;
    @NotNull
    private final HashMap<String, CharSequence> groupNameLookup = new HashMap<>();
    @NotNull
    private final List<NotificationChannelGroup> channelGroupList = new ArrayList();
    private final int wmFlags = -2130444288;

    @VisibleForTesting
    public static /* synthetic */ void getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public ChannelEditorDialogController(@NotNull Context c, @NotNull INotificationManager noMan, @NotNull ChannelEditorDialog.Builder dialogBuilder) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(noMan, "noMan");
        Intrinsics.checkNotNullParameter(dialogBuilder, "dialogBuilder");
        this.noMan = noMan;
        this.dialogBuilder = dialogBuilder;
        Context applicationContext = c.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "c.applicationContext");
        this.context = applicationContext;
    }

    @Nullable
    public final OnChannelEditorDialogFinishedListener getOnFinishListener() {
        return this.onFinishListener;
    }

    public final void setOnFinishListener(@Nullable OnChannelEditorDialogFinishedListener onChannelEditorDialogFinishedListener) {
        this.onFinishListener = onChannelEditorDialogFinishedListener;
    }

    @NotNull
    public final List<NotificationChannel> getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.paddedChannels;
    }

    @NotNull
    public final HashMap<String, CharSequence> getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.groupNameLookup;
    }

    public final void prepareDialogForApp(@NotNull String appName, @NotNull String packageName, int i, @NotNull Set<NotificationChannel> channels, @NotNull Drawable appIcon, @Nullable NotificationInfo.OnSettingsClickListener onSettingsClickListener) {
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(channels, "channels");
        Intrinsics.checkNotNullParameter(appIcon, "appIcon");
        this.appName = appName;
        this.packageName = packageName;
        this.appUid = Integer.valueOf(i);
        this.appIcon = appIcon;
        boolean checkAreAppNotificationsOn = checkAreAppNotificationsOn();
        this.appNotificationsEnabled = checkAreAppNotificationsOn;
        this.onSettingsClickListener = onSettingsClickListener;
        this.appNotificationsCurrentlyEnabled = Boolean.valueOf(checkAreAppNotificationsOn);
        this.channelGroupList.clear();
        this.channelGroupList.addAll(fetchNotificationChannelGroups());
        buildGroupNameLookup();
        this.providedChannels.clear();
        this.providedChannels.addAll(channels);
        padToFourChannels(channels);
        initDialog();
        this.prepared = true;
    }

    private final void buildGroupNameLookup() {
        for (NotificationChannelGroup notificationChannelGroup : this.channelGroupList) {
            if (notificationChannelGroup.getId() != null) {
                HashMap<String, CharSequence> groupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                String id = notificationChannelGroup.getId();
                Intrinsics.checkNotNullExpressionValue(id, "group.id");
                CharSequence name = notificationChannelGroup.getName();
                Intrinsics.checkNotNullExpressionValue(name, "group.name");
                groupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core.put(id, name);
            }
        }
    }

    private final void padToFourChannels(Set<NotificationChannel> set) {
        this.paddedChannels.clear();
        CollectionsKt.addAll(this.paddedChannels, SequencesKt.take(CollectionsKt.asSequence(set), 4));
        CollectionsKt.addAll(this.paddedChannels, SequencesKt.take(SequencesKt.distinct(SequencesKt.filterNot(getDisplayableChannels(CollectionsKt.asSequence(this.channelGroupList)), new ChannelEditorDialogController$padToFourChannels$1(this))), 4 - this.paddedChannels.size()));
        if (this.paddedChannels.size() != 1 || !Intrinsics.areEqual("miscellaneous", this.paddedChannels.get(0).getId())) {
            return;
        }
        this.paddedChannels.clear();
    }

    private final Sequence<NotificationChannel> getDisplayableChannels(Sequence<NotificationChannelGroup> sequence) {
        return SequencesKt.sortedWith(SequencesKt.flatMap(sequence, ChannelEditorDialogController$getDisplayableChannels$channels$1.INSTANCE), new Comparator<T>() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$getDisplayableChannels$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                NotificationChannel notificationChannel = (NotificationChannel) t;
                CharSequence name = notificationChannel.getName();
                String str = null;
                String obj = name == null ? null : name.toString();
                if (obj == null) {
                    obj = notificationChannel.getId();
                }
                NotificationChannel notificationChannel2 = (NotificationChannel) t2;
                CharSequence name2 = notificationChannel2.getName();
                if (name2 != null) {
                    str = name2.toString();
                }
                if (str == null) {
                    str = notificationChannel2.getId();
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(obj, str);
                return compareValues;
            }
        });
    }

    public final void show() {
        if (!this.prepared) {
            throw new IllegalStateException("Must call prepareDialogForApp() before calling show()");
        }
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.show();
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    public final void close() {
        done();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void done() {
        resetState();
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.dismiss();
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
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

    @NotNull
    public final CharSequence groupNameForId(@Nullable String str) {
        CharSequence charSequence = this.groupNameLookup.get(str);
        return charSequence == null ? "" : charSequence;
    }

    public final void proposeEditForChannel(@NotNull NotificationChannel channel, int i) {
        Intrinsics.checkNotNullParameter(channel, "channel");
        if (channel.getImportance() == i) {
            this.edits.remove(channel);
        } else {
            this.edits.put(channel, Integer.valueOf(i));
        }
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.updateDoneButtonText(hasChanges());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    public final void proposeSetAppNotificationsEnabled(boolean z) {
        this.appNotificationsEnabled = z;
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.updateDoneButtonText(hasChanges());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    public final boolean areAppNotificationsEnabled() {
        return this.appNotificationsEnabled;
    }

    private final boolean hasChanges() {
        return (this.edits.isEmpty() ^ true) || !Intrinsics.areEqual(Boolean.valueOf(this.appNotificationsEnabled), this.appNotificationsCurrentlyEnabled);
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
            return list == null ? CollectionsKt.emptyList() : list;
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

    @VisibleForTesting
    public final void apply() {
        for (Map.Entry<NotificationChannel, Integer> entry : this.edits.entrySet()) {
            NotificationChannel key = entry.getKey();
            int intValue = entry.getValue().intValue();
            if (key.getImportance() != intValue) {
                setChannelImportance(key, intValue);
            }
        }
        if (!Intrinsics.areEqual(Boolean.valueOf(this.appNotificationsEnabled), this.appNotificationsCurrentlyEnabled)) {
            applyAppNotificationsOn(this.appNotificationsEnabled);
        }
    }

    @VisibleForTesting
    public final void launchSettings(@NotNull View sender) {
        Intrinsics.checkNotNullParameter(sender, "sender");
        NotificationInfo.OnSettingsClickListener onSettingsClickListener = this.onSettingsClickListener;
        if (onSettingsClickListener == null) {
            return;
        }
        Integer num = this.appUid;
        Intrinsics.checkNotNull(num);
        onSettingsClickListener.onClick(sender, null, num.intValue());
    }

    private final void initDialog() {
        this.dialogBuilder.setContext(this.context);
        ChannelEditorDialog build = this.dialogBuilder.build();
        this.dialog = build;
        if (build != null) {
            Window window = build.getWindow();
            if (window != null) {
                window.requestFeature(1);
            }
            ChannelEditorDialog channelEditorDialog = this.dialog;
            if (channelEditorDialog == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialog");
                throw null;
            }
            channelEditorDialog.setTitle(" ");
            ChannelEditorDialog channelEditorDialog2 = this.dialog;
            if (channelEditorDialog2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialog");
                throw null;
            }
            channelEditorDialog2.setContentView(R$layout.notif_half_shelf);
            channelEditorDialog2.setCanceledOnTouchOutside(true);
            channelEditorDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$1
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    OnChannelEditorDialogFinishedListener onFinishListener = ChannelEditorDialogController.this.getOnFinishListener();
                    if (onFinishListener == null) {
                        return;
                    }
                    onFinishListener.onChannelEditorDialogFinished();
                }
            });
            final ChannelEditorListView channelEditorListView = (ChannelEditorListView) channelEditorDialog2.findViewById(R$id.half_shelf_container);
            if (channelEditorListView != null) {
                channelEditorListView.setController(this);
                channelEditorListView.setAppIcon(this.appIcon);
                channelEditorListView.setAppName(this.appName);
                channelEditorListView.setChannels(getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
            }
            channelEditorDialog2.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$3
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    List<NotificationChannel> list;
                    list = ChannelEditorDialogController.this.providedChannels;
                    for (NotificationChannel notificationChannel : list) {
                        ChannelEditorListView channelEditorListView2 = channelEditorListView;
                        if (channelEditorListView2 != null) {
                            channelEditorListView2.highlightChannel(notificationChannel);
                        }
                    }
                }
            });
            TextView textView = (TextView) channelEditorDialog2.findViewById(R$id.done_button);
            if (textView != null) {
                textView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ChannelEditorDialogController.this.apply();
                        ChannelEditorDialogController.this.done();
                    }
                });
            }
            TextView textView2 = (TextView) channelEditorDialog2.findViewById(R$id.see_more_button);
            if (textView2 != null) {
                textView2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View it) {
                        ChannelEditorDialogController channelEditorDialogController = ChannelEditorDialogController.this;
                        Intrinsics.checkNotNullExpressionValue(it, "it");
                        channelEditorDialogController.launchSettings(it);
                        ChannelEditorDialogController.this.done();
                    }
                });
            }
            Window window2 = channelEditorDialog2.getWindow();
            if (window2 == null) {
                return;
            }
            window2.setBackgroundDrawable(new ColorDrawable(0));
            window2.addFlags(this.wmFlags);
            window2.setType(2017);
            window2.setWindowAnimations(16973910);
            WindowManager.LayoutParams attributes = window2.getAttributes();
            attributes.format = -3;
            attributes.setTitle(ChannelEditorDialogController.class.getSimpleName());
            attributes.gravity = 81;
            attributes.setFitInsetsTypes(window2.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
            attributes.width = -1;
            attributes.height = -2;
            Unit unit = Unit.INSTANCE;
            window2.setAttributes(attributes);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("dialog");
        throw null;
    }
}
