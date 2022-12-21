package javax.crypto;

import java.p026io.IOException;
import java.p026io.Reader;
import java.security.GeneralSecurityException;

final class CryptoPolicyParser {
    /* access modifiers changed from: package-private */
    public CryptoPermission[] getPermissions() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public void read(Reader reader) throws ParsingException, IOException {
    }

    CryptoPolicyParser() {
    }

    static final class ParsingException extends GeneralSecurityException {
        ParsingException(String str) {
            super("");
        }

        ParsingException(int i, String str) {
            super("");
        }

        ParsingException(int i, String str, String str2) {
            super("");
        }
    }
}
