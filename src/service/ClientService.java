package service;

import model.Client;
import model.Compte;
import model.TypeCompte;
import model.Transaction;
import model.exceptions.*;
import repository.ClientRepository;
import repository.CompteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClientService {

    private ClientRepository clientRepository;
    private CompteRepository compteRepository;

    public ClientService(ClientRepository clientRepository, CompteRepository compteRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
    }

    public Client creerClient(String nom, String prenom, String email, String motDePasse) {
        // Check if email already exists
        if (clientRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        Client client = new Client(nom, prenom, email, motDePasse);
        clientRepository.save(client);
        return client;
    }

    // Create account for client
    public Compte creerCompte(int idClient, TypeCompte typeCompte, BigDecimal soldeInitial) {
        Optional<Client> clientOpt = clientRepository.findById(idClient);
        if (!clientOpt.isPresent()) {
            throw new ClientNotFoundException("Client introuvable");
        }

        if (soldeInitial.compareTo(BigDecimal.ZERO) < 0) {
            throw new MontantInvalideException("Le solde initial ne peut pas être négatif");
        }

        Client client = clientOpt.get();
        Compte compte = new Compte(typeCompte, soldeInitial, new ArrayList<Transaction>(), idClient);

        client.ajouterCompte(compte);
        compteRepository.save(compte);
        clientRepository.save(client);

        return compte;
    }

    // Get client information
    public Optional<Client> obtenirClient(int idClient) {
        return clientRepository.findById(idClient);
    }

    // Update client information
    public void modifierClient(int idClient, String nom, String prenom, String email) {
        Optional<Client> clientOpt = clientRepository.findById(idClient);
        if (!clientOpt.isPresent()) {
            throw new ClientNotFoundException("Client introuvable");
        }

    }

    // Delete client and all associated accounts
    public void supprimerClient(int idClient) {
        Optional<Client> clientOpt = clientRepository.findById(idClient);
        if (!clientOpt.isPresent()) {
            throw new ClientNotFoundException("Client introuvable");
        }

        Client client = clientOpt.get();

        // Delete all client's accounts
        for (Compte compte : client.getComptes()) {
            compteRepository.deleteCompte(compte.getIdCompte());
        }

        clientRepository.deleteById(idClient);
    }

    // Calculate total balance for all client accounts
    public BigDecimal calculerSoldeTotal(int idClient) {
        Optional<Client> clientOpt = clientRepository.findById(idClient);
        if (!clientOpt.isPresent()) {
            throw new ClientNotFoundException("Client introuvable");
        }

        return clientOpt.get().getComptes().stream()
                .map(Compte::getSolde)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Get all transactions for a client with filtering capabilities
    public List<Transaction> obtenirTransactionsClient(int idClient, Predicate<Transaction> filter) {
        Optional<Client> clientOpt = clientRepository.findById(idClient);
        if (!clientOpt.isPresent()) {
            throw new ClientNotFoundException("Client introuvable");
        }

        return clientOpt.get().getComptes().stream()
                .flatMap(compte -> compte.getTransactions().stream())
                .filter(filter)
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Sort by date descending
                .collect(Collectors.toList());
    }

    // Calculate total deposits for client
    public BigDecimal calculerTotalDepots(int idClient) {
        return obtenirTransactionsClient(idClient,
                t -> t.getTypeTransaction().name().equals("DEPOT"))
                .stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate total withdrawals for client
    public BigDecimal calculerTotalRetraits(int idClient) {
        return obtenirTransactionsClient(idClient,
                t -> t.getTypeTransaction().name().equals("RETRAIT"))
                .stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Detect suspicious transactions (high amounts or repetitive operations)
    public List<Transaction> detecterTransactionsSuspectes(int idClient) {
        BigDecimal seuilSuspect = new BigDecimal("10000"); // Threshold for high amounts

        return obtenirTransactionsClient(idClient, t -> true)
                .stream()
                .filter(t -> t.getMontant().compareTo(seuilSuspect) > 0)
                .collect(Collectors.toList());
    }

    // List all clients
    public List<Client> listerTousLesClients() {
        return clientRepository.findAll();
    }

    // Authenticate client
    public Optional<Client> authentifierClient(String email, String motDePasse) {
        if ("manager".equals(email) && "manager".equals(motDePasse)) {
            // Return a dummy Client object for manager
            return Optional.of(new Client("Manager", "", "manager", "manager"));
        }
        return clientRepository.findByEmail(email)
                .filter(client -> client.getMotDePasse().equals(motDePasse));
    }
}
