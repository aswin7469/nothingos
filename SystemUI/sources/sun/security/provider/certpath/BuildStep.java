package sun.security.provider.certpath;

import java.security.cert.X509Certificate;

public class BuildStep {
    public static final int BACK = 2;
    public static final int FAIL = 4;
    public static final int FOLLOW = 3;
    public static final int POSSIBLE = 1;
    public static final int SUCCEED = 5;
    private X509Certificate cert;
    private int result;
    private Throwable throwable;
    private Vertex vertex;

    public String resultToString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "Internal error: Invalid step result value.\n" : "Certificate satisfies conditions.\n" : "Certificate backed out since path does not satisfy conditions.\n" : "Certificate satisfies conditions.\n" : "Certificate backed out since path does not satisfy build requirements.\n" : "Certificate to be tried.\n";
    }

    public BuildStep(Vertex vertex2, int i) {
        this.vertex = vertex2;
        if (vertex2 != null) {
            this.cert = vertex2.getCertificate();
            this.throwable = this.vertex.getThrowable();
        }
        this.result = i;
    }

    public Vertex getVertex() {
        return this.vertex;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public String getIssuerName() {
        return getIssuerName((String) null);
    }

    public String getIssuerName(String str) {
        X509Certificate x509Certificate = this.cert;
        return x509Certificate == null ? str : x509Certificate.getIssuerX500Principal().toString();
    }

    public String getSubjectName() {
        return getSubjectName((String) null);
    }

    public String getSubjectName(String str) {
        X509Certificate x509Certificate = this.cert;
        return x509Certificate == null ? str : x509Certificate.getSubjectX500Principal().toString();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public int getResult() {
        return this.result;
    }

    public String toString() {
        int i = this.result;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 5) {
                            return "Internal Error: Invalid step result\n";
                        }
                    }
                }
            }
            String resultToString = resultToString(i);
            return resultToString + this.vertex.throwableToString();
        }
        return resultToString(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0014, code lost:
        if (r1 != 5) goto L_0x0042;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String verboseToString() {
        /*
            r3 = this;
            int r0 = r3.getResult()
            java.lang.String r0 = r3.resultToString(r0)
            int r1 = r3.result
            r2 = 2
            if (r1 == r2) goto L_0x002d
            r2 = 3
            if (r1 == r2) goto L_0x0017
            r2 = 4
            if (r1 == r2) goto L_0x002d
            r2 = 5
            if (r1 == r2) goto L_0x0017
            goto L_0x0042
        L_0x0017:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append((java.lang.String) r0)
            sun.security.provider.certpath.Vertex r0 = r3.vertex
            java.lang.String r0 = r0.moreToString()
            r1.append((java.lang.String) r0)
            java.lang.String r0 = r1.toString()
            goto L_0x0042
        L_0x002d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append((java.lang.String) r0)
            sun.security.provider.certpath.Vertex r0 = r3.vertex
            java.lang.String r0 = r0.throwableToString()
            r1.append((java.lang.String) r0)
            java.lang.String r0 = r1.toString()
        L_0x0042:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append((java.lang.String) r0)
            java.lang.String r0 = "Certificate contains:\n"
            r1.append((java.lang.String) r0)
            sun.security.provider.certpath.Vertex r3 = r3.vertex
            java.lang.String r3 = r3.certToString()
            r1.append((java.lang.String) r3)
            java.lang.String r3 = r1.toString()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.BuildStep.verboseToString():java.lang.String");
    }

    public String fullToString() {
        return resultToString(getResult()) + this.vertex.toString();
    }
}
