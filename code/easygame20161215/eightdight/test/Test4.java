package easygame20161215.eightdight.test;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by L on 2017/1/4.
 */
public class Test4 {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        Iterator<Integer> each = list.iterator();
        while (each.hasNext()) {
            if (each.next() == 5) {
                each.remove();
                break;
            }
        }
        list.forEach(System.out::println);
    }
}
