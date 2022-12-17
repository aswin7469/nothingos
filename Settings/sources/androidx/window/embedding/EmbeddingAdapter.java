package androidx.window.embedding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.WindowMetrics;
import androidx.window.core.ExperimentalWindowApi;
import androidx.window.core.PredicateAdapter;
import androidx.window.extensions.embedding.ActivityRule;
import androidx.window.extensions.embedding.ActivityStack;
import androidx.window.extensions.embedding.EmbeddingRule;
import androidx.window.extensions.embedding.SplitInfo;
import androidx.window.extensions.embedding.SplitPairRule;
import androidx.window.extensions.embedding.SplitPlaceholderRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: EmbeddingAdapter.kt */
public final class EmbeddingAdapter {
    @NotNull
    private final PredicateAdapter predicateAdapter;

    public EmbeddingAdapter(@NotNull PredicateAdapter predicateAdapter2) {
        Intrinsics.checkNotNullParameter(predicateAdapter2, "predicateAdapter");
        this.predicateAdapter = predicateAdapter2;
    }

    @NotNull
    public final List<SplitInfo> translate(@NotNull List<? extends SplitInfo> list) {
        Intrinsics.checkNotNullParameter(list, "splitInfoList");
        Iterable<SplitInfo> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (SplitInfo translate : iterable) {
            arrayList.add(translate(translate));
        }
        return arrayList;
    }

