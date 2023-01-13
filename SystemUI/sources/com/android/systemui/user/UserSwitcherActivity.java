package com.android.systemui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.LifecycleActivity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(mo65042d1 = {"\u0000w\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0010\u0018\u00002\u00020\u0001:\u0001,B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\b\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#H\u0007J\b\u0010%\u001a\u00020!H\u0002J\b\u0010&\u001a\u00020!H\u0016J\u0012\u0010'\u001a\u00020!2\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\b\u0010*\u001a\u00020!H\u0014J\b\u0010+\u001a\u00020!H\u0002R\u0010\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0004\n\u0002\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X.¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u0006-"}, mo65043d2 = {"Lcom/android/systemui/user/UserSwitcherActivity;", "Lcom/android/systemui/util/LifecycleActivity;", "userSwitcherController", "Lcom/android/systemui/statusbar/policy/UserSwitcherController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "layoutInflater", "Landroid/view/LayoutInflater;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "userManager", "Landroid/os/UserManager;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "(Lcom/android/systemui/statusbar/policy/UserSwitcherController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Landroid/view/LayoutInflater;Lcom/android/systemui/plugins/FalsingManager;Landroid/os/UserManager;Lcom/android/systemui/settings/UserTracker;)V", "adapter", "com/android/systemui/user/UserSwitcherActivity$adapter$1", "Lcom/android/systemui/user/UserSwitcherActivity$adapter$1;", "addButton", "Landroid/view/View;", "addUserRecords", "", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$UserRecord;", "broadcastReceiver", "Landroid/content/BroadcastReceiver;", "manageUserRecord", "parent", "Landroid/view/ViewGroup;", "popupMenu", "Lcom/android/systemui/user/UserSwitcherPopupMenu;", "userSwitchedCallback", "Lcom/android/systemui/settings/UserTracker$Callback;", "buildUserViews", "", "getMaxColumns", "", "userCount", "initBroadcastReceiver", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "showPopupMenu", "ItemAdapter", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity extends LifecycleActivity {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    /* access modifiers changed from: private */
    public final UserSwitcherActivity$adapter$1 adapter;
    private View addButton;
    private List<UserSwitcherController.UserRecord> addUserRecords;
    private final BroadcastDispatcher broadcastDispatcher;
    private BroadcastReceiver broadcastReceiver;
    private final FalsingManager falsingManager;
    /* access modifiers changed from: private */
    public final LayoutInflater layoutInflater;
    /* access modifiers changed from: private */
    public final UserSwitcherController.UserRecord manageUserRecord;
    private ViewGroup parent;
    private UserSwitcherPopupMenu popupMenu;
    /* access modifiers changed from: private */
    public final UserManager userManager;
    private final UserTracker.Callback userSwitchedCallback;
    /* access modifiers changed from: private */
    public final UserSwitcherController userSwitcherController;
    private final UserTracker userTracker;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @Inject
    public UserSwitcherActivity(UserSwitcherController userSwitcherController2, BroadcastDispatcher broadcastDispatcher2, LayoutInflater layoutInflater2, FalsingManager falsingManager2, UserManager userManager2, UserTracker userTracker2) {
        Intrinsics.checkNotNullParameter(userSwitcherController2, "userSwitcherController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(userManager2, "userManager");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        this.userSwitcherController = userSwitcherController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.layoutInflater = layoutInflater2;
        this.falsingManager = falsingManager2;
        this.userManager = userManager2;
        this.userTracker = userTracker2;
        this.addUserRecords = new ArrayList();
        this.userSwitchedCallback = new UserSwitcherActivity$userSwitchedCallback$1(this);
        this.manageUserRecord = new UserSwitcherController.UserRecord((UserInfo) null, (Bitmap) null, false, false, false, false, false, false);
        this.adapter = new UserSwitcherActivity$adapter$1(this, userSwitcherController2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C1894R.layout.user_switcher_fullscreen);
        getWindow().getDecorView().setSystemUiVisibility(770);
        View requireViewById = requireViewById(C1894R.C1898id.user_switcher_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou…(R.id.user_switcher_root)");
        this.parent = (ViewGroup) requireViewById;
        requireViewById(C1894R.C1898id.cancel).setOnClickListener(new UserSwitcherActivity$$ExternalSyntheticLambda1(this));
        View requireViewById2 = requireViewById(C1894R.C1898id.add);
        requireViewById2.setOnClickListener(new UserSwitcherActivity$$ExternalSyntheticLambda2(this));
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<View>(R.…)\n            }\n        }");
        this.addButton = requireViewById2;
        UserSwitcherController userSwitcherController2 = this.userSwitcherController;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        userSwitcherController2.init(viewGroup);
        initBroadcastReceiver();
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            viewGroup2 = viewGroup3;
        }
        viewGroup2.post(new UserSwitcherActivity$$ExternalSyntheticLambda3(this));
        UserTracker userTracker2 = this.userTracker;
        UserTracker.Callback callback = this.userSwitchedCallback;
        Executor mainExecutor = getMainExecutor();
        Intrinsics.checkNotNullExpressionValue(mainExecutor, "mainExecutor");
        userTracker2.addCallback(callback, mainExecutor);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3302onCreate$lambda1$lambda0(UserSwitcherActivity userSwitcherActivity, View view) {
        Intrinsics.checkNotNullParameter(userSwitcherActivity, "this$0");
        userSwitcherActivity.finish();
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3303onCreate$lambda3$lambda2(UserSwitcherActivity userSwitcherActivity, View view) {
        Intrinsics.checkNotNullParameter(userSwitcherActivity, "this$0");
        userSwitcherActivity.showPopupMenu();
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-4  reason: not valid java name */
    public static final void m3304onCreate$lambda4(UserSwitcherActivity userSwitcherActivity) {
        Intrinsics.checkNotNullParameter(userSwitcherActivity, "this$0");
        userSwitcherActivity.buildUserViews();
    }

    private final void showPopupMenu() {
        List arrayList = new ArrayList();
        for (UserSwitcherController.UserRecord add : this.addUserRecords) {
            arrayList.add(add);
        }
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        Context context = this;
        objectRef.element = new ItemAdapter(context, C1894R.layout.user_switcher_fullscreen_popup_item, this.layoutInflater, new UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1(this), new UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2(this));
        ((ItemAdapter) objectRef.element).addAll(arrayList);
        UserSwitcherPopupMenu userSwitcherPopupMenu = new UserSwitcherPopupMenu(context, this.falsingManager);
        View view = this.addButton;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("addButton");
            view = null;
        }
        userSwitcherPopupMenu.setAnchorView(view);
        userSwitcherPopupMenu.setAdapter((ListAdapter) objectRef.element);
        userSwitcherPopupMenu.setOnItemClickListener(new UserSwitcherActivity$$ExternalSyntheticLambda4(this, objectRef, userSwitcherPopupMenu));
        userSwitcherPopupMenu.show();
        this.popupMenu = userSwitcherPopupMenu;
    }

    /* access modifiers changed from: private */
    /* renamed from: showPopupMenu$lambda-7$lambda-6  reason: not valid java name */
    public static final void m3305showPopupMenu$lambda7$lambda6(UserSwitcherActivity userSwitcherActivity, Ref.ObjectRef objectRef, UserSwitcherPopupMenu userSwitcherPopupMenu, AdapterView adapterView, View view, int i, long j) {
        Intrinsics.checkNotNullParameter(userSwitcherActivity, "this$0");
        Intrinsics.checkNotNullParameter(objectRef, "$popupMenuAdapter");
        Intrinsics.checkNotNullParameter(userSwitcherPopupMenu, "$this_apply");
        Intrinsics.checkNotNullParameter(adapterView, "parent");
        Intrinsics.checkNotNullParameter(view, "view");
        if (!userSwitcherActivity.falsingManager.isFalseTap(1) && view.isEnabled()) {
            UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) ((ItemAdapter) objectRef.element).getItem(i - 1);
            if (Intrinsics.areEqual((Object) userRecord, (Object) userSwitcherActivity.manageUserRecord)) {
                userSwitcherActivity.startActivity(new Intent().setAction("android.settings.USER_SETTINGS"));
            } else {
                userSwitcherActivity.adapter.onUserListItemClicked(userRecord);
            }
            userSwitcherPopupMenu.dismiss();
            userSwitcherActivity.popupMenu = null;
            if (!userRecord.isAddUser) {
                userSwitcherActivity.finish();
            }
        }
    }

    /* access modifiers changed from: private */
    public final void buildUserViews() {
        ViewGroup viewGroup = this.parent;
        View view = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        int childCount = viewGroup.getChildCount();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < childCount; i3++) {
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                viewGroup2 = null;
            }
            if (Intrinsics.areEqual(viewGroup2.getChildAt(i3).getTag(), (Object) "user_view")) {
                if (i == 0) {
                    i2 = i3;
                }
                i++;
            }
        }
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup3 = null;
        }
        viewGroup3.removeViews(i2, i);
        this.addUserRecords.clear();
        Flow flow = (Flow) requireViewById(C1894R.C1898id.flow);
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup4 = null;
        }
        int width = viewGroup4.getWidth();
        int maxColumns = getMaxColumns(this.adapter.getTotalUserViews());
        int dimensionPixelSize = (width - ((maxColumns - 1) * getResources().getDimensionPixelSize(C1894R.dimen.user_switcher_fullscreen_horizontal_gap))) / maxColumns;
        flow.setMaxElementsWrap(maxColumns);
        int count = this.adapter.getCount();
        for (int i4 = 0; i4 < count; i4++) {
            UserSwitcherController.UserRecord item = this.adapter.getItem(i4);
            UserSwitcherActivity$adapter$1 userSwitcherActivity$adapter$1 = this.adapter;
            Intrinsics.checkNotNullExpressionValue(item, "item");
            if (userSwitcherActivity$adapter$1.doNotRenderUserView(item)) {
                this.addUserRecords.add(item);
            } else {
                UserSwitcherActivity$adapter$1 userSwitcherActivity$adapter$12 = this.adapter;
                ViewGroup viewGroup5 = this.parent;
                if (viewGroup5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                    viewGroup5 = null;
                }
                View view2 = userSwitcherActivity$adapter$12.getView(i4, (View) null, viewGroup5);
                ImageView imageView = (ImageView) view2.requireViewById(C1894R.C1898id.user_switcher_icon);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (dimensionPixelSize < layoutParams.width) {
                    layoutParams.width = dimensionPixelSize;
                    layoutParams.height = dimensionPixelSize;
                    imageView.setLayoutParams(layoutParams);
                }
                view2.setId(View.generateViewId());
                ViewGroup viewGroup6 = this.parent;
                if (viewGroup6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                    viewGroup6 = null;
                }
                viewGroup6.addView(view2);
                flow.addView(view2);
                view2.setOnClickListener(new UserSwitcherActivity$$ExternalSyntheticLambda0(this, item));
            }
        }
        if (!this.addUserRecords.isEmpty()) {
            this.addUserRecords.add(this.manageUserRecord);
            View view3 = this.addButton;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("addButton");
            } else {
                view = view3;
            }
            view.setVisibility(0);
            return;
        }
        View view4 = this.addButton;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("addButton");
        } else {
            view = view4;
        }
        view.setVisibility(8);
    }

    /* access modifiers changed from: private */
    /* renamed from: buildUserViews$lambda-9  reason: not valid java name */
    public static final void m3301buildUserViews$lambda9(UserSwitcherActivity userSwitcherActivity, UserSwitcherController.UserRecord userRecord, View view) {
        Intrinsics.checkNotNullParameter(userSwitcherActivity, "this$0");
        if (!userSwitcherActivity.falsingManager.isFalseTap(1) && view.isEnabled()) {
            if (!userRecord.isCurrent || userRecord.isGuest) {
                userSwitcherActivity.adapter.onUserListItemClicked(userRecord);
            }
        }
    }

    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            broadcastReceiver2 = null;
        }
        broadcastDispatcher2.unregisterReceiver(broadcastReceiver2);
        this.userTracker.removeCallback(this.userSwitchedCallback);
    }

    private final void initBroadcastReceiver() {
        this.broadcastReceiver = new UserSwitcherActivity$initBroadcastReceiver$1(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            broadcastReceiver2 = null;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, broadcastReceiver2, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    public final int getMaxColumns(int i) {
        if (i < 5) {
            return 4;
        }
        return (int) Math.ceil(((double) i) / 2.0d);
    }

    @Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BE\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\r0\n¢\u0006\u0002\u0010\u000eJ\"\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00062\b\u0010\u001b\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u001d\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\r0\n¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/user/UserSwitcherActivity$ItemAdapter;", "Landroid/widget/ArrayAdapter;", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$UserRecord;", "parentContext", "Landroid/content/Context;", "resource", "", "layoutInflater", "Landroid/view/LayoutInflater;", "textGetter", "Lkotlin/Function1;", "", "iconGetter", "Landroid/graphics/drawable/Drawable;", "(Landroid/content/Context;ILandroid/view/LayoutInflater;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getIconGetter", "()Lkotlin/jvm/functions/Function1;", "getLayoutInflater", "()Landroid/view/LayoutInflater;", "getParentContext", "()Landroid/content/Context;", "getResource", "()I", "getTextGetter", "getView", "Landroid/view/View;", "position", "convertView", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserSwitcherActivity.kt */
    private static final class ItemAdapter extends ArrayAdapter<UserSwitcherController.UserRecord> {
        private final Function1<UserSwitcherController.UserRecord, Drawable> iconGetter;
        private final LayoutInflater layoutInflater;
        private final Context parentContext;
        private final int resource;
        private final Function1<UserSwitcherController.UserRecord, String> textGetter;

        public final Context getParentContext() {
            return this.parentContext;
        }

        public final int getResource() {
            return this.resource;
        }

        public final LayoutInflater getLayoutInflater() {
            return this.layoutInflater;
        }

        public final Function1<UserSwitcherController.UserRecord, String> getTextGetter() {
            return this.textGetter;
        }

        public final Function1<UserSwitcherController.UserRecord, Drawable> getIconGetter() {
            return this.iconGetter;
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public ItemAdapter(Context context, int i, LayoutInflater layoutInflater2, Function1<? super UserSwitcherController.UserRecord, String> function1, Function1<? super UserSwitcherController.UserRecord, ? extends Drawable> function12) {
            super(context, i);
            Intrinsics.checkNotNullParameter(context, "parentContext");
            Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
            Intrinsics.checkNotNullParameter(function1, "textGetter");
            Intrinsics.checkNotNullParameter(function12, "iconGetter");
            this.parentContext = context;
            this.resource = i;
            this.layoutInflater = layoutInflater2;
            this.textGetter = function1;
            this.iconGetter = function12;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) getItem(i);
            if (view == null) {
                view = this.layoutInflater.inflate(this.resource, viewGroup, false);
            }
            Function1<UserSwitcherController.UserRecord, Drawable> function1 = this.iconGetter;
            Intrinsics.checkNotNullExpressionValue(userRecord, "item");
            ((ImageView) view.requireViewById(C1894R.C1898id.icon)).setImageDrawable(function1.invoke(userRecord));
            ((TextView) view.requireViewById(C1894R.C1898id.text)).setText(this.textGetter.invoke(userRecord));
            Intrinsics.checkNotNullExpressionValue(view, "view");
            return view;
        }
    }
}
