package edu.agile.service.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.agile.service.entities.Category;
import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaCategory;

public class IdeaCategoryRepositoryImpl implements IdeaCategoryRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<IdeaCategory> getIdeaCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdeaCategory getIdeaCategory(int id) {
		try {
			IdeaCategory ideaCategory = 
					(IdeaCategory) this.entityManager.createQuery("select i from IdeaCategory i where i.idIdea=" + id).getResultList().get(0);
	        return ideaCategory;
		} catch (Exception e) {
			// No idea found
			return null;
		}
	}
	
	@Override
	public Category getCategory(int id) {
		try {
			Category category = 
					(Category) this.entityManager.createQuery("select i from Category i where i.id=" + id).getResultList().get(0);
	        return category;
		} catch (Exception e) {
			// No idea found
			return null;
		}
	}
	
}
