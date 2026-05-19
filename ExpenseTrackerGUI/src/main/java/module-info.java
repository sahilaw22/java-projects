module com.expensetrackergui.etgui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.expensetrackergui.etgui to javafx.fxml;
    opens com.expensetrackergui.etgui.controller to javafx.fxml;
    exports com.expensetrackergui.etgui;
    exports com.expensetrackergui.etgui.controller;
}