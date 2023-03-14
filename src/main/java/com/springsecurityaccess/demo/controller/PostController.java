package com.springsecurityaccess.demo.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurityaccess.demo.model.Post;
import com.springsecurityaccess.demo.model.PostStatus;
import com.springsecurityaccess.demo.repository.Postrepository;

@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	private Postrepository postrepository;
	
	
	@PostMapping("/create")
	public String createPost(@RequestBody Post post, Principal principal) {
		post.setStatus(PostStatus.PENDING);
		post.setUserName(principal.getName());
		postrepository.save(post);
		return principal.getName()+" you post has been successfully published, requires Admin/Moderator approval";
	}
	
	@GetMapping("/approve/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approveRequest(@PathVariable int postId) {
		Post post=postrepository.findById(postId).get();
		post.setStatus(PostStatus.APPROVED);
		postrepository.save(post);
		return "post has been approved";
	}
	
	@GetMapping("/approveAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approveAll() {
		postrepository.findAll().stream().filter(post->post.getStatus().equals(PostStatus.PENDING)).forEach(post->{
			post.setStatus(PostStatus.APPROVED);
			postrepository.save(post);
		});
		return "Approved all posts!";
	}
	
	@GetMapping("/reject/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String rejectPost(@PathVariable int postId) {
		Post post=postrepository.findById(postId).get();
		post.setStatus(PostStatus.REJECTED);
		postrepository.save(post);
		return "The post was rejected";
	}
	
	@GetMapping("/viewAllApproved")
	public List<Post> viewAll(){
		return postrepository.findAll().stream().filter(post-> post.getStatus().equals(PostStatus.APPROVED)).collect(Collectors.toList());
	}
	
	@GetMapping("/viewAll")
	public List<Post> viewAllApproved(){
		return postrepository.findAll();
	}
	
	@GetMapping("/rejectAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String rejectAll() {
		postrepository.findAll().stream().filter(post-> post.getStatus().equals(PostStatus.PENDING)).forEach(post->{
			post.setStatus(PostStatus.REJECTED);
			postrepository.save(post);
		});
		return "All post has been rejected";
	}
}
