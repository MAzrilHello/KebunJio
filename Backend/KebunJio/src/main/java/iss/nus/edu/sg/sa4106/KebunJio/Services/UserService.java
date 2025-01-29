package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
//Check if user exists
    public boolean isUserExists(String email, String username) {
        return userRepository.findByEmail(email) != null || userRepository.findByUsername(username) != null;
    }

    public User registerUser(User user) {
        try {

            if (userRepository.findByUsername(user.getUsername()) != null ||
                    userRepository.findByEmail(user.getEmail()) != null) {
                throw new RuntimeException("Email or Username already exists!");
            }
            String encryptedPassword = AES_password.encrypt(user.getPassword());
            user.setPassword(encryptedPassword);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error during user registration: " + e.getMessage());
        }
    }

    public User loginUser(String emailOrUsername, String password) {
        try {
            User user = userRepository.findByEmail(emailOrUsername);
            if (user == null) {
                user = userRepository.findByUsername(emailOrUsername);
            }

            if (user != null) {
                String decryptedPassword = AES_password.decrypt(user.getPassword());

                if (decryptedPassword.equals(password)) {
                    return user;
                }
            }

            throw new RuntimeException("Invalid credentials!");

        } catch (Exception e) {
            throw new RuntimeException("Error during user login: " + e.getMessage());
        }
    }
}
