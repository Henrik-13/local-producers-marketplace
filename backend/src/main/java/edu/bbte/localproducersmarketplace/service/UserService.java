package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.User;

import java.util.Collection;

public interface UserService {
    User findById(Long id);

    void deleteById(Long id);

    Collection<User> findAll();
}
