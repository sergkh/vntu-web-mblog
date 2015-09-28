package labs;

import labs.models.Post;
import labs.models.User;
import labs.services.PostsService;
import labs.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/")
	public String index(Model model) {
		
		// якщо користувач вже увійшов, то перекинути його з реєстрації на домашню сторінку		
		if(!User.isAnonymous()) {
			return "redirect:/home";
		}
		
		return "index";
	}
	
	@RequestMapping("/home")
	public String home(Model model, 
					   @RequestParam(value = "page", defaultValue = "1") int page) {
		
		Page<Post> postsPage = postsService.getPosts(page, 5); // 5 постів на сторінку
		model.addAttribute("posts", postsPage.getContent());
		model.addAttribute("users", usersService.getSubscribeRecommendations());
		model.addAttribute("pagesCount", postsPage.getTotalPages());
		model.addAttribute("currentPage", page);
		return "home";
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String createPost(@RequestParam("text") String postText) {
		postsService.addPost(postText);
		return "redirect:home";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestParam("login") String login, 
						   @RequestParam("email") String email, 
						   @RequestParam("pass") String pass) {
		
		usersService.register(login, email, pass);

		return "redirect:home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value="/subscribe", method = RequestMethod.POST)
	public String subscribe(@RequestParam("login") String login) {
		usersService.subscribe(login);
		return "redirect:home";
	}
	
}
