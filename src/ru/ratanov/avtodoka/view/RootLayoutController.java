package ru.ratanov.avtodoka.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ratanov.avtodoka.MainApp;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Created by ACER on 19.02.2017.
 */
public class RootLayoutController {

    // Ссылка на главное приложение
    MainApp mainApp;

    /**
     * Вызывается главным приложением, и оставляет ссылку на самого себя.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность выбора файла.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        //Задаем фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Файлы Excel (*.xlsx)", "*.xlsx"
        );
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Показываем диалог открытия файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadDataFromFile(file);
        }
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать файл для сохранения в XML.
     */
    @FXML
    private void handleSaveToXML() {
        FileChooser fileChooser = new FileChooser();

        // Задаем фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml"
        );
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Показываем диалог сохранения файла.
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Убеждаемся, что файл имеет корректное расширение
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveClientsToXML(file);
        }
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать файл базы для зугрузки.
     */
    @FXML
    private void handleLoadFromXML() {
        FileChooser fileChooser = new FileChooser();

        // Задаем фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml"
        );
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Показываем диалог загрузки файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadClientsFromXML(file);
        }
    }

    /**
     * Открывает настройки приложения
     */
    @FXML
    private void handleOpenSettings() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Settings.fxml"));
            AnchorPane settingsPage = loader.load();

            Stage settingsStage = new Stage();
            settingsStage.setTitle("Настройки");
            settingsStage.getIcons().add(new Image("file:resources/images/icon.png"));
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(settingsPage);
            settingsStage.setScene(scene);

            SettingsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(settingsStage);

            settingsStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleUpdateDB() {
        File file = mainApp.getUpdateFilePath();
        if (file != null) {
            mainApp.updateBD(file);
        }
    }

}
