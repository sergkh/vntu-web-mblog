package labs.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
    @Size(min = 1, max = 512)
    @Column(unique = true)
	private String login;
	
	@NotBlank
    @Size(min = 1, max = 512)
    @Column(unique = true)
	private String email;
	
	@NotBlank
    @Size(min = 1, max = 100)
	private String passHash;
	
	@OneToMany(mappedBy = "author")
	private List<Post> posts = new ArrayList<>();
	
	@OneToMany
	private Set<User> subscriptions = new HashSet<>();
	
	public User() {
		super();
	}
	
	public User(String login, String email, String passHash) {
		this.login = login;
		this.email = email;
		this.passHash = passHash;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassHash() {
		return passHash;
	}
	
	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Set<User> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<User> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
