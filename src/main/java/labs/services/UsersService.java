package labs.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import labs.models.User;
import labs.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
	
	@Autowired
	private UserRepository usersRepo;
	
	@PostConstruct
	@Transactional
	public void createAdminUser() {
		register("admin", "admin@mail.com", "qwerty");
	}
	
	@Transactional
	public void register(String login, String email, String pass) {
		String passHash = new BCryptPasswordEncoder().encode(pass);
		
		User u = new User(login, email, passHash);

		// підпишемо користувача на самого себе
		u.getSubscriptions().add(u);

		usersRepo.save(u);
	}
	
	@Transactional
	public List<User> getSubscribeRecommendations() {
		User currentUser = usersRepo.findOne(1L);

		// перетворює список користувачів на список їх ідентифікаторів
		List<Long> ignoreIds = currentUser.getSubscriptions()
										  .stream()
										  .map(u -> u.getId())
										  .collect(Collectors.toList());

		return usersRepo.findFirst10ByIdNotIn(ignoreIds);
	}
	
	@Transactional
	public void subscribe(String login) {
		User u = usersRepo.findByLogin(login);

		// буде замінено після реалізації логіну
		User currentUser = usersRepo.findOne(1L);
		
		if(currentUser.getId() != u.getId()) {		
			currentUser.getSubscriptions().add(u);
		}
	}
	
}
