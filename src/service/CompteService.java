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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CompteService {

    private CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public void effectuerDepot(int idCompte, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif");
        }

        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        Compte compte = compteOpt.get();
        compte.setSolde(compte.getSolde().add(montant));

        Transaction transaction = new Transaction(TypeTransaction.DEPOT, montant, "Dépôt", compte);
        compte.ajouterTransaction(transaction);

        compteRepository.save(compte);
    }

    public void effectuerRetrait(int idCompte, BigDecimal montant) {
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

    public void effectuerVirement(int idCompteSource, int idCompteDestination, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif");
        }

        Optional<Compte> sourceOpt = compteRepository.findById(idCompteSource);
        Optional<Compte> destOpt = compteRepository.findById(idCompteDestination);

        if (!sourceOpt.isPresent() || !destOpt.isPresent()) {
            throw new CompteNotFoundException("Compte source ou destination introuvable");
        }

        Compte source = sourceOpt.get();
        Compte dest = destOpt.get();

        if (source.getSolde().compareTo(montant) < 0) {
            throw new SoldeInsufficientException("Solde insuffisant pour le virement");
        }

        source.setSolde(source.getSolde().subtract(montant));
        dest.setSolde(dest.getSolde().add(montant));

        Transaction transactionSource = new Transaction(
            java.util.UUID.randomUUID(), TypeTransaction.VIREMENT, montant,
            new java.util.Date(), "Virement vers compte " + idCompteDestination, source, dest);
        Transaction transactionDest = new Transaction(
            java.util.UUID.randomUUID(), TypeTransaction.VIREMENT, montant,
            new java.util.Date(), "Virement reçu de compte " + idCompteSource, dest, source);

        source.ajouterTransaction(transactionSource);
        dest.ajouterTransaction(transactionDest);

        compteRepository.save(source);
        compteRepository.save(dest);
    }

    public BigDecimal consulterSolde(int idCompte) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compteOpt.get().getSolde();
    }

    public List<Transaction> listerTransactions(int idCompte) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compteOpt.get().getTransactions();
    }

    public void fermerCompte(int idCompte) {
        if (!compteRepository.deleteCompte(idCompte)) {
            throw new CompteNotFoundException("Compte introuvable");
        }
    }

    public List<Transaction> filtrerTransactionsParType(int idCompte, TypeTransaction type) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compteOpt.get().getTransactions().stream()
                .filter(t -> t.getTypeTransaction().equals(type))
                .collect(Collectors.toList());
    }

    public List<Transaction> filtrerTransactionsParMontant(int idCompte, BigDecimal montantMin, BigDecimal montantMax) {
        return filtrerTransactions(idCompte, transaction ->
                transaction.getMontant().compareTo(montantMin) >= 0 &&
                        transaction.getMontant().compareTo(montantMax) <= 0);
    }

    public List<Transaction> filtrerTransactionsParDate(int idCompte, LocalDate dateDebut, LocalDate dateFin) {
        return filtrerTransactions(idCompte, transaction -> {
            // Convert Date to LocalDate for comparison (Java 8 compatible)
            LocalDate transactionDate = transaction.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            return !transactionDate.isBefore(dateDebut) && !transactionDate.isAfter(dateFin);
        });
    }

    public List<Transaction> filtrerTransactions(int idCompte, Predicate<Transaction> critere) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        return compteOpt.get().getTransactions().stream()
                .filter(critere)
                .collect(Collectors.toList());
    }

    public List<Transaction> trierTransactionsParMontant(int idCompte, boolean croissant) {
        return obtenirTransactionsTries(idCompte,
                croissant ? Comparator.comparing(Transaction::getMontant)
                        : Comparator.comparing(Transaction::getMontant).reversed());
    }

    public List<Transaction> trierTransactionsParDate(int idCompte, boolean croissant) {
        return obtenirTransactionsTries(idCompte,
                croissant ? Comparator.comparing(Transaction::getDate)
                        : Comparator.comparing(Transaction::getDate).reversed());
    }

    private List<Transaction> obtenirTransactionsTries(int idCompte, Comparator<Transaction> comparator) {
        Optional<Compte> compteOpt = compteRepository.findById(idCompte);
        if (!compteOpt.isPresent()) {
            throw new CompteNotFoundException("Compte introuvable");
        }

        return compteOpt.get().getTransactions().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // Statistics methods using streams
    public BigDecimal calculerTotalDepots(int idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.DEPOT);
    }

    public BigDecimal calculerTotalRetraits(int idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.RETRAIT);
    }

    public BigDecimal calculerTotalVirements(int idCompte) {
        return calculerTotalParType(idCompte, TypeTransaction.VIREMENT);
    }

    private BigDecimal calculerTotalParType(int idCompte, TypeTransaction type) {
        return filtrerTransactionsParType(idCompte, type).stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Detect suspicious transactions
    public List<Transaction> detecterTransactionsSuspectes(int idCompte) {
        BigDecimal seuilSuspect = new BigDecimal("10000");

        return filtrerTransactions(idCompte, transaction ->
                transaction.getMontant().compareTo(seuilSuspect) > 0 ||
                        isTransactionRepetitive(idCompte, transaction));
    }

    private boolean isTransactionRepetitive(int idCompte, Transaction transaction) {
        long countSimilar = filtrerTransactions(idCompte, t ->
                t.getTypeTransaction().equals(transaction.getTypeTransaction()) &&
                        t.getMontant().equals(transaction.getMontant()) &&
                        !t.equals(transaction))
                .size();

        return countSimilar >= 3; // Suspicious if 3 or more similar transactions
    }
}
