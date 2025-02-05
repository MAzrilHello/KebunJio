package iss.nus.edu.sg.sa4106.KebunJio.Services;

import com.mongodb.DuplicateKeyException;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Session;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.SessionRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private UserRepository userRepo;
    
    // create non-logged in session
    public Session startNewSession() {
    	Session session = new Session();
    	LocalDateTime currentTime = LocalDateTime.now();
    	session.setStartDateTime(currentTime);
    	session.setLastActionDateTime(currentTime);
    	sessionRepo.save(session);
    	return session;
    }
    
    // create logged in session
    public Session startNewSession(User user) {
    	Session session = new Session();
    	LocalDateTime currentTime = LocalDateTime.now();
    	session.setStartDateTime(currentTime);
    	session.setLastActionDateTime(currentTime);
    	session.setUser(user);
    	sessionRepo.save(session);
    	return session;
    }
    
    // get session
    public Optional<Session> getSession(String sessionId) {
    	return sessionRepo.findById(sessionId);
    }
    
    // get user and session
    //public Optional<Object[]> getSessionAndUser(int sessionId) {
    //	Optional<Object[]> sessionAndUser = sessionRepo.findUserAndSessionBySessionId(sessionId);
    //	return sessionAndUser;
    //}
    
    // get the session's user
    public Optional<User> getSessionUser(String sessionId) {
    	//Optional<Object[]> sessionAndUser = getSessionAndUser(sessionId);
    	//if (sessionAndUser.isPresent() == false) {
    	//	return Optional.empty();
    	//}
    	//Object[] theObject = sessionAndUser.get();
    	//Optional<User> theUser = Optional.of((User) theObject[1]);
    	//return theUser;
    	Optional<Session> findSession = getSession(sessionId);
    	if (findSession.isPresent() == false) {
    		return Optional.empty();
    	}
    	Session thisSession = findSession.get();
    	Optional<User> findUser = userRepo.findById(thisSession.getUser().getId());
    	return findUser;
    }
    
    // update the session time
    public boolean updateSessionTime(String sessionId) {
    	Optional<Session> findSession = sessionRepo.findById(sessionId);
    	if (findSession.isEmpty()) {
    		return false;
    	}
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	Session session = findSession.get();
    	session.setLastActionDateTime(currentTime);
    	sessionRepo.save(session);
    	
    	return true;
    }
    
    
    // end the session
    public boolean endSession(String sessionId) {
    	Optional<Session> findSession = sessionRepo.findById(sessionId);
    	if (findSession.isEmpty()) {
    		return false;
    	}
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	Session session = findSession.get();
    	session.setLastActionDateTime(currentTime);
    	session.setEndDateTime(currentTime);
    	sessionRepo.save(session);
    	
    	return true;
    }
    
    
    // logout
    //public Session logoutOfSession(String sessionId) {
    //	// find the session and user
    //	Optional<Session> sessionAndUser = getSession(sessionId);
    	// just start a new session if the id does not exist
   // 	if (sessionAndUser.isEmpty()) {
   // 		return startNewSession();
   // 	}
    //	Object[] theObject = sessionAndUser.get();
    	// check if there is a valid user (aka is logged in)
    //	Optional<User> theUser = Optional.of((User) theObject[1]);
    //	Session theSession = (Session) theObject[0];
    	// if no valid user return the original user-less session
    //	if (theUser.isEmpty()) {
    //		return theSession;
    //	}
    	// if was previously logged in
    	// end session
    //	endSession(theSession.getSessionId());
    //	return startNewSession();
    //}
}
