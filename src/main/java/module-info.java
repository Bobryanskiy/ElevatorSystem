module com.github.bobryanskiy.elevatorsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.github.bobryanskiy.elevatorsystem to javafx.fxml;
    exports com.github.bobryanskiy.elevatorsystem;
}