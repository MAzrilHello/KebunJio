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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userProfile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfilePlantHistoryService plantHistoryService;
	private static final String UPLOAD_DIR = "uploads/";

	@PostMapping("/upload-avatar")
	public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpSession sessionObj) {

		User user = (User) sessionObj.getAttribute("loggedInUser");
		if (user == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String originalFilename = file.getOriginalFilename();
			if (originalFilename == null || originalFilename.isEmpty()) {
				return new ResponseEntity<>("Invalid file name", HttpStatus.BAD_REQUEST);
			}

			String filename = "user_" + user.getId() + "_" + originalFilename;
			Path filePath = uploadPath.resolve(filename);

			Files.write(filePath, file.getBytes());

			String fileUrl = "/uploads/" + filename;

			user.setAvatarUrl(fileUrl);
			userService.saveUser(user);
			sessionObj.setAttribute("loggedInUser", user);

			return ResponseEntity.ok(fileUrl);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
		}
	}

	@GetMapping
    public ResponseEntity showProfilePage(HttpSession sessionObj) {
    	User user = (User) sessionObj.getAttribute("loggedInUser");
    	if(user == null) {
    		String message =  "Can not found User";
    		return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    	}
    	
    	List<Plant> history = plantHistoryService.getPlantsByUserId(user.getId());
    	
    	long totalPlanted = history.size();
    	long uniquePlantTypes = history.stream().map(Plant::getEdiblePlantSpeciesId).distinct().count();
    	
    	UserprofileDAO userProfileInfo = new UserprofileDAO(user,history,totalPlanted,uniquePlantTypes);
    	
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

    @PostMapping("/update")
    public ResponseEntity updateProfile(@RequestParam String username,
    		                            @RequestParam String email,
    		                            @RequestParam String phoneNumber,
										@RequestParam(required = false) String avatarUrl,
    		                            HttpSession sessionObj){
    	User user = (User) sessionObj.getAttribute("loggedInUser");
    	if(user == null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	
    	User updateUser = userService.UpdateUser(user, username, email, phoneNumber,avatarUrl);
		userService.saveUser(user);
    	sessionObj.setAttribute("loggedInUser", updateUser);
    	return new ResponseEntity<>(HttpStatus.OK);
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
