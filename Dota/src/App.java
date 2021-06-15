import connection.Handler;
import controller.Drawer;
import controller.PageLoader;
import javafx.application.Application;
import javafx.stage.Stage;

import static utills.StaticData.handler;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PageLoader.openPage("Map");
        handler = new Handler("localhost", 6969);
        Drawer drawer = new Drawer();
        drawer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
