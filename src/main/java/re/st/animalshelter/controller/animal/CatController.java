package re.st.animalshelter.controller.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import re.st.animalshelter.enumeration.breed.CatBreed;
import re.st.animalshelter.service.AnimalService;

@Controller
@RequestMapping("/cat")
public class CatController {
    private final AnimalService animalService;

    @Autowired
    public CatController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/add")
    public String getForm(Model model) {
        model.addAttribute("breeds", CatBreed.values());
        return "animal/cat_form";
    }

    @PostMapping("/receive")
    public String receive(@RequestParam("age") int age,
                          @RequestParam("breed") CatBreed breed,
                          @RequestParam("amount") int amount) {
        animalService.saveCat(age, breed, amount);
        return "animal/menu";
    }

}
