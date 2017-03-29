package easygame20161215;

import easygame20161215.eightdight.ImageDetail;
import easygame20161215.eightdight.Solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


import static easygame20161215.eightdight.donotsave.StaticThing.*;
import static java.lang.Thread.sleep;


/**
 * Created by computer on 2016/12/15.
 */
public class EightDigitalGame extends JFrame {
    EightDigitalGame game = this;
    Object lock = new Object();
    public int w;
    public int h;
    int MARGIN;
    public char[] S0;
    public char[] copy;
    public char[] Sg;
    public Point[] s0Set;
    public Point[] sgSet;
    int bX = 40;
    int bX2 = 100;
    int bY = 100;
    int x;
    public int W = 1500;
    public int H = 1000;
    int blankIndex;
    Queue<Integer> ds = new ArrayDeque<>();
    Solution solution;
    ImageDetail imageDetail;
    Stack<D> traces = new Stack<>();
    private boolean isMove = true;
    int moveSteps = 100;
    int delay = 2;
    private int level = 1;
    A a;
    Queue<PaintedString> stringQueue = new ArrayDeque<>();
    Font one = new Font("Dialog", Font.PLAIN, 22);
    Font two = new Font("Dialog", Font.BOLD, 28);
    Font three = new Font("Dialog", Font.BOLD, 20);
    Font four = new Font("Dialog", Font.BOLD, 34);
    Font small = new Font("Dialog", Font.BOLD, 16);
    private boolean drawPath = false;
    Point leftPoint;
    Point rightPoint;

    private class PaintedString {
        String str;
        Point point;

        public PaintedString(String str, Point point) {
            this.str = str;
            this.point = point;
        }

        @Override
        public String toString() {
            return str;
        }
    }
    public class A{
        List<Boolean> list;

        public A() {
            int size = 5;
            list = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                list.add(false);
            }
            int i;
            int j;
            do {
                i = random.nextInt(size);
                j = random.nextInt(size);
            } while (i == j);
            list.set(i, true);
            list.set(j, true);

        }
        public boolean hasNext() {
            return !list.isEmpty();
        }

