package com.android.systemui.people.widget;

import android.app.people.ConversationChannel;
import android.content.pm.ShortcutInfo;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 INSTANCE = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6();

    private /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        ShortcutInfo shortcutInfo;
        shortcutInfo = ((ConversationChannel) obj).getShortcutInfo();
        return shortcutInfo;
    }
}
