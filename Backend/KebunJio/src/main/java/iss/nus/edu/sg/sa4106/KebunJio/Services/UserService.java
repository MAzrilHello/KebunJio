package iss.nus.edu.sg.sa4106.KebunJio.Services;

import com.mongodb.DuplicateKeyException;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            user.setId(null);

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

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error during user login: " + e.getMessage());
        }


    }

    public User UpdateUser(User user,String username, String email, String phoneNumber){
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
