package repository;

import model.Compte;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CompteRepository {

    private final List<Compte> comptes = new ArrayList<>();

    public void save(Compte compte) {
        comptes.add(compte);
    }

    public Optional<Compte> findById(UUID idCompte) {
        return comptes.stream()
                .filter(compte -> compte.getIdCompte().equals(idCompte))
                .findFirst();
    }

    public List<Compte> findAll() {
        return new ArrayList<>(comptes); // Retourner une copie pour l'encapsulation
    }

    public boolean deleteCompte(UUID idCompte) {
        return comptes.removeIf(compte -> compte.getIdCompte().equals(idCompte));
    }

    // Trouver les comptes d'un client
    public List<Compte> findByClientId(UUID clientId) {
        return comptes.stream()
                .filter(compte -> compte.getProprietaire().getIdClient().equals(clientId))
                .collect(Collectors.toList());
    }

    public boolean exists(UUID idCompte) {
        return findById(idCompte).isPresent();
    }

    public int count() {
        return comptes.size();
    }
}
