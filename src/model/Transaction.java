package model;

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
        this.idTransaction = idTransaction;
        this.typeTransaction = typeTransaction;
        this.montant = montant;
        this.date = date;
        this.motif = motif;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;

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


}
