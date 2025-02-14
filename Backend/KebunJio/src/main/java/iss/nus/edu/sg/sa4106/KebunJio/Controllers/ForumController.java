package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.CommentDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.CommentLikeDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostWithCommentDAO;
import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostWithUpvoteDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Models.CommentLike;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PostES;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.CommentLikeService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.CommentService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UpvoteService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;
import jakarta.servlet.http.HttpSession;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Forum")
public class ForumController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UpvoteService upvoteService;
	
	@Autowired
	private CommentLikeService clService;
	@Autowired
	private UserService userService;
	//URL: /Forum
	@GetMapping
	public ResponseEntity getAllPosts() {
		List<Post> postList = postService.getAllPosts();
		List<PostWithUpvoteDAO> resultList = new ArrayList<>();
		for(Post post : postList) {
			int upvoteCount = upvoteService.getUpvoteCountByPost(post.getId());
			
			Optional<User> postUserOp = userService.getUserById(post.getId());
			String username = "";
			if(postUserOp.isPresent()) {
				User postUser = postUserOp.get();
				username=postUser.getUsername();
			}
			List<Comment> commentList = commentService.getCommentsByPostId(post.getId());
			int commentCount = commentList.size();
			PostWithUpvoteDAO result = new PostWithUpvoteDAO();
			result.setPost(post);
			result.setUpvoteCount(upvoteCount);
			result.setCommentCount(commentCount);
			result.setUsername(username);
			resultList.add(result);
		}
		return new ResponseEntity<>(resultList,HttpStatus.OK);
	}
	
	// URL:/Forum/Post/Create
	// User info store in Session
	@PostMapping("/Post/Create")
	public ResponseEntity createNewPost(@RequestBody @Valid PostDAO postData,BindingResult bindingResult,HttpSession sessionObj){
		// wait for user Create 
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		
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
	public ResponseEntity getPostById(@PathVariable String id) {
		Post post = postService.getPostByPostId(id);
		List<Comment> commentList = commentService.getCommentsByPostId(id);
		for(Comment comment : commentList) {
			Map<String,Integer> likeAndDislikeCount = clService.getLikeAndDislikeCountByComment(comment.getId());
	
	        int likeCount = (likeAndDislikeCount != null) ? likeAndDislikeCount.getOrDefault("likeCount", 0) : 0;
	        int dislikeCount = (likeAndDislikeCount != null) ? likeAndDislikeCount.getOrDefault("dislikeCount", 0) : 0;
			
	        comment.setLikeCount(likeCount);
			comment.setDislikeCount(dislikeCount);
		}
		
		if(post!=null) {
			PostWithCommentDAO postWithComments = new PostWithCommentDAO(post,commentList);
			return new ResponseEntity<>(postWithComments,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// URL: /Forum/Post/{id}
	@PutMapping("/Post/{id}")
	public ResponseEntity updatePostById(@PathVariable String id,@RequestBody PostDAO newPost,HttpSession sessionObj) {
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		
		Post editPost = postService.getPostByPostId(id);
		if(!editPost.getUserId().equals(userId)) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		
		List<Post> postList = postService.getPostsByUserId(userId);
		return new ResponseEntity<>(postList,HttpStatus.OK);
	}
	
	// URL: /Forum/Post/{id}/Upvote
	@PutMapping("/Post/{id}/Upvote")
	public ResponseEntity upvotePost(@PathVariable String id,boolean hasUpvoted,HttpSession sessionObj) {
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		
		if(upvoteService.calculateUpvote(id,userId)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	// URL: /Forum/Post/{id}/CreateComment
	@PostMapping("/Post/{id}/CreateComment")
	public ResponseEntity createComment(HttpSession sessionObj,@PathVariable String id,@RequestBody CommentDAO commentDAO) {
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		String postId = id;
		if(commentService.createComment(commentDAO,postId,userId)) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// URL: /Forum/Post/{id}/CommentLike
	@PutMapping("/Post/Comment/{commentId}/Like")
	public ResponseEntity likeComment(@PathVariable String commentId,HttpSession sessionObj) {
		// Check the likeStatus
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		String userId = currentUser.getId();
		CommentLike currentCL = clService.getCommentLikeByUserAndComment(userId, commentId);
		
		// if not like the comment
		if(currentCL==null) {
			if(clService.createCommentLike(userId, commentId,"like")) {
				String message = "Like successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	
		}else if(currentCL!=null) {
			if(currentCL.isLike()) {
				// if have liked, cancel like
				clService.updateCommentLike(currentCL, "like", false);
				String message ="Cancel like successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}else {
				clService.updateCommentLike(currentCL, "like", true);
				clService.updateCommentLike(currentCL, "dislike", false);
				String message = "Like and cancel dislike successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/Post/Comment/{commentId}/Dislike")
	public ResponseEntity dislikeComment(@PathVariable String commentId,HttpSession sessionObj) {
		// Check the likeStatus
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		String userId = currentUser.getId();
		CommentLike currentCL = clService.getCommentLikeByUserAndComment(userId, commentId);
		
		// if not like the comment
		if(currentCL==null) {
			if(clService.createCommentLike(userId, commentId,"dislike")) {
				String message = "Dislike successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	
		}else if(currentCL!=null) {
			if(currentCL.isDislike()) {
				// if have liked, cancel like
				clService.updateCommentLike(currentCL, "dislike", false);
				String message ="Cancel dislike successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}else {
				clService.updateCommentLike(currentCL, "like", false);
				clService.updateCommentLike(currentCL, "dislike", true);
				String message = "Dislike and cancel like successful";
				return new ResponseEntity<>(message,HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/Post/Comment/{commentId}/Edit")
	public ResponseEntity editComment(@PathVariable String commentId,@RequestBody CommentDAO updateComment,HttpSession sessionObj) {
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
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
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();

		
		Post deletePost = postService.getPostByPostId(id);

		if(!currentUser.isAdmin()) {
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
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		
		String userId = currentUser.getId();
		
		Comment deleteComment = commentService.getCommentByCommentId(commentId); 

		if(!currentUser.isAdmin()) {
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
	
	//Search
	@GetMapping("/Search")
	public ResponseEntity searchPosts(@RequestParam String query) {
		List<PostES> searchResult = postService.searchES(query);
	    List<Map<String, Object>> responseList = new ArrayList<>();

	    for (PostES postES : searchResult) {
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("post", postES);
	        responseMap.put("upvoteCount", upvoteService.getUpvoteCountByPost(postES.getId()));
	        responseMap.put("commentCount", commentService.getCommentsByPostId(postES.getId()).size());

	        responseList.add(responseMap);
	    }
		return new ResponseEntity<>(responseList,HttpStatus.OK);
	}
	
	
}
