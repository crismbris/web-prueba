package edu.agile.service.repository;

import java.util.List;

import edu.agile.service.entities.Category;
import edu.agile.service.entities.IdeaCategory;

public interface IdeaCategoryRepositoryCustom {
	List<IdeaCategory> getIdeaCategories();

	IdeaCategory getIdeaCategory(int id);
	
	Category getCategory(int id);
}
