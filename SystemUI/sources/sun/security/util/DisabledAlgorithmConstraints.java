package sun.security.util;

import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisabledAlgorithmConstraints extends AbstractAlgorithmConstraints {
    public static final String PROPERTY_CERTPATH_DISABLED_ALGS = "jdk.certpath.disabledAlgorithms";
    public static final String PROPERTY_JAR_DISABLED_ALGS = "jdk.jar.disabledAlgorithms";
    public static final String PROPERTY_TLS_DISABLED_ALGS = "jdk.tls.disabledAlgorithms";
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("certpath");
    private final Constraints algorithmConstraints;
    private final String[] disabledAlgorithms;

    public DisabledAlgorithmConstraints(String str) {
        this(str, new AlgorithmDecomposer());
    }

    public DisabledAlgorithmConstraints(String str, AlgorithmDecomposer algorithmDecomposer) {
        super(algorithmDecomposer);
        String[] algorithms = getAlgorithms(str);
        this.disabledAlgorithms = algorithms;
        this.algorithmConstraints = new Constraints(algorithms);
    }

    public final boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters) {
        if (set != null && !set.isEmpty()) {
            return checkAlgorithm(this.disabledAlgorithms, str, this.decomposer);
        }
        throw new IllegalArgumentException("No cryptographic primitive specified");
    }

    public final boolean permits(Set<CryptoPrimitive> set, Key key) {
        return checkConstraints(set, "", key, (AlgorithmParameters) null);
    }

    public final boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        if (str != null && str.length() != 0) {
            return checkConstraints(set, str, key, algorithmParameters);
        }
        throw new IllegalArgumentException("No algorithm name specified");
    }

    public final void permits(Set<CryptoPrimitive> set, CertConstraintParameters certConstraintParameters) throws CertPathValidatorException {
        checkConstraints(set, certConstraintParameters);
    }

    public final void permits(Set<CryptoPrimitive> set, X509Certificate x509Certificate) throws CertPathValidatorException {
        checkConstraints(set, new CertConstraintParameters(x509Certificate));
    }

    public boolean checkProperty(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        for (String lowerCase2 : this.disabledAlgorithms) {
            if (lowerCase2.toLowerCase(Locale.ENGLISH).indexOf(lowerCase) >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkConstraints(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        if (key == null) {
            throw new IllegalArgumentException("The key cannot be null");
        } else if ((str == null || str.length() == 0 || permits(set, str, algorithmParameters)) && permits(set, key.getAlgorithm(), (AlgorithmParameters) null)) {
            return this.algorithmConstraints.permits(key);
        } else {
            return false;
        }
    }

    private void checkConstraints(Set<CryptoPrimitive> set, CertConstraintParameters certConstraintParameters) throws CertPathValidatorException {
        X509Certificate certificate = certConstraintParameters.getCertificate();
        String sigAlgName = certificate.getSigAlgName();
        if (!permits(set, sigAlgName, (AlgorithmParameters) null)) {
            throw new CertPathValidatorException("Algorithm constraints check failed on disabled signature algorithm: " + sigAlgName, (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        } else if (permits(set, certificate.getPublicKey().getAlgorithm(), (AlgorithmParameters) null)) {
            this.algorithmConstraints.permits(certConstraintParameters);
        } else {
            throw new CertPathValidatorException("Algorithm constraints check failed on disabled public key algorithm: " + sigAlgName, (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        }
    }

    private static class Constraints {
        private static final Pattern keySizePattern = Pattern.compile("keySize\\s*(<=|<|==|!=|>|>=)\\s*(\\d+)");
        private Map<String, Set<Constraint>> constraintsMap = new HashMap();

        public Constraints(String[] strArr) {
            String[] strArr2 = strArr;
            int length = strArr2.length;
            int i = 0;
            int i2 = 0;
            while (i2 < length) {
                String str = strArr2[i2];
                if (str != null && !str.isEmpty()) {
                    String trim = str.trim();
                    if (DisabledAlgorithmConstraints.debug != null) {
                        DisabledAlgorithmConstraints.debug.println("Constraints: " + trim);
                    }
                    int indexOf = trim.indexOf(32);
                    if (indexOf > 0) {
                        String hashName = AlgorithmDecomposer.hashName(trim.substring(i, indexOf).toUpperCase(Locale.ENGLISH));
                        String[] split = trim.substring(indexOf + 1).split("&");
                        int length2 = split.length;
                        KeySizeConstraint keySizeConstraint = null;
                        int i3 = i;
                        int i4 = i3;
                        KeySizeConstraint keySizeConstraint2 = null;
                        while (i3 < length2) {
                            String trim2 = split[i3].trim();
                            Matcher matcher = keySizePattern.matcher(trim2);
                            if (matcher.matches()) {
                                if (DisabledAlgorithmConstraints.debug != null) {
                                    DisabledAlgorithmConstraints.debug.println("Constraints set to keySize: " + trim2);
                                }
                                keySizeConstraint = new KeySizeConstraint(hashName, Constraint.Operator.m1825of(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                            } else if (trim2.equalsIgnoreCase("jdkCA")) {
                                if (DisabledAlgorithmConstraints.debug != null) {
                                    DisabledAlgorithmConstraints.debug.println("Constraints set to jdkCA.");
                                }
                                if (i4 == 0) {
                                    keySizeConstraint = new jdkCAConstraint(hashName);
                                    i4 = 1;
                                } else {
                                    throw new IllegalArgumentException("Only one jdkCA entry allowed in property. Constraint: " + trim);
                                }
                            }
                            if (keySizeConstraint2 == null) {
                                if (!this.constraintsMap.containsKey(hashName)) {
                                    this.constraintsMap.putIfAbsent(hashName, new HashSet());
                                }
                                if (keySizeConstraint != null) {
                                    this.constraintsMap.get(hashName).add(keySizeConstraint);
                                }
                            } else {
                                keySizeConstraint2.nextConstraint = keySizeConstraint;
                            }
                            i3++;
                            keySizeConstraint2 = keySizeConstraint;
                        }
                        continue;
                    } else {
                        this.constraintsMap.putIfAbsent(trim.toUpperCase(Locale.ENGLISH), new HashSet());
                    }
                }
                i2++;
                i = 0;
            }
        }

        private Set<Constraint> getConstraints(String str) {
            return this.constraintsMap.get(str);
        }

        public boolean permits(Key key) {
            Set<Constraint> constraints = getConstraints(key.getAlgorithm());
            if (constraints == null) {
                return true;
            }
            for (Constraint permits : constraints) {
                if (!permits.permits(key)) {
                    if (DisabledAlgorithmConstraints.debug == null) {
                        return false;
                    }
                    Debug r2 = DisabledAlgorithmConstraints.debug;
                    r2.println("keySizeConstraint: failed key constraint check " + KeyUtil.getKeySize(key));
                    return false;
                }
            }
            return true;
        }

        public void permits(CertConstraintParameters certConstraintParameters) throws CertPathValidatorException {
            X509Certificate certificate = certConstraintParameters.getCertificate();
            if (DisabledAlgorithmConstraints.debug != null) {
                Debug r1 = DisabledAlgorithmConstraints.debug;
                r1.println("Constraints.permits(): " + certificate.getSigAlgName());
            }
            Set<String> decomposeOneHash = AlgorithmDecomposer.decomposeOneHash(certificate.getSigAlgName());
            if (decomposeOneHash != null && !decomposeOneHash.isEmpty()) {
                decomposeOneHash.add(certificate.getPublicKey().getAlgorithm());
                for (String constraints : decomposeOneHash) {
                    Set<Constraint> constraints2 = getConstraints(constraints);
                    if (constraints2 != null) {
                        for (Constraint permits : constraints2) {
                            permits.permits(certConstraintParameters);
                        }
                    }
                }
            }
        }
    }

    private static abstract class Constraint {
        String algorithm;
        Constraint nextConstraint;

        public abstract void permits(CertConstraintParameters certConstraintParameters) throws CertPathValidatorException;

        public boolean permits(Key key) {
            return true;
        }

        private Constraint() {
            this.nextConstraint = null;
        }

        enum Operator {
            EQ,
            NE,
            LT,
            LE,
            GT,
            GE;

            /* renamed from: of */
            static Operator m1825of(String str) {
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case 60:
                        if (str.equals("<")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 62:
                        if (str.equals(">")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1084:
                        if (str.equals("!=")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1921:
                        if (str.equals("<=")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1952:
                        if (str.equals("==")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 1983:
                        if (str.equals(">=")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        return LT;
                    case 1:
                        return GT;
                    case 2:
                        return NE;
                    case 3:
                        return LE;
                    case 4:
                        return EQ;
                    case 5:
                        return GE;
                    default:
                        throw new IllegalArgumentException("Error in security property. " + str + " is not a legal Operator");
                }
            }
        }
    }

    private static class jdkCAConstraint extends Constraint {
        jdkCAConstraint(String str) {
            super();
            this.algorithm = str;
        }

        public void permits(CertConstraintParameters certConstraintParameters) throws CertPathValidatorException {
            if (DisabledAlgorithmConstraints.debug != null) {
                Debug r0 = DisabledAlgorithmConstraints.debug;
                r0.println("jdkCAConstraints.permits(): " + this.algorithm);
            }
            if (!certConstraintParameters.isTrustedMatch()) {
                return;
            }
            if (this.nextConstraint != null) {
                this.nextConstraint.permits(certConstraintParameters);
                return;
            }
            throw new CertPathValidatorException("Algorithm constraints check failed on certificate anchor limits", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        }
    }

    private static class KeySizeConstraint extends Constraint {
        private int maxSize;
        private int minSize;
        private int prohibitedSize = -1;

        public KeySizeConstraint(String str, Constraint.Operator operator, int i) {
            super();
            this.algorithm = str;
            int i2 = 0;
            switch (C48261.f917x2fd3bea0[operator.ordinal()]) {
                case 1:
                    this.minSize = 0;
                    this.maxSize = Integer.MAX_VALUE;
                    this.prohibitedSize = i;
                    return;
                case 2:
                    this.minSize = i;
                    this.maxSize = i;
                    return;
                case 3:
                    this.minSize = i;
                    this.maxSize = Integer.MAX_VALUE;
                    return;
                case 4:
                    this.minSize = i + 1;
                    this.maxSize = Integer.MAX_VALUE;
                    return;
                case 5:
                    this.minSize = 0;
                    this.maxSize = i;
                    return;
                case 6:
                    this.minSize = 0;
                    this.maxSize = i > 1 ? i - 1 : i2;
                    return;
                default:
                    this.minSize = Integer.MAX_VALUE;
                    this.maxSize = -1;
                    return;
            }
        }

        public void permits(CertConstraintParameters certConstraintParameters) throws CertPathValidatorException {
            if (permitsImpl(certConstraintParameters.getCertificate().getPublicKey())) {
                return;
            }
            if (this.nextConstraint != null) {
                this.nextConstraint.permits(certConstraintParameters);
                return;
            }
            throw new CertPathValidatorException("Algorithm constraints check failed on keysize limits", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        }

        public boolean permits(Key key) {
            if (this.nextConstraint != null && this.nextConstraint.permits(key)) {
                return true;
            }
            if (DisabledAlgorithmConstraints.debug != null) {
                Debug r0 = DisabledAlgorithmConstraints.debug;
                r0.println("KeySizeConstraints.permits(): " + this.algorithm);
            }
            return permitsImpl(key);
        }

        private boolean permitsImpl(Key key) {
            if (this.algorithm.compareToIgnoreCase(key.getAlgorithm()) != 0) {
                return true;
            }
            int keySize = KeyUtil.getKeySize(key);
            if (keySize == 0) {
                return false;
            }
            if (keySize <= 0) {
                return true;
            }
            if (keySize < this.minSize || keySize > this.maxSize || this.prohibitedSize == keySize) {
                return false;
            }
            return true;
        }
    }

    /* renamed from: sun.security.util.DisabledAlgorithmConstraints$1 */
    static /* synthetic */ class C48261 {

        /* renamed from: $SwitchMap$sun$security$util$DisabledAlgorithmConstraints$Constraint$Operator */
        static final /* synthetic */ int[] f917x2fd3bea0;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator[] r0 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f917x2fd3bea0 = r0
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.EQ     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f917x2fd3bea0     // Catch:{ NoSuchFieldError -> 0x001d }
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.NE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f917x2fd3bea0     // Catch:{ NoSuchFieldError -> 0x0028 }
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.LT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f917x2fd3bea0     // Catch:{ NoSuchFieldError -> 0x0033 }
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.LE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f917x2fd3bea0     // Catch:{ NoSuchFieldError -> 0x003e }
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.GT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f917x2fd3bea0     // Catch:{ NoSuchFieldError -> 0x0049 }
                sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator r1 = sun.security.util.DisabledAlgorithmConstraints.Constraint.Operator.GE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.util.DisabledAlgorithmConstraints.C48261.<clinit>():void");
        }
    }
}
