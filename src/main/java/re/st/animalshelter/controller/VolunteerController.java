package re.st.animalshelter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.service.UserService;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {
    private final UserService userService;

    @Autowired
    public VolunteerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/menu")
    public String menu() {
        return "volunteer/volunteer_menu";
    }

    @GetMapping("/appoint")
    public String create(Model model) {
        model.addAttribute("users", userService.getAllByPosition(Position.USER));
        return "volunteer/appoint";
    }

    @PostMapping("/save_appoint")
    public String saveVolunteer(@RequestParam("userName")String userName,  @RequestParam("user_id") long userId) {
        boolean exist = userService.attendVolunteer(userId, userName);
        return exist ? "redirect:/volunteer/menu" : "user/not_found";
    }
}
