package re.st.animalshelter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("petowner")
public class VolunteerController {

    @GetMapping("/chat")
    public ResponseEntity<?> chat() {
        return null;
    }

    @GetMapping("/")
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
