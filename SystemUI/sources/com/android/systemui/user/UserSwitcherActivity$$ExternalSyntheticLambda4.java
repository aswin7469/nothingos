package com.android.systemui.user;

import android.view.View;
import android.widget.AdapterView;
import kotlin.jvm.internal.Ref;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherActivity$$ExternalSyntheticLambda4 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ UserSwitcherActivity f$0;
    public final /* synthetic */ Ref.ObjectRef f$1;
    public final /* synthetic */ UserSwitcherPopupMenu f$2;

    public /* synthetic */ UserSwitcherActivity$$ExternalSyntheticLambda4(UserSwitcherActivity userSwitcherActivity, Ref.ObjectRef objectRef, UserSwitcherPopupMenu userSwitcherPopupMenu) {
        this.f$0 = userSwitcherActivity;
        this.f$1 = objectRef;
        this.f$2 = userSwitcherPopupMenu;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        UserSwitcherActivity.m3305showPopupMenu$lambda7$lambda6(this.f$0, this.f$1, this.f$2, adapterView, view, i, j);
    }
}
