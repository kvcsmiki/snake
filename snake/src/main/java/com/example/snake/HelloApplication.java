package com.example.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HelloApplication extends Application {

    static int speed = 10;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static Color foodColor = Color.RED;
    static int square = 25;
    static int trueWidth = width * square;
    static int trueHeight = height * square;
    static ArrayList<Square> snake = new ArrayList<>();
    static Dir direction = Dir.up;
    static Dir lastDirection = Dir.up;
    static boolean gameOver = false;
    static Random rand = new Random();
    static int kubut = 12;

    @Override
    public void start(Stage stage) throws IOException {

        newFood();
        VBox root = new VBox();
        Canvas c = new Canvas(trueWidth,trueHeight);
        GraphicsContext gc = c.getGraphicsContext2D();
        root.getChildren().add(c);
        new AnimationTimer(){
            long lastTick = 0;
            public void handle(long now){
                if(lastTick == 0){
                    lastTick = now;
                    tick(gc);
                    return;
                }
                if(now - lastTick > 1000000000 / speed){
                    lastTick = now;
                    tick(gc);
                }
            }
        }.start();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(root, trueWidth, trueHeight);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent->{
            System.out.println(keyEvent.getCode());
            if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                direction = Dir.up;
            if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
                direction = Dir.left;
            if(keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                direction = Dir.down;
            if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                direction = Dir.right;
            }
        );


        snake.add(new Square(width/2,height/2));

        stage.setTitle("Snake jatek :D");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void newFood(){
        go: while(true){
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);
            for(Square c : snake){
                if(c.x == foodX && c.y == foodY)
                    continue go;
            }
            break;
        }
    }
    public static void tick(GraphicsContext gc){
        if(gameOver){
            //TODO NEW SCENE
            gc.setFill(Color.RED);
            gc.setFont(new Font("",46));
            gc.fillText("Gatyesz",trueWidth/2-100,trueHeight/2);

            return;
        }
        for(int i=snake.size()-1;i>=1;i--){
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;
        }
        switch(direction){
            case up ->{
                if(lastDirection == Dir.down){
                    snake.get(0).y++;
                    if(snake.get(0).y >= height)
                        gameOver = true;
                    break;
                }
                lastDirection = Dir.up;
                snake.get(0).y--;
                if(snake.get(0).y < 0)
                    gameOver = true;
            }
            case down-> {
                if(lastDirection == Dir.up){
                    snake.get(0).y--;
                    if(snake.get(0).y < 0)
                        gameOver = true;
                    break;
                }
                lastDirection = Dir.down;
                    snake.get(0).y++;
                    if (snake.get(0).y >= height)
                        gameOver = true;
            }
            case left-> {
                if(lastDirection == Dir.right){
                    snake.get(0).x++;
                    if(snake.get(0).x >= width)
                        gameOver = true;
                    break;
                }
                lastDirection = Dir.left;
                    snake.get(0).x--;
                    if (snake.get(0).x < 0)
                        gameOver = true;
            }
            case right-> {
                if(lastDirection == Dir.left){
                    snake.get(0).x--;
                    if(snake.get(0).x < 0)
                        gameOver = true;
                    break;
                }
                lastDirection = Dir.right;
                    snake.get(0).x++;
                    if (snake.get(0).x >= width)
                        gameOver = true;
            }
        }
        if(foodX == snake.get(0).x && foodY == snake.get(0).y){
            snake.add(new Square(-1,-1));
            newFood();
        }
        for(int i=1;i<snake.size();i++){
            if(snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y)
                gameOver = true;
        }

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,trueWidth, trueHeight);

        gc.setFill(foodColor);
        gc.fillRoundRect(foodX*square,foodY*square,square,square,20,20);

        for(Square s: snake){
            gc.setFill(Color.WHITE);
            gc.fillRect(s.x*square,s.y*square,square,square);
        }

    }
}
