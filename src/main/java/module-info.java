module com.heshanthenura.backgroundcolor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.heshanthenura.backgroundcolor to javafx.fxml;
    exports com.heshanthenura.backgroundcolor;
}