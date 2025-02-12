package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.UserprofileDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserProfilePlantHistoryService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/userProfile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfilePlantHistoryService plantHistoryService;

    @GetMapping
    public ResponseEntity showProfilePage(HttpSession sessionObj) {
    	User user = (User) sessionObj.getAttribute("loggedInUser");
    	if(user == null) {
    		String message =  "Can not foun User";
    		return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    	}
    	
    	List<Plant> history = plantHistoryService.getPlantsByUserId(user.getId());
    	
    	long totalPlanted = history.size();
		long totalHarvested = history.stream().filter(Plant::getHarvested).count();
    	long uniquePlantTypes = history.stream().map(Plant::getEdiblePlantSpeciesId).distinct().count();
    	
    	UserprofileDAO userProfileInfo = new UserprofileDAO(user,history,totalPlanted,totalHarvested,uniquePlantTypes);
    	
    	return new ResponseEntity<>(userProfileInfo,HttpStatus.OK);
    }
//    public String showProfilePage(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        List<Plant> history = plantHistoryService.getPlantsByUser(user);
//
//        long totalPlanted = history.size();
//        long uniquePlantTypes = history.stream().map(Plant::getEdiblePlantSpecies).distinct().count();
//
//        model.addAttribute("user", user);
//        model.addAttribute("totalPlanted", totalPlanted);
//        model.addAttribute("uniquePlantTypes", uniquePlantTypes);
//        model.addAttribute("plantHistory", history);
//
//        return "userProfile";
//    }

	@PutMapping ("/update")
	public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updateData, HttpSession sessionObj) {
		try {
			User user = (User) sessionObj.getAttribute("loggedInUser");
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			}

			String username = updateData.get("username");
			String email = updateData.get("email");
			String phoneNumber = updateData.get("phoneNumber");

			User updatedUser = userService.UpdateUser(user, username, email, phoneNumber);
			sessionObj.setAttribute("loggedInUser", updatedUser);

			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error updating profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



//    public String updateProfile(
//            @RequestParam String username,
//            @RequestParam String email,
//            @RequestParam String phoneNumber,
//            HttpSession session,
//            Model model) {
//        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        User updatedUser = userService.UpdateUser(user, username, email, phoneNumber);
//        session.setAttribute("loggedInUser", updatedUser);
//
//        model.addAttribute("success", "Profile updated successfully!");
//        return "redirect:/userProfile";
//    }
}
