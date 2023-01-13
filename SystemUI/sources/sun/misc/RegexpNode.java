package sun.misc;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintStream;

/* compiled from: RegexpPool */
class RegexpNode {

    /* renamed from: c */
    char f859c;
    int depth;
    boolean exact;
    RegexpNode firstchild;
    RegexpNode nextsibling;

    /* renamed from: re */
    String f860re;
    Object result;

    RegexpNode() {
        this.f860re = null;
        this.f859c = '#';
        this.depth = 0;
    }

    RegexpNode(char c, int i) {
        this.f860re = null;
        this.f859c = c;
        this.depth = i;
    }

    /* access modifiers changed from: package-private */
    public RegexpNode add(char c) {
        RegexpNode regexpNode;
        RegexpNode regexpNode2 = this.firstchild;
        if (regexpNode2 == null) {
            regexpNode = new RegexpNode(c, this.depth + 1);
        } else {
            while (regexpNode2 != null) {
                if (regexpNode2.f859c == c) {
                    return regexpNode2;
                }
                regexpNode2 = regexpNode2.nextsibling;
            }
            regexpNode = new RegexpNode(c, this.depth + 1);
            regexpNode.nextsibling = this.firstchild;
        }
        this.firstchild = regexpNode;
        return regexpNode;
    }

    /* access modifiers changed from: package-private */
    public RegexpNode find(char c) {
        for (RegexpNode regexpNode = this.firstchild; regexpNode != null; regexpNode = regexpNode.nextsibling) {
            if (regexpNode.f859c == c) {
                return regexpNode;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void print(PrintStream printStream) {
        if (this.nextsibling != null) {
            printStream.print(NavigationBarInflaterView.KEY_CODE_START);
            while (this != null) {
                printStream.write((int) this.f859c);
                RegexpNode regexpNode = this.firstchild;
                if (regexpNode != null) {
                    regexpNode.print(printStream);
                }
                this = this.nextsibling;
                printStream.write(this != null ? 124 : 41);
            }
            return;
        }
        printStream.write((int) this.f859c);
        RegexpNode regexpNode2 = this.firstchild;
        if (regexpNode2 != null) {
            regexpNode2.print(printStream);
        }
    }
}
