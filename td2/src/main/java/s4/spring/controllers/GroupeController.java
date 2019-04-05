package s4.spring.td2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import s4.spring.td2.entities.Groupe;

import s4.spring.td2.repositories.GroupeRepository;

@Controller
@RequestMapping("/groupe/")
public class GroupeController {

	@Autowired
	private GroupeRepository groupeRep;
	
	/*@RequestMapping("create")
	@ResponseBody
	public String createGroupe() {
		Groupe grp = new Groupe();
		grp.setAliases("Pouli");
		grp.setMail("t24nk1ll3r@outlook.fr");
		grp.setName("Sondouiche");
		groupeRep.save(grp);
		
		return grp+" ajouté dans la bdd.";
		
	}*/
	
	
}
