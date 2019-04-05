package s4.spring.td2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import s4.spring.td2.entities.User;
import s4.spring.td2.repositories.UserRepository;

@Controller
@RequestMapping("/groupe/")
public class UserController {

	@Autowired
	private UserRepository userRep;
	
	@RequestMapping("create")
	@ResponseBody
	public String createUser() {
		User user= new User();
		user.setMail("t24nk1ll3r@outlook.fr");
		user.setFirstName("Sandwich");
		user.setLastName("Poulie");
		userRep.save(user);
		
		return user +" ajouté dans la bdd.";
		
	}
	
	
}
