package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.LoginDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.RegisterDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



// changed to Rest API
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

//    public String showLoginPage() {
//        return "login";
//    }



    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDAO loginDAO,
    		                      HttpSession sessionObj,
    		                      Model model){
    	try {
    		if(loginDAO.emailOrUsername.equals("Admin") && loginDAO.password.equals("admin")) {
    			// get the adminUser Info
    			User adminUser = userService.loginUser(loginDAO.emailOrUsername, loginDAO.password);
    			sessionObj.setAttribute("loggedInUser",adminUser);
    			return new ResponseEntity<User>(adminUser,HttpStatus.OK);
    		}else {
    			User user = userService.loginUser(loginDAO.emailOrUsername, loginDAO.password);
    			if(user == null) {
    				// not find the user
    				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    			}else {
    				sessionObj.setAttribute("loggedInUser", user);
    				sessionObj.setAttribute("userId", user.getId());
    				return new ResponseEntity<User>(user,HttpStatus.OK);
    			}
    		}
    	}catch(Exception e) {
    		throw new RuntimeException("Login failed");
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
    
    @PutMapping("/current")
    public ResponseEntity<User> editProfile(@RequestBody User updateInfo, HttpSession sessionObj) {
    	System.out.println("ActivityLog: getting currentUser");
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		if (currentUser == null) {
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}
		try {
			Optional<User> findUser = userService.getUserById(currentUser.getId());
			if (findUser.isEmpty()) {
				return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			}
			User updatedUser = userService.UpdateUser(findUser.get(),
														updateInfo.getUsername(),
														updateInfo.getEmail(),
														updateInfo.getPhoneNumber());
			sessionObj.setAttribute("loggedInUser", updatedUser);
			return new ResponseEntity<User>(updatedUser,
											HttpStatus.OK);
		} catch (RuntimeException e) {
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
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
