package ru.ratanov.avtodoka.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by ACER on 24.02.2017.
 *
 * Вспомогательный класс для обертывания списка клиентов.
 * Используется для сохранения и чтения базы в XML.
 */
@XmlRootElement(name = "clients")
public class ClientListWrapper {

    private List<Client> clients;

    @XmlElement(name = "client")
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
