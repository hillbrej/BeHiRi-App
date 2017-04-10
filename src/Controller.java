
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    Button btHello;
    @FXML
    Button btWorld;
    @FXML
    TextField tbFeld;


    public void btnHelloClicked(ActionEvent actionEvent){
        tbFeld.setText("Hello");
    }

    public void btnWorldClicked(ActionEvent actionEvent){
        tbFeld.setText("World");
    }

}
