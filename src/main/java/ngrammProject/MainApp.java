package ngrammProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by OEM on 23.03.2018.
 */
public class MainApp extends Application {
    public static Stage pStage;
    Controller control = new Controller();
    private static ObservableList<DataModel> dataModelList = FXCollections.observableArrayList();

    static {
        dataModelList.add(new DataModel("Ничего", 0.0, 0.0, 0.0));
    }

    static void setDataModelList(ObservableList<DataModel> dataModelList) {
        MainApp.dataModelList = dataModelList;
    }

    public static void setEmptyDataModelList() {
        MainApp.dataModelList = FXCollections.observableArrayList();
    }

    public ObservableList<DataModel> getData() {
        return dataModelList;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlFile = "/fxml/design.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        primaryStage.setTitle("G. Emil");
        primaryStage.setScene(new Scene(root));
        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setMainApp(this);
        primaryStage.show();
    }


    public static Stage getPrimaryStage() {
        return pStage;
    }
}
