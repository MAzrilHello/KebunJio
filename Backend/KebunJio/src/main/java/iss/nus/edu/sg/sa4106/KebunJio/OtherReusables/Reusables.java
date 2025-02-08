package iss.nus.edu.sg.sa4106.KebunJio.OtherReusables;

import org.springframework.http.HttpStatus;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;

public class Reusables {
	public static HttpStatus editStatusType(User loggedInUser, String userId) {
		if (loggedInUser == null) {
			return HttpStatus.UNAUTHORIZED;
		} else if (loggedInUser.getId().equals(userId)) {
			return HttpStatus.OK;
		} else {
			return HttpStatus.FORBIDDEN;
		}
		
	}
}
