package main;

import gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.Repository;
import service.Service;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ui.fxml"));
        Scene scene = new Scene(loader.load(), 800, 700); // Set larger width & height

        // Get controller instance and set the service
        Controller controller = loader.getController();
        Repository repo = new Repository();
        Service service = new Service(repo);
        controller.setService(service);

        stage.setScene(scene);
        stage.setTitle("Faculty");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
