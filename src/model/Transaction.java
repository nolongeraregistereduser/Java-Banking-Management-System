package model;

import model.exceptions.MontantInvalideException;
import model.exceptions.TransactionInvalideException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    UUID idTransaction;
    TypeTransaction typeTransaction;
    BigDecimal montant;
    Date date;
    String motif;
    Compte compteSource;
    Compte compteDestination;



    public Transaction(UUID idTransaction, TypeTransaction typeTransaction, BigDecimal montant, Date date, String motif, Compte compteSource, Compte compteDestination) {


        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontantInvalideException("Le montant doit être positif et non null");
        }

        // Validation du type de transaction
        if (typeTransaction == null) {
            throw new TransactionInvalideException("Le type de transaction ne peut pas être null");
        }

        // Validation du compte source
        if (compteSource == null) {
            throw new TransactionInvalideException("Le compte source ne peut pas être null");
        }



        // Validation pour les virements
        if (typeTransaction == TypeTransaction.VIREMENT) {
            if (compteDestination == null) {
                throw new TransactionInvalideException("Le compte destination est obligatoire pour un virement");
            }
            if (compteSource.equals(compteDestination)) {
                throw new TransactionInvalideException("Le compte source et destination ne peuvent pas être identiques");
            }
        }

        if (typeTransaction != TypeTransaction.VIREMENT && compteDestination != null) {
            throw new TransactionInvalideException("Le compte destination ne doit être spécifié que pour les virements");
        }

        this.idTransaction = idTransaction;
        this.typeTransaction = typeTransaction;
        this.montant = montant;
        this.date = new Date(); // date actuelle
        this.motif = motif;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;


    }


    // Constructeur simplifié pour dépôt/retrait ( overloading )

    public Transaction(TypeTransaction typeTransaction, BigDecimal montant,
                       String motif, Compte compteSource) {
        this(UUID.randomUUID(), typeTransaction, montant, new Date(), motif, compteSource, null);
    }




    public UUID getIdTransaction() {
        return idTransaction;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }


    public BigDecimal getMontant() {
        return montant;
    }

    public Date getDate() {
        return date;
    }

    public String getMotif() {
        return motif;
    }


    public Compte getCompteSource() { return compteSource; }
    public Compte getCompteDestination() { return compteDestination; }


}
