package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.CommentDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.CommentLikeDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostWithCommentDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.CommentService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Forum")
@CrossOrigin(origins = "*")
public class ForumController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;
	private UserService userService;
	
	//URL: /Forum
	@GetMapping
	public ResponseEntity<List<PostWithCommentDAO>> getAllPosts() {
		List<Post> postList = postService.getAllPosts();

		List<PostWithCommentDAO> postsWithAvatars = postList.stream().map(post -> {
			Optional<User> userOptional = userService.getUserById(post.getUserId());

			String avatarUrl = userOptional.map(User::getAvatarUrl).orElse("/default-avatar.png"); // 处理 Optional

			List<Comment> comments = commentService.getCommentsByPostId(post.getId());

			return new PostWithCommentDAO(post, comments, avatarUrl);
		}).collect(Collectors.toList());

		return new ResponseEntity<>(postsWithAvatars, HttpStatus.OK);
	}


	// URL:/Forum/Post/Create
	// User info store in Session
	@PostMapping("/Post/Create")
	public ResponseEntity createNewPost(@RequestBody @Valid PostDAO postData,BindingResult bindingResult,HttpSession SessionObj){
		// wait for user Create 
		String userId = (String) SessionObj.getAttribute("userId");
//		String userId = "679b022388afce6495e8dbca";
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(bindingResult.getAllErrors(),HttpStatus.BAD_REQUEST);
		}
		
		if(postService.createPost(postData,userId)) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
			String message = "Created Post Error";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// URL: /Forum/Post/{id}
	@GetMapping("/Post/{id}")
	public ResponseEntity<PostWithCommentDAO> getPostById(@PathVariable String id) {
		Post post = postService.getPostByPostId(id);
		List<Comment> commentList = commentService.getCommentsByPostId(id);

		if (post != null) {
			Optional<User> userOptional = userService.getUserById(post.getUserId());
			String avatarUrl = userOptional.map(User::getAvatarUrl).orElse("/default-avatar.png"); // 处理 Optional

			return new ResponseEntity<>(new PostWithCommentDAO(post, commentList, avatarUrl), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	// URL: /Forum/Post/{id}
	@PutMapping("/Post/{id}")
	public ResponseEntity updatePostById(@PathVariable String id,@RequestBody PostDAO newPost,HttpSession sessionObj) {
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		Post editPost = postService.getPostByPostId(id);
		if(!editPost.getUserId().equals(userId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(postService.updatePostByPostId(userId, newPost)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// URL: /Forum/User/Posts
	@GetMapping("/User/Posts")
	public ResponseEntity getPostsByUserId(HttpSession sessionObj) {
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		List<Post> postList = postService.getPostsByUserId(userId);
		return new ResponseEntity<>(postList,HttpStatus.OK);
	}
	
	// URL: /Forum/Post/{id}/Upvote
	public ResponseEntity upvotePost(@PathVariable String id,boolean hasUpvoted) {
		if(postService.calculateUpvote(id, hasUpvoted)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	// URL: /Forum/Post/{id}/CreateComment
	@PostMapping("/Post/{id}/CreateComment")
	public ResponseEntity createComment(HttpSession sessionObj,@PathVariable String id,@RequestBody CommentDAO commentDAO) {
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		String postId = id;
		if(commentService.createComment(commentDAO,postId,userId)) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// URL: /Forum/Post/{id}/CommentLike
	@PutMapping("/Post/Comment/{commentId}/Like")
	public ResponseEntity likeComment(@PathVariable String commentId,@RequestBody CommentLikeDAO likeStatus) {
		// Check the likeStatus
		boolean hasLiked = likeStatus.hasLiked;
		boolean hasDisliked = likeStatus.hasDisliked;
		
		// if not like the comment
		if(!hasLiked && hasDisliked) {
			// assume user has dislike the comment, update both like and dislike
			commentService.calculateLikeCount(commentId, hasLiked);
			commentService.calculateDislikeCount(commentId, hasDisliked);
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(hasLiked && hasDisliked) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			commentService.calculateLikeCount(commentId, hasLiked);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping("/Post/Comment/{commentId}/Dislike")
	public ResponseEntity dislikeComment(@PathVariable String commentId,@RequestBody CommentLikeDAO likeStatus) {
		// Check the likeStatus
		boolean hasLiked = likeStatus.hasLiked;
		boolean hasDisliked = likeStatus.hasDisliked;
		
		// if not like the comment
		if(hasLiked && !hasDisliked){
			// assume user has dislike the comment, update both like and dislike
			commentService.calculateLikeCount(commentId, hasLiked);
			commentService.calculateDislikeCount(commentId, hasDisliked);
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(hasLiked && hasDisliked) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			commentService.calculateDislikeCount(commentId, hasDisliked);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping("/Post/Comment/{commentId}/Edit")
	public ResponseEntity editComment(@PathVariable String commentId,@RequestBody CommentDAO updateComment,HttpSession sessionObj) {
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		Comment editComment = commentService.getCommentByCommentId(commentId);
		if(!editComment.getUserId().equals(userId)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			if(commentService.updateCommentByCommentId(commentId, updateComment.commentContent)) {
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@DeleteMapping("/User/Post/{id}")
	public ResponseEntity deletePost(@PathVariable String id,HttpSession sessionObj) {
		// check the post whether belongs to user?
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		User user = (User) sessionObj.getAttribute("user");
		
		Post deletePost = postService.getPostByPostId(id);

		if(!user.isAdmin()) {
			if(deletePost.getUserId().equals(userId)) {
				postService.deletePostByPostId(id);
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}else {
			postService.deletePostByPostId(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/Post/Comment/{commentId}")
	public ResponseEntity deleteComment(@PathVariable String commentId,HttpSession sessionObj) {
//		String userId = (String) sessionObj.getAttribute("userId");
		String userId = "679b022388afce6495e8dbca";
		User user = (User) sessionObj.getAttribute("user");
		
		Comment deleteComment = commentService.getCommentByCommentId(commentId); 

		if(!user.isAdmin()) {
			if(deleteComment.getUserId().equals(userId)) {
				commentService.deleteCommentByCommentId(commentId);
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}else {
			commentService.deleteCommentByCommentId(commentId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
}
