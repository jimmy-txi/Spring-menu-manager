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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jimmy.corp.MenuManager.Entity.Categorie;
import jimmy.corp.MenuManager.Entity.Menu;
import jimmy.corp.MenuManager.Repository.CategorieRepository;
import jimmy.corp.MenuManager.Repository.MenuRepository;

@Controller
public class MenuController {

    private MenuRepository menuRepo;

    public MenuController( MenuRepository pr) {
        this.menuRepo = pr;
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

