package com.android.settings.homepage.contextualcards;

import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ContextualCardManager$$ExternalSyntheticLambda6 implements Comparator {
    public final int compare(Object obj, Object obj2) {
        return Double.compare(((ContextualCard) obj2).getRankingScore(), ((ContextualCard) obj).getRankingScore());
    }
}
