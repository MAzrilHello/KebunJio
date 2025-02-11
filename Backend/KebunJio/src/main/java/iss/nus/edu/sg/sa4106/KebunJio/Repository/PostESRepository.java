package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.PostES;

public interface PostESRepository extends ElasticsearchRepository<PostES,String> {
	
	// Find title or content
	List<PostES> findByTitleContainingOrContentContaining(String title,String content);

}
