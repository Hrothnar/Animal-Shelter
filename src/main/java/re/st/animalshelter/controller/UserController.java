package re.st.animalshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.service.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;
    private final ReportService reportService;
    private final FileService fileService;

    @Autowired
    public UserController(UserService userService,
                          AnimalService animalService,
                          VolunteerService volunteerService,
                          ReportService reportService,
                          FileService fileService) {
        this.userService = userService;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
        this.reportService = reportService;
        this.fileService = fileService;
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "user/menu";
    }

    @GetMapping("/find")
    public String findUser(Model model) {
        model.addAttribute("users", userService.getAll());
        return "user/find";
    }

    @PostMapping("/receive")
    public String foundUser(@RequestParam("userName") String userName, @RequestParam("user_id") long id) {
        long userId = userService.getId(id, userName);
        return userId != -1L ? "redirect:/user/" + userId : "not_found";
    }

    @GetMapping("/{id}")
    public String getUserActions(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "user/actions";
    }

    @GetMapping("/{id}/update")
    public String updateUser(@PathVariable long id, Model model) {
        User user = userService.getById(id);
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
        Set<Animal> animals = userService.getById(id).getActiveAnimals();
        model.addAttribute("id", id);
        model.addAttribute("animals", animals);
        return "user/add_time";
    }

    @PostMapping("/{id}/save_time")
    public String saveTime(@PathVariable("id") long id,
                           @RequestParam("animal_id") long animalId,
                           @RequestParam("time") int time) {
        userService.addProbationTime(id, animalId, time);
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/probation")
    public String endProbation(@PathVariable("id") long id, Model model) {
        Set<Animal> animals = userService.getById(id).getActiveAnimals();
        model.addAttribute("id", id);
        model.addAttribute("animals", animals);
        return "user/probation";
    }

    @PostMapping("/{id}/save_probation")
    public String saveProbation(@PathVariable("id") long id,
                                @RequestParam("animal_id") long animalId,
                                @RequestParam("button") String button) {
        userService.updateProbation(id, animalId, button);
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/animal")
    public String attachAnimal(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("volunteers", volunteerService.getAllVolunteers());
        model.addAttribute("cats", animalService.getCatsWithoutOwner());
        model.addAttribute("dogs", animalService.getDogsWithoutOwner());
        return "user/attach_animal";
    }

    @PostMapping("/{id}/animal_attach")
    public String saveAttachedAnimal(@PathVariable("id") long id,
                                     @RequestParam("animal_id") long animalId,
                                     @RequestParam("volunteer_id") long volunteerId) {
        userService.attachAnimal(id, animalId, volunteerId);
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/reports")
    public String chooseUserReports(@PathVariable("id") long id, Model model) {
        LinkedList<Report> reports = userService.getById(id).getReports().stream()
                .sorted(Comparator.comparing(Report::getTime).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
        model.addAttribute("reports", reports);
        return "user/reports";
    }

    @PostMapping("/{id}/report")
    public String showReport(@PathVariable("id") long id, @RequestParam("report_id") long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        String text = fileService.getText(report.getTextPath());
        model.addAttribute("id", id);
        model.addAttribute("report", report);
        model.addAttribute("text", text);
        return "user/report";
    }

    @PostMapping("/{id}/update_report")
    public String updateReport(@PathVariable("id") long id,
                               @RequestParam("report_id") long reportId,
                               @RequestParam("button") String button) {
        long chatId = userService.getById(id).getChatId();
        reportService.updateReport(chatId, reportId, button);
        return "redirect:/user/" + id;
    }
}

