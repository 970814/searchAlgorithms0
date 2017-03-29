package easygame20161215.eightdight.test;

import easygame20161215.eightdight.Solution;

/**
 * Created by L on 2017/2/5.
 */
public class Test5 {
    public static void main(String[] args) {
        int s = 0;
        for (int i = 1; i <= 1000; i++) {
            s += i;
        }
        System.out.println(s);
        System.out.println((int) 'a');
        System.out.println((int) 'Z');
        System.out.println(new Solution().cHash('b'));

    }
}
