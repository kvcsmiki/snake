package com.example.snake;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
public class HelloApplication extends Application {

    static int speed = 10;
    static int width = 20;
    static int height = 23;
    static int scoreBoardX = 0;
    static int scoreBoardY = 0;
    static int scoreBoardX2 = width;
    static int scoreBoardY2 = 3;
    static int gameX = 0;
    static int gameY = scoreBoardY2;
    static int gameX2 = width;
    static int gameY2 = height;
    static int foodX = 0;
    static int foodY = 0;
    static Color foodColor = Color.RED;
    static int square = 25;
    static int trueWidth = width * square;
    static int trueHeight = height * square;
    static int center = trueHeight/2;
    static ArrayList<Square> snake = new ArrayList<>();
    static Dir direction = Dir.up;
    static Dir lastDirection = Dir.up;
    static boolean gameOver = true;
    static boolean victory = false;
    static int score = 0;
    static int highscore;
    static InputStream head;
    static ArrayList<Integer> scores = new ArrayList<>();
    static boolean runs = true;

    static {
        try {
            head = new FileInputStream("C:\\img\\fej.png");
        } catch (FileNotFoundException e) {
        }
    }
    static InputStream ujra;
    static {
        try {
            ujra = new FileInputStream("C:\\img\\retry.png");
        } catch (FileNotFoundException e) {
        }
    }
    static Image retry = new Image(ujra);
    static Image fej = new Image(head);

