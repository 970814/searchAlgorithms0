package easygame20161215;



import java.awt.*;

/**
 * Created by computer on 2016/12/15.
 */
public class Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new EightDigitalGame(2, 1).setVisible(true));

    }
    /*
    B  5  A  6
    D  7  E  4
    F  0  8  9
    1  C  2  3
    C  0  B  F
    7  A  6  5
    2  E  8  4
    1  D  3  9
     */
}