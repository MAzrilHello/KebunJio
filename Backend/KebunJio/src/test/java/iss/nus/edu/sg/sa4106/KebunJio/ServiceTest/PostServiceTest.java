package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.PostDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PostES;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PostESRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PostRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PostService;

@ExtendWith(MockitoExtension.class)
class PostServiceTest{
	// @Mock can build a mock object independent DB or Net
	@Mock
	private PostRepository postRepository;
	
    @Mock
    private PostESRepository postESRepository; // 确保 mock 了这个
	
    @Spy
    @InjectMocks
	private PostService postService;
    
    private PostService spyPostService;

	private PostDAO postDAO;
	
	private Post post;
	
	private Post post1;
	
	private PostES postES;
	
    
    @BeforeEach
    void setUp() {
        postDAO = new PostDAO();
        postDAO.title = "Test Title";
        postDAO.content = "Test Content";
        postDAO.postCategory = "Tech";

        post = new Post();
        post.setId("p0");
        post.setTitle(postDAO.title);
        post.setContent(postDAO.content);
        post.setPostCategory(postDAO.postCategory);
        post.setPublishedDateTime(LocalDateTime.now());
        post.setUserId("u1");
        
        post1 = new Post();
        post1.setId("p1");
        post1.setTitle("Post 1");
        post1.setContent("Post 1");
        post1.setPostCategory("Others");
        post1.setPublishedDateTime(LocalDateTime.now());
        post1.setUserId("u1");
        
        postES = new PostES();
        postES.setId(post.getId());
        postES.setTitle(post.getTitle());
        postES.setContent(post.getContent());
        postES.setPostCategory(post.getPostCategory());
        postES.setPublishedDateTime(post.getPublishedDateTime());
        postES.setUserId(post.getUserId());
        
    }
    
    //Test CreatePost
    @Test
    void testCreatePost_Success() {
    	when(postRepository.save(any(Post.class))).thenReturn(post);
    	
        boolean result = postService.createPost(postDAO, "1");
        
        // assert if result is true;
        assertTrue(result);
        
        // Verify whether service be used
        verify(postRepository, times(1)).save(any(Post.class));
    }
    
    @Test
    void testCreatePost_DB_Fail() {
    	//set throw a exception when execute postRepository.save()
    	when(postRepository.save(any(Post.class))).thenThrow(new RuntimeException("DB Error"));
    	
    	// assert the exception should be RuntimeException when execute createPost
    	Exception exception = assertThrows(RuntimeException.class,()->{
    		postService.createPost(postDAO, "1");
    	});
    	
    	// assert the message
    	assertEquals("Created Post Error",exception.getMessage());
    	
    	verify(postRepository,times(1)).save(any(Post.class));
    }
    //Test GetAllPosts
    @Test
    void testGetAllPosts() {
    	when(postRepository.findAll()).thenReturn(List.of(post,post1));
    	
    	List<Post> posts = postService.getAllPosts();
    	
    	assertNotNull(posts);
    	assertEquals(2,posts.size());
    	assertEquals("Test Title",posts.get(0).getTitle());
    	assertEquals("Post 1",posts.get(1).getTitle());
    	
    	verify(postRepository, times(1)).findAll();
    	
    }
    
