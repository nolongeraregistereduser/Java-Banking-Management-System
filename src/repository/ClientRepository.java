package repository;

import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository {

    private final List<Client> clients = new ArrayList<>();

    public void save(Client client) {
        // Remove existing client with same ID if exists
        clients.removeIf(c -> c.getIdClient() == client.getIdClient());
        clients.add(client);
    }

    public Optional<Client> findById(int idClient) {
        return clients.stream()
                .filter(client -> client.getIdClient() == idClient)
                .findFirst();
    }

    public Optional<Client> findByEmail(String email) {
        return clients.stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    public List<Client> findAll() {
        return new ArrayList<>(clients);
    }

    public boolean deleteById(int idClient) {
        return clients.removeIf(client -> client.getIdClient() == idClient);
    }

    public boolean exists(int idClient) {
        return findById(idClient).isPresent();
    }

    public int count() {
        return clients.size();
    }
}
