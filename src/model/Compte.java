package model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Compte {

    UUID idCompte;
    TypeCompte typeCompte;
    BigDecimal solde;
    List<Transaction> transactions;
    UUID idClient;


    public compte(UUID idCompte, TypeCompte typeCompte, BigDecimal solde, List<Transaction> transactions, UUID idClient) {
        this.idCompte = UUID.randomUUID();
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.transactions = transactions;
        this.idClient = idClient;
    }

    public UUID getIdCompte() {
        return idCompte;
    }


    public TypeCompte getTypeCompte() {
        return typeCompte;
    }


    public BigDecimal getSolde() {
        return solde;
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }


    public UUID getIdClient() {
        return idClient;
    }


}
