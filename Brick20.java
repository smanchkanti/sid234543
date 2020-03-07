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
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontPosture; 
import javafx.scene.text.FontWeight; 

public class Brick20 extends Application

{
    //Game Panel Sizing
    final int GAME_PANEL_WIDTH = 800;
    final int GAME_PANEL_HEIGHT = 1050;
    final int GAME_PANEL_MIN = 0;

    //Coloring
    final Color GAME_PANEL_COLOR = Color.TAN;
    final Color PADDLE_COLOR = Color.GREEN;
    final Color BALL_COLOR = Color.ORANGE;
    final Color BRICK_COLOR_ONE = Color.YELLOW;
    final Color BRICK_COLOR_TWO = Color.ROYALBLUE;
    final Color TEXT_COLOR = Color.RED;

    //Paddle Configurations
    final int START_PADDLE_X = 425;
    final int START_PADDLE_Y = 850;
    final int PADDLE_WIDTH = 100;
    final int PADDLE_HEIGHT = 60;          
    final int PADDLE_PIXELS_CLICK = 60;
    final int PADDLE_RADIUS = 15;

    //Ball Configurations
    final int BALL_RADIUS = 12;
    final int BALL_PIXEL_SPEED = 6;
    final int START_BALL_X = 400;
    final int START_BALL_Y = 600;
    double dx = 0;
    double dy = 0;

    //brick variables
    final int BRICK_SIDE_BORDER = 0;
    final int BRICK_TOP_BORDER = 50;
    final int BRICK_WIDTH = 70;
    final int BRICK_HEIGHT = 45;
    final int BRICK_BETWEENX = 0;
    final int BRICK_BETWEENY = 20;
    final int HORIZONTAL_BRICK_SPACE = GAME_PANEL_WIDTH - (2 * BRICK_SIDE_BORDER);
    final int BRICK_PER_ROW = HORIZONTAL_BRICK_SPACE / (BRICK_WIDTH + BRICK_BETWEENX);
    final int BRICK_X_SPACING = 10;
    final int BRICK_Y_SPACING = 40;
    final int BRICK_RADIUS = 10;
    final int BRICK_ROWS = 6;

    //classification variables for ball contact with bricks
    static final int NONE = 0;
    static final int TOP = 1;
    static final int BOTTOM = 2;
    static final int RIGHT = 3;
    static final int LEFT = 4;

    //gameplay variables
    int ballCounter = 0;
    int blocksDestroyed = 0;
    //int attemptsLeft = 0;
    int remainingBricks = 54;
    int scoreCount = 0;

    //object creation
    Circle ball = new Circle(BALL_RADIUS,BALL_COLOR);
    Rectangle paddle = new Rectangle(START_PADDLE_X,START_PADDLE_Y,PADDLE_WIDTH,PADDLE_HEIGHT);
    Rectangle brickArray[][] = new Rectangle[BRICK_ROWS][BRICK_PER_ROW];
    Text text = new Text();
    Text score = new Text();
    Pane root = new Pane();

    //Text variables
    final String welcomeText = "Welcome to Brick Breaker! \n    Please Click Space to Start \n\n    ";
    final String blankText = "";
   // final String spaceContinue = "Press Space to Continue (" + attemptsLeft + ")";
    final String gameOver = "        Game Over! \nPress Space to Restart"; 
    final String winner = "               You Win! \n    Press Space to Restart";
    final int FONT_SIZE = 25;
    final int START_TEXT_X_ONE = 180 ;
    final int START_TEXT_Y_ONE = 550;
    final int START_TEXT_X_TWO = 210 ;
    final int START_TEXT_Y_TWO = 550;
    final int START_TEXT_X_THREE = 210 ;
    final int START_TEXT_Y_THREE = 550;

