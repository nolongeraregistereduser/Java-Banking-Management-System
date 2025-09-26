package service;

import model.Compte;
import model.Transaction;
import model.TypeTransaction;
import model.exceptions.*;
import repository.CompteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompteService {

    private CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public void effectuerDepot(UUID idCompte, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif");
        }

        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) { // Changé isEmpty() par !isPresent() dosn't exist in Java 8
            throw new CompteNotFoundException("Compte introuvable");
        }

        Compte compte = compteOpt.get();
        compte.setSolde(compte.getSolde().add(montant));

        Transaction transaction = new Transaction(TypeTransaction.DEPOT, montant, "Dépôt", compte);
        compte.ajouterTransaction(transaction);

        compteRepository.save(compte);
    }

    public void effectuerRetrait(UUID idCompte, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif");
        }

        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        Compte compte = compteOpt.get();
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new SoldeInsufficientException("Solde insuffisant pour le retrait");
        }

        compte.setSolde(compte.getSolde().subtract(montant));

        Transaction transaction = new Transaction(TypeTransaction.RETRAIT, montant, "Retrait", compte);
        compte.ajouterTransaction(transaction);

        compteRepository.save(compte);
    }

    public void effectuerVirement(UUID idCompteSource, UUID idCompteDestination, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif");
        }

        Optional<Compte> compteSourceOpt = compteRepository.findById(idCompteSource);
        Optional<Compte> compteDestOpt = compteRepository.findById(idCompteDestination);

        if (!compteSourceOpt.isPresent() || !compteDestOpt.isPresent()) { // ✅ Correction
            throw new CompteNotFoundException("Compte source ou destination introuvable");
        }

        Compte compteSource = compteSourceOpt.get();
        Compte compteDestination = compteDestOpt.get();

        if (compteSource.getSolde().compareTo(montant) < 0) {
            throw new SoldeInsufficientException("Solde insuffisant pour le virement");
        }

        compteSource.setSolde(compteSource.getSolde().subtract(montant));
        compteDestination.setSolde(compteDestination.getSolde().add(montant));

        // ✅ Utiliser constructeur simple pour Transaction
        Transaction transactionSource = new Transaction(TypeTransaction.VIREMENT, montant, "Virement sortant", compteSource);
        Transaction transactionDest = new Transaction(TypeTransaction.VIREMENT, montant, "Virement entrant", compteDestination);

        compteSource.ajouterTransaction(transactionSource);
        compteDestination.ajouterTransaction(transactionDest);

        compteRepository.save(compteSource);
        compteRepository.save(compteDestination);
    }

    public BigDecimal consulterSolde(UUID idCompte) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compteOpt.get().getSolde();
    }

    public List<Transaction> listerTransactions(UUID idCompte) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compteOpt.get().getTransactions();
    }

    public void fermerCompte(UUID idCompte) {
        if (!compteRepository.exists(idCompte)) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        compteRepository.deleteCompte(idCompte);
    }
}
