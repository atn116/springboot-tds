package s4.spring.td5.controllers;

import java.util.List;

import s4.spring.td5.entities.Script;
import s4.spring.td5.repositories.ScriptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/rest/script")
public class ScriptRestController {
	
	@Autowired
	private ScriptRepository scriptRepo;	
	
	@ResponseBody
	@GetMapping("")
	public List<Script> get(){
		return scriptRepo.findAll();
	}
	
	@PostMapping("create")
	public Script post(@RequestBody Script script) {
		return scriptRepo.saveAndFlush(script);
	}
}