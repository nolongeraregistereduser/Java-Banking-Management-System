package controller;

import service.CompteService;
import model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class CompteController {

    private CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }


    public void deposer(UUID idCompte, BigDecimal montant) {
        compteService.effectuerDepot(idCompte, montant);
    }

    public void retirer(UUID idCompte, BigDecimal montant) {
        compteService.effectuerRetrait(idCompte, montant);
    }

    public void virer(UUID idCompteSource, UUID idCompteDestination, BigDecimal montant) {
        compteService.effectuerVirement(idCompteSource, idCompteDestination, montant);
    }

    public void afficherSolde(UUID idCompte) {
        BigDecimal solde = compteService.consulterSolde(idCompte);
        System.out.println("Solde du compte " + idCompte + " : " + solde + " €");
    }

    public void afficherHistorique(UUID idCompte) {
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

    public void fermerCompte(UUID idCompte) {
        compteService.fermerCompte(idCompte);
        System.out.println("Compte " + idCompte + " fermé avec succès.");
    }


}
