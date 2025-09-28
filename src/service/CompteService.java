package service;

import model.Compte;
import model.Transaction;
import model.TypeTransaction;
import model.exceptions.*;
import repository.CompteRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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


    public List<Transaction> filtrerTransactionsParType(UUID idCompte, TypeTransaction type) {
        return filtrerTransactions(idCompte, transaction -> transaction.getTypeTransaction().equals(type));
    }

    public List<Transaction> filtrerTransactionsParMontant(UUID idCompte, BigDecimal montantMin, BigDecimal montantMax) {
        return filtrerTransactions(idCompte, transaction ->
                transaction.getMontant().compareTo(montantMin) >= 0 &&
                        transaction.getMontant().compareTo(montantMax) <= 0);
    }

    public List<Transaction> filtrerTransactionsParDate(UUID idCompte, LocalDate dateDebut, LocalDate dateFin) {
        return filtrerTransactions(idCompte, transaction -> {
            // Convert Date to LocalDate for comparison (Java 8 compatible)
            LocalDate transactionDate = transaction.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            return !transactionDate.isBefore(dateDebut) && !transactionDate.isAfter(dateFin);
        });
    }

    public List<Transaction> filtrerTransactions(UUID idCompte, Predicate<Transaction> critere) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        return compteOpt.get().getTransactions().stream()
                .filter(critere)
                .collect(Collectors.toList());
    }

    public List<Transaction> trierTransactionsParMontant(UUID idCompte, boolean croissant) {
        return obtenirTransactionsTries(idCompte,
                croissant ? Comparator.comparing(Transaction::getMontant)
                        : Comparator.comparing(Transaction::getMontant).reversed());
    }

    public List<Transaction> trierTransactionsParDate(UUID idCompte, boolean croissant) {
        return obtenirTransactionsTries(idCompte,
                croissant ? Comparator.comparing(Transaction::getDate)
                        : Comparator.comparing(Transaction::getDate).reversed());
    }

    private List<Transaction> obtenirTransactionsTries(UUID idCompte, Comparator<Transaction> comparator) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        return compteOpt.get().getTransactions().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // Statistics methods using streams
    public BigDecimal calculerTotalDepots(UUID idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.DEPOT);
    }

    public BigDecimal calculerTotalRetraits(UUID idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.RETRAIT);
    }

    public BigDecimal calculerTotalVirements(UUID idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.VIREMENT);
    }

    private BigDecimal calculerTotalParType(UUID idCompte, TypeTransaction type) {
        return filtrerTransactionsParType(idCompte, type).stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Detect suspicious transactions
    public List<Transaction> detecterTransactionsSuspectes(UUID idCompte) {
        BigDecimal seuilSuspect = new BigDecimal("10000");

        return filtrerTransactions(idCompte, transaction ->
                transaction.getMontant().compareTo(seuilSuspect) > 0 ||
                        isTransactionRepetitive(idCompte, transaction));
    }

    private boolean isTransactionRepetitive(UUID idCompte, Transaction transaction) {
        long countSimilar = filtrerTransactions(idCompte, t ->
                t.getTypeTransaction().equals(transaction.getTypeTransaction()) &&
                        t.getMontant().equals(transaction.getMontant()) &&
                        !t.equals(transaction))
                .size();

        return countSimilar >= 3; // Suspicious if 3 or more similar transactions
    }
}