    @Override
    public void start(Stage stage){


        VBox root = new VBox();
        Canvas c = new Canvas(trueWidth,trueHeight);
        GraphicsContext gc = c.getGraphicsContext2D();
        root.getChildren().add(c);
        startingGui(gc);
        feltoltes(scores);
        getHighscore(scores,gc);

        AnimationTimer timer = new AnimationTimer(){
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
        };
        Scene scene = new Scene(root, trueWidth, trueHeight);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent->{
            if(runs){
                if (keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                    direction = Dir.up;
                if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
                    direction = Dir.left;
                if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                    direction = Dir.down;
                if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                    direction = Dir.right;
            }
            if(keyEvent.getCode() == KeyCode.ENTER && (gameOver || victory)){
                score = 0;
                victory = false;
                gameOver = false;
                runs = true;
                setStart(gc);
                timer.start();
                }
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                System.exit(0);
            if(keyEvent.getCode() == KeyCode.SPACE){
                if(runs){
                    runs = false;
                    timer.stop();

                }
                else{
                    runs = true;
                    timer.start();
                }

            }
            }

        );
        stage.setTitle("Snake jatek :D");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    public static void scoreBoardDraw(GraphicsContext gc){
        int centerY = (scoreBoardY + scoreBoardY2) / 2;
        if(scoreBoardY2 % 2 != 0)
            centerY+= 1;
        //scoreboard
        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(scoreBoardX*square,scoreBoardY*square,scoreBoardX2*square,scoreBoardY2*square);
        //score
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("",24));
        gc.fillText("Score: "+score+"",0,centerY*square);
        //highscore
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("",24));
        gc.fillText("Highscore: "+highscore+"",(scoreBoardX2-6)*square,centerY*square);
    }
    public void startingGui(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,trueWidth,trueHeight);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",80));
        gc.fillText("SNAKE GAME ",7,center-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press Enter to play!", 60, center+30);
    }
    public static void gameOverGui(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(foodX*square,foodY*square,square,square,20,20);
        gc.setFill(Color.RED);
        gc.setFont(new Font("",60));
        gc.fillText("GAME OVER",80,center-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press Enter to",110,center+30);
        gc.drawImage(retry,75,center+40);
    }
    public static void victoryGui(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,trueWidth,trueHeight);
        gc.setFill(Color.GREEN);
        gc.setFont(new Font("",60));
        gc.fillText("YOU WIN",80,center-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press Enter to retry",60,center+30);
    }
    public static void newFood(){
        go: while(true){
            foodX = (int)(Math.random()*gameX2-1+1);
            foodY = (int)(Math.random()*(gameY2-1-2)+3);
            for(Square c : snake){
                if(c.x == foodX && c.y == foodY)
                    continue go;
            }
            break;
        }
    }
    public static void setStart(GraphicsContext gc){
        snake.removeAll(snake);
        newFood();
        snake.add(new Square(width/2,height/2));
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,trueWidth, trueHeight);
        gc.setFill(foodColor);
        gc.fillRoundRect(foodX*square,foodY*square,square,square,20,20);
        scoreBoardDraw(gc);
        for(Square s: snake) {
            gc.setFill(Color.WHITE);
            gc.fillRect(s.x * square, s.y * square, square, square);
        }

    }
    public static void feltoltes(ArrayList<Integer> scores){
        try {
            RandomAccessFile be = new RandomAccessFile("score.txt", "r");
            for(String sor = be.readLine(); sor != null; sor = be.readLine())
                scores.add(Integer.parseInt(sor));
            be.close();

        }catch(IOException e){
            System.out.println("A fájl még nem létezik");
        }
    }

    public static void mentes(int score,GraphicsContext gc, ArrayList<Integer> scores){
        try {
            RandomAccessFile ki = new RandomAccessFile("score.txt", "rw");
            for(String sor = ki.readLine(); sor != null ; sor=ki.readLine()){
            }
            scores.add(score);
            ki.writeBytes(score+"\r\n");
            ki.close();
            getHighscore(scores,gc);

        }catch(IOException e){
            System.out.println("Hiba: "+e.getMessage());
        }
    }
    public static void getHighscore(ArrayList<Integer> scores,GraphicsContext gc){
        for(int elem : scores)
            if(elem > highscore)
                highscore = elem;
        scoreBoardDraw(gc);
    }
    public static void tick(GraphicsContext gc){
        if(victory){
            victoryGui(gc);
            return;
        }
        if(gameOver){
            gameOverGui(gc);
            return;
        }
        //movement
        Square ujpoz = new Square (snake.get(snake.size()-1).x,snake.get(snake.size()-1).y);
        for(int i=snake.size()-1;i>=1;i--){
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;
        }
        switch(direction){
            case up ->{
                if(lastDirection == Dir.down){
                    snake.get(0).y++;
                    if(snake.get(0).y > gameY2-1){
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
                    break;
                }
                lastDirection = Dir.up;
                snake.get(0).y--;
                if(snake.get(0).y < gameY){
                    gameOver = true;
                    mentes(score,gc,scores);
                    return;
                }
            }
            case down-> {
                if(lastDirection == Dir.up){
                    snake.get(0).y--;
                    if(snake.get(0).y < gameY){
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
                    break;
                }
                lastDirection = Dir.down;
                    snake.get(0).y++;
                    if (snake.get(0).y > gameY2-1){
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
            }
            case left-> {
                if(lastDirection == Dir.right){
                    snake.get(0).x++;
                    if(snake.get(0).x > gameX2-1){
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
                    break;
                }
                lastDirection = Dir.left;
                    snake.get(0).x--;
                    if (snake.get(0).x < gameX) {
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
            }
            case right-> {
                if(lastDirection == Dir.left){
                    snake.get(0).x--;
                    if(snake.get(0).x < gameX) {
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
                    break;
                }
                lastDirection = Dir.right;
                    snake.get(0).x++;
                    if (snake.get(0).x > gameX2-1) {
                        gameOver = true;
                        mentes(score,gc,scores);
                        return;
                    }
            }
        }
        //eat food
        if(foodX == snake.get(0).x && foodY == snake.get(0).y){
            snake.add(ujpoz);
            score++;
            if(snake.size() == width*height - width*3){
                victory = true;
                mentes(score,gc,scores);
                return;
            }
            newFood();
        }
        //self destroy
        if(!gameOver){
            for (int i = 1; i < snake.size(); i++) {
                if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                    gc.setFill(Color.RED);
                    gc.fillRect(snake.get(i).x*square,snake.get(i).y*square,square,square);
                    gameOver = true;
                    mentes(score, gc, scores);
                    return;
                }
            }
        }
        //color

        //background
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(gameX*square,gameY*square,gameX2*square, gameY2*square);
        //scoreboard
        scoreBoardDraw(gc);
        //food
        gc.setFill(foodColor);
        gc.fillRoundRect(foodX*square,foodY*square,square,square,20,20);
        //snake
        for(int i=0;i<snake.size();i++){
            if(i == 0){
                gc.drawImage(fej,snake.get(i).x*square,snake.get(i).y*square,square,square);

            }
            else {
                gc.setFill(Color.BLACK);
                gc.fillRect(snake.get(i).x * square, snake.get(i).y * square, square, square);
                gc.setFill(Color.rgb(41,71,107));
                gc.fillRect(snake.get(i).x * square, snake.get(i).y * square, square-1, square-1);
            }
        }


    }
}
