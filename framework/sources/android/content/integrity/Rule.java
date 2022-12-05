package android.content.integrity;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
@SystemApi
/* loaded from: classes.dex */
public final class Rule implements Parcelable {
    public static final Parcelable.Creator<Rule> CREATOR = new Parcelable.Creator<Rule>() { // from class: android.content.integrity.Rule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Rule mo3559createFromParcel(Parcel in) {
            return new Rule(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Rule[] mo3560newArray(int size) {
            return new Rule[size];
        }
    };
    public static final int DENY = 0;
    public static final int FORCE_ALLOW = 1;
    private final int mEffect;
    private final IntegrityFormula mFormula;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Effect {
    }

    public Rule(IntegrityFormula formula, int effect) {
        Preconditions.checkArgument(isValidEffect(effect), "Unknown effect: %d", Integer.valueOf(effect));
        Objects.requireNonNull(formula);
        this.mFormula = formula;
        this.mEffect = effect;
    }

    Rule(Parcel in) {
        this.mFormula = IntegrityFormula.readFromParcel(in);
        this.mEffect = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        IntegrityFormula.writeToParcel(this.mFormula, dest, flags);
        dest.writeInt(this.mEffect);
    }

    public IntegrityFormula getFormula() {
        return this.mFormula;
    }

    public int getEffect() {
        return this.mEffect;
    }

    public String toString() {
        return String.format("Rule: %s, %s", this.mFormula, effectToString(this.mEffect));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule that = (Rule) o;
        if (this.mEffect == that.mEffect && Objects.equals(this.mFormula, that.mFormula)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mFormula, Integer.valueOf(this.mEffect));
    }

    private static String effectToString(int effect) {
        switch (effect) {
            case 0:
                return "DENY";
            case 1:
                return "FORCE_ALLOW";
            default:
                throw new IllegalArgumentException("Unknown effect " + effect);
        }
    }

    private static boolean isValidEffect(int effect) {
        return effect == 0 || effect == 1;
    }
}
