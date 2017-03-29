package easygame20161215.eightdight;

import easygame20161215.eightdight.donotsave.Node;
import easygame20161215.eightdight.donotsave.NodeStar;

import java.util.*;

import static easygame20161215.eightdight.donotsave.Node.rootNode;
import static easygame20161215.eightdight.donotsave.StaticThing.*;

/**
 * Created by L on 2016/12/27.
 */
public class Search {
    /**
     * Space 9%
     * Time: 5742ms,5957ms,5843ms
     */
    public static void BFS(Solution s) {//do not save the state
        char[] s0 = s.S0;
        char[] sg = s.Sg;
        Record record = new Record(s.length);
        record.useModel();
        Queue<Node> q = new ArrayDeque<>();
        q.offer(rootNode(s0));
        int depth = 0;
        try {
            while (true) {
                Node x = q.poll();
                if (twoRectangleIsSame(x.toRectangle(s0), sg)) {
                    s.path = x;
                    return;
                }
//                depth++;
                final int v = opposite(x.d);
                for (int i = 0; i < 4; i++) {
                    if (v != i) {
                        Node y = x.moveTo(s, i);//s is the dimension
                        if (y != null && !record.isVisited(y.toRectangle(s0))) {
                            q.offer(y);
                        }

                    }
                }
            }
        } catch (Throwable e) {
//            System.out.println(depth);
            throw e;
        }
    }

