package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String emailOrUsername,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            User user = userService.loginUser(emailOrUsername, password);
            if(emailOrUsername=="Admin"&&password=="admin"){
                return "redirect:/dashboard";
            }
            session.setAttribute("loggedInUser", user);
            return "redirect:/userProfile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/current")
    @ResponseBody
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }


    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }


    @PostMapping("/process-register")
    public String processRegister(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "redirect:/register";
        }
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(password);

            userService.registerUser(newUser);

            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
