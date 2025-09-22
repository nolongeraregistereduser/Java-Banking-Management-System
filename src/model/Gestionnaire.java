package model;

import java.util.List;
import java.util.UUID;

public class Gestionnaire extends Personne {

    UUID idGestionnaire;
    String departement;
    List<Client> listeClients;

    public Gestionnaire(String nom, String prenom, String email, String motDepasse, UUID idGestionnaire, String departement, List<Client> listeClients) {
        super(nom, prenom, email, motDepasse);
        this.idGestionnaire = UUID.randomUUID();
        this.departement = departement;
        this.listeClients = listeClients;
    }

    public UUID getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getDepartement() {
        return departement;
    }

    public List<Client> getListeClients() {
        return listeClients;
    }




    @Override
    public void afficher() {
        System.out.println("Gestionnaire " + idGestionnaire);
    }
}
