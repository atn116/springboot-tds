package s4.spring.td5.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import s4.spring.td5.entities.Category;
import s4.spring.td5.entities.History;
import s4.spring.td5.entities.Language;
import s4.spring.td5.entities.Script;
import s4.spring.td5.entities.User;
import s4.spring.td5.repositories.CategoryRepository;
import s4.spring.td5.repositories.HistoryRepository;
import s4.spring.td5.repositories.LanguageRepository;
import s4.spring.td5.repositories.ScriptRepository;
import s4.spring.td5.repositories.UserRepository;

@Controller
public class ScriptController {

	@Autowired
	private ScriptRepository scriptRepo;
	@Autowired
	private CategoryRepository categRepo;
	
	  @Autowired 
	  private HistoryRepository historyRepo;
	 
	@Autowired
	private LanguageRepository languageRepo;
	@Autowired
	private UserRepository userRepo;

	private User activeUser;

	@GetMapping({ "/login" })
	public String login(Model model) {
		return "login";
	}

	@RequestMapping({ "/logout" })
	public RedirectView logout() {
		activeUser = null;
		return new RedirectView("login");
	}

	@RequestMapping("/index")
	public String index(ModelMap model) {
		if (activeUser != null) {
			model.addAttribute("activeUser", activeUser);
			return "index";
		}
		return "login";
	}

	@RequestMapping("/create")
	@ResponseBody
	public String create() {
		User user = new User();
		user.setEmail("cedric.pierre-auguste@outlook.com");
		user.setLogin("cpa");
		user.setPassword("atn");
		user.setIdentity("Cédric Pierre-Auguste");
		userRepo.save(user);
		
		User user2 = new User();
		user2.setLogin("sovanL");
		user2.setIdentity("Sovanarit Long");
		user2.setPassword("1903");
		userRepo.save(user2);
		
		User root = new User();
		root.setLogin("root");
		root.setPassword("");
		root.setIdentity("Root");
		userRepo.save(root);
		
		Language lang = new Language();
		lang.setName("PHP");
		languageRepo.save(lang);

		Language lang2 = new Language();
		lang2.setName("Java");
		languageRepo.save(lang2);
		
		Language lang3 = new Language();
		lang3.setName("C#");
		languageRepo.save(lang3);

		Category categ = new Category();
		categ.setName("Script de tests unitaires");
		categRepo.save(categ);

		Category categ2 = new Category();
		categ2.setName("Script de base de données");
		categRepo.save(categ2);
		
		Category categ3 = new Category();
		categ3.setName("Script web");
		categRepo.save(categ3);
		
		return user.toString() + " , " + user2.toString()+" , "+root.toString()+" , "+ lang.toString() + " , " + lang2.toString() + " , " + lang3.toString()+" , " 
		 +categ.toString() + " , " + categ2.toString()+ " , " + categ3.toString()+ " ont été ajoutés dans la base de données.";
	}


	@PostMapping("/loginPost")
	public RedirectView loginPost(HttpServletRequest request) {
		List<User> users = userRepo.findAll();
		User conn = new User();
		conn.setLogin(request.getParameter("login"));
		conn.setPassword(request.getParameter("password"));

		for (User u : users) {
			if (u.getLogin().equals(conn.getLogin()) && u.getPassword().equals(conn.getPassword())) {
				activeUser = u;

				List<Script> allScripts = scriptRepo.findAll();
				List<Script> userScripts = new ArrayList<>();

				for (Script s : allScripts) {
					if (s.getUser().getId() == activeUser.getId())
						userScripts.add(s);
				}
				conn.setScripts(userScripts);

				return new RedirectView("index");
			}
		}
		return new RedirectView("login");
	}

	@RequestMapping("/script/new")
	public String createScript(ModelMap model) {
		if (activeUser != null) {
			List<Language> langs = languageRepo.findAll();
			model.addAttribute("langs", langs);
			List<Category> categs = categRepo.findAll();
			model.addAttribute("categs", categs);
			return "createScript";
		}
		return "../login";
	}

	@RequestMapping("/script/{id}/delete")
	public RedirectView deleteScript(@PathVariable("id") int id, ModelMap model) {
		if (activeUser != null) {
			scriptRepo.deleteById(id);
			activeUser.getScripts().remove(scriptRepo.getOne(id));
			return new RedirectView("../../index");
		}

		return new RedirectView("../../login");
	}

	@PostMapping("/script/submit")
	public RedirectView addScript(Script s) {
		if (activeUser != null) {
			s.setUser(activeUser);
			scriptRepo.saveAndFlush(s);
			activeUser.getScripts().add(s);

			return new RedirectView("../index");
		}
		return new RedirectView("../login");
	}
	
	@PostMapping("/script/{id}/submit")
	public RedirectView modifScript(@PathVariable("id") int id,Script s) {
		if (activeUser != null) {
			History h = new History();
			h.setDate(new Date());
			h.setComment("Modification du script "+id);
			h.setContent("Modification");
			historyRepo.saveAndFlush(h);
			scriptRepo.deleteById(id);
			activeUser.getScripts().remove(scriptRepo.getOne(id));
			s.setId(id);
			s.setUser(activeUser);
			scriptRepo.saveAndFlush(s);
			activeUser.getScripts().add(s);
			
			
			
		
			return new RedirectView("../../index");
		}
		return new RedirectView("../../login");
	}
	

	@RequestMapping("/script/{id}")
	@GetMapping
	public String scriptEdit(@PathVariable("id") int id, ModelMap model) {

		if (activeUser != null) {

			Optional<Script> s = scriptRepo.findById(id);

			if (s.isPresent()) {
				
				Script script = s.get();
				
				if (script.getUser().getId() == activeUser.getId()) {
					
					model.addAttribute("script", script);
					List<Category> categs = categRepo.findAll();
					model.addAttribute("categs", categs);
					List<Language> langs = languageRepo.findAll();
					model.addAttribute("langs", langs);
					Category selectedCategory = script.getCategory();
					categs.remove(selectedCategory);
					model.addAttribute("selectedCategory", selectedCategory);
					Language selectedLang = script.getLanguage();
					model.addAttribute("selectedLang", selectedLang);

					return "editScript";
				}
			}
			model.addAttribute("activeUser", activeUser);
			return "index";
		}else
		return "login";

	}
	@RequestMapping("search")
	public String search(ModelMap model) {
		
		if(activeUser != null) {
		
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			HttpEntity entity = new HttpEntity<>(header);
			/*ResponseEntity<List<Script>> response = restTemplate.exchange("http://localhost:8080/rest/"+user.getId(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<Script>>(){});
	 
			List<Script> scripts = response.getBody();*/
			
			model.addAttribute("scriptsTrouves", activeUser.getScripts());
			model.addAttribute("activeUser", activeUser);
				
			return "search";
		}
		else
			return "login";
	}
}