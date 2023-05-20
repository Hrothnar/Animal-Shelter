package re.st.animalshelter.controller.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.st.animalshelter.service.AnimalService;

@Controller
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "animal/menu";
    }

    @GetMapping("/remove")
    public String removeAnimalFromShelter(Model model) {
        model.addAttribute("cats", animalService.getUnattachedCats());
        model.addAttribute("dogs", animalService.getUnattachedDogs());
        return "animal/remove";
    }

    @PostMapping("/save_remove")
    public String saveRemoveFromShelter(@RequestParam("animal_id") long id) {
        animalService.remove(id);
        return "redirect:/animal/menu";
    }
}
