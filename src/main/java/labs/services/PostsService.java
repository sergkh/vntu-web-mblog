package labs.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import labs.models.Post;
import labs.models.User;
import labs.repositories.PostRepository;
import labs.repositories.UserRepository;

@Service
public class PostsService {

	@Autowired
	private UserRepository usersRepo;
	
	@Autowired
	private PostRepository postsRepo;

	@Transactional
	public List<Post> getRecentPosts() {
		User currentUser = usersRepo.findOne(1L);
		
		return postsRepo.findByAuthorInOrderByCreatedAtAsc(
				currentUser.getSubscriptions());
	}
	
	@Transactional
	public void addPost(String text) {
		User currentUser = usersRepo.findOne(1L);
		postsRepo.save(new Post(currentUser, text, new Date()));
	}
}
