package ui;

import controller.CompteController;
import controller.ClientController;
import model.*;
import model.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class MenuConsole {

    private CompteController compteController;
    private ClientController clientController;
    private Scanner scanner;
    private Client clientConnecte;
    private boolean isGestionnaire;

    public MenuConsole(CompteController compteController, ClientController clientController) {
        this.compteController = compteController;
        this.clientController = clientController;
        this.scanner = new Scanner(System.in);
        this.clientConnecte = null;
        this.isGestionnaire = false;
    }

    public void afficherMenu() {
        System.out.println("🏦 === SYSTÈME BANCAIRE MAROCAIN ===");

        // Authentication first
        if (!authentifier()) {
            System.out.println("Échec de l'authentification. Au revoir!");
            return;
        }

        boolean continuer = true;
        while (continuer) {
            if (isGestionnaire) {
                continuer = afficherMenuGestionnaire();
            } else {
                continuer = afficherMenuClient();
            }
        }

        System.out.println("Au revoir!");
        scanner.close();
    }

    private boolean authentifier() {
        System.out.println("\n=== AUTHENTIFICATION ===");
        System.out.println("1. Se connecter comme Client");
        System.out.println("2. Se connecter comme Gestionnaire");
        System.out.println("3. Créer un nouveau compte client");
        System.out.print("Votre choix : ");

        int choix = obtenirChoixInt();

        switch (choix) {
            case 1:
                return authentifierClient();
            case 2:
                return authentifierGestionnaire();
            case 3:
                return creerNouveauClient();
            default:
                System.out.println("Choix invalide!");
                return false;
        }
    }

    private boolean authentifierClient() {
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Optional<Client> clientOpt = clientController.authentifierClient(email, motDePasse);
        if (clientOpt.isPresent()) {
            clientConnecte = clientOpt.get();
            isGestionnaire = false;
            System.out.println("Connexion réussie! Bienvenue " + clientConnecte.getPrenom());
            return true;
        } else {
            System.out.println("Email ou mot de passe incorrect");
            return false;
        }
    }

    private boolean authentifierGestionnaire() {
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        if ("manager".equals(email) && "manager".equals(motDePasse)) {
            isGestionnaire = true;
            System.out.println("Connexion gestionnaire réussie!");
            return true;
        } else {
            System.out.println("Email ou mot de passe gestionnaire incorrect");
            return false;
        }
    }

    private boolean creerNouveauClient() {
        try {
            System.out.println("\n=== CRÉATION NOUVEAU CLIENT ===");
            System.out.print("Nom : ");
            String nom = scanner.nextLine();
            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Email : ");
            String email = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String motDePasse = scanner.nextLine();

            Client nouveauClient = clientController.creerClient(nom, prenom, email, motDePasse);
            System.out.println(" Client créé avec succès! ID: " + nouveauClient.getIdClient());

            // Auto-login after creation
            clientConnecte = nouveauClient;
            isGestionnaire = false;
            return true;

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la création: " + e.getMessage());
            return false;
        }
    }

    private boolean afficherMenuClient() {
        System.out.println(" === ESPACE CLIENT ===");
        System.out.println("Client: " + clientConnecte.getPrenom() + " " + clientConnecte.getNom());
        System.out.println("1. Consulter mes informations");
        System.out.println("2. Voir mes comptes");
        System.out.println("3. Créer un nouveau compte");
        System.out.println("4. Effectuer un dépôt");
        System.out.println("5. Effectuer un retrait");
        System.out.println("6. Effectuer un virement");
        System.out.println("7. Consulter solde d'un compte");
        System.out.println("8. Historique des transactions");
        System.out.println("9. Filtrer les transactions");
        System.out.println("10. Statistiques personnelles");
        System.out.println("11. Détecter transactions suspectes");
        System.out.println("0. Se déconnecter");
        System.out.print("Votre choix : ");

        int choix = obtenirChoixInt();

        try {
            switch (choix) {
                case 1:
                    clientController.afficherInformationsClient(clientConnecte.getIdClient());
                    break;
                case 2:
                    clientController.afficherComptesClient(clientConnecte.getIdClient());
                    break;
                case 3:
                    creerCompteClient();
                    break;
                case 4:
                    effectuerDepot();
                    break;
                case 5:
                    effectuerRetrait();
                    break;
                case 6:
                    effectuerVirement();
                    break;
                case 7:
                    consulterSolde();
                    break;
                case 8:
                    afficherHistorique();
                    break;
                case 9:
                    menuFiltrerTransactions();
                    break;
                case 10:
                    clientController.afficherStatistiquesClient(clientConnecte.getIdClient());
                    break;
                case 11:
                    clientController.detecterTransactionsSuspectes(clientConnecte.getIdClient());
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Choix invalide!");
            }
        } catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }

        return true;
    }

    private boolean afficherMenuGestionnaire() {
        System.out.println("\n👨‍ === ESPACE GESTIONNAIRE ===");
        System.out.println("1. Lister tous les clients");
        System.out.println("2. Créer un nouveau client");
        System.out.println("3. Supprimer un client");
        System.out.println("4. Gérer les comptes d'un client");
        System.out.println("5. Effectuer opération sur compte");
        System.out.println("6. Consulter transactions d'un client");
        System.out.println("7. Détecter transactions suspectes");
        System.out.println("8. Rapports et statistiques");
        System.out.println("0. Se déconnecter");
        System.out.print("Votre choix : ");

        int choix = obtenirChoixInt();

        try {
            switch (choix) {
                case 1:
                    listerTousLesClients();
                    break;
                case 2:
                    creerClientGestionnaire();
                    break;
                case 3:
                    supprimerClient();
                    break;
                case 4:
                    gererComptesClient();
                    break;
                case 5:
                    menuOperationsGestionnaire();
                    break;
                case 6:
                    consulterTransactionsClient();
                    break;
                case 7:
                    detecterTransactionsSuspectesTousClients();
                    break;
                case 8:
                    menuRapports();
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Choix invalide!");
            }
        } catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }

        return true;
    }

    // Client Methods
    private void creerCompteClient() {
        System.out.println("\n=== CRÉER UN NOUVEAU COMPTE ===");
        System.out.println("Types de compte disponibles:");
        System.out.println("1. COURANT");
        System.out.println("2. ÉPARGNE");
        System.out.println("3. DÉPÔT À TERME");
        System.out.print("Votre choix : ");

        int choixType = obtenirChoixInt();
        TypeCompte typeCompte;

        switch (choixType) {
            case 1: typeCompte = TypeCompte.COURANT; break;
            case 2: typeCompte = TypeCompte.EPARGNE; break;
            case 3: typeCompte = TypeCompte.DEPOT_A_TERME; break;
            default:
                System.out.println("Type de compte invalide!");
                return;
        }

        System.out.print("Solde initial (minimum 0 €) : ");
        BigDecimal soldeInitial = obtenirMontant();

        try {
            Compte nouveauCompte = clientController.creerCompte(clientConnecte.getIdClient(), typeCompte, soldeInitial);
            System.out.println(" Compte créé avec succès!");
            System.out.println("ID du compte: " + nouveauCompte.getIdCompte());
            System.out.println("Type: " + nouveauCompte.getTypeCompte());
            System.out.println("Solde initial: " + nouveauCompte.getSolde() + " €");
        } catch (Exception e) {
            System.out.println(" Erreur lors de la création: " + e.getMessage());
        }
    }

    // Manager Methods
    private void listerTousLesClients() {
        List<Client> clients = clientController.listerTousLesClients();
        System.out.println("\n=== LISTE DES CLIENTS ===");
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
        } else {
            for (Client client : clients) {
                System.out.println("ID: " + client.getIdClient() +
                                 " | Nom: " + client.getNom() + " " + client.getPrenom() +
                                 " | Email: " + client.getEmail() +
                                 " | Comptes: " + client.getComptes().size());
            }
        }
    }

    private void creerClientGestionnaire() {
        System.out.println("\n=== CRÉER NOUVEAU CLIENT ===");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        try {
            Client client = clientController.creerClient(nom, prenom, email, motDePasse);
            System.out.println(" Client créé avec succès! ID: " + client.getIdClient());
        } catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void supprimerClient() {
        System.out.print("ID du client à supprimer : ");
        try {
            int idClient = Integer.parseInt(scanner.nextLine());
            clientController.supprimerClient(idClient);
            System.out.println(" Client supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // Utility Methods
    private int choisirCompteClient() {
        clientController.afficherComptesClient(clientConnecte.getIdClient());
        System.out.print("ID du compte : ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("ID de compte invalide!");
            return -1;
        }
    }

    private void afficherListeTransactions(List<Transaction> transactions, String titre) {
        System.out.println("\n=== " + titre + " ===");
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvée.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("Type: " + t.getTypeTransaction() +
                                 " | Montant: " + t.getMontant() + "€" +
                                 " | Date: " + t.getDate() +
                                 " | Motif: " + t.getMotif());
            }
        }
    }

    private int obtenirChoixInt() {
        try {
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return choix;
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    private BigDecimal obtenirMontant() {
        try {
            BigDecimal montant = scanner.nextBigDecimal();
            scanner.nextLine(); // Consume newline
            return montant;
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return BigDecimal.ZERO;
        }
    }

    // Basic operations (existing methods enhanced)
    private void effectuerDepot() {
        int idCompte = choisirCompteClient();
        if (idCompte <= 0) return;

        System.out.print("Montant à déposer : ");
        BigDecimal montant = obtenirMontant();

        compteController.deposer(idCompte, montant);
        System.out.println("Dépôt effectué avec succès!");
    }

    private void effectuerRetrait() {
        int idCompte = choisirCompteClient();
        if (idCompte <= 0) return;

        System.out.print("Montant à retirer : ");
        BigDecimal montant = obtenirMontant();

        compteController.retirer(idCompte, montant);
        System.out.println("Retrait effectué avec succès!");
    }

    private void effectuerVirement() {
        System.out.println("=== VIREMENT ===");
        int idCompteSource = choisirCompteClient();
        if (idCompteSource <= 0) return;

        System.out.print("ID du compte destination : ");
        int idCompteDestination;
        try {
            idCompteDestination = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ ID de compte destination invalide!");
            return;
        }

        System.out.print("Montant à virer : ");
        BigDecimal montant = obtenirMontant();

        compteController.virer(idCompteSource, idCompteDestination, montant);
        System.out.println("Virement effectué avec succès!");
    }

    private void consulterSolde() {
        int idCompte = choisirCompteClient();
        if (idCompte <= 0) return;

        compteController.afficherSolde(idCompte);
    }

    private void afficherHistorique() {
        int idCompte = choisirCompteClient();
        if (idCompte <= 0) return;

        compteController.afficherHistorique(idCompte);
    }

    private void menuFiltrerTransactions() {
        System.out.println("\n=== FILTRER LES TRANSACTIONS ===");
        System.out.println("1. Par type de transaction");
        System.out.println("2. Par montant");
        System.out.println("3. Par date");
        System.out.println("4. Trier par montant");
        System.out.println("5. Trier par date");
        System.out.print("Votre choix : ");

        int choix = obtenirChoixInt();
        int idCompte = choisirCompteClient();
        if (idCompte <= 0) return;

        try {
            switch (choix) {
                case 1:
                    filtrerParType(idCompte);
                    break;
                case 2:
                    filtrerParMontant(idCompte);
                    break;
                case 3:
                    filtrerParDate(idCompte);
                    break;
                case 4:
                    trierParMontant(idCompte);
                    break;
                case 5:
                    trierParDate(idCompte);
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }

    private void filtrerParType(int idCompte) {
        System.out.println("Types de transaction:");
        System.out.println("1. DÉPÔT");
        System.out.println("2. RETRAIT");
        System.out.println("3. VIREMENT");
        System.out.print("Votre choix : ");

        int choixType = obtenirChoixInt();
        TypeTransaction type;

        switch (choixType) {
            case 1: type = TypeTransaction.DEPOT; break;
            case 2: type = TypeTransaction.RETRAIT; break;
            case 3: type = TypeTransaction.VIREMENT; break;
            default:
                System.out.println("Type invalide!");
                return;
        }

        List<Transaction> transactions = compteController.filtrerTransactionsParType(idCompte, type);
        afficherListeTransactions(transactions, "TRANSACTIONS " + type);
    }

    private void filtrerParMontant(int idCompte) {
        System.out.print("Montant minimum : ");
        BigDecimal montantMin = obtenirMontant();
        System.out.print("Montant maximum : ");
        BigDecimal montantMax = obtenirMontant();

        List<Transaction> transactions = compteController.filtrerTransactionsParMontant(idCompte, montantMin, montantMax);
        afficherListeTransactions(transactions, "TRANSACTIONS ENTRE " + montantMin + "€ ET " + montantMax + "€");
    }

    private void filtrerParDate(int idCompte) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.print("Date début (dd/MM/yyyy) : ");
            java.time.LocalDate dateDebut = java.time.LocalDate.parse(scanner.nextLine(), formatter);
            System.out.print("Date fin (dd/MM/yyyy) : ");
            java.time.LocalDate dateFin = java.time.LocalDate.parse(scanner.nextLine(), formatter);

            List<Transaction> transactions = compteController.filtrerTransactionsParDate(idCompte, dateDebut, dateFin);
            afficherListeTransactions(transactions, "TRANSACTIONS DU " + dateDebut + " AU " + dateFin);

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println(" Format de date invalide! Utilisez dd/MM/yyyy");
        }
    }

    private void trierParMontant(int idCompte) {
        System.out.print("Ordre croissant? (o/n) : ");
        boolean croissant = scanner.nextLine().toLowerCase().startsWith("o");

        List<Transaction> transactions = compteController.trierTransactionsParMontant(idCompte, croissant);
        afficherListeTransactions(transactions, "TRANSACTIONS TRIÉES PAR MONTANT");
    }

    private void trierParDate(int idCompte) {
        System.out.print("Ordre croissant? (o/n) : ");
        boolean croissant = scanner.nextLine().toLowerCase().startsWith("o");

        List<Transaction> transactions = compteController.trierTransactionsParDate(idCompte, croissant);
        afficherListeTransactions(transactions, "TRANSACTIONS TRIÉES PAR DATE");
    }

    // Placeholder methods for manager features
    private void gererComptesClient() {
        System.out.println(" Fonctionnalité en développement...");
    }

    private void menuOperationsGestionnaire() {
        System.out.println(" Fonctionnalité en développement...");
    }

    private void consulterTransactionsClient() {
        System.out.println(" Fonctionnalité en développement...");
    }

    private void detecterTransactionsSuspectesTousClients() {
        System.out.println(" Fonctionnalité en développement...");
    }

    private void menuRapports() {
        System.out.println(" Fonctionnalité en développement...");
    }
}
