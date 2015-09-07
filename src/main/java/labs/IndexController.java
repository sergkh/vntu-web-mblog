package labs;

import labs.services.PostsService;
import labs.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
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
		return "index";
	}
	
	@RequestMapping("/home")
	public String home(Model model, 
					   @RequestParam(value = "page", defaultValue = "1") int page) {
		model.addAttribute("posts", postsService.getPosts(page));
		model.addAttribute("users", usersService.getSubscribeRecommendations());
		model.addAttribute("pagesCount", postsService.pagesCount());
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
	
	@RequestMapping(value="/subscribe", method = RequestMethod.POST)
	public String subscribe(@RequestParam("login") String login) {
		usersService.subscribe(login);
		return "redirect:home";
	}
	
}
