package easygame20161215.eightdight.donotsave;

import easygame20161215.eightdight.Solution;

import java.util.Iterator;
import java.util.LinkedList;

import static easygame20161215.eightdight.donotsave.StaticThing.exchange;
import static easygame20161215.eightdight.donotsave.StaticThing.findBlank;

/**
 * Created by L on 2017/1/4.
 */
public class NodeStar extends Node {
    public int depth;//g
    public int value;//h
    public char[] rect = null;

    /*@Override
    public char[] toRectangle(char[] s0) {

        if (pi == null) {
            return rect = super.toRectangle(s0);
        }
        rect = ((NodeStar) pi).rect;
        exchange(rect, pi.index, index);
        return rect;
    }*/

    private NodeStar(int d, Node pi, int blank, int depth, int value) {
        super(d, pi, blank);
        this.depth = depth;
        this.value = value;
    }

    public static NodeStar rootNodeStar(Solution s) {
        return rootNodeStar(findBlank(s.S0), s.s0Value());
    }

    public static NodeStar rootNodeStar(int index, int value) {
        return new NodeStar(-1, null, index, 0, value);
    }

    public NodeStar moveTo(Solution s, int i, char[] thisRect) {
        int newIndex = s.withinBounds(index, i);
        if (newIndex < 0) {
            return null;
        } else {
            int dest = s.sgIndexOf(thisRect[newIndex]);
            exchange(thisRect, index, newIndex);
            //前面两条代码的顺序不可交换,否则计算的sgIndexOf总是计算的是空格
            int h = s.distanceOf(index, dest) - s.distanceOf(newIndex, dest) + value;
            return new NodeStar(i, this, newIndex, depth + 1, h);
        }
    }

    public void insertOrderByf(LinkedList<NodeStar> starQueue) {
        Iterator<NodeStar> each = starQueue.iterator();
        int i = 0;
        int f0 = value + depth;
        while (each.hasNext()) {
            i++;
            NodeStar star = each.next();
            int f = star.value + star.depth;
            if (f0 < f) {
                --i;
                break;
            }
            /* else if (f0 == f) {

            }*/
        }
//        JOptionPane.showMessageDialog(null, "before: " + starQueue);
        starQueue.add(i, this);
//        JOptionPane.showMessageDialog(null, "after: "+starQueue);
    }

    @Deprecated
    public void replace(NodeStar star, LinkedList<NodeStar> starQueue) {
        int index = -1;
        copy(star);
        Iterator<NodeStar> each = starQueue.iterator();
        int f = star.depth + star.value;
        int j = 0;
        while (each.hasNext()) {
            NodeStar s = each.next();
            if (s.depth + s.value > f) {

            }
        }
    }

    private void copy(NodeStar star) {
        depth = star.depth;
        value = star.value;
        d = star.d;
        pi = star.pi;
    }

    @Override
    public String toString() {
        return (depth + value) + "";
    }
}
