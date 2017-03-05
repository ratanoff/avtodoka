package ru.ratanov.avtodoka;
/**
 * Created by ACER on 19.02.2017.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.ratanov.avtodoka.model.Client;
import ru.ratanov.avtodoka.model.ClientListWrapper;
import ru.ratanov.avtodoka.view.ClientDetailController;
import ru.ratanov.avtodoka.view.RootLayoutController;
import ru.ratanov.avtodoka.view.TableLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    private TableLayoutController tableLayoutController;

    public MainApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Авто-Дока");


        // Устанавливаем иконку приложения
        this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

        File file = new File("resources/xml/db.xml");
        loadClientsFromXML(file);

        initRootLayout();
        loadTableContent();


    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Client> getClientData() {
        return clientData;
    }

    /**
     * Инициализирует корневой макет
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даем контроллеру корневого макета доступ к главному приложению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружаем таблицу
     */
    private void loadTableContent() {
        try {
            // Загружаем макет
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TableLayout.fxml"));
            AnchorPane tableLayout = loader.load();

            // Помещаем таблицу в центр корневого макета
            rootLayout.setCenter(tableLayout);

            // Даем контроллеру доступ к главному приложению
            tableLayoutController = loader.getController();
            tableLayoutController.setMainApp(this);

            int size = clientData.size();
            String updated = clientData.get(size - 1).getDate();
            tableLayoutController.initFooter(size, updated);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает информацию о клиентах из указанного Excel файла.
     *
     * @param file
     */
    public void loadDataFromFile(File file) {
        System.out.println(file.getPath());

        try {
            // Тестирование
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            for (int i = 0; i < 12; i++) {

                XSSFSheet sheet = workbook.getSheetAt(i);

                System.out.println(sheet.getSheetName() + " : " + sheet.getLastRowNum());

                // Добавление клиента в базу
                for (int k = 2; k < 118; k++) { // 118
                    XSSFRow row = sheet.getRow(k);
                    XSSFCell cell = row.getCell(0);
                    if (cell != null) {
                        if (!cell.toString().isEmpty()) {
                            Client client = new Client();
                            client.setDate(row.getCell(0).toString());
                            client.setAuto(row.getCell(2).toString());
                            row.getCell(3).setCellType(CellType.STRING);
                            client.setAmount(row.getCell(3).toString());
                            client.setWork(row.getCell(16).toString());
                            row.getCell(18).setCellType(CellType.STRING);
                            client.setName(row.getCell(18).getStringCellValue());
                            row.getCell(19).setCellType(CellType.STRING);
                            client.setPhone(row.getCell(19).getStringCellValue());

                            // Мастер
                            StringBuilder sb = new StringBuilder();
                            for (int j = 21; j < 26; j++) {
                                String master = row.getCell(j).toString();
                                if (!master.isEmpty()) {
                                    sb.append(sheet.getRow(0).getCell(j).toString()).append(", ");
                                }
                            }

                            if (!sb.toString().isEmpty()) {
                                client.setMaster(sb.substring(0, sb.length() - 2));
                            } else {
                                client.setMaster(sb.toString());
                            }

                            clientData.add(client);
                        }
                    }
                }
            }

            System.out.println("Загружено записей = " + clientData.size());

            loadTableContent();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает информацию о клиентах из указанного XML файла.
     * Текущая информация о клиентах будет заменена.
     *
     * @param file
     */
    public void loadClientsFromXML(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ClientListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршаллизация
            ClientListWrapper wrapper = (ClientListWrapper) um.unmarshal(file);

            clientData.clear();
            clientData.addAll(wrapper.getClients());

            System.out.println("Загружено записей: " + clientData.size());

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось загрузить данные");
            alert.setContentText("Не удалось загрузить данные из файла:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Сохраняет базу клиентов в XML файл.
     *
     * @param file
     */
    public void saveClientsToXML(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ClientListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обертываем наши данные о клиентах
            ClientListWrapper wrapper = new ClientListWrapper();
            wrapper.setClients(clientData);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить данные");
            alert.setContentText("Не удалось сохранит данные в файл:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Обновление БД
     *
     * @param file
     */
    public void updateBD(File file) {
        System.out.println("Обновление БД");
        ObservableList<Client> newClients = FXCollections.observableArrayList();

        try {
            // Тестирование
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                XSSFSheet sheet = workbook.getSheetAt(i);

                System.out.println(sheet.getSheetName() + " : " + sheet.getLastRowNum());

                // Добавление клиента в базу
                for (int k = 2; k < sheet.getLastRowNum(); k++) { // 118
                    XSSFRow row = sheet.getRow(k);
                    XSSFCell cell = row.getCell(0);
                    if (cell != null) {
                        if (!cell.toString().isEmpty()) {
                            Client newClient = new Client();
                            newClient.setDate(row.getCell(0).toString());
                            newClient.setAuto(row.getCell(2).toString());
                            row.getCell(3).setCellType(CellType.STRING);
                            newClient.setAmount(row.getCell(3).toString());
                            newClient.setWork(row.getCell(16).toString());
                            row.getCell(18).setCellType(CellType.STRING);
                            newClient.setName(row.getCell(18).getStringCellValue());
                            row.getCell(19).setCellType(CellType.STRING);
                            newClient.setPhone(row.getCell(19).getStringCellValue());

                            // Мастер
                            StringBuilder sb = new StringBuilder();
                            for (int j = 21; j < 26; j++) {
                                String master = row.getCell(j).toString();
                                if (!master.isEmpty()) {
                                    sb.append(sheet.getRow(0).getCell(j).toString()).append(", ");
                                }
                            }

                            if (!sb.toString().isEmpty()) {
                                newClient.setMaster(sb.substring(0, sb.length() - 2));
                            } else {
                                newClient.setMaster(sb.toString());
                            }
                            if (!clientData.contains(newClient)) {
                                newClients.add(newClient);
                            }
                        }
                    }
                }
            }

            clientData.addAll(newClients);
            System.out.println("Добавлено записей = " + newClients.size());

            loadTableContent();

            File newFile = new File("resources/xml/db.xml");
            saveClientsToXML(newFile);

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает детальную информацию о клиенте.
     *
     * @param client
     */
    public void showClientDetail(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ClientDetail.fxml"));
            AnchorPane page = loader.load();

            // Создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Информация о заказе");
            dialogStage.getIcons().add(new Image("file:resources/images/icon.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаем клиента в контроллер
            ClientDetailController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setClient(client);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает preference файла для обновления, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     * @return
     */
    public File getUpdateFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        String filePath = preferences.get("updateFilePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задает путь к файлу для обновления базы.
     * Этот путь сохраняется в реестре ОС.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public void setUpdateFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            preferences.put("updateFilePath", file.getPath());
        } else {
            preferences.remove("updateFilePath");
        }
    }

}
