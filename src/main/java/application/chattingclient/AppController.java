package application.chattingclient;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.charset.StandardCharsets;

import static application.chattingclient.ClientMain.startClient;
import static application.chattingclient.ClientMain.stopClient;

public class AppController {
    @FXML
    private ToolBar bottomTool;

    @FXML
    private Button enterBtn;

    @FXML
    private Button sendBtn;

    @FXML
    private TextField nameSpace;

    @FXML
    private TextField msgText;

    @FXML
    protected void enterBtnClick() {
        if(enterBtn.getText()=="들어가기") {
            String name = nameSpace.getText();
            startClient(name);
            bottomTool.setDisable(false);
            enterBtn.setText("나가기");
        }else{
            stopClient();
            bottomTool.setDisable(true);
            enterBtn.setText("들어가기");
        }
    }

    @FXML
    protected void sendBtnClick() {
        msgText.getText();
    }

}