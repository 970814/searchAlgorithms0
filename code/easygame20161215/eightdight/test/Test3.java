package easygame20161215.eightdight.test;

import easygame20161215.eightdight.Solution;

/**
 * Created by L on 2017/1/4.
 */
public class Test3 {
    public static void main(String[] args) {
        System.out.println(new Solution() {
            {
                setInput();
                mapping();

            }
        }.s0Value());
    }
}
