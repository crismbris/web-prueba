package edu.agile.service.repository;

import java.util.List;

import edu.agile.service.entities.IdeaVote;

public interface IdeaVoteRepositoryCustom {
	List<IdeaVote> getIdeaVotes();

    IdeaVote getIdeaVote(int ideaId, int userId);
}
