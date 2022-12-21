package org.xml.sax.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.XMLConstants;

public class NamespaceSupport {
    /* access modifiers changed from: private */
    public static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(Collections.emptyList());
    public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
    public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
    private int contextPos;
    private Context[] contexts;
    private Context currentContext;
    /* access modifiers changed from: private */
    public boolean namespaceDeclUris;

    public NamespaceSupport() {
        reset();
    }

    public void reset() {
        Context[] contextArr = new Context[32];
        this.contexts = contextArr;
        this.namespaceDeclUris = false;
        this.contextPos = 0;
        Context context = new Context();
        this.currentContext = context;
        contextArr[0] = context;
        context.declarePrefix(XMLConstants.XML_NS_PREFIX, "http://www.w3.org/XML/1998/namespace");
    }

    public void pushContext() {
        Context[] contextArr = this.contexts;
        int length = contextArr.length;
        contextArr[this.contextPos].declsOK = false;
        int i = this.contextPos + 1;
        this.contextPos = i;
        if (i >= length) {
            Context[] contextArr2 = new Context[(length * 2)];
            System.arraycopy((Object) this.contexts, 0, (Object) contextArr2, 0, length);
            this.contexts = contextArr2;
        }
        Context[] contextArr3 = this.contexts;
        int i2 = this.contextPos;
        Context context = contextArr3[i2];
        this.currentContext = context;
        if (context == null) {
            Context context2 = new Context();
            this.currentContext = context2;
            contextArr3[i2] = context2;
        }
        int i3 = this.contextPos;
        if (i3 > 0) {
            this.currentContext.setParent(this.contexts[i3 - 1]);
        }
    }

    public void popContext() {
        this.contexts[this.contextPos].clear();
        int i = this.contextPos - 1;
        this.contextPos = i;
        if (i >= 0) {
            this.currentContext = this.contexts[i];
            return;
        }
        throw new EmptyStackException();
    }

    public boolean declarePrefix(String str, String str2) {
        if (str.equals(XMLConstants.XML_NS_PREFIX) || str.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return false;
        }
        this.currentContext.declarePrefix(str, str2);
        return true;
    }

    public String[] processName(String str, String[] strArr, boolean z) {
        String[] processName = this.currentContext.processName(str, z);
        if (processName == null) {
            return null;
        }
        strArr[0] = processName[0];
        strArr[1] = processName[1];
        strArr[2] = processName[2];
        return strArr;
    }

    public String getURI(String str) {
        return this.currentContext.getURI(str);
    }

    public Enumeration getPrefixes() {
        return this.currentContext.getPrefixes();
    }

    public String getPrefix(String str) {
        return this.currentContext.getPrefix(str);
    }

    public Enumeration getPrefixes(String str) {
        ArrayList arrayList = new ArrayList();
        Enumeration prefixes = getPrefixes();
        while (prefixes.hasMoreElements()) {
            String str2 = (String) prefixes.nextElement();
            if (str.equals(getURI(str2))) {
                arrayList.add(str2);
            }
        }
        return Collections.enumeration(arrayList);
    }