    //Test GetPostByPostId
    @Test
    void testGetPostByPostId_Found() {
    	when(postRepository.findById("p0")).thenReturn(Optional.of(post));
    	
    	Post result = postService.getPostByPostId("p0");
    	
        assertNotNull(result);
        assertEquals("p0", result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
    
        verify(postRepository, times(1)).findById("p0");
    }
    
    @Test
    void testGetPostByPostId_NotFound() {
    	when(postRepository.findById("1")).thenReturn(Optional.empty());
    	
    	Post result = postService.getPostByPostId("1");
    	
    	assertNull(result);
    	
    	verify(postRepository,times(1)).findById("1");
    }
    
    //Test UpdatePostByPostId
    @Test
    void testUpdatePostByPostId_Success() {
    	when(postRepository.findById("p0")).thenReturn(Optional.of(post));
    	
    	PostDAO updatePost = new PostDAO();
    	updatePost.title="Update Title";
    	updatePost.content="Update Content";
    	updatePost.postCategory="Update Category";
    	
    	when(postRepository.save(any(Post.class))).thenReturn(post);
    	
    	boolean result = postService.updatePostByPostId("p0", updatePost);
    	
    	assertTrue(result);
        assertEquals("Update Title", post.getTitle());
        assertEquals("Update Content", post.getContent());
        assertEquals("Update Category", post.getPostCategory());
        
        verify(postRepository,times(1)).findById("p0");
        verify(postRepository, times(1)).save(any(Post.class));
    	
    }
    
    @Test
    void testUpdatePostByPostId_PostNotFound() {
        when(postRepository.findById("p0")).thenReturn(Optional.empty());
        
        PostDAO updatePost = new PostDAO();
    	updatePost.title="Update Title";
    	updatePost.content="Update Content";
    	updatePost.postCategory="Update Category";

        boolean result = postService.updatePostByPostId("p0", updatePost);

        assertFalse(result);
        
        verify(postRepository,times(1)).findById("p0");
        verify(postRepository, times(0)).save(any(Post.class));
    }
    
    @Test
    void testUpdatePostByPostId_SaveError() {
        when(postRepository.findById("p0")).thenReturn(Optional.of(post));
        
        PostDAO updatePost = new PostDAO();
    	updatePost.title="Update Title";
    	updatePost.content="Update Content";
    	updatePost.postCategory="Update Category";
    	
    	when(postRepository.save(any(Post.class))).thenThrow(new RuntimeException("Save Error"));
    	
       	Exception exception = assertThrows(RuntimeException.class,()->{
    		postService.updatePostByPostId("p0", updatePost);
    	});
    	
    	assertEquals("Update Post Error",exception.getMessage());
    	
    	verify(postRepository,times(1)).findById("p0");
    	verify(postRepository,times(1)).save(any(Post.class));
    }
    
    //Test GetPostByUserId
    @Test
    void testGetPostByUserId_NotNull() {
    	when(postRepository.findByUserId("u1")).thenReturn(new ArrayList<>(List.of(post,post1)));
    	
    	List<Post> result = postService.getPostsByUserId("u1");
    	
    	assertNotNull(result);
    	assertEquals(2,result.size());
    	assertEquals("u1",result.get(0).getUserId());
    	assertEquals("u1",result.get(1).getUserId());
    			
    	verify(postRepository,times(1)).findByUserId("u1");
    }
    
    @Test
    void testGetPostByUserId_Null() {
    	when(postRepository.findByUserId("2")).thenReturn(null);
    	
    	List<Post> result = postService.getPostsByUserId("2");
    	
    	assertNull(result);

    	verify(postRepository,times(1)).findByUserId("2");
    }
    
    //Test DeletePostByPostId
    @Test
    void testDeletePostByPostId_Exist() {
    	when(postRepository.existsById("p0")).thenReturn(true);
    	doNothing().when(postRepository).deleteById("p0");
    	
    	boolean result = postService.deletePostByPostId("p0");
    	
    	assertTrue(result);
    	
    	verify(postRepository,times(1)).existsById("p0");
    	verify(postRepository,times(1)).deleteById("p0");
    }
    
    @Test
    void testDeletePostByPostId_NotExist() {
    	when(postRepository.existsById("p2")).thenReturn(false);
    	
    	boolean result = postService.deletePostByPostId("p2");
    	
    	assertFalse(result);
    	
    	verify(postRepository,times(1)).existsById("p2");
    }
    
    //Test Sync ES
    @Test
    void testSyncES() {
    	when(postESRepository.save(any(PostES.class))).thenReturn(postES);
    	
    	Post result = postService.syncES(post);
    	
    	assertNotNull(result);
    	assertEquals("p0",post.getId());
    	
    	verify(postESRepository,times(1)).save(any(PostES.class));
    }
    
    //Test SearchES
    @Test
    void testSearchES() {
    	String query = "Test";
    	when(postESRepository.findByTitleContainingOrContentContaining(query, query)).thenReturn(List.of(postES));
    
    	List<PostES> result = postService.searchES(query);
    	
    	assertNotNull(result);
    	assertEquals(1,result.size());
    	assertEquals("Test Title",result.get(0).getTitle());
    	
    	verify(postESRepository,times(1)).findByTitleContainingOrContentContaining(query, query);
    }
    
}