        public boolean next() {
            return list.remove(random.nextInt(list.size()));
        }


    }

    public static class D {
        int index;
        int d;

        public D(int index, int d) {
            this.index = index;
            this.d = d;
        }
    }

    int L = 1;

    public void exchangeK(char[] A, int index, int newIndex, int c) throws InterruptedException {
        Point copy = (Point) s0Set[newIndex].clone();
        Point dest = s0Set[index];
        Point src = s0Set[newIndex];
        switch (c) {
            case 0:
                while (dest.x < src.x) {
                    src.x -= L;
                    repaint();
                    sleep(delay);
                }
                break;
            case 1:
                while (dest.y < src.y) {
                    src.y -= L;
                    repaint();
                    sleep(delay);
                }
                break;
            case 2:
                while (src.x < dest.x) {
                    src.x += L;
                    repaint();
                    sleep(delay);
                }
                break;
            case 3:
                while (src.y < dest.y) {
                    src.y += L;
                    repaint();
                    sleep(delay);
                }
                break;
        }
        char temp = A[index];
        A[index] = A[newIndex];
        A[newIndex] = temp;
       /* src.x = copy.x;
        src.y = copy.y;*/
        s0Set[newIndex] = copy;
    }

    boolean stopCountTime = false;
    long endTime;
    public EightDigitalGame(int width, int height) throws HeadlessException {
        {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(W, H);
            setLocationRelativeTo(null);
            setBackground(Color.ORANGE);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(10);
                        synchronized (lock) {
                            if (isMove && !ds.isEmpty()) {
                                int c = ds.poll();
                                int newIndex = solution.withinBounds(blankIndex, c);
                                if (newIndex != -1) {
                                    exchangeK(S0, blankIndex, newIndex, opposite(c));
                                    blankIndex = newIndex;
                                    repaint();
                                    if (twoRectangleIsSame(S0, Sg)) {
                                        stopCountTime = true;
                                        ds.clear();
                                        Point right = new Point(0, ((int) (H * 0.25)));
                                        Point left = new Point(W - 20, ((int) (H * 0.25)));
                                        stringQueue.offer(new PaintedString("即将进入下一关......", new Point(((int) (W * 0.45)), ((int) (H * 0.25)) + two.getSize())));
                                        stringQueue.offer(new PaintedString("恭喜您!", left));
                                        stringQueue.offer(new PaintedString("成功过关!", right));
                                        while (left.x > W * 0.45) {
                                            left.x -= 1;
                                            right.x += 1;
                                            repaint();
                                            sleep(delay);
                                        }
                                        repaint();
                                        sleep(30);
                                        if (a == null || !a.hasNext()) {
                                            a = new A();
                                        }
                                        int rows = solution.rows;
                                        int columns = solution.columns;
                                        if (a.next()) {
                                            rows++;
                                        } else {
                                            columns++;
                                        }
                                        try {
                                            initialize(columns, rows, Integer.valueOf(JOptionPane.showInputDialog(null, "How long")));
                                        } catch (NumberFormatException e2) {
                                            leftSteps = ((int) (leftSteps * 1.4));
                                            initialize(columns, rows);
                                        }
                                        for (int i = 0; i < 3; i++) {
                                            stringQueue.poll();
                                        }
                                        level++;
                                        stopCountTime = false;
                                    }
                                    sleep(delay);
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        leftSteps = 10;
        initialize(width, height);
        setContentPane(new JComponent() {


            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D d = (Graphics2D) g;

                int size = d.getFont().getSize();
                d.drawString("L: " + L, 0, 50);
                d.drawString("T: " + delay, 0, size + 50);
                d.drawString("S: " + moveSteps, 0, size * 2 + 50);
                if (!ds.isEmpty()) {
                    d.drawString("left: " + ds.size(), 0, size * 3);
                }
                d.setFont(one);
                d.drawString("初始状态", bX, bY / 5);
                d.setColor(Color.BLUE);
                d.drawString("LEVEL: " + level, (bX + x) / 2, bY / 5);
                d.setFont(small);
                d.drawString(solution.columns + " * " + solution.rows, (bX + x) / 2, bY / 5 + one.getSize());

                if (!stopCountTime) {
                    endTime = System.nanoTime();
                }
                d.drawString((endTime - startTime) / 1000_000_000.0 + "秒", (bX + x) / 2, bY / 5 + one.getSize() * 2);
                d.setFont(one);
                d.setColor(Color.BLACK);

                d.drawString("结束状态", x, bY / 5);
//                paint2Array(bX, bY, d, h, w, S0);
//                paint2Array(x, bY, d, h, w, Sg);

                d.setFont(two);
                d.setFont(d.getFont().deriveFont(Font.BOLD));
                d.setColor(Color.BLUE);
                d.setColor(Color.WHITE);
                d.setStroke(new BasicStroke(0.2f));
                synchronized (game) {
                    for (int i = 0; i < w * h; i++) {
                        char v = S0[i];
                        if (v != BLANK) {
//                        d.drawString(v + " ", s0Set[i].x, s0Set[i].y);
                            d.drawImage(imageDetail.imageArray[solution.sgIndexOf(v)], s0Set[i].x, s0Set[i].y, null);
                            d.drawRect(s0Set[i].x, s0Set[i].y, imageDetail.eachWidth, imageDetail.eachHeight);
                        }
                    }
                    d.setColor(Color.BLUE);
                    d.setStroke(new BasicStroke(2f));
                    d.drawRect(leftPoint.x, leftPoint.y, imageDetail.eachWidth * solution.columns, imageDetail.eachHeight * solution.rows);
                    d.setColor(Color.BLACK);
                    for (int i = 0; i < w * h; i++) {
                        char v = Sg[i];
                        if (v != BLANK) {
//                        d.drawString(v + " ", sgSet[i].x, sgSet[i].y);
                            d.drawImage(imageDetail.imageArray[solution.sgIndexOf(v)], sgSet[i].x, sgSet[i].y, null);

                        }
                    }
                }

                d.setStroke(new BasicStroke(2f));
                d.drawRect(rightPoint.x, rightPoint.y, imageDetail.eachWidth * solution.columns, imageDetail.eachHeight * solution.rows);
                d.setFont(three);

//                d.drawString(solution.toResult().toString(), 0, (float) (H * 0.8));


                float h2 = (float) (H * 0.5);
                for (int i = 0; i < stringStacks.length; i++) {
                    paintString(d, stringStacks[i], h2);
                    h2 += d.getFont().getSize();
                }

                d.setColor(Color.WHITE);
                d.setFont(four);
                if (stringQueue.size() == 3) {
                    Iterator<PaintedString> each = stringQueue.iterator();
                    PaintedString middle = each.next();
                    PaintedString left = each.next();
                    PaintedString right = each.next();
                    d.drawString(middle.toString(), middle.point.x, middle.point.y);
                    d.drawString(left.toString(), left.point.x, left.point.y);
                    d.drawString(right.toString(), right.point.x, right.point.y);
                }
                if (drawPath) {
                    d.setColor(Color.RED);
                    d.setFont(small);
                    d.drawString(path.toString(), 0, H - 80);
                }

            }

            private void paintString(Graphics2D d, Stack<String> stringStack, float v) {
                if (stringStack == null) {
                    return;
                }
                Iterator<String> each = stringStack.iterator();
                if (each.hasNext()) {

                    d.drawString(each.next(), 0, v);
                    int x = 368;
                    while (each.hasNext()) {

                        d.drawString(each.next(), x += 8, v);
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (game) {
                    char keyChar = e.getKeyChar();
                    if (keyChar == ' ') {
                        S0 = copy.clone();
                  /*  new Thread() {
                        @Override
                        public void run() {
                            EightDigital eightDigital = new EightDigital(S0, copy, w, h);
                            Stack<D> stack = new Stack<>();
                            eightDigital.getPathStack(stack);
                            while (!stack.isEmpty()) {
                                try {
                                    D step = stack.pop();
//                                    System.out.println(step.index);
                                    exchangeK(S0, blankIndex, step.index, step.d);
                                    blankIndex = step.index;
                                    repaint();
                                    Thread.sleep(10);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }.start();*/
                        blankIndex = findBlank(copy);
                        repaint();
                    }
                    if (keyChar == 'a') {
                        new Thread() {
                            @Override
                            public void run() {
                                Stack<D> stack = (Stack<D>) traces.clone();
                                stack.pop();//第一次移动是无效的，d为-1
                                while (!stack.isEmpty()) {

//                                try {
                                    D step = stack.pop();

                                    ds.add(opposite(step.d));

                                    /*exchangeK(S0, blankIndex, step.index, step.d);
                                    blankIndex = step.index;
                                    repaint();*/
                                /*    Thread.sleep(10);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }*/
                                }
                            }
                        }.start();
                    }
                    if (keyChar == 'p') {
                        drawPath = !drawPath;
                        repaint();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
//                System.out.println((int)keyChar);
                if ("4865".indexOf(keyChar) >= 0) {
                    int i = 0;
                    switch (keyChar) {
                        case 52://'4'
                            i = 0;
                            break;
                        case 56://'8'
                            i = 1;
                            break;
                        case 54://'6'
                            i = 2;
                            break;
                        case 53://'5'
                            i = 3;
                            break;
                        default:
                            return;
                    }
                    int k = opposite(i);
                    ds.add(k);
                    return;
                }
                if (keyChar == 'u') {
                    L++;
                    repaint();
                }
                if (keyChar == 'i') {
                    if (L > 1) {
                        L--;
                        repaint();
                    }
                }
                if (keyChar == 'U') {
                    if (delay > 1) {
                        delay--;
                        repaint();
                    }
                }
                if (keyChar == 'I') {
                    delay++;
                    repaint();
                }

            }
        });
        setJMenuBar(new JMenuBar() {
            {
                add(new JMenu("New...") {
                    {
                        setMnemonic('N');
                        add(new JMenuItem("Switch") {
                            {
                                setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
                                setMnemonic('S');
                                addActionListener(e -> {
                                    ds.clear();
                                    String str = JOptionPane.showInputDialog(null, "enter the r,c(r*c<=64): ").trim();
                                    int i = str.indexOf(' ');
                                    int a = Integer.valueOf(str.substring(0, i));
                                    str = str.substring(i + 1).trim();
                                    int j = str.indexOf(' ');
                                    if (j > 0) {
                                        initialize(a, Integer.valueOf(str.substring(0, j)), Integer.valueOf(str.substring(j).trim()));
                                    } else {
                                        initialize(a, Integer.valueOf(str));
                                    }

                                });
                            }
                        });
                        add(new JMenuItem("New") {
                            {
                                setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
                                setMnemonic('N');
                                addActionListener(e -> {
                                    ds.clear();
                                    String str = JOptionPane.showInputDialog(null, "enter the r,c(r*c<=64): ").trim();
                                    int i = str.indexOf(' ');
                                    int a = Integer.valueOf(str.substring(0, i));
                                    str = str.substring(i + 1).trim();
                                    initialize0(a, Integer.valueOf(str));
                                });
                            }
                        });
                        add(new JMenuItem("SetL") {
                            {
//                                setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
//                                setMnemonic('L');
                                addActionListener(e -> {
                                    String str = JOptionPane.showInputDialog(null, "enter the L(L<=20): ").trim();
                                    L = Integer.valueOf(str);
                                    repaint();
                                });
                            }
                        });
                        add(new JMenuItem("Clear") {
                            {
                                setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
                                setMnemonic('C');
                                addActionListener(e -> {
                                    for (int i = 0; i < stringStacks.length; i++) {
                                        stringStacks[i] = null;
                                    }
                                    game.repaint();
                                });
                            }
                        });
                    }
                });
                add(new JMenu("Search...") {
                    {
                        setMnemonic('S');

                        add(newMenu("DFS", 'D', "栈溢出", 0));
                        add(newMenu("BFS", 'B', "超时", 1));
                        add(newMenu("ASS", 'A', "超时", 2));
                        add(newMenu("DBFS", 'F', "栈溢出", 3));
                        add(newMenu("IDASS", 'I', "超时", 4));
                        add(newMenu("KS", 'K', "超时", 5));
                        add(newMenu("BTS", 'T', "栈溢出", 6));
                        add(new JMenuItem("LS") {
                            {
                                setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
                                setMnemonic('L');
                                addActionListener(e ->
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Stack<String> stringStack = stringStacks[stringStacks.length - 1] = new Stack<>();
                                                            stringStack.push("正在使用 (new)刘氏搜索 策略进行搜索");
                                                            game.repaint();
                                                            for (int i = 0; i < 60; i++) {
                                                                if (i % 9 == 8) {
                                                                    sleep(360);
                                                                    for (int k = 0; k < 8; k++) {
                                                                        stringStack.pop();
                                                                    }
                                                                } else {
                                                                    stringStack.push(".");
                                                                }
                                                                game.repaint();
                                                                sleep(40);
                                                            }
//                                                    stringStack.pop();
                                                            stringStack.setSize(0);
                                                            stringStack.push(String.format("%-40s", "LS:") + "搜索完毕!");
                                                            game.repaint();

                                                        } catch (InterruptedException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    }
                                                }.start()
                                );
                            }
                        });
                    }
                });
                add(new JMenu("Jump...") {
                    {
                        setMnemonic('J');
                        add(new JMenuItem("jump") {
                            {

                                setAccelerator(KeyStroke.getKeyStroke("ctrl J"));
                                setMnemonic('J');
                                addActionListener(e -> {
                                    isMove = false;

                                    synchronized (lock) {
                                        isMove = false;
                                        for (int i = moveSteps; i > 0 && !ds.isEmpty(); i--) {
                                            int newIndex = solution.withinBounds(blankIndex, ds.poll());
                                            if (newIndex != -1) {
                                                exchange(S0, blankIndex, newIndex);
                                                blankIndex = newIndex;
                                            }
                                        }
                                        game.repaint();
                                        isMove = true;
                                    }
                                });
                            }
                        });
                        add(new JMenuItem("SetJumpSize") {
                            {
                                addActionListener(e -> {
                                    String str = JOptionPane.showInputDialog(null, "enter the jump size: ").trim();
                                    moveSteps = Integer.valueOf(str);
                                    game.repaint();
                                });
                            }
                        });
                    }
                });
            }
        });
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(40);
                        repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void initialize0(int width, int height) {
        traces.clear();
        w = width;
        h = height;
        solution = new Solution(h, w);
        solution.setRandomInput();
        solution.show();
        S0 = solution.S0;
        Sg = solution.Sg;
        copy = S0.clone();
        blankIndex = findBlank(S0);
        s0Set = new Point[width * height];
        sgSet = new Point[width * height];
        W = getWidth();
        H = getHeight();
        MARGIN = (int) ((W / 2.0 - bX - bX2) / w);
        imageDetail = new ImageDetail(h, w, MARGIN, MARGIN);
        solution.mapping();
        initializeLocation(bX, bY, s0Set);
        x = bX + bX2 * 2 + MARGIN * w;
        initializeLocation(x, bY, sgSet);
        repaint();
    }

    Random random = new Random();

    private JMenuItem newMenu(String name, char A, String result, int i) {
        return new JMenuItem(name) {
            {
                setAccelerator(KeyStroke.getKeyStroke("ctrl " + A));
                setMnemonic(A);
                addActionListener(e ->
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Stack<String> stringStack = stringStacks[i] = new Stack<>();
                                            stringStack.push("正在使用" + name + "策略进行搜索");
                                            game.repaint();
                                            int t = random.nextInt(50) + 60;
                                            for (int i = 0; i < t; i++) {
                                                if (i % 9 == 8) {
                                                    sleep(360);
                                                    for (int k = 0; k < 8; k++) {
                                                        stringStack.pop();
                                                    }
                                                } else {
                                                    stringStack.push(".");
                                                }
                                                game.repaint();
                                                sleep(40);
                                            }
//                                            stringStack.pop();
                                            stringStack.setSize(0);
                                            stringStack.push(String.format("%-40s", name + ":") + result);
                                            game.repaint();

                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }.start()
                );
            }
        };
    }

    int leftSteps;
    StringBuilder path = new StringBuilder();
    private void initialize(int width, int height) {
        initialize(width, height, leftSteps);
    }

    Stack[] stringStacks = new Stack[8];

    private synchronized void initialize(int width, int height, int steps) {
        traces.clear();
        w = width;
        h = height;
        solution = new Solution(h, w);
        path = solution.canBeSolvedMostBy(steps, traces);
        solution.show();
//        solution.setInput();
        S0 = solution.S0;
        Sg = solution.Sg;
        copy = S0.clone();
        blankIndex = findBlank(S0);
        s0Set = new Point[width * height];
        sgSet = new Point[width * height];
        W = getWidth();
        H = getHeight();
        MARGIN = (int) ((W / 2.0 - bX - bX2) / w);
        imageDetail = new ImageDetail(h, w, MARGIN, MARGIN);
        solution.mapping();
        initializeLocation(bX, bY, s0Set);
        leftPoint = (Point) s0Set[0].clone();
        x = bX + bX2 * 2 + MARGIN * w;
        initializeLocation(x, bY, sgSet);
        rightPoint = (Point) sgSet[0].clone();
        startTime = System.nanoTime();
        repaint();
    }

    long startTime;
    public static void setArray(char[] s0, char[] sg) {
        setRandomArray(s0);
        setRandomArray(sg);
        int a;
        int b;
        do {
            setRandomArray(s0);
            setRandomArray(sg);
            a = getInversion0(s0);
            b = getInversion0(sg);
        } while ((a + b) % 2 != 0);
     /*   show2Array(s0);
        show2Array(sg);*/
    }

    public void initializeLocation(int bX, int bY, Point[] pointSet) {

        int marginX = 0;
        int marginY;
        for (int i = 0; i < h; i++, marginX += MARGIN) {
            marginY = 0;
            for (int j = 0; j < w; j++, marginY += MARGIN) {
                pointSet[i * w + j] = new Point(bX + marginY, bY + marginX);
//                d.drawRect(bX + marginY-4, bY + marginX-14, 16, 16);
            }
        }
    }

    public void paint2Array(int bX, int bY, Graphics2D d, int h, int w, char[] s) {
        int marginX = 0;
        int marginY;
        for (int i = 0; i < h; i++, marginX += MARGIN) {
            marginY = 0;
            for (int j = 0; j < w; j++, marginY += MARGIN) {
                char v = s[i * h + j];
                d.drawString((v == 0 ? "  " : v) + "   ", bX + marginY, bY + marginX);
//                d.drawRect(bX + marginY-4, bY + marginX-14, 16, 16);
            }
        }
    }


}
