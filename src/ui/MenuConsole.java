package ui;

import controller.CompteController;
import model.exceptions.*;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class MenuConsole {

    private CompteController compteController;
    private Scanner scanner;

    public MenuConsole(CompteController compteController) {
        this.compteController = compteController;
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenu() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n=== SYSTÈME BANCAIRE ===");
            System.out.println("1. Effectuer un dépôt");
            System.out.println("2. Effectuer un retrait");
            System.out.println("3. Effectuer un virement");
            System.out.println("4. Consulter le solde");
            System.out.println("5. Afficher l'historique");
            System.out.println("6. Fermer un compte");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne

            try {
                switch (choix) {
                    case 1:
                        effectuerDepot();
                        break;
                    case 2:
                        effectuerRetrait();
                        break;
                    case 3:
                        effectuerVirement();
                        break;
                    case 4:
                        consulterSolde();
                        break;
                    case 5:
                        afficherHistorique();
                        break;
                    case 6:
                        fermerCompte();
                        break;
                    case 0:
                        continuer = false;
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (CompteNotFoundException | SoldeInsufficientException | MontantInvalideException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void effectuerDepot() {
        System.out.print("ID du compte : ");
        UUID idCompte = UUID.fromString(scanner.nextLine());
        System.out.print("Montant à déposer : ");
        BigDecimal montant = scanner.nextBigDecimal();
        scanner.nextLine();

        compteController.deposer(idCompte, montant);
    }

    private void effectuerRetrait() {
        System.out.print("ID du compte : ");
        UUID idCompte = UUID.fromString(scanner.nextLine());
        System.out.print("Montant à retirer : ");
        BigDecimal montant = scanner.nextBigDecimal();
        scanner.nextLine();

        compteController.retirer(idCompte, montant);
    }

    private void effectuerVirement() {
        System.out.print("ID du compte source : ");
        UUID idCompteSource = UUID.fromString(scanner.nextLine());
        System.out.print("ID du compte destination : ");
        UUID idCompteDestination = UUID.fromString(scanner.nextLine());
        System.out.print("Montant à virer : ");
        BigDecimal montant = scanner.nextBigDecimal();
        scanner.nextLine();

        compteController.virer(idCompteSource, idCompteDestination, montant);
    }

    private void consulterSolde() {
        System.out.print("ID du compte : ");
        UUID idCompte = UUID.fromString(scanner.nextLine());

        compteController.afficherSolde(idCompte);
    }

    private void afficherHistorique() {
        System.out.print("ID du compte : ");
        UUID idCompte = UUID.fromString(scanner.nextLine());

        compteController.afficherHistorique(idCompte);
    }

    private void fermerCompte() {
        System.out.print("ID du compte à fermer : ");
        UUID idCompte = UUID.fromString(scanner.nextLine());

        compteController.fermerCompte(idCompte);
    }
}
