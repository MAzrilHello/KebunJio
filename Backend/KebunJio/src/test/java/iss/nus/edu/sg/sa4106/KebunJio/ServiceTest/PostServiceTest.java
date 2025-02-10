package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;

@SpringBootTest
public class PostServiceTest {
	@Autowired
	private PostService postService;
	
	@Test
	void testAdd() {
		int result = postService.add(1, 3);
		Assertions.assertEquals(4,result);
	}
	
}
