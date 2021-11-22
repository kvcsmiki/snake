package com.example.snake;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.*;

public class App extends Application{

    static @Getter
    int square = 25;
    static @Getter int trueWidth = 20 * square;
    static @Getter int trueHeight = 20 * square;
    static FileInputStream icon;

    @Override
    public void start(Stage stage) throws IOException{
        try{
            icon = new FileInputStream(getClass().getClassLoader().getResource("papo2.jpg").getFile());
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), trueWidth, trueHeight);
        GameController controller = loader.getController();
        controller.initHandlers(scene);
        stage.setTitle("Snake jatek :D");
        stage.setScene(scene);
        stage.getIcons().add(new Image(icon));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
