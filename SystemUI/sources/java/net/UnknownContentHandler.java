package java.net;

import java.p026io.IOException;

/* compiled from: URLConnection */
class UnknownContentHandler extends ContentHandler {
    static final ContentHandler INSTANCE = new UnknownContentHandler();

    UnknownContentHandler() {
    }

    public Object getContent(URLConnection uRLConnection) throws IOException {
        return uRLConnection.getInputStream();
    }
}