    public Enumeration getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }

    public void setNamespaceDeclUris(boolean z) {
        int i = this.contextPos;
        if (i != 0) {
            throw new IllegalStateException();
        } else if (z != this.namespaceDeclUris) {
            this.namespaceDeclUris = z;
            if (z) {
                this.currentContext.declarePrefix(XMLConstants.XMLNS_ATTRIBUTE, NSDECL);
                return;
            }
            Context[] contextArr = this.contexts;
            Context context = new Context();
            this.currentContext = context;
            contextArr[i] = context;
            context.declarePrefix(XMLConstants.XML_NS_PREFIX, "http://www.w3.org/XML/1998/namespace");
        }
    }

    public boolean isNamespaceDeclUris() {
        return this.namespaceDeclUris;
    }

    final class Context {
        Hashtable attributeNameTable;
        private boolean declSeen = false;
        private ArrayList<String> declarations = null;
        boolean declsOK = true;
        String defaultNS = null;
        Hashtable elementNameTable;
        private Context parent = null;
        Hashtable prefixTable;
        Hashtable uriTable;

        Context() {
            copyTables();
        }

        /* access modifiers changed from: package-private */
        public void setParent(Context context) {
            this.parent = context;
            this.declarations = null;
            this.prefixTable = context.prefixTable;
            this.uriTable = context.uriTable;
            this.elementNameTable = context.elementNameTable;
            this.attributeNameTable = context.attributeNameTable;
            this.defaultNS = context.defaultNS;
            this.declSeen = false;
            this.declsOK = true;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.parent = null;
            this.prefixTable = null;
            this.uriTable = null;
            this.elementNameTable = null;
            this.attributeNameTable = null;
            this.defaultNS = null;
        }

        /* access modifiers changed from: package-private */
        public void declarePrefix(String str, String str2) {
            if (this.declsOK) {
                if (!this.declSeen) {
                    copyTables();
                }
                if (this.declarations == null) {
                    this.declarations = new ArrayList<>();
                }
                String intern = str.intern();
                String intern2 = str2.intern();
                if (!"".equals(intern)) {
                    this.prefixTable.put(intern, intern2);
                    this.uriTable.put(intern2, intern);
                } else if ("".equals(intern2)) {
                    this.defaultNS = null;
                } else {
                    this.defaultNS = intern2;
                }
                this.declarations.add(intern);
                return;
            }
            throw new IllegalStateException("can't declare any more prefixes in this context");
        }

        /* access modifiers changed from: package-private */
        public String[] processName(String str, boolean z) {
            Hashtable hashtable;
            String str2;
            this.declsOK = false;
            if (z) {
                hashtable = this.attributeNameTable;
            } else {
                hashtable = this.elementNameTable;
            }
            String[] strArr = (String[]) hashtable.get(str);
            if (strArr != null) {
                return strArr;
            }
            String[] strArr2 = new String[3];
            strArr2[2] = str.intern();
            int indexOf = str.indexOf(58);
            if (indexOf == -1) {
                if (!z) {
                    String str3 = this.defaultNS;
                    if (str3 == null) {
                        strArr2[0] = "";
                    } else {
                        strArr2[0] = str3;
                    }
                } else if (str != XMLConstants.XMLNS_ATTRIBUTE || !NamespaceSupport.this.namespaceDeclUris) {
                    strArr2[0] = "";
                } else {
                    strArr2[0] = NamespaceSupport.NSDECL;
                }
                strArr2[1] = strArr2[2];
            } else {
                String substring = str.substring(0, indexOf);
                String substring2 = str.substring(indexOf + 1);
                if ("".equals(substring)) {
                    str2 = this.defaultNS;
                } else {
                    str2 = (String) this.prefixTable.get(substring);
                }
                if (str2 == null) {
                    return null;
                }
                if (!z && XMLConstants.XMLNS_ATTRIBUTE.equals(substring)) {
                    return null;
                }
                strArr2[0] = str2;
                strArr2[1] = substring2.intern();
            }
            hashtable.put(strArr2[2], strArr2);
            return strArr2;
        }

        /* access modifiers changed from: package-private */
        public String getURI(String str) {
            if ("".equals(str)) {
                return this.defaultNS;
            }
            Hashtable hashtable = this.prefixTable;
            if (hashtable == null) {
                return null;
            }
            return (String) hashtable.get(str);
        }

        /* access modifiers changed from: package-private */
        public String getPrefix(String str) {
            Hashtable hashtable = this.uriTable;
            if (hashtable == null) {
                return null;
            }
            return (String) hashtable.get(str);
        }

        /* access modifiers changed from: package-private */
        public Enumeration getDeclaredPrefixes() {
            ArrayList<String> arrayList = this.declarations;
            return arrayList == null ? NamespaceSupport.EMPTY_ENUMERATION : Collections.enumeration(arrayList);
        }

        /* access modifiers changed from: package-private */
        public Enumeration getPrefixes() {
            Hashtable hashtable = this.prefixTable;
            if (hashtable == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return hashtable.keys();
        }

        private void copyTables() {
            Hashtable hashtable = this.prefixTable;
            if (hashtable != null) {
                this.prefixTable = (Hashtable) hashtable.clone();
            } else {
                this.prefixTable = new Hashtable();
            }
            Hashtable hashtable2 = this.uriTable;
            if (hashtable2 != null) {
                this.uriTable = (Hashtable) hashtable2.clone();
            } else {
                this.uriTable = new Hashtable();
            }
            this.elementNameTable = new Hashtable();
            this.attributeNameTable = new Hashtable();
            this.declSeen = true;
        }
    }
}
