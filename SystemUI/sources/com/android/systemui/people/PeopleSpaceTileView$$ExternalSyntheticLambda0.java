package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceTileView$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ LauncherApps f$0;
    public final /* synthetic */ PeopleSpaceTile f$1;

    public /* synthetic */ PeopleSpaceTileView$$ExternalSyntheticLambda0(LauncherApps launcherApps, PeopleSpaceTile peopleSpaceTile) {
        this.f$0 = launcherApps;
        this.f$1 = peopleSpaceTile;
    }

    public final void onClick(View view) {
        this.f$0.startShortcut(this.f$1.getPackageName(), this.f$1.getId(), (Rect) null, (Bundle) null, this.f$1.getUserHandle());
    }
}
