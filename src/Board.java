import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    int dots;
    //let's consider size of one dot = 10
    int dotSize = 25;
    int allDots = 750*750;

    int[] x = new int[allDots];
    int[] y = new int[allDots];

    int apple_x, apple_y;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean topDirection = false;
    boolean bottomDirection = false;

    boolean inGame = false;

    Timer timer;
    int DELAY = 140;

    Image headRight;
    Image headLeft;
    Image headBottom;
    Image headTop;
    Image apple;
    Image bodyDot;

    int[] random_x = {25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650, 675, 700, 725,750, 775, 800, 825, 850};
    int[] random_y = {75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625};

    private ImageIcon titleImage = new ImageIcon("./src/resources/assets/snaketitle.png");

    int level = 1;
    boolean start = false;
    boolean pause = false;

    Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.GRAY);
        loadImages();
        initGame();
    }

    public void initGame() {
        locateApple();
        //Initially Snake has three dots when game starts
        dots = 3;
        for(int z = 0; z < dots; z++) {
            y[z] = 150;
            x[z] = 150 + (dotSize*z);
        }

        initialiseTimer();
    }

    public void initialiseTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Drawing header of game
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 851, 55);
        g.drawRect(24, 74, 851, 576);

        g.setColor(Color.BLACK);
        titleImage.paintIcon(this, g, 25, 11);
        g.fillRect(25, 75,850, 575);
        doDrawing(g);
    }

    //load images from resources
    public void loadImages() {
        ImageIcon a = new ImageIcon("./src/resources/apple.png");
        apple = a.getImage();

        ImageIcon b = new ImageIcon("./src/resources/dot.png");
        bodyDot = b.getImage();

        ImageIcon c = new ImageIcon("./src/resources/h1.png");
        headLeft = c.getImage();

        ImageIcon d = new ImageIcon("./src/resources/h2.png");
        headRight = d.getImage();

        ImageIcon e = new ImageIcon("./src/resources/h3.png");
        headBottom = e.getImage();

        ImageIcon f = new ImageIcon("./src/resources/h4.png");
        headTop = f.getImage();
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        doDrawing(g);
//    }

    public void doDrawing(Graphics g) {

        //Drawing Score Section
        int score = (dots-3)*10;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("SCORE: " + score, 750, 50);


        if(inGame && !pause) {
            g.drawImage(apple, apple_x, apple_y, this);

            if(leftDirection) {
                g.drawImage(headLeft, x[0], y[0], this);
            }else if(rightDirection) {
                g.drawImage(headRight, x[0], y[0], this);
            } else if(topDirection) {
                g.drawImage(headTop, x[0], y[0], this);
            }else{
                g.drawImage(headBottom, x[0], y[0], this);
            }

            for(int z=1; z<dots; z++) {
                g.drawImage(bodyDot, x[z], y[z], this);
            }

            Toolkit.getDefaultToolkit().sync();
        }else if(pause) {
            pauseGame(g);
        }else {
            gameOver(g);
        }
    }

    //using this same method to display welcome screen and Game over
    public void gameOver(Graphics g) {
        String title = "";
        String instruction = "";
        String levMsg = level == 1 ? "Easy" : level == 2 ? "Medium" : "Hard";
        int tw = 150;
        //Start true and then Game is over
        if(start) {
            tw = 300;
            title = "Game Over";
            instruction = "Press SPACE to restart or CTRL to change level";
        }else {
            title = "Welcome To Snake Game";
            instruction = "Press SPACE to start or CTRL to change level";
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(title, tw, 300);

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(instruction, 230, 350);

        if(level == 1) g.setColor(Color.GREEN);
        else if(level == 2) g.setColor(Color.ORANGE);
        else g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.PLAIN, 30));
        g.drawString(levMsg, 380, 400);

    }

    public void pauseGame(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game is Paused", 250, 300);

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press SPACE to resume", 350, 350);
    }

    public void checkCollision() {
        //collision with its own body
        for(int z = 0; z < dots; z++) {
            if(z > 3 && x[0] == x[z] && y[0] == y[z]) inGame = false;
        }

        //collision outside the board
        if(x[0] < 25) x[0] = 850;
        if(x[0] > 850) x[0] = 25;

        if(y[0] < 75)  y[0] = 625;
        if(y[0] > 625) y[0] = 75;

        if(!inGame) {
            level = 1;
            dots = 3;
            locateApple();
            DELAY = 140;
            timer.stop();
            initialiseTimer();
        }
    }

    public void checkApple() {
        if(apple_x == x[0] && apple_y == y[0]) {
            locateApple();
            dots++;
        }
    }

    public void locateApple() {
        Random random = new Random();
        apple_x = random_x[random.nextInt(34)];
        apple_y = random_y[random.nextInt(23)];

        //Apple not come on snake body
        for(int z = dotSize; z >= 0; z--) {
            if(x[z] == apple_x && y[z] == apple_y) locateApple();
        }
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

            if(key == KeyEvent.VK_SPACE && !inGame) {
                inGame = true;
                start = true;
            }else if(key == KeyEvent.VK_SPACE && inGame) {
                pause = pause ? false : true;
            }

            if(key == KeyEvent.VK_CONTROL && !inGame) {
                if(level == 3) level = 1;
                else level++;

                timer.stop();
                if(level == 1) DELAY = 140;
                else if(level == 2) DELAY = 100;
                else DELAY = 60;
                initialiseTimer();
            }
        }
    }
}
