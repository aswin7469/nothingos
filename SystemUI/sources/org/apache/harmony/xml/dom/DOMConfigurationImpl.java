package org.apache.harmony.xml.dom;

import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;

public final class DOMConfigurationImpl implements DOMConfiguration {
    /* access modifiers changed from: private */
    public static final Map<String, Parameter> PARAMETERS;
    /* access modifiers changed from: private */
    public boolean cdataSections = true;
    /* access modifiers changed from: private */
    public boolean comments = true;
    /* access modifiers changed from: private */
    public boolean datatypeNormalization = false;
    /* access modifiers changed from: private */
    public boolean entities = true;
    /* access modifiers changed from: private */
    public DOMErrorHandler errorHandler;
    /* access modifiers changed from: private */
    public boolean namespaces = true;
    /* access modifiers changed from: private */
    public String schemaLocation;
    /* access modifiers changed from: private */
    public String schemaType;
    /* access modifiers changed from: private */
    public boolean splitCdataSections = true;
    /* access modifiers changed from: private */
    public boolean validate = false;
    /* access modifiers changed from: private */
    public boolean wellFormed = true;

    interface Parameter {
        boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj);

        Object get(DOMConfigurationImpl dOMConfigurationImpl);

        void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj);
    }

    static {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        PARAMETERS = treeMap;
        treeMap.put("canonical-form", new FixedParameter(false));
        treeMap.put("cdata-sections", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.cdataSections);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.cdataSections = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("check-character-normalization", new FixedParameter(false));
        treeMap.put("comments", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.comments);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.comments = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("datatype-normalization", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.datatypeNormalization);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    dOMConfigurationImpl.datatypeNormalization = true;
                    dOMConfigurationImpl.validate = true;
                    return;
                }
                dOMConfigurationImpl.datatypeNormalization = false;
            }
        });
        treeMap.put("element-content-whitespace", new FixedParameter(true));
        treeMap.put("entities", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.entities);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.entities = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("error-handler", new Parameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return dOMConfigurationImpl.errorHandler;
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.errorHandler = (DOMErrorHandler) obj;
            }

            public boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                return obj == null || (obj instanceof DOMErrorHandler);
            }
        });
        treeMap.put("infoset", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(!dOMConfigurationImpl.entities && !dOMConfigurationImpl.datatypeNormalization && !dOMConfigurationImpl.cdataSections && dOMConfigurationImpl.wellFormed && dOMConfigurationImpl.comments && dOMConfigurationImpl.namespaces);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    dOMConfigurationImpl.entities = false;
                    dOMConfigurationImpl.datatypeNormalization = false;
                    dOMConfigurationImpl.cdataSections = false;
                    dOMConfigurationImpl.wellFormed = true;
                    dOMConfigurationImpl.comments = true;
                    dOMConfigurationImpl.namespaces = true;
                }
            }
        });
        treeMap.put("namespaces", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.namespaces);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.namespaces = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("namespace-declarations", new FixedParameter(true));
        treeMap.put("normalize-characters", new FixedParameter(false));
        treeMap.put("schema-location", new Parameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return dOMConfigurationImpl.schemaLocation;
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.schemaLocation = (String) obj;
            }

            public boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                return obj == null || (obj instanceof String);
            }
        });
        treeMap.put("schema-type", new Parameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return dOMConfigurationImpl.schemaType;
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.schemaType = (String) obj;
            }

            public boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                return obj == null || (obj instanceof String);
            }
        });
        treeMap.put("split-cdata-sections", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.splitCdataSections);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.splitCdataSections = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("validate", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.validate);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.validate = ((Boolean) obj).booleanValue();
            }
        });
        treeMap.put("validate-if-schema", new FixedParameter(false));
        treeMap.put("well-formed", new BooleanParameter() {
            public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
                return Boolean.valueOf(dOMConfigurationImpl.wellFormed);
            }

            public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
                dOMConfigurationImpl.wellFormed = ((Boolean) obj).booleanValue();
            }
        });
    }

    static class FixedParameter implements Parameter {
        final Object onlyValue;

        FixedParameter(Object obj) {
            this.onlyValue = obj;
        }

        public Object get(DOMConfigurationImpl dOMConfigurationImpl) {
            return this.onlyValue;
        }

        public void set(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
            if (!this.onlyValue.equals(obj)) {
                throw new DOMException(9, "Unsupported value: " + obj);
            }
        }

        public boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
            return this.onlyValue.equals(obj);
        }
    }

    static abstract class BooleanParameter implements Parameter {
        BooleanParameter() {
        }

        public boolean canSet(DOMConfigurationImpl dOMConfigurationImpl, Object obj) {
            return obj instanceof Boolean;
        }
    }

    public boolean canSetParameter(String str, Object obj) {
        Parameter parameter = PARAMETERS.get(str);
        return parameter != null && parameter.canSet(this, obj);
    }

    public void setParameter(String str, Object obj) throws DOMException {
        Parameter parameter = PARAMETERS.get(str);
        if (parameter != null) {
            try {
                parameter.set(this, obj);
            } catch (NullPointerException unused) {
                throw new DOMException(17, "Null not allowed for " + str);
            } catch (ClassCastException unused2) {
                throw new DOMException(17, "Invalid type for " + str + ": " + obj.getClass());
            }
        } else {
            throw new DOMException(8, "No such parameter: " + str);
        }
    }

    public Object getParameter(String str) throws DOMException {
        Parameter parameter = PARAMETERS.get(str);
        if (parameter != null) {
            return parameter.get(this);
        }
        throw new DOMException(8, "No such parameter: " + str);
    }

    public DOMStringList getParameterNames() {
        return internalGetParameterNames();
    }

    private static DOMStringList internalGetParameterNames() {
        Map<String, Parameter> map = PARAMETERS;
        final String[] strArr = (String[]) map.keySet().toArray(new String[map.size()]);
        return new DOMStringList() {
            public String item(int i) {
                String[] strArr = strArr;
                if (i < strArr.length) {
                    return strArr[i];
                }
                return null;
            }

            public int getLength() {
                return strArr.length;
            }

            public boolean contains(String str) {
                return DOMConfigurationImpl.PARAMETERS.containsKey(str);
            }
        };
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0074, code lost:
        r4 = ((org.apache.harmony.xml.dom.TextImpl) r4).minimize();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007a, code lost:
        if (r4 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007c, code lost:
        checkTextValidity(r4.buffer);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a4, code lost:
        r4 = r4.getFirstChild();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a8, code lost:
        if (r4 == null) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00aa, code lost:
        r0 = r4.getNextSibling();
        normalize(r4);
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void normalize(org.w3c.dom.Node r4) {
        /*
            r3 = this;
            short r0 = r4.getNodeType()
            java.lang.String r1 = "wf-invalid-character"
            r2 = 2
            switch(r0) {
                case 1: goto L_0x008c;
                case 2: goto L_0x0082;
                case 3: goto L_0x0074;
                case 4: goto L_0x004d;
                case 5: goto L_0x00b3;
                case 6: goto L_0x00b3;
                case 7: goto L_0x0043;
                case 8: goto L_0x0024;
                case 9: goto L_0x00a4;
                case 10: goto L_0x00b3;
                case 11: goto L_0x00a4;
                case 12: goto L_0x00b3;
                default: goto L_0x000a;
            }
        L_0x000a:
            org.w3c.dom.DOMException r3 = new org.w3c.dom.DOMException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Unsupported node type "
            r0.<init>((java.lang.String) r1)
            short r4 = r4.getNodeType()
            r0.append((int) r4)
            java.lang.String r4 = r0.toString()
            r0 = 9
            r3.<init>(r0, r4)
            throw r3
        L_0x0024:
            org.apache.harmony.xml.dom.CommentImpl r4 = (org.apache.harmony.xml.dom.CommentImpl) r4
            boolean r0 = r3.comments
            if (r0 != 0) goto L_0x0033
            org.w3c.dom.Node r3 = r4.getParentNode()
            r3.removeChild(r4)
            goto L_0x00b3
        L_0x0033:
            boolean r0 = r4.containsDashDash()
            if (r0 == 0) goto L_0x003c
            r3.report(r2, r1)
        L_0x003c:
            java.lang.StringBuffer r4 = r4.buffer
            r3.checkTextValidity(r4)
            goto L_0x00b3
        L_0x0043:
            org.apache.harmony.xml.dom.ProcessingInstructionImpl r4 = (org.apache.harmony.xml.dom.ProcessingInstructionImpl) r4
            java.lang.String r4 = r4.getData()
            r3.checkTextValidity(r4)
            goto L_0x00b3
        L_0x004d:
            org.apache.harmony.xml.dom.CDATASectionImpl r4 = (org.apache.harmony.xml.dom.CDATASectionImpl) r4
            boolean r0 = r3.cdataSections
            if (r0 == 0) goto L_0x0070
            boolean r0 = r4.needsSplitting()
            if (r0 == 0) goto L_0x006a
            boolean r0 = r3.splitCdataSections
            if (r0 == 0) goto L_0x0067
            r4.split()
            java.lang.String r0 = "cdata-sections-splitted"
            r1 = 1
            r3.report(r1, r0)
            goto L_0x006a
        L_0x0067:
            r3.report(r2, r1)
        L_0x006a:
            java.lang.StringBuffer r4 = r4.buffer
            r3.checkTextValidity(r4)
            goto L_0x00b3
        L_0x0070:
            org.apache.harmony.xml.dom.TextImpl r4 = r4.replaceWithText()
        L_0x0074:
            org.apache.harmony.xml.dom.TextImpl r4 = (org.apache.harmony.xml.dom.TextImpl) r4
            org.apache.harmony.xml.dom.TextImpl r4 = r4.minimize()
            if (r4 == 0) goto L_0x00b3
            java.lang.StringBuffer r4 = r4.buffer
            r3.checkTextValidity(r4)
            goto L_0x00b3
        L_0x0082:
            org.apache.harmony.xml.dom.AttrImpl r4 = (org.apache.harmony.xml.dom.AttrImpl) r4
            java.lang.String r4 = r4.getValue()
            r3.checkTextValidity(r4)
            goto L_0x00b3
        L_0x008c:
            r0 = r4
            org.apache.harmony.xml.dom.ElementImpl r0 = (org.apache.harmony.xml.dom.ElementImpl) r0
            org.w3c.dom.NamedNodeMap r0 = r0.getAttributes()
            r1 = 0
        L_0x0094:
            int r2 = r0.getLength()
            if (r1 >= r2) goto L_0x00a4
            org.w3c.dom.Node r2 = r0.item(r1)
            r3.normalize(r2)
            int r1 = r1 + 1
            goto L_0x0094
        L_0x00a4:
            org.w3c.dom.Node r4 = r4.getFirstChild()
        L_0x00a8:
            if (r4 == 0) goto L_0x00b3
            org.w3c.dom.Node r0 = r4.getNextSibling()
            r3.normalize(r4)
            r4 = r0
            goto L_0x00a8
        L_0x00b3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.harmony.xml.dom.DOMConfigurationImpl.normalize(org.w3c.dom.Node):void");
    }

    private void checkTextValidity(CharSequence charSequence) {
        if (this.wellFormed && !isValid(charSequence)) {
            report(2, "wf-invalid-character");
        }
    }

    private boolean isValid(CharSequence charSequence) {
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= charSequence.length()) {
                return true;
            }
            char charAt = charSequence.charAt(i);
            if (!(charAt == 9 || charAt == 10 || charAt == 13 || ((charAt >= ' ' && charAt <= 55295) || (charAt >= 57344 && charAt <= 65533)))) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    private void report(short s, String str) {
        DOMErrorHandler dOMErrorHandler = this.errorHandler;
        if (dOMErrorHandler != null) {
            dOMErrorHandler.handleError(new DOMErrorImpl(s, str));
        }
    }
}
