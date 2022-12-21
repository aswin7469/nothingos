package java.net;

import libcore.content.type.MimeMap;

class DefaultFileNameMap implements FileNameMap {
    DefaultFileNameMap() {
    }

    public String getContentTypeFor(String str) {
        int indexOf = str.indexOf(35);
        if (indexOf >= 0) {
            str = str.substring(0, indexOf);
        }
        if (str.endsWith("/")) {
            return "text/html";
        }
        int lastIndexOf = str.lastIndexOf(47);
        if (lastIndexOf >= 0) {
            str = str.substring(lastIndexOf);
        }
        MimeMap mimeMap = MimeMap.getDefault();
        int i = -1;
        do {
            int i2 = i + 1;
            String guessMimeTypeFromExtension = mimeMap.guessMimeTypeFromExtension(str.substring(i2));
            if (guessMimeTypeFromExtension != null && (lastIndexOf < 0 || i >= 0)) {
                return guessMimeTypeFromExtension;
            }
            i = str.indexOf(46, i2);
        } while (i >= 0);
        return null;
    }
}
