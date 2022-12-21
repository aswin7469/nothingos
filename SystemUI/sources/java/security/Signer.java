package java.security;

@Deprecated(forRemoval = true, since = "1.2")
public abstract class Signer extends Identity {
    private static final long serialVersionUID = -1763464102261361480L;
    private PrivateKey privateKey;

    protected Signer() {
    }

    public Signer(String str) {
        super(str);
    }

    public Signer(String str, IdentityScope identityScope) throws KeyManagementException {
        super(str, identityScope);
    }

    public PrivateKey getPrivateKey() {
        check("getSignerPrivateKey");
        return this.privateKey;
    }

    public final void setKeyPair(KeyPair keyPair) throws InvalidParameterException, KeyException {
        check("setSignerKeyPair");
        final PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey2 = keyPair.getPrivate();
        if (publicKey == null || privateKey2 == null) {
            throw new InvalidParameterException();
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws KeyManagementException {
                    Signer.this.setPublicKey(publicKey);
                    return null;
                }
            });
            this.privateKey = privateKey2;
        } catch (PrivilegedActionException e) {
            throw ((KeyManagementException) e.getException());
        }
    }

    /* access modifiers changed from: package-private */
    public String printKeys() {
        return (getPublicKey() == null || this.privateKey == null) ? "\tno keys" : "\tpublic and private keys initialized";
    }

    public String toString() {
        return "[Signer]" + super.toString();
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }
}
