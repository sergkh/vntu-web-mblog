package labs.repositories;

import java.util.List;
import java.util.Set;

import labs.models.Post;
import labs.models.User;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
	
    public List<Post> findByAuthorInOrderByCreatedAtAsc(Set<User> users);
}
