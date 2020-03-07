import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent; 
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.input.MouseEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javafx.beans.binding.Bindings;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 
   
 
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;

 public class Brick900 extends Application
{

    static final int WIDTHGAMESCENE = 699;
    static final int HEIGHTGAME = 829;
    static final int PADDLE_WIDTH = 150;
    static final int PADDLE_HEIGHT = 20;
    static final int RADIUS = 10;
    static final int THEINITIALBALLOFTHEX = WIDTHGAMESCENE / 2;
    static final int THEINITIALBALLOFTHEY = HEIGHTGAME / 2;
    static final int THESPEEDOFTHEBALL = 6;
    static final int THEINITIALXOFTHEPADDLE = WIDTHGAMESCENE / 2 - PADDLE_WIDTH / 2;
    static final int THENAMEOFTHEPADDLELOCATEDABOVEBACKWALL = 50;
    static final int PADDLE_INITIAL_Y = HEIGHTGAME - THENAMEOFTHEPADDLELOCATEDABOVEBACKWALL;
    static final int PADDLE_DISTANCE_FROM_WALL = 5;
    static final int PADDLE_SPEED = 88;
    static final int EDGEONTHELEFT = 0;
    static final int EDGEONTHERIGHT = WIDTHGAMESCENE;
    static final int EDGEONTHETOP = 0;
    static final int EDGEONTHEBOTTOM = HEIGHTGAME;
    static int dx = 0;
    static int dy = 10;
    static final int BRICK_ROWS = 6;
    static final int BRICK_SIDE_BORDER = 50;
    static final int BRICK_TOP_BORDER = 50;
    static final int BRICK_WIDTH = 80;
    static final int BRICK_HEIGHT = 30;
    static final int BRICK_BETWEENX = 60;
    static final int BRICK_BETWEENY = 20;
    static final int HORIZONTAL_BRICK_SPACE = WIDTHGAMESCENE - (2 * BRICK_SIDE_BORDER);
    static final int BRICKS_PER_ROW = HORIZONTAL_BRICK_SPACE / (BRICK_WIDTH + BRICK_BETWEENX);
    static final int NONE = 0;
    static final int TOP = 1;
    static final int BOTTOM = 2;
    static final int RIGHT = 3;
    static final int LEFT = 4;
    static boolean collisionFound;
    static int bricksLeft = BRICKS_PER_ROW * BRICK_ROWS;
    static int score = 0;
    private Group root;

    final int START_TEXT_X_ONE = 180 ;
    final int START_TEXT_Y_ONE = 550;
    final int START_TEXT_X_TWO = 210 ;
    final int START_TEXT_Y_TWO = 550;
    final int START_TEXT_X_THREE = 210 ;
    final int START_TEXT_Y_THREE = 550;

 
    @Override
    public void start(Stage stage)
    {
        
        
        Rectangle[][] bricks = new
            Rectangle[BRICK_ROWS][BRICKS_PER_ROW];
        Pane root = new Pane();
        for (int i=0; i < BRICK_ROWS; i++)   {
            for (int j=0; j < BRICKS_PER_ROW; j++) {
                int brickX = i*(BRICK_WIDTH+10)+80;
                int brickY = j*(BRICK_HEIGHT+10)+51;
                Rectangle brick = new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT) ;
                brick.setFill(Color.WHITE);
                bricks[i][j] = brick;
                root.getChildren().addAll(bricks[i][j]);

            }
        }
        

