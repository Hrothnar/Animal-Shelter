package re.st.animalshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.st.animalshelter.dto.NotifyDTO;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.model.response.TextResponse;
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
    private final TextResponse textResponse;

    @Autowired
    public UserController(UserService userService,
                          AnimalService animalService,
                          VolunteerService volunteerService,
                          ReportService reportService,
                          FileService fileService,
                          TextResponse textResponse) {
        this.userService = userService;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
        this.reportService = reportService;
        this.fileService = fileService;
        this.textResponse = textResponse;
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "user/user_menu";
    }

    @GetMapping("/find")
    public String findUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/user_find";
    }

    @PostMapping("/receive")
    public String receive(@RequestParam("userName") String userName, @RequestParam("user_id") long userId) {
        System.out.println(userId + "   " + userName);
        long id = userService.getUserId(userId, userName);
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
        return "user/add_time";
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
        model.addAttribute("cats", animalService.getActiveCatsAsDTO());
        model.addAttribute("dogs", animalService.getActiveDogsAsDTO());
        return "user/attach_animal";
    }

    @PostMapping("/{id}/animal_attach")
    public String saveAttachedAnimal(@PathVariable("id") long userId,
                                     @RequestParam("animal_id") long animalId,
                                     @RequestParam("volunteer_id") long volunteerId) {
        userService.attachAnimal(userId, animalId, volunteerId);
        return "redirect:/user/" + userId;
    }

    @GetMapping("/{id}/report")
    public String chooseUserReports(@PathVariable("id") long userId, Model model) {
        LinkedList<Report> reports = userService.getUserById(userId).getReports().stream()
                .sorted(Comparator.comparing(Report::getTime).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
        model.addAttribute("reports", reports);
        return "user/report";
    }

    @PostMapping("/{id}/show_report")
    public String showReport(@PathVariable("id") long userId, @RequestParam("report_id") long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        String text = fileService.getText(report.getTextPath());
        model.addAttribute("id", userId);
        model.addAttribute("report", report);
        model.addAttribute("text", text);
        return "user/show_report";
    }

    @PostMapping("/{id}/update_report")
    public String updateReport(@PathVariable("id") long userId,
                               @RequestParam("report_id") long reportId,
                               @RequestParam("button") String button) {
        long chatId = userService.getUserById(userId).getChatId();
        NotifyDTO notifyDTO = reportService.updateReport(chatId, reportId, button);
        textResponse.sendNotify(notifyDTO);
        return "redirect:/user/" + userId;
    }


}

