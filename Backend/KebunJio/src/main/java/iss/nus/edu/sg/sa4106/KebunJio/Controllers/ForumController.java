package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "*")
public class ForumController {
	@Autowired
	private PostService postService;
	
	//URL: /Forum
	@GetMapping
	public ResponseEntity getAllPosts() {
		List<Post> postList = postService.getAllPosts();
		return new ResponseEntity<>(postList,HttpStatus.OK);
	}
	
	// URL:/Forum/Post/Create
	@PostMapping("/Post/Create")
	public ResponseEntity createNewPost(@RequestBody @Valid PostDAO postData,BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(bindingResult.getAllErrors(),HttpStatus.BAD_REQUEST);
		}
		
		if(postService.createPost(postData)) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
			String message = "Created Post Error";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// URL: /Forum/Post/{id}
	@GetMapping("/Post/{id}")
	public ResponseEntity getPostById(@PathVariable String id) {
		Post post = postService.getPostByPostId(id);
		if(post!=null) {
			return new ResponseEntity<>(post,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
