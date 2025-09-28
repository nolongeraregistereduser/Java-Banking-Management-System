package repository;

import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientRepository {

    private final List<Client> clients = new ArrayList<>();

    public void save(Client client) {
        // Remove existing client with same ID if exists
        clients.removeIf(c -> c.getIdClient().equals(client.getIdClient()));
        clients.add(client);
    }

    public Optional<Client> findById(UUID idClient) {
        return clients.stream()
                .filter(client -> client.getIdClient().equals(idClient))
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

    public boolean deleteById(UUID idClient) {
        return clients.removeIf(client -> client.getIdClient().equals(idClient));
    }

    public boolean exists(UUID idClient) {
        return findById(idClient).isPresent();
    }

    public int count() {
        return clients.size();
    }
}
