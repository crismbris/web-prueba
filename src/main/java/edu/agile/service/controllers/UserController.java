package edu.agile.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.agile.service.entities.User;
import edu.agile.service.repository.UserRepository;
import edu.agile.service.responses.UserCreateResponse;
import edu.agile.service.responses.UserResponse;

@RestController
public class UserController {

	@Autowired
	private UserRepository repository;
	
	@GetMapping(path="/user/login", consumes = "application/json", produces="application/json")
	public String doLogin(@RequestBody User user) throws JsonProcessingException {
		String email;
		String password;
		
		User loggedUser = null;
		String response = "{'message':'User or password does not match.'}";
		if(user!= null) {
			email = user.getEmail();
			password = user.getPassword();
			loggedUser = repository.doLogin(email, password);				
		}
		
		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new UserResponse());
		mapper.registerModule(module);
		
		response = mapper.writeValueAsString(loggedUser);
		
		return response;
		
	}
	
	@GetMapping(path="/user/login/{u}/{p}", produces="application/json")
	public String doLoginUrl(@PathVariable String u , @PathVariable String p) throws JsonProcessingException {
		String email;
		String password;
		
		User loggedUser = null;
		String response = "{'message':'User or password does not match.'}";
		if(u!= null && p!=null) {
			email = u;
			password = p;
			loggedUser = repository.doLogin(email, password);				
		}
		
		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new UserResponse());
		mapper.registerModule(module);
		
		response = mapper.writeValueAsString(loggedUser);
		
		return response;
		
	}
	
	@PostMapping(path="/user/register", consumes = "application/json", produces="application/json")
	public String create(@RequestBody User user) throws JsonProcessingException {
		String response = "";
		
		boolean alreadyExists = true;
				
		if(user.getUsername()!=null && user.getPassword()!=null) {
			User userRegistered = repository.getUserByEmail(user.getEmail());

			if(userRegistered==null) {
				alreadyExists = false;
				repository.save(user);
			}			
		}

		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new UserCreateResponse());
		mapper.registerModule(module);
		
		if(alreadyExists) {
			User aExists = new User();
			aExists.setId(0);
			response = mapper.writeValueAsString(aExists);
		}else {
			response = mapper.writeValueAsString(user);
		}
		
		return response;
	}

}