    private final SplitInfo translate(SplitInfo splitInfo) {
        boolean z;
        ActivityStack primaryActivityStack = splitInfo.getPrimaryActivityStack();
        Intrinsics.checkNotNullExpressionValue(primaryActivityStack, "splitInfo.primaryActivityStack");
        boolean z2 = false;
        try {
            z = primaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused) {
            z = false;
        }
        List activities = primaryActivityStack.getActivities();
        Intrinsics.checkNotNullExpressionValue(activities, "primaryActivityStack.activities");
        ActivityStack activityStack = new ActivityStack(activities, z);
        ActivityStack secondaryActivityStack = splitInfo.getSecondaryActivityStack();
        Intrinsics.checkNotNullExpressionValue(secondaryActivityStack, "splitInfo.secondaryActivityStack");
        try {
            z2 = secondaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused2) {
        }
        List activities2 = secondaryActivityStack.getActivities();
        Intrinsics.checkNotNullExpressionValue(activities2, "secondaryActivityStack.activities");
        return new SplitInfo(activityStack, new ActivityStack(activities2, z2), splitInfo.getSplitRatio());
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    private final Object translateActivityPairPredicates(Set<SplitPairFilter> set) {
        Class<Activity> cls = Activity.class;
        return this.predicateAdapter.buildPairPredicate(Reflection.getOrCreateKotlinClass(cls), Reflection.getOrCreateKotlinClass(cls), new EmbeddingAdapter$translateActivityPairPredicates$1(set));
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    private final Object translateActivityIntentPredicates(Set<SplitPairFilter> set) {
        return this.predicateAdapter.buildPairPredicate(Reflection.getOrCreateKotlinClass(Activity.class), Reflection.getOrCreateKotlinClass(Intent.class), new EmbeddingAdapter$translateActivityIntentPredicates$1(set));
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    private final Object translateParentMetricsPredicate(SplitRule splitRule) {
        return this.predicateAdapter.buildPredicate(Reflection.getOrCreateKotlinClass(WindowMetrics.class), new EmbeddingAdapter$translateParentMetricsPredicate$1(splitRule));
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    private final Object translateActivityPredicates(Set<ActivityFilter> set) {
        return this.predicateAdapter.buildPredicate(Reflection.getOrCreateKotlinClass(Activity.class), new EmbeddingAdapter$translateActivityPredicates$1(set));
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    private final Object translateIntentPredicates(Set<ActivityFilter> set) {
        return this.predicateAdapter.buildPredicate(Reflection.getOrCreateKotlinClass(Intent.class), new EmbeddingAdapter$translateIntentPredicates$1(set));
    }

    @SuppressLint({"WrongConstant"})
    private final SplitPairRule translateSplitPairRule(SplitPairRule splitPairRule, Class<?> cls) {
        SplitPairRule.Builder finishSecondaryWithPrimary = SplitPairRule.Builder.class.getConstructor(new Class[]{cls, cls, cls}).newInstance(new Object[]{translateActivityPairPredicates(splitPairRule.getFilters()), translateActivityIntentPredicates(splitPairRule.getFilters()), translateParentMetricsPredicate(splitPairRule)}).setSplitRatio(splitPairRule.getSplitRatio()).setLayoutDirection(splitPairRule.getLayoutDirection()).setShouldClearTop(splitPairRule.getClearTop()).setFinishPrimaryWithSecondary(splitPairRule.getFinishPrimaryWithSecondary()).setFinishSecondaryWithPrimary(splitPairRule.getFinishSecondaryWithPrimary());
        Intrinsics.checkNotNullExpressionValue(finishSecondaryWithPrimary, "SplitPairRuleBuilder::cl…nishSecondaryWithPrimary)");
        SplitPairRule build = finishSecondaryWithPrimary.build();
        Intrinsics.checkNotNullExpressionValue(build, "builder.build()");
        return build;
    }

    @SuppressLint({"WrongConstant"})
    private final SplitPlaceholderRule translateSplitPlaceholderRule(SplitPlaceholderRule splitPlaceholderRule, Class<?> cls) {
        SplitPlaceholderRule.Builder finishPrimaryWithSecondary = SplitPlaceholderRule.Builder.class.getConstructor(new Class[]{Intent.class, cls, cls, cls}).newInstance(new Object[]{splitPlaceholderRule.getPlaceholderIntent(), translateActivityPredicates(splitPlaceholderRule.getFilters()), translateIntentPredicates(splitPlaceholderRule.getFilters()), translateParentMetricsPredicate(splitPlaceholderRule)}).setSplitRatio(splitPlaceholderRule.getSplitRatio()).setLayoutDirection(splitPlaceholderRule.getLayoutDirection()).setSticky(splitPlaceholderRule.isSticky()).setFinishPrimaryWithSecondary(splitPlaceholderRule.getFinishPrimaryWithSecondary());
        Intrinsics.checkNotNullExpressionValue(finishPrimaryWithSecondary, "SplitPlaceholderRuleBuil…nishPrimaryWithSecondary)");
        SplitPlaceholderRule build = finishPrimaryWithSecondary.build();
        Intrinsics.checkNotNullExpressionValue(build, "builder.build()");
        return build;
    }

    private final ActivityRule translateActivityRule(ActivityRule activityRule, Class<?> cls) {
        ActivityRule build = ActivityRule.Builder.class.getConstructor(new Class[]{cls, cls}).newInstance(new Object[]{translateActivityPredicates(activityRule.getFilters()), translateIntentPredicates(activityRule.getFilters())}).setShouldAlwaysExpand(activityRule.getAlwaysExpand()).build();
        Intrinsics.checkNotNullExpressionValue(build, "ActivityRuleBuilder::cla…and)\n            .build()");
        return build;
    }

    @NotNull
    public final Set<EmbeddingRule> translate(@NotNull Set<? extends EmbeddingRule> set) {
        SplitPairRule splitPairRule;
        Intrinsics.checkNotNullParameter(set, "rules");
        Class<?> predicateClassOrNull$window_release = this.predicateAdapter.predicateClassOrNull$window_release();
        if (predicateClassOrNull$window_release == null) {
            return SetsKt__SetsKt.emptySet();
        }
        Iterable<EmbeddingRule> iterable = set;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (EmbeddingRule embeddingRule : iterable) {
            if (embeddingRule instanceof SplitPairRule) {
                splitPairRule = translateSplitPairRule((SplitPairRule) embeddingRule, predicateClassOrNull$window_release);
            } else if (embeddingRule instanceof SplitPlaceholderRule) {
                splitPairRule = translateSplitPlaceholderRule((SplitPlaceholderRule) embeddingRule, predicateClassOrNull$window_release);
            } else if (embeddingRule instanceof ActivityRule) {
                splitPairRule = translateActivityRule((ActivityRule) embeddingRule, predicateClassOrNull$window_release);
            } else {
                throw new IllegalArgumentException("Unsupported rule type");
            }
            arrayList.add((EmbeddingRule) splitPairRule);
        }
        return CollectionsKt___CollectionsKt.toSet(arrayList);
    }
}
