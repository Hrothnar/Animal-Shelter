package re.st.animalshelter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/animal")
public class AnimalController {


    @GetMapping("/add")
    public String getForm() {
        return "animal_form";
    }

    @PostMapping("/receive")
    public String receive(@RequestParam) {

    }







//    @PostMapping("/add")
//    public ResponseEntity<?> addAnimal(@RequestBody Animal animal, Shelter shelter) {
//        return null;
//    }
//
//    @PatchMapping("/update")
//    public RequestEntity<?> updateAnimal(@RequestBody Animal animal, Shelter shelter) {
//        return null;
//    }
//
//    @DeleteMapping("/remove{id}")
//    public ResponseEntity<?> removeAnimal(@PathVariable("id") String id) {
//        return null;
//    }


}
