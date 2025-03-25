module com.example.recipeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens main to javafx.graphics, javafx.fxml;  // Allow JavaFX to access Main class
    opens gui to javafx.fxml;  // Allow JavaFX to access Controller class

    exports main;
    exports gui;  // Ensure the gui package is accessible
}
