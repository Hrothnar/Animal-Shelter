package re.st.animalshelter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.service.AnimalService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.service.VolunteerService;

import java.util.Set;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {
    private final UserService userService;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;

    @Autowired
    public VolunteerController(UserService userService, AnimalService animalService, VolunteerService volunteerService) {
        this.userService = userService;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
    }

    @GetMapping("/menu")
    public String menu() {
        return "volunteer/menu";
    }

    @GetMapping("/appoint")
    public String appoint(Model model) {
        model.addAttribute("users", userService.getAllByPosition(Position.USER));
        return "volunteer/appoint";
    }

    @PostMapping("/save_appoint")
    public String saveVolunteer(@RequestParam("userName") String userName, @RequestParam("user_id") long userId) {
        boolean exist = userService.attendVolunteer(userId, userName);
        return exist ? "redirect:/volunteer/menu" : "not_found";
    }

    @GetMapping("/find")
    public String findVolunteer(Model model) {
        model.addAttribute("volunteers", volunteerService.getAllVolunteers());
        return "volunteer/find";
    }

    @PostMapping("/receive")
    public String foundVolunteer(@RequestParam("userName") String userName, @RequestParam("volunteer_id") long id) {
        long volunteerId = volunteerService.getId(id, userName);
        return volunteerId != -1L ? "redirect:/volunteer/" + volunteerId : "not_found";
    }

    @GetMapping("/{id}")
    public String getVolunteerAction(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "volunteer/actions";
    }

    @GetMapping("/{id}/remove")
    public String removeVolunteer(@PathVariable("id") long id) {
        userService.removeVolunteer(id);
        return "volunteer/removed";
    }

    @GetMapping("/{id}/attach_animal")
    public String attachAnimal(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("cats", animalService.getCatsWithoutVolunteer());
        model.addAttribute("dogs", animalService.getDogsWithoutVolunteer());
        return "volunteer/attach_animal";
    }

    @PostMapping("/{id}/save_animal")
    public String saveAnimal(@PathVariable("id") long id, @RequestParam("animal_id") long animalId) {
        volunteerService.attachAnimal(id, animalId);
        return "redirect:/volunteer/" + id;
    }

    @GetMapping("/{id}/remove_animal")
    public String removeAnimal(@PathVariable("id") long id, Model model) {
        Set<Animal> animals = volunteerService.getById(id).getAnimals();
        model.addAttribute("animals", animals);
        return "volunteer/remove_animal";
    }

    @PostMapping("/{id}/save_removed_animal")
    public String saveRemovedAnimal(@PathVariable("id") long id, @RequestParam("animal_id") long animalId) {
        volunteerService.removeAnimal(id, animalId);
        return "redirect:/volunteer/" + id;
    }
}
