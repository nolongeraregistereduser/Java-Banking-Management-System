package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gesionnaire extends Personne {

   private UUID idGestionnaire;
   private String departement;
   private List<Client> listeClients;

   public Gesionnaire(String nom, String prenom, String email, String motDePasse, String departement) {
       super(nom, prenom, email, motDePasse);
       this.idGestionnaire = UUID.randomUUID();
       this.departement = departement;
       this.listeClients = new ArrayList<>();
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


    public void ajouterClient(Client client) {
        if (client != null && !listeClients.contains(client)) {
            listeClients.add(client);
        }
    }

    public void supprimerClient(Client client) {
        listeClients.remove(client);
    }

    public void setListeClients(List<Client> listeClients) {
        this.listeClients = listeClients;
    }



    @Override
    public void afficher() {
        System.out.println("Gestionnaire ID: " + idGestionnaire + ", Département: " + departement);
       }


}
