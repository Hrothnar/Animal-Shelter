package re.st.animalshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.service.InformationService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final InformationService informationService;

    @Autowired
    public StorageController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping("/menu")
    public String menu() {
        return "storage/storage_menu";
    }

    @GetMapping("/add")
    public String getForm(Model model) {
        model.addAttribute("buttons", Button.values());
        model.addAttribute("shelters", Shelter.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("person_types", new LinkedList<>(List.of(true, false)));
        return "storage/storage_form";
    }

    @PostMapping("/receive")
    public String receiveData(@RequestParam("button") Button button,
                              @RequestParam("shelter") Shelter shelter,
                              @RequestParam("status") Status status,
                              @RequestParam("person_type") boolean owner,
                              @RequestParam("text") String text,
                              @RequestParam("file") MultipartFile file) {
        informationService.saveInformation(button, shelter, status, owner, text, file);
        return "redirect:/storage/menu";
    }


}
