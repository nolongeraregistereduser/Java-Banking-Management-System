# Java Banking Management System

A console-based banking management application for Moroccan banks, built with Java 8. This project demonstrates object-oriented programming, collections, Java Time API, Streams, and the MVC architecture.

## Features

- **Client Operations:**
  - Register and log in as a client
  - View personal information and accounts
  - Display transaction history (deposit, withdrawal, transfer)
  - Filter and sort transactions by type, amount, or date
  - Calculate total balance and totals for deposits/withdrawals

- **Manager Operations:**
  - Log in as manager (email: `manager`, password: `manager`)
  - Create, modify, or delete clients and their accounts
  - Add, modify, or delete transactions for any account
  - View and filter transactions for any client
  - Identify suspicious transactions (high amounts, repetitive operations)
  - Generate statistics and reports

## Technical Stack

- **Language:** Java 8
- **Architecture:** MVC (Model-View-Controller)
- **Collections:** List, Map
- **Streams & Lambdas:** Filtering, sorting, aggregation
- **Functional Interfaces:** Predicate, Function, Consumer, Supplier
- **Optional:** For handling absent values
- **Java Time API:** For transaction dates
- **Exception Handling:** Custom exceptions for business rules

## Project Structure

```
src/
  Main.java
  controller/
    ClientController.java
    CompteController.java
  model/
    Client.java
    Compte.java
    Gestionnaire.java
    Personne.java
    Transaction.java
    TypeCompte.java
    TypeTransaction.java
    exceptions/
      ClientNotFoundException.java
      CompteNotFoundException.java
      MontantInvalideException.java
      SoldeInsufficientException.java
      TransactionInvalideException.java
  repository/
    ClientRepository.java
    CompteRepository.java
  service/
    ClientService.java
    CompteService.java
  ui/
    MenuConsole.java
```

## How to Run

1. Clone the repository
2. Open in your favorite Java IDE (e.g., IntelliJ IDEA)
3. Make sure your JDK is set to Java 8
4. Run `Main.java`

## Authentication

- **Manager:**
  - Email: `manager`
  - Password: `manager`
- **Client:**
  - Register via the console menu

## UML Diagram

![UML Diagram](PLACEHOLDER_FOR_UML_IMAGE)

> Replace the above placeholder with your UML diagram image.

## License

MIT
