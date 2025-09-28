import controller.CompteController;
import repository.CompteRepository;
import service.CompteService;
import ui.MenuConsole;

public class Main {
    public static void main(String[] args) {
        // Créer les couches dans l'ordre
        CompteRepository compteRepository = new CompteRepository();
        CompteService compteService = new CompteService(compteRepository);
        CompteController compteController = new CompteController(compteService);

        // Lancer l'interface utilisateur
        MenuConsole menu = new MenuConsole(compteController);
        menu.afficherMenu();
    }
}
