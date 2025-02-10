package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.RegisterDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// changed to Rest API
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

//    public String showLoginPage() {
//        return "login";
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String emailOrUsername,
                                    @RequestParam String password,
                                    HttpSession sessionObj) {
        try {
            // Hardcoded logic for testing
            if ("username1".equals(emailOrUsername) && "password123".equals(password)) {
                // Simulate fetching user from MongoDB
                User user = new User();
                user.setId("679ecd82057c505d560bdbcb");
                user.setUsername("username1");
                user.setEmail("username1@gmail.com");
                user.setPhoneNumber("+6592720645");
                user.setAdmin(false);

                // Store the user in session
                sessionObj.setAttribute("loggedInUser", user);
                sessionObj.setAttribute("userId", user.getId());

                // Remove password before returning response
                user.setPassword(null);

                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                // Invalid credentials
                return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Login failed due to an internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public String login(@RequestParam String emailOrUsername,
//                        @RequestParam String password,
//                        HttpSession session,
//                        Model model) {
//
//        try {
//        	// may have some problem
//            if (emailOrUsername.equals("Admin") && password.equals("admin")) {
//                User adminUser = new User();
//                adminUser.setUsername("Admin");
//                adminUser.setEmail("admin@example.com");
//                session.setAttribute("loggedInUser", adminUser);
//                return "redirect:/dashboard";
//            }
//            User user = userService.loginUser(emailOrUsername, password);
//            // have logic problem
//            if (user == null) {
//                model.addAttribute("error", "Invalid username or password!");
//                return "redirect:/userProfile";
//            }
//
//            session.setAttribute("loggedInUser", user);
//            // we need userId
//            session.setAttribute("userId",user.getId());
//            return "redirect:/userProfile";
//
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "login";
//        }
//    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession sessionObj) {
    	sessionObj.invalidate();
    	return new ResponseEntity<>(HttpStatus.OK);
    }
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login";
//    }

      
      @GetMapping("/current")
      public ResponseEntity getCurrentUser(HttpSession sessionObj) {
		  User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		  return new ResponseEntity<>(currentUser != null ? currentUser : new User(), HttpStatus.OK);

	  }
//    @ResponseBody
//    public User getCurrentUser(HttpSession session) {
//        return (User) session.getAttribute("loggedInUser");
//    }


//    @GetMapping("/register")
//    public String showRegisterPage() {
//        return "register";
//    }


    @PostMapping("/signup")
    public ResponseEntity processRegister(@RequestBody RegisterDAO registerInfo) {
    	
    	if(!registerInfo.password.equals(registerInfo.confirmPassword)) {
    		String message = "Password is not same with confirmPassword";
    		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    	}
    	try {
    		User newUser = new User();
    		newUser.setAdmin(false);
    		newUser.setEmail(registerInfo.email);
    		newUser.setPassword(registerInfo.password);
    		newUser.setPhoneNumber(registerInfo.contactPhone);
    		newUser.setUsername(registerInfo.username);
    		
    		userService.registerUser(newUser);
    		return new ResponseEntity<>(HttpStatus.CREATED);
    	}catch(Exception e) {
    		throw new RuntimeException("Created Error");
    	}
    	
    }
//    public String processRegister(
//            @RequestParam String email,
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String confirmPassword,
//            Model model) {
//        if (!password.equals(confirmPassword)) {
//            model.addAttribute("error", "Passwords do not match!");
//            return "register";
//        }
//        try {
//            User newUser = new User();
//            newUser.setEmail(email);
//            newUser.setUsername(username);
//            newUser.setPassword(password);
//
//            userService.registerUser(newUser);
//
//            model.addAttribute("success", "Registration successful! Please login.");
//            return "redirect:/login";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "register";
//        }
//    }
}
