package jimmy.corp.MenuManager.Controller;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jimmy.corp.MenuManager.Entity.Categorie;
import jimmy.corp.MenuManager.Entity.Menu;
import jimmy.corp.MenuManager.Repository.CategorieRepository;
import jimmy.corp.MenuManager.Repository.MenuRepository;
import jimmy.corp.MenuManager.Repository.PlatRepository;

@Controller
public class MenuController {

    private MenuRepository menuRepo;
    private PlatRepository platRepo;

    public MenuController( MenuRepository mr,PlatRepository pr ) {
        this.menuRepo = mr;
        this.platRepo = pr;
    }

    @GetMapping("/menus")
    public String listerMenus(
        @RequestParam(value = "mc", defaultValue = "")   String  motsCles,
        @RequestParam(value = "p", defaultValue = "0")   int     page,
        @RequestParam(value = "s", defaultValue = "5") int     size,
        @RequestParam(value = "act", defaultValue = "5") String     act,
        @RequestParam(value = "id", defaultValue = "0")  Integer id,
        Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> pageMenu;
        if(motsCles.length()>0){
            pageMenu = this.menuRepo.rechercheParContenu('%'+motsCles+'%', pageable);
        }else{
            pageMenu = this.menuRepo.findAll(pageable);
        }

        model.addAttribute("listeMenus", pageMenu.getContent());
        model.addAttribute("page", pageMenu);
        model.addAttribute("mc", motsCles);

        if (id>0 && ("new".equals(act) || "mod".equals(act))) {
            this.menuRepo.findById(id).ifPresent(
                prod -> {
                    model.addAttribute("menu", prod);
                    model.addAttribute("act",act);
                }
            );
        } else if ("del".equals(act)) {
            model.addAttribute("act", act);
        }

        return "menus";
    }


    @GetMapping("/menuEdit")
    public String editerMenu(
            @RequestParam(value = "id", defaultValue = "0") Integer id,
            Model model
    ) {
        if (id > 0) {
            Optional<Menu> optMenu = menuRepo.findById(id);
            if (optMenu.isPresent()) {
                model.addAttribute("menu", optMenu.get());
            } else {
                // Si l'id fourni ne correspond à aucun Menu, rediriger vers la liste des menus
                return "redirect:/menus";
            }
        } else {
            model.addAttribute("menu", new Menu());
        }
        // Si votre Menu contient une liste de plats, ajoutez-la dans le modèle
        model.addAttribute("listePlats", platRepo.findAll());
        return "menuEdit";
    }

    @PostMapping("/menuSave")
    public String saveMenu(
            @RequestParam(value = "s", defaultValue = "5") int s,
            @RequestParam(value = "p", defaultValue = "0") int p,
            @RequestParam(value = "mc", defaultValue = "") String mc,
            @Valid Menu menu,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("s", s);
            model.addAttribute("p", p);
            model.addAttribute("mc", mc);
            // Remets la liste des plats pour le multi-select (si nécessaire)
            model.addAttribute("listePlats", platRepo.findAll());
            return "menuEdit";
        }

        // Détermine si c'est une création ou une modification
        boolean isNew = (menu.getId() == 0);
        menuRepo.save(menu);
        
        String act = isNew ? "new" : "mod";

        // On passe les paramètres de pagination et l'action via redirect attributes
        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("mc", mc);
        redirectAttributes.addAttribute("act", act);
        // On ajoute également le menu en flash attribute pour pouvoir afficher son nom dans l'alerte
        redirectAttributes.addFlashAttribute("menu", menu);

        return "redirect:/menus";
    }




    @GetMapping("/menuDelete")
    public String supprimerProduit(
        int s,
        int p,
        String mc,
        Integer id,
        RedirectAttributes redirectAttributes 
    ){
        this.menuRepo.deleteById(id);

        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("mc", mc);

        return "redirect:/menus?&act=del";
    }



}

