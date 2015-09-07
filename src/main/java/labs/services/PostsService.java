package labs.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import labs.models.Post;
import labs.models.User;
import labs.repositories.PostRepository;
import labs.repositories.UserRepository;

@Service
public class PostsService {
	private static final int PAGE_SIZE = 10; 

	@Autowired
	private UserRepository usersRepo;
	
	@Autowired
	private PostRepository postsRepo;

	@Transactional
	public List<Post> getPosts(int page) {
		User currentUser = usersRepo.findOne(1L);
		
		return postsRepo.findByAuthorInOrderByCreatedAtDesc(
				currentUser.getSubscriptions(),
				new PageRequest(page-1, PAGE_SIZE) // spring рахує сторінки з нуля
		);
	}

	@Transactional
	public int pagesCount() {
		User currentUser = usersRepo.findOne(1L);
		int postsCount = postsRepo.countByAuthorIn(currentUser.getSubscriptions());
		
		// Math.ceil - округляє до найближчого цілого числа, більшого або рівного аргументу
		// наприклад 1.1 → 2, 7 → 7 
		return (int) Math.ceil(postsCount / (double)PAGE_SIZE);
	}

	
	@Transactional
	public void addPost(String text) {
		User currentUser = usersRepo.findOne(1L);
		postsRepo.save(new Post(currentUser, text, new Date()));
	}
}
