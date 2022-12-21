package org.json;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONStringer {
    private final String indent;
    final StringBuilder out;
    private final List<Scope> stack;

    enum Scope {
        EMPTY_ARRAY,
        NONEMPTY_ARRAY,
        EMPTY_OBJECT,
        DANGLING_KEY,
        NONEMPTY_OBJECT,
        NULL
    }

    public JSONStringer() {
        this.out = new StringBuilder();
        this.stack = new ArrayList();
        this.indent = null;
    }

    JSONStringer(int i) {
        this.out = new StringBuilder();
        this.stack = new ArrayList();
        char[] cArr = new char[i];
        Arrays.fill(cArr, ' ');
        this.indent = new String(cArr);
    }

    public JSONStringer array() throws JSONException {
        return open(Scope.EMPTY_ARRAY, NavigationBarInflaterView.SIZE_MOD_START);
    }

    public JSONStringer endArray() throws JSONException {
        return close(Scope.EMPTY_ARRAY, Scope.NONEMPTY_ARRAY, NavigationBarInflaterView.SIZE_MOD_END);
    }

    public JSONStringer object() throws JSONException {
        return open(Scope.EMPTY_OBJECT, "{");
    }

    public JSONStringer endObject() throws JSONException {
        return close(Scope.EMPTY_OBJECT, Scope.NONEMPTY_OBJECT, "}");
    }

    /* access modifiers changed from: package-private */
    public JSONStringer open(Scope scope, String str) throws JSONException {
        if (!this.stack.isEmpty() || this.out.length() <= 0) {
            beforeValue();
            this.stack.add(scope);
            this.out.append(str);
            return this;
        }
        throw new JSONException("Nesting problem: multiple top-level roots");
    }

    /* access modifiers changed from: package-private */
    public JSONStringer close(Scope scope, Scope scope2, String str) throws JSONException {
        Scope peek = peek();
        if (peek == scope2 || peek == scope) {
            List<Scope> list = this.stack;
            list.remove(list.size() - 1);
            if (peek == scope2) {
                newline();
            }
            this.out.append(str);
            return this;
        }
        throw new JSONException("Nesting problem");
    }

    private Scope peek() throws JSONException {
        if (!this.stack.isEmpty()) {
            List<Scope> list = this.stack;
            return list.get(list.size() - 1);
        }
        throw new JSONException("Nesting problem");
    }

    private void replaceTop(Scope scope) {
        List<Scope> list = this.stack;
        list.set(list.size() - 1, scope);
    }

    public JSONStringer value(Object obj) throws JSONException {
        if (this.stack.isEmpty()) {
            throw new JSONException("Nesting problem");
        } else if (obj instanceof JSONArray) {
            ((JSONArray) obj).writeTo(this);
            return this;
        } else if (obj instanceof JSONObject) {
            ((JSONObject) obj).writeTo(this);
            return this;
        } else {
            beforeValue();
            if (obj == null || (obj instanceof Boolean) || obj == JSONObject.NULL) {
                this.out.append(obj);
            } else if (obj instanceof Number) {
                this.out.append(JSONObject.numberToString((Number) obj));
            } else {
                string(obj.toString());
            }
            return this;
        }
    }

    public JSONStringer value(boolean z) throws JSONException {
        if (!this.stack.isEmpty()) {
            beforeValue();
            this.out.append(z);
            return this;
        }
        throw new JSONException("Nesting problem");
    }

    public JSONStringer value(double d) throws JSONException {
        if (!this.stack.isEmpty()) {
            beforeValue();
            this.out.append(JSONObject.numberToString(Double.valueOf(d)));
            return this;
        }
        throw new JSONException("Nesting problem");
    }

    public JSONStringer value(long j) throws JSONException {
        if (!this.stack.isEmpty()) {
            beforeValue();
            this.out.append(j);
            return this;
        }
        throw new JSONException("Nesting problem");
    }

    private void string(String str) {
        this.out.append("\"");
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == 12) {
                this.out.append("\\f");
            } else if (charAt != 13) {
                if (charAt != '\"' && charAt != '/' && charAt != '\\') {
                    switch (charAt) {
                        case 8:
                            this.out.append("\\b");
                            break;
                        case 9:
                            this.out.append("\\t");
                            break;
                        case 10:
                            this.out.append("\\n");
                            break;
                        default:
                            if (charAt > 31) {
                                this.out.append(charAt);
                                break;
                            } else {
                                this.out.append(String.format("\\u%04x", Integer.valueOf((int) charAt)));
                                break;
                            }
                    }
                } else {
                    StringBuilder sb = this.out;
                    sb.append('\\');
                    sb.append(charAt);
                }
            } else {
                this.out.append("\\r");
            }
        }
        this.out.append("\"");
    }

    private void newline() {
        if (this.indent != null) {
            this.out.append("\n");
            for (int i = 0; i < this.stack.size(); i++) {
                this.out.append(this.indent);
            }
        }
    }

    public JSONStringer key(String str) throws JSONException {
        if (str != null) {
            beforeKey();
            string(str);
            return this;
        }
        throw new JSONException("Names must be non-null");
    }

    private void beforeKey() throws JSONException {
        Scope peek = peek();
        if (peek == Scope.NONEMPTY_OBJECT) {
            this.out.append(',');
        } else if (peek != Scope.EMPTY_OBJECT) {
            throw new JSONException("Nesting problem");
        }
        newline();
        replaceTop(Scope.DANGLING_KEY);
    }

    private void beforeValue() throws JSONException {
        if (!this.stack.isEmpty()) {
            Scope peek = peek();
            if (peek == Scope.EMPTY_ARRAY) {
                replaceTop(Scope.NONEMPTY_ARRAY);
                newline();
            } else if (peek == Scope.NONEMPTY_ARRAY) {
                this.out.append(',');
                newline();
            } else if (peek == Scope.DANGLING_KEY) {
                this.out.append(this.indent == null ? ":" : ": ");
                replaceTop(Scope.NONEMPTY_OBJECT);
            } else if (peek != Scope.NULL) {
                throw new JSONException("Nesting problem");
            }
        }
    }

    public String toString() {
        if (this.out.length() == 0) {
            return null;
        }
        return this.out.toString();
    }
}
