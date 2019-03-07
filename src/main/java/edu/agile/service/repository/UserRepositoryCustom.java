package edu.agile.service.repository;

import java.util.List;
import edu.agile.service.entities.User;

public interface UserRepositoryCustom {
	List<User> getUsers();

	User getUser(int id);
    
    User doLogin(String user, String password);
    
//    User create();
    User getUserByUsername(String username);
    
    User getUserByEmail(String email);
}
