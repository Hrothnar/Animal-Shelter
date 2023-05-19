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
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.service.StorageService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/menu")
    public String menu() {
        return "storage/storage_menu";
    }

    @GetMapping("/add")
    public String getForm(Model model) {
        model.addAttribute("buttons", Button.getValidButtons());
        model.addAttribute("statuses", Status.getValidStatuses());
        model.addAttribute("commands", Command.getValidCommands());
        model.addAttribute("shelters", Shelter.values());
        model.addAttribute("person_types", new LinkedList<>(List.of(StorageService.ANY, StorageService.USER, StorageService.OWNER)));
        return "storage/storage_form";
    }

    @PostMapping("/receive")
    public String receiveData(@RequestParam("code") String code,
                              @RequestParam("shelter") Shelter shelter,
                              @RequestParam("person_type") String person,
                              @RequestParam("text") String text,
                              @RequestParam("file") MultipartFile file) {
        storageService.saveInformation(code, shelter, person, text, file);
        return "redirect:/storage/menu";
    }


}
