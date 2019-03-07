package edu.agile.service.repository;

import java.util.List;

import edu.agile.service.entities.Idea;


public interface IdeaRepositoryCustom {
	
    List<Idea> getIdeasInfo();
    List<Idea> getIdeasInfo(String filterCategory);

    Idea getIdeaInfo(String id);
}
