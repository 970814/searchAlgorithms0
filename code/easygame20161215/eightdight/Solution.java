package easygame20161215.eightdight;

import easygame20161215.eightdight.donotsave.*;
import easygame20161215.EightDigitalGame;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

import static easygame20161215.eightdight.donotsave.StaticThing.*;
import static java.lang.Math.abs;
import static java.lang.System.arraycopy;


/**
 * Created by L on 2016/12/27.
 */
public class Solution {
    public static int[][] K = {
            {0, -1},
            {-1, 0},
            {0, 1},
            {1, 0},
    };


    public final char[] S0;
    public final char[] Sg;
    public int[] map;//为方便计算h值，以后只需要花费O(1)的时间就可以找出某个数码本应该在的位置
    public final int rows;
    public final int columns;
    final int length;
    public Node path;
    public int[][] k;
    public int[] n;
    public int[] pathIDASS = new int[160];
    public int nIDASS = 0;

    public Solution(int r, int c) {
        rows = r;
        columns = c;

        length = rows * columns;
        S0 = new char[length];
        Sg = new char[length];

        path = null;
        k = new int[][]{
                {0, -1},
                {-columns, 0},
                {0, 1},
                {columns, 0},
        };
    }

    public Solution() {
        this(4, 4);
    }

    public void mapping() {
        map = new int[length];//quickSearch mapping A star used
        for (int i = 0; i < Sg.length; i++) {
            map[cHash(Sg[i])] = i;
        }
    }



    public void setRandomInput() {
        setRandomArray(S0);
        setRandomArray(Sg);
    }
    public void setInput() {
        setInputArray(S0);
        setInputArray(Sg);
    }

    public void show() {
        show2Ar(S0);
        show2Ar(Sg);
    }