    @Override
    public void start(Stage stage)
    {

        //Create Game Panel
        Scene gameScene = new Scene(root,GAME_PANEL_WIDTH,GAME_PANEL_HEIGHT);
        stage.setTitle("BrickBreaker");
        stage.setScene(gameScene);
        gameScene.setFill(GAME_PANEL_COLOR);
        root.setStyle("-fx-background-color: transparent;");

        //Create Paddle
        Rectangle paddle = new Rectangle(START_PADDLE_X,START_PADDLE_Y,PADDLE_WIDTH,PADDLE_HEIGHT);
        paddle.setFill(PADDLE_COLOR);
        root.getChildren().addAll(paddle);
        paddle.setArcWidth(PADDLE_RADIUS);
        paddle.setArcHeight(PADDLE_RADIUS);

        //Text on Game Scene
        text.setFont(Font.font("comic sans", FontWeight.BOLD, FontPosture.REGULAR, FONT_SIZE));
        text.setFill(TEXT_COLOR);
        score.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        score.setFill(TEXT_COLOR);
        text.setText(welcomeText); 
        text.setX(START_TEXT_X_ONE); 
        text.setY(START_TEXT_Y_ONE); 
        score.setX(START_TEXT_X_ONE + 600); 
        score.setY(START_TEXT_Y_ONE + 400);
        //score.setText("hello");

        root.getChildren().add(text);
        root.getChildren().add(score);
        //Arrow Key Controls and Paddle Bounding
        gameScene.setOnKeyPressed(new EventHandler <KeyEvent>() {
                @Override public void handle(KeyEvent event) {
                    switch (event.getCode()) {

                        case RIGHT:
                        if (paddle.getX() <= (GAME_PANEL_WIDTH-PADDLE_WIDTH)){
                            if ((GAME_PANEL_WIDTH - paddle.getX()) <= (PADDLE_WIDTH + PADDLE_PIXELS_CLICK)){
                                paddle.setX(paddle.getX() + (GAME_PANEL_WIDTH - (paddle.getX()+PADDLE_WIDTH)));
                            }
                            else {
                                paddle.setX(paddle.getX() + PADDLE_PIXELS_CLICK);
                            }
                        }
                        break;
                        case LEFT:
                        if (paddle.getX() >= GAME_PANEL_MIN){
                            if ((paddle.getX()) <= (PADDLE_PIXELS_CLICK)){
                                paddle.setX(GAME_PANEL_MIN);
                            }                            
                            else{
                                paddle.setX(paddle.getX() - PADDLE_PIXELS_CLICK);
                            }
                        }
                        break;

                    }
                    //reset- spacebar
                    if (event.getCode() == KeyCode.SPACE) {
                        if (remainingBricks == 0) {
                            resetField();
                            ballCounter = 0;
                        } else if (ballCounter == 3 && remainingBricks != 0) {
                            resetField();
                            blocksDestroyed = 0;
                            ballCounter = 0;
                        } else {
                            ball.relocate(START_BALL_X,START_BALL_Y);
                            dx = getRandomXStart(BALL_PIXEL_SPEED);
                            dy = Math.sqrt(Math.pow(BALL_PIXEL_SPEED,2) - Math.pow(dx,2));
                            ballCounter++;
                            text.setText(blankText);
                            //System.out.println("BD: " + blocksDestroyed + " BC: " + ballCounter);
                        }
                    }
                }
            });

        for (int y = 0; y < BRICK_ROWS; y++) {
            for (int x = 0; x < BRICK_PER_ROW; x++) {
                Rectangle brick = new Rectangle(BRICK_SIDE_BORDER + BRICK_WIDTH * x + BRICK_X_SPACING * x, BRICK_TOP_BORDER + BRICK_HEIGHT * y + BRICK_Y_SPACING * y,
                        BRICK_WIDTH, BRICK_HEIGHT);

                brickArray[y][x] = brick;
                root.getChildren().addAll(brick);

                //color
                brick.setFill(BRICK_COLOR_ONE);
                brick.setArcWidth(BRICK_RADIUS);
                brick.setArcHeight(BRICK_RADIUS);

            }
        }

        ball.relocate(START_BALL_X,START_BALL_Y);
        root.getChildren().addAll(ball);
        stage.show();

        //Timeline
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(15),new EventHandler<ActionEvent>(){

                        @Override
                        public void handle(final ActionEvent t){
                            ball.setLayoutX(ball.getLayoutX() + dx);
                            ball.setLayoutY(ball.getLayoutY() - dy);
                      //      attemptsLeft = 3 - ballCounter;
                            score.setText(scoreString(scoreCount));
                            //ball motion relative to Game Scene
                            if (ball.getLayoutY() + BALL_RADIUS >= GAME_PANEL_HEIGHT){
                                dy = 0;
                                dx = 0;
                         //       text.setText(continueString(attemptsLeft));
                            }
                            if(ball.getLayoutY() - BALL_RADIUS <= GAME_PANEL_MIN) {
                                dy = -dy;
                            }
                            if (ball.getLayoutY() > START_PADDLE_Y + PADDLE_HEIGHT + 5) {
                                dy = 0;
                                dx = 0;
                       //         text.setText(continueString(attemptsLeft));
                                if (ballCounter == 3) {
                                    text.setText(gameOver); 
                                    text.setX(START_TEXT_X_TWO); 
                                    text.setY(START_TEXT_Y_TWO);
                                }
                            }
                            if (remainingBricks == 0) {
                                    text.setText(winner);
                                    dy = 0;
                                    dx = 0;
                            }
                            if  ( ((ball.getLayoutY() >= paddle.getY()) && (ball.getLayoutY() <= paddle.getY() + BALL_RADIUS)) && ((ball.getLayoutX() >= paddle.getX()) && (ball.getLayoutX() <= paddle.getX() + PADDLE_WIDTH)) ) {
                                dy = -dy;
                            }
                            if(ball.getLayoutX() + BALL_RADIUS >= GAME_PANEL_WIDTH) {
                                dx = -(dx);
                            }
                            if((ball.getLayoutX() - BALL_RADIUS) <= GAME_PANEL_MIN){
                                dx = -(dx);
                            }

                            //ball motion relative to bricks
                            for (int y = 0; y < BRICK_ROWS; y++) {
                                for (int x = 0; x < BRICK_PER_ROW; x++) {

                                    Rectangle brick = brickArray[y][x];
                                    int collisionStatus = collisionDetection(brick, ball, dx, dy);
                                    //System.out.println("BD: " + blocksDestroyed + " BC: " + ballCounter);
                                    //System.out.println(collisionStatus);

                                    if (brick.getFill() != GAME_PANEL_COLOR) {
                                        if (collisionStatus != NONE) {
                                            //System.out.println("YUH");
                                            if (brickArray[y][x].getFill() == BRICK_COLOR_ONE) {
                                                brick.setFill(BRICK_COLOR_TWO);
                                                scoreCount = scoreCount + 5;
                                                if (collisionStatus == TOP || collisionStatus == BOTTOM) {
                                                    dy = -dy;
                                                }

                                                else {
                                                    dx = -dx;
                                                }
                                            }

                                            else {
                                                //System.out.println("case");
                                                brick.setFill(GAME_PANEL_COLOR);
                                                brick = null;
                                                root.getChildren().removeAll(brick);
                                                remainingBricks--;
                                                scoreCount = scoreCount + 10;

                                            }
                                            collisionStatus = NONE;
                                        }

                                    }
                                }
                            }

                        }
                    }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
        stage.show();

        //Mouse Controls
        EventHandler<MouseEvent> movePaddle = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    if (e.getX() != (paddle.getX() + PADDLE_WIDTH / 2))
                        paddle.setX(e.getX() - PADDLE_WIDTH / 2);
                }
            };
        stage.addEventFilter(MouseEvent.MOUSE_MOVED, movePaddle);

    }

    public double getRandomXStart(int x)    {
        double dx = Math.random()*
            x/2+1;
        if (Math.random() <= .5)
            return dx;
        else
            return -dx;
    }

    //method for detecting collisions and precise "type" of collision
    public int collisionDetection(Rectangle brick, Circle ball,
    double dx, double dy) {
        int collisionStatus = NONE;
        // Collision on Vertical?
        if (((ball.getLayoutY() >= brick.getY()) && (ball.getLayoutY() <= brick.getY() + BRICK_HEIGHT)) && (((ball.getLayoutX() + BALL_RADIUS) >= brick.getX()) &&
            (((ball.getLayoutX() + BALL_RADIUS) <= brick.getX() +BRICK_WIDTH)) || (((ball.getLayoutX() - BALL_RADIUS) >= brick.getX()) && ((ball.getLayoutX() - BALL_RADIUS) <= brick.getX() +
                    BRICK_WIDTH))))
            if (dy > 0)
                collisionStatus = LEFT;
            else
                collisionStatus = RIGHT;
        // Collision on Horizontal?
        if (((ball.getLayoutX() >= brick.getX()) &&
            (ball.getLayoutX() <= brick.getX() + BRICK_WIDTH)) &&
        (((ball.getLayoutY() + BALL_RADIUS) >= brick.getY()) &&
            (((ball.getLayoutY() + BALL_RADIUS) <= brick.getY() +
                    BRICK_HEIGHT)) ||
            (((ball.getLayoutY() - BALL_RADIUS) >= brick.getY()) &&
                ((ball.getLayoutY() - BALL_RADIUS) <= brick.getY() +

                    BRICK_HEIGHT))))
            if (dx > 0)
                collisionStatus = TOP;
            else
                collisionStatus = BOTTOM;

        return collisionStatus;
    }

    public void resetField() {
        for (int y = 0; y < BRICK_ROWS; y++) {
            for (int x = 0; x < BRICK_PER_ROW; x++) {

                brickArray[y][x] = null;

            }
        }

        for (int y = 0; y < BRICK_ROWS; y++) {
            for (int x = 0; x < BRICK_PER_ROW; x++) {
                Rectangle brick = new Rectangle(BRICK_SIDE_BORDER + BRICK_WIDTH * x + BRICK_X_SPACING * x, BRICK_TOP_BORDER + BRICK_HEIGHT * y + BRICK_Y_SPACING * y,
                        BRICK_WIDTH, BRICK_HEIGHT);

                brickArray[y][x] = brick;

                //color
                brick.setFill(BRICK_COLOR_ONE);
                brick.setArcWidth(BRICK_RADIUS);
                brick.setArcHeight(BRICK_RADIUS);
                root.getChildren().addAll(brick);

            }
        }
        ball.relocate(START_BALL_X,START_BALL_Y);
        text.setText(welcomeText); 
        text.setX(START_TEXT_X_ONE); 
        text.setY(START_TEXT_Y_ONE);
        blocksDestroyed = 0;
        ballCounter = 0;
        root.getChildren().removeAll(ball);
        root.getChildren().addAll(ball);
        ball.relocate(START_BALL_X,START_BALL_Y);
        remainingBricks = 54;
        scoreCount = 0;

    }

    public String continueString(int x) {
        return "Press Space to Continue (" + x + ")";
    }

    public String scoreString(int x) {
        return "         Score: " + x;
    }

}
