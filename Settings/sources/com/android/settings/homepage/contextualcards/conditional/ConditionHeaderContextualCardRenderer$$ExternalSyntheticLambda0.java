package com.android.settings.homepage.contextualcards.conditional;

import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionHeaderContextualCardRenderer;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ConditionHeaderContextualCardRenderer f$0;
    public final /* synthetic */ ConditionHeaderContextualCardRenderer.ConditionHeaderCardHolder f$1;

    public /* synthetic */ ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda0(ConditionHeaderContextualCardRenderer conditionHeaderContextualCardRenderer, ConditionHeaderContextualCardRenderer.ConditionHeaderCardHolder conditionHeaderCardHolder) {
        this.f$0 = conditionHeaderContextualCardRenderer;
        this.f$1 = conditionHeaderCardHolder;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$bindView$0(this.f$1, (ContextualCard) obj);
    }
}