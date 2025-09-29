package controller;

import service.CompteService;
import model.Transaction;
import model.TypeTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class CompteController {

    private CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }


    public void deposer(int idCompte, BigDecimal montant) {
        compteService.effectuerDepot(idCompte, montant);
    }

    public void retirer(int idCompte, BigDecimal montant) {
        compteService.effectuerRetrait(idCompte, montant);
    }

    public void virer(int idCompteSource, int idCompteDestination, BigDecimal montant) {
        compteService.effectuerVirement(idCompteSource, idCompteDestination, montant);
    }

    public void afficherSolde(int idCompte) {
        BigDecimal solde = compteService.consulterSolde(idCompte);
        System.out.println("Solde du compte " + idCompte + " : " + solde + " €");
    }

    public void afficherHistorique(int idCompte) {
        List<Transaction> transactions = compteService.listerTransactions(idCompte);
        System.out.println("Historique des transactions pour le compte " + idCompte + " :");
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvée.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println("- " + transaction.getTypeTransaction() + " : " + transaction.getMontant() + " € (" + transaction.getMotif() + ")");
            }
        }
    }

    public void fermerCompte(int idCompte) {
        compteService.fermerCompte(idCompte);
        System.out.println("Compte " + idCompte + " fermé avec succès.");
    }

    // New filtering and sorting methods
    public List<Transaction> filtrerTransactionsParType(int idCompte, TypeTransaction type) {
        return compteService.filtrerTransactionsParType(idCompte, type);
    }

    public List<Transaction> filtrerTransactionsParMontant(int idCompte, BigDecimal montantMin, BigDecimal montantMax) {
        return compteService.filtrerTransactionsParMontant(idCompte, montantMin, montantMax);
    }

    public List<Transaction> filtrerTransactionsParDate(int idCompte, LocalDate dateDebut, LocalDate dateFin) {
        return compteService.filtrerTransactionsParDate(idCompte, dateDebut, dateFin);
    }

    public List<Transaction> trierTransactionsParMontant(int idCompte, boolean croissant) {
        return compteService.trierTransactionsParMontant(idCompte, croissant);
    }

    public List<Transaction> trierTransactionsParDate(int idCompte, boolean croissant) {
        return compteService.trierTransactionsParDate(idCompte, croissant);
    }

    public List<Transaction> detecterTransactionsSuspectes(int idCompte) {
        return compteService.detecterTransactionsSuspectes(idCompte);
    }
}
