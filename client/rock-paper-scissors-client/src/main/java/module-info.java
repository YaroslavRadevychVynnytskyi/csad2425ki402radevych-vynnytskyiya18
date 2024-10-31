module rock.paper.scissors.client {
    requires com.fazecast.jSerialComm;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires static lombok;
    requires ini4j;

    opens application.game to javafx.fxml;
    exports application.game;
}