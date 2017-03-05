package ru.ratanov.avtodoka.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ratanov.avtodoka.model.Client;

/**
 * Created by ACER on 26.02.2017.
 */
public class ClientDetailController {

    @FXML
    private Label date;
    @FXML
    private Label auto;
    @FXML
    private Label amount;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label master;
    @FXML
    private Label work;


    private Stage dialogStage;
    private Client client;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setClient(Client client) {
        this.client = client;

        date.setText(client.getDate());
        auto.setText(client.getAuto());
        amount.setText(client.getAmount());
        name.setText(client.getName());
        phone.setText(client.getPhone());
        master.setText(client.getMaster());
        work.setText(client.getWork());

    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
