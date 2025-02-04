package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserProfilePlantHistoryService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfilePlantHistoryService plantHistoryService;

    @GetMapping
    public String showProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Plant> history = plantHistoryService.getPlantsByUser(user);

        long totalPlanted = history.size();
        long uniquePlantTypes = history.stream().map(Plant::getEdiblePlantSpecies).distinct().count();

        model.addAttribute("user", user);
        model.addAttribute("totalPlanted", totalPlanted);
        model.addAttribute("uniquePlantTypes", uniquePlantTypes);
        model.addAttribute("plantHistory", history);

        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        User updatedUser = userService.UpdateUser(user, username, email, phoneNumber);
        session.setAttribute("loggedInUser", updatedUser);

        model.addAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
