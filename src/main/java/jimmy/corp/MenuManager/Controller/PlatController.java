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
import jimmy.corp.MenuManager.Entity.Plat;
import jimmy.corp.MenuManager.Repository.CategorieRepository;
import jimmy.corp.MenuManager.Repository.MenuRepository;
import jimmy.corp.MenuManager.Repository.PlatRepository;

@Controller
public class PlatController {

    private PlatRepository platRepo;
    private CategorieRepository categRepo;
    private MenuRepository menuRepo;


    public PlatController( PlatRepository pr, CategorieRepository cr, MenuRepository mr) {
        this.platRepo = pr;
        this.categRepo = cr;
        this.menuRepo = mr;
    }

    @GetMapping("/plats")
    public String listerPlats(
        @RequestParam(value = "mc", defaultValue = "")   String  motsCles,
        @RequestParam(value = "p", defaultValue = "0")   int     page,
        @RequestParam(value = "s", defaultValue = "5") int     size,
        @RequestParam(value = "act", defaultValue = "5") String     act,
        @RequestParam(value = "id", defaultValue = "0")  Integer id,
        Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Plat> pagePlat;
        if(motsCles.length()>0){
            pagePlat = this.platRepo.rechercheParContenu('%'+motsCles+'%', pageable);
        }else{
            pagePlat = this.platRepo.findAll(pageable);
        }

        model.addAttribute("listePlats", pagePlat.getContent());
        model.addAttribute("page", pagePlat);
        model.addAttribute("mc", motsCles);

        if (id>0 && ("new".equals(act) || "mod".equals(act))) {
            this.platRepo.findById(id).ifPresent(
                prod -> {
                    model.addAttribute("plat", prod);
                    model.addAttribute("act",act);
                }
            );
        } else if ("del".equals(act)) {
            model.addAttribute("act", act);
        }

        return "plats";
    }

    @GetMapping("/platEdit")
    public String editerProduit(
        @RequestParam(value = "mc", defaultValue = "")  String  motsCles,
        @RequestParam(value = "p", defaultValue = "0")  int     page,
        @RequestParam(value = "s", defaultValue = "5")  int     size,
        @RequestParam(value = "id", defaultValue = "0") Integer id,
        Model model
    ) {
        if (id>0){
            Optional<Plat> optPro = this.platRepo.findById(id);
            if (optPro.isPresent()) {
                model.addAttribute("plat", optPro.get());
            }else {
                return "redirect:/plats";
            }
        } else {
            model.addAttribute("plat", new Plat());
        }
        List<Categorie> categories = categRepo.findAll();
        model.addAttribute("categs", categories);
        model.addAttribute("mc", motsCles);
        model.addAttribute("p", page);
        model.addAttribute("s", size);

        return "platEdit";
    }

    
    @PostMapping("/platSave")
    public String saveProduit(
        @RequestParam int s,
        @RequestParam int p,
        @RequestParam String mc,
        @Valid Plat plat,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mc", mc);
            model.addAttribute("p", p);
            model.addAttribute("s", s);
            // N'oublie pas de remettre la liste des catégories en cas d'erreur
            model.addAttribute("listeCategories", categRepo.findAll());
            return "platEdit";
        }
        
        // La propriété 'categorie' de l'objet 'plat' est déjà renseignée via le binding.
        platRepo.save(plat);
        
        String action = (plat.getId() == null) ? "new" : "mod";
        return "redirect:/plats?p=" + p + "&s=" + s + "&mc=" + mc + "&act=" + action + "&id=" + plat.getId();
    }
    


    @GetMapping("/platDelete")
    public String supprimerProduit(
        @RequestParam int s,
        @RequestParam int p,
        @RequestParam String mc,
        @RequestParam Integer id,
        RedirectAttributes redirectAttributes
    ) {
        // Récupère le plat à supprimer
        Optional<Plat> optPlat = platRepo.findById(id);
        if(optPlat.isPresent()){
            Plat plat = optPlat.get();
            // Pour chaque menu qui référence ce plat, retirez-le de la liste
            for(Menu menu : plat.getMenus()){
                menu.getPlats().remove(plat);
                menuRepo.save(menu);
            }
            // Maintenant, on peut supprimer le plat
            platRepo.delete(plat);
        }
        
        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("mc", mc);
        return "redirect:/plats?&act=del";
    }
}

