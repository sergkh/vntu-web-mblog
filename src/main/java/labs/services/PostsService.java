package labs.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import labs.models.Post;

@Service
public class PostsService {
	private static final List<Post> INITIAL_POSTS = Arrays.asList(
		new Post("Admin", "First post", new Date()),
		new Post("Tester", "Second post", new Date()),
		new Post("Admin", "Third post", new Date())
	); 
	
	private List<Post> posts = new ArrayList<Post>(INITIAL_POSTS);
	
	public List<Post> getRecentPosts() {
		return posts;
	}
	
	public void addPost(Post p) {
		posts.add(p);
	}
}
