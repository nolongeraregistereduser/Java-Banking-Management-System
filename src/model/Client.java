package model;
import java.util.ArrayList;
import java.util.List;

public class Client extends Personne{
    private static int idCounter = 1;
    private int idClient;
    List<Compte> comptes;

    public Client(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idClient = idCounter++;
        this.comptes = new ArrayList<>();
    }

    public int getIdClient() {
        return idClient;
    }

    public List<Compte> getComptes() {
        return comptes;
    }

    public void ajouterCompte(Compte compte) {
        if (compte != null && !comptes.contains(compte)) {
            comptes.add(compte);
            // Synchronisation: set client ID as int
            compte.setIdClient(this.idClient);
        }
    }

    public void supprimerCompte(Compte compte) {
        comptes.remove(compte);
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
