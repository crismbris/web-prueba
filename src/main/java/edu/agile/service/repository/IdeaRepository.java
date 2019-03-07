package edu.agile.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import edu.agile.service.entities.Idea;

//@RepositoryRestResource(path = "/ideas")
@Repository
@RestResource(exported = false)
public interface IdeaRepository extends JpaRepository<Idea, Integer>, IdeaRepositoryCustom{
}
