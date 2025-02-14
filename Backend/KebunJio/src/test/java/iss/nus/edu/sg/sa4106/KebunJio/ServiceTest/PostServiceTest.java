package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;

public class PostServiceTest {

    private final PostService postService = new PostService();

    @Test
    void testAdd() {
        int result = postService.add(4, 4);
        Assertions.assertEquals(8, result);
    }
}
