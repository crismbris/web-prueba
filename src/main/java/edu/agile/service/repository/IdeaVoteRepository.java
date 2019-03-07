package edu.agile.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaVote;

@Repository
@RestResource(exported = false)
public interface IdeaVoteRepository extends JpaRepository<IdeaVote, Integer>, IdeaVoteRepositoryCustom{

}
