package androidx.constraintlayout.solver;

import java.util.Arrays;
/* loaded from: classes.dex */
public class SolverVariable {
    private static int uniqueErrorId = 1;
    public float computedValue;
    public boolean inGoal;
    private String mName;
    Type mType;
    public int id = -1;
    int definitionId = -1;
    public int strength = 0;
    float[] strengthVector = new float[8];
    float[] goalStrengthVector = new float[8];
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    public int usageInRowCount = 0;

    /* loaded from: classes.dex */
    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void increaseErrorId() {
        uniqueErrorId++;
    }

    public SolverVariable(Type type, String str) {
        this.mType = type;
    }

    public final void addToRow(ArrayRow arrayRow) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i < i2) {
                if (this.mClientEquations[i] == arrayRow) {
                    return;
                }
                i++;
            } else {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = arrayRow;
                this.mClientEquationsCount = i3 + 1;
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0019, code lost:
        r4.mClientEquationsCount--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001f, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
        if (r1 >= (r0 - 1)) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x000f, code lost:
        r5 = r4.mClientEquations;
        r2 = r1 + 1;
        r5[r1] = r5[r2];
        r1 = r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void removeFromRow(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        int i2 = 0;
        while (i2 < i) {
            if (this.mClientEquations[i2] == arrayRow) {
                break;
            }
            i2++;
        }
    }

    public final void updateReferencesWithNewDefinition(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].updateFromRow(arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.inGoal = false;
        Arrays.fill(this.goalStrengthVector, 0.0f);
    }

    public void setType(Type type, String str) {
        this.mType = type;
    }

    public String toString() {
        return "" + this.mName;
    }
}