        Scene gameScene = new Scene(root, WIDTHGAMESCENE, HEIGHTGAME, Color.BLACK);
        stage.setTitle("BrickBreakerDay3");
        stage.setScene(gameScene);

 
        Rectangle paddle = new Rectangle(THEINITIALXOFTHEPADDLE, PADDLE_INITIAL_Y,
                PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFill(Color.ROYALBLUE);
         paddle.setArcWidth(20);
        paddle.setArcHeight(20);
        root.getChildren().addAll(paddle);
         
          
        Text txtScore;
        txtScore = new Text(500, 39, Integer.toString(score));
       
        txtScore.setFill(Color.LIMEGREEN);
        txtScore.setStyle("-fx-font: 24 verdana;");
        root.getChildren().add(txtScore);
    

        gameScene.setOnKeyPressed(new EventHandler <KeyEvent>() {
                @Override public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case RIGHT: {
                            if ((paddle.getX()+PADDLE_WIDTH) > EDGEONTHERIGHT)
                                paddle.setX(EDGEONTHERIGHT - 
                                    (PADDLE_WIDTH + PADDLE_DISTANCE_FROM_WALL));
                            else
                                paddle.setX(paddle.getX() + PADDLE_SPEED);
                            break;
                        }
                        case LEFT:  {
                            if (paddle.getX() <= EDGEONTHELEFT)
                                paddle.setX(PADDLE_DISTANCE_FROM_WALL); 
                            else
                                paddle.setX(paddle.getX() - PADDLE_SPEED); 
                            break;
                        }
                    }
                }
            });

        Circle ball = new Circle(RADIUS,Color.ORANGE);
        ball.relocate(THEINITIALBALLOFTHEX,THEINITIALBALLOFTHEY);
        root.getChildren().addAll(ball);
           
        
    System.out.println(score);

        EventHandler<MouseEvent> movePaddle = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    if (e.getX() != (paddle.getX() + PADDLE_WIDTH / 2))
                        paddle.setX(e.getX() - PADDLE_WIDTH / 2); 
                }
            };

        stage.addEventFilter(MouseEvent.MOUSE_MOVED, movePaddle); 

        stage.show();

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(15), 
                    new EventHandler<ActionEvent>() {

                        double dx = getRandomXStart();
                        double dy = Math.sqrt(Math.pow(THESPEEDOFTHEBALL,2)-Math.pow(dx,2));

                        @Override
                        public void handle(final ActionEvent t) {
                           //mediaPlayer.play();
             txtScore.setText("Score:" + score);
                            ball.setLayoutX(ball.getLayoutX() + dx);
                            ball.setLayoutY(ball.getLayoutY() + dy);
                            

                            if (((ball.getLayoutX() >= paddle.getX()) &&
                                (ball.getLayoutX() <= paddle.getX() + PADDLE_WIDTH)) &&
                            ((ball.getLayoutY() + RADIUS) >= paddle.getY()))
                                dy = -dy;

                            if (ball.getLayoutY() + RADIUS > EDGEONTHEBOTTOM)
                                dx = dy = 0;

                            if (ball.getLayoutY() - RADIUS < EDGEONTHETOP)
                                dy = -dy;

                            if (ball.getLayoutX() + RADIUS > EDGEONTHERIGHT)
                                dx = -dx;

                            if (ball.getLayoutX() - RADIUS < EDGEONTHELEFT)
                                dx = -dx;
                            collisionFound = false;
                          

                            for (int i=0; i < BRICK_ROWS; i++)   {
                                for (int j=0; j < BRICKS_PER_ROW; j++) {
                                    Rectangle brick = bricks[i][j];
                                    if (brick!= null)
                                    {
                                        int collisionStatus = checkForCollision(brick, ball, dx, dy);                         
                                        if (collisionStatus != NONE)   {   

                                            if (brick.getFill() == Color.WHITE
                                            )    {
                                                brick.setFill(Color.RED);
                                                if (collisionStatus == TOP || collisionStatus == BOTTOM)
                                                    dy = -dy;
                                                else
                                                    dx = -dx;
                                            }
                                            else { 
                                                bricks[i][j] = null;
                                                root.getChildren().removeAll(brick);
                                                bricksLeft--;
                                                if (bricksLeft == 0)   
                                                    dx = dy = 0;
                                            }
                                            collisionFound = true;
                                            if (collisionFound == true)
                                            score += 100;
                                        }
                                        
                                         

                                    }
                                }
                            }

                        }
                    }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public int checkForCollision(Rectangle brick, Circle ball,double dx, double dy) {
        int collisionStatus = NONE;
        if (((ball.getLayoutY() >= brick.getY()) &&
            (ball.getLayoutY() <= brick.getY() + BRICK_HEIGHT))
        &&
        (((ball.getLayoutX() + RADIUS) >= brick.getX()) &&
            (((ball.getLayoutX() + RADIUS) <= brick.getX() +
                    BRICK_WIDTH)) ||
            (((ball.getLayoutX() - RADIUS) >= brick.getX()) &&
                ((ball.getLayoutX() - RADIUS) <= brick.getX() +
                    BRICK_WIDTH))))
            if (dy > 0)
                collisionStatus = LEFT;
            else
                collisionStatus = RIGHT;
        
        if (((ball.getLayoutX() >= brick.getX()) &&
            (ball.getLayoutX() <= brick.getX() + BRICK_WIDTH)) &&
        (((ball.getLayoutY() + RADIUS) >= brick.getY()) &&
            (((ball.getLayoutY() + RADIUS) <= brick.getY() +
                    BRICK_HEIGHT)) ||
            (((ball.getLayoutY() - RADIUS) >= brick.getY()) &&
                ((ball.getLayoutY() - RADIUS) <= brick.getY() +
                    BRICK_HEIGHT))))
            if (dx > 0)
                collisionStatus = TOP;
            else
                collisionStatus = BOTTOM;
        return collisionStatus;

    }
    
    public double getRandomXStart()    {
        double dx = Math.random()*THESPEEDOFTHEBALL/2+1;
        if (Math.random() <= .5)
            return dx;
        else
            return -dx;
    }
    
   
}


