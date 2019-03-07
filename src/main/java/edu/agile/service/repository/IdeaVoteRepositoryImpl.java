package edu.agile.service.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaVote;

public class IdeaVoteRepositoryImpl implements IdeaVoteRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<IdeaVote> getIdeaVotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdeaVote getIdeaVote(int ideaId, int userId) {
		try {
	        return (IdeaVote) this.entityManager.createQuery("select v from IdeaVote v where user_id=" + userId + ""
	        		+ " and idea_id = "+ideaId).getResultList().get(0);
		} catch (Exception e) {
			// No IdeaVote found
			return null;
		}
	}

//	@Override
//	public IdeaVote getIdeaVote(String id) {
//		try {
//	        return (IdeaVote) this.entityManager.createQuery("select i from IdeaVote i where ideaId=" + id).getResultList().get(0);
//		} catch (Exception e) {
//			// No idea found
//			return null;
//		}
//	}
}
