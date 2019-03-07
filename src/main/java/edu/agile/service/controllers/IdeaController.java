package edu.agile.service.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.agile.service.entities.Category;
import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaCategory;
import edu.agile.service.entities.IdeaVote;
import edu.agile.service.repository.IdeaCategoryRepository;
import edu.agile.service.repository.IdeaRepository;
import edu.agile.service.repository.IdeaVoteRepository;
import edu.agile.service.repository.UserRepository;
import edu.agile.service.responses.IdeaInfoResponse;
import edu.agile.service.responses.IdeaVoteResponse;
import edu.agile.service.responses.IdeasInfoResponse;



@RestController
public class IdeaController {

	@Autowired
	private IdeaRepository repository;

	@Autowired
	private IdeaVoteRepository ideaVoteRepository;
	
	@Autowired
	private IdeaCategoryRepository ideaCategoryRepository;

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping(path="/ideas/add", consumes = "application/json", produces="application/json")
	public String create(@RequestBody Idea idea) throws JsonProcessingException {
				
		if(idea.getCreated()==null) {
			idea.setCreated(new Date());
			idea.setIdeaVoteCollection(null);
			idea.setCreatedBy("demo");
			idea = repository.save(idea);
			
			List<IdeaCategory> categories = new ArrayList<IdeaCategory>();
			
			for(IdeaCategory ic : idea.getIdeaCategoryCollection()) {
				Category category = ideaCategoryRepository.getCategory(ic.getCategoryId().getId());
				IdeaCategory ideaCategory = new IdeaCategory();
				ideaCategory.setCreated(new Date());
				ideaCategory.setCreatedBy("demo");
				ideaCategory.setCategoryId(category);
				ideaCategory.setIdeaId(idea);
				ideaCategoryRepository.save(ideaCategory);
				
				categories.add(ideaCategory);
			}
			
			
		}
		Idea insertedIdea = repository.getIdeaInfo(""+idea.getId());
		
		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(Idea.class, new IdeaInfoResponse());
		mapper.registerModule(module);
		
		String serialized = mapper.writeValueAsString(insertedIdea);
		
		return serialized;
	}

	@GetMapping(path="/ideas", produces="application/json")
	public String getIdeasInfo() throws JsonProcessingException {

		List<Idea> result = repository.getIdeasInfo();
		
		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(List.class, new IdeasInfoResponse());
		mapper.registerModule(module);
		
		String serialized = mapper.writeValueAsString(result);
		return serialized;
		
	}

	@GetMapping(path="/ideas/category/{filterCategory}", produces="application/json")
	public String getIdeasInfo(@PathVariable String filterCategory) throws JsonProcessingException {

		List<Idea> result = repository.getIdeasInfo(filterCategory);
		
		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(List.class, new IdeasInfoResponse());
		mapper.registerModule(module);
		
		String serialized = mapper.writeValueAsString(result);
		return serialized;
		
	}

	@GetMapping(path="/ideas/{id}", produces="application/json")
	public String getIdeaInfo(@PathVariable String id) {
		Idea idea = repository.getIdeaInfo(id);

		ObjectMapper mapper = new ObjectMapper();
		 
		SimpleModule module = new SimpleModule();
		module.addSerializer(Idea.class, new IdeaInfoResponse());
		mapper.registerModule(module);
		
		String serialized = "{\"id\": 0}";
		try {
			serialized = mapper.writeValueAsString(idea);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return serialized;
	}

	@PostMapping(path="/ideas/{id}/vote/{val}", consumes = "application/json", produces="application/json")
	public String vote(@PathVariable String id, @PathVariable String val) throws JsonProcessingException {
		Idea idea = repository.getIdeaInfo(id);

		IdeaVote ideaVote = ideaVoteRepository.getIdeaVote(idea.getId(), 1);

		if(ideaVote==null) {
			idea.setIdeaVoteCollection(null);
			ideaVote = new IdeaVote();
			ideaVote.setUserId(userRepository.getUser(1));
			ideaVote.setVote(Integer.parseInt(val));			
			ideaVote.setIdeaId(idea);
			ideaVoteRepository.save(ideaVote);
			
			ObjectMapper mapper = new ObjectMapper();
			 
			SimpleModule module = new SimpleModule();
			module.addSerializer(IdeaVote.class, new IdeaVoteResponse());
			mapper.registerModule(module);
			
			String serialized = mapper.writeValueAsString(ideaVote);
			 			
			return serialized;
		}else {
			idea.setIdeaVoteCollection(null);
			ideaVote.setUserId(userRepository.getUser(1));
			ideaVote.setVote(Integer.parseInt(val));			
			ideaVote.setIdeaId(idea);
			ideaVoteRepository.save(ideaVote);
			
			ObjectMapper mapper = new ObjectMapper();
			 
			SimpleModule module = new SimpleModule();
			module.addSerializer(IdeaVote.class, new IdeaVoteResponse());
			mapper.registerModule(module);
			
			String serialized = mapper.writeValueAsString(ideaVote);
			 			
			return serialized;
		}
	}

}
