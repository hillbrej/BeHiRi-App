package BeHiRi_ErsterAnsatz.Test_BeHiRi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       // Parent root = FXMLLoader.load(getClass().getResource("Main/sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("BeHiRi Movie Database Application");
        primaryStage.setScene(new Scene(root, 1100, 675));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
