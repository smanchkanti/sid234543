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
import javafx.scene.input.MouseEvent;

public class Brick30 extends Application
{

    static final int WIDTHGAMESCENE = 699;
    static final int HEIGHTGAME = 829;
    static final int PADDLE_WIDTH = 150;
    static final int PADDLE_HEIGHT = 20;
    static final int RADIUS = 10;
    static final int THEINITIALBALLOFTHEX = WIDTHGAMESCENE / 2;
    static final int THEINITIALBALLOFTHEY = HEIGHTGAME / 2;
    static final int THESPEEDOFTHEBALL = 6;
    static final int THESPEEDOFTHEBAL2 = 10;
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
    static int dx2 = 5;
    static int dy = 10;
    static int dy2= 34;
    static final int BRICK_ROWS = 6;
    static final int BRICK_SIDE_BORDER = 50;
    static final int BRICK_TOP_BORDER = 50;
    static final int BRICK_WIDTH = 80;
    static final int BRICK_HEIGHT = 40;
    static final int BRICK_BETWEENX = 40;
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
                Rectangle brick = new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
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
        paddle.setFill(Color.CYAN);
        paddle.setArcWidth(20);
        paddle.setArcHeight(20);
       
        root.getChildren().addAll(paddle);

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

        Circle ball = new Circle(RADIUS,Color.BLUE);
        Circle ball2 = new Circle(RADIUS,Color.GREEN);
        ball2.relocate(THEINITIALBALLOFTHEX,THEINITIALBALLOFTHEY);
        ball.relocate(THEINITIALBALLOFTHEX,THEINITIALBALLOFTHEY);
        root.getChildren().addAll(ball);
        root.getChildren().addAll(ball2);
        
    
        //Moves paddle by Mouse movement
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
                            
                            ball2.setLayoutX(ball.getLayoutX() + dx2 + 300);
                            ball2.setLayoutY(ball.getLayoutY() + dy2 - 45);

                            if (((ball2.getLayoutX() >= paddle.getX()) &&
                                (ball2.getLayoutX() <= paddle.getX() + PADDLE_WIDTH)) &&
                            ((ball2.getLayoutY() + RADIUS) >= paddle.getY()))
                                dy2 = -dy2;

                            if (ball2.getLayoutY() + RADIUS > EDGEONTHEBOTTOM)
                                dx2 = dy2 = 0;

                            if (ball2.getLayoutY() - RADIUS < EDGEONTHETOP)
                                dy2 = -dy2;

                            if (ball2.getLayoutX() + RADIUS > EDGEONTHERIGHT)
                                dx2 = -dx2;

                            if (ball2.getLayoutX() - RADIUS < EDGEONTHELEFT)
                                dx2 = -dx2;
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
                                            else {  //brick color is RED - make it disappear
                                                bricks[i][j] = null;
                                                root.getChildren().removeAll(brick);
                                                bricksLeft--;
                                                if (bricksLeft == 0)   
                                                    dx = dy = 0;
                                            }
                                            collisionFound = true;
                                        }

                                    }
                                }
                            }
                            for (int i=0; i < BRICK_ROWS; i++)   {
                                for (int j=0; j < BRICKS_PER_ROW; j++) {
                                    Rectangle brick = bricks[i][j];
                                    if (brick!= null)
                                    {
                                        int collisionStatus = checkForCollision(brick, ball, dx2, dy2);                         
                                        if (collisionStatus != NONE)   {   

                                            if (brick.getFill() == Color.WHITE
                                            )    {
                                                brick.setFill(Color.RED);
                                                if (collisionStatus == TOP || collisionStatus == BOTTOM)
                                                    dy2 = -dy2;
                                                else
                                                    dx2 = -dx2;
                                            }
                                            else {  //brick color is RED - make it disappear
                                                bricks[i][j] = null;
                                                root.getChildren().removeAll(brick);
                                                bricksLeft--;
                                                if (bricksLeft == 0)   
                                                    dx2 = dy2 = 0;
                                            }
                                            collisionFound = true;
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