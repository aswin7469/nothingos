package sun.security.provider.certpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdjacencyList {
    private List<List<Vertex>> mOrigList;
    private ArrayList<BuildStep> mStepList = new ArrayList<>();

    public AdjacencyList(List<List<Vertex>> list) {
        this.mOrigList = list;
        buildList(list, 0, (BuildStep) null);
    }

    public Iterator<BuildStep> iterator() {
        return Collections.unmodifiableList(this.mStepList).iterator();
    }

    private boolean buildList(List<List<Vertex>> list, int i, BuildStep buildStep) {
        List<Vertex> list2 = list.get(i);
        boolean z = true;
        boolean z2 = true;
        for (Vertex vertex : list2) {
            if (vertex.getIndex() != -1) {
                if (list.get(vertex.getIndex()).size() != 0) {
                    z = false;
                }
            } else if (vertex.getThrowable() == null) {
                z2 = false;
            }
            this.mStepList.add(new BuildStep(vertex, 1));
        }
        if (!z) {
            boolean z3 = false;
            for (Vertex vertex2 : list2) {
                if (!(vertex2.getIndex() == -1 || list.get(vertex2.getIndex()).size() == 0)) {
                    BuildStep buildStep2 = new BuildStep(vertex2, 3);
                    this.mStepList.add(buildStep2);
                    z3 = buildList(list, vertex2.getIndex(), buildStep2);
                }
            }
            if (z3) {
                return true;
            }
            if (buildStep == null) {
                this.mStepList.add(new BuildStep((Vertex) null, 4));
            } else {
                this.mStepList.add(new BuildStep(buildStep.getVertex(), 2));
            }
            return false;
        } else if (z2) {
            if (buildStep == null) {
                this.mStepList.add(new BuildStep((Vertex) null, 4));
            } else {
                this.mStepList.add(new BuildStep(buildStep.getVertex(), 2));
            }
            return false;
        } else {
            ArrayList arrayList = new ArrayList();
            for (Vertex vertex3 : list2) {
                if (vertex3.getThrowable() == null) {
                    arrayList.add(vertex3);
                }
            }
            if (arrayList.size() == 1) {
                this.mStepList.add(new BuildStep((Vertex) arrayList.get(0), 5));
            } else {
                this.mStepList.add(new BuildStep((Vertex) arrayList.get(0), 5));
            }
            return true;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (List<Vertex> it : this.mOrigList) {
            sb.append("LinkedList[");
            int i2 = i + 1;
            sb.append(i);
            sb.append("]:\n");
            for (Vertex vertex : it) {
                sb.append(vertex.toString());
                sb.append("\n");
            }
            i = i2;
        }
        sb.append("]\n");
        return sb.toString();
    }
}
