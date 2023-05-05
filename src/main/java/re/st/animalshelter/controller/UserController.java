package re.st.animalshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.service.AnimalService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.service.VolunteerService;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;

    @Autowired
    public UserController(UserService userService, AnimalService animalService, VolunteerService volunteerService) {
        this.userService = userService;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "user/user_menu";
    }

    @GetMapping("/find")
    public String findUser() {
        return "user/user_find";
    }

    @PostMapping("/receive")
    public String receive(@RequestParam("userName") String userName,
                          @RequestParam("passport") String passport,
                          @RequestParam("email") String email) {
        long id = userService.getUserId(userName, email, passport);
        return id != -1L ? "redirect:/user/" + id : "user/not_found";
    }

    @GetMapping("/{id}")
    public String getUserAction(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "user/actions";
    }

    @GetMapping("/{id}/update")
    public String updateUser(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/{id}/save_update")
    public String saveUpdate(@PathVariable long id, @ModelAttribute("user") User user) {
        userService.updateData(id, user);
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/time")
    public String addTime(@PathVariable("id") long id, Model model) {
        Set<Animal> animals = userService.getUserById(id).getActiveAnimals();
        model.addAttribute("id", id);
        model.addAttribute("animals", animals);
        return "/user/add_time";
    }

    @PostMapping("/{id}/save_time")
    public String saveTime(@PathVariable("id") long userId,
                           @RequestParam("animal_id") long animalId,
                           @RequestParam("time") int time) {
        animalService.updateExpirationTime(animalId, time);
        return "redirect:/user/" + userId;
    }

    @GetMapping("/{id}/animal")
    public String attachAnimal(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("volunteers", volunteerService.getAllVolunteers());
        model.addAttribute("cats", animalService.getActiveCats());
        model.addAttribute("dogs", animalService.getActiveDogs());
        return "user/attach_animal";
    }

    @PostMapping("/{id}/animal_attach")
    public String saveAttachedAnimal(@PathVariable("id") long userId,
                                     @RequestParam("animal_id") long animalId,
                                     @RequestParam("volunteer_id") long volunteerId) {
        userService.attachAnimal(userId, animalId, volunteerId);
        return "redirect:/user/" + userId;
    }









//    @PostMapping("/find")
//    public String find(HttpServletRequest request, Model model) {
//        String userName = request.getParameter("userName");
//        String email = request.getParameter("email");
//        String passport = request.getParameter("passport");
//        User user = userService.getUser(userName, email, passport);
//        if (Objects.isNull(user)) {
//            return "not_found";
//        }
//        model.addAttribute("user", user);
//        return "show";
//    }
//
//    @PostMapping("/edit")
//    public String edit(HttpServletRequest request) {
//        long id = Long.parseLong(request.getParameter("id"));
//        String fullName = request.getParameter("fullName");
//        String phoneNumber = request.getParameter("phoneNumber");
//        String email = request.getParameter("email");
//        String passport = request.getParameter("passport");
//        userService.updateData(id, fullName, phoneNumber, email, passport);
//        return "greeting";
//    }
//
//    @GetMapping("/new")
//    public String create(Model model) {
//        model.addAttribute("user", new User());
//        return "show";
//    }

}

