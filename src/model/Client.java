package model;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

public class Client extends Personne{

    UUID idClient;
    List<Compte> comptes;

    public Client(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idClient = UUID.randomUUID();
        this.comptes = new ArrayList<>();
    }

    public UUID getIdClient() {
        return idClient;
    }

    public List<Compte> getComptes() {
        return comptes;
    }


    public void ajouterCompte(Compte compte) {
        if (compte != null && !comptes.contains(compte)) {
            comptes.add(compte);
            compte.setIdClient(this.idClient); // Synchronisation
        }
    }

    public void supprimerCompte(Compte compte) {
        comptes.remove(compte);
    }

    public UUID getIdCompte() {
        return idClient;
    }

    public List<Compte> getListeComptes() {
        return comptes;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }

    

    @Override
    public void afficher() {
        System.out.println("Client ID: " + idClient);
    }

}
