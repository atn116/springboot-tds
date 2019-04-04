package s4.spring.td5.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import s4.spring.td5.entities.Category;
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
	/*
	 * @Autowired private HistoryRepository historyRepo;
	 */
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
		user.setEmail("test5@insert.net");
		user.setLogin("abc");
		user.setPassword("def");
		user.setIdentity("Test");
		userRepo.save(user);
		User root = new User();
		root.setLogin("root");
		root.setIdentity("Root");
		userRepo.save(root);
		Language lang = new Language();
		lang.setName("PHP");
		languageRepo.save(lang);

		Language lang2 = new Language();
		lang2.setName("Java");
		languageRepo.save(lang2);

		Category categ = new Category();
		categ.setName("Test_Script");
		categRepo.save(categ);

		Category categ2 = new Category();
		categ2.setName("testScript2");
		categRepo.save(categ2);
		return user.toString() + " , " + root.toString()+ lang.toString() + " , " + lang2.toString() + categ.toString() + " , " + categ2.toString()+ " ajoutés dans la bdd.";
	}
	
/*	@RequestMapping("/create/category")
	@ResponseBody
	public String addCategory() {
		if(activeUser!=null) {
			return "createCategory";
		}
		
		return "../../login";

	}*/


	@PostMapping("loginPost")
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
		return new RedirectView("../login");
	}

	@RequestMapping("/script/new")
	public String createScript(ModelMap model) {
		if (activeUser != null) {
			List<Language> langs = languageRepo.findAll();
			List<Category> categs = categRepo.findAll();
			model.addAttribute("langs", langs);
			model.addAttribute("categs", categs);
			return "createScript";
		}
		return "../login";
	}

	@RequestMapping("/script/{id}/delete")
	public RedirectView deleteScript(@PathVariable("id") int id, ModelMap model) {
		if (activeUser != null) {
			activeUser.getScripts().remove(scriptRepo.getOne(id));
			scriptRepo.deleteById(id);
			return new RedirectView("../../index");
		}

		return new RedirectView("../../login");
	}

	@PostMapping("/script/submit")
	public RedirectView addScript(Script s) {
		if (activeUser != null) {
			s.setUser(activeUser);
			// userRepo.save(activeUser);
			scriptRepo.saveAndFlush(s);
			activeUser.getScripts().add(s);

			return new RedirectView("../index");
		}
		return new RedirectView("../login");
	}

	@RequestMapping("/script/{id}")
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
					langs.remove(selectedLang);
					model.addAttribute("selectedLang", selectedLang);

					
					

					return "editScript";
				}
			}
			model.addAttribute("activeUser", activeUser);
			return "../index";
		}
		return "../login";

	}
}
