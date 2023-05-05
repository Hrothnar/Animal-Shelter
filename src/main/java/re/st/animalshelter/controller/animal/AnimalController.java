package re.st.animalshelter.controller.animal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/animal")
public class AnimalController {

    @GetMapping("/menu")
    public String getMenu() {
        return "animal/animal_menu";
    }
}
