import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int height = 400;
    int width = 400;
    int dots;
    //let's consider size of one dot = 10
    int dotSize = 10;
    int allDots = 40*40;

    int[] x = new int[allDots];
    int[] y = new int[allDots];

    int apple_x, apple_y;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean topDirection = false;
    boolean bottomDirection = false;

    boolean inGame = true;

    Timer timer;
    int DELAY = 140;

    Image head;
    Image apple;
    Image bodyDot;

    Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));
        loadImages();
        initGame();
    }

    public void initGame() {
        locateApple();
        //Initially Snake has three dots when game starts
        dots = 3;
        for(int z = 0; z < dots; z++) {
            y[z] = 50;
            x[z] = 50 + (dotSize*z);
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    //load images from resources
    public void loadImages() {
        ImageIcon a = new ImageIcon("./src/resources/apple.png");
        apple = a.getImage();

        ImageIcon b = new ImageIcon("./src/resources/dot.png");
        bodyDot = b.getImage();

        ImageIcon c = new ImageIcon("./src/resources/head.png");
        head = c.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g) {
        if(inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            g.drawImage(head, x[0], y[0], this);
            for(int z=1; z<dots; z++) {
                g.drawImage(bodyDot, x[z], y[z], this);
            }

            Toolkit.getDefaultToolkit().sync();
        }else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over";
        Font f = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(f);

        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString(msg, (width - fontMetrics.stringWidth(msg)) / 2, height / 2);
    }

    public void checkCollision() {
        //collision with it's own body
        for(int z = 0; z < dots; z++) {
            if(z > 3 && x[0] == x[z] && y[0] == y[z]) inGame = false;
        }

        //collision outside the board
        if(x[0] < 0 || x[0] >= width) inGame = false;
        if(y[0] < 0 || y [0] >= height) inGame = false;

        if(!inGame) {
            timer.stop();
        }
    }

    public void checkApple() {
        if(apple_x == x[0] && apple_y == y[0]) {
            locateApple();
            dots++;
        }
    }

    public void locateApple() {
        int x = (int)(Math.random() * 39);
        apple_x = x*10;
        int y = (int)(Math.random() * 39);
        apple_y = y*10;
    }

    public void move() {
        for(int z = dots - 1; z > 0; z--) {
            x[z] = x[z-1];
            y[z] = y[z-1];
        }

        if(leftDirection) {
            x[0] -= dotSize;
        }

        if(rightDirection) {
            x[0] += dotSize;
        }

        if(topDirection) {
            y[0] -= dotSize;
        }

        if(bottomDirection) {
            y[0] += dotSize;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            checkCollision();
            checkApple();
            move();
        }
        repaint();
    }

    public  class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                topDirection = false;
                bottomDirection = false;
            }

            if(key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                topDirection = false;
                bottomDirection = false;
            }

            if(key == KeyEvent.VK_UP && !bottomDirection) {
                topDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if(key == KeyEvent.VK_DOWN && !topDirection) {
                bottomDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
