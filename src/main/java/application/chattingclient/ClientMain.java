package application.chattingclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain extends Application {
    static Socket socket;

    public static void startClient(String name){
        try{
            String serverIp = "127.0.0.1";

            socket = new Socket(serverIp,1119);
            System.out.println("연결 완");
            Thread sender = new Thread(new ClientSender(socket, name));
            Thread receiver = new Thread(new ClientReceiver(socket));
            sender.start();
            receiver.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } // try
    }

    public static void stopClient(){
        try{
            if(socket!=null && !socket.isClosed()){
                socket.close();
            }
        } catch (Exception e) {
            stopClient();
        }
    }

    static class ClientReceiver implements Runnable{

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
                } catch (IOException e) { }
            } // while
        } // run
    } // ClientReceiver

    static class ClientSender implements Runnable{

        Socket socket;
        DataOutputStream out;
        String name;
        String msg;

        ClientSender(Socket socket, String name){
            this.socket = socket;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            } catch (IOException e) {  }
        }

        public static void getMsgText(String msg){
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                if(out!=null){
                    out.writeUTF(name);
                }

                while (out!=null){
                    out.writeUTF("["+name+"]");
                }
            } catch (IOException e) { }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("어쩌구톡");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}