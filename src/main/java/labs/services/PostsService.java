package labs.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	public Page<Post> getPosts(int page, int pageSize) {
		User currentUser = usersRepo.findOne(1L);
		
		return postsRepo.findByAuthorInOrderByCreatedAtDesc(
				currentUser.getSubscriptions(),
				new PageRequest(page-1, pageSize) // spring рахує сторінки з нуля
		);
	}

	@Transactional
	public void addPost(String text) {
		User currentUser = usersRepo.findOne(1L);
		postsRepo.save(new Post(currentUser, text, new Date()));
	}
}
