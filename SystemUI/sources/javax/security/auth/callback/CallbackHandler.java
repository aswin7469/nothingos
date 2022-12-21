package javax.security.auth.callback;

import java.p026io.IOException;

public interface CallbackHandler {
    void handle(Callback[] callbackArr) throws IOException, UnsupportedCallbackException;
}
