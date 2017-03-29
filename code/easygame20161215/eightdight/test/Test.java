package easygame20161215.eightdight.test;

import easygame20161215.eightdight.Search;
import easygame20161215.eightdight.Solution;

import java.util.function.Consumer;

import static easygame20161215.eightdight.donotsave.StaticThing.D;
import static java.lang.System.nanoTime;
import static testandLeetCode.TwoSum.time;

/**
 * Created by L on 2016/12/27.
 */
public class Test {
    public static void main(String[] args) {

        Solution solution = new Solution(6, 6);
        solution.setInput();

//        scanner.nextLine();
//        int[] path = solution.canBeSolvedMostBy(140);
        solution.show();

        f0(Search::IDASS, solution);
        System.out.println("IDASS...");
        f(Search::DBFS, solution);
        System.out.println("DBFS...");
        f(Search::ASS, solution);
        System.out.println("ASS...");

//        solution.path = null;


//        f(Search::BFS, solution);
//        System.gc();
//        printPath(path);
//        scanner.nextLine();
    }

    private static void f(Consumer<Solution> action, Solution s) {
        long start = nanoTime();
        action.accept(s);
        long end = nanoTime();
        System.out.println(s.toResult());
        System.out.println(time(end - start));
    }
    private static void f0(Consumer<Solution> action, Solution s) {
        long start = nanoTime();
        action.accept(s);
        long end = nanoTime();
        System.out.println("depth: " + s.nIDASS);
        for (int i = 0; i < s.nIDASS; i++) {
            System.out.print(D[s.pathIDASS[i]] + ", ");
        }
        System.out.println(time(end - start));
    }
    //630.55mb

    /*
     6  3  A  0    	F  5  A  7
     5  F  8  7    	6  B  8  4
     9  C  B  D    	C  9  2  0
     1  E  4  2    	E  1  D  3
     */
/**
 L  O  J  0  6
 C  H  F  K  3
 G  A  N  5  8
 B  4  M  7  1
 E  9  I  D  2
 C  H  L  5  K
 A  3  7  N  J
 O  9  8  F  6
 4  E  B  M  1
 I  0  G  D  2
 */


    /**
     A  G  H  B  I
     K  F  L  7  5
     E  D  0  O  N
     C  1  3  M  4
     9  2  J  6  8
     H  B  F  I  5
     A  L  G  K  7
     9  E  2  O  D
     C  0  1  6  N
     3  M  J  8  4
     */
    /**
     D  9  6  B  7  J
     5  M  A  4  R  X
     O  S  H  W  Y  Q
     1  0  I  8  E  V
     2  G  3  P  L  C
     N  F  T  Z  K  U
     9  A  W  4  B  J
     D  6  M  H  7  R
     5  S  I  C  U  L
     N  2  8  V  Q  X
     1  3  T  Z  0  K
     O  F  P  G  Y  E
     →, ↓, ↓, ↓, ←, ←, ↑, ←, ↑, →, ↑, ↑, →, →, ↑, ←, ←, ↓, →, →, ↓, ←, ↑, ↑, →, ↓, ↓, ←, ←, ↑, ←, ←, ↓, ←, ↑, ↑, →, ↓, →, ↑, →, ↓, →, ↓, ←, ←, ←, ↓, ←, ↑, ↑, →, ↓, ↓, ←, ↓, →, ↓, →, ↑, →, →, ↓, ←, ←, ↑, ↑, ←, ←, ↑, ↑, ↑, →, ↓, ↓, ↓, ←, ↑, →, ↑,
     */
}//T(n) up  39.4%     S(n) up 54.7%


/*      difficult
        2  6  4
        1  3  7
        0  5  8

        8  1  5
        7  3  6
        4  0  2*/


    /*
     使用该算法：ASS搜索效率特别低
     1  2  B  7
        4  F  5  9
        D  E  0  8
        A  C  3  6
        1  4  7  5
        F  0  2  B
        A  3  E  9
        D  C  6  8*/

//[←, ↑, →, →, ↓, ←, ↓, ←, ↑, →, ↑, →, ↓, ↓, ←, ↑, ↑]

 /*
   使用该算法：ASS搜索效率特别低
   F  D  9  E
        B  A  2  C
        6  5  7  3
        4  1  0  8
        D  0  9  E
        F  4  A  3
        1  5  2  C
        6  B  7  8 */