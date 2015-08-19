package labs;

import java.util.Date;

import labs.models.Post;
import labs.services.PostsService;

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
	
	@RequestMapping("/home")
	public String index(Model model) {
		model.addAttribute("posts", postsService.getRecentPosts());
		return "index";
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String createPost(@RequestParam("text") String postText) {
		postsService.addPost(new Post("Unknown", postText, new Date()));
		return "redirect:home";
	}
}
