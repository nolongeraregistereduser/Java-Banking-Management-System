# Java Banking Management System

Une application console Java pour la gestion digitalisée des comptes bancaires et des transactions, développée dans le cadre d'un projet de digitalisation pour une banque marocaine.

## Table des Matières

- [Description](#description)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Structure du Projet](#structure-du-projet)
- [Technologies Utilisées](#technologies-utilisées)
- [Règles de Gestion](#règles-de-gestion)
- [Auteur](#auteur)

## Description

Cette application console Java simule un système de gestion bancaire permettant aux clients et gestionnaires de gérer efficacement les comptes bancaires (courant, épargne, dépôt à terme) et de suivre toutes les transactions avec des statistiques détaillées.

L'application respecte les principes de la programmation orientée objet et utilise les fonctionnalités avancées de Java 8+ (Collections, Streams, API Time, Lambda expressions).

## Fonctionnalités

### Pour les Clients
- Consultation des informations personnelles et comptes bancaires
- Affichage de l'historique des transactions
- Filtrage et tri des transactions (par type, montant, date)
- Calcul du solde total et des montants par type d'opération

### Pour les Gestionnaires
- Création, modification et suppression de clients et comptes
- Gestion complète des transactions (ajout, modification, suppression)
- Consultation et filtrage des transactions par client/compte
- Calcul automatique des soldes et totaux
- Identification des transactions suspectes

## Architecture

L'application suit le pattern **MVC (Model-View-Controller)** avec une couche Service :

```
src/
├── model/          # Entités métier (Client, Compte, Transaction)
├── view/           # Interface console et affichage
├── controller/     # Gestion des interactions utilisateur
├── service/        # Logique métier avancée
├── enums/          # Types de comptes et transactions
└── utils/          # Utilitaires et helpers
```

## Prérequis

- **Java 8** ou version supérieure
- **Git** pour le clonage du repository
- Un terminal ou IDE Java (IntelliJ IDEA, Eclipse, VSCode)

## Installation

1. **Cloner le repository**
   ```bash
   git clone https://github.com/nolongeraregistereduser/Java-Banking-Management-System.git
   cd Java-Banking-Management-System
   ```

2. **Compilation du projet**
   ```bash
   # Via ligne de commande
   javac -d bin src/**/*.java
   
   # Ou via votre IDE préféré
   ```

3. **Exécution directe avec le JAR**
   ```bash
   java -jar banking-system.jar
   ```

## Utilisation

### Démarrage de l'application
```bash
java -cp bin Main
```

### Navigation dans l'application

1. **Menu principal** : Choisir entre mode Client ou Gestionnaire
2. **Authentification** : Saisir email et mot de passe
3. **Menu fonctionnel** : Accéder aux différentes fonctionnalités selon le rôle

### Exemples d'utilisation

#### Création d'un nouveau client (Gestionnaire)
```
1. Se connecter en tant que gestionnaire
2. Choisir "Gestion des clients"
3. Sélectionner "Créer un nouveau client"
4. Saisir les informations requises
```

#### Consultation des transactions (Client)
```
1. Se connecter en tant que client
2. Choisir "Mes transactions"
3. Filtrer par date/type/montant si nécessaire
```

## Structure du Projet

```
Java-Banking-Management-System/
├── src/
│   ├── model/
│   │   ├── Personne.java          # Classe abstraite
│   │   ├── Client.java            # Entité Client
│   │   ├── Gestionnaire.java      # Entité Gestionnaire
│   │   ├── Compte.java            # Entité Compte
│   │   └── Transaction.java       # Entité Transaction
│   ├── enums/
│   │   ├── TypeCompte.java        # COURANT, EPARGNE, DEPOTATERME
│   │   └── TypeTransaction.java   # DEPOT, RETRAIT, VIREMENT
│   ├── service/
│   │   ├── ClientService.java     # Services clients
│   │   ├── CompteService.java     # Services comptes
│   │   └── TransactionService.java # Services transactions
│   ├── controller/
│   │   ├── ClientController.java
│   │   └── GestionnaireController.java
│   ├── view/
│   │   ├── MenuPrincipal.java
│   │   ├── ClientView.java
│   │   └── GestionnaireView.java
│   └── Main.java                  # Point d'entrée
├── docs/
│   └── diagramme-classes.puml     # Diagramme UML
├── banking-system.jar             # Fichier exécutable
└── README.md
```

## Technologies Utilisées

- **Java 8+** - Langage principal
- **Collections Framework** - List, Map, Set
- **Stream API** - Filtrage, tri, agrégation
- **Lambda Expressions** - Programmation fonctionnelle
- **Java Time API** - Gestion des dates
- **Optional** - Gestion des valeurs nulles
- **Functional Interfaces** - Predicate, Function, Consumer, Supplier

## Règles de Gestion

### Contraintes métier
- Un client peut posséder plusieurs comptes bancaires
- Chaque compte appartient à un seul client
- Les virements nécessitent un solde suffisant
- Tous les montants doivent être positifs

### Gestion des exceptions
- `IllegalArgumentException` → Montant négatif
- `NoSuchElementException` → Client/compte introuvable
- `IllegalStateException` → Transaction non valide
- `ArithmeticException` → Solde insuffisant

## Auteur

**[Nolongeraregistereduser]**
- GitHub: [@nolongeraregistereduser](https://github.com/nolongeraregistereduser)

---

## Licence

Ce projet est développé bga3 l7ob wl passion

---

*Développé pour la digitalisation bancaire au Maroc*
