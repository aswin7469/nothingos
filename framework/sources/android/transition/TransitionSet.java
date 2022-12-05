package android.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes3.dex */
public class TransitionSet extends Transition {
    static final int FLAG_CHANGE_EPICENTER = 8;
    private static final int FLAG_CHANGE_INTERPOLATOR = 1;
    private static final int FLAG_CHANGE_PATH_MOTION = 4;
    private static final int FLAG_CHANGE_PROPAGATION = 2;
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    int mCurrentListeners;
    ArrayList<Transition> mTransitions = new ArrayList<>();
    private boolean mPlayTogether = true;
    boolean mStarted = false;
    private int mChangeFlags = 0;

    public TransitionSet() {
    }

    public TransitionSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransitionSet);
        int ordering = a.getInt(0, 0);
        setOrdering(ordering);
        a.recycle();
    }

    public TransitionSet setOrdering(int ordering) {
        switch (ordering) {
            case 0:
                this.mPlayTogether = true;
                break;
            case 1:
                this.mPlayTogether = false;
                break;
            default:
                throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + ordering);
        }
        return this;
    }

    public int getOrdering() {
        return !this.mPlayTogether ? 1 : 0;
    }

    public TransitionSet addTransition(Transition transition) {
        if (transition != null) {
            addTransitionInternal(transition);
            if (this.mDuration >= 0) {
                transition.mo2760setDuration(this.mDuration);
            }
            if ((this.mChangeFlags & 1) != 0) {
                transition.mo2761setInterpolator(getInterpolator());
            }
            if ((this.mChangeFlags & 2) != 0) {
                transition.setPropagation(getPropagation());
            }
            if ((this.mChangeFlags & 4) != 0) {
                transition.setPathMotion(getPathMotion());
            }
            if ((this.mChangeFlags & 8) != 0) {
                transition.setEpicenterCallback(getEpicenterCallback());
            }
        }
        return this;
    }

    private void addTransitionInternal(Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    public Transition getTransitionAt(int index) {
        if (index < 0 || index >= this.mTransitions.size()) {
            return null;
        }
        return this.mTransitions.get(index);
    }

    @Override // android.transition.Transition
    /* renamed from: setDuration  reason: collision with other method in class */
    public TransitionSet mo2760setDuration(long duration) {
        ArrayList<Transition> arrayList;
        super.mo2760setDuration(duration);
        if (this.mDuration >= 0 && (arrayList = this.mTransitions) != null) {
            int numTransitions = arrayList.size();
            for (int i = 0; i < numTransitions; i++) {
                this.mTransitions.get(i).mo2760setDuration(duration);
            }
        }
        return this;
    }

    @Override // android.transition.Transition
    /* renamed from: setStartDelay  reason: collision with other method in class */
    public TransitionSet mo2763setStartDelay(long startDelay) {
        return (TransitionSet) super.mo2763setStartDelay(startDelay);
    }

    @Override // android.transition.Transition
    /* renamed from: setInterpolator  reason: collision with other method in class */
    public TransitionSet mo2761setInterpolator(TimeInterpolator interpolator) {
        this.mChangeFlags |= 1;
        ArrayList<Transition> arrayList = this.mTransitions;
        if (arrayList != null) {
            int numTransitions = arrayList.size();
            for (int i = 0; i < numTransitions; i++) {
                this.mTransitions.get(i).mo2761setInterpolator(interpolator);
            }
        }
        return (TransitionSet) super.mo2761setInterpolator(interpolator);
    }

    @Override // android.transition.Transition
    /* renamed from: addTarget  reason: collision with other method in class */
    public TransitionSet mo2751addTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2751addTarget(target);
        }
        return (TransitionSet) super.mo2751addTarget(target);
    }

    @Override // android.transition.Transition
    /* renamed from: addTarget  reason: collision with other method in class */
    public TransitionSet mo2750addTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2750addTarget(targetId);
        }
        return (TransitionSet) super.mo2750addTarget(targetId);
    }

    @Override // android.transition.Transition
    /* renamed from: addTarget  reason: collision with other method in class */
    public TransitionSet mo2753addTarget(String targetName) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2753addTarget(targetName);
        }
        return (TransitionSet) super.mo2753addTarget(targetName);
    }

    @Override // android.transition.Transition
    /* renamed from: addTarget  reason: collision with other method in class */
    public TransitionSet mo2752addTarget(Class targetType) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2752addTarget(targetType);
        }
        return (TransitionSet) super.mo2752addTarget(targetType);
    }

    @Override // android.transition.Transition
    /* renamed from: addListener  reason: collision with other method in class */
    public TransitionSet mo2749addListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.mo2749addListener(listener);
    }

    @Override // android.transition.Transition
    /* renamed from: removeTarget  reason: collision with other method in class */
    public TransitionSet mo2756removeTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2756removeTarget(targetId);
        }
        return (TransitionSet) super.mo2756removeTarget(targetId);
    }

    @Override // android.transition.Transition
    /* renamed from: removeTarget  reason: collision with other method in class */
    public TransitionSet mo2757removeTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2757removeTarget(target);
        }
        return (TransitionSet) super.mo2757removeTarget(target);
    }

    @Override // android.transition.Transition
    /* renamed from: removeTarget  reason: collision with other method in class */
    public TransitionSet mo2758removeTarget(Class target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2758removeTarget(target);
        }
        return (TransitionSet) super.mo2758removeTarget(target);
    }

    @Override // android.transition.Transition
    /* renamed from: removeTarget  reason: collision with other method in class */
    public TransitionSet mo2759removeTarget(String target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).mo2759removeTarget(target);
        }
        return (TransitionSet) super.mo2759removeTarget(target);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(View target, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(target, exclude);
        }
        return super.excludeTarget(target, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(String targetName, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetName, exclude);
        }
        return super.excludeTarget(targetName, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(int targetId, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetId, exclude);
        }
        return super.excludeTarget(targetId, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(Class type, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(type, exclude);
        }
        return super.excludeTarget(type, exclude);
    }

    @Override // android.transition.Transition
    /* renamed from: removeListener  reason: collision with other method in class */
    public TransitionSet mo2755removeListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.mo2755removeListener(listener);
    }

    @Override // android.transition.Transition
    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.mChangeFlags |= 4;
        if (this.mTransitions != null) {
            for (int i = 0; i < this.mTransitions.size(); i++) {
                this.mTransitions.get(i).setPathMotion(pathMotion);
            }
        }
    }

    public TransitionSet removeTransition(Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    private void setupStartEndListeners() {
        TransitionSetListener listener = new TransitionSetListener(this);
        Iterator<Transition> it = this.mTransitions.iterator();
        while (it.hasNext()) {
            Transition childTransition = it.next();
            childTransition.mo2749addListener(listener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class TransitionSetListener extends TransitionListenerAdapter {
        TransitionSet mTransitionSet;

        TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }

        @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            TransitionSet transitionSet = this.mTransitionSet;
            transitionSet.mCurrentListeners--;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.mo2755removeListener(this);
        }
    }

    @Override // android.transition.Transition
    protected void createAnimators(ViewGroup sceneRoot, TransitionValuesMaps startValues, TransitionValuesMaps endValues, ArrayList<TransitionValues> startValuesList, ArrayList<TransitionValues> endValuesList) {
        long startDelay = getStartDelay();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            Transition childTransition = this.mTransitions.get(i);
            if (startDelay > 0 && (this.mPlayTogether || i == 0)) {
                long childStartDelay = childTransition.getStartDelay();
                if (childStartDelay > 0) {
                    childTransition.mo2763setStartDelay(startDelay + childStartDelay);
                } else {
                    childTransition.mo2763setStartDelay(startDelay);
                }
            }
            childTransition.createAnimators(sceneRoot, startValues, endValues, startValuesList, endValuesList);
        }
    }

    @Override // android.transition.Transition
    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        int numTransitions = this.mTransitions.size();
        if (!this.mPlayTogether) {
            for (int i = 1; i < numTransitions; i++) {
                Transition previousTransition = this.mTransitions.get(i - 1);
                final Transition nextTransition = this.mTransitions.get(i);
                previousTransition.mo2749addListener(new TransitionListenerAdapter() { // from class: android.transition.TransitionSet.1
                    @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                    public void onTransitionEnd(Transition transition) {
                        nextTransition.runAnimators();
                        transition.mo2755removeListener(this);
                    }
                });
            }
            Transition firstTransition = this.mTransitions.get(0);
            if (firstTransition != null) {
                firstTransition.runAnimators();
                return;
            }
            return;
        }
        for (int i2 = 0; i2 < numTransitions; i2++) {
            this.mTransitions.get(i2).runAnimators();
        }
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition childTransition = it.next();
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureStartValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition childTransition = it.next();
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureEndValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override // android.transition.Transition
    void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    @Override // android.transition.Transition
    public void pause(View sceneRoot) {
        super.pause(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).pause(sceneRoot);
        }
    }

    @Override // android.transition.Transition
    public void resume(View sceneRoot) {
        super.resume(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).resume(sceneRoot);
        }
    }

    @Override // android.transition.Transition
    protected void cancel() {
        super.cancel();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).cancel();
        }
    }

    @Override // android.transition.Transition
    void forceToEnd(ViewGroup sceneRoot) {
        super.forceToEnd(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).forceToEnd(sceneRoot);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.transition.Transition
    /* renamed from: setSceneRoot  reason: collision with other method in class */
    public TransitionSet mo2762setSceneRoot(ViewGroup sceneRoot) {
        super.mo2762setSceneRoot(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).mo2762setSceneRoot(sceneRoot);
        }
        return this;
    }

    @Override // android.transition.Transition
    void setCanRemoveViews(boolean canRemoveViews) {
        super.setCanRemoveViews(canRemoveViews);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setCanRemoveViews(canRemoveViews);
        }
    }

    @Override // android.transition.Transition
    public void setPropagation(TransitionPropagation propagation) {
        super.setPropagation(propagation);
        this.mChangeFlags |= 2;
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setPropagation(propagation);
        }
    }

    @Override // android.transition.Transition
    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        this.mChangeFlags |= 8;
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }

    @Override // android.transition.Transition
    String toString(String indent) {
        String result = super.toString(indent);
        for (int i = 0; i < this.mTransitions.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            sb.append("\n");
            sb.append(this.mTransitions.get(i).toString(indent + "  "));
            result = sb.toString();
        }
        return result;
    }

    @Override // android.transition.Transition
    /* renamed from: clone  reason: collision with other method in class */
    public TransitionSet mo2754clone() {
        TransitionSet clone = (TransitionSet) super.m2748clone();
        clone.mTransitions = new ArrayList<>();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            clone.addTransitionInternal(this.mTransitions.get(i).m2748clone());
        }
        return clone;
    }
}
