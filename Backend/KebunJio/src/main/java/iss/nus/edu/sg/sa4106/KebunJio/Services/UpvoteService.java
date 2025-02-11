package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Upvote;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.UpvoteRepository;

@Service
public class UpvoteService {
	@Autowired
	private UpvoteRepository upvoteRepository;
	
	public int getUpvoteCountByPost(String postId) {
		 int result = 0;
		 List<Upvote> upvoteList = upvoteRepository.findByPostId(postId);
		 if(upvoteList==null) {
			 return result;
		 }
		 return upvoteList.size();
	}
	
	@Transactional
	public boolean calculateUpvote(String postId,String userId,boolean hasUpvoted) {
		boolean result = false;
		Optional<Upvote> upvoteOp = upvoteRepository.findUpvote(postId, userId);
		System.out.println("hasUpvoted: " + hasUpvoted);

		if(!hasUpvoted) {
		
			if(upvoteOp.isPresent()) {
				System.out.print(upvoteOp.get());
				return result;
			}
			Upvote newUpvoteForPost = new Upvote();
			newUpvoteForPost.setPostId(postId);
			newUpvoteForPost.setUserId(userId);
			try {
				upvoteRepository.save(newUpvoteForPost);
				result =true;
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("Upvote Failed");
			}
		}else {
		
			if(upvoteOp.isPresent()) {
				Upvote upvote = upvoteOp.get();
				System.out.print(upvote);
				upvoteRepository.delete(upvote);
				result = true;
			}
		}
		
		return result;
	}
}
