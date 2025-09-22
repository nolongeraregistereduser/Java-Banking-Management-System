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



    public Transaction(){

    }
}
