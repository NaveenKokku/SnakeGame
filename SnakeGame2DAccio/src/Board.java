import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//import java.util.*;
//import java.util.Timer;


public class Board extends JPanel implements ActionListener {
    int bWidth = 400;
    int bHeight = 400;
    int maxDots = 1600;
    int dotSize = 10;
    int dots;
    int x[] = new int[maxDots];
    int y[] = new int[maxDots];
    int highScore = 0;
    Image body, head, apple;
    int xApple;
    int yApple;
    Timer timer;
    int delay = 150;
    boolean inGame = true;
    boolean keyPressLeft = true;
    boolean keyPressRight = false;
    boolean keyPressUp = false;
    boolean keyPressDown = false;

    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(bWidth, bHeight));
        setBackground(Color.black);
        initGame();
        loadImages();

    }

    public void initGame(){
      dots = 3;
      x[0] = 150;
      y[0] = 150;
      for(int i = 1; i < dots; i++){
          x[i] = x[0] + dotSize*i;
          y[i] = y[0];
      }
      locateApple();
     timer = new Timer(delay, this);
     timer.start();

    }
    //load images from resources folder to image object
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon( "src/resources/dot.png");
        body = bodyIcon.getImage();

        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    //dram images at snake and apple position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }


    public void doDrawing(Graphics g){
        if(inGame) {
            g.drawImage(apple, xApple, yApple, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[0], y[0], this);
                } else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else{
            if((dots-3) > highScore){
                highScore = (dots-3)*100;
            }
           gameOver(g);
           timer.stop();
        }
    }

    //this method is used to locate the position of the apple randomly at different places everytime
    public void locateApple(){
         xApple = ((int)(Math.random()*39))*dotSize;
         yApple = ((int)(Math.random()*39))*dotSize;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame) {
            checkFruit();
            checkCollision();
            move();
        }
        repaint();
    }

    public void move(){
        for(int i = dots-1; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(keyPressLeft){
            x[0]-=dotSize;
        }
        if(keyPressRight){
            x[0]+=dotSize;
        }
        if(keyPressUp){
            y[0]-=dotSize;
        }
        if(keyPressDown){
            y[0]+=dotSize;
        }
    }

    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !keyPressRight){
                keyPressLeft = true;
                keyPressUp = false;
                keyPressDown = false;
            }

            if(key == KeyEvent.VK_RIGHT && !keyPressLeft){
                keyPressRight = true;
                keyPressUp = false;
                keyPressDown = false;
            }

            if(key == KeyEvent.VK_UP && !keyPressDown){
                keyPressUp = true;
                keyPressLeft = false;
                keyPressRight = false;
            }

            if(key == KeyEvent.VK_DOWN && !keyPressUp){
                keyPressDown = true;
                keyPressLeft = false;
                keyPressRight = false;
            }

        }
    }

    public void checkCollision(){
        for(int i = 1; i < dots ; i++){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if(x[0] < 0 || x[0] >= bWidth || y[0] < 0 || y[0] > bHeight){
            inGame = false;
        }
    }

    public void checkFruit(){
        if(x[0] == xApple && y[0] == yApple){
            dots++;
            locateApple();
        }
    }

    public void gameOver(Graphics g){
       String msg = "Game Over";
       int score = (dots-3)*100;
       String scoreMsg = "Score :"+Integer.toString(score);
       String highScoreMsg = "High Score : "+Integer.toString(highScore);
       Font small = new Font("Helvetica", Font.BOLD, 14);
       FontMetrics fontMetrics =  getFontMetrics(small);

       g.setColor(Color. WHITE);
       g.setFont(small);
       g.drawString(msg, (bWidth - fontMetrics.stringWidth(msg))/2, bHeight/4);
       g.drawString(highScoreMsg,(bWidth - fontMetrics.stringWidth(highScoreMsg))/2, 3*bHeight/4);
       g.drawString(scoreMsg, (bWidth - fontMetrics.stringWidth(scoreMsg))/2, bHeight/2);
    }

}
