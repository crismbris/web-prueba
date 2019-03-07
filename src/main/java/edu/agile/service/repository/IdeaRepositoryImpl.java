package edu.agile.service.repository;
import edu.agile.service.entities.Idea;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class IdeaRepositoryImpl implements IdeaRepositoryCustom {
    
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Idea getIdeaInfo(String id) {
		try {
			Idea idea = (Idea) this.entityManager.createQuery("select i from Idea i where i.id=" + id).getResultList().get(0);
	        return idea;
		} catch (Exception e) {
			// No idea found
			return null;
		}
	}

	@Override
	public List<Idea> getIdeasInfo() {
//		return this.entityManager.createQuery("select i.id,i.title,i.description from Idea i").getResultList();
		return this.entityManager.createQuery("select i from Idea i").getResultList();
	}
	
	@Override
	public List<Idea> getIdeasInfo(String filterCategory) {
		return this.entityManager.createQuery("SELECT i FROM Idea i, IdeaCategory ic WHERE i.id=ic.ideaId and ic.categoryId="+ filterCategory).getResultList();
	}
}
