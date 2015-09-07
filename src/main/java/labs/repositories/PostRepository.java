package labs.repositories;

import java.util.List;
import java.util.Set;

import labs.models.Post;
import labs.models.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
	
    public List<Post> findByAuthorInOrderByCreatedAtDesc(Set<User> users, Pageable pageable);
    public int countByAuthorIn(Set<User> users);
}