    @SuppressWarnings("Duplicates")
    public static void DBFS(Solution s) {//do not save the state
        char[] s0 = s.S0;
        char[] sg = s.Sg;
        Record record = new Record(s.length);
        record.useModel("DBFS");
        Queue<Node> q = new ArrayDeque<>();
        Queue<Node> p = new ArrayDeque<>();

        s.path = rootNode(sg);
        record.isVisitedByDBFS(s0, true);
        if (record.isVisitedByDBFS(sg, false) == 2) {
            return;
        }
        q.offer(rootNode(s0));
        p.offer(s.path);
        int[] n = {2};
        s.n = n;
        while (true) {
//            System.out.println(1);
            Node x;
            int v;
            x = q.poll();
            v = opposite(x.d);
            for (int i = 0; i < 4; i++) {
                if (v != i) {
                    Node y = x.moveTo(s, i);//s is the dimension
                    if (y != null) {
                        char[] yRect = y.toRectangle(s0);
                        int r = record.isVisitedByDBFS(yRect, true);
                        if (r == 0) {
                            q.offer(y);
                            n[0]++;
                        } else if (r == 2) {//searched result
                            for (Node z : p) {
                                if (twoRectangleIsSame(yRect, z.toRectangle(sg))) {
                                    z.pi.quickLink(z);
                                    z.pi.pi = y;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            x = p.poll();
            v = opposite(x.d);
            for (int i = 0; i < 4; i++) {
                if (v != i) {
                    Node y = x.moveTo(s, i);//s is the dimension
                    if (y != null) {
                        char[] yRect = y.toRectangle(sg);
                        int r = record.isVisitedByDBFS(yRect, false);
                        if (r == 0) {
                            p.offer(y);
                            n[0]++;
                        } else if (r == 2) {//searched result
                            for (Node z : q) {
                                if (twoRectangleIsSame(yRect, z.toRectangle(s0))) {
                                    y.pi.quickLink(y);
                                    y.pi.pi = z;
                                    return;
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    public static void ASS(Solution s) {

        char[] s0 = s.S0;
        char[] sg = s.Sg;
        Record record = new Record(s.length);
        record.useModel();

        LinkedList<NodeStar> starQueue = new LinkedList<>();
        s.mapping();
//        System.out.println(s.s0Value());
        starQueue.offer(NodeStar.rootNodeStar(s));
        int[] n = {1};
        s.n = n;
        while (true) {
            NodeStar xStar = starQueue.poll();
            char[] rect = xStar.toRectangle(s0);
            if (twoRectangleIsSame(rect, sg)) {
                s.path = xStar;
                return;
            }

            final int v = opposite(xStar.d);
            for (int i = 0; i < 4; i++) {
                if (v != i) {
                    NodeStar y = xStar.moveTo(s, i, rect);//s is the dimension

                    if (y != null) {

//                        char[] yRect = y.toRectangle(s0);
                        char[] yRect = rect;
                        if (!record.isVisited(yRect)) {
                            y.insertOrderByf(starQueue);
                            n[0]++;
                        } else {
                            Iterator<NodeStar> each = starQueue.iterator();
                            while (each.hasNext()) {
                                NodeStar star = each.next();
                                if (star.value == y.value && twoRectangleIsSame(star.toRectangle(s0), yRect)) {
                                    if (star.depth > y.depth) {
                                        each.remove();
                                        y.insertOrderByf(starQueue);
                                        n[0]++;
                                    }
                                    break;
                                }
                            }
                        }
                    }

                }
            }

        }
    }




/*for (Node z : q) {
    if (twoRectangleIsSame(yRect, z.toRectangle(s0))) {
        System.out.println(1);
        StringBuilder builder = new StringBuilder();
        Queue<Character> path = new ArrayDeque<>();
        s.toResult(s0.clone(), new StringBuilder(), y, new int[]{0}, path);
        builder.append(path.toString());
        path.cl ear();
        s.toResult(s0.clone(), new StringBuilder(), z, new int[]{0}, path);
        builder.append('\n').append(path.toString()).append('\n');
        System.out.println(builder);
        System.out.println(s.toResult(s0.clone(), new StringBuilder(), y, new int[]{0}, new ArrayDeque<>()));
        System.out.println(s.toResult(s0.clone(), new StringBuilder(), z, new int[]{0}, new ArrayDeque<>()));
        contains = true;
        break;
    }
}*/

    /**
     * 如果搜索到结果返回真，否则超过深度返回假
     */
    static long n = 0;
    public static boolean DFS(Solution solution, final int depth, final int maxDepth, final int preD, final int blankIndex, final int h) {
        n++;
        if (twoRectangleIsSame(solution.S0, solution.Sg)) {
            return true;
        }
        if (h > maxDepth - depth) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int c = opposite(preD);
            if (i != c) {
                int newIndex = solution.withinBounds(blankIndex, i);
                if (newIndex >= 0) {
//                    solution.pathIDASS.push(opposite(i));//直接添加相反的方向，因为程序是按空格来移动的，然而实际上用数字，他们的方向是相反的
                    solution.pathIDASS[solution.nIDASS++] = opposite(i);
                    int dest = solution.sgIndexOf(solution.S0[newIndex]);
                    exchange(solution.S0, blankIndex, newIndex);
                    if (DFS(solution, depth + 1, maxDepth, i, newIndex, h
                            - solution.distanceOf(newIndex, dest)
                            + solution.distanceOf(blankIndex, dest))) {
                        return true;
                    }
                    solution.nIDASS--;
//                    solution.pathIDASS.pop();
                    exchange(solution.S0, blankIndex, newIndex);
                }

            }
        }
        return false;
    }

    public static void IDASS(Solution solution) {
        solution.mapping();
        char[] copy = solution.S0.clone();
        final int h = solution.s0Value();
        final int blankIndex = findBlank(copy);
        int maxDepth = h;
        maxDepth = 72;
        System.out.println("h----> " + h);
        while (!DFS(solution, 0, maxDepth, -1, blankIndex, h)) {
            System.out.println("深度为: " + maxDepth + "---" + "访问节点: " + n);
            /*if (maxDepth >= 60) {
                maxDepth += 2;
            } else {
                maxDepth += 8;
            }*/
            maxDepth += 2;
            System.arraycopy(copy, 0, solution.S0, 0, copy.length);
            n = 0;
        }
        System.arraycopy(copy, 0, solution.S0, 0, copy.length);
        System.out.println("深度为: " + maxDepth + "---" + "访问节点: " + n);
    }
}