    public void show2Ar(char... A) {
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + "  ");
            if (i % columns == columns - 1) {
                System.out.println();
            }
        }
    }

    public StringBuilder toResult() {
        StringBuilder b = new StringBuilder();
        int[] depth = {0};
        Queue<Character> traces = new ArrayDeque<>();
        String str0 = "初始状态S0为";
        String strG = "结果状态Sg为";
        int n = 2 * str0.length();
        String format = "%-" + n + "s";
        String str = String.format(format + format, str0, strG);
        b.append(str)
                .append('\n');
        append2ArString(b, format);

        return toResult(S0.clone(), b, path, depth, traces)
                .append("使用的最小移动次数为: " + depth[0])
                .append('\n')
                .append("使用的一个最优移动策略为: " + traces)
                .append("访问过的节点：" + this.n[0]);
    }

    public StringBuilder toResult(char[] A, StringBuilder b, Node node, int[] steps, Queue<Character> path) {
        int d = node.d;
        if (d >= 0 && d < 4) {
            steps[0]++;
            toResult(A, b, node.pi, steps, path);
            exchange(A, node.pi.index, node.index);
            int k = opposite(node.d);
//            b.append("moveTo " + k + "(" + D[k] + ") :");
            path.offer(D[k]);
//            b.append('\n');
//            appendArrayString(b, A);
        }
        return b;
    }

    private void append2ArString(StringBuilder b, String format) {
        int n = S0.length / columns;
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < columns; j++) {
                t.append(S0[j + columns * i])
                        .append("  ");
            }
            b.append(String.format(format, t));
            t.setLength(0);
            b.append('\t');
            for (int j = 0; j < columns; j++) {
                t.append(Sg[j + columns * i])
                        .append("  ");
            }
            b.append(String.format(format, t));
            t.setLength(0);
            b.append('\n');
        }
    }
    private void appendArrayString(StringBuilder b, char[] A) {
        for (int i = 0; i < A.length; i++) {
            b.append(A[i])
                    .append("  ");
            if (i % columns == columns - 1) {
                b.append('\n');
            }
        }
    }

    public boolean withinBounds0(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < columns;
    }

    public int withinBounds(int index, int moveD) {
        int X = index / columns + K[moveD][0];
        int Y = index % columns + K[moveD][1];
        if (withinBounds0(X, Y))
            return index + k[moveD][0] + k[moveD][1];
        else return -1;
    }

    public int s0Value() {
        return computeValue(S0);
    }

    public int computeValue(char[] A) {//O(n^2) improved to O(n)
        int value = 0;
        try {
            for (int i = 0; i < A.length; i++) {
                if (A[i] != BLANK) {
                    //            int j = linearSearchFromSg(A[i]);//O(n)
                    int j = sgIndexOf(A[i]);
                    value += distanceOf(i, j);
                }
            }
        } catch (NullPointerException e) {
            if (map == null)
                throw new RuntimeException("you must mapping sg set.first");
            else throw e;
        }
        return value;
    }

    public int distanceOf(int i, int j) {
        return abs(j / columns - i / columns) + abs(j % columns - i % columns);
    }

    @Deprecated
    public int linearSearchFromSg(char c) {
        for (int i = 0; i < Sg.length; i++) {
            if (c == Sg[i]) {
                return i;
            }
        }
        throw new RuntimeException("impossible");
    }

    public int cHash(char c) {
        return c - (c > 'Z' ? 'a' - 36 : c > '9' ? 'A' - 10 : '0');
    }

    public int sgIndexOf(char c) {
        return map[cHash(c)];
    }

    public void arrayCopyBy(char[] s0, char[] sg) {
        arraycopy(s0, 0, S0, 0, s0.length);
        arraycopy(sg, 0, Sg, 0, sg.length);
    }

    public void setTrace(Stack<EightDigitalGame.D> traces) {
        Node x = path;
        do {
            traces.push(new EightDigitalGame.D(x.index, opposite(x.d)));
//            System.out.println(D[opposite(x.d)]);
            x = x.pi;
        } while (x != null);
//        System.out.println(traces.size());
        System.out.println(toResult());

    }

    public StringBuilder canBeSolvedMostBy(int depth, Stack<EightDigitalGame.D> traces) {

        int[] path = canBeSolvedMostBy2(depth);

        for (int i = path.length - 1; i >= 0; i--) {
            traces.push(new EightDigitalGame.D(-1, path[i]));
        }
        traces.push(new EightDigitalGame.D(-1, -1));
        StringBuilder builder = new StringBuilder("一个还原路径为: ");
        StringBuilder b = new StringBuilder();
        builder.append('\n');
        for (int i : path) {
            builder.append(D[i]).append(',');
            b.append(D[i]).append(',');
        }
        builder.append('\n');
        builder.append("depth: " + path.length);
        System.out.println(builder);
        return b;
    }

    public int[] canBeSolvedMostBy(int depth) {
        setRandomArray(S0);
        int[] path = new int[depth];
        arraycopy(S0, 0, Sg, 0, Sg.length);
        int index = findBlank(Sg);
        int v = -1;
        int i = 0;
        while (i < path.length) {
            int c = random.nextInt(4);
            if (v != c) {
                int newIndex = withinBounds(index, c);
                if (newIndex >= 0) {
                    v = opposite(c);
                    exchange(Sg, index, newIndex);
                    index = newIndex;
                    path[i++] = v;
                }
            }
        }
        return path;
    }
    public int[] canBeSolvedMostBy2(int times) {
        setRandomArray(S0);
        int[] path = new int[times];
        arraycopy(S0, 0, Sg, 0, Sg.length);
        int index = findBlank(Sg);
        int v = -1;
        int size = 0;
        for (int i = 0; i < times; i++) {
            int c = random.nextInt(4);
            if (v != c) {
                int newIndex = withinBounds(index, c);
                if (newIndex >= 0) {
                    v = opposite(c);
                    exchange(Sg, index, newIndex);
                    index = newIndex;
                    path[size++] = v;
                }
            }
        }
        return Arrays.copyOf(path, size);
    }
}
