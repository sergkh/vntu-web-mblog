package labs;

import javax.validation.Valid;

import labs.models.Post;
import labs.models.RegistrationForm;
import labs.models.User;
import labs.services.PostsService;
import labs.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
		
		model.addAttribute("form", new RegistrationForm());
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
		model.addAttribute("currentUser", User.getCurrentUser());
		return "home";
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String createPost(@RequestParam("text") String postText) {
		postsService.addPost(postText);
		return "redirect:home";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@Valid RegistrationForm formData, BindingResult bindResult, Model model) {
		
		if(bindResult.hasErrors()) {
			model.addAttribute("errors", bindResult.getFieldErrors());
			model.addAttribute("form", formData);
			return "index";
		}
		
		usersService.register(formData.getLogin(), 
							  formData.getEmail(),
							  formData.getAge(),
							  formData.getCountry(),
						  	  formData.getPass());

		return "redirect:/home";
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
