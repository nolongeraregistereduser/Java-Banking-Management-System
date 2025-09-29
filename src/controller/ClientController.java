package controller;

import model.Client;
import model.Compte;
import model.TypeCompte;
import model.Transaction;
import model.TypeTransaction;
import service.ClientService;
import service.CompteService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ClientController {

    private ClientService clientService;
    private CompteService compteService;

    public ClientController(ClientService clientService, CompteService compteService) {
        this.clientService = clientService;
        this.compteService = compteService;
    }

    // Client management
    public Client creerClient(String nom, String prenom, String email, String motDePasse) {
        return clientService.creerClient(nom, prenom, email, motDePasse);
    }

    public Compte creerCompte(int idClient, TypeCompte typeCompte, BigDecimal soldeInitial) {
        return clientService.creerCompte(idClient, typeCompte, soldeInitial);
    }

    public Optional<Client> obtenirClient(int idClient) {
        return clientService.obtenirClient(idClient);
    }

    public void supprimerClient(int idClient) {
        clientService.supprimerClient(idClient);
    }

    public List<Client> listerTousLesClients() {
        return clientService.listerTousLesClients();
    }

    public Optional<Client> authentifierClient(String email, String motDePasse) {
        return clientService.authentifierClient(email, motDePasse);
    }

    // Client statistics and information
    public void afficherInformationsClient(int idClient) {
        Optional<Client> clientOpt = clientService.obtenirClient(idClient);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("=== INFORMATIONS CLIENT ===");
            System.out.println("ID: " + client.getIdClient());
            System.out.println("Nom: " + client.getNom() + " " + client.getPrenom());
            System.out.println("Email: " + client.getEmail());
            System.out.println("Nombre de comptes: " + client.getComptes().size());
            System.out.println("Solde total: " + clientService.calculerSoldeTotal(idClient) + " €");
        } else {
            System.out.println("Client introuvable.");
        }
    }

    public void afficherComptesClient(int idClient) {
        Optional<Client> clientOpt = clientService.obtenirClient(idClient);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("=== COMPTES DU CLIENT ===");
            if (client.getComptes().isEmpty()) {
                System.out.println("Aucun compte trouvé.");
            } else {
                for (Compte compte : client.getComptes()) {
                    System.out.println("ID: " + compte.getIdCompte() +
                                     " | Type: " + compte.getTypeCompte() +
                                     " | Solde: " + compte.getSolde() + " €");
                }
            }
        } else {
            System.out.println("Client introuvable.");
        }
    }

    public void afficherStatistiquesClient(int idClient) {
        System.out.println("=== STATISTIQUES CLIENT ===");
        System.out.println("Solde total: " + clientService.calculerSoldeTotal(idClient) + " €");
        System.out.println("Total des dépôts: " + clientService.calculerTotalDepots(idClient) + " €");
        System.out.println("Total des retraits: " + clientService.calculerTotalRetraits(idClient) + " €");
    }

    // Transaction filtering for clients
    public void afficherTransactionsFiltrées(int idClient, TypeTransaction type) {
        List<Transaction> transactions = clientService.obtenirTransactionsClient(idClient,
            t -> t.getTypeTransaction().equals(type));

        System.out.println("=== TRANSACTIONS " + type + " ===");
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction de ce type trouvée.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("Montant: " + t.getMontant() + " € | Date: " + t.getDate() + " | Motif: " + t.getMotif());
            }
        }
    }

    public void detecterTransactionsSuspectes(int idClient) {
        List<Transaction> suspectes = clientService.detecterTransactionsSuspectes(idClient);
        System.out.println("=== TRANSACTIONS SUSPECTES ===");
        if (suspectes.isEmpty()) {
            System.out.println("Aucune transaction suspecte détectée.");
        } else {
            for (Transaction t : suspectes) {
                System.out.println("⚠️ Montant: " + t.getMontant() + " € | Type: " + t.getTypeTransaction() + " | Date: " + t.getDate());
            }
        }
    }
}
