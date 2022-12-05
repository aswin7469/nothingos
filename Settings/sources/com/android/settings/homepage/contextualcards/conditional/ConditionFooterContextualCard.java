package com.android.settings.homepage.contextualcards.conditional;

import com.android.settings.homepage.contextualcards.ContextualCard;
/* loaded from: classes.dex */
public class ConditionFooterContextualCard extends ContextualCard {
    @Override // com.android.settings.homepage.contextualcards.ContextualCard
    public int getCardType() {
        return 5;
    }

    private ConditionFooterContextualCard(Builder builder) {
        super(builder);
    }

    /* loaded from: classes.dex */
    public static class Builder extends ContextualCard.Builder {
        @Override // com.android.settings.homepage.contextualcards.ContextualCard.Builder
        /* renamed from: setCardType  reason: collision with other method in class */
        public Builder mo390setCardType(int i) {
            throw new IllegalArgumentException("Cannot change card type for " + Builder.class.getName());
        }

        @Override // com.android.settings.homepage.contextualcards.ContextualCard.Builder
        /* renamed from: build  reason: collision with other method in class */
        public ConditionFooterContextualCard mo389build() {
            return new ConditionFooterContextualCard(this);
        }
    }
}
