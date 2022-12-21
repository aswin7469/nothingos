package java.net;

import java.p026io.IOException;

public abstract class ContentHandler {
    public abstract Object getContent(URLConnection uRLConnection) throws IOException;

    public Object getContent(URLConnection uRLConnection, Class[] clsArr) throws IOException {
        Object content = getContent(uRLConnection);
        for (Class isInstance : clsArr) {
            if (isInstance.isInstance(content)) {
                return content;
            }
        }
        return null;
    }
}
