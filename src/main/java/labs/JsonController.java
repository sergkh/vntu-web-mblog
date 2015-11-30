package labs;

import labs.json.UserExistenceResponse;
import labs.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonController {
	
	@Autowired
	private UsersService usersService;
	
	@ResponseBody // вказує що замість шаблону клієнту необхідно повернути об’єкт результату в JSON форматі
	@RequestMapping(value="/exists", method = RequestMethod.GET)
	public UserExistenceResponse checkUserExists(@RequestParam("login") String login) {
		return new UserExistenceResponse(usersService.userExists(login));
	}
}
