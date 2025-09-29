package model;

import java.math.BigDecimal;
import java.util.List;

public class Compte {
    private static int idCounter = 1;
    int idCompte;
    TypeCompte typeCompte;
    BigDecimal solde;
    List<Transaction> transactions;
    int idClient;

    public Compte(TypeCompte typeCompte, BigDecimal solde, List<Transaction> transactions, int idClient) {
        this.idCompte = idCounter++;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.transactions = transactions;
        this.idClient = idClient;
    }

    public int getIdCompte() {
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

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public void ajouterTransaction(Transaction transaction) {
        if (transaction != null && !transactions.contains(transaction)) {
            transactions.add(transaction);
        }
    }
}
