import javax.swing.*;

public class Board extends JPanel {
    int height = 400;
    int width = 400;
    int dots = 0;

    //let's consider size of one dot = 10
    int allDots = 40*40;

    int[] x = new int[allDots];
    int[] y = new int[allDots];

    int apple_x, apple_y;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean topDirection = false;
    boolean bottomDirection = false;

    Timer timer;


    Board() {

    }

}
