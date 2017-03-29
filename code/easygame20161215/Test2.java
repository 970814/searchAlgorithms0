package easygame20161215;

/**
 * Created by pc on 2016/12/23.
 */
public class Test2 {
    public static int getInversion(int[] A) {
        int n = 0;
        for (int i = 0; i < A.length - 1; i++) {
            for (int j = i + 1; j < A.length; j++) {
                if (A[i] > A[j]) {
                    n++;
                }
            }
        }
        return n;
    }

    public static int getInversion0(int[] A) {
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

    public static void main(String[] args) {

       /* char ch = 'z';
        for (int i = 0; i < 8; i++) {
            System.out.println((char) (ch + i));
            System.out.println((ch + i));
        }*/
        System.out.println((char)(126));
        for (int i = 127; i < 1127; i++) {
            System.out.printf("\\u%04x",i);
        }
        System.out.println(getInversion(new int[]{4, 3, 1, 2}));
    }
}
