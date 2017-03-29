package easygame20161215;



import java.util.*;

import static testandLeetCode.eightdigital.EightDigital2.opposite;


/**
 * Created by L on 2016/12/14.
 */
@Deprecated
public class EightDigital {
    public int[] S0;
    public int[] Sg;
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    public Queue<Character> path = new ArrayDeque<>();
    public EightDigital(int[] s0, int[] sg, int width, int height) {
        Width = width;
        Height = height;
        S0 = s0;
        Sg = sg;
        K = new int[][]{
                {0, -1},
                {-Width, 0},
                {0, 1},
                {Width, 0},
        };

    }

    public static void setRandomArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        for (int i = 0; i < a.length - 1; i++) {
            exchange(a, i, random.nextInt(a.length - i) + i);
        }
    }

    private static void setArray(int[] a) {
        System.out.println("enter nine number: ");
        for (int i = 0; i < a.length; i++) {
            a[i] = scanner.nextInt();
        }
        System.out.println("accepted...");
    }

    public EightDigital(int width, int height) {
        Width = width;
        Height = height;
        S0 = new int[width * height];
        Sg = new int[width * height];
//        setArray(S0, Sg);
        setRandomArray(S0);
        setRandomArray(Sg);
        K = new int[][]{
                {0, -1},
                {-Width, 0},
                {0, 1},
                {Width, 0},
        };
    }

    private void setArray(int[] s0, int[] sg) {
        int a;
        int b;
        do {
            setRandomArray(s0);
            setRandomArray(sg);
            a = getInversion(s0);
            b = getInversion(sg);
        } while ((a + b) % 2 != 0);
    }

    private int getInversion(int[] A) {
        int n = 0;
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i] != 0) {
                for (int j = i + 1; j < A.length; j++) {
                    if (A[j] != 0 && A[i] > A[j]) {
                        n++;
                    }
                }
            }
        }
        System.out.println("inversion: " + n);
        return n;
    }

    public static int[][] K2 = {
            {0, -1},
            {-1, 0},
            {0, 1},
            {1, 0},
    };

    public static char[] D = {
            '←', '↑', '→', '↓',
    };
    public int Width;
    public int Height;
    public int[][] K;

    public class PathTree {
        public int findBlank(int[] A) {
            for (int i = 0; i < A.length; i++) {
                if (A[i] == 0) {
                    return i;
                }
            }
            throw new RuntimeException("impossible");
        }

        //        int[] S0;
        Node root;
//        int[] Sg;

        public int opposite(int c) {
            return c < 0 ? -1 : (c + 2) % 4;
        }

        public PathTree() {
            this.root = new Node(-3, null, findBlank(S0), S0.clone());
        }

        public boolean isTarget(Node z) {
//            show2Array(z.toRectangle());

            return isEqual(z.toRectangle(), Sg);
        }

        public StringBuilder getPath(Node z) {

            StringBuilder builder = new StringBuilder();
            int[] steps = {0};
            getPath(z, builder, path, steps);
            return builder
                    .append("使用的最小移动次数为: " + steps[0])
                    .append('\n')
                    .append("使用的一个最优移动策略为: " + path);
        }




        public void getPath(Node z, StringBuilder builder, Queue<Character> path, int[] steps) {

            if (z == root) {
                z.rect = S0.clone();
            } else {
                getPath(z.pi, builder, path, steps);
                steps[0]++;
                z.rect = z.pi.rect;
                exchange(z.rect, z.pi.index, z.index);
                int k = opposite(z.d);
                builder.append(steps[0] + " " + D[k] + " :").append('\n');
                path.offer(D[k]);
                appendArrayString(builder, z.rect);
            }
        }

        public class Node {
            Node pi;
            int d;
            int index;
            int[] rect;

            public Node(int d, Node pi) {
                this(d, pi, pi.moveTo(d), null);
            }

            public Node(int d, Node pi, int blank) {
                this(d, pi, blank, null);
            }


            private int moveTo(int d) {
                return index + K[d][0] + K[d][1];
            }

            public Node(int d, Node pi, int blank, int[] rect) {
                this.pi = pi;
                this.d = d;
                index = blank;
                this.rect = rect;
            }


            /* public int[] toRectangle() {
                 if (rect != null) {
                     return rect;
                 } else {
                     rect = pi.toRectangle().clone();
                     exchange(rect, pi.index, index);
                     return pi.rect;
                 }
             }*/
            public int[] toRectangle() {
                toRectangle(this);
                return rect;
            }

            private void toRectangle(Node node) {//这里的rect同一个对象.....
                if (node == root) {
                    rect = S0.clone();
                } else {
                    toRectangle(node.pi);
//                    node.rect = node.pi.rect;
                    exchange(rect, node.pi.index, node.index);
                }
            }

          /*  private void toRectangle(Node node) {    -- static
                if (node == root) {
                    node.rect = S0.clone();
                } else {
                    toRectangle(node.pi);
                    node.rect = node.pi.rect;       -- 正确写法
                    exchange(node.rect, node.pi.index, node.index);
                }
            }*/

           /* private void toRectangle(Node node) {//这里的rect同一个对象.....
                if (node == root) {
                    rect = S0.clone();
                } else {
                    toRectangle(node.pi);
                    node.rect = node.pi.rect;
                    exchange(rect, node.pi.index, node.index);
                }
            }*/

        }
    }

    public static void exchange(int[] b, int j, int k) {
//        System.out.println(j + ", " + k);
        int temp = b[j];
        b[j] = b[k];
        b[k] = temp;
    }
    public void getPathStack(Stack<EightDigitalGame.D> stack) {
        PathTree pathTree = new PathTree();
        PathTree.Node z;
        int[] allSteps = {0};
        z = eightDigital(pathTree, allSteps);
        do {
            stack.push(new EightDigitalGame.D(z.index,opposite(z.d)));
            z = z.pi;
        } while (z != null);
    }
    private static boolean isEqual(int[] A, int[] B) {
        if (A.length != B.length) {
            return false;
        }
        if (A == B) {
            return true;
        }
        for (int i = 0; i < A.length; i++) {
            if (A[i] != B[i]) {
                return false;
            }
        }
        return true;
    }

    public void show2Array(int... A) {
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + "  ");
            if (i % Width == Width - 1) {
                System.out.println();
            }
        }
    }

    public PathTree.Node eightDigital(PathTree pathTree, int[] allSteps) {
        Queue<PathTree.Node> nodeQueue = new ArrayDeque<>();
        nodeQueue.offer(pathTree.root);
        while (true) {
            PathTree.Node z = nodeQueue.poll();;
            if (pathTree.isTarget(z)) {
                return z;
            }
            int i = z.index;
            int v = pathTree.opposite(z.d);//if v is -1, then this intended that can use all direction
            for (int c = 0; c < 4; c++) {
                if (c != v) {
                    int x = i / Width + K2[c][0];
                    int y = i % Width + K2[c][1];
                    if (x >= 0 && x < Height && y >= 0 && y < Width) {
                        PathTree.Node m = pathTree.new Node(c, z);
                        nodeQueue.offer(m);
                        allSteps[0]++;
                    }
                }
            }
        }
    }

    public void printPath() {
        System.out.println("S0: ");
        show2Array(S0);
        System.out.println("Sg: ");
        show2Array(Sg);
        System.out.println();
        int[] allSteps = {0};
        PathTree pathTree = new PathTree();
        PathTree.Node z;

        try {

            z = eightDigital(pathTree, allSteps);
            System.out.println(pathTree.getPath(z));

            System.out.println("一共尝试移动的次数为: " + allSteps[0]);
        } catch (NullPointerException e) {
            System.out.println("无解");
        }

    }

    private void appendArrayString(StringBuilder b, int[] clone) {
        for (int i = 0; i < clone.length; i++) {
            b.append(clone[i])
                    .append("  ");
            if (i % Width == Width - 1) {
                b.append('\n');
            }
        }
    }
}
