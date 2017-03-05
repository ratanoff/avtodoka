package ru.ratanov.avtodoka.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.ratanov.avtodoka.MainApp;

import java.io.File;

/**
 * Created by ACER on 02.03.2017.
 */
public class SettingsController {

    private MainApp mainApp;
    private Stage stage;

    @FXML
    private TextField fileForUpdate;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        File file = mainApp.getUpdateFilePath();
        fileForUpdate.setText(file.getPath());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void selectFileForUpdate() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Файл отчета Excel (*.xlsx)", "(*.xlsx)"
        );
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileForUpdate.setText(file.getPath());
        }
    }

    @FXML
    private void handleSave() {
        File file = new File(fileForUpdate.getText());
        if (file != null) {
            mainApp.setUpdateFilePath(file);
        }
        stage.close();
    }

    @FXML
    private void handleClose() {
        stage.close();
    }

}
