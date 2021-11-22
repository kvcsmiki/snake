module com.example.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.snake to javafx.fxml;
    exports com.example.snake;
}