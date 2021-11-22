package com.example.snake;

import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
@Getter
public class Gamestate {

    private final int speed;
    private int score;
    private Square foodPos;
    private final int width;
    private final int height;
    private final int gameX1;
    private final int gameX2;
    private final int gameY1;
    private final int gameY2;
    private final int scoreBoardX1;
    private final int scoreBoardY1;
    private final int scoreBoardX2;
    private final int scoreBoardY2;
    private ArrayList<Square> snake;
    public ArrayList<Integer> scores;
    @Setter
    private Dir direction;
    private Dir lastDirection;
    @Getter(value = AccessLevel.NONE)
    private boolean gameOver;
    private boolean victory;
    private @Setter boolean runs;
    private @Setter boolean firstStart;
    private Square lastSquare;
    private InputStream fej;{
    try {
            fej = new FileInputStream(getClass().getClassLoader().getResource("fej.png").getFile());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    private InputStream ujra;
    {
        try {
            ujra= new FileInputStream(getClass().getClassLoader().getResource("retry.png").getFile());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    private Image head;
    private Image retry;


    public Gamestate() {
        speed = 10;
        score = 0;
        width = 20;
        height = 20;
        gameX1 = 0;
        gameX2 = width;
        gameY1 = 3;
        gameY2 = height;
        scoreBoardX1 = 0;
        scoreBoardY1 = 0;
        scoreBoardX2 = width;
        scoreBoardY2 = 3;
        snake = new ArrayList<>();
        scores = new ArrayList<>();
        loadScores();
        direction = Dir.up;
        lastDirection = Dir.up;
        gameOver = true;
        victory = true;
        runs = false;
        firstStart = false;
        head = new Image("fej.png");
        retry = new Image("retry.png");

        newFood();
    }

    public void setStart(){
        score = 0;
        victory = false;
        gameOver = false;
        runs = true;
        firstStart = true;
        snake.removeAll(snake);
        newFood();
        addPosToSnake(new Square((getGameX1()+getGameX2())/2, (getGameY1()+getGameY2())/2));

    }

    private void loadScores(){
        try {
            RandomAccessFile be = new RandomAccessFile("score.txt", "r");

            for(String sor = be.readLine(); sor != null; sor = be.readLine()){
                scores.add(Integer.parseInt(sor));
            }
            be.close();

        }catch(IOException e){
            System.out.println("A fájl még nem létezik");
        }
    }
    public void saveScores(){
        try {
            RandomAccessFile ki = new RandomAccessFile("score.txt", "rw");
            for(String sor = ki.readLine(); sor != null ; sor=ki.readLine()){
            }
            scores.add(score);
            ki.writeBytes(score+"\r\n");
            ki.close();

        }catch(IOException e){
            System.out.println("Hiba: "+e.getMessage());
        }
    }

    public int getHighScore(){
        int highscore = 0;
        if(scores.get(0) != null)
            highscore = scores.get(0);
        for(int score : scores) {
            if (score > highscore)
                highscore = score;
        }
        return highscore;
    }

    private Square getRandomPos(){
        return new Square((int)(Math.random()*(gameX2-1-gameX1+1)+gameX1), (int)(Math.random()*(gameY2-1-gameY1+1)+gameY1));
    }
    public void newFood(){
        Square newFoodPos = getRandomPos();

        while(snake.contains(newFoodPos)){
            newFoodPos = getRandomPos();
        }
        foodPos = newFoodPos;
    }
    public void addPosToSnake(Square pos){
        snake.add(pos);
    }
    public void eatFood(){
        if(foodPos.equals(snake.get(0))){
            score++;
            addPosToSnake(lastSquare);
            if(snake.size() == width * height - width*3){
                victory = true;
                saveScores();
                return;
            }
            newFood();
        }
    }

    private void moveSnakeHead(){
        int headX = snake.get(0).x;
        int headY = snake.get(0).y;

        switch(lastDirection == direction.opposite() ? direction : direction.opposite()){
            case up -> headY++;
            case down -> headY--;
            case left -> headX++;
            case right -> headX--;
        }
        if(lastDirection != direction.opposite()){
            lastDirection = direction;
        }
        snake.set(0, new Square(headX, headY));
        gameOver = switch(lastDirection == direction.opposite() ? direction : direction.opposite()) {
            case up -> snake.get(0).y >= gameY2;
            case down -> snake.get(0).y < gameY1;
            case left -> snake.get(0).x >= gameX2;
            case right -> snake.get(0).x < gameX1;
        };
        if(gameOver)
            saveScores();
    }

    public boolean isHeadInBody(){
        for(int i=1;i<snake.size();i++){
            if(snake.get(0).equals(snake.get(i)))
                return true;
        }
        return false;
    }

    public boolean isGameOver(){
        return gameOver || isHeadInBody();
    }
    public boolean isVictory(){
        return victory;
    }

    private void moveSnakeBody() {
        lastSquare = new Square(snake.get(snake.size()-1).x,snake.get(snake.size()-1).y);
        for(int i=snake.size()-1;i>=1;i--){
            int newX = snake.get(i-1).x;
            int newY = snake.get(i-1).y;
            snake.set(i, new Square(newX, newY));
        }
    }
    public void moveSnake(){
        moveSnakeBody();
        moveSnakeHead();
    }
}
