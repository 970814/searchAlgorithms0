package easygame20161215.eightdight.donotsave;

import easygame20161215.eightdight.Solution;

import static easygame20161215.eightdight.donotsave.StaticThing.*;

/**
 * Created by L on 2016/12/27.
 */
public class Node {
    public Node pi;
    public int d;
    public final int index;

    public static Node rootNode(char[] s0) {
        return rootNode(findBlank(s0));
    }

    public static Node rootNode(int index) {
        return new Node(-1, null, index);
    }

    protected Node(int d, Node pi, int blank) {
        this.pi = pi;
        this.d = d;
        index = blank;
    }

    public char[] toRectangle(char[] s0) {
        char[] s = s0.clone();
        toRect(s);
        return s;
    }

    private void toRect(char[] s) {//这里的rect同一个对象.....
        if (pi != null) {
            pi.toRect(s);
            exchange(s, pi.index, index);
        }
    }

    public Node moveTo(Solution s, int i) {
        int newIndex = s.withinBounds(index, i);

        return newIndex >= 0 ? new Node(i, this, newIndex) : null;
    }

    public void quickLink(Node PI) {
        if (pi != null) {
            pi.quickLink(this);
        }
        pi = PI;
        d = opposite(pi.d);
    }
}
