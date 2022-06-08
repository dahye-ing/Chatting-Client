package application.chattingclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML private ToolBar bottomTool;
    @FXML private Button enterBtn;
    @FXML private Button sendBtn;
    @FXML private TextField nameSpace;
    @FXML private TextField msgText;
    @FXML private TextArea textArea;
    Socket socket;

    public void startClient(){
        String serverIp = "127.0.0.1";
        int port = 1119;
        Thread thread = new Thread(() -> {
            try{
                socket = new Socket(serverIp,port);
                System.out.println("0");
                Platform.runLater(()->{
                    nameSpace.setEditable(false);
                    bottomTool.setDisable(false);
                    enterBtn.setText("나가기");
                });
                Thread receiver = new Thread(new ClientReceiver(socket));
                receiver.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void stopClient(){
        try{
            if(socket!=null && !socket.isClosed()){
                socket.close();
            }
        } catch (Exception e) {
            stopClient();
        }
    }

    public void send(String msg){
        Thread thread = new Thread(() -> {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(msg);
            } catch (IOException e) {  }
        });
        thread.start();
    }

    class ClientReceiver implements Runnable{

        Socket socket;
        DataInputStream in;

        ClientReceiver(Socket socket){
            this.socket = socket;
            try{
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) { }
        }

        @Override
        public void run() {
            while (in!=null){
                try{
                    String msg = in.readUTF();
                    Platform.runLater(()->{
                        textArea.appendText(msg);
                    });
                } catch (IOException e) { }
            } // while
        } // run
    } // ClientReceiver


    @FXML
    protected void onEnterBtnClick() {
        if(enterBtn.getText().equals("들어가기")) {
            startClient();
        }else{
            stopClient();
            nameSpace.setEditable(true);
            bottomTool.setDisable(true);
            enterBtn.setText("들어가기");
        }
    }

    @FXML
    protected void onSendBtnClick() {
        String name = nameSpace.getText();
        String txt = msgText.getText();
        send("["+name+"] "+txt);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enterBtn.setOnAction(event -> onEnterBtnClick());
        sendBtn.setOnAction(event -> onSendBtnClick());
    }

}