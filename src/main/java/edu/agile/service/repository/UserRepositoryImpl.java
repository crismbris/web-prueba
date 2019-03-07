package edu.agile.service.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.agile.service.entities.Idea;
import edu.agile.service.entities.User;

public class UserRepositoryImpl implements UserRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(int id) {
		try {
	        return (User) this.entityManager.createQuery("select u from User u where id=" + id).getResultList().get(0);
		} catch (Exception e) {
			// No user found
			return null;
		}
	}

	@Override
	public User doLogin(String email, String password) {
		try {
	        return (User) this.entityManager.createQuery("select u from User u where email='" + email + "' and password='"+password+"'").getResultList().get(0);
		} catch (Exception e) {
			// No user found
			return null;
		}
	}

	@Override
	public User getUserByUsername(String username) {
		try {
	        return (User) this.entityManager.createQuery("select u from User u where username='" + username +"'").getResultList().get(0);
		} catch (Exception e) {
			// No user found
			return null;
		}
	}

	@Override
	public User getUserByEmail(String email) {
		try {
	        return (User) this.entityManager.createQuery("select u from User u where email='" + email +"'").getResultList().get(0);
		} catch (Exception e) {
			// No user found
			return null;
		}
	}

}
