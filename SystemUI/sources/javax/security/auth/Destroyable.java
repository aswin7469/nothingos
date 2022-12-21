package javax.security.auth;

public interface Destroyable {
    boolean isDestroyed() {
        return false;
    }

    void destroy() throws DestroyFailedException {
        throw new DestroyFailedException();
    }
}
