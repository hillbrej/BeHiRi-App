package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //root.getStylesheets().add("Main/stylesheet.css");
        //root.getStylesheets().add("Main/stylesheet2.css");//style laden am anfang
        primaryStage.setTitle("BeHiRi-App");
        //primaryStage.getIcons().add(new Image("C:/Users/Beier/Desktop/favicon(3).ico"));
        //primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("C:/Users/Beier/Desktop/favicon(3).ico")));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 550));

        primaryStage.show();
    }



    public static void main(String[] args)
    {
        Controller myController = new Controller();
        myController.setData("A bis Z", "Z bis A");
        launch(args);
        //myController.setMyListView("das", "ist");
    }
}
