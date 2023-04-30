package re.st.animalshelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

//@Controller
//@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String menu() {
        return "/menu";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }
//
//    @PostMapping("/find")
//    public String find(HttpServletRequest request, Model model) {
//        String userName = request.getParameter("userName");
//        String email = request.getParameter("email");
//        String passport = request.getParameter("passport");
//        User user = userService.getUser(userName, email, passport);
//        if (Objects.isNull(user)) {
//            return "not_found";
//        }
//        model.addAttribute("user", user);
//        return "show";
//    }
//
//    @PostMapping("/edit")
//    public String edit(HttpServletRequest request) {
//        long id = Long.parseLong(request.getParameter("id"));
//        String fullName = request.getParameter("fullName");
//        String phoneNumber = request.getParameter("phoneNumber");
//        String email = request.getParameter("email");
//        String passport = request.getParameter("passport");
//        userService.updateData(id, fullName, phoneNumber, email, passport);
//        return "greeting";
//    }
//
//    @GetMapping("/new")
//    public String create(Model model) {
//        model.addAttribute("user", new User());
//        return "show";
//    }

}

