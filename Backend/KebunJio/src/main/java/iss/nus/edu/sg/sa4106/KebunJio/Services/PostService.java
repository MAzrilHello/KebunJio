package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PostES;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PostESRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PostRepository;

/* ToDiscuss:
 * 1. Do we need a interface for all service?
*/
@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private PostESRepository postESRepository;
	
	//Function1: CreatePost
	public boolean createPost(PostDAO newPostData,String userId) {
		boolean result = false;
		
		Post newPost = new Post();
		// Set the new Post
		newPost.setTitle(newPostData.title);
		newPost.setContent(newPostData.content);
		newPost.setPostCategory(newPostData.postCategory);
		newPost.setPublishedDateTime(LocalDateTime.now());
		newPost.setUserId(userId);
		try {
			postRepository.save(newPost);
			syncES(newPost);
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Created Post Error");
		}
		
		return result;
	}
	
	//Function2: GetAllPosts
	public List<Post> getAllPosts(){
		return postRepository.findAll();
	}
	
	//Function3: GetPostByPostId
	public Post getPostByPostId(String id) {
		Optional<Post> post = postRepository.findById(id);
		if(post.isPresent()) {
			return post.get();
		}else {
			return null;
		}
	}
	
	//Function4: UpdatePostByPostId
	public boolean updatePostByPostId(String id,PostDAO updatePost) {
		boolean result = false;
		// try to find the post
		Optional<Post> post = postRepository.findById(id);
		if(post.isPresent()) {
			Post newPost = post.get();
			newPost.setTitle(updatePost.title);
			newPost.setContent(updatePost.content);
			newPost.setPostCategory(updatePost.postCategory);
			try {
				postRepository.save(newPost);
				syncES(newPost);
				result = true;
			}catch(Exception e) {
				throw new RuntimeException("Update Post Error");
			}
		}
		
		return result;
	}
	
	//Function5: GetPostByUserId
	public List<Post> getPostsByUserId(String userId){
		List<Post> postList = postRepository.findByUserId(userId);
		Collections.sort(postList,(p1,p2)-> p2.getPublishedDateTime().compareTo(p1.getPublishedDateTime()));
		return postList;
	}
	
	//Function6: DeletePostByPostId
	@Transactional
	public boolean deletePostByPostId(String id) {
		boolean result = false;
		if(postRepository.existsById(id)) {
			postRepository.deleteById(id);
			postESRepository.deleteById(id);
			result = true;
		}
		return result;
	}
	
	
	// DataSync to ES
	public Post syncES(Post post) {
		
        PostES postES = new PostES();
        postES.setId(post.getId());
        postES.setTitle(post.getTitle());
        postES.setContent(post.getContent());
        postES.setPublishedDateTime(post.getPublishedDateTime());
        postES.setUserId(post.getUserId());
        postES.setPostCategory(post.getPostCategory());
        postESRepository.save(postES);

        return post;
	}
	
	//Search ES
	public List<PostES> searchES(String query){
		return postESRepository.findByTitleContainingOrContentContaining(query, query);
	}
	//Function for Junit and GithubActions
	public int add(int a,int b) {
		return a+b;
	}
}
