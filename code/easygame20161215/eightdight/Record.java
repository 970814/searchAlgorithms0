package easygame20161215.eightdight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pc on 2016/12/31.
 */
@SuppressWarnings("Duplicates")
public class Record {
    public static int DefaultSize = 11;
    public int MaxSize;
    public int[] f;
    public ArrayList<Integer> list;
    public boolean[] visited = null;
    public List[] hashLists = null;
    public int states;
    boolean useHash = false;
    boolean useDBFS = false;
    final int unknownSize;

    public Record(int size) {
        if (size > DefaultSize) {
            unknownSize = size - DefaultSize;
            useHash = true;//当尺寸过大时候，无法映射所有可能的矩阵
            size = DefaultSize;
        } else {
            unknownSize = 0;
        }

        MaxSize = size;
        f = new int[MaxSize];
        f[0] = 1;
        for (int i = 1; i < f.length; i++) {
            f[i] = f[i - 1] * i;
        }
        list = new ArrayList<>(MaxSize);
        for (int i = 0; i < MaxSize; i++) {
            list.add(i);
        }
        states = f[f.length - 1] * f.length;
    }

    public void setMaxSize(int size) {

    }

    public boolean isVisited(char[] A) {
        if (useHash) {
            return isVisitedHash(A);
        } else {
            return isVisited0(A);
        }
    }
    private boolean isVisited0(char[] A) {
        int code = encode(A);
        boolean result = visited[code];
        visited[code] = true;
        return result;
    }
    private boolean isVisitedHash(char[] A) {
        int h = hashCode(A);
        if (hashLists[h] == null) {
            hashLists[h] = new ArrayList<>();
        }
        List<char[]> list = hashLists[h];
        for (char[] rect : list) {
            boolean isSame = true;

            for (int i = 0; i < unknownSize; i++) {
                if (rect[i] != A[i]) {
                    isSame = false;
                    break;
                }
            }

            if (isSame) {
                return true;
            }
        }
//        list.add(A);
        list.add(Arrays.copyOfRange(A, 0, unknownSize));
        return false;
    }

    public void useModel(String model) {
        if (model.equals("DBFS")) {
            useDBFS = true;
        }
        if (useHash || useDBFS) {
            hashLists = new List[states];
        } else {
            visited = new boolean[states];
        }
    }

    public int isVisitedByDBFS(char[] A, boolean isS0) {
        int h = hashCode(A);
        if (hashLists[h] == null) {
            hashLists[h] = new ArrayList<>();
        }
        List<UsedByDBFS> list = hashLists[h];
        for (UsedByDBFS rect : list) {
            boolean isSame = true;
            for (int i = 0; i < unknownSize; i++) {
                if (rect.rect[i] != A[i]) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                if (isS0 == rect.isS0) {
                    return 1;//is visited
                } else {
                    return 2;//search the result
                }
            }
        }
        list.add(new UsedByDBFS(Arrays.copyOfRange(A, 0, unknownSize), isS0));//对于每个矩阵可少储存9个空间
        return 0;//not visited
    }

    public int encode(char[] A) {
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

    public int hashCode(char[] A) {//如果前半部分相同，那么如果后半部分的hash相同，则两个矩形全等
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

    public int[] decode(int code) {
        int[] A = new int[MaxSize];
        ArrayList<Integer> copy = (ArrayList<Integer>) list.clone();
        for (int i = 0; i < A.length; i++) {
            int which = code / f[MaxSize - 1 - i];
            A[i] = copy.remove(which);
            code %= f[MaxSize - 1 - i];
        }
        return A;
    }

    public void useModel() {
        useModel("");
    }
}
