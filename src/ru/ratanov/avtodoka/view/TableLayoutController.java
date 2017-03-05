package ru.ratanov.avtodoka.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.ratanov.avtodoka.MainApp;
import ru.ratanov.avtodoka.model.Client;


/**
 * Created by ACER on 19.02.2017.
 */
public class TableLayoutController {

    private MainApp mainApp;

    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> dateColumn;
    @FXML
    private TableColumn<Client, String> autoColumn;
    @FXML
    private TableColumn<Client, String> amountColumn;
    @FXML
    private TableColumn<Client, String> workColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TableColumn<Client, String> phoneColumn;
    @FXML
    private TableColumn<Client, String> masterColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Label dbSize;

    @FXML
    private Label lastUpdated;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        clientTable.setItems(mainApp.getClientData());
    }

    @FXML
    private void initialize() {
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        autoColumn.setCellValueFactory(cellData -> cellData.getValue().autoProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        workColumn.setCellValueFactory(cellData -> cellData.getValue().workProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        masterColumn.setCellValueFactory(cellData -> cellData.getValue().masterProperty());

        clientTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
                    mainApp.showClientDetail(selectedClient);
                }
            }
        });
    }

    public void initFooter(int size, String updated) {
        dbSize.setText(String.valueOf(size));
        lastUpdated.setText(updated);
    }

    @FXML
    private void doSearch() {
        String query = searchField.getText().toLowerCase();
        String[] searchWords = query.split(" ");
        System.out.println("searchWords size = " + searchWords.length);

        if (!query.isEmpty()) {
            ObservableList<Client> resultClients = FXCollections.observableArrayList();

            for (int i = 0; i < mainApp.getClientData().size(); i++) {
                Client client = mainApp.getClientData().get(i);

                if (searchWords.length == 1) { // Если в поисковом запросе 1 слово
                    if (client.getAuto().toLowerCase().contains(query)) {
                        resultClients.add(client);
                    }
                } else { // Если в поисковом запросе больше одного слова
                    int all = searchWords.length;
                    int count = 0;
                    for (String searchWord : searchWords) {
                        if (client.getAuto().toLowerCase().contains(searchWord)) {
                            count++;
                        }
                    }
                    if (count == all) { // Если все поисковые слова имеют совпадение - добавляем клиента в результаты.
                        resultClients.add(client);
                    }
                }

            }

            clientTable.setItems(resultClients);
        } else {
            clientTable.setItems(mainApp.getClientData());
        }
    }
}
