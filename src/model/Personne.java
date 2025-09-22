package model;

public abstract class Personne {
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;

    public Personne(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getFullName() {
        return prenom + " " + nom;
    }

    public abstract void afficher();

    public  String toString(){
        return "Nom: " + nom + ", Prénom: " + prenom + ", Email: " + email;
    }
}
