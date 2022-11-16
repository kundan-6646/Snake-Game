import javax.swing.*;

public class SnakeGame {
    JFrame frame;
    SnakeGame() {
        frame = new JFrame("Snake Game");
        Board board = new Board();
        frame.add(board);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(10, 10,905, 700);
    }

    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame();
    }
}