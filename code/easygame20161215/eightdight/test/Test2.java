package easygame20161215.eightdight.test;

import testandLeetCode.Permutation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pc on 2016/12/31.
 */
public class Test2 {
    public static int MaxSize = 9;
    public static int[] f;
    public static ArrayList<Integer> list;

    static {
        f = new int[MaxSize];
        f[0] = 1;
        for (int i = 1; i < f.length; i++) {
            f[i] = f[i - 1] * i;
        }
        list = new ArrayList<>(MaxSize);
        for (int i = 0; i < MaxSize; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) {

        System.out.println(hashCode(11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
        System.out.println(hashCode(11, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
    }

    public static void test() {
        int[] A = new int[9];
        for (int i = 0; i < A.length; i++) {
            A[i] = i;
        }
        int[] B;
        do {
            int code = encode(A);
            B = decode(code);
            System.out.println(Arrays.toString(A) + " code=" + code + " decode: " + Arrays.toString(B));
        } while (twoRectangleIsSame(A, B) && Permutation.nextPermutation(A));
    }

    public static int encode(int[] A) {
        int code = 0;
        for (int i = 0; i < A.length - 1; i++) {
            int c = 0;
            for (int j = i + 1; j < A.length; j++) {
                if (A[i] > A[j]) {
                    c++;
                }
            }
            code += c * f[MaxSize - 1 - i];
        }
        return code;
    }

    public static int hashCode(int... A) {//如果前半部分相同，那么如果后半部分的hash相同，则两个矩形全等
        int n = A.length;
        int code = 0;
        int startIndex = n - MaxSize;
        for (int i = startIndex; i < n - 1; i++) {
            int c = 0;
            for (int j = i + 1; j < n; j++) {
                if (A[i] > A[j]) {
                    c++;
                }
            }
            code += c * f[MaxSize - 1 - i + startIndex];
        }
        return code;
    }

    public static int[] decode(int code) {
        int[] A = new int[MaxSize];
        ArrayList<Integer> copy = (ArrayList<Integer>) list.clone();
        for (int i = 0; i < A.length; i++) {
            int which = code / f[MaxSize - 1 - i];
            A[i] = copy.remove(which);
            code %= f[MaxSize - 1 - i];
        }
        return A;
    }

    public static boolean twoRectangleIsSame(int[] A, int[] B) {
        if (A == B)
            return true;
        if (A.length != B.length)
            return false;
        for (int i = 0; i < A.length; i++)
            if (A[i] != B[i])
                return false;
        return true;
    }
}
