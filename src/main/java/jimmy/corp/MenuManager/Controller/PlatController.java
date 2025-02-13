package jimmy.corp.MenuManager.Controller;

import org.springframework.data.domain.Pageable;

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
import jimmy.corp.MenuManager.Entity.Plat;
import jimmy.corp.MenuManager.Repository.PlatRepository;

@Controller
public class PlatController {

    private PlatRepository repo;

    public PlatController( PlatRepository pr ){
        this.repo = pr;
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
            pagePlat = this.repo.rechercheParContenu('%'+motsCles+'%', pageable);
        }else{
            pagePlat = this.repo.findAll(pageable);
        }

        model.addAttribute("listePlats", pagePlat.getContent());
        model.addAttribute("page", pagePlat);
        model.addAttribute("mc", motsCles);

        if (id>0 && ("new".equals(act) || "mod".equals(act))) {
            this.repo.findById(id).ifPresent(
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
            Optional<Plat> optPro = this.repo.findById(id);
            if (optPro.isPresent()) {
                model.addAttribute("plat", optPro.get());
            }else {
                return "redirect:/plats";
            }
        } else {
            model.addAttribute("plat", new Plat());
        }
        model.addAttribute("mc", motsCles);
        model.addAttribute("p", page);
        model.addAttribute("s", size);

        return "platEdit";
    }

    
    @PostMapping("/platSave")
    public String saveProduit(
        int s,
        int p,
        String mc,
        @Valid Plat plat, 
        BindingResult bindingResult,
        Model model
    ){

        if ( bindingResult.hasErrors()) {
            model.addAttribute("mc", mc);
            model.addAttribute("p", p);
            model.addAttribute("s", s);
            model.addAttribute("plat", plat);
            return "platEdit";
        }

        String action = (plat.getId() == null) ? "new" : "mod";
        this.repo.save(plat);


        return "redirect:/plats?p="+p+"&s="+s+"&mc="+mc+"&act="+action+"&id="+plat.getId();
    }

    @GetMapping("/platDelete")
    public String supprimerProduit(
        int s,
        int p,
        String mc,
        Integer id,
        RedirectAttributes redirectAttributes 
    ){
        this.repo.deleteById(id);

        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("mc", mc);

        return "redirect:/plats?&act=del";
    }



}

