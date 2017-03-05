package ru.ratanov.avtodoka.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ACER on 19.02.2017.
 */
public class Client {

    private StringProperty date;
    private StringProperty auto;
    private StringProperty amount;
    private StringProperty work;
    private StringProperty name;
    private StringProperty phone;
    private StringProperty master;

    public Client() {
        this(null, null, null, null, null, null, null);
    }

    public Client(String date, String auto, String amount, String work, String name, String phone, String master) {
        this.date = new SimpleStringProperty(date);
        this.auto = new SimpleStringProperty(auto);
        this.amount = new SimpleStringProperty(amount);
        this.work = new SimpleStringProperty(work);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.master = new SimpleStringProperty(master);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getAuto() {
        return auto.get();
    }

    public StringProperty autoProperty() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto.set(auto);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getWork() {
        return work.get();
    }

    public StringProperty workProperty() {
        return work;
    }

    public void setWork(String work) {
        this.work.set(work);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getMaster() {
        return master.get();
    }

    public StringProperty masterProperty() {
        return master;
    }

    public void setMaster(String master) {
        this.master.set(master);
    }

    @Override
    public boolean equals(Object obj) {
        Client newClient = (Client) obj;

        return this.getAuto().equals(newClient.getAuto())
                && this.getWork().equals(newClient.getWork());
    }
}
