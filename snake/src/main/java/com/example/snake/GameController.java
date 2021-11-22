package com.example.snake;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameController {

    private Color foodColor;
    private Gamestate gamestate;
    private AnimationTimer timer;

    @FXML
    VBox root;
    @FXML
    Canvas canvas;

    @FXML
    public void initialize(){
        foodColor = Color.RED;
        gamestate = new Gamestate();
        drawStart();
        initTimer();
    }
    private GraphicsContext getGrapicsContext(){
        return canvas.getGraphicsContext2D();
    }

    public void initHandlers(Scene scene){
        scene.setOnKeyPressed(this::keyPressHandler);
    }

    private void keyPressHandler(KeyEvent keyEvent){
        if(!gamestate.isFirstStart()){
            if(keyEvent.getCode() != null){
                gamestate.setFirstStart(true);
                gamestate.setStart();
                timer.start();
                return;
            }
        }
        if(keyEvent.getCode() == KeyCode.ESCAPE && !(gamestate.isVictory() || gamestate.isGameOver())){
            gamestate.saveScores();
            System.exit(0);
        }
        if(keyEvent.getCode() == KeyCode.ESCAPE)
            System.exit(0);
        if(keyEvent.getCode() == KeyCode.ENTER && (gamestate.isGameOver() || gamestate.isVictory())){
            gamestate.setStart();
            timer.start();
        }
        if(gamestate.isRuns()) {
            Dir dir = switch (keyEvent.getCode()) {
                case W, UP -> Dir.up;
                case A, LEFT -> Dir.left;
                case S, DOWN -> Dir.down;
                case D, RIGHT -> Dir.right;
                default -> gamestate.getDirection();
            };
            gamestate.setDirection(dir);
            if(keyEvent.getCode() == KeyCode.SPACE){
                gamestate.setRuns(false);
                timer.stop();
                drawPause();

            }
        }
        else{
            if(keyEvent.getCode() == KeyCode.SPACE){
                timer.start();
                gamestate.setRuns(true);
            }
        }


    }

    private void drawPause(){
        var gc = getGrapicsContext();
        gc.setFill(Color.rgb(0,0,0,0.7));
        gc.fillRect(gamestate.getGameX1()*App.getSquare(), gamestate.getGameY1()*App.getSquare(), gamestate.getGameX2()*App.getSquare(), gamestate.getGameY2()*App.getSquare());
        gc.setFill(Color.LIGHTBLUE);
        gc.setFont(new Font("",60));
        gc.fillText("PAUSED",140, (gamestate.getGameY1()+ gamestate.getGameY2()/2)*App.getSquare()-30);

    }

    private void drawStart(){
        var gc = getGrapicsContext();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,gamestate.getWidth()* App.getSquare(),gamestate.getHeight()* App.getSquare());
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",80));
        gc.fillText("SNAKE GAME ",7, gamestate.getHeight()/2*App.getSquare()-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press any key to play!", 35, gamestate.getHeight()/2*App.getSquare()+30);
    }

    private void drawSnake(){
        var gc = getGrapicsContext();
        gc.drawImage(gamestate.getHead(),gamestate.getSnake().get(0).x*App.getSquare(),gamestate.getSnake().get(0).y*App.getSquare(),App.getSquare(),App.getSquare());
        for(int i=1;i<gamestate.getSnake().size();i++){
            gc.setFill(Color.BLACK);
            gc.fillRect(gamestate.getSnake().get(i).x * App.getSquare(), gamestate.getSnake().get(i).y * App.getSquare(), App.getSquare(), App.getSquare());
            gc.setFill(Color.rgb(41,71,107));
            gc.fillRect(gamestate.getSnake().get(i).x * App.getSquare(), gamestate.getSnake().get(i).y * App.getSquare(), App.getSquare()-1, App.getSquare()-1);
        }
    }

    private void drawBackground(){
        var gc = getGrapicsContext();
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(gamestate.getGameX1()*App.getSquare(), gamestate.getGameY1()*App.getSquare(), gamestate.getGameX2()*App.getSquare(), gamestate.getGameY2()*App.getSquare());
    }

    private void drawFood(){
        var gc = getGrapicsContext();
        gc.setFill(foodColor);
        gc.fillRoundRect(gamestate.getFoodPos().x* App.getSquare(),
                gamestate.getFoodPos().y* App.getSquare(),
                App.getSquare(), App.getSquare(),
                20,20);
    }

    private void drawGameOver(){
        var gc = getGrapicsContext();
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(gamestate.getFoodPos().x* App.getSquare(),
                gamestate.getFoodPos().y* App.getSquare(),
                App.getSquare(), App.getSquare(),
                20,20);
        gc.setFill(Color.RED);
        gc.setFont(new Font("",60));
        gc.fillText("GAME OVER",80,gamestate.getHeight()/2*App.getSquare()-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press Enter to",110, gamestate.getHeight()/2*App.getSquare()+30);
        gc.drawImage(gamestate.getRetry(),75,gamestate.getHeight()/2*App.getSquare()+40);
    }
    private void drawVictory(){
        var gc = getGrapicsContext();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,gamestate.getWidth()* App.getSquare(),gamestate.getHeight()* App.getSquare());
        gc.setFill(Color.GREEN);
        gc.setFont(new Font("",60));
        gc.fillText("YOU WIN",80, (gamestate.getGameX1()+ gamestate.getGameX2())/2*App.getSquare()-30);
        gc.setFont(new Font("",46));
        gc.fillText("Press Enter to retry",60, (gamestate.getGameX1()+ gamestate.getGameX2())/2*App.getSquare()+30);
    }

    private void drawScoreBoard(){
        var gc = getGrapicsContext();
        int centerY = (gamestate.getScoreBoardY2() + gamestate.getScoreBoardY1() ) / 2;
        if(centerY % 2 != 0)
            centerY+=1;
        //scoreboard
        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(gamestate.getScoreBoardX1()* App.getSquare(),gamestate.getScoreBoardY1()* App.getSquare(),
                gamestate.getScoreBoardX2()* App.getSquare(),gamestate.getScoreBoardY2()* App.getSquare());
        //score
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("",24));
        gc.fillText("Score: "+gamestate.getScore()+"",0,centerY* App.getSquare());
        //highscore
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("",24));
        gc.fillText("Highscore: "+gamestate.getHighScore()+"",(gamestate.getScoreBoardX2()-6)* App.getSquare(),centerY* App.getSquare());
    }

    private void drawSelfDestroy(){
        var gc = getGrapicsContext();
        var snake = gamestate.getSnake();
        for(int i= 1; i< snake.size();i++){
            if(snake.get(0).equals(snake.get(i))){
                gc.setFill(Color.RED);
                gc.fillRect(snake.get(i).x* App.getSquare(), snake.get(i).y* App.getSquare(),
                        App.getSquare(), App.getSquare());
                return;
            }
        }
    }



    private void initTimer() {
        timer = new AnimationTimer(){
            long lastTick = 0;
            public void handle(long now){
                if(lastTick == 0){
                    lastTick = now;
                    tick();
                    return;
                }
                if(now - lastTick > 1000000000 / gamestate.getSpeed()){
                    lastTick = now;
                    tick();
                }
            }
        };
    }

    private void tick() {
        if(gamestate.isHeadInBody()){
            drawSelfDestroy();
        }

        if(gamestate.isGameOver()) {
            drawGameOver();
            timer.stop();
            gamestate.setRuns(false);
            return;
        }
        else if(gamestate.isVictory()){
            timer.stop();
            gamestate.setRuns(false);
            drawVictory();
            return;

        } else{
            gamestate.moveSnake();
            gamestate.eatFood();
            if(gamestate.isHeadInBody()){
                drawSelfDestroy();
                return;
            }
            if(gamestate.isGameOver() || gamestate.isVictory())
                return;
            drawBackground();
            drawScoreBoard();
            drawFood();
            drawSnake();


        }

    }
}
