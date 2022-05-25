module application.chattingclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens application.chattingclient to javafx.fxml;
    exports application.chattingclient;
}