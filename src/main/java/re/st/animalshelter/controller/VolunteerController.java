package re.st.animalshelter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String create() {
        return "volunteer/appoint";
    }

    @PostMapping("/save_appoint")
    public String saveVolunteer(@RequestParam("userName")String userName) {
        boolean exist = userService.attendVolunteer(userName);
        return exist ? "redirect:/volunteer/menu" : "user/not_found";
    }




//
//    @GetMapping("/chat")
//    public ResponseEntity<?> chat() {
//        return null;
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<?> showAllReports() {
//        return null;
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<?> showOwnerReports(@PathVariable("id") String id) {
//        return null;
//    }
//
//    @PatchMapping("/add/{id}")
//    public ResponseEntity<?> addPetToOwner(@PathVariable("id") String id) {
//        return null;
//    }
//
//    @PatchMapping("/remove/{id}")
//    public ResponseEntity<?> removePetFromOwner(@PathVariable("id") String id) {
//        return null;
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteOldOwners() {
//        return null;
//    }


}
