package s4.spring.td2.controllers;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import s4.spring.td2.entities.Groupe;
import s4.spring.td2.entities.Organization;
import s4.spring.td2.repositories.GroupeRepository;
import s4.spring.td2.repositories.OrgasRepository;

@Controller
@RequestMapping("/orgas/")
public class OrgasController {
	
	@Autowired
	private OrgasRepository orgRep;
	
	
	@GetMapping({"","index"})
	public String index(Model model) {
		List<Organization> orgas = orgRep.findAll(Sort.by("id"));
		
		model.addAttribute("orgas",orgas);
		
		return "index";
	}
	
	
	//récupérer data du formulaire et l'insert dans un repository
	@PostMapping("createOrga")
	public RedirectView newOrga(Organization org  ) {
		orgRep.save(org);
		return new RedirectView("index");
	}
	
	@PostMapping("edit/createOrga")
	public RedirectView editOrga(Organization org  ) {
		orgRep.save(org);
		return new RedirectView("../index");
	}
	
	
	@GetMapping("edit/{id}")
	public String editOrga(@PathVariable int id,Model model) {
		Optional<Organization> opt=  orgRep.findById(id);
		if(opt.isPresent()) {
			model.addAttribute("orga", opt.get());
			return "edit";
		}
		
		return "orgas/404";
		
	}
	
	
	
	@GetMapping("createOrga")
	public String createOrga() {
		return "createOrga";
	}
	
	@RequestMapping("create/groupes/{id}")
	@ResponseBody
	public String createOrgaWithGroupes(@PathVariable int id){
		Optional<Organization> optOrg = orgRep.findById(id);
		if(optOrg.isPresent()) {
			Organization org=optOrg.get();
			Groupe grp= new Groupe();
			grp.setName("Sandwich Poulie");
			org.addGroupe(grp);
			org.getGroupes().add(grp);
			orgRep.save(org);
			return optOrg+" et "+grp+" ajoutés à la bdd.";
		}
		return "Organisation non trouvée";
	}
	
	@RequestMapping("groupes")
	@ResponseBody
	public String createGroupe(GroupeRepository groupeRep) {
		Groupe grp=new Groupe();
		groupeRep.save(grp);
		return grp+" ajouté à la bdd.";
	}
	
}
