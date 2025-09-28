import controller.CompteController;
import controller.ClientController;
import repository.CompteRepository;
import repository.ClientRepository;
import service.CompteService;
import service.ClientService;
import ui.MenuConsole;

public class Main {
    public static void main(String[] args) {
        // Créer les repositories
        CompteRepository compteRepository = new CompteRepository();
        ClientRepository clientRepository = new ClientRepository();

        // Créer les services
        CompteService compteService = new CompteService(compteRepository);
        ClientService clientService = new ClientService(clientRepository, compteRepository);

        // Créer les contrôleurs
        CompteController compteController = new CompteController(compteService);
        ClientController clientController = new ClientController(clientService, compteService);

        // Lancer l'interface utilisateur avec les deux contrôleurs
        MenuConsole menu = new MenuConsole(compteController, clientController);
        menu.afficherMenu();
    }
}
