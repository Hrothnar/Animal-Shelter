package re.st.animalshelter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class VolunteerController {


    @GetMapping()
    public String menu() {
//        model.addAttribute("name", "value");
        return "greeting";
    }

    @GetMapping("/chat")
    public ResponseEntity<?> chat() {
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<?> showAllReports() {
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> showOwnerReports(@PathVariable("id") String id) {
        return null;
    }

    @PatchMapping("/add/{id}")
    public ResponseEntity<?> addPetToOwner(@PathVariable("id") String id) {
        return null;
    }

    @PatchMapping("/remove/{id}")
    public ResponseEntity<?> removePetFromOwner(@PathVariable("id") String id) {
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOldOwners() {
        return null;
    }


}
