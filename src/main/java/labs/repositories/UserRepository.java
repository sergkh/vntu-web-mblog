package labs.repositories;

import java.util.List;

import labs.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByLogin(String login);
	
	List<User> findFirst10ByIdNotIn(List<Long> users);
}
